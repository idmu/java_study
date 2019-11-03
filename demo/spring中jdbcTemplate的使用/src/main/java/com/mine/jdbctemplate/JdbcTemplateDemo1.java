package com.mine.jdbctemplate;

import com.mine.dao.IAccountDao;
import com.mine.domain.Account;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcTemplateDemo1 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext cls = new ClassPathXmlApplicationContext("bean.xml");
        IAccountDao accountDao = cls.getBean("accountDao", IAccountDao.class);
        Account account = accountDao.getAccountById(8);
//        Account account = accountDao.getAccountByName("阿珂");
//        account.setMoney(777f);
//        accountDao.updateAccount(account);
        System.out.println(account);



    }
}
