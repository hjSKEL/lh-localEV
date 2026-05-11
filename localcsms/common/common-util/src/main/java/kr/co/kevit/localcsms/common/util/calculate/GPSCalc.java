/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.calculate;


import kr.co.kevit.localcsms.common.util.number.NumberConstants;

import java.math.BigDecimal;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 12. 15.
 */
public final class GPSCalc {

    private GPSCalc() {
        throw new IllegalStateException("Utility class");
    }

    public static int calculateDistance(double startLat, double startLon, double endLat, double endLon) {
        //
        if (startLat < NumberConstants.LAT_30 || startLat > NumberConstants.LAT_40 || endLat < NumberConstants.LAT_30
                || endLat > NumberConstants.LAT_40) {
            return NumberConstants.MINUS_999;
        }
        if (startLon < NumberConstants.LON_126 || startLon > NumberConstants.LON_128 || endLon < NumberConstants.LON_126
                || endLon > NumberConstants.LON_128) {
            return NumberConstants.MINUS_999;
        }
        BigDecimal lat = BigDecimal.valueOf(startLat);
        BigDecimal lon = BigDecimal.valueOf(startLon);
        BigDecimal xlat = BigDecimal.valueOf(endLat);
        BigDecimal ylon = BigDecimal.valueOf(endLon);
        BigDecimal x = xlat.subtract(lat).divide(NumberConstants.LAT_1M, BigDecimal.ROUND_HALF_UP);
        BigDecimal y = ylon.subtract(lon).divide(NumberConstants.LON_1M, BigDecimal.ROUND_HALF_UP);
        x = x.multiply(x);
        y = y.multiply(y);
        BigDecimal z = x.add(y);
        return (int) Math.sqrt(z.doubleValue());
    }
}
