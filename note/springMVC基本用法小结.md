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

##### 响应数据和结果
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