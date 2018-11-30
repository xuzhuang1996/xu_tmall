package tmall.servlet;


import java.util.Date;
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
        orderItemDAO.fill(os);
        request.setAttribute("os", os);
        request.setAttribute("page", page);
		return "admin/listOrder.jsp";
	}
	
	public String delivery(HttpServletRequest request, HttpServletResponse response, Page page) {
		int oid = Integer.parseInt(request.getParameter("id"));
		Order o = orderDAO.get(oid);
		o.setDeliveryDate(new Date());//日期容易忘
		o.setStatus(orderDAO.waitConfirm);
		orderDAO.update(o);//这个最容易忘，总是set以后忘记更新
		return "@admin_Order_list";//哎为啥要重定向。填的admin/listOrder.jsp报错，估计是没有这个os page数据吧
	}


}
