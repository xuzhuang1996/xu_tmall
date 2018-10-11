package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import mydb.Question;
import mydb.QuestionAction;

public class QuestionAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//第一步，声明局部变量（根据需求来写局部变量，而不是一开始就写好）
		String title,optionA,optionB,optionC,optionD,answer;
		QuestionAction questionA = new QuestionAction();
		Question question = new Question();
		//第二步，读取浏览器发送的请求参数
		title = request.getParameter("title");
		optionA = request.getParameter("optionA");
		optionB = request.getParameter("optionB");
		optionC = request.getParameter("optionC");
		optionD = request.getParameter("optionD");
		answer = request.getParameter("answer");
		//第三步，将读取的试题内容，保存到数据库
		question.setTitle(title);
		question.setOptionA(optionA);
		question.setOptionB(optionB);
		question.setOptionC(optionC);
		question.setOptionD(optionD);
		question.setAnswer(answer);
		questionA.add(question);
		//在添加试题的表单提交数据后，就应该跳转到servlet页面中，而页面中的内容，则由servlet的response来写入
		//这里的QuestionQueryServlet路径其实是人为定义的，就是web.xml中人为定义的，不一定非要与servlet名字相同
		request.getRequestDispatcher("QuestionQueryServlet").forward(request, response);;
		//response.getOutputStream().write("试题添加成功".getBytes());
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
		
	}

}
