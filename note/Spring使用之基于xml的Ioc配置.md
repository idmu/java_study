Spring是一个轻量级的控制反转(IOC)和面向切面(AOP)的容器框架.而IOC的核心就是解除代码间的依赖关系,削减程序间的耦合.Spring中的解耦思想从应用层面来说主要体现在两个方面:①使用反射来创建对象, 而避免使用new关键字;②通过读取配置文件来获取要创建的对象全限定类名.

##### 基本配置
* 环境搭建
1. 通过maven创建一个项目.然后在项目的核心配置文件(pom.xml)中配置spring的包.
	
```java
<dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.5.RELEASE</version>
        </dependency> 
</dependencies>
```
2 . 在配置文件完善配置信息
----  bean标签:用于配置让spring创建对象, 并且存入ioc容器之中.
---- id 属性:对象的唯一标识.
---- class 属性:指定要创建对象的全限定类名.
```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="accountService" class="com.mine.service.impl.AccountImpl"></bean>
</beans>

```
3. 测试配置结果
```ApplicationContext 接口的实现类:```
1)ClassPathXmlApplicationContext: 
它是从类的根路径下加载配置文件.(推荐)
2)FileSystemXmlApplicationContext:
 它是从磁盘路径上加载配置文件, 配置文件可以在磁盘的任意位置.
3)AnnotationConfigApplicationContext: 
当我们使用注解配置容器对象时,需要使用此类来创建 spring 容器,它用来读取注解.
```java
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringDemo {
    public static void main(String[] args) {
        // 通过ApplicationContext接口来获取spring容器
        ClassPathXmlApplicationContext path = new ClassPathXmlApplicationContext("bean.xml");
        // 通过bean的id获取对象
        Object accountService = path.getBean("accountService");
        System.out.println(accountService);
    }
}
```

#####   Bean 标签
bean标签的主要作用是配置对象让 spring 来创建。默认情况下它调用的是类中的无参构造函数, 如果没有无参构造函数则不能创建成功.
* 属性
```id:```给对象在容器中提供一个唯一标识.用于获取对象. 
```class:```指定类的全限定类名, 用于反射创建对象。默认情况下调用无参构造函数. 
```scope:```指定对象的作用范围.
scope属性取值主要包括如下:
```singleton :```默认值，单例的.
```prototype :```多例的.
```request :```WEB项目中,Spring创建一个Bean的对象,将对象存入到request域中. * session :WEB项目中,Spring创建一个Bean的对象,将对象存入到session域中. * ```global session :```WEB项目中,应用在Portlet环境.如果没有Portlet环境那么相globalSession 相当于 session.
```init-method:```指定类中的初始化方法名称.
```destroy-method:```指定类中销毁方法名称.
* bean 生命周期
```单例对象:scope="singleton" ```一个应用只有一个对象的实例,它的作用范围就是整个应用.
1)生命周期:
     对象出生:当应用加载，创建容器时, 对象就被创建了.
     对象活着:只要容器在, 对象一直活着.
     对象死亡:当应用卸载, 销毁容器时, 对象就被销毁了.
2)```多例对象:scope="prototype"```每次访问对象时, 都会重新创建对象实例.
生命周期:
  对象出生:当使用对象时, 创建新的对象实例.
  对象活着:只要对象在使用中, 就一直活着.
  对象死亡:当对象长时间不用时, 被 java 的垃圾回收器回收了.
*  Bean 的实例化
```方式一: 使用默认无参构造函数```
这种场景下, 它会根据默认无参构造函数来创建类对象. 如果 bean 中没有默认无参构造函数, 将会创建失败.

```java
<bean id="accountService" class="com.mine.service.impl.AccountImpl"/>
```
  ```方式二: 使用静态工厂的方法创建对象```
静态工厂方法创建对象时, xml中的bean对象配置:
```id 属性:```指定 bean 的 id，用于从容器中获取 
```class 属性:```指定静态工厂的全限定类名 
```factory-method 属性:```指定生产对象的静态方法
```java
// 提供一个静态工厂方法
public class StaticFactory {
    public static IAccountSave createAccountService() {
        return  new AccountImpl();
    }
}

// 配置bean
<bean id="accountService"
          class="com.mine.factory.StaticFactory"
          factory-method="createAccountService"></bean>

其中:
使用 StaticFactory 类中的静态方法 createAccountService 创建对象，并存入 spring 容器.

```
```方式三: 使用实例工厂的方法来创建对象```

```java
// 提供一个实例工厂方法
public class InstanceFactory {
public IAccountService createAccountService(){
return new AccountServiceImpl(); }
 }
// 配置bean.xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="instanceFactory" class="com.mine.factory.InstanceFactory"></bean>
    <bean id="accountService"
          factory-bean="instanceFactory"
          factory-method="createAccountService"></bean>
</beans>

```

##### spring 的依赖注入
* 第一种: 使用构造函数注入
 使用构造函数这种注入方式需要为当前类提供一个与参数列表相对应的构造函数.
 构造函数注入方式的bean配置标签为```constructor-arg```:
 主要属性:
  ```index:```指定参数在构造函数参数列表的索引位置 
  ```type:```指定参数在构造函数中的数据类型
 ```name:```指定参数在构造函数中的名称<推荐使用>
 ```value:```它能赋的值是基本数据类型和 String 类型
```ref:```它能赋的值是其他 bean 类型，也就是说，必须得是在配置文件中配置过的 bean.例如下例中的date属性.
```java
eg:
// 提供一个构造函数
public class AccountDaoImpl implements IAccountDao {
    private int age;
    private String name;
    private Date birthday;

    public AccountDaoImpl(int age, String name, Date birthday) {
        this.age = age;
        this.name = name;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "AccountDaoImpl{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}

// 配置bean.xml文件
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="accountDao" class="com.mine.dao.impl.AccountDaoImpl">
        <constructor-arg name ="age" value ="19"></constructor-arg>
        <constructor-arg name ="name" value = "二狗子"></constructor-arg>
        <constructor-arg name ="birthday" ref = "now"></constructor-arg>
    </bean>
    <bean id = "now" class="java.util.Date"></bean>
</beans>

```
* 第二种: 使用set方法注入 <推荐使用>
 set方法注入需要在类中提供对应属性的set方法.
 set方法注入其bean配置的标签为:```property```
 主要属性有:
 ```name:```找的是类中 set 方法后面的部分 
 ```ref:```给属性赋值是其他 bean 类型的 
 ```value:```给属性赋值是基本数据类型和 string 类型的

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id = "accountDao" class="com.mine.dao.impl.AccountDaoImpl">
        <property name="age" value="21"></property>
        <property name="name" value="孙尚香"></property>
        <property name="birthday" ref="now"></property>
    </bean>
    <bean id = "now" class="java.util.Date"></bean>
</beans>
```
构造函数注入,在获取bean对象时,注入数据是必须的操作,否则对象无法创建成功.这种方式的弊端主要是,它颠覆了bean对象的实例化方式,使我们在创建对象时, 即使可能用不到某些数据, 但依然需要提供.
*  复杂集合类型的注入
  在注入集合数据时，只要结构相同，标签可以互换. 如List 结构的: array,list,set
Map 结构的 map,entry,props,prop等标签均可互换.
```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id = "accountDao" class="com.mine.dao.impl.AccountDaoImpl">
        <property name="mylist">
            <array>
                <value>李白</value>
                <value>阿珂</value>
                <value>孙尚香</value>
            </array>
        </property>
        <property name="myMap">
            <map>
                <entry key="马尔扎哈" value="24"></entry>
                <entry key="古力娜扎" value="37"></entry>
                <entry key="费尔巴哈" value="23"></entry>
            </map>
        </property>
    </bean>
</beans>

```