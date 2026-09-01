/**
 * 충전이력 조정내역 목록 (TB_RCRC003)
 */
var adjustmentListJs = (function () {
    "use strict";

    var data = { searchCond: {} };

    function _init() {
        //
        $("#btnSearch").click(_searchOnClick);
        $("#btnReset").click(_searchResetClick);
        $("#rechargingId").keypress(function (event) {
            if (event.keyCode === 13) { _searchOnClick(); }
        });
        $('#date1').datepicker({ todayBtn: "linked", autoClose: true, format: "yyyy-mm-dd" });
        $('#date2').datepicker({ todayBtn: "linked", autoClose: true, format: "yyyy-mm-dd" });

        _searchOnClick(); //최초 진입 시 전체 조정내역 표출(기간 미지정)
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, adjustmentListJs.search);
        data.searchCond = {};
        data.searchCond.rechargingId = $("#rechargingId").val().trim();
        if ($("#date1").val() && $("#date2").val()) {
            data.searchCond.fromDate = $("#date1").val() + " 00:00:00";
            data.searchCond.toDate = $("#date2").val() + " 23:59:59";
        }
        _search();
    }

    function _searchResetClick() {
        $("#date1").val("");
        $("#date2").val("");
        $("#rechargingId").val("");
        _searchOnClick();
    }

    function _search() {
        //
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="13">' + _commonMsg.searching + '</td></tr>');

        var paging = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        if (data.searchCond.rechargingId) {
            param += "&rechargingId=" + encodeURIComponent(data.searchCond.rechargingId);
        }
        if (data.searchCond.fromDate) {
            param += "&fromDate=" + encodeURIComponent(data.searchCond.fromDate);
        }
        if (data.searchCond.toDate) {
            param += "&toDate=" + encodeURIComponent(data.searchCond.toDate);
        }

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/recharging/adjustment/list" + param,
            dataType: 'json',
            success: _displayList,
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _displayList(jsonData) {
        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();
        if (jsonData.criteria.totalItemCount === 0) {
            $("#tBodyList").append('<tr style="text-align:center;"><td colspan="13">' + _commonMsg.noData + '</td></tr>');
            return;
        }
        var list = jsonData.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        for (var i = 0; i < list.length; i++) {
            var a = list[i];
            var html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + (a.regDate ? formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(a.regDate)), 'YYYY-MM-DD HH:MM:SS') : '-') + '</td>';
            html += '<td>' + a.rechargingId + '</td>';
            html += '<td>' + (a.complexName || '-') + '</td>';
            html += '<td>' + (a.dong || '-') + '</td>';
            html += '<td>' + (a.ho || '-') + '</td>';
            html += '<td>' + (a.custName || '-') + '</td>';
            html += '<td>' + formmatUtilsJs.cardFormat(a.cutCardNo) + '</td>';
            html += '<td>' + (a.chStartDate ? formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(a.chStartDate)), 'YYYY-MM-DD HH:MM:SS') : '-') + '</td>';
            html += '<td>' + (a.chEndDate ? formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(a.chEndDate)), 'YYYY-MM-DD HH:MM:SS') : '-') + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(a.adjustAmount || 0) + '</td>';
            html += '<td>' + (a.adjustReason || '-') + '</td>';
            html += '<td>' + a.regId + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }
    }

    return {
        init: _init,
        search: _search
    };
})();
