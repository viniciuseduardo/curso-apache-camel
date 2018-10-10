package br.com.sysmi.treinamento;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class TreinamentoCamelConfiguration {

	@Autowired
	DispatcherServlet dispatcherServlet;
	
	@Primary
	@Bean(name = "treinamentoDataSource")
	@ConfigurationProperties(prefix = "datasource.treinamento")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Primary
	@Bean(name = "treinamentoJdbcTemplate")
	public JdbcTemplate primaryjdbcTemplate(@Qualifier("treinamentoDataSource") DataSource dsTreinamento) {
		return new JdbcTemplate(dsTreinamento);
	}	
}