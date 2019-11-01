package com.mine.service.impl;

import com.mine.dao.IAccountDao;
import com.mine.domain.Account;
import com.mine.service.IAccountService;

import java.util.List;

public class AccountServiceImpl implements IAccountService {
    private IAccountDao accountDao;


    public void setAccountDao(IAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public List findAllAccount() {
        return accountDao.findAllAccount();
    }

    public Account findAccountById(Integer accountId) {
        return accountDao.findAccountById(accountId);
    }

    public void saveAccount(Account account) {
         accountDao.saveAccount(account);
    }

    public void updateAccount(Account account) {
         accountDao.updateAccount(account);

    }

    public void deleteAccountById(Integer accountId) {
         accountDao.deleteAccountById(accountId);
    }
}
