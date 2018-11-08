package tmall.bean;

//在产品页面，立即购买，或者加入购物车就会创建一条OrderItem对象。
//而此时必须有Product，并且是登录状态(能够从Session中取出User)
//但是此时还没有和Order关联起来
public class OrderItem {
	private int number;
    private Product product;
    private Order order;
    private User user;
    private int id;
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
