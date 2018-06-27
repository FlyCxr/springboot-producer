package com.springboot.entity;

import com.springboot.common.BaseEntity;
import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户表实体
 */
@SuppressWarnings("all")
public class UserEntity extends BaseEntity implements Serializable {

    @Column(name = "userName")
    private String userName;

    @Column(name = "nickName")
    private String nickName;

    @Column(name = "password")
    private String password;

    @Column(name = "phoneNum")
    private String phoneNum;

    @Column(name = "qqNum")
    private String qqNum;

    @Column(name = "wecatNum")
    private String wecatNum;

    @Column(name = "weiboNum")
    private String weiboNum;

    @Column(name = "email")
    private String email;

    @Column(name = "userType")
    private String userType;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "lastLoginTime")
    private Date lastLoginTime;

    @Column(name = "loginStatus")
    private String loginStatus;

    @Column(name = "registerTime")
    private Date registerTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getQqNum() {
        return qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    public String getWecatNum() {
        return wecatNum;
    }

    public void setWecatNum(String wecatNum) {
        this.wecatNum = wecatNum;
    }

    public String getWeiboNum() {
        return weiboNum;
    }

    public void setWeiboNum(String weiboNum) {
        this.weiboNum = weiboNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
}
