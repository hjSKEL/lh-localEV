/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean;

import com.google.gson.Gson;
import kr.co.kevit.ocpp16.daemon.BeanStore;
import kr.co.kevit.ocpp16.daemon.bean.base.OcppLogBean;
import kr.co.kevit.ocpp16.daemon.store.Action;
import kr.co.kevit.ocpp16.daemon.store.ActionStore;
import kr.co.kevit.ocpp16.daemon.store.Dispacher;
import kr.co.kevit.ocpp16.daemon.util.DateUtils;
import kr.co.kevit.ocpp16.request.*;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2021. 4. 15.
 */
public class RemoteBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteBean.class);

    private static final String SET_NETWORK_PROFILE_SUBSIDY = "SetNetworkProfile";
    private static final String SET_CHARGE_LIMIT = "SetChargeLimit";
    private static final String DATA_TRANSFER = "DataTransfer";

    public RemoteBean() {
        //
    }

    public String execute(String message) {
        //
        if(LOGGER.isDebugEnabled()) {            
            LOGGER.debug("RemoteBean START : {}", DateUtils.getCurrentDateAsString(DateUtils.DATE_TIME_FORMAT_WITHOUT_DASH));
        }
        LOGGER.info("[REMOTE] MESSAGE : {}", message);
        String data[] = message.split(";");
        String csId = data[0];
        String actionName = data[1];
//        Object object = new Gson().fromJson(data[2], Object.class);

        if(SET_NETWORK_PROFILE_SUBSIDY.equals(actionName)) {
            List<Object> reqs = new ArrayList<>();
            reqs.add(2);
            reqs.add(DateUtils.getCurrentDateAsString(DateUtils.YYYYMMDDHHMMSSSSS));
            reqs.add(DATA_TRANSFER);
            kr.co.kevit.ocpp16.request.DataTransfer request = new DataTransfer();
            request.setVendorId("kr.go.seoul");
            request.setMessageId(actionName);
            request.setData(data[2]);
            reqs.add(request);
            boolean result = Dispacher.dispatch(csId, new Gson().toJson(reqs));
            return result ? "SUCCESS" : "FAIL";
        } else if(SET_CHARGE_LIMIT.equals(actionName)) {
            List<Object> reqs = new ArrayList<>();
            reqs.add(2);
            reqs.add(DateUtils.getCurrentDateAsString(DateUtils.YYYYMMDDHHMMSSSSS));
            reqs.add(DATA_TRANSFER);
            kr.co.kevit.ocpp16.request.DataTransfer request = new DataTransfer();
            request.setVendorId("kr.co.ise");
            request.setMessageId(actionName);
            request.setData(data[2]);
            reqs.add(request);
            boolean result = Dispacher.dispatch(csId, new Gson().toJson(reqs));
            return result ? "SUCCESS" : "FAIL";
        }

        List<Object> reqs = new ArrayList<>();
        reqs.add(2);
        reqs.add(DateUtils.getCurrentDateAsString(DateUtils.YYYYMMDDHHMMSSSSS));
        reqs.add(actionName);
        Map<String, Object> dataObj = makeObject(actionName, data[2]);
        reqs.add(dataObj.get("data"));
        boolean result = Dispacher.dispatch(csId, new Gson().toJson(reqs));
        OcppLogBean cs2CsmsOcppLogBean = (OcppLogBean) BeanStore.getInstance().getBean(OcppLogBean.class.getSimpleName());
        Integer connectorId = (Integer)dataObj.get("connectorId");
        cs2CsmsOcppLogBean.requestLog(reqs.get(1).toString(), actionName, csId, connectorId.toString(), new Gson().toJson(reqs.get(3)));
        
        // To save ActionStore
        Action action = new Action();
        action.setId(reqs.get(1).toString());
        action.setName(actionName);

        action.setConnectorId(connectorId.toString());
        ActionStore.getInstance().setAction(csId, action);
        if(LOGGER.isDebugEnabled()) {            
            LOGGER.debug("RemoteBean END : {}", DateUtils.getCurrentDateAsString(DateUtils.DATE_TIME_FORMAT_WITHOUT_DASH));
        }
        return result ? "SUCCESS" : "FAIL";
    }

    private Map<String, Object> makeObject(String actionName, String json) {
        //
        Map<String, Object> resultMap = new HashMap<>(2);
        Object data = null;
        switch(actionName) {
            case "SendLocalList":
                data = new Gson().fromJson(json, SendLocalList.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "GetLocalListVersion":
                data = new Gson().fromJson(json, GetLocalListVersion.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "InstallCertificate":
                data = new Gson().fromJson(json, InstallCertificate.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "GetInstalledCertificateIds":
                data = new Gson().fromJson(json, GetInstalledCertificateIds.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "DeleteCertificate":
                data = new Gson().fromJson(json, DeleteCertificate.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "Reset":
                data = new Gson().fromJson(json, Reset.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "UnlockConnector":
                data = new Gson().fromJson(json, UnlockConnector.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", ((UnlockConnector)data).getConnectorId());
                return resultMap;
            case "ChangeAvailability":
                data = new Gson().fromJson(json, ChangeAvailability.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", ((ChangeAvailability)data).getConnectorId());
                return resultMap;
            case "ChangeConfiguration":
                ChangeConfiguration changeConfiguration = new Gson().fromJson(json, ChangeConfiguration.class);
                if ("AuthorizationKey".equals(changeConfiguration.getKey())) {
                    char[] bytes = Hex.encodeHex(changeConfiguration.getValue().getBytes(), false);
                    changeConfiguration.setValue(String.valueOf(bytes));
                }
                resultMap.put("data", changeConfiguration);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "ClearCache":
                data = new Gson().fromJson(json, ClearCache.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "SetChargingProfile":
                data = new Gson().fromJson(json, SetChargingProfile.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", ((SetChargingProfile)data).getConnectorId());
                return resultMap;
            case "ClearChargingProfile":
                data = new Gson().fromJson(json, ClearChargingProfile.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", ((ClearChargingProfile)data).getConnectorId());
                return resultMap;
            case "GetConfiguration":
                data = new Gson().fromJson(json, GetConfiguration.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "DataTransfer":
                data = new Gson().fromJson(json, DataTransfer.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "GetDiagnostics":
                data = new Gson().fromJson(json, GetDiagnostics.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "GetLog":
                data = new Gson().fromJson(json, GetLog.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "ReserveNow":
                data = new Gson().fromJson(json, ReserveNow.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", ((ReserveNow)data).getConnectorId());
                return resultMap;
            case "CancelReservation":
                data = new Gson().fromJson(json, CancelReservation.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "GetCompositeSchedule":
                data = new Gson().fromJson(json, GetCompositeSchedule.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", ((GetCompositeSchedule)data).getConnectorId());
                return resultMap;
            case "SignedUpdateFirmware":
                data = new Gson().fromJson(json, SignedUpdateFirmware.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "RemoteStartTransaction":
                data = new Gson().fromJson(json, RemoteStartTransaction.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", ((RemoteStartTransaction)data).getConnectorId());
                return resultMap;
            case "RemoteStopTransaction":
                data = new Gson().fromJson(json, RemoteStopTransaction.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
            case "TriggerMessage":
                data = new Gson().fromJson(json, TriggerMessage.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", ((TriggerMessage)data).getConnectorId());
                return resultMap;
            case "UpdateFirmware":
                data = new Gson().fromJson(json, UpdateFirmware.class);
                resultMap.put("data", data);
                resultMap.put("connectorId", 0);
                return resultMap;
        }
        data = new Object();
        resultMap.put("connectorId", 0);
        resultMap.put("data", data);
        return resultMap;
    }
}
