package com.springboot.common;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.io.Serializable;
import java.util.List;
import tk.mybatis.mapper.entity.Example;

@SuppressWarnings("all")
public class BaseServiceImpl<T> implements BaseService<T> {

    protected BaseDao<T> baseDao;

    public void setBaseDao(BaseDao<T> baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public Integer add(T entity) {
        return baseDao.insert(entity);
    }

    @Override
    public Integer delete(List<String> ids, Class clazz) {
        Example example = new Example(clazz);
        example.createCriteria().andIn("id", ids);
        return baseDao.deleteByExample(example);
    }

    @Override
    public T findById(Serializable id) {
        return baseDao.selectByPrimaryKey(id);
    }

    @Override
    public List<T> getAll() {
        return baseDao.selectAll();
    }

    @Override
    public JSONObject getPage(T entity) {
        BaseEntity bd = (BaseEntity) entity;
        Page<Object> page = PageHelper.startPage(bd.getPage()==null?1:bd.getPage(), bd.getPageSize()==null?10:bd.getPageSize());
        JSONObject result = new JSONObject();
        result.put("items",baseDao.select(entity));
        result.put("totalCount",page.getTotal());
        return result;
    }
}
