/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.enumtype.charger.RechargingStatus;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.process.RechargingService;
import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;
import kr.co.kevit.ocpp16.daemon.store.DaemonStore;
import kr.co.kevit.ocpp16.response.Heartbeat;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 3. 6.
 */
public class HeartbeatBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatBean.class);

    @Autowired
    private ChargerStatusService chargerStatusService;

    @Autowired
    private RechargingService rechargingService;

    private final String EVT000 = "EVT000";

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Object control(String csId, List<Object> reqs) {
        //
        Date lastDate = DaemonStore.HeartbeatMap.get(csId);
        Date curDate = new Date();
        //
        if (lastDate != null && (curDate.getTime() - lastDate.getTime() < 270000)) {
            Heartbeat heartbeat = new Heartbeat();
            heartbeat.setCurrentTime(DateUtils.dateToString(curDate, DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));

            if(LOGGER.isDebugEnabled()) {                
                LOGGER.debug("HeartbeatBean Cache Response : {}", new Gson().toJson(heartbeat));
            }
            return heartbeat;
        }
        DaemonStore.HeartbeatMap.put(csId, curDate);

        String[] csIds = csId.split(StringConstants.DASH);

        Object object = reqs.get(3);
        Gson gson = new Gson();
        if(LOGGER.isDebugEnabled()) {            
            String text = gson.toJson(object);
            LOGGER.debug("HeartbeatBean Request : {}", text);
        }

        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0],csIds[1]);
        for (ChargerStatusInfo chargerStatusInfo : chargerStatusInfos) {
            if (!StringUtils.isEmpty(chargerStatusInfo.getRechargingId()) && chargerStatusInfo.getRechargingId().length() == 24) {
                Recharging rc = rechargingService.retrieveRecharging4IfById(chargerStatusInfo.getRechargingId());
                if (rc != null && (RechargingStatus.RECS04.getCode().equals(rc.getChStatCode())
                        || RechargingStatus.RECS05.getCode().equals(rc.getChStatCode()))) {

                    chargerStatusInfo.setCuEleEnerge(BigDecimal.ZERO);
                    chargerStatusInfo.setCutCardNo(null);
                    chargerStatusInfo.setRechargingId(null);
                    chargerStatusInfo.setInstChSum(BigDecimal.ZERO);// 순간충전금액
                    chargerStatusInfo.setInstChAmont(BigDecimal.ZERO);// 순간 충전량
                    chargerStatusInfo.setInstChCost(BigDecimal.ZERO);// 순간충전단가
                    chargerStatusInfo.setChSum(BigDecimal.ZERO);// 충전금액
                    chargerStatusInfo.setChStartDate(null);// 충전시작시간
                    chargerStatusInfo.setChEndDate(null);// 충전종료시간
                }
            }
            chargerStatusInfo.setInfoCollDate(new Date());
            chargerStatusInfo.setEventCode(EVT000);
            chargerStatusInfo.setUpdateDate(new Date());
            chargerStatusService.modifyChargerStatusWithoutHis(chargerStatusInfo);
        }

        // -09:00 시간, 기준시로 보냄.
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setCurrentTime(DateUtils.dateToString(new Date(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));
        if(LOGGER.isDebugEnabled()) {            
            LOGGER.debug("HeartbeatBean Response : {}", gson.toJson(heartbeat));
        }
        return heartbeat;
    }
}
