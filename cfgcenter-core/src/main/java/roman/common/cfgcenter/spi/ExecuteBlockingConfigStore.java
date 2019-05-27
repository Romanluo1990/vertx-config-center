package roman.common.cfgcenter.spi;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

public abstract class ExecuteBlockingConfigStore implements ConfigStore {

    private Vertx vertx;

    public ExecuteBlockingConfigStore(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void get(Handler<AsyncResult<Buffer>> completionHandler) {
        vertx.executeBlocking(blockingCode(), completionHandler);
    }

    abstract Handler<Future<Buffer>> blockingCode();
}
