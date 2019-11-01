package com.mine;

import com.mine.domain.Account;
import com.mine.service.IAccountService;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class test {
    @Test
    public void findAllAccount() {
        ApplicationContext cls = new ClassPathXmlApplicationContext("bean.xml");
        IAccountService accountService = cls.getBean("accountService", IAccountService.class);
        List<Account> allAccount = accountService.findAllAccount();
        for (Account account : allAccount) {
            System.out.println(account );
        }
    }

    @Test
    public void findOne() {
        ApplicationContext cls = new ClassPathXmlApplicationContext("bean.xml");
        IAccountService accountService = cls.getBean("accountService", IAccountService.class);
        Account account = accountService.findAccountById(3);
        System.out.println(account);
    }
    @Test
    public void saveTest() {
        Account account = new Account();
        account.setMoney(9988f);
        account.setName("孙尚香");
        ApplicationContext cls = new ClassPathXmlApplicationContext("bean.xml");
        IAccountService accountService = cls.getBean("accountService", IAccountService.class);
        accountService.saveAccount(account);
    }

    @Test
    public void updateTest() {
        ApplicationContext cls = new ClassPathXmlApplicationContext("bean.xml");
        IAccountService accountService = cls.getBean("accountService", IAccountService.class);
        Account account = accountService.findAccountById(4);
        account.setMoney(1990f);
        accountService.updateAccount(account);
    }
    @Test
    public void deleteTest() {
        ApplicationContext cls = new ClassPathXmlApplicationContext("bean.xml");
        IAccountService accountService = cls.getBean("accountService", IAccountService.class);
        accountService.deleteAccountById(1);
    }
}



