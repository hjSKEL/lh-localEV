/**
 * OCPP 1.6 명령 페이로드 빌더 (개발자용 - 27개 전체 명령)
 * daemon/ocpp16-daemon의 ocpp16Command.js 기반
 */
var ocpp16DevControlJs = function () {
    //
    function _Reset(params) {
        var obj = { type: params[0] };
        return JSON.stringify(obj);
    }

    function _UnlockConnector(params) {
        var obj = { connectorId: Number(params[0]) };
        return JSON.stringify(obj);
    }

    function _ChangeConfiguration(params) {
        var obj = { key: params[0], value: params[1] };
        return JSON.stringify(obj);
    }

    function _CancelReservation(params) {
        var obj = { reservationId: Number(params[0]) };
        return JSON.stringify(obj);
    }

    function _ChangeAvailability(params) {
        var obj = { connectorId: Number(params[0]), type: params[1] };
        return JSON.stringify(obj);
    }

    function _ClearCache() {
        return JSON.stringify({});
    }

    function _ClearChargingProfile(params) {
        var obj = {};
        if (params[0] !== "") obj.id = Number(params[0]);
        if (params[1] !== "") obj.connectorId = Number(params[1]);
        if (params[2] !== "") obj.stackLevel = Number(params[2]);
        if (params[3] !== "") obj.chargingProfilePurpose = params[3];
        return JSON.stringify(obj);
    }

    function _DataTransfer(params) {
        var obj = { vendorId: params[0], messageId: params[1], data: params[2] };
        return JSON.stringify(obj);
    }

    function _GetCompositeSchedule(params) {
        var obj = { connectorId: Number(params[0]), duration: Number(params[1]), chargingRateUnit: params[2] };
        return JSON.stringify(obj);
    }

    function _GetConfiguration(params) {
        if (params.length === 0) return JSON.stringify({});
        var obj = { key: params };
        return JSON.stringify(obj);
    }

    function _GetDiagnostics(params) {
        var obj = {
            location: params[0],
            retries: Number(params[1]),
            retryInterval: Number(params[2]),
            startTime: params[3],
            stopTime: params[4]
        };
        return JSON.stringify(obj);
    }

    function _GetLocalListVersion() {
        return JSON.stringify({});
    }

    function _RemoteStartTransaction(params) {
        var obj = { idTag: params[0], connectorId: Number(params[1]) };
        if (params.length > 2) {
            obj.chargingProfile = {
                chargingProfileId: Number(params[3]),
                stackLevel: Number(params[4]),
                chargingProfilePurpose: params[5],
                chargingProfileKind: params[6],
                chargingSchedule: {
                    chargingRateUnit: params[11],
                    chargingSchedulePeriod: params[15],
                    duration: Number(params[12]),
                    minChargingRate: Number(params[14])
                },
                validFrom: params[9],
                validTo: params[10]
            };
            if (params[7] !== '0') obj.chargingProfile.transactionId = Number(params[7]);
            if (params[8] !== 'N/A') obj.chargingProfile.recurrencyKind = params[8];
            if (params[13] !== '') obj.chargingProfile.chargingSchedule.startSchedule = params[13];
        }
        return JSON.stringify(obj);
    }

    function _RemoteStopTransaction(params) {
        var obj = { transactionId: Number(params[0]) };
        return JSON.stringify(obj);
    }

    function _ReserveNow(params) {
        var obj = {
            connectorId: Number(params[0]),
            expiryDate: params[1],
            idTag: params[2],
            reservationId: Number(params[3])
        };
        if (params[4] && params[4] !== "") obj.parentIdTag = params[4];
        return JSON.stringify(obj);
    }

    function _SendLocalList(params) {
        var obj = {
            listVersion: Number(params[0]),
            updateType: params[1],
            localAuthorizationList: []
        };
        for (var i = 0; i < params[2].length; i++) {
            obj.localAuthorizationList.push({
                idTag: params[2][i],
                idTagInfo: { status: "Accepted", expiryDate: "9999-12-31T00:00:00Z", parentIdTag: "1004100410040006" }
            });
        }
        return JSON.stringify(obj);
    }

    function _SetChargingProfile(params) {
        var obj = {
            connectorId: Number(params[0]),
            csChargingProfiles: {
                chargingProfileId: Number(params[1]),
                stackLevel: Number(params[2]),
                chargingProfilePurpose: params[3],
                chargingProfileKind: params[4],
                chargingSchedule: {
                    chargingRateUnit: params[9],
                    chargingSchedulePeriod: params[13],
                    duration: Number(params[10]),
                    startSchedule: params[11],
                    minChargingRate: Number(params[12])
                },
                validFrom: params[7],
                validTo: params[8]
            }
        };
        if (params[5] !== '0') obj.csChargingProfiles.transactionId = Number(params[5]);
        if (params[6] !== 'N/A') obj.csChargingProfiles.recurrencyKind = params[6];
        return JSON.stringify(obj);
    }

    function _TriggerMessage(params) {
        var obj = { requestedMessage: params[0] };
        if (params[1] !== "0") obj.connectorId = Number(params[1]);
        return JSON.stringify(obj);
    }

    function _UpdateFirmware(params) {
        var obj = {
            location: params[0],
            retrieveDate: params[1],
            retries: Number(params[2]),
            retryInterval: Number(params[3])
        };
        return JSON.stringify(obj);
    }

    function _DeleteCertificate(params) {
        var obj = {
            certificateHashData: {
                hashAlgorithm: params[0],
                issuerNameHash: params[1],
                issuerKeyHash: params[2],
                serialNumber: params[3]
            }
        };
        return JSON.stringify(obj);
    }

    function _InstallCertificate(params) {
        var obj = { certificateType: params[0], certificate: params[1] };
        return JSON.stringify(obj);
    }

    function _CertificateSigned(params) {
        var obj = { certificateChain: params[0] };
        return JSON.stringify(obj);
    }

    function _GetInstalledCertificateIds(params) {
        var obj = { certificateType: params[0] };
        return JSON.stringify(obj);
    }

    function _GetLog(params) {
        var obj = {
            logType: params[0],
            requestId: Number(params[1]),
            retries: Number(params[2]),
            retryInterval: Number(params[3]),
            log: {
                remoteLocation: params[4],
                oldestTimestamp: params[5],
                latestTimestamp: params[6]
            }
        };
        return JSON.stringify(obj);
    }

    function _SignedUpdateFirmware(params) {
        var obj = {
            requestId: Number(params[0]),
            retries: Number(params[1]),
            retryInterval: Number(params[2]),
            firmware: {
                location: params[3],
                retrieveDateTime: params[4],
                installDateTime: params[5],
                signingCertificate: params[6],
                signature: params[7]
            }
        };
        return JSON.stringify(obj);
    }

    function _SetChargeLimit(params) {
        var obj = { vendorId: 'kr.co.iiac', messageId: 'SetChargingLimit' };
        obj.data = '{"soc":' + params[0] + ',"time":' + params[1] + '}';
        return JSON.stringify(obj);
    }

    function _SetNetworkProfile(params) {
        var obj = { vendorId: 'kr.co.iiac', messageId: 'SetNetworkProfile' };
        obj.data = '{"ocppCsmsUrl":"' + params[0] + '","userName":"' + params[1] + '","password":"' + params[2] + '"}';
        return JSON.stringify(obj);
    }

    function _makeParam(type, params) {
        switch (type) {
            case 'Reset': return _Reset(params);
            case 'UnlockConnector': return _UnlockConnector(params);
            case 'ChangeConfiguration': return _ChangeConfiguration(params);
            case 'CancelReservation': return _CancelReservation(params);
            case 'ChangeAvailability': return _ChangeAvailability(params);
            case 'ClearCache': return _ClearCache();
            case 'ClearChargingProfile': return _ClearChargingProfile(params);
            case 'DataTransfer': return _DataTransfer(params);
            case 'GetCompositeSchedule': return _GetCompositeSchedule(params);
            case 'GetConfiguration': return _GetConfiguration(params);
            case 'GetDiagnostics': return _GetDiagnostics(params);
            case 'GetLocalListVersion': return _GetLocalListVersion();
            case 'RemoteStartTransaction': return _RemoteStartTransaction(params);
            case 'RemoteStopTransaction': return _RemoteStopTransaction(params);
            case 'ReserveNow': return _ReserveNow(params);
            case 'SendLocalList': return _SendLocalList(params);
            case 'SetChargingProfile': return _SetChargingProfile(params);
            case 'TriggerMessage':
            case 'ExtendedTriggerMessage': return _TriggerMessage(params);
            case 'UpdateFirmware': return _UpdateFirmware(params);
            case 'CertificateSigned': return _CertificateSigned(params);
            case 'GetInstalledCertificateIds': return _GetInstalledCertificateIds(params);
            case 'DeleteCertificate': return _DeleteCertificate(params);
            case 'InstallCertificate': return _InstallCertificate(params);
            case 'GetLog': return _GetLog(params);
            case 'SignedUpdateFirmware': return _SignedUpdateFirmware(params);
            case 'SetChargeLimit': return _SetChargeLimit(params);
            case 'SetNetworkProfile': return _SetNetworkProfile(params);
        }
    }

    function _types() {
        return [
            { name: 'Reset', value: 'Reset' },
            { name: 'RemoteStartTransaction', value: 'RemoteStartTransaction' },
            { name: 'RemoteStopTransaction', value: 'RemoteStopTransaction' },
            { name: 'UnlockConnector', value: 'UnlockConnector' },
            { name: 'ChangeAvailability', value: 'ChangeAvailability' },
            { name: 'ChangeConfiguration', value: 'ChangeConfiguration' },
            { name: 'GetConfiguration', value: 'GetConfiguration' },
            { name: 'ClearCache', value: 'ClearCache' },
            { name: 'SetChargingProfile', value: 'SetChargingProfile' },
            { name: 'ClearChargingProfile', value: 'ClearChargingProfile' },
            { name: 'GetCompositeSchedule', value: 'GetCompositeSchedule' },
            { name: 'TriggerMessage', value: 'TriggerMessage' },
            { name: 'ExtendedTriggerMessage', value: 'ExtendedTriggerMessage' },
            { name: 'DataTransfer', value: 'DataTransfer' },
            { name: 'UpdateFirmware', value: 'UpdateFirmware' },
            { name: 'SignedUpdateFirmware', value: 'SignedUpdateFirmware' },
            { name: 'GetDiagnostics', value: 'GetDiagnostics' },
            { name: 'GetLog', value: 'GetLog' },
            { name: 'ReserveNow', value: 'ReserveNow' },
            { name: 'CancelReservation', value: 'CancelReservation' },
            { name: 'SendLocalList', value: 'SendLocalList' },
            { name: 'GetLocalListVersion', value: 'GetLocalListVersion' },
            { name: 'InstallCertificate', value: 'InstallCertificate' },
            { name: 'DeleteCertificate', value: 'DeleteCertificate' },
            { name: 'CertificateSigned', value: 'CertificateSigned' },
            { name: 'GetInstalledCertificateIds', value: 'GetInstalledCertificateIds' },
            { name: 'SetChargeLimit (IIAC)', value: 'SetChargeLimit' },
            { name: 'SetNetworkProfile (IIAC)', value: 'SetNetworkProfile' }
        ];
    }

    return {
        makeParam: _makeParam,
        types: _types
    };
}();
