/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype;

/**
 * 
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 14.
 */
public enum PropertyKey {
    //
    KEVIT_LOGIN_CO("kevit.login.company"),

    RootFolderLocation("file.rootFolder"),
    TmpFolderLocation("file.tmpFolder"),
    StorageFolderLocation("file.storageFolder"),
    CheckListFolderLocation("file.checkListFolder"),
    OcppFolderLocation("file.ocpp")
    ;

    private String key;

    private PropertyKey(String key) {

        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
