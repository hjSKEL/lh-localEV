/**
 * VAT 검증 이력
 */
var vatHisListJs = function () {
    "use strict";

    var data = { searchCond: {} };

    function _init() {
        _initDatepickers();
        _initEvent();
        _applyQueryString();
        _searchOnClick();
    }

    function _initDatepickers() {
        $("#date1").val(_formatDate(_addDay(new Date(), -30)));
        $("#date2").val(_formatDate(new Date()));
        $('#date1').datepicker({ todayBtn: "linked", autoclose: true, format: "yyyy-mm-dd" });
        $('#date2').datepicker({ todayBtn: "linked", autoclose: true, format: "yyyy-mm-dd" });
    }

    function _initEvent() {
        $("#btnSearch").click(_searchOnClick);
        $("#btnReset").click(_searchResetClick);
        $("#resultStatus").change(_searchOnClick);
        $("#sWord").keypress(function (event) {
            if (event.keyCode == 13) _searchOnClick();
        });
    }

    function _applyQueryString() {
        var qs = location.search;
        if (!qs) return;
        var params = {};
        qs.substring(1).split('&').forEach(function (kv) {
            var i = kv.indexOf('=');
            if (i > 0) params[decodeURIComponent(kv.substring(0, i))] = decodeURIComponent(kv.substring(i + 1));
        });
        if (params.vatNo) {
            $("#searchType").val("VATNO");
            $("#sWord").val(params.vatNo);
        }
    }

    function _searchResetClick() {
        $("#date1").val(_formatDate(_addDay(new Date(), -30)));
        $("#date2").val(_formatDate(new Date()));
        $("#resultStatus").val("");
        $("#searchType").val("VATNO");
        $("#sWord").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, vatHisListJs.search);
        data.searchCond.vatNo = "";

        var searchKey = $("#sWord").val().trim();
        if ($("#searchType").val() === "VATNO") {
            data.searchCond.vatNo = searchKey;
        }
        data.searchCond.resultStatus = $("#resultStatus").val();
        data.searchCond.fromDate = $("#date1").val() + " 00:00:00";
        data.searchCond.toDate = $("#date2").val() + " 23:59:59";
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="9">' + _commonMsg.searching + '</td></tr>');

        var paging = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&vatNo=" + encodeURIComponent(data.searchCond.vatNo);
        param += "&resultStatus=" + encodeURIComponent(data.searchCond.resultStatus);
        param += "&fromDate=" + encodeURIComponent(data.searchCond.fromDate);
        param += "&toDate=" + encodeURIComponent(data.searchCond.toDate);

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/payment/vat/his" + param,
            dataType: 'json',
            success: _display,
            error: function (xhRequest) { parent.layerJs.fn_exception(xhRequest); }
        });
    }

    function _display(jsonData) {
        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();

        if (jsonData.criteria.totalItemCount == 0) {
            $("#tBodyList").append('<tr style="text-align:center;"><td colspan="9">' + _commonMsg.noData + '</td></tr>');
            return;
        }

        var result = jsonData.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        var html = '';
        for (var i = 0, length = result.length; i < length; ++i) {
            var row = result[i];
            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + _formatDateTime(row.verifiedDt) + '</td>';
            html += '<td>' + (row.vatNo || '-') + '</td>';
            html += '<td>' + (row.companyNm || '-') + '</td>';
            html += '<td>' + (row.cpId || '-') + '</td>';
            html += '<td>' + (row.csId || '-') + '</td>';
            html += '<td>' + (row.evseId !== null && row.evseId !== undefined ? row.evseId : '-') + '</td>';
            html += '<td>' + _statusBadge(row.resultStatus) + '</td>';
            html += '<td>' + (row.reasonCd || '-') + '</td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _statusBadge(status) {
        if (status === 'Accepted') return '<span class="label label-primary">' + _msg.statusAccept + '</span>';
        if (status === 'Rejected') return '<span class="label label-danger">' + _msg.statusReject + '</span>';
        return status || '-';
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

    function _addDay(dt, days) {
        var r = new Date(dt.getTime());
        r.setDate(r.getDate() + days);
        return r;
    }

    return {
        init: _init,
        search: _search
    };
}();
