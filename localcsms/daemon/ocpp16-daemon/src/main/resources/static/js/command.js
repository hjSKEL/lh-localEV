/**
 * OCPP 1.6 Command Console
 * - GET  /command/sessions   : 연결된 충전기 목록
 * - POST /command/{cpId}     : 명령 전송
 */

'use strict';

/* =========================================================
 * 1. 명령별 파라미터 정의
 * ========================================================= */
const COMMANDS = {
    RemoteStartTransaction: [
        { name: 'connectorId', type: 'number',  label: 'Connector ID', required: true,  min: 0, placeholder: '1' },
        { name: 'idTag',       type: 'text',    label: 'ID Tag',       required: true,  placeholder: 'RFID or App ID' },
    ],
    RemoteStopTransaction: [
        { name: 'transactionId', type: 'number', label: 'Transaction ID', required: true, placeholder: '0' },
    ],
    UnlockConnector: [
        { name: 'connectorId', type: 'number', label: 'Connector ID', required: true, min: 1, placeholder: '1' },
    ],
    Reset: [
        { name: 'type', type: 'select', label: '리셋 유형', required: true, options: ['Hard', 'Soft'] },
    ],
    ClearCache: [],
    ChangeAvailability: [
        { name: 'connectorId', type: 'number', label: 'Connector ID',      required: true,  min: 0, placeholder: '0 = 전체' },
        { name: 'type',        type: 'select', label: '가용성',            required: true,  options: ['Operative', 'Inoperative'] },
    ],
    ChangeConfiguration: [
        { name: 'key',   type: 'text', label: 'Key',   required: true,  placeholder: 'HeartbeatInterval' },
        { name: 'value', type: 'text', label: 'Value', required: true,  placeholder: '300' },
    ],
    GetConfiguration: [
        { name: '_keys', type: 'text', label: 'Key (쉼표 구분, 비우면 전체)', required: false, placeholder: 'HeartbeatInterval,MeterValueSampleInterval' },
    ],
    ReserveNow: [
        { name: 'connectorId',   type: 'number',         label: 'Connector ID',   required: true,  min: 1, placeholder: '1' },
        { name: 'expiryDate',    type: 'datetime-local', label: 'Expiry Date',    required: true },
        { name: 'idTag',         type: 'text',           label: 'ID Tag',         required: true },
        { name: 'reservationId', type: 'number',         label: 'Reservation ID', required: true,  placeholder: '1' },
        { name: 'parentIdTag',   type: 'text',           label: 'Parent ID Tag',  required: false },
    ],
    CancelReservation: [
        { name: 'reservationId', type: 'number', label: 'Reservation ID', required: true, placeholder: '1' },
    ],
    SetChargingProfile: '__json__',   // 복잡한 구조 → JSON 직접 입력
    ClearChargingProfile: [
        { name: 'id',                    type: 'number', label: 'Profile ID (선택)',    required: false },
        { name: 'connectorId',           type: 'number', label: 'Connector ID (선택)', required: false },
        { name: 'chargingProfilePurpose',type: 'select', label: 'Purpose (선택)',       required: false,
          options: ['', 'ChargePointMaxProfile', 'TxDefaultProfile', 'TxProfile'] },
        { name: 'stackLevel',            type: 'number', label: 'Stack Level (선택)',  required: false },
    ],
    GetCompositeSchedule: [
        { name: 'connectorId',      type: 'number', label: 'Connector ID',     required: true,  min: 0, placeholder: '1' },
        { name: 'duration',         type: 'number', label: 'Duration (초)',    required: true,  placeholder: '3600' },
        { name: 'chargingRateUnit', type: 'select', label: 'Rate Unit (선택)', required: false, options: ['', 'A', 'W'] },
    ],
    GetLocalListVersion: [],
    SendLocalList: [
        { name: 'listVersion', type: 'number', label: 'List Version', required: true, placeholder: '1' },
        { name: 'updateType',  type: 'select', label: 'Update Type',  required: true, options: ['Differential', 'Full'] },
    ],
    GetDiagnostics: [
        { name: 'location',      type: 'text',           label: 'Upload URL',           required: true,  placeholder: 'ftp://...' },
        { name: 'retries',       type: 'number',         label: '재시도 횟수 (선택)',   required: false },
        { name: 'retryInterval', type: 'number',         label: '재시도 간격 초 (선택)', required: false },
        { name: 'startTime',     type: 'datetime-local', label: 'Start Time (선택)',    required: false },
        { name: 'stopTime',      type: 'datetime-local', label: 'Stop Time (선택)',     required: false },
    ],
    UpdateFirmware: [
        { name: 'location',      type: 'text',           label: 'Download URL',          required: true },
        { name: 'retrieveDate',  type: 'datetime-local', label: 'Retrieve Date',         required: true },
        { name: 'retries',       type: 'number',         label: '재시도 횟수 (선택)',    required: false },
        { name: 'retryInterval', type: 'number',         label: '재시도 간격 초 (선택)', required: false },
    ],
    GetLog: [
        { name: 'logType',              type: 'select',         label: 'Log Type',                required: true,  options: ['DiagnosticsLog', 'SecurityLog'] },
        { name: 'requestId',            type: 'number',         label: 'Request ID',              required: true,  placeholder: '1' },
        { name: 'log.remoteLocation',   type: 'text',           label: 'Remote Location (URL)',   required: true,  placeholder: 'ftp://...' },
        { name: 'log.oldestTimestamp',  type: 'datetime-local', label: 'Oldest Timestamp (선택)', required: false },
        { name: 'log.latestTimestamp',  type: 'datetime-local', label: 'Latest Timestamp (선택)', required: false },
    ],
    TriggerMessage: [
        { name: 'requestedMessage', type: 'select', label: '메시지 유형', required: true,
          options: ['BootNotification','DiagnosticsStatusNotification','FirmwareStatusNotification','Heartbeat','MeterValues','StatusNotification'] },
        { name: 'connectorId', type: 'number', label: 'Connector ID (선택)', required: false, min: 1 },
    ],
    DataTransfer: [
        { name: 'vendorId',  type: 'text', label: 'Vendor ID',         required: true },
        { name: 'messageId', type: 'text', label: 'Message ID (선택)', required: false },
        { name: 'data',      type: 'text', label: 'Data (선택)',       required: false },
    ],
};

/* =========================================================
 * 2. 상태
 * ========================================================= */
let selectedCpId   = null;
let refreshTimer   = null;
let isJsonMode     = false;

/* =========================================================
 * 3. 초기화
 * ========================================================= */
document.addEventListener('DOMContentLoaded', () => {
    loadSessions();
    startAutoRefresh();
});

/* =========================================================
 * 4. 세션 목록
 * ========================================================= */
function loadSessions() {
    fetch('/command/sessions')
        .then(r => r.json())
        .then(data => renderSessions(data.connected || []))
        .catch(e => addLog('error', 'SESSIONS', null, '세션 조회 실패: ' + e.message));
}

function renderSessions(cpIds) {
    const list = document.getElementById('sessionList');
    const count = document.getElementById('sessionCount');
    count.textContent = cpIds.length + '개 연결';

    if (cpIds.length === 0) {
        list.innerHTML = '<div class="text-center text-muted p-3 small">연결된 충전기 없음</div>';
        return;
    }

    list.innerHTML = cpIds.map(id => `
        <button type="button"
                class="list-group-item list-group-item-action cp-item d-flex justify-content-between align-items-center px-3 py-2 ${id === selectedCpId ? 'active' : ''}"
                onclick="selectCp('${id}')">
            <span><i class="bi bi-plug-fill me-2"></i>${id}</span>
            <span class="badge bg-success">연결</span>
        </button>
    `).join('');
}

function selectCp(cpId) {
    selectedCpId = cpId;
    document.getElementById('selectedCpId').value = cpId;
    renderSessions(
        Array.from(document.querySelectorAll('.cp-item')).map(el =>
            el.querySelector('span:first-child').textContent.trim()
        )
    );
    addLog('info', 'SELECT', cpId, '충전기 선택됨');
}

function startAutoRefresh() {
    refreshTimer = setInterval(loadSessions, 10000);
}

/* =========================================================
 * 5. 명령 선택 → 폼 렌더링
 * ========================================================= */
function onActionChange() {
    const action = document.getElementById('actionSelect').value;
    const paramForm       = document.getElementById('paramForm');
    const jsonToggleArea  = document.getElementById('jsonToggleArea');
    const jsonPayloadArea = document.getElementById('jsonPayloadArea');
    const jsonModeToggle  = document.getElementById('jsonModeToggle');

    hideResult();
    isJsonMode = false;
    jsonModeToggle.checked = false;

    if (!action) {
        paramForm.innerHTML = '';
        jsonToggleArea.style.display = 'none';
        return;
    }

    const def = COMMANDS[action];

    // SetChargingProfile 등 복잡 구조 → JSON만
    if (def === '__json__') {
        paramForm.innerHTML = '';
        jsonToggleArea.style.display = 'block';
        jsonPayloadArea.style.display = 'block';
        jsonModeToggle.checked = true;
        isJsonMode = true;
        document.getElementById('jsonPayload').value = getDefaultJson(action);
        return;
    }

    // 파라미터 폼 렌더링
    paramForm.innerHTML = def.map(p => buildParamHtml(p)).join('');
    jsonToggleArea.style.display = def.length > 0 ? 'block' : 'none';
    jsonPayloadArea.style.display = 'none';
}

function buildParamHtml(p) {
    const reqClass  = p.required ? 'param-required' : '';
    const requiredA = p.required ? 'required' : '';

    if (p.type === 'select') {
        const opts = p.options.map(o =>
            `<option value="${o}">${o || '-- 선택 안함 --'}</option>`
        ).join('');
        return `
            <div class="mb-2">
                <label class="form-label param-label ${reqClass}">${p.label}</label>
                <select id="param_${p.name}" class="form-select form-select-sm" ${requiredA}>
                    ${opts}
                </select>
            </div>`;
    }

    const extra = p.min !== undefined ? `min="${p.min}"` : '';
    const ph    = p.placeholder ? `placeholder="${p.placeholder}"` : '';
    return `
        <div class="mb-2">
            <label class="form-label param-label ${reqClass}">${p.label}</label>
            <input type="${p.type}" id="param_${p.name}"
                   class="form-control form-control-sm" ${requiredA} ${extra} ${ph}>
        </div>`;
}

/* =========================================================
 * 6. JSON 직접 편집 토글
 * ========================================================= */
function toggleJsonMode() {
    isJsonMode = document.getElementById('jsonModeToggle').checked;
    const jsonPayloadArea = document.getElementById('jsonPayloadArea');

    if (isJsonMode) {
        jsonPayloadArea.style.display = 'block';
        // 현재 폼 값 → JSON으로 변환하여 textarea에 채우기
        try {
            const payload = buildPayloadFromForm();
            document.getElementById('jsonPayload').value = JSON.stringify(payload, null, 2);
        } catch (e) {
            document.getElementById('jsonPayload').value = '{}';
        }
    } else {
        jsonPayloadArea.style.display = 'none';
    }
}

/* =========================================================
 * 7. Payload 생성
 * ========================================================= */
function buildPayloadFromForm() {
    const action = document.getElementById('actionSelect').value;
    const def    = COMMANDS[action];

    if (def === '__json__' || isJsonMode) {
        const raw = document.getElementById('jsonPayload').value.trim();
        return raw ? JSON.parse(raw) : {};
    }

    const payload = {};

    def.forEach(p => {
        const el = document.getElementById('param_' + p.name);
        if (!el) return;

        let val = el.value.trim();
        if (val === '' && !p.required) return;

        // 특수 처리
        if (p.name === '_keys') {
            // GetConfiguration: key 배열
            if (val) payload.key = val.split(',').map(k => k.trim()).filter(Boolean);
            return;
        }

        if (p.type === 'number') {
            val = val !== '' ? Number(val) : undefined;
            if (val === undefined) return;
        }

        if (p.type === 'datetime-local' && val) {
            val = new Date(val).toISOString();
        }

        // 중첩 키 처리 (예: log.remoteLocation → payload.log.remoteLocation)
        setNestedValue(payload, p.name, val);
    });

    return payload;
}

function setNestedValue(obj, path, value) {
    const parts = path.split('.');
    let cur = obj;
    for (let i = 0; i < parts.length - 1; i++) {
        if (!cur[parts[i]]) cur[parts[i]] = {};
        cur = cur[parts[i]];
    }
    cur[parts[parts.length - 1]] = value;
}

/* =========================================================
 * 8. 명령 전송
 * ========================================================= */
function sendCommand() {
    const cpId   = document.getElementById('selectedCpId').value.trim();
    const action = document.getElementById('actionSelect').value;

    if (!cpId)   { showAlert('충전기 ID를 입력하거나 목록에서 선택하세요.'); return; }
    if (!action) { showAlert('명령을 선택하세요.'); return; }

    let payload;
    try {
        payload = buildPayloadFromForm();
    } catch (e) {
        showAlert('JSON 형식 오류: ' + e.message);
        return;
    }

    const sendBtn = document.getElementById('sendBtn');
    sendBtn.disabled = true;
    sendBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>전송 중...';

    addLog('info', action, cpId, '명령 전송 중...');
    hideResult();

    fetch(`/command/${encodeURIComponent(cpId)}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ action, payload })
    })
    .then(r => r.json())
    .then(data => {
        const ok = data.status === 'SUCCESS';
        showResult(ok, data);
        addLog(ok ? 'success' : 'fail', action, cpId,
               ok ? JSON.stringify(data.result) : (data.message || 'FAIL'));
    })
    .catch(e => {
        showResult(false, { status: 'ERROR', message: e.message });
        addLog('fail', action, cpId, '통신 오류: ' + e.message);
    })
    .finally(() => {
        sendBtn.disabled = false;
        sendBtn.innerHTML = '<i class="bi bi-send-fill me-2"></i>명령 전송';
    });
}

/* =========================================================
 * 9. 결과 표시
 * ========================================================= */
function showResult(ok, data) {
    const box    = document.getElementById('resultBox');
    const header = document.getElementById('resultHeader');
    box.className    = 'mt-3 p-2 ' + (ok ? 'result-success' : 'result-fail');
    box.style.display = 'block';
    box.textContent   = JSON.stringify(data, null, 2);
    header.style.removeProperty('display');
}

function hideResult() {
    document.getElementById('resultBox').style.display = 'none';
    document.getElementById('resultHeader').style.display = 'none';
}

function copyResult() {
    const text = document.getElementById('resultBox').textContent;
    navigator.clipboard.writeText(text).then(() => {
        const btn = document.querySelector('#resultHeader button');
        btn.innerHTML = '<i class="bi bi-check2"></i> 복사됨';
        setTimeout(() => { btn.innerHTML = '<i class="bi bi-clipboard"></i> 복사'; }, 1500);
    });
}

/* =========================================================
 * 10. 로그 패널
 * ========================================================= */
function addLog(type, action, cpId, message) {
    const panel = document.getElementById('logPanel');

    // 첫 로그 등록 시 placeholder 제거
    if (panel.querySelector('.text-muted')) panel.innerHTML = '';

    const typeClass = { success: 'log-success', fail: 'log-fail', info: 'log-info', error: 'log-fail' }[type] || 'log-info';
    const badge     = { success: 'SUCCESS', fail: 'FAIL', info: 'INFO', error: 'ERROR' }[type] || type.toUpperCase();
    const now       = new Date().toLocaleTimeString('ko-KR');

    const entry = document.createElement('div');
    entry.className = `log-entry ${typeClass}`;
    entry.innerHTML = `
        <div class="d-flex gap-2 align-items-start">
            <span class="log-badge">${badge}</span>
            <div class="flex-fill">
                <div class="d-flex justify-content-between">
                    <strong>${action}</strong>
                    <span class="log-time">${now}</span>
                </div>
                ${cpId ? `<div class="text-muted" style="font-size:0.75rem;">cpId: ${cpId}</div>` : ''}
                <div class="mt-1" style="word-break:break-all;">${escapeHtml(message)}</div>
            </div>
        </div>`;

    panel.insertBefore(entry, panel.firstChild);

    // 최대 100개 유지
    while (panel.children.length > 100) panel.removeChild(panel.lastChild);
}

function clearLog() {
    document.getElementById('logPanel').innerHTML =
        '<div class="text-center text-muted p-3 small">명령 기록이 없습니다</div>';
}

/* =========================================================
 * 11. 유틸
 * ========================================================= */
function showAlert(msg) {
    alert(msg);
}

function escapeHtml(str) {
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

/** SetChargingProfile 기본 JSON 예시 */
function getDefaultJson(action) {
    if (action === 'SetChargingProfile') {
        return JSON.stringify({
            connectorId: 1,
            csChargingProfiles: {
                chargingProfileId: 1,
                stackLevel: 0,
                chargingProfilePurpose: "TxDefaultProfile",
                chargingProfileKind: "Absolute",
                chargingSchedule: {
                    chargingRateUnit: "A",
                    chargingSchedulePeriod: [
                        { startPeriod: 0, limit: 32 }
                    ]
                }
            }
        }, null, 2);
    }
    return '{}';
}
