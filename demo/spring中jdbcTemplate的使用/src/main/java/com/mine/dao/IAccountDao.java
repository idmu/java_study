package com.mine.dao;

import com.mine.domain.Account;

public interface IAccountDao {

    Account getAccountById(Integer accountId);

    Account getAccountByName(String accountName);

    void updateAccount(Account account);

}
