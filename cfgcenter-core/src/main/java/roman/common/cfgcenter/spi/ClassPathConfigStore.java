package roman.common.cfgcenter.spi;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import roman.common.cfgcenter.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class ClassPathConfigStore extends ExecuteBlockingConfigStore{

    private String path;

    public ClassPathConfigStore(Vertx vertx, String path) {
        super(vertx);
        this.path = path;
    }

    @Override
    Handler<Future<Buffer>> blockingCode() {
        return fut -> {
            try (InputStream is = ClassLoader.getSystemResourceAsStream(path)) {
                byte[] bytes = IOUtils.toByteArray(is);
                Buffer buffer = new BufferImpl();
                buffer.appendBytes(bytes);
                fut.complete(buffer);
            } catch (IOException e) {
                fut.fail(e);
            }
        };
    }
}
