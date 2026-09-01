/**
 * 고객충전현황
 */
let rechargingCustomerJs = function () {
    "use strict";

    let data = {
        searchCond: { dateOrder: 'Z', status: ['RECS03'] } //dateOrder Z: 기본정렬(단지-동-호-충전시작시간), status: 완료(종료)된 충전만 기본 조회
    };

    function _init() {
        //
        let endDate = dateUtilsJs.addDay(dateUtilsJs.currentMonthFirstDay(), -1);
        let startDate = dateUtilsJs.addMonth(dateUtilsJs.currentMonthFirstDay(), -1);

        $("#date1").val(dateUtilsJs.formatDate(startDate, "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.formatDate(endDate, "YYYY-MM-DD"));
        _initEvent();
        _searchOnClick(); //최초 진입 시 date1~date2 기간을 포함해 조회
    }

    function _initEvent() {
        $("#btnSearch").click(function () {
            _resetSort();
            _searchOnClick();
        });
        $("#btnReset").click(function () {
            _searchResetClick();
        });
        //검색조건 Enter키로 검색기능
        $("#searchKey").keypress(function (event) {
            if (event.keyCode === 13) {
                _resetSort();
                _searchOnClick();
            }
        });
        $("#saveExcelcs").click(function () {
            _downloadExcel();
        });
        $("#btnAdjustSave").click(_onClickSaveAdjust);

        $('#date1').datepicker({
            todayBtn: "linked",
            autoclose: true,
            format: "yyyy-mm-dd"
        });
        $('#date2').datepicker({
            todayBtn: "linked",
            autoclose: true,
            format: "yyyy-mm-dd"
        });

        $("#sType").change(function () {
            $("#searchKey").val("");
            switch ($("#sType").val()) {
                case 'custName':
                    $("#Popup_Customer").modal();
                    customerPopupJs.init(_selectedCustomer);
                    break;
            }
        });

        //세대주명 선택 상태에서 검색어 입력창을 다시 클릭해도 고객검색 팝업이 뜨도록
        $("#searchKey").click(function () {
            switch ($("#sType").val()) {
                case 'custName':
                    $("#Popup_Customer").modal();
                    customerPopupJs.init(_selectedCustomer);
                    break;
            }
        });

        //정렬(단지/동/호/충전시작시간/충전종료시간) 컬럼 헤더 클릭 - 클릭할 때마다 오름차순/내림차순 토글, 직전 검색 조건은 그대로 유지
        $(".sortBtn").click(function () {
            let $btn = $(this);
            let toAsc = $btn.data("state") !== "asc";
            $btn.data("state", toAsc ? "asc" : "desc").text(toAsc ? "▲" : "▼");
            data.searchCond.dateOrder = toAsc ? $btn.data("asc") : $btn.data("desc");
            _search();
        });
    }

    //정렬 상태를 기본정렬(단지-동-호-충전시작시간)로 되돌림 - 검색/초기화 시 공통 사용(UI만, dateOrder는 각 검색 함수가 설정)
    function _resetSort() {
        $(".sortBtn").data("state", "desc").text("▼");
    }

    function _searchResetClick() {
        _resetSort();
        let endDate = dateUtilsJs.addDay(dateUtilsJs.currentMonthFirstDay(), -1);
        let startDate = dateUtilsJs.addMonth(dateUtilsJs.currentMonthFirstDay(), -1);

        //.val()만으로는 datepicker 내부상태가 갱신되지 않아 이후 달력에서 이전 선택값이 남거나
        //선택 시 입력값이 비어버리는 현상이 생김 - 'update'를 함께 호출해 내부상태를 동기화
        $("#date1").val(dateUtilsJs.formatDate(startDate, "YYYY-MM-DD")).datepicker('update');
        $("#date2").val(dateUtilsJs.formatDate(endDate, "YYYY-MM-DD")).datepicker('update');
        $("#sType").val("custName");
        $("#searchKey").val("");
        _searchOnClick(); //초기화 시 date1~date2 기간을 포함해 재조회
    }

    //검색: 충전기간 + (회원카드번호 직접입력 또는 세대주명 팝업선택)
    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, rechargingCustomerJs.search);
        data.searchCond = { dateOrder: 'Z', status: ['RECS03'] };

        data.searchCond.dateType = $("#dateType").val();
        data.searchCond.fromDate = formmatUtilsJs.removeDash($("#date1").val()) + "000000";
        data.searchCond.toDate = formmatUtilsJs.removeDash($("#date2").val()) + "235959";

        let searchKey = $("#searchKey").val().replace(/-/g, '').trim();
        if ($("#sType").val() === 'custCardNo') {
            if (searchKey.length !== 16) {
                toastr.warning(_msg.cardDigit16, _msg.cardNumber);
                return;
            }
            data.searchCond.cutCardNo = searchKey;
        }

        $("#searchKey").val(searchKey);
        _search();
    }

    function _search() {
        //
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="18">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        if (data.searchCond.customerId) {
            param += "&customerId=" + data.searchCond.customerId;
        }
        if (data.searchCond.cutCardNo) {
            param += "&cutCardNo=" + data.searchCond.cutCardNo;
        }
        if (data.searchCond.fromDate) {
            param += "&fromDate=" + data.searchCond.fromDate;
        }
        if (data.searchCond.toDate) {
            param += "&toDate=" + data.searchCond.toDate;
        }
        param += "&dateOrder=" + data.searchCond.dateOrder;
        if (data.searchCond.dateType) {
            param += "&dateType=" + data.searchCond.dateType;
        }
        param += "&status=" + data.searchCond.status;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/recharging/customer/list" + param,
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
        $("#totalPaySum").text("(청구금액합계: " + formmatUtilsJs.commaFormat(jsonData.criteria.paySumTotal || 0) + "원)");
        $("#tBodyList").empty();
        let html = '';
        if (jsonData.criteria.totalItemCount === 0) {
            html = '<tr style="text-align:center;">';
            html += '<td colspan="18">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
            return;
        }
        let result = jsonData.result;
        let noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        for (let i = 0, length = result.length; i < length; ++i) {
            html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + (result[i].complexName ? result[i].complexName : "-") + '</td>';
            html += '<td>' + result[i].cpName + '</td>';
            if (result[i].chStatCode == 'RECS02') {
                html += '<td><a href="#" onclick="rechargingCustomerJs.searchRechargingDetail(\'' + result[i].rechargingId + '\')">' + result[i].rechargingId + '</a></td>';
            } else {
                html += '<td>' + result[i].rechargingId + '</td>';
            }
            html += '<td>' + result[i].cpId + "-" + result[i].csId + '</td>';
            html += '<td>' + (result[i].dong || '') + '</td>';
            html += '<td>' + (result[i].ho || '') + '</td>';
            html += '<td>' + (result[i].custName ? result[i].custName : "-") + '</td>';
            html += '<td>' + formmatUtilsJs.cardFormat(result[i].cutCardNo) + '</td>';

            if (result[i].chStartDate) {
                let cdt = new Date(result[i].chStartDate);
                html += '<td> ' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + ' </td>';
            } else {
                html += '<td> </td>';
            }
            if (result[i].chEndDate) {
                let cdt = new Date(result[i].chEndDate);
                html += '<td> ' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + ' </td>';
            } else {
                html += '<td> </td>';
            }
            if (result[i].chStartDate && result[i].chEndDate) {
                let chTime = _chargingTime(new Date(result[i].chStartDate), new Date(result[i].chEndDate));
                html += chTime + '</td>';
            } else {
                html += '<td> </td>';
            }
            html += '<td>' + (result[i].chUseAmount ? result[i].chUseAmount : "0") + '</td>';
            html += '<td>' + (result[i].chUseUnitCost ? result[i].chUseUnitCost : "0") + '</td>';
            html += '<td>' + (result[i].chUseCost ? result[i].chUseCost : "0") + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].adjustAmount || 0) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].paySum || 0) + '</td>';
            html += '<td><button class="btn btn-warning btn-xs" onclick="rechargingCustomerJs.openAdjustPopup(\'' + result[i].rechargingId + '\')">조정</button></td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }
    }

    //충전소요시간
    function _chargingTime(start, end) {
        let result = ''; //50400000 => 14시간	//86400000 => 1일
        let gapTime = end.getTime() - start.getTime();
        let d = parseInt(gapTime / (1000 * 60 * 60 * 24));
        let h = Math.floor((gapTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        let m = Math.floor((gapTime % (1000 * 60 * 60)) / (1000 * 60));
        let s = Math.floor((gapTime % (1000 * 60)) / 1000);

        if (gapTime >= 50400000) {
            result += '<td style="background-color:#FFDCDC">';
        } else {
            result += '<td>';
        }

        if (d > 0) {
            result += d + _msg.day;
        }
        if (h > 0) {
            result += h + _msg.hour;
        }
        if (m > 0) {
            result += m + _msg.minute;
        }
        if (s >= 0) {
            result += s + _msg.second;
        }
        return result;
    }

    function _downloadExcel() {
        toastr.info(_msg.pleaseWait, _msg.excelDownload);
        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&customerId="
        if (data.searchCond.customerId) {
            param += data.searchCond.customerId;
        }
        param += "&cutCardNo="
        if (data.searchCond.cutCardNo) {
            param += data.searchCond.cutCardNo;
        }
        if (data.searchCond.fromDate) {
            param += "&fromDate=" + data.searchCond.fromDate;
        }
        if (data.searchCond.toDate) {
            param += "&toDate=" + data.searchCond.toDate;
        }
        param += "&dateOrder=" + data.searchCond.dateOrder;
        if (data.searchCond.dateType) {
            param += "&dateType=" + data.searchCond.dateType;
        }

        parent.layerJs.fn_download(_ctx + "/ws/recharging/download/customer/list" + param);
    }

    function _selectedCustomer(obj) {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, rechargingCustomerJs.search);
        data.searchCond = { dateOrder: 'Z', status: ['RECS03'], customerId: obj.id };
        $("#searchKey").val(obj.name);
        _resetSort();
        _search();
    }

    function _searchRechargingDetail(rechargingId) {
        //
        let param = "?rechargingId=" + rechargingId;

        parent.layerJs.fn_moveMenu('20000104', _msg.exceptionMgmt, _ctx + '/recharging/exception/view' + param, 'THIS', true);
    }

    //조정금액 등록 모달 오픈
    function _openAdjustPopup(rechargingId) {
        $("#adj_rechargingId").text(rechargingId).data("rechargingId", rechargingId);
        $("#adj_amount").val("");
        $("#adj_reason").val("");
        $("#Popup_RechargingAdjustment").modal();
    }

    function _onClickSaveAdjust() {
        let rechargingId = $("#adj_rechargingId").data("rechargingId");
        let adjustAmount = parseInt($("#adj_amount").val(), 10);
        if (isNaN(adjustAmount)) {
            toastr.warning("조정금액을 입력해주세요.");
            return;
        }
        $.ajax({
            type: 'POST',
            url: _ctx + "/ws/recharging/adjustment",
            contentType: 'application/json',
            data: JSON.stringify({
                rechargingId: rechargingId,
                adjustAmount: adjustAmount,
                adjustReason: $("#adj_reason").val().trim()
            }),
            dataType: 'json',
            success: function (res) {
                if (res && res.status === 'SUCCESS') {
                    toastr.success("조정금액이 등록되었습니다.");
                    $("#Popup_RechargingAdjustment").modal('hide');
                    _search(); //현재 목록 그대로 재조회 - 조정금액/청구금액 갱신
                } else {
                    swal("오류", (res && res.message) || "등록에 실패했습니다.", "error");
                }
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    return {
        init: _init,
        search: _search,
        searchRechargingDetail: _searchRechargingDetail,
        openAdjustPopup: _openAdjustPopup
    };
}();
