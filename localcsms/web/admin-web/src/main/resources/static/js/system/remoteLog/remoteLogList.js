/**
 * 원격명령 로그 목록
 * /system/remoteLog/list
 */
let remoteLogListJs = function () {
    "use strict";

    let data = {
        pageNumber: 0,
        pageItemSize: 20,
        totalCount: 0,
        pageCount: 0,
        currentUuid: ''
    };

    function _statusLabel(status) {
        switch (status) {
            case 'RMST01': return '<span class="label label-primary">' + _msg.statusReq + '</span>';
            case 'RMST02': return '<span class="label label-success">' + _msg.statusRes + '</span>';
            case 'RMST03': return '<span class="label label-danger">' + _msg.statusFail + '</span>';
            default: return status || '';
        }
    }

    function _statusText(status) {
        switch (status) {
            case 'RMST01': return _msg.statusReq;
            case 'RMST02': return _msg.statusRes;
            case 'RMST03': return _msg.statusFail;
            default: return status || '';
        }
    }

    function _init() {
        _initEvent();
        _search();
    }

    function _initEvent() {
        $('#btnSearch').on('click', function () {
            data.pageNumber = 0;
            _search();
        });

        $('#btnReset').on('click', function () {
            $('#actionName').val('');
            $('#ocppVersion').val('');
            $('#status').val('');
            data.pageNumber = 0;
            _search();
        });

        $('#actionName').on('keypress', function (e) {
            if (e.keyCode === 13) {
                data.pageNumber = 0;
                _search();
            }
        });

        $(document).on('click', '.page-link', function (e) {
            e.preventDefault();
            let page = parseInt($(this).data('page'));
            if (!isNaN(page) && page !== data.pageNumber) {
                data.pageNumber = page;
                _search();
            }
        });

        $('#btnDelete').on('click', function () {
            if (!data.currentUuid) return;
            if (!confirm(_commonMsg.confirmDelete)) return;
            _deleteOne(data.currentUuid);
        });
    }

    function _search() {
        let params = {
            actionName: $('#actionName').val().trim(),
            ocppVersion: $('#ocppVersion').val(),
            status: $('#status').val(),
            pageNumber: data.pageNumber,
            pageItemSize: data.pageItemSize
        };

        $.ajax({
            type: 'GET',
            url: _ctx + '/ws/system/remoteLog/list',
            data: params,
            success: function (res) {
                if (!res) { _clearTable(); return; }
                let criteria = res.criteria || {};
                let list = res.result || [];
                data.totalCount = criteria.totalItemCount || 0;
                data.pageCount  = criteria.pageCount || 1;
                data.pageNumber = criteria.pageNumber || 0;

                $('#totalCount').text(_commonMsg.totalCount.replace('{0}', data.totalCount));
                _renderTable(list, data.pageNumber, data.pageItemSize);
                _renderPaging();
            },
            error: function () {
                alert(_commonMsg.queryError);
            }
        });
    }

    function _renderTable(list, pageNumber, pageItemSize) {
        let $tbody = $('#tBodyList').empty();
        if (!list || list.length === 0) {
            $tbody.append('<tr><td colspan="6" style="text-align:center;">' + _commonMsg.noData + '</td></tr>');
            return;
        }
        let startNo = pageNumber * pageItemSize + 1;
        for (let i = 0; i < list.length; i++) {
            let v = list[i];
            let uuidShort = v.uuid ? v.uuid.substring(0, 8) + '...' : '';
            let regDt = v.regDt ? v.regDt.replace('T', ' ').substring(0, 19) : '';
            let ocppVer = v.ocppVersion || '-';

            $tbody.append(
                '<tr style="cursor:pointer;" data-uuid="' + _escapeHtml(v.uuid) + '">' +
                '<td>' + (startNo + i) + '</td>' +
                '<td style="font-family:monospace;">' + _escapeHtml(uuidShort) + '</td>' +
                '<td><span class="label label-default">' + _escapeHtml(ocppVer) + '</span></td>' +
                '<td><strong>' + _escapeHtml(v.actionName || '') + '</strong></td>' +
                '<td>' + _statusLabel(v.status) + '</td>' +
                '<td>' + regDt + '</td>' +
                '</tr>'
            );
        }
        // 행 클릭 → 상세 팝업
        $tbody.find('tr').on('click', function () {
            let uuid = $(this).data('uuid');
            if (uuid) _showDetail(uuid);
        });
    }

    function _showDetail(uuid) {
        data.currentUuid = uuid;
        $.ajax({
            type: 'GET',
            url: _ctx + '/ws/system/remoteLog/' + uuid,
            success: function (v) {
                if (!v) { alert(_commonMsg.dataNotFound); return; }
                $('#detailUuid').text(v.uuid || '');
                $('#detailOcppVersion').text(v.ocppVersion || '-');
                $('#detailActionName').text(v.actionName || '');
                $('#detailStatus').html(_statusLabel(v.status) + ' (' + (v.status || '') + ')');
                $('#detailRegDt').text(v.regDt ? v.regDt.replace('T', ' ').substring(0, 19) : '');
                $('#detailReqPayload').text(_tryFormatJson(v.reqPayload));
                $('#detailResPayload').text(_tryFormatJson(v.resPayload));
                $('#detailModal').modal('show');
            },
            error: function () {
                alert(_commonMsg.queryError);
            }
        });
    }

    function _deleteOne(uuid) {
        $.ajax({
            type: 'DELETE',
            url: _ctx + '/ws/system/remoteLog/' + uuid,
            success: function (res) {
                if (res && res.status === 'SUCCESS') {
                    $('#detailModal').modal('hide');
                    _search();
                } else {
                    alert(_commonMsg.failDelete);
                }
            },
            error: function () {
                alert(_commonMsg.failDelete);
            }
        });
    }

    function _tryFormatJson(str) {
        if (!str) return '';
        try {
            return JSON.stringify(JSON.parse(str), null, 2);
        } catch (e) {
            return str;
        }
    }

    function _renderPaging() {
        let $ul = $('#pagination').empty();
        if (data.pageCount <= 1) return;
        let current = data.pageNumber;
        let total   = data.pageCount;
        let block   = 10;
        let start   = Math.floor(current / block) * block;
        let end     = Math.min(start + block, total);
        if (start > 0) {
            $ul.append('<li class="page-item"><a class="page-link" data-page="' + (start - 1) + '" href="#">&laquo;</a></li>');
        }
        for (let i = start; i < end; i++) {
            let active = (i === current) ? ' active' : '';
            $ul.append('<li class="page-item' + active + '"><a class="page-link" data-page="' + i + '" href="#">' + (i + 1) + '</a></li>');
        }
        if (end < total) {
            $ul.append('<li class="page-item"><a class="page-link" data-page="' + end + '" href="#">&raquo;</a></li>');
        }
    }

    function _clearTable() {
        $('#tBodyList').html('<tr><td colspan="6" style="text-align:center;">' + _commonMsg.noData + '</td></tr>');
        $('#pagination').empty();
        $('#totalCount').text(_commonMsg.totalCount.replace('{0}', '0'));
    }

    function _escapeHtml(str) {
        if (!str) return '';
        return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    }

    return { init: _init, search: _search };
}();

$(document).ready(function () {
    remoteLogListJs.init();
});
