package com.springboot.service;

import com.springboot.common.BaseService;
import com.springboot.entity.DrvierEntity;
import java.util.List;

@SuppressWarnings("all")
public interface DrvierService extends BaseService<DrvierEntity>{

    /**
     * 根据手机号查询司机
     * @param phoneNum
     * @return
     */
    DrvierEntity getDrvierByPhoneNum(String phoneNum);

    /**
     * 根据购车部门用车部门查询司机
     * @param buyCarUnit
     * @param useCarUnit
     * @return
     */
    List<DrvierEntity> getDrviersByUnit(String buyCarUnit, String useCarUnit);

    /**
     * 根据ID，版本号更新司机
     * @param entity
     * @return
     */
    int updateDrvier(DrvierEntity entity);
}
