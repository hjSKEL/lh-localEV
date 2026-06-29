/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import kr.co.kevit.ocpp201.enumtype.DERUnitEnumType;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 21.
 */
public class OverstayRuleListType {
    //
    private Map<String, Object> customData;

    /**
     */
    private RationalNumberType overstayPowerThreshold;

    /**
     * required
     *  "minItems": 1,
     *  "maxItems": 5
     */
    private List<OverstayRuleType> overstayRule;

    /**
     * required
     *  Time till overstay is applied in seconds.
     */
    private Integer overstayTimeThreshold;

}
