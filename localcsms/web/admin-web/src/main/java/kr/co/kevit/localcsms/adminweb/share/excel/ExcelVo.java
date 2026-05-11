/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.share.excel;

import java.util.List;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 11. 12.
 */
public class ExcelVo {

    private List<ExcelDataVo> title;

    private List<ExcelDataVo> body;

    /**
     * Get title
     * 
     * @return title
     */
    public List<ExcelDataVo> getTitle() {
        return title;
    }

    /**
     * Set title
     * 
     * @param title
     */
    public void setTitle(List<ExcelDataVo> title) {
        this.title = title;
    }

    /**
     * Get body
     * 
     * @return body
     */
    public List<ExcelDataVo> getBody() {
        return body;
    }

    /**
     * Set body
     * 
     * @param body
     */
    public void setBody(List<ExcelDataVo> body) {
        this.body = body;
    }

}
