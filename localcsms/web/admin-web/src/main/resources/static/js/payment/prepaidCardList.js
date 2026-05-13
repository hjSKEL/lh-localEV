/**
 * 선불카드 관리
 */
var prepaidCardListJs = function () {
    "use strict";

    var data = {
        searchCond: {},
        result: []
    };

    function _init() {
        _initStatusSelect();
        _initEvent();
        _initExpireDatePicker();
        _searchOnClick();
    }

    function _initStatusSelect() {
        var html = '<option value="">' + _msg.cardStatusAll + '</option>';
        html += '<option value="PPCS01">' + _msg.cardStatActive + '</option>';
        html += '<option value="PPCS02">' + _msg.cardStatStopped + '</option>';
        html += '<option value="PPCS03">' + _msg.cardStatExpired + '</option>';
        $("#status").html(html);
    }

    function _initExpireDatePicker() {
        $('#issue_expireDate').datepicker({
            todayBtn: "linked",
            autoclose: true,
            format: "yyyy-mm-dd"
        });
    }

    function _initEvent() {
        $("#btnSearch").click(_searchOnClick);
        $("#status").change(_searchOnClick);
        $("#btnReset").click(_searchResetClick);
        $("#sWord").keypress(function (event) {
            if (event.keyCode == 13) _searchOnClick();
        });

        $("#btnIssueCard").click(_openIssueModal);
        $("#btnSelectCustomer").click(function () {
            $('#Popup_Customer').modal();
            customerPopupJs.init(_onCustomerSelected);
        });
        $("#btnIssueSave").click(_submitIssue);
    }

    function _searchResetClick() {
        $("#sWord").val("");
        $("#status").val("");
        $("#searchType").val("CARDNO");
        _searchOnClick();
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, prepaidCardListJs.search);
        data.searchCond.cardNo = "";
        data.searchCond.customerId = "";
        data.searchCond.customerName = "";

        var searchKey = $("#sWord").val().trim();
        switch ($("#searchType").val()) {
            case "CARDNO":
                data.searchCond.cardNo = searchKey;
                break;
            case "CUSTOMER_ID":
                data.searchCond.customerId = searchKey;
                break;
            case "CUSTOMER_NAME":
                data.searchCond.customerName = searchKey;
                break;
        }
        data.searchCond.cardStatCode = $("#status").val();
        $("#sWord").val(searchKey);
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        var html = '<tr style="text-align:center;"><td colspan="9">' + _commonMsg.searching + '</td></tr>';
        $("#tBodyList").append(html);

        var paging = pageInfoJs.getPaging();
        var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&cardNo=" + encodeURIComponent(data.searchCond.cardNo);
        param += "&customerId=" + encodeURIComponent(data.searchCond.customerId);
        param += "&customerName=" + encodeURIComponent(data.searchCond.customerName);
        param += "&cardStatCode=" + encodeURIComponent(data.searchCond.cardStatCode);

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/payment/prepaidCard" + param,
            dataType: 'json',
            success: _display,
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _display(jsonData) {
        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();

        if (jsonData.criteria.totalItemCount == 0) {
            $("#tBodyList").append('<tr style="text-align:center;"><td colspan="9">' + _commonMsg.noData + '</td></tr>');
            return;
        }

        data.result = jsonData.result;
        var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        var html = '';
        for (var i = 0, length = data.result.length; i < length; ++i) {
            var row = data.result[i];
            html += '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + row.cardNo + '</td>';
            html += '<td>' + (row.customerId ? row.customerId : '-') + '</td>';
            html += '<td>' + (row.customerName ? row.customerName : '-') + '</td>';
            html += '<td>' + _statusBadge(row.cardStatCode) + '</td>';
            html += '<td style="text-align:right;">' + _commaFormat(row.balance) + '</td>';
            html += '<td>' + _expireCell(row.expireDate) + '</td>';
            html += '<td>' + _formatDateTime(row.writer && row.writer.registrationDate) + '</td>';
            html += '<td>' + _actionButtons(i, row) + '</td>';
            html += '</tr>';
        }
        $("#tBodyList").append(html);
    }

    function _statusBadge(code) {
        switch (code) {
            case 'PPCS01': return '<span class="label label-primary">' + _msg.cardStatActive + '</span>';
            case 'PPCS02': return '<span class="label label-warning">' + _msg.cardStatStopped + '</span>';
            case 'PPCS03': return '<span class="label label-default">' + _msg.cardStatExpired + '</span>';
            default: return code || '-';
        }
    }

    function _expireCell(expireDate) {
        if (!expireDate) return '-';
        var dt = new Date(expireDate);
        var txt = _formatDate(dt);
        if (dt.getTime() < new Date().getTime()) {
            return '<span style="color:#dd6b55;">' + txt + '</span>';
        }
        return txt;
    }

    function _actionButtons(index, row) {
        var html = '';
        html += '<button type="button" class="btn btn-xs btn-info" onclick="prepaidCardListJs.viewHistory(\'' + row.cardNo + '\')">' + _msg.typeUse + '/' + _msg.typeIssue + '</button> ';
        if (row.cardStatCode === 'PPCS01') {
            html += '<button type="button" class="btn btn-xs btn-warning" onclick="prepaidCardListJs.changeStatus(\'' + row.cardNo + '\',\'PPCS02\')">' + _msg.cardStatStopped + '</button>';
        } else if (row.cardStatCode === 'PPCS02') {
            html += '<button type="button" class="btn btn-xs btn-primary" onclick="prepaidCardListJs.changeStatus(\'' + row.cardNo + '\',\'PPCS01\')">' + _msg.cardStatActive + '</button>';
        }
        return html;
    }

    function _viewHistory(cardNo) {
        self.location = _ctx + "/prepaidCard/his/list?cardNo=" + encodeURIComponent(cardNo);
    }

    function _changeStatus(cardNo, targetStatCode) {
        var prompt = (targetStatCode === 'PPCS02') ? _msg.confirmStop : _msg.confirmResume;
        swal({
            title: _msg.prepaidCardMgmt,
            text: prompt,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.confirm,
            cancelButtonText: _msg.cancel,
            closeOnConfirm: true
        }, function () {
            $.ajax({
                type: 'PUT',
                url: _ctx + "/ws/payment/prepaidCard/" + encodeURIComponent(cardNo) + "/status/" + targetStatCode,
                contentType: "application/json",
                dataType: 'json',
                success: function (jsonData) {
                    if (jsonData.status == 'SUCCESS') {
                        toastr.success(_commonMsg.successChange, _msg.prepaidCardMgmt);
                        _search();
                    } else {
                        toastr.error(jsonData.result || _commonMsg.failChange, _msg.prepaidCardMgmt);
                    }
                },
                error: function (xhRequest) {
                    parent.layerJs.fn_exception(xhRequest);
                    toastr.error(_commonMsg.failChange, _msg.prepaidCardMgmt);
                }
            });
        });
    }

    function _openIssueModal() {
        $("#issue_cardNo").val("");
        $("#issue_customerId").val("");
        $("#issue_customerName").val("");
        $("#issue_expireDate").val("");
        $("#issue_balance").val(0);
        $("#Popup_PrepaidCard_Issue").modal();
    }

    function _onCustomerSelected(obj) {
        $("#issue_customerId").val(obj.id);
        $("#issue_customerName").val(obj.name);
    }

    function _submitIssue() {
        var cardNo = $("#issue_cardNo").val().trim();
        var customerId = $("#issue_customerId").val();
        var expireDate = $("#issue_expireDate").val();
        var balance = parseInt($("#issue_balance").val(), 10);
        if (!cardNo) {
            toastr.warning(_msg.inputCardNo, _msg.prepaidCardMgmt);
            return;
        }
        if (!customerId) {
            toastr.warning(_msg.inputCustomer, _msg.prepaidCardMgmt);
            return;
        }
        if (isNaN(balance) || balance < 0) {
            toastr.warning(_msg.inputBalance, _msg.prepaidCardMgmt);
            return;
        }

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/payment/prepaidCard/check/" + encodeURIComponent(cardNo),
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData.status !== 'SUCCESS') {
                    toastr.error(_msg.duplicateCardNo, _msg.prepaidCardMgmt);
                    return;
                }
                _doIssue(cardNo, customerId, expireDate, balance);
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _doIssue(cardNo, customerId, expireDate, balance) {
        var body = {
            cardNo: cardNo,
            customerId: customerId,
            balance: balance
        };
        if (expireDate) {
            // Jackson 기본: 숫자(밀리초)는 java.util.Date로 역직렬화됨
            body.expireDate = new Date(expireDate + "T23:59:59").getTime();
        }
        $.ajax({
            type: 'POST',
            url: _ctx + "/ws/payment/prepaidCard",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(body),
            success: function (jsonData) {
                if (jsonData.status == 'SUCCESS') {
                    toastr.success(_msg.successIssue, _msg.prepaidCardMgmt);
                    $("#Popup_PrepaidCard_Issue").modal('hide');
                    _search();
                } else {
                    toastr.error(jsonData.result || _msg.failIssue, _msg.prepaidCardMgmt);
                }
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
                toastr.error(_msg.failIssue, _msg.prepaidCardMgmt);
            }
        });
    }

    function _commaFormat(n) {
        if (n === undefined || n === null) return '0';
        return n.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }

    function _formatDate(dt) {
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
        return _formatDate(dt) + ' ' + hh + ':' + mm;
    }

    return {
        init: _init,
        search: _search,
        viewHistory: _viewHistory,
        changeStatus: _changeStatus
    };
}();
