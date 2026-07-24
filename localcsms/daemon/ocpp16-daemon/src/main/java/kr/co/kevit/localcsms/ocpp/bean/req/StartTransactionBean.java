package kr.co.kevit.localcsms.ocpp.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargeStatusType;
import kr.co.kevit.localcsms.common.util.enumtype.charger.RechargingStatus;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.process.CustomerMgtService;
import kr.co.kevit.localcsms.customer.process.CustomerService;
import kr.co.kevit.localcsms.ocpp.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp.model.OcppMessage;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.process.ProductPriceService;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.process.RechargingService;
import kr.co.kevit.ocpp16.domain.IdTagInfo;
import kr.co.kevit.ocpp16.enumtype.IdTagInfoStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public class StartTransactionBean implements ControlerBean {

    private static final Logger log = LoggerFactory.getLogger(StartTransactionBean.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String EVT0J3 = "EVT0J3";

    @Autowired
    private ChargerStatusService chargerStatusService;

    @Autowired
    private ChargingStationService chargingStationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMgtService custMgtService;

    @Autowired
    private RechargingService rechargingService;

    @Autowired
    private ProductPriceService priceService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String[] csIds = cpCsId.split(StringConstants.DASH);

        kr.co.kevit.ocpp16.request.StartTransaction request = objectMapper.treeToValue(msg.getPayload(),
                kr.co.kevit.ocpp16.request.StartTransaction.class);
        if (log.isDebugEnabled()) {
            log.debug("StartTransactionBean.control cpCsId={} payload={}", cpCsId, msg.getPayload());
        }

        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0],
                csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.get(0);
        if (chargerStatusInfos.size() > 1) {
            for (ChargerStatusInfo datum : chargerStatusInfos) {
                if (datum.getEvseId() == request.getConnectorId()) {
                    chargerStatusInfo = datum;
                    break;
                }
            }
        }

        kr.co.kevit.ocpp16.response.StartTransaction response = new kr.co.kevit.ocpp16.response.StartTransaction();
        IdTagInfo idTagInfo = new IdTagInfo();
        idTagInfo.setParentIdTag(StringConstants.BLANK);
        response.setIdTagInfo(idTagInfo);

        String custCardNo = request.getIdTag();
        Date curDt = new Date();
        CustomerMgt customerMgt = custMgtService.retrieveCustomerMgtByCustomerCardNo(custCardNo);
        if (customerMgt == null) {
            idTagInfo.setStatus(IdTagInfoStatus.Invalid);
        } else {
            // 만료된 회원 카드번호 경우 또는 미수금이 존재하는 카드번호 경우
            if (StringConstants.Y.equals(customerMgt.getDeleteYn())) {
                idTagInfo.setStatus(IdTagInfoStatus.Expired);
                response.setIdTagInfo(idTagInfo);
            } else if (!StringConstants.MEMB01.equals(customerMgt.getCutGrdCode())) {
                // 준회원인 경우
                idTagInfo.setStatus(IdTagInfoStatus.Invalid);
                response.setIdTagInfo(idTagInfo);
            } else if (StringConstants.Y.equals(customerMgt.getStopYn())) {
                // 정지된 회원인 경우
                idTagInfo.setStatus(IdTagInfoStatus.Blocked);
                response.setIdTagInfo(idTagInfo);
            } else {
                // 정상적인 회원인 경우
                idTagInfo.setParentIdTag(customerMgt.getParentCardNo());
                idTagInfo.setStatus(IdTagInfoStatus.Accepted);
            }
        }
        Recharging recharging = makeNewRecharging(request, chargerStatusInfo, customerMgt);
        recharging.setStartCaEleEnerge(new BigDecimal(request.getMeterStart()).divide(new BigDecimal(1000))); // 시작 시
                                                                                                              // 전력량 (Wh
                                                                                                              // -> kWh)
        recharging.setEndCaEleEnerge(BigDecimal.ZERO);
        rechargingService.registerRecharging(recharging);

        chargerStatusInfo.setInfoCollDate(new Date());
        chargerStatusInfo.setCsCableStatus(StringConstants.ONE);
        chargerStatusInfo.setCsStatCode(ChargeStatusType.CHARGING.getCode());
        chargerStatusInfo.setCaEleEnerge(recharging.getStartCaEleEnerge()); // 시작 시 전력량 (Wh -> kWh)
        chargerStatusInfo.setCuEleEnerge(BigDecimal.ZERO);
        chargerStatusInfo.setCutCardNo(request.getIdTag());
        chargerStatusInfo.setRechargingId(recharging.getRechargingId());
        chargerStatusInfo.setEventCode(EVT0J3);
        chargerStatusInfo.setInstChSum(BigDecimal.ZERO); // 순간충전금액
        chargerStatusInfo.setInstChAmont(BigDecimal.ZERO); // 순간 충전량
        chargerStatusInfo.setInstChCost(BigDecimal.ZERO); // 순간충전단가
        chargerStatusInfo.setChSum(BigDecimal.ZERO); // 충전금액
        chargerStatusInfo.setChStartDate(recharging.getChStartDate()); // 충전시작시간
        chargerStatusInfo.setChEndDate(chargerStatusInfo.getChStartDate()); // 충전종료시간
        chargerStatusInfo.setUpdateDate(new Date());
        chargerStatusService.modifyChargerStatus(chargerStatusInfo);

        int idLength = recharging.getRechargingId().length() - 9;
        response.setTransactionId(
                Integer.parseInt(recharging.getRechargingId().substring(idLength)));
        return objectMapper.valueToTree(response);
    }

    private Recharging makeNewRecharging(kr.co.kevit.ocpp16.request.StartTransaction request,
            ChargerStatusInfo chargerStatusInfo, CustomerMgt customerMgt) {
        ChargingStation station = chargingStationService.retrieveChargingStationByCpIdNCsId(chargerStatusInfo.getCpId(),
                chargerStatusInfo.getCsId());
        Customer customer = customerService.retrieveCustomerByUserId(customerMgt.getCustomerId());

        Recharging recharging = new Recharging();
        recharging.setChStatCode(RechargingStatus.RECS02.getCode());
        recharging.setCpId(station.getCpId());
        recharging.setCsId(station.getCsId());
        recharging.setEvseId(chargerStatusInfo.getEvseId());
        if (request.getTimestamp() == null) {
            request.setTimestamp(
                    DateUtils.dateToString(new Date(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS, DateUtils.UTC));
        }
        if (request.getTimestamp().contains(StringConstants.DOT)) {
            recharging.setChStartDate(DateUtils.stringToDate(request.getTimestamp(),
                    DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS, DateUtils.UTC));
        } else {
            recharging.setChStartDate(DateUtils.stringToDate(request.getTimestamp(),
                    DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));
        }
        ProductPrice product = priceService.retrieveLiveProductPriceByType(station.getProdType(),
                recharging.getChStartDate());
        recharging.setChEndDate(recharging.getChStartDate());
        recharging.setChUseAmount(BigDecimal.ZERO);
        recharging.setChUseCost(BigDecimal.ZERO);
        recharging.setChUseUnitCost(BigDecimal.valueOf(product.getFee()));
        recharging.setCustomerId(customerMgt.getCustomerId());
        recharging.setCompanyId(customer.getCompanyId());
        recharging.setCutCardNo(request.getIdTag());
        recharging.setFinalPaySum(0);
        recharging.setPaySum(0);
        recharging.setProductId(product.getId());
        return recharging;
    }
}
