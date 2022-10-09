package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * ClassName:UserServiceImpl
 * Package:com.bjpowernode.crm.settings.service.impl
 * Date:2022/9/9 11:04
 * Author:duhao
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> paramMap) {
        return userMapper.selectByLoginActAndPwd(paramMap);
    }

    @Override
    public User queryUserByLoginAct(String loginAct) {
        return userMapper.selectByLoginAct(loginAct);
    }
}
