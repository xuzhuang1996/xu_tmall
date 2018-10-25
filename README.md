# ExamManagement
servlet版考试管理系统
来自B站：https://www.bilibili.com/video/av24740747/?p=12

1.实现试题的增删，所需表：试题信息表.新增questionAddServlet类处理相应HTML中表单提交的数据。并存储与数据库。（此过程需要对表单进行处理)。这里是按钮submit自动跳转到servlet。（要执行servlet必须通过访问的方式使用）

2.实现试题的查询，在QuestionQueryServlet类中从数据库获得查询的List对象后，将对象再次请求转发到对应jsp页面中。对相应jsp页面进行接收list对象处理。（这里我人为将试题管理中对应的exams.html改成QuestionQueryServlet的路径，才能顺利操作到它），分页处理，在原始查询上增加special参数。在QuestionQueryServlet中得到分页所需的总页数，通过request进行请求。接着在前台进行代码处理，包括总页当前页的数字，首页尾页的跳转，

3.中途更改代码，可以自定义连接池：https://www.bilibili.com/video/av20052105/?p=6。 我选择c3p0.在xml中编写时需注意将&转义为‘&-a-m-p-;’（将-去掉，这个编辑器好像也将其转义了）    再就是拼写问题，将myelipse里面的spelling关闭就不会检查拼写问题了。。。。此外在重新使用数据库连接池后，依然遇到连接过多的问题。原因是GetC3p0DB.getConnection();只在初始化赋值，在关闭判断操作中未处理。在处理后，无论怎样连接都没问题了。

4.给题目增加ajax，判断数据库中是否已存在该题目。在title的textarea下增加onchange事件，当textarea改变后，执行。将数据用过滤器来过滤，如果是ajax则只进行判断是否存在，如果不是ajax请求，就继续下去。这里跟submit不同，submit是将所有name属性值下的数据提交，而ajax只是将我需要的title数据提交。提交后数据库查询，然后返回处理。BUG：ajax那块，由于发送给过滤器的X-Requested-With值一直为空（不知道为啥），选择在xmlHttp的open函数之后添加这句XmlHttp.setRequestHeader("X-Requested-With", "XMLHttpRequest");。人为给这个请求对象添加请求头数据，用于过滤器的判断。

5.试题删除，由于之前在试题查询那边，试题的显示中，试题的ID就是题号，因此这里删除可以直接根据ID来删除。

需要改进：

1.Question类是试题表，不应该放在myDB包下，应该放在myDB包下的实体entity包中，

2./ExamManagement/QuestionQueryServlet这样的地址，在web.xml里面应该为自定义的，而不是跟servlet同名，建议比如试题用成/ExamManagement/question/QuestionQueryServlet,用question来分别。


