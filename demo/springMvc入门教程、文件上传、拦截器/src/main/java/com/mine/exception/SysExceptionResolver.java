package com.mine.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 异常处理器
public class SysExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        // 获取异常信息
         SysException exception = null;
        if (e instanceof SysException) {
            exception = (SysException) e;
        } else {
            exception = new SysException("系统正在升级, 请于...在来访问");
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("errorMsg",exception.getMessage());
        mv.setViewName("error"); // 指定跳转的页面
        return mv;
    }
}
