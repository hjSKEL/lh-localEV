/**
 * 고객조회
 */
let customerPopupJs = function () {
    "use strict";

    let cbFunc = undefined;

    function _init(callbackFunc) {
        cbFunc = callbackFunc;
        _initEvent();
        _searchOnClick();
    }

    function _init2(callbackFunc, type, sWord) {
		$('#Popup_Customer_searchType').val(type);
        $("#Popup_Customer_sWord").val(sWord);
        cbFunc = callbackFunc;
        _initEvent();
        _searchOnClick();
    }

    function _initEvent() {
        //
        $('#Popup_Customer_btnSearch').unbind('click');
        $("#Popup_Customer_btnSearch").click(function () {
            //
            _searchOnClick();
        });

        //검색조건 Enter키로 검색기능
        $("#Popup_Customer_sWord").keypress(function (event) {
            if (event.keyCode == 13) {
                _searchOnClick();
            }
        });
    }

    function _searchOnClick() {
        //
        Popup_Customer_pageInfoJs.init('Popup_Customer_pageInfoJs', 'Popup_Customer_pagingUl', 10, 10, customerPopupJs.search);

        _search();
    }

    function _search() {
        //
        $("#Popup_Customer_tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="6">' + _commonMsg.searching + '</td>';
        $("#Popup_Customer_tBodyList").append(html);

        let paging = Popup_Customer_pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
		let searchType = $('#Popup_Customer_searchType').val();
		let sWord = $('#Popup_Customer_sWord').val().trim();
		switch (searchType) {
			case 'A':
				param += "&custName=" + encodeURI(sWord);
				break;
			case 'B':
				param += "&customerId=" + sWord;
				break;
			case 'C':
				param += "&mblPhoneNo=" + sWord.replace(/-/g, '');
				break;
		}

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/search" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayCustomer(jsonData);
                //console.log(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            }
        });
    }

    function _displayCustomer(jsonData) {
        Popup_Customer_pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

        $("#Popup_Customer_tBodyList").empty();
        let html = '';
        if (jsonData.criteria.totalItemCount == 0) {
            html = '<tr style="text-align:center;">';
            html += '<td colspan="5">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#Popup_Customer_tBodyList").append(html);
            return;
        }

        let result = jsonData.result;
        for (let i = 0, length = result.length; i < length; ++i) {
            html = '<tr style="text-align:center;cursor:pointer;" onclick="customerPopupJs.selectSearch(\'' + result[i].customerId + '\',\'' + result[i].custName + '\')">';
            html += '<td>' + result[i].customerId + '</td>';
            html += '<td>' + ((result[i].dong || '') + '-' + (result[i].ho || '')) + '</td>';
            html += '<td>' + result[i].custName + '</td>';
            html += '<td>' + formmatUtilsJs.phoneFormat(result[i].mblPhoneNo) + '</td>';
            $("#Popup_Customer_tBodyList").append(html);
        }
    }

    function _selectSearch(id, name) {
        $('#Popup_Customer').modal('toggle');
        cbFunc({id: id, name: name});
    }

    return {
        init: _init,
        init2: _init2,
        search: _search,
        selectSearch: _selectSearch
    };
}();
