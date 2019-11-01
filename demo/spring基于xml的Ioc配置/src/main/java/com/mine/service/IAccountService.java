package com.mine.service;

import com.mine.domain.Account;

import java.util.List;

public interface IAccountService {
    /**
     * 查询所有数据
     * @return
     */
    List findAllAccount();

    /**
     * 根据id查询对应账户
     * @param accountId
     * @return
     */
   Account findAccountById(Integer accountId);

    /**
     * 保存账户
     * @param account
     */
   void saveAccount(Account account);

    /**
     * 更新
     * @param account
     */
   void  updateAccount (Account account);

    /**
     * 根据id删除账户
     * @param accountId
     */
   void deleteAccountById(Integer accountId);
}
