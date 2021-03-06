package tmall.xu_util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import tmall.bean.Product;
import tmall.bean.Review;
import tmall.bean.User;
import tmall.dao.ProductDAO;
import tmall.dao.UserDAO;

public class ReviewListHandler extends BeanListHandler<Review>{
	public ReviewListHandler() {
		super(Review.class, new BasicRowProcessor(new BeanProcessor(mapColumnsToFields())));
	}
	
	@Override
	public List<Review> handle(ResultSet rs) throws SQLException {
		List<Review> p= super.handle(rs);//拿到符合映射的东西，已经遍历过一次了。因此下面再次使用需要重新定向
		rs.first();//重新定向rs的游标。再次读取使用
		for(int index = 0 ;index<p.size();index++) {
			Product product = new ProductDAO().get(rs.getInt("pid"));
	        User user = new UserDAO().get(rs.getInt("uid"));
	        Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
			p.get(index).setProduct(product);//设置不符合直接映射的东西
			p.get(index).setUser(user);
			p.get(index).setCreateDate(createDate);
			rs.next();//游标下移
		}
		return p;
	}
	//根据该函数中的来映射，都一样
	public static Map<String, String> mapColumnsToFields() {
	      Map<String, String> columnsToFieldsMap = new HashMap<>();
	      columnsToFieldsMap.put("id", "id");
	      columnsToFieldsMap.put("value", "value");    
	      //columnsToFieldsMap.put("cid", "category");//如果是需要自定义处理的，就不能出现在映射中
	      return columnsToFieldsMap;
    }
}
