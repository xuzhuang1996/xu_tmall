package tmall.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import tmall.bean.Property;
import tmall.mydb.GetC3p0DB;
import tmall.xu_util.PropertyHandler;

public class PropertyDAO {
	
	public int getTotal(int cid) {
        int total = 0;
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "select count(*) from Property where cid =" + cid;
            QueryRunner queryRunner = new QueryRunner();
            total = ((Long)queryRunner.query(conn,sql,new ScalarHandler<>())).intValue();//ScalarHandler返回某一个值或者统计函数的值
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return total;
    }
	
	
	public List<Property> list(int cid) {
        return list(cid, 0, Short.MAX_VALUE);
    }
	
	public List<Property> list(int cid, int start, int count) {
        List<Property> beans = new ArrayList<Property>();
        String sql = "select * from Property where cid = ? order by id desc limit ?,? ";
  
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
            beans = qr.query(conn,sql,new BeanListHandler<Property>(Property.class) ,cid , start, count);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return beans;
    }
	
	//==================================基本操作=========================================================
	public void add(Property bean) {
        String sql = "insert into Property values(null,?,?)";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
        	qr.update(conn , sql , bean.getCategory().getId(), bean.getName());
        	//是线程安全
        	String id = ( qr.query(conn, "SELECT LAST_INSERT_ID()", new ScalarHandler<>())).toString();               
        	bean.setId(Integer.valueOf(id));
        } catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public int update(Property bean) {
        String sql = "update Property set cid= ?, name=? where id = ?";
        try (Connection conn = GetC3p0DB.getConnection(); ) {
        	QueryRunner qr = new QueryRunner();
        	return qr.update(conn , sql , bean.getCategory().getId() ,bean.getName() , bean.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return 0;
    }
	
	public int delete(int id) {
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "delete from Property where id = " + id;
            QueryRunner qr = new QueryRunner();
            return qr.update(conn , sql );
        } catch (SQLException e) {
  
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return 0;
    }
	
	public Property get(int id) {
        Property bean = new Property();
  
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "select * from Property where id = " + id;
            QueryRunner qr = new QueryRunner();
            //当查询得到的结果与对象的具体属性不一致时应该怎么解决
            //bean = qr.query(conn,sql, new BeanListHandler<Property>(Property.class)).get(0);
            bean = qr.query(conn,sql, new PropertyHandler());//使用自定义的属性处理工具。
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return bean;
    }
		
}
