package kr.co.kevit.ocpp16.daemon.bean.req;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;

public class FirmwareStatusNotificationBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirmwareStatusNotificationBean.class);

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Object control(String csId, List<Object> reqs) {
        //
        if (LOGGER.isDebugEnabled()) {
            // String[] csIds = csId.split(StringConstants.DASH);
            Object object = reqs.get(3);
            Gson gson = new Gson();
            String text = gson.toJson(object);
            kr.co.kevit.ocpp16.request.FirmwareStatusNotification request = gson.fromJson(text,kr.co.kevit.ocpp16.request.FirmwareStatusNotification.class);

            LOGGER.debug("FirmwareStatusNotificationBean Request : {}", text);
            LOGGER.debug("getStatus : {}", request.getStatus());
        }
        return new Object();
    }

}