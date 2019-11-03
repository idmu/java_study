package com.mine.service;

import com.mine.domain.Account;

public interface IAccountService {
    /**
     * 查询
     * @param accountId
     * @return
     */
    Account getAccountById(Integer accountId);

    /**
     * 转账
     * @param sourceName  转出
     * @param targetName 目标账户
     * @param money 转账金额
     */
    void transfer(String sourceName, String targetName, Float money);
}
