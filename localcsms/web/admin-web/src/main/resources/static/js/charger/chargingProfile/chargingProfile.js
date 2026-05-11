/**
 * 충전 프로파일 - 목록 페이지
 */
let chargingProfileJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    // Jackson serializes enum as name() – map both code and name to display label
    const PURPOSE_LABEL = {
        'CHPP01': 'ChargingStationMaxProfile', 'ChargingStationMaxProfile': 'ChargingStationMaxProfile',
        'CHPP02': 'TxDefaultProfile',          'TxDefaultProfile': 'TxDefaultProfile',
        'CHPP03': 'TxProfile',                 'TxProfile': 'TxProfile',
        'CHPP04': 'PriorityCharging',          'PriorityCharging': 'PriorityCharging',
        'CHPP05': 'LocalGeneration',           'LocalGeneration': 'LocalGeneration',
        'CHPP06': 'ChargingStationExternalConstraints', 'ChargingStationExternalConstraints': 'ChargingStationExternalConstraints'
    };

    const KIND_LABEL = {
        'CHKD01': 'Absolute',  'Absolute': 'Absolute',
        'CHKD02': 'Recurring', 'Recurring': 'Recurring',
        'CHKD03': 'Relative',  'Relative': 'Relative'
    };

    function _init() {
        // URL 파라미터로 전달된 초기값 반영
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

        $("#btnGoRegister").click(function () {
            let cpId = $("#searchCpId").val().trim();
            let csId = $("#searchCsId").val().trim();
            let url = _ctx + "/charger/chargingProfile/detail?mode=new";
            if (cpId) url += "&cpId=" + cpId;
            if (csId) url += "&csId=" + csId;
            window.location.href = url;
        });
    }

    function _resetClick() {
        $("#searchCpId").val("");
        $("#searchCsId").val("");
        $("#searchPurpose").val("");
        _searchOnClick();
    }

    function _searchOnClick() {
        data.searchCond = {};

        let cpId    = $("#searchCpId").val().trim();
        let csId    = $("#searchCsId").val().trim();
        let purpose = $("#searchPurpose").val();

        if (cpId) data.searchCond.cpId = cpId;
        if (csId) data.searchCond.csId = csId;
        if (purpose) data.searchCond.purpose = purpose;

        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, chargingProfileJs.search);
        _search();
    }

    function _search() {
        $("#tBodyList").empty().append(
            '<tr><td colspan="12" style="text-align:center;">' + _commonMsg.searching + '</td></tr>'
        );

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        if (data.searchCond.cpId)    param += "&cpId="    + data.searchCond.cpId;
        if (data.searchCond.csId)    param += "&csId="    + data.searchCond.csId;
        if (data.searchCond.purpose) param += "&purpose=" + data.searchCond.purpose;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/chargingProfile/list" + param,
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
                '<tr><td colspan="12" style="text-align:center;">' + _commonMsg.noData + '</td></tr>'
            );
            return;
        }

        let result = jsonData.result;
        let paging = pageInfoJs.getPaging();
        let noIndex = (paging.pageNumber - 1) * paging.pageItemSize + 1;
        let html = '';

        for (let i = 0, len = result.length; i < len; i++) {
            let r = result[i];
            let regDt    = r.writer && r.writer.registrationDate ? new Date(r.writer.registrationDate).toLocaleString('ko-KR') : '-';
            let validFrom = r.validFrom ? new Date(r.validFrom).toLocaleString('ko-KR') : '-';
            let validTo   = r.validTo   ? new Date(r.validTo).toLocaleString('ko-KR')   : '-';

            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td style="font-family:monospace;">'
                + '<a href="#" onclick="chargingProfileJs.goDetail(' + r.profileId + ',\''
                + r.cpId + '\',\'' + r.csId + '\'); return false;">'
                + r.profileId + '</a></td>';
            html += '<td>' + (r.cpId || '-') + '</td>';
            html += '<td>' + (r.csId || '-') + '</td>';
            html += '<td>' + r.evseId + '</td>';
            html += '<td>' + r.stackLevel + '</td>';
            html += '<td>' + _purposeBadge(r.purpose) + '</td>';
            html += '<td>' + _kindBadge(r.kind) + '</td>';
            html += '<td>' + validFrom + '</td>';
            html += '<td>' + validTo + '</td>';
            html += '<td>' + (r.csStatus || '-') + '</td>';
            html += '<td>' + regDt + '</td>';
            html += '</tr>';
        }

        $("#tBodyList").append(html);
    }

    function _purposeBadge(purpose) {
        if (!purpose) return '<span class="label label-default">-</span>';
        // Jackson serializes enum as name() e.g. "ChargingStationMaxProfile"
        let key = (typeof purpose === 'object') ? purpose.code : purpose;
        let label = PURPOSE_LABEL[key] || key;
        let cls = 'label-default';
        if (key === 'CHPP01' || key === 'ChargingStationMaxProfile') cls = 'label-danger';
        else if (key === 'CHPP02' || key === 'TxDefaultProfile')     cls = 'label-warning';
        else if (key === 'CHPP03' || key === 'TxProfile')            cls = 'label-info';
        else if (key === 'CHPP04' || key === 'PriorityCharging')     cls = 'label-primary';
        return '<span class="label ' + cls + '">' + label + '</span>';
    }

    function _kindBadge(kind) {
        if (!kind) return '<span class="label label-default">-</span>';
        let key = (typeof kind === 'object') ? kind.code : kind;
        let label = KIND_LABEL[key] || key;
        return '<span class="label label-default">' + label + '</span>';
    }

    function _goDetail(profileId, cpId, csId) {
        window.location.href = _ctx + "/charger/chargingProfile/detail?profileId=" + profileId
            + "&cpId=" + cpId + "&csId=" + csId;
    }

    return {
        init: _init,
        search: _search,
        goDetail: _goDetail
    };
}();
