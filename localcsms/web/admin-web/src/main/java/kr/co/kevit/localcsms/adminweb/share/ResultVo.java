/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.share;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 9.
 */
public class ResultVo implements Serializable{
    
    /**  */
    private static final long serialVersionUID = -3581811993871858440L;

    private String result1;
    
    private String result2;
    
    private String result3;
    
    private String result4;
    
    private List<String> result1s;
    
    private List<String> result2s;

    private List<String> result3s;

    /**
     * Get result1
     * @return result1
     */
    public String getResult1() {
        return result1;
    }

    /**
     * Set result1
     * @param result1
     */
    public void setResult1(String result1) {
        this.result1 = result1;
    }

    /**
     * Get result2
     * @return result2
     */
    public String getResult2() {
        return result2;
    }

    /**
     * Set result2
     * @param result2
     */
    public void setResult2(String result2) {
        this.result2 = result2;
    }

    /**
     * Get result3
     * @return result3
     */
    public String getResult3() {
        return result3;
    }

    /**
     * Set result3
     * @param result3
     */
    public void setResult3(String result3) {
        this.result3 = result3;
    }

    /**
     * Get result4
     * @return result4
     */
    public String getResult4() {
        return result4;
    }

    /**
     * Set result4
     * @param result4
     */
    public void setResult4(String result4) {
        this.result4 = result4;
    }

    /**
     * Get result1s
     * @return result1s
     */
    public List<String> getResult1s() {
        return result1s;
    }

    /**
     * Set result1s
     * @param result1s
     */
    public void setResult1s(List<String> result1s) {
        this.result1s = result1s;
    }

    /**
     * Get result2s
     * @return result2s
     */
    public List<String> getResult2s() {
        return result2s;
    }

    /**
     * Set result2s
     * @param result2s
     */
    public void setResult2s(List<String> result2s) {
        this.result2s = result2s;
    }

    /**
     * Get result3s
     * @return result3s
     */
    public List<String> getResult3s() {
        return result3s;
    }

    /**
     * Set result3s
     * @param result3s
     */
    public void setResult3s(List<String> result3s) {
        this.result3s = result3s;
    }

}
