package br.com.sysmi.treinamento.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component		
public class OrderJMSRoute extends RouteBuilder {
	public static final String ID = "queue-reader";
	public static final String URI_QUEUE = "jms:queue:curso-apache-camel?testConnectionOnStartup=true&listenerConnectionFactory=#jmsConnectionFactoryOrder";
	
	@Override
	public void configure() throws Exception {
        from(URI_QUEUE)
			.routeId(ID)
			.routeDescription("Listen order queue for processing")
			.log("Order Details: \n ${body}")
			.to(OrderDatabaseRoute.URI_INSERT_ORDER)
        .end();
	}
}