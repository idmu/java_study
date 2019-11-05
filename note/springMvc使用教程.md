##### 环境搭建与配置

1. 创建一个基于web骨架的maven项目.若maven工程创建过慢,则可以通过创建页面的Properties配置```archetypeCatalog:internal```键值来优化.
2.  在main目录下添加java和resources文件夹, 属性分别设置为```Sources root```和```Resouces root```
3. 在pom文件中引入开发的jar包的坐标.

```java
    
//  版本锁定 
 <properties>
    <spring.version>5.0.2.RELEASE</spring.version>
</properties>


<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-web</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>servlet-api</artifactId>
        <version>2.5</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>javax.servlet.jsp</groupId>
        <artifactId>jsp-api</artifactId>
        <version>2.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>

```
4 . 配置tomcat服务器, 并在web.xml中配置前端控制器

```java
<web-app>
  <display-name>Archetype Created Web Application</display-name>

  <servlet>
    <servlet-name>dispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    // 配置Servlet的初始化参数，读取springmvc的配置文件，创建spring容器
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:springmvc.xml</param-value>
    </init-param>
    // 配置servlet启动时加载对象
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>
</web-app>


```
5 . 在resources下创建一个springmvc.xml的配置文件.

 

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    // 开启注解扫描
    <context:component-scan base-package="com.mine"/>

	// 视图解析器
    <bean id="internalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/pages/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

    // 开启springmvc的注解支持
    <mvc:annotation-driven/>
</beans>


```

##### SpringMVC的执行实现

* SpringMVC请求的执行流程
1)当启动Tomcat服务器的时候，因为配置了load-on-startup标签，所以会创建DispatcherServlet对象， 就会加载springmvc.xml配置文件.其中dispatcherServlet表示前端控制器. 用户请求到达前端控制器, 相当于mvc 模式中的c模块, 是整个流程控制的中心，由它调用其它组件处理用户的请求，极大程度的降低了组件之间的耦合性。
2)开启了注解扫描，那么HelloController对象就会被创建.
3)从index.jsp发送请求，请求会先到达DispatcherServlet核心控制器，根据配置@RequestMapping注解,找到执行的具体方法.
4)根据执行方法的返回值，再根据配置的视图解析器，去指定的目录下查找指定名称的JSP文件.
5)Tomcat服务器渲染页面，做出响应.
 处理器映射器

* RequestMapping标签
RequestMapping注解的作用是建立请求URL和处理方法之间的对应关系.RequestMapping注解可以作用在方法和类上.
1) 作用在类上表示存在第一级的访问目录
2) 作用在方法上表示第二级的访问目录
RequestMapping的属性:
a) path: 指定请求的路径url
b) value: value与path的属性表达含义一致
c) mthod:指定该方法的请求方式
d) params:指定请求参数的限制条件
e) headers:表示发送的请求中必须包含的请求头

```java
@Controller
@RequestMapping(path = "/hello")
public class HelloController {
    @RequestMapping(path = "/hello")
    public String sayHello() {
        System.out.println("hello mvc!");
        return  "success";
    }

    @RequestMapping(path = "/getUser")
    public void getUser() {
        System.out.println("获取了账号信息");
    }
}

```
* 请求参数的绑定
表单提交的数据都是k=v格式的:如" username=root&password=123";  SpringMVC的参数绑定过程就是把表单提交的请求参数，作为控制器中方法的参数进行绑定. 要求提交表单的name和参数的名称是相同的.如果提交的数据类型是JavaBean类型, 提交表单的name则需要JavaBean中的属性名称需要一致. 如果一个JavaBean类中包含其他的引用类型,那么表单的name属性需要编写成:对象.属性, 例如: user.name.
如果是集合属性数据封装,在jsp页面表示成" list[0].属性"的样式即可.
##### 常用注解
* RequestParam
RequestParam作用是把请求中的指定名称的参数传递给控制器中的形参赋值.
属性value:请求参数中的名称; 属性required表请求参数中是否必须提供此参数，默认值是true，必须提供.

```java
@RequestMapping(path="/hello")
// 表示页面提交过来的参数必须是usernmae
public String sayHello(@RequestParam(value="username")String name) {
    System.out.println(name);
    return "success";
}
```
* RequestBody
作用是用于获取请求体的内容(注意:get方法不可以).包含一个required属性用来表示是否必须有请求体，默认值是true.当取值为true时,get请求会报错,当值为false时,get请求结果是null.

```java
@RequestMapping(path="/hello")
public String sayHello(@RequestBody String body) {
    System.out.println(body);
    return "success";
}
```
* PathVariable
用于绑定url中的占位符. 例如:url中有/delete/{id}，{id}就是占位符.

```java
 <a href="user/hello/1">测试PathVariable</a>
/**
* 接收请求 * @return */
@RequestMapping(path="/hello/{sid}")
public String sayHello(@PathVariable(value="sid") String id) {
    System.out.println(id);
    return "success";
}

```
*  RequestHeader
获取指定请求头的值.

```java
@RequestMapping(path="/hello")
    public String sayHello(@RequestHeader(value="Accept") String header) {
        System.out.println(header);
        return "success";
    }
```
*  CookieValue
用于获取指定cookie的名称的值

```java
 @RequestMapping(path="/hello")
public String sayHello(@CookieValue(value="JSESSIONID") String cookieValue) {
    System.out.println(cookieValue);
    return "success";
}

```
* ModelAttribute
用在方法上, 表示当前方法会在控制器方法执行前线执行; 用在参数上, 表示获取指定的数据给参数赋值.
* SessionAttributes
表示多次执行控制器方法间的参数共享.

```java
@Controller
@RequestMapping(path="/user")
@SessionAttributes(value= {"username","password","age"},types=
{String.class,Integer.class})
public class HelloController {
/**
* 向session中存入值 * @return
*/
// 把数据存入到session域对象中
@RequestMapping(path="/save")
public String save(Model model) {
System.out.println("向session域中保存数据"); model.addAttribute("username", "root"); model.addAttribute("password", "123"); model.addAttribute("age", 20);
    return "success";
}
/**
* 从session中获取值 * @return
*/
@RequestMapping(path="/find")
public String find(ModelMap modelMap) {
    String username = (String) modelMap.get("username");
    String password = (String) modelMap.get("password");
    Integer age = (Integer) modelMap.get("age");
    System.out.println(username + " : "+password +" : "+age);
    return "success";
}
/**
* 清除值
* @return */
@RequestMapping(path="/delete")
public String delete(SessionStatus status) {
		status.setComplete();
        return "success";
    }
}
  
```

##### 响应返回值类型
* 返回值为字符串
Controller方法返回字符串可以指定逻辑视图的名称，底层会根据视图解析器为物理视图的地址.

```java
//  控制器方法页面, 通过model方法设置信息到域中
@Controller
@RequestMapping("/hello")
public class HelloController {
    @RequestMapping("/sayHello")
    public String sayHello(Model model) {
        System.out.println("访问了hello方法");
        User user = new User();
        user.setUsername("李白");
        user.setPassword("234");
        user.setAge(15);
        model.addAttribute("user",user);
        return "success";
    }
}
// 页面上通过${ xxxx } 的方法获取字符串内容.
<form action="/hello/sayHello" >
    账号: <input type="text" name="username" value="${user.username}"><br>
    密码: <input type="text" name="password" value="${user.password}"><br>
    年龄: <input type="text" name="age" value="${user.age}"><br>
    <input type="submit" value="提交">
</form>

// isELIgnored="false
```
* 返回值是void
如果返回值是void, 默认会跳转到@RequestMapping(value="/xxxx") 设置的页面.
具体可以使用请求转发或者重定向跳转到指定的页面.

```java
<a href="/getUser">测试返回是void</a>
@RequestMapping("/getUser")
    public void getUser() {
        System.out.println("getuser方法执行了");
    }
// 由于返回值为void,所以默认会跳转至http://localhost:8080/getUser.jsp页面.
// 而getUser.jsp页面是不存在.

```
重定向或者消息转发来处理:

```java
@RequestMapping("/getUser")
    public void getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("getuser方法执行了");
        // 手动转发需要完整路径
        // request.getRequestDispatcher("/WEB-INF/pages/success.jsp").forward(request,response);
        // 重定向
        //  response.sendRedirect(request.getContextPath() + "/index.jsp");
        // 直接响应数据
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("你好");
        return;
    }
    
```

* 返回值是ModelAndView对象
ModelAndView对象是Spring提供的一个对象，可以用来调整具体的JSP视图.
提供了```addObject```和```setViewName```两个方法,其中addObject表示将对象加入到request的域中,setViewName表示跳转的页面.

```java
// 控制器中
@RequestMapping(value="/getUser")
public ModelAndView getUser() throws Exception {
	ModelAndView mv = new ModelAndView(); 
	User user = new Usr();
	user.setUsername("赵四");
    user.setPassword("456");
  	mv.addObject("users", user);
  	mv.setViewName("success");
	return mv;
// 在jsp页面通过 ${ user.username }方法获取.
<body>
<h3>查询所有的数据</h3>
<c:forEach items="${ users }" var="user">
        ${ user.username }
    </c:forEach>
</body>


```
* 基于框架关键字的方式实现转发和重定向
1)forward请求转发,  "forward:转发的JSP路径"，不走视图解析器了，所以需要编写完整的路径. ```return "forward:/WEB-INF/pages/success.jsp"; ```
2)redirect重定向. ```return "redirect:/add.jsp";```

```java
@RequestMapping("/delete")
public String delete() throws Exception {
	System.out.println("delete方法执行了...");
	// 完整路径
	// return "forward:/WEB-INF/pages/success.jsp"; 
	// 重定向不需要在加项目名称
	return "redirect:/add.jsp";
}

```

##### 简单应用
* 文件上传
1. 传统的文件上传方式:
a) 需要导入文件上传的jar包

```java
<dependency>
        <groupId>commons-fileupload</groupId>
        <artifactId>commons-fileupload</artifactId>
        <version>1.3.1</version>
    </dependency>
    <dependency>
        <groupId>commons-io</groupId>
        <artifactId>commons-io</artifactId>
        <version>2.4</version> 
</dependency>
```
b) 具体实现

```java
@RequestMapping("/upload")
    public String uploadFile(HttpServletRequest request) throws Exception {
        System.out.println("文件上传..");
        // 获取到需要上传文件的路径
        String realPath = request.getSession().getServletContext().getRealPath("/uploads/");
        // 获取file路径,向路径上传文件
        File file = new File(realPath);
        // 判断路径是否存在
        if (!file.exists()) {
            file.mkdirs();
        }
        // 创建磁盘文件工厂方法
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload fileUpload = new ServletFileUpload(factory);
        // 解析request对象
        List<FileItem> list = fileUpload.parseRequest(request);
        // 遍历
        for (FileItem item : list) {
            // 判断是普通字段还是文件上传
            if (item.isFormField()) {
            } else {
                // 获取到上传文件的名称
                String fileName = item.getName();
                String uuid = UUID.randomUUID().toString().replace("-","");
                fileName = uuid + "_" + fileName;
                // 上传文件
                item.write(new File(file, fileName));
                // 删除临时文件
                item.delete();
            }
        }
        return "success";
    }
}

```
2.基于SpringMVC框架的上传方式 
SpringMVC框架提供了MultipartFile对象,该对象表示上传的文件,要求变量名称必须和表单file标签的name属性名称相同.
a)在springmvc.xml 配置文件解析器对象，要求id名称必须是multipartResolver.

```java
// 配置文件解析器
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
    // 设置上传文件的大小
    <property name="maxUploadSize" value="10485760"></property>
    </bean>
```
b) 上传代码实现

```java
@RequestMapping("/uploadFileBySpring")
    // 要求MultipartFile对象变量名称必须和表单file标签的name属性名称相同。
    public String uploadFileBySpring(HttpServletRequest request, MultipartFile upload) throws Exception {
        System.out.println("springmvc的文件上传");
        String path = request.getSession().getServletContext().getRealPath("/mvcUpload");
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 通过upload获取文件的名字
        String filename = upload.getOriginalFilename();
        // 设置文件名为唯一值,拼接uuid
        String uuid = UUID.randomUUID().toString().replace("-", "");
        filename = uuid + "_" + filename;
        upload.transferTo(new File(path,filename));
        return "success"; 
    }
    
```
3. SpringMVC跨服务器的文件上传
a) 导入开发需要的jar包

```java
 
<dependency>
    <groupId>com.sun.jersey</groupId>
    <artifactId>jersey-core</artifactId>
    <version>1.18.1</version>
</dependency>
<dependency>
    <groupId>com.sun.jersey</groupId>
    <artifactId>jersey-client</artifactId>
    <version>1.18.1</version>
</dependency>

```
b)具体代码实现

```java
 public String uploadOtherServlet(HttpServletRequest request, MultipartFile upload) throws Exception {
        // 获取上传的文件的路径
        String path = "http://localhost:8080/uploads/";
        String filename = upload.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        filename = uuid + "_" + filename;
        // 创建客户端的连接
        Client client = Client.create();
        // 和图片服务器进行连接
        WebResource webResource = client.resource(path + filename);
        // 上传
        webResource.put(upload.getBytes());
        return "success";
    }
    
```

##### SpringMVC的异常处理
一般情况下,客户端浏览器发送请求到前端控制器,依次调用web,service和dao层.正常的异常处理是依次将异常向上抛出.如果整个过程都没有异常处理,异常则会依次抛出直到显示在浏览器.这样的话最终有可能会在页面展示非常不友好的异常错误信息.所以通常需要通过自定义来处理异常.
1 .  自定义异常类

```java
// 自定义异常类
public class SysException extends Exception {
    // 存储提示信息
    private String message;
    @Override
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public SysException(String message) {
        this.message = message;
    }
}

```
2. 自定义异常处理器
```java
// 异常处理器
public class SysExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        // 获取异常信息
         SysException exception = null;
        if (e instanceof SysException) {
            exception = (SysException) e;
        } else {
            exception = new SysException("系统正在升级, 请于...在来访问");
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("errorMsg",exception.getMessage());
        mv.setViewName("error");
        return mv;
    }
    
```
3. 在springmvc中配置异常处理器

```java
  // 配置异常处理器
    <bean id="exceptionResolver" class="com.mine.exception.SysExceptionResolver"/>
    
```
经过自定义异常类, 在开发中,可以直接抛出自定义的异常.

```java
// 异常处理类
    @RequestMapping("/exceptionTest")
    public String exceptionTest() throws SysException {
        System.out.println("异常处理测试");
        try {
            int i = 110 / 0;
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            throw new SysException("出现异常了,自定义异常友好界面抛出");
        }
        return "success";
    }
    
    
```

##### 拦截器
* 拦截器介绍
拦截器用于对处理器进行预处理和后处理.自定义拦截器,需要```实现HandlerInterceptor接口```.
拦截器接口提供了三个方法:
1)```preHandle```方法是controller方法执行前拦截的方法;
其中可以使用request或者response跳转到指定的页面.返回值为true,表示放行, 执行下一个拦截器, 如果没有拦截器, 执行controller中的方法.返回值为false,表示不放行, 不会执行controller中的方法.
2)```postHandle```是controller方法执行后执行的方法, 在JSP视图执行前;可以使用request或者response跳转到指定的页面. 如果指定了跳转的页面，那么controller方法跳转的页面将不会显示.
3)```afterCompletion```方法是在JSP执行后执行; request或者response不能再跳转页面了
拦截器和过滤器的功能比较类似, 区别主要在于:①过滤器是Servlet规范的一部分,任何框架都可以使用过滤器技术; 拦截器是SpringMVC框架独有的.②过滤器配置了/*,可以拦截任何资源;拦截器只会对控制器中的方法进行拦截.
可以使用request或者response跳转到指定的页面.
* 自定义拦截器
 1.  自定义拦截器类，实现HandlerInterceptor接口，重写需要的方法.

```java
// 自定义拦截器，需要实现HandlerInterceptor接口
public class MineInterceptor implements HandlerInterceptor {
    @Override
    // 预处理, controller执行之前
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 返回ture表示放行,不拦截
        // 返回false 表示拦截
        System.out.println("preInterceptor执行了----之前");
//        request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request,response);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("interceptor执行---之后");
        // 指定页面后跳转至指定的页面, 而不会再跳控制器中指定的页面了
        request.getRequestDispatcher("/WEB-INF/pages/response.jsp").forward(request,response);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("interceptor----after");
        // 不能再跳转了
    }
    
```
2. 在springmvc.xml中配置拦截器, 多个拦截器配置只需要添加多个```</mvc:interceptor>```即可.

```java
// 配置拦截器
    <mvc:interceptors>
        <mvc:interceptor>
    // mapping表示要拦截的方法, 方法可以调整匹配关系
            <mvc:mapping path="/hello/*"/>
     // mapping表示不需要拦截的方法,mapping与exclude-mapping选一个就可以
    /* <mvc:exclude-mapping path=""/> */
           // 注册拦截器对象 
            <bean class="com.mine.interceptor.MineInterceptor"/>
        </mvc:interceptor>
    </mvc:interceptors>
    
```

 

##### 易忽略问题
* js被拦截
DispatcherServlet会拦截到所有的资源，导致一个问题就是静态资源(img、css、js)也会被拦截到，从而不能被使用. 解决问题就是需要配置静态资源不进行拦截，在springmvc.xml配置文件添加如下配置 :
在mvc:resources标签配置不过滤
其中location元素表示webapp目录下的包下的所有文件;  mapping元素表示以/static开头的所有请求路径，如/static/a 或者/static/a/b.

```java
 
// 设置静态资源不过滤 
<mvc:resources location="/css/" mapping="/css/**"/> 
<mvc:resources location="/images/" mapping="/images/**"/> 
<mvc:resources location="/js/" mapping="/js/**"/> 

```
*  请求参数中文乱码的解决
在web.xml中配置Spring提供的过滤器类

```java
 
	// 配置过滤器，解决中文乱码的问题 
        <filter-name>characterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-
class>
	// 指定字符集 
		<init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>characterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    
```
* 自定义类型转换器
springMVC框架本身提供了许多的类型转换方法,但个别情况下依然需要自定义来处理类型的异常. 自定义转换器需要继承Converter;同时需要在xml中配置自定义转换器,配置类型转换器生效.具体参照实例:

```java

  // 把字符串转换成日期
    public class StringToDateConverer implements Converter<String, Date> {
        @Override
        public Date convert(String source) {
            if (source == null) {
                throw new RuntimeException("请传入数据.");
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = df.parse(source);
                return date;
            } catch (Exception e) {
                throw new RuntimeException("字符串转日期转换错误");
            }
        }
    }
    
```
自定义转换器配置

```java
 
// 注册自定义类型转换器 
    <bean id="conversionService" class="org.springframework.context.support.ConversionServiceFactoryBean">
        <property name="converters">
            <set>
                <bean class="cn.com.utils.StringToDateConverter"/>
            </set>
        </property>
    </bean>
// 开启Spring对MVC注解的支持 
<mvc:annotation-driven conversion-service="conversionService"/>

```
* 使用原生的ServletAPI
在控制器中使用原生的ServletAPI对象, 只需要在控制器的对应方法参数定义HttpServletRequest和HttpServletResponse对象即可.