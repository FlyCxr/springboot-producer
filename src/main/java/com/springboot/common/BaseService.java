package com.springboot.common;

import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("all")
public interface BaseService<T>{

    /**
     * 保存新增实体
     * @param entity
     * @return
     */
    Integer add(T entity);

    /**
     * 根据ID单条或者批量删除实体
     * @param ids
     * @param clazz
     * @return
     */
    Integer delete(List<String> ids, Class clazz);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    T findById(Serializable id);

    /**
     * 查询所有
     * @return
     */
    List<T> getAll();

    /**
     * 分页
     * @param entity
     * @return
     */
    JSONObject getPage(T entity);

}
