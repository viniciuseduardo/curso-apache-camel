package br.com.sysmi.treinamento.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sysmi.treinamento.beans.Order;
import br.com.sysmi.treinamento.beans.OrderItem;

@Component
public class OrderDAO {
	
	@Autowired
	@Qualifier("treinamentoJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	private static final String VALUES = "VALUES";
	private static final String INSERT_ORDER = "INSERT INTO `curso-camel`.`order`(`idCompra`, `nomeComprador`, `identidade`) ";
	private static final String INSERT_ORDER_ITEM = "INSERT INTO `curso-camel`.`order_items`(`order_id`, `itemId`, `sigla`, `descricao`, `preco`, `quantidade`) ";
	private static final String SELECT_ORDER = "SELECT * FROM `order` WHERE `idCompra` = ?";
	private static final String SELECT_ORDER_ITEM = "SELECT * FROM `order_items` WHERE `order_id` = ?";
	
	public OrderDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
    }

	public void insertOrder(@Body() Order order) {
				jdbcTemplate.update(INSERT_ORDER + VALUES + " (?,?,?)", 
				new Object[]{
						order.getIdCompra(), order.getNomeComprador(), order.getIdentidade()
				},
				new int[]{
						Types.INTEGER,Types.VARCHAR,Types.VARCHAR
				});
				
				for (OrderItem item : order.getItems()) {
					insertOrderItem(item, order.getIdCompra());
				}
	}
	
	public void insertOrderItem(OrderItem orderItem, Integer orderId) {
		orderItem.setOrderId(orderId);
		jdbcTemplate.update(INSERT_ORDER_ITEM + VALUES + "(?,?,?,?,?,?)",
		new Object[]{
				orderItem.getOrderId(), orderItem.getItemId(), orderItem.getSigla(),
				orderItem.getDescricao(), orderItem.getPreco(), orderItem.getQuantidade()
		},
		new int[]{
				Types.INTEGER,Types.INTEGER,Types.VARCHAR,
				Types.VARCHAR,Types.DECIMAL,Types.INTEGER
		});
	}
	
	public Order showOrder(Integer orderId){
		ObjectMapper mapper = new ObjectMapper();
		
		Order order = jdbcTemplate.queryForObject(
				SELECT_ORDER, new Object[] { orderId }, 
				new BeanPropertyRowMapper<Order>(Order.class));
		
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		List<Map<String, Object>> rsOrderItems = jdbcTemplate.queryForList(SELECT_ORDER_ITEM, orderId);
		for (Map<String, Object> item : rsOrderItems) {
			OrderItem orderItem = mapper.convertValue(item, OrderItem.class);
			Logger.getLogger(getClass()).info(orderItem);
			orderItems.add(orderItem);
		}
		
		order.setItems(orderItems);		
		Logger.getLogger(getClass()).info(order);

		return order;
	}	
}

