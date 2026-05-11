/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.domain;

/**
 * 
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 14.
 */
public class FrameworkFile {

    /**
     * FILE_NM
     */
    private String name;

    /**
     * FILE_KIND
     */
    private String kind;

    /**
     * FILE_SIZE
     */
    private long size;

    private FileStorage storage;

    private String register;

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(256);

        return buffer.append(name).append(" / ").append(kind).append(" / ").append(size).append(" / ")
                .append(storage.getLocation()).toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public FileStorage getStorage() {
        return storage;
    }

    public void setStorage(FileStorage storage) {
        this.storage = storage;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }
}
