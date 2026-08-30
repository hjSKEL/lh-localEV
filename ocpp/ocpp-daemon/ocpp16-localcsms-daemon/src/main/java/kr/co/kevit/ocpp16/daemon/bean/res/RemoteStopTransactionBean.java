package kr.co.kevit.ocpp16.daemon.bean.res;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusInfoHisService;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;
import kr.co.kevit.ocpp16.enumtype.ARStatusEnum;

public class RemoteStopTransactionBean implements ControlerBean{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteStopTransactionBean.class);
    
    @Autowired
    private ChargerStatusService chargerStatusService;
    
    @Autowired
    private ChargerStatusInfoHisService statusHisService;
    
    @Override
    public Object control(String csId, List<Object> res) {
        //
        Object object = res.get(2);
        Gson gson = new Gson();
        String text = gson.toJson(object);
        kr.co.kevit.ocpp16.response.RemoteStopTransaction response = gson.fromJson(text,kr.co.kevit.ocpp16.response.RemoteStopTransaction.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RequestStopTransactionBean.getStatus : {}", response.getStatus());
        }
        String csIds [] = csId.split(StringConstants.DASH);
        List<ChargerStatusInfo> status = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        ChargerStatusInfo datum = status.get(0);
        datum.setEvseId(1);
        datum.setInfoCollDate(new Date());
        datum.setEventCode(response.getStatus() == ARStatusEnum.Accepted ? "EVTR01" : "EVTR02");
        statusHisService.registerChargerStatusHis(datum);
        return null;
    }

}
