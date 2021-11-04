package com.ruiyuan.springdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {


    @RequestMapping("/")
    public String index() {
        System.out.println("index方法被调用");
        return "index";
    }

    @PostMapping("login")
    public String login(String username, String password) {
        System.out.println("login方法被调用");
        System.out.println("login登录名：" + username + "密码：" + password);
        return "redirect:/main";
    }

    @RequestMapping(value = "/main")
    public String main(){
        System.out.println("main方法被调用");
        return "main";
    }

}
