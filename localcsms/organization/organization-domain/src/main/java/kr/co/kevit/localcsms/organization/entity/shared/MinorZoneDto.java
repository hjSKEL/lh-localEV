/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.shared;

import kr.co.kevit.localcsms.organization.entity.domain.MinorZone;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 20.
 */
public class MinorZoneDto extends MinorZone{

    /**  */
    private static final long serialVersionUID = -2264451477196865305L;
    
    /**
     */
    private String sidoCodeName;
    
    /**
     */
    private String siguCodeName;
    
    /**
     */
    private String employeeName;
    
    /**
     * 
     */
    private String companyName;

    /**
     * Get sidoCodeName
     * @return sidoCodeName
     */
    public String getSidoCodeName() {
        return sidoCodeName;
    }

    /**
     * Set sidoCodeName
     * @param sidoCodeName
     */
    public void setSidoCodeName(String sidoCodeName) {
        this.sidoCodeName = sidoCodeName;
    }

    /**
     * Get siguCodeName
     * @return siguCodeName
     */
    public String getSiguCodeName() {
        return siguCodeName;
    }

    /**
     * Set siguCodeName
     * @param siguCodeName
     */
    public void setSiguCodeName(String siguCodeName) {
        this.siguCodeName = siguCodeName;
    }

    /**
     * Get employeeName
     * @return employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Set employeeName
     * @param employeeName
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * Get companyName
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Set companyName
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
}
