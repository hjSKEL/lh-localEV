/**
 * 배터리 교체 기록 상세 (페이징 없음)
 */
var batterySwapRecordJs = function () {
    "use strict";

    var state = {
        requestId: null
    };

    var STATUS_BADGE = {
        IN:      { cls: 'label-warning', txt: 'IN' },
        OUT:     { cls: 'label-primary', txt: 'OUT' },
        Timeout: { cls: 'label-danger',  txt: 'Timeout' }
    };

    function _init(requestId) {
        if (!requestId) {
            toastr.error(_commonMsg.dataNotFound, _msg.recordMgmt);
            return;
        }
        state.requestId = requestId;
        $("#hdr_requestId").text(requestId);

        $("#btnList").click(function () { self.location = _ctx + "/recharging/batterySwapRecord/list"; });
        $("#btnSlotHistory").click(function () {
            self.location = _ctx + "/charger/swapSlot/his/list?eventRefId=" + encodeURIComponent(state.requestId);
        });

        _load();
    }

    function _load() {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/recharging/batterySwapRecord/" + encodeURIComponent(state.requestId),
            dataType: 'json',
            success: function (record) {
                if (!record) {
                    toastr.error(_commonMsg.dataNotFound, _msg.recordMgmt);
                    $("#tBodyDetails").html('<tr style="text-align:center;"><td colspan="7">' + _commonMsg.noData + '</td></tr>');
                    return;
                }
                _renderHeader(record);
                _renderDetails(record.details || []);
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _renderHeader(record) {
        $("#cell_requestId").text(record.requestId);
        $("#cell_status").html(_statusBadge(record.status));
        $("#cell_cp").text(record.cpId + (record.cpName ? ' ' + record.cpName : ''));
        $("#cell_cs").text(record.csId);

        var inDt = record.inDateTime ? _formatDateTime(record.inDateTime) : '-';
        var outDt;
        if (record.outDateTime) {
            outDt = _formatDateTime(record.outDateTime);
        } else if (record.status === 'Timeout') {
            outDt = '<span style="color:#dd6b55;">-</span>';
        } else {
            outDt = '-';
        }
        $("#cell_inDt").text(inDt);
        $("#cell_outDt").html(outDt);

        $("#cell_inCustomer").text(_customerCell(record.inCustomerName, record.inCustomerId));
        $("#cell_outCustomer").text(_customerCell(record.outCustomerName, record.outCustomerId));

        var cntMismatch = (record.status === 'OUT' && record.inCount !== record.outCount);
        $("#cell_inCnt").html(_cntCell(record.inCount, cntMismatch));
        $("#cell_outCnt").html(_cntCell(record.outCount, cntMismatch));

        $("#cell_duration").text(_durationText(record.inDateTime, record.outDateTime));
    }

    function _renderDetails(details) {
        var $tb = $("#tBodyDetails").empty();
        if (!details || details.length === 0) {
            $tb.append('<tr style="text-align:center;"><td colspan="7">' + _commonMsg.noData + '</td></tr>');
            _setAvg('in', null, null);
            _setAvg('out', null, null);
            return;
        }

        var inSocSum = 0, inSohSum = 0, inN = 0;
        var outSocSum = 0, outSohSum = 0, outN = 0;
        var html = '';
        for (var i = 0; i < details.length; ++i) {
            var d = details[i];
            var unfit = (d.type === 'OUT' && d.soh != null && Number(d.soh) < 80);
            html += '<tr' + (unfit ? ' style="background-color:#fdecea;"' : '') + '>';
            html += '<td>' + (i + 1) + '</td>';
            html += '<td>' + _typeBadge(d.type) + '</td>';
            html += '<td>' + d.evseId + '</td>';
            html += '<td>' + (d.serialNumber || '-') + '</td>';
            html += '<td style="text-align:right;">' + (d.soc != null ? d.soc : '-') + '</td>';
            html += '<td style="text-align:right;' + (unfit ? 'color:#dd6b55;' : '') + '">' + (d.soh != null ? d.soh : '-') + '</td>';
            html += '<td>' + _formatDate(d.productionDate) + '</td>';
            html += '</tr>';

            if (d.type === 'IN') {
                if (d.soc != null) { inSocSum += Number(d.soc); inN++; }
                if (d.soh != null) { inSohSum += Number(d.soh); }
            } else if (d.type === 'OUT') {
                if (d.soc != null) { outSocSum += Number(d.soc); outN++; }
                if (d.soh != null) { outSohSum += Number(d.soh); }
            }
        }
        $tb.append(html);

        _setAvg('in', inN > 0 ? (inSocSum / inN) : null, inN > 0 ? (inSohSum / inN) : null);
        _setAvg('out', outN > 0 ? (outSocSum / outN) : null, outN > 0 ? (outSohSum / outN) : null);
    }

    function _setAvg(prefix, soc, soh) {
        $("#avg_" + prefix + "_soc").text(soc != null ? soc.toFixed(2) : '-');
        $("#avg_" + prefix + "_soh").text(soh != null ? soh.toFixed(2) : '-');
    }

    function _statusBadge(code) {
        var meta = STATUS_BADGE[code];
        if (!meta) return code || '-';
        return '<span class="label ' + meta.cls + '">' + meta.txt + '</span>';
    }

    function _typeBadge(t) {
        if (t === 'IN')  return '<span class="label label-warning">IN</span>';
        if (t === 'OUT') return '<span class="label label-primary">OUT</span>';
        return t || '-';
    }

    function _customerCell(name, id) {
        if (!name && !id) return '-';
        if (name && id) return name + ' (' + id + ')';
        return name || id;
    }

    function _cntCell(n, mismatch) {
        var v = (n != null ? n : 0);
        return mismatch
            ? '<span style="color:#dd6b55;">' + v + ' ⚠</span>'
            : ('' + v);
    }

    function _durationText(from, to) {
        if (!from || !to) return '-';
        var ms = new Date(to).getTime() - new Date(from).getTime();
        if (ms < 0) return '-';
        var sec = Math.floor(ms / 1000);
        var min = Math.floor(sec / 60);
        var hr  = Math.floor(min / 60);
        if (hr > 0)  return hr + '시간 ' + (min % 60) + '분';
        if (min > 0) return min + '분 ' + (sec % 60) + '초';
        return sec + '초';
    }

    function _formatDate(val) {
        if (!val) return '-';
        var dt = new Date(val);
        var y = dt.getFullYear();
        var m = ('0' + (dt.getMonth() + 1)).slice(-2);
        var d = ('0' + dt.getDate()).slice(-2);
        return y + '-' + m + '-' + d;
    }

    function _formatDateTime(val) {
        if (!val) return '-';
        var dt = new Date(val);
        var hh = ('0' + dt.getHours()).slice(-2);
        var mm = ('0' + dt.getMinutes()).slice(-2);
        var ss = ('0' + dt.getSeconds()).slice(-2);
        return _formatDate(dt) + ' ' + hh + ':' + mm + ':' + ss;
    }

    return {
        init: _init
    };
}();
