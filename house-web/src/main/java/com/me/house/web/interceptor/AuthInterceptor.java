package com.me.house.web.interceptor;

import com.google.common.base.Joiner;
import com.me.house.common.constant.CommonConstants;
import com.me.house.common.model.User;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 将登录的用户(如果有)存放到threadlocal中
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //将这次请求的参数设置到request的attributes中
        //有些参数一个key是对应多个value的, eg. checkbox
        Map<String, String[]> map = request.getParameterMap();
        map.forEach((k,v) -> {
            if(k.equals("errorMsg") || k.equals("successMsg") || k.equals("target")){
                request.setAttribute(k, Joiner.on(",").join(v));
            }
        });
        String reqUri = request.getRequestURI();
        if(reqUri.startsWith("/static") || reqUri.startsWith("/error")){
            return true;
        }
        HttpSession session = request.getSession(true);
        User user = (User)session.getAttribute(CommonConstants.LOGIN_USER_KEY);
        if(user != null){
            //将user存到threadlocal中
            UserContext.setUser(user);
        }
        return true;
    }

    //Called after HandlerAdapter actually invoked the handler(controller), but before the DispatcherServlet renders the view.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserContext.remove();
    }
}
