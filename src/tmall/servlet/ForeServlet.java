package tmall.servlet;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.HtmlUtils;

import tmall.bean.Category;
import tmall.bean.User;
import tmall.dao.CategoryDAO;
import tmall.dao.ProductDAO;
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

    
}
