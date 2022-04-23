package com.sangjie.setting.controller;

import com.sangjie.exception.LoginException;
import com.sangjie.setting.domain.User;
import com.sangjie.setting.service.impl.UserServiceImpl;
import com.sangjie.util.MD5Util;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Resource
    private UserServiceImpl userService;

    @RequestMapping(value = "/settings/user/login.do", method = RequestMethod.POST)
    @ResponseBody
    public Map login(String loginAct, String loginPwd, HttpServletRequest request) throws LoginException {
        Map<String,String> map = new HashMap<>();
        Map res = new HashMap();
        //接收ip地址
        String ip = request.getRemoteAddr();
        loginPwd = MD5Util.getMD5(loginPwd);
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = null;
        user = userService.login(map, ip);
        request.getSession().setAttribute("user", user);
        res.put("success", true);
        //System.out.println(user);
        return res;
    }
}
