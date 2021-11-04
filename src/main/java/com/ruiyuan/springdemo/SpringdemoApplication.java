package com.ruiyuan.springdemo;

import com.ruiyuan.springdemo.entity.MUser;
import com.ruiyuan.springdemo.service.MUserService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;


@Slf4j
@SpringBootApplication
@MapperScan(value = "com.ruiyuan.springdemo.dao")
public class SpringdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringdemoApplication.class, args);
    }

    @RestController
    public class IndexController {
        @RequestMapping("/hello")
        public String index() {
            return "hello, world";
        }
    }

    @RestController
    public class HelloController {
        @RequestMapping("/helloo")
        public String handle01(@RequestParam("name") String name) {

            log.info("请求进来了....");

            return "Hello, Spring Boot 2!" + "你好：" + name;
        }
    }

    @RestController
    public class delete {
        @Autowired
        MUserService userService;
    }

    @RestController
    @RequestMapping("/user")
    public class UserController {
        @Autowired
        MUserService userService;

        @GetMapping("/{id}")
        public Object test(@PathVariable("id") Long id) {
            return userService.queryById(id);
        }
    }

    @RestController
    @RequestMapping(value = "/postuser", method = RequestMethod.POST)
    public class UserpostController {
        @Autowired
        MUserService userService;

        @PostMapping()
        public Object test(@RequestBody MUser mUser) {
            return userService.insert(mUser);
        }
    }

    @RestController
    @RequestMapping(value = "/deuser", method = RequestMethod.POST)
    public class UserdeController {
        @Autowired
        MUserService userService;

        @DeleteMapping("/{id}")
        public Object test(@PathVariable("id") Long id) {
            return userService.deleteById(id);
        }
    }
}
