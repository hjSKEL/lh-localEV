/**
 * 로컬 인증 목록 관리 - 상세 페이지
 */
let localCustomerDetailJs = function () {
    "use strict";

    let cpId = '';
    let csId = '';
    let detailData = null;

    function _init() {
        cpId = $("#paramCpId").val();
        csId = $("#paramCsId").val();

        if (!cpId || !csId) {
            window.location.href = _ctx + "/localCustomer/list";
            return;
        }

        _initEvent();
        _loadDetail();
    }

    function _initEvent() {
        $("#btnList").click(function () {
            window.location.href = _ctx + "/localCustomer/list";
        });

        $("#btnDelete").click(function () {
            if (!confirm(_msg.confirmDeleteWithTokens)) return;
            _deleteHeader();
        });

        $("#btnAddToken").click(function () {
            _addToken();
        });

        $("#inputToken").keypress(function (e) {
            if (e.keyCode === 13) _addToken();
        });
    }

    // ── 상세 조회 ───────────────────────────────────────────────────────────
    function _loadDetail() {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/localCustomer/detail?cpId=" + cpId + "&csId=" + csId,
            dataType: 'json',
            success: function (data) {
                if (!data || !data.cpId) {
                    toastr.error(_msg.dataNotFound);
                    return;
                }
                detailData = data;
                _renderHeader(data);
                _renderTokens(data.tokens || []);
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _renderHeader(data) {
        $("#infoCpId").text(data.cpId || '-');
        $("#infoCsId").text(data.csId || '-');
        $("#infoVersion").html(data.versionNo != null
            ? '<span class="label label-default">v' + data.versionNo + '</span>' : '-');
        $("#infoUpdateType").text(data.lastUpdateType || '-');
        $("#infoLastSendDate").text(data.lastSendDate
            ? new Date(data.lastSendDate).toLocaleString('ko-KR') : '-');
        $("#infoStatus").html(_statusBadge(data.status));
        $("#infoRegDate").text(data.writer && data.writer.registrationDate
            ? new Date(data.writer.registrationDate).toLocaleString('ko-KR') : '-');
        $("#infoUpdDate").text(data.writer && data.writer.updateDate
            ? new Date(data.writer.updateDate).toLocaleString('ko-KR') : '-');
    }

    // ── 토큰 목록 렌더링 ────────────────────────────────────────────────────
    function _renderTokens(tokens) {
        let tbody = $("#tBodyTokens");
        tbody.empty();

        $("#tokenTotalCount").text(_commonMsg.totalCount.replace('{0}', tokens.length));

        if (tokens.length === 0) {
            tbody.append('<tr style="text-align:center;"><td colspan="8">' + _msg.noTokenData + '</td></tr>');
            return;
        }

        let html = '';
        for (let i = 0; i < tokens.length; i++) {
            let t = tokens[i];
            let expDate = t.cacheExpireDate
                ? new Date(t.cacheExpireDate).toLocaleString('ko-KR') : '-';
            let statusBadge = _tokenStatusBadge(t.tokenStatus);

            html += '<tr>';
            html += '<td>' + (i + 1) + '</td>';
            html += '<td style="font-family:monospace;">' + (t.token || '-') + '</td>';
            html += '<td>' + (t.tokenType || '-') + '</td>';
            html += '<td>' + statusBadge + '</td>';
            html += '<td>' + expDate + '</td>';
            html += '<td style="font-family:monospace;">' + (t.parentIdToken || '-') + '</td>';
            html += '<td>' + (t.parentTokenType || '-') + '</td>';
            html += '<td><button type="button" class="btn btn-xs btn-danger" '
                + 'onclick="localCustomerDetailJs.deleteToken(\'' + _escapeQuote(t.token) + '\',\'' + _escapeQuote(t.tokenType) + '\')">'
                + '<i class="fa fa-trash"></i></button></td>';
            html += '</tr>';
        }

        tbody.append(html);
    }

    // ── 토큰 추가 ───────────────────────────────────────────────────────────
    function _addToken() {
        let token = $("#inputToken").val().trim();
        let tokenType = $("#inputTokenType").val();
        let tokenStatus = $("#inputTokenStatus").val();
        let cacheExpireDate = $("#inputCacheExpireDate").val();
        let parentIdToken = $("#inputParentIdToken").val().trim();
        let parentTokenType = $("#inputParentTokenType").val();

        if (!token) {
            toastr.warning(_msg.inputTokenValue);
            return;
        }

        let payload = {
            cpId: cpId,
            csId: csId,
            token: token,
            tokenType: tokenType,
            tokenStatus: tokenStatus,
            cacheExpireDate: cacheExpireDate || null,
            parentIdToken: parentIdToken || null,
            parentTokenType: parentTokenType || null
        };

        $("#btnAddToken").prop('disabled', true);

        $.ajax({
            type: 'POST',
            url: _ctx + "/ws/localCustomer/token",
            contentType: 'application/json',
            data: JSON.stringify(payload),
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData.status === 'SUCCESS') {
                    toastr.success(_msg.tokenAdded);
                    $("#inputToken, #inputParentIdToken").val('');
                    $("#inputCacheExpireDate").val('');
                    $("#inputParentTokenType").val('');
                    _loadDetail();
                } else {
                    toastr.error(jsonData.result || _msg.tokenAddFail);
                }
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            },
            complete: function () {
                $("#btnAddToken").prop('disabled', false);
            }
        });
    }

    // ── 토큰 삭제 ───────────────────────────────────────────────────────────
    function _deleteToken(token, tokenType) {
        if (!confirm(_msg.confirmDeleteToken)) return;

        $.ajax({
            type: 'DELETE',
            url: _ctx + "/ws/localCustomer/token?cpId=" + cpId + "&csId=" + csId
                + "&token=" + encodeURIComponent(token) + "&tokenType=" + encodeURIComponent(tokenType),
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData.status === 'SUCCESS') {
                    toastr.success(_msg.tokenDeleted);
                    _loadDetail();
                } else {
                    toastr.error(jsonData.result || _msg.tokenDeleteFail);
                }
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    // ── 헤더 삭제 ───────────────────────────────────────────────────────────
    function _deleteHeader() {
        $.ajax({
            type: 'DELETE',
            url: _ctx + "/ws/localCustomer?cpId=" + cpId + "&csId=" + csId,
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData.status === 'SUCCESS') {
                    toastr.success(_commonMsg.successDelete);
                    window.location.href = _ctx + "/localCustomer/list";
                } else {
                    toastr.error(jsonData.result || _commonMsg.failDelete);
                }
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    // ── 상태 배지 ───────────────────────────────────────────────────────────
    function _statusBadge(status) {
        if (status === 'Accepted')        return '<span class="label label-primary">Accepted</span>';
        if (status === 'Failed')          return '<span class="label label-danger">Failed</span>';
        if (status === 'NotSupported')    return '<span class="label label-warning">NotSupported</span>';
        if (status === 'VersionMismatch') return '<span class="label label-warning">VersionMismatch</span>';
        return '<span class="label label-default">' + (status || '-') + '</span>';
    }

    function _tokenStatusBadge(status) {
        if (status === 'Accepted')     return '<span class="label label-primary">Accepted</span>';
        if (status === 'Blocked')      return '<span class="label label-danger">Blocked</span>';
        if (status === 'Expired')      return '<span class="label label-warning">Expired</span>';
        if (status === 'Invalid')      return '<span class="label label-danger">Invalid</span>';
        if (status === 'NoCredit')     return '<span class="label label-warning">NoCredit</span>';
        if (status === 'ConcurrentTx') return '<span class="label label-info">ConcurrentTx</span>';
        return '<span class="label label-default">' + (status || '-') + '</span>';
    }

    function _escapeQuote(str) {
        return str ? str.replace(/'/g, "\\'") : '';
    }

    return {
        init:        _init,
        deleteToken: _deleteToken
    };
}();
