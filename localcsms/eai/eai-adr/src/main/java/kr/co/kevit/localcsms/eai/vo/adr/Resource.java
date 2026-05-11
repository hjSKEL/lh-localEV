/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.adr;

import java.util.List;

/**
resource: a resource is an energy device or system subject to control by a VEN.
 * 
{
    "id": "7f4ec970-44d4-4103-8d22-1fc9caff7e28",
    "createdDateTime": "2025-01-11T15:39:22.752290+00:00",
    "modificationDateTime": "2025-01-11T15:39:22.752290+00:00",
    "venID": "kevit_der_pilot_ven",
    "objectType": "RESOURCE",
    "resourceName": "412200001-03"
}

 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
public class Resource {
    
    /**
    : VTN provisioned ID of this object instance.
     */
    private String id;
    
    /**
    : server provisions timestamp on object creation, e.g.
    "2023-06-15T12:58:08.000Z".
     */
    private String createdDateTime;
    
    /**
    : server provisions timestamp on object modification,
    e.g. "2023-06-16T12:58:08.000Z".
     */
    private String modificationDateTime;
    
    /**
    : Used as discriminator. RESOURCE
     */
    private String objectType;
    
    /**
     * 
    : String identifier for resource. resource may be configured with ID out-of-band.
     */
    private String resourceName;
    
    /**
    : VTN provisioned on object creation based on path
     */
    private String venID;
    
    /**
    : A list of valuesMap objects describing attributes.
     */
    private List<ValuesMap> attributes;
    
    /**
    : An array of valuesMap objects.
     */
    private List<ValuesMap>targets;

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

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getVenID() {
        return venID;
    }

    public void setVenID(String venID) {
        this.venID = venID;
    }

    public List<ValuesMap> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ValuesMap> attributes) {
        this.attributes = attributes;
    }

    public List<ValuesMap> getTargets() {
        return targets;
    }

    public void setTargets(List<ValuesMap> targets) {
        this.targets = targets;
    }

}
