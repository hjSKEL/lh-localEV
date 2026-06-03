/**
 * 배터리 교체 기록 목록
 */
var batterySwapRecordListJs = function () {
    "use strict";

    var data = {
        searchCond: {}
    };

    var STATUS_BADGE = {
        IN:      { cls: 'label-warning', txt: 'IN' },
        OUT:     { cls: 'label-primary', txt: 'OUT' },
        Timeout: { cls: 'label-danger',  txt: 'Timeout' }
    };

    function _init() {
        _initDates();
        _initStatusSelect();
        _loadPoints();
        _initEvent();
        _searchOnClick();
    }

    function _initDates() {
        $("#date1").val(_formatDate(_addDay(new Date(), -7)));
        $("#date2").val(_formatDate(new Date()));
        $('#date1,#date2').datepicker({ todayBtn: "linked", autoclose: true, format: "yyyy-mm-dd" });
    }

    function _initStatusSelect() {
        var html = '<option value="">' + _msg.statusAll + '</option>'
                 + '<option value="IN">IN</option>'
                 + '<option value="OUT">OUT</option>'
                 + '<option value="Timeout">Timeout</option>';
        $("#status").html(html);
    }

    function _loadPoints() {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/chargePoint?pageNumber=0&pageItemSize=1000",
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
            url: _ctx + "/ws/charger/finder?cpId=" + encodeURIComponent(cpId) + "&pageNumber=0&pageItemSize=1000",
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
        $("#csSelect,#status").change(_searchOnClick);
        $("#sWord").keypress(function (e) { if (e.keyCode == 13) _searchOnClick(); });
    }

    function _searchReset() {
        _initDates();
        $("#cpSelect").val("");
        _loadStations('');
        $("#csSelect").val("");
        $("#status").val("");
        $("#searchType").val("REQUEST_ID");
        $("#sWord").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, batterySwapRecordListJs.search);
        data.searchCond.cpId = $("#cpSelect").val();
        data.searchCond.csId = $("#csSelect").val();
        data.searchCond.status = $("#status").val();
        data.searchCond.inCustomerId = "";
        data.searchCond.outCustomerId = "";
        data.searchCond.requestId = "";

        var key = $("#sWord").val().trim();
        switch ($("#searchType").val()) {
            case "REQUEST_ID":
                if (key.length > 0 && !/^\d+$/.test(key)) {
                    toastr.warning(_msg.requestIdOnly, _msg.recordMgmt);
                    return;
                }
                data.searchCond.requestId = key;
                break;
            case "IN_CUSTOMER_ID":  data.searchCond.inCustomerId = key; break;
            case "OUT_CUSTOMER_ID": data.searchCond.outCustomerId = key; break;
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
        p += "&inCustomerId=" + encodeURIComponent(data.searchCond.inCustomerId || '');
        p += "&outCustomerId=" + encodeURIComponent(data.searchCond.outCustomerId || '');
        p += "&status=" + encodeURIComponent(data.searchCond.status || '');
        p += "&fromDate=" + encodeURIComponent(data.searchCond.fromDate || '');
        p += "&toDate=" + encodeURIComponent(data.searchCond.toDate || '');

        // requestId 는 SearchCond 에 없으므로 클라이언트에서 단건 페치로 처리 (조회타입 REQUEST_ID 단독 입력 시).
        if (data.searchCond.requestId && data.searchCond.requestId.length > 0) {
            _searchByRequestId(data.searchCond.requestId);
            return;
        }

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/recharging/batterySwapRecord" + p,
            dataType: 'json',
            success: _display,
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _searchByRequestId(reqId) {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/recharging/batterySwapRecord/" + encodeURIComponent(reqId),
            dataType: 'json',
            success: function (record) {
                var fake = { criteria: { totalItemCount: record ? 1 : 0 }, result: record ? [record] : [] };
                pageInfoJs.setTotalCount(fake.criteria.totalItemCount);
                _display(fake);
            },
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
            var cntMismatch = (row.status === 'OUT' && row.inCount !== row.outCount);
            var crossCustomer = (row.inCustomerId && row.outCustomerId && row.inCustomerId !== row.outCustomerId);
            var duration = _durationText(row.inDateTime, row.outDateTime);
            var outDtCell = row.outDateTime ? _formatDateTime(row.outDateTime) : (row.status === 'Timeout' ? '<span style="color:#dd6b55;">-</span>' : '-');
            html += '<tr style="cursor:pointer;" onclick="batterySwapRecordListJs.openDetail(' + row.requestId + ')">';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + row.requestId + '</td>';
            html += '<td>' + row.cpId + '</td>';
            html += '<td>' + (row.cpName || '-') + '</td>';
            html += '<td>' + row.csId + '</td>';
            html += '<td>' + _statusBadge(row.status) + '</td>';
            html += '<td>' + _formatDateTime(row.inDateTime) + '</td>';
            html += '<td' + (crossCustomer ? ' style="background-color:#fff8e1;"' : '') + '>' + _customerCell(row.inCustomerName, row.inCustomerId) + '</td>';
            html += '<td style="text-align:right;' + (cntMismatch ? 'color:#dd6b55;' : '') + '">' + (row.inCount || 0) + (cntMismatch ? ' ⚠' : '') + '</td>';
            html += '<td>' + outDtCell + '</td>';
            html += '<td' + (crossCustomer ? ' style="background-color:#fff8e1;"' : '') + '>' + _customerCell(row.outCustomerName, row.outCustomerId) + '</td>';
            html += '<td style="text-align:right;' + (cntMismatch ? 'color:#dd6b55;' : '') + '">' + (row.outCount || 0) + (cntMismatch ? ' ⚠' : '') + '</td>';
            html += '<td>' + duration + '</td>';
            html += '<td><button type="button" class="btn btn-xs btn-info" onclick="event.stopPropagation();batterySwapRecordListJs.openDetail(' + row.requestId + ')"><i class="fa fa-search"></i></button></td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _statusBadge(code) {
        var meta = STATUS_BADGE[code];
        if (!meta) return code || '-';
        return '<span class="label ' + meta.cls + '">' + meta.txt + '</span>';
    }

    function _customerCell(name, id) {
        if (!name && !id) return '-';
        if (name && id) return name + ' (' + id + ')';
        return name || id;
    }

    function _durationText(from, to) {
        if (!from || !to) return '-';
        var ms = new Date(to).getTime() - new Date(from).getTime();
        if (ms < 0) return '-';
        var sec = Math.floor(ms / 1000);
        var min = Math.floor(sec / 60);
        var hr  = Math.floor(min / 60);
        if (hr > 0)  return hr + '시간 ' + (min % 60) + '분';
        if (min > 0) return min + '분 ' + (sec % 60) + '초';
        return sec + '초';
    }

    function _openDetail(requestId) {
        self.location = _ctx + "/recharging/batterySwapRecord/detail?requestId=" + encodeURIComponent(requestId);
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
        search: _search,
        openDetail: _openDetail
    };
}();
