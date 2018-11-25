# xu_tmall
jsp+servlet模仿天猫,来自http://how2j.cn。


# 一、数据库部分

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

# 二、后台部分
&ensp;&ensp;&ensp;&ensp;4.开始写servlet，由于是普通Java项目，为了调试我将其转为动态项目。http://how2j.cn/k/servlet/servlet-switch/1346.html 。然后将webroot下的bin放进相应位置，不然servlet没法创建。  

- 新建基类BaseBackServlet，重写了servlet的service方法，有时候也会直接重写service()方法，在其中提供相应的服务，就不用区分到底是get还是post了，没有源码中判断的过程，后面的继承它后直接进入service方法了。理解后写CategoryServlet，根据客户端发送的数据进行处理后，进行页面跳转。之后开始写listCategory.jsp。现在测试：首先浏览器地址为http://localhost:8080/xu_tmall/admin_Category_list ，表示请求categoryservlet的list方法。跳转到servlet处理。处理后将List<Category> cs得到的目录数据传到相应jsp页面。页面就可以显示了。（这里eclipse发布到Tomcat的时候不是以外面的文件夹发布的，而是以当前打开项目中的文件来发布，也就是中途加了一些文件后要及时刷新，不然Tomcat对应的webapps下面找不到相应文件）
- 对于adminPage.jsp这个文件，好好理解分页。上下2个是处理上页下页的按钮，中间那个，adminPage_complete.jsp做了处理，就是页数太多的处理   

&ensp;&ensp;&ensp;&ensp;5.分类增加。先处理文件上传。Servlet 可以与 HTML form 标签一起使用，来允许用户上传文件到服务器。以下几点需要注意：

- 表单 method 属性应该设置为 POST 方法，不能使用 GET 方法，
- 表单 enctype 属性应该设置为 multipart/form-data.，表示提交的数据是二进制文件
- 表单 action 属性应该设置为在后端服务器上处理文件上传的 Servlet 文件。
- 上传单个文件，您应该使用单个带有属性 type="file" 的 <input .../> 标签。为了允许多个文件上传，请包含多个 name 属性值不同的 input 标签。输入标签具有不同的名称属性的值。浏览器会为每个 input 标签关联一个浏览按钮
利用servlet将文件处理成输入流以后，在add方法中将字段保存数据库，将输入流保存为jpg至本地文件目录。  

**流程就是**
jsp发送数据action="admin_category_add"，过滤器拦截admin_category_add，request获取method为add，转到分类的servlet下，
执行继承过来的service，执行add方法后返回@admin_category_list，由redirectStartWithCase函数进行地址重定向。转向admin_category_list，又是一波过滤器servlet的处理进入listcategory.jsp
(注意，每一次重定向，之前的request参数就没有了。)  
&ensp;&ensp;&ensp;&ensp;6.分类删除。listcategory.jsp里面删除按钮，点击后返回confirm("确认要删除")方法的窗口，再次点击确认删除，自动进入a之前的href，否则不进入（不知道怎么实现的）。此时，过滤器处理，到service方法下，获取delete方法，到CategoryServlet的delete方法下，根据request带过来的参数id执行DAO。（删除之后选择客户端的重定向跳转，如果使用服务端跳转的话，在跳转时候，用户也许会习惯性的F5刷新页面，那么这个时候提交的路径是删除，去删除一条不存在的数据）  
&ensp;&ensp;&ensp;&ensp;7.分类的修改。'改'这个操作，分成了2个函数来完成，一个函数用来进入到对应分类下的edit.jsp，一个函数用于更新。（editcategory.jsp的c.name可以改成requestScope.c.name这样看起来传值明白一点）最后就是，更改图片后谷歌浏览器刷新没反应，按ctrl+f5可以强制刷新，这样就可以看到效果。  
&ensp;&ensp;&ensp;&ensp;8.属性的list。属性跟分类比，多了一个分页的时候按钮的参数。
在进入list.jsp(这里是说所有的东西的list页面)的时候，通过page对象就把所有的页面信息数据传到adminPage.jsp了，所以那个分页按钮才有准确的按钮个数，现在需要在page对象那边传一个参数来确定cid的值，才能准确表示对应的分类下的具体页面的所有属性值。因此页面的这个参数主要是确定按钮的对应地址。  
&ensp;&ensp;&ensp;&ensp;9。属性的增加。需要注意，之前进入list的时候分类页面下有传一个cid，而这里没直接传。我觉得可以request传一个，但是之后重定向。不可。request.getParameter("cid")拿不到值。直接在地址栏加cid就好了。  另外中文乱码问题，在分类中由于是二进制什么的，所以不出现乱码。现在属性没有二进制读取，博主的过滤器不起作用，我的解决办法：name = new String (request.getParameter("name").getBytes ("iso-8859-1"), "UTF-8");为了不经常这样处理了，选择新建一个类XuEncodeUtil工具来处理。因此那个过滤器暂时不要了。  
&ensp;&ensp;&ensp;&ensp;10.属性的删除。  
&ensp;&ensp;&ensp;&ensp;11.属性的编辑。   
&ensp;&ensp;&ensp;&ensp;12.产品的操作。删除的时候，没有cid，需要从当前产品的id查找产品对应的分类cid。   
&ensp;&ensp;&ensp;&ensp;13.产品图片的操作。这里图片没有存数据库，而是在文件夹中，产品图片的对象中存了id跟类型，以及属于哪个产品。然后jsp展示的时候在相应文件夹取id的图片。（调试的时候如果想看request的参数设置，在coyoteRequest里面的参数，点击这个参数的英文就能出现具体）。另外，现在才发现。之前图片DAO，第一张图片没有设置，导致产品管理里面的图片没有。后来在Handler里面设置了，因此DAO自带的setFirstProductImage没用了。  
&ensp;&ensp;&ensp;&ensp;14对于产品属性值的设置。主要是初始化的问题，propertyValueDAO.init(p);如果新增一个产品。不使用这个函数的话，在数据库中查找根据产品pid来查propertyValue中值，会发现是空的。接着在这个产品下去设置属性，进入edit.jsp页面，会发现是空的，但此时数据库中却出现了属性值对应的行项（进行了初始化）。接着再进去，页面就有属性与属性框了。于是我将初始化写进了新增的函数里，每次新增一个产品，就给他初始化属性与属性值。另外需要注意的是，删除的时候，由于有的产品有图片，不能删除，只能等下级图片删除后才能删除。最后就是ajax乱码问题。确认编码过滤器没屁用。ajax反而不需要工具类的转换。直接就是这个值。
   
**Servlet怎么写呢。首先写list，怎么写呢，看哪个页面能进到这个list.jsp里面，需要哪些参数，由servlet提供的。确定好参数后在list方法中提供参数后访问list.jsp。之后写add，然后是delete。最后先写edit进到edit.jsp页面，在这个页面提交数据后在update里接受进行更新。**    
**完整版的分页怎么做的，主要是中间那个分页，前后先不管。当点击一页的时候，对其周围的页数进行判断，符合条件的就做按钮出来。然后对当前页设置disable，以及current样式**  
![image text](https://github.com/xuzhuang1996/xu_tmall/blob/master/img_git/page.PNG)
      

# 三、前后台部分
&ensp;&ensp;&ensp;&ensp;15
