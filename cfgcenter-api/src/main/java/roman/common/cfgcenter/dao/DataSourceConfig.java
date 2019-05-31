package roman.common.cfgcenter.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import roman.common.cfgcenter.KeyMatchPropertyListener;
import roman.common.cfgcenter.PropertyManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * DataSource配置
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = DataSourceConfig.PACKAGE)
public class DataSourceConfig {

	static final String PACKAGE = "roman.common.cfgcenter.dao.repository";

	private static final String PREFIX = "datasource.hikari.";

	private static final String[] KEYS = {"jdbcUrl", "username",
			"password", "autoCommit", "connectionTimeout", "idleTimeout", "maxLifetime",
			"connectionTestQuery", "minimumIdle", "maximumPoolSize",
			"poolName", "initializationFailFast", "isolationInternalQueries",
			"allowPoolSuspension", "readOnly", "registerMBeans", "catalog", "connectionInitSql",
			"driverClassName", "transactionIsolation", "validationTimeout", "leakDetectionThreshold"};

	/**
	 * 可动态修改DataSource，
	 * 配置中心修改dataSource属性后（比如最大连接数，超时时间）使用新配置datasource
	 * @return
	 */
	@Bean(name = "dataSource")
	public DataSource dataSource() {
		DataSource dataSource = createDataSource();
		DataSourceDelegate delegate = new DataSourceDelegate(dataSource);
		KeyMatchPropertyListener dataSourceConfigListener = new KeyMatchPropertyListener(key -> key.startsWith(PREFIX),
				event -> delegate.setDelagate(createDataSource()));
		PropertyManager.register(dataSourceConfigListener);
		return delegate;
	}

	private DataSource createDataSource() {
		Properties properties = new Properties();
		Map<String, String> propertyMap = PropertyManager.getPropertyMap();
		Arrays.stream(KEYS)
				.forEach(key ->  {
					String value = propertyMap.get(PREFIX + key);
					if(Objects.nonNull(value)){
						properties.put(key, value);
					}
				});
		return new HikariDataSource(new HikariConfig(properties));
	}


}
