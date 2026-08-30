package kr.co.kevit.ocpp16.daemon.bean.base;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.co.kevit.ocpp16.daemon.BeanStore;
import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;
import kr.co.kevit.ocpp16.daemon.bean.DataTransferControlerBean;
import kr.co.kevit.ocpp16.enumtype.DataTransferStatusEnum;
import kr.co.kevit.ocpp16.license.Vendor;
import kr.co.kevit.ocpp16.util.DataTransferUtils;

public class DataTransferBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataTransferBean.class);
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Object control(String csId, List<Object> reqs) {
        //
        int value = new BigDecimal(reqs.get(0).toString()).intValue();
        if(value == 2) {
            return processRequest(csId, reqs);
        }
        if(value == 3) {
            return processResponse(csId, reqs);
        }
        return null;
    }
    
    protected Object processRequest(String csId, List<Object> reqs) {
        //
        Object object = reqs.get(3);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String text = gson.toJson(object);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("DataTransferBean.control.processRequest : {}", text);
        }
        kr.co.kevit.ocpp16.request.DataTransfer request = gson.fromJson(text,kr.co.kevit.ocpp16.request.DataTransfer.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getVendorId : {}", request.getVendorId());
            LOGGER.debug("getMessageId : {}", request.getMessageId());
            LOGGER.debug("getData : {}", request.getData());
        }
        kr.co.kevit.ocpp16.response.DataTransfer response = new kr.co.kevit.ocpp16.response.DataTransfer();
        Boolean isSupportedVendor = DataTransferUtils.getInstance().existVendorId(request.getVendorId());
        
        if(isSupportedVendor) {
            Boolean isSupportedMessage = DataTransferUtils.getInstance().isSupportedMessageId(request.getVendorId(), request.getMessageId());
            if(isSupportedMessage == null) {
                response.setStatus(DataTransferStatusEnum.UnknownMessageId);
            }else {
                response.setStatus(isSupportedMessage ? DataTransferStatusEnum.Accepted : DataTransferStatusEnum.Rejected);
            }
        }else {
            response.setStatus(DataTransferStatusEnum.UnknownVendorId);
        }
        if(DataTransferStatusEnum.Accepted == response.getStatus()) {
            String beanName = Vendor.getInstance().getBeanName(request.getVendorId(), request.getMessageId());
            if(beanName != null) {
              DataTransferControlerBean controller = (DataTransferControlerBean) BeanStore.getInstance().getBean(beanName);
              Object result = controller.control(csId, request);
              response.setData(gson.toJson(result));
            }
        }
        return response;
    }
    
    protected Object processResponse(String csId, List<Object> reqs) {
        //
        Object object = reqs.get(3);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String text = gson.toJson(object);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("DataTransferBean.control.processResponse : {}", text);
        }
        kr.co.kevit.ocpp16.response.DataTransfer request = gson.fromJson(text,kr.co.kevit.ocpp16.response.DataTransfer.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStatus : {}", request.getStatus());
            LOGGER.debug("getData : {}", request.getData());
        }
        return null;
    }

}