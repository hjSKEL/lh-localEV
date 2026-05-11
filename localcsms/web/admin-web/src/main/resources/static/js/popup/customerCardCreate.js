/**
 * 고객카드등록 팝업
 */
var customerCardCreateJs = function(){
    "use strict";
    
    var data = {};
    
	function _init(callbackFunc) {
		_initEvent();
	};
	
	function _initEvent(){
		//
   		$("#btnCustomerCardRegister").click(function(){
   			//
   			_registerCustomerCardOnClick();
   		});
	};
	
function _validate(){
		
		data = {};
		
		if ($("#popupCutCardNo").val() === '') {
			swal(_commonMsg.validationCheck, _msg.checkCardNo, "warning");
			return false;
		}
		data.cutCardNo = $("#popupCutCardNo").val().replace(/-/gi, "");
		
		return true;
	};
	
	function _registerCustomerCardOnClick () {
		//
		if(!_validate()){
			return ;
		} 
		
		$.ajax({
			type : 'POST' ,
			method : 'POST',
			url : _ctx + "/ws/customer/card",
			contentType:"application/json",
			dataType : 'json' ,
			data : JSON.stringify(data),
			success : function(result) {
				if(result.status == "SUCCESS"){
					toastr.success(_commonMsg.successRegister, _msg.customerCard);
					$('#Popup_CustomerCardCreate').modal('toggle');
					_listOnClick();
				} else {
					toastr.error(_msg.dupCardNo, _msg.customerCard);
				}
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
				toastr.error(_commonMsg.failRegister, _msg.customerMgmt);
			}
		});
	}
	
	function _listOnClick(){
		self.location= _ctx + "/customerCard/list";
	};
	
	return {
		init : _init
	};
}();
