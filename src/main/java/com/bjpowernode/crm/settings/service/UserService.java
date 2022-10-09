package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.User;

import java.util.Map;

/**
 * ClassName:UserService
 * Package:com.bjpowernode.crm.settings.service
 * Date:2022/9/9 11:03
 * Author:duhao
 */
public interface UserService{
    User queryUserByLoginActAndPwd(Map<String, Object> paramMap);

    User queryUserByLoginAct(String loginAct);
}
