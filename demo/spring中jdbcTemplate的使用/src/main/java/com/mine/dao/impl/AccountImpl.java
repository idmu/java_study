package com.mine.dao.impl;

import com.mine.dao.IAccountDao;
import com.mine.domain.Account;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

public class AccountImpl extends JdbcDaoSupport implements IAccountDao {
    public Account getAccountById(Integer accountId) {
        List<Account> accounts = getJdbcTemplate().query("select * from count where id = ?", new BeanPropertyRowMapper<Account>(Account.class),accountId);
        return accounts.isEmpty() ? null : accounts.get(0);
    }

    public Account getAccountByName(String accountName) {
        List<Account> accounts = getJdbcTemplate().query("select * from count where name = ?", new BeanPropertyRowMapper<Account>(Account.class),accountName);
        if (accounts.isEmpty()) {
            return null;
        }
        if (accounts.size() > 1) {
           throw new RuntimeException("查询结果不唯一");
        }
        return accounts.get(0);
    }

    public void updateAccount(Account account) {
        getJdbcTemplate().update("update count set name = ?, money = ? where id = ?",account.getName(),account.getMoney(),account.getId());
    }

}
