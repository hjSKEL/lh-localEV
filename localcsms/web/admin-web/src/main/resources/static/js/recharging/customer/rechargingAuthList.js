/**
 * 충전관리 - 인증이력
 */
let rechargingAuthListJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    function _init() {
        $("#date1").val(dateUtilsJs.formatDate(dateUtilsJs.addDay(new Date(), -7), "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        _initEvent();
        _searchOnClick();
    }

    function _initEvent() {
        //
        $("#btnSearch").click(function () {
            _searchOnClick();
        });
        $("#btnReset").click(function () {
            _searchResetClick();
        });
        $("#saveExcelcs").click(function () {
            _downloadExcel();
        });
        //검색조건 Enter키로 검색기능
        $("#searchKey").keypress(function (event) {
            if (event.keyCode == 13) {
                _searchOnClick();
            }
        });
        $('#date1').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm-dd"
        });
        $('#date2').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm-dd"
        });
    }

    function _searchResetClick() {
        $("#date1").val(dateUtilsJs.formatDate(dateUtilsJs.addDay(new Date(), -7), "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        $("#sType").val("");
        $("#searchKey").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        //
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, rechargingAuthListJs.search);

        data.searchCond.cpId = "";
        data.searchCond.csId = "";
        data.searchCond.cpName = "";
        data.searchCond.custName = "";
        data.searchCond.cutCardNo = "";

        let searchKey = $("#searchKey").val().trim();
        if (searchKey) {
            switch ($("#sType").val()) {
                case 'CHARGE_POINT':
                    data.searchCond.cpName = searchKey;
                    break;
                case 'CHARGING_STATION':
                    data.searchCond.csId = searchKey.replace(/-/g, '');
                    break;
                case 'CUST_NM':
                    data.searchCond.custName = searchKey;
                    break;
                case 'CUT_CRD_NO':
                    data.searchCond.cutCardNo = searchKey.replace(/-/g, '');
                    break;
            }
        }

        if (data.searchCond.csId && data.searchCond.csId.length > 0) {
            if (data.searchCond.csId.length !== 8) {
                toastr.warning(_msg.chargerIdDigit8, _msg.chargerId);
                return;
            }
            data.searchCond.cpId = data.searchCond.csId.substring(0, 6);
            data.searchCond.csId = data.searchCond.csId.substring(6);
        }

        data.searchCond.fromDate = formmatUtilsJs.removeDash($("#date1").val()) + "000000";
        data.searchCond.toDate = formmatUtilsJs.removeDash($("#date2").val()) + "235959";
        _search();
    }

    function _search() {
        //
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="7">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&cpId=" + data.searchCond.cpId;
        param += "&csId=" + data.searchCond.csId;
        param += "&cpName=" + encodeURIComponent(data.searchCond.cpName);
        param += "&custName=" + encodeURIComponent(data.searchCond.custName);
        param += "&cutCardNo=" + data.searchCond.cutCardNo;
        param += "&fromDate=" + data.searchCond.fromDate;
        param += "&toDate=" + data.searchCond.toDate;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/authorize/list" + param,
            dataType: 'json',
            success: function (jsonData) {
                _displayList(jsonData);
            },
            error: function (xhRequest) {
                //
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayList(jsonData) {
        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();
        if (jsonData.criteria.totalItemCount == 0) {
            let html = '<tr style="text-align:center;">';
            html += '<td colspan="7">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
            return;
        }
        let result = jsonData.result;
        let noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        for (let i = 0, length = result.length; i < length; ++i) {
            let html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            let cdt = new Date(result[i].infoCollDate);
            html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            if (result[i].eventCode === 'EVT0A1') {
                html += '<td style="color:#1ab394;">인증성공</td>';
            } else {
                html += '<td style="color:#ED5565;">인증실패</td>';
            }
            html += '<td>' + (result[i].cpName || '') + '</td>';
            html += '<td>' + result[i].cpId + '-' + result[i].csId + '</td>';
            html += '<td>' + (result[i].cutCardNo ? formmatUtilsJs.cardFormat(result[i].cutCardNo) : '') + '</td>';
            html += '<td>' + (result[i].custName || '') + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }
    }

    function _downloadExcel() {
        //
        toastr.info(_msg.pleaseWait, _msg.excelDownload);
        let param = "?cpId=" + data.searchCond.cpId;
        param += "&csId=" + data.searchCond.csId;
        param += "&cpName=" + encodeURIComponent(data.searchCond.cpName);
        param += "&custName=" + encodeURIComponent(data.searchCond.custName);
        param += "&cutCardNo=" + data.searchCond.cutCardNo;
        param += "&fromDate=" + data.searchCond.fromDate;
        param += "&toDate=" + data.searchCond.toDate;
        parent.layerJs.fn_download(_ctx + "/ws/charger/authorize/download/list" + param);
    }

    return {
        init: _init,
        search: _search
    };
}();
