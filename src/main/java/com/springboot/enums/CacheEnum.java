package com.springboot.enums;

/**
 * 缓存前缀类型枚举
 */
@SuppressWarnings("all")
public enum CacheEnum {

    USER_LOGIN("userLogin_","用户登录"),

    USER_LOGIN_FAILURE("userLoginFailure_","用户登录失败限制次数统计");

    /**
     * 缓存前缀
     */
    private String cachePrefix;

    /**
     * 中文描述
     */
    private String chDesc;

    private CacheEnum(String cachePrefix, String chDesc) {
        this.cachePrefix = cachePrefix;
        this.chDesc = chDesc;
    }

    public String getCachePrefix() {
        return cachePrefix;
    }

    public void setCachePrefix(String cachePrefix) {
        this.cachePrefix = cachePrefix;
    }

    public String getChDesc() {
        return chDesc;
    }

    public void setChDesc(String chDesc) {
        this.chDesc = chDesc;
    }
}
