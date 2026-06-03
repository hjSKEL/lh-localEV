/**
 * 방전현황 (V2X) 목록.
 * REST: /ws/recharging/discharging
 */
var dischargingListJs = (function () {

    function _init() {
        $("#fromDate, #toDate").datepicker({ format: 'yyyy-mm-dd', autoclose: true, language: 'kr' });
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, dischargingListJs.search);
        $("#btnSearch").on("click", _onSearch);
        $("#btnReset").on("click", _onReset);
        _search(0);
    }

    function _onSearch() { _search(0); }

    function _onReset() {
        $("#fromDate, #toDate, #cpId, #csId, #evccId, #dcId").val('');
        $("#dchStatCode").val('');
        _search(0);
    }

    function _buildParam(pageNumber) {
        var p = "?pageNumber=" + (pageNumber || 0) + "&pageItemSize=20";
        var fromDate = $("#fromDate").val();
        var toDate = $("#toDate").val();
        var dch = $("#dchStatCode").val();
        var cpId = $("#cpId").val();
        var csId = $("#csId").val();
        var evccId = $("#evccId").val();
        var dcId = $("#dcId").val();
        if (fromDate) p += "&fromDate=" + encodeURIComponent(fromDate);
        if (toDate) p += "&toDate=" + encodeURIComponent(toDate);
        if (dch) p += "&status=" + encodeURIComponent(dch);
        if (cpId) p += "&cpId=" + encodeURIComponent(cpId);
        if (csId) p += "&csId=" + encodeURIComponent(csId);
        if (evccId) p += "&evccId=" + encodeURIComponent(evccId);
        if (dcId) p += "&dcId=" + encodeURIComponent(dcId);
        return p;
    }

    function _search(pageNumber) {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/recharging/discharging" + _buildParam(pageNumber),
            dataType: 'json',
            success: function (page) {
                _render(page);
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _render(page) {
        var $tbody = $("#dischargingTbody").empty();
        var list = (page && page.result) || [];
        if (list.length === 0) {
            $tbody.append('<tr><td colspan="14" style="text-align:center;">-</td></tr>');
        } else {
            for (var i = 0; i < list.length; i++) {
                var r = list[i];
                var html = '<tr>';
                html += '<td>' + (r.dcId || '') + '</td>';
                html += '<td>' + (r.cpName || r.cpId || '') + '</td>';
                html += '<td>' + (r.csId || '') + '</td>';
                html += '<td>' + (r.evseId || '') + '</td>';
                html += '<td>' + (r.evccId || '') + '</td>';
                html += '<td>' + (r.idTagType || '') + '</td>';
                html += '<td>' + ((r.carName || '') + (r.carNo ? ' (' + r.carNo + ')' : '')) + '</td>';
                html += '<td>' + (r.custName || '') + '</td>';
                html += '<td>' + (r.dchStartDateStr || '') + '</td>';
                html += '<td>' + (r.dchEndDateStr || '') + '</td>';
                html += '<td class="text-right">' + (r.dchUseAmount || 0) + '</td>';
                html += '<td class="text-right">' + (r.dchUseUnitCost || 0) + '</td>';
                html += '<td class="text-right">' + (r.dchUseCost || 0) + '</td>';
                html += '<td>' + _statusLabel(r.dchStatCode) + '</td>';
                html += '</tr>';
                $tbody.append(html);
            }
        }
        if (page && page.criteria) {
            pageInfoJs.setPage(page.criteria.pageNumber, page.criteria.totalItemCount, page.criteria.pageItemSize);
        }
    }

    function _statusLabel(code) {
        switch (code) {
            case 'DCSS01': return '시작';
            case 'DCSS02': return '방전중';
            case 'DCSS03': return '완료';
            case 'DCSS04': return '중단';
            case 'DCSS05': return '실패';
            default: return code || '';
        }
    }

    return {
        init: _init,
        search: _search
    };
})();
