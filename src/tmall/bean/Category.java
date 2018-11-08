package tmall.bean;

import java.util.List;

public class Category {
	private String name;
    private int id;
    //一个分类里有多个产品，保持一对多的关系
    //由于业务上没有需求，即根据分类来查询对应的属性，因此不设计属性的一对多的关系
    private List<Product> products;
    //一个分类会对应多行产品，而一行产品里又有多个产品记录
    //为了实现界面上的这个功能，为Category类设计了这样一个集合属性
    private List<List<Product>> productsByRow;
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    @Override
    public String toString() {
        return "Category [name=" + name + "]";
    }
 
    public List<Product> getProducts() {
        return products;
    }
 
    public void setProducts(List<Product> products) {
        this.products = products;
    }
 
    public List<List<Product>> getProductsByRow() {
        return productsByRow;
    }
 
    public void setProductsByRow(List<List<Product>> productsByRow) {
        this.productsByRow = productsByRow;
    }
}
