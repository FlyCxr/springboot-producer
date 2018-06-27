package com.springboot.dao;

import com.springboot.common.BaseDao;
import com.springboot.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@SuppressWarnings("all")
@Repository("userDao")
public interface UserDao extends BaseDao<UserEntity> {

    /**
     * 根据用户名和密码查询有效用户，登录
     * @param userName
     * @param password
     * @return
     */
    UserEntity getUserByLogin(@Param("userName") String userName, @Param("password")String password);

    /**
     * 校验用户名是否存在
     * @param userName
     * @return
     */
    UserEntity getUserByUserName(@Param("userName")String userName);
}
