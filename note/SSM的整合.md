思路主要是先搭建Spring的配置环境,然后在通过spring整合SpringMVC框架,最后再整合MyBatis. 文中完整的demo点我(包含整合必要的jar和xml配置等)

##### 搭建Spring的开发环境
1 . 创建个基于maven的web骨架工程.
2. 创建一个source Root的java文件夹和resources Root的resources文件夹.
3.  在pom.xml中引入工程必要的jar包.jar包的完整坐标可参考文首demo中的pom.xml文件.(
4. 编写dao,domain以及service层.
5. 搭建和测试Spring的开发环境.
在resources下新建一个applicationContext.xml的配置文件，编写具体的配置信息.
重点是在配置注解扫描时, 由于controller部分是基于springMVC的框架来管理的,所以要注意忽略掉web层的注解.同理在springMVC的注解扫描配置时依然需要注意只扫描web层.

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xmlns:tx="http://www.springframework.org/schema/tx"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd
    http://www.springframework.org/schema/aop
    http://www.springframework.org/schema/aop/spring-aop.xsd
      http://www.springframework.org/schema/tx
    http://www.springframework.org/schema/tx/spring-tx.xsd">
// 开启注解扫描，要扫描的是service和dao层的注解，要忽略web层注解，因为web层让SpringMVC框架 去管理 
<context:component-scan base-package="cn.itcast"> 
// 配置要忽略的注解 
<context:exclude-filter type="annotation"
expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
</beans>


```

##### Spring整合SpringMVC框架
1. 在web.xml中配置DispatcherServlet前端控制器. 

```java
// 配置前端控制器:服务器启动必须加载，需要加载springmvc.xml配置文件 
<servlet>
<servlet-name>dispatcherServlet</servlet-name> <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class> 
// 配置初始化参数，创建完DispatcherServlet对象，加载springmvc.xml配置文件 
 <init-param>
  <param-name>contextConfigLocation</param-name>
        <param-value>classpath:springmvc.xml</param-value>
  </init-param>
//  服务器启动的时候，让DispatcherServlet对象创建 
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>

```
2.  创建springmvc.xml的配置文件，编写配置文件.

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
// 扫描controller的注解，别的不扫描 
 <context:component-scan base-package="cn.itcast">
        <context:include-filter type="annotation"
expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
//  配置视图解析器 
<bean id="viewResolver"class="org.springframework.web.servlet.view.InternalResourceViewResolver"> 
// JSP文件所在的目录
<property name="prefix" value="/WEB-INF/pages/" /> 
// 文件的后缀名
<property name="suffix" value=".jsp" />
</bean>
// 设置静态资源不过滤 
<mvc:resources location="/css/" mapping="/css/**" /> <mvc:resources location="/images/" mapping="/images/**" /> <mvc:resources location="/js/" mapping="/js/**" />
// 开启对SpringMVC注解的支持
 <mvc:annotation-driven />
</beans>
  
```

3. spring整合springMVC的框架
整合的目的主要是可以成功在controller中调用service层的方法,  只需要在项目启动的时候去加载spring的配置文件applicationContext.xml, 可以通过监听器监听servletContext的创建和销毁来实现.在web.xml中配置ContextLoaderListener监听器, 默认只会加载WEB-INF目录下的applicationContext.xml配置文件,因此需要设置applicationContext.xml的路径.

```java
<listener>
  <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
// 设置applicationContext.xml的路径.便于监听器加载
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:applicationContext.xml</param-value>
</context-param>

```
至此spring整合springMVC完成, 可以在web层的controller中通过注解注入的方式获取service容器. web层可以成功调用service层业务接口.  例如:

```java
// web
@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @RequestMapping("/findAll")
    public String findAll() {
        System.out.println("表现层方法调用");
        accountService.findAll();
        accountService.savaAccount(new Account());
        return "success";
    }
}

```

##### Spring整合MyBatis框架
1. 在resources编写MapConfig.xml的配置文件, 编写核心配置文件.
2. 在AccountDao接口的方法上通过注解的方式编写sql语句.
3. 编写测试的方法.
4. 在applicationConfig中配置mybatis相关内容,连接池,sqlsession工厂等.也就是把SqlMapConfig.xml配置文件中的内容配置到applicationContext.xml配置文件中.

```java
 <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="com.mysql.jdbc.Driver"/>
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/ssm?characterEncoding=utf8"/>
        <property name="user" value="root"/>
        <property name="password" value="12345678"/>
    </bean>
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
     <bean id="mapperScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.mine.dao"/>
 </bean>
 
```
5. 在applicationConfig完善声明式事务管理.
至此, SSM整合完成.
在整合的过程,一定要注意jar的版本问题,否则,极易引起一些未知错误.