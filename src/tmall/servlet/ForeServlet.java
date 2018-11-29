package tmall.servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.HtmlUtils;

import tmall.bean.Category;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.bean.PropertyValue;
import tmall.bean.Review;
import tmall.bean.User;
import tmall.dao.CategoryDAO;
import tmall.dao.ProductDAO;
import tmall.dao.ProductImageDAO;
import tmall.xu_util.Page;
import tmall.xu_util.XuEncodeUtil;

/**
 * Servlet implementation class ForeServlet
 */
@WebServlet("/ForeServlet")
public class ForeServlet extends BaseForeServlet {
	private static final long serialVersionUID = 1L;
       
    //categoryMenu.jsp需要cs，我觉得主要是缺信息，cs是有了，但是这个cs的其他信息不够
    public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
    	List<Category> cs = new CategoryDAO().list();
        new ProductDAO().fill(cs);
        new ProductDAO().fillByRow(cs);
        request.setAttribute("cs", cs);
    	return "home.jsp";
    }
    
    public String register(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String name = XuEncodeUtil.getNewString(request.getParameter("name"));
        String password = request.getParameter("password");
        name = HtmlUtils.htmlEscape(name);//java后台对前端输入的特殊字符进行转义
        if(userDAO.isExist(name)) {
        	request.setAttribute("msg", "用户名已经被使用,不能使用");
        	return "register.jsp";
        }
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        userDAO.add(user);
    	return "@registerSuccess.jsp";//肯定要重定向啊，万一有mse怎么办
    }
    
    public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String name = XuEncodeUtil.getNewString(request.getParameter("name"));
    	String password = request.getParameter("password");
    	name = HtmlUtils.htmlEscape(name);//java后台对前端输入的特殊字符进行转义
    	User user = userDAO.get(name, password);
    	if(user == null) {
    		request.setAttribute("msg", "密码错误或不存在该用户");
    		return "login.jsp";
    	}
    	//getSession()：
    	//当向Session中存取登录信息时，一般建议：HttpSession session =request.getSession();
    	//当从Session中获取登录信息时，一般建议：HttpSession session =request.getSession(false);
    	request.getSession().setAttribute("user", user);//现在有用户了，因此进入下一个页面的时候，前台过滤器会拿到这个用户。
    	return "@forehome";//不是这个home.jsp的原因，需要重定向嘛
    }
    
    public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
        request.getSession().removeAttribute("user");
        return "@forehome";
    }
    //product,review,provertyvalue.现在还差数据没给
    public String product(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int pid= Integer.parseInt(request.getParameter("pid"));
    	Product p = productDAO.get(pid);
    	List<Review> reviews = reviewDAO.list(pid);
    	List<PropertyValue> pvs = propertyValueDAO.list(pid);
    	//刚取出的product没有初始化图片，最多一个first图片，而没有所有的图片
    	List<ProductImage> productSingleImages = productImageDAO.list(p, ProductImageDAO.type_single);
        List<ProductImage> productDetailImages = productImageDAO.list(p, ProductImageDAO.type_detail);
        p.setProductSingleImages(productSingleImages);
        p.setProductDetailImages(productDetailImages);
    	request.setAttribute("p", p);
    	request.setAttribute("reviews", reviews);
    	request.setAttribute("pvs", pvs);
    	return "product.jsp";
    }
    
    public String search(HttpServletRequest request, HttpServletResponse response, Page page) {
    	//String keyword = XuEncodeUtil.getNewString(request.getParameter("keyword"));
    	String keyword =request.getParameter("keyword");//将提交方式改为get了。没办法。哎
    	List<Product> ps = productDAO.search(keyword, 0, 20);
    	//productDAO.setSaleAndReviewNumber(ps);
        request.setAttribute("ps", ps);
    	return "searchResult.jsp";//为啥不是这个productsBySearch.jsp，因为它放在最里面，地址不对访问不到。访问searchResult.jsp是因为直接在外面
    }
    
    //
    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page){
    	//博主写的进行了一个强制转化，我没有
    	if(request.getSession().getAttribute("user")==null)return "%fail";//失败后执行$("#loginModal").modal('show');这个modal哪里需要就将jsp放哪里
    	else return "%success";
    }
    
    //购物车就生成订单项，应该点击购物车之后就更新右上角的购物车数量。有空做
    public String addCart(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int pid= Integer.parseInt(request.getParameter("pid"));
    	int num= Integer.parseInt(request.getParameter("num"));
    	User user = (User)request.getSession().getAttribute("user");
    	
    	//如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。 那么就应该在对应的OrderItem基础上，调整数量，
    	boolean found = false;
    	List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
    	for (OrderItem oi : ois) {
            if (oi.getProduct().getId() == pid) {
                oi.setNumber(oi.getNumber() + num);
                orderItemDAO.update(oi);//容易忘
                found = true;
                break;
            }
        }
        if (!found) {
            OrderItem oi = new OrderItem();
        	oi.setUser(user);
        	oi.setProduct(productDAO.get(pid));
            orderItemDAO.add(oi);
        }
    	return "%success";
    }
    
    public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String name = request.getParameter("name");
    	String password = request.getParameter("password");
    	name = HtmlUtils.htmlEscape(name);
    	User user = userDAO.get(name, password);
    	if(user == null) {
    		return "%fail";
    	}
    	request.getSession().setAttribute("user", user);
    	return "%success";
    }
    
    public String category(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int cid= Integer.parseInt(request.getParameter("cid"));
    	String sort = request.getParameter("sort");
    	Category c= categoryDAO.get(cid);
    	List<Product>ps= productDAO.list(cid);//数据库不可能存这些信息，只能每次拿出来的时候进行设置
    	productDAO.fill(Collections.singletonList(c));//????将该分类下的产品进行绑定
    	//productDAO.setSaleAndReviewNumber(c.getProducts());//将所有产品进行属性赋值,这个的前提是每一个都要有一个订单项，不然报错
    	//对产品进行排序
    	if(sort!=null) {
    		switch(sort) {
	    	case "all":
	    		ps.sort((p1,p2)->p2.getReviewCount() * p2.getSaleCount() - p1.getReviewCount() * p1.getSaleCount());
	    		break;
	    	case "review":
	    	    ps.sort((p1,p2)->p2.getReviewCount() - p1.getReviewCount());
	    		break;
	    	case "date":
	    		ps.sort((p1,p2)->p2.getCreateDate().compareTo(p1.getCreateDate()));
	    		break;
	    	case "saleCount":
	    		ps.sort((p1,p2)->p2.getSaleCount() - p1.getSaleCount());
	    		break;
	    	case "price":
	    		ps.sort((p1,p2)->(int)(p1.getPromotePrice() - p2.getPromotePrice()));
	    		break;
	    	default:
	    		break;
    	}
    	}
    	c.setProducts(ps);
    	request.setAttribute("c", c);
    	return "category.jsp";
    }
    
    //在产品页，如果已经登录，点击购买，会提交数据到服务端，生成订单项，并且跳转到结算页面。我觉得这个名字应该改成buyNow
    public String buyone(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int pid= Integer.parseInt(request.getParameter("pid"));
    	User user = (User)request.getSession().getAttribute("user");
    	int number =Integer.parseInt(request.getParameter("num"));
    	OrderItem oi = new OrderItem();
    	oi.setUser(user);
    	oi.setProduct(productDAO.get(pid));
    	//如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。 那么就应该在对应的OrderItem基础上，调整数量，当然了，这个操作我肯定不做了
    	oi.setNumber(number);
    	orderItemDAO.add(oi);
    	int oiid = oi.getId();
    	return "@forebuy?oiid=" + oiid;//我的话直接写return "buy.jsp"，但是buy.jsp接收的参数是ois，即所有订单项。因此肯定时考虑了从购物车购买的情况。将2种情况进行综合，在结算页面
    }
    
    //在结算页面显示被选中的订单项，缺ois与total
    public String buy(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String[] oiids = request.getParameterValues("oiid");//是获得如checkbox类（名字相同，但值有多个）的数据
    	List<OrderItem>ois=new ArrayList<>();
    	float total =0.0f;
    	for(int i=0; i<oiids.length;i++) {
    		int id = Integer.parseInt(oiids[i]);
    		OrderItem oi = orderItemDAO.get(id);
    		total += oi.getProduct().getPromotePrice()*oi.getNumber();
    		//由于firstImage只在listHandler中处理。如果单独在handler中处理就报数据库连接过多的错误(原因未知)，因此这里只能自己单独给每个产品加firstImage
//    		List<ProductImage>pisSingle = new ProductImageDAO().list(oi.getProduct(), "type_single", 0, 1);
//    		if (!pisSingle.isEmpty())
//    			oi.getProduct().setFirstProductImage(pisSingle.get(0));
    		productDAO.setFirstProductImage(oi.getProduct());
    		//最后
    		ois.add(oi);
    	}
    	//ois为什么要放在session里而不是request中？.购物车里面要用，放在 request 里查不到。
    	request.getSession().setAttribute("ois", ois);
    	request.setAttribute("total", total);
    	return "buy.jsp";
    }
    
    //缺ois
    public String cart(HttpServletRequest request, HttpServletResponse response, Page page) {
    	User user = (User)request.getSession().getAttribute("user");
//    	if(user==null)return "login.jsp";//用了那个过滤器后，就不需要对每一个缺session的页面加这句话了。
    	List<OrderItem>ois=orderItemDAO.listByUser(user.getId());
    	for(OrderItem oi:ois) {
    		productDAO.setFirstProductImage(oi.getProduct());
    	}
    	request.setAttribute("ois", ois);
    	return "cart.jsp";
    }
    
    public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int oiid = Integer.parseInt(request.getParameter("oiid"));
    	if(orderItemDAO.delete(oiid)<=0)return "%fail";
    	else return "%success";
    }
    
    public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page) {
    	
    	return "";
    }
    
    public String changeOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int oiid = Integer.parseInt(request.getParameter("oiid"));//我觉得这个好点
    	int num = Integer.parseInt(request.getParameter("num"));
    	OrderItem oi = orderItemDAO.get(oiid);
    	oi.setNumber(num);
    	orderItemDAO.update(oi);
    	return "%success";
    }

    
}
