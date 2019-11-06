package com.mine.dao;

import com.mine.domain.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AccountDao {
    // 查询
    @Select("select *from account")
    public List<Account> findAll();
    // 保存
    @Insert("insert into account (name, money) values (#{name}, #{money})")
    public void savaAccount(Account account);
}
