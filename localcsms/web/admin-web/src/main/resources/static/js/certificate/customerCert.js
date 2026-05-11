/**
 * 고객 인증서 목록 + 상세
 */
var customerCertJs = function () {
    "use strict";

    var data = {
        searchCond: {}
    };
    var result;

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
        $("#customerId, #eMaid").keypress(function (event) {
            if (event.keyCode === 13) {
                _searchOnClick();
            }
        });
        $("#btnBackToList").click(function () {
            _showList();
        });
    }

    function _resetOnClick() {
        $("#customerId").val("");
        $("#eMaid").val("");
        $("#status").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, customerCertJs.search);
        data.searchCond.customerId = $("#customerId").val().trim();
        data.searchCond.eMaid = $("#eMaid").val().trim();
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
        param += "&status=" + (data.searchCond.status || "");

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/certificate/customer/search" + param,
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
            html += '<td>' + (result[i].status || '-') + '</td>';
            html += '<td>' + (result[i].certValidFrom || '-') + '</td>';
            html += '<td>' + (result[i].certValidTo || '-') + '</td>';
            html += '<td>' + (result[i].writer && result[i].writer.registrationDate ? dateUtilsJs.formatDate(new Date(result[i].writer.registrationDate), 'YYYY-MM-DD HH:MM:SS') : '-') + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }
    }

    function _searchDetail(index) {
        var customerIdVal = result[index].customerId;
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/certificate/customer/detail/" + encodeURIComponent(customerIdVal),
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
        $("#detail_eMaid").text(jsonData.eMaid || '-');
        $("#detail_customerId").text(jsonData.customerId || '-');
        $("#detail_pcid").text(jsonData.pcid || '-');
        $("#detail_status").text(jsonData.status || '-');
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
