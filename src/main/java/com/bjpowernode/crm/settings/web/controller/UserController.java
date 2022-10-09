package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.model.Result;
import com.bjpowernode.crm.commons.utils.MD5Util;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:
 * Package:com.bjpowernode.crm.settings.web.controller
 * author:郭鑫
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin() {
        return "settings/qx/user/login";
    }

    @RequestMapping("settings/qx/user/login.do")
    @ResponseBody
    public Object login(HttpServletRequest request, String loginAct, String loginPwd){

        //密码加密  准备参数
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loginAct",loginAct);
        paramMap.put("loginPwd", MD5Util.getMD5(loginPwd));
        //根据用户密码验证账号是否存在
        User user = userService.queryUserByLoginActAndPwd(paramMap);

        //判断用户是否存在
//        Map<String,Object> retMap = new HashMap<>();
        if(user == null){
//            retMap.put("code",0);
//            retMap.put("message","账号或密码有误");
//            return retMap;
            return Result.fail("账号或密码错误");
        }
        //将用户的信息存入session
        request.getSession().setAttribute("sessionUser",user);
//        retMap.put("code",1);
//        return retMap;
        return Result.success();
    }
}
