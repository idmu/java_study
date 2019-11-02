package com.mine.utils;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 用于记录日志的工具类
 */
public class Logger {

    /**
     * 前置通知
     */
    public void beforePrintLog() {
        System.out.println("前置通知logger中的printLog方法开始执行了...");
    }

    /**
     * 后置通知
     */
    public void afterReturningPrintLog() {
        System.out.println("后置通知logger中的printLog方法开始执行了...");
    }

    /**
     * 异常通知
     */
    public void afterThrowingPrintLog() {
        System.out.println("异常通知logger中的printLog方法开始执行了...");
    }

    /**
     * 最终通知
     */
    public void afterPrintLog() {
        System.out.println("最终通知logger中的printLog方法开始执行了...");
    }

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
