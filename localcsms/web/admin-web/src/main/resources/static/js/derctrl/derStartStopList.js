/**
 * DER Start/Stop 이벤트 이력
 */
var derStartStopListJs = function () {
    "use strict";
    var data = { searchCond: {}, result: [] };

    function _init() {
        $("#startedYn").html('<option value="">'+_msg.startedAll+'</option><option value="Y">'+_msg.startedY+'</option><option value="N">'+_msg.startedN+'</option>');
        $("#btnSearch").click(_searchOnClick);
        $("#btnReset").click(function () { $("#fCpId,#fCsId,#fControlId").val(""); $("#startedYn").val(""); _searchOnClick(); });
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, derStartStopListJs.search);
        data.searchCond = {
            cpId: $("#fCpId").val().trim(), csId: $("#fCsId").val().trim(),
            controlId: $("#fControlId").val().trim(), startedYn: $("#startedYn").val()
        };
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr><td colspan="7" style="text-align:center;">' + _commonMsg.searching + '</td></tr>');
        var p = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (p.pageNumber - 1) + "&pageItemSize=" + p.pageItemSize;
        param += "&cpId="      + encodeURIComponent(data.searchCond.cpId || '');
        param += "&csId="      + encodeURIComponent(data.searchCond.csId || '');
        param += "&controlId=" + encodeURIComponent(data.searchCond.controlId || '');
        param += "&startedYn=" + encodeURIComponent(data.searchCond.startedYn || '');

        $.ajax({
            type: 'GET', url: _ctx + "/ws/derctrl/startstop" + param, dataType: 'json',
            success: function (j) {
                pageInfoJs.setTotalCount(j.criteria.totalItemCount);
                $("#totalCount").html(_commonMsg.totalCount.replace('{0}', j.criteria.totalItemCount));
                $("#tBodyList").empty();
                if (j.criteria.totalItemCount == 0) {
                    $("#tBodyList").append('<tr><td colspan="7" style="text-align:center;">' + _commonMsg.noData + '</td></tr>');
                    return;
                }
                var no = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
                var html = '';
                for (var i = 0; i < j.result.length; i++) {
                    var r = j.result[i];
                    html += '<tr>';
                    html += '<td>' + (i + no) + '</td>';
                    html += '<td>' + r.seq + '</td>';
                    html += '<td>' + r.cpId + ' / ' + r.csId + '</td>';
                    html += '<td>' + (r.controlId || '-') + '</td>';
                    html += '<td>' + (r.startedYn === 'Y' ? '<span class="label label-primary">Start</span>' : '<span class="label label-default">Stop</span>') + '</td>';
                    html += '<td>' + _fmt(r.timestampDt) + '</td>';
                    html += '<td>' + _fmt(r.receivedDate) + '</td>';
                    html += '</tr>';
                }
                $("#tBodyList").append(html);
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _fmt(v) {
        if (!v) return '-';
        var d = new Date(v);
        return d.getFullYear() + '-' + ('0'+(d.getMonth()+1)).slice(-2) + '-' + ('0'+d.getDate()).slice(-2)
                + ' ' + ('0'+d.getHours()).slice(-2) + ':' + ('0'+d.getMinutes()).slice(-2) + ':' + ('0'+d.getSeconds()).slice(-2);
    }

    return { init: _init, search: _search };
}();
