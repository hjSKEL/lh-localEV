/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * 배터리 교체 충전소
 *
 * TB : TB_BSCP001
 *
 * 배터리 교환 충전기(BatterySwapStation) 1..N 을 보유하는 사이트.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class BatterySwapPoint implements Serializable {

    /** UID */
    private static final long serialVersionUID = 5742210148839047213L;

    /**
     * PK
     * 충전소ID
     * CP_ID   CHAR(6 BYTE)   NOT NULL,
     */
    private String cpId;

    /**
     * 충전소명
     * CP_NM   VARCHAR(60 BYTE)   NOT NULL,
     */
    private String cpName;

    /**
     * 보유 충전기 수
     * ST_CNT   INT   DEFAULT 0,
     */
    private int stationCount;

    /**
     * 위도
     * LAT   DECIMAL(10,7),
     */
    private BigDecimal latitude;

    /**
     * 경도
     * LON   DECIMAL(11,7),
     */
    private BigDecimal longitude;

    /**
     * 기본주소 (도로명/지번 주소)
     * BSC_ADDR   VARCHAR(200 BYTE),
     */
    private String basicAddress;

    /**
     * 상세주소 (건물 호수 등)
     * DTL_ADDR   VARCHAR(200 BYTE),
     */
    private String detailAddress;

    /**
     * 상세위치 (지하층/구역 등 부가 안내)
     * DTL_LOC   VARCHAR(200 BYTE),
     */
    private String detailLocation;

    /**
     * 개소 운영 시작일
     * OPEN_DT   DATETIME,
     */
    private Date openedFrom;

    /**
     * 폐소일 (NULL 이면 운영중)
     * CLOSE_DT   DATETIME,
     */
    private Date closedDate;

    /**
     * 등록정보
     */
    private Writer writer;

    /**
     * Object Relation — 보유 교환 충전기 목록
     */
    private List<BatterySwapStation> stations;

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public int getStationCount() {
        return stationCount;
    }

    public void setStationCount(int stationCount) {
        this.stationCount = stationCount;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getBasicAddress() {
        return basicAddress;
    }

    public void setBasicAddress(String basicAddress) {
        this.basicAddress = basicAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getDetailLocation() {
        return detailLocation;
    }

    public void setDetailLocation(String detailLocation) {
        this.detailLocation = detailLocation;
    }

    public Date getOpenedFrom() {
        return openedFrom;
    }

    public void setOpenedFrom(Date openedFrom) {
        this.openedFrom = openedFrom;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public List<BatterySwapStation> getStations() {
        return stations;
    }

    public void setStations(List<BatterySwapStation> stations) {
        this.stations = stations;
    }

}
