/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.domain;

/**
 * 
 * TB_SYFL002
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 14.
 */
public class FileStorage {

    /**
     * ID
     */
    private String id;

    /**
     * SEQ
     */
    private int modifySeq;

    /**
     * LOCATION
     */
    private String location;
    /**
     * NAME
     */
    private String fileName;

    public FileStorage() {
    }

    public FileStorage(String location, String fileName) {
        super();
        this.location = location;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(512);
        buffer.append(location);
        buffer.append(" / ");
        buffer.append(fileName);
        return buffer.toString();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getModifySeq() {
        return modifySeq;
    }

    public void setModifySeq(int modifySeq) {
        this.modifySeq = modifySeq;
    }
}
