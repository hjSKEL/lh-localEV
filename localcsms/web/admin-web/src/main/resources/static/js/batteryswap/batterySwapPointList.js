/**
 * 배터리 교체 충전소 목록
 */
var batterySwapPointListJs = function () {
    "use strict";

    var data = {
        searchCond: {},
        result: []
    };

    function _init() {
        _initOperatingSelect();
        _initEvent();
        _searchOnClick();
    }

    function _initOperatingSelect() {
        var html = '<option value="">' + _msg.operatingAll + '</option>';
        html += '<option value="Y">' + _msg.operating + '</option>';
        html += '<option value="N">' + _msg.closed + '</option>';
        $("#operatingYn").html(html);
    }

    function _initEvent() {
        $("#btnSearch").click(_searchOnClick);
        $("#btnReset").click(_searchReset);
        $("#operatingYn").change(_searchOnClick);
        $("#sWord").keypress(function (e) { if (e.keyCode == 13) _searchOnClick(); });
        $("#btnCreate").click(function () {
            self.location = _ctx + "/batterySwap/point/detail";
        });
    }

    function _searchReset() {
        $("#operatingYn").val("");
        $("#searchType").val("CP_NAME");
        $("#sWord").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, batterySwapPointListJs.search);
        data.searchCond.cpId = "";
        data.searchCond.cpName = "";

        var key = $("#sWord").val().trim();
        switch ($("#searchType").val()) {
            case "CP_ID":   data.searchCond.cpId = key; break;
            case "CP_NAME": data.searchCond.cpName = key; break;
        }
        data.searchCond.operatingYn = $("#operatingYn").val();
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="9">' + _commonMsg.searching + '</td></tr>');

        var paging = pageInfoJs.getPaging();
        var p = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        p += "&cpId=" + encodeURIComponent(data.searchCond.cpId);
        p += "&cpName=" + encodeURIComponent(data.searchCond.cpName);
        p += "&operatingYn=" + encodeURIComponent(data.searchCond.operatingYn);

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/batterySwap/point" + p,
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
            $("#tBodyList").append('<tr style="text-align:center;"><td colspan="9">' + _commonMsg.noData + '</td></tr>');
            return;
        }

        data.result = json.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        var html = '';
        for (var i = 0, len = data.result.length; i < len; ++i) {
            var row = data.result[i];
            var cnt = (row.registeredStationCount !== undefined ? row.registeredStationCount : row.stationCount) || 0;
            var addr = (row.basicAddress ? row.basicAddress : '') + (row.detailAddress ? ' ' + row.detailAddress : '');
            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td><a href="#" onclick="batterySwapPointListJs.detail(\'' + row.cpId + '\')">' + row.cpId + '</a></td>';
            html += '<td>' + (row.cpName || '-') + '</td>';
            html += '<td style="text-align:right;">' + cnt + '</td>';
            html += '<td style="text-align:left;">' + (addr || '-') + '</td>';
            html += '<td>' + _formatCoord(row.latitude, row.longitude) + '</td>';
            html += '<td>' + _formatDate(row.openedFrom) + '</td>';
            html += '<td>' + _statusBadge(row.closedDate) + '</td>';
            html += '<td>'
                  + '<button type="button" class="btn btn-xs btn-info" onclick="batterySwapPointListJs.detail(\'' + row.cpId + '\')">'
                  + '<i class="fa fa-pencil"></i></button> '
                  + '<button type="button" class="btn btn-xs btn-danger" onclick="batterySwapPointListJs.remove(\'' + row.cpId + '\')">'
                  + '<i class="fa fa-trash"></i></button>'
                  + '</td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _statusBadge(closedDate) {
        if (closedDate) {
            return '<span class="label label-default">' + _msg.closed + ' (' + _formatDate(closedDate) + ')</span>';
        }
        return '<span class="label label-primary">' + _msg.operating + '</span>';
    }

    function _formatCoord(lat, lon) {
        if (lat == null || lon == null || lat === '' || lon === '') return '-';
        return Number(lat).toFixed(4) + ', ' + Number(lon).toFixed(4);
    }

    function _formatDate(val) {
        if (!val) return '-';
        var dt = new Date(val);
        var y = dt.getFullYear();
        var m = ('0' + (dt.getMonth() + 1)).slice(-2);
        var d = ('0' + dt.getDate()).slice(-2);
        return y + '-' + m + '-' + d;
    }

    function _detail(cpId) {
        self.location = _ctx + "/batterySwap/point/detail?cpId=" + encodeURIComponent(cpId);
    }

    function _remove(cpId) {
        swal({
            title: _msg.pointMgmt,
            text: _msg.confirmDelete,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.confirm,
            cancelButtonText: _msg.cancel,
            closeOnConfirm: true
        }, function () {
            $.ajax({
                type: 'DELETE',
                url: _ctx + "/ws/batterySwap/point/" + encodeURIComponent(cpId),
                dataType: 'json',
                success: function (json) {
                    if (json.status == 'SUCCESS') {
                        toastr.success(_msg.successDelete, _msg.pointMgmt);
                        _search();
                    } else {
                        toastr.error(json.result || _msg.failDelete, _msg.pointMgmt);
                    }
                },
                error: function (xhr) {
                    parent.layerJs.fn_exception(xhr);
                    toastr.error(_msg.failDelete, _msg.pointMgmt);
                }
            });
        });
    }

    return {
        init: _init,
        search: _search,
        detail: _detail,
        remove: _remove
    };
}();
