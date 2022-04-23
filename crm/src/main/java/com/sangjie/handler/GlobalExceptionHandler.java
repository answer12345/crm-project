package com.sangjie.handler;

import com.sangjie.exception.LoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = LoginException.class)
    @ResponseBody
    public Map doLoginException(Exception e) {
        Map map = new HashMap();
        String message = e.getMessage();
        map.put("success", false);
        map.put("msg", message);
        return map;
    }
}
