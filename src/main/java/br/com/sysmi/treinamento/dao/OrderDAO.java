package br.com.sysmi.treinamento.dao;

import java.sql.Types;

import org.apache.camel.Body;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import br.com.sysmi.treinamento.beans.Order;
import br.com.sysmi.treinamento.beans.OrderItem;

@Component
public class OrderDAO {
	
	@Autowired
	@Qualifier("treinamentoJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	private static final String VALUES = "VALUES";
	private static final String INSERT_ORDER = "INSERT INTO `curso-camel`.`order`(`id`, `buyer_name`, `identity`) ";
	private static final String INSERT_ORDER_ITEM = "INSERT INTO `curso-camel`.`order_items`(`order_id`, `item_id`, `sigle`, `description`, `price`, `quantity`) ";
	
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
}

