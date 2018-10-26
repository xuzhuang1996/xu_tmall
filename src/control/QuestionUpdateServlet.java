package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mydb.Question;
import mydb.QuestionAction;

public class QuestionUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title,optionA,optionB,optionC,optionD,answer;
		int id;
		QuestionAction questionA = new QuestionAction();
		Question question = new Question();
		//第二步，读取浏览器发送的请求参数
        request.setCharacterEncoding("UTF-8");//解决中文乱码
		title = request.getParameter("title");
		optionA = request.getParameter("optionA");
		optionB = request.getParameter("optionB");
		optionC = request.getParameter("optionC");
		optionD = request.getParameter("optionD");
		answer = request.getParameter("answer");
		id = Integer.parseInt(request.getParameter("id"));
		//第三步，将读取的试题内容，保存到数据库
		question.setTitle(title);
		question.setOptionA(optionA);
		question.setOptionB(optionB);
		question.setOptionC(optionC);
		question.setOptionD(optionD);
		question.setAnswer(answer);
		question.setId(id);
		questionA.update(question);
		request.getRequestDispatcher("QuestionQueryServlet").forward(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
		
	}

}
