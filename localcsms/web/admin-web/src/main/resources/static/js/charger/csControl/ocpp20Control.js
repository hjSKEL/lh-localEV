/**
 * 충전관리 - 충전기 제어
 */
var ocpp20ControlJs = function() {
	"use strict";

	var data = {
		searchCond : {} 
	};

    let err = {
        makerType: null,
        csKindType: null,
        csErrorStatus: [],
    };

    let idxArr = [];
    let seqArr = [];
    
	function _init() {
		// console.log("......");
		_initData();
		_initEvent();
		if (queryString.cpId && queryString.csId) {
			$("#btnSearch").trigger("click");
		}
	}
	
	function _initData(){
		//
		if (queryString.cpId && queryString.csId) {
			$("#sWord").val(queryString.cpId + queryString.csId);
		}
	};

	function _initEvent() {
		//
		$("#date1").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
		$("#date2").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
		$("#date3").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
		$("#date4").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
		$('#date1').datepicker({
			todayBtn: "linked",
			autoClose: true,
			format: "yyyy-mm-dd"
		});
		$('#date2').datepicker({
			todayBtn: "linked",
			autoClose: true,
			format: "yyyy-mm-dd"
		});
		$('#date3').datepicker({
			todayBtn: "linked",
			autoClose: true,
			format: "yyyy-mm-dd"
		});
		$('#date4').datepicker({
			todayBtn: "linked",
			autoClose: true,
			format: "yyyy-mm-dd"
		});

		$("#btnSearch").click(function() {
			//
        	$("#date1").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        	let cpCsId = $("#sWord").val();
        	cpCsId = cpCsId.split("-");
        	data.searchCond.cpId = cpCsId[0];
        	data.searchCond.csId = cpCsId[1];
        	_searchEnvInfoOnClick();
		});

		$("#btnLogSearch").click(function() {
			//
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
		var ocpp20Commands = ocpp20CommandJs.type();
		for(var i =0, size = ocpp20Commands.length; i < size ; ++i){
			html = '<option value="' + ocpp20Commands[i].value + '">' + ocpp20Commands[i].name + '</option>';
			$("#ocppCommandType").append(html);	
		}
		
		$("#btnCommand").click(function(){
			//
			_sendCommandOnClick();
		});
		
		$("#ocppCommandType").change(function(){
			//
			_changeOcppCommand(this);
		});
	};
	
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
	};

	function _searchEnvInfo(){
		//
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/charger/chargingStation/" + data.searchCond.cpId + "/" + data.searchCond.csId,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				if(jsonData){
					if(jsonData.ocppVersion != "ocpp2.0.1"){		
						let param = "?cpId="+ data.searchCond.cpId + "&csId=" + data.searchCond.csId; 
						self.location= _ctx + "/charger/chargingStation/OCPP16/control" + param;
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
    	pageInfo2Js.init('pageInfo2Js', 'pagingUl', $("#pagingNum").val(), ocpp20ControlJs.searchLog);

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
	
	function _changeOcppCommand(target){
		//
		$('div[name=OCPPCommand]').hide();
		var selectedValue = target.value;
		$("#" + selectedValue).show();
	}
	
    function _validate(type, params){
    	//
    	if(type == "RequestStopTransaction"){
    		if(!params[0] || params[0].length == 0){
    			return false;
    		}
    	}
    	if(type == "RequestStartTransaction"){
    		if(!params[2] || params[2].length != 16){
    			return false;
    		}
    		if(!params[0] || params[0] < 1  || params[0] > 3){
    			return false;
    		}
    	}
    	return true;
    }
    
	function _sendCommandOnClick(){
		//
		var ocppCommandType = $("#ocppCommandType").val();
		var params = [];
		switch(ocppCommandType){
			case 'RequestStopTransaction' ://2.0
				params[0] = $("#" + ocppCommandType + "Value").val();
                if(!_validate("RequestStopTransaction", params)){
                	//
                	swal(_commonMsg.validationCheck, _msg.inputTransactionId, "warning");
                	return ;
                }
				break;
			case 'Reset'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				break;
			case 'RequestStartTransaction' ://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
                if(!_validate("RequestStartTransaction", params)){
                	//
                	swal(_commonMsg.validationCheck, _msg.inputCardAndEvse, "warning");
                	return ;
                }
				break;
		}
		var valueStr = ocpp20CommandJs.makeParam(ocppCommandType, params);
		_sendCommand(ocppCommandType, valueStr);
	}
	
	function _sendCommand(ocppCommandType, valueStr){
		//
		var param = {
				param1 : ocppCommandType,
				param2 :valueStr
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
    			type : 'PUT' ,
    			method : 'PUT',
    			url : _ctx + '/ws/cmd/ocpp20/' + data.searchCond.cpId + '-' + data.searchCond.csId,
    			contentType:"application/json",
    			dataType : 'json' ,
    			data : JSON.stringify(param),
    			success : function(jsonData) {
    				if(jsonData.status == 'SUCCESS'){
    					toastr.success(_commonMsg.successSend, _msg.title);
    				}else{
    					toastr.error(jsonData.result , _msg.title);
    				}
    			},
    			error : function(xhRequest, ErrorText, thrownError) {
    				//
    				toastr.error(_commonMsg.failSend, _msg.title);
    			}
    		});
        });
	}

	function _log(){
		//
		console.log(data);
	}
	
	return {
		init : _init,
		changeOcppCommand : _changeOcppCommand,
        searchLog: _searchLog,
		log : _log
	};
}();
