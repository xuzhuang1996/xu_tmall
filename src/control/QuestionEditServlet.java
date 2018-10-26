package control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mydb.Question;
import mydb.QuestionAction;

public class QuestionEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		QuestionAction questionA = new QuestionAction();
		String title = request.getParameter("title");//如果字符串中有特殊字符如空格，加号，会很尬尬
		//为+做的处理，getQueryString拿到title=7+5,然后再去单独解析拿到7+5.加号问题没有解决。。。。。。。
		//根据题目获取数据库的试题信息,这里顺便用一下这个函数，
		List<Question> questionList = questionA.getAllTitle(title);
		//将得到的试题信息推送到jsp
		request.setAttribute("question", questionList.get(0));
		request.getRequestDispatcher("/exammanager/examEdit.jsp").forward(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
