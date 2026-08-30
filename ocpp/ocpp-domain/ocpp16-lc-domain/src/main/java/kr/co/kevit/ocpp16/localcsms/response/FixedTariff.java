/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.localcsms.response;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 12. 1.
 */
public class FixedTariff {
    
    /**
     * Optional
     */
    private Integer connectorId;
    
    /**
     * Required
     */
    private String timestamp;
    
    /**
     * Optional. 
     * This contains the identifier that needs to be authorized.
     * The blank is guest, id is member's id.
     */
    private String idTag;
    
    /**
     * 
     */
    private double price;

    /**
     * Get connectorId
     * @return connectorId
     */
    public Integer getConnectorId() {
        return connectorId;
    }

    /**
     * Set connectorId
     * @param connectorId
     */
    public void setConnectorId(Integer connectorId) {
        this.connectorId = connectorId;
    }

    /**
     * Get timestamp
     * @return timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Set timestamp
     * @param timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get idTag
     * @return idTag
     */
    public String getIdTag() {
        return idTag;
    }

    /**
     * Set idTag
     * @param idTag
     */
    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }

    /**
     * Get price
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Set price
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
}
