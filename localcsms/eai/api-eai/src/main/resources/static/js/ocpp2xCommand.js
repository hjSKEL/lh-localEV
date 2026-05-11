/**
 * OCPP 2.0.1 명령 파라미터 빌더 (api-eai 용)
 * ocpp20Command.js 기반 — makeParamDev / typeDev API
 */
var ocpp2xCommandJs = function () {
    "use strict";

    /* ── Phase 1: 핵심 명령 payload 빌더 ─────────────────────────────── */

    function _Reset(p) {
        var obj = { type: p[0] };
        if (p[1] && p[1] !== '') obj.evseId = Number(p[1]);
        return JSON.stringify(obj);
    }

    function _ChangeAvailability(p) {
        var obj = { operationalStatus: p[2] };
        if (p[0] && p[0] !== '') {
            obj.evse = { id: Number(p[0]) };
            if (p[1] && p[1] !== '') obj.evse.connectorId = Number(p[1]);
        }
        return JSON.stringify(obj);
    }

    function _UnlockConnector(p) {
        return JSON.stringify({ evseId: Number(p[0]), connectorId: Number(p[1]) });
    }

    function _RequestStartTransaction(p) {
        var obj = { remoteStartId: Number(p[1]), idToken: { idToken: p[2], type: p[18] } };
        if (p[0] && p[0] !== '') obj.evseId = Number(p[0]);
        if (p[3] && p[3] !== '') obj.groupIdToken = { idToken: p[3], type: p[18] };
        // ChargingProfile
        if (p[4] && p[4] !== '') {
            obj.chargingProfile = {
                id: Number(p[4]),
                stackLevel: Number(p[5]),
                chargingProfilePurpose: p[6],
                chargingProfileKind: p[7],
                chargingSchedule: [{
                    id: Number(p[16]),
                    chargingRateUnit: p[12],
                    chargingSchedulePeriod: p[17],
                    duration: Number(p[13]),
                    minChargingRate: Number(p[15])
                }]
            };
            if (p[9] && p[9] !== '') obj.chargingProfile.recurrencyKind = p[9];
            if (p[10] && p[10] !== '') obj.chargingProfile.validFrom = p[10];
            if (p[11] && p[11] !== '') obj.chargingProfile.validTo = p[11];
            if (p[14] && p[14] !== '') obj.chargingProfile.chargingSchedule[0].startSchedule = p[14];
        }
        return JSON.stringify(obj);
    }

    function _RequestStopTransaction(p) {
        return JSON.stringify({ transactionId: p[0] });
    }

    function _GetTransactionStatus(p) {
        var obj = {};
        if (p[0] && p[0] !== '') obj.transactionId = p[0];
        return JSON.stringify(obj);
    }

    function _TriggerMessage(p) {
        var obj = { requestedMessage: p[2] };
        if (p[0] && p[0] !== '') {
            obj.evse = { id: Number(p[0]) };
            if (p[1] && p[1] !== '') obj.evse.connectorId = Number(p[1]);
        }
        return JSON.stringify(obj);
    }

    function _GetVariables(p) {
        return JSON.stringify({ getVariableData: p });
    }

    function _SetVariables(p) {
        return JSON.stringify({ setVariableData: p });
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
        var obj = { versionNumber: Number(p[0]), updateType: p[1] };
        var list = [];
        if (p[2] && p[2].length > 0) {
            for (var i = 0; i < p[2].length; i++) {
                var item = { idToken: { idToken: p[2][i], type: 'ISO15693' } };
                if (p[3] === 'Y') {
                    item.idToken.idTokenInfo = { status: 'Accepted', cacheExpiryDateTime: '9999-12-31T00:00:00Z' };
                }
                list.push(item);
            }
        }
        if (list.length > 0) obj.localAuthorizationList = list;
        return JSON.stringify(obj);
    }

    function _ReserveNow(p) {
        var obj = {
            expiryDateTime: p[1],
            idToken: { idToken: p[2], type: 'ISO15693' },
            id: Number(p[3])
        };
        if (p[0] && p[0] !== '') obj.evseId = Number(p[0]);
        if (p[4] && p[4] !== '') obj.groupIdToken = { idToken: p[4], type: 'ISO15693' };
        if (p[5] && p[5] !== '') obj.connectorType = p[5];
        return JSON.stringify(obj);
    }

    function _CancelReservation(p) {
        return JSON.stringify({ reservationId: Number(p[0]) });
    }

    function _SetChargingProfile(p) {
        var obj = {
            evseId: Number(p[0]),
            chargingProfile: {
                id: Number(p[1]),
                stackLevel: Number(p[2]),
                chargingProfilePurpose: p[3],
                chargingProfileKind: p[4],
                chargingSchedule: [{
                    id: Number(p[14]),
                    chargingRateUnit: p[9],
                    chargingSchedulePeriod: p[13],
                    duration: Number(p[10]),
                    minChargingRate: Number(p[12])
                }]
            }
        };
        if (p[5] && p[5] !== '') obj.chargingProfile.transactionId = p[5];
        if (p[6] && p[6] !== '') obj.chargingProfile.recurrencyKind = p[6];
        if (p[7] && p[7] !== '') obj.chargingProfile.validFrom = p[7];
        if (p[8] && p[8] !== '') obj.chargingProfile.validTo = p[8];
        if (p[11] && p[11] !== '') obj.chargingProfile.chargingSchedule[0].startSchedule = p[11];
        return JSON.stringify(obj);
    }

    function _ClearChargingProfile(p) {
        var obj = {};
        if (p[0] && p[0] !== '') obj.chargingProfileId = Number(p[0]);
        if (p[1] && p[1] !== '') {
            if (!obj.chargingProfileCriteria) obj.chargingProfileCriteria = {};
            obj.chargingProfileCriteria.evseId = Number(p[1]);
        }
        if (p[2] && p[2] !== '') {
            if (!obj.chargingProfileCriteria) obj.chargingProfileCriteria = {};
            obj.chargingProfileCriteria.stackLevel = Number(p[2]);
        }
        if (p[3] && p[3] !== '') {
            if (!obj.chargingProfileCriteria) obj.chargingProfileCriteria = {};
            obj.chargingProfileCriteria.chargingProfilePurpose = p[3];
        }
        return JSON.stringify(obj);
    }

    function _GetChargingProfiles(p) {
        var obj = { requestId: Number(p[0]), chargingProfile: {} };
        if (p[1] && p[1] !== '') obj.evseId = Number(p[1]);
        if (p[2] && p[2] !== '') obj.chargingProfile.chargingProfilePurpose = p[2];
        if (p[3] && p[3] !== '') obj.chargingProfile.stackLevel = Number(p[3]);
        if (p[4] && p[4].length > 0) obj.chargingProfile.chargingProfileId = p[4];
        if (p[5] && p[5].length > 0) obj.chargingProfile.chargingLimitSource = p[5];
        return JSON.stringify(obj);
    }

    function _GetCompositeSchedule(p) {
        var obj = { evseId: Number(p[0]), duration: Number(p[1]) };
        if (p[2] && p[2] !== '') obj.chargingRateUnit = p[2];
        return JSON.stringify(obj);
    }

    function _UpdateFirmware(p) {
        var obj = {
            requestId: Number(p[0]),
            firmware: { location: p[3], retrieveDateTime: p[4] }
        };
        if (p[1] && p[1] !== '') obj.retries = Number(p[1]);
        if (p[2] && p[2] !== '') obj.retryInterval = Number(p[2]);
        if (p[5] && p[5] !== '') obj.firmware.installDateTime = p[5];
        if (p[6] && p[6] !== '') obj.firmware.signingCertificate = p[6];
        if (p[7] && p[7] !== '') obj.firmware.signature = p[7];
        return JSON.stringify(obj);
    }

    function _GetLog(p) {
        var obj = {
            logType: p[0],
            requestId: Number(p[1]),
            log: { remoteLocation: p[4] }
        };
        if (p[2] && p[2] !== '') obj.retries = Number(p[2]);
        if (p[3] && p[3] !== '') obj.retryInterval = Number(p[3]);
        if (p[5] && p[5] !== '') obj.log.oldestTimestamp = p[5];
        if (p[6] && p[6] !== '') obj.log.latestTimestamp = p[6];
        return JSON.stringify(obj);
    }

    function _DataTransfer(p) {
        return JSON.stringify({ vendorId: p[0], messageId: p[1], data: p[2] });
    }

    /* ── Phase 2: 인증서/보안/모니터링 명령 ──────────────────────────── */

    function _CertificateSigned(p) {
        return JSON.stringify({ certificateChain: p[1], certificateType: p[0] });
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

    function _GetInstalledCertificateIds(p) {
        var obj = {};
        if (p[0] && p[0].length > 0) {
            obj.certificateType = [];
            for (var i = 0; i < p[0].length; i++) {
                obj.certificateType.push(p[0][i]);
            }
        }
        return JSON.stringify(obj);
    }

    function _InstallCertificate(p) {
        return JSON.stringify({ certificateType: p[0], certificate: p[1] });
    }

    function _GetReport(p) {
        var obj = { requestId: Number(p[0]) };
        if (p[1] && p[1] !== '') {
            obj.componentCriteria = [];
            obj.componentCriteria.push(p[1]);
        }
        if (p[2] && p[2] !== '') {
            obj.componentVariable = [];
            obj.componentVariable.push({
                component: { name: p[2], evse: { id: Number(p[3]) } },
                variable: { name: p[4] }
            });
        }
        return JSON.stringify(obj);
    }

    function _GetMonitoringReport(p) {
        var obj = { requestId: Number(p[0]) };
        if (p[1] && p[1].length > 0) {
            obj.monitoringCriteria = [];
            for (var i = 0; i < p[1].length; i++) {
                obj.monitoringCriteria.push(p[1][i]);
            }
        }
        if (p[2] && p[2].length > 0) {
            obj.componentVariable = [];
            for (var j = 0; j < p[2].length; j++) {
                var temp = {
                    component: { name: p[2][j].cName },
                    variable: { name: p[2][j].aName }
                };
                if (p[2][j].evseId && p[2][j].evseId !== '') {
                    temp.component.evse = { id: Number(p[2][j].evseId) };
                }
                obj.componentVariable.push(temp);
            }
        }
        return JSON.stringify(obj);
    }

    function _SetVariableMonitoring(p) {
        return JSON.stringify({ setMonitoringData: p[0] });
    }

    function _ClearVariableMonitoring(p) {
        return JSON.stringify({ id: p[0] });
    }

    function _SetMonitoringBase(p) {
        return JSON.stringify({ monitoringBase: p[0] });
    }

    function _SetMonitoringLevel(p) {
        return JSON.stringify({ severity: Number(p[0]) });
    }

    function _SetNetworkProfile(p) {
        return JSON.stringify({
            configurationSlot: Number(p[0]),
            connectionData: {
                ocppVersion: p[1],
                ocppTransport: p[2],
                ocppCsmsUrl: p[3],
                messageTimeout: Number(p[4]),
                securityProfile: Number(p[5]),
                ocppInterface: p[6]
            }
        });
    }

    function _CustomerInformation(p) {
        var obj = {
            requestId: Number(p[0]),
            report: p[1] === 'true',
            clear: p[2] === 'true'
        };
        if (p[4] && p[4] !== '') obj.customerIdentifier = p[4];
        if (p[5] && p[5] !== '') {
            obj.customerCertificate = {
                hashAlgorithm: 'SHA256',
                issuerNameHash: '5D9AA3B240912700F8621901B1FD95C73E2BDBAA00CCE071ADE233CD74A21DDC',
                issuerKeyHash: '41D1DE817DDFA5EDD6D2AC64F98C70D25EC7B3842034D73F5E76A06839D0866A',
                serialNumber: '588D1A56DC2DAC7CAEB2089BAAF1EBE5'
            };
        }
        if (p[3] && p[3] !== '') {
            obj.idToken = { idToken: p[3], type: 'ISO15693' };
        }
        return JSON.stringify(obj);
    }

    function _CostUpdated(p) {
        return JSON.stringify({ totalCost: Number(p[0]), transactionId: p[1] });
    }

    /* ── Phase 3: 고급 명령 ──────────────────────────────────────────── */

    function _SetDisplayMessage(p) {
        var obj = {
            message: {
                id: Number(p[0]),
                priority: p[1],
                message: { format: p[2], content: p[3] }
            }
        };
        if (p[4] && p[4] !== '') obj.message.state = p[4];
        if (p[5] && p[5] !== '') obj.message.startDateTime = p[5];
        if (p[6] && p[6] !== '') obj.message.endDateTime = p[6];
        if (p[7] && p[7] !== '') obj.message.transactionId = p[7];
        if (p[8] && p[8] !== '') {
            obj.message.display = { name: p[8] };
            if (p[9] && p[9] !== '') obj.message.display.instance = p[9];
            if (p[10] && p[10] !== '' && p[11] && p[11] !== '') obj.message.display.evse = {};
            if (p[10] && p[10] !== '') obj.message.display.evse.id = Number(p[10]);
            if (p[11] && p[11] !== '') obj.message.display.evse.connectorId = Number(p[11]);
        }
        return JSON.stringify(obj);
    }

    function _GetDisplayMessages(p) {
        var obj = { requestId: Number(p[0]) };
        if (p[1] && p[1] !== '') obj.priority = p[1];
        if (p[2] && p[2] !== '') obj.state = p[2];
        if (p[3] && p[3] !== '') {
            obj.id = [];
            var ids = p[3].split(',');
            for (var i = 0; i < ids.length; i++) {
                obj.id.push(ids[i].trim());
            }
        }
        return JSON.stringify(obj);
    }

    function _ClearDisplayMessage(p) {
        return JSON.stringify({ id: Number(p[0]) });
    }

    function _PublishFirmware(p) {
        var obj = { requestId: Number(p[0]), location: p[1], checksum: p[2] };
        if (p[3] && p[3] !== '') obj.retries = Number(p[3]);
        if (p[4] && p[4] !== '') obj.retryInterval = Number(p[4]);
        return JSON.stringify(obj);
    }

    function _UnpublishFirmware(p) {
        return JSON.stringify({ checksum: p[0] });
    }

    function _GetDERControl(p) {
        var obj = { requestId: Number(p[0]) };
        if (p[1]) obj.isDefault = p[1] === 'true';
        if (p[2]) obj.controlType = p[2];
        if (p[3]) obj.controlId = p[3];
        return JSON.stringify(obj);
    }

    function _SetDERControl(p) {
        return JSON.stringify({
            isDefault: p[0] === 'true',
            controlType: p[1],
            controlId: p[2]
        });
    }

    function _ClearDERControl(p) {
        var obj = { isDefault: p[0] === 'true' };
        if (p[1]) obj.controlType = p[1];
        if (p[2]) obj.controlId = p[2];
        return JSON.stringify(obj);
    }

    function _GetTransactionStatusSimple(p) {
        var obj = {};
        if (p[0] !== '') obj.transactionId = p[0];
        return JSON.stringify(obj);
    }

    /* ── 디스패치 ────────────────────────────────────────────────────── */

    function _makeParamDev(type, params) {
        switch (type) {
            case 'Reset':                      return _Reset(params);
            case 'UnlockConnector':            return _UnlockConnector(params);
            case 'SetVariables':               return _SetVariables(params);
            case 'CancelReservation':          return _CancelReservation(params);
            case 'ChangeAvailability':         return _ChangeAvailability(params);
            case 'ClearCache':                 return _ClearCache();
            case 'ClearChargingProfile':       return _ClearChargingProfile(params);
            case 'DataTransfer':               return _DataTransfer(params);
            case 'GetCompositeSchedule':       return _GetCompositeSchedule(params);
            case 'GetVariables':               return _GetVariables(params);
            case 'GetLocalListVersion':        return _GetLocalListVersion();
            case 'RequestStartTransaction':    return _RequestStartTransaction(params);
            case 'RequestStopTransaction':     return _RequestStopTransaction(params);
            case 'ReserveNow':                 return _ReserveNow(params);
            case 'SendLocalList':              return _SendLocalList(params);
            case 'SetChargingProfile':         return _SetChargingProfile(params);
            case 'TriggerMessage':             return _TriggerMessage(params);
            case 'UpdateFirmware':             return _UpdateFirmware(params);
            case 'CertificateSigned':          return _CertificateSigned(params);
            case 'GetInstalledCertificateIds': return _GetInstalledCertificateIds(params);
            case 'DeleteCertificate':          return _DeleteCertificate(params);
            case 'InstallCertificate':         return _InstallCertificate(params);
            case 'CostUpdated':                return _CostUpdated(params);
            case 'GetLog':                     return _GetLog(params);
            case 'GetBaseReport':              return _GetBaseReport(params);
            case 'GetChargingProfiles':        return _GetChargingProfiles(params);
            case 'GetReport':                  return _GetReport(params);
            case 'SetNetworkProfile':          return _SetNetworkProfile(params);
            case 'GetTransactionStatus':       return _GetTransactionStatusSimple(params);
            case 'GetMonitoringReport':        return _GetMonitoringReport(params);
            case 'SetMonitoringBase':          return _SetMonitoringBase(params);
            case 'SetVariableMonitoring':      return _SetVariableMonitoring(params);
            case 'SetMonitoringLevel':         return _SetMonitoringLevel(params);
            case 'ClearVariableMonitoring':    return _ClearVariableMonitoring(params);
            case 'CustomerInformation':        return _CustomerInformation(params);
            case 'SetDisplayMessage':          return _SetDisplayMessage(params);
            case 'GetDisplayMessages':         return _GetDisplayMessages(params);
            case 'ClearDisplayMessage':        return _ClearDisplayMessage(params);
            case 'UnpublishFirmware':          return _UnpublishFirmware(params);
            case 'PublishFirmware':            return _PublishFirmware(params);
            case 'GetDERControl':              return _GetDERControl(params);
            case 'SetDERControl':              return _SetDERControl(params);
            case 'ClearDERControl':            return _ClearDERControl(params);
            default: return '{}';
        }
    }

    function _typeDev() {
        return [
            'CancelReservation',
            'CertificateSigned',
            'ChangeAvailability',
            'ClearCache',
            'ClearChargingProfile',
            'ClearDERControl',
            'ClearDisplayMessage',
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
            'GetReport',
            'GetTransactionStatus',
            'GetVariables',
            'InstallCertificate',
            'PublishFirmware',
            'RequestStartTransaction',
            'RequestStopTransaction',
            'Reset',
            'ReserveNow',
            'SendLocalList',
            'SetChargingProfile',
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
            'UpdateFirmware'
        ];
    }

    return {
        makeParamDev: _makeParamDev,
        typeDev: _typeDev
    };
}();
