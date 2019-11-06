package com.mine.controller;

import com.mine.domain.Account;
import com.mine.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

// web
@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @RequestMapping("/findAll")
    public String findAll(Model model) {
        System.out.println("表现层方法调用");
        List<Account> list = accountService.findAll();
//        accountService.savaAccount(new Account());
        model.addAttribute("list",list);
        return "success";
    }

    @RequestMapping("/saveAccount")
    public void saveAccount(HttpServletRequest request, HttpServletResponse response, Account account) throws Exception{
        accountService.savaAccount(account);
        response.sendRedirect(request.getContextPath() + "/account/findAll");
        return ;
    }
}
