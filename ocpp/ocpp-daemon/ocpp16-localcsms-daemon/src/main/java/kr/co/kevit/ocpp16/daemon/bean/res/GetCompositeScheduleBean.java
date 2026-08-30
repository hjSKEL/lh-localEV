package kr.co.kevit.ocpp16.daemon.bean.res;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;

public class GetCompositeScheduleBean implements ControlerBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GetCompositeScheduleBean.class);
    
    @Override
    public Object control(String csId, List<Object> res) {
        //
        Object object = res.get(2);
        Gson gson = new Gson();
        String text = gson.toJson(object);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GetCompositeScheduleBean.control text : {}", text);
        }
        kr.co.kevit.ocpp16.response.GetCompositeSchedule response = gson.fromJson(text,kr.co.kevit.ocpp16.response.GetCompositeSchedule.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GetCompositeSchedule.getStatus : {}", response.getStatus());
            LOGGER.debug("GetCompositeSchedule.getConnectorId : {}", response.getConnectorId());
            LOGGER.debug("GetCompositeSchedule.getScheduleStart : {}", response.getScheduleStart());
            LOGGER.debug("GetCompositeSchedule.getChargingSchedule : {}", response.getChargingSchedule());
            
            if(response.getChargingSchedule()!=null) {
                LOGGER.debug("GetCompositeSchedule.getChargingSchedule.getChargingRateUnit : {}", response.getChargingSchedule().getChargingRateUnit());
                LOGGER.debug("GetCompositeSchedule.getChargingSchedule.getDuration : {}", response.getChargingSchedule().getDuration());
                LOGGER.debug("GetCompositeSchedule.getChargingSchedule.getMinChargingRate : {}", response.getChargingSchedule().getMinChargingRate());
                LOGGER.debug("GetCompositeSchedule.getChargingSchedule.getStartSchedule : {}", response.getChargingSchedule().getStartSchedule());
            }
        }
        return null;
    }

}
