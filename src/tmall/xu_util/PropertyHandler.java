package tmall.xu_util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.dao.CategoryDAO;

//数据库表中的列名和等价的javabean对象名称不相似，那么我们可以通过使用自定义的BasicRowProcessor对象来映射它们
public class PropertyHandler extends BeanHandler<Property>{

	public PropertyHandler() {
		super(Property.class, new BasicRowProcessor(new BeanProcessor(mapColumnsToFields())));
		
	}

	@Override
	public Property handle(ResultSet rs) throws SQLException {
		Property p= super.handle(rs);//拿到符合映射的东西
		Category category = new CategoryDAO().get(rs.getInt("cid"));//根据结果集中的cid获取目录类中的对象。
		p.setCategory(category);//设置不符合直接映射的东西
		return p;
	}
	//根据该函数中的来映射
	public static Map<String, String> mapColumnsToFields() {
	      Map<String, String> columnsToFieldsMap = new HashMap<>();
	      columnsToFieldsMap.put("id", "id");
	      columnsToFieldsMap.put("name", "name");    
	      //columnsToFieldsMap.put("cid", "category");//如果是需要自定义处理的，就不能出现在映射中
	      return columnsToFieldsMap;
    }
	
}
