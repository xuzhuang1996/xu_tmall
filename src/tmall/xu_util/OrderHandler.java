package tmall.xu_util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;


import tmall.bean.Order;
import tmall.bean.User;
import tmall.dao.UserDAO;



public class OrderHandler extends BeanHandler<Order>{
	public OrderHandler() {
		super(Order.class, new BasicRowProcessor(new BeanProcessor(mapColumnsToFields())));
		
	}

	@Override
	public Order handle(ResultSet rs) throws SQLException {
		Order bean= super.handle(rs);//拿到符合映射的东西
		Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
        Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
        Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
        Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
        User user = new UserDAO().get(rs.getInt("uid"));
        bean.setCreateDate(createDate);
        bean.setPayDate(payDate);
        bean.setDeliveryDate(deliveryDate);
        bean.setConfirmDate(confirmDate);
        bean.setUser(user);
		return bean;
	}
	//根据该函数中的来映射
	public static Map<String, String> mapColumnsToFields() {
	      Map<String, String> columnsToFieldsMap = new HashMap<>();
	      columnsToFieldsMap.put("orderCode", "orderCode");
	      columnsToFieldsMap.put("address", "address");    
	      columnsToFieldsMap.put("post", "post");    
	      columnsToFieldsMap.put("receiver", "receiver");    
	      columnsToFieldsMap.put("mobile", "mobile");    
	      columnsToFieldsMap.put("userMessage", "userMessage");    
	      columnsToFieldsMap.put("status", "status"); 
	      columnsToFieldsMap.put("id", "id");    
	      //columnsToFieldsMap.put("cid", "category");//如果是需要自定义处理的，就不能出现在映射中
	      return columnsToFieldsMap;
    }
	
}
