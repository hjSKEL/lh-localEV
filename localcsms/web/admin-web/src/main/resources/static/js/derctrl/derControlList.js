/**
 * OCPP 2.1 R04 — DER Control 마스터 관리
 */
var derControlListJs = function () {
    "use strict";

    var data = { searchCond: {}, result: [] };

    var CONTROL_TYPES = [
        "EnterService","FreqDroop","FreqWatt","FixedPFAbsorb","FixedPFInject","FixedVar",
        "Gradients","LimitMaxDischarge","VoltWatt","VoltVar","WattPF","WattVar",
        "HFMustTrip","HFMayTrip","HVMustTrip","HVMomCess","HVMayTrip",
        "LFMustTrip","LVMustTrip","LVMomCess","LVMayTrip","PowerMonitoringMustTrip"
    ];

    var SAMPLE_FREQ_DROOP = JSON.stringify({
        freqDroop: {
            priority: 6,
            overFreq: 50.5,
            underFreq: 49.5,
            overDroop: 0.05,
            underDroop: 0.05,
            responseTime: 10
        }
    }, null, 2);

    var SAMPLE_FREQ_WATT = JSON.stringify({
        curve: {
            priority: 4,
            yUnit: "PctMaxW",
            startTime: new Date().toISOString().replace(/\.\d{3}Z$/, "Z"),
            duration: 900,
            curveData: [
                { x: 49,   y: 75 },
                { x: 49.5, y: 90 },
                { x: 50.5, y: 100 },
                { x: 51,   y: 100 }
            ]
        }
    }, null, 2);

    var SAMPLE_ENTER_SERVICE = JSON.stringify({
        enterService: {
            priority: 1,
            highVoltage: 250,
            lowVoltage: 210,
            highFreq: 50.5,
            lowFreq: 49.5
        }
    }, null, 2);

    function _init() {
        _initOriginSelect();
        _initStatusSelect();
        _initControlTypeSelects();
        _initEvent();
        _searchOnClick();
    }

    function _initOriginSelect() {
        var html  = '<option value="">'         + _msg.originAll    + '</option>';
            html += '<option value="CSMS">'     + _msg.originCsms   + '</option>';
            html += '<option value="CS_REPORT">'+ _msg.originReport + '</option>';
        $("#originCd").html(html);
    }
    function _initStatusSelect() {
        var html  = '<option value="">'         + _msg.statusAll      + '</option>';
            html += '<option value="PENDING">'  + _msg.statusPending  + '</option>';
            html += '<option value="ACTIVE">'   + _msg.statusActive   + '</option>';
            html += '<option value="REPLACED">' + _msg.statusReplaced + '</option>';
            html += '<option value="CLEARED">'  + _msg.statusCleared  + '</option>';
            html += '<option value="REJECTED">' + _msg.statusRejected + '</option>';
        $("#statusCd").html(html);
    }
    function _initControlTypeSelects() {
        var html = '<option value="">' + _msg.typeAll + '</option>';
        for (var i = 0; i < CONTROL_TYPES.length; i++) {
            html += '<option value="' + CONTROL_TYPES[i] + '">' + CONTROL_TYPES[i] + '</option>';
        }
        $("#controlType").html(html);

        // 등록 모달의 controlType (전체 표시 — 전체는 옵션 제거)
        var html2 = '';
        for (var j = 0; j < CONTROL_TYPES.length; j++) {
            html2 += '<option value="' + CONTROL_TYPES[j] + '">' + CONTROL_TYPES[j] + '</option>';
        }
        $("#form_controlType").html(html2);
    }

    function _initEvent() {
        $("#btnSearch").click(_searchOnClick);
        $("#originCd, #controlType, #statusCd").change(_searchOnClick);
        $("#btnReset").click(_searchReset);
        $("#btnRegister").click(_openRegister);
        $("#btnSave").click(_submitRegister);
        $("#btnSample1").click(function () {
            $("#form_controlType").val("FreqDroop");
            $("#form_isDefault").val("Y");
            $("#form_paramJson").val(SAMPLE_FREQ_DROOP);
        });
        $("#btnSample2").click(function () {
            $("#form_controlType").val("FreqWatt");
            $("#form_isDefault").val("N");
            $("#form_paramJson").val(SAMPLE_FREQ_WATT);
        });
        $("#btnSample3").click(function () {
            $("#form_controlType").val("EnterService");
            $("#form_isDefault").val("N");
            $("#form_paramJson").val(SAMPLE_ENTER_SERVICE);
        });
        $("#btnValidate").click(_validateJson);
        $("#btnClearSave").click(_submitClear);
    }

    function _searchReset() {
        $("#fCpId").val(""); $("#fCsId").val("");
        $("#originCd").val(""); $("#controlType").val(""); $("#statusCd").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, derControlListJs.search);
        data.searchCond = {
            cpId: $("#fCpId").val().trim(),
            csId: $("#fCsId").val().trim(),
            originCd: $("#originCd").val(),
            controlType: $("#controlType").val(),
            statusCd: $("#statusCd").val()
        };
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="10">' + _commonMsg.searching + '</td></tr>');
        var p = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (p.pageNumber - 1) + "&pageItemSize=" + p.pageItemSize;
        param += "&cpId="        + encodeURIComponent(data.searchCond.cpId || '');
        param += "&csId="        + encodeURIComponent(data.searchCond.csId || '');
        param += "&originCd="    + encodeURIComponent(data.searchCond.originCd || '');
        param += "&controlType=" + encodeURIComponent(data.searchCond.controlType || '');
        param += "&statusCd="    + encodeURIComponent(data.searchCond.statusCd || '');
        $.ajax({
            type: 'GET', url: _ctx + "/ws/derctrl" + param, dataType: 'json',
            success: _display, error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _display(jsonData) {
        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();
        if (jsonData.criteria.totalItemCount == 0) {
            $("#tBodyList").append('<tr style="text-align:center;"><td colspan="10">' + _commonMsg.noData + '</td></tr>');
            return;
        }
        data.result = jsonData.result;
        var no = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        var html = '';
        for (var i = 0; i < data.result.length; i++) {
            var r = data.result[i];
            html += '<tr>';
            html += '<td>' + (i + no) + '</td>';
            html += '<td title="' + r.controlId + '">' + _shorten(r.controlId, 14) + '</td>';
            html += '<td>' + _originBadge(r.originCd) + '</td>';
            html += '<td>' + (r.cpId || '-') + ' / ' + (r.csId || '-') + '</td>';
            html += '<td>' + r.controlType + '</td>';
            html += '<td>' + (r.isDefault || '-') + '</td>';
            html += '<td>' + (r.priority == null ? '-' : r.priority) + '</td>';
            html += '<td>' + _statusBadge(r.statusCd) + '</td>';
            html += '<td>' + _formatDateTime(r.csAckDt) + '</td>';
            html += '<td>' + _actionButtons(i, r) + '</td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _shorten(s, n) { if (!s) return '-'; return s.length > n ? s.substring(0, n) + '…' : s; }
    function _originBadge(o) {
        if (o === 'CSMS')      return '<span class="label label-primary">CSMS</span>';
        if (o === 'CS_REPORT') return '<span class="label label-info">CS_REPORT</span>';
        return o || '-';
    }
    function _statusBadge(s) {
        switch (s) {
            case 'PENDING':  return '<span class="label label-warning">'+ _msg.statusPending +'</span>';
            case 'ACTIVE':   return '<span class="label label-primary">'+ _msg.statusActive +'</span>';
            case 'REPLACED': return '<span class="label label-default">'+ _msg.statusReplaced +'</span>';
            case 'CLEARED':  return '<span class="label label-default">'+ _msg.statusCleared +'</span>';
            case 'REJECTED': return '<span class="label label-danger">' + _msg.statusRejected +'</span>';
            default:         return s || '-';
        }
    }
    function _actionButtons(i, r) {
        var html = '<button class="btn btn-xs btn-info" onclick="derControlListJs.view(' + i + ')">' + _msg.btnView + '</button>';
        if (r.originCd === 'CSMS' && (r.statusCd === 'PENDING' || r.statusCd === 'REJECTED')) {
            html += ' <button class="btn btn-xs btn-primary" onclick="derControlListJs.push(\'' + r.controlId + '\')">' + _msg.btnPush + '</button>';
        }
        if (r.originCd === 'CSMS' && r.statusCd === 'ACTIVE') {
            html += ' <button class="btn btn-xs btn-warning" onclick="derControlListJs.clearOne(\'' + r.controlId + '\',\'' + (r.cpId || '') + '\',\'' + (r.csId || '') + '\')">' + _msg.btnClear + '</button>';
        }
        return html;
    }

    /* ----- 등록 ----- */
    function _openRegister() {
        $("#form_isDefault").val("Y");
        $("#form_cpId").val(""); $("#form_csId").val("");
        $("#form_controlType").val("FreqDroop");
        $("#form_description").val("");
        $("#form_paramJson").val(SAMPLE_FREQ_DROOP);
        $.ajax({
            type: 'GET', url: _ctx + "/ws/derctrl/nextId", dataType: 'json',
            success: function (res) { if (res.status === 'SUCCESS') $("#form_controlId").val(res.result); }
        });
        $("#Popup_Der_Register").modal();
    }

    function _validateJson() {
        var raw = $("#form_paramJson").val().trim();
        if (!raw) { toastr.warning(_msg.inputJson, _msg.derMgmt); return false; }
        try {
            JSON.parse(raw);
            toastr.success('OK', _msg.derMgmt);
            return true;
        } catch (e) {
            toastr.error(_msg.invalidJson + ': ' + e.message, _msg.derMgmt);
            return false;
        }
    }

    function _submitRegister() {
        var cpId = $("#form_cpId").val().trim();
        var csId = $("#form_csId").val().trim();
        var raw  = $("#form_paramJson").val().trim();
        if (!cpId || !csId) { toastr.warning(_msg.inputCpCs, _msg.derMgmt); return; }
        if (!raw)           { toastr.warning(_msg.inputJson, _msg.derMgmt); return; }
        try { JSON.parse(raw); } catch (e) { toastr.error(_msg.invalidJson + ': ' + e.message, _msg.derMgmt); return; }

        var body = {
            controlId:   $("#form_controlId").val().trim() || null,
            originCd:    'CSMS',
            cpId:        cpId,
            csId:        csId,
            isDefault:   $("#form_isDefault").val(),
            controlType: $("#form_controlType").val(),
            description: $("#form_description").val().trim() || null,
            paramJson:   raw
        };
        $.ajax({
            type: 'POST', url: _ctx + "/ws/derctrl",
            contentType: 'application/json', dataType: 'json',
            data: JSON.stringify(body),
            success: function (res) {
                if (res.status === 'SUCCESS') {
                    toastr.success(_msg.successRegister, _msg.derMgmt);
                    $("#Popup_Der_Register").modal('hide');
                    _search();
                } else {
                    toastr.error(res.result || _msg.failRegister, _msg.derMgmt);
                }
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); toastr.error(_msg.failRegister, _msg.derMgmt); }
        });
    }

    /* ----- Push (SetDERControl) ----- */
    function _push(controlId) {
        swal({
            title: _msg.derMgmt, text: _msg.confirmPush, type: 'warning',
            showCancelButton: true, confirmButtonColor: '#1c84c6',
            confirmButtonText: _msg.confirm, cancelButtonText: _msg.cancel, closeOnConfirm: true
        }, function () {
            $.ajax({
                type: 'PUT', url: _ctx + "/ws/derctrl/" + encodeURIComponent(controlId) + "/push",
                dataType: 'json',
                success: function (res) {
                    if (res.status === 'SUCCESS') {
                        toastr.success(_msg.successPush, _msg.derMgmt); _search();
                    } else {
                        toastr.error(res.result || _msg.failPush, _msg.derMgmt);
                    }
                },
                error: function (xhr) { parent.layerJs.fn_exception(xhr); toastr.error(_msg.failPush, _msg.derMgmt); }
            });
        });
    }

    /* ----- Clear ----- */
    function _clearOne(controlId, cpId, csId) {
        $("#clr_cpId").val(cpId);
        $("#clr_csId").val(csId);
        $("#clr_isDefault").val("");
        $("#clr_controlType").val("");
        $("#clr_controlId").val(controlId);
        $("#Popup_Der_Clear").modal();
    }
    function _submitClear() {
        var body = {
            cpId:        $("#clr_cpId").val().trim(),
            csId:        $("#clr_csId").val().trim(),
            isDefault:   $("#clr_isDefault").val() || null,
            controlType: $("#clr_controlType").val().trim() || null,
            controlId:   $("#clr_controlId").val().trim() || null
        };
        if (!body.cpId || !body.csId) { toastr.warning(_msg.inputCpCs, _msg.derMgmt); return; }
        $.ajax({
            type: 'PUT', url: _ctx + "/ws/derctrl/clear",
            contentType: 'application/json', dataType: 'json',
            data: JSON.stringify(body),
            success: function (res) {
                if (res.status === 'SUCCESS') {
                    toastr.success(_msg.successClear, _msg.derMgmt);
                    $("#Popup_Der_Clear").modal('hide');
                    _search();
                } else {
                    toastr.error(res.result || _msg.failClear, _msg.derMgmt);
                }
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); toastr.error(_msg.failClear, _msg.derMgmt); }
        });
    }

    /* ----- 상세 ----- */
    function _view(idx) {
        var r = data.result[idx];
        if (!r) return;
        var rows = [
            ['controlId',  r.controlId],
            ['origin',     r.originCd],
            ['CP / CS',    (r.cpId || '-') + ' / ' + (r.csId || '-')],
            ['type',       r.controlType],
            ['isDefault',  r.isDefault],
            ['priority',   r.priority == null ? '-' : r.priority],
            ['startTime',  _formatDateTime(r.startTime)],
            ['duration',   r.durationSec == null ? '-' : (r.durationSec + 's')],
            ['status',     _statusBadge(r.statusCd)],
            ['superseded', r.isSuperseded || '-'],
            ['csAckDt',    _formatDateTime(r.csAckDt)],
            ['reasonCd',   r.reasonCd || '-'],
            ['description',r.description || '-'],
            ['regDt',      _formatDateTime(r.writer && r.writer.registrationDate)]
        ];
        var html = '';
        for (var i = 0; i < rows.length; i++) {
            html += '<tr><th style="width:25%;">' + rows[i][0] + '</th><td>' + rows[i][1] + '</td></tr>';
        }
        $("#detailBody").html(html);
        var pj = r.paramJson;
        try { pj = JSON.stringify(JSON.parse(r.paramJson), null, 2); } catch (e) {}
        $("#detailJson").text(pj);
        $("#Popup_Der_Detail").modal();
    }

    function _fmtDate(d) {
        var y = d.getFullYear(), m = ('0' + (d.getMonth() + 1)).slice(-2), dd = ('0' + d.getDate()).slice(-2);
        return y + '-' + m + '-' + dd;
    }
    function _formatDateTime(v) {
        if (!v) return '-';
        var d = new Date(v), hh = ('0' + d.getHours()).slice(-2), mm = ('0' + d.getMinutes()).slice(-2);
        return _fmtDate(d) + ' ' + hh + ':' + mm;
    }

    return {
        init: _init,
        search: _search,
        view: _view,
        push: _push,
        clearOne: _clearOne
    };
}();
