```@Component```作用:通用注解，通常用于标注某个不太容易判断应该归属于哪个分层的spring组件；即把资源让 spring 来管理，相当于在 xml 中配置一个 bean。
```@Repository```用于标注数据访问组件,即DAO组件。
```@Service```一般用于标注业务层 的注解。
```@Controller```一般用于表现层的注解， 比如struts中的action等。
上述注解均有属性值value：指定 bean 的 id，如果不指定 value 属性，默认 bean 的 id 是当前类的类名，首字母小写。
```@Autowired```
作用：自动按照类型注入。当有多个类型匹配时，使用要注入的对象变量名称作为 bean 的 id，在 spring 容器查找，找到了也可以注入成功。找不到则会报错。
```@Qualifier```
作用：在自动按照类型注入的基础之上，再按照 Bean 的 id 注入。它在给字段注入时不能独立使用，必须和@Autowire 一起使用；但是给方法参数注入时，可以独立使用。
属性：value：可以用来指定 bean 的 id。
```@Resource```
作用：直接按照 Bean 的 id 注入。
属性：name：指定 bean 的 id。
```@Value```
作用：注入基本数据类型和 String 类型数据的。
属性：value：用于指定值。
```@Scope```
作用：指定 bean 的作用范围。
属性：value：指定范围的值。
属性value的取值通常包括：singleton/prototype/request/session/globalsession。
四种常见的 Spring Bean 的作用域：
①singleton : 唯一 bean 实例，Spring 中的 bean 默认都是单例的。
②prototype : 每次请求都会创建一个新的 bean 实例。
③request : 每一次 HTTP 请求都会产生一个新的 bean，该 bean 仅在当前 HTTP request 内有效。
④session : 每一次 HTTP 请求都会产生一个新的 bean，该 bean 仅在当前 HTTP session 内有效。
```@PostConstruct```
作用：用于指定初始化方法。@PostConstruct和@PreDestroy大致相当于<bean.../>元素的 init-method 属性和 destroy-method 属性指定的方法。@PostConstruct该注解被用来修饰一个非静态的void（）方法。被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行，init（）方法之前执行。该注解的方法在整个Bean初始化中的执行顺序为：Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注解的方法)。
```@PreDestroy```
作用：用于指定销毁方法。
```@Configuration```
作用：用于指定当前类是一个 spring 配置类，也可以使用 @Component来代替，不过使用Configuration注解声明一个配置类显得语义更加清晰，更加严谨。
```@ComponentScan```
作用：用于指定 spring 在初始化容器时要扫描的包。和在 spring 的 xml 配置文件中配置的作用是一样的。：
```java
<context:component-scan base-package="com.mine.test"/>
```
属性：basePackages：用于指定要扫描的包。和注解中的 value 属性作用一样。

```java
@Configuration
@ComponentScan("com.mine.test")
public class SpringConfiguration {
}
```
```@Bean```
作用：
该注解**只能写在方法**上，表明使用此方法创建一个对象，并且放入 spring 容器。
属性：name：给当前@Bean 注解方法创建的对象指定一个名称(即 bean 的 id）。
```@PropertySource```
用于加载.properties 文件中的配置。例如我们配置数据源时，可以把连接数据库的信息写到properties 配置文件中，就可以使用此注解指定 properties 配置文件的位置。
属性：
value[]：用于指定 properties 文件位置。如果是在类路径下，需要写上classpath:

```java
@Configuration
@PropertySource("classpath:jdbc.properties")
public class JdbcConfig{
}
```

```@Import```
作用：用于导入其他配置类，在引入其他配置类时，可以不用再写@Configuration 注解。当然，写上也没问题。
属性：value[]：用于指定其他配置类的字节码。
```java
@Configuration
@ComponentScan(basePackages = "com.mine.spring")
@Import({ JdbcConfig.class})
public class SpringConfiguration {
}
```

```@ContextConfiguration  ```
locations  属性：用于指定配置文件的位置。如果是类路径下，需要用 classpath:表明。
classes  属性：用于指定注解的类。当不使用 xml 配置时，需要用此属性指定注解类的位置。

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:bean.xml"})
public class AccountServiceTest {
}
```
```@SpringBootApplication```
点击项目中的@SpringBootApplication注解查看源码。如下：
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
@Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
@Filter(type = FilterType.CUSTOM, classes =
AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
/**
* Exclude specific auto-configuration classes such that they will never be
applied.
* @return the classes to exclude
*/
@AliasFor(annotation = EnableAutoConfiguration.class)
Class<?>[] exclude() default {};
... ... ...
}
```
其中，
```@SpringBootConfiguration```相当于```@Configuration```，即标注该类是Spring的一个配置类。
```@EnableAutoConfiguration```表示开启SpringBoot的自动配置功能。因此，```@SpringBootApplication我们可以看作是@SpringBootConfiguration、@EnableAutoConfiguration与@ComponentScan这三个注解的集合体。```
通过查看@EnableAutoConfiguration的源码，发现存在一个@AutoConfigurationPackage注解的类，而其中通过@Import(AutoConfigurationImportSelector.class) 导入了AutoConfigurationImportSelector类，而AutoConfigurationImportSelector中，由此可以知道导入哪些组件的选择器；将所有需要导入的组件以全类名的方式返回；这些组件就会被添加到容器中。具体的就是AutoConfigurationImportSelector中存在```SpringFactoriesLoader.loadFactoryNames 方法的作用就是从类路径下的META-INF/spring.factories文件中读取获取EnableAutoConfiguration指定的值```。在spring.factories 中存在大量的通过EnableAutoConfiguration指定的以Configuration为结尾的类名称，这些类就是存有自动配置信息的类，而SpringApplication在获取这些类名后会依次加载。
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
... ... ...
}
```
```@GetMapping("names")```等价于```@RequestMapping(value="/names",method=RequestMethod.GET)```
```@PostMapping("names")```等价于```@RequestMapping(value="/names",method=RequestMethod.POST)```
```@RestController```
  ```@RestController```的作用相当于```@Controller和@ResponseBody的合体```，表示这是个控制器 bean，并且是将函数的返回值直接填入 HTTP 响应体中。```仅使用 @Controller注解 而不加 @ResponseBody```，多用于返回一个页面视图的场景，这种情况常见于前后端不分离的比较传统的 Spring MVC 应用。（当我们仅仅使用 @Controller注解，那么Spring 默认会根据返回的页面视图名称去 resources 目录下 templates 目录下找对应的视图并返回，所以通常也建议把页面放在 resources/templates 目录下）。而```@Controller +@ResponseBody```则表示返回 JSON 或 XML 形式数据。
  
```java
@RestController
public class MineController {
    @PostMapping("/login")
    public User HelloUser(@RequestBody User user) {
        return User;
    }

}
```
```@RequestBody```
主要用来```接收前端传递给后端的json字符串```中的数据的(请求体中的数据的)；GET方式无请求体，所以使用@RequestBody接收数据时，前端不能使用GET方式提交数据，而是用POST方式进行提交。在同一个接收方法里，@RequestBody与@RequestParam()可以同时使用，@RequestBody最多只能有一个，而@RequestParam()可以有多个。
```@RequestParam```用于获取查询参数。
```@PathVariable```用于映射 URL 绑定的占位符，也就是获取路径参数。通过 @PathVariable 可以将 URL 中占位符参数绑定到控制器处理方法的入参中。例如：@RequestMapping("/testPathVariable/{id}")
   
```java
@RequestMapping("/testPathVariable/{id}")
 public String testPathVariable(@PathVariable("id") Integer id) {
        System.out.println("testPathVariable:" + id);
        return SUCCESS;
    }
```
```@Value```
我们可以通过@Value注解将配置文件中的值映射到一个Spring管理的Bean的字段上。只能用于获取业务逻辑中的某个配置文件中的某项值，不支持获取复杂的数据类型，如map等。
```java
//在application.properties或application.yml中配置信息如下：
lable：我来测试一下abd
person:
	name: libai
	age: 45
//  实体bean代码：
@Controller
public class QuickStartController {
	@Value("${person.name}")
	private String name;
	@Value("${person.age}")
	private Integer age;
	@RequestMapping("/mineTest")
	@ResponseBody
	public String mineTest(){
		return "springboot 测试成功! name="+name+",age="+age;
		}
	}
```
 ```@ConfigurationProperties```
作用：通过注解@ConfigurationProperties(prefix="配置文件中的key的前缀")可以将配置文件中的配置自动与实体进行映射。但需要字段必须提供set方法才可以，而使用@Value注解修饰的字段不需要提供set方法。（获取全局配置文件的值），比@value更强大，支持复杂类型数据的获取。

```java
person:
	name: libai
	age: 45
//-----------------
 @Controller
    @ConfigurationProperties(prefix = "person")
    public class QuickStartController {
        private String name;
        private Integer age;
        @RequestMapping("/mineTest")
        @ResponseBody
        public String mineTest(){
            return "springboot 测试成功! name="+name+",age="+age;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setAge(Integer age) {
            this.age = age;
        }
    }
```
```@PropertySource```加载指定的 properties 文件。读取指定的文件后，可以通过@Value获取配置字段的值。

```java
@PropertySource(value = "classpath:application.properties",ignoreResourceNotFound = false)
```
```@ImportResource```:作用是导入spring的配置文件, 让配置文件里面的内容生效.spring boot里面若没有spring的配置文件, 我们自己编写配置之后,也无法自动识别.想让编写的配置文件生效,加载进来.可以通过@ImportResource标注在一个配置类上来实现.

```java
@Configuration
@ImportResource("classpath:bean.xml") //导入xml配置项
public class MineSystemConfig {
 
}
```
