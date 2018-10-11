# ExamManagement
servlet版考试管理系统
来自B站：https://www.bilibili.com/video/av24740747/?p=12

1.实现试题的增删，所需表：试题信息表.新增questionAddServlet类处理相应HTML中表单提交的数据。并存储与数据库。（此过程需要对表单进行处理)

2.实现试题的查询，在QuestionQueryServlet类中从数据库获得查询的List对象后，将对象再次请求转发到对应jsp页面中。对相应jsp页面进行接收list对象处理。

需要改进：

1.Question类是试题表，不应该放在myDB包下，应该放在myDB包下的实体entity包中，
