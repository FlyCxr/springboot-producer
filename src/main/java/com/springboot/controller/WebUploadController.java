package com.springboot.controller;

import com.springboot.service.WebUploadService;
import com.springboot.util.WebUploadFileUtil;
import com.springboot.vo.WebUploadVo;
import io.swagger.annotations.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("all")
@Api(value="/webupload", tags="WebUpload分片上传")
@RequestMapping(value = "webupload")
@Controller
public class WebUploadController {

    @Autowired
    private WebUploadService webUploadService;

    @ApiIgnore//使用该注解忽略这个API
    @RequestMapping(value="index",method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @ApiIgnore//使用该注解忽略这个API
    @RequestMapping(value="getFormats",method = RequestMethod.GET)
    public Object getFormats(){
        //TODO 限制上传文档格式
        return null;
    }

    @ApiOperation(value="校验文件是否存在，分片是否存在", notes="表单提交")
    @ApiImplicitParam(name = "form-data", value = "表单提交", required = true, dataType = "WebUploadVo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "校验成功"),
            @ApiResponse(code = 201, message = "资源已被提前修改"),
            @ApiResponse(code = 401, message = "未认证,调用需要用户通过认证"),
            @ApiResponse(code = 403, message = "不允许的,服务端正常解析和请求,但是调用被回绝或者不被允许"),
            @ApiResponse(code = 404, message = "指定的资源不存在"),
            @ApiResponse(code = 500, message = "系统异常")
    })
    @RequestMapping(value = "check", method = RequestMethod.POST)
    @ResponseBody
    public Object check(@ModelAttribute("form-data")WebUploadVo vo, HttpServletRequest request) {
        return webUploadService.check(vo);
    }

    @ApiOperation(value="上传接口", notes="表单提交")
    @ApiImplicitParam(name = "form-data", value = "表单提交", required = true, dataType = "WebUploadVo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "校验成功"),
            @ApiResponse(code = 201, message = "资源已被提前修改"),
            @ApiResponse(code = 401, message = "未认证,调用需要用户通过认证"),
            @ApiResponse(code = 403, message = "不允许的,服务端正常解析和请求,但是调用被回绝或者不被允许"),
            @ApiResponse(code = 404, message = "指定的资源不存在"),
            @ApiResponse(code = 500, message = "系统异常")
    })
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(@ModelAttribute("form-data")WebUploadVo vo,HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile multipartFile = files.get(0);
        String fileName = multipartFile.getName();
        File f = new File(multipartFile.getOriginalFilename());
        Object response;
        try{
            WebUploadFileUtil.inputStreamToFile(multipartFile.getInputStream(),f);
            if(StringUtils.isBlank(request.getParameter("chunk"))){
                response = webUploadService.unChunkUpload(f,vo);
            }else{
                response = webUploadService.chunkUpload(f,vo);
            }
        }catch(IOException e){
            e.printStackTrace();
            return null;
        } finally {
            File del = new File(f.toURI());
            if(del.exists()){
                f.delete();
            }
        }
        return response;
    }
}
