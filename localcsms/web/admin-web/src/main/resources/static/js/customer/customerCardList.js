/**
 * 회원카드 목록 (세대당 최대 5장) — TB_CUCA001 / CustomerCardResource.
 *cardListTbody
 * 회원 상세 페이지 (customer.html) 의 "회원카드목록" 섹션에 바인딩.
 * REST: /ws/customer/card* (기존 customerCard.js/customerCardList.js 화면과 동일 엔드포인트 재사용)
 */
var customerCardListJs = (function () {

    var customerId = null;
    var MAX_CARD_COUNT = 5;

    var STOP_RSN_LABEL = { LOSS: '분실', USER_REQ: '사용자요청', UNPAID: '관리비(요금)미납' };

    function _stopReasonLabel(c) {
        if (c.stopRsnCd === 'ETC') return c.stopRsnTxt || '기타';
        return STOP_RSN_LABEL[c.stopRsnCd] || '-';
    }

    function _statLabel(c) {
        return c.stopYn === 'Y' ? '정지' : '정상';
    }

    var data = { searchCond: {} };
    var editingCutCardNo = null;  // null = 카드 추가 모드, 값 있음 = 카드 수정 모드
    var _cardListCache = [];  // _renderList 로 받은 목록 - "카드상세" 클릭 시 팝업에 채워넣기 위한 조회용

    function _init(custId) {
        customerId = custId;
        if (customerId) {
            // 회원 상세 페이지에 임베딩되는 카드 목록
            $("#btnCardListAdd").off("click").on("click", _onClickAdd);
            $("#btnCardNoChecker").off("click").on("click", _onClickCheckCardNo);
            $("#btnCardListSave").off("click").on("click", _onClickSave);
            $("#btnCardStop").off("click").on("click", _onClickStop);
            $("#btnCardReactivate").off("click").on("click", _onClickReactivate);
            $("#btnCardListCancel").off("click").on("click", _onClickCancel);
            $("#stopRsnCd").off("change").on("change", function () {
                $("#stopRsnTxt").toggle($(this).val() === 'ETC');
            });
            // ESC/배경클릭/닫기버튼 등 어떤 경로로 팝업이 닫히든 추가/수정 모드를 초기화
            $("#Popup_CardInfo").off("hidden.bs.modal").on("hidden.bs.modal", _resetCardForm);
            _loadList();
        } else {
            // 독립된 /customerCard/list 화면
            _initListPage();
        }
    }

    function _initListPage() {
        $("#btnSearch").off("click").on("click", _searchOnClick);
        $("#btnReset").off("click").on("click", _searchResetClick);
        $("#sWord").off("keypress").on("keypress", function (event) {
            if (event.keyCode === 13) _searchOnClick();
        });
        $("#status").off("change").on("change", _searchOnClick);
        $("#saveExcelcs").off("click").on("click", _downloadExcel);

        //정렬(회원카드번호/세대주명) 컬럼 헤더 클릭 - 클릭할 때마다 오름차순/내림차순 토글
        $(".sortBtn").off("click").on("click", function () {
            var $btn = $(this);
            var toAsc = $btn.data("state") !== "asc";
            $btn.data("state", toAsc ? "asc" : "desc").text(toAsc ? "▲" : "▼");
            data.searchCond.sortOrder = toAsc ? $btn.data("asc") : $btn.data("desc");
            _searchOnClick();
        });

        $("#status").append('<option value="" selected>' + _msg.cardStatusAll + '</option>');
        $("#status").append('<option value="N">정상</option>');
        $("#status").append('<option value="Y">정지</option>');
        _searchOnClick();
    }

    function _searchResetClick() {
        $("#sWord").val("");
        $("#status").val("");
        $("#searchType").val("ID");
        $(".sortBtn").data("state", "desc").text("▼");
        data.searchCond.sortOrder = "";
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, customerCardListJs.search);
        data.searchCond.customerId = "";
        data.searchCond.customerName = "";
        data.searchCond.cutCardNo = "";
        var searchType = $("#searchType").val();
        var searchKey = $("#sWord").val().replace(/-/g, '').trim();
        switch (searchType) {
            case "ID":
                data.searchCond.customerId = searchKey;
                break;
            case "NAME":
                data.searchCond.customerName = searchKey;
                break;
            case "CARDNO":
                if (searchKey && searchKey.length !== 16) {
                    toastr.warning(_msg.cardDigit16, _msg.cardNumber);
                    return;
                }
                data.searchCond.cutCardNo = searchKey;
                break;
            default:
        }
        data.searchCond.stopYn = $("#status").val();
        $("#sWord").val(searchKey);
        _search();
    }

    function _search() {
        var paging = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&customerName=" + encodeURIComponent(data.searchCond.customerName || "");
        param += "&cutCardNo=" + encodeURIComponent(data.searchCond.cutCardNo || "");
        param += "&stopYn=" + encodeURIComponent(data.searchCond.stopYn || "");
        param += "&sortOrder=" + encodeURIComponent(data.searchCond.sortOrder || "");
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/card" + param,
            dataType: 'json',
            success: _displayListPage,
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _displayListPage(page) {
        pageInfoJs.setTotalCount(page.criteria.totalItemCount);
        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', page.criteria.totalItemCount));
        var $tbody = $("#tBodyList").empty();
        if (page.criteria.totalItemCount === 0) {
            $tbody.append('<tr><td colspan="10" style="text-align:center;">' + _commonMsg.noData + '</td></tr>');
            return;
        }
        var list = page.result || [];
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        for (var i = 0; i < list.length; i++) {
            var c = list[i];
            var html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + formmatUtilsJs.cardFormat(c.cutCardNo) + '</td>';
            html += '<td' + (c.stopYn === 'Y' ? ' style="background-color:#FFDCDC;"' : '') + '>' + _statLabel(c) + '</td>';
            html += '<td>' + (c.customerId ? '<a href="#" onclick="customerCardListJs.searchDetail(\'' + c.customerId + '\')">' + (c.customerName || c.customerId) + '</a>' : (c.customerName || '-')) + '</td>';
            html += '<td>' + (c.complexName || '-') + '</td>';
            html += '<td>' + (c.dong || '-') + '</td>';
            html += '<td>' + (c.ho || '-') + '</td>';
            html += '<td>' + (c.writer && c.writer.registrationDate ? dateUtilsJs.formatDate(new Date(c.writer.registrationDate), 'YYYY-MM-DD') : '-') + '</td>';
            html += '<td>' + (c.stopYn === 'Y' ? _stopReasonLabel(c) : '-') + '</td>';
            html += '<td>' + (c.stopDate ? dateUtilsJs.formatDate(new Date(c.stopDate), 'YYYY-MM-DD') : '-') + '</td>';
            html += '</tr>';
            $tbody.append(html);
        }
    }

    function _loadList() {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/card",
            data: { customerId: customerId },
            dataType: 'json',
            success: function (page) {
                _renderList((page && page.result) || []);
            },
            error: function (xhr) {
                parent.layerJs.fn_exception(xhr);
            }
        });
    }

    function _renderList(list) {
        _cardListCache = list;
        var $tbody = $("#cardListTbody").empty();
        if (list.length === 0) {
            $tbody.append('<tr><td colspan="5" style="text-align:center;">-</td></tr>');
        } else {
            for (var i = 0; i < list.length; i++) {
                var c = list[i];
                var regDt = c.writer && c.writer.registrationDate
                    ? formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(c.writer.registrationDate)), 'YYYY-MM-DD')
                    : '';
                var upDt = c.writer && c.writer.updateDate
                    ? formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(c.writer.updateDate)), 'YYYY-MM-DD')
                    : '';
                var html = '<tr>';
                html += '<td style="text-align:center;">' + (c.cutCardNo || '') + '</td>';
                html += '<td style="text-align:center;">' + _statLabel(c) + '</td>';
                html += '<td style="text-align:center;">' + regDt + '</td>';
                html += '<td style="text-align:center;">' + upDt + '</td>';
                html += '<td style="text-align:center;">';
                html += '<button class="btn btn-primary btn-xs" onclick="customerCardListJs.openEditPopup(\'' + c.cutCardNo + '\')">카드상세</button>';
                html += '</td>';
                html += '</tr>';
                $tbody.append(html);
            }
        }
        var activeCount = list.filter(function (c) { return c.stopYn !== 'Y'; }).length;
        $("#cardListCount").text(activeCount + ' / ' + MAX_CARD_COUNT);
        $("#btnCardListAdd").prop('disabled', activeCount >= MAX_CARD_COUNT);
    }

    // 회원카드 상세 팝업의 세대주명/세대정보는 별도 조회 없이 customer.html 자체 폼(#custName/#cxNum/#dong/#ho)의 현재 값을 그대로 표시.
    function _fillCustomerInfo() {
        $("#cardCustName").text($("#custName").val() || '-');
        var cx = $("#cxNum").val(), dong = $("#dong").val(), ho = $("#ho").val();
        var addr = (cx ? cx + '단지 ' : '') + (dong ? dong + '동 ' : '') + (ho ? ho + '호' : '');
        $("#cardCustAddr").text(addr || '-');
    }

    function _setAddMode() {
        $("#cardInfoTitle").text("회원카드추가");
        $("#btnCardNoChecker").attr("disabled", false);
        $("#cutCardNo1,#cutCardNo2,#cutCardNo3,#cutCardNo4").val('').prop('readonly', false);
        $("#stopRsnCd").val('');
        $("#stopRsnTxt").val('').hide();
        $("#stopDate").val('');
        $("#trStopStatus,#trStopDate").hide();
        $("#btnCardListSave").show();
        $("#btnCardStop,#btnCardReactivate").hide();
        $("#btnCardListCancel span").text("취소").show();
        _fillCustomerInfo();
    }

    // c.stopYn === 'Y' 이면 정지된 카드 - 정지사유는 읽기전용 텍스트로만 보여주고 "정지해제" 버튼만 노출.
    // 그 외엔 정상 카드 - 정지사유를 선택하게 하고("해당사항 없음" 기본값) "카드정지" 버튼만 노출.
    function _setDetailMode(c) {
        $("#cardInfoTitle").text("회원카드상세");
        $("#btnCardNoChecker").attr("disabled", true);
        $("#cutCardNo1,#cutCardNo2,#cutCardNo3,#cutCardNo4").prop('readonly', true);
        $("#trStopStatus,#trStopDate").show();
        $("#stopStatusLabel").text(_statLabel(c));
        $("#btnCardListSave").hide();
        $("#btnCardListCancel span").text("닫기").show();
        _fillCustomerInfo();

        if (c.stopYn === 'Y') {
            $("#stopRsnLabel").show().text(_stopReasonLabel(c));
            $("#stopRsnCd,#stopRsnTxt,#btnCardStop").hide();
            $("#btnCardReactivate").show();
        } else {
            $("#stopRsnLabel").hide().text('해당사항 없음');
            $("#stopRsnCd").val('').show();
            $("#stopRsnTxt").val('').hide();
            $("#btnCardStop").show();
            $("#btnCardReactivate").hide();
        }
    }

    function _resetCardForm() {
        editingCutCardNo = null;
        _setAddMode();
    }

    function _onClickAdd() {
        _resetCardForm();
        $("#Popup_CardInfo").modal();
    }

    function _onClickEdit(cutCardNo) {
        editingCutCardNo = cutCardNo;
        var c = _cardListCache.filter(function (x) { return x.cutCardNo === cutCardNo; })[0] || {};
        var no = c.cutCardNo || cutCardNo || '';
        $("#cutCardNo1").val(no.substr(0, 4));
        $("#cutCardNo2").val(no.substr(4, 4));
        $("#cutCardNo3").val(no.substr(8, 4));
        $("#cutCardNo4").val(no.substr(12, 4));
        $("#stopDate").val(c.stopDate ? dateUtilsJs.formatDate(new Date(c.stopDate), 'YYYY-MM-DD') : '');
        _setDetailMode(c);
        $("#Popup_CardInfo").modal();
    }

    function _onClickCheckCardNo() {
        var cutCardNo = ($("#cutCardNo1").val() + $("#cutCardNo2").val() + $("#cutCardNo3").val() + $("#cutCardNo4").val()).trim();
        if (!/^\d{16}$/.test(cutCardNo)) {
            swal("확인", _msg.cardDigit16, "warning");
            return;
        }
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/card/check/" + cutCardNo,
            dataType: 'json',
            success: function (res) {
                if (res && res.status === 'SUCCESS') {
                    toastr.success(res.message || "사용 가능한 카드번호입니다.");
                } else {
                    swal("확인", (res && res.message) || "이미 등록된 카드번호입니다.", "warning");
                }
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _onClickCancel() {
        $("#Popup_CardInfo").modal('hide');
    }

    function _onClickSave() {
        var cutCardNo = ($("#cutCardNo1").val() + $("#cutCardNo2").val() + $("#cutCardNo3").val() + $("#cutCardNo4").val()).trim();
        if (!/^\d{16}$/.test(cutCardNo)) {
            swal("확인", _msg.cardDigit16, "warning");
            return;
        }
        $.ajax({
            type: 'POST',
            url: _ctx + "/ws/customer/card",
            contentType: 'application/json',
            data: JSON.stringify({ cutCardNo: cutCardNo, customerId: customerId }),
            dataType: 'json',
            success: function (res) {
                if (res && res.status === 'SUCCESS') {
                    toastr.success("카드가 등록되었습니다.");
                    $("#Popup_CardInfo").modal('hide');
                    _loadList();
                } else {
                    swal("오류", (res && res.message) || "카드 등록에 실패했습니다.", "error");
                }
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _onClickStop() {
        var stopRsnCd = $("#stopRsnCd").val();
        var stopRsnTxt = $("#stopRsnTxt").val().trim();
        if (!stopRsnCd) {
            toastr.warning("정지 사유를 선택해주세요.");
            return;
        }
        if (stopRsnCd === 'ETC' && !stopRsnTxt) {
            toastr.warning("기타 사유를 입력해주세요.");
            return;
        }
        swal({
            title: "확인",
            text: "선택한 카드를 정지하시겠습니까?",
            type: "warning",
            showCancelButton: true,
            confirmButtonText: _msg.confirm,
            cancelButtonText: _msg.cancel
        }, function (isConfirm) {
            if (!isConfirm) return;
            $.ajax({
                type: 'PUT',
                url: _ctx + "/ws/customer/card/stop/" + encodeURIComponent(editingCutCardNo),
                contentType: 'application/json',
                data: JSON.stringify({ stopRsnCd: stopRsnCd, stopRsnTxt: stopRsnCd === 'ETC' ? stopRsnTxt : null }),
                dataType: 'json',
                success: function (res) {
                    if (res && res.status === 'SUCCESS') {
                        toastr.success("카드가 정지되었습니다.");
                        $("#Popup_CardInfo").modal('hide');
                        _loadList();
                    } else {
                        swal("오류", (res && res.message) || "처리에 실패했습니다.", "error");
                    }
                },
                error: function (xhr) { parent.layerJs.fn_exception(xhr); }
            });
        });
    }

    function _onClickReactivate() {
        swal({
            title: "확인",
            text: "선택한 카드의 정지를 해제하시겠습니까?",
            type: "warning",
            showCancelButton: true,
            confirmButtonText: _msg.confirm,
            cancelButtonText: _msg.cancel
        }, function (isConfirm) {
            if (!isConfirm) return;
            $.ajax({
                type: 'PUT',
                url: _ctx + "/ws/customer/card/reactivate/" + encodeURIComponent(editingCutCardNo),
                dataType: 'json',
                success: function (res) {
                    if (res && res.status === 'SUCCESS') {
                        toastr.success("카드 정지가 해제되었습니다.");
                        $("#Popup_CardInfo").modal('hide');
                        _loadList();
                    } else {
                        swal("오류", (res && res.message) || "처리에 실패했습니다.", "error");
                    }
                },
                error: function (xhr) { parent.layerJs.fn_exception(xhr); }
            });
        });
    }

    function _downloadExcel() {
        toastr.info("잠시만 기다려 주세요.", "엑셀 다운로드");
        var param = "?customerName=" + encodeURIComponent(data.searchCond.customerName || "");
        param += "&cutCardNo=" + encodeURIComponent(data.searchCond.cutCardNo || "");
        param += "&stopYn=" + encodeURIComponent(data.searchCond.stopYn || "");
        param += "&sortOrder=" + encodeURIComponent(data.searchCond.sortOrder || "");
        parent.layerJs.fn_download(_ctx + "/ws/customer/card/download/list" + param);
    }

    function _searchDetail(customerId) {
        let param = "?customerId=" + customerId;
        parent.layerJs.fn_moveMenu('20000203', _msg.customerMgmt, _ctx + "/customer/detail" + param, 'THIS', true);
    }

    return {
        init: _init,
        search: _search,
        searchDetail: _searchDetail,
        openEditPopup: _onClickEdit
    };
})();
