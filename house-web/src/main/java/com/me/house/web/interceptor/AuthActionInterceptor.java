package com.me.house.web.interceptor;

import com.google.common.base.Joiner;
import com.me.house.common.model.User;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * 拦截未登录的用户
 */
@Component
public class AuthActionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = UserContext.getUser();
        if(user == null){
            String msg = URLEncoder.encode("please login!", "utf-8");
            String target = URLEncoder.encode(request.getRequestURL().toString(), "utf-8");
            if("GET".equalsIgnoreCase(request.getMethod())){
                response.sendRedirect("/accounts/signin?errorMsg=" + msg + "&target=" + target);
                return false;
            }else{
                response.sendRedirect("/accounts/signin?errorMsg="+msg);
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
