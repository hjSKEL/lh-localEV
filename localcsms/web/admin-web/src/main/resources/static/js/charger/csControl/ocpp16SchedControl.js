/**
 * 충전관리 - 충전기 제어
 */
let ocpp16ControlJs = function () {
    "use strict";

    let data = {
        searchCond: {},
        schedule : {
        	csUniqId : "",
        	startTime : "",
        	endTime : "",
        	duration : "",
        	limitKW : 0
        }
    };
    
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
        $("#btnSearch").click(function () {
        	let cpCsId = $("#sWord").val();
        	cpCsId = cpCsId.split("-");
        	data.searchCond.cpId = cpCsId[0];
        	data.searchCond.csId = cpCsId[1];
        	_searchEnvInfoOnClick();
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

        $("#btnCommand").click(function () {
            //
            _sendCommandOnClick();
        });
    }

    function _searchFunc(jsonData) {
        err.makerType = jsonData.makerType;
        err.csKindType = jsonData.csKindType;
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
	}
	
	function _searchEnvInfo(){
		//
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/charger/chargingStation/" + data.searchCond.cpId + "/" + data.searchCond.csId,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				if(jsonData){
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
    	pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, ocpp16ControlJs.searchLog);
        _searchKevitLog();
    }

    function _searchLog() {
        _searchKevitLog();
    }

    function _searchKevitLog() {
        //
        $("#KEVITtBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="5">' + _commonMsg.searching + '</td>';
        $("#KEVITtBodyList").append(html);

        let paging = pageInfoJs.getPaging();
		let param = "?pageNumber=0&pageItemSize=" + paging.pageItemSize;
		param += "&csUniqId=" + data.searchCond.cpId + data.searchCond.csId;
		
		$.ajax({ 
			type: 'GET' ,
			url : _ctx + "/ws/charging/schedule/list" + param,
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
    	pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
        $("#KEVITtBodyList").empty();
        if (jsonData.result.length === 0) {
            let html = '<tr style="text-align:center;">';
            html += '<td colspan="5">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#KEVITtBodyList").append(html);
            return;
        }
        let result = jsonData.result;
        for (let i = 0, length = result.length; i < length; ++i) {
            let html = '<tr>';
            html += '<td>' + result[i].seq + '</td>';
            html += '<td>' + formmatUtilsJs.dateFormmat(result[i].startTime, 'YYYY-MM-DD HH:MM:SS') + '</td>';
            html += '<td>' + formmatUtilsJs.dateFormmat(result[i].endTime, 'YYYY-MM-DD HH:MM:SS') + '</td>';
            html += '<td>' + result[i].duration + '</td>';
            html += '<td>' + result[i].limitKW + '</td>';
            html += '</tr>';
            $("#KEVITtBodyList").append(html);
        }
    }

    function _sendCommandOnClick() {
        //
        let params = [];
        let temp, UTCYear, UTCMonth, UTCDay, UTCHours, UTCMinutes, UTCSeconds;
        params[0] = "0";//connectorId
        params[1] = "1";//chargingProfileId
        params[2] = "1";//stackLevel
        params[3] = "ChargePointMaxProfile";//chargingProfilePurpose
        params[4] = "Absolute";//chargingProfileKind
        params[5] = "0";//transactionId
        params[6] = "Daily";//recurrencyKind
        let crDt = new Date();
        params[7] = dateUtilsJs.formatDate(crDt, 'YYYY-MM-DD HH:MM:SS');
    	data.schedule.startTime = dateUtilsJs.date2String(crDt);
    	data.schedule.duration = $("#drDuration").val();
        crDt.setSeconds(crDt.getSeconds() + Number(data.schedule.duration));
        params[8] = dateUtilsJs.formatDate(crDt, 'YYYY-MM-DD HH:MM:SS');
        data.schedule.endTime = dateUtilsJs.date2String(crDt);
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
        data.schedule.limitKW = $("#limitKW").val();
        let limitW =  data.schedule.limitKW * 1000;
        
        params[13].push({startPeriod: 0, numberPhases: 1, limit: limitW});
        
        let valueStr = ocpp16CommandJs.makeParam("SetChargingProfile", params);
        _sendCommand("SetChargingProfile", valueStr);
    }

    function _sendCommand(ocppCommandType, valueStr) {
        //
        let param = {
            param1: ocppCommandType,
            param2: valueStr
        };
        data.schedule.csUniqId = data.searchCond.cpId + data.searchCond.csId;
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
                    	_saveChargingSchedule();
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
    
    function _saveChargingSchedule(){
    	//
        $.ajax({
            type: 'PUT',
            method: 'PUT',
            url: _ctx + '/ws/charging/schedule',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(data.schedule),
            success: function (jsonData) {
            	//
            	_searchLogClick();
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            }
        });
    }
    
    function _log() {
        //
        console.log(data);
    }
    
    return {
        init: _init,
        searchLog: _searchLog
    };
}();
