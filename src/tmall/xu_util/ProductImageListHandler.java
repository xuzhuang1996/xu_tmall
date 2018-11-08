package tmall.xu_util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;


import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.dao.ProductDAO;

public class ProductImageListHandler extends BeanListHandler<ProductImage> {
	public ProductImageListHandler() {
		super(ProductImage.class, new BasicRowProcessor(new BeanProcessor(mapColumnsToFields())));
		
	}

	@Override
	public List<ProductImage> handle(ResultSet rs) throws SQLException {
		List<ProductImage> pi= super.handle(rs);//拿到符合映射的东西
		rs.first();//重新定向rs的游标。再次读取使用
		for(int index = 0 ; index<pi.size(); index++) {
			Product product = new ProductDAO().get(rs.getInt("pid"));//根据结果集中的pid获取目录类中的对象。
			pi.get(index).setProduct(product);//设置不符合直接映射的东西
			rs.next();//游标下移
		}
		return pi;
	}
	//根据该函数中的来映射
	public static Map<String, String> mapColumnsToFields() {
	      Map<String, String> columnsToFieldsMap = new HashMap<>();
	      columnsToFieldsMap.put("id", "id");
	      columnsToFieldsMap.put("type", "type");    
	      //columnsToFieldsMap.put("pid", "category");//如果是需要自定义处理的，就不能出现在映射中
	      return columnsToFieldsMap;
	}
}
