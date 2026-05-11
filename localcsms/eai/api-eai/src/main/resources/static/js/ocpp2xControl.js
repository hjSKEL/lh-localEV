/**
 * OCPP 2.0.1 충전기 제어 UI 컨트롤러 (api-eai 용)
 * ocpp20Control.js 기반 — 바닐라 JS + fetch API + api-eai bypass 엔드포인트
 */
let ocpp2xControlJs = function () {
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
        let now = new Date().toISOString().substring(0, 19) + 'Z';
        _setVal('ReserveNowValue2', now);
        _setVal('UpdateFirmwareValue5', now);
        _setVal('GetLogValue6', now);
        _setVal('GetLogValue7', now);

        // GetVariables 체크박스 목록 채우기
        let allVarKeys = ocpp2xVarJs.getAllVariableKey();
        let getVarTbody = _id('GetVariablesTBody');
        if (getVarTbody) {
            let html = '';
            for (var i = 0; i < allVarKeys.length; i++) {
                html += '<tr>';
                html += '<td><input name="GetVariablesCheckBox" type="checkbox" meta-value="' + allVarKeys[i] + '"></td>';
                html += '<td>' + allVarKeys[i] + '</td>';
                html += '<td></td>';
                html += '</tr>';
            }
            getVarTbody.innerHTML = html;
        }
    }

    function _initEvent() {
        // 명령 드롭다운 채우기
        let sel = _id('ocppCommandType');
        ocpp2xCommandJs.typeDev().forEach(function (cmd) {
            let opt = document.createElement('option');
            opt.value = cmd;
            opt.textContent = cmd;
            sel.appendChild(opt);
        });

        _id('btnCommand').addEventListener('click', _sendCommandOnClick);
        sel.addEventListener('change', function () { _changeOcppCommand(this); });

        // 동적 행 추가 버튼들
        _bindClick('btnAddSchedulePeriod', _addChargingSchedulePeriod);
        _bindClick('btnRTAddChargingSchedulePeriod', _addRTChargingSchedulePeriod);
        _bindClick('btnAddSetVariableMonitoring', _addSetVariableMonitoring);
        _bindClick('btnAddGetMonitoringReport', _addGetMonitoringReport);
        _bindClick('btnAddIdToken', _addIdToken);
        _bindClick('btnAddSetVariables', _addSetVariables);
    }

    function _bindClick(id, fn) {
        let el = _id(id);
        if (el) el.addEventListener('click', fn);
    }

    /* ── 명령 탭 전환 ────────────────────────────────────────────────── */
    function _changeOcppCommand(target) {
        document.querySelectorAll('div[name="OCPPCommand"]').forEach(function(e) { e.style.display = 'none'; });
        let v = target.value;
        if (v && _id(v)) _id(v).style.display = 'block';
        if (v === 'SendLocalList') _getLocalList();
    }

    /* ── SendLocalList 고객 목록 조회 ─────────────────────────────────── */
    function _getLocalList() {
        let cpId = _val('csId').trim();
        if (!cpId) return;
    }

    /* ── 파라미터 수집 ───────────────────────────────────────────────── */
    function _sendCommandOnClick() {
        let ocppCommandType = _val('ocppCommandType');
        if (!ocppCommandType) { alert('명령을 선택하세요.'); return; }
        let params = [];

        switch (ocppCommandType) {
            case 'RequestStopTransaction':
            case 'CancelReservation':
            case 'UnpublishFirmware':
                params[0] = _val(ocppCommandType + 'Value');
                break;
            case 'GetInstalledCertificateIds':
                params[0] = _getSelectedValues(ocppCommandType + 'Value');
                break;
            case 'CertificateSigned':
            case 'UnlockConnector':
            case 'Reset':
            case 'InstallCertificate':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                break;
            case 'ChangeAvailability':
            case 'TriggerMessage':
            case 'DataTransfer':
            case 'GetCompositeSchedule':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                break;
            case 'GetDERControl':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                params[3] = _val(ocppCommandType + 'Value4');
                break;
            case 'SetDERControl':
            case 'ClearDERControl':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                break;
            case 'ClearChargingProfile':
            case 'DeleteCertificate':
            case 'GetDisplayMessages':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                params[3] = _val(ocppCommandType + 'Value4');
                break;
            case 'PublishFirmware':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                params[3] = _val(ocppCommandType + 'Value4');
                params[4] = _val(ocppCommandType + 'Value5');
                break;
            case 'ReserveNow':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                params[3] = _val(ocppCommandType + 'Value4');
                params[4] = _val(ocppCommandType + 'Value5');
                params[5] = _val(ocppCommandType + 'Value6');
                break;
            case 'GetVariables':
                params = [];
                document.querySelectorAll("input[name='GetVariablesCheckBox']").forEach(function(cb) {
                    if (cb.checked) {
                        let varInstance = ocpp2xVarJs.getVariable(cb.getAttribute('meta-value'));
                        let param = {
                            variable: { name: varInstance.variableName },
                            component: { name: varInstance.componentName }
                        };
                        if (varInstance.variableInstance) {
                            param.variable.instance = varInstance.variableInstance;
                        }
                        params.push(param);
                    }
                });
                break;
            case 'SetVariables':
                params = [];
                let svTrList = _id('SetVariablesTbody');
                if (svTrList) {
                    Array.from(svTrList.children).forEach(function(tr) {
                        let keyTd = tr.children[0];
                        let valueTd = tr.children[1];
                        let selEl = keyTd.querySelector('select');
                        let key = selEl ? selEl.value : '';
                        let varInstance = ocpp2xVarJs.getVariable(key);
                        let temp = {
                            variable: { name: varInstance.variableName },
                            component: { name: varInstance.componentName }
                        };
                        let inputEls = valueTd.querySelectorAll('input');
                        if (inputEls.length === 1) {
                            temp.attributeValue = inputEls[0].value;
                        }
                        if (inputEls.length === 2) {
                            temp.component.evse = {};
                            if (inputEls[0].getAttribute('meter-value') === 'value') {
                                temp.attributeValue = inputEls[0].value;
                                temp.component.evse.id = Number(inputEls[1].value);
                            } else {
                                temp.component.evse.id = Number(inputEls[0].value);
                                temp.attributeValue = inputEls[1].value;
                            }
                        }
                        params.push(temp);
                    });
                }
                break;
            case 'SendLocalList':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = [];
                let slSel = _id(ocppCommandType + 'Value3');
                if (slSel) {
                    for (let i = 0; i < slSel.options.length; i++) {
                        params[2].push(slSel.options[i].value);
                    }
                }
                params[3] = _val(ocppCommandType + 'Value4');
                break;
            case 'RequestStartTransaction':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                params[3] = _val(ocppCommandType + 'Value4');
                params[4] = _val(ocppCommandType + 'Value5');
                params[5] = _val(ocppCommandType + 'Value6');
                params[6] = _val(ocppCommandType + 'Value7');
                params[7] = _val(ocppCommandType + 'Value8');
                params[8] = _val(ocppCommandType + 'Value9');
                params[9] = _val(ocppCommandType + 'Value10');
                params[10] = _val(ocppCommandType + 'Value11');
                params[11] = _val(ocppCommandType + 'Value12');
                params[12] = _val(ocppCommandType + 'Value13');
                params[13] = _val(ocppCommandType + 'Value14');
                params[14] = _val(ocppCommandType + 'Value15');
                params[15] = _val(ocppCommandType + 'Value16');
                params[16] = _val(ocppCommandType + 'Value17');
                params[17] = [];
                params[18] = _val(ocppCommandType + 'Value19');
                let rtPeriods = _id(ocppCommandType + 'Value18');
                if (rtPeriods) {
                    Array.from(rtPeriods.children).forEach(function(tr) {
                        let startPeriod = tr.children[0].querySelector('input').value;
                        let numberPhases = tr.children[1].querySelector('input').value;
                        let limit = tr.children[2].querySelector('input').value;
                        params[17].push({ startPeriod: Number(startPeriod), numberPhases: Number(numberPhases), limit: Number(limit) });
                    });
                }
                break;
            case 'SetChargingProfile':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                params[3] = _val(ocppCommandType + 'Value4');
                params[4] = _val(ocppCommandType + 'Value5');
                params[5] = _val(ocppCommandType + 'Value6');
                params[6] = _val(ocppCommandType + 'Value7');
                params[7] = _val(ocppCommandType + 'Value8');
                params[8] = _val(ocppCommandType + 'Value9');
                params[9] = _val(ocppCommandType + 'Value10');
                params[10] = _val(ocppCommandType + 'Value11');
                params[11] = _val(ocppCommandType + 'Value12');
                params[12] = _val(ocppCommandType + 'Value13');
                params[13] = [];
                let scpPeriods = _id(ocppCommandType + 'Value14');
                if (scpPeriods) {
                    Array.from(scpPeriods.children).forEach(function(tr) {
                        let startPeriod = tr.children[0].querySelector('input').value;
                        let numberPhases = tr.children[1].querySelector('input').value;
                        let limit = tr.children[2].querySelector('input').value;
                        params[13].push({ startPeriod: Number(startPeriod), numberPhases: Number(numberPhases), limit: Number(limit) });
                    });
                }
                params[14] = _val(ocppCommandType + 'Value15');
                break;
            case 'SetVariableMonitoring':
                let svmTbody = _id(ocppCommandType + 'Value1');
                params[0] = [];
                if (svmTbody) {
                    Array.from(svmTbody.children).forEach(function(tr) {
                        let type = tr.children[0].querySelector('select').value;
                        let value = tr.children[1].querySelector('input').value;
                        let severity = tr.children[2].querySelector('input').value;
                        let component = tr.children[3].querySelector('input').value;
                        let evseId = tr.children[4].querySelector('input').value;
                        let variable = tr.children[5].querySelector('input').value;
                        let temp = { value: Number(value), type: type, severity: Number(severity), component: { name: component }, variable: { name: variable } };
                        if (evseId && evseId !== '') temp.component.evse = { id: Number(evseId) };
                        params[0].push(temp);
                    });
                }
                break;
            case 'GetLog':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                params[3] = _val(ocppCommandType + 'Value4');
                params[4] = _val(ocppCommandType + 'Value5');
                params[5] = _val(ocppCommandType + 'Value6');
                params[6] = _val(ocppCommandType + 'Value7');
                break;
            case 'CostUpdated':
            case 'GetBaseReport':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                break;
            case 'SetMonitoringLevel':
            case 'ClearDisplayMessage':
            case 'GetTransactionStatus':
            case 'SetMonitoringBase':
                params[0] = _val(ocppCommandType + 'Value1');
                break;
            case 'ClearVariableMonitoring':
                params[0] = [];
                let idStr = _val(ocppCommandType + 'Value1');
                if (idStr) {
                    let idParts = idStr.split(',');
                    for (var ci = 0; ci < idParts.length; ci++) {
                        params[0].push(Number(idParts[ci].trim()));
                    }
                }
                break;
            case 'SetDisplayMessage':
                params[0]  = _val(ocppCommandType + 'Value1');
                params[1]  = _val(ocppCommandType + 'Value2');
                params[2]  = _val(ocppCommandType + 'Value3');
                params[3]  = _val(ocppCommandType + 'Value4');
                params[4]  = _val(ocppCommandType + 'Value5');
                params[5]  = _val(ocppCommandType + 'Value6');
                params[6]  = _val(ocppCommandType + 'Value7');
                params[7]  = _val(ocppCommandType + 'Value8');
                params[8]  = _val(ocppCommandType + 'Value9');
                params[9]  = _val(ocppCommandType + 'Value10');
                params[10] = _val(ocppCommandType + 'Value11');
                params[11] = _val(ocppCommandType + 'Value12');
                break;
            case 'CustomerInformation':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                params[3] = _val(ocppCommandType + 'Value4');
                params[4] = _val(ocppCommandType + 'Value5');
                params[5] = _val(ocppCommandType + 'Value6');
                break;
            case 'SetNetworkProfile':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                params[3] = _val(ocppCommandType + 'Value4');
                params[4] = _val(ocppCommandType + 'Value5');
                params[5] = _val(ocppCommandType + 'Value6');
                params[6] = _val(ocppCommandType + 'Value7');
                break;
            case 'GetReport':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                params[3] = _val(ocppCommandType + 'Value4');
                params[4] = _val(ocppCommandType + 'Value5');
                break;
            case 'GetMonitoringReport':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _getSelectedValues(ocppCommandType + 'Value2');
                params[2] = [];
                let gmrTbody = _id(ocppCommandType + 'Value3');
                if (gmrTbody) {
                    Array.from(gmrTbody.querySelectorAll('tr')).forEach(function(tr) {
                        params[2].push({
                            cName: tr.children[0].querySelector('input').value,
                            evseId: tr.children[1].querySelector('input').value,
                            aName: tr.children[2].querySelector('input').value
                        });
                    });
                }
                break;
            case 'GetChargingProfiles':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                params[3] = _val(ocppCommandType + 'Value4');
                params[4] = [];
                let cpIdStr = _val(ocppCommandType + 'Value5');
                if (cpIdStr && cpIdStr !== '') params[4] = cpIdStr.split(',');
                let chargingLimitSource = [];
                document.querySelectorAll("input[name='GetChargingProfileCheckBox']").forEach(function(cb) {
                    if (cb.checked) chargingLimitSource.push(cb.getAttribute('meta-value'));
                });
                params[5] = chargingLimitSource;
                break;
            case 'UpdateFirmware':
                params[0] = _val(ocppCommandType + 'Value1');
                params[1] = _val(ocppCommandType + 'Value2');
                params[2] = _val(ocppCommandType + 'Value3');
                params[3] = _val(ocppCommandType + 'Value4');
                params[4] = _val(ocppCommandType + 'Value5');
                params[5] = _val(ocppCommandType + 'Value6');
                params[6] = _val(ocppCommandType + 'Value7');
                params[7] = _val(ocppCommandType + 'Value8');
                break;
            case 'GetLocalListVersion':
            case 'ClearCache':
                break;
        }

        let valueStr = ocpp2xCommandJs.makeParamDev(ocppCommandType, params);
        _sendCommand(ocppCommandType, valueStr);
    }

    /* ── 유틸: select 다중 선택값 ───────────────────────────────────── */
    function _getSelectedValues(selId) {
        let sel = _id(selId);
        if (!sel) return [];
        let result = [];
        for (let i = 0; i < sel.options.length; i++) {
            if (sel.options[i].selected) result.push(sel.options[i].value);
        }
        return result;
    }

    /* ── API 호출 ────────────────────────────────────────────────────── */
    function _sendCommand(cmdType, valueStr) {
        let cpId = _val('csId').trim();
        if (!cpId) { alert('충전기 ID를 입력하세요.'); return; }

        let payload;
        try { payload = JSON.parse(valueStr); } catch (e) { payload = {}; }

        if (!confirm('명령문을 전송하시겠습니까?\n명령: ' + cmdType + '\n대상: ' + cpId)) return;

        let btn = _id('btnCommand');
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>전송 중...';

        _addLog('info', '[' + cmdType + '] → ' + cpId);

        fetch(_ctx + '/ocpp2x/bypass/' + cpId, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ action: cmdType, payload: payload })
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

    /* ── 세션 목록 ───────────────────────────────────────────────────── */
    function loadSessions() {
        fetch(_ctx + '/ocpp2x/bypass/sessions')
            .then(function (res) { return res.json(); })
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
                    return '<button class="list-group-item list-group-item-action cp-item py-2 px-3"'
                        + ' data-cpid="' + cpId + '"'
                        + ' onclick="ocpp2xControlJs.selectCp(\'' + cpId + '\')">'
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
            '<span class="log-badge" style="background:' + badgeColor + '">' + badgeText + '</span> '
            + '<span class="log-time">' + time + '</span> ' + msg;
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
            navigator.clipboard.writeText(box.textContent).then(function () { alert('복사되었습니다.'); });
        }
    }

    /* ── 동적 행 관리 ────────────────────────────────────────────────── */
    function _addChargingSchedulePeriod() {
        let html = '<tr>';
        html += '<td><input type="number" placeholder="0" class="form-control form-control-sm"></td>';
        html += '<td><input type="number" placeholder="3" class="form-control form-control-sm"></td>';
        html += '<td><input type="number" placeholder="1" class="form-control form-control-sm"></td>';
        html += '<td><button type="button" class="btn btn-sm btn-outline-danger" onclick="ocpp2xControlJs.removeRow(this)">삭제</button></td>';
        html += '</tr>';
        let tbody = _id('SetChargingProfileValue14');
        if (tbody) tbody.insertAdjacentHTML('beforeend', html);
    }

    function _addRTChargingSchedulePeriod() {
        let html = '<tr>';
        html += '<td><input type="number" placeholder="0" class="form-control form-control-sm"></td>';
        html += '<td><input type="number" placeholder="3" class="form-control form-control-sm"></td>';
        html += '<td><input type="number" placeholder="1" class="form-control form-control-sm"></td>';
        html += '<td><button type="button" class="btn btn-sm btn-outline-danger" onclick="ocpp2xControlJs.removeRow(this)">삭제</button></td>';
        html += '</tr>';
        let tbody = _id('RequestStartTransactionValue18');
        if (tbody) tbody.insertAdjacentHTML('beforeend', html);
    }

    function _addSetVariableMonitoring() {
        let html = '<tr>';
        html += '<td><select class="form-select form-select-sm">'
            + '<option value="UpperThreshold">UpperThreshold</option>'
            + '<option value="LowerThreshold">LowerThreshold</option>'
            + '<option value="Delta">Delta</option>'
            + '<option value="Periodic">Periodic</option>'
            + '<option value="PeriodicClockAligned">PeriodicClockAligned</option>'
            + '</select></td>';
        html += '<td><input type="number" placeholder="0" class="form-control form-control-sm"></td>';
        html += '<td><input type="number" placeholder="0" class="form-control form-control-sm"></td>';
        html += '<td><input type="text" class="form-control form-control-sm"></td>';
        html += '<td><input type="number" class="form-control form-control-sm"></td>';
        html += '<td><input type="text" class="form-control form-control-sm"></td>';
        html += '<td><button type="button" class="btn btn-sm btn-outline-danger" onclick="ocpp2xControlJs.removeRow(this)">삭제</button></td>';
        html += '</tr>';
        let tbody = _id('SetVariableMonitoringValue1');
        if (tbody) tbody.insertAdjacentHTML('beforeend', html);
    }

    function _addGetMonitoringReport() {
        let html = '<tr>';
        html += '<td><input type="text" name="cname" placeholder="ex)EVSE" class="form-control form-control-sm"></td>';
        html += '<td><input type="number" name="evseId" placeholder="ex)1" class="form-control form-control-sm"></td>';
        html += '<td><input type="text" name="vname" placeholder="ex)Available" class="form-control form-control-sm"></td>';
        html += '<td><button type="button" class="btn btn-sm btn-outline-danger" onclick="ocpp2xControlJs.removeRow(this)">삭제</button></td>';
        html += '</tr>';
        let tbody = _id('GetMonitoringReportValue3');
        if (tbody) tbody.insertAdjacentHTML('beforeend', html);
    }

    function _addIdToken() {
        let input = _id('sendLocalListInput');
        let tag = input ? input.value.trim() : '';
        if (!tag) return;
        let sel = _id('SendLocalListValue3');
        let opt = document.createElement('option');
        opt.value = tag;
        opt.textContent = tag;
        opt.selected = true;
        sel.appendChild(opt);
        if (input) input.value = '';
    }

    function _addSetVariables() {
        let varList = ocpp2xVarJs.getAllVariableKey();
        let html = '<tr><td>';
        html += '<select class="form-select form-select-sm" onchange="ocpp2xControlJs.changeSetVariableItem(this)">';
        for (let i = 0; i < varList.length; i++) {
            html += '<option value="' + varList[i] + '"' + (i === 0 ? ' selected' : '') + '>' + varList[i] + '</option>';
        }
        html += '</select></td>';
        let variable = ocpp2xVarJs.getVariable(varList[0]);
        html += '<td><input type="text" class="form-control form-control-sm">';
        html += '<div class="small text-muted">';
        html += variable.componentName;
        if (variable.evse) html += ',evse:' + variable.evse;
        html += ',' + variable.variableName;
        if (variable.variableInstance) html += ',instance:' + variable.variableInstance;
        html += ',' + variable.variableCharacteristics.dataType;
        if (variable.variableCharacteristics.valuesList) html += ',values:' + variable.variableCharacteristics.valuesList;
        html += ',' + variable.variableAttributes.mutability;
        html += '</div></td>';
        html += '<td><button type="button" class="btn btn-sm btn-outline-danger" onclick="ocpp2xControlJs.removeRow(this)">삭제</button></td>';
        html += '</tr>';
        let tbody = _id('SetVariablesTbody');
        if (tbody) tbody.insertAdjacentHTML('beforeend', html);
    }

    function _changeSetVariableItem(target) {
        let variable = ocpp2xVarJs.getVariable(target.value);
        let valueTd = target.parentElement.parentElement.children[1];
        let html = '';
        if (variable.evse) {
            html += '<input type="number" placeholder="evseId" meter-value="evseId" class="form-control form-control-sm">';
        }
        html += '<input type="text" placeholder="value" meter-value="value" class="form-control form-control-sm">';
        html += '<div class="small text-muted">';
        html += variable.componentName;
        if (variable.evse) html += ',evse:' + variable.evse;
        html += ',' + variable.variableName;
        if (variable.variableInstance) html += ',instance:' + variable.variableInstance;
        html += ',' + variable.variableCharacteristics.dataType;
        if (variable.variableCharacteristics.valuesList) html += ',values:' + variable.variableCharacteristics.valuesList;
        html += ',' + variable.variableAttributes.mutability;
        html += '</div>';
        valueTd.innerHTML = html;
    }

    function _removeRow(btn) {
        btn.closest('tr').remove();
    }

    /* ── 공개 API ────────────────────────────────────────────────────── */
    return {
        init:                    _init,
        loadSessions:            loadSessions,
        selectCp:                _selectCp,
        clearLog:                _clearLog,
        copyResult:              _copyResult,
        removeRow:               _removeRow,
        changeSetVariableItem:   _changeSetVariableItem,
        addSetVariables:         _addSetVariables
    };
}();
