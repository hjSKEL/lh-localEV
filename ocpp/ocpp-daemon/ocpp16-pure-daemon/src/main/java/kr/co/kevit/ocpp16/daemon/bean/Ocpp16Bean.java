/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.kevit.ocpp16.daemon.BeanStore;
import kr.co.kevit.ocpp16.daemon.bean.base.OcppLogBean;
import kr.co.kevit.ocpp16.daemon.bean.base.ActionBean;
import kr.co.kevit.ocpp16.daemon.store.Dispacher;
import kr.co.kevit.ocpp16.daemon.util.DateUtils;
import kr.co.kevit.ocpp16.daemon.util.StringConstants;
import kr.co.kevit.ocpp16.exception.OCPPErrorCode;
import kr.co.kevit.ocpp16.exception.OCPPException;
import kr.co.kevit.ocpp16.util.OCPPUtils;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 4. 15.
 */
public class Ocpp16Bean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Ocpp16Bean.class);
    
    public Ocpp16Bean() {
        //
    }

    public void execute(Exchange exchange){
        //
        if(LOGGER.isDebugEnabled()) {            
            LOGGER.debug("Ocpp16Bean START : {}", DateUtils.getCurrentDateAsString(DateUtils.DATE_TIME_FORMAT_WITHOUT_DASH));
        }
        String text = exchange.getIn().getBody().toString();
        String csId = exchange.getIn().getHeader("websocket.connectionKey").toString();
        LOGGER.info("[TXT][{}] : TIME : {}, MessageId : {}, MESSAGE : {}",csId, DateUtils.getCurrentDateAsString(DateUtils.DATE_TIME_FORMAT3), exchange.getIn().getMessageId(), text);
        try {
            List<Object> testSon = new Gson().fromJson(text, ArrayList.class);
            int value = new BigDecimal(testSon.get(0).toString()).intValue();

            switch (value) {
            case 2:
                processRequest(csId, testSon);
                break;
            case 3:
                processResponse(csId, testSon);
                break;
            case 4:
                processResponseErr(csId, testSon);
                break;
            default:
                //
            }
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
        if(LOGGER.isDebugEnabled()) {            
            LOGGER.debug("Ocpp16Bean END : {}", DateUtils.getCurrentDateAsString(DateUtils.DATE_TIME_FORMAT_WITHOUT_DASH));
        }

    }

    private void processRequest(String csId, List<Object> reqs) {
        //
        // log
        OcppLogBean cs2CsmsOcppLogBean = (OcppLogBean) BeanStore.getInstance().getBean(OcppLogBean.class.getSimpleName());
        String text = new Gson().toJson(reqs.get(3));
        String connectorId = null;
        int index = text.indexOf("connectorId");
        if(index > -1) {
            //TODO 10개 이상의 커넥터 있을 경우
            if (reqs.get(2).toString().equals("DataTransfer")) {
                connectorId = text.substring(index + 14, index + 15);
            } else {
                connectorId = text.substring(index + 13, index + 14);
            }
        }else {
            connectorId = StringConstants.ZERO;
        }
        
        cs2CsmsOcppLogBean.requestLog(reqs.get(1).toString(), reqs.get(2).toString(), csId, connectorId, text);
        
        Object o = null;
        OCPPUtils utils = OCPPUtils.getInstance();

        List<Object> res = new ArrayList<>(5);
        try {
            String command = (String) reqs.get(2);
            boolean isRegistedType = utils.isRegistedType(command);
            if (!isRegistedType) {
                res.add(4);
                res.add(reqs.get(1));
                res.add(OCPPErrorCode.NotImplemented.toString());
                res.add(OCPPErrorCode.NotImplemented.getDesc());
                res.add(new Object());
                Dispacher.dispatch(csId, new Gson().toJson(res));
                // log
                cs2CsmsOcppLogBean.responseLog(reqs.get(1).toString(), Integer.valueOf(4).toString(), reqs.get(2).toString(), csId, connectorId, OCPPErrorCode.NotImplemented.toString());
                return;
            }
            boolean isSupportedType = utils.isSupportedType(command);
            if (!isSupportedType) {
                res.add(4);
                res.add(reqs.get(1));
                res.add(OCPPErrorCode.NotSupported.toString());
                res.add(OCPPErrorCode.NotSupported.getDesc());
                res.add(new Object());
                Dispacher.dispatch(csId, new Gson().toJson(res));
                
                // log
                cs2CsmsOcppLogBean.responseLog(reqs.get(1).toString(), Integer.valueOf(4).toString(), reqs.get(2).toString(), csId, connectorId, OCPPErrorCode.NotSupported.toString());
                return;
            }

            ControlerBean controller = (ControlerBean) BeanStore.getInstance().getBean(command);
            if(controller != null)
                o = controller.control(csId, reqs);
            else
                throw new OCPPException(OCPPErrorCode.NotImplemented);
            
            res.add(3);
            res.add(reqs.get(1));
//            res.add(command);
            res.add(o);
            Dispacher.dispatch(csId, new Gson().toJson(res));
            
            // log
            cs2CsmsOcppLogBean.responseLog(reqs.get(1).toString(), Integer.valueOf(3).toString(), reqs.get(2).toString(), csId, connectorId, new Gson().toJson(o));
            return;
        } catch (OCPPException oe) {
            res.add(4);
            res.add(reqs.get(1));
            res.add(oe.getCode().toString());
            res.add(oe.getCode().getDesc());
            res.add(new Object());
            Dispacher.dispatch(csId, new Gson().toJson(res));
            
            // log
            cs2CsmsOcppLogBean.responseLog(reqs.get(1).toString(), Integer.valueOf(4).toString(), reqs.get(2).toString(), csId, connectorId, oe.getCode().toString());
            return;
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
            res.add(4);
            res.add(reqs.get(1));
            res.add(OCPPErrorCode.InternalError.toString());
            res.add(OCPPErrorCode.InternalError.getDesc());
            res.add(new Object());
            Dispacher.dispatch(csId, new Gson().toJson(res));
            
            // log
            cs2CsmsOcppLogBean.responseLog(reqs.get(1).toString(), Integer.valueOf(4).toString(), reqs.get(2).toString(), csId, connectorId, OCPPErrorCode.InternalError.toString());
            return;
        }
    }

    private void processResponse(String csId, List<Object> res) {
        //
        try {
            ActionBean csms2CsOcppLogBean = (ActionBean) BeanStore.getInstance().getBean(ActionBean.class.getSimpleName());
            String command = (String)csms2CsOcppLogBean.control(csId, StringConstants.THREE, res);
            if(command != null) {                
                ControlerBean controller = (ControlerBean) BeanStore.getInstance().getBean(command);
                if(controller != null)
                    controller.control(csId, res);
            }
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    private void processResponseErr(String csId, List<Object> res) {
        //
        try {
            ActionBean controller = (ActionBean) BeanStore.getInstance().getBean(ActionBean.class.getSimpleName());
            if(controller != null)
                controller.control(csId, StringConstants.FOUR, res);
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
