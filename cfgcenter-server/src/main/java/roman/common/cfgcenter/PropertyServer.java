package roman.common.cfgcenter;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import roman.common.cfgcenter.spi.EventBusPullConfigtoreFactory;
import roman.common.cfgcenter.util.EnvUtils;

import java.util.*;
import java.util.function.Consumer;

@Slf4j
public class PropertyServer {

    private static final String FORMAT = "raw";

    private static final String RAW_KEY = "configs";

    private static final String RAW_TYPE = "json-array";

    private Vertx vertx;

    private ConfigRetriever retriever;

    public PropertyServer(ConfigStoreOptions...  configStoreOptions) {
        vertx = VertxFactory.getVertx();
        ConfigRetrieverOptions options = getConfigRetrieverOptions(configStoreOptions);
        retriever = ConfigRetriever.create(vertx, options);
        initPullReply();
    }

    public void publishProperty(){
        Handler<Map<String, JsonObject>> handler = domainProperties -> {
            domainProperties.forEach(this::eventBusPush);
        };
        internalPublishProperty(handler);
    }

    public void publishProperty(String domain){
        Handler<Map<String, JsonObject>> handler = domainProperties -> {
            JsonObject jsonObject = domainProperties.get(domain);
            if(Objects.nonNull(jsonObject))
                eventBusPush(domain, jsonObject);
        };
        internalPublishProperty(handler);
    }

    public void publishProperty(String domain, String key){
        Handler<Map<String, JsonObject>> handler = domainProperties -> {
            String value = domainProperties.get(domain).getString(key);
            if(Objects.nonNull(value))
                eventBusPush(domain, new JsonObject().put(key, value));
        };
        internalPublishProperty(handler);
    }

    private void internalPublishProperty(Handler<Map<String, JsonObject>> handler){
        retriever.getConfig(ar -> {
            if(ar.succeeded()){
                JsonObject conf = ar.result();
                JsonArray resultSet = conf.getJsonArray(RAW_KEY);
                Map<String, JsonObject> domainProperties = new HashMap<>();
                Consumer<JsonObject> rowConsumer = row -> {
                    String domain = row.getString("domain");
                    String key = row.getString("key");
                    String value = row.getString("value");
                    domainProperties.compute(domain, (domainKey, confJsonObject) -> {
                        if(confJsonObject == null)
                            confJsonObject = new JsonObject();
                        return confJsonObject;
                    })
                    .put(key, value);
                };
                resultSet.stream()
                        .map(row -> (JsonObject)row)
                        .forEach(rowConsumer);
                handler.handle(domainProperties);
            }
        });
    }

    private ConfigRetrieverOptions getConfigRetrieverOptions(ConfigStoreOptions[] configStoreOptions) {
        ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions();
        Arrays.stream(configStoreOptions)
                .peek(options -> options.setFormat(FORMAT))
                .peek(options -> options.getConfig().put("raw.key", RAW_KEY).put("raw.type", RAW_TYPE))
                .forEach(configRetrieverOptions::addStore);
        configRetrieverOptions.setScanPeriod(-1);
        return configRetrieverOptions;
    }

    private void initPullReply() {
        String address = EnvUtils.getEventBusPullAddress();
        if(Objects.isNull(address)){
            address = EventBusPullConfigtoreFactory.NAME;
        }
        vertx.eventBus().<String>consumer(address, msg -> {
            String domain = msg.body();
            Handler<Map<String, JsonObject>> handler = domainPropertyMap -> {
                JsonObject jsonObject = domainPropertyMap.get(domain);
                if(Objects.nonNull(jsonObject))
                    msg.reply(jsonObject);
                else
                    msg.fail(500, "无配置项");
            };
            internalPublishProperty(handler);
        });
    }

    /**
     * 发布配置到客户端
     * @param domain
     * @param confg
     */
    private void eventBusPush(String domain, JsonObject confg) {
        vertx.eventBus().publish(domain + "-config-push", confg);
    }


}
