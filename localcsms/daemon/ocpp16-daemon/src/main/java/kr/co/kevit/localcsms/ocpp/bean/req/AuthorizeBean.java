package kr.co.kevit.localcsms.ocpp.bean.req;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargerStatusInfoHisService;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.process.CustomerMgtService;
import kr.co.kevit.localcsms.ocpp.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp.model.OcppMessage;
import kr.co.kevit.ocpp16.domain.IdTagInfo;
import kr.co.kevit.ocpp16.enumtype.IdTagInfoStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AuthorizeBean implements ControlerBean {

    private static final Logger log = LoggerFactory.getLogger(AuthorizeBean.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ChargerStatusService chargerStatusService;

    @Autowired
    private ChargerStatusInfoHisService csStatusHisService;

    @Autowired
    private CustomerMgtService customerService;

    @Autowired
    private ChargingStationService chargingStationService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        log.debug("AuthorizeBean cpCsId={}", cpCsId);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String[] csIds = cpCsId.split(StringConstants.DASH);

        kr.co.kevit.ocpp16.request.Authorize request = objectMapper.treeToValue(msg.getPayload(),
                kr.co.kevit.ocpp16.request.Authorize.class);
        kr.co.kevit.ocpp16.response.Authorize response = new kr.co.kevit.ocpp16.response.Authorize();
        IdTagInfo idTagInfo = new IdTagInfo();
        idTagInfo.setParentIdTag(StringConstants.BLANK);
        Date curDt = DateUtils.changeDateWithDayLevel(new Date(), 7);
        idTagInfo.setExpiryDate(DateUtils.dateToString(curDt, DateUtils.RFC3339_DEFAULT_DATE_FORMAT));

        ChargingStation chargingStation = chargingStationService.retrieveChargingStationByCpIdNCsId(csIds[0], csIds[1]);
        if (StringConstants.N.equals(chargingStation.getUseYn())) {
            idTagInfo.setStatus(IdTagInfoStatus.Invalid);
            response.setIdTagInfo(idTagInfo);
            return objectMapper.valueToTree(response);
        }

        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0],
                csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.get(0);
        chargerStatusInfo.setInfoCollDate(new Date());
        chargerStatusInfo.setEventCode("EVT0A1");
        chargerStatusInfo.setCutCardNo(request.getIdTag());
        chargerStatusInfo.setUpdateDate(new Date());

        CustomerMgt customerMgt = customerService.retrieveCustomerMgtByCustomerCardNo(request.getIdTag());
        if (customerMgt == null) {
            chargerStatusInfo.setEvseId(0);
            chargerStatusInfo.setEventCode("EVT0A2");
            csStatusHisService.registerChargerStatusHis(chargerStatusInfo);
            idTagInfo.setStatus(IdTagInfoStatus.Invalid);
            response.setIdTagInfo(idTagInfo);
            return objectMapper.valueToTree(response);
        } else {
            // 만료된 회원 카드번호 경우 또는 미수금이 존재하는 카드번호 경우
            if (StringConstants.Y.equals(customerMgt.getDeleteYn())) {
                idTagInfo.setStatus(IdTagInfoStatus.Expired);
                response.setIdTagInfo(idTagInfo);
                return objectMapper.valueToTree(response);
            }
            // 준회원인 경우
            if (!StringConstants.MEMB01.equals(customerMgt.getCutGrdCode())) {
                idTagInfo.setStatus(IdTagInfoStatus.Invalid);
                response.setIdTagInfo(idTagInfo);
                return objectMapper.valueToTree(response);
            }
            // 정지된 회원인 경우
            if (StringConstants.Y.equals(customerMgt.getStopYn())) {
                idTagInfo.setStatus(IdTagInfoStatus.Blocked);
                response.setIdTagInfo(idTagInfo);
                return objectMapper.valueToTree(response);
            }
        }

        if (chargerStatusInfos.size() == 1) {
            chargerStatusService.modifyChargerStatus(chargerStatusInfo);
        } else {
            chargerStatusInfo.setEvseId(0);
            csStatusHisService.registerChargerStatusHis(chargerStatusInfo);
        }
        idTagInfo.setParentIdTag(customerMgt.getParentCardNo());
        idTagInfo.setStatus(IdTagInfoStatus.Accepted);
        response.setIdTagInfo(idTagInfo);
        return objectMapper.valueToTree(response);
    }
}
