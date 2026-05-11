/**
 * OCPP 2.0.1 명령 파라미터 빌더
 */
var ocpp20CommandJs = function () {
    "use strict";

    /* ── 명령별 payload 빌더 ───────────────────────────────────────────── */

    function _Reset(p) {
        // type: Immediate | OnIdle
        return JSON.stringify({ type: p[0] });
    }

    function _ChangeAvailability(p) {
        // p[0]=operationalStatus, p[1]=evseId('' = 전체)
        var obj = { operationalStatus: p[0] };
        if (p[1] !== '') obj.evse = { id: Number(p[1]) };
        return JSON.stringify(obj);
    }

    function _UnlockConnector(p) {
        return JSON.stringify({ evseId: Number(p[0]), connectorId: Number(p[1]) });
    }

    function _RequestStartTransaction(p) {
        // p[0]=evseId, p[1]=remoteStartId, p[2]=idToken, p[3]=idTokenType
        return JSON.stringify({
            evseId: Number(p[0]),
            remoteStartId: Number(p[1]),
            idToken: { idToken: p[2], type: p[3] }
        });
    }

    function _RequestStopTransaction(p) {
        return JSON.stringify({ transactionId: p[0] });
    }

    function _GetTransactionStatus(p) {
        var obj = {};
        if (p[0] !== '') obj.transactionId = p[0];
        return JSON.stringify(obj);
    }

    function _TriggerMessage(p) {
        // p[0]=requestedMessage, p[1]=evseId(0=생략)
        var obj = { requestedMessage: p[0] };
        if (p[1] !== '' && p[1] !== '0') obj.evse = { id: Number(p[1]) };
        return JSON.stringify(obj);
    }

    function _GetVariables(p) {
        // p[0] = [{componentName, variableName}, ...]
        var data = (p[0] || []).map(function (r) {
            return { component: { name: r.componentName }, variable: { name: r.variableName } };
        });
        return JSON.stringify({ getVariableData: data });
    }

    function _SetVariables(p) {
        // p[0] = [{componentName, componentInstance, variableName, value}, ...]
        var data = (p[0] || []).map(function (r) {
            var comp = { name: r.componentName };
            if (r.componentInstance) comp.instance = r.componentInstance;
            return {
                attributeValue: r.value,
                component: comp,
                variable: { name: r.variableName }
            };
        });
        return JSON.stringify({ setVariableData: data });
    }

    function _GetBaseReport(p) {
        return JSON.stringify({ requestId: Number(p[0]), reportBase: p[1] });
    }

    function _ClearCache() {
        return JSON.stringify({});
    }

    function _GetLocalListVersion() {
        return JSON.stringify({});
    }

    function _SendLocalList(p) {
        // p[0]=versionNumber, p[1]=updateType, p[2]=idToken 배열
        var list = (p[2] || []).map(function (tag) {
            return { idToken: { idToken: tag, type: 'ISO14443' } };
        });
        return JSON.stringify({ versionNumber: Number(p[0]), updateType: p[1], localAuthorizationList: list });
    }

    function _ReserveNow(p) {
        // p[0]=id, p[1]=expiryDateTime, p[2]=idToken, p[3]=idTokenType, p[4]=evseId
        var obj = {
            id: Number(p[0]),
            expiryDateTime: p[1],
            idToken: { idToken: p[2], type: p[3] }
        };
        if (p[4] !== '') obj.evseId = Number(p[4]);
        return JSON.stringify(obj);
    }

    function _CancelReservation(p) {
        return JSON.stringify({ reservationId: Number(p[0]) });
    }

    function _SetChargingProfile(p) {
        // p[0]=evseId, p[1]=profileId, p[2]=stackLevel, p[3]=purpose, p[4]=kind,
        // p[5]=scheduleId, p[6]=rateUnit, p[7]=periods(array), p[8]=validFrom, p[9]=validTo
        var schedule = { id: Number(p[5]), chargingRateUnit: p[6], chargingSchedulePeriod: p[7] };
        var profile = {
            id: Number(p[1]),
            stackLevel: Number(p[2]),
            chargingProfilePurpose: p[3],
            chargingProfileKind: p[4],
            chargingSchedule: [schedule]
        };
        if (p[8] !== '') profile.validFrom = p[8];
        if (p[9] !== '') profile.validTo = p[9];
        return JSON.stringify({ evseId: Number(p[0]), chargingProfile: profile });
    }

    function _ClearChargingProfile(p) {
        // p[0]=chargingProfileId, p[1]=evseId, p[2]=purpose, p[3]=stackLevel
        var obj = {};
        if (p[0] !== '') obj.chargingProfileId = Number(p[0]);
        var criteria = {};
        if (p[1] !== '') criteria.evseId = Number(p[1]);
        if (p[2] !== '') criteria.chargingProfilePurpose = p[2];
        if (p[3] !== '') criteria.stackLevel = Number(p[3]);
        if (Object.keys(criteria).length > 0) obj.chargingProfileCriteria = criteria;
        return JSON.stringify(obj);
    }

    function _GetChargingProfiles(p) {
        // p[0]=requestId, p[1]=evseId, p[2]=purpose, p[3]=stackLevel
        var obj = { requestId: Number(p[0]) };
        if (p[1] !== '') obj.evseId = Number(p[1]);
        var criteria = {};
        if (p[2] !== '') criteria.chargingProfilePurpose = p[2];
        if (p[3] !== '') criteria.stackLevel = Number(p[3]);
        if (Object.keys(criteria).length > 0) obj.chargingProfile = criteria;
        return JSON.stringify(obj);
    }

    function _GetCompositeSchedule(p) {
        var obj = { duration: Number(p[0]), evseId: Number(p[1]) };
        if (p[2] !== '') obj.chargingRateUnit = p[2];
        return JSON.stringify(obj);
    }

    function _UpdateFirmware(p) {
        // p[0]=requestId, p[1]=location, p[2]=retrieveDateTime, p[3]=installDateTime,
        // p[4]=retries, p[5]=retryInterval
        var firmware = { location: p[1], retrieveDateTime: p[2] };
        if (p[3] !== '') firmware.installDateTime = p[3];
        var obj = { requestId: Number(p[0]), firmware: firmware };
        if (p[4] !== '') obj.retries = Number(p[4]);
        if (p[5] !== '') obj.retryInterval = Number(p[5]);
        return JSON.stringify(obj);
    }

    function _GetLog(p) {
        // p[0]=logType, p[1]=requestId, p[2]=retries, p[3]=retryInterval,
        // p[4]=remoteLocation, p[5]=oldestTimestamp, p[6]=latestTimestamp
        var logObj = { remoteLocation: p[4] };
        if (p[5] !== '') logObj.oldestTimestamp = p[5];
        if (p[6] !== '') logObj.latestTimestamp = p[6];
        var obj = { logType: p[0], requestId: Number(p[1]), log: logObj };
        if (p[2] !== '') obj.retries = Number(p[2]);
        if (p[3] !== '') obj.retryInterval = Number(p[3]);
        return JSON.stringify(obj);
    }

    function _DataTransfer(p) {
        return JSON.stringify({ vendorId: p[0], messageId: p[1], data: p[2] });
    }

    /* ── OCPP 2.1 추가 명령 ────────────────────────────────────────────── */

    function _csv(s) {
        return (s || '').split(',').map(function (x) { return x.trim(); }).filter(function (x) { return x !== ''; });
    }

    function _AdjustPeriodicEventStream(p) {
        return JSON.stringify({
            id: Number(p[0]),
            params: { interval: Number(p[1]), values: Number(p[2]) }
        });
    }

    function _AFRRSignal(p) {
        return JSON.stringify({ timestamp: p[0], signal: Number(p[1]) });
    }

    function _CertificateSigned(p) {
        return JSON.stringify({ certificateChain: p[1], certificateType: p[0] });
    }

    function _ChangeTransactionTariff(p) {
        var tariff = {};
        try { tariff = JSON.parse(p[1] || '{}'); } catch (e) { }
        return JSON.stringify({ transactionId: p[0], tariff: tariff });
    }

    function _ClearDERControl(p) {
        var obj = { isDefault: p[0] === 'true' };
        if (p[1] !== '') obj.controlType = p[1];
        if (p[2] !== '') obj.controlId = p[2];
        return JSON.stringify(obj);
    }

    function _ClearDisplayMessage(p) {
        return JSON.stringify({ id: Number(p[0]) });
    }

    function _ClearTariffs(p) {
        var obj = {};
        var ids = _csv(p[0]);
        if (ids.length > 0) obj.tariffIds = ids;
        if (p[1] !== '') obj.tariffKind = p[1];
        return JSON.stringify(obj);
    }

    function _ClearVariableMonitoring(p) {
        return JSON.stringify({ id: _csv(p[0]).map(Number) });
    }

    function _CostUpdated(p) {
        return JSON.stringify({ totalCost: Number(p[0]), transactionId: p[1] });
    }

    function _CustomerInformation(p) {
        var obj = {
            requestId: Number(p[0]),
            report: p[1] === 'true',
            clear: p[2] === 'true'
        };
        if (p[3] !== '') obj.customerIdentifier = p[3];
        if (p[4] !== '') obj.idToken = { idToken: p[4], type: 'ISO14443' };
        return JSON.stringify(obj);
    }

    function _DeleteCertificate(p) {
        return JSON.stringify({
            certificateHashData: {
                hashAlgorithm: p[0],
                issuerNameHash: p[1],
                issuerKeyHash: p[2],
                serialNumber: p[3]
            }
        });
    }

    function _GetDERControl(p) {
        var obj = { requestId: Number(p[0]) };
        if (p[1] !== '') obj.isDefault = p[1] === 'true';
        if (p[2] !== '') obj.controlType = p[2];
        if (p[3] !== '') obj.controlId = p[3];
        return JSON.stringify(obj);
    }

    function _GetDisplayMessages(p) {
        var obj = { requestId: Number(p[0]) };
        var ids = _csv(p[1]).map(Number);
        if (ids.length > 0) obj.id = ids;
        if (p[2] !== '') obj.priority = p[2];
        if (p[3] !== '') obj.state = p[3];
        return JSON.stringify(obj);
    }

    function _GetInstalledCertificateIds(p) {
        var obj = {};
        var types = _csv(p[0]);
        if (types.length > 0) obj.certificateType = types;
        return JSON.stringify(obj);
    }

    function _GetMonitoringReport(p) {
        var obj = { requestId: Number(p[0]) };
        var crit = _csv(p[1]);
        if (crit.length > 0) obj.monitoringCriteria = crit;
        return JSON.stringify(obj);
    }

    function _GetPeriodicEventStream() {
        return JSON.stringify({});
    }

    function _GetReport(p) {
        var obj = { requestId: Number(p[0]) };
        var crit = _csv(p[1]);
        if (crit.length > 0) obj.componentCriteria = crit;
        return JSON.stringify(obj);
    }

    function _GetTariffs(p) {
        return JSON.stringify({ evseId: Number(p[0]) });
    }

    function _InstallCertificate(p) {
        return JSON.stringify({ certificateType: p[0], certificate: p[1] });
    }

    function _NotifyAllowedEnergyTransfer(p) {
        return JSON.stringify({ allowedEnergyTransfer: _csv(p[0]) });
    }

    function _NotifyWebPaymentStarted(p) {
        return JSON.stringify({ evseId: Number(p[0]), timeout: Number(p[1]) });
    }

    function _PublishFirmware(p) {
        var obj = {
            requestId: Number(p[0]),
            location: p[1],
            checksum: p[2]
        };
        if (p[3] !== '') obj.retries = Number(p[3]);
        if (p[4] !== '') obj.retryInterval = Number(p[4]);
        return JSON.stringify(obj);
    }

    function _RequestBatterySwap(p) {
        return JSON.stringify({
            requestId: Number(p[0]),
            idToken: { idToken: p[1], type: p[2] }
        });
    }

    function _SetDefaultTariff(p) {
        var tariff = {};
        try { tariff = JSON.parse(p[1] || '{}'); } catch (e) { }
        return JSON.stringify({ evseId: Number(p[0]), tariff: tariff });
    }

    function _SetDERControl(p) {
        var control = {};
        try { control = JSON.parse(p[2] || '{}'); } catch (e) { }
        var obj = { isDefault: p[0] === 'true', controlType: p[1] };
        obj[p[1].charAt(0).toLowerCase() + p[1].slice(1)] = control;
        return JSON.stringify(obj);
    }

    function _SetDisplayMessage(p) {
        var msg = {
            id: Number(p[0]),
            priority: p[1],
            message: { format: p[3], content: p[4] }
        };
        if (p[2] !== '') msg.state = p[2];
        if (p[5] !== '') msg.transactionId = p[5];
        return JSON.stringify({ message: msg });
    }

    function _SetMonitoringBase(p) {
        return JSON.stringify({ monitoringBase: p[0] });
    }

    function _SetMonitoringLevel(p) {
        return JSON.stringify({ severity: Number(p[0]) });
    }

    function _SetNetworkProfile(p) {
        var conn = {
            ocppCsmsUrl: p[1],
            ocppVersion: p[2],
            messageTimeout: Number(p[3]),
            securityProfile: Number(p[4]),
            ocppInterface: p[5]
        };
        if (p[6]) conn.ocppTransport = p[6];
        if (p[7]) conn.identity = p[7];
        if (p[8]) conn.basicAuthPassword = p[8];
        return JSON.stringify({
            configurationSlot: Number(p[0]),
            connectionData: conn
        });
    }

    function _SetVariableMonitoring(p) {
        // p[0] = [{componentName, variableName, value, type, severity}, ...]
        var data = (p[0] || []).map(function (r) {
            return {
                value: Number(r.value),
                type: r.type,
                severity: Number(r.severity),
                component: { name: r.componentName },
                variable: { name: r.variableName }
            };
        });
        return JSON.stringify({ setMonitoringData: data });
    }

    function _UnpublishFirmware(p) {
        return JSON.stringify({ checksum: p[0] });
    }

    function _UpdateDynamicSchedule(p) {
        var update = {};
        try { update = JSON.parse(p[1] || '{}'); } catch (e) { }
        return JSON.stringify({
            chargingProfileId: Number(p[0]),
            scheduleUpdate: update
        });
    }

    function _UsePriorityCharging(p) {
        return JSON.stringify({
            transactionId: p[0],
            activate: p[1] === 'true'
        });
    }

    /* ── 디스패치 ──────────────────────────────────────────────────────── */

    function _makeParamDev(type, params) {
        switch (type) {
            case 'Reset': return _Reset(params);
            case 'ChangeAvailability': return _ChangeAvailability(params);
            case 'UnlockConnector': return _UnlockConnector(params);
            case 'RequestStartTransaction': return _RequestStartTransaction(params);
            case 'RequestStopTransaction': return _RequestStopTransaction(params);
            case 'GetTransactionStatus': return _GetTransactionStatus(params);
            case 'TriggerMessage': return _TriggerMessage(params);
            case 'GetVariables': return _GetVariables(params);
            case 'SetVariables': return _SetVariables(params);
            case 'GetBaseReport': return _GetBaseReport(params);
            case 'ClearCache': return _ClearCache();
            case 'GetLocalListVersion': return _GetLocalListVersion();
            case 'SendLocalList': return _SendLocalList(params);
            case 'ReserveNow': return _ReserveNow(params);
            case 'CancelReservation': return _CancelReservation(params);
            case 'SetChargingProfile': return _SetChargingProfile(params);
            case 'ClearChargingProfile': return _ClearChargingProfile(params);
            case 'GetChargingProfiles': return _GetChargingProfiles(params);
            case 'GetCompositeSchedule': return _GetCompositeSchedule(params);
            case 'UpdateFirmware': return _UpdateFirmware(params);
            case 'GetLog': return _GetLog(params);
            case 'DataTransfer': return _DataTransfer(params);
            case 'AdjustPeriodicEventStream': return _AdjustPeriodicEventStream(params);
            case 'AFRRSignal': return _AFRRSignal(params);
            case 'CertificateSigned': return _CertificateSigned(params);
            case 'ChangeTransactionTariff': return _ChangeTransactionTariff(params);
            case 'ClearDERControl': return _ClearDERControl(params);
            case 'ClearDisplayMessage': return _ClearDisplayMessage(params);
            case 'ClearTariffs': return _ClearTariffs(params);
            case 'ClearVariableMonitoring': return _ClearVariableMonitoring(params);
            case 'CostUpdated': return _CostUpdated(params);
            case 'CustomerInformation': return _CustomerInformation(params);
            case 'DeleteCertificate': return _DeleteCertificate(params);
            case 'GetDERControl': return _GetDERControl(params);
            case 'GetDisplayMessages': return _GetDisplayMessages(params);
            case 'GetInstalledCertificateIds': return _GetInstalledCertificateIds(params);
            case 'GetMonitoringReport': return _GetMonitoringReport(params);
            case 'GetPeriodicEventStream': return _GetPeriodicEventStream();
            case 'GetReport': return _GetReport(params);
            case 'GetTariffs': return _GetTariffs(params);
            case 'InstallCertificate': return _InstallCertificate(params);
            case 'NotifyAllowedEnergyTransfer': return _NotifyAllowedEnergyTransfer(params);
            case 'NotifyWebPaymentStarted': return _NotifyWebPaymentStarted(params);
            case 'PublishFirmware': return _PublishFirmware(params);
            case 'RequestBatterySwap': return _RequestBatterySwap(params);
            case 'SetDefaultTariff': return _SetDefaultTariff(params);
            case 'SetDERControl': return _SetDERControl(params);
            case 'SetDisplayMessage': return _SetDisplayMessage(params);
            case 'SetMonitoringBase': return _SetMonitoringBase(params);
            case 'SetMonitoringLevel': return _SetMonitoringLevel(params);
            case 'SetNetworkProfile': return _SetNetworkProfile(params);
            case 'SetVariableMonitoring': return _SetVariableMonitoring(params);
            case 'UnpublishFirmware': return _UnpublishFirmware(params);
            case 'UpdateDynamicSchedule': return _UpdateDynamicSchedule(params);
            case 'UsePriorityCharging': return _UsePriorityCharging(params);
            default: return '{}';
        }
    }

    function _typeDev() {
        return [
            'AdjustPeriodicEventStream',
            'AFRRSignal',
            'CancelReservation',
            'CertificateSigned',
            'ChangeAvailability',
            'ChangeTransactionTariff',
            'ClearCache',
            'ClearChargingProfile',
            'ClearDERControl',
            'ClearDisplayMessage',
            'ClearTariffs',
            'ClearVariableMonitoring',
            'CostUpdated',
            'CustomerInformation',
            'DataTransfer',
            'DeleteCertificate',
            'GetBaseReport',
            'GetChargingProfiles',
            'GetCompositeSchedule',
            'GetDERControl',
            'GetDisplayMessages',
            'GetInstalledCertificateIds',
            'GetLocalListVersion',
            'GetLog',
            'GetMonitoringReport',
            'GetPeriodicEventStream',
            'GetReport',
            'GetTariffs',
            'GetTransactionStatus',
            'GetVariables',
            'InstallCertificate',
            'NotifyAllowedEnergyTransfer',
            'NotifyWebPaymentStarted',
            'PublishFirmware',
            'RequestBatterySwap',
            'RequestStartTransaction',
            'RequestStopTransaction',
            'Reset',
            'ReserveNow',
            'SendLocalList',
            'SetChargingProfile',
            'SetDefaultTariff',
            'SetDERControl',
            'SetDisplayMessage',
            'SetMonitoringBase',
            'SetMonitoringLevel',
            'SetNetworkProfile',
            'SetVariableMonitoring',
            'SetVariables',
            'TriggerMessage',
            'UnlockConnector',
            'UnpublishFirmware',
            'UpdateDynamicSchedule',
            'UpdateFirmware',
            'UsePriorityCharging'
        ];
    }

    return {
        makeParamDev: _makeParamDev,
        typeDev: _typeDev
    };
}();
