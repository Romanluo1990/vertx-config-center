package roman.common.cfgcenter;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import roman.common.cfgcenter.spi.EventBusPullConfigtoreFactory;
import roman.common.cfgcenter.util.EnvUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class PropertyServer {

    private static final String JDBC_STORE_PROVIDERCLASS = "jdbc.store.providerClass";

    private static final String JDBC_STORE_SQL = "jdbc.store.sql";

    private Map<String, Map<String, String>> domainPropertyMap = new ConcurrentHashMap<>();

    private Vertx vertx;

    public PropertyServer() {
        vertx = VertxFactory.getVertx();
        ConfigRetrieverOptions options = getConfigRetrieverOptions();
        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
        initProperty(retriever);
        initPullReply();
        initSync();
    }

    public void updateProperty(String domain, String key, String value){
        Map<String, String> properties = new HashMap<>();
        properties.put(key, value);
        updateProperty(domain, properties);
    }

    public void updateProperty(String domain, Map<String, String> properties){
        internalUpdateProperty(domain, properties);
        eventBusPush(domain, properties);
    }

    public void publishProperty(){
        domainPropertyMap.forEach(this::eventBusPush);
    }

    public void publishProperty(String domain){
        eventBusPush(domain, domainPropertyMap.get(domain));
    }

    private void internalUpdateProperty(String domain, Map<String, String> properties){
        domainPropertyMap.compute(domain, (key, value) -> {
            if(value == null){
                value = new ConcurrentHashMap<>();
            }
            value.putAll(properties);
            return value;
        });
    }

    private void internalUpdateProperty(String domain, JsonObject jsonObject) {
        Map<String, String> properties = new HashMap<>();
        jsonObject.getMap().forEach((key, value) -> properties.put(key, Objects.toString(value)));
        internalUpdateProperty(domain, properties);
    }

    private void initProperty(ConfigRetriever retriever) {
        retriever.getConfig(ar -> {
            if(ar.succeeded()){
                JsonObject conf = ar.result();
                JsonArray resultSet = conf.getJsonArray("ResultSet");
                Map<String, List<JsonObject>> domains = resultSet.stream()
                        .map(row -> (JsonObject)row)
                        .collect(Collectors.groupingBy(row -> row.getString("domain")));
                domains.forEach((domain, confs) -> {
                    Map<String, String> propertyMap = new HashMap<>();
                    confs.forEach(property ->  propertyMap.put(property.getString("key"), property.getString("value")));
                    updateProperty(domain, propertyMap);
                });
            }
        });
    }

    private ConfigRetrieverOptions getConfigRetrieverOptions() {
        ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions();
        ConfigStoreOptions jdbcStore = new ConfigStoreOptions()
                .setType("jdbc")
                .setFormat("raw")
                .setConfig(new JsonObject()
                        .put("provider_class", PropertyManager.getString(JDBC_STORE_PROVIDERCLASS))
                        .put("sql", PropertyManager.getString(JDBC_STORE_SQL))
                        .put("raw.key", "ResultSet")
                        .put("raw.type", "json-array")
                        .mergeIn(new HikariCPDataSourceConfig()));
        configRetrieverOptions.addStore(jdbcStore);
        return configRetrieverOptions;
    }

    private void initPullReply() {
        String address = EnvUtils.getEventBusPullAddress();
        if(Objects.isNull(address)){
            address = EventBusPullConfigtoreFactory.NAME;
        }
        vertx.eventBus().<String>consumer(address, msg -> {
            String domain = msg.body();
            Map<String, String> properties = domainPropertyMap.get(domain);
            JsonObject confg = new JsonObject();
            if(Objects.nonNull(properties)){
                properties.forEach(confg::put);
            }
            msg.reply(confg);
        });
    }

    /**
     * 发布配置到客户端
     * @param domain
     * @param properties
     */
    private void eventBusPush(String domain, Map<String, String> properties) {
        JsonObject confg = new JsonObject();
        properties.forEach(confg::put);
        vertx.eventBus().publish(EnvUtils.getDomain() + "-config-sync",
                new JsonObject()
                        .put("domain", domain)
                        .put("confg", confg));
        vertx.eventBus().publish(domain + "-config-push", confg);
    }

    /**
     * 服务端更新同步
     */
    private void initSync() {
        vertx.eventBus().<JsonObject>consumer(EnvUtils.getDomain() + "-config-sync", msg -> {
            msg.address();
            JsonObject msgBody = msg.body();
            String domain = msgBody.getString("domain");
            JsonObject confg = msgBody.getJsonObject("confg");
            internalUpdateProperty(domain, confg);
        });
    }
}
