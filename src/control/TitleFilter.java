package control;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import mydb.QuestionAction;

/**
 * Servlet Filter implementation class TitleFilter
 */
@WebFilter("/QuestionAddServlet")
public class TitleFilter implements Filter {

    /**
     * Default constructor. 
     */
    public TitleFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		QuestionAction questionA = new QuestionAction();
		//首先对题目做一个判断，并用ajax来处理,这里不返回json来解析了，只返回字符串
		if(isAjaxRequest(request)){
			//ajax判断是否有题目存在
			if(questionA.getAllTitle(request.getParameter("title")).size()>0)
			{
				response.getWriter().write(request.getParameter("title")+":已存在该题目，不允许再次添加");
			}
		}
		else chain.doFilter(request, response);//如果不是ajax请求，直接添加
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	//判断是否为ajax请求
	private boolean isAjaxRequest(ServletRequest request) {
    	HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    	String head = httpServletRequest.getHeader("X-Requested-With");
    	return (head != null && "XMLHttpRequest".equalsIgnoreCase(head));
    }


}
