package tmall.servlet;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.HtmlUtils;

import tmall.bean.Category;
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
    	return "productsBySearch.jsp";
    }
    
    //
    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page){
    	//博主写的进行了一个强制转化，我没有
    	if(request.getSession().getAttribute("user")==null)return "%fail";//失败后执行$("#loginModal").modal('show');这个modal哪里需要就将jsp放哪里
    	else return "%success";
    }
    
    public String addCart(HttpServletRequest request, HttpServletResponse response, Page page) {
    	return "";
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

    
}
