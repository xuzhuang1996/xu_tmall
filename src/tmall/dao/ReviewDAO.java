package tmall.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import tmall.bean.Review;
import tmall.mydb.GetC3p0DB;
import tmall.xu_util.DateUtil;

public class ReviewDAO {
	
	
	//=====================基本操作============================================
	public void add(Review bean) {
        String sql = "INSERT INTO `Review` VALUES(NULL, ?, ?, ?, ?)";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
        	qr.update(conn,sql,bean.getContent(),bean.getUser().getId(),bean.getProduct().getId(),DateUtil.d2t(bean.getCreateDate()));
        	//是线程安全
        	String id = ( qr.query(conn, "SELECT LAST_INSERT_ID()", new ScalarHandler<>())).toString();               
        	bean.setId(Integer.valueOf(id));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
    }
	
	public int update(Review bean) {
        String sql = "UPDATE `Review` SET `content` = ?, `uid` = ?, `pid` = ?, `createDate` = ? " +
                "WHERE id = ?";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
        	return qr.update(conn , sql , bean.getContent() ,bean.getUser().getId(), bean.getProduct().getId(),
        			DateUtil.d2t(bean.getCreateDate()), bean.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return 0;
    }
	
	public int delete(int id) {
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "DELETE FROM `Review` WHERE `id` = " + id;
            QueryRunner qr = new QueryRunner();
            return qr.update(conn , sql );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return 0;
    }
	
	public Review get(int id) {
        Review bean = new Review();
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "SELECT * FROM `Review` WHERE `id` = " + id;
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return bean;
    }
}
