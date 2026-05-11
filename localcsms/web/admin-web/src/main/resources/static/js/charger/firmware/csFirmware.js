/**
 * 충전기 펌웨어 업데이트 - 목록 페이지
 */
let csFirmwareJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    const STATUS_MAP = {
        'CSFW01': { label: _fwMsg.csfw01, cls: 'label-primary' },
        'CSFW02': { label: _fwMsg.csfw02, cls: 'label-info'    },
        'CSFW03': { label: _fwMsg.csfw03, cls: 'label-default' },
        'CSFW04': { label: _fwMsg.csfw04, cls: 'label-danger'  },
        'CSFW05': { label: _fwMsg.csfw05, cls: 'label-warning' },
        'CSFW06': { label: _fwMsg.csfw06, cls: 'label-success' },
        'CSFW07': { label: _fwMsg.csfw07, cls: 'label-danger'  }
    };

    function _init() {
        let initCpId = $("#hidCpId").val();
        let initCsId = $("#hidCsId").val();
        if (initCpId) $("#searchCpId").val(initCpId);
        if (initCsId) $("#searchCsId").val(initCsId);

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
    }

    function _resetClick() {
        $("#searchCpId").val("");
        $("#searchCsId").val("");
        $("#searchStatus").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        data.searchCond = {};

        let cpId   = $("#searchCpId").val().trim();
        let csId   = $("#searchCsId").val().trim();
        let status = $("#searchStatus").val();

        if (cpId)   data.searchCond.cpId   = cpId;
        if (csId)   data.searchCond.csId   = csId;
        if (status) data.searchCond.status = status;

        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, csFirmwareJs.search);
        _search();
    }

    function _search() {
        $("#tBodyList").empty().append(
            '<tr><td colspan="8" style="text-align:center;">' + _commonMsg.searching + '</td></tr>'
        );

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        if (data.searchCond.cpId)   param += "&cpId="   + data.searchCond.cpId;
        if (data.searchCond.csId)   param += "&csId="   + data.searchCond.csId;
        if (data.searchCond.status) param += "&status=" + data.searchCond.status;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/firmware/list" + param,
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
                '<tr><td colspan="8" style="text-align:center;">' + _commonMsg.noData + '</td></tr>'
            );
            return;
        }

        let result = jsonData.result;
        let paging = pageInfoJs.getPaging();
        let noIndex = (paging.pageNumber - 1) * paging.pageItemSize + 1;
        let html = '';

        for (let i = 0, len = result.length; i < len; i++) {
            let r = result[i];
            let reqDt = r.requestDate ? new Date(r.requestDate).toLocaleString('ko-KR') : '-';
            let updDt = r.updateDate  ? new Date(r.updateDate).toLocaleString('ko-KR')  : '-';
            let urlDisplay = r.url || '-';
            let urlTruncated = urlDisplay.length > 40 ? urlDisplay.substring(0, 40) + '...' : urlDisplay;

            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + (r.cpId || '-') + '</td>';
            html += '<td>' + (r.csId || '-') + '</td>';
            html += '<td style="text-align:left; max-width:300px; overflow:hidden; text-overflow:ellipsis;"'
                  + ' title="' + _escapeHtml(urlDisplay) + '">'
                  + _escapeHtml(urlTruncated) + '</td>';
            html += '<td>' + _statusBadge(r.status) + '</td>';
            html += '<td>' + (r.requestEmployeeId || '-') + '</td>';
            html += '<td>' + reqDt + '</td>';
            html += '<td>' + updDt + '</td>';
            html += '</tr>';
        }

        $("#tBodyList").append(html);
    }

    function _statusBadge(status) {
        if (!status) return '<span class="label label-default">-</span>';
        let info = STATUS_MAP[status];
        if (!info) return '<span class="label label-default">' + status + '</span>';
        return '<span class="label ' + info.cls + '">' + info.label + '</span>';
    }

    function _escapeHtml(str) {
        if (!str) return '';
        return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
                  .replace(/"/g, '&quot;').replace(/'/g, '&#039;');
    }

    return {
        init: _init,
        search: _search
    };
}();
