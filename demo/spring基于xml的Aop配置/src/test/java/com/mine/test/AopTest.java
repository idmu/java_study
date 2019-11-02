package com.mine.test;

import com.mine.service.IAccountService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AopTest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext cls = new ClassPathXmlApplicationContext("bean.xml");
        IAccountService accountService = cls.getBean("accountService", IAccountService.class);
        accountService.saveAccount();
    }
}
