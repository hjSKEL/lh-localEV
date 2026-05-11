/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.ieee;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
POST /note HTTP/1.1
Host: {hostname}
Content-Type: application/sep+xml
Content-Length: {contentLength}
<Notification xmlns="urn:ieee:std:2030.5:ns" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <subscribedResource>/upt/0/mr/4/r</subscribedResource>
    <Resource xsi:type="Reading">
        <timePeriod>
        <duration>0</duration>
        <start>12987364</start>
        </timePeriod>
        <value>1001</value>
    </Resource>
    <status>0</status>
    <subscriptionURI>/edev/8/sub/5</subscriptionURI>
</Notification>
 */
/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
@XmlRootElement(name = "Notification", namespace = "urn:ieee:std:2030.5:ns")
@XmlAccessorType(XmlAccessType.FIELD)
public class Notification {
    
    @XmlElement(name = "subscribedResource",namespace = "urn:ieee:std:2030.5:ns")
    private String subscribedResource;
    
    @XmlElement(name = "Resource",namespace = "urn:ieee:std:2030.5:ns")
    private Resource resource;
    
    @XmlElement(name = "status",namespace = "urn:ieee:std:2030.5:ns")
    private int status;
    
    @XmlElement(name = "subscriptionURI",namespace = "urn:ieee:std:2030.5:ns")
    private String subscriptionURI;

    public String getSubscribedResource() {
        return subscribedResource;
    }

    public void setSubscribedResource(String subscribedResource) {
        this.subscribedResource = subscribedResource;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSubscriptionURI() {
        return subscriptionURI;
    }

    public void setSubscriptionURI(String subscriptionURI) {
        this.subscriptionURI = subscriptionURI;
    }

}
