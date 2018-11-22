package tmall.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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
            // 获取分页信息，如果没有设置值，就会用上面初始化的0和5,就算报异常也忽略
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
            // 借助反射，调用对应的方法（之所以用反射，因为目前的list、add等方法都是虚函数，而service方法在基类才有，子类只是继承了，不能直接调用子类自己的list等方法）
            // 如果不用反射也可以做，但是每个类都需要自己写service方法，也就是重写doget和dopost,所以说反射节省了代码量
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
            response.sendRedirect(redirect.substring(1));//地址重定向，属于客户端跳转
        else if (redirect.startsWith("%"))
        {
        	//request.setCharacterEncoding("utf-8");  response.setContentType("text/html;charset=utf-8");
            response.getWriter().print(redirect.substring(1));
        }
        else
            request.getRequestDispatcher(redirect).forward(request, response);//分发，服务端跳转,redirect就是那个地址，可为servlet也可以为直接的jsp
    }
    //这个函数本来应该放在每一个要处理的上传的add函数下的，但是都要用，干脆在父类下写的了
    InputStream parseUpload(HttpServletRequest request, Map<String, String> params) {
        InputStream is = null;
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 设置上传文件的大小限制为10M，设置内存临界值 - 超过后将产生临时文件并存储于临时目录中（这个需要设置临时目录，不处理）
            factory.setSizeThreshold(1024 * 10240);
            @SuppressWarnings("unchecked")
			List<FileItem> items = upload.parseRequest(request);
            if(items == null || items.size()<=0)return null;
            // 迭代表单数据
            for (FileItem item : items) {
                //FileItem item = (FileItem) item1;
            	//处理不在表单中的字段,因为浏览器指定了以二进制的形式（multipart/form-data）提交数据，那么就不能通过常规的手段获取非File字段
                if (!item.isFormField()) {
                    // item.getInputStream() 获取上传文件的输入流
                    is = item.getInputStream();
                } else {
                	//request.getParameter("heroName")行不通
                    String paramName = item.getFieldName();//是表单字段的话，保存在map里
                    String paramValue = item.getString();
                    paramValue = new String(paramValue.getBytes("ISO-8859-1"), "UTF-8");
                    params.put(paramName, paramValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }
}
