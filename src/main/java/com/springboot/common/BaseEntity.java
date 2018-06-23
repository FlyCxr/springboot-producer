package com.springboot.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Transient;

@SuppressWarnings("all")
public class BaseEntity {

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

}
