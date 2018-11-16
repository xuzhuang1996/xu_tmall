# xu_tmall
jsp+servlet模仿天猫,来自http://how2j.cn。

一、数据库部分

1.首先建表，没有外键的表需要先建：User,Category。   、

- 之后，具体设计应该是根据范式来确定，因此不深究。每个类别会有不同的属性，因此property（属性表）需要外键来指向Category。如电视共用5种属性的话，这5种属性的名字都在property存储，根据cid外键来指明这是哪个类别的属性：在属性表中添加外键名为fk_property_category的外键，此外键的参照为分类表的id。Category为主，property为从，这里没有关联增删操作，否则，Category变时property跟着变，但property变时Category不变。    
- product表，一个分类下含多个产品，因此product设置外键指向Category。    
- propertyvalue属性值表，由于该属性值表存储所有的属性的值，此外，该属性是哪个产品的属性的呢，因此有2个外键。    
- productimage表，保存2种图片，详细图片与缩略图。这里并没有将图片保存在数据库？    
- review表包括用户与产品。     
- order_订单表，orderitem订单项表，orderitem，包括用户、产品、在哪个订单里。购物车功能中，每个用户的订单项会显示状态，因此不需要购物车。


2.属性表对应的类的建立   
3.类对应的数据库操作类DAO。

- add函数，需要注意，插入的同时需取出其自增的id，因此使用线程安全的“SELECT LAST_INSERT_ID()”。
- 另外，在根据id获取对象，选择自定义一个对象处理Handler，用来将类的属性不与表的字段相匹配的情况。在list函数中，添加ListHandler类来处理查询多个的情况。对ResultSet rs重新定向，用法与mapgis的Recordset一致。
- （在MySQL被删除的情况下重装，注册表啥的都没了，但还是装不了，显示缺文件。缺C++2015啥的，下载地址为https://www.microsoft.com/en-us/download/details.aspx?id=53587） 。
- （不打算创建service层，即使dao层比较臃肿，时间不够。因此分页查询就使用原来的，不改）

4.开始写servlet，由于是普通Java项目，为了调试我将其转为动态项目。http://how2j.cn/k/servlet/servlet-switch/1346.html 。然后将webroot下的bin放进相应位置，不然servlet没法创建。  

- 新建基类BaseBackServlet，重写了servlet的service方法，有时候也会直接重写service()方法，在其中提供相应的服务，就不用区分到底是get还是post了，没有源码中判断的过程，后面的继承它后直接进入service方法了。理解后写CategoryServlet，根据客户端发送的数据进行处理后，进行页面跳转。之后开始写listCategory.jsp。现在测试：首先浏览器地址为http://localhost:8080/xu_tmall/admin_Category_list ，表示请求categoryservlet的list方法。跳转到servlet处理。处理后将List<Category> cs得到的目录数据传到相应jsp页面。页面就可以显示了。（这里eclipse发布到Tomcat的时候不是以外面的文件夹发布的，而是以当前打开项目中的文件来发布，也就是中途加了一些文件后要及时刷新，不然Tomcat对应的webapps下面找不到相应文件）
- 对于adminPage.jsp这个文件，好好理解分页。上下2个是处理上页下页的按钮，中间那个，adminPage_complete.jsp做了处理，就是页数太多的处理   

5.分类增加。先处理文件上传。Servlet 可以与 HTML form 标签一起使用，来允许用户上传文件到服务器。以下几点需要注意：

- 表单 method 属性应该设置为 POST 方法，不能使用 GET 方法，
- 表单 enctype 属性应该设置为 multipart/form-data.，表示提交的数据是二进制文件
- 表单 action 属性应该设置为在后端服务器上处理文件上传的 Servlet 文件。
- 上传单个文件，您应该使用单个带有属性 type="file" 的 <input .../> 标签。为了允许多个文件上传，请包含多个 name 属性值不同的 input 标签。输入标签具有不同的名称属性的值。浏览器会为每个 input 标签关联一个浏览按钮
利用servlet将文件处理成输入流以后，在add方法中将字段保存数据库，将输入流保存为jpg至本地文件目录。   


**流程就是**     

jsp发送数据action="admin_category_add"，过滤器拦截admin_category_add，request获取method为add，转到分类的servlet下，
执行继承过来的service，执行add方法后返回@admin_category_list，由redirectStartWithCase函数进行地址重定向。转向admin_category_list，又是一波过滤器servlet的处理进入listcategory.jsp
(注意，每一次重定向，之前的request参数就没有了。)

