package tmall.xu_util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.dao.CategoryDAO;

public class PropertyListHandler extends BeanListHandler<Property>{
	public PropertyListHandler() {
		super(Property.class, new BasicRowProcessor(new BeanProcessor(mapColumnsToFields())));
		
	}

	@Override
	public List<Property> handle(ResultSet rs) throws SQLException {
		List<Property> p= super.handle(rs);//拿到符合映射的东西，已经遍历过一次了。因此下面再次使用需要重新定向
		rs.first();//重新定向rs的游标。再次读取使用
		for(int index = 0 ;index<p.size();index++) {
			Category category = new CategoryDAO().get(rs.getInt("cid"));//根据结果集中的cid获取目录类中的对象。
			p.get(index).setCategory(category);//设置不符合直接映射的东西
			rs.next();//游标下移
		}
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
