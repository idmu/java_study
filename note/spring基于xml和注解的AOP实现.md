所谓AOP, 即指的事面向切面编程.AOP的优势主要在于在程序运行期间，可以实现在不修改源码的情况下对已有方法进行增强.AOP的本质其实使用的动态代理技术.

#####   AOP的XML配置
* 在resources下新建一个bean.xml的文件, 然后导入aop配置.

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">

```
*  spring中基于xml的Aop配置步骤
1) 把通知的bean配置进来, 交给spring管理.
2) 使用aop:config标签来表示aop开始的位置.
3) 使用aop:aspect标签表示开始配置切面.其中 id属性表示给切面提供一个唯一标识; ref表示指定通知类bean的id.
4) 在aop:aspect标签内部使用对应的标签来配置通知类型.
```aop:before ```表前置通知, 切入点方法执行之前执行.
```aop:after-returning```配置后置通知,切入点方法正常执行之后。它和异常通知只能有一个执行;
```aop:after-throwing ```配置异常通知, 切入点方法执行产生异常后执行。它和后置通知只能执行一个;
```aop:after```用于配置最终通知,无论切入点方法执行时是否有异常, 它都会在其后面执行.
```aop:around```环绕通知.
```切入点表达式的写法:```execution(表达式).
```表达式格式:```访问修饰符 返回值 包名.包名.包名...类名.方法名(参数列表).
例如:public void com.mine.service.impl.AccountServiceImpl.saveAccount()
```关于表达式的写法```
a. 返回值可以使用*号，表示任意返回值 
b. 包名可以使用 * 号，表示任意包，但是有几级包，需要写几个*
c. 使用..来表示当前包，及其子包.
d. 类名可以使用*号，表示任意类.
e. 方法名可以使用*号，表示任意方法.
f. 参数列表可以使用*，表示参数可以是任意数据类型，但是必须有参数.
```java

* com..*.*(*)
```

g. 参数列表可以使用..表示有无参数均可，有参数可以是任意类型.

```java
* com..*.*(..)
```

h.全通配方式:
```java
* *..*.*(..)
```
i. ```通常情况下，我们都是对业务层的方法进行增强，所以切入点表达式都是切到业务层实现类.推荐写法 : ```

```java
execution(* com.mine.service.impl.*.*(..))  //推荐写法
```
5) 关于aop配置标签
method 用于指定通知中的方法名.
pointcut 属性用于指定切入点表达式,该表达式的含义是指对当前的业务层中哪些方法增强.
expression 用于指定表达式内容;
pointcut-ref 指定切入点表达式的引用; 
```java
<aop:pointcut id="pt" expression="execution(* com..mine.service.impl.*.*(..))"/>
```

aop配置实例:

```java
    <aop:config>
        <!--关于切面表达式,id属性用于表达切面表达式的唯一标识,
           expression用于指定表达式内容, 此标签写在aop:aspect内部表示只有当前切面可用,写在外面表示所有切面可用.-->
        <aop:pointcut id="pt" expression="execution(* com..mine.service.impl.*.*(..))"/>
        <aop:aspect id="loggerAdvice" ref="logger">
            <!--建立通知方法和切入点方法的关联-->
        <aop:before method="beforePrintLog" pointcut-ref="pt"></aop:before>
        <aop:after-returning method="afterReturningPrintLog" pointcut-ref="pt"></aop:after-returning>
        <aop:after-throwing method="afterThrowingPrintLog"  pointcut-ref="pt"></aop:after-throwing>
        <aop:after method="afterPrintLog"  pointcut-ref="pt"></aop:after>
        </aop:aspect>
    </aop:config>
```

##### 环绕通知
```aop:around```. 通常情况下，环绕通知都是独立使用的.spring 提供了一个接口''ProceedingJoinPoint''，它可以作为环绕通知的方法参数. 在环绕通知执行时，spring提供了一个"proceed()"方法可直接明确调用切入点方法，我们直接使用即可.
它是 spring 框架为我们提供的一种可以在代码中手动控制增强代码什么时候执行的方式.
在环绕通知中, 前后通知跟"proceed( )"方法的前后顺序相关, 在"proceed()"之前就表示是前置通知,之后则表示后置通知,在exception中表示异常通知, 在finally中则表示是最终通知.
实例:

```java
   public Object aroundPringLog(ProceedingJoinPoint pjp) {
        Object resValue = null;
        try {
            Object[] args = pjp.getArgs(); // 获取方法执行所需要的参数
            System.out.println("aroundPringLog中的logger方法开始记录日志了...前置");
            resValue = pjp.proceed(args); // 明确调用业务层的切入点方法 ....切入点方法
            System.out.println("aroundPringLog中的logger方法开始记录日志了...后置");
            return resValue;
        } catch (Throwable throwable) {
            System.out.println("aroundPringLog中的logger方法开始记录日志了...异常");
           throw new RuntimeException(throwable);
        } finally {
            System.out.println("aroundPringLog中的logger方法开始记录日志了...最终");

        }
    }

```
#####   AOP的注解配置
全注解形式配置, 不需要xml文件的方式,实例如下:
```java
package com.mine.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * 用于记录日志的工具类
 */
@Component("logger")
@Aspect // 表示当前类是个切面类
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.mine")
public class Logger {

    @Pointcut("execution(* com..mine.service.impl.*.*(..))")
    private void pt(){}
    /**
     * 前置通知
     */
//    @Before("pt()")
    public void beforePrintLog() {
        System.out.println("前置通知logger中的printLog方法开始执行了...");
    }

    /**
     * 后置通知
     */
//    @AfterReturning("pt()")
    public void afterReturningPrintLog() {
        System.out.println("后置通知logger中的printLog方法开始执行了...");
    }

    /**
     * 异常通知
     */
//    @AfterThrowing("pt()")
    public void afterThrowingPrintLog() {
        System.out.println("异常通知logger中的printLog方法开始执行了...");
    }

    /**
     * 最终通知
     */
//    @After("pt()")
    public void afterPrintLog() {
        System.out.println("最终通知logger中的printLog方法开始执行了...");
    }
    @Around("pt()")
    public Object aroundPringLog(ProceedingJoinPoint pjp) {
        Object resValue = null;
        try {
            Object[] args = pjp.getArgs(); // 获取方法执行所需要的参数
            System.out.println("aroundPringLog中的logger方法开始记录日志了...前置");
            resValue = pjp.proceed(args); // 明确调用业务层的切入点方法 ....切入点方法
            System.out.println("aroundPringLog中的logger方法开始记录日志了...后置");
            return resValue;
        } catch (Throwable throwable) {
            System.out.println("aroundPringLog中的logger方法开始记录日志了...异常");
           throw new RuntimeException(throwable);
        } finally {
            System.out.println("aroundPringLog中的logger方法开始记录日志了...最终");

        }
    }
}


```

经过测试发现, spring基于注解的aop配置的四种通知存在顺序调用问题,若使用环绕通知则不存在这个问题.所以当我们选用注解的方式来进行aop配置时,推荐使用环绕通知,而非那四种通知模式.
修改测试类中的容器解析方式:

```java
public class AopTest {
    public static void main(String[] args) {
        ApplicationContext cls = new AnnotationConfigApplicationContext(Logger.class);
        IAccountService accountService = cls.getBean("accountService", IAccountService.class);
        accountService.saveAccount();
    }
}

```
