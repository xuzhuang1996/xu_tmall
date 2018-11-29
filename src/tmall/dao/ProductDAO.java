package tmall.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.mydb.GetC3p0DB;
import tmall.xu_util.DateUtil;
import tmall.xu_util.ProductHandler;
import tmall.xu_util.ProductListHandler;

public class ProductDAO {
	public int getTotal(int cid) {
        int total = 0;
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "SELECT COUNT(*) FROM `Product` WHERE `cid` = " + cid;
            QueryRunner queryRunner = new QueryRunner();
            total = ((Long)queryRunner.query(conn,sql,new ScalarHandler<>())).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return total;
    }
	
	public List<Product> list(int cid) {
        return list(cid, 0, Short.MAX_VALUE);
    }

    public List<Product> list(int cid, int start, int count) {
        List<Product> beans = new ArrayList<>();
        String sql = "SELECT * FROM `Product` WHERE `cid` = ? ORDER BY `id` DESC LIMIT ?, ? ";
        try (Connection conn = GetC3p0DB.getConnection();) {
            QueryRunner qr = new QueryRunner();
            beans = qr.query(conn,sql,new ProductListHandler() ,cid ,start ,count);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return beans;
    }
    
    public List<Product> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<Product> list(int start, int count) {
        List<Product> beans = new ArrayList<Product>();
        String sql = "SELECT * FROM `Product` LIMIT ?,? ";
        try (Connection conn = GetC3p0DB.getConnection();){
        		QueryRunner qr = new QueryRunner();
                beans = qr.query(conn,sql,new ProductListHandler() ,start ,count);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return beans;
    }
    
    public List<Product> search(String keyword, int start, int count) {
        List<Product> beans = new ArrayList<>();
        if (null == keyword || 0 == keyword.trim().length())
            return beans;
        String sql = "SELECT * FROM `Product` WHERE `name` LIKE ? LIMIT ?, ? ";
        try (Connection conn = GetC3p0DB.getConnection();) {
            QueryRunner qr = new QueryRunner();
            beans = qr.query(conn,sql,new ProductListHandler() ,"%" + keyword.trim() + "%" , start ,count);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return beans;
    }
    
    public void fill(List<Category> cs) {
        for (Category c : cs)
            fill(c);
    }

    private void fill(Category c) {
        List<Product> ps = this.list(c.getId());
        c.setProducts(ps);
    }
    
    //以productNumberEachRow为长度把你从这个分类下搜索到的所有商品，以productNumberEachRow个为一行展示
    public void fillByRow(List<Category> cs) {
        int productNumberEachRow = 8;
        for (Category c : cs) {
            List<Product> products = c.getProducts();
            List<List<Product>> productsByRow = new ArrayList<>();
            for (int i = 0; i < products.size(); i += productNumberEachRow) {
                int size = i + productNumberEachRow;
                size = size > products.size() ? products.size() : size;
                List<Product> productsOfEachRow = products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            c.setProductsByRow(productsByRow);
        }
    }
    
    public void setFirstProductImage(Product p) {
        List<ProductImage> pis = new ProductImageDAO().list(p, ProductImageDAO.type_single);
        if (!pis.isEmpty())
            p.setFirstProductImage(pis.get(0));
    }

    private void setSaleAndReviewNumber(Product p) {
        int saleCount = new OrderItemDAO().getSaleCount(p.getId());
        p.setSaleCount(saleCount);
        int reviewCount = new ReviewDAO().getTotal(p.getId());//这里没用getcount
        p.setReviewCount(reviewCount);
    }

    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product p : products)
            setSaleAndReviewNumber(p);
    }
    
	
	//===================================基本操作====================================
	public void add(Product bean) {
        String sql = "INSERT INTO `Product` VALUES(NULL, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
        	qr.update(conn , sql , bean.getName() ,bean.getSubTitle() ,bean.getOrignalPrice() ,
        			bean.getPromotePrice(), bean.getStock(), bean.getCategory().getId(), DateUtil.d2t(bean.getCreateDate()));
        	String id = ( qr.query(conn, "SELECT LAST_INSERT_ID()", new ScalarHandler<>())).toString();               
        	bean.setId(Integer.valueOf(id));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
    }
	
	public int update(Product bean) {
        String sql = "UPDATE `Product` SET `name` = ?, `subTitle` = ?, `orignalPrice` = ?, " +
                "`promotePrice` = ?, `stock` = ?, `cid` = ?, `createDate` = ? WHERE `id` = ?";
        try (Connection conn = GetC3p0DB.getConnection();) {
        	QueryRunner qr = new QueryRunner();
        	return qr.update(conn ,sql ,bean.getName() ,bean.getSubTitle(), bean.getOrignalPrice(), bean.getPromotePrice(),
        			bean.getStock(), bean.getCategory().getId() ,DateUtil.d2t(bean.getCreateDate()), bean.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return 0;
    }
	
	public int delete(int id) {
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "DELETE FROM `Product` WHERE `id` = " + id;
            QueryRunner qr = new QueryRunner();
            return qr.update(conn , sql );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return 0;
    }
	
	public Product get(int id) {
        Product bean = new Product();
        try (Connection conn = GetC3p0DB.getConnection();) {
            String sql = "SELECT * FROM `Product` WHERE `id` = " + id;
            QueryRunner qr = new QueryRunner();
            bean = qr.query(conn,sql, new ProductHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        return bean;
    }
	
}
