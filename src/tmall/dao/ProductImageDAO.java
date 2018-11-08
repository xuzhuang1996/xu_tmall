package tmall.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.mydb.GetC3p0DB;
import tmall.xu_util.ProductImageHandler;
import tmall.xu_util.ProductImageListHandler;

//产品图片保存是按照productImage.id.png的方式保存的，这样就解决了文件名的问题，无需额外维护文件名信息，并且不会重复。 
public class ProductImageDAO {
	//常量通常不会放在bean里。 在较多场合bean都会自动生成，如果放在bean里，在自动生成的时候，就被覆盖了。bean尽量保持清爽
	public static final String type_single = "type_single";
    public static final String type_detail = "type_detail";
    
    public int getTotal() {
        int total = 0;
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "SELECT count(*) FROM `ProductImage`";
            QueryRunner queryRunner = new QueryRunner();
            total = ((Long)queryRunner.query(conn,sql,new ScalarHandler<>())).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return total;
    }
    
    public List<ProductImage> list(Product p, String type) {
        return list(p, type, 0, Short.MAX_VALUE);
    }

    public List<ProductImage> list(Product p, String type, int start, int count) {
        List<ProductImage> beans = new ArrayList<ProductImage>();
        String sql = "SELECT * FROM `ProductImage` WHERE `pid` = ? AND `type` = ? ORDER BY `id` " +
                "DESC LIMIT ?, ? ";
        
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
            beans = qr.query(conn,sql,new ProductImageListHandler(),p.getId(),type,start,count);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return beans;
    }
    
	//==================================基本操作(没写更新,因为业务上不需要)=========================================================
	public void add(ProductImage bean) {
        String sql = "INSERT INTO `ProductImage` VALUES(NULL, ?, ?)";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
        	qr.update(conn , sql , bean.getProduct().getId(), bean.getType());
        	//是线程安全
        	String id = ( qr.query(conn, "SELECT LAST_INSERT_ID()", new ScalarHandler<>())).toString();               
        	bean.setId(Integer.valueOf(id));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
    }
	
	public int delete(int id) {
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "DELETE FROM `ProductImage` WHERE `id` = " + id;
            QueryRunner qr = new QueryRunner();
            return qr.update(conn , sql );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return 0;
    }
	
	public ProductImage get(int id) {
        ProductImage bean = new ProductImage();
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "SELECT * FROM `ProductImage` WHERE `id` = " + id;
            QueryRunner qr = new QueryRunner();
            //当查询得到的结果与对象的具体属性不一致时应该怎么解决
            bean = qr.query(conn,sql, new ProductImageHandler());//使用自定义的属性处理工具。
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return bean;
    }
}
