package kr.co.kevit.localcsms.eai.api.dto.model;

/** OCPP 1.6 AuthorizationData (SendLocalList) */
public class AuthorizationData {

    private String idTag;
    /** null 이면 해당 idTag 를 로컬 리스트에서 삭제 */
    private IdTagInfo idTagInfo;

    public String getIdTag()            { return idTag; }
    public void setIdTag(String v)      { this.idTag = v; }

    public IdTagInfo getIdTagInfo()          { return idTagInfo; }
    public void setIdTagInfo(IdTagInfo v)    { this.idTagInfo = v; }
}
