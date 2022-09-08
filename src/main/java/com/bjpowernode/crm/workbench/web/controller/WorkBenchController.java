package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ClassName:
 * Package:com.bjpowernode.crm.workbench.web.controller
 * author:郭鑫
 */
@Controller
public class WorkBenchController {

    @RequestMapping("/workbench/index.do")
    public String index() {


        return "workbench/index";
    }
}
