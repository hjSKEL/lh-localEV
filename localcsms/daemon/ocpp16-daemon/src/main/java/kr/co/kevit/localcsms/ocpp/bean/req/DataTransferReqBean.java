package kr.co.kevit.localcsms.ocpp.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.ocpp.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp.model.OcppMessage;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.process.ProductPriceService;
import kr.co.kevit.ocpp16.enumtype.DataTransferStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataTransferReqBean implements ControlerBean {

    private static final Logger log = LoggerFactory.getLogger(DataTransferReqBean.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ChargingStationService chargingStationService;

    @Autowired
    private ProductPriceService priceService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        kr.co.kevit.ocpp16.request.DataTransfer dataTransfer = objectMapper.treeToValue(msg.getPayload(),
                kr.co.kevit.ocpp16.request.DataTransfer.class);

        log.debug("DataTransferReqBean.control cpCsId={} messageId={} data={}",
                cpCsId, dataTransfer.getMessageId(), dataTransfer.getData());

        kr.co.kevit.ocpp16.response.DataTransfer response = new kr.co.kevit.ocpp16.response.DataTransfer();

        String[] csIds = cpCsId.split(StringConstants.DASH);
        if ("FixedTariff".equals(dataTransfer.getMessageId())) {
            response.setStatus(DataTransferStatusEnum.Accepted);
            response.setData(processFixedTariff(csIds, dataTransfer));
            return objectMapper.valueToTree(response);
        }
        if ("CustomUnitPrice".equals(dataTransfer.getMessageId())) {
            response.setStatus(DataTransferStatusEnum.Accepted);
            response.setData(processCustomUnitPrice(csIds, dataTransfer));
            return objectMapper.valueToTree(response);
        }
        if ("CustomStatusNoti".equals(dataTransfer.getMessageId())) {
            response.setStatus(DataTransferStatusEnum.Accepted);
            response.setData(processCustomStatusNoti(csIds, dataTransfer));
            return objectMapper.valueToTree(response);
        }
        log.warn("DataTransferReqBean: 알 수 없는 messageId={}", dataTransfer.getMessageId());
        response.setStatus(DataTransferStatusEnum.UnknownMessageId);
        return objectMapper.valueToTree(response);
    }

    private String processCustomUnitPrice(String[] csIds, kr.co.kevit.ocpp16.request.DataTransfer dataTransfer)
            throws Exception {

        Map<String, Object> tariffMap = new HashMap<>();
        List<Map<String, String>> data = new ArrayList<>();

        // 현재 시간을 조회하고 분/초/밀리세컨드를 0으로 절삭하여 정시(hour) 기준으로 맞춘다.
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date startAt = cal.getTime();
        for (int i = 0; i < 24; ++i) {
            Date endAt = DateUtils.changeDateWithHourLevel(startAt, 1);
            Map<String, String> datum = new HashMap<>();
            // {\"startAt\":\"2025-01-09T06:00:00Z\",
            // \"endAt\":\"2025-01-09T07:00:00Z\",\"price\":\"660\"}
            datum.put("startAt", DateUtils.dateToString(startAt, DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));
            datum.put("endAt", DateUtils.dateToString(endAt, DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));
            datum.put("price", "200");
            data.add(datum);

            // 다음 구간의 startAt은 이번 구간의 endAt으로 이어간다.
            startAt = endAt;
        }
        tariffMap.put("tariff", data);

        return objectMapper.writeValueAsString(tariffMap);
    }

    private String processCustomStatusNoti(String[] csIds, kr.co.kevit.ocpp16.request.DataTransfer dataTransfer)
            throws Exception {

        Map<String, Object> tariffMap = new HashMap<>();
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> datum = new HashMap<>();
        datum.put("stdAt", DateUtils.getCurrentDateAsString(DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
        datum.put("chargingAmt", "0");
        data.add(datum);
        tariffMap.put("tariff", data);

        return objectMapper.writeValueAsString(tariffMap);
    }

    private String processFixedTariff(String[] csIds, kr.co.kevit.ocpp16.request.DataTransfer dataTransfer)
            throws Exception {

        kr.co.kevit.ocpp16.localcsms.request.FixedTariff request = objectMapper.readValue(dataTransfer.getData(),
                kr.co.kevit.ocpp16.localcsms.request.FixedTariff.class);

        Date startDate;
        if (request.getTimestamp().contains(StringConstants.DOT)) {
            startDate = DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS,
                    DateUtils.UTC);
        } else {
            startDate = DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT,
                    DateUtils.UTC);
        }

        ChargingStation station = chargingStationService.retrieveChargingStationByCpIdNCsId(csIds[0], csIds[1]);
        ProductPrice price = priceService.retrieveLiveProductPriceByType(station.getProdType(), startDate);

        kr.co.kevit.ocpp16.localcsms.response.FixedTariff fixedTariffResponse = new kr.co.kevit.ocpp16.localcsms.response.FixedTariff();
        fixedTariffResponse.setConnectorId(request.getConnectorId());
        fixedTariffResponse.setIdTag(request.getIdTag());
        fixedTariffResponse.setTimestamp(request.getTimestamp());
        fixedTariffResponse.setPrice(price.getFee());

        return objectMapper.writeValueAsString(fixedTariffResponse);
    }
}
