package tmall.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import tmall.bean.Order;
import tmall.mydb.GetC3p0DB;
import tmall.xu_util.DateUtil;
import tmall.xu_util.OrderHandler;
import tmall.xu_util.OrderListHandler;

//也需要测试
public class OrderDAO {
	public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";
    
    
    public int getTotal() {
        int total = 0;
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "SELECT count(*) FROM `Order_`";
            QueryRunner queryRunner = new QueryRunner();
            total = ((Long)queryRunner.query(conn,sql,new ScalarHandler<>())).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return total;
    }
    
    public List<Order> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<Order> list(int start, int count) {
        List<Order> beans = new ArrayList<Order>();
        String sql = "SELECT * FROM `Order_` ORDER BY `id` DESC LIMIT ?, ? ";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
            beans = qr.query(conn,sql,new OrderListHandler() ,start, count);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return beans;
    }
    
    public List<Order> list(int uid, String excludedStatus) {
        return list(uid, excludedStatus, 0, Short.MAX_VALUE);
    }

    //根据用户订单的状态进行list
    public List<Order> list(int uid, String excludedStatus, int start, int count) {
        List<Order> beans = new ArrayList<>();
        String sql = "SELECT * FROM `Order_` WHERE `uid` = ? AND STATUS != ? ORDER BY `id` DESC " +
                "LIMIT ?, ? ";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
            beans = qr.query(conn,sql,new OrderListHandler() ,uid , excludedStatus, start, count);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return beans;
    }
    //========================================基本操作=======================================
    public void add(Order bean) {
        String sql = "INSERT INTO `Order_` VALUES(" +
                "NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
        	qr.update(conn , sql , bean.getOrderCode(), bean.getAddress(), bean.getPost(), bean.getReceiver(),
        			bean.getMobile(), bean.getUserMessage(), DateUtil.d2t(bean.getCreateDate()),
        			DateUtil.d2t(bean.getPayDate()), DateUtil.d2t(bean.getDeliveryDate()), bean.getConfirmDate(),
        			bean.getUser().getId(), bean.getStatus());
        	
        	String id = ( qr.query(conn, "SELECT LAST_INSERT_ID()", new ScalarHandler<>())).toString();               
        	bean.setId(Integer.valueOf(id));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
    }
    
    public int update(Order bean) {
        String sql = "UPDATE `Order_` " +
                "SET `address` = ?, `post` = ?, `receiver` = ?, `mobile` = ?, `userMessage` = ? ," +
                "`createDate` = ?, " +
                "`payDate` = ?, `deliveryDate` = ?, `confirmDate` = ?, `orderCode` = ?, `uid` = " +
                "?, `status` = ? WHERE `id` = ?";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
        	return qr.update(conn , sql , bean.getAddress() ,bean.getPost() ,bean.getReceiver() ,bean.getMobile(),
        			bean.getUserMessage(), DateUtil.d2t(bean.getCreateDate()), DateUtil.d2t(bean.getPayDate()),
        			DateUtil.d2t(bean.getDeliveryDate()), DateUtil.d2t(bean.getConfirmDate()), bean.getOrderCode(),
        			bean.getUser().getId(), bean.getStatus(), bean.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return 0;
    }
    
    public int delete(int id) {
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "DELETE FROM `Order_` WHERE `id` = " + id;
            QueryRunner qr = new QueryRunner();
            return qr.update(conn , sql );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return 0;
    }
    
    public Order get(int id) {
        Order bean = new Order();
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "SELECT * FROM `Order_` WHERE `id` = " + id;
            QueryRunner qr = new QueryRunner();
            //当查询得到的结果与对象的具体属性不一致时应该怎么解决
            bean = qr.query(conn,sql, new OrderHandler());//使用自定义的属性处理工具。
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return bean;
    }
    
}
