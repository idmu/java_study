package com.mine.dao.impl;

import com.mine.dao.IAccountDao;
import com.mine.domain.Account;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import sun.plugin2.main.server.ResultHandler;

import java.util.List;

public class AccountDaoImpl implements IAccountDao {
    private QueryRunner runner;

    public AccountDaoImpl(QueryRunner runner) {
        this.runner = runner;
    }

    public List findAllAccount() {
        try {
            return runner.query("select * from count ", new BeanListHandler<Account>(Account.class));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public Account findAccountById(Integer accountId) {
        try {
            return runner.query("select *from count where id = ?", new BeanHandler<Account>(Account.class), accountId);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    public void saveAccount(Account account) {
            try {
                runner.update("insert into count (name, money) values (?, ?)",account.getName(),account.getMoney() );
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

    public void updateAccount(Account account) {
        try {
            runner.update("update count set name = ?, money = ? where id = ?",account.getName(), account.getMoney(), account.getId());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void deleteAccountById(Integer accountId) {
        try {
            runner.update("delete from count where id = ?", accountId);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
