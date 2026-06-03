/**
 * 충전소 인증서 목록 + 상세
 */
var csCertJs = function () {
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
        $("#csId").keypress(function (event) {
            if (event.keyCode === 13) {
                _searchOnClick();
            }
        });
        $("#btnBackToList").click(function () {
            _showList();
        });
    }

    function _resetOnClick() {
        $("#csId").val("");
        $("#certStatus").val("");
        $("#caType").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, csCertJs.search);
        data.searchCond.csId = $("#csId").val().trim();
        data.searchCond.certStatus = $("#certStatus").val();
        data.searchCond.caType = $("#caType").val();
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        var html = '<tr style="text-align:center;"><td colspan="7">' + _commonMsg.searching + '</td></tr>';
        $("#tBodyList").append(html);

        var paging = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&csId=" + (data.searchCond.csId || "");
        param += "&certStatus=" + (data.searchCond.certStatus || "");
        param += "&caType=" + (data.searchCond.caType || "");

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/cert/search" + param,
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
            html = '<tr style="text-align:center;"><td colspan="7">' + _commonMsg.noData + '</td></tr>';
            $("#tBodyList").append(html);
            return;
        }
        result = jsonData.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        for (var i = 0, len = result.length; i < len; ++i) {
            html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td><a href="#" onclick="csCertJs.searchDetail(' + i + ')">' + (result[i].csId || '-') + '</a></td>';
            html += '<td>' + (result[i].CommonName || '-') + '</td>';
            html += '<td>' + _caTypeLabel(result[i].caType) + '</td>';
            html += '<td>' + (result[i].certStatus || '-') + '</td>';
            html += '<td>' + (result[i].expiredDate ? dateUtilsJs.formatDate(new Date(result[i].expiredDate), 'YYYY-MM-DD HH:MM:SS') : '-') + '</td>';
            html += '<td>' + (result[i].writer && result[i].writer.registrationDate ? dateUtilsJs.formatDate(new Date(result[i].writer.registrationDate), 'YYYY-MM-DD HH:MM:SS') : '-') + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }
    }

    function _caTypeLabel(caType) {
        if (caType === 'R') return 'ROOT';
        if (caType === 'S') return 'SubCA';
        return caType || '-';
    }

    function _searchDetail(index) {
        var certIdVal = result[index].certId;
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/cert/detail/" + encodeURIComponent(certIdVal),
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
        $("#detail_certId").text(jsonData.certId || '-');
        $("#detail_csId").text(jsonData.csId || '-');
        $("#detail_commonName").text(jsonData.CommonName || '-');
        $("#detail_caType").text(_caTypeLabel(jsonData.caType));
        $("#detail_certStatus").text(jsonData.certStatus || '-');
        $("#detail_expiredDate").text(jsonData.expiredDate ? dateUtilsJs.formatDate(new Date(jsonData.expiredDate), 'YYYY-MM-DD HH:MM:SS') : '-');
        $("#detail_fileLocation").text(jsonData.fileLocation || '-');
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
