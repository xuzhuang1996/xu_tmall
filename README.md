# ExamManagement
servlet版考试管理系统
来自B站：https://www.bilibili.com/video/av24740747/?p=12

1.实现试题的增删，所需表：试题信息表.新增questionAddServlet类处理相应HTML中表单提交的数据。并存储与数据库。（此过程需要对表单进行处理)。这里是按钮submit自动跳转到servlet。（要执行servlet必须通过访问的方式使用）

2.实现试题的查询，在QuestionQueryServlet类中从数据库获得查询的List对象后，将对象再次请求转发到对应jsp页面中。对相应jsp页面进行接收list对象处理。（这里我人为将试题管理中对应的exams.html改成QuestionQueryServlet的路径，才能顺利操作到它），分页处理，在原始查询上增加special参数。在QuestionQueryServlet中得到分页所需的总页数，通过request进行请求。接着在前台进行代码处理，包括总页当前页的数字，首页尾页的跳转，

需要改进：

1.Question类是试题表，不应该放在myDB包下，应该放在myDB包下的实体entity包中，

2./ExamManagement/QuestionQueryServlet这样的地址，在web.xml里面应该为自定义的，而不是跟servlet同名
