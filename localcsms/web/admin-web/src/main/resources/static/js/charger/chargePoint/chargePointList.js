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
            data.searchCond.cpName = queryString.cpName || "";
            data.searchCond.cpId = queryString.cpId || "";
            data.searchCond.delYn = queryString.delYn || "";
            _search();

            if (queryString.cpId) {
                $("#searchType").val("cpId");
                $("#searchKey").val(decodeURI(queryString.cpId));
            } else {
                $("#searchType").val("cpName");
                $("#searchKey").val(decodeURI(queryString.cpName));
            }
            $("#delYn").val(queryString.delYn || "");
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
        $("#searchKey").keypress(function () {
            if (event.keyCode == 13) {
                _searchOnClick();
            }
        });

        // 사용여부
        $("#delYn").change(function () {
            _searchOnClick();
        });
    }

    function _searchResetClick() {
        $("#searchType").val("cpName");
        $("#searchKey").val("");
        $("#delYn").val("");
        _searchOnClick();
    }

    function _registerOnClick() {
        //
        self.location = _ctx + "/charger/chargePoint";
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, chargePointListJs.search);
        data.searchCond.cpName = "";
        data.searchCond.cpId = "";
        data.searchCond.delYn = "";

        let searchType = $("#searchType").val();
        let keyword = $("#searchKey").val().trim();

        // 충전소명 / 충전소ID
        if (keyword !== "") {
            data.searchCond[searchType] = encodeURI(keyword);
        }
        // 사용여부
        data.searchCond.delYn = $("#delYn").val();

        $("#searchKey").val(keyword);

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
        param += "&cpId=" + data.searchCond.cpId;
        param += "&delYn=" + data.searchCond.delYn;

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
        // searchCpId: 목록의 충전소ID 검색조건. 상세페이지의 cpId(조회 대상)와 이름이 겹쳐 별도 파라미터로 넘긴다.
        let param = "?cpId=" + cpId;
        let paging = pageInfoJs.getPaging();
        param += "&pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&cpName=" + (data.searchCond.cpName || "");
        param += "&searchCpId=" + (data.searchCond.cpId || "");
        param += "&delYn=" + (data.searchCond.delYn || "");
        self.location = _ctx + "/charger/chargePoint/detail" + param;
    }

    function _downloadExcel() {
        //
        toastr.info(_msg.pleaseWait, _msg.excelDownload);
        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&cpName=" + data.searchCond.cpName;
        param += "&cpId=" + data.searchCond.cpId;
        param += "&delYn=" + data.searchCond.delYn;
        parent.layerJs.fn_download(_ctx + "/ws/charger/chargePoint/download/list" + param);

    }

    return {
        init: _init,
        search: _search,
        searchDetail: _searchDetail
    };
}();
