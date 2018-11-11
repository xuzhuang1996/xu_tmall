package tmall.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.mydb.GetC3p0DB;
import tmall.xu_util.OrderItemHandler;
import tmall.xu_util.OrderItemListHandler;

public class OrderItemDAO {
	
    public int getTotal() {
        int total = 0;
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "SELECT count(*) FROM `OrderItem`";
            QueryRunner queryRunner = new QueryRunner();
            total = ((Long)queryRunner.query(conn,sql,new ScalarHandler<>())).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return total;
    }
    
    int getSaleCount(int pid) {
        int total = 0;
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "SELECT sum(number) FROM `OrderItem` WHERE `pid` = " + pid;
            QueryRunner queryRunner = new QueryRunner();
            total = ((Long)queryRunner.query(conn,sql,new ScalarHandler<>())).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return total;
    }
    
    public List<OrderItem> listByUser(int uid) {
        return listByUser(uid, 0, Short.MAX_VALUE);
    }

    private List<OrderItem> listByUser(int uid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();
        String sql = "SELECT " +
                "* " +
                "FROM `OrderItem` WHERE `uid` = ? AND `oid` = -1 ORDER BY `id` DESC " +
                "LIMIT ?, ? ";
        try (Connection conn = GetC3p0DB.getConnection();) {
            QueryRunner qr = new QueryRunner();
            beans = qr.query(conn,sql,new OrderItemListHandler() ,uid ,start ,count );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return beans;
    }
    
    public List<OrderItem> listByOrder(int oid) {
        return listByOrder(oid, 0, Short.MAX_VALUE);
    }

    private List<OrderItem> listByOrder(int oid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();
        String sql = "SELECT * FROM `OrderItem` WHERE `oid` = ? ORDER BY `id` DESC LIMIT ?, ? ";
        try (Connection conn = GetC3p0DB.getConnection();) {
            QueryRunner qr = new QueryRunner();
            beans = qr.query(conn,sql,new OrderItemListHandler() ,oid ,start ,count );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return beans;
    }
    
    public List<OrderItem> listByProduct(int pid) {
        return listByProduct(pid , Short.MAX_VALUE);
    }

    private List<OrderItem> listByProduct(int pid, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();
        String sql = "SELECT * FROM `OrderItem` WHERE `pid` = ? ORDER BY `id` DESC LIMIT ?, ? ";
        try (Connection conn = GetC3p0DB.getConnection();) {
            QueryRunner qr = new QueryRunner();
            beans = qr.query(conn,sql,new OrderItemListHandler() ,pid ,0 ,count );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return beans;
    }
    //暂时不清楚用途
    public void fill(List<Order> os) {
        for (Order o : os) {
            List<OrderItem> ois = listByOrder(o.getId());
            float total = 0;
            int totalNumber = 0;
            for (OrderItem oi : ois) {
                total += oi.getNumber() * oi.getProduct().getPromotePrice();
                totalNumber += oi.getNumber();
            }
            o.setTotal(total);
            o.setOrderItems(ois);
            o.setTotalNumber(totalNumber);
        }
    }

    public void fill(Order o) {
        List<OrderItem> ois = listByOrder(o.getId());
        float total = 0;
        for (OrderItem oi : ois)
            total += oi.getNumber() * oi.getProduct().getPromotePrice();

        o.setTotal(total);
        o.setOrderItems(ois);
    }
    
	//========================================基本操作==================================
	public void add(OrderItem bean) {
        String sql = "INSERT INTO `OrderItem` VALUES(NULL, ?, ?, ?, ?)";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
            // 订单项在创建的时候，是没有蒂订单信息的
            if (null == bean.getOrder())
            	qr.update(conn , sql , bean.getProduct().getId() ,-1 ,bean.getUser().getId() ,bean.getNumber() );
            else
            	qr.update(conn , sql , bean.getProduct().getId() ,bean.getOrder().getId() ,bean.getUser().getId() ,
            			bean.getNumber() );
            String id = ( qr.query(conn, "SELECT LAST_INSERT_ID()", new ScalarHandler<>())).toString();               
        	bean.setId(Integer.valueOf(id));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
    }
	
	public int update(OrderItem bean) {
        String sql = "UPDATE OrderItem SET pid = ?, oid= ?, uid = ?, `number` = ? WHERE `id` = ?";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
            if (null == bean.getOrder())
            	return qr.update(conn , sql , bean.getProduct().getId() ,-1 , bean.getUser().getId(), bean.getNumber(),
            			bean.getId());
            else
            	return qr.update(conn , sql , bean.getProduct().getId() ,bean.getOrder().getId() , bean.getUser().getId(), 
            			bean.getNumber(), bean.getId());
                
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return 0;
    }
	
	public int delete(int id) {
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
            String sql = "DELETE FROM `OrderItem` WHERE `id` = " + id;
            return qr.update(conn , sql );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return 0;
    }
	
	public OrderItem get(int id) {
        OrderItem bean = new OrderItem();
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "SELECT * FROM `OrderItem` WHERE `id` = " + id;
            QueryRunner qr = new QueryRunner();
            bean = qr.query(conn,sql, new OrderItemHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return bean;
    }
}
