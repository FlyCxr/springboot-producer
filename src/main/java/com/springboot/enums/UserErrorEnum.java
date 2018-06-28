package com.springboot.enums;

/**
 * 用户操作错误枚举
 */
@SuppressWarnings("all")
public enum UserErrorEnum {

    SIGN_FAILURE("SIGN_ERROR_001","invalid signature"),

    USERNAME_NOT_EXIST("USER_ERROR_001","userName does not exist"),

    ACCOUNT_ALREADY_LOCK("USER_ERROR_002","account has been locked"),

    USERNAME_OR_PWD_NOT_RIGHT("USER_ERROR_003","userName or password is not correct"),

    USER_NOT_LOGIN("USER_ERROR_004","user not logged in"),

    USER_TOKEN_FAILURE("USER_ERROR_005","token fails, please login again");

    private String code;

    private String msg;

    UserErrorEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
