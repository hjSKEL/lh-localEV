/**
 * Default Tariff Assignment 관리 (EVSE 기본 요금표 + CS push)
 */
var tariffDefaultListJs = function () {
    "use strict";

    var data = { searchCond: {}, result: [] };

    function _init() {
        _initStatusSelect();
        _initEvent();
        _searchOnClick();
    }

    function _initStatusSelect() {
        var html  = '<option value="">'         + _msg.statusAll      + '</option>';
            html += '<option value="PENDING">'  + _msg.statusPending  + '</option>';
            html += '<option value="ACTIVE">'   + _msg.statusActive   + '</option>';
            html += '<option value="REPLACED">' + _msg.statusReplaced + '</option>';
            html += '<option value="CLEARED">'  + _msg.statusCleared  + '</option>';
            html += '<option value="REJECTED">' + _msg.statusRejected + '</option>';
        $("#statusCd").html(html);
    }

    function _initEvent() {
        $("#btnSearch").click(_searchOnClick);
        $("#statusCd").change(_searchOnClick);
        $("#btnReset").click(_searchReset);
        $("#btnAssign").click(_openAssign);
        $("#btnSave").click(_submitAssign);
    }

    function _searchReset() {
        $("#fCpId").val(""); $("#fCsId").val(""); $("#fEvseId").val(""); $("#statusCd").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, tariffDefaultListJs.search);
        data.searchCond = {
            assignType: 'DEFAULT_EVSE',
            cpId:     $("#fCpId").val().trim(),
            csId:     $("#fCsId").val().trim(),
            evseId:   $("#fEvseId").val(),
            statusCd: $("#statusCd").val()
        };
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="9">' + _commonMsg.searching + '</td></tr>');

        var paging = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&assignType=" + encodeURIComponent(data.searchCond.assignType);
        param += "&cpId="       + encodeURIComponent(data.searchCond.cpId || '');
        param += "&csId="       + encodeURIComponent(data.searchCond.csId || '');
        if (data.searchCond.evseId) param += "&evseId=" + encodeURIComponent(data.searchCond.evseId);
        param += "&statusCd="   + encodeURIComponent(data.searchCond.statusCd || '');

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
            $("#tBodyList").append('<tr style="text-align:center;"><td colspan="9">' + _commonMsg.noData + '</td></tr>');
            return;
        }
        data.result = jsonData.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        var html = '';
        for (var i = 0; i < data.result.length; ++i) {
            var row = data.result[i];
            var loc = (row.cpId || '-') + ' / ' + (row.csId || '-') + ' / ' + (row.evseId == null ? '-' : row.evseId);
            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + row.seq + '</td>';
            html += '<td>' + row.tariffId + '</td>';
            html += '<td>' + loc + '</td>';
            html += '<td>' + (row.tariffCurrency || '-') + '</td>';
            html += '<td>' + _formatDateTime(row.validFrom) + '</td>';
            html += '<td>' + _statusBadge(row.statusCd) + '</td>';
            html += '<td>' + _formatDateTime(row.csAckDt) + '</td>';
            html += '<td>' + _actionButtons(row) + '</td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _statusBadge(s) {
        switch (s) {
            case 'PENDING':  return '<span class="label label-warning">' + _msg.statusPending  + '</span>';
            case 'ACTIVE':   return '<span class="label label-primary">' + _msg.statusActive   + '</span>';
            case 'REPLACED': return '<span class="label label-default">' + _msg.statusReplaced + '</span>';
            case 'CLEARED':  return '<span class="label label-default">' + _msg.statusCleared  + '</span>';
            case 'REJECTED': return '<span class="label label-danger">'  + _msg.statusRejected + '</span>';
            default:         return '<span class="label label-default">' + (s || '-') + '</span>';
        }
    }
    function _actionButtons(row) {
        if (row.statusCd === 'PENDING' || row.statusCd === 'REJECTED') {
            return '<button type="button" class="btn btn-xs btn-primary" onclick="tariffDefaultListJs.push(' + row.seq + ')">' + _msg.btnPush + '</button>';
        }
        return '-';
    }

    function _openAssign() {
        $("#form_tariffId").val("");
        $("#form_cpId").val("");
        $("#form_csId").val("");
        $("#form_evseId").val("0");
        $("#Popup_Default_Assign").modal();
    }

    function _submitAssign() {
        var tariffId = $("#form_tariffId").val().trim();
        var cpId     = $("#form_cpId").val().trim();
        var csId     = $("#form_csId").val().trim();
        var evseId   = $("#form_evseId").val();
        if (!tariffId)            { toastr.warning(_msg.inputTariffId, _msg.defaultMgmt); return; }
        if (!cpId || !csId)       { toastr.warning(_msg.inputCpCs,     _msg.defaultMgmt); return; }
        $.ajax({
            type: 'POST',
            url: _ctx + "/ws/payment/tariff/assignment/default",
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({
                tariffId: tariffId, cpId: cpId, csId: csId,
                evseId: evseId === '' ? null : parseInt(evseId, 10)
            }),
            success: function (res) {
                if (res.status === 'SUCCESS') {
                    toastr.success(_msg.successAssign, _msg.defaultMgmt);
                    $("#Popup_Default_Assign").modal('hide');
                    _search();
                } else {
                    toastr.error(res.result || _msg.failAssign, _msg.defaultMgmt);
                }
            },
            error: function (xhr) {
                parent.layerJs.fn_exception(xhr);
                toastr.error(_msg.failAssign, _msg.defaultMgmt);
            }
        });
    }

    function _push(seq) {
        swal({
            title: _msg.defaultMgmt, text: _msg.confirmPush, type: 'warning',
            showCancelButton: true, confirmButtonColor: '#1c84c6',
            confirmButtonText: _msg.confirm, cancelButtonText: _msg.cancel, closeOnConfirm: true
        }, function () {
            $.ajax({
                type: 'PUT',
                url: _ctx + "/ws/payment/tariff/assignment/" + seq + "/push",
                dataType: 'json',
                success: function (res) {
                    if (res.status === 'SUCCESS') {
                        toastr.success(_msg.successPush, _msg.defaultMgmt);
                        _search();
                    } else {
                        toastr.error(res.result || _msg.failPush, _msg.defaultMgmt);
                    }
                },
                error: function (xhr) {
                    parent.layerJs.fn_exception(xhr);
                    toastr.error(_msg.failPush, _msg.defaultMgmt);
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
        push: _push
    };
}();
