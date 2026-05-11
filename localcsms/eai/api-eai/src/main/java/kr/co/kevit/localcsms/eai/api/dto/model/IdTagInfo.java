package kr.co.kevit.localcsms.eai.api.dto.model;

import java.time.OffsetDateTime;

/** OCPP 1.6 IdTagInfo */
public class IdTagInfo {

    /** Accepted | Blocked | Expired | Invalid | ConcurrentTx */
    private String status;
    private OffsetDateTime expiryDate;
    private String parentIdTag;

    public String getStatus()                    { return status; }
    public void setStatus(String v)              { this.status = v; }

    public OffsetDateTime getExpiryDate()        { return expiryDate; }
    public void setExpiryDate(OffsetDateTime v)  { this.expiryDate = v; }

    public String getParentIdTag()               { return parentIdTag; }
    public void setParentIdTag(String v)         { this.parentIdTag = v; }
}
