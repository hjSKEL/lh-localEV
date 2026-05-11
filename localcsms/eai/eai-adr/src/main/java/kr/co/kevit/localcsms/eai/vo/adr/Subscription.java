/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.adr;

import java.util.List;

/**
 * An object created by a client to receive notification of operations on objects.
{
    "clientName": "kevit_der_pilot_ven_name",
    "programID": "program-1",
    "objectOperations": [
        {
            "callbackUrl": "https://myserver.com/event_callbacks",
            "operations": ["POST","PUT"],
            "objects": ["EVENT"]
        },
        {
            "callbackUrl": "https://myserver.com/program_callbacks",
            "operations": ["POST","PUT"],
            "objects": ["PROGRAM"]
        }
    ]
}

 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 11.
 */
public class Subscription {
    
    /**
     * 
    : VTN provisioned ID of this object instance.
     */
    private String id;
    
    /**
     * 
    : server provisions timestamp on object creation, e.g.
    "2023-06-15T12:58:08.000Z".
     */
    private String createdDateTime;
    
    /**
     * 
    : server provisions timestamp on object modification,
    e.g. "2023-06-16T12:58:08.000Z".
     */
    private String modificationDateTime;
    
    /**
     * 
    : Used as discriminator. SUBSCRIPTION
     */
    private String objectType;
    
    /**
     * 
    : User generated identifier
     */
    private String clientName;
    
    /**
     * 
    : ID attribute of program object this subscription is associated with.
     */
    private String programID;
    
    /**
    : list of objects and operations to subscribe to.
     * 
     */
    private List<ObjectOperation>objectOperations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getModificationDateTime() {
        return modificationDateTime;
    }

    public void setModificationDateTime(String modificationDateTime) {
        this.modificationDateTime = modificationDateTime;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getProgramID() {
        return programID;
    }

    public void setProgramID(String programID) {
        this.programID = programID;
    }

    public List<ObjectOperation> getObjectOperations() {
        return objectOperations;
    }

    public void setObjectOperations(List<ObjectOperation> objectOperations) {
        this.objectOperations = objectOperations;
    }
    
}
