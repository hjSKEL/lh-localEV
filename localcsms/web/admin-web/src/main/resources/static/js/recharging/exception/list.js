/**
 * 충전기 충전정보
 */
let listJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    function _init() {
        $("#date1").val(formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(dateUtilsJs.addDay(new Date(), -7)), "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        _searchRechargingOnClick();
        _initEvent();
    }

    function _initEvent() {
        //
        $("#btnSearch").click(function () {
            //
			_searchRechargingOnClick();
        });
        $("#btnReset").click(function () {
            _searchResetClick();
        });
        //검색조건 Enter키로 검색기능
        $("#searchKey").keypress(function (event) {
            if (event.keyCode == 13) {
				_searchRechargingOnClick();
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

        $("#saveExcelcs").click(function () {
            _downloadExcel();
        });
    }

    function _searchResetClick() {
        $("#date1").val(formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(dateUtilsJs.addDay(new Date(), -7)), "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        $("#searchKey").val("");
        $("#dateOrder").val("E");
        _searchRechargingOnClick();
    }

    function _searchRechargingOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, listJs.search);

        data.searchCond.csUniqId = "";
        data.searchCond.cpName = "";
		data.searchCond.rechargingId = ""
        data.searchCond.etc = "";
        data.searchCond.dateOrder = $("#dateOrder").val();

        let searchKey = $("#searchKey").val();
        if (searchKey && searchKey.length > 0) {
            switch ($("#sType").val()) {
                case 'CS_UNIQ_ID':
                    data.searchCond.csUniqId = searchKey;
                    break;
                case 'CHARGE_POINT':
                    data.searchCond.cpName = encodeURI(searchKey);
                    break;
				case 'RECHARGING_ID':
					data.searchCond.rechargingId = searchKey;
					break;
                case 'ETC':
                    data.searchCond.etc = encodeURI(searchKey);
                    break;
            }

        }

        data.searchCond.fromDate = formmatUtilsJs.removeDash($("#date1").val()) + "000000";
        data.searchCond.toDate = formmatUtilsJs.removeDash($("#date2").val()) + "235959";
        _search();
    }

    function _search() {
        //
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="13">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&csUniqId=" + data.searchCond.csUniqId;
        param += "&cpName=" + data.searchCond.cpName;
		param += "&rechargingId=" + data.searchCond.rechargingId;
        param += "&etc=" + data.searchCond.etc;
        param += "&fromDate=" + data.searchCond.fromDate;
        param += "&toDate=" + data.searchCond.toDate;
        param += "&dateOrder=" + data.searchCond.dateOrder;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/recharging/exception/list" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayRecharging(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayRecharging(jsonData) {
        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();
        let html = '';
        if (jsonData.criteria.totalItemCount == 0) {
            html = '<tr style="text-align:center;">';
            html += '<td colspan="13">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
            return;
        }
        let result = jsonData.result;
        let noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        for (let i = 0, length = result.length; i < length; ++i) {
            html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + result[i].cpName + '</td>';
            html += '<td>' + result[i].csUniqId + '</td>';
            html += '<td>' + result[i].csCableChn + '</td>'
            html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].makerType) + '</td>';
            html += '<td>' + result[i].rechargingId + '</td>';
            if (result[i].chStartDate) {
                let cdt = new Date(result[i].chStartDate);
                html += '<td> ' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM') + ' </td>';
            } else {
                html += '<td> </td>';
            }
            if (result[i].chEndDate) {
                let cdt = new Date(result[i].chEndDate);
                html += '<td> ' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM') + ' </td>';
            } else {
                html += '<td> </td>';
            }
            html += '<td>' + result[i].chUseAmount.toLocaleString() + '</td>';
            html += '<td>' + result[i].chUseCost.toLocaleString() + '</td>';
            html += '<td>' + result[i].finalPaySum.toLocaleString() + '</td>';
            html += '<td>' + result[i].etc + '</td>'
            html += '<td>' + formmatUtilsJs.cardFormat(result[i].cutCardNo) + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }
    }

    function _downloadExcel() {
        toastr.info(_msg.pleaseWait, _msg.excelDownload);
        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&csUniqId=" + data.searchCond.csUniqId;
        param += "&cpName=" + data.searchCond.cpName;
        param += "&rechargingId=" + data.searchCond.rechargingId;
        param += "&etc=" + data.searchCond.etc;
        param += "&fromDate=" + data.searchCond.fromDate;
        param += "&toDate=" + data.searchCond.toDate;
        param += "&dateOrder=" + data.searchCond.dateOrder;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/recharging/exception/download" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                if(jsonData.status == 'SUCCESS'){
                    parent.layerJs.fn_download("/ws/download/DWRC007?tokenId=" + jsonData.result);
                }else{
                    alert(_commonMsg.excelFail + " : "+jsonData.result);
                }
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
                alert(_commonMsg.commError);
            }
        });
    }

    return {
        init: _init,
        search: _search,
    };
}();
