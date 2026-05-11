/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.entity.domain;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.string.StringUtils;

/**
 * TB_PDPD002
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 14.
 */
public class ProductPrice implements Serializable{

    /**  */
    private static final long serialVersionUID = 2737895587798685556L;
    
    /**
     * 상품 ID
     * POLO01-0001
     * 11자리
     */
    private String id;
    
    /**
     * 상품 타입
     * PO 상품, HI/LO 고압/저압, 01 순번
     * 6자리
     * PROD_TP
     */
    private String productType;
    
    /**
     * 상품 타입 별 순번
     * SEQ
     */
    private int seq;
    
    /**
     * 시작 년월일
     * STRT_DT
     */
    private String startDt;
    
    /**
     * 종료 년월일
     * END_DT
     */
    private String endDt;
    
    /**
     * 요금
     * FEE
     */
    private double fee = 0.0;
    
    /**
     * 
     */
    private Writer writer;
    
    public void makeProductId() {
        this.id = this.productType + "-" + StringUtils.leftPadding(String.valueOf(seq), '0', 4);
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
     * Get productType
     * @return productType
     */
    public String getProductType() {
        return productType;
    }

    /**
     * Set productType
     * @param productType
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * Get seq
     * @return seq
     */
    public int getSeq() {
        return seq;
    }

    /**
     * Set seq
     * @param seq
     */
    public void setSeq(int seq) {
        this.seq = seq;
    }

    /**
     * Get startDt
     * @return startDt
     */
    public String getStartDt() {
        return startDt;
    }

    /**
     * Set startDt
     * @param startDt
     */
    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    /**
     * Get endDt
     * @return endDt
     */
    public String getEndDt() {
        return endDt;
    }

    /**
     * Set endDt
     * @param endDt
     */
    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    /**
     * Get fee
     * @return fee
     */
    public double getFee() {
        return fee;
    }

    /**
     * Set fee
     * @param fee
     */
    public void setFee(double fee) {
        this.fee = fee;
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
    
    
}
