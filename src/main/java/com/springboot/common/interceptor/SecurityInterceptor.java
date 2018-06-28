package com.springboot.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.springboot.enums.CacheEnum;
import com.springboot.enums.StatusEnum;
import com.springboot.enums.UserErrorEnum;
import com.springboot.exception.BusinessException;
import com.springboot.redis.RedisSingleClient;
import com.springboot.util.StringUtil;
import com.springboot.vo.BaseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 安全验证拦截器
 * token校验，延长，签名安全校验。
 */
@SuppressWarnings("all")
@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisSingleClient redisSingleClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityInterceptor.class);

    //在请求处理之前进行调用Controller方法调用之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        try{
            String requestURI = request.getRequestURI();
            //登录,注册,忘记密码 不进行拦截token。修改密码还是要拦截的
            if(requestURI.contains("login") || requestURI.contains("register") || requestURI.contains("forgotPwd")){
                String reqSign = request.getParameter("sign");
                //TODO 检验sign
                if(true){
                    return true;
                }else{
                    //签名无效
                    throw new BusinessException(UserErrorEnum.SIGN_FAILURE.getCode());
                }
            }else{
                String reqToken = request.getParameter("token");
                String reqUser = request.getParameter("userName");
                String reqSign = request.getParameter("sign");
                String token = redisSingleClient.get(CacheEnum.USER_LOGIN.getCachePrefix() + reqUser);
                //未登录
                if(StringUtil.isBlank(reqToken)){
                    throw new BusinessException(UserErrorEnum.USER_NOT_LOGIN.getCode());
                }else {
                    //token失效
                    if(StringUtil.isBlank(token) || !token.equals(reqToken)){
                        throw new BusinessException(UserErrorEnum.USER_TOKEN_FAILURE.getCode());
                    }else{
                        //此时延迟token时间30分钟
                        redisSingleClient.expire(CacheEnum.USER_LOGIN.getCachePrefix() + reqUser,30*60);
                        //TODO 检验sign
                        if(true){
                            return true;
                        }else{
                            //签名无效
                            throw new BusinessException(UserErrorEnum.SIGN_FAILURE.getCode());
                        }
                    }
                }
            }
        } catch (BusinessException e){
            e.printStackTrace();
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = null;
            try{
                writer = response.getWriter();
                BaseVo vo = new BaseVo();
                vo.setMsgCode(e.getMessage());
                vo.setStatus(StatusEnum.FAILURE_999.getCode());
                writer.append(JSONObject.toJSONString(vo));
                writer.flush();
            } finally {
                if(writer != null){
                    try{
                        writer.close();
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
