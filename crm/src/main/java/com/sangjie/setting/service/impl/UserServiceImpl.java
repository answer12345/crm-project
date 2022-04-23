package com.sangjie.setting.service.impl;

import com.sangjie.exception.LoginException;
import com.sangjie.setting.dao.UserDao;
import com.sangjie.setting.domain.User;
import com.sangjie.setting.service.UserService;
import com.sangjie.util.DateTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User login(Map<String, String> map, String ip) throws LoginException {
        User user = null;
        user = userDao.login(map);

        if (user == null) {
            throw new LoginException("账号或者密码错误");
        }

        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime) < 0) {
            throw new LoginException("账号以失效");
        }

        String lockState = user.getLockState();
        if ("0".equals(lockState)) {
            throw new LoginException("账号以锁定");
        }

        String allowIps = user.getAllowIps();
        if (allowIps != null && !"".equals(allowIps)) {
            if (!allowIps.contains(ip)) {
                throw new LoginException("ip地址受限，请联系管理员");
            }
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        List res = userDao.getUserList();
        return res;
    }
}
