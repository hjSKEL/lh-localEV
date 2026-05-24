/**
 * 사업자(VAT) 관리
 */
var vatListJs = function () {
    "use strict";

    var data = { searchCond: {}, result: [] };
    var editMode = 'NEW'; // 'NEW' | 'EDIT'

    function _init() {
        _initUseYnSelect();
        _initEvent();
        _searchOnClick();
    }

    function _initUseYnSelect() {
        var html = '';
        html += '<option value="">' + _msg.useAll + '</option>';
        html += '<option value="Y">' + _msg.useY + '</option>';
        html += '<option value="N">' + _msg.useN + '</option>';
        $("#useYn").html(html);
    }

    function _initEvent() {
        $("#btnSearch").click(_searchOnClick);
        $("#useYn").change(_searchOnClick);
        $("#btnReset").click(_searchResetClick);
        $("#sWord").keypress(function (event) {
            if (event.keyCode == 13) _searchOnClick();
        });
        $("#btnRegister").click(_openRegisterModal);
        $("#btnSave").click(_submitSave);
    }

    function _searchResetClick() {
        $("#sWord").val("");
        $("#useYn").val("");
        $("#searchType").val("VATNO");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, vatListJs.search);
        data.searchCond.vatNo = "";
        data.searchCond.companyNm = "";

        var searchKey = $("#sWord").val().trim();
        switch ($("#searchType").val()) {
            case "VATNO":   data.searchCond.vatNo = searchKey; break;
            case "COMPANY": data.searchCond.companyNm = searchKey; break;
        }
        data.searchCond.useYn = $("#useYn").val();
        $("#sWord").val(searchKey);
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr style="text-align:center;"><td colspan="9">' + _commonMsg.searching + '</td></tr>');

        var paging = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&vatNo=" + encodeURIComponent(data.searchCond.vatNo);
        param += "&companyNm=" + encodeURIComponent(data.searchCond.companyNm);
        param += "&useYn=" + encodeURIComponent(data.searchCond.useYn);

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/payment/vat" + param,
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
            $("#tBodyList").append('<tr style="text-align:center;"><td colspan="9">' + _commonMsg.noData + '</td></tr>');
            return;
        }

        data.result = jsonData.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        var html = '';
        for (var i = 0, length = data.result.length; i < length; ++i) {
            var row = data.result[i];
            var addr = (row.addr1 || '') + (row.addr2 ? ' ' + row.addr2 : '') + (row.city ? ' ' + row.city : '') + (row.postalCd ? ' (' + row.postalCd + ')' : '');
            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + row.vatNo + '</td>';
            html += '<td>' + (row.companyNm || '-') + '</td>';
            html += '<td>' + (row.repNm || '-') + '</td>';
            html += '<td style="text-align:left;">' + (addr || '-') + '</td>';
            html += '<td>' + (row.country || '-') + '</td>';
            html += '<td>' + _useBadge(row.useYn) + '</td>';
            html += '<td>' + _formatDateTime(row.writer && row.writer.registrationDate) + '</td>';
            html += '<td>' + _actionButtons(i, row) + '</td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _useBadge(useYn) {
        if (useYn === 'Y') return '<span class="label label-primary">' + _msg.useY + '</span>';
        return '<span class="label label-default">' + _msg.useN + '</span>';
    }

    function _actionButtons(index, row) {
        var html = '';
        html += '<button type="button" class="btn btn-xs btn-info" onclick="vatListJs.edit(' + index + ')">' + _msg.btnEdit + '</button> ';
        if (row.useYn === 'Y') {
            html += '<button type="button" class="btn btn-xs btn-warning" onclick="vatListJs.toggleUse(\'' + row.vatNo + '\',\'N\')">' + _msg.useN + '</button>';
        } else {
            html += '<button type="button" class="btn btn-xs btn-primary" onclick="vatListJs.toggleUse(\'' + row.vatNo + '\',\'Y\')">' + _msg.useY + '</button>';
        }
        return html;
    }

    function _openRegisterModal() {
        editMode = 'NEW';
        $("#popupTitle").text(_msg.vatMgmt);
        $("#form_vatNo").val("").prop('readonly', false);
        $("#form_companyNm").val("");
        $("#form_repNm").val("");
        $("#form_addr1").val("");
        $("#form_addr2").val("");
        $("#form_city").val("");
        $("#form_postalCd").val("");
        $("#form_country").val("KR");
        $("#form_bizType").val("");
        $("#form_bizItem").val("");
        $("#form_useYn").val("Y");
        $("#Popup_Vat_Edit").modal();
    }

    function _edit(index) {
        var row = data.result[index];
        if (!row) return;
        editMode = 'EDIT';
        $("#popupTitle").text(_msg.vatMgmt);
        $("#form_vatNo").val(row.vatNo).prop('readonly', true);
        $("#form_companyNm").val(row.companyNm || "");
        $("#form_repNm").val(row.repNm || "");
        $("#form_addr1").val(row.addr1 || "");
        $("#form_addr2").val(row.addr2 || "");
        $("#form_city").val(row.city || "");
        $("#form_postalCd").val(row.postalCd || "");
        $("#form_country").val(row.country || "KR");
        $("#form_bizType").val(row.bizType || "");
        $("#form_bizItem").val(row.bizItem || "");
        $("#form_useYn").val(row.useYn || "Y");
        $("#Popup_Vat_Edit").modal();
    }

    function _submitSave() {
        var vatNo     = $("#form_vatNo").val().trim();
        var companyNm = $("#form_companyNm").val().trim();
        var addr1     = $("#form_addr1").val().trim();
        var city      = $("#form_city").val().trim();
        if (!vatNo)     { toastr.warning(_msg.inputVatNo,   _msg.vatMgmt); return; }
        if (!companyNm) { toastr.warning(_msg.inputCompany, _msg.vatMgmt); return; }
        if (!addr1)     { toastr.warning(_msg.inputAddr1,   _msg.vatMgmt); return; }
        if (!city)      { toastr.warning(_msg.inputCity,    _msg.vatMgmt); return; }

        var body = {
            vatNo: vatNo,
            companyNm: companyNm,
            repNm: $("#form_repNm").val().trim() || null,
            addr1: addr1,
            addr2: $("#form_addr2").val().trim() || null,
            city: city,
            postalCd: $("#form_postalCd").val().trim() || null,
            country: $("#form_country").val().trim() || "KR",
            bizType: $("#form_bizType").val().trim() || null,
            bizItem: $("#form_bizItem").val().trim() || null,
            useYn: $("#form_useYn").val()
        };

        if (editMode === 'NEW') {
            _doRegister(body);
        } else {
            _doModify(body);
        }
    }

    function _doRegister(body) {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/payment/vat/check/" + encodeURIComponent(body.vatNo),
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData.status !== 'SUCCESS') {
                    toastr.error(_msg.duplicateVatNo, _msg.vatMgmt);
                    return;
                }
                $.ajax({
                    type: 'POST',
                    url: _ctx + "/ws/payment/vat",
                    contentType: "application/json",
                    dataType: 'json',
                    data: JSON.stringify(body),
                    success: function (res) {
                        if (res.status == 'SUCCESS') {
                            toastr.success(_msg.successRegister, _msg.vatMgmt);
                            $("#Popup_Vat_Edit").modal('hide');
                            _search();
                        } else {
                            toastr.error(res.result || _msg.failRegister, _msg.vatMgmt);
                        }
                    },
                    error: function (xhRequest) {
                        parent.layerJs.fn_exception(xhRequest);
                        toastr.error(_msg.failRegister, _msg.vatMgmt);
                    }
                });
            },
            error: function (xhRequest) { parent.layerJs.fn_exception(xhRequest); }
        });
    }

    function _doModify(body) {
        $.ajax({
            type: 'PUT',
            url: _ctx + "/ws/payment/vat/" + encodeURIComponent(body.vatNo),
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(body),
            success: function (res) {
                if (res.status == 'SUCCESS') {
                    toastr.success(_msg.successModify, _msg.vatMgmt);
                    $("#Popup_Vat_Edit").modal('hide');
                    _search();
                } else {
                    toastr.error(res.result || _msg.failModify, _msg.vatMgmt);
                }
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
                toastr.error(_msg.failModify, _msg.vatMgmt);
            }
        });
    }

    function _toggleUse(vatNo, target) {
        var prompt = (target === 'N') ? _msg.confirmDeactivate : _msg.confirmActivate;
        swal({
            title: _msg.vatMgmt,
            text: prompt,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.confirm,
            cancelButtonText: _msg.cancel,
            closeOnConfirm: true
        }, function () {
            $.ajax({
                type: 'PUT',
                url: _ctx + "/ws/payment/vat/" + encodeURIComponent(vatNo) + "/use/" + target,
                contentType: "application/json",
                dataType: 'json',
                success: function (res) {
                    if (res.status == 'SUCCESS') {
                        toastr.success(_msg.successChange, _msg.vatMgmt);
                        _search();
                    } else {
                        toastr.error(res.result || _msg.failChange, _msg.vatMgmt);
                    }
                },
                error: function (xhRequest) {
                    parent.layerJs.fn_exception(xhRequest);
                    toastr.error(_msg.failChange, _msg.vatMgmt);
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
        edit: _edit,
        toggleUse: _toggleUse
    };
}();
