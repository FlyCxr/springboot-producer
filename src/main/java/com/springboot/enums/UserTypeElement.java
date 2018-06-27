package com.springboot.enums;

/**
 * 用户类型枚举
 */
@SuppressWarnings("all")
public enum UserTypeElement {

    ADMIN("admin","系统管理员"),
    USER("user","普通用户"),
    DRIVER("driver","司机用户");

    /**
     * 英文标识
     */
    private String userType;

    /**
     * 中文描述
     */
    private String chDesc;

    private UserTypeElement(String userType, String chDesc) {
        this.userType = userType;
        this.chDesc = chDesc;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getChDesc() {
        return chDesc;
    }

    public void setChDesc(String chDesc) {
        this.chDesc = chDesc;
    }
}
