package com.springboot.enums;

/**
 * 业务互斥类型枚举
 */
@SuppressWarnings("all")
public enum MutexElementType {

    UPDATE_DRVIER("drvierId_","修改司机手机号"),;

    /**
     * 业务前缀
     */
    private String prefix;

    /**
     * 具体业务操作描述
     */
    private String name;

    private MutexElementType(String prefix, String name) {
        this.prefix = prefix;
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MutexElementType{" +
                "prefix='" + prefix + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
