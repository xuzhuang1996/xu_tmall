package tmall.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import tmall.bean.Review;
import tmall.mydb.GetC3p0DB;
import tmall.xu_util.DateUtil;
import tmall.xu_util.ReviewHandler;
import tmall.xu_util.ReviewListHandler;

//需要测试一下，日期的转化。
public class ReviewDAO {
	
    public int getTotal() {
        int total = 0;
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "SELECT count(*) FROM `Review`";
            QueryRunner queryRunner = new QueryRunner();
            total = ((Long)queryRunner.query(conn,sql,new ScalarHandler<>())).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return total;
    }

    public int getTotal(int pid) {
        int total = 0;
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "SELECT count(*) FROM `Review` WHERE `pid` = " + pid;
            QueryRunner queryRunner = new QueryRunner();
            total = ((Long)queryRunner.query(conn,sql,new ScalarHandler<>())).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return total;
    }
	//有时候因为网络比较卡，用户在提交评价的时候，发现没有提交成功，就会多次点击提交按钮，其结果会导致同一个评论内容提交了多次。
    //为了在业务上避免这种情况的发生，在插入数据库之前会使用isExist来判断评论是否已经存在了
    public boolean isExist(String content, int pid) {
        String sql = "SELECT * FROM `Review` WHERE `content` = ? AND `pid` = ?";
        try (Connection conn = GetC3p0DB.getConnection();) {
            QueryRunner qr = new QueryRunner();
            if(qr.query(conn,sql,new ReviewHandler(), content, pid)!=null)return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return false;
    }
    
    public List<Review> list(int pid) {
        return list(pid, 0, Short.MAX_VALUE);
    }
    
    public List<Review> list(int pid, int start, int count) {
        List<Review> beans = new ArrayList<>();
        String sql = "SELECT * FROM `Review` WHERE `pid` = ? ORDER BY `id` DESC LIMIT ?, ? ";
        try (Connection conn = GetC3p0DB.getConnection();) {
            QueryRunner qr = new QueryRunner();
            beans = qr.query(conn ,sql ,new ReviewListHandler() ,pid ,start ,count);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return beans;
    }
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
            QueryRunner qr = new QueryRunner();
            bean = qr.query(conn,sql,new ReviewHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return bean;
    }
}
