package br.com.sysmi.treinamento;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.http.common.CamelServlet;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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
	
	@Bean
	ServletRegistrationBean servletRegistrationBean() {
		final ServletRegistrationBean servlet = new ServletRegistrationBean(new CamelHttpTransportServlet(), "/treinamento-camel/*");
		servlet.setName(CamelServlet.class.getSimpleName());
		return servlet;
	}
	
	@Bean(name = "jmsConnectionFactoryOrder")
	public ConnectionFactory jmsConnectionFactory() {
		final PooledConnectionFactory pool = new PooledConnectionFactory();
		pool.setConnectionFactory(new ActiveMQConnectionFactory());
		return pool;
	}	
}
