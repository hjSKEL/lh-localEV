/**
 * Tariff 마스터 관리
 */
var tariffListJs = function () {
    "use strict";

    var data = { searchCond: {}, result: [] };

    var SAMPLE_DRIVER = JSON.stringify({
        energy:   { prices: [{ priceKwh: 0.25 }],   taxRates: [{ type: "VAT", tax: 20 }] },
        idleTime: { prices: [{ priceMinute: 0.10 }], taxRates: [{ type: "VAT", tax: 20 }] },
        fixedFee: { prices: [{ priceFixed: 0.50 }],  taxRates: [{ type: "VAT", tax: 20 }] }
    }, null, 2);

    var SAMPLE_DEFAULT_FULL = JSON.stringify({
        energy:          { prices: [{ priceKwh: 0.25 }],   taxRates: [{ type: "VAT", tax: 20 }] },
        chargingTime:    { prices: [{ priceMinute: 0.10 }], taxRates: [{ type: "VAT", tax: 20 }] },
        idleTime:        { prices: [{ priceMinute: 0.10 }], taxRates: [{ type: "VAT", tax: 20 }] },
        fixedFee:        { prices: [{ priceFixed: 0.50 }],  taxRates: [{ type: "VAT", tax: 20 }] },
        reservationTime: { prices: [{ priceMinute: 0.10 }], taxRates: [{ type: "VAT", tax: 20 }] },
        minCost:         { exclTax: 15, inclTax: 15, taxRates: [{ type: "VAT", tax: 20 }] },
        maxCost:         { exclTax: 15, inclTax: 15, taxRates: [{ type: "VAT", tax: 20 }] }
    }, null, 2);

    function _init() {
        _initKindSelect();
        _initStatusSelect();
        _initEvent();
        _searchOnClick();
    }

    function _initKindSelect() {
        var html  = '<option value="">' + _msg.kindAll + '</option>';
            html += '<option value="DEFAULT">' + _msg.kindDefault + '</option>';
            html += '<option value="DRIVER">'  + _msg.kindDriver  + '</option>';
            html += '<option value="ADHOC">'   + _msg.kindAdhoc   + '</option>';
        $("#tariffKind").html(html);
    }

    function _initStatusSelect() {
        var html  = '<option value="">'         + _msg.statusAll      + '</option>';
            html += '<option value="ACTIVE">'   + _msg.statusActive   + '</option>';
            html += '<option value="REPLACED">' + _msg.statusReplaced + '</option>';
            html += '<option value="CLEARED">'  + _msg.statusCleared  + '</option>';
            html += '<option value="EXPIRED">'  + _msg.statusExpired  + '</option>';
        $("#statusCd").html(html);
    }

    function _initEvent() {
        $("#btnSearch").click(_searchOnClick);
        $("#tariffKind, #statusCd").change(_searchOnClick);
        $("#btnReset").click(_searchReset);
        $("#sWord").keypress(function (e) { if (e.keyCode == 13) _searchOnClick(); });
        $("#btnRegister").click(_openRegister);
        $("#btnSave").click(_submitRegister);
        $("#btnSample1").click(function () { $("#form_tariffJson").val(SAMPLE_DRIVER); });
        $("#btnSample2").click(function () { $("#form_tariffJson").val(SAMPLE_DEFAULT_FULL); });
        $("#btnValidate").click(_validateJson);
        $("#form_tariffKind").change(_loadNextId);
    }

    function _searchReset() {
        $("#sWord").val("");
        $("#tariffKind").val("");
        $("#statusCd").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, tariffListJs.search);
        data.searchCond = {
            tariffId:   $("#sWord").val().trim(),
            tariffKind: $("#tariffKind").val(),
            statusCd:   $("#statusCd").val()
        };
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="9">' + _commonMsg.searching + '</td></tr>');

        var paging = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&tariffId="   + encodeURIComponent(data.searchCond.tariffId   || '');
        param += "&tariffKind=" + encodeURIComponent(data.searchCond.tariffKind || '');
        param += "&statusCd="   + encodeURIComponent(data.searchCond.statusCd   || '');

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/payment/tariff" + param,
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
            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + row.tariffId + '</td>';
            html += '<td>' + _kindBadge(row.tariffKind) + '</td>';
            html += '<td>' + (row.currency || '-') + '</td>';
            html += '<td>' + _formatDateTime(row.validFrom) + '</td>';
            html += '<td>' + _statusBadge(row.statusCd) + '</td>';
            html += '<td>' + (row.activeAssignmentCount || 0) + '</td>';
            html += '<td style="text-align:left;">' + (row.description || '-') + '</td>';
            html += '<td>' + _actionButtons(i, row) + '</td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _kindBadge(k) {
        switch (k) {
            case 'DEFAULT': return '<span class="label label-primary">'+ _msg.kindDefault +'</span>';
            case 'DRIVER':  return '<span class="label label-info">'   + _msg.kindDriver  +'</span>';
            case 'ADHOC':   return '<span class="label label-warning">'+ _msg.kindAdhoc   +'</span>';
            default:        return k || '-';
        }
    }
    function _statusBadge(s) {
        switch (s) {
            case 'ACTIVE':   return '<span class="label label-primary">' + _msg.statusActive   + '</span>';
            case 'REPLACED': return '<span class="label label-default">' + _msg.statusReplaced + '</span>';
            case 'CLEARED':  return '<span class="label label-default">' + _msg.statusCleared  + '</span>';
            case 'EXPIRED':  return '<span class="label label-default">' + _msg.statusExpired  + '</span>';
            default:         return '<span class="label label-default">' + (s || '-') + '</span>';
        }
    }
    function _actionButtons(index, row) {
        var html = '';
        html += '<button type="button" class="btn btn-xs btn-info" onclick="tariffListJs.view(' + index + ')">' + _msg.btnView + '</button>';
        if (row.statusCd === 'ACTIVE') {
            html += ' <button type="button" class="btn btn-xs btn-danger" onclick="tariffListJs.deprecate(\'' + row.tariffId + '\')">' + _msg.btnDeprecate + '</button>';
        }
        return html;
    }

    /* ------ 등록 ------ */
    function _openRegister() {
        $("#form_tariffId").val("");
        $("#form_tariffKind").val("DRIVER");
        $("#form_currency").val("EUR");
        $("#form_validFrom").val("");
        $("#form_description").val("");
        $("#form_tariffJson").val(SAMPLE_DRIVER);
        _loadNextId();
        $("#Popup_Tariff_Register").modal();
    }

    function _loadNextId() {
        var kind = $("#form_tariffKind").val();
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/payment/tariff/nextId/" + kind,
            dataType: 'json',
            success: function (res) {
                if (res.status === 'SUCCESS') $("#form_tariffId").val(res.result);
            }
        });
    }

    function _validateJson() {
        var raw = $("#form_tariffJson").val().trim();
        if (!raw) { toastr.warning(_msg.inputJson, _msg.tariffMgmt); return false; }
        try {
            JSON.parse(raw);
            toastr.success('OK', _msg.tariffMgmt);
            return true;
        } catch (e) {
            toastr.error(_msg.invalidJson + ': ' + e.message, _msg.tariffMgmt);
            return false;
        }
    }

    function _submitRegister() {
        var currency = $("#form_currency").val().trim();
        var jsonRaw  = $("#form_tariffJson").val().trim();
        if (!currency) { toastr.warning(_msg.inputCurrency, _msg.tariffMgmt); return; }
        if (!jsonRaw)  { toastr.warning(_msg.inputJson,     _msg.tariffMgmt); return; }
        try { JSON.parse(jsonRaw); }
        catch (e) { toastr.error(_msg.invalidJson + ': ' + e.message, _msg.tariffMgmt); return; }

        var body = {
            tariffId:    $("#form_tariffId").val().trim() || null,
            tariffKind:  $("#form_tariffKind").val(),
            currency:    currency,
            validFrom:   _isoToDate($("#form_validFrom").val().trim()),
            tariffJson:  jsonRaw,
            description: $("#form_description").val().trim() || null
        };

        $.ajax({
            type: 'POST',
            url: _ctx + "/ws/payment/tariff",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(body),
            success: function (res) {
                if (res.status === 'SUCCESS') {
                    toastr.success(_msg.successRegister, _msg.tariffMgmt);
                    $("#Popup_Tariff_Register").modal('hide');
                    _search();
                } else {
                    toastr.error(res.result || _msg.failRegister, _msg.tariffMgmt);
                }
            },
            error: function (xhr) {
                parent.layerJs.fn_exception(xhr);
                toastr.error(_msg.failRegister, _msg.tariffMgmt);
            }
        });
    }

    function _isoToDate(iso) {
        if (!iso) return null;
        // 컨트롤러에서 String → Date 변환 보장은 안 되므로 yyyy-MM-dd HH:mm:ss 로 normalizing
        // 단, OCPP 응답에는 ISO 문자열이 들어가야 하므로 별도 처리 — Tariff master 의 validFrom 은 java.util.Date
        return iso; // body 는 JSON 이므로 ISO 문자열 그대로 전달 (Jackson + Date 매핑)
    }

    /* ------ 상세 ------ */
    function _view(index) {
        var row = data.result[index];
        if (!row) return;
        var rows = [
            ['tariffId',        row.tariffId],
            ['kind',            _kindBadge(row.tariffKind)],
            ['currency',        row.currency || '-'],
            ['validFrom',       _formatDateTime(row.validFrom)],
            ['validTo',         _formatDateTime(row.validTo)],
            ['status',          _statusBadge(row.statusCd)],
            ['description',     row.description || '-'],
            ['active mappings', row.activeAssignmentCount || 0],
            ['regDate',         _formatDateTime(row.writer && row.writer.registrationDate)],
            ['regUser',         (row.writer && row.writer.regUserId) || '-']
        ];
        var html = '';
        for (var i = 0; i < rows.length; i++) {
            html += '<tr><th style="width:25%;">' + rows[i][0] + '</th><td>' + rows[i][1] + '</td></tr>';
        }
        $("#detailBody").html(html);
        var prettyJson = row.tariffJson;
        try { prettyJson = JSON.stringify(JSON.parse(row.tariffJson), null, 2); } catch (e) {}
        $("#detailJson").text(prettyJson);
        $("#Popup_Tariff_Detail").modal();
    }

    /* ------ 폐기 ------ */
    function _deprecate(tariffId) {
        swal({
            title: _msg.tariffMgmt, text: _msg.confirmDeprecate, type: 'warning',
            showCancelButton: true, confirmButtonColor: '#DD6B55',
            confirmButtonText: _msg.confirm, cancelButtonText: _msg.cancel, closeOnConfirm: true
        }, function () {
            $.ajax({
                type: 'PUT',
                url: _ctx + "/ws/payment/tariff/" + encodeURIComponent(tariffId) + "/deprecate",
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify({ newStatus: 'REPLACED', reason: 'manual deprecate' }),
                success: function (res) {
                    if (res.status === 'SUCCESS') {
                        toastr.success(_msg.successDeprecate, _msg.tariffMgmt);
                        _search();
                    } else {
                        toastr.error(res.result || _msg.failDeprecate, _msg.tariffMgmt);
                    }
                },
                error: function (xhr) {
                    parent.layerJs.fn_exception(xhr);
                    toastr.error(_msg.failDeprecate, _msg.tariffMgmt);
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
        view: _view,
        deprecate: _deprecate
    };
}();
