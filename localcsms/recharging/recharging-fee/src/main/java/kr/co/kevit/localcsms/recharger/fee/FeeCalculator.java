/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.fee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 6.
 */
public class FeeCalculator {
    
    private static FeeCalculator calc = new FeeCalculator();
    
    public static FeeCalculator getInstance() {
        return calc;
    }
    /**
     * 
     * @param product
     * @param startTime YYYYMMDDHHmmss
     * @param chargeAmount
     * @return
     */
    public Map<String, BigDecimal> calculate(ProductPrice product, BigDecimal chargeAmount){
        //
        Map<String, BigDecimal> result = new HashMap<>(2);
        BigDecimal unitPrice = BigDecimal.valueOf(product.getFee());
        result.put(StringConstants.UNIT_PRICE, unitPrice);
        result.put(StringConstants.PRICE, unitPrice.multiply(chargeAmount).setScale(2, RoundingMode.FLOOR));
        return result;
    }

    /**
     * 방전 단가 기반 계산 (V2X export 보상).
     *
     * <p>{@link ProductPrice#getDischargeFee()} 가 0 이면 unit/price 모두 0 — 방전 보상 없음 정책.</p>
     *
     * @param product          단가 정보
     * @param dischargeAmount  방전량 (BigDecimal, kWh 또는 Wh — 호출처와 단위 일치)
     * @return {UNIT_PRICE, PRICE} 맵
     */
    public Map<String, BigDecimal> calculateDischarge(ProductPrice product, BigDecimal dischargeAmount){
        Map<String, BigDecimal> result = new HashMap<>(2);
        BigDecimal unitPrice = BigDecimal.valueOf(product.getDischargeFee());
        result.put(StringConstants.UNIT_PRICE, unitPrice);
        result.put(StringConstants.PRICE, unitPrice.multiply(dischargeAmount).setScale(2, RoundingMode.FLOOR));
        return result;
    }

}
