package br.com.sysmi.treinamento.rest;

import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import br.com.sysmi.treinamento.beans.Order;
import br.com.sysmi.treinamento.routes.OrderDatabaseRoute;

@Component
public class OrderRestRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration()
        .component("servlet")
        .contextPath("/treinamento-camel")
    	.apiContextPath("/api-doc")
            .apiProperty("api.title", "Treinamento Camel - REST API")
            .apiProperty("api.version", "1.0")
            .apiProperty("cors", "true")
            .apiContextRouteId("doc-api")
        .bindingMode(RestBindingMode.json);
        	
        rest("/orders")
            .get("{id}").description("Get Order")
                .produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .outType(Order.class)
    			.param().description("ORDER ID.")
	    			.name("id")
	    			.type(RestParamType.path)
	    			.dataType("integer")
	    		.endParam()
				.route().routeId("get-order")
		            .log("Searching order ID = ${headers.id}...")
		            .to(OrderDatabaseRoute.URI_SHOW_ORDER)
		            .choice()
		            	.when(body().isNull())
		            		.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpServletResponse.SC_NOT_FOUND))
						.otherwise()
							.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpServletResponse.SC_OK))
					.endChoice()
				.end();
    }
}
