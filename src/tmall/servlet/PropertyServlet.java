package tmall.servlet;


import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.xu_util.Page;
import tmall.xu_util.XuEncodeUtil;

@WebServlet("/PropertyServlet")
public class PropertyServlet extends BaseBackServlet{
	private static final long serialVersionUID = 1L;
	
    public PropertyServlet() {
        super(); 
    }

@Override
public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
	//前端传来name,cid.这里不一样在于，之前进入list的时候分类页面下有传一个cid，而这里没直接传。我觉得可以request传一个，但是之后重定向。不可
	int cid = Integer.parseInt(request.getParameter("cid"));
	//String name=XuEncodeUtil.getNewString(request.getParameter("name"));
	String name=request.getParameter("name");
	Category c = categoryDAO.get(cid);
	Property p = new Property();
	p.setCategory(c);
	p.setName(name);
	propertyDAO.add(p);
	return "@admin_Property_list?cid="+cid;//由于之后重定向，因此直接在这里面写cid
}

@Override
public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
	int id = Integer.parseInt(request.getParameter("id"));
	int cid = propertyDAO.get(id).getCategory().getId();//拿到当前删除的属性的分类cid。用于待会删除之后的重定向
	propertyDAO.delete(id);
	return "@admin_Property_list?cid="+cid;
}

@Override
public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
	int id = Integer.parseInt(request.getParameter("id"));
	Property p =propertyDAO.get(id);
	request.setAttribute("p", p);
	return "admin/editProperty.jsp";
}

@Override
public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
	String name=request.getParameter("name");
	int cid = Integer.parseInt(request.getParameter("cid"));
	int id = Integer.parseInt(request.getParameter("id"));
	Property p =propertyDAO.get(id);
	p.setName(name);
	propertyDAO.update(p);
	return "@admin_Property_list?cid="+cid;
}

@Override
public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
	int cid = Integer.parseInt(request.getParameter("cid"));
	int total = propertyDAO.getTotal(cid);
	Category c = categoryDAO.get(cid);
	List<Property> ps = propertyDAO.list(cid, page.getStart(), page.getCount());
	page.setTotal(total);
	page.setParam("&cid="+cid);
	request.setAttribute("c", c);
    request.setAttribute("ps", ps);
    request.setAttribute("page", page);
    return "admin/listProperty.jsp";//服务器跳转.要直接这样访问，必须有ps跟page这样的对象，不然页面没有信息
}

}
