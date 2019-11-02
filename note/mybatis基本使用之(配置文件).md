#### 基本环境搭建

1 新建一个无骨架的maven工程, 要在maven的directory中指定maven的安装路径和setting.xml,responstory的位置.
2  删除新建工程src目录,让其成为一个父类,充当父工程的角色.这样设计的目的是不需要在重复的为后来新建的模块设置对应的jar依赖.
3 在pom.xml中导入常用的jar包,如mysql, mybatis,junit等.

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!--父工程-->
    <groupId>cn.mineTest</groupId>
    <artifactId>mybatis_01</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>mybatis_study_01</module>
    </modules>
    <!--导入依赖-->
    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.46</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.2</version>
        </dependency>
    </dependencies>
    <build>
        <!--容错处理,以免配置文件无法导入或生效的问题-->
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>
</project>

```

 4  创建一个模块作为我们的工程项目,观察父包中多了一个新建的这个模块.新建的模块不需要在重新导包了.这就是maven父子继承的好处.(父工程的核心配置中会有子工程的mudule,表示子工程也拥有跟父类相同的配置)

```java
<modules>
        <module>mybatis_study_01</module>
    </modules>
```

#### mybatis的集成

1 直接在mybatis搜索即可

```java
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.2</version>
</dependency>
```

2. 参照mybatis文档,封装加载核心配置文件的工具类.例如:

```java
package com.mine.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;

// sqlSessionFactory工具类
public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;
    static {
        try {
            String resource = "mybatis_config.xml";
         InputStream  inputStream = Resources.getResourceAsStream(resource);
         sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取可以执行sql的sqlSession对象
    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }
}
```

常见错误:

- 配置文件没有注册,错误提示如下:

```java
org.apache.ibatis.binding.BindingException: Type interface 
com.mine.dao.UserDao is not known to the MapperRegistry.
```

解决方法:每一个Mapper.xml都需要在mybatis的核心配置文件中注册.

```java
例如:在mybatis核心配置中注册UserMapper.xml.
<mappers>
        <mapper resource="com/mine/dao/UserMapper.xml"/>
    </mappers>
```

- 配置文件无法导入或生效的问题.即resouces中的配置文件导出失败.解决方案是在build中配置resource.

```java
<build>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>true</filtering>
        </resource>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>true</filtering>
        </resource>
    </resources>
</build>
```

#### CRUD操作

- namespace中的包名要和Dao/Mapper中的接口的包名保持一致.
- select选择查询语句
  - id: 指的就是namespace中的方法名.
  - resultType: sql语句执行的返回值.
  - parameterType: 参数类型. 

注意点: 增删改需要提交事务.
例如:

```java
// UserMapper
public interface UserMapper {
    List<User> getUserList();
    User getUserById(int id);
}
// UserMapper.xml
<mapper namespace="com.mine.dao.UserMapper">
  <select id="getUserList" resultType="com.mine.pojo.User">
    select * from mybatis.user
    </select>

    <select id="getUserById" resultType="com.mine.pojo.User" parameterType="int">
        select * from mybatis.user where id = #{id}
    </select>
    // 
    <insert id="addUser" parameterType="com.mine.pojo.User" >
        insert into mybatis.user (id, name, pwd) values (#{id}, #{name}, #{pwd})
    </insert>

    <update id="updateUser" parameterType="com.mine.pojo.User">
    update mybatis.user set name = #{name}, pwd = #{pwd} where id = #{id}
    </update>

    <delete id="deleteUser" parameterType="com.mine.pojo.User">
        delete from mybatis.user where id = #{id}
    </delete>
</mapper>


```

#### 配置解析优化

###### 环境配置（environments)

尽管可以配置多个环境，但每个 SqlSessionFactory 实例只能选择一种环境.可以通过默认使用的环境 ID（default="development"）属性来切换配置环境。

###### 属性（properties）

可以通过properties属性来实现引用配置文件,这些属性都是可以外部配置且动态替换,既可以在典型的java属性文件中配置,亦可以通过properties元素的子元素来传递.
例如:  
(1)我们可以在resources下新建一个db. properties文件,在其中配置db相关的设置.

```java
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/mybatis?useSSL=false
username=root
password=12345678
```

(2)然后在核心配置文件中通过properties标签元素引入db.proerties文件即可.

```java
<configuration>
<!--引入外部配置文件-->
    <properties resource="db.properties"></properties>
        <environments default="development">
            <environment id="development">
                <transactionManager type="JDBC"/>
                <dataSource type="POOLED">
                    <property name="driver" value="${driver}"/>
                    <property name="url" value="${url}"/>
                    <property name="username" value="${username}"/>
                    <property name="password" value="${password}"/>
                </dataSource>
            </environment>
        </environments>
    <mappers>
        <mapper resource="com/mine/dao/UserMapper.xml"/>
    </mappers>
</configuration>

```

- 关于引入外部文件的注意事项:
  (1)可以直接引入
  (2)可以在其中增加一些属性配置
  (3)如果两个文件同一个字段,则会优先使用外部配置文件的.比如我们在db. properties中配置了账户密码,在核心配置文件中引入db. properties之后,在properties标签中也配置了账户密码,那么此时会优先使用外部配置文件db. properties中的账户密码.

##### 类型别名（typeAliases）

类型别名目的是为Java类型设置一个短的名字,它只和 XML 配置有关,存在的意义仅在于用来减少类完全限定名的冗余.

- 给类型取别名有两种方式:
  (1)直接使用typeAlias标签

```java
  <typeAliases>
        <typeAlias type="com.mine.pojo.User" alias="user"/>
    </typeAliases>
    
```

(2)指定一个包名
指定包名这种方式,Mybatis会在包名下面搜索需要的JavaBean.这种设置,会扫描实体类的包,则它的默认别名就是当前这个类的类名,首字母小写.

```java
 <typeAliases>
        <package name="com.mine.pojo" />
    </typeAliases>
```

两种取别名的方式区别在于第一种可以自定义别名,第二种则不行.对于第二种方法若一定要自定义别名,而不采用类首字母小写的方式作为别名,则需要在对应的实体类上增加注解的方式可以实现.

```java
例如:
@Alias("hello")
public class User {
```

#### 映射器(Mapper)

MapperRegistry主要是帮助我们注册绑定Mapper文件.有三种实现方式.
(1) 直接使用resource来指定对应文件.

```java
<mappers>
        <mapper resource="com/mine/dao/UserMapper.xml"/> </mappers>
```

(2)使用class文件帮到注册

```java
<mappers>
        <mapper class="com.mine.dao.UserMapper.xml"/> </mappers>
```

(3)使用扫描包进行注入绑定

```java
<mappers>
        <mapper package="com.mine.dao"/>
</mappers>
```

其中方法(2)和(3)中需要尤其注意:
接口和Mapper配置文件必须同名
接口和Mapper配置文件必须在同一个包下.

#### 日志

如果数据库操作,出现异常,则需要排错,日志就是最好的帮手.
日志工厂:logImpl
(1)STDOUT_LOGGING为标准日志工厂实现.

- 在mybatis的核心配置文件中,配置我们的日志

```java
<!--日志配置-->
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
 </settings>
```

(2)LOG4J

- 可以控制日志信息输送的目的地是控制台、文件、GUI组件等
- 可以控制每一条日志的输出格式
- 可以定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程
- 可以通过一个配置文件来灵活地进行配置，而不需要修改应用的代码。
  log4j的配置:
  (1)先导入log4j依赖包.
  (2)log4j.properties配置

```java
### 配置根 ###
log4j.rootLogger = DEBUG,console ,file

### 配置输出到控制台 ###
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.Threshold = DEBUG
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern = [%c] - %m%n

### 配置输出到文件 ###
log4j.appender.file = org.apache.log4j.RollingFileAppender
log4j.appender.file.File = ./logs/log4jTest.log
log4j.appender.file.maxFileSize = 10mb
#log4j.appender.fileAppender.Append = true
log4j.appender.file.Threshold = DEBUG
log4j.appender.file.layout = org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern = [%p][%d{yy-MM-dd}][%c]%m%n

### 设置输出sql的级别，其中logger后面的内容全部为jar包中所包含的包名 ###
log4j.logger.org.apache = DEBUG
log4j.logger.org.mybatis = DEBUG
log4j.logger.java.sql.Connection = DEBUG
log4j.logger.java.sql.Statement = DEBUG
log4j.logger.java.sql.PreparedStatement = DEBUG
log4j.logger.java.sql.ResultSet = DEBUG

```

(3)在setting中配置log4j为日志的实现方式

```java
<settings>
        <setting name="logImpl" value="LOG4J"/>
    </settings>
```

log4j使用:
(1) 在要使用log4j的类中导入包  ```import org.apache.log4j.Logger;```
(2) 生成日志对象, 日志对象为当前类的class

```java
 @Test
    public void log4jTest() {
        Logger logger = Logger.getLogger(UserDaoTest.class);
        logger.info("log4j测试来了");
        logger.debug("log4j的debug来了");
        logger.error("log4j的error来了");
    }
    
```

(3)日常开发中只需要在需要日志的地方补充上必要的logger.info语句即可快速定位.
