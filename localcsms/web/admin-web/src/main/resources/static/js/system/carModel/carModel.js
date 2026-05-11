/**
 * 차량모델관리 등록/상세
 */
let carModelJs = function(){
    'use strict';

    let data = {};
	function _init() {
		_initEvent();
		if (carModelId) {
			_search(carModelId);
		}
	}

	function _initEvent(){
		//
		$('#btnCarModelRegister').click(function(){
			//
			_registerOnClick();
		});

		$('#btnList').click(function(){
			//
			_listOnClick();
		});
		$('#btnCarModelUpdate').click(function(){
			_updateOnClick();
		});

		//연결코드
		let linkCodes = parent.commonCodeJs.getCodesByParentCode('CHRA00');
		for(let i =0, length = linkCodes.length; i < length ; ++i){
			$('#linkCode').append('<option value=\'' + linkCodes[i].code + '\'>' + linkCodes[i].codeName + '</option>');
		}
		$('#linkCode').val('CHRA01');

		//제조사
		let carMakerCodes = parent.commonCodeJs.getCodesByParentCode('CM0000');
		for(let i =0, length = carMakerCodes.length; i < length ; ++i){
			$('#carMaker').append('<option value=\'' + carMakerCodes[i].code + '\'>' + carMakerCodes[i].codeName + '</option>');
		}
	}

	function _registerOnClick(){
		//
		if(!_validate()){
			return ;
		}
		data.carModelId = $('#carMaker').val();
		toastr.info(_msg.registering, _msg.carModelMgmt);
		$.ajax({
			type: 'POST' ,
			method: 'POST',
			url: _ctx + '/ws/system/carModel',
			contentType:'application/json',
			dataType: 'json' ,
			data: JSON.stringify(data),
			success: function(jsonData) {
				if(jsonData.status == 'SUCCESS'){
					swal({
			            title: _msg.carModelMgmt,
			            text: _commonMsg.successRegister,
			            type: 'success',
			            showCancelButton: false,
			        }, function () {
						_listOnClick();
					});
				}else{
					toastr.error(_commonMsg.failRegister, _msg.carModelMgmt);
				}
			}, error: function(xhRequest, ErrorText, thrownError) {
				//
				toastr.error(_commonMsg.failRegister, _msg.carModelMgmt);
			}
		});
	}

	function _search(carModelId){
		//
		$.ajax({
			type: 'GET' ,
			url: _ctx + '/ws/system/carModel/' + carModelId,
			dataType: 'json' ,
			success: function(jsonData, textStatus, jqXHR) {
				_displayCarModelInfo(jsonData);
			}, error: function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}

	function _displayCarModelInfo(jsonData){
		if(!jsonData){
			return ;
		}
		data = jsonData;
		$('#carName').val(jsonData.carName);
		$('#linkCode').val(jsonData.linkCode);
		$('#batteryCapacity').val(jsonData.batteryCapacity);
		$('#kmKwh').val(jsonData.kmKwh);
		$('#drivingDistance').val(jsonData.drivingDistance);
		$('#carMaker').val(carModelId.substr(0,4));
	}

	function _validate(){
		data = {};

		data.carModelId = carModelId;
		if ($('#carName').val() === '') {
			swal(_commonMsg.validationCheck, _msg.enterCarName, 'warning');
			return false;
		}
		data.carName = $('#carName').val();

		if ($('#batteryCapacity').val() === '') {
			swal(_commonMsg.validationCheck, _msg.enterBatteryCapacity, 'warning');
			return false;
		}
		data.batteryCapacity = $('#batteryCapacity').val();
		data.linkCode = $('#linkCode').val();

		data.drivingDistance = $('#drivingDistance').val();
		data.kmKwh = $('#kmKwh').val();

		if ($('#drivingDistance').val() === '') {
			swal(_commonMsg.validationCheck, _msg.enterDrivingDistance, 'warning');
			return false;
		}

		if ($('#kmKwh').val() === '') {
			swal(_commonMsg.validationCheck, _msg.enterKmKwh, 'warning');
			return false;
		}

		return true;
	}

	function _updateOnClick(){
		//
		 swal({
	            title: _msg.carModelMgmt,
	            text: _msg.confirmModifyCarModel,
	            type: 'warning',
	            showCancelButton: true,
	            confirmButtonColor: '#DD6B55',
	            confirmButtonText: _msg.btnModify,
	            cancelButtonText: _msg.btnCancel,
	            closeOnConfirm: false
	        }, function () {
	        	if(!_validate()){
	    			return ;
	    		}
				toastr.info(_msg.modifying, _msg.carModelMgmt);

	    		$.ajax({
	    			type: 'PUT' ,
	    			method: 'PUT',
	    			url: _ctx + '/ws/system/carModel/' + carModelId,
	    			contentType:'application/json',
	    			dataType: 'json' ,
	    			data: JSON.stringify(data),
	    			success : function(jsonData) {
	    				if(jsonData.status == 'SUCCESS'){
							swal({
								title: _msg.carModelMgmt,
								text: _commonMsg.successModify,
								type: 'success',
								showCancelButton: false,
							}, function () {
								_listOnClick();
							});
	    				}else{
	    					toastr.error(_commonMsg.failModify, _msg.carModelMgmt);
	    				}
	    			}, error: function(xhRequest, ErrorText, thrownError) {
	    				//
	    				toastr.error(_commonMsg.failModify, _msg.carModelMgmt);
	    			}
	    		});
	        });
	}

	function _listOnClick(){

		let param = '?pageNumber=' + queryString.pageNumber;
		param += "&pageItemSize=" + queryString.pageItemSize;
		param += "&carName=" + queryString.carName;

   		if(queryString.pageItemSize){
   			self.location= _ctx + '/system/carModel/list' + param;
   		}else{
   			self.location= _ctx + '/system/carModel/list';
   		}
	}

	function _moveDetail(id){
		self.location= _ctx + '/system/carModel/detail?carModelId=' + id;
	}

	return {
		init : _init
	};
}();
