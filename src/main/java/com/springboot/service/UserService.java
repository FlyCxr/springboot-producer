package com.springboot.service;

import com.springboot.common.BaseService;
import com.springboot.entity.UserEntity;
import com.springboot.vo.UserVo;

@SuppressWarnings("all")
public interface UserService extends BaseService<UserEntity> {

    /**
     * 登录接口
     * @param userVo
     * @return
     */
    UserVo login(UserEntity userVo);
}
