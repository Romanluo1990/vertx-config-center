package roman.common.cfgcenter.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * DataSource配置
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = DataSourceConfig.PACKAGE)
public class DataSourceConfig {

	static final String PACKAGE = "roman.common.cfgcenter.dao.repository";

	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "datasource.hikari")
	public DataSource druid() {
		return new HikariDataSource();
	}

}
