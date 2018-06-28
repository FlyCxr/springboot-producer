package com.springboot.service.impl;

import com.springboot.common.BaseServiceImpl;
import com.springboot.dao.UserDao;
import com.springboot.entity.UserEntity;
import com.springboot.enums.CacheEnum;
import com.springboot.enums.LoginStatusEnum;
import com.springboot.enums.UserErrorEnum;
import com.springboot.exception.BusinessException;
import com.springboot.redis.RedisSingleClient;
import com.springboot.service.UserService;
import com.springboot.util.AESUtil;
import com.springboot.util.StringUtil;
import com.springboot.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;

@Service("userService")
@SuppressWarnings("all")
public class UserServiceImpl extends BaseServiceImpl<UserEntity> implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${token.validTime}")
    private String tokenValidTime;

    @Autowired
    private RedisSingleClient redisSingleClient;

    private UserDao userDao;
    @Resource
    public void setBaseDao(UserDao userDao){
        super.setBaseDao(userDao);
        this.userDao = userDao;
    }

    @Override
    public UserVo login(UserEntity request) {
        String userName = request.getUserName();
        String password = request.getPassword();
        //校验用户名是否存在
        UserEntity checkUserName = userDao.getUserByUserName(userName);
        if(checkUserName == null){
            throw new BusinessException(UserErrorEnum.USERNAME_NOT_EXIST.getCode());
        }
        //校验该用户是否已登录
        String checkToken = redisSingleClient.get(CacheEnum.USER_LOGIN.getCachePrefix() + userName);
        UserVo vo = new UserVo();
        if(StringUtil.isBlank(checkToken)){//未登录开始登录
            //校验账号是否已被锁定
            String failureCount = redisSingleClient.get(CacheEnum.USER_LOGIN_FAILURE.getCachePrefix() + userName);
            if(StringUtil.isNotBlank(failureCount) && Integer.valueOf(failureCount) >= 5){
                throw new BusinessException(UserErrorEnum.ACCOUNT_ALREADY_LOCK.getCode());
            }
            UserEntity login = userDao.getUserByLogin(userName,password);
            if(login!=null){
                //登录成功生成token
                String loginToken = AESUtil.encrypt(CacheEnum.USER_LOGIN.getCachePrefix() + userName);
                //设置token到redis
                redisSingleClient.set(CacheEnum.USER_LOGIN.getCachePrefix() + userName,loginToken,30*60);
                //删除失败统计
                redisSingleClient.delete(CacheEnum.USER_LOGIN_FAILURE.getCachePrefix() + userName);
                vo.setLoginStatus(LoginStatusEnum.LOGIN_SUCCESS.getLoginStatus());
                vo.setToken(loginToken);
                //TODO 更新上次登录时间
            }else{
                //登录失败，记录登录失败次数
                if(redisSingleClient.exists(CacheEnum.USER_LOGIN_FAILURE.getCachePrefix() + userName)){
                    String count = redisSingleClient.get(CacheEnum.USER_LOGIN_FAILURE.getCachePrefix() + userName);
                    redisSingleClient.set(CacheEnum.USER_LOGIN_FAILURE.getCachePrefix() + userName,String.valueOf(Integer.valueOf(count)+1),30*60);
                }else{
                    redisSingleClient.set(CacheEnum.USER_LOGIN_FAILURE.getCachePrefix() + userName,"1",30*60);
                }
                throw new BusinessException(UserErrorEnum.USERNAME_OR_PWD_NOT_RIGHT.getCode());
            }
        }else{
            Date now = new Date();
            //已登录,延迟token有效时间
            redisSingleClient.expire(CacheEnum.USER_LOGIN.getCachePrefix() + userName,Integer.valueOf(tokenValidTime));
            //TODO 更新上次登录时间
            vo.setLoginStatus(LoginStatusEnum.LOGIN_SUCCESS.getLoginStatus());
            vo.setToken(checkToken);
        }
        return vo;
    }
}
