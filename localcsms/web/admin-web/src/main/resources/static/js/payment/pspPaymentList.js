/**
 * PSP 결제 내역 관리
 */
var pspPaymentListJs = function () {
    "use strict";

    var data = { searchCond: {}, result: [] };
    var currentDetail = null;

    function _init() {
        _initStatusSelect();
        _initProviderSelect();
        _initEvent();
        _searchOnClick();
    }

    function _initStatusSelect() {
        var html = '';
        html += '<option value="">' + _msg.statusAll + '</option>';
        html += '<option value="AUTHORIZED">' + _msg.statusAuthorized + '</option>';
        html += '<option value="STARTED">' + _msg.statusStarted + '</option>';
        html += '<option value="SETTLED">' + _msg.statusSettled + '</option>';
        html += '<option value="CANCELED">' + _msg.statusCanceled + '</option>';
        $("#statusCd").html(html);
    }

    function _initProviderSelect() {
        var providers = ['', 'TOSS', 'NICE', 'KICC', 'INICIS', 'KCP', 'OTHER'];
        var html = '<option value="">' + _msg.providerAll + '</option>';
        for (var i = 1; i < providers.length; i++) {
            html += '<option value="' + providers[i] + '">' + providers[i] + '</option>';
        }
        $("#pspProvider").html(html);
    }

    function _initEvent() {
        $("#btnSearch").click(_searchOnClick);
        $("#statusCd, #pspProvider").change(_searchOnClick);
        $("#btnReset").click(_searchResetClick);
        $("#sWord").keypress(function (event) { if (event.keyCode == 13) _searchOnClick(); });
        $("#btnRegister").click(_openRegisterModal);
        $("#btnSave").click(_submitRegister);
        $("#btnSettleSave").click(_submitSettle);
        $("#btnCancelSave").click(_submitCancel);
    }

    function _searchResetClick() {
        $("#sWord").val("");
        $("#statusCd").val("");
        $("#pspProvider").val("");
        $("#fromDate").val("");
        $("#toDate").val("");
        $("#searchType").val("PSP_REF");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, pspPaymentListJs.search);
        data.searchCond = {
            pspRef: "", rechargingId: "", cardLast4: "", csId: "",
            statusCd: $("#statusCd").val(),
            pspProvider: $("#pspProvider").val(),
            fromDate: _toDateTime($("#fromDate").val(), '00:00:00'),
            toDate:   _toDateTime($("#toDate").val(),   '23:59:59')
        };
        var key = $("#sWord").val().trim();
        switch ($("#searchType").val()) {
            case "PSP_REF":    data.searchCond.pspRef       = key; break;
            case "RC_ID":      data.searchCond.rechargingId = key; break;
            case "CARD_LAST4": data.searchCond.cardLast4    = key; break;
            case "CS_ID":      data.searchCond.csId         = key; break;
        }
        $("#sWord").val(key);
        _search();
    }

    function _toDateTime(d, suffix) {
        if (!d) return "";
        return d + ' ' + suffix;
    }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="12">' + _commonMsg.searching + '</td></tr>');

        var paging = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&pspRef="       + encodeURIComponent(data.searchCond.pspRef);
        param += "&rechargingId=" + encodeURIComponent(data.searchCond.rechargingId);
        param += "&cardLast4="    + encodeURIComponent(data.searchCond.cardLast4);
        param += "&csId="         + encodeURIComponent(data.searchCond.csId);
        param += "&statusCd="     + encodeURIComponent(data.searchCond.statusCd);
        param += "&pspProvider="  + encodeURIComponent(data.searchCond.pspProvider);
        if (data.searchCond.fromDate) param += "&fromDate=" + encodeURIComponent(data.searchCond.fromDate);
        if (data.searchCond.toDate)   param += "&toDate="   + encodeURIComponent(data.searchCond.toDate);

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/payment/psp" + param,
            dataType: 'json',
            success: _display,
            error: function (xhRequest) { parent.layerJs.fn_exception(xhRequest); }
        });
    }

    function _display(jsonData) {
        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();

        if (jsonData.criteria.totalItemCount == 0) {
            $("#tBodyList").append('<tr style="text-align:center;"><td colspan="12">' + _commonMsg.noData + '</td></tr>');
            return;
        }

        data.result = jsonData.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        var html = '';
        for (var i = 0, length = data.result.length; i < length; ++i) {
            var row = data.result[i];
            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + row.pspRef + '</td>';
            html += '<td>' + _statusBadge(row.statusCd) + '</td>';
            html += '<td>' + (row.pspProvider || '-') + '</td>';
            html += '<td>' + (row.cpId || '-') + '</td>';
            html += '<td>' + (row.csId || '-') + '</td>';
            html += '<td>' + (row.cardLast4 || '-') + '</td>';
            html += '<td style="text-align:right;">' + _formatAmount(row.authAmount, row.currency) + '</td>';
            html += '<td style="text-align:right;">' + _formatAmount(row.settledAmount, row.currency) + '</td>';
            html += '<td>' + _formatDateTime(row.authDate) + '</td>';
            html += '<td>' + (row.rechargingId || '-') + '</td>';
            html += '<td>' + _actionButtons(i, row) + '</td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _statusBadge(s) {
        switch (s) {
            case 'AUTHORIZED': return '<span class="label label-info">'    + _msg.statusAuthorized + '</span>';
            case 'STARTED':    return '<span class="label label-warning">' + _msg.statusStarted    + '</span>';
            case 'SETTLED':    return '<span class="label label-primary">' + _msg.statusSettled    + '</span>';
            case 'CANCELED':   return '<span class="label label-default">' + _msg.statusCanceled   + '</span>';
            default:           return '<span class="label label-default">' + (s || '-') + '</span>';
        }
    }

    function _formatAmount(v, currency) {
        if (v == null) return '-';
        var n = Number(v);
        if (isNaN(n)) return '-';
        return n.toLocaleString() + ' ' + (currency || '');
    }

    function _actionButtons(index, row) {
        var html = '';
        html += '<button type="button" class="btn btn-xs btn-info" onclick="pspPaymentListJs.view(' + index + ')">' + _msg.btnView + '</button> ';
        if (row.statusCd === 'AUTHORIZED' || row.statusCd === 'STARTED') {
            html += '<button type="button" class="btn btn-xs btn-primary" onclick="pspPaymentListJs.settle(' + index + ')">' + _msg.btnSettle + '</button> ';
            html += '<button type="button" class="btn btn-xs btn-danger"  onclick="pspPaymentListJs.cancelRow(' + index + ')">' + _msg.btnCancel + '</button>';
        }
        return html;
    }

    /* ----- 등록 ----- */
    function _openRegisterModal() {
        $("#form_pspRef").val("");
        $("#form_pspProvider").val("TOSS");
        $("#form_cpId").val("");
        $("#form_csId").val("");
        $("#form_evseId").val("");
        $("#form_cardBin").val("");
        $("#form_cardLast4").val("");
        $("#form_cardholderNm").val("");
        $("#form_authAmount").val("");
        $("#form_maxCost").val("");
        $("#form_maxEnergy").val("");
        $("#form_currency").val("KRW");
        $("#form_extraInfo").val("");

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/payment/psp/nextRef",
            dataType: 'json',
            success: function (res) {
                if (res.status === 'SUCCESS') $("#form_pspRef").val(res.result);
            }
        });
        $("#Popup_Psp_Register").modal();
    }

    function _submitRegister() {
        var cpId = $("#form_cpId").val().trim();
        var csId = $("#form_csId").val().trim();
        var auth = $("#form_authAmount").val();
        if (!cpId || !csId) { toastr.warning(_msg.inputCpCs,       _msg.pspMgmt); return; }
        if (!auth)          { toastr.warning(_msg.inputAuthAmount, _msg.pspMgmt); return; }

        var body = {
            pspRef:       $("#form_pspRef").val().trim() || null,
            pspProvider:  $("#form_pspProvider").val(),
            cpId:         cpId,
            csId:         csId,
            evseId:       $("#form_evseId").val() ? parseInt($("#form_evseId").val(), 10) : null,
            cardBin:      $("#form_cardBin").val().trim() || null,
            cardLast4:    $("#form_cardLast4").val().trim() || null,
            cardholderNm: $("#form_cardholderNm").val().trim() || null,
            authAmount:   parseFloat(auth),
            maxCost:      $("#form_maxCost").val() ? parseFloat($("#form_maxCost").val()) : null,
            maxEnergy:    $("#form_maxEnergy").val() ? parseFloat($("#form_maxEnergy").val()) : null,
            currency:     $("#form_currency").val().trim() || 'KRW',
            extraInfo:    $("#form_extraInfo").val().trim() || null
        };

        $.ajax({
            type: 'POST',
            url: _ctx + "/ws/payment/psp",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(body),
            success: function (res) {
                if (res.status == 'SUCCESS') {
                    toastr.success(_msg.successRegister, _msg.pspMgmt);
                    $("#Popup_Psp_Register").modal('hide');
                    _search();
                } else {
                    toastr.error(res.result || _msg.failRegister, _msg.pspMgmt);
                }
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
                toastr.error(_msg.failRegister, _msg.pspMgmt);
            }
        });
    }

    /* ----- 정산 ----- */
    function _settle(index) {
        var row = data.result[index];
        if (!row) return;
        $("#settle_pspRef").text(row.pspRef);
        $("#settle_authAmount").text(_formatAmount(row.authAmount, row.currency));
        $("#settle_settledAmount").val(row.authAmount || '');
        $("#settle_receiptUrl").val(row.receiptUrl || '');
        $("#settle_receiptId").val(row.receiptId || '');
        currentDetail = row;
        $("#Popup_Psp_Settle").modal();
    }

    function _submitSettle() {
        if (!currentDetail) return;
        var amount = $("#settle_settledAmount").val();
        if (!amount) { toastr.warning(_msg.inputSettledAmount, _msg.pspMgmt); return; }
        var body = {
            settledAmount: parseFloat(amount),
            receiptUrl:    $("#settle_receiptUrl").val().trim() || null,
            receiptId:     $("#settle_receiptId").val().trim() || null
        };
        $.ajax({
            type: 'PUT',
            url: _ctx + "/ws/payment/psp/" + encodeURIComponent(currentDetail.pspRef) + "/settle",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(body),
            success: function (res) {
                if (res.status == 'SUCCESS') {
                    toastr.success(_msg.successSettle, _msg.pspMgmt);
                    $("#Popup_Psp_Settle").modal('hide');
                    _search();
                } else {
                    toastr.error(res.result || _msg.failSettle, _msg.pspMgmt);
                }
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
                toastr.error(_msg.failSettle, _msg.pspMgmt);
            }
        });
    }

    /* ----- 취소 ----- */
    function _cancelRow(index) {
        var row = data.result[index];
        if (!row) return;
        $("#cancel_pspRef").text(row.pspRef);
        $("#cancel_reasonCd").val('USER_REQUEST');
        $("#cancel_remark").val('');
        currentDetail = row;
        $("#Popup_Psp_Cancel").modal();
    }

    function _submitCancel() {
        if (!currentDetail) return;
        var body = {
            reasonCd: $("#cancel_reasonCd").val().trim() || null,
            remark:   $("#cancel_remark").val().trim() || null
        };
        $.ajax({
            type: 'PUT',
            url: _ctx + "/ws/payment/psp/" + encodeURIComponent(currentDetail.pspRef) + "/cancel",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(body),
            success: function (res) {
                if (res.status == 'SUCCESS') {
                    toastr.success(_msg.successCancel, _msg.pspMgmt);
                    $("#Popup_Psp_Cancel").modal('hide');
                    _search();
                } else {
                    toastr.error(res.result || _msg.failCancel, _msg.pspMgmt);
                }
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
                toastr.error(_msg.failCancel, _msg.pspMgmt);
            }
        });
    }

    /* ----- 상세 + 이력 ----- */
    function _view(index) {
        var row = data.result[index];
        if (!row) return;
        currentDetail = row;
        _renderDetail(row);
        _loadHistory(row.pspRef);
        $("#Popup_Psp_Detail").modal();
    }

    function _renderDetail(row) {
        var rows = [
            ['PSP Ref',         row.pspRef],
            ['Status',          _statusBadge(row.statusCd)],
            ['Provider',        row.pspProvider || '-'],
            ['CP / CS / EVSE',  (row.cpId || '-') + ' / ' + (row.csId || '-') + ' / ' + (row.evseId == null ? '-' : row.evseId)],
            ['Card',            (row.cardBin || '-') + ' **** ' + (row.cardLast4 || '-') + ' (' + (row.cardholderNm || '-') + ')'],
            ['Auth Amount',     _formatAmount(row.authAmount,    row.currency)],
            ['Max Cost',        _formatAmount(row.maxCost,       row.currency)],
            ['Max Energy',      row.maxEnergy == null ? '-' : (Number(row.maxEnergy).toLocaleString() + ' Wh')],
            ['Settled Amount',  _formatAmount(row.settledAmount, row.currency)],
            ['Recharging ID',   row.rechargingId || '-'],
            ['Auth Date',       _formatDateTime(row.authDate)],
            ['Started Date',    _formatDateTime(row.startedDate)],
            ['Settled Date',    _formatDateTime(row.settledDate)],
            ['Canceled Date',   _formatDateTime(row.canceledDate)],
            ['Reason Code',     row.reasonCd || '-'],
            ['Receipt URL',     row.receiptUrl ? '<a href="' + row.receiptUrl + '" target="_blank">' + row.receiptUrl + '</a>' : '-'],
            ['Receipt ID',      row.receiptId || '-'],
            ['Extra Info',      row.extraInfo ? '<code>' + _escape(row.extraInfo) + '</code>' : '-']
        ];
        var html = '';
        for (var i = 0; i < rows.length; i++) {
            html += '<tr><th style="width:25%;">' + rows[i][0] + '</th><td>' + rows[i][1] + '</td></tr>';
        }
        $("#detailBody").html(html);
    }

    function _loadHistory(pspRef) {
        $("#hisBody").empty();
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/payment/psp/his?pageNumber=0&pageItemSize=200&pspRef=" + encodeURIComponent(pspRef),
            dataType: 'json',
            success: function (res) {
                if (!res || !res.result || res.result.length === 0) {
                    $("#hisBody").append('<tr><td colspan="7" style="text-align:center;">' + _commonMsg.noData + '</td></tr>');
                    return;
                }
                var html = '';
                for (var i = 0; i < res.result.length; i++) {
                    var h = res.result[i];
                    html += '<tr>';
                    html += '<td>' + _formatDateTime(h.occurredDate) + '</td>';
                    html += '<td>' + (h.preStatusCd || '-') + '</td>';
                    html += '<td>' + (h.postStatusCd || '-') + '</td>';
                    html += '<td style="text-align:right;">' + (h.amount == null ? '-' : Number(h.amount).toLocaleString()) + '</td>';
                    html += '<td>' + (h.reasonCd || '-') + '</td>';
                    html += '<td>' + (h.remark || '-') + '</td>';
                    html += '<td>' + (h.operId || '-') + '</td>';
                    html += '</tr>';
                }
                $("#hisBody").html(html);
            }
        });
    }

    function _escape(s) {
        return String(s).replace(/[&<>]/g, function (c) {
            return { '&': '&amp;', '<': '&lt;', '>': '&gt;' }[c];
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
        var ss = ('0' + dt.getSeconds()).slice(-2);
        return _formatDate(dt) + ' ' + hh + ':' + mm + ':' + ss;
    }

    return {
        init: _init,
        search: _search,
        view: _view,
        settle: _settle,
        cancelRow: _cancelRow
    };
}();
