package kr.co.kevit.ocpp16.daemon.bean.res;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;

public class UpdateFirmwareBean implements ControlerBean{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateFirmwareBean.class);
    
    @Override
    public Object control(String csId, List<Object> res) {
        //
        Object object = res.get(2);
        Gson gson = new Gson();
        String text = gson.toJson(object);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("UpdateFirmwareBean.control : {}", text);
        }
//        kr.co.ise.csms.daemon.ocpp16.response.UpdateFirmware response = gson.fromJson(text,kr.co.ise.csms.daemon.ocpp16.response.UpdateFirmware.class);
        return null;
    }

}
