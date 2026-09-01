/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.entity.shared;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 8. 31.
 */
public class ProductSearchCond extends PageCriteria implements Serializable{

    /**  */
    private static final long serialVersionUID = 6883902234986959136L;

    /**
     * 상품명
     */
    private String name;

    /**
     * 정렬 기준. A=요금제ID ASC, B=요금제ID DESC, C=요금제명 ASC, D=요금제명 DESC. 비어있으면 요금제명 오름차순(기본).
     */
    private String sortOrder;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

}
