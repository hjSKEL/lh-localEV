var ocpp16CommandJs = function () {
    //
    function _Reset(params) {
        var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
        var obj = {type: params[0]};//Hard, Soft
        return JSON.stringify(obj);
    }

    function _GetConfiguration(params) {
        var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
        var obj = {key: []};
        obj.key = params;
        return JSON.stringify(obj);
    }

    function _RemoteStartTransaction(params) {
        var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
//		var obj = {idTag:params[0], connectorId : params[1], chargingProfile : {}};
        var obj = {idTag: params[0], connectorId: params[1]};
        return JSON.stringify(obj);
    }

    function _RemoteStopTransaction(params) {
        var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
        var obj = {transactionId: params[0]};
        return JSON.stringify(obj);
    }

    function _UpdateFirmware(params) {
        var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
        var obj = {
            location: params[0],
            retrieveDate: params[1],
            retries: params[2],
            retryInterval: params[3],
            connectorId: params[4]
        };
        return JSON.stringify(obj);
    }
    
    function _SetChargingProfile(params) {
        var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
        var obj = {
            connectorId: params[0],
            csChargingProfiles: {
                chargingProfileId: params[1],
                stackLevel: params[2],
                chargingProfilePurpose: params[3],
                chargingProfileKind: params[4],
                chargingSchedule: {
                    chargingRateUnit: params[9],
                    chargingSchedulePeriod: params[13],
                    duration: params[10],
                    startSchedule: params[11],
                    minChargingRate: params[12]
                },
                transactionId: params[5],
                recurrencyKind: params[6],
                validFrom: params[7],
                validTo: params[8]
            }
        };
        return JSON.stringify(obj);
    }
    
    function _KevitChangeResponse() {
        var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
        var obj = {};
        return JSON.stringify(obj);
    }

    function _makeParam(type, params) {
        switch (type) {
            case 'Reset':
                return _Reset(params);
            case 'RemoteStartTransaction':
                return _RemoteStartTransaction(params);
            case 'RemoteStopTransaction' :
                return _RemoteStopTransaction(params);
            case 'SetChargingProfile' :
                return _SetChargingProfile(params);
        }
    }

    function _type() {
    	// nameKey: 호출 측에서 _msg[nameKey] 로 다국어 치환 가능 (없으면 name 폴백).
    	let types = [
    		{name: '충전기재시작', value : 'Reset',                  nameKey: 'cmdReset'},
    		{name: '원격충전시작', value : 'RemoteStartTransaction', nameKey: 'cmdRemoteStart'},
    		{name: '원격충전종료', value : 'RemoteStopTransaction', nameKey: 'cmdRemoteStop'},
    		{name: '스마트충전',   value : 'SetChargingProfile',    nameKey: 'cmdSmartCharging'}
    	];
    	return types;
    }

    return {
        makeParam: _makeParam,
        type: _type
    };
}();
