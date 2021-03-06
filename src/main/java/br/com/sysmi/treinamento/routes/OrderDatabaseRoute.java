package br.com.sysmi.treinamento.routes;

import java.sql.SQLRecoverableException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import br.com.sysmi.treinamento.beans.OrderItem;
import br.com.sysmi.treinamento.beans.Order;

@Component
public class OrderDatabaseRoute extends RouteBuilder {
	public static final String ID_INSERT_ORDER = "sql-insert-order";
	public static final String URI_INSERT_ORDER = "seda:" + ID_INSERT_ORDER;
	public static final String ID_SHOW_ORDER = "sql-show-order";
	public static final String URI_SHOW_ORDER = "seda:" + ID_SHOW_ORDER;	
	

	@Override
	public void configure() throws Exception {

		from(URI_INSERT_ORDER)
			.routeId(ID_INSERT_ORDER)
			.routeDescription("Rota de Processamento da Compra -  Inserção de Pedido")
			.log("Recebendo pedido de salvamento")
			.unmarshal().json(JsonLibrary.Jackson, Order.class)
		    .filter().method(Order.class, "checkLimitItems(${body.items.size})")
		    	.log("Pedido excede o limite de itens.")
		    	.stop()
		    .end()			
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					Order order = exchange.getIn().getBody(Order.class);	
					
					List<OrderItem> cleanItens = order.getItems().stream().filter( item -> item.hasStock()).collect(Collectors.toList());
					Logger.getLogger(getClass()).info(cleanItens);
					order.setItems(cleanItens);
					exchange.getIn().setBody(order);
				}
			})
			.log("Pedido tratado: \n ${body}")
			.doTry()
				.to("bean:orderDAO?method=insertOrder")
			.doCatch(DuplicateKeyException.class)
				.log("Pedido já cadastrado.")
				.stop()
			.end()
			.log("Pedido cadastrado com sucesso.")
		.end();
		
		from(URI_SHOW_ORDER)
			.routeId(ID_SHOW_ORDER)
			.routeDescription("Rota de Consulta de Compra")
			.doTry()
				.to("bean:orderDAO?method=showOrder(${headers.id})")
				.log("Retrieved successfully.")
			.doCatch(EmptyResultDataAccessException.class)
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpServletResponse.SC_NOT_FOUND))	
		.end();
	}

}
