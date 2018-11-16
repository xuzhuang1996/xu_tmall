package tmall.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Category;
import tmall.xu_util.ImageUtil;
import tmall.xu_util.Page;

/**
 * Servlet implementation class CategoryServlet
 */
@WebServlet("/CategoryServlet")
public class CategoryServlet extends BaseBackServlet {
	private static final long serialVersionUID = 1L;


    public CategoryServlet() {
    }

    //增加分类，分类里只有一个名字跟id
	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		Map<String, String> params = new HashMap<>();
		InputStream is = super.parseUpload(request, params);//在基类对request的不是表单字段的文件进行处理，获得文件的输入流，并将表单字段保存
		String name = params.get("name");
		Category c = new Category();
        c.setName(name);
        categoryDAO.add(c);//增加了分类
        
        File imageFolder = new File(request.getSession().getServletContext().getRealPath("img/category"));//getRealPath定位到webRoot目录
        File file = new File(imageFolder, c.getId() + ".jpg");//该类主要用于文件和目录的创建、文件的查找和文件的删除
        try {
            if (null != is && 0 != is.available()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte b[] = new byte[1024 * 1024];
                    int length = 0;
                    while (-1 != (length = is.read(b))) {
                        fos.write(b, 0, length);
                    }
                    fos.flush();
                    // 通过如下代码，把文件保存为jpg格式,.这里因为是部署到tomcat，因此相应的Java目录下并没有保存之后的id.jpg图片，而是在tomcat对应下的路径
                    BufferedImage img = ImageUtil.change2jpg(file);
                    ImageIO.write(img, "jpg", file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return "@admin_Category_list";
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
		List<Category> cs = categoryDAO.list(page.getStart(), page.getCount());
        int total = categoryDAO.getTotal();
        page.setTotal(total);
        request.setAttribute("thecs", cs);
        request.setAttribute("page", page);//？？？request还能传对象？
        return "admin/listCategory.jsp";
	}

}
