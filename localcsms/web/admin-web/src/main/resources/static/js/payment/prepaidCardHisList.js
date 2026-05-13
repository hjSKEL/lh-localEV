/**
 * 선불카드 거래이력
 */
var prepaidCardHisListJs = function () {
    "use strict";

    var data = {
        searchCond: {}
    };

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
        $("#typeCode").change(_searchOnClick);
        $("#sWord").keypress(function (event) {
            if (event.keyCode == 13) _searchOnClick();
        });
    }

    function _applyQueryString() {
        // 카드 목록에서 cardNo 파라미터로 진입한 경우 자동 입력
        var qs = location.search;
        if (!qs) return;
        var params = {};
        qs.substring(1).split('&').forEach(function (kv) {
            var i = kv.indexOf('=');
            if (i > 0) params[decodeURIComponent(kv.substring(0, i))] = decodeURIComponent(kv.substring(i + 1));
        });
        if (params.cardNo) {
            $("#searchType").val("CARDNO");
            $("#sWord").val(params.cardNo);
        } else if (params.rechargingId) {
            $("#searchType").val("RECHARGING_ID");
            $("#sWord").val(params.rechargingId);
        } else if (params.customerId) {
            $("#searchType").val("CUSTOMER_ID");
            $("#sWord").val(params.customerId);
        }
    }

    function _searchResetClick() {
        $("#date1").val(_formatDate(_addDay(new Date(), -30)));
        $("#date2").val(_formatDate(new Date()));
        $("#typeCode").val("");
        $("#searchType").val("CARDNO");
        $("#sWord").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, prepaidCardHisListJs.search);
        data.searchCond.cardNo = "";
        data.searchCond.customerId = "";
        data.searchCond.rechargingId = "";

        var searchKey = $("#sWord").val().trim();
        switch ($("#searchType").val()) {
            case "CARDNO":        data.searchCond.cardNo = searchKey; break;
            case "CUSTOMER_ID":   data.searchCond.customerId = searchKey; break;
            case "RECHARGING_ID": data.searchCond.rechargingId = searchKey; break;
        }
        data.searchCond.typeCode = $("#typeCode").val();
        data.searchCond.fromDate = $("#date1").val() + " 00:00:00";
        data.searchCond.toDate = $("#date2").val() + " 23:59:59";
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="10">' + _commonMsg.searching + '</td></tr>');

        var paging = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&cardNo=" + encodeURIComponent(data.searchCond.cardNo);
        param += "&customerId=" + encodeURIComponent(data.searchCond.customerId);
        param += "&rechargingId=" + encodeURIComponent(data.searchCond.rechargingId);
        param += "&typeCode=" + encodeURIComponent(data.searchCond.typeCode);
        param += "&fromDate=" + encodeURIComponent(data.searchCond.fromDate);
        param += "&toDate=" + encodeURIComponent(data.searchCond.toDate);

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/payment/prepaidCard/his" + param,
            dataType: 'json',
            success: _display,
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
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

        var result = jsonData.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        var html = '';
        for (var i = 0, length = result.length; i < length; ++i) {
            var row = result[i];
            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + _formatDateTime(row.writer && row.writer.registrationDate) + '</td>';
            html += '<td>' + row.cardNo + '</td>';
            html += '<td>' + (row.customerName ? row.customerName : '-') + '</td>';
            html += '<td>' + _typeBadge(row.typeCode) + '</td>';
            html += '<td style="text-align:right;' + (row.amount < 0 ? 'color:#dd6b55;' : 'color:#1ab394;') + '">' + _commaFormat(row.amount) + '</td>';
            html += '<td style="text-align:right;">' + _commaFormat(row.balanceBefore) + '</td>';
            html += '<td style="text-align:right;">' + _commaFormat(row.balanceAfter) + '</td>';
            html += '<td>' + (row.rechargingId ? row.rechargingId : '-') + '</td>';
            html += '<td>' + (row.writer && row.writer.regUserId ? row.writer.regUserId : '-') + '</td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _typeBadge(typeCode) {
        if (typeCode === 'ISSUE') return '<span class="label label-primary">' + _msg.typeIssue + '</span>';
        if (typeCode === 'USE')   return '<span class="label label-warning">' + _msg.typeUse + '</span>';
        return typeCode || '-';
    }

    function _commaFormat(n) {
        if (n === undefined || n === null) return '0';
        return n.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
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
