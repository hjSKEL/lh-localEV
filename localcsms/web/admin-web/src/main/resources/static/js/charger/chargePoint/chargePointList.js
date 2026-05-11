/**
 * 충전관리 - 충전소관리
 */
let chargePointListJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    function _init() {
        _initEvent();
//        zipCodeJs.searchZipCode(undefined, _disZipDoSiCode);
        if (queryString.pageItemSize) {
            pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, chargePointListJs.search);
            pageInfoJs.setPageNumber(Number(queryString.pageNumber) + 1);
            data.searchCond.cpName = queryString.cpName;
            data.searchCond.csType = queryString.csType;
            _search();

            $("#cpName").val(decodeURI(queryString.cpName));
            $("#csType").val(queryString.csType);
        } else {
            _searchOnClick();
        }
    }

    function _initEvent() {
        //
        $("#saveExcelcs").click(function () {
            _downloadExcel();
        });

        $("#btnSearch").click(function () {
            //
            _searchOnClick();
        });
        $("#btnReset").click(function () {
            _searchResetClick();
        });
        $("#btnForm").click(function () {
            //
            _registerOnClick();
        });

        // 검색조건 Enter키로 검색기능
        $("#cpName").keypress(function () {
            if (event.keyCode == 13) {
                _searchOnClick();
            }
        });
        // 충전기 유형
        $("#csType").append('<option value="">' + _msg.csTypeAll + '</option>');
        $("#csType").append('<option value="hiCs">' + _msg.csTypeFast + '</option>');
        $("#csType").append('<option value="loCs">' + _msg.csTypeSlow + '</option>');

    }

    function _searchResetClick() {
        $("#csType").val("");
        $("#cpName").val("");
        _searchOnClick();
    }
    
    function _registerOnClick() {
        //
        self.location = _ctx + "/charger/chargePoint";
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, chargePointListJs.search);
        data.searchCond.cpName = "";
        data.searchCond.csType = "";
        
        let cpName = $("#cpName").val().trim();
        let csType = $("#csType").val();
        
        // 충전소명
        if (cpName && cpName !== "") {
            data.searchCond.cpName = encodeURI(cpName);
        }
        // 충전기유형
        if (csType && csType !== "") {
            data.searchCond.csType = encodeURI(csType);
        }
        
        $("#cpName").val(cpName);
        
        _search();
    }

    function _search() {
        //
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="9">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&cpName=" + data.searchCond.cpName;
        param += "&csType=" + data.searchCond.csType;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/chargePoint/allChargePointList4Monitoring" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displaySpot(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displaySpot(jsonData) {

        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();
        let html = '';
        if (jsonData.criteria.totalItemCount == 0) {
            html = '<tr style="text-align:center;">';
            html += '<td colspan="9">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
            return;
        }
        let result = jsonData.result;
        let noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        for (let i = 0, length = result.length; i < length; ++i) {
            html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + result[i].cpId + '</td>';
            html += '<td><a href="#" onclick="chargePointListJs.searchDetail(' + result[i].cpId + ')">' + result[i].cpName + '</a></td>';
            html += '<td>' + (result[i].cpLocation ? result[i].cpLocation : '-') + '</td>';
            html += '<td>' + result[i].lowCsCount + '</td>';
            html += '<td>' + result[i].highCsCount + '</td>';
            html += '<td>' + result[i].electSupplyCapability + '</td>';
            html += '<td>' + result[i].cpUseYn + '</td>';
            html += '<td>' + dateUtilsJs.formatDate(new Date(result[i].writer.registrationDate), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }

    }

    function _searchDetail(cpId) {
        //
        let param = "?cpId=" + cpId;
        let paging = pageInfoJs.getPaging();
        param += "&pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&cpName=" + data.searchCond.cpName;
        param += "&csType=" + data.searchCond.csType;
        self.location = _ctx + "/charger/chargePoint/detail" + param;
    }

    function _downloadExcel() {
        //
        toastr.info(_msg.pleaseWait, _msg.excelDownload);
        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&cpName=" + data.searchCond.cpName;
        param += "&csType=" + data.searchCond.csType;
        parent.layerJs.fn_download(_ctx + "/ws/charger/chargePoint/download/list" + param);
        
    }

    return {
        init: _init,
        search: _search,
        searchDetail: _searchDetail
    };
}();
