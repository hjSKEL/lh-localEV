/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.entity.domain;

import java.io.Serializable;
import java.util.List;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * 상품
 * TB_PDPD001
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 14.
 */
public class Product implements Serializable{

    /**  */
    private static final long serialVersionUID = -7421574555053967006L;
    
    /**
     * 상품 타입
     * PO 상품, HI/LO 고압/저압, 01 순번
     * 6자리
     */
    private String id;
    
    private String name;
    
    private Writer writer;
    
    /**
     * Relation
     */
    private List<ProductPrice> prices;

    /**
     * Get name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Set id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get writer
     * @return writer
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * Set writer
     * @param writer
     */
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Get prices
     * @return prices
     */
    public List<ProductPrice> getPrices() {
        return prices;
    }

    /**
     * Set prices
     * @param prices
     */
    public void setPrices(List<ProductPrice> prices) {
        this.prices = prices;
    }
    
}
