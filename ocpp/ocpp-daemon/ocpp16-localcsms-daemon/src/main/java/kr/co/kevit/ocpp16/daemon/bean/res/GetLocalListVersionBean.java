package kr.co.kevit.ocpp16.daemon.bean.res;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;

public class GetLocalListVersionBean implements ControlerBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GetLocalListVersionBean.class);
    
    @Override
    public Object control(String csId, List<Object> res) {
        //
        Object object = res.get(2);
        Gson gson = new Gson();
        String text = gson.toJson(object);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GetLocalListVersionBean.control text : {}", text);
        }
        kr.co.kevit.ocpp16.response.GetLocalListVersion response = gson.fromJson(text,kr.co.kevit.ocpp16.response.GetLocalListVersion.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GetLocalListVersionBean.getListVersion : {}", response.getListVersion());
        }
        return null;
    }

}
