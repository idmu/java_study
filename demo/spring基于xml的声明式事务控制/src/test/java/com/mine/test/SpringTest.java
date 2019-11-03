package com.mine.test;

import com.mine.service.IAccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:bean.xml"})
public class SpringTest {
    @Autowired
    private IAccountService accountService;

    @Test
    public void transfer() {
        accountService.transfer("ddd","孙尚香",10f);
    }
}
