package com.me.house.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/7/28.
 */
@ControllerAdvice
public class ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(value = {Exception.class})
    public String error500(HttpServletRequest request, Exception e){
        logger.error(request.getRequestURI() + ": encounter 500: "+ e.getMessage());
        e.printStackTrace(); // for debug
        return "error/500";
    }
}
