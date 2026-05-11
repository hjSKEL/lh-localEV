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

import java.util.Date;

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
        kr.co.kevit.ocpp16.request.DataTransfer dataTransfer =
                objectMapper.treeToValue(msg.getPayload(), kr.co.kevit.ocpp16.request.DataTransfer.class);

        log.debug("DataTransferReqBean.control cpCsId={} messageId={} data={}",
                cpCsId, dataTransfer.getMessageId(), dataTransfer.getData());

        kr.co.kevit.ocpp16.response.DataTransfer response = new kr.co.kevit.ocpp16.response.DataTransfer();

        if (!"FixedTariff".equals(dataTransfer.getMessageId())) {
            log.warn("DataTransferReqBean: 알 수 없는 messageId={}", dataTransfer.getMessageId());
            response.setStatus(DataTransferStatusEnum.UnknownMessageId);
            return objectMapper.valueToTree(response);
        }

        String[] csIds = cpCsId.split(StringConstants.DASH);

        kr.co.kevit.ocpp16.localcsms.request.FixedTariff request =
                objectMapper.readValue(dataTransfer.getData(), kr.co.kevit.ocpp16.localcsms.request.FixedTariff.class);

        Date startDate;
        if (request.getTimestamp().contains(StringConstants.DOT)) {
            startDate = DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS, DateUtils.UTC);
        } else {
            startDate = DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC);
        }

        ChargingStation station = chargingStationService.retrieveChargingStationByCpIdNCsId(csIds[0], csIds[1]);
        ProductPrice price = priceService.retrieveLiveProductPriceByType(station.getProdType(), startDate);

        kr.co.kevit.ocpp16.localcsms.response.FixedTariff fixedTariffResponse = new kr.co.kevit.ocpp16.localcsms.response.FixedTariff();
        fixedTariffResponse.setConnectorId(request.getConnectorId());
        fixedTariffResponse.setIdTag(request.getIdTag());
        fixedTariffResponse.setTimestamp(request.getTimestamp());
        fixedTariffResponse.setPrice(price.getFee());

        response.setStatus(DataTransferStatusEnum.Accepted);
        response.setData(objectMapper.writeValueAsString(fixedTariffResponse));

        return objectMapper.valueToTree(response);
    }
}
