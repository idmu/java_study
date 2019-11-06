package com.mine.test;

import com.mine.domain.Account;
import com.mine.service.AccountService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {
    @Test
    public void testSpring() {
        ClassPathXmlApplicationContext cls = new ClassPathXmlApplicationContext("applicationContext.xml");
        AccountService accountService = cls.getBean("accountService", AccountService.class);
        accountService.findAll();
        accountService.savaAccount(new Account());
    }
}
