package com.bjpowernode.crm.commons.model;

import java.util.HashMap;

/**
 * ClassName:Result
 * Package:com.bjpowernode.crm.commons.model
 * Date:2022/10/9 15:02
 * Author:duhao
 */
public class Result extends HashMap<String,Object> {

    public static Result success(){
        Result result = new Result();
        result.put("code",1);
        return result;
    }

    public static Result fail(String message){
        Result result = new Result();
        result.put("code",0);
        result.put("message",message);
        return result;
    }
}
