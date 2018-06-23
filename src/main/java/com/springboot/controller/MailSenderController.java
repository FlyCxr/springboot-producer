package com.springboot.controller;

import com.springboot.enums.StatusEnum;
import com.springboot.vo.BaseVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController()
@RequestMapping(value = "mail")
@Api(value="/mail", tags="发送邮件API")
public class MailSenderController {

    private static final Logger logger = LoggerFactory.getLogger(MailSenderController.class);

    @Autowired
    private JavaMailSender mailSender;

    @ApiOperation(value="发送邮件TEST", notes="无参GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "发送成功"),
            @ApiResponse(code = 201, message = "资源已被提前修改"),
            @ApiResponse(code = 401, message = "未认证,调用需要用户通过认证"),
            @ApiResponse(code = 403, message = "不允许的,服务端正常解析和请求,但是调用被回绝或者不被允许"),
            @ApiResponse(code = 404, message = "指定的资源不存在"),
            @ApiResponse(code = 500, message = "系统异常")
    })
    @RequestMapping(value = "sendMail", method = RequestMethod.GET)
    public ResponseEntity<BaseVo> sendMail(){
        BaseVo r = new BaseVo();
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("15000814726@163.com");
            message.setTo("757617714@qq.com");
            message.setSubject("主题：简单邮件");
            message.setText("简单邮件内容");
            mailSender.send(message);
            logger.info("sendMail ok");
        }catch (Exception e){
            r.setStatus(StatusEnum.ERROR_500.getCode());
            r.setMsgValue(e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }
}
