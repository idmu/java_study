package com.mine.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// 自定义拦截器，需要实现HandlerInterceptor接口
public class MineInterceptor implements HandlerInterceptor {
    @Override
    // 预处理, controller执行之前
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 返回ture表示放行,不拦截
        // 返回false 表示拦截
        System.out.println("preInterceptor执行了----之前");
//        request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request,response);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("interceptor执行---之后");
        // 指定页面后跳转至指定的页面, 而不会再跳控制器中指定的页面了
        request.getRequestDispatcher("/WEB-INF/pages/response.jsp").forward(request,response);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("interceptor----after");
        // 不能再跳转了
    }
}
