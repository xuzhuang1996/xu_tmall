package tmall.servlet;


import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Order;
import tmall.xu_util.Page;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends BaseBackServlet {
	private static final long serialVersionUID = 1L;
       
    public OrderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		int total = userDAO.getTotal();
        page.setTotal(total);
        List<Order>os=orderDAO.list(page.getStart(), page.getCount());
        request.setAttribute("os", os);
        request.setAttribute("page", page);
		return "admin/listOrder.jsp";
	}


}
