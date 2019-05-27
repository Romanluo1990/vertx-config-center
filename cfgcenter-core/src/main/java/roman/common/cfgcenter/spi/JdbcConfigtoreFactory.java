package roman.common.cfgcenter.spi;

import io.vertx.config.spi.ConfigStore;
import io.vertx.config.spi.ConfigStoreFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

public class JdbcConfigtoreFactory implements ConfigStoreFactory {

    @Override
    public String name() {
        return "jdbc";
    }

    @Override
    public ConfigStore create(Vertx vertx, JsonObject configuration) {
        JDBCClient jdbcClient = JDBCClient.createNonShared(vertx, configuration);
        String sql = configuration.getString("sql");
        if (sql == null) {
            throw new IllegalArgumentException("The `sql` configuration is required.");
        }
        return new JdbcConfigStore(jdbcClient, sql);
    }
}
