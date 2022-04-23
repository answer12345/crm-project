package com.sangjie.setting.service;

import com.sangjie.exception.LoginException;
import com.sangjie.setting.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User login(Map<String,String> map, String ip) throws LoginException;

    List<User> getUserList();
}
