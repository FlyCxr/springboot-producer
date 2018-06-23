package com.springboot.service.impl;

import com.springboot.common.BaseServiceImpl;
import com.springboot.dao.DrvierDao;
import com.springboot.entity.DrvierEntity;
import com.springboot.service.DrvierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("all")
@Service("drvierService")
public class DrvierServiceImpl extends BaseServiceImpl<DrvierEntity> implements DrvierService {

    private DrvierDao drvierDao;
    @Resource
    public void setBaseDao(DrvierDao drvierDao){
        super.setBaseDao(drvierDao);
        this.drvierDao = drvierDao;
    }

    @Override
    public DrvierEntity getDrvierByPhoneNum(String phoneNum){
        return drvierDao.getDrvierByPhoneNum(phoneNum);
    }

    @Override
    public List<DrvierEntity> getDrviersByUnit(String buyCarUnit, String useCarUnit) {
        return drvierDao.getDrviersByUnit(buyCarUnit,useCarUnit);
    }

    @Transactional
    @Override
    public int updateDrvier(DrvierEntity entity) {
        return drvierDao.updateDrvier(entity);
    }
}
