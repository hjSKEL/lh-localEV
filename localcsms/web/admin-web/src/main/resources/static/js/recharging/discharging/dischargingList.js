/**
 * 방전현황 (V2X) 목록.
 * REST: /ws/recharging/discharging
 */
var dischargingListJs = (function () {

    var data = { searchCond: {} };

    function _init() {
        $("#date1").val(dateUtilsJs.formatDate(dateUtilsJs.addDay(new Date(), -7), "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.currentDate("YYYY-MM-DD"));

        $('#date1, #date2').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm-dd"
        });

        $("#btnSearch").click(_searchOnClick);
        $("#btnReset").click(_searchResetClick);
        $("#searchKey").keypress(function (event) {
            if (event.keyCode == 13) {
                _searchOnClick();
            }
        });
        $("input:radio[name='dchStatus']").click(_searchOnClick);
        $("#dateOrder").change(_searchOnClick);

        _searchOnClick();
    }

    function _searchResetClick() {
        $("#date1").val(dateUtilsJs.formatDate(dateUtilsJs.addDay(new Date(), -7), "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        $("#searchKey").val("");
        $("input:radio[name='dchStatus'][value='']").prop('checked', true);
        $("#dateOrder").val("B");
        $("#dateType").val("S");
        $("#sType").val("CP_ID");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, dischargingListJs.search);
        data.searchCond = { cpId: "", csId: "", evccId: "", dcId: "" };
        data.searchCond.dateType = $("#dateType").val();
        data.searchCond.dateOrder = $("#dateOrder").val();
        data.searchCond.status = $("input:radio[name='dchStatus']:checked").val();

        var key = $("#searchKey").val().trim();
        if (key) {
            switch ($("#sType").val()) {
                case 'CP_ID':   data.searchCond.cpId = key; break;
                case 'CS_ID':   data.searchCond.csId = key; break;
                case 'EVCC_ID': data.searchCond.evccId = key; break;
                case 'DC_ID':   data.searchCond.dcId = key; break;
            }
        }
        data.searchCond.fromDate = $("#date1").val();
        data.searchCond.toDate = $("#date2").val();
        _search();
    }

    function _buildParam() {
        var paging = pageInfoJs.getPaging();
        var c = data.searchCond;
        var p = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        p += "&dateType=" + encodeURIComponent(c.dateType || 'S');
        p += "&dateOrder=" + encodeURIComponent(c.dateOrder || 'B');
        if (c.status) p += "&status=" + encodeURIComponent(c.status);
        if (c.cpId)   p += "&cpId=" + encodeURIComponent(c.cpId);
        if (c.csId)   p += "&csId=" + encodeURIComponent(c.csId);
        if (c.evccId) p += "&evccId=" + encodeURIComponent(c.evccId);
        if (c.dcId)   p += "&dcId=" + encodeURIComponent(c.dcId);
        if (c.fromDate) p += "&fromDate=" + encodeURIComponent(c.fromDate);
        if (c.toDate)   p += "&toDate=" + encodeURIComponent(c.toDate);
        return p;
    }

    function _search() {
        $("#dischargingTbody").html('<tr style="text-align:center;"><td colspan="15">' + _commonMsg.searching + '</td></tr>');
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/recharging/discharging" + _buildParam(),
            dataType: 'json',
            success: function (page) {
                _render(page);
            },
            error: function (xhr) {
                parent.layerJs.fn_exception(xhr);
            }
        });
    }

    function _render(page) {
        var totalCount = (page && page.criteria) ? page.criteria.totalItemCount : 0;
        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', totalCount));

        var $tbody = $("#dischargingTbody").empty();
        var list = (page && page.result) || [];
        if (list.length === 0) {
            $tbody.append('<tr style="text-align:center;"><td colspan="15">' + _commonMsg.noData + '</td></tr>');
        } else {
            var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
            for (var i = 0; i < list.length; i++) {
                var r = list[i];
                var html = '<tr>';
                html += '<td>' + (i + noIndex) + '</td>';
                html += '<td>' + (r.dcId || '') + '</td>';
                html += '<td>' + (r.cpName || r.cpId || '') + '</td>';
                html += '<td>' + (r.csId || '') + '</td>';
                html += '<td>' + (r.evseId || '') + '</td>';
                html += '<td>' + (r.evccId || '') + '</td>';
                html += '<td>' + (r.idTagType || '') + '</td>';
                html += '<td>' + (r.custName || '') + '</td>';
                html += '<td>' + (r.dchStartDateStr || '') + '</td>';
                html += '<td>' + (r.dchEndDateStr || '') + '</td>';
                html += '<td style="text-align:right;">' + (r.dchUseAmount || 0) + '</td>';
                html += '<td style="text-align:right;">' + (r.dchUseUnitCost || 0) + '</td>';
                html += '<td style="text-align:right;">' + (r.dchUseCost || 0) + '</td>';
                html += '<td>' + _statusLabel(r.dchStatCode) + '</td>';
                html += '</tr>';
                $tbody.append(html);
            }
        }
        if (page && page.criteria) {
            pageInfoJs.setTotalCount(totalCount);
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
