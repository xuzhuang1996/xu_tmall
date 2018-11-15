package tmall.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.dao.CategoryDAO;
import tmall.dao.OrderDAO;
import tmall.dao.OrderItemDAO;
import tmall.dao.ProductDAO;
import tmall.dao.ProductImageDAO;
import tmall.dao.PropertyDAO;
import tmall.dao.PropertyValueDAO;
import tmall.dao.ReviewDAO;
import tmall.dao.UserDAO;
import tmall.xu_util.Page;

public abstract class BaseBackServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	public abstract String add(HttpServletRequest request, HttpServletResponse response, Page page);

    public abstract String delete(HttpServletRequest request, HttpServletResponse response, Page page);

    public abstract String edit(HttpServletRequest request, HttpServletResponse response, Page page);

    public abstract String update(HttpServletRequest request, HttpServletResponse response, Page page);

    public abstract String list(HttpServletRequest request, HttpServletResponse response, Page page);
    
    CategoryDAO categoryDAO = new CategoryDAO();
    OrderDAO orderDAO = new OrderDAO();
    OrderItemDAO orderItemDAO = new OrderItemDAO();
    ProductDAO productDAO = new ProductDAO();
    ProductImageDAO productImageDAO = new ProductImageDAO();
    PropertyDAO propertyDAO = new PropertyDAO();
    PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    UserDAO userDAO = new UserDAO();
    
    //有时候也会直接重写service()方法，在其中提供相应的服务，就不用区分到底是get还是post了。
    //其他servlet继承这个service方法后,
    public void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 获取分页信息，如果没有设置值，就会用上面初始化的0和5
            int start = 0;
            int count = 5;
            try {
                start = Integer.parseInt(request.getParameter("page.start"));
            } catch (Exception ignored) {
            }
            try {
                count = Integer.parseInt(request.getParameter("page.count"));
            } catch (Exception ignored) {
            }
            Page page = new Page(start, count);
            // 借助反射，调用对应的方法
            String method = (String) request.getAttribute("method");//list
            //参数1：底层方法的对象，参数2："product x"给方法传递的参数，可能有多个
            Method m = this.getClass().getMethod(method, HttpServletRequest.class,
                    HttpServletResponse.class, Page.class);
            String redirect = m.invoke(this, request, response, page).toString();//拿到servlet处理后返回的字符串。

            // 根据方法的返回值，进行相应的客户端跳转，服务端跳转，或者仅仅是输出字符串 */
            redirectStartWithCase(request, response, redirect);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    static void redirectStartWithCase(HttpServletRequest request, HttpServletResponse response, String redirect) throws IOException, ServletException {
        if (redirect.startsWith("@"))
            response.sendRedirect(redirect.substring(1));
        else if (redirect.startsWith("%"))
            response.getWriter().print(redirect.substring(1));
        else
            request.getRequestDispatcher(redirect).forward(request, response);
    }
}
