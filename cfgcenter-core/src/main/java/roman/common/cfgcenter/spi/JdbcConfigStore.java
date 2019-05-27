package roman.common.cfgcenter.spi;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;

import java.util.List;

public class JdbcConfigStore implements ConfigStore {

    private JDBCClient jdbcClient;

    private String sql;

    public JdbcConfigStore(JDBCClient jdbcClient, String sql) {
        this.jdbcClient = jdbcClient;
        this.sql = sql;
    }

    @Override
    public void get(Handler<AsyncResult<Buffer>> completionHandler) {
        Handler<AsyncResult<ResultSet>> arHandler = ar -> {
            if(ar.succeeded()){
                List<JsonObject> rows = ar.result().getRows();
                completionHandler.handle(Future.succeededFuture(new JsonArray(rows).toBuffer()));
            }else{
                completionHandler.handle(Future.failedFuture(ar.cause()));
            }
        };
        jdbcClient.query(sql, arHandler);
    }
}
