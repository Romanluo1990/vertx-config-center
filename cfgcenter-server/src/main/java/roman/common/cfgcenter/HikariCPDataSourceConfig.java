package roman.common.cfgcenter;

import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

class HikariCPDataSourceConfig extends JsonObject {

    private static final String PREFIX = "vertx.datasource.hikari.";

    private static final String[] KEYS = {"dataSourceClassName", "jdbcUrl", "username",
            "password", "autoCommit", "connectionTimeout", "idleTimeout", "maxLifetime",
            "connectionTestQuery", "minimumIdle", "maximumPoolSize",
            "poolName", "initializationFailFast", "isolationInternalQueries",
            "allowPoolSuspension", "readOnly", "registerMBeans", "catalog", "connectionInitSql",
            "driverClassName", "transactionIsolation", "validationTimeout", "leakDetectionThreshold"};

    HikariCPDataSourceConfig() {
        Map<String, String> propertyMap = PropertyManager.getPropertyMap();
        Arrays.stream(KEYS)
                .filter(key -> Objects.nonNull(propertyMap.get(PREFIX + key)))
                .forEach(key ->  putValueFun.apply(propertyMap).accept(key));
    }

    private Function<Map<String, String>, Consumer<String>> putValueFun = propertyMap -> key -> {
        String value = propertyMap.get(PREFIX + key);
        switch (key) {
            case "dataSourceClassName":
                put(key, value);
                break;
            case "jdbcUrl":
                put(key, value);
                break;
            case "username":
                put(key, value);
                break;
            case "password":
                put(key, value);
                break;
            case "autoCommit":
                put(key, Boolean.valueOf(value));
                break;
            case "connectionTimeout":
                put(key, Long.valueOf(value));
                break;
            case "idleTimeout":
                put(key, Long.valueOf(value));
                break;
            case "maxLifetime":
                put(key, Long.valueOf(value));
                break;
            case "connectionTestQuery":
                put(key, value);
                break;
            case "minimumIdle":
                put(key, Integer.valueOf(value));
                break;
            case "maximumPoolSize":
                put(key, Integer.valueOf(value));
                break;
            case "poolName":
                put(key, value);
                break;
            case "initializationFailFast":
                put(key, Boolean.valueOf(value));
                break;
            case "isolationInternalQueries":
                put(key, Boolean.valueOf(value));
                break;
            case "allowPoolSuspension":
                put(key, Boolean.valueOf(value));
                break;
            case "readOnly":
                put(key, Boolean.valueOf(value));
                break;
            case "registerMBeans":
                put(key, Boolean.valueOf(value));
                break;
            case "catalog":
                put(key, value);
                break;
            case "connectionInitSql":
                put(key, value);
                break;
            case "driverClassName":
                put(key, value);
                break;
            case "transactionIsolation":
                put(key, value);
                break;
            case "validationTimeout":
                put(key, Long.valueOf(value));
                break;
            case "leakDetectionThreshold":
                put(key, Long.valueOf(value));
                break;
            default:
                break;
        }
    };
}
