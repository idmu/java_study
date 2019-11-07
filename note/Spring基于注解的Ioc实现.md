
####  Spring使用之基于注解的Ioc配置



Spring的注解需要在配置文件中指定需要扫描的包.具体配置参照:

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">
    <!--告知spring在创建容器时候需要扫描的包, 配置所需要的标签不是在bean约束中,而是在一个名为context的名称空间和约束中.-->
    <context:component-scan base-package="com.mine"></context:component-scan>
</beans>


```

##### 常用注解
* @Component
 @Component作用是把资源让 spring 来管理. 相当于在 xml 中配置一个 bean. 它有一个value属性, 通常用来指定 bean 的 id. 如果不指定 value 属性，默认 bean 的 id 是当前类的类名, 首字母小写.

```java
// 在需要创建对象的类中添加注解关键字@Component
@Component
public class AccountServiceImpl implements IAccountService {
    public AccountServiceImpl () {
        System.out.println("创建了对象");
    }
}
// 在测试文件中, 如果@Component注解未指定value值,则默认id为当前类名,首字母小写.即下面的"accountServiceImpl".
public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext cls = new ClassPathXmlApplicationContext("bean.xml");
        IAccountService accountService = (IAccountService) cls.getBean("accountServiceImpl");
        System.out.println(accountService);
    }
}

```
* @Controller @Service @Repository
这三个注解的作用跟@Component 一模一样,区别主要在于:Controller: 一般用于表现层的注解;  @Service: 一般用于业务层的注解; @Repository: 一般用于持久层的注解.
* @Autowired
 @Autowired会自动按照类型注入. 当使用注解注入属性时, set 方法可以省略. 只要容器中有唯一一个bean对象类型和要注入的变量类型匹配, 就可以注入成功. 它只能注入其他 bean 类型, 当有多个 类型匹配时，使用要注入的对象变量名称作为 bean 的 id，在 spring 容器查找，找到了也可以注入成功,找不到 就报错.@Autowired注解的位置大多用在变量和方法上,当然不限于此.

```java
@Service("accountService")
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private IAccountDao dao;
    public void saveAccout() {
        dao.saveAccount();
    }
}

```
* @Qualifier
在自动按照类型注入的基础之上,再按照名称(Bean的id )注入。它在给类成员注入时不能独立使用，必须和@Autowire 一起使用; 但是给方法参数注入时，可以独立使用.
属性: 有一个属性value,用于指定注入的bean 的 id.
在给类成员注入时,必须要和@Autowired组合使用.
* @Resource
直接按照 Bean的 id 注入, 可以独立使用. 它也只能注入其他 bean 类型.
* @Value
用来注入基本数据类型和 String 类型数据的.它有一个value属性,用来指定数据的值,它可以使用spring中的SpEL表达式. <SpEL表达式的写法: ${}>
* 说明: @Autowired,@Qualifier和@Resource三个都只能注入其他的bean类型的数据,而基本数据类型和string类型只能通过@Value来实现.
* @Scope
用来指定bean的作用范围.有一个value属性值用来指定范围的值。
取值包括:singleton prototype request session globalsession

##### 新注解
* @ComponentScan
用于指定 spring 在初始化容器时要扫描的包.作用和在 spring 的 xml 配置文件中的:<context:component-scan base-package="com.mine"/>是一样的.其中的basePackages属性用于指定要扫描的包,和该注解中的 value 属性作用一样.
* @Bean
Bean注解只能写在方法上, 表明使用此方法创建一个对象,并且放入 spring 容器.
属性name表示给当前@Bean 注解方法创建的对象指定一个名称(即 bean 的 id).
实例:需要创建一个SpringConfiguration的类来配置注解相关信息
*  @Import
用于导入其他配置类, 在引入其他配置类时, 可以不用再写@Configuration 注解, 当然,写上也不影响.
* @PropertySource
用于加载.properties 文件中的配置. 例如我们配置数据源时，可以把连接数据库的信息写到 properties 配置文件中，就可以使用此注解指定 properties 配置文件的位置. 属性value[]:用于指定 properties 文件位置。如果是在类路径下，需要写上 classpath.
如:
存在一个jdbcConfig.properties的配置文件

```java
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/jdbcDemo
jdbc.user=root
jdbc.password=12345678
```
在配置文件中:
```java
@Configuration
@ComponentScan("com.mine")
@PropertySource("jdbcConfig.properties")
public class SpringConfiguration {
    @Value("${jdbc.driver}")
    private String driver;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.user}")
    private String user;

    @Value("${jdbc.password}")
    private String password;

    /**
     * 创建一个QueryRunner对象
     * @param dataSource
     * @return
     */
    @Bean(name = "runner")
    @Scope("prototype")
    public QueryRunner createQueryRunner (DataSource dataSource) {
        return new QueryRunner(dataSource);
    }

    @Bean(name ="dataSource")
    public DataSource createDataSource() {
        try {
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(driver);
            dataSource.setJdbcUrl(url);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            return dataSource;
        } catch (Exception e){
            throw new RuntimeException();
        }
    }
 }
 
```

同时在测试模块需要修改读取注解的方法:

```java
ApplicationContext cls = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        
```

##### Junit的集成
* 分析
在测试类中，每个测试方法都有以下两行代码:

```java
ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml"); 
IAccountService as = ac.getBean("accountService",IAccountService.class);
 
```
这两行代码的作用是获取容器，如果不写的话，直接会提示空指针异常.所以又不能轻易删掉.
* 为什么不把测试类配到 xml 中?
配置到xml当然可以正常使用, 只是因为:
①当我们在 xml 中配置了一个 bean，spring 加载配置文件创建容器时，就会创建对象.
②测试类只是我们在测试功能时使用，而在项目中它并不参与程序逻辑，也不会解决需求上的问题，所以创建完了，并没有使用. 那么存在容器中就会造成资源的浪费. 所以，我们不应该把测试配置到 xml 文件中.

* 集成
1) 导入spring整合junit的jar包(坐标)

```java
<dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.0.7.RELEASE</version> 
</dependency>
```
2)使用junit提供的一个main方法把原有的main方法替换掉, 替换成spring提供的@Runwith. 

```java
@RunWith(SpringJUnit4ClassRunner.class)
```
之所以需要替换,是以为junit单元测试中, 没有main方法也能执行, junit本身集成了一个main方法, 该方法会判断当前测试类中哪些方法包含@Test的注解,junit就会让这些带有@Test的方法执行. 而junit本身并不会关注我们的程序是否集成了spring,所以在执行测试方法时候, 它也不会为我们读取配置文件或配置类来为我们创建容器.查看源码可知, SpringJUnit4ClassRunner其本身也是继承了junit. 虽然junit不会为我们创建容器,由于SpringJUnit4ClassRunner是spring提供,所以它一定会为我们创建容器.

3)告知spring的运行期,spring的Ioc创建是基于xml还是注解.并且通过@ContextConfiguration 注解来说明位置.
@ContextConfiguration 注解:
其中,
locations 属性用于指定配置文件的位置. 如果是类路径下，需要用 classpath表明.

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:bean.xml"})
public class AccountServiceTest {
}

```
 classes 属性用于指定注解的类, 当不使用 xml 配置时，需要用此属性指定注解类的位置.

```java
 @RunWith(SpringJUnit4ClassRunner.class)
 @ContextConfiguration(classes = SpringConfiguration.class)
public class test {

}
```
4)通过以上三步的配置, 在后续的单元测试中, 可以不需要在重复写创建容器的代码,简单配置即可.实例如下:

```java
// 替换原有的main方法
 @RunWith(SpringJUnit4ClassRunner.class)
 // 指定注解类的位置
 @ContextConfiguration(classes = SpringConfiguration.class)
public class test {
    // 注入
    @Autowired
    private IAccountService accountService;

    @Test
    public void findAllAccount() {
        List<Account> allAccount = accountService.findAllAccount();
        for (Account account : allAccount) {
            System.out.println(account);
        }
    }
}


```
当使用spring 5.x版本时候, 需要junit的包必须是4.12以上,否则会报错.
