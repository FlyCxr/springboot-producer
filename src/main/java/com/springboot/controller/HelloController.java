package com.springboot.controller;

import com.springboot.entity.DrvierEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController()
@RequestMapping(value = "hello")
@ApiIgnore//使用该注解忽略这个API
public class HelloController {

    /**
     * 获取PATH路径参数
     * @param username
     * @return
     */
    @RequestMapping(value = "get/{username}", method = RequestMethod.GET)
    public String getHello(@PathVariable String username){
        return username;
    }

    /**
     * 获取URL参数,defaultValue为默认值
     * @param username
     * @return
     */
    @RequestMapping(value = "get2", method = RequestMethod.GET)
    public String getHello2(@RequestParam(value="username",defaultValue = "123") String username){
        return username;
    }

    /**
     * 获取URL参数,required默认为true。默认参数必传，不传报错
     * @param username
     * @return
     */
    @RequestMapping(value = "get3", method = RequestMethod.GET)
    public String getHello3(@RequestParam(value="username",required = false) String username){
        return username+"           "+"get3";
    }

    /**
     * Post接收表单参数
     * @return
     */
    @RequestMapping(value = "postForm", method = RequestMethod.POST)
    public String postFormHello(@ModelAttribute("user") DrvierEntity user){
        return user.toString();
    }

    /**
     * Post接收请求体JSON参数
     * @return
     */
    @RequestMapping(value = "postJson", method = RequestMethod.POST,consumes="application/json;charset=UTF-8",produces="application/json;charset=UTF-8")
    public String postJsonHello(@RequestBody DrvierEntity user){
        System.out.println(user.toString());
        return "{\"name\":\"董杨炀\",\"value\":\"123456\"}";
    }

}
