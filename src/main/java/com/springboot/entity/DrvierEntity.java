package com.springboot.entity;

import com.springboot.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "DRIVER")
@SuppressWarnings("all")
public class DrvierEntity extends BaseEntity{

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "createdTime")
    private Date createdTime;

    @Column(name = "createdClientId")
    private String createdClientId;

    @Column(name = "createdClientType")
    private String createdClientType;

    @Column(name = "updatedBy")
    private String updatedBy;

    @Column(name = "updatedTime")
    private Date updatedTime;

    @Column(name = "updatedClientId")
    private String updatedClientId;

    @Column(name = "updatedClientType")
    private String updatedClientType;

    @Column(name = "BUY_CAR_UNIT")
    private String buyCarUnit;

    @Column(name = "DRVIER_NAME")
    private String drvierName;

    @Column(name = "DRVIER_PHONE")
    private String drvierPhone;

    @Column(name = "USE_CAR_UNIT")
    private String useCarUnit;

    @Column(name = "VERSION")
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedClientId() {
        return createdClientId;
    }

    public void setCreatedClientId(String createdClientId) {
        this.createdClientId = createdClientId;
    }

    public String getCreatedClientType() {
        return createdClientType;
    }

    public void setCreatedClientType(String createdClientType) {
        this.createdClientType = createdClientType;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedClientId() {
        return updatedClientId;
    }

    public void setUpdatedClientId(String updatedClientId) {
        this.updatedClientId = updatedClientId;
    }

    public String getUpdatedClientType() {
        return updatedClientType;
    }

    public void setUpdatedClientType(String updatedClientType) {
        this.updatedClientType = updatedClientType;
    }

    public String getBuyCarUnit() {
        return buyCarUnit;
    }

    public void setBuyCarUnit(String buyCarUnit) {
        this.buyCarUnit = buyCarUnit;
    }

    public String getDrvierName() {
        return drvierName;
    }

    public void setDrvierName(String drvierName) {
        this.drvierName = drvierName;
    }

    public String getDrvierPhone() {
        return drvierPhone;
    }

    public void setDrvierPhone(String drvierPhone) {
        this.drvierPhone = drvierPhone;
    }

    public String getUseCarUnit() {
        return useCarUnit;
    }

    public void setUseCarUnit(String useCarUnit) {
        this.useCarUnit = useCarUnit;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "DrvierEntity{" +
                "id=" + id +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", createdClientId='" + createdClientId + '\'' +
                ", createdClientType='" + createdClientType + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedTime=" + updatedTime +
                ", updatedClientId='" + updatedClientId + '\'' +
                ", updatedClientType='" + updatedClientType + '\'' +
                ", buyCarUnit='" + buyCarUnit + '\'' +
                ", drvierName='" + drvierName + '\'' +
                ", drvierPhone='" + drvierPhone + '\'' +
                ", useCarUnit='" + useCarUnit + '\'' +
                ", version=" + version +
                '}';
    }
}
