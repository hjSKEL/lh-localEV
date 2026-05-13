/**
 * 배터리 슬롯 현황 (실시간) - Read-only
 */
var swapSlotStatusListJs = function () {
    "use strict";

    var data = {
        searchCond: {},
        result: [],
        slotStates: [],
        refreshTimer: null
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

    function _init() {
        _initSlotStateOptions();
        _initRefreshOptions();
        _loadPoints();
        _initEvent();
        _searchOnClick();
    }

    function _initSlotStateOptions() {
        var codes = parent.commonCodeJs.getCodesByParentCode("BSSS00") || [];
        var html = '<option value="">' + _msg.slotStateAll + '</option>';
        if (codes.length === 0) {
            ['EMPTY','OCCUPIED_IDLE','CHARGING','READY','RESERVED','FAULT','MAINTENANCE'].forEach(function (c) {
                data.slotStates.push(c);
                html += '<option value="' + c + '">' + c + '</option>';
            });
        } else {
            for (var i = 0; i < codes.length; ++i) {
                data.slotStates.push(codes[i].code);
                html += '<option value="' + codes[i].code + '">' + codes[i].codeName + '</option>';
            }
        }
        $("#slotState").html(html);
    }

    function _initRefreshOptions() {
        var html = '<option value="0">' + _msg.refreshOff + '</option>';
        [5, 10, 30].forEach(function (s) {
            html += '<option value="' + s + '">' + _msg.refreshLabel.replace('{0}', s) + '</option>';
        });
        $("#refresh").html(html);
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
                if (json && json.result) {
                    for (var i = 0; i < json.result.length; ++i) {
                        var s = json.result[i];
                        html += '<option value="' + s.csId + '">' + s.csId + '</option>';
                    }
                }
                $("#csSelect").html(html);
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
        $("#csSelect").change(_searchOnClick);
        $("#slotState").change(_searchOnClick);
        $("#sWord").keypress(function (e) { if (e.keyCode == 13) _searchOnClick(); });
        $("#refresh").change(_applyRefresh);

        $("#btnViewAllHistory").click(_viewAllHistory);

        $(window).on('beforeunload', _clearTimer);
    }

    function _applyRefresh() {
        _clearTimer();
        var sec = parseInt($("#refresh").val(), 10) || 0;
        if (sec > 0) {
            data.refreshTimer = setInterval(function () {
                _search();
            }, sec * 1000);
        }
    }

    function _clearTimer() {
        if (data.refreshTimer) {
            clearInterval(data.refreshTimer);
            data.refreshTimer = null;
        }
    }

    function _searchReset() {
        $("#cpSelect").val("");
        _loadStations('');
        $("#csSelect").val("");
        $("#slotState").val("");
        $("#searchType").val("BAT_SN");
        $("#sWord").val("");
        $("#refresh").val("0");
        _clearTimer();
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, swapSlotStatusListJs.search);
        data.searchCond.cpId = $("#cpSelect").val();
        data.searchCond.csId = $("#csSelect").val();
        data.searchCond.slotState = $("#slotState").val();
        data.searchCond.batterySerialNo = "";
        data.searchCond.evseId = "";

        var key = $("#sWord").val().trim();
        switch ($("#searchType").val()) {
            case "BAT_SN":  data.searchCond.batterySerialNo = key; break;
            case "EVSE_ID": data.searchCond.evseId = key; break;
        }
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="13">' + _commonMsg.searching + '</td></tr>');

        var paging = pageInfoJs.getPaging();
        var p = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        p += "&cpId=" + encodeURIComponent(data.searchCond.cpId || '');
        p += "&csId=" + encodeURIComponent(data.searchCond.csId || '');
        if (data.searchCond.evseId) p += "&evseId=" + encodeURIComponent(data.searchCond.evseId);
        p += "&slotState=" + encodeURIComponent(data.searchCond.slotState || '');
        p += "&batterySerialNo=" + encodeURIComponent(data.searchCond.batterySerialNo || '');

        var pCount = "?cpId=" + encodeURIComponent(data.searchCond.cpId || '');
        pCount += "&csId=" + encodeURIComponent(data.searchCond.csId || '');
        if (data.searchCond.evseId) pCount += "&evseId=" + encodeURIComponent(data.searchCond.evseId);
        pCount += "&batterySerialNo=" + encodeURIComponent(data.searchCond.batterySerialNo || '');

        $.when(
            $.ajax({ type: 'GET', url: _ctx + "/ws/batterySwap/slot" + p, dataType: 'json' }),
            $.ajax({ type: 'GET', url: _ctx + "/ws/batterySwap/slot/count-by-state" + pCount, dataType: 'json' })
        ).done(function (listResp, countResp) {
            _display(listResp[0]);
            _renderChips(countResp[0] || {});
        }).fail(function (xhr) {
            parent.layerJs.fn_exception(xhr);
        });
    }

    function _renderChips(counts) {
        var total = 0;
        var html = '';
        for (var i = 0; i < data.slotStates.length; ++i) {
            var code = data.slotStates[i];
            var n = counts[code] || 0;
            total += n;
            var meta = STATE_BADGE[code] || { cls: 'label-default', txt: code };
            html += '<span class="label ' + meta.cls + '" style="margin-right:5px; cursor:pointer;"'
                  + ' onclick="swapSlotStatusListJs.filterByState(\'' + code + '\')">'
                  + meta.txt + ' ' + n + '</span>';
        }
        $("#totalCount").html(_msg.totalSlots.replace('{0}', total));
        $("#stateChips").html(html);
    }

    function _display(json) {
        pageInfoJs.setTotalCount(json.criteria.totalItemCount);
        $("#tBodyList").empty();

        if (json.criteria.totalItemCount == 0) {
            $("#tBodyList").append('<tr style="text-align:center;"><td colspan="13">' + _commonMsg.noData + '</td></tr>');
            return;
        }

        data.result = json.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        var html = '';
        for (var i = 0, len = data.result.length; i < len; ++i) {
            var row = data.result[i];
            var sohStyle = (row.currentSoH != null && Number(row.currentSoH) < 80) ? 'color:#dd6b55;' : '';
            html += '<tr style="cursor:pointer;" onclick="swapSlotStatusListJs.openDetail(\'' + row.cpId + '\',\'' + row.csId + '\',' + row.evseId + ')">';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + row.cpId + '</td>';
            html += '<td>' + (row.cpName || '-') + '</td>';
            html += '<td>' + row.csId + '</td>';
            html += '<td>' + row.evseId + '</td>';
            html += '<td>' + _stateBadge(row.slotState) + '</td>';
            html += '<td>' + (row.ocppConnectorStatus || '-') + '</td>';
            html += '<td>' + (row.batterySerialNo || '-') + '</td>';
            html += '<td style="text-align:right;">' + (row.currentSoC != null ? row.currentSoC : '-') + '</td>';
            html += '<td style="text-align:right;' + sohStyle + '">' + (row.currentSoH != null ? row.currentSoH : '-') + '</td>';
            html += '<td>' + _formatDateTime(row.chargingStartDate) + '</td>';
            html += '<td>' + _formatDateTime(row.estimatedReadyDate) + '</td>';
            html += '<td>' + (row.lastEventType || '-') + '</td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _stateBadge(code) {
        var meta = STATE_BADGE[code];
        if (!meta) return code || '-';
        return '<span class="label ' + meta.cls + '">' + meta.txt + '</span>';
    }

    function _filterByState(code) {
        $("#slotState").val(code);
        _searchOnClick();
    }

    // ----- Detail modal -----
    var modalCtx = { cpId: '', csId: '', evseId: 0 };

    function _openDetail(cpId, csId, evseId) {
        modalCtx = { cpId: cpId, csId: csId, evseId: evseId };
        $("#popupSlotKey").text(cpId + ' / ' + csId + ' / #' + evseId);
        _resetModalFields();
        $("#Popup_SlotStatus").modal();

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/batterySwap/slot/" + encodeURIComponent(cpId) + "/" + encodeURIComponent(csId) + "/" + encodeURIComponent(evseId),
            dataType: 'json',
            success: _fillModal,
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/batterySwap/slot/his"
                  + "?cpId=" + encodeURIComponent(cpId)
                  + "&csId=" + encodeURIComponent(csId)
                  + "&evseId=" + encodeURIComponent(evseId)
                  + "&pageNumber=0&pageItemSize=5",
            dataType: 'json',
            success: _fillRecent,
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _resetModalFields() {
        ['pp_cpId','pp_csId','pp_evseId','pp_slotState','pp_ocpp','pp_lastEvent',
         'pp_batterySn','pp_vendor','pp_soc','pp_soh','pp_prodDate',
         'pp_chStart','pp_estReady','pp_reqId','pp_reserveUntil',
         'pp_faultCode','pp_faultSince'].forEach(function (id) {
            $("#" + id).text('-');
        });
        $("#pp_recentBody").empty()
            .append('<tr style="text-align:center;"><td colspan="7">' + _commonMsg.searching + '</td></tr>');
    }

    function _fillModal(slot) {
        if (!slot) {
            toastr.error(_commonMsg.dataNotFound, _msg.slotMgmt);
            return;
        }
        $("#pp_cpId").text(slot.cpId);
        $("#pp_csId").text(slot.csId);
        $("#pp_evseId").text(slot.evseId);
        $("#pp_slotState").html(_stateBadge(slot.slotState));
        $("#pp_ocpp").text(slot.ocppConnectorStatus || '-');
        $("#pp_lastEvent").text((slot.lastEventType || '-') + (slot.lastEventDate ? ' / ' + _formatDateTime(slot.lastEventDate) : ''));
        $("#pp_batterySn").text(slot.batterySerialNo || '-');
        $("#pp_vendor").text(slot.vendorInfo || '-');
        $("#pp_soc").text(slot.currentSoC != null ? slot.currentSoC + ' %' : '-');
        $("#pp_soh").text(slot.currentSoH != null ? slot.currentSoH + ' %' : '-');
        $("#pp_prodDate").text(_formatDate(slot.productionDate));
        $("#pp_chStart").text(_formatDateTime(slot.chargingStartDate));
        $("#pp_estReady").text(_formatDateTime(slot.estimatedReadyDate));
        $("#pp_reqId").text(slot.reservedRequestId != null ? slot.reservedRequestId : '-');
        $("#pp_reserveUntil").text(_formatDateTime(slot.reservedUntil));
        $("#pp_faultCode").text(slot.faultCode || '-');
        $("#pp_faultSince").text(_formatDateTime(slot.faultedSince));
    }

    function _fillRecent(json) {
        var $tb = $("#pp_recentBody").empty();
        if (!json || !json.result || json.result.length === 0) {
            $tb.append('<tr style="text-align:center;"><td colspan="7">' + _commonMsg.noData + '</td></tr>');
            return;
        }
        var html = '';
        for (var i = 0; i < json.result.length; ++i) {
            var h = json.result[i];
            html += '<tr>';
            html += '<td>' + _formatDateTime(h.eventDate) + '</td>';
            html += '<td>' + (h.eventType || '-') + '</td>';
            html += '<td>' + _stateBadge(h.prevState) + '</td>';
            html += '<td>' + _stateBadge(h.newState) + '</td>';
            html += '<td>' + (h.batterySerialNo || '-') + '</td>';
            html += '<td style="text-align:right;">' + (h.currentSoC != null ? h.currentSoC : '-') + '</td>';
            html += '<td style="text-align:right;">' + (h.currentSoH != null ? h.currentSoH : '-') + '</td>';
            html += '</tr>';
        }
        $tb.append(html);
    }

    function _viewAllHistory() {
        if (!modalCtx.cpId) return;
        self.location = _ctx + "/batterySwap/slot/his/list"
            + "?cpId=" + encodeURIComponent(modalCtx.cpId)
            + "&csId=" + encodeURIComponent(modalCtx.csId)
            + "&evseId=" + encodeURIComponent(modalCtx.evseId);
    }

    function _formatDate(val) {
        if (!val) return '-';
        var dt = new Date(val);
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

    return {
        init: _init,
        search: _search,
        openDetail: _openDetail,
        filterByState: _filterByState
    };
}();
