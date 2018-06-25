package com.springboot.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@SuppressWarnings("all")
public class BaseEntity {

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

    @Column(name = "VERSION")
    private Integer version;

    @Column(name = "ACTIVE",length = 2)
    private String active;

    @Transient
    @JsonIgnore
    private Integer page;

    @Transient
    @JsonIgnore
    private Integer pageSize;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", createdClientId='" + createdClientId + '\'' +
                ", createdClientType='" + createdClientType + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedTime=" + updatedTime +
                ", updatedClientId='" + updatedClientId + '\'' +
                ", updatedClientType='" + updatedClientType + '\'' +
                ", version=" + version +
                ", active='" + active + '\'' +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
