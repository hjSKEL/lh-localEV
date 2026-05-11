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
    
}
