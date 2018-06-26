package com.springboot.entity;

import com.springboot.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "DRIVER")
@SuppressWarnings("all")
public class DrvierEntity extends BaseEntity implements Serializable {

    @Column(name = "BUY_CAR_UNIT")
    private String buyCarUnit;

    @Column(name = "DRVIER_NAME")
    private String drvierName;

    @Column(name = "DRVIER_PHONE")
    private String drvierPhone;

    @Column(name = "USE_CAR_UNIT")
    private String useCarUnit;

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

    @Override
    public String toString() {
        return "DrvierEntity{" +
                "buyCarUnit='" + buyCarUnit + '\'' +
                ", drvierName='" + drvierName + '\'' +
                ", drvierPhone='" + drvierPhone + '\'' +
                ", useCarUnit='" + useCarUnit + '\'' +
                '}';
    }
}
