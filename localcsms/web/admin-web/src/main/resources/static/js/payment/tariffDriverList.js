/**
 * Driver Tariff Assignment 관리
 */
var tariffDriverListJs = function () {
    "use strict";

    var data = { searchCond: {}, result: [] };

    function _init() {
        _initStatusSelect();
        _initEvent();
        _searchOnClick();
    }

    function _initStatusSelect() {
        var html  = '<option value="">'         + _msg.statusAll      + '</option>';
            html += '<option value="ACTIVE">'   + _msg.statusActive   + '</option>';
            html += '<option value="REPLACED">' + _msg.statusReplaced + '</option>';
            html += '<option value="CLEARED">'  + _msg.statusCleared  + '</option>';
        $("#statusCd").html(html);
    }

    function _initEvent() {
        $("#btnSearch").click(_searchOnClick);
        $("#statusCd").change(_searchOnClick);
        $("#btnReset").click(_searchReset);
        $("#sWord").keypress(function (e) { if (e.keyCode == 13) _searchOnClick(); });
        $("#btnAssign").click(_openAssign);
        $("#btnSave").click(_submitAssign);
    }

    function _searchReset() {
        $("#sWord").val("");
        $("#statusCd").val("");
        $("#searchType").val("IDTOKEN");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, tariffDriverListJs.search);
        var key = $("#sWord").val().trim();
        data.searchCond = {
            assignType: 'DRIVER_IDTOKEN',
            statusCd:   $("#statusCd").val(),
            idToken: "", tariffId: ""
        };
        switch ($("#searchType").val()) {
            case "IDTOKEN":   data.searchCond.idToken   = key; break;
            case "TARIFF_ID": data.searchCond.tariffId  = key; break;
        }
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="8">' + _commonMsg.searching + '</td></tr>');

        var paging = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&assignType="  + encodeURIComponent(data.searchCond.assignType);
        param += "&statusCd="    + encodeURIComponent(data.searchCond.statusCd || '');
        param += "&idToken="     + encodeURIComponent(data.searchCond.idToken  || '');
        param += "&tariffId="    + encodeURIComponent(data.searchCond.tariffId || '');

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/payment/tariff/assignment" + param,
            dataType: 'json',
            success: _display,
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _display(jsonData) {
        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();
        if (jsonData.criteria.totalItemCount == 0) {
            $("#tBodyList").append('<tr style="text-align:center;"><td colspan="8">' + _commonMsg.noData + '</td></tr>');
            return;
        }
        data.result = jsonData.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        var html = '';
        for (var i = 0; i < data.result.length; ++i) {
            var row = data.result[i];
            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + row.seq + '</td>';
            html += '<td>' + (row.idToken || '-') + '</td>';
            html += '<td>' + row.tariffId + '</td>';
            html += '<td>' + (row.tariffCurrency || '-') + '</td>';
            html += '<td>' + _formatDateTime(row.validFrom) + '</td>';
            html += '<td>' + _statusBadge(row.statusCd) + '</td>';
            html += '<td>' + _actionButtons(row) + '</td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _statusBadge(s) {
        switch (s) {
            case 'ACTIVE':   return '<span class="label label-primary">' + _msg.statusActive   + '</span>';
            case 'REPLACED': return '<span class="label label-default">' + _msg.statusReplaced + '</span>';
            case 'CLEARED':  return '<span class="label label-default">' + _msg.statusCleared  + '</span>';
            default:         return '<span class="label label-default">' + (s || '-') + '</span>';
        }
    }
    function _actionButtons(row) {
        if (row.statusCd === 'ACTIVE') {
            return '<button type="button" class="btn btn-xs btn-danger" onclick="tariffDriverListJs.clear(\'' + row.idToken + '\')">' + _msg.btnClear + '</button>';
        }
        return '-';
    }

    function _openAssign() {
        $("#form_idToken").val("");
        $("#form_tariffId").val("");
        $("#Popup_Driver_Assign").modal();
    }

    function _submitAssign() {
        var idToken  = $("#form_idToken").val().trim();
        var tariffId = $("#form_tariffId").val().trim();
        if (!idToken)  { toastr.warning(_msg.inputIdToken,  _msg.driverMgmt); return; }
        if (!tariffId) { toastr.warning(_msg.inputTariffId, _msg.driverMgmt); return; }
        $.ajax({
            type: 'POST',
            url: _ctx + "/ws/payment/tariff/assignment/driver",
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({ tariffId: tariffId, idToken: idToken }),
            success: function (res) {
                if (res.status === 'SUCCESS') {
                    toastr.success(_msg.successAssign, _msg.driverMgmt);
                    $("#Popup_Driver_Assign").modal('hide');
                    _search();
                } else {
                    toastr.error(res.result || _msg.failAssign, _msg.driverMgmt);
                }
            },
            error: function (xhr) {
                parent.layerJs.fn_exception(xhr);
                toastr.error(_msg.failAssign, _msg.driverMgmt);
            }
        });
    }

    function _clear(idToken) {
        swal({
            title: _msg.driverMgmt, text: _msg.confirmClear, type: 'warning',
            showCancelButton: true, confirmButtonColor: '#DD6B55',
            confirmButtonText: _msg.confirm, cancelButtonText: _msg.cancel, closeOnConfirm: true
        }, function () {
            $.ajax({
                type: 'PUT',
                url: _ctx + "/ws/payment/tariff/assignment/driver/" + encodeURIComponent(idToken) + "/clear",
                dataType: 'json',
                success: function (res) {
                    if (res.status === 'SUCCESS') {
                        toastr.success(_msg.successClear, _msg.driverMgmt);
                        _search();
                    } else {
                        toastr.error(res.result || _msg.failClear, _msg.driverMgmt);
                    }
                },
                error: function (xhr) {
                    parent.layerJs.fn_exception(xhr);
                    toastr.error(_msg.failClear, _msg.driverMgmt);
                }
            });
        });
    }

    function _formatDate(dt) {
        var y = dt.getFullYear();
        var m = ('0' + (dt.getMonth() + 1)).slice(-2);
        var d = ('0' + dt.getDate()).slice(-2);
        return y + '-' + m + '-' + d;
    }
    function _formatDateTime(val) {
        if (!val) return '-';
        var dt = new Date(val);
        var hh = ('0' + dt.getHours()).slice(-2);
        var mm = ('0' + dt.getMinutes()).slice(-2);
        return _formatDate(dt) + ' ' + hh + ':' + mm;
    }

    return {
        init: _init,
        search: _search,
        clear: _clear
    };
}();
