/**
 * 충전소
 */
let chargePointJs = function () {
    "use strict";
    
    let data = {};
    
    let checkFlag = 0;
    
    function _init() {
        $("#ctrStartDate").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        $("#ctrEndDate").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        _initEvent();
        if (cpId) {
            _search(cpId);
        }else{
        	$("#cpId").removeAttr("readonly");
        	$('#cpIdCheckLabel').html(_msg.checkDuplicate);
			$('#cpIdCheckLabel').css('color', 'red');
        	$("#btnCpIdChecker").show();
        }
    }

    function _initEvent() {
    	//
        $("#btnRegister").click(function () {
            _registerOnClick();
        });
        $("#btnList").click(function () {
            _listOnClick();
        });
        $("#btnUpdate").click(function () {
            _updateOnClick();
        });
        $("#btnDelete").click(function () {
            _deleteOnClick();
        });
        $("#btnCpIdChecker").click(function(){
			_checkCpIdOnClick()
		});
    }

    function _search(cpId) {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/chargePoint/" + cpId,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displaychargePointInfo(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displaychargePointInfo(jsonData) {
        if (!jsonData) {
            return;
        }
        data = jsonData;
        $("#cpId").val(jsonData.cpId);
        //충전소명
        $("#cpName").val(jsonData.cpName);
        //급속충전기대수
        $("#highCsCount").val(jsonData.highCsCount);
        //완속충전기대수
        $("#lowCsCount").val(jsonData.lowCsCount);
        //충전소 위치
        $("#cpLocation").val(jsonData.cpLocation);
        //사용 여부
        $("#cpUseYn").val(jsonData.cpUseYn);
        //
        //$("#deleteYn").val(jsonData.deleteYn);
        //충전소 전력량
        $("#electSupplyCapability").val(jsonData.electSupplyCapability);
        //
        if(jsonData.deleteDate){
        	$("#tdDeleteDate").html(dateUtilsJs.formatDate(new Date(jsonData.deleteDate), 'YYYY-MM-DD HH:MM:SS'));
        }
        //메모
        $("#memo").val(jsonData.memo);
    }

    function _validate() {
        //충전소ID
        data.cpId = $("#cpId").val().trim();
        if (!data.cpId == null || data.cpId == '') {
            swal(_commonMsg.validationCheck, _msg.inputCpId6, "warning");
            $('#cpIdCheckLabel').html(_msg.checkDuplicate);
			$('#cpIdCheckLabel').css('color', 'red');
			checkFlag = 0;
            return false;
        }
        //충전소명
        data.cpName = $("#cpName").val().trim();
        if (!data.cpName || data.cpName == '') {
            swal(_commonMsg.validationCheck, _msg.inputCpName, "warning");
            return false;
        }
        //급속충전기대수
        data.highCsCount = $("#highCsCount").val();
        //완속충전기대수
        data.lowCsCount = $("#lowCsCount").val();
        //충전소위치
        data.cpLocation = $("#cpLocation").val().trim();
        if (!data.cpLocation || data.cpLocation == '') {
            swal(_commonMsg.validationCheck, _msg.inputCpLocation, "warning");
            return false;
        }
        //사용 여부
        data.cpUseYn = $("#cpUseYn").val();
        //data.deleteYn = $("#deleteYn").val();
        //충전소 전력량
        data.electSupplyCapability = $("#electSupplyCapability").val();
        //메모
        data.memo = $("#memo").val().trim();
        return true;
    }

    function _registerOnClick() {
		if (!checkFlag) {
			toastr.error(_msg.checkDuplicateFirst, _msg.title);
			return;
		}
        if (!_validate()) {
            return;
        }
        $.ajax({
            type: 'POST',
            method: 'POST',
            url: _ctx + "/ws/charger/chargePoint",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(data),
            success: function (jsonData) {
                if (jsonData.status == 'SUCCESS') {
                    toastr.success(_commonMsg.successRegister, _msg.title);
                    swal({
			            title: _msg.title,
			            text: _commonMsg.registered,
			            type: "success",
			            showCancelButton: false,
			            confirmButtonColor: "#DD6B55",
			            confirmButtonText: _msg.btnConfirm,
        			}, function(){
						_moveDetail(jsonData.result.cpId);
					});
                }
                else {
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
        swal({
            title: _msg.title,
            text: _msg.confirmChange,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.btnModify,
            cancelButtonText: _msg.btnCancel,
            closeOnConfirm: false
        }, function () {
            $.ajax({
                type: 'PUT',
                method: 'PUT',
                url: _ctx + "/ws/charger/chargePoint/" + cpId,
                contentType: "application/json",
                dataType: 'json',
                data: JSON.stringify(data),
                success: function (jsonData) {
                    if (jsonData.status == 'SUCCESS') {
                        toastr.success(_commonMsg.successModify, _msg.title);
                        swal({
				            title: _msg.title,
				            text: _commonMsg.modified,
				            type: "success",
				            showCancelButton: false,
				            confirmButtonColor: "#DD6B55",
				            confirmButtonText: _msg.btnConfirm,
        				}, function(){
							_moveDetail(cpId);
						});
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

    function _deleteOnClick() {
        swal({
            title: _msg.title,
            text: _msg.confirmDeleteWithChargers,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.btnDelete,
            cancelButtonText: _msg.btnCancel,
        }, function () {
            toastr.warning(_msg.checkingRemainingChargers, _msg.title);
            $.ajax({
                type: 'PUT',
                method: 'PUT',
                url: _ctx + "/ws/charger/chargePoint/delete/" + cpId,
                contentType: "application/json",
                dataType: 'json',
                success: function (jsonData) {
                    if (jsonData.status === 'SUCCESS') {
                        _listOnClick();
                    } else {
                        toastr.error(_commonMsg.failDelete, _msg.title);
                    }
                },
                error: function (xhRequest, ErrorText, thrownError) {
                    //
                	parent.layerJs.fn_exception(xhRequest);
                    toastr.error(_commonMsg.failDelete, _msg.title);
                }
            });
        });
    }
    
    function _checkCpIdOnClick() {
		let cpId = $("#cpId").val();
		cpId = (!cpId) ? "" : cpId.trim();
		cpId = cpId.replaceAll("-","");
		if(cpId == "" || cpId.length != 6){
			toastr.error(_msg.inputCpId6, _msg.title);
			$('#cpIdCheckLabel').html(_msg.checkDuplicate);
			$('#cpIdCheckLabel').css('color', 'red');
			checkFlag = 0;
			return false;
		}
		if (cpId.substring(0,1) == '0') {
			toastr.error(_msg.cpIdNoLeadingZero, _msg.title);
			$('#cpIdCheckLabel').html(_msg.checkDuplicate);
			$('#cpIdCheckLabel').css('color', 'red');
			checkFlag = 0;
			return false;
		}
		
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/charger/chargePoint/checkDupleChargePointId/" + cpId,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				if(jsonData.status == "SUCCESS"){
					toastr.success(_msg.cpIdAvailable, _msg.title);
					$('#cpIdCheckLabel').html(_msg.cpIdAvailableLabel);
					$('#cpIdCheckLabel').css('color', 'green');
					checkFlag = 1;
				}else{
					$('#cpIdCheckLabel').html(_msg.cpIdAlreadyExists);
					$('#cpIdCheckLabel').css('color', 'red');
					toastr.error(_msg.cpIdDuplicate, _msg.title);
					checkFlag = 0;
				}
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
			}
		});
	}

    function _listOnClick() {
        let param = '?pageNumber=' + queryString.pageNumber;
        param += "&pageItemSize=" + queryString.pageItemSize;
        param += "&cpName=" + queryString.cpName;
        param += "&csType=" + queryString.csType;
        if (queryString.pageItemSize) {
            self.location = _ctx + "/charger/chargePoint/list" + param;
        } else {
            self.location = _ctx + "/charger/chargePoint/list";
        }
    }

    function _moveDetail(id) {
        let param = '&pageNumber=' + queryString.pageNumber;
        param += "&pageItemSize=" + queryString.pageItemSize;
        param += "&cpName=" + queryString.cpName;
        param += "&csType=" + queryString.csType;
        if (queryString.pageItemSize) {
            self.location = _ctx + "/charger/chargePoint/detail?cpId=" + id + param;
        } else {
            self.location = _ctx + "/charger/chargePoint/detail?cpId=" + id;
        }
    }

    return {
        init: _init
    };
}();
