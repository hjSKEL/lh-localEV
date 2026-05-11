/**
 * 로컬 인증 목록 관리 - 목록 페이지
 */
let localCustomerJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    function _init() {
        _initEvent();
        _searchOnClick();
    }

    function _initEvent() {
        $("#btnSearch").click(function () {
            _searchOnClick();
        });

        $("#btnReset").click(function () {
            _resetClick();
        });

        $("#searchCpId, #searchCsId").keypress(function (e) {
            if (e.keyCode === 13) _searchOnClick();
        });

        $("input:radio[name='searchStatus']").click(function () {
            _searchOnClick();
        });

        $("#btnRegister").click(function () {
            _registerLocalCustomer();
        });

        $("#registerModal").on("hidden.bs.modal", function () {
            $("#regCpId, #regCsId").val("");
            $("#alertRegister").hide();
        });
    }

    function _resetClick() {
        $("#searchCpId").val("");
        $("#searchCsId").val("");
        $("input:radio[name='searchStatus'][value='']").prop('checked', true);
        _searchOnClick();
    }

    function _searchOnClick() {
        data.searchCond = {};

        let cpId = $("#searchCpId").val().trim();
        let csId = $("#searchCsId").val().trim();
        let statusVal = $("input:radio[name='searchStatus']:checked").val();

        if (cpId) data.searchCond.cpId = cpId;
        if (csId) data.searchCond.csId = csId;
        if (statusVal) data.searchCond.status = statusVal;

        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, localCustomerJs.search);
        _search();
    }

    function _search() {
        $("#tBodyList").empty().append(
            '<tr style="text-align:center;"><td colspan="9">' + _commonMsg.searching + '</td></tr>'
        );

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        if (data.searchCond.cpId)   param += "&cpId="   + data.searchCond.cpId;
        if (data.searchCond.csId)   param += "&csId="   + data.searchCond.csId;
        if (data.searchCond.status) param += "&status=" + data.searchCond.status;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/localCustomer/list" + param,
            dataType: 'json',
            success: function (jsonData) {
                _display(jsonData);
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _display(jsonData) {
        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();

        if (jsonData.criteria.totalItemCount === 0) {
            $("#tBodyList").append(
                '<tr style="text-align:center;"><td colspan="9">' + _commonMsg.noData + '</td></tr>'
            );
            return;
        }

        let result = jsonData.result;
        let paging = pageInfoJs.getPaging();
        let noIndex = (paging.pageNumber - 1) * paging.pageItemSize + 1;
        let html = '';

        for (let i = 0, len = result.length; i < len; i++) {
            let r = result[i];
            let statusBadge = _statusBadge(r.status);
            let lastSend = r.lastSendDate
                ? new Date(r.lastSendDate).toLocaleString('ko-KR') : '-';
            let regDate = r.writer && r.writer.registrationDate
                ? new Date(r.writer.registrationDate).toLocaleString('ko-KR') : '-';
            let version = r.versionNo != null
                ? '<span class="label label-default">v' + r.versionNo + '</span>' : '-';
            let tokenCount = r.tokens != null ? r.tokens.length : '-';

            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td style="font-family:monospace;">'
                + '<a href="#" onclick="localCustomerJs.goDetail(\'' + r.cpId + '\',\'' + r.csId + '\'); return false;">'
                + r.cpId + '</a></td>';
            html += '<td style="font-family:monospace;">' + (r.csId || '-') + '</td>';
            html += '<td>' + version + '</td>';
            html += '<td>' + (r.lastUpdateType || '-') + '</td>';
            html += '<td>' + lastSend + '</td>';
            html += '<td>' + statusBadge + '</td>';
            html += '<td>' + tokenCount + '</td>';
            html += '<td>' + regDate + '</td>';
            html += '</tr>';
        }

        $("#tBodyList").append(html);
    }

    function _statusBadge(status) {
        if (status === 'Accepted')        return '<span class="label label-primary">Accepted</span>';
        if (status === 'Failed')          return '<span class="label label-danger">Failed</span>';
        if (status === 'NotSupported')    return '<span class="label label-warning">NotSupported</span>';
        if (status === 'VersionMismatch') return '<span class="label label-warning">VersionMismatch</span>';
        return '<span class="label label-default">' + (status || '-') + '</span>';
    }

    function _goDetail(cpId, csId) {
        window.location.href = _ctx + "/localCustomer/detail?cpId=" + cpId + "&csId=" + csId;
    }

    function _registerLocalCustomer() {
        let cpId = $("#regCpId").val().trim();
        let csId = $("#regCsId").val().trim();

        if (!cpId || cpId.length !== 6) {
            _showAlert(false, _msg.inputError, _msg.inputCpId6);
            return;
        }
        if (!csId || csId.length !== 2) {
            _showAlert(false, _msg.inputError, _msg.inputCsId2);
            return;
        }

        let payload = {
            cpId: cpId,
            csId: csId,
            versionNo: 0,
            status: ''
        };

        $("#btnRegister").prop('disabled', true).text(_msg.processing);

        $.ajax({
            type: 'POST',
            url: _ctx + "/ws/localCustomer",
            contentType: 'application/json',
            data: JSON.stringify(payload),
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData.status === 'SUCCESS') {
                    $("#registerModal").modal('hide');
                    toastr.success(_msg.localAuthRegistered);
                    _searchOnClick();
                } else {
                    _showAlert(false, _msg.registerFail, jsonData.result || _msg.alreadyRegistered);
                }
            },
            error: function (xhRequest) {
                _showAlert(false, _commonMsg.error, _commonMsg.requestError);
            },
            complete: function () {
                $("#btnRegister").prop('disabled', false).text(_msg.btnRegister);
            }
        });
    }

    function _showAlert(isSuccess, title, message) {
        let alertEl = $("#alertRegister");
        alertEl.removeClass("alert-success alert-danger")
            .addClass(isSuccess ? "alert-success" : "alert-danger")
            .show();
        $("#alertTitle").text(title);
        $("#alertMessage").text(message);
    }

    return {
        init: _init,
        search: _search,
        goDetail: _goDetail
    };
}();
