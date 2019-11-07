springBoot主要是提供了一种款速使用spring的方式,没有代码生成,也无需配置xml.其核心功能提现在起步依赖和自动装配.起步依赖指的是将具备某种功能的坐标打包到一起，并提供一些默认的功能。自动装配则提现在Spring Boot是一个运行时的过程,对于spring的配置应该用哪个,不需要配置哪个,该过程是Spring自动完成的。

##### springBoot开发环境部署
* 使用idea创建一个基础项目,不携带骨架式的.
1. 使用idea工具创建一个普通的maven工程.(无需携带骨架)
2. 添加SpringBoot的起步依赖.
1)springBoot的项目必须要继承springBoot的起步依赖spring-boot-starter-parent.
即:
```java
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.1.RELEASE</version>
 </parent>
```
2)同时,springBoot要集成springMVC进行Controller的开发，也必须要导入web的启动依赖.

```java
<dependencies>
        <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
</dependencies>

```
3. 编写springBoot引导类.
 要通过springBoot提供的引导类起步,springBoot才可以进行访问.
<```@SpringBootApplication```表示SpringBoot的引导类.>

```java
// 用以声明当前类是一个springBoot的引导类
@SpringBootApplication
public class MySpringBootApplication {
// main方法是程序的入口
    public static void main(String[] args) {
        // run表示运行springBoot的引导类,参数是引导类的字节码文件
        SpringApplication.run(MySpringBootApplication.class);
    }
}

```
4. 编写controller类
在springBoot引导类MySpringBootApplication的同级目录新建一个子包controller,编写控制器相关代码.尤其需要注意的是,新建的这个controller包, 一定要和引导类在同级目录下，否则识别不到.

```java
@Controller
public class HelloController {
    @RequestMapping("/hello")
    @ResponseBody
    public String helloBoot() {
        return "hello springBoot";
    }
}
```
*  使用idea的spring initalizr快速构建项目
spring initalizr的这种创建方式默认已经为我们提供了web的起步依赖相关坐标.
1. 新创建一个新项目, 选择spring initalizr ,直接点next.
2. 填写项目信息,选择初始化的组件,填写项目路径.在构建工具选择页面选择想要配置的组件, 等待项目构建成功.
3. SpringBootApplication的同路径下新建一个包 controller,编码即可.


##### springBoot工程的热部署
编码过程中需要反复修改类、页面等资源，每次修改后都是需要重新启动才生效，而热部署刚好可以优化这一弊端, 使得资源修改后不需要再次重启即可生效.
1 . 导入依赖
```java
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
</dependency>
```
2 . 修改compiler项. 勾选.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191106145247553.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NTEwODA4Nw==,size_16,color_FFFFFF,t_70)
3 . 修改registry配置,勾选.
win: Shift+Ctrl+Alt+/，搜索Registry;
mac: command+shift+A ; 搜索Registry;
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191106150828123.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NTEwODA4Nw==,size_16,color_FFFFFF,t_70)

##### springBoot配置文件
* 关于yml文件
springBoot的很多配置都有默认值,如果想要替换默认的配置就需要添加application.properties或者application.yml(application.yaml)进行配置, springBoot默认会从Resources目录下加载application.properties或application.yml(application.yaml)文件.application.properties里面的内容以```key: value```的形式配置.其中需要注意的是:```key: value```之间冒号与value之间必须要一个空格. 
application.yml文件实例:

```yaml
# 关于格式: ①注意 "-"与后面值之间必须有一空格; ②":"与value之间的必须有一空格.
# 普通数据
name: libai
# 对象的配置
teacher:
  name: libai
  age: 18
  address: fujian
# 行内对象配置
person: {name: zhangsan, age: 18}
# 配置数组
city:
  - beijing
  - shanghai
  - nanjing
  - shenzhen
citys: [beijing, shanghai, fujian]
# 配置集合(对象数据)
students:
  - name: li
    age: 15
    address: shanghai
  - name: mi
    age: 13
    address: guangzhou
studentss: [{name: li, age: 15, address: shanghai}, {name: mi, age: 13, address: guangzhou}]
# Map配置
server: 9999


```

* 配置文件的读取
1. 通过@Value注入的方式.要访问yml的某值, 可以先定义个属性,然后通过```@Value("${xx}")```的方式来获取对应值.

```java
@Controller
public class YmlTestController {
    @Value("${name}")
    // 定义一个name属性,通过@Value注入, 然后就会去application.yml中找name这个属性的值来映射赋值
    private String name;
    @Value("${teacher.address}")
    private String address;
    @RequestMapping("/getYml")
    @ResponseBody
    public String ymlTest() {
        // 获取配置文件的信息
        return "address:" + address;
    }
}

```

2. 使用```@ConfigurationPropertiess(prefix="xx")```注解方式
通过注解@ConfigurationProperties(prefix="配置文件中的key的前缀")可以将配置文件中的配置自动与实体进行映射, 这种方式在yml中对应属性配置时会有提示.
1)在pom.xml导入依赖jar包.

```java
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-configuration-processor</artifactId>
	<optional>true</optional>
</dependency>
```
  2) 在控制器中,配置 ```@ConfigurationPropertiess(prefix="xx")```注解,xx为配置文件的对象名(key前缀);定义需要获取配置文件中的属性,设置对应属性的set和get方法.映射完成. 具体实现参照:

```java
@Controller
// 添加注解, 映射yml中的person对象
@ConfigurationProperties(prefix = "person")
public class YmlTestController {
    // 定义需要获取的属性
    private String name;
    private String age;

    @RequestMapping("/getYml")
    @ResponseBody
    public String ymlTest() {
        // 获取配置文件的信息
        return "name:" + name +", age:" + age;
    }
    // 设置set和get方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

}

```

##### springBoot与其他框架的整合
1. 基于spring initalizr新建一个携带spring web的工程, 导入mybatis的起步依赖jar包和mysql的jar包.

```java
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>

```
2. 在application.properties中添加数据量的连接信息. 连接的信息的key值需要在jar包下的springboot-autoconfigure下的MATA-INF下的metadata.json文件中查找对应key的name然后进行设置, 如果key不对的话最终数据库是无法映射成功的.
然后在application.properties中配置数据库连接信息. 如:

```java
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/ssm?useUnicode=true&characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=12345678

```
3. 提供一个数据库, 对应一个实体对象, 同时resources下提供一个与实体类对应的mapper文件.

```java

//--------- AccountMapper--------
// @Mapper标记该类是一个mybatis的mapper接口，可以被spring boot自动扫描到spring上下文中
@Mapper
public interface AccountMapper {
    public List<Account> findAllAccount();
}
//--------- AccountMapper.xml--------
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >

<mapper namespace="com.mine.mapper.AccountMapper">
    <select id="findAllAccount" resultType="com.mine.domain.Account">
        select  * from account;
    </select>
</mapper>
//---------测试控制器--------
@Controller
public class AccountController {
    @Autowired
    private AccountMapper accountMapper;
    @RequestMapping("/getAccountList")
    @ResponseBody
    public List getAccountList() {
        List<Account> list = accountMapper.findAllAccount();
        return  list;
    }
}

```



4. 在application.properties中添加mybatis的信息.配置字段的key值也是需要在jar包下的springboot-autoconfigure下的MATA-INF下的metadata.json文件中查找对应key的name然后进行设置.如果key值不对会导致无法映射成功.

```java
#spring集成Mybatis环境
#pojo别名扫描包 
mybatis.type-aliases-package=com.mine.domain
#加载Mybatis映射文件
mybatis.mapper-locations=classpath:mapper/*Mapper.xml



```

##### 集成junit

1. 导入起步依赖坐标
```java
<dependency>
<groupId>org.springframework.boot</groupId> <artifactId>spring-boot-starter-test</artifactId> <scope>test</scope>
</dependency>

```
2. 直接利用注解的方式指定.
```@SpringBootTest```的属性指定的是引导类的字节码对象
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisApplication.class)
public class MybatisTest {
    @Autowired
    private AccountMapper accountMapper;
    @Test
    public void getlist() {
        List<Account> list = accountMapper.findAllAccount();
        System.out.println(list);
    }
}

```