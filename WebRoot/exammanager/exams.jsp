<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!-- 在页面中使用Taglib指令导入标签库　 -->
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
		<title>Insert title here</title>
		<link href="../style/style.css" rel="stylesheet" type="text/css" />
		<script>
			function getPage(pageIndex,tag){
			    var result = pageIndex+tag;
			    if(result>${requestScope.totalPage} || result<1)return;
			    window.location.href = "/ExamManagement/QuestionQueryServlet?pageIndex="+result;
			}
		</script>

	</head>
	<body style="overflow: scroll; overflow-y: hidden">
		<div>

			<div id="ks">
				<!--ks_top-->
				<div class="main_top">
					<p>
						试题管理
					</p>
				</div>
				<!--ks_bottom-->
				<div class="ks_bottom">

					<!--ti-->
					<div class="ti">
                        <!--指明我们要调用的集合来自哪,-->
                        <!-- 自定义一个var变量，作为每次循环的参数 -->
                        <!-- request 指在一次请求的全过程中有效，即从http请求到服务器处理结束，返回响应的整个过程，存放在HttpServletRequest对象中。在这个过程中可以使用forward方式跳转多个jsp。在这些页面里你都可以使用这个变量。 -->
                        <!-- 接着，将需要动态生成的每道题的html代码贴上去 -->
                        <!-- 接着，在相应地方将question的相关内容添加到对应地方 -->
						<c:forEach items="${requestScope.questionList}" var="question">
						
							<p>
							    <!-- 先拿编号 -->
								<span style="font-weight: bold; color: #296DB8;">${question.id}.${question.title}</span>
	
							</p>
	
							<p>
								<div id="ispan3" align="left" style="padding-left: 36px">
									<span name="option" style="font-weight: bold;">A、${question.optionA}</span>
									<span name="option"
										style="font-weight: bold; padding-left: 120px">B、${question.optionB}</span>
									<span name="option"
										style="font-weight: bold; padding-left: 120px">C、${question.optionC}</span>
									<span name="option"
										style="font-weight: bold; padding-left: 120px">D、${question.optionD}</span>
	
								</div>
							</p>
							<p style="padding-left: 36px">
								(答案是:&nbsp;
								<span id="an3" style="font-weight: bold; color: #F80015;">${question.answer}</span>&nbsp;)&nbsp;&nbsp;&nbsp;[
	
								<a href="./examEdit.html"><span
									style="color: #FF8005;">编辑</span>
								</a>&nbsp;&nbsp;
								<a href = "#" onclick="delbtn()"><span
									style="color: #FF8005;">删除</span>
								</a>]
							</p>
							
						</c:forEach>

					</div>
					
					
					

					<div style="margin-top: 10px">
					<!-- 暂时就不加disable了，<input type="button" value="首页" onclick="getPage(1,0)" disabled> -->
							<input type="button" value="首页" onclick="getPage(1,0)" >
							<input type="button" value="上一页" onclick="getPage(${requestScope.pageIndex},-1)" >
							<input type="button" value="下一页" onclick="getPage(${requestScope.pageIndex},1)">
							<input type="button" value="尾页" onclick="getPage(${requestScope.totalPage},0)">
							<input type="hidden" value="" name="cp">
							<!-- 在这里修改页数信息 -->
							<font color="red">${requestScope.pageIndex}</font> /
							<font color="red">${requestScope.totalPage}</font> 跳转到
							<select name="selPage" onchange="getPage(this.value,0)">
                                <!-- 通过循环来实现页数选择 -->
                                <c:forEach var="i" begin="1" end="${requestScope.totalPage}">
	                                <option value="${i}" selected>${i}</option>
                                </c:forEach>
								
							</select>
					</div>
				</div>

			</div>

		</div>
		</div>
		</div>
	</body>
</html>
