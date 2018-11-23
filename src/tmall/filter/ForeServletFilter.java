package tmall.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import tmall.bean.Category;
import tmall.bean.OrderItem;
import tmall.bean.User;
import tmall.dao.CategoryDAO;
import tmall.dao.OrderItemDAO;

/**
 * Servlet Filter implementation class ForeServletFilter
 */
@WebFilter("/*")
public class ForeServletFilter implements Filter {

    /**
     * Default constructor. 
     */
    public ForeServletFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        String contextPath = request.getContextPath();//获取路径：/xu_tmall
        request.getServletContext().setAttribute("contextPath", contextPath);
        
        User user = (User) request.getSession().getAttribute("user");
        int cartTotalItemNumber = 0;
        //如果用户在这个会话中存在，则拿到用户的订单项
        if (null != user) {
            List<OrderItem> ois = new OrderItemDAO().listByUser(user.getId());//拿到该用户的所有订单项。
            for (OrderItem oi : ois)
                cartTotalItemNumber += oi.getNumber();
        }
        request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
        
        List<Category> cs = (List<Category>) request.getAttribute("cs");
        //如果请求中没用分类的信息，则从数据库中拿
        if (null == cs) {
            cs = new CategoryDAO().list();
            request.setAttribute("cs", cs);
        }
        
        String uri = request.getRequestURI();
        System.out.println("begin ==> " + uri);
        uri = StringUtils.remove(uri, contextPath);
        if (uri.startsWith("/fore") && !uri.startsWith("/foreServlet")) {
            String method = StringUtils.substringAfterLast(uri, "/fore");
            request.setAttribute("method", method);
            req.getRequestDispatcher("/foreServlet").forward(request, response);
            return;
        }
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
