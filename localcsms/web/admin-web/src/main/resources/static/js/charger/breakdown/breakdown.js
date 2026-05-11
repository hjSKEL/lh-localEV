/**
 * 충전소
 */
let breakdownJs = function () {
    "use strict";
    
    let flag = "";
    
    let stations;
    
    let data = {
		breakdownInfo : {},
		breakdownRepairInfo : {},
		breakdownMgtInfo : {}
	};
    
    function _init() {
		_initEvent();
        if (id != 'null') {
            _search(id);
        }
        else if (id == 'null' && queryString.cpId) {
			if(queryString.cpId && queryString.csId) {
				$("#cpId").val(queryString.cpId);
				_listCsId(queryString.cpId);
			}
        }
    }

    function _initEvent() {
		//충전소 검색 팝업
		$("#btnSearchChargePoint").click(function () {
            $("#Popup_ChargePoint").modal();
            chargePointSearchPopupJs.init(_selectedChargePoint);
        });
    	//
        $("#btnRegister").click(function () {
            _registerOnClick();
        });
        $("#btnList").click(function () {
            _listOnClick();
        });
        $("#btnSuccess").click(function () {
			flag=1;
            _updateOnClick(flag);
        });
        $("#btnFail").click(function () {
			flag=2;
            _updateOnClick(flag);
        });
        //급속/완속 종벌
        let csCatCodes = parent.commonCodeJs.getCodesByParentCode('CHRA00');
        for (let i = 0, length = csCatCodes.length; i < length; ++i) {
            $("#csCatCode").append('<option value="' + csCatCodes[i].code + '">' + csCatCodes[i].codeName + '</option>');
        }
        
    }

    function _search(id) {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/breakdown/" + id,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayBreakdownInfo(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayBreakdownInfo(jsonData) {
        if (!jsonData) {
            return;
        }
        data = jsonData;
        $("#cpId").val(jsonData.cpId);
        //
        $("#csId").val(jsonData.csId);
        //
        $("#receiptDate").val(jsonData.receiptDate);
        //
        $("#receiptTime").val(jsonData.receiptTime);
        //
        $("#breakdownStatus").val(jsonData.breakdownStatus);
        //
        $("#carModelName").val(jsonData.receptInfo.carModelName);
        //
        $("#breakdownContent").val(jsonData.receptInfo.breakdownContent);
        //
        $("#regUserName").val(jsonData.writer.regUserName);
        //
        $("#csCatCode").val(jsonData.receptInfo.csCatCode);
        //
        $("#reporterName").val(jsonData.receptInfo.reporterName);
        //
        $("#reporterAddr").val(jsonData.receptInfo.reporterAddr);
        //
        $("#reporterPhoneNum").val(formmatUtilsJs.phoneFormat(jsonData.receptInfo.reporterPhoneNum));
        //
        $("#stationErrorCode").val(jsonData.receptInfo.stationErrorCode);
        //
        if(jsonData.repairInfo) {
			//충전소 위치
        	$("#reason").val(jsonData.repairInfo.reason);
        	//
			$("#repairContent").val(jsonData.repairInfo.repairContent);
			//
			$("#repairNote").val(jsonData.repairInfo.repairNote);
			//
			$("#updUserId").val(jsonData.repairInfo.writer.updUserId);
		}
		if(!$("#updUserId").val()) {
			$("#btnSuccess").show();
			$("#btnFail").show();
		}
    }

    function _validate() {
        
        //충전소ID
        let cpId = $("#cpId").val();
        if (!cpId || cpId == '') {
            swal(_commonMsg.validationCheck, _msg.selectCpId, "warning");
            return false;
        }
        //충전기ID
       	let csId = $("#csId").val();
        if (!csId || csId == '') {
            swal(_commonMsg.validationCheck, _msg.selectCsId, "warning");
            return false;
        }
        //
        let breakdownContent = $("#breakdownContent").val();
        if (!breakdownContent || breakdownContent == '') {
			swal(_commonMsg.validationCheck, _msg.inputBreakdownContent, "warning");
            return false;
		}
        //차종
        let carModelName = $("#carModelName").val();
        //충전기타입
        let csCatCode = $("#csCatCode").val();
        //
        let reporterName = $("#reporterName").val();
        //
        let reporterAddr = $("#reporterAddr").val();
        //
        let reporterPhoneNum = $("#reporterPhoneNum").val().replace(/-/gi, '');; 
        //
        let reason = $("#reason").val();
        //
        let repairContent = $("#repairContent").val();
        //
        let repairNote = $("#repairNote").val();
        //
        let stationErrorCode = $("#stationErrorCode").val();
        if(!stationErrorCode) {
			stationErrorCode = null;
		}
		let receiptDate = $("#receiptDate").val();
		let receiptTime = $("#receiptTime").val();
        
        data.breakdownInfo = {
				csCatCode : csCatCode,
				carModelName : carModelName,
				stationErrorCode : stationErrorCode,
				reporterName : reporterName,
				reporterAddr : reporterAddr,
				reporterPhoneNum : reporterPhoneNum,
				breakdownContent : breakdownContent,
				breakdownMgtInfo : {
					cpId : cpId,
					csId : csId,
				}
		};
		
		data.breakdownRepairInfo = {
			id : id,
			reason : reason,
			repairContent : repairContent,
			repairNote : repairNote,
			breakdownMgtInfo : {
				id : id,
				receiptDate : receiptDate,
				receiptTime : receiptTime,
				cpId : cpId,
				csId : csId,
			}
		};
		
        return true;
    }

    function _registerOnClick() {
        if (!_validate()) {
            return;
        }
        
	    $.ajax({
            type: 'POST',
            method: 'POST',
            url: _ctx + "/ws/charger/breakdown",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(data.breakdownInfo),
            success: function (jsonData) {
                if (jsonData.status == 'SUCCESS') {
                    toastr.success(_commonMsg.successRegister, _msg.title);
                    _listOnClick()
                } else {
                    toastr.error(_commonMsg.failRegister, _msg.title);
                }
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
                toastr.error(_commonMsg.failRegister, _msg.title);
            }
        });
    }

    function _updateOnClick() {
        if (!_validate()) {
            return;
        }
        if (flag==1) {
			data.breakdownRepairInfo.breakdownMgtInfo.breakdownStatus = 'BDST02';
		}
		if (flag==2) {
			data.breakdownRepairInfo.breakdownMgtInfo.breakdownStatus = 'BDST03';
		}
        swal({
            title: _msg.title,
            text: _msg.confirmRepairChange,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.btnModify,
            cancelButtonText: _msg.btnCancel,
            closeOnConfirm: false
        }, function () {
            $.ajax({
                type: 'POST',
                method: 'POST',
                url: _ctx + "/ws/charger/breakdown/repair",
                contentType: "application/json",
                dataType: 'json',
                data: JSON.stringify(data.breakdownRepairInfo),
                success: function (jsonData) {
                    if (jsonData.status == 'SUCCESS') {
                        toastr.success(_commonMsg.successModify, _msg.title);
                        _listOnClick();
                    } else {
                        toastr.error(_commonMsg.failModify, _msg.title);
                    }
                },
                error: function (xhRequest, ErrorText, thrownError) {
                    //
                	parent.layerJs.fn_exception(xhRequest);
                    toastr.error(_commonMsg.failModify, _msg.title);
                }
            });
        });
    }
    
    function _selectedChargePoint(param) {
        $("#cpId").val(param.cpId);
        _listCsId(param.cpId);
    }
    
    function _listCsId(value) {
        $.ajax({
            type: 'GET',
            method: 'GET',
            url: _ctx + "/ws/charger/" + value + "/list" ,
            contentType: "application/json",
            dataType: 'json',
            success: function (jsonData) {
				stations = jsonData;
				_displayStationInfo();
            },
            error: function (xhRequest, ErrorText, thrownError) {
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }
    
    function _displayStationInfo() {
		$('#csId').empty();
		$('#csId').append('<option value="">' + _msg.selectCsIdPlaceholder + '</option>');
        for (let i = 0, size = stations.length; i < size; ++i) {
            $('#csId').append('<option value="' + stations[i].csId + '">' + stations[i].csId + '</option>');
        }
        $('#csId').append(stations[0].csId);
        if(queryString.csId) {
			$("#csId").val(queryString.csId);
		}
	}

    function _listOnClick() {
        let param = '?pageNumber=' + queryString.pageNumber;
        param += "&pageItemSize=" + queryString.pageItemSize;
        param += "&cpId=" + queryString.cpId;
        param += "&csId=" + queryString.csId;
        param += "&breakdownStatus=" + queryString.breakdownStatus;
        if(queryString.fromDate){        	
        	param += "&fromDate=" + queryString.fromDate;
        }
        if(queryString.toDate){        	
        	param += "&toDate=" + queryString.toDate;
        }
        //param += "&receiptDate=" + queryString.receiptDate;
        if (queryString.pageItemSize) {
            self.location = _ctx + "/charger/breakdown/list" + param;
        } else {
            self.location = _ctx + "/charger/breakdown/list";
        }
    }

    return {
        init: _init,
        selectedChargePoint: _selectedChargePoint,
    };
}();
