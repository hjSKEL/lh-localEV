/**
 * 충전기 디스플레이 메시지 - 목록 페이지
 */
let displayMessageJs = function () {
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

        $("#btnGoRegister").click(function () {
            let cpId = $("#searchCpId").val().trim();
            let csId = $("#searchCsId").val().trim();
            let url = _ctx + "/charger/displayMessage/detail?mode=new";
            if (cpId) url += "&cpId=" + cpId;
            if (csId) url += "&csId=" + csId;
            window.location.href = url;
        });
    }

    function _resetClick() {
        $("#searchCpId").val("");
        $("#searchCsId").val("");
        $("#searchPriority").val("");
        $("#searchStatus").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        data.searchCond = {};

        let cpId     = $("#searchCpId").val().trim();
        let csId     = $("#searchCsId").val().trim();
        let priority = $("#searchPriority").val();
        let status   = $("#searchStatus").val();

        if (cpId)     data.searchCond.cpId     = cpId;
        if (csId)     data.searchCond.csId     = csId;
        if (priority) data.searchCond.priority = priority;
        if (status)   data.searchCond.status   = status;

        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, displayMessageJs.search);
        _search();
    }

    function _search() {
        $("#tBodyList").empty().append(
            '<tr><td colspan="10" style="text-align:center;">' + _commonMsg.searching + '</td></tr>'
        );

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        if (data.searchCond.cpId)     param += "&cpId="     + data.searchCond.cpId;
        if (data.searchCond.csId)     param += "&csId="     + data.searchCond.csId;
        if (data.searchCond.priority) param += "&priority=" + data.searchCond.priority;
        if (data.searchCond.status)   param += "&status="   + data.searchCond.status;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/displayMessage/list" + param,
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
                '<tr><td colspan="10" style="text-align:center;">' + _commonMsg.noData + '</td></tr>'
            );
            return;
        }

        let result = jsonData.result;
        let paging = pageInfoJs.getPaging();
        let noIndex = (paging.pageNumber - 1) * paging.pageItemSize + 1;
        let html = '';

        for (let i = 0, len = result.length; i < len; i++) {
            let r = result[i];
            let regDt = r.writer && r.writer.registrationDate
                ? new Date(r.writer.registrationDate).toLocaleString('ko-KR') : '-';
            let startDate = r.startDate ? new Date(r.startDate).toLocaleString('ko-KR') : '-';
            let endDate   = r.endDate   ? new Date(r.endDate).toLocaleString('ko-KR')   : '-';

            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td style="font-family:monospace;">'
                + '<a href="#" onclick="displayMessageJs.goDetail(' + r.messageId + ',\''
                + r.cpId + '\',\'' + r.csId + '\'); return false;">'
                + r.messageId + '</a></td>';
            html += '<td>' + (r.cpId || '-') + '</td>';
            html += '<td>' + (r.csId || '-') + '</td>';
            html += '<td>' + _priorityBadge(r.priority) + '</td>';
            html += '<td>' + _statusBadge(r.status) + '</td>';
            html += '<td>' + startDate + '</td>';
            html += '<td>' + endDate + '</td>';
            html += '<td>' + (r.csStatus || '-') + '</td>';
            html += '<td>' + regDt + '</td>';
            html += '</tr>';
        }

        $("#tBodyList").append(html);
    }

    function _priorityBadge(priority) {
        if (priority === 'AlwaysFront')  return '<span class="label label-danger">AlwaysFront</span>';
        if (priority === 'InFront')      return '<span class="label label-warning">InFront</span>';
        if (priority === 'NormalCycle')  return '<span class="label label-default">NormalCycle</span>';
        return '<span class="label label-default">' + (priority || '-') + '</span>';
    }

    function _statusBadge(status) {
        if (status === 'DMST01') return '<span class="label label-primary">' + _msg.statusActive + '</span>';
        if (status === 'DMST02') return '<span class="label label-warning">' + _msg.statusPending + '</span>';
        if (status === 'DMST03') return '<span class="label label-default">' + _msg.statusCompleted + '</span>';
        if (status === 'DMST04') return '<span class="label label-default">' + _msg.statusCancelled + '</span>';
        if (status === 'DMST05') return '<span class="label label-default">' + _msg.statusExpired + '</span>';
        if (status === 'DMST06') return '<span class="label label-danger">' + _msg.statusError + '</span>';
        return '<span class="label label-default">' + (status || '-') + '</span>';
    }

    function _goDetail(messageId, cpId, csId) {
        window.location.href = _ctx + "/charger/displayMessage/detail?messageId=" + messageId
            + "&cpId=" + cpId + "&csId=" + csId;
    }

    return {
        init: _init,
        search: _search,
        goDetail: _goDetail
    };
}();
