/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.file;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 7. 31.
 */
public enum FileType {
    
    PNG("png", ".png"),
    JPG("jpg", ".jpg"),
    PDF("pdf", ".pdf")
    ;
    
    private String type;
    private String fileExtension;
    
    FileType(String type, String fileExtension){
        this.type = type;
        this.fileExtension = fileExtension;
    }

    public String getType() {
        return type;
    }

    public String getFileExtension() {
        return fileExtension;
    }  

}
