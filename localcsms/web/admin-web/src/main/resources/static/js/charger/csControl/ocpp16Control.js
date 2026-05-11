/**
 * 충전관리 - 충전기 제어
 */
let ocpp16ControlJs = function () {
    "use strict";

    let data = {
        searchCond: {},
    };

    let err = {
        makerType: null,
        csKindType: null,
        csErrorStatus: [],
    };

    let idxArr = [];
    let seqArr = [];
    
    function _init() {
        _initData();
        _initEvent();
        if (queryString.cpId && queryString.csId) {
			$("#btnSearch").trigger("click");
		}
    }

    function _initData() {
		if (queryString.cpId && queryString.csId) {
			$("#sWord").val(queryString.cpId + queryString.csId);
		}
    }

    function _initEvent() {
    	$("#date1").val(dateUtilsJs.currentDate("YYYY-MM-DD"));

        $('#date1').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm-dd"
        });
        
        $("#btnSearch").click(function () {
        	$("#date1").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        	let cpCsId = $("#sWord").val();
        	cpCsId = cpCsId.split("-");
        	data.searchCond.cpId = cpCsId[0];
        	data.searchCond.csId = cpCsId[1];
        	_searchEnvInfoOnClick();
        });

        $("#btnLogSearch").click(function () {
        	_searchLogClick();
        });
        
        //제조사
        let makerTypes = parent.commonCodeJs.getCodesByParentCode('CHMK00');
        $("#makerType").append('<option value="">' + _commonMsg.select + '</option>');
        let html = '';
        for (let i = 0, size = makerTypes.length; i < size; ++i) {
            html = '<option value="' + makerTypes[i].code + '">';
            html += makerTypes[i].codeName;
            html += '</option>';
            $("#makerType").append(html);
        }

        html = '';
        let ocpp15Commands;
        ocpp15Commands = ocpp16CommandJs.type();
        for (let i = 0, size = ocpp15Commands.length; i < size; ++i) {
            html = '<option value="' + ocpp15Commands[i].value + '">' + ocpp15Commands[i].name + '</option>';
            $("#ocppCommandType").append(html);
        }
        
        if(queryString.ocppCommandType && queryString.transactionId) {
        	if(queryString.ocppCommandType === "RemoteStopTransaction") {
        		$("#Reset").hide();
        		$("#RemoteStopTransaction").show();
        		$("#ocppCommandType").val("RemoteStopTransaction");
        	}
        	$("#RemoteStopTransactionValue").val(queryString.transactionId);
        }

        $("#btnCommand").click(function () {
            //
            _sendCommandOnClick();
        });

        $("#ocppCommandType").change(function () {
            //
            _changeOcppCommand(this);
        });
        
        $("#btnAddChargingSchedulePeriod").click(function () {
            //
            _addChargingSchedulePeriod();
        });
        
        $("#hiddenAdd").click(function () {
            $(".addInfo").show();
            $("#hiddenSub").show();
            $("#hiddenAdd").hide();
        });

        $("#hiddenSub").click(function () {
            $(".addInfo").hide();
            $("#hiddenSub").hide();
            $("#hiddenAdd").show();
        });

        $("#pagingNum, #csCableChn, #ocppCheck").change(function () {
            //
            _searchLogClick();
        });
        $("#date1").change(function () {
            //
        	let date1 = $("#date1").val();
        	if(data.searchCond.fromDate != date1){        		
        		_searchLogClick();
        	}
        });
    }

    function _searchFunc(jsonData) {
        err.makerType = jsonData.makerType;
        err.csKindType = jsonData.csKindType;

        _searchLogClick();
    }
    
	function _searchEnvInfoOnClick(){
		//
		var sWord = $("#sWord").val().replace(/-/g, '').trim();
		if(!sWord || sWord == ''){
			toastr.warning(_commonMsg.searchInputReq, _msg.title);
			return;
		}
		/*if(!sWord.includes('-')){
			toastr.warning("충전기 ID는 xxxxxx-xx 형식입니다.", "충전기제어");
			return ;
		}*/
		data.searchCond.csId = sWord;
        if (data.searchCond.csId && data.searchCond.csId.length > 0) {
            if (data.searchCond.csId.length !== 8) {
				$("#csUniqIdtr").html("");
				toastr.warning(_msg.csIdLength8, _msg.csIdLabel);
                return;
            }
            data.searchCond.cpId = data.searchCond.csId.substring(0, 6);
            data.searchCond.csId = data.searchCond.csId.substring(6);
        }
        $("#sWord").val(sWord);
		_searchEnvInfo();
		_searchLogClick();
	}
	
	function _searchEnvInfo(){
		//
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/charger/chargingStation/" + data.searchCond.cpId + "/" + data.searchCond.csId,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				if(jsonData){
					if(jsonData.ocppVersion != "ocpp1.6"){		
						let param = "?cpId="+ data.searchCond.cpId + "&csId=" + data.searchCond.csId; 
						self.location= _ctx + "/charger/chargingStation/OCPP20/control" + param;
						return ;
					}
					_displayEnvInfo(jsonData);
				}else{
					toastr.warning(_msg.chargerNotFound, _msg.title);
				}
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}
    function _displayEnvInfo(jsonData) {
        if (!jsonData) {
            return;
        }
        $("#csUniqIdtr").html(jsonData.csUniqId + "(" + jsonData.cpId + "-" + jsonData.csId + ")");
        $("#makerType").val(jsonData.makerType);
        $("#fmwVer").val(jsonData.fwVer);
        $("#cpNametr").html(jsonData.cpName);
        $("#protocolType").val(jsonData.ocppVersion);
        
    }

    function _searchLogClick() {
        idxArr = [0];
        seqArr = [0];
    	pageInfo2Js.init('pageInfo2Js', 'pagingUl', $("#pagingNum").val(), ocpp16ControlJs.searchLog);

        data.searchCond.fromDate = "";
        data.searchCond.toDate = "";
        data.searchCond.csCableChn = "";
        data.searchCond.connectorId = "";
        data.searchCond.idx = "";
        data.searchCond.seq = "";

        if ($("#date1").val() != null && $("#date1").val() != '') {
        	data.searchCond.fromDate = $("#date1").val();
            data.searchCond.toDate = data.searchCond.fromDate;
        }

        let csCableChn = $("#csCableChn").val();
        data.searchCond.csCableChn = csCableChn;
        data.searchCond.connectorId = csCableChn;

        _searchKevitLog();
    }

    function _searchLog() {
        let ocppCheck = $('#ocppCheck').is(':checked');
        _searchKevitLog();
    }

    function _searchKevitLog() {
        //
        $("#KEVITtBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="18">' + _commonMsg.searching + '</td>';
        $("#KEVITtBodyList").append(html);

        let paging = pageInfo2Js.getPaging();
		let param = "?pageNumber=0&pageItemSize=" + paging.pageItemSize;
		param += "&evseId=" + data.searchCond.csCableChn;
		param += "&cpId=" + data.searchCond.cpId;
		param += "&csId=" + data.searchCond.csId;
        if(paging.pageNumber - 1 == 0 && data.searchCond.fromDate != '') {
        	param += "&fromDate=" + formmatUtilsJs.removeDash(data.searchCond.fromDate);
        	param += "&toDate=" + formmatUtilsJs.removeDash(data.searchCond.toDate);
        } else {
            param += "&csStatusId=" + idxArr[paging.pageNumber - 1];
        }
		
		$.ajax({ 
			type: 'GET' ,
			url : _ctx + "/ws/charger/logList" + param,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayKevitLog(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
			}
		});
        
    }

    function _displayKevitLog(jsonData) {
        pageInfo2Js.setCount(jsonData.result.length);
        $("#KEVITtBodyList").empty();
        if (jsonData.result.length === 0) {
            let html = '<tr style="text-align:center;">';
            html += '<td colspan="18">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#KEVITtBodyList").append(html);
            return;
        }
        err.csErrorStatus = [];
        let result = jsonData.result;
        let noIndex = (pageInfo2Js.getPaging().pageNumber - 1) * pageInfo2Js.getPaging().pageItemSize + 1;

        for (let i = 0, length = result.length; i < length; ++i) {
            let paging = pageInfo2Js.getPaging();
            let idx = paging.pageNumber;
            if(!idxArr[idx]) {
                idxArr[idx] = result[length - 1].csStatusId;
            }
            let html = '<tr>';
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
            $("#KEVITtBodyList").append(html);
        }
        let csErrorStatus = new Set(err.csErrorStatus);

        err.csErrorStatus = [...csErrorStatus];
    }

    function _changeOcppCommand(target) {
        //
        $('div[name=OCPPCommand]').hide();

        let selectedValue = target.value;
        $("#" + selectedValue).show();
    }
    function _validate(type, params){
    	//
    	if(type == "RemoteStopTransaction"){
    		if(!params[0] || params[0].length != 22){
    			return false;
    		}
    	}
    	if(type == "RemoteStartTransaction"){
    		if(!params[0] || params[0].length != 16){
    			return false;
    		}
    		if(!params[1] || params[1] < 1  || params[1] > 3){
    			return false;
    		}
    	}
    	return true;
    }
    
    function _validate4Date(date){
    	//
    	if(!date || date == "" || date.length != 19)
    		return false;
    	
    	if(date.substring(4,5) != "-"
    		|| date.substring(7,8) != "-"
    		|| date.substring(10,11) != " "
    		|| date.substring(13,14) != ":"
    		|| date.substring(16,17) != ":")
    		return false;
    	
    	date = date.replaceAll("-", "");
    	date = date.replaceAll(":", "");
    	date = date.replaceAll(" ", "");
    	if(date.length != 14){
    		return false;
    	}
    	//"2023-07-25 11:00:00".substring(4, 5)'-'
    	//"2023-07-25 11:00:00".substring(7,8) -
    	//"2023-07-25 11:00:00".substring(10,11)' '
    	//"2023-07-25 11:00:00".substring(13,14):
    	//"2023-07-25 11:00:00".substring(16,17):
    	return true;
    }
    
    function _validate4Date2(date1, date2){
    	date1 = date1.replaceAll("-", "")
    	date1 = date1.replaceAll(":", "")
    	date1 = date1.replaceAll(" ", "")
    	
    	date2 = date2.replaceAll("-", "")
    	date2 = date2.replaceAll(":", "")
    	date2 = date2.replaceAll(" ", "")
    	
    	let start = dateUtilsJs.string2date(date1, "YYYYMMDDHH24MISS")
    	let end = dateUtilsJs.string2date(date2, "YYYYMMDDHH24MISS")
    	if(start.getTime() >= end.getTime()){
    		return false;
    	}
    	return true;
    }

    function _sendCommandOnClick() {
        //
        let ocppCommandType = $("#ocppCommandType").val();
        let params = [];
        switch (ocppCommandType) {
            case 'Reset':
                params[0] = $("#" + ocppCommandType + "Value").val();
                break;
            case 'RemoteStopTransaction' :
                params[0] = $("#" + ocppCommandType + "Value").val().trim();
                if(!_validate("RemoteStopTransaction", params)){
                	//
                	swal(_commonMsg.validationCheck, _msg.inputTransactionId22, "warning");
                	return ;
                }
                params[0] = $("#" + ocppCommandType + "Value").val().trim().substr(-10);
                break;
            case 'RemoteStartTransaction' :
                params[0] = $("#" + ocppCommandType + "Value1").val().replace(/-/g, '').trim();
                params[1] = $("#" + ocppCommandType + "Value2").val().trim();
                if(!_validate("RemoteStartTransaction", params)){
                	//
                	swal(_commonMsg.validationCheck, _msg.inputCardAndConnector, "warning");
                	return ;
                }
                break;
            case 'SetChargingProfile':
                let temp, UTCYear, UTCMonth, UTCDay, UTCHours, UTCMinutes, UTCSeconds;
                params[0] = "0";//connectorId
                params[1] = "1";//chargingProfileId
                params[2] = "1";//stackLevel
                params[3] = "ChargePointMaxProfile";//chargingProfilePurpose
                params[4] = "Absolute";//chargingProfileKind
                params[5] = "0";//transactionId
                params[6] = "Daily";//recurrencyKind
                params[7] = $("#" + ocppCommandType + "Value8").val().trim();
                if(!_validate4Date(params[7])){
                	swal(_commonMsg.validationCheck, _msg.invalidStartDateFormat, "warning");
                	return ;
                }
                params[8] = $("#" + ocppCommandType + "Value9").val().trim();
                if(!_validate4Date(params[8])){
                	swal(_commonMsg.validationCheck, _msg.invalidEndDateFormat, "warning");
                	return ;
                }
                if(!_validate4Date2(params[7], params[8])){
                	swal(_commonMsg.validationCheck, _msg.endDateMustBeFuture, "warning");
                	return ;
                }
                temp = new Date(params[7]);
                UTCYear = temp.getUTCFullYear();
                UTCMonth = temp.getUTCMonth() + 1;
                UTCMonth = UTCMonth < 10 ? '0' + UTCMonth : UTCMonth;
                UTCDay = temp.getUTCDate();
                UTCDay = UTCDay < 10 ? '0' + UTCDay : UTCDay;
                UTCHours = temp.getUTCHours();
                UTCHours = UTCHours < 10 ? '0' + UTCHours : UTCHours;
                UTCMinutes = temp.getUTCMinutes();
                UTCMinutes = UTCMinutes < 10 ? '0' + UTCMinutes : UTCMinutes;
                UTCSeconds = temp.getUTCSeconds();
                UTCSeconds = UTCSeconds < 10 ? '0' + UTCSeconds : UTCSeconds;
                params[7] = UTCYear + '-' + UTCMonth + '-' + UTCDay + 'T' + UTCHours + ':' + UTCMinutes + ':' + UTCSeconds + 'Z';
                temp = new Date(params[8]);
                UTCYear = temp.getUTCFullYear();
                UTCMonth = temp.getUTCMonth() + 1;
                UTCMonth = UTCMonth < 10 ? '0' + UTCMonth : UTCMonth;
                UTCDay = temp.getUTCDate();
                UTCDay = UTCDay < 10 ? '0' + UTCDay : UTCDay;
                UTCHours = temp.getUTCHours();
                UTCHours = UTCHours < 10 ? '0' + UTCHours : UTCHours;
                UTCMinutes = temp.getUTCMinutes();
                UTCMinutes = UTCMinutes < 10 ? '0' + UTCMinutes : UTCMinutes;
                UTCSeconds = temp.getUTCSeconds();
                UTCSeconds = UTCSeconds < 10 ? '0' + UTCSeconds : UTCSeconds;
                params[8] = UTCYear + '-' + UTCMonth + '-' + UTCDay + 'T' + UTCHours + ':' + UTCMinutes + ':' + UTCSeconds + 'Z';
                params[9] = "W";//chargingRateUnit
                params[10] = (new Date(params[8]).getTime() - new Date(params[7]).getTime()) / 1000;//duration
                params[11] = params[7];
                params[12] = "2000";//minChargingRate
                params[13] = [];
                let value14 = $("#" + ocppCommandType + "Value14");
                let chList = value14.children();
                if(chList.length == 0){
                	swal(_commonMsg.validationCheck, _msg.registerSchedulePeriod, "warning");
                	return ;
                }
                let isValied = true;
                for (let i = 0, length = chList.length; i < length; ++i) {
                    let startPeriod = $(chList[i].children[0]).find("INPUT").val().trim();
                    if(!startPeriod){
                    	isValied = false;
                    }
                    if(i == 0 && startPeriod != 0){
                    	isValied = false;
                    }
                    if(i > 0 && startPeriod <= 0){
                    	isValied = false;
                    }
                    let numberPhases = "3";//
                    let limit = $(chList[i].children[1]).find("INPUT").val().trim();
                    if(!limit || limit <= 0){
                    	isValied = false;
                    }
                    params[13].push({startPeriod: startPeriod, numberPhases: numberPhases, limit: limit});
                }
                if(!isValied){
                	swal(_commonMsg.validationCheck, _msg.checkSchedulePeriod, "warning");
                	return ;
                }
                break;

        }
        let valueStr = ocpp16CommandJs.makeParam(ocppCommandType, params);
        _sendCommand(ocppCommandType, valueStr);
    }

    function _sendCommand(ocppCommandType, valueStr) {
        //
        let param = {
            param1: ocppCommandType,
            param2: valueStr
        };

        swal({
            title: _msg.title,
            text: _msg.confirmSendCommand,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.btnConfirm,
            cancelButtonText: _msg.btnCancel,
            closeOnConfirm: true
        }, function () {
            $.ajax({
                type: 'PUT',
                method: 'PUT',
                url: _ctx + '/ws/cmd/ocpp16/' + data.searchCond.cpId + "-" + data.searchCond.csId,
                contentType: "application/json",
                dataType: 'json',
                data: JSON.stringify(param),
                success: function (jsonData) {
                    if (jsonData.status === 'SUCCESS') {
                        toastr.success(_commonMsg.successSend, _msg.title);
                    } else {
                        toastr.error(_commonMsg.failSend, _msg.title);
                    }
                },
                error: function (xhRequest, ErrorText, thrownError) {
                    //
                	parent.layerJs.fn_exception(xhRequest);
                    toastr.error(_commonMsg.failSend, _msg.title);
                }
            });
        });
    }

    function _cardFommat(card_val) {
        var tmp = "";
        if (card_val.length < 4) {
            tmp += card_val;
        } else if (card_val.length < 8) {
            tmp += card_val.substr(0, 4);
            tmp += '-';
            tmp += card_val.substr(4);
        } else if (card_val.length < 12) {
            tmp += card_val.substr(0, 4);
            tmp += '-';
            tmp += card_val.substr(4, 4);
            tmp += '-';
            tmp += card_val.substr(8);
        } else {
            tmp += card_val.substr(0, 4);
            tmp += '-';
            tmp += card_val.substr(4, 4);
            tmp += '-';
            tmp += card_val.substr(8, 4);
            tmp += '-';
            tmp += card_val.substr(12);
        }
        return tmp;

    }

    function _phoneFommat(phone) {
        if (phone.substr(0, 2) === '02') {
            return phone.substr(0, 2) + '-' + phone.substr(2, 4) + '-' + phone.substr(6, 4);
        } else if (phone.length === 10) {
            return phone.substr(0, 3) + '-' + phone.substr(3, 3) + '-' + phone.substr(5, 4);
        } else if (phone.length === 11) {
            return phone.substr(0, 3) + '-' + phone.substr(3, 4) + '-' + phone.substr(7, 4);
        }
        return phone;
    }

    function _log() {
        //
        console.log(data);
    }

    function _doCustCardNo(custCardNo) {
        navigator.clipboard.writeText(custCardNo).then(function () {
            toastr.success(custCardNo, _msg.copyValue);
        }, function () {
            toastr.error(_msg.clipboardNotSupported, _msg.copyValue);
        });
    }

    function _searchRechargingDetail(rechargingId) {
        let param = "?rechargingId=" + rechargingId;
        if (parent.UserRole == 'ADMIN') {
            parent.layerJs.fn_moveMenu('20000007', _msg.menuRechargingException, '/evAdmin/recharging/exception/view' + param, 'THIS', true);
        } else if (parent.UserRole == 'OPERATION') {
            parent.layerJs.fn_moveMenu('30100107', _msg.menuRechargingException, '/evAdmin/recharging/exception/view' + param, 'THIS', true);
        }
    }
    
    function _addChargingSchedulePeriod() {
        //
        let html = "<tr>";
        html += '<td><input type="number" min=0 placeholder="ex)0"   class="form-control input-sm" /></td>';
        html += '<td><input type="number" min=0 placeholder="ex)3000" class="form-control input-sm" /></td>';
        html += '<td><button onclick="ocpp16ControlJs.removeChargingSchedulePeriod(this);">' + _msg.btnDelete + '</button></td>';
        html += "</tr>";
        $("#SetChargingProfileValue14").append(html);
    }

    function _removeChargingSchedulePeriod(that) {
        //
        that.parentElement.parentElement.remove();
    }
    
    return {
        init: _init,
        changeOcppCommand: _changeOcppCommand,
        doCustCardNo: _doCustCardNo,
        searchLog: _searchLog,
        removeChargingSchedulePeriod : _removeChargingSchedulePeriod
    };
}();
