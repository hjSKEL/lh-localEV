package kr.co.kevit.localcsms.ocpp.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.localcsms.ocpp.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp.model.OcppMessage;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.process.ProductPriceService;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.fee.FeeCalculator;
import kr.co.kevit.localcsms.recharger.process.RechargingService;
import kr.co.kevit.ocpp16.domain.MeterValue;
import kr.co.kevit.ocpp16.domain.SampledValue;
import kr.co.kevit.ocpp16.enumtype.MeasurandTypeEnum;
import kr.co.kevit.ocpp16.enumtype.UnitEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class MeterValuesBean implements ControlerBean {

    private static final Logger log = LoggerFactory.getLogger(MeterValuesBean.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ChargerStatusService chargerStatusService;

    @Autowired
    private RechargingService rechargingService;

    @Autowired
    private ProductPriceService priceService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String[] csIds = cpCsId.split(StringConstants.DASH);

        kr.co.kevit.ocpp16.request.MeterValues request = objectMapper.treeToValue(msg.getPayload(),
                kr.co.kevit.ocpp16.request.MeterValues.class);
        if (log.isDebugEnabled()) {
            log.debug("MeterValuesBean.control cpCsId={} payload={}", cpCsId, msg.getPayload());
        }
        if (request.getTransactionId() == null || 0 == request.getTransactionId() || request.getMeterValue() == null
                || request.getMeterValue().isEmpty()) {
            return objectMapper.createObjectNode();
        }

        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0],
                csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.get(0);
        if (chargerStatusInfos.size() > 1) {
            for (ChargerStatusInfo datum : chargerStatusInfos) {
                if (datum.getEvseId() == request.getConnectorId()) {
                    chargerStatusInfo = datum;
                } else {
                    datum.setInfoCollDate(new Date());
                    datum.setUpdateDate(new Date());
                    chargerStatusService.modifyChargerStatusWithoutHis(datum);
                }
            }
        }
        Recharging recharging = null;
        if (!StringUtils.isEmpty(chargerStatusInfo.getRechargingId())) {
            recharging = rechargingService.retrieveRecharging4IfById(chargerStatusInfo.getRechargingId());
        }
        if (recharging == null || !recharging.getRechargingId().endsWith(request.getTransactionId().toString())) {
            return objectMapper.createObjectNode();
        }

        MeterValue meterValue = request.getMeterValue().get(0);
        if (meterValue.getTimestamp() == null) {
            return objectMapper.createObjectNode();
        }

        SampledValue sampledValue = getSampledValue(meterValue.getSampledValue(),
                MeasurandTypeEnum.Energy_Active_Import_Register);
        if (sampledValue == null) {
            sampledValue = new SampledValue();
            sampledValue.setValue(StringConstants.ZERO);
            sampledValue.setUnit(UnitEnum.Wh);
        }
        BigDecimal cuEleEnerge = new BigDecimal(sampledValue.getValue());
        if (sampledValue.getUnit() == UnitEnum.Wh) {
            cuEleEnerge = cuEleEnerge.divide(new BigDecimal(1000));
        }
        cuEleEnerge = cuEleEnerge.subtract(recharging.getStartCaEleEnerge());
        if (meterValue.getTimestamp().contains(StringConstants.DOT)) {
            chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(meterValue.getTimestamp(),
                    DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS, DateUtils.UTC));
        } else {
            chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(meterValue.getTimestamp(),
                    DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));
        }
        chargerStatusInfo.setCutCardNo(null);
        chargerStatusInfo.setInstChAmont(cuEleEnerge.subtract(recharging.getChUseAmount())); // 순간 충전량
        chargerStatusInfo.setCuEleEnerge(cuEleEnerge);
        if (chargerStatusInfo.getChStartDate() == null) {
            chargerStatusInfo.setChStartDate(recharging.getChStartDate());
        }
        chargerStatusInfo.setChEndDate(chargerStatusInfo.getInfoCollDate()); // 충전종료시간
        ProductPrice productPrice = priceService.retrieveProductPriceInCache(recharging.getProductId());
        Map<String, BigDecimal> priceMap = FeeCalculator.getInstance().calculate(productPrice,
                chargerStatusInfo.getInstChAmont());
        chargerStatusInfo.setInstChCost(priceMap.get(StringConstants.UNIT_PRICE)); // 순간충전단가
        chargerStatusInfo.setInstChSum(priceMap.get(StringConstants.PRICE)); // 순간충전금액
        chargerStatusInfo.setCaEleEnerge(recharging.getStartCaEleEnerge().add(cuEleEnerge));
        chargerStatusInfo.setChSum(recharging.getChUseCost().add(chargerStatusInfo.getInstChSum())); // 충전요금
        chargerStatusInfo.setUpdateDate(new Date());

        recharging.setChUseCost(chargerStatusInfo.getChSum());
        recharging.setChUseAmount(cuEleEnerge);
        recharging.setChEndDate(chargerStatusInfo.getInfoCollDate());
        rechargingService.modifyRecharging(recharging);
        chargerStatusInfo.setEventCode(StringConstants.BLANK);
        chargerStatusService.modifyChargerStatus(chargerStatusInfo);

        return objectMapper.createObjectNode();
    }

    private SampledValue getSampledValue(List<SampledValue> data, MeasurandTypeEnum measurandType) {
        for (SampledValue sampledValue : data) {
            if (sampledValue.getMeasurand() == null) {
                sampledValue.setMeasurand(MeasurandTypeEnum.Energy_Active_Import_Register);
            }
            if (sampledValue.getUnit() == null) {
                sampledValue.setUnit(UnitEnum.Wh);
            }
            if (sampledValue.getMeasurand() == measurandType) {
                return sampledValue;
            }
        }
        return null;
    }
}
