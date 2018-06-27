package com.springboot.enums;

/**
 * 登录状态枚举
 */
@SuppressWarnings("all")
public enum LoginStatusEnum {

    ONLINE("online","在线"),
    OFFLINE("offline","下线"),
    LOGIN_FAILURE("loginFailure","登录失败"),
    LOGIN_SUCCESS("loginSuccess","登录成功");

    /**
     * 英文标识
     */
    private String loginStatus;

    /**
     * 中文描述
     */
    private String chDesc;

    private LoginStatusEnum(String loginStatus, String chDesc) {
        this.loginStatus = loginStatus;
        this.chDesc = chDesc;
    }

    public String getChDesc() {
        return chDesc;
    }

    public void setChDesc(String chDesc) {
        this.chDesc = chDesc;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }
}
