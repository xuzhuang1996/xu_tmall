package tmall.servlet;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.PropertyValue;
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
		propertyValueDAO.init(p);
		return "@admin_Product_list?cid="+cid;
	}


	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		int cid = productDAO.get(id).getCategory().getId();
		productDAO.delete(id);
		return "@admin_Product_list?cid="+cid;//如果有图片的话，就删不掉
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
	
	public String editPropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pid = Integer.parseInt(request.getParameter("id"));
		Product p = productDAO.get(pid);
		List<PropertyValue> pvs = propertyValueDAO.list(pid);
		//propertyValueDAO.init(p);//如果新增一个产品。不使用这个函数的话，在数据库中查找根据产品pid来查propertyValue中值，会发现是空的。接着在这个产品下去设置属性，会发现是空的，
		//但此时数据库中却出现了属性值对应的行项。接着再进去，页面就有属性值了。（我觉得应该新建的时候就初始化,）
		request.setAttribute("p", p);
		request.setAttribute("pvs", pvs);
		return "admin/editProductValue.jsp";
	}
	
	//这里采用异步提交方式，编辑即修改,修改成功用绿色边框表示.但我不能每次编辑都访问一次数据库吧？编辑成功后应该返回ajax的内容，因此应该以%开头
	public String updatePropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pvid = Integer.parseInt(request.getParameter("pvid"));
		String value = XuEncodeUtil.getNewString(request.getParameter("value"));//ajax依然乱码
		PropertyValue pv = propertyValueDAO.get(pvid);
        pv.setValue(value);
        propertyValueDAO.update(pv);
		return "%success";
	}

}
