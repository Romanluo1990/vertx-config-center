package roman.common.cfgcenter;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import lombok.extern.slf4j.Slf4j;
import roman.common.cfgcenter.util.EnvUtils;

import java.util.concurrent.CountDownLatch;

@Slf4j
class VertxFactory {

    private static final String VERTX_ROOT = "io.vertx.cfgcenter";

    private static volatile Vertx vertx;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private VertxFactory() {
    }

    static synchronized Vertx getVertx() {
        if(vertx == null){
            init();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return vertx;
    }

    private static void init() {
        String zoo = EnvUtils.getZoo();
        if(zoo != null){
            JsonObject zkConfig = new JsonObject();
            zkConfig.put("zookeeperHosts", zoo);
            zkConfig.put("rootPath", VERTX_ROOT);
            zkConfig.put("retry", new JsonObject()
                    .put("initialSleepTime", 3000)
                    .put("maxTimes", 3));
            ClusterManager mgr = new ZookeeperClusterManager(zkConfig);
            VertxOptions options = new VertxOptions().setClusterManager(mgr);
            Vertx.clusteredVertx(options, res -> {
                try{
                    if (res.succeeded()) {
                        vertx = res.result();
                    } else {
                        log.warn("Vertx init error", res.cause());
                    }
                }finally {
                    countDownLatch.countDown();
                }
            });
        }else{
            try{
                vertx = Vertx.vertx();
            }finally {
                countDownLatch.countDown();
            }
        }
    }

}
