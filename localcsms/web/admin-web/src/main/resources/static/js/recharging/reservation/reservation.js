/**
 * 예약 관리 - 목록 페이지
 */
let reservationJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    function _init() {
        _initEvent();
        _searchOnClick();
    }

    function _initEvent() {
        $("#btnGoRegister").click(function () {
            _goRegister();
        });

        $("#btnSearch").click(function () {
            _searchOnClick();
        });

        $("#btnReset").click(function () {
            _resetClick();
        });

        $("#searchKey").keypress(function (event) {
            if (event.keyCode === 13) {
                _searchOnClick();
            }
        });

        $("input:radio[name='rsvStatus']").click(function () {
            _searchOnClick();
        });
    }

    function _resetClick() {
        $("#searchKey").val("");
        $("input:radio[name='rsvStatus'][value='']").prop('checked', true);
        _searchOnClick();
    }

    function _searchOnClick() {
        data.searchCond = {};

        let searchKey = $("#searchKey").val().trim();
        let sType = $("#sType").val();

        if (searchKey) {
            if (sType === 'CHARGER_ID') {
                let key = searchKey.replace(/-/g, '');
                if (key.length !== 8) {
                    toastr.warning(_msg.chargerIdDigit8, _msg.chargerId);
                    return;
                }
                data.searchCond.cpId = key.substring(0, 6);
                data.searchCond.csId = key.substring(6);
            } else {
                data.searchCond.cutCardNo = searchKey;
            }
        }

        let statusVal = $("input:radio[name='rsvStatus']:checked").val();
        if (statusVal) {
            data.searchCond.status = statusVal;
        }

        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, reservationJs.search);
        _search();
    }

    function _search() {
        $("#tBodyList").empty().append(
            '<tr style="text-align:center;"><td colspan="9">' + _commonMsg.searching + '</td></tr>'
        );

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        if (data.searchCond.cpId) param += "&cpId=" + data.searchCond.cpId;
        if (data.searchCond.csId) param += "&csId=" + data.searchCond.csId;
        if (data.searchCond.cutCardNo) param += "&cutCardNo=" + data.searchCond.cutCardNo;
        if (data.searchCond.status) param += "&status=" + data.searchCond.status;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/reservation/list" + param,
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
            let expiredDate = r.expiredDate ? new Date(r.expiredDate).toLocaleString('ko-KR') : '-';
            let regDate = (r.writer && r.writer.registrationDate)
                ? new Date(r.writer.registrationDate).toLocaleString('ko-KR') : '-';

            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td><a href="#" onclick="reservationJs.goDetail(' + r.rsvId + ', \'' + r.cpId + '\', \'' + r.csId + '\'); return false;">' + r.rsvId + '</a></td>';
            html += '<td>' + (r.cpId || '-') + '</td>';
            html += '<td>' + (r.csId || '-') + '</td>';
            html += '<td>' + (r.cutCardNo || '-') + '</td>';
            html += '<td>' + (r.parentCardNo || '-') + '</td>';
            html += '<td>' + expiredDate + '</td>';
            html += '<td>' + statusBadge + '</td>';
            html += '<td>' + regDate + '</td>';
            html += '</tr>';
        }

        $("#tBodyList").append(html);
    }

    function _statusBadge(status) {
        if (status === 'RSVT01') return '<span class="label label-primary"' + _msg.statusReserved + '</span>';
        if (status === 'RSVT02') return '<span class="label label-danger"' + _msg.statusCancelled + '</span>';
        return '<span class="label label-default">' + (status || '-') + '</span>';
    }

    function _goDetail(rsvId, cpId, csId) {
        window.location.href = _ctx + "/reservation/detail?rsvId=" + rsvId
            + "&cpId=" + cpId + "&csId=" + csId;
    }

    function _goRegister() {
        window.location.href = _ctx + "/reservation/detail";
    }

    return {
        init: _init,
        search: _search,
        goDetail: _goDetail,
        goRegister: _goRegister
    };
}();
