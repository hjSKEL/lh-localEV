/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.domain;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.number.NumberConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;

/**
 * 차량 모델 TB_SYCO005
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 9. 10.
 */
public class CarModel implements Serializable {

    /**  */
    private static final long serialVersionUID = -341714564120085821L;

    /**
     * 차량모델아이디 CAR_MODEL_ID CHAR(9) NOT NULL,
     */
    private String carModelId;

    /**
     * 차량이름. CAR_NM VARCHAR(100 BYTE) NOT NULL,
     * 
     */
    private String carName;

    /**
     * 연결코드 LINK_CD CHAR(6 BYTE) NOT NULL,
     */
    private String linkCode;

    /**
     * 주행 가능 거리 DRV_DIS INT
     * 
     */
    private int drivingDistance = NumberConstants.ZERO;

    /**
     * 1kW에 주행 가능 거리 KM_KWH
     */
    private double kmKwh = NumberConstants.ZERO_ZERO;

    /**
     * 베터리용량 BATT_CPCT DOUBLE
     */
    private double batteryCapacity;

    public String getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(String carModelId) {
        this.carModelId = carModelId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }

    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(double batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public int getDrivingDistance() {
        return drivingDistance;
    }

    public void setDrivingDistance(int drivingDistance) {
        this.drivingDistance = drivingDistance;
    }

    public double getKmKwh() {
        return kmKwh;
    }

    public void setKmKwh(double kmKwh) {
        this.kmKwh = kmKwh;
    }

    public void makeCarModelId(String maxCarModelId) {
        String seqStr = StringUtils.leftPadding(String.valueOf(StringUtils.isEmpty(maxCarModelId) ? 1 : maxCarModelId),
                '0', 4);
        this.setCarModelId("CM" + seqStr);
    }
}
