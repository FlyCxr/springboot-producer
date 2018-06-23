package com.springboot.dao;

import com.springboot.common.BaseDao;
import com.springboot.entity.DrvierEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper统一配置@MapperScan在扫描路径在application类中
@SuppressWarnings("all")
@Repository("drvierDao")
public interface DrvierDao extends BaseDao<DrvierEntity> {

    /**
     * 根据手机号查询司机
     * @param phoneNum
     * @return
     */
    DrvierEntity getDrvierByPhoneNum(@Param("phoneNum") String phoneNum);

    /**
     * 根据购车部门用车部门查询司机
     * @param buyCarUnit
     * @param useCarUnit
     * @return
     */
    List<DrvierEntity> getDrviersByUnit(@Param("buyCarUnit")String buyCarUnit, @Param("useCarUnit")String useCarUnit);

    /**
     * 根据版本号,ID更新司机
     * @param entity
     * @return
     */
    int updateDrvier(@Param("entity")DrvierEntity entity);
}

