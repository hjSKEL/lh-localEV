/**
 * 배터리 교환 충전기 목록 (전체)
 */
var batterySwapStationListJs = function () {
    "use strict";

    var data = {
        searchCond: {}
    };

    function _init() {
        _initStatusSelect();
        _initEnabledSelect();
        _initEvent();
        _searchOnClick();
    }

    function _initStatusSelect() {
        var codes = parent.commonCodeJs.getCodesByParentCode("BSOS00") || [];
        var html = '<option value="">' + _msg.statusAll + '</option>';
        if (codes.length === 0) {
            html += '<option value="Operative">Operative</option>'
                  + '<option value="Inoperative">Inoperative</option>'
                  + '<option value="Maintenance">Maintenance</option>';
        } else {
            for (var i = 0; i < codes.length; ++i) {
                html += '<option value="' + codes[i].code + '">' + codes[i].codeName + '</option>';
            }
        }
        $("#operationalStatus").html(html);
    }

    function _initEnabledSelect() {
        var html = '<option value="">' + _msg.enabledAll + '</option>';
        html += '<option value="Y">Y</option>';
        html += '<option value="N">N</option>';
        $("#enabled").html(html);
    }

    function _initEvent() {
        $("#btnSearch").click(_searchOnClick);
        $("#btnReset").click(_searchReset);
        $("#operationalStatus,#enabled").change(_searchOnClick);
        $("#sWord").keypress(function (e) { if (e.keyCode == 13) _searchOnClick(); });
    }

    function _searchReset() {
        $("#operationalStatus").val("");
        $("#enabled").val("");
        $("#searchType").val("CP_NAME");
        $("#sWord").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, batterySwapStationListJs.search);
        data.searchCond.cpId = "";
        data.searchCond.csId = "";
        data.searchCond.cpName = "";

        var key = $("#sWord").val().trim();
        switch ($("#searchType").val()) {
            case "CP_NAME":
                data.searchCond.cpName = key;
                break;
            case "CHARGER_ID":
                if (key.length > 0) {
                    if (key.length !== 8) {
                        toastr.warning(_msg.chargerIdDigit8, _msg.stationMgmt);
                        return;
                    }
                    data.searchCond.cpId = key.substring(0, 6);
                    data.searchCond.csId = key.substring(6);
                }
                break;
        }
        data.searchCond.operationalStatus = $("#operationalStatus").val();
        data.searchCond.enabled = $("#enabled").val();
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="11">' + _commonMsg.searching + '</td></tr>');

        var paging = pageInfoJs.getPaging();
        var p = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        p += "&cpId=" + encodeURIComponent(data.searchCond.cpId);
        p += "&csId=" + encodeURIComponent(data.searchCond.csId);
        p += "&cpName=" + encodeURIComponent(data.searchCond.cpName);
        p += "&operationalStatus=" + encodeURIComponent(data.searchCond.operationalStatus);
        p += "&enabled=" + encodeURIComponent(data.searchCond.enabled);

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/batterySwap/station" + p,
            dataType: 'json',
            success: _display,
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _display(json) {
        pageInfoJs.setTotalCount(json.criteria.totalItemCount);
        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', json.criteria.totalItemCount));
        $("#tBodyList").empty();

        if (json.criteria.totalItemCount == 0) {
            $("#tBodyList").append('<tr style="text-align:center;"><td colspan="11">' + _commonMsg.noData + '</td></tr>');
            return;
        }

        var result = json.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        var html = '';
        for (var i = 0, len = result.length; i < len; ++i) {
            var row = result[i];
            var avail = (row.totalSlotCount || 0) - (row.reservedSlotCount || 0);
            var slotTxt = avail + '/' + (row.totalSlotCount || 0);
            if (avail <= 0 && (row.totalSlotCount || 0) > 0) {
                slotTxt = '<span style="color:#dd6b55;">' + slotTxt + '</span>';
            }
            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + row.cpId + '</td>';
            html += '<td><a href="#" onclick="batterySwapStationListJs.openPoint(\'' + row.cpId + '\')">' + (row.cpName || '-') + '</a></td>';
            html += '<td>' + row.csId + '</td>';
            html += '<td>' + _operStatBadge(row.operationalStatus) + '</td>';
            html += '<td>' + (row.enabled === 'Y' ? '<i class="fa fa-check text-navy"></i>' : '<i class="fa fa-times text-muted"></i>') + '</td>';
            html += '<td>' + slotTxt + '</td>';
            html += '<td>' + (row.supportedBatteryModel || '-') + '</td>';
            html += '<td style="text-align:right;">' + (row.minSoHThreshold != null ? row.minSoHThreshold : '-') + '</td>';
            html += '<td style="text-align:right;">' + (row.minSoCForDispatch != null ? row.minSoCForDispatch : '-') + '</td>';
            html += '<td style="text-align:right;">' + (row.swapTimeoutSec != null ? row.swapTimeoutSec : '-') + '</td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _operStatBadge(code) {
        switch (code) {
            case 'Operative':   return '<span class="label label-primary">Operative</span>';
            case 'Maintenance': return '<span class="label label-warning">Maintenance</span>';
            case 'Inoperative': return '<span class="label label-default">Inoperative</span>';
            default: return code || '-';
        }
    }

    function _openPoint(cpId) {
        self.location = _ctx + "/batterySwap/point/detail?cpId=" + encodeURIComponent(cpId);
    }

    return {
        init: _init,
        search: _search,
        openPoint: _openPoint
    };
}();
