package com.springboot.controller;

import com.springboot.entity.UserEntity;
import com.springboot.enums.StatusEnum;
import com.springboot.service.UserService;
import com.springboot.vo.BaseVo;
import com.springboot.vo.UserVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("all")
@Api(value="/user", tags="用户模块API")
@RestController()
@RequestMapping(value = "user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value="登录接口", notes="JSON参数")
    @ApiImplicitParam(name = "entity", value = "用户实体", required = true, dataType = "DrvierEntity")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "登录成功"),
            @ApiResponse(code = 201, message = "资源已被提前修改"),
            @ApiResponse(code = 401, message = "未认证,调用需要用户通过认证"),
            @ApiResponse(code = 403, message = "不允许的,服务端正常解析和请求,但是调用被回绝或者不被允许"),
            @ApiResponse(code = 404, message = "指定的资源不存在"),
            @ApiResponse(code = 500, message = "系统异常")
    })
    @RequestMapping(value = "login", method = RequestMethod.POST,consumes="application/json;charset=UTF-8",produces="application/json;charset=UTF-8")
    public ResponseEntity<BaseVo> login (@RequestBody UserEntity entity){
        logger.info("login RequestParam ：" + entity.toString());
        BaseVo r = new BaseVo();
        try {
            UserVo vo = userService.login(entity);
            r.setData(vo);
        } catch (Exception e) {
            r.setMsgValue(e.getMessage());
            r.setStatus(StatusEnum.ERROR_500.getCode());
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }
}
