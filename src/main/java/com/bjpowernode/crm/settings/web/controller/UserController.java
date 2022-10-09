package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.model.Result;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.MD5Util;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:
 * Package:com.bjpowernode.crm.settings.web.controller
 * author:郭鑫
 */
@Controller
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin() {
        return "settings/qx/user/login";
    }

    @RequestMapping("settings/qx/user/login.do")
    @ResponseBody
    public Object login(HttpServletRequest request, HttpServletResponse response,String loginAct, String loginPwd,Boolean isRemPwd) throws ParseException {

        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        User userDetail = userService.queryUserByLoginAct(loginAct);

        if(userDetail.getLoginAct() == null){
            return Result.fail("账号或密码不正确");
        }
        BoundValueOperations boundValueOperations = redisTemplate.boundValueOps(loginAct);
        Integer errorCount = (Integer) boundValueOperations.get();

        if(errorCount != null && errorCount == 12){
          return Result.fail("输入次数已达上限,请明日再试");
        }
        //密码加密  准备参数
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loginAct",loginAct);
        paramMap.put("loginPwd", MD5Util.getMD5(loginPwd));
        //根据用户密码验证账号是否存在
        User user = userService.queryUserByLoginActAndPwd(paramMap);

        //判断用户是否存在
//        Map<String,Object> retMap = new HashMap<>();
        if(user == null){
            boundValueOperations.increment(1);
            boundValueOperations.expire(DateUtils.getRemainSecondsOneDay(new Date()), TimeUnit.SECONDS);
//            retMap.put("code",0);
//            retMap.put("message","账号或密码有误");
//            return retMap;
            return Result.fail("账号或密码错误");
        }

        //验证用户账号是否失效
        if(!"".equals(user.getExpireTime()) && (new Date().compareTo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(user.getExpireTime()))) > 0){
            return Result.fail("账号已失效");
        }

        //判断账户锁定状态
        if(!"".equals(user.getLockState()) && (Integer.parseInt(user.getLockState()) == 0)){
            return Result.fail("账号已被锁定,无法登陆");
        }
        //判断用户ip地址是否被允许访问
        //获取用户的IP地址
        String ipAddress = request.getRemoteHost();
        if(!"".equals(user.getAllowIps()) && (!user.getAllowIps().contains(ipAddress))){
            return Result.fail("当前ip地址不允许访问");
        }
        //判断用户是否勾选记住我
        if(isRemPwd){
            Cookie c1 = new Cookie("loginAct",loginAct);
            c1.setMaxAge(60*60*24*7);
            response.addCookie(c1);
            Cookie c2 = new Cookie("loginPwd",loginPwd);
            c2.setMaxAge(60*60*24*7);
            response.addCookie(c2);
        }
        //将用户的信息存入session
        request.getSession().setAttribute("sessionUser",user);
//        retMap.put("code",1);
//        return retMap;
        return Result.success();
    }

    @RequestMapping("settings/qx/user/logout.do")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }
}
