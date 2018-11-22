package tmall.servlet;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.User;
import tmall.xu_util.Page;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends BaseBackServlet {
	private static final long serialVersionUID = 1L;

    public UserServlet() {
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
		List<User>us = userDAO.list(page.getStart(), page.getCount());
		int total = userDAO.getTotal();
        page.setTotal(total);
		request.setAttribute("us", us);
		request.setAttribute("page", page);
		return "admin/listUser.jsp";
	}

}
