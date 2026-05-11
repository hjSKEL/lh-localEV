/**
 * 충전관리 - 충전기 제어 (Bootstrap 5 / Vanilla JS)
 */
let ocpp16ControlDevJs = function () {
    "use strict";

    /* ── 유틸 ─────────────────────────────────── */
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

    /* ── 초기화 ───────────────────────────────── */
    function _init() {
        _initData();
        _initEvent();
        loadSessions();
        setInterval(loadSessions, 10000);
    }

    function _initData() {
        _setVal('UpdateFirmwareValue2',   moment().format('YYYY-MM-DDTHH:mm:ss') + 'Z');
        _setVal('GetDiagnosticsValue4',   moment().format('YYYY-MM-DDTHH:mm:ss') + 'Z');
        _setVal('GetDiagnosticsValue5',   moment().format('YYYY-MM-DDTHH:mm:ss') + 'Z');
        _setVal('ReserveNowValue2',       moment().format('YYYY-MM-DDTHH:mm:ss') + 'Z');
    }

    function _initEvent() {
        /* 명령 목록 드롭다운 채우기 */
        let sel = _id('ocppCommandType');
        ocpp16CommandJs.typeDev().forEach(function (cmd) {
            let opt = document.createElement('option');
            opt.value = cmd;
            opt.textContent = cmd;
            sel.appendChild(opt);
        });

        _id('btnCommand').addEventListener('click', _sendCommandOnClick);
        sel.addEventListener('change', function () { _changeOcppCommand(this); });
        _id('btnAddChargingSchedulePeriod').addEventListener('click', _addChargingSchedulePeriod);

        /* RemoteStartTransaction: 스마트충전 토글 */
        let val3 = _id('RemoteStartTransactionValue3');
        if (val3) {
            val3.addEventListener('change', function () {
                let infoDiv = _id('RemoteStartSmartCharging');
                let scpDiv  = _id('SetChargingProfile');
                let isO = (this.value === 'O');
                if (infoDiv) infoDiv.style.display = isO ? '' : 'none';
                if (scpDiv)  scpDiv.style.display  = isO ? '' : 'none';
            });
        }

        /* hiddenAdd / hiddenSub (addInfo 토글) */
        let hiddenAdd = _id('hiddenAdd');
        let hiddenSub = _id('hiddenSub');
        if (hiddenAdd && hiddenSub) {
            hiddenAdd.addEventListener('click', function () {
                document.querySelectorAll('.addInfo').forEach(e => e.style.display = '');
                hiddenSub.style.display = '';
                hiddenAdd.style.display = 'none';
            });
            hiddenSub.addEventListener('click', function () {
                document.querySelectorAll('.addInfo').forEach(e => e.style.display = 'none');
                hiddenSub.style.display = 'none';
                hiddenAdd.style.display = '';
            });
        }
    }

    /* ── 명령 탭 전환 ──────────────────────────── */
    function _changeOcppCommand(target) {
        document.querySelectorAll('div[name="OCPPCommand"]').forEach(e => e.style.display = 'none');
        let selectedValue = target.value;
        if (selectedValue && _id(selectedValue)) {
            _id(selectedValue).style.display = 'block';
        }
    }

    /* ── 전송 전 파라미터 수집 ─────────────────── */
    function _sendCommandOnClick() {
        let ocppCommandType = _val('ocppCommandType');
        if (!ocppCommandType) { alert('명령을 선택하세요.'); return; }
        let params = [];

        switch (ocppCommandType) {
            case 'Reset':
            case 'UnlockConnector':
                params[0] = _val(ocppCommandType + "Value").trim();
                break;

            case 'RemoteStopTransaction':
                params[0] = Number(_val(ocppCommandType + "Value"));
                break;

            case 'CancelReservation':
            case 'CertificateSigned':
            case 'GetInstalledCertificateIds':
            case 'PnC_InstallCertificate':
                params[0] = _val(ocppCommandType + "Value").trim();
                break;

            case 'TriggerMessage':
            case 'ExtendedTriggerMessage':
            case 'ChangeAvailability':
            case 'ChangeConfiguration':
            case 'InstallCertificate':
            case 'SetChargeLimit':
                params[0] = _val(ocppCommandType + "Value1").replace(/-/g, '').trim();
                params[1] = _val(ocppCommandType + "Value2").trim();
                break;

            case 'RemoteStartTransaction':
                params[0] = _val(ocppCommandType + "Value1").replace(/-/g, '').trim();
                params[1] = _val(ocppCommandType + "Value2").trim();
                if (_val(ocppCommandType + "Value3") === "O") {
                    params[2]  = _val("SetChargingProfileValue1").trim();
                    params[3]  = _val("SetChargingProfileValue2").trim();
                    params[4]  = _val("SetChargingProfileValue3").trim();
                    params[5]  = _val("SetChargingProfileValue4").trim();
                    params[6]  = _val("SetChargingProfileValue5").trim();
                    params[7]  = _val("SetChargingProfileValue6").trim();
                    params[8]  = _val("SetChargingProfileValue7").trim();
                    params[9]  = _toUTC(_val("SetChargingProfileValue8").trim());
                    params[10] = _toUTC(_val("SetChargingProfileValue9").trim());
                    params[11] = _val("SetChargingProfileValue10").trim();
                    params[12] = _val("SetChargingProfileValue11").trim();
                    params[13] = _toUTC(_val("SetChargingProfileValue12").trim());
                    params[14] = _val("SetChargingProfileValue13").trim();
                    params[15] = _readSchedulePeriods("SetChargingProfileValue14");
                }
                break;

            case 'DataTransfer':
            case 'GetCompositeSchedule':
            case 'SetNetworkProfile':
                params[0] = _val(ocppCommandType + "Value1").trim();
                params[1] = _val(ocppCommandType + "Value2").trim();
                params[2] = _val(ocppCommandType + "Value3").trim();
                break;

            case 'ClearChargingProfile':
            case 'DeleteCertificate':
            case 'PnC_DeleteCertificate':
            case 'UpdateFirmware':
                params[0] = _val(ocppCommandType + "Value1").trim();
                params[1] = _val(ocppCommandType + "Value2").trim();
                params[2] = _val(ocppCommandType + "Value3").trim();
                params[3] = _val(ocppCommandType + "Value4").trim();
                break;

            case 'GetDiagnostics':
            case 'ReserveNow':
                params[0] = _val(ocppCommandType + "Value1").trim();
                params[1] = _val(ocppCommandType + "Value2").trim();
                params[2] = _val(ocppCommandType + "Value3").trim();
                params[3] = _val(ocppCommandType + "Value4").trim();
                params[4] = _val(ocppCommandType + "Value5").trim();
                break;

            case 'GetConfiguration':
                document.querySelectorAll('input[type="checkbox"][name="GetConfigurationCheckBox"]').forEach(function (cb) {
                    if (cb.checked) params.push(cb.getAttribute('meta-value'));
                });
                break;

            case 'SendLocalList': {
                params[0] = _val(ocppCommandType + "Value1").trim();
                params[1] = _val(ocppCommandType + "Value2").trim();
                let opts = _id(ocppCommandType + 'Value3').options;
                let idTags = [];
                for (let i = 0; i < opts.length; i++) idTags.push(opts[i].value);
                params[2] = idTags;
                break;
            }

            case 'SetChargingProfile': {
                params[0]  = _val(ocppCommandType + "Value1").trim();
                params[1]  = _val(ocppCommandType + "Value2").trim();
                params[2]  = _val(ocppCommandType + "Value3").trim();
                params[3]  = _val(ocppCommandType + "Value4").trim();
                params[4]  = _val(ocppCommandType + "Value5").trim();
                params[5]  = _val(ocppCommandType + "Value6").trim();
                params[6]  = _val(ocppCommandType + "Value7").trim();
                params[7]  = _toUTC(_val(ocppCommandType + "Value8").trim());
                params[8]  = _toUTC(_val(ocppCommandType + "Value9").trim());
                params[9]  = _val(ocppCommandType + "Value10").trim();
                params[10] = _val(ocppCommandType + "Value11").trim();
                params[11] = _toUTC(_val(ocppCommandType + "Value12").trim());
                params[12] = _val(ocppCommandType + "Value13").trim();
                params[13] = _readSchedulePeriods(ocppCommandType + "Value14");
                break;
            }

            case 'GetLog':
                params[0] = _val(ocppCommandType + "Value1").trim();
                params[1] = _val(ocppCommandType + "Value2").trim();
                params[2] = _val(ocppCommandType + "Value3").trim();
                params[3] = _val(ocppCommandType + "Value4").trim();
                params[4] = _val(ocppCommandType + "Value5").trim();
                params[5] = _val(ocppCommandType + "Value6").trim();
                params[6] = _val(ocppCommandType + "Value7").trim();
                break;

            case 'SignedUpdateFirmware':
                params[0] = _val(ocppCommandType + "Value1").trim();
                params[1] = _val(ocppCommandType + "Value2").trim();
                params[2] = _val(ocppCommandType + "Value3").trim();
                params[3] = _val(ocppCommandType + "Value4").trim();
                params[4] = _val(ocppCommandType + "Value5").trim();
                params[5] = _val(ocppCommandType + "Value6").trim();
                params[6] = _val(ocppCommandType + "Value7").trim();
                params[7] = _val(ocppCommandType + "Value8").trim();
                break;

            case 'GetLocalListVersion':
            case 'ClearCache':
                break;
        }

        let valueStr = ocpp16CommandJs.makeParamDev(ocppCommandType, params);
        _sendCommand(ocppCommandType, valueStr);
    }

    /* chargingSchedulePeriod 행 수집 */
    function _readSchedulePeriods(tbodyId) {
        let result = [];
        let tbody = _id(tbodyId);
        if (!tbody) return result;
        let rows = tbody.children;
        for (let i = 0; i < rows.length; i++) {
            let cols = rows[i].children;
            result.push({
                startPeriod:  Number(cols[0].querySelector('input').value),
                numberPhases: Number(cols[1].querySelector('input').value),
                limit:        Number(cols[2].querySelector('input').value)
            });
        }
        return result;
    }

    /* ── API 호출 ─────────────────────────────── */
    function _sendCommand(cmdType, valueStr) {
        let cpId = _val('csId').trim();
        if (!cpId) { alert('충전기 ID를 입력하세요.'); return; }

        let payload;
        try { payload = JSON.parse(valueStr); } catch (e) { payload = {}; }

        let btn = _id('btnCommand');
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>전송 중...';

        _addLog('info', `[${cmdType}] → ${cpId}`);

        fetch(_ctx + '/ocpp16/bypass/' + cpId, {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify({ action: cmdType, payload: payload })
        })
        .then(function (res) {
            return res.json().then(function (data) {
                return { ok: res.ok, data: data };
            });
        })
        .then(function (result) {
            _showResult(result.data, result.ok);
            _addLog(result.ok ? 'success' : 'fail',
                    '[' + cmdType + '] ' + (result.ok ? '성공' : '실패'));
        })
        .catch(function (err) {
            _showResult({ error: err.message }, false);
            _addLog('fail', '[' + cmdType + '] 오류: ' + err.message);
        })
        .finally(function () {
            btn.disabled = false;
            btn.innerHTML = '<i class="bi bi-send-fill me-2"></i>명령 전송';
        });
    }

    /* ── 세션 목록 ────────────────────────────── */
    function loadSessions() {
        fetch(_ctx + '/ocpp16/bypass/sessions')
            .then(function (res) { return res.json(); })
            .then(function (data) {
                let list     = _id('sessionList');
                let countEl  = _id('sessionCount');
                if (!list) return;

                let sessions = Array.isArray(data) ? data
                    : (data.connected || data.sessions || data.data || []);

                if (sessions.length === 0) {
                    list.innerHTML = '<div class="text-center text-muted p-3 small">연결된 충전기 없음</div>';
                    if (countEl) countEl.textContent = '0개 연결';
                    return;
                }

                if (countEl) countEl.textContent = sessions.length + '개 연결';

                list.innerHTML = sessions.map(function (s) {
                    let cpId = typeof s === 'string' ? s
                        : (s.cpId || s.chargePointId || s.id || JSON.stringify(s));
                    return '<button class="list-group-item list-group-item-action cp-item py-2 px-3"'
                        + ' data-cpid="' + cpId + '"'
                        + ' onclick="ocpp16ControlDevJs.selectCp(\'' + cpId + '\')">'
                        + '<div class="d-flex align-items-center gap-2">'
                        + '<i class="bi bi-plug-fill text-success"></i>'
                        + '<span class="text-truncate small">' + cpId + '</span>'
                        + '</div></button>';
                }).join('');
            })
            .catch(function () { /* silent */ });
    }

    function _selectCp(cpId) {
        _setVal('csId', cpId);
        document.querySelectorAll('.cp-item').forEach(function (el) {
            el.classList.toggle('active', el.dataset.cpid === cpId);
        });
    }

    /* ── 로그 패널 ────────────────────────────── */
    function _addLog(type, msg) {
        let panel = _id('logPanel');
        if (!panel) return;

        let placeholder = panel.querySelector('.log-placeholder');
        if (placeholder) placeholder.remove();

        let time = new Date().toLocaleTimeString('ko-KR');
        let badgeText  = type === 'success' ? 'OK' : type === 'fail' ? 'ERR' : 'INFO';
        let badgeColor = type === 'success' ? '#198754' : type === 'fail' ? '#dc3545' : '#0dcaf0';

        let entry = document.createElement('div');
        entry.className = 'log-entry';
        entry.innerHTML =
            '<span class="log-badge" style="background:' + badgeColor + '">' + badgeText + '</span> '
            + '<span class="log-time">' + time + '</span> '
            + msg;
        panel.insertBefore(entry, panel.firstChild);

        while (panel.children.length > 50) panel.removeChild(panel.lastChild);
    }

    function _clearLog() {
        let panel = _id('logPanel');
        if (panel) panel.innerHTML = '<div class="text-center text-muted p-3 small log-placeholder">명령 기록이 없습니다</div>';
    }

    /* ── 결과 표시 ────────────────────────────── */
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
            navigator.clipboard.writeText(box.textContent).then(function () {
                alert('복사되었습니다.');
            });
        }
    }

    /* ── 충전 스케줄 기간 관리 ──────────────────── */
    function _addChargingSchedulePeriod() {
        let html = '<tr>'
            + '<td><input type="number" placeholder="0"   class="form-control form-control-sm" /></td>'
            + '<td><input type="number" placeholder="3"   class="form-control form-control-sm" /></td>'
            + '<td><input type="number" placeholder="0.1" class="form-control form-control-sm" /></td>'
            + '<td><button type="button" class="btn btn-sm btn-outline-danger"'
            + ' onclick="ocpp16ControlDevJs.removeChargingSchedulePeriod(this)">삭제</button></td>'
            + '</tr>';
        _id('SetChargingProfileValue14').insertAdjacentHTML('beforeend', html);
    }

    function _removeChargingSchedulePeriod(that) {
        that.closest('tr').remove();
    }

    /* ── 공개 API ─────────────────────────────── */
    return {
        init:                        _init,
        changeOcppCommand:           _changeOcppCommand,
        loadSessions:                loadSessions,
        selectCp:                    _selectCp,
        clearLog:                    _clearLog,
        copyResult:                  _copyResult,
        removeChargingSchedulePeriod: _removeChargingSchedulePeriod
    };
}();
