package com.springboot.service.impl;

import com.springboot.common.BaseServiceImpl;
import com.springboot.dao.DrvierDao;
import com.springboot.entity.DrvierEntity;
import com.springboot.enums.MutexElement;
import com.springboot.enums.MutexElementType;
import com.springboot.redis.BusinessLockService;
import com.springboot.service.DrvierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("all")
@Service("drvierService")
public class DrvierServiceImpl extends BaseServiceImpl<DrvierEntity> implements DrvierService {

    @Autowired
    private BusinessLockService businessLockService;

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

    /**
     * redis锁演示，这里用version乐观锁已可以
     * @param entity
     * @return
     */
    @Transactional
    @Override
    public int updateDrvier(DrvierEntity entity) {
        String a = entity.getId()+"";
        int i = 2;
        MutexElement mutexElement = new MutexElement(a, MutexElementType.UPDATE_DRVIER);
        if(businessLockService.lock(mutexElement,0)){
            i = drvierDao.updateDrvier(entity);
            businessLockService.unlock(mutexElement);
        }
        return i;
    }
}
