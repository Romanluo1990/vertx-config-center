package roman.common.cfgcenter.spi;

import io.vertx.config.impl.spi.FileConfigtoreFactory;
import io.vertx.config.impl.spi.HttpConfigStoreFactory;
import io.vertx.config.spi.ConfigStore;
import io.vertx.config.spi.ConfigStoreFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.net.URI;
import java.util.Objects;

public class URIConfigtoreFactory implements ConfigStoreFactory {

    @Override
    public String name() {
        return "uri";
    }

    @Override
    public ConfigStore create(Vertx vertx, JsonObject configuration) {
        String uriPath = configuration.getString("uri");
        Objects.requireNonNull(uriPath);
        URI uri = URI.create(uriPath);
        String scheme = uri.getScheme();
        String path = uri.getSchemeSpecificPart();
        configuration.put("path", path);
        ConfigStore configStore;
        switch (scheme){
            case "classpath":
                configStore = new ClassPathConfigtoreFactory().create(vertx, configuration);
                break;
            case "file":
                configStore = new FileConfigtoreFactory().create(vertx, configuration);
                break;
            case "http":
                configStore = new HttpConfigStoreFactory().create(vertx, configuration);
                break;
            default:
                throw new IllegalArgumentException("only support: classpath, file, http");
        }
        return configStore;
    }
}
