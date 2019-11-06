package com.mine.service;

import com.mine.domain.Account;

import java.util.List;

public interface AccountService {
    // 查询
    public List<Account> findAll();
    // 保存
    public void savaAccount(Account account);
}
