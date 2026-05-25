/**
 * DER 알람 이력 조회
 */
var derAlarmListJs = function () {
    "use strict";
    var data = { searchCond: {}, result: [] };

    function _init() {
        var types = ["EnterService","FreqDroop","FreqWatt","FixedPFAbsorb","FixedPFInject","FixedVar",
            "Gradients","LimitMaxDischarge","VoltWatt","VoltVar","WattPF","WattVar",
            "HFMustTrip","HFMayTrip","HVMustTrip","HVMomCess","HVMayTrip",
            "LFMustTrip","LVMustTrip","LVMomCess","LVMayTrip","PowerMonitoringMustTrip"];
        var html = '<option value="">' + _msg.typeAll + '</option>';
        for (var i = 0; i < types.length; i++) html += '<option value="' + types[i] + '">' + types[i] + '</option>';
        $("#controlType").html(html);
        $("#alarmEnded").html('<option value="">'+_msg.endedAll+'</option><option value="N">'+_msg.endedN+'</option><option value="Y">'+_msg.endedY+'</option>');

        $("#btnSearch").click(_searchOnClick);
        $("#btnReset").click(function () { $("#fCpId,#fCsId,#fromDate,#toDate").val(""); $("#controlType,#alarmEnded").val(""); _searchOnClick(); });
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, derAlarmListJs.search);
        data.searchCond = {
            cpId: $("#fCpId").val().trim(), csId: $("#fCsId").val().trim(),
            controlType: $("#controlType").val(), alarmEnded: $("#alarmEnded").val(),
            fromDate: _date($("#fromDate").val(), '00:00:00'),
            toDate:   _date($("#toDate").val(),   '23:59:59')
        };
        _search();
    }

    function _date(d, s) { return d ? d + ' ' + s : ''; }

    function _search() {
        $("#tBodyList").empty();
        $("#tBodyList").append('<tr><td colspan="8" style="text-align:center;">' + _commonMsg.searching + '</td></tr>');
        var p = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (p.pageNumber - 1) + "&pageItemSize=" + p.pageItemSize;
        param += "&cpId="        + encodeURIComponent(data.searchCond.cpId || '');
        param += "&csId="        + encodeURIComponent(data.searchCond.csId || '');
        param += "&controlType=" + encodeURIComponent(data.searchCond.controlType || '');
        param += "&alarmEnded="  + encodeURIComponent(data.searchCond.alarmEnded || '');
        if (data.searchCond.fromDate) param += "&fromDate=" + encodeURIComponent(data.searchCond.fromDate);
        if (data.searchCond.toDate)   param += "&toDate="   + encodeURIComponent(data.searchCond.toDate);

        $.ajax({
            type: 'GET', url: _ctx + "/ws/derctrl/alarm" + param, dataType: 'json',
            success: function (j) {
                pageInfoJs.setTotalCount(j.criteria.totalItemCount);
                $("#totalCount").html(_commonMsg.totalCount.replace('{0}', j.criteria.totalItemCount));
                $("#tBodyList").empty();
                if (j.criteria.totalItemCount == 0) {
                    $("#tBodyList").append('<tr><td colspan="8" style="text-align:center;">' + _commonMsg.noData + '</td></tr>');
                    return;
                }
                data.result = j.result;
                var no = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
                var html = '';
                for (var i = 0; i < j.result.length; i++) {
                    var r = j.result[i];
                    html += '<tr>';
                    html += '<td>' + (i + no) + '</td>';
                    html += '<td>' + r.seq + '</td>';
                    html += '<td>' + r.cpId + ' / ' + r.csId + '</td>';
                    html += '<td>' + r.controlType + '</td>';
                    html += '<td>' + (r.gridEventFault || '-') + '</td>';
                    html += '<td>' + (r.alarmEnded === 'Y' ? '<span class="label label-default">Ended</span>' : '<span class="label label-danger">Start</span>') + '</td>';
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
