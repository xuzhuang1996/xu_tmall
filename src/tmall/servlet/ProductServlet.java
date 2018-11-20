package tmall.servlet;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.xu_util.Page;
import tmall.xu_util.XuEncodeUtil;


@WebServlet("/ProductServlet")
public class ProductServlet extends BaseBackServlet {
	private static final long serialVersionUID = 1L;
       

    public ProductServlet() {}


	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));
		String name=XuEncodeUtil.getNewString(request.getParameter("name"));
		String subTitle=XuEncodeUtil.getNewString(request.getParameter("subTitle"));
		float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
		float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
		int stock = Integer.parseInt(request.getParameter("stock"));
		Product p =new Product();
		p.setCategory(categoryDAO.get(cid));
		p.setName(name);
		p.setSubTitle(subTitle);
		p.setOrignalPrice(orignalPrice);
		p.setPromotePrice(promotePrice);
		p.setStock(stock);
		productDAO.add(p);
		return "@admin_Product_list?cid="+cid;
	}


	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		int cid = productDAO.get(id).getCategory().getId();
		productDAO.delete(id);
		return "@admin_Product_list?cid="+cid;
	}


	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Product p = productDAO.get(id);
		request.setAttribute("p", p);
		return "admin/editProduct.jsp";
	}


	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		int cid = Integer.parseInt(request.getParameter("cid"));
		String name=XuEncodeUtil.getNewString(request.getParameter("name"));
		String subTitle=XuEncodeUtil.getNewString(request.getParameter("subTitle"));
		float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
		float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
		int stock = Integer.parseInt(request.getParameter("stock"));
		Product p = productDAO.get(id);
		p.setName(name);
		p.setSubTitle(subTitle);
		p.setOrignalPrice(orignalPrice);
		p.setPromotePrice(promotePrice);
		p.setStock(stock);
		productDAO.update(p);
		return "@admin_Product_list?cid="+cid;//过滤器。以及属性值应该不考虑吧？（后面考虑）
	}


	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c=  categoryDAO.get(cid);
		List<Product>ps = productDAO.list(cid, page.getStart(), page.getCount());
		int total = productDAO.getTotal(cid);
		page.setTotal(total);
		page.setParam("&cid="+cid);
		request.setAttribute("c", c);
		request.setAttribute("ps", ps);
        request.setAttribute("page", page);
		return "admin/listProduct.jsp";
	}

}
