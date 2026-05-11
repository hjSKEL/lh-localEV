/**
 * 예외 관리 화면
 */
let excepViewJs = function () {
    "use strict";

    let data = {
        searchCond: {},
        recharging: undefined,
        logs: []
    };

    let err = {
        makerType: null,
        csKindType: null,
        csErrorStatus: [],
    };

    let idxArr = [0];

    function _init() {
        _initEvent();
        if (queryString.rechargingId) {
            $("#rechargingId").val(queryString.rechargingId);
            $("#chUseUnitCost").attr("readonly",true);
            _searchRecharingClick();
        }
    }

    function _initEvent() {
        $("#btnSearch").click(function () {
            _searchRecharingClick();
        });
        $("#btnLogSearch").click(function () {
            _searchChargerStatusLogClick();
        });
        $("#btnForm").click(function () {
            _modifyRecharingInfoClick();
        });

        $('#date1').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm-dd"
        });

        // 검색조건 Enter키로 검색기능
        $("#rechargingId").keypress(function (event) {
            if (event.keyCode == 13) {
                _searchRecharingClick();
            }
        });

		$("#errorType").change(function () {
			if($("#errorType").val() == ""){
				$('#etc').val("");
				$('#etc').attr('disabled', false);
			} else {
				if($("#errorType").val() == _msg.selectLabel){
					$('#etc').val("");
				} else {
					var text = $("#errorType :selected").text();
					$('#etc').val(text);
				}
				$('#etc').attr('disabled', true);
			}
		});
		
        $('input[name="etc"]').change(function () {
            let etc = $('input[name="etc"]:checked').val();
            $('#etc').val(etc);
            if (etc != '') {
                $('#etc').attr('disabled', true);
            } else {
                $('#etc').attr('disabled', false);
            }
        });

        $("#date1, #pagingNum").change(function () {
            //
            _searchChargerStatusLogClick();
        });
    }

    function _searchRecharingClick() {
        let rechargingId = $("#rechargingId").val().trim();
        if (!rechargingId || rechargingId == '') {
            toastr.warning(_msg.inputRechargingId, _msg.exceptionMgmt);
            return;
        }
        $("#rechargingId").val(rechargingId);


        $('#etc').val(null);
        $('#etc').attr('disabled', true);
        $('input[name="etc"]:checked').prop('checked', false);

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/recharging/detail/" + rechargingId,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                if (jsonData.rechargingId) {
                    if (jsonData.chEndDate) {
                        $("#date1").val(dateUtilsJs.formatDate(new Date(jsonData.chEndDate), 'YYYY-MM-DD'));
                    } else {
                        $("#date1").val(dateUtilsJs.formatDate(new Date(jsonData.chStartDate), 'YYYY-MM-DD'));
                    }
                    _displayRecharingInfo(jsonData);
                } else {
                    $("#date1").val(null);
                    $("#cpId").html("");
                    $("#csId").html("");
                    $("#chStartDate").val("");
                    $("#chEndDate").val("");
                    $("#chStatCode").html("");
                    $("#chUseAmount").val("0");
                    $("#chUseUnitCost").val("0");
                    $("#chUseCost").val("0");
                    $("#paySum").val("0");
                    $("#sWord").val("");

                    $("#btnFormDiv").hide();
                    toastr.warning(_msg.noRechargingInfo, _msg.rechargingHistory);
                }
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayRecharingInfo(jsonData) {
        data.recharging = jsonData;
        $("#cpId").html(jsonData.cpId + "-" + jsonData.csId + "&nbsp;" + '<button class="btn btn-sm btn-warning" onclick="excepViewJs.searchBrkdownList(\'' + jsonData.cpId + "-" + jsonData.csId+ '\')"' + _msg.breakdownHistory + '</button>');
        $("#cpName").html(jsonData.cpName);
        _searchCustomerName(jsonData.customerId);
        let chStartDate = new Date(jsonData.chStartDate);
        $("#chStartDate").html(formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(chStartDate), 'YYYY-MM-DD HH:MM:SS'));
        let chEndDate = new Date(jsonData.chEndDate);
        if (jsonData.chEndDate) {
            $("#chEndDate").val(formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(chEndDate), 'YYYY-MM-DD HH:MM:SS'));
        } else {
            $("#chEndDate").val("");
        }
        $("#chStatCode").html(parent.commonCodeJs.getCodeNameBySubCode(jsonData.chStatCode));
        $("#chUseAmount").val(jsonData.chUseAmount);
        $("#chUseUnitCost").val(jsonData.chUseUnitCost);
        $("#chUseCost").val(jsonData.chUseCost);
        $("#chUseCost").val(jsonData.chUseCost);
        $("#paySum").val(jsonData.paySum);
        $("#csCableChn").val(jsonData.evseId);

        if (jsonData.chStatCode == 'RECS01' || jsonData.chStatCode == 'RECS02') {
            $("#btnFormDiv").show();
        } else {
            $("#btnFormDiv").hide();
            toastr.warning(_msg.onlyStartOrChargingEditable, _msg.rechargingHistory);
        }
        _searchChargerStatusLogClick();
    }

    function _searchChargerStatusLogClick() {
        idxArr = [0];
        pageInfo2Js.init('pageInfo2Js', 'pagingUl', $("#pagingNum").val(), excepViewJs.search);

        data.searchCond.csUniqId = data.recharging.cpId + "-" + data.recharging.csId;
        data.searchCond.fromDate = $("#date1").val();
        data.searchCond.idx = "";
        let csCableChn = $("#csCableChn").val();
        data.searchCond.csCableChn = csCableChn;

        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="18">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfo2Js.getPaging();
        let param = "?pageNumber=0&pageItemSize=" + paging.pageItemSize;

        let cpCsId = data.searchCond.csUniqId.split("-");
        param += "&cpId=" + cpCsId[0];
        param += "&csId=" + cpCsId[1];
        param += "&evseId=" + data.searchCond.csCableChn;
        if (paging.pageNumber - 1 == 0) {
            param += "&fromDate=" + formmatUtilsJs.removeDash(data.searchCond.fromDate);
            param += "&toDate=" + formmatUtilsJs.removeDash(data.searchCond.fromDate);
        } else {
        	param += "&csStatusId=" + idxArr[paging.pageNumber - 1];
        }

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/logList" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayChargerStatus(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayChargerStatus(jsonData) {
        data.logs = jsonData.result;
        pageInfo2Js.setCount(jsonData.result.length);
        $("#tBodyList").empty();
        if (jsonData.result.length === 0) {
            let html = '<tr style="text-align:center;">';
            html += '<td colspan="18">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
            return;
        }
        err.csErrorStatus = [];
        let result = jsonData.result;
        let noIndex = (pageInfo2Js.getPaging().pageNumber - 1) * pageInfo2Js.getPaging().pageItemSize + 1;
        for (let i = 0, length = result.length; i < length; ++i) {
            let paging = pageInfo2Js.getPaging();
            let idx = paging.pageNumber;
            if (!idxArr[idx]) {
                idxArr[idx] = result[length - 1].csStatusId;
            }
            let html = '<tr style="text-align:center;" onClick="excepViewJs.selectedChargerStatusLog(' + result[i].csStatusId + ');">';
            html += '<td>' + (i + noIndex) + '</td>';
            let cdt = new Date(result[i].updateDate);
            html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            cdt = new Date(result[i].infoCollDate);
            html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            html += '<td>' + (result[i].rechargingId ? result[i].rechargingId : "") + '</td>';
            html += '<td>' + result[i].cpId + "-" + result[i].csId + '</td>';
            html += '<td>' + result[i].evseId + '</td>';
            //html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].csCatCode) + '</td>';
            html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].csStatCode) + '</td>';
            html += '<td> ' + parent.commonCodeJs.getChargerCableStatusDesc(result[i].csCableStatus) + ' </td>';
            html += '<td> ' + parent.commonCodeJs.getCodeNameBySubCode(result[i].csErrorStatus) + ' </td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].cuEleEnerge) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].caEleEnerge) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].instChAmont) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].instChSum) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].chSum) + '</td>';
            if (result[i].chStartDate) {
                cdt = new Date(result[i].chStartDate);
                html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            } else {
                html += '<td></td>';
            }
            if (result[i].chEndDate) {
                cdt = new Date(result[i].chEndDate);
                html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            } else {
                html += '<td></td>';
            }
            html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].eventCode) + '</td>';
            html += '<td>' + (result[i].cutCardNo ? result[i].cutCardNo : "") + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }

        let csErrorStatus = new Set(err.csErrorStatus);

        err.csErrorStatus = [...csErrorStatus];
        _csAlarmSearch();
    }

    function _modifyRecharingInfoClick() {
        if (!data.recharging) {
            toastr.warning(_msg.searchRechargingFirst, _msg.rechargingHistory);
            return;
        }
        if (!(data.recharging.chStatCode == 'RECS01' || data.recharging.chStatCode == 'RECS02' || data.recharging.chStatCode == 'RECS06')) {
            toastr.warning(_msg.statusMustBeStartOrCharging, _msg.rechargingHistory);
            return;
        }
        let chEndDateStrTrim = $("#chEndDate").val().trim().replace(/\s*/g, "");
        let chEndDateStr = chEndDateStrTrim.replace(/[:-]/gi, "");
        if (!chEndDateStr || chEndDateStr.length != 14) {
            toastr.warning(_msg.invalidEndDateFormat, _msg.rechargingHistory);
            return;
        }
        let chEndDate = undefined;
        try {
            chEndDate = dateUtilsJs.string2date(chEndDateStr, 'YYYYMMDDHH24MISS');
            data.recharging.chEndDate = chEndDate;
        } catch (ex) {
            toastr.warning(_msg.invalidEndDateFormat, _msg.rechargingHistory);
            return;
        }
        if (chEndDateStr != dateUtilsJs.date2String(chEndDate)) {
            toastr.warning(_msg.invalidEndDateFormat, _msg.rechargingHistory);
            return;
        }
        if (!$("#chUseAmount").val() || $("#chUseAmount") < 0 ) {
            toastr.warning(_msg.invalidChargeAmount, _msg.rechargingHistory);
            return;
        }
        //KEVIT 충전단가 예외처리
        if ($("#customerBid").val() !== "KE") {
            if (!$("#chUseUnitCost").val() || $("#chUseUnitCost").val() == 0 || $("#chUseUnitCost").val() < 0) {
                toastr.warning(_msg.invalidUnitPrice, _msg.rechargingHistory);
                return;
            }
        }
        if (!$("#chUseCost").val() || $("#chUseCost").val() < 0 || $("#chUseCost").val() > 99999 ) {
            toastr.warning(_msg.invalidChargePrice, _msg.rechargingHistory);
            return;
        }
        if (!$("#paySum").val() || $("#paySum").val() < 0 || $("#paySum").val() > 99999 ) {
            toastr.warning(_msg.invalidPaymentAmount, _msg.rechargingHistory);
            return;
        }
        data.recharging.chStartDate = new Date(data.recharging.chStartDate);
        data.recharging.chUseAmount = $("#chUseAmount").val();
        data.recharging.chUseCost = $("#chUseCost").val();
        data.recharging.chUseUnitCost = $("#chUseUnitCost").val();
        data.recharging.paySum = $("#paySum").val();
        data.recharging.chargingStation = undefined;

        if (data.recharging.chEndDate < data.recharging.chStartDate) {
            toastr.warning(_msg.endBeforeStart, _msg.rechargingHistory);
            return;
        }

        if ($('#errorType').val()==_msg.selectLabel) {
            toastr.warning(_msg.selectOrInputErrorType, _msg.rechargingHistory);
            return;
        }
        if ($('#errorType').val()=="") {
			data.recharging.errorContent = $("#etc").val();
		} else {
			data.recharging.errorContent = $("#errorType").val();
		}
        if(data.recharging.errorContent == "" || data.recharging.errorContent.length > 125){
        	toastr.warning(_msg.errorContentMax125, _msg.rechargingHistory);
        	return ;
        }
        _modifyRecharingInfo();
    }

    function _searchCustomerName(customerId) {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/detail/" + customerId,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                $("#customerName").html('<td class="footable-visible"><a href="#" onclick="excepViewJs.searchCustomerDetail(\'' + customerId + '\')">' + jsonData.custName + '</a>' +
                    '&nbsp;&nbsp;&nbsp;<button class="btn btn-sm btn-success" onclick="excepViewJs.searchRechargingList(\'' + customerId + '\')"' + _msg.rechargingHistoryBtn + '</button>' + '</td>');
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _modifyRecharingInfo() {
        //
        let conStr = _msg.confirmEndCharging;
        swal({
            title: _msg.exceptionProcess,
            text: conStr,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.endChargingProcess,
            cancelButtonText: _msg.cancel,
            closeOnConfirm: false
        }, function () {
            $.ajax({
                type: 'PUT',
                method: 'PUT',
                contentType: "application/json",
                dataType: 'json',
                url: _ctx + "/ws/recharging",
                data: JSON.stringify(data.recharging),
                success: function (jsonData, textStatus, jqXHR) {
                    if (jsonData.status == "SUCCESS") {
                        swal(_msg.exceptionProcess, _msg.chargingEnded, "success");
                        $("#rechargingId").val(data.recharging.rechargingId);
                        _searchRecharingClick();
                    } else {
                        swal(_msg.exceptionProcess, jsonData.resultMsg, "error");
                    }
                },
                error: function (xhRequest, ErrorText, thrownError) {
                	parent.layerJs.fn_exception(xhRequest);
                    swal(_msg.exceptionProcess, _commonMsg.systemError, "error");
                }
            });
        });
    }

    function _searchCustomerDetail(customerId) {
        $("#Popup_CustomerInfo").modal();
        customerInfoPopupJs.search(customerId);
    }

    function _searchBrkdownList(csUniqId) {
        $("#Popup_BrkdownList").modal();
        breakdownListPopupJs.search(csUniqId);
    }

    function _csAlarmSearch() {
        let param = "?makerTypeCode=" + err.makerType;
        param += '&errorCode=' + err.csErrorStatus;
        param += '&csKindTypes=' + err.csKindType;

        $.ajax({
            type: 'GET',
            url: _ctx + '/ws/charger/csAlarm/list' + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                if (jsonData.length == 0) return;
                let result = jsonData;
                for (let i = 0, length = result.length; i < length; ++i) {
                    let count = $('.' + result[i].errorCode).length;
                    for (let j = 0; j < count; ++j) {
                        let html = result[i].manufacturerCode + '(' + parent.commonCodeJs.getCodeNameBySubCode(result[i].errorCode) + ')'
                        $('.' + result[i].errorCode).eq(j).html(html);
                    }
                }
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _searchRechargingList(customerId) {
        $("#Popup_CustomerRecharging").modal();
        customerRechargingPopupJs.init(customerId);
    }

    return {
        init: _init,
        search: _search,
        searchCustomerDetail: _searchCustomerDetail,
        searchBrkdownList: _searchBrkdownList,
        searchRechargingList: _searchRechargingList,
    };
}();