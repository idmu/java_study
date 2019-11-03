package com.mine.service.impl;

import com.mine.dao.IAccountDao;
import com.mine.dao.impl.AccountImpl;
import com.mine.domain.Account;
import com.mine.service.IAccountService;

public class AccountServiceImpl implements IAccountService {

    private IAccountDao accountDao;

    public void setAccountDao(IAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account getAccountById(Integer accountId) {
        return accountDao.getAccountById(accountId);
    }

    public void transfer(String sourceName, String targetName, Float money) {
        Account source = accountDao.getAccountByName(sourceName);
        Account target = accountDao.getAccountByName(targetName);
        source.setMoney(source.getMoney() - money);
        target.setMoney(target.getMoney() + money);
        accountDao.updateAccount(source);
        accountDao.updateAccount(target);
    }
}
