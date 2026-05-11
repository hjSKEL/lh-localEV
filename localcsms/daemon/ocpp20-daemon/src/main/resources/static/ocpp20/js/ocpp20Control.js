/**
 * OCPP 2.0.1 충전기 제어 UI 컨트롤러
 */
let ocpp20ControlJs = function () {
    "use strict";

    /* ── 유틸 ────────────────────────────────────────────────────────── */
    function _id(id)          { return document.getElementById(id); }
    function _val(id)         { let el = _id(id); return el ? el.value : ''; }
    function _setVal(id, val) { let el = _id(id); if (el) el.value = val; }

    function _toUTC(dateStr) {
        if (!dateStr) return dateStr;
        let t = new Date(dateStr);
        return t.getUTCFullYear() + '-' +
            String(t.getUTCMonth() + 1).padStart(2, '0') + '-' +
            String(t.getUTCDate()).padStart(2, '0') + 'T' +
            String(t.getUTCHours()).padStart(2, '0') + ':' +
            String(t.getUTCMinutes()).padStart(2, '0') + ':' +
            String(t.getUTCSeconds()).padStart(2, '0') + 'Z';
    }

    /* ── 초기화 ──────────────────────────────────────────────────────── */
    function _init() {
        _initData();
        _initEvent();
        loadSessions();
        setInterval(loadSessions, 10000);
    }

    function _initData() {
        let now = moment().format('YYYY-MM-DDTHH:mm:ss') + 'Z';
        _setVal('ReserveNowValue2',        now);
        _setVal('UpdateFirmwareValue3',    now);
        _setVal('GetLogValue6',            now);
        _setVal('GetLogValue7',            now);
        _setVal('AFRRSignalValue1',        now);
    }

    function _initEvent() {
        let sel = _id('ocppCommandType');
        ocpp20CommandJs.typeDev().forEach(function (cmd) {
            let opt = document.createElement('option');
            opt.value = cmd;
            opt.textContent = cmd;
            sel.appendChild(opt);
        });

        _id('btnCommand').addEventListener('click', _sendCommandOnClick);
        sel.addEventListener('change', function () { _changeOcppCommand(this); });

        // GetVariables 행 추가
        _id('btnAddGetVar').addEventListener('click', _addGetVariableRow);
        // SetVariables 행 추가
        _id('btnAddSetVar').addEventListener('click', _addSetVariableRow);
        // SetChargingProfile 기간 추가
        _id('btnAddSchedulePeriod').addEventListener('click', _addSchedulePeriod);
        // SendLocalList idToken 추가
        _id('btnAddIdToken').addEventListener('click', _addIdToken);
        // SetVariableMonitoring 행 추가
        let btnMon = _id('btnAddMonitoring');
        if (btnMon) btnMon.addEventListener('click', _addMonitoringRow);
    }

    /* ── 명령 탭 전환 ────────────────────────────────────────────────── */
    function _changeOcppCommand(target) {
        document.querySelectorAll('div[name="OCPPCommand"]').forEach(e => e.style.display = 'none');
        let v = target.value;
        if (v && _id(v)) _id(v).style.display = 'block';
    }

    /* ── 파라미터 수집 ───────────────────────────────────────────────── */
    function _sendCommandOnClick() {
        let type = _val('ocppCommandType');
        if (!type) { alert('명령을 선택하세요.'); return; }
        let p = [];

        switch (type) {
            case 'Reset':
                p[0] = _val('ResetValue');
                break;

            case 'ChangeAvailability':
                p[0] = _val('ChangeAvailabilityValue1');
                p[1] = _val('ChangeAvailabilityValue2').trim();
                break;

            case 'UnlockConnector':
                p[0] = _val('UnlockConnectorValue1').trim();
                p[1] = _val('UnlockConnectorValue2').trim();
                break;

            case 'RequestStartTransaction':
                p[0] = _val('RequestStartTransactionValue1').trim();
                p[1] = _val('RequestStartTransactionValue2').trim();
                p[2] = _val('RequestStartTransactionValue3').trim();
                p[3] = _val('RequestStartTransactionValue4');
                break;

            case 'RequestStopTransaction':
                p[0] = _val('RequestStopTransactionValue').trim();
                break;

            case 'GetTransactionStatus':
                p[0] = _val('GetTransactionStatusValue').trim();
                break;

            case 'TriggerMessage':
                p[0] = _val('TriggerMessageValue1');
                p[1] = _val('TriggerMessageValue2').trim();
                break;

            case 'GetVariables':
                p[0] = _readTableRows('GetVariablesBody', ['componentName', 'variableName']);
                break;

            case 'SetVariables':
                p[0] = _readTableRows('SetVariablesBody', ['componentName', 'componentInstance', 'variableName', 'value']);
                break;

            case 'GetBaseReport':
                p[0] = _val('GetBaseReportValue1').trim();
                p[1] = _val('GetBaseReportValue2');
                break;

            case 'ClearCache':
            case 'GetLocalListVersion':
                break;

            case 'SendLocalList': {
                p[0] = _val('SendLocalListValue1').trim();
                p[1] = _val('SendLocalListValue2');
                let opts = _id('SendLocalListValue3').options;
                let tags = [];
                for (let i = 0; i < opts.length; i++) tags.push(opts[i].value);
                p[2] = tags;
                break;
            }

            case 'ReserveNow':
                p[0] = _val('ReserveNowValue1').trim();
                p[1] = _toUTC(_val('ReserveNowValue2').trim());
                p[2] = _val('ReserveNowValue3').trim();
                p[3] = _val('ReserveNowValue4');
                p[4] = _val('ReserveNowValue5').trim();
                break;

            case 'CancelReservation':
                p[0] = _val('CancelReservationValue').trim();
                break;

            case 'SetChargingProfile':
                p[0] = _val('SetChargingProfileValue1').trim();
                p[1] = _val('SetChargingProfileValue2').trim();
                p[2] = _val('SetChargingProfileValue3').trim();
                p[3] = _val('SetChargingProfileValue4');
                p[4] = _val('SetChargingProfileValue5');
                p[5] = _val('SetChargingProfileValue6').trim();
                p[6] = _val('SetChargingProfileValue7');
                p[7] = _readSchedulePeriods('SetChargingProfilePeriods');
                p[8] = _toUTC(_val('SetChargingProfileValue8').trim());
                p[9] = _toUTC(_val('SetChargingProfileValue9').trim());
                break;

            case 'ClearChargingProfile':
                p[0] = _val('ClearChargingProfileValue1').trim();
                p[1] = _val('ClearChargingProfileValue2').trim();
                p[2] = _val('ClearChargingProfileValue3');
                p[3] = _val('ClearChargingProfileValue4').trim();
                break;

            case 'GetChargingProfiles':
                p[0] = _val('GetChargingProfilesValue1').trim();
                p[1] = _val('GetChargingProfilesValue2').trim();
                p[2] = _val('GetChargingProfilesValue3');
                p[3] = _val('GetChargingProfilesValue4').trim();
                break;

            case 'GetCompositeSchedule':
                p[0] = _val('GetCompositeScheduleValue1').trim();
                p[1] = _val('GetCompositeScheduleValue2').trim();
                p[2] = _val('GetCompositeScheduleValue3');
                break;

            case 'UpdateFirmware':
                p[0] = _val('UpdateFirmwareValue1').trim();
                p[1] = _val('UpdateFirmwareValue2').trim();
                p[2] = _toUTC(_val('UpdateFirmwareValue3').trim());
                p[3] = _toUTC(_val('UpdateFirmwareValue4').trim());
                p[4] = _val('UpdateFirmwareValue5').trim();
                p[5] = _val('UpdateFirmwareValue6').trim();
                break;

            case 'GetLog':
                p[0] = _val('GetLogValue1');
                p[1] = _val('GetLogValue2').trim();
                p[2] = _val('GetLogValue3').trim();
                p[3] = _val('GetLogValue4').trim();
                p[4] = _val('GetLogValue5').trim();
                p[5] = _toUTC(_val('GetLogValue6').trim());
                p[6] = _toUTC(_val('GetLogValue7').trim());
                break;

            case 'DataTransfer':
                p[0] = _val('DataTransferValue1').trim();
                p[1] = _val('DataTransferValue2').trim();
                p[2] = _val('DataTransferValue3').trim();
                break;

            case 'AdjustPeriodicEventStream':
                p[0] = _val('AdjustPeriodicEventStreamValue1').trim();
                p[1] = _val('AdjustPeriodicEventStreamValue2').trim();
                p[2] = _val('AdjustPeriodicEventStreamValue3').trim();
                break;

            case 'AFRRSignal':
                p[0] = _toUTC(_val('AFRRSignalValue1').trim());
                p[1] = _val('AFRRSignalValue2').trim();
                break;

            case 'CertificateSigned':
                p[0] = _val('CertificateSignedValue1');
                p[1] = _val('CertificateSignedValue2').trim();
                break;

            case 'ChangeTransactionTariff':
                p[0] = _val('ChangeTransactionTariffValue1').trim();
                p[1] = _val('ChangeTransactionTariffValue2').trim();
                break;

            case 'ClearDERControl':
                p[0] = _val('ClearDERControlValue1');
                p[1] = _val('ClearDERControlValue2').trim();
                p[2] = _val('ClearDERControlValue3').trim();
                break;

            case 'ClearDisplayMessage':
                p[0] = _val('ClearDisplayMessageValue').trim();
                break;

            case 'ClearTariffs':
                p[0] = _val('ClearTariffsValue1').trim();
                p[1] = _val('ClearTariffsValue2');
                break;

            case 'ClearVariableMonitoring':
                p[0] = _val('ClearVariableMonitoringValue').trim();
                break;

            case 'CostUpdated':
                p[0] = _val('CostUpdatedValue1').trim();
                p[1] = _val('CostUpdatedValue2').trim();
                break;

            case 'CustomerInformation':
                p[0] = _val('CustomerInformationValue1').trim();
                p[1] = _val('CustomerInformationValue2');
                p[2] = _val('CustomerInformationValue3');
                p[3] = _val('CustomerInformationValue4').trim();
                p[4] = _val('CustomerInformationValue5').trim();
                break;

            case 'DeleteCertificate':
                p[0] = _val('DeleteCertificateValue1');
                p[1] = _val('DeleteCertificateValue2').trim();
                p[2] = _val('DeleteCertificateValue3').trim();
                p[3] = _val('DeleteCertificateValue4').trim();
                break;

            case 'GetDERControl':
                p[0] = _val('GetDERControlValue1').trim();
                p[1] = _val('GetDERControlValue2');
                p[2] = _val('GetDERControlValue3').trim();
                p[3] = _val('GetDERControlValue4').trim();
                break;

            case 'GetDisplayMessages':
                p[0] = _val('GetDisplayMessagesValue1').trim();
                p[1] = _val('GetDisplayMessagesValue2').trim();
                p[2] = _val('GetDisplayMessagesValue3');
                p[3] = _val('GetDisplayMessagesValue4');
                break;

            case 'GetInstalledCertificateIds':
                p[0] = _val('GetInstalledCertificateIdsValue').trim();
                break;

            case 'GetMonitoringReport':
                p[0] = _val('GetMonitoringReportValue1').trim();
                p[1] = _val('GetMonitoringReportValue2').trim();
                break;

            case 'GetPeriodicEventStream':
                break;

            case 'GetReport':
                p[0] = _val('GetReportValue1').trim();
                p[1] = _val('GetReportValue2').trim();
                break;

            case 'GetTariffs':
                p[0] = _val('GetTariffsValue').trim();
                break;

            case 'InstallCertificate':
                p[0] = _val('InstallCertificateValue1');
                p[1] = _val('InstallCertificateValue2').trim();
                break;

            case 'NotifyAllowedEnergyTransfer':
                p[0] = _val('NotifyAllowedEnergyTransferValue').trim();
                break;

            case 'NotifyWebPaymentStarted':
                p[0] = _val('NotifyWebPaymentStartedValue1').trim();
                p[1] = _val('NotifyWebPaymentStartedValue2').trim();
                break;

            case 'PublishFirmware':
                p[0] = _val('PublishFirmwareValue1').trim();
                p[1] = _val('PublishFirmwareValue2').trim();
                p[2] = _val('PublishFirmwareValue3').trim();
                p[3] = _val('PublishFirmwareValue4').trim();
                p[4] = _val('PublishFirmwareValue5').trim();
                break;

            case 'RequestBatterySwap':
                p[0] = _val('RequestBatterySwapValue1').trim();
                p[1] = _val('RequestBatterySwapValue2').trim();
                p[2] = _val('RequestBatterySwapValue3');
                break;

            case 'SetDefaultTariff':
                p[0] = _val('SetDefaultTariffValue1').trim();
                p[1] = _val('SetDefaultTariffValue2').trim();
                break;

            case 'SetDERControl':
                p[0] = _val('SetDERControlValue1');
                p[1] = _val('SetDERControlValue2');
                p[2] = _val('SetDERControlValue3').trim();
                break;

            case 'SetDisplayMessage':
                p[0] = _val('SetDisplayMessageValue1').trim();
                p[1] = _val('SetDisplayMessageValue2');
                p[2] = _val('SetDisplayMessageValue3');
                p[3] = _val('SetDisplayMessageValue4');
                p[4] = _val('SetDisplayMessageValue5').trim();
                p[5] = _val('SetDisplayMessageValue6').trim();
                break;

            case 'SetMonitoringBase':
                p[0] = _val('SetMonitoringBaseValue');
                break;

            case 'SetMonitoringLevel':
                p[0] = _val('SetMonitoringLevelValue').trim();
                break;

            case 'SetNetworkProfile':
                p[0] = _val('SetNetworkProfileValue1').trim();
                p[1] = _val('SetNetworkProfileValue2').trim();
                p[2] = _val('SetNetworkProfileValue3');
                p[3] = _val('SetNetworkProfileValue4').trim();
                p[4] = _val('SetNetworkProfileValue5').trim();
                p[5] = _val('SetNetworkProfileValue6');
                p[6] = _val('SetNetworkProfileValue7');
                p[7] = _val('SetNetworkProfileValue8').trim();
                p[8] = _val('SetNetworkProfileValue9').trim();
                break;

            case 'SetVariableMonitoring':
                p[0] = _readMonitoringRows('SetVariableMonitoringBody');
                break;

            case 'UnpublishFirmware':
                p[0] = _val('UnpublishFirmwareValue').trim();
                break;

            case 'UpdateDynamicSchedule':
                p[0] = _val('UpdateDynamicScheduleValue1').trim();
                p[1] = _val('UpdateDynamicScheduleValue2').trim();
                break;

            case 'UsePriorityCharging':
                p[0] = _val('UsePriorityChargingValue1').trim();
                p[1] = _val('UsePriorityChargingValue2');
                break;
        }

        let valueStr = ocpp20CommandJs.makeParamDev(type, p);
        _sendCommand(type, valueStr);
    }

    /* ── 동적 행 읽기 ────────────────────────────────────────────────── */
    function _readTableRows(tbodyId, fields) {
        let result = [];
        let tbody = _id(tbodyId);
        if (!tbody) return result;
        Array.from(tbody.children).forEach(function (tr) {
            let inputs = tr.querySelectorAll('input');
            let obj = {};
            fields.forEach(function (f, i) {
                obj[f] = inputs[i] ? inputs[i].value.trim() : '';
            });
            result.push(obj);
        });
        return result;
    }

    function _readSchedulePeriods(tbodyId) {
        let result = [];
        let tbody = _id(tbodyId);
        if (!tbody) return result;
        Array.from(tbody.children).forEach(function (tr) {
            let cols = tr.children;
            result.push({
                startPeriod:  Number(cols[0].querySelector('input').value),
                limit:        Number(cols[1].querySelector('input').value),
                numberPhases: cols[2].querySelector('input').value !== ''
                    ? Number(cols[2].querySelector('input').value) : undefined
            });
        });
        return result;
    }

    function _readMonitoringRows(tbodyId) {
        let result = [];
        let tbody = _id(tbodyId);
        if (!tbody) return result;
        Array.from(tbody.children).forEach(function (tr) {
            let cols = tr.children;
            result.push({
                componentName: cols[0].querySelector('input').value.trim(),
                variableName:  cols[1].querySelector('input').value.trim(),
                value:         cols[2].querySelector('input').value.trim(),
                type:          cols[3].querySelector('select').value,
                severity:      cols[4].querySelector('input').value.trim()
            });
        });
        return result;
    }

    /* ── API 호출 ────────────────────────────────────────────────────── */
    function _sendCommand(cmdType, valueStr) {
        let cpId = _val('csId').trim();
        if (!cpId) { alert('충전기 ID를 입력하세요.'); return; }

        let payload;
        try { payload = JSON.parse(valueStr); } catch (e) { payload = {}; }

        let btn = _id('btnCommand');
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>전송 중...';

        _addLog('info', `[${cmdType}] → ${cpId}`);

        fetch('/ocpp20/command/' + cpId, {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify({ action: cmdType, payload: payload })
        })
        .then(res => res.json().then(data => ({ ok: res.ok, data })))
        .then(function (result) {
            _showResult(result.data, result.ok);
            _addLog(result.ok ? 'success' : 'fail',
                    `[${cmdType}] ` + (result.ok ? '성공' : '실패'));
        })
        .catch(function (err) {
            _showResult({ error: err.message }, false);
            _addLog('fail', `[${cmdType}] 오류: ${err.message}`);
        })
        .finally(function () {
            btn.disabled = false;
            btn.innerHTML = '<i class="bi bi-send-fill me-2"></i>명령 전송';
        });
    }

    /* ── 세션 목록 ───────────────────────────────────────────────────── */
    function loadSessions() {
        fetch('/ocpp20/command/sessions')
            .then(res => res.json())
            .then(function (data) {
                let list    = _id('sessionList');
                let countEl = _id('sessionCount');
                if (!list) return;

                let sessions = Array.isArray(data) ? data
                    : (data.connected || data.sessions || []);

                if (sessions.length === 0) {
                    list.innerHTML = '<div class="text-center text-muted p-3 small">연결된 충전기 없음</div>';
                    if (countEl) countEl.textContent = '0개 연결';
                    return;
                }
                if (countEl) countEl.textContent = sessions.length + '개 연결';

                list.innerHTML = sessions.map(function (s) {
                    let cpId = typeof s === 'string' ? s : (s.cpId || s.id || JSON.stringify(s));
                    return `<button class="list-group-item list-group-item-action cp-item py-2 px-3"
                                    data-cpid="${cpId}"
                                    onclick="ocpp20ControlJs.selectCp('${cpId}')">
                                <div class="d-flex align-items-center gap-2">
                                    <i class="bi bi-plug-fill text-success"></i>
                                    <span class="text-truncate small">${cpId}</span>
                                </div>
                            </button>`;
                }).join('');
            })
            .catch(() => { /* silent */ });
    }

    function _selectCp(cpId) {
        _setVal('csId', cpId);
        document.querySelectorAll('.cp-item').forEach(function (el) {
            el.classList.toggle('active', el.dataset.cpid === cpId);
        });
    }

    /* ── 로그 패널 ───────────────────────────────────────────────────── */
    function _addLog(type, msg) {
        let panel = _id('logPanel');
        if (!panel) return;
        let ph = panel.querySelector('.log-placeholder');
        if (ph) ph.remove();

        let time      = new Date().toLocaleTimeString('ko-KR');
        let badgeText  = type === 'success' ? 'OK' : type === 'fail' ? 'ERR' : 'INFO';
        let badgeColor = type === 'success' ? '#198754' : type === 'fail' ? '#dc3545' : '#0dcaf0';

        let entry = document.createElement('div');
        entry.className = 'log-entry';
        entry.innerHTML =
            `<span class="log-badge" style="background:${badgeColor}">${badgeText}</span> `
            + `<span class="log-time">${time}</span> ${msg}`;
        panel.insertBefore(entry, panel.firstChild);
        while (panel.children.length > 50) panel.removeChild(panel.lastChild);
    }

    function _clearLog() {
        let panel = _id('logPanel');
        if (panel) panel.innerHTML = '<div class="text-center text-muted p-3 small log-placeholder">명령 기록이 없습니다</div>';
    }

    /* ── 결과 표시 ───────────────────────────────────────────────────── */
    function _showResult(data, success) {
        let box = _id('resultBox');
        if (!box) return;
        box.textContent = JSON.stringify(data, null, 2);
        box.style.display = 'block';
        box.className = success ? 'result-success' : 'result-fail';
        let hdr = _id('resultHeader');
        if (hdr) hdr.style.removeProperty('display');
    }

    function _copyResult() {
        let box = _id('resultBox');
        if (box && box.textContent) {
            navigator.clipboard.writeText(box.textContent).then(() => alert('복사되었습니다.'));
        }
    }

    /* ── 동적 행 관리 ────────────────────────────────────────────────── */
    function _addGetVariableRow() {
        _id('GetVariablesBody').insertAdjacentHTML('beforeend',
            '<tr>' +
            '<td><input type="text" placeholder="Connector" class="form-control form-control-sm"></td>' +
            '<td><input type="text" placeholder="AvailabilityState" class="form-control form-control-sm"></td>' +
            '<td><button type="button" class="btn btn-sm btn-outline-danger" onclick="ocpp20ControlJs.removeRow(this)">삭제</button></td>' +
            '</tr>');
    }

    function _addSetVariableRow() {
        _id('SetVariablesBody').insertAdjacentHTML('beforeend',
            '<tr>' +
            '<td><input type="text" placeholder="Connector" class="form-control form-control-sm"></td>' +
            '<td><input type="text" placeholder="(빈값=루트)" class="form-control form-control-sm"></td>' +
            '<td><input type="text" placeholder="AvailabilityState" class="form-control form-control-sm"></td>' +
            '<td><input type="text" placeholder="값" class="form-control form-control-sm"></td>' +
            '<td><button type="button" class="btn btn-sm btn-outline-danger" onclick="ocpp20ControlJs.removeRow(this)">삭제</button></td>' +
            '</tr>');
    }

    function _addSchedulePeriod() {
        _id('SetChargingProfilePeriods').insertAdjacentHTML('beforeend',
            '<tr>' +
            '<td><input type="number" placeholder="0" class="form-control form-control-sm"></td>' +
            '<td><input type="number" placeholder="11000" class="form-control form-control-sm"></td>' +
            '<td><input type="number" placeholder="3" class="form-control form-control-sm"></td>' +
            '<td><button type="button" class="btn btn-sm btn-outline-danger" onclick="ocpp20ControlJs.removeRow(this)">삭제</button></td>' +
            '</tr>');
    }

    function _addMonitoringRow() {
        _id('SetVariableMonitoringBody').insertAdjacentHTML('beforeend',
            '<tr>' +
            '<td><input type="text" placeholder="Connector" class="form-control form-control-sm"></td>' +
            '<td><input type="text" placeholder="AvailabilityState" class="form-control form-control-sm"></td>' +
            '<td><input type="number" value="1" class="form-control form-control-sm"></td>' +
            '<td><select class="form-select form-select-sm">' +
            '<option value="UpperThreshold" selected>UpperThreshold</option>' +
            '<option value="LowerThreshold">LowerThreshold</option>' +
            '<option value="Delta">Delta</option>' +
            '<option value="Periodic">Periodic</option>' +
            '<option value="PeriodicClockAligned">PeriodicClockAligned</option>' +
            '</select></td>' +
            '<td><input type="number" value="0" min="0" max="9" class="form-control form-control-sm"></td>' +
            '<td><button type="button" class="btn btn-sm btn-outline-danger" onclick="ocpp20ControlJs.removeRow(this)">삭제</button></td>' +
            '</tr>');
    }

    function _addIdToken() {
        let input = _id('sendLocalListInput');
        let tag   = input ? input.value.trim() : '';
        if (!tag) return;
        let sel = _id('SendLocalListValue3');
        let opt = document.createElement('option');
        opt.value = tag;
        opt.textContent = tag;
        sel.appendChild(opt);
        if (input) input.value = '';
    }

    function _removeRow(btn) {
        btn.closest('tr').remove();
    }

    /* ── 공개 API ────────────────────────────────────────────────────── */
    return {
        init:          _init,
        loadSessions:  loadSessions,
        selectCp:      _selectCp,
        clearLog:      _clearLog,
        copyResult:    _copyResult,
        removeRow:     _removeRow
    };
}();
