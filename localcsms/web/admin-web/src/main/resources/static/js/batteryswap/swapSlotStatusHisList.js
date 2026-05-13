/**
 * 배터리 슬롯 상태이력
 */
var swapSlotStatusHisListJs = function () {
    "use strict";

    var data = {
        searchCond: {},
        stations: {} // { cpId: [csId,...] }
    };

    var STATE_BADGE = {
        EMPTY:         { cls: 'label-default',  txt: 'EMPTY' },
        OCCUPIED_IDLE: { cls: 'label-info',     txt: 'OCCUPIED_IDLE' },
        CHARGING:      { cls: 'label-primary',  txt: 'CHARGING' },
        READY:         { cls: 'label-success',  txt: 'READY' },
        RESERVED:      { cls: 'label-warning',  txt: 'RESERVED' },
        FAULT:         { cls: 'label-danger',   txt: 'FAULT' },
        MAINTENANCE:   { cls: 'label-warning',  txt: 'MAINTENANCE' }
    };

    var EVENT_BADGE = {
        BatteryIn:          'label-primary',
        BatteryOut:         'label-primary',
        StatusNotification: 'label-primary',
        TransactionEvent:   'label-primary',
        Manual:             'label-warning',
        Timeout:            'label-danger'
    };

    function _init() {
        _initDates();
        _initEventTypeOptions();
        _initNewStateOptions();
        _initEvseOptions(0);
        _loadPoints();
        _initEvent();
        _applyQueryString();
        _searchOnClick();
    }

    function _initDates() {
        $("#date1").val(_formatDate(_addDay(new Date(), -1)));
        $("#date2").val(_formatDate(new Date()));
        $('#date1,#date2').datepicker({ todayBtn: "linked", autoclose: true, format: "yyyy-mm-dd" });
    }

    function _initEventTypeOptions() {
        var html = '<option value="">' + _msg.eventAll + '</option>';
        ['BatteryIn','BatteryOut','StatusNotification','TransactionEvent','Manual','Timeout'].forEach(function (t) {
            html += '<option value="' + t + '">' + t + '</option>';
        });
        $("#eventType").html(html);
    }

    function _initNewStateOptions() {
        var codes = parent.commonCodeJs.getCodesByParentCode("BSSS00") || [];
        var html = '<option value="">' + _msg.newStateAll + '</option>';
        if (codes.length === 0) {
            ['EMPTY','OCCUPIED_IDLE','CHARGING','READY','RESERVED','FAULT','MAINTENANCE'].forEach(function (c) {
                html += '<option value="' + c + '">' + c + '</option>';
            });
        } else {
            for (var i = 0; i < codes.length; ++i) {
                html += '<option value="' + codes[i].code + '">' + codes[i].codeName + '</option>';
            }
        }
        $("#newState").html(html);
    }

    function _initEvseOptions(maxSlot) {
        var html = '<option value="">' + _msg.slotAll + '</option>';
        for (var i = 1; i <= (maxSlot || 16); ++i) {
            html += '<option value="' + i + '">#' + i + '</option>';
        }
        $("#evseSelect").html(html);
    }

    function _loadPoints() {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/batterySwap/point?pageNumber=0&pageItemSize=1000",
            dataType: 'json',
            success: function (json) {
                var html = '<option value="">' + _msg.pointAll + '</option>';
                if (json && json.result) {
                    for (var i = 0; i < json.result.length; ++i) {
                        var p = json.result[i];
                        html += '<option value="' + p.cpId + '">' + p.cpId + ' ' + (p.cpName || '') + '</option>';
                    }
                }
                $("#cpSelect").html(html);
                _loadStations('');
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _loadStations(cpId) {
        if (!cpId) {
            $("#csSelect").html('<option value="">' + _msg.stationAll + '</option>');
            return;
        }
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/batterySwap/station?cpId=" + encodeURIComponent(cpId) + "&pageNumber=0&pageItemSize=1000",
            dataType: 'json',
            success: function (json) {
                var html = '<option value="">' + _msg.stationAll + '</option>';
                var maxSlot = 0;
                if (json && json.result) {
                    for (var i = 0; i < json.result.length; ++i) {
                        var s = json.result[i];
                        html += '<option value="' + s.csId + '" data-slots="' + (s.totalSlotCount || 0) + '">' + s.csId + '</option>';
                        if ((s.totalSlotCount || 0) > maxSlot) maxSlot = s.totalSlotCount;
                    }
                }
                $("#csSelect").html(html);
                _initEvseOptions(maxSlot);
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _initEvent() {
        $("#btnSearch").click(_searchOnClick);
        $("#btnReset").click(_searchReset);
        $("#cpSelect").change(function () {
            _loadStations($(this).val());
            _searchOnClick();
        });
        $("#csSelect").change(function () {
            var slots = parseInt($("#csSelect option:selected").data('slots'), 10) || 16;
            _initEvseOptions(slots);
            _searchOnClick();
        });
        $("#evseSelect,#eventType,#newState").change(_searchOnClick);
        $("#sWord").keypress(function (e) { if (e.keyCode == 13) _searchOnClick(); });
    }

    function _applyQueryString() {
        var qs = location.search;
        if (!qs) return;
        var params = {};
        qs.substring(1).split('&').forEach(function (kv) {
            var i = kv.indexOf('=');
            if (i > 0) params[decodeURIComponent(kv.substring(0, i))] = decodeURIComponent(kv.substring(i + 1));
        });
        if (params.cpId) {
            $("#cpSelect").val(params.cpId);
            _loadStations(params.cpId);
            setTimeout(function () {
                if (params.csId) {
                    $("#csSelect").val(params.csId);
                    var slots = parseInt($("#csSelect option:selected").data('slots'), 10) || 16;
                    _initEvseOptions(slots);
                    if (params.evseId) $("#evseSelect").val(params.evseId);
                }
                _searchOnClick();
            }, 250);
        }
    }

    function _searchReset() {
        _initDates();
        $("#cpSelect").val("");
        _loadStations('');
        $("#csSelect").val("");
        _initEvseOptions(0);
        $("#evseSelect").val("");
        $("#eventType").val("");
        $("#newState").val("");
        $("#searchType").val("BAT_SN");
        $("#sWord").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, swapSlotStatusHisListJs.search);
        data.searchCond.cpId = $("#cpSelect").val();
        data.searchCond.csId = $("#csSelect").val();
        data.searchCond.evseId = $("#evseSelect").val();
        data.searchCond.eventType = $("#eventType").val();
        data.searchCond.newState = $("#newState").val();
        data.searchCond.batterySerialNo = "";
        data.searchCond.eventRefId = "";

        var key = $("#sWord").val().trim();
        switch ($("#searchType").val()) {
            case "BAT_SN": data.searchCond.batterySerialNo = key; break;
            case "REF_ID": data.searchCond.eventRefId = key; break;
        }
        data.searchCond.fromDate = $("#date1").val() + " 00:00:00";
        data.searchCond.toDate   = $("#date2").val() + " 23:59:59";
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="14">' + _commonMsg.searching + '</td></tr>');

        var paging = pageInfoJs.getPaging();
        var p = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        p += "&cpId=" + encodeURIComponent(data.searchCond.cpId || '');
        p += "&csId=" + encodeURIComponent(data.searchCond.csId || '');
        if (data.searchCond.evseId) p += "&evseId=" + encodeURIComponent(data.searchCond.evseId);
        p += "&eventType=" + encodeURIComponent(data.searchCond.eventType || '');
        p += "&newState=" + encodeURIComponent(data.searchCond.newState || '');
        p += "&batterySerialNo=" + encodeURIComponent(data.searchCond.batterySerialNo || '');
        p += "&eventRefId=" + encodeURIComponent(data.searchCond.eventRefId || '');
        p += "&fromDate=" + encodeURIComponent(data.searchCond.fromDate || '');
        p += "&toDate=" + encodeURIComponent(data.searchCond.toDate || '');

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/batterySwap/slot/his" + p,
            dataType: 'json',
            success: _display,
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _display(json) {
        pageInfoJs.setTotalCount(json.criteria.totalItemCount);
        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', json.criteria.totalItemCount));
        $("#tBodyList").empty();

        if (json.criteria.totalItemCount == 0) {
            $("#tBodyList").append('<tr style="text-align:center;"><td colspan="14">' + _commonMsg.noData + '</td></tr>');
            return;
        }

        var result = json.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        var html = '';
        for (var i = 0; i < result.length; ++i) {
            var row = result[i];
            var sohStyle = (row.currentSoH != null && Number(row.currentSoH) < 80) ? 'color:#dd6b55;' : '';
            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + _formatDateTime(row.eventDate) + '</td>';
            html += '<td>' + row.cpId + '</td>';
            html += '<td>' + (row.cpName || '-') + '</td>';
            html += '<td>' + row.csId + '</td>';
            html += '<td>' + row.evseId + '</td>';
            html += '<td>' + _eventBadge(row.eventType) + '</td>';
            html += '<td>' + (row.eventRefId || '-') + '</td>';
            html += '<td>' + _stateBadge(row.prevState) + '</td>';
            html += '<td>' + _stateBadge(row.newState) + '</td>';
            html += '<td>' + (row.batterySerialNo || '-') + '</td>';
            html += '<td style="text-align:right;">' + (row.currentSoC != null ? row.currentSoC : '-') + '</td>';
            html += '<td style="text-align:right;' + sohStyle + '">' + (row.currentSoH != null ? row.currentSoH : '-') + '</td>';
            html += '<td>' + (row.writer && row.writer.regUserId ? row.writer.regUserId : '-') + '</td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _stateBadge(code) {
        if (!code) return '-';
        var meta = STATE_BADGE[code];
        if (!meta) return code;
        return '<span class="label ' + meta.cls + '">' + meta.txt + '</span>';
    }

    function _eventBadge(t) {
        if (!t) return '-';
        var cls = EVENT_BADGE[t] || 'label-default';
        return '<span class="label ' + cls + '">' + t + '</span>';
    }

    function _formatDate(dt) {
        var y = dt.getFullYear();
        var m = ('0' + (dt.getMonth() + 1)).slice(-2);
        var d = ('0' + dt.getDate()).slice(-2);
        return y + '-' + m + '-' + d;
    }

    function _formatDateTime(val) {
        if (!val) return '-';
        var dt = new Date(val);
        var hh = ('0' + dt.getHours()).slice(-2);
        var mm = ('0' + dt.getMinutes()).slice(-2);
        var ss = ('0' + dt.getSeconds()).slice(-2);
        return _formatDate(dt) + ' ' + hh + ':' + mm + ':' + ss;
    }

    function _addDay(dt, n) {
        var d = new Date(dt.getTime());
        d.setDate(d.getDate() + n);
        return d;
    }

    return {
        init: _init,
        search: _search
    };
}();
