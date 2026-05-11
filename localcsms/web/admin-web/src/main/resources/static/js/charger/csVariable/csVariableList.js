/**
 * 충전기 설정 변수 목록 (OCPP 2.x)
 * /charger/csVariable/list?cpId=&csId=
 */
let csVariableListJs = function () {
    "use strict";

    let data = {
        cpId: '',
        csId: '',
        pageNumber: 0,
        pageItemSize: 20,
        totalCount: 0,
        pageCount: 0
    };

    function _init() {
        data.cpId = $('#cpId').val();
        data.csId = $('#csId').val();
        _initEvent();
        _search();
    }

    function _initEvent() {
        $('#btnSearch').on('click', function () {
            data.pageNumber = 0;
            _search();
        });

        $('#btnReset').on('click', function () {
            $('#cpId').val('');
            $('#csId').val('');
            $('#compNm').val('');
            $('#varNm').val('');
            $('#attrTp').val('Actual');
            data.pageNumber = 0;
            _search();
        });

        $(document).on('click', '.page-link', function (e) {
            e.preventDefault();
            let page = parseInt($(this).data('page'));
            if (!isNaN(page) && page !== data.pageNumber) {
                data.pageNumber = page;
                _search();
            }
        });
    }

    function _search() {
        let params = {
            cpId: $('#cpId').val().trim(),
            csId: $('#csId').val().trim(),
            compNm: $('#compNm').val().trim(),
            varNm: $('#varNm').val().trim(),
            attrTp: $('#attrTp').val(),
            pageNumber: data.pageNumber,
            pageItemSize: data.pageItemSize
        };

        $.ajax({
            type: 'GET',
            url: _ctx + '/ws/charger/csVariable/list',
            data: params,
            success: function (res) {
                if (!res) {
                    _clearTable();
                    return;
                }
                let criteria = res.criteria || {};
                let list = res.result || [];
                data.totalCount = criteria.totalItemCount || 0;
                data.pageCount  = criteria.pageCount || 1;
                data.pageNumber = criteria.pageNumber || 0;

                $('#totalCount').text(_commonMsg.totalCount.replace('{0}', data.totalCount));
                _renderTable(list, data.pageNumber, data.pageItemSize);
                _renderPaging();
            },
            error: function (xhr) {
                alert(_commonMsg.queryError);
            }
        });
    }

    function _renderTable(list, pageNumber, pageItemSize) {
        let $tbody = $('#tBodyList').empty();
        if (!list || list.length === 0) {
            $tbody.append('<tr><td colspan="13" style="text-align:center;">' + _commonMsg.noData + '</td></tr>');
            return;
        }
        let startNo = pageNumber * pageItemSize + 1;
        $.each(list, function (i, v) {
            let evse  = v.evseId  > 0 ? v.evseId  : '-';
            let conn  = v.connId  > 0 ? v.connId  : '-';
            let inst  = v.compInst  || '-';
            let vInst = v.varInst   || '-';
            let val   = v.attrVal   != null ? v.attrVal : '';
            let stat  = v.attrStat  || '';
            let upd   = v.updDt     ? v.updDt.replace('T', ' ').substring(0, 19) : '';

            let statClass = 'Accepted' === stat ? 'label-success' : 'label-warning';

            $tbody.append(
                '<tr>' +
                '<td>' + (startNo + i) + '</td>' +
                '<td>' + (v.cpId || '') + '</td>' +
                '<td>' + (v.csId || '') + '</td>' +
                '<td><strong>' + (v.compNm || '') + '</strong></td>' +
                '<td>' + inst + '</td>' +
                '<td>' + evse + '</td>' +
                '<td>' + conn + '</td>' +
                '<td>' + (v.varNm || '') + '</td>' +
                '<td>' + vInst + '</td>' +
                '<td><span class="label label-default">' + (v.attrTp || '') + '</span></td>' +
                '<td style="text-align:left; max-width:300px; word-break:break-all;">' + _escapeHtml(val) + '</td>' +
                '<td><span class="label ' + statClass + '">' + stat + '</span></td>' +
                '<td>' + upd + '</td>' +
                '</tr>'
            );
        });
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
        $('#tBodyList').html('<tr><td colspan="13" style="text-align:center;">' + _commonMsg.noData + '</td></tr>');
        $('#pagination').empty();
        $('#totalCount').text(_commonMsg.totalCount.replace('{0}', '0'));
    }

    function _escapeHtml(str) {
        if (!str) return '';
        return String(str)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;');
    }

    return { init: _init, search: _search };
}();

$(document).ready(function () {
    csVariableListJs.init();
});
