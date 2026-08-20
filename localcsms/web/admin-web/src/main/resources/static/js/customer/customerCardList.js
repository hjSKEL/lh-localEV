/**
 * 회원카드 목록 (세대당 최대 5장) — TB_CUCA001 / CustomerCardResource.
 *cardListTbody
 * 회원 상세 페이지 (customer.html) 의 "회원카드목록" 섹션에 바인딩.
 * REST: /ws/customer/card* (기존 customerCard.js/customerCardList.js 화면과 동일 엔드포인트 재사용)
 */
var customerCardListJs = (function () {

    var customerId = null;
    var MAX_CARD_COUNT = 5;
    var STAT_LABEL = {
        MEML01: '정상',
        MEML02: '분실',
        MEML03: '삭제/불량'
    };

    var data = { searchCond: {} };
    var editingCutCardNo = null;  // null = 카드 추가 모드, 값 있음 = 카드 수정 모드

    function _init(custId) {
        customerId = custId;
        if (customerId) {
            // 회원 상세 페이지에 임베딩되는 카드 목록
            $("#btnCardListAdd").off("click").on("click", _onClickAdd);
            $("#btnCardListSave").off("click").on("click", _onClickSave);
            $("#btnCardListCancel").off("click").on("click", _onClickCancel);
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

        var statList = parent.commonCodeJs.getCodesByParentCode('MEML00');
        $("#status").append('<option value="" selected>' + _msg.cardStatusAll + '</option>');
        for (var i = 0; i < statList.length; i++) {
            $("#status").append('<option value="' + statList[i].code + '">' + statList[i].codeName + '</option>');
        }
        _searchOnClick();
    }

    function _searchResetClick() {
        $("#sWord").val("");
        $("#status").val("");
        $("#searchType").val("ID");
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
        data.searchCond.custStatCode = $("#status").val();
        $("#sWord").val(searchKey);
        _search();
    }

    function _search() {
        var paging = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&customerName=" + encodeURIComponent(data.searchCond.customerName || "");
        param += "&cutCardNo=" + encodeURIComponent(data.searchCond.cutCardNo || "");
        param += "&custStatCode=" + encodeURIComponent(data.searchCond.custStatCode || "");
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
            html += '<td>' + (STAT_LABEL[c.custStatCode] || c.custStatCode || '') + '</td>';
            html += '<td>' + (c.customerId ? '<a href="#" onclick="customerCardListJs.searchDetail(\'' + c.customerId + '\')">' + (c.customerName || c.customerId) + '</a>' : (c.customerName || '-')) + '</td>';
            html += '<td>' + (c.complexName || '-') + '</td>';
            html += '<td>' + (c.dong || '-') + '</td>';
            html += '<td>' + (c.ho || '-') + '</td>';
            html += '<td>' + (c.writer && c.writer.registrationDate ? dateUtilsJs.formatDate(new Date(c.writer.registrationDate), 'YYYY-MM-DD') : '-') + '</td>';
            html += '<td>' + (c.lossDate ? dateUtilsJs.formatDate(new Date(c.lossDate), 'YYYY-MM-DD') : '-') + '</td>';
            html += '<td>' + (c.delDate ? dateUtilsJs.formatDate(new Date(c.delDate), 'YYYY-MM-DD') : '-') + '</td>';
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
        var $tbody = $("#cardListTbody").empty();
        if (list.length === 0) {
            $tbody.append('<tr><td colspan="4" style="text-align:center;">-</td></tr>');
        } else {
            for (var i = 0; i < list.length; i++) {
                var c = list[i];
                var regDt = c.writer && c.writer.registrationDate
                    ? formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(c.writer.registrationDate)), 'YYYY-MM-DD')
                    : '';
                var upDt = c.writer && c.writer.updateDate
                    ? formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(c.writer.updateDate)), 'YYYY-MM-DD')
                    : '';
                var isActive = c.custStatCode === 'MEML01';
                var html = '<tr>';
                html += '<td style="text-align:center;">' + (c.cutCardNo || '') + '</td>';
                html += '<td style="text-align:center;">' + (STAT_LABEL[c.custStatCode] || c.custStatCode || '') + '</td>';
                html += '<td style="text-align:center;">' + regDt + '</td>';
                html += '<td style="text-align:center;">' + upDt + '</td>';
                html += '<td style="text-align:center;">';
                if (isActive) {
                    html += '<button class="btn btn-primary btn-xs" onclick="customerCardListJs.openEditPopup(\'' + c.cutCardNo + '\',\'' + c.custStatCode + '\')">카드상세</button>';
                }
                html += '</td>';
                html += '</tr>';
                $tbody.append(html);
            }
        }
        var activeCount = list.filter(function (c) { return c.custStatCode === 'MEML01'; }).length;
        $("#cardListCount").text(activeCount + ' / ' + MAX_CARD_COUNT);
        $("#btnCardListAdd").prop('disabled', activeCount >= MAX_CARD_COUNT);
    }

    function _setAddMode() {
        $("#cardInfoTitle").text("회원카드추가");
        $("#btnCardNoChecker").attr("disabled", false);
        $("#cutCardNo1,#cutCardNo2,#cutCardNo3,#cutCardNo4").val('').prop('readonly', false);
        $("#cardStatTh,#cardStatTd").hide();
        $("#trStopYn,#trLossYn,#trDelYn").hide();
        $("#btnCardListSave span").text("등록");
        $("#btnCardListCancel span").text("취소").show();
    }

    function _setDetailMode() {
        $("#cardInfoTitle").text("회원카드상세");
        $("#btnCardNoChecker").attr("disabled", true);
        $("#cutCardNo1,#cutCardNo2,#cutCardNo3,#cutCardNo4").prop('readonly', true);
        $("#cardStatTh,#cardStatTd").show();
        $("#trStopYn,#trLossYn,#trDelYn").show();
        $("#btnCardListSave span").text("수정");
        $("#btnCardListCancel span").text("닫기").show();
    }

    function _resetCardForm() {
        editingCutCardNo = null;
        _setAddMode();
    }

    function _onClickAdd() {
        _resetCardForm();
        $("#Popup_CardInfo").modal();
    }

    function _onClickEdit(cutCardNo, custStatCode) {
        editingCutCardNo = cutCardNo;
        _setDetailMode();
        $("#Popup_CardInfo").modal();
    }

    function _onClickCancel() {
        $("#Popup_CardInfo").modal('hide');
    }

    function _onClickSave() {
        if (editingCutCardNo) {
            _saveStatusChange(editingCutCardNo, $("#custStatCodeTd").val());
            return;
        }
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

    function _saveStatusChange(cutCardNo, custStatCode) {
        var confirmMsg = custStatCode === 'MEML02' ? _msg.confirmLost
            : custStatCode === 'MEML03' ? _msg.confirmDeleteDefect
            : "정상처리 하시겠습니까?";
        swal({
            title: "확인",
            text: confirmMsg,
            type: "warning",
            showCancelButton: true,
            confirmButtonText: _msg.confirm,
            cancelButtonText: _msg.cancel
        }, function (isConfirm) {
            if (!isConfirm) return;
            $.ajax({
                type: 'PUT',
                url: _ctx + "/ws/customer/card/changeCustStatCode/" + encodeURIComponent(cutCardNo) + "/status/" + custStatCode,
                dataType: 'json',
                success: function (res) {
                    if (res && res.status === 'SUCCESS') {
                        toastr.success("카드 상태가 변경되었습니다.");
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
