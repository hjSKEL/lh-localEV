/**
 * 충전기 상태 로그
 */
let statusLogListJs = function () {
    "use strict";
    let data = {
        searchCond: {}
    };

    let idxArr = [0];

    function _init() {
        _initEvent();
    }

    function _initEvent() {
        //
        $("#btnSearch").click(function () {
            //
            _searchChargerStatusLogClick();
        });
        $("#btnReset").click(function () {
            _searchResetClick();
        });

        $('#date1').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm-dd"
        });
        //검색조건 Enter키로 검색기능
        $("#sWord").keypress(function (event) {
            if (event.keyCode === 13) {
                _searchChargerStatusLogClick();
            }
        });
        
        $("#date1").val(formmatUtilsJs.dateFormmat(dateUtilsJs.currentDate(), 'YYYY-MM-DD'));

        $("#pagingNum, #csCableChn, #searchType").change(function () {
            //
            _searchChargerStatusLogClick();
        });
        /*$("#date1").change(function () {
            //
        	let date1 = $("#date1").val();
        	if(data.searchCond.fromDate != date1){        		
        		_searchChargerStatusLogClick();
        	}
        });*/
        
        if (queryString.cpCsId) {
            _searchChargerStatusLogClick();
        }
    }

    function _searchResetClick() {
    	//
    	$("#date1").val(formmatUtilsJs.dateFormmat(dateUtilsJs.currentDate(), 'YYYY-MM-DD'));
        $("#sWord").val("");
        $("#csCableChn").val("0");
    }

    function _searchChargerStatusLogClick() {
        idxArr = [0];
        pageInfo2Js.init('pageInfo2Js', 'pagingUl', $("#pagingNum").val(), statusLogListJs.search);

        data.searchCond.cpId = "";
        data.searchCond.csId = "";
        data.searchCond.fromDate = "";
        data.searchCond.csCableChn = "";
        data.searchCond.idx = "";

        if ($("#date1").val() != null && $("#date1").val() != '') {
        	data.searchCond.fromDate = $("#date1").val();
        	data.searchCond.toDate = $("#date1").val();
        }

        let csCableChn = $("#csCableChn").val();
        data.searchCond.csCableChn = csCableChn;

        if (queryString.cpId && queryString.csId) {
            $("#sWord").val(queryString.cpId + '-' + queryString.csId);
        }
        let searchType = $("#searchType").val();
        let sWord = $("#sWord").val().replace(/-/g, '').trim();
        if (!sWord || sWord === '') {
            toastr.warning(_commonMsg.searchInputReq, _msg.title);
            return;
        }
        data.searchCond.csId = sWord;
        if (data.searchCond.csId && data.searchCond.csId.length > 0) {
            if (data.searchCond.csId.length !== 8) {
				toastr.warning(_msg.csIdLength8, _msg.csIdLabel);
                return;
            }
            data.searchCond.cpId = data.searchCond.csId.substring(0, 6);
            data.searchCond.csId = data.searchCond.csId.substring(6);
        }
        
        $("#sWord").val(sWord);
        
        _search();
        
    }

    function _search() {
        //
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="18">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfo2Js.getPaging();
        let param = "?pageNumber=0&pageItemSize=" + paging.pageItemSize;

        param += "&cpId=" + data.searchCond.cpId;
        param += "&csId=" + data.searchCond.csId;
        param += "&evseId=" + data.searchCond.csCableChn;
        if(paging.pageNumber - 1 == 0 && data.searchCond.fromDate != '') {
        	param += "&fromDate=" + formmatUtilsJs.removeDash(data.searchCond.fromDate);
        	param += "&toDate=" + formmatUtilsJs.removeDash(data.searchCond.toDate);
        } else {
            param += "&csStatusId=" + idxArr[paging.pageNumber - 1];
        }

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/logList" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayChargerStatus(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayChargerStatus(jsonData) {
        pageInfo2Js.setCount(jsonData.result.length);
        $("#tBodyList").empty();
        if (jsonData.result.length === 0) {
            let html = '<tr style="text-align:center;">';
            html += '<td colspan="18">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
            return;
        }
        let result = jsonData.result;
        let noIndex = (pageInfo2Js.getPaging().pageNumber - 1) * pageInfo2Js.getPaging().pageItemSize + 1;

        for (let i = 0, length = result.length; i < length; ++i) {
            let paging = pageInfo2Js.getPaging();
            let idx = paging.pageNumber;
            if(!idxArr[idx]) {
                idxArr[idx] = result[length - 1].csStatusId;
            }
            let html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            let cdt = new Date(result[i].updateDate);
            html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            cdt = new Date(result[i].infoCollDate);
            html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            html += '<td>' + (result[i].rechargingId ? result[i].rechargingId : "") + '</td>';
            html += '<td>' + result[i].cpId + "-" + result[i].csId + '</td>';
            html += '<td>' + result[i].evseId + '</td>';
            //html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].csCatCode) + '</td>';
            html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].csStatCode) + '</td>';
            html += '<td> ' + parent.commonCodeJs.getChargerCableStatusDesc(result[i].csCableStatus) + ' </td>';
            html += '<td> ' + parent.commonCodeJs.getCodeNameBySubCode(result[i].csErrorStatus) + ' </td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].cuEleEnerge) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].caEleEnerge) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].instChAmont) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].instChSum) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].chSum) + '</td>';
            if (result[i].chStartDate) {
                cdt = new Date(result[i].chStartDate);
                html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            } else {
                html += '<td></td>';
            }
            if (result[i].chEndDate) {
                cdt = new Date(result[i].chEndDate);
                html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            } else {
                html += '<td></td>';
            }
            html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].eventCode) + '</td>';
            html += '<td>' + (result[i].cutCardNo ? result[i].cutCardNo : "") + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }
    }

    return {
        init: _init,
        search: _search,
    };
}();

