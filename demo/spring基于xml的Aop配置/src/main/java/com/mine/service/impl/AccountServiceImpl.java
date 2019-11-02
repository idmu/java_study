package com.mine.service.impl;

import com.mine.service.IAccountService;

public class AccountServiceImpl implements IAccountService {
    @Override
    public void saveAccount() {
        System.out.println("执行了保存");
    }

    @Override
    public void updateAccount(int i) {
        System.out.println("执行了更新" + i);

    }

    @Override
    public int deleteAccount(int i) {
        System.out.println("执行了删除" + i);
        return 0;
    }
}
