package com.mine.service.impl;

import com.mine.dao.AccountDao;
import com.mine.domain.Account;
import com.mine.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
    public List<Account> findAll() {
        System.out.println("业务层 : 查询所有账户信息");
        return accountDao.findAll();
    }

    public void savaAccount(Account account) {
        System.out.println("业务成: 保存账户");
        accountDao.savaAccount(account);
    }
}
