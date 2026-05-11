/**
 * 고객 충전 이력
 */
let customerRechargingPopupJs = function () {
    "use strict";

    let data = {};

    function _init(customerId) {
        data.customerId = customerId;
        _searchOnClick();
    }

    function _searchOnClick() {
        Popup_ChargerRecharging_pageInfoJs.init('Popup_ChargerRecharging_pageInfoJs', 'Popup_ChargerRecharging_pagingUl', 10, 10, customerRechargingPopupJs.search);
        _search();
    }

    function _search() {
        let paging = Popup_ChargerRecharging_pageInfoJs.getPaging();
        console.log(paging.pageItemSize);
        let param = '?pageNumber=' + (paging.pageNumber - 1) + '&pageItemSize=' + paging.pageItemSize;
        param += '&customerId=' + data.customerId;

        $.ajax({
            type: 'GET',
            url: _ctx + '/ws/recharging/list' + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _display(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            }
        });
    }

    function _display(jsonData) {
        Popup_ChargerRecharging_pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
        if (jsonData.criteria.totalItemCount === 0) {
            $('#Popup_ChargerRecharging_tBodyList').empty();
            let html = '<tr style="text-align:center;">';
            html += '<td colspan="7">' + _msg.noHistory + '</td>'
            html += '</tr>';
            $('#Popup_ChargerRecharging_tBodyList').append(html);
            return;
        }
        $('#Popup_ChargerRecharging_tBodyList').empty();
        for (let i = 0, length = jsonData.result.length; i < length; i++) {
            let html = '<tr style="text-align:center;">';
            html += '<td>' + jsonData.result[i].rechargingId + '</td>'
            let chStartDate = new Date(jsonData.result[i].chStartDate);
            html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(chStartDate), 'YYYY-MM-DD HH:MM') + '</td>'
            if (jsonData.result[i].chEndDate) {
                let chEndDate = new Date(jsonData.result[i].chEndDate);
                html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(chEndDate), 'YYYY-MM-DD HH:MM') + '</td>'
            } else {
                html += '<td></td>'
            }
            html += '<td>' + jsonData.result[i].cpId + '-' + jsonData.result[i].csId + '</td>'
            html += '<td>' + (parent.commonCodeJs.getCodeNameBySubCode(jsonData.result[i].chStatCode)) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(jsonData.result[i].chUseAmount) + 'kWh</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(jsonData.result[i].paySum) + _msg.won + '</td>';
            html += '</tr>';
            $('#Popup_ChargerRecharging_tBodyList').append(html);
        }
    }

    return {
        init: _init,
        search: _search,
    };
}();
