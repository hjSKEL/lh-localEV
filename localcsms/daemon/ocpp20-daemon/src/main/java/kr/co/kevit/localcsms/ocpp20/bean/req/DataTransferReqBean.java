package kr.co.kevit.localcsms.ocpp20.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.process.ProductPriceService;
import kr.co.kevit.ocpp201.enumtype.DataTransferStatusEnumType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("DataTransfer")
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
        kr.co.kevit.ocpp201.request.DataTransfer dataTransfer = objectMapper.treeToValue(msg.getPayload(), kr.co.kevit.ocpp201.request.DataTransfer.class);

        log.debug("DataTransferReqBean.control cpCsId={} messageId={} data={}", cpCsId, dataTransfer.getMessageId(), dataTransfer.getData());

        kr.co.kevit.ocpp201.response.DataTransfer response = new kr.co.kevit.ocpp201.response.DataTransfer();
        response.setStatus(DataTransferStatusEnumType.Rejected);
        return objectMapper.valueToTree(response);
    }
}
