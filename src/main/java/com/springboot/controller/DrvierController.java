package com.springboot.controller;

import com.springboot.entity.DrvierEntity;
import com.springboot.enums.StatusEnum;
import com.springboot.exception.BusinessException;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(DrvierController.class);

    @Autowired
    private DrvierService drvierService;

    @ApiOperation(value="根据ID获取司机信息", notes="PATH参数")
    @ApiImplicitParam(name = "id", value = "司机ID", required = true, dataType = "Long", paramType = "PATH")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "系统异常"),
            @ApiResponse(code = 666, message = "业务成功"),
            @ApiResponse(code = 999, message = "业务失败")
    })
    @RequestMapping(value = "getDrvier/{id}", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ResponseEntity<BaseVo> getDrvierById (@PathVariable(value = "id") Long id){
        BaseVo r = new BaseVo();
        try {
            DrvierEntity entity = drvierService.findById(id);
            r.setData(entity);
        } catch (BusinessException e){
            r.setMsgCode(e.getMessage());
            r.setStatus(StatusEnum.FAILURE_999.getCode());
            e.printStackTrace();
        } catch (Exception e) {
            r.setStatus(StatusEnum.ERROR_500.getCode());
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value="根据手机号查询司机", notes="PATH参数")
    @ApiImplicitParam(name = "phoneNum", value = "司机手机号", required = true, dataType = "String", paramType = "PATH")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "系统异常"),
            @ApiResponse(code = 666, message = "业务成功"),
            @ApiResponse(code = 999, message = "业务失败")
    })
    @RequestMapping(value = "getDrvierByPhoneNum", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ResponseEntity<BaseVo> getDrvierByPhoneNum (@RequestParam(value = "phoneNum") String phoneNum) throws Exception{
        BaseVo r = new BaseVo();
        try {
            DrvierEntity entity = drvierService.getDrvierByPhoneNum(phoneNum);
            r.setData(entity);
        } catch (BusinessException e){
            r.setMsgCode(e.getMessage());
            r.setStatus(StatusEnum.FAILURE_999.getCode());
            e.printStackTrace();
        } catch (Exception e) {
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
            @ApiResponse(code = 500, message = "系统异常"),
            @ApiResponse(code = 666, message = "业务成功"),
            @ApiResponse(code = 999, message = "业务失败")
    })
    @RequestMapping(value = "getDrviersByUnit", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ResponseEntity<BaseVo> getDrviersByUnit (@RequestParam(value="buyCarUnit",required = false) String buyCarUnit,
                                               @RequestParam(value="useCarUnit",required = false) String useCarUnit){
        BaseVo r = new BaseVo();
        try {
            List<DrvierEntity> list = drvierService.getDrviersByUnit(buyCarUnit,useCarUnit);
            r.setData(list);
        } catch (BusinessException e){
            r.setMsgCode(e.getMessage());
            r.setStatus(StatusEnum.FAILURE_999.getCode());
            e.printStackTrace();
        } catch (Exception e) {
            r.setStatus(StatusEnum.ERROR_500.getCode());
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value="根据ID和版本号更新司机的用车部门和购车部门", notes="JSON参数")
    @ApiImplicitParam(name = "entity", value = "司机实体", required = true, dataType = "DrvierEntity")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "系统异常"),
            @ApiResponse(code = 666, message = "业务成功"),
            @ApiResponse(code = 999, message = "业务失败")
    })
    @RequestMapping(value = "updateDrvier", method = RequestMethod.POST,consumes="application/json;charset=UTF-8",produces="application/json;charset=UTF-8")
    public ResponseEntity<BaseVo> updateDrvier (@RequestBody DrvierEntity entity){
        BaseVo r = new BaseVo();
        try {
            int a = drvierService.updateDrvier(entity);
            if(a == 2 || a == 0){
                r.setMsgCode("DRVIER_ERROR_001");//司机已被更改
                r.setStatus(StatusEnum.FAILURE_999.getCode());
            }
        } catch (BusinessException e){
            r.setMsgCode(e.getMessage());
            r.setStatus(StatusEnum.FAILURE_999.getCode());
            e.printStackTrace();
        } catch (Exception e) {
            r.setStatus(StatusEnum.ERROR_500.getCode());
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }
}
