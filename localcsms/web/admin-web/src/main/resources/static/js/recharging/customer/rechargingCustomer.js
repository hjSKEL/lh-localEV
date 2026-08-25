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
        _searchOnClick();

        chargingPointPopupJs.init();
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
        $("#saveCondExcelcs").click(function () {
            _openCondExcelModal();
        });
        $("#ce_complex").change(_refreshDongSelect);
        $("#ce_dong").change(_refreshHoSelect);
        $("#btnCondExcelDownload").click(_submitCondExcel);

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

        $("#sType").change(function () {
            data.searchCond = {};
            $("#searchKey").val("");
            switch ($("#sType").val()) {
                case 'COMPANY':
                    $("#Popup_Company").modal();
                    companyPopupJs.init(_selectedCompany);
                    break;
                case 'CUSTOMER':
                    $("#Popup_Customer").modal();
                    customerPopupJs.init(_selectedCustomer);
                    break;
            }
        });

        $("#searchKey").click(function () {
            //
            data.searchCond = {};
            //$("#searchKey").val("");
            switch ($("#sType").val()) {
                case 'COMPANY':
                    $("#Popup_Company").modal();
                    companyPopupJs.init(_selectedCompany);
                    break;
                case 'CUSTOMER':
                    $("#Popup_Customer").modal();
                    customerPopupJs.init(_selectedCustomer);
                    break;
            }
        });

        //정렬(동/호/충전시작시간/충전종료시간) 컬럼 헤더 클릭 - 클릭할 때마다 오름차순/내림차순 토글
        $(".sortBtn").click(function () {
            let $btn = $(this);
            let toAsc = $btn.data("state") !== "asc";
            $btn.data("state", toAsc ? "asc" : "desc").text(toAsc ? "▲" : "▼");
            data.searchCond.dateOrder = toAsc ? $btn.data("asc") : $btn.data("desc");
            _searchOnClick();
        });
    }

    //정렬 상태를 기본정렬(단지-동-호-충전시작시간)로 되돌림 - 검색/초기화 시 공통 사용
    function _resetSort() {
        $(".sortBtn").data("state", "desc").text("▼");
        data.searchCond.dateOrder = 'Z';
    }

    function _searchResetClick() {
        _resetSort();
        data.searchCond.status = ['RECS03'];
        $("#dateType").val("E");
        let endDate = dateUtilsJs.addDay(dateUtilsJs.currentMonthFirstDay(), -1);
        let startDate = dateUtilsJs.addMonth(dateUtilsJs.currentMonthFirstDay(), -1);

        $("#date1").val(dateUtilsJs.formatDate(startDate, "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.formatDate(endDate, "YYYY-MM-DD"));
        $("#searchKey").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, rechargingCustomerJs.search);
        data.searchCond.cutCardNo = '';
        data.searchCond.mblPhoneNo = '';
        data.searchCond.cpId = '';
        data.searchCond.csId = '';
        let searchKey = $("#searchKey").val().replace(/-/g, '').trim();
        if ($("#sType").val() === 'CUT_CARD_NO') {
            data.searchCond.cutCardNo = searchKey;
        } else if ($("#sType").val() === 'CUT_MBLPHONE_NO') {
            data.searchCond.mblPhoneNo = searchKey;
        } else if ($("#sType").val() === 'CSUNIQID') {
            data.searchCond.csId = searchKey;
        }
        if (data.searchCond.csId && data.searchCond.csId.length > 0) {
            data.searchCond.csId = data.searchCond.csId.replace("-", "");
            if (data.searchCond.csId.length !== 8) {
                toastr.warning(_msg.chargerIdDigit8, _msg.chargerId);
                return;
            }
            data.searchCond.cpId = data.searchCond.csId.substring(0, 6);
            data.searchCond.csId = data.searchCond.csId.substring(6);
        }
        if (data.searchCond.cutCardNo && data.searchCond.cutCardNo.length > 0) {
            if (data.searchCond.cutCardNo.length !== 16) {
                toastr.warning(_msg.cardDigit16, _msg.cardNumber);
                return;
            }
        }

        $("#searchKey").val(searchKey);

        data.searchCond.dateType = $("#dateType").val();
        data.searchCond.fromDate = formmatUtilsJs.removeDash($("#date1").val()) + "000000";
        data.searchCond.toDate = formmatUtilsJs.removeDash($("#date2").val()) + "235959";

        // if (parent.UserRole === 'COMPANY_USER') {
        //     data.searchCond.companyId = parent.roleCompanyId;
        // }
        _search();
    }

    function _search() {
        //
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="15">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        // if (data.searchCond.companyId) {
        //     param += "&companyId=" + data.searchCond.companyId;
        // }
        if (data.searchCond.customerId) {
            param += "&customerId=" + data.searchCond.customerId;
        }
        if (data.searchCond.cutCardNo) {
            param += "&cutCardNo=" + data.searchCond.cutCardNo;
        }
        if (data.searchCond.mblPhoneNo) {
            param += "&mblPhoneNo=" + data.searchCond.mblPhoneNo;
        }
        if (data.searchCond.csId) {
            param += "&cpId=" + data.searchCond.cpId + "&csId=" + data.searchCond.csId;
        }
        param += "&fromDate=" + data.searchCond.fromDate;
        param += "&toDate=" + data.searchCond.toDate;
        param += "&dateOrder=" + data.searchCond.dateOrder;
        param += "&dateType=" + data.searchCond.dateType;
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
        $("#tBodyList").empty();
        let html = '';
        if (jsonData.criteria.totalItemCount === 0) {
            html = '<tr style="text-align:center;">';
            html += '<td colspan="15">' + _commonMsg.noData + '</td>';
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
        // param += "&companyId="
        // if (data.searchCond.companyId) {
        //     param += data.searchCond.companyId;
        // }
        param += "&customerId="
        if (data.searchCond.customerId) {
            param += data.searchCond.customerId;
        }
        param += "&cutCardNo="
        if (data.searchCond.cutCardNo) {
            param += data.searchCond.cutCardNo;
        }
        param += "&mblPhoneNo="
        if (data.searchCond.mblPhoneNo) {
            param += data.searchCond.mblPhoneNo;
        }
        param += "&cpId="
        if (data.searchCond.csId) {
            param += data.searchCond.cpId + "&csId=" + data.searchCond.csId;
        }
        param += "&fromDate=" + data.searchCond.fromDate;
        param += "&toDate=" + data.searchCond.toDate;
        param += "&dateOrder=" + data.searchCond.dateOrder;
        param += "&dateType=" + data.searchCond.dateType;

        parent.layerJs.fn_download(_ctx + "/ws/recharging/download/customer/list" + param);
    }

    //조건별다운로드 모달의 단지/동/호 데이터 - 모달 열 때 1회 조회해 캐시
    let _condData = { customers: [] };

    //조건별다운로드 모달 오픈 - 조회월/단지 선택지를 DB값으로 채움
    function _openCondExcelModal() {
        let now = new Date();
        let curYear = now.getFullYear();
        let curMonth = now.getMonth() + 1;
        let yearHtml = "";
        for (let y = curYear; y >= curYear - 3; --y) {
            yearHtml += '<option value="' + y + '"' + (y === curYear ? ' selected' : '') + '>' + y + '년</option>';
        }
        $("#ce_year").html(yearHtml);
        let monthHtml = "";
        for (let m = 1; m <= 12; ++m) {
            monthHtml += '<option value="' + m + '"' + (m === curMonth ? ' selected' : '') + '>' + m + '월</option>';
        }
        $("#ce_month").html(monthHtml);

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/organization/complex/search?pageItemSize=9999",
            dataType: 'json',
            success: function (jsonData) {
                let result = jsonData.result || [];
                let html = '<option value="">전체</option>';
                for (let i = 0; i < result.length; ++i) {
                    html += '<option value="' + result[i].complexId + '">' + result[i].complexName + '</option>';
                }
                $("#ce_complex").html(html);
            },
            error: function (xhRequest) { parent.layerJs.fn_exception(xhRequest); }
        });
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/search?pageItemSize=99999",
            dataType: 'json',
            success: function (jsonData) {
                _condData.customers = jsonData.result || [];
                _refreshDongSelect();
            },
            error: function (xhRequest) { parent.layerJs.fn_exception(xhRequest); }
        });

        $("#Popup_Recharging_CondExcel").modal();
    }

    //선택된 단지에 속한 동 목록(DB값)으로 동 select 갱신
    function _refreshDongSelect() {
        let complexId = $("#ce_complex").val();
        let dongList = [];
        for (let i = 0; i < _condData.customers.length; ++i) {
            let c = _condData.customers[i];
            if (complexId && c.complexId !== complexId) { continue; }
            if (c.dong && dongList.indexOf(c.dong) === -1) { dongList.push(c.dong); }
        }
        dongList.sort();
        let html = '<option value="">전체</option>';
        for (let i = 0; i < dongList.length; ++i) {
            html += '<option value="' + dongList[i] + '">' + dongList[i] + '</option>';
        }
        $("#ce_dong").html(html);
        _refreshHoSelect();
    }

    //선택된 단지+동에 속한 호 목록(DB값)으로 호 select 갱신
    function _refreshHoSelect() {
        let complexId = $("#ce_complex").val();
        let dong = $("#ce_dong").val();
        let hoList = [];
        for (let i = 0; i < _condData.customers.length; ++i) {
            let c = _condData.customers[i];
            if (complexId && c.complexId !== complexId) { continue; }
            if (dong && c.dong !== dong) { continue; }
            if (c.ho && hoList.indexOf(c.ho) === -1) { hoList.push(c.ho); }
        }
        hoList.sort();
        let html = '<option value="">전체</option>';
        for (let i = 0; i < hoList.length; ++i) {
            html += '<option value="' + hoList[i] + '">' + hoList[i] + '</option>';
        }
        $("#ce_ho").html(html);
    }

    function _submitCondExcel() {
        let year = parseInt($("#ce_year").val());
        let month = parseInt($("#ce_month").val());
        let lastDay = new Date(year, month, 0).getDate();
        let monthStr = (month < 10 ? "0" : "") + month;
        toastr.info(_msg.pleaseWait, _msg.excelDownload);
        let param = "?status=RECS03";
        param += "&fromDate=" + year + monthStr + "01000000";
        param += "&toDate=" + year + monthStr + lastDay + "235959";
        param += "&dateType=S";
        param += "&dateOrder=Z";
        if ($("#ce_complex").val()) { param += "&complexId=" + $("#ce_complex").val(); }
        if ($("#ce_dong").val()) { param += "&dong=" + $("#ce_dong").val(); }
        if ($("#ce_ho").val()) { param += "&ho=" + $("#ce_ho").val(); }
        parent.layerJs.fn_download(_ctx + "/ws/recharging/download/customer/list" + param);
        $("#Popup_Recharging_CondExcel").modal('hide');
    }

    function _calcDate(no) {
        //
        let from = dateUtilsJs.addDay(new Date(), -no);
        let to = new Date();
        $('#date2').val(formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(to), "YYYY-MM-DD"));
        $('#date1').val(formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(from), "YYYY-MM-DD")); //몇일 전
    }

    function _selectedCustomer(obj) {
        data.searchCond.customerId = obj.id;
        $("#searchKey").val(obj.name);
        _searchOnClick();
    }

    function _selectedCompany(obj) {
        data.searchCond.companyId = obj.companyId;
        $("#searchKey").val(obj.companyName);
        _searchOnClick();
    }

    function _popup(cpId) {
        $("#Popup_ChargingPointInfo").modal();
        chargingPointPopupJs.search(cpId);
    }

    function _searchRechargingDetail(rechargingId) {
        //
        let param = "?rechargingId=" + rechargingId;

        parent.layerJs.fn_moveMenu('20000104', _msg.exceptionMgmt, _ctx + '/recharging/exception/view' + param, 'THIS', true);
    }

    return {
        init: _init,
        search: _search,
        calcDate: _calcDate,
        searchRechargingDetail: _searchRechargingDetail,
        popup: _popup,
    };
}();
