package com.springboot.controller;

import com.springboot.entity.DrvierEntity;
import com.springboot.enums.StatusEnum;
import com.springboot.service.DrvierService;
import com.springboot.vo.BaseVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SuppressWarnings("all")
@Api(value="/drvier", tags="司机模块API")
@RestController()
@RequestMapping(value = "drvier")
public class DrvierController {

    private static final Logger logger = LoggerFactory.getLogger(DrvierController.class);

    @Autowired
    private DrvierService drvierService;

    @ApiOperation(value="根据ID获取司机信息", notes="PATH参数")
    @ApiImplicitParam(name = "id", value = "司机ID", required = true, dataType = "Long", paramType = "PATH")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 401, message = "未认证,调用需要用户通过认证"),
            @ApiResponse(code = 403, message = "不允许的,服务端正常解析和请求,但是调用被回绝或者不被允许"),
            @ApiResponse(code = 404, message = "指定的资源不存在"),
            @ApiResponse(code = 500, message = "系统异常")
    })
    @RequestMapping(value = "getDrvier/{id}", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ResponseEntity<BaseVo> getDrvierById (@PathVariable(value = "id") Long id){
        logger.info("getDrvierById RequestParam [id:"+id+"]");
        BaseVo r = new BaseVo();
        try {
            DrvierEntity entity = drvierService.findById(id);
            r.setData(entity);
        } catch (Exception e) {
            r.setMsgValue(e.getMessage());
            r.setStatus(StatusEnum.ERROR_500.getCode());
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value="根据手机号查询司机", notes="PATH参数")
    @ApiImplicitParam(name = "phoneNum", value = "司机手机号", required = true, dataType = "String", paramType = "PATH")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 401, message = "未认证,调用需要用户通过认证"),
            @ApiResponse(code = 403, message = "不允许的,服务端正常解析和请求,但是调用被回绝或者不被允许"),
            @ApiResponse(code = 404, message = "指定的资源不存在"),
            @ApiResponse(code = 500, message = "系统异常")
    })
    @RequestMapping(value = "getDrvierByPhoneNum", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ResponseEntity<BaseVo> getDrvierByPhoneNum (@RequestParam(value = "phoneNum") String phoneNum) throws Exception{
        logger.info("getDrvierByPhoneNum RequestParam [phoneNum:"+phoneNum+"]");
        BaseVo r = new BaseVo();
        try {
            DrvierEntity entity = drvierService.getDrvierByPhoneNum(phoneNum);
            r.setData(entity);
        } catch (Exception e) {
            r.setMsgValue(e.getMessage());
            r.setStatus(StatusEnum.ERROR_500.getCode());
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value="根据用车部门和购车部门查询司机List", notes="URL参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "buyCarUnit", value = "购车部门", required = true, dataType = "String", paramType = "URL"),
            @ApiImplicitParam(name = "useCarUnit", value = "用车部门", required = true, dataType = "String", paramType = "URL"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 401, message = "未认证,调用需要用户通过认证"),
            @ApiResponse(code = 403, message = "不允许的,服务端正常解析和请求,但是调用被回绝或者不被允许"),
            @ApiResponse(code = 404, message = "指定的资源不存在"),
            @ApiResponse(code = 500, message = "系统异常")
    })
    @RequestMapping(value = "getDrviersByUnit", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ResponseEntity<BaseVo> getDrviersByUnit (@RequestParam(value="buyCarUnit",required = false) String buyCarUnit,
                                               @RequestParam(value="useCarUnit",required = false) String useCarUnit){
        logger.info("getUserById RequestParam [buyCarUnit:"+buyCarUnit+",useCarUnit:"+useCarUnit+"]");
        BaseVo r = new BaseVo();
        try {
            List<DrvierEntity> list = drvierService.getDrviersByUnit(buyCarUnit,useCarUnit);
            r.setData(list);
        } catch (Exception e) {
            r.setMsgValue(e.getMessage());
            r.setStatus(StatusEnum.ERROR_500.getCode());
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value="根据ID和版本号更新司机的用车部门和购车部门", notes="JSON参数")
    @ApiImplicitParam(name = "entity", value = "司机实体", required = true, dataType = "DrvierEntity")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "修改成功"),
            @ApiResponse(code = 201, message = "资源已被提前修改"),
            @ApiResponse(code = 401, message = "未认证,调用需要用户通过认证"),
            @ApiResponse(code = 403, message = "不允许的,服务端正常解析和请求,但是调用被回绝或者不被允许"),
            @ApiResponse(code = 404, message = "指定的资源不存在"),
            @ApiResponse(code = 500, message = "系统异常")
    })
    @RequestMapping(value = "updateDrvier", method = RequestMethod.POST,consumes="application/json;charset=UTF-8",produces="application/json;charset=UTF-8")
    public ResponseEntity<BaseVo> updateDrvier (@RequestBody DrvierEntity entity){
        logger.info("updateDrvier RequestParam ：" + entity.toString());
        BaseVo r = new BaseVo();
        try {
            int a = drvierService.updateDrvier(entity);
            if(a == 2 || a == 0){
                r.setStatus(StatusEnum.FAILURE_201.getCode());
            }
        } catch (Exception e) {
            r.setMsgValue(e.getMessage());
            r.setStatus(StatusEnum.ERROR_500.getCode());
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }
}
