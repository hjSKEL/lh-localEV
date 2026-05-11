/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.domain;

import kr.co.kevit.localcsms.common.util.enumtype.FileManagementOperator;

/**
 * TB_SYFL001
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2020. 7. 31.
 */
public class ManagementFile extends FrameworkFile {

    /**
     * ID
     */
    private String id;

    /**
     * OWNER_ID
     */
    private String owner;

    /**
     * 등록순번
     */
    private int order;
    private Boolean isLast;

    /**
     * SEQ 파일 수정 이력번호
     */
    private int modifySeq;

    /** 파일 상태 */
    private String status;

    private FileManagementOperator managementOperator;

    public int getNextModifySeq() {
        //
        return ++modifySeq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getModifySeq() {
        return modifySeq;
    }

    public void setModifySeq(int modifySeq) {
        this.modifySeq = modifySeq;
    }

    public Boolean isLast() {
        return isLast;
    }

    public void setLast(Boolean isLast) {
        this.isLast = isLast;
    }

    public FileManagementOperator getManagementOperator() {
        return managementOperator;
    }

    public void setManagementOperator(FileManagementOperator managementOperator) {
        this.managementOperator = managementOperator;
    }

    public Boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(Boolean isLast) {
        this.isLast = isLast;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
