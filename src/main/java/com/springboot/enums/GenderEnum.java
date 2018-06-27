package com.springboot.enums;

/**
 * 性别枚举
 */
@SuppressWarnings("all")
public enum GenderEnum {

    MAN("man","男"),
    WOMAN("woman","女"),
    UNKNOWN("unknown","未知");

    /**
     * 英文标识
     */
    private String gender;

    /**
     * 中文描述
     */
    private String chDesc;

    private GenderEnum(String gender, String chDesc) {
        this.gender = gender;
        this.chDesc = chDesc;
    }

    public String getChDesc() {
        return chDesc;
    }

    public void setChDesc(String chDesc) {
        this.chDesc = chDesc;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
