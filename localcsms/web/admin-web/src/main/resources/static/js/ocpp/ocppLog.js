/**
 *
 */
let ocppLogJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    let seqArr = [0];

    function _init() {
        _initEvent();
    }

    function _initEvent() {
        //
        _findRegionList();
        $("#btnSearch").click(function () {
            //
            _searchOcppLogClick();
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
                _searchOcppLogClick();
            }
        });

        $("#date1, #pagingNum, #csCableChn, #searchType").change(function () {
            //
            _searchOcppLogClick();
        });
        if (queryString.cpCsId) {
            _searchOcppLogClick();
        }
    }

    function _searchResetClick() {
    	$("#date1").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        $("#sWord").val("");
        $("#csCableChn").val("");
    }

    function _findRegionList() {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/chargingStationRegion/list",
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                let html = '';
                for (let i = 0, size = jsonData.result.length; i < size; ++i) {
                    html = '<option value="' + jsonData.result[i].regionId + '">';
                    html += jsonData.result[i].regionId;
                    html += '</option>';
                    $("#regionId").append(html);
                }
                $("#region").val(jsonData.result[0].regionId);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _searchOcppLogClick() {
        seqArr = [0];
        pageInfo2Js.init('pageInfo2Js', 'pagingUl', $("#pagingNum").val(), ocppLogJs.search);

        data.searchCond.cpId = "";
        data.searchCond.csId = "";
        data.searchCond.fromDate = "";
        data.searchCond.toDate = "";
        data.searchCond.connectorId = "";
        data.searchCond.seq = "";

        if ($("#date1").val() != null && $("#date1").val() != '') {
        	data.searchCond.fromDate = $("#date1").val();
        	data.searchCond.toDate = $("#date1").val();
        }

        let csCableChn = $("#csCableChn").val();
        data.searchCond.connectorId = csCableChn;

        if (queryString.cpCsId) {
            $("#sWord").val(queryString.cpCsId);
        }
        let searchType = $("#searchType").val();
        let sWord = $("#sWord").val();
        if (!sWord || sWord === '') {
            toastr.warning(_msg.enterSearch, _msg.ocppLog);
            return;
        }
        
        if (!sWord.includes('-')) {
            toastr.warning(_msg.idFormat, _msg.ocppLog);
            return;
        }
        let ids = sWord.split("-");
        data.searchCond.cpId = ids[0];
        data.searchCond.csId = ids[1];

        _search();
    }

    function _search() {
        //
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="19">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfo2Js.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;

        param += "&cpId=" + data.searchCond.cpId;
        param += "&csId=" + data.searchCond.csId;
        param += "&regionId=" + data.searchCond.regionId;
        param += "&connectorId=" + data.searchCond.connectorId;
        if(paging.pageNumber - 1 == 0 && data.searchCond.fromDate != '') {
        	param += "&fromDate=" + formmatUtilsJs.removeDash(data.searchCond.fromDate);
        	param += "&toDate=" + formmatUtilsJs.removeDash(data.searchCond.toDate);
        } else {
            param += "&seq=" + seqArr[paging.pageNumber - 1];
        }

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/ocpp/log/search" + param,
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
            html += '<td colspan="10">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
            return;
        }
        let result = jsonData.result;
        let noIndex = (pageInfo2Js.getPaging().pageNumber - 1) * pageInfo2Js.getPaging().pageItemSize + 1;

        for (let i = 0, length = result.length; i < length; ++i) {
            let paging = pageInfo2Js.getPaging();
            let idx = paging.pageNumber;
            if(!seqArr[idx]) {
                seqArr[idx] = result[length - 1].seq;
            }
            let html = '<tr style="text-align:center;">';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + result[i].id + '</td>';
            html += '<td>' + result[i].cpId + '</td>';
            html += '<td>' + result[i].csId + '</td>';
            html += '<td>' + result[i].connectorId + '</td>';
            html += '<td>' + result[i].directType + '</td>';
            html += '<td>' + result[i].messageTypeId + '</td>';
            html += '<td>' + result[i].ocppVer + '</td>';
            html += '<td>' + result[i].action + '</td>';
            html += '<td>' + result[i].payloadJson + '</td>';
            let regDt = new Date(result[i].regDate);
            html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(regDt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }
    }

    return {
        init: _init,
        search: _search
    };
}();
