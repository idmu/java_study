package com.mine.test;

import com.mine.dao.AccountDao;
import com.mine.domain.Account;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisTest {
    /*
    @Test
    public void testFindAll() throws Exception {
        InputStream resource = Resources.getResourceAsStream("MapperConfig.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resource);
        SqlSession sqlSession = sessionFactory.openSession();
        AccountDao accountDao = sqlSession.getMapper(AccountDao.class);
        List<Account> list = accountDao.findAll();
        System.out.println(list);
        sqlSession.commit();
        sqlSession.close();
        resource.close();
    }

    @Test
    public void saveAccount() throws Exception {
        Account account = new Account();
        account.setName("李白");
        account.setMoney(700d);
        InputStream resource = Resources.getResourceAsStream("MapperConfig.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resource);
        SqlSession sqlSession = sessionFactory.openSession();
        AccountDao accountDao = sqlSession.getMapper(AccountDao.class);
        accountDao.savaAccount(account);
        sqlSession.commit();
        sqlSession.close();
        resource.close();
    }
     */
}
