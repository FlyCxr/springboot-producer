package com.springboot.controller;

import com.springboot.enums.StatusEnum;
import com.springboot.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController()
@RequestMapping(value = "kafka")
@ApiIgnore//使用该注解忽略这个API
@SuppressWarnings("all")
public class kafkaController {

    @Value("${kafka.test.topic}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping(value = "send",method = RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ResponseEntity<BaseVo> send(@RequestParam(value="data",defaultValue = "data123") String data){
        BaseVo r = new BaseVo();
        try {
            kafkaTemplate.send(topicName, data);
        } catch (Exception e) {
            r.setMsgValue(e.getMessage());
            r.setStatus(StatusEnum.ERROR_500.getCode());
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

}
