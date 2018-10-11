package control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mydb.Question;
import mydb.QuestionAction;

public class QuestionQueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
		 * The doGet method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to get.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//第一步，声明局部变量
		QuestionAction questionA = new QuestionAction();
		List<Question> questionList = null;
		int pageIndex = 0;//在这里进行分页处理,需要根据浏览器请求的页数，来计算当前页对应的起始行
		int startLine = 0;//当前页对应起始行的行号
		int totalCount = 0;//总行数
		int totalPage = 0;//总页数
		//第二步，使用JDBC技术将数据库中试题读取到内存中
		//第三步，将得到的数据行转换为对象的形式保存。
		//当请求页数为空或者空字符串时,pageIndex为空时，pageIndex=1；否则，将pageIndex转为int数据
		pageIndex = request.getParameter("pageIndex")==null || "".equals(request.getParameter("pageIndex"))?1:Integer.valueOf(request.getParameter("pageIndex"));
		startLine = (pageIndex - 1)*3;//每页3行
		questionList = questionA.queryAll(startLine,3);
		totalCount = questionA.count();
		totalPage = totalCount%3==0?totalCount/3:totalCount/3+1;//如果不是恰好3的倍数，页数加1
		//第四步，将得到的试题对象，通过请求转发，共享给指定的jsp.
		request.setAttribute("questionList", questionList);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("pageIndex", pageIndex);//按钮进行数据请求，因此得到pageIndex的值，通过该servlet进行来确定下次请求的数据
		//getRequestDispatcher是服务器内部跳转，地址栏信息不变，只能跳转到web应用内的网页。
		//通过forward命令发送请求，发送之后，再jsp实现相关内容
		request.getRequestDispatcher("exammanager/exams.jsp").forward(request, response);
	}

	/**
		 * The doPost method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to post.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
