package tmall.xu_util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;

import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.User;
import tmall.dao.OrderDAO;
import tmall.dao.ProductDAO;
import tmall.dao.UserDAO;

public class OrderItemHandler extends BeanHandler<OrderItem> {
	public OrderItemHandler() {
		super(OrderItem.class, new BasicRowProcessor(new BeanProcessor(mapColumnsToFields())));
		
	}

	@Override
	public OrderItem handle(ResultSet rs) throws SQLException {
		OrderItem bean= super.handle(rs);//拿到符合映射的东西
		Product product = new ProductDAO().get(rs.getInt("pid"));
		User user = new UserDAO().get(rs.getInt("uid"));
		int oid = rs.getInt("oid");
		if (-1 != oid) {
            Order order = new OrderDAO().get(oid);
            bean.setOrder(order);
        }
		bean.setProduct(product);
        bean.setUser(user);
		return bean;
	}
	//根据该函数中的来映射
	public static Map<String, String> mapColumnsToFields() {
	      Map<String, String> columnsToFieldsMap = new HashMap<>();
	      columnsToFieldsMap.put("id", "id");
	      columnsToFieldsMap.put("number", "number");
	      return columnsToFieldsMap;
    }
}
