/**
 * 고객충전현황
 */
let rechargingCustomerJs = function () {
    "use strict";

    let data = {
        searchCond: { dateOrder: 'Z', status: ['RECS03'] } //dateOrder Z: 기본정렬(단지-동-호-충전시작시간), status: 완료(종료)된 충전만 기본 조회
    };

    //단지/동/호 select·radio에 쓰이는 조직 데이터 - 최초 1회만 조회해 캐시(간편검색 영역 + 조건별다운로드 모달 공용)
    let _orgData = { complexes: [], customers: [], loaded: false };

    function _init() {
        //
        let endDate = dateUtilsJs.addDay(dateUtilsJs.currentMonthFirstDay(), -1);
        let startDate = dateUtilsJs.addMonth(dateUtilsJs.currentMonthFirstDay(), -1);

        $("#date1").val(dateUtilsJs.formatDate(startDate, "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.formatDate(endDate, "YYYY-MM-DD"));
        _initEvent();
        _initSimpleSearchArea();
        _showInitialGuide(); //사용자가 검색 조건을 설정하고 검색 버튼을 누르기 전에는 목록을 조회하지 않음

        chargingPointPopupJs.init();
    }

    function _initEvent() {
        $("#btnSimpleSearch").click(function () {
            _resetSort();
            _simpleSearchOnClick();
        });
        $("#btnSearch").click(function () {
            _resetSort();
            _keywordSearchOnClick();
        });
        $("#btnReset").click(function () {
            _searchResetClick();
        });
        //검색조건 Enter키로 검색기능
        $("#searchKey").keypress(function (event) {
            if (event.keyCode === 13) {
                _resetSort();
                _keywordSearchOnClick();
            }
        });
        $("input[name='dateSearchMode']").change(function () {
            let isDetail = $(this).val() === 'detail';
            $("#detailDateGroup").css('display', isDetail ? 'inline-flex' : 'none');
        });
        $("#simpleDong").change(_refreshSimpleHoSelect);
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
            $("#searchKey").val("");
            switch ($("#sType").val()) {
                case 'CUSTOMER':
                    $("#Popup_Customer").modal();
                    customerPopupJs.init(_selectedCustomer);
                    break;
            }
        });

        $("#searchKey").click(function () {
            //
            switch ($("#sType").val()) {
                case 'CUSTOMER':
                    $("#Popup_Customer").modal();
                    customerPopupJs.init(_selectedCustomer);
                    break;
            }
        });

        //정렬(동/호/충전시작시간/충전종료시간) 컬럼 헤더 클릭 - 클릭할 때마다 오름차순/내림차순 토글, 직전 검색 조건은 그대로 유지
        $(".sortBtn").click(function () {
            let $btn = $(this);
            let toAsc = $btn.data("state") !== "asc";
            $btn.data("state", toAsc ? "asc" : "desc").text(toAsc ? "▲" : "▼");
            data.searchCond.dateOrder = toAsc ? $btn.data("asc") : $btn.data("desc");
            _search();
        });
    }

    //단지 조직 데이터(complex/customer)를 최초 1회만 조회 - 간편검색 영역과 조건별다운로드 모달이 공유
    function _loadOrgData(callback) {
        if (_orgData.loaded) {
            callback();
            return;
        }
        $.when(
            $.ajax({ type: 'GET', url: _ctx + "/ws/organization/complex/search?pageItemSize=9999", dataType: 'json' }),
            $.ajax({ type: 'GET', url: _ctx + "/ws/customer/search?pageItemSize=99999", dataType: 'json' })
        ).done(function (complexRes, customerRes) {
            _orgData.complexes = complexRes[0].result || [];
            _orgData.customers = customerRes[0].result || [];
            _orgData.loaded = true;
            callback();
        }).fail(function (xhRequest) { parent.layerJs.fn_exception(xhRequest); });
    }

    //complexId/dong 조건에 맞는 customers 중 field(dong 또는 ho)의 중복 제거·정렬된 목록
    function _distinctSorted(customers, complexId, dong, field) {
        let list = [];
        for (let i = 0; i < customers.length; ++i) {
            let c = customers[i];
            if (complexId && c.complexId !== complexId) { continue; }
            if (dong && c.dong !== dong) { continue; }
            let v = c[field];
            if (v && list.indexOf(v) === -1) { list.push(v); }
        }
        list.sort();
        return list;
    }

    function _optionsHtml(list) {
        let html = '<option value="">전체</option>';
        for (let i = 0; i < list.length; ++i) {
            html += '<option value="' + list[i] + '">' + list[i] + '</option>';
        }
        return html;
    }

    //간편검색 영역(연/월, 단지 라디오, 동/호 select) 초기화
    function _initSimpleSearchArea() {
        let now = new Date();
        let curYear = now.getFullYear();
        let curMonth = now.getMonth() + 1;
        let yearHtml = "";
        for (let y = curYear; y >= curYear - 3; --y) {
            yearHtml += '<option value="' + y + '"' + (y === curYear ? ' selected' : '') + '>' + y + '년</option>';
        }
        $("#simpleYear").html(yearHtml);
        let monthHtml = "";
        for (let m = 1; m <= 12; ++m) {
            monthHtml += '<option value="' + m + '"' + (m === curMonth ? ' selected' : '') + '>' + m + '월</option>';
        }
        $("#simpleMonth").html(monthHtml);

        _loadOrgData(function () {
            let html = '<label style="margin:0;"><input type="radio" name="searchComplex" value="" checked>&nbsp;전체</label>';
            for (let i = 0; i < _orgData.complexes.length; ++i) {
                html += '<label style="margin:0;"><input type="radio" name="searchComplex" value="' + _orgData.complexes[i].complexId + '">&nbsp;' + _orgData.complexes[i].complexName + '</label>';
            }
            $("#complexRadioGroup").html(html);
            $("input[name='searchComplex']").change(_refreshSimpleDongSelect);
            _refreshSimpleDongSelect();
        });
    }

    function _refreshSimpleDongSelect() {
        let complexId = $("input[name='searchComplex']:checked").val();
        $("#simpleDong").html(_optionsHtml(_distinctSorted(_orgData.customers, complexId, '', 'dong')));
        _refreshSimpleHoSelect();
    }

    function _refreshSimpleHoSelect() {
        let complexId = $("input[name='searchComplex']:checked").val();
        let dong = $("#simpleDong").val();
        $("#simpleHo").html(_optionsHtml(_distinctSorted(_orgData.customers, complexId, dong, 'ho')));
    }

    //목록 조회 전 안내 문구 표시 - 페이지 최초 진입 시, 초기화 시 공통 사용
    function _showInitialGuide() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="15">' + _msg.searchGuide + '</td></tr>');
        $("#totalCount").html("");
    }

    //정렬 상태를 기본정렬(단지-동-호-충전시작시간)로 되돌림 - 검색/초기화 시 공통 사용(UI만, dateOrder는 각 검색 함수가 설정)
    function _resetSort() {
        $(".sortBtn").data("state", "desc").text("▼");
    }

    function _searchResetClick() {
        _resetSort();
        $("#dateType").val("E");
        let endDate = dateUtilsJs.addDay(dateUtilsJs.currentMonthFirstDay(), -1);
        let startDate = dateUtilsJs.addMonth(dateUtilsJs.currentMonthFirstDay(), -1);

        $("#date1").val(dateUtilsJs.formatDate(startDate, "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.formatDate(endDate, "YYYY-MM-DD"));
        $("input[name='dateSearchMode'][value='month']").prop('checked', true);
        $("#detailDateGroup").hide();
        $("input[name='searchComplex'][value='']").prop('checked', true);
        _refreshSimpleDongSelect();
        $("#searchKey").val("");
        _showInitialGuide(); //초기화 후에는 사용자가 다시 검색 버튼을 눌러야 조회됨
    }

    //간편 검색: 충전기간(연월 또는 상세) + 단지/동/호
    function _simpleSearchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, rechargingCustomerJs.search);
        data.searchCond = { dateOrder: 'Z', status: ['RECS03'] };

        let complexId = $("input[name='searchComplex']:checked").val();
        if (complexId) { data.searchCond.complexId = complexId; }
        if ($("#simpleDong").val()) { data.searchCond.dong = $("#simpleDong").val(); }
        if ($("#simpleHo").val()) { data.searchCond.ho = $("#simpleHo").val(); }

        if ($("input[name='dateSearchMode']:checked").val() === 'detail') {
            data.searchCond.dateType = $("#dateType").val();
            data.searchCond.fromDate = formmatUtilsJs.removeDash($("#date1").val()) + "000000";
            data.searchCond.toDate = formmatUtilsJs.removeDash($("#date2").val()) + "235959";
        } else {
            let year = parseInt($("#simpleYear").val());
            let month = parseInt($("#simpleMonth").val());
            let lastDay = new Date(year, month, 0).getDate();
            let monthStr = (month < 10 ? "0" : "") + month;
            data.searchCond.dateType = 'E'; //충전종료 시각 기준
            data.searchCond.fromDate = year + monthStr + "01000000";
            data.searchCond.toDate = year + monthStr + lastDay + "235959";
        }
        _search();
    }

    //키워드 검색: 회원카드번호/고객명(충전기ID 검색은 제외)
    function _keywordSearchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, rechargingCustomerJs.search);
        data.searchCond = { dateOrder: 'Z', status: ['RECS03'] };

        let searchKey = $("#searchKey").val().replace(/-/g, '').trim();
        if ($("#sType").val() === 'CUT_CARD_NO') {
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
        html += '<td colspan="15">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        if (data.searchCond.customerId) {
            param += "&customerId=" + data.searchCond.customerId;
        }
        if (data.searchCond.cutCardNo) {
            param += "&cutCardNo=" + data.searchCond.cutCardNo;
        }
        if (data.searchCond.complexId) {
            param += "&complexId=" + data.searchCond.complexId;
        }
        if (data.searchCond.dong) {
            param += "&dong=" + encodeURIComponent(data.searchCond.dong);
        }
        if (data.searchCond.ho) {
            param += "&ho=" + encodeURIComponent(data.searchCond.ho);
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
        param += "&customerId="
        if (data.searchCond.customerId) {
            param += data.searchCond.customerId;
        }
        param += "&cutCardNo="
        if (data.searchCond.cutCardNo) {
            param += data.searchCond.cutCardNo;
        }
        if (data.searchCond.complexId) {
            param += "&complexId=" + data.searchCond.complexId;
        }
        if (data.searchCond.dong) {
            param += "&dong=" + encodeURIComponent(data.searchCond.dong);
        }
        if (data.searchCond.ho) {
            param += "&ho=" + encodeURIComponent(data.searchCond.ho);
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

    //조건별다운로드 모달 오픈 - 조회월 선택지 채우고, 단지/동/호는 캐시된 _orgData로 채움
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

        _loadOrgData(function () {
            let html = '<option value="">전체</option>';
            for (let i = 0; i < _orgData.complexes.length; ++i) {
                html += '<option value="' + _orgData.complexes[i].complexId + '">' + _orgData.complexes[i].complexName + '</option>';
            }
            $("#ce_complex").html(html);
            _refreshDongSelect();
        });

        $("#Popup_Recharging_CondExcel").modal();
    }

    //선택된 단지에 속한 동 목록(DB값)으로 동 select 갱신
    function _refreshDongSelect() {
        let complexId = $("#ce_complex").val();
        $("#ce_dong").html(_optionsHtml(_distinctSorted(_orgData.customers, complexId, '', 'dong')));
        _refreshHoSelect();
    }

    //선택된 단지+동에 속한 호 목록(DB값)으로 호 select 갱신
    function _refreshHoSelect() {
        let complexId = $("#ce_complex").val();
        let dong = $("#ce_dong").val();
        $("#ce_ho").html(_optionsHtml(_distinctSorted(_orgData.customers, complexId, dong, 'ho')));
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
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, rechargingCustomerJs.search);
        data.searchCond = { dateOrder: 'Z', status: ['RECS03'], customerId: obj.id };
        $("#searchKey").val(obj.name);
        _resetSort();
        _search();
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
