/**
 * 고객 인증서 목록 + 상세
 */
var customerCertJs = function () {
    "use strict";

    var data = {
        searchCond: {}
    };
    var result;
    var currentEmaid = null;

    function _init() {
        _initEvent();
        _searchOnClick();
    }

    function _initEvent() {
        $("#btnSearch").click(function () {
            _searchOnClick();
        });
        $("#btnReset").click(function () {
            _resetOnClick();
        });
        $("#customerId, #eMaid, #pcid").keypress(function (event) {
            if (event.keyCode === 13) {
                _searchOnClick();
            }
        });
        $("#btnBackToList").click(function () {
            _showList();
        });
        $("#btnChangeStatus").click(function () {
            _changeStatusOnClick();
        });
    }

    function _resetOnClick() {
        $("#customerId").val("");
        $("#eMaid").val("");
        $("#pcid").val("");
        $("#status").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, customerCertJs.search);
        data.searchCond.customerId = $("#customerId").val().trim();
        data.searchCond.eMaid = $("#eMaid").val().trim();
        data.searchCond.pcid = $("#pcid").val().trim();
        data.searchCond.status = $("#status").val();
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        var html = '<tr style="text-align:center;"><td colspan="8">' + _commonMsg.searching + '</td></tr>';
        $("#tBodyList").append(html);

        var paging = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&customerId=" + (data.searchCond.customerId || "");
        param += "&eMaid=" + (data.searchCond.eMaid || "");
        param += "&pcid=" + (data.searchCond.pcid || "");
        param += "&status=" + (data.searchCond.status || "");

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/cert/search" + param,
            dataType: 'json',
            success: function (jsonData) {
                _displayList(jsonData);
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayList(jsonData) {
        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();
        var html = '';
        if (jsonData.criteria.totalItemCount === 0) {
            html = '<tr style="text-align:center;"><td colspan="8">' + _commonMsg.noData + '</td></tr>';
            $("#tBodyList").append(html);
            return;
        }
        result = jsonData.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        for (var i = 0, len = result.length; i < len; ++i) {
            html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + (result[i].eMaid || '-') + '</td>';
            html += '<td><a href="#" onclick="customerCertJs.searchDetail(' + i + ')">' + (result[i].customerId || '-') + '</a></td>';
            html += '<td>' + (result[i].pcid || '-') + '</td>';
            html += '<td>' + _statusLabel(result[i].status) + '</td>';
            html += '<td>' + (result[i].certValidFrom || '-') + '</td>';
            html += '<td>' + (result[i].certValidTo || '-') + '</td>';
            html += '<td>' + (result[i].writer && result[i].writer.registrationDate ? dateUtilsJs.formatDate(new Date(result[i].writer.registrationDate), 'YYYY-MM-DD HH:MM:SS') : '-') + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }
    }

    /** 상태 코드(CERT0x) → 다국어 라벨. 레이아웃 pCodes(CERT00)에 로드된 공통코드로 조회(로케일 반영). */
    function _statusLabel(code) {
        if (!code) {
            return '-';
        }
        return parent.commonCodeJs.getCodeNameBySubCode(code) || code;
    }

    function _searchDetail(index) {
        var customerIdVal = result[index].customerId;
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/cert/detail/" + encodeURIComponent(customerIdVal),
            dataType: 'json',
            success: function (jsonData) {
                _displayDetail(jsonData);
                _showDetail();
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayDetail(jsonData) {
        if (!jsonData) return;
        currentEmaid = jsonData.eMaid || null;
        $("#detail_eMaid").text(jsonData.eMaid || '-');
        $("#detail_customerId").text(jsonData.customerId || '-');
        $("#detail_pcid").text(jsonData.pcid || '-');
        $("#detail_status").val(jsonData.status || '');
        $("#detail_serialNumber").text(jsonData.serialNumber || '-');
        $("#detail_xsdMsgDefNamespace").text(jsonData.xsdMsgDefNamespace || '-');
        $("#detail_subjectDn").text(jsonData.subjectDn || '-');
        $("#detail_certValidFrom").text(jsonData.certValidFrom || '-');
        $("#detail_certValidTo").text(jsonData.certValidTo || '-');
        $("#detail_ocspResponderURL").text(jsonData.ocspResponderURL || '-');
        if (jsonData.writer) {
            $("#detail_regDate").text(jsonData.writer.registrationDate ? dateUtilsJs.formatDate(new Date(jsonData.writer.registrationDate), 'YYYY-MM-DD HH:MM:SS') : '-');
            $("#detail_updDate").text(jsonData.writer.updateDate ? dateUtilsJs.formatDate(new Date(jsonData.writer.updateDate), 'YYYY-MM-DD HH:MM:SS') : '-');
        } else {
            $("#detail_regDate").text('-');
            $("#detail_updDate").text('-');
        }
    }

    function _changeStatusOnClick() {
        if (!currentEmaid) {
            return;
        }
        var newStatus = $("#detail_status").val();
        $.ajax({
            type: 'PUT',
            url: _ctx + "/ws/customer/cert/status/" + encodeURIComponent(currentEmaid) + "?status=" + encodeURIComponent(newStatus),
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData && jsonData.status === 'SUCCESS') {
                    toastr.success(_commonMsg.successModify);
                    _search();
                } else {
                    toastr.error(_commonMsg.failModify + (jsonData && jsonData.result ? ' : ' + jsonData.result : ''));
                }
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _showDetail() {
        $("#listSection").hide();
        $("#detailSection").show();
    }

    function _showList() {
        $("#detailSection").hide();
        $("#listSection").show();
    }

    return {
        init: _init,
        search: _search,
        searchDetail: _searchDetail
    };
}();
