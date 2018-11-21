package tmall.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.mydb.GetC3p0DB;
import tmall.xu_util.PropertyValueListHandler;

public class PropertyValueDAO {
	
    public void init(Product p) {
        List<Property> pts = new PropertyDAO().list(p.getCategory().getId());
        for (Property pt : pts) {
            PropertyValue pv = get(pt.getId(), p.getId());
            if (null == pv) {
                pv = new PropertyValue();
                pv.setProduct(p);
                pv.setProperty(pt);
                this.add(pv);
            }
        }
    }
    
    //所有这个查询都是一样的
    public int getTotal() {
        int total = 0;
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "SELECT count(*) FROM `PropertyValue`";
            QueryRunner queryRunner = new QueryRunner();
            total = ((Long)queryRunner.query(conn,sql,new ScalarHandler<>())).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return total;
    }
	
    

    
	public List<PropertyValue> list(int pid) {
        List<PropertyValue> beans = new ArrayList<>();
        //String sql = "SELECT * FROM `PropertyValue` WHERE `pid` = ? ORDER BY `ptid` DESC";
        List<Map<String, Object>> Myparams = new ArrayList<Map<String,Object>>();
		Map<String,Object> Myparam = new HashMap<String,Object>();
		Myparam.put("name", "pid");
		Myparam.put("rela", "=");
		Myparam.put("value", pid);
		Myparams.add(Myparam);
		List<PropertyValue> temp = query(Myparams);
		if(temp.size()>0)beans = temp;
        return beans;
    }
	
	//分页查询还是没有好的解决方式
	public List<PropertyValue> list(int start, int count) {
        List<PropertyValue> beans = new ArrayList<PropertyValue>();
        String sql = "SELECT * FROM `PropertyValue` ORDER BY `id` DESC LIMIT ?, ? ";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
        	beans = qr.query(conn,sql,new PropertyValueListHandler(), start ,count);
        } catch (SQLException e) {
           e.printStackTrace();
        } catch (Exception e1) {
		   e1.printStackTrace();
		}
        return beans;
    }
	
	public List<PropertyValue> list() {
        return list(0, Short.MAX_VALUE);
    }
	

	
	//==================================基本操作================================================================
	public void add(PropertyValue bean) {
        String sql = "INSERT INTO `PropertyValue` VALUES(NULL, ?, ?, ?)";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
        	qr.update(conn , sql , bean.getProduct().getId(), bean.getProperty().getId() ,bean.getValue());
        	//是线程安全
        	String id = ( qr.query(conn, "SELECT LAST_INSERT_ID()", new ScalarHandler<>())).toString();               
        	bean.setId(Integer.valueOf(id));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
    }
	
	public int update(PropertyValue bean) {
        String sql = "UPDATE `PropertyValue` SET `pid` = ?, `ptid` = ?, `value` = ? WHERE `id` = ?";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
        	return qr.update(conn , sql , bean.getProduct().getId() ,bean.getProperty().getId() , bean.getValue() ,bean.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return 0;
    }
	
	public int delete(int id) {
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "DELETE FROM `PropertyValue` WHERE id = " + id;
            QueryRunner qr = new QueryRunner();
            return qr.update(conn , sql );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return 0;
    }
	
	public PropertyValue get(int id) {
        PropertyValue bean = null;
        List<Map<String, Object>> Myparams = new ArrayList<Map<String,Object>>();
		Map<String,Object> Myparam = new HashMap<String,Object>();
		Myparam.put("name", "id");
		Myparam.put("rela", "=");
		Myparam.put("value", id);
		Myparams.add(Myparam);
		List<PropertyValue> temp = query(Myparams);
		if(temp.size()>0)bean = temp.get(0);
        return bean;
    }
	
	public PropertyValue get(int ptid, int pid) {
        PropertyValue bean = null;
        List<Map<String, Object>> Myparams = new ArrayList<Map<String,Object>>();
		Map<String,Object> Myparam = new HashMap<String,Object>();
		Myparam.put("name", "pid");
		Myparam.put("rela", "=");
		Myparam.put("value", pid);
		Map<String,Object> Myparam2 = new HashMap<String,Object>();
		Myparam2.put("name", "ptid");
		Myparam2.put("rela", "=");
		Myparam2.put("value", ptid);
		Myparams.add(Myparam);
		Myparams.add(Myparam2);
		List<PropertyValue> temp = query(Myparams);
		if(temp.size()>0)bean = temp.get(0);
        return bean;
    }
	
	//用于查询,如果不写action层，就直接在里面做了。目前只支持属性判断。
	private List<PropertyValue> query(List<Map<String, Object>> params) {
		List<PropertyValue> result = null;
		StringBuilder sql=new StringBuilder();
		//1=1是为了跟and一起用。不然where没地方放，如果集合不存在，直接执行下面语句也可以
		sql.append("select * from PropertyValue where 1=1 ");
		if(params!=null&&params.size()>0){
			for (int i = 0; i < params.size(); i++) {
				Map<String, Object> map=params.get(i);
				sql.append(" and  "+map.get("name")+" "+map.get("rela")+" "+map.get("value")+" ");
			}
		}
		sql.append("  ORDER BY ptid DESC");
		System.out.println(sql.toString());
		//sql.append(special);//为了分页查询，需要在sql语句结尾加上limit 0,3这样的字符来选择分页的数量
		try (Connection conn = GetC3p0DB.getConnection();) {
            QueryRunner qr = new QueryRunner();
            result = qr.query(conn,sql.toString(),new PropertyValueListHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
		return result;
	}
	
	
}
