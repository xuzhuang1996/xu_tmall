package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import tmall.bean.Category;
import tmall.mydb.GetC3p0DB;

public class CategoryDAO {
	public int getTotal() {
        int total = 0;
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "select count(*) from Category";
            QueryRunner queryRunner = new QueryRunner();
            total = ((Long)queryRunner.query(conn,sql,new ScalarHandler<>())).intValue();//ScalarHandler返回某一个值或者统计函数的值
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
	
	public List<Category> list() {
        return list(0, Short.MAX_VALUE);//免得重新获取总数
    }
	
	//分页
    public List<Category> list(int start, int count) {
        List<Category> beans = new ArrayList<Category>();
        String sql = "select * from Category order by id desc limit ?,? ";//降序desc
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
            beans = qr.query(conn,sql,new BeanListHandler<Category>(Category.class) ,start, count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }
	
	
	//===============================下面是基本操作==========================================================================
	//这个操作没有使用DbUtils，由于插入后还要取出ID。
	public void add(Category bean) {
		String sql = "insert into category values(null,?)";
		//Java7 新特性: try-with-resources
        try (Connection conn = GetC3p0DB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
        	ps.setString(1, bean.getName());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
            	//因为在插入这个数据之后，在业务上有可能会马上需要用到他的id
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public int update(Category bean) {
		String sql = "update category set name= ? where id = ?";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
        	return qr.update(conn , sql , bean.getName() , bean.getId());
        } 
	    catch (Exception e) {
			e.printStackTrace();
		} 
        return 0;
	}
	
	public int delete(int id) {
		try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
            String sql = "delete from Category where id = " + id;
            return qr.update(conn , sql );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
	
	public Category get(int id) {
        Category bean = null;//请考虑下如果这个id在数据库中没有对应的记录
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "select * from Category where id = " + id;
            QueryRunner qr = new QueryRunner();
            bean = qr.query(conn,sql, new BeanListHandler<Category>(Category.class)).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }
	
	
	
}
