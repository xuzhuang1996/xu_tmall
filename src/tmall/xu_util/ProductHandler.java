package tmall.xu_util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.dao.CategoryDAO;
import tmall.dao.ProductImageDAO;

public class ProductHandler extends BeanHandler<Product>{
	public ProductHandler() {
		super(Product.class, new BasicRowProcessor(new BeanProcessor(mapColumnsToFields())));
	}
	
	@Override
	public Product handle(ResultSet rs) throws SQLException {
		Product bean= super.handle(rs);//拿到符合映射的东西
		Category category = new CategoryDAO().get(rs.getInt("cid"));
		Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
		bean.setCategory(category);
		bean.setCreateDate(createDate);
		//在listhandler中很好的工作，但是在单独的产品中行不通，不知道为啥
//		List<ProductImage>pisSingle = new ProductImageDAO().list(bean, "type_single", 0, 1);
//		if (!pisSingle.isEmpty())
//			bean.setFirstProductImage(pisSingle.get(0));
		return bean;
	}
	//根据该函数中的来映射
	public static Map<String, String> mapColumnsToFields() {
	      Map<String, String> columnsToFieldsMap = new HashMap<>();
	      columnsToFieldsMap.put("id", "id");
	      columnsToFieldsMap.put("name", "name");
	      columnsToFieldsMap.put("subTitle", "subTitle");    
	      columnsToFieldsMap.put("orignalPrice", "orignalPrice");    
	      columnsToFieldsMap.put("promotePrice", "promotePrice");    
	      columnsToFieldsMap.put("stock", "stock");    
	      return columnsToFieldsMap;
    }
	
}
