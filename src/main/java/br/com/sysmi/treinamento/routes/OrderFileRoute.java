package br.com.sysmi.treinamento.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class OrderFileRoute extends RouteBuilder {
	public static final String ID = "file-reader";
	public static final String URI = "direct:" + ID;
	
	@Override
	public void configure() throws Exception {		
		
		from(URI)
			.routeId(ID)
			.routeDescription("Read Order Files")	
			.from("file:./files?fileName=Order.json&delete=true")
			.log("Reading order file...")
			.log("Order Details: \n ${body}")
			.to(OrderDatabaseRoute.URI_INSERT_ORDER)
		.end();
	}

}
