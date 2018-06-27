package com.springboot.enums;

/**
 * 互斥对象
 */
@SuppressWarnings("all")
public class MutexElement {

    /**
     * 业务唯一标识
     */
    private String uniqueNum;

    /**
     * 锁定业务对象类型
     */
    private MutexElementType type;

    /**
     * 锁定时间,默认两分钟,两分钟后自动解锁
     */
    private int ttl = 120;

    public MutexElement(String uniqueNum, MutexElementType type) {
        this.uniqueNum = uniqueNum;
        this.type = type;
    }

    public String getUniqueNum() {
        return uniqueNum;
    }

    public void setUniqueNum(String uniqueNum) {
        this.uniqueNum = uniqueNum;
    }

    public MutexElementType getType() {
        return type;
    }

    public void setType(MutexElementType type) {
        this.type = type;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        return "MutexElement{" +
                "uniqueNum='" + uniqueNum + '\'' +
                ", type=" + type +
                ", ttl=" + ttl +
                '}';
    }
}
