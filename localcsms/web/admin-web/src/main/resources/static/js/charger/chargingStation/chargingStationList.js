/**
 * 충전관리 - 충전기관리
 */
let chargingStationListJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    function _init() {
        _initEvent();
        if (queryString.pageItemSize) {
            pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, chargingStationListJs.search);
            pageInfoJs.setPageNumber(Number(queryString.pageNumber) + 1);
            data.searchCond.csKindType = queryString.csKindType;
            data.searchCond.cpId = '';
            data.searchCond.cpName = '';
            data.searchCond.csUniqId = '';

            if (queryString.sType === 'CP_ID') {
                data.searchCond.cpId = queryString.searchKey;
            } else if (queryString.sType === 'CP_NAME') {
                data.searchCond.cpName = queryString.searchKey;
            } else if (queryString.sType === 'CS_ID') {
                data.searchCond.csUniqId = queryString.searchKey;
            }

            //검색 정보 유지
            $("#sType").val(queryString.sType);
            $("#searchKey").val(queryString.searchKey);
            $("#csKindType").val(queryString.csKindType);

            _search();

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
        $("#btnForm").click(function () {
            //
            _registerOnClick();
        });

        $("#btnReset").click(function () {
            _searchResetClick();
        });

        //충전기 유형
        let csKindTypes = parent.commonCodeJs.getCodesByParentCode('CHKT00');
        $("#csKindType").append('<option value="">' + _msg.csKindTypeAll + '</option>');
        let html2 = '';
        for (let i = 0, size = csKindTypes.length; i < size; ++i) {
            html2 = '<option value="' + csKindTypes[i].code + '">';
            html2 += csKindTypes[i].codeName;
            html2 += '</option>';
            $("#csKindType").append(html2);
        }
        $("#csKindType").change(function () {
            _searchOnClick();
        });

        //검색조건 Enter키로 검색기능
        $("#searchKey").keypress(function (event) {
            if (event.keyCode === 13) {
                _searchOnClick();
            }
        });

        //정렬(충전소명/충전기ID/충전기용량/설치년월/등록일) 컬럼 헤더 클릭 - 클릭할 때마다 오름차순/내림차순 토글
        $(".sortBtn").click(function () {
            let $btn = $(this);
            let toAsc = $btn.data("state") !== "asc";
            $btn.data("state", toAsc ? "asc" : "desc").text(toAsc ? "▲" : "▼");
            data.searchCond.sortOrder = toAsc ? $btn.data("asc") : $btn.data("desc");
            _search();
        });
    }

    function _searchResetClick() {
        $("#searchKey").val("");
        $("#csKindType").val("");
        $("#sType").val("CP_ID");
        $(".sortBtn").data("state", "desc").text("▼");
        data.searchCond.sortOrder = "";
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, chargingStationListJs.search);

        data.searchCond.cpName = "";
        data.searchCond.cpId = "";
        data.searchCond.csUniqId = "";
        data.searchCond.csKindType = "";

        data.searchCond.csKindType = $("#csKindType").val();

		let sType = $("#sType").val();
		let searchKey = $("#searchKey").val().trim();
        switch (sType) {
            case 'CP_NAME':
                data.searchCond.cpName = encodeURI(searchKey);
                break;
            case 'CP_ID':
				if(searchKey && searchKey.length != 6){
					toastr.warning(_msg.cpIdLength6, _msg.cpIdLabel);
                	return;
				} else {
					data.searchCond.cpId = searchKey;
				}
                break;
            case 'CS_ID':
                data.searchCond.csUniqId = searchKey;
                break;
        }
        
        $("#searchKey").val(searchKey);
        
        _search();
    }

    function _search() {
        //
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="11">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&cpId=" + data.searchCond.cpId;
        param += "&cpName=" + data.searchCond.cpName;
        param += "&csKindType=" + data.searchCond.csKindType;
        param += "&csUniqId=" + data.searchCond.csUniqId;
        param += "&sortOrder=" + (data.searchCond.sortOrder || "");

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/finder" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayCharger(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayCharger(jsonData) {
        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();
        let html = '';
        if (jsonData.criteria.totalItemCount === 0) {
            html = '<tr style="text-align:center;">';
            html += '<td colspan="11">' + _commonMsg.noData + '</td>';
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
            html += '<td><a href="#" onclick="chargingStationListJs.searchDetail(\'' + result[i].cpId + '\',\'' + result[i].csId + '\')">' + result[i].cpId + '-' + result[i].csId + '</a></td>';
            html += '<td>' + result[i].csUniqId + '</td>';
            html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].csKindType) + '</td>';
            html += '<td>' + result[i].csChanelCount + '</td>';
            html += '<td>' + result[i].electSupplyCapability + '</td>';
            html += '<td>' + result[i].useYn + '</td>';
            html += '<td>' + result[i].brkdownYn + '</td>';
            if(result[i].insYearMon) {
				let year = result[i].insYearMon.substring(0,4);
				let month = result[i].insYearMon.substring(4,7);
				html += '<td>' + _msg.yearMonth.replace('{0}', year).replace('{1}', month) + '</td>';
			} else {
				html += '<td> - </td>';
			}
            html += '<td>' + dateUtilsJs.formatDate(new Date(result[i].writer.registrationDate), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }
    }

    function _searchDetail(cpId, csId) {
        //
        let param = "?cpId=" + cpId + "&csId=" + csId;
        let paging = pageInfoJs.getPaging();
        param += "&pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&csKindType=" + data.searchCond.csKindType;
        param += "&csUniqId=" + data.searchCond.csUniqId;
        param += "&cpName=" + data.searchCond.cpName;
        param += "&searchKey=" + decodeURI($("#searchKey").val());
        param += "&sType=" + $("#sType").val();
        self.location = _ctx + "/charger/chargingStation/detail" + param;
    }

    function _registerOnClick() {
        //
        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&csKindType=" + data.searchCond.csKindType;
        param += "&csUniqId=" + data.searchCond.csUniqId;
        param += "&sType=" + $("#sType").val();
        param += "&searchKey=" + decodeURI($("#searchKey").val());

        self.location = _ctx + "/charger/chargingStation" + param;
    }

    function _downloadExcel() {
        //
        toastr.info(_msg.pleaseWait, _msg.excelDownload);
        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&cpId=" + data.searchCond.cpId;
        param += "&cpName=" + data.searchCond.cpName;
        param += "&csKindType=" + data.searchCond.csKindType;
        param += "&csUniqId=" + data.searchCond.csUniqId;
        param += "&sortOrder=" + (data.searchCond.sortOrder || "");

        parent.layerJs.fn_download(_ctx + "/ws/charger/chargingStation/download/list" + param);

    }

    return {
        init: _init,
        search: _search,
        searchDetail: _searchDetail
    };
}();
