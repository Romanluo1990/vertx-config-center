package roman.common.cfgcenter;

import com.google.common.eventbus.EventBus;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import roman.common.cfgcenter.util.EnvUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class PropertyClient {

    private Map<String, String> propertyMap;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private Vertx vertx;

    private EventBus eventBus = new EventBus("propertyClient");

    public PropertyClient() {
        vertx = VertxFactory.getVertx();
        ConfigRetrieverOptions options = getConfigRetrieverOptions();
        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
        initProperty(retriever);
    }

    public Map<String, String> getPropertyMap() {
        if(propertyMap == null){
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return propertyMap;
    }

    public void register(PropertyListener propertyListener) {
        eventBus.register(propertyListener);
    }

    public void unregister(PropertyListener propertyListener) {
        eventBus.unregister(propertyListener);
    }

    private ConfigRetrieverOptions getConfigRetrieverOptions() {
        ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions();
        configRetrieverOptions.setScanPeriod(-1);
        addLocalStore(configRetrieverOptions);
        addRemoteStore(configRetrieverOptions);
        return configRetrieverOptions;
    }

    private void addRemoteStore(ConfigRetrieverOptions configRetrieverOptions) {
        String domain = EnvUtils.getDomain();
        if(domain != null){
            ConfigStoreOptions ebGetStore = new ConfigStoreOptions()
                    .setType("event-bus-pull")
                    .setFormat("json")
                    .setOptional(true)
                    .setConfig(new JsonObject()
                            .put("address", EnvUtils.getEventBusPullAddress())
                            .put("domain", domain)
                    );
            configRetrieverOptions.addStore(ebGetStore);
        }
    }

    private void addLocalStore(ConfigRetrieverOptions configRetrieverOptions) {
        String uri = EnvUtils.getPropertyURI();
        ConfigStoreOptions fileStore = new ConfigStoreOptions()
                .setType("uri")
                .setOptional(true)
                .setFormat("encode-properties")
                .setConfig(new JsonObject()
                        .put("charset", EnvUtils.getPropertyCharset())
                        .put("uri", uri)
                        .put("raw-data", true)
                );
        configRetrieverOptions.addStore(fileStore);
        ConfigStoreOptions sysStore = new ConfigStoreOptions()
                .setType("sys");
        configRetrieverOptions.addStore(sysStore);
        ConfigStoreOptions envStore = new ConfigStoreOptions()
                .setType("env");
        configRetrieverOptions.addStore(envStore);
    }

    private void initProperty(ConfigRetriever retriever) {
        retriever.getConfig(ar -> {
            try{
                if(ar.succeeded()){
                    JsonObject conf = ar.result();
                    propertyMap = new ConcurrentHashMap<>();
                    updateProperty(conf);
                    listenEventBusPush();
                }else{
                    log.warn("PropertyManager init error", ar.cause());
                }
            }finally {
                countDownLatch.countDown();
            }
        });
    }

    private void listenEventBusPush() {
        String domain = EnvUtils.getDomain();
        if(domain != null){
            vertx.eventBus().<JsonObject>consumer(domain + "-config-push", msg -> {
                JsonObject confg = msg.body();
                updateProperty(confg);
            });
        }
    }

    private void updateProperty(JsonObject jsonObject) {
        Map<String, String> properties = getPropertyMap();
        jsonObject.getMap().forEach((key, value) -> {
            String oldValue = properties.get(key);
            properties.put(key, Objects.toString(value));
            String newVlaue = properties.get(key);
            eventBus.post(new PropertyModifiedEvent(key, oldValue, newVlaue));
        });
    }
}
