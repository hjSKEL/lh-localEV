/**
f * 고객 등록/상세
 */
let customerJs = function(){
    "use strict";
    
    let data = {};
    
    let cardNoChecker = false;
    
	function _init() {
		_initEvent();	
		if (customerId) {
			$("#btnCardNoChecker").hide();
			_search(customerId);
		}else{
			$("#custName").attr("readonly",false);
			$("#mblPhoneNo1").attr("readonly",false);
			$("#mblPhoneNo2").attr("readonly",false);
			$("#mblPhoneNo3").attr("readonly",false);
			
			$("#cutCardNo1").attr("readonly",false);
			$("#cutCardNo2").attr("readonly",false);
			$("#cutCardNo3").attr("readonly",false);
			$("#cutCardNo4").attr("readonly",false);
			$("#btnCardNoChecker").show();
			$("#custStatCodeTd").html('<option selected value="00">' + _msg.statusNew + '</option>');
		}
		parent.carModelJs.init(_displayCarModel);
	}
	
	function _initEvent(){
		//
		$("#btnCustomerRegister").click(function(){
			//
			_registerCustomerOnClick();
		});
		
		$("#btnLostCardRequest").click(function(){
			//
			_lostCardOnClick();
		});
		$("#btnDeleteCardRequest").click(function(){
			//
			_deleteCardOnClick();
		});
		
		$("#btnList").click(function(){
			//
			_listOnClick();
		});
		$("#btnCustomerUpdate").click(function(){
			_updateCustomerOnClick();
		});
		$("#btnCustomerCardSave").click(function(){
			_saveCustomerCardOnClick();
		});
		
		$("#btnCardNoChecker").click(function(){
			_checkCardNoOnClick();
		});
		
		$("#custStatCodeTd").change(function(){
			let value = $("#custStatCodeTd").val();
			if(value == "00"){
				$("#btnCardNoChecker").show();
				$("#cutCardNo1").attr("readonly", false);
				$("#cutCardNo2").attr("readonly", false);
				$("#cutCardNo3").attr("readonly", false);
				$("#cutCardNo4").attr("readonly", false);
				return ;
			}
			if(value == "MEML02"){
				_lostCardOnClick();
			}
			if(value == "MEML03"){			
				_deleteCardOnClick();
			}
		});
		
		$("#carModelId").change(function(){
			let value = $("#carModelId").val();
			$("#carName").val($("#carModelId :selected").text());
			if(value == "CM9999"){
				$("#carName").attr("readonly", false);
			}else{
				$("#carName").attr("readonly", true);
			}
		});

		// V2X 가입 토글 — allowedEnergyTransfer 영역 표시/숨김
		$("#v2xContractYn").change(function () {
			_toggleAllowedEnergyTransferGroup($(this).is(":checked"));
		});
	}

	function _toggleAllowedEnergyTransferGroup(enabled) {
		if (enabled) {
			$("#allowedEnergyTransferGroup").show();
			$("#allowedEnergyTransferHint").hide();
		} else {
			$("#allowedEnergyTransferGroup").hide();
			$("#allowedEnergyTransferHint").show();
			$(".allowedEnergyMode").prop("checked", false);
		}
	}

	function _loadV2xFields(jsonData) {
		var v2x = jsonData.v2xContractYn === "Y";
		$("#v2xContractYn").prop("checked", v2x);
		_toggleAllowedEnergyTransferGroup(v2x);
		var csv = jsonData.allowedEnergyTransfer || "";
		var modes = csv.split(",").map(function (s) { return s.trim(); }).filter(function (s) { return !!s; });
		$(".allowedEnergyMode").each(function () {
			$(this).prop("checked", modes.indexOf($(this).val()) >= 0);
		});
	}

	function _collectAllowedEnergyTransfer() {
		if (!$("#v2xContractYn").is(":checked")) return null;
		var arr = [];
		$(".allowedEnergyMode:checked").each(function () { arr.push($(this).val()); });
		return arr.length === 0 ? null : arr.join(",");
	}
	
	function _checkCardNoOnClick(){
		//
		let cutCardNo = String($("#cutCardNo1").val()) + String($("#cutCardNo2").val()) + String($("#cutCardNo3").val()) + String($("#cutCardNo4").val());
		cutCardNo = (!cutCardNo) ? "" : cutCardNo.trim();
		cutCardNo = cutCardNo.replaceAll("-","");
		if(cutCardNo == "" || cutCardNo.length != 16){
			toastr.warning(_msg.cardDigit16, _msg.memberCard);
			return ;
		}
		
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/customer/card/check/" + cutCardNo,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				if(jsonData.status == "SUCCESS"){
					toastr.success(_msg.cardAvailable, _msg.memberCard);
					cardNoChecker = true;
				}else{
					toastr.error(_msg.cardDuplicate, _msg.memberCard);
				}
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
			}
		});
	}
	
	function _registerCustomerOnClick(){
		//
		
		if(!cardNoChecker) {
			toastr.error(_msg.cardDuplicateCheck, _msg.customerMgmt);
			return ;
		}

		if(!_validate()){
			return ;
		}

		$.ajax({
			type : 'POST' ,
			method : 'POST',
			url : _ctx + "/ws/customer",
			contentType:"application/json",
			dataType : 'json' ,
			data : JSON.stringify(data),
			success : function(jsonData) {
				if(jsonData.status === 'SUCCESS'){
					toastr.success(_commonMsg.successRegister, _msg.customerMgmt);
					_moveDetail(jsonData.result);
				}else{
					toastr.error(_commonMsg.failRegister, _msg.customerMgmt);
				}
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
				toastr.error(_commonMsg.failRegister, _msg.customerMgmt);
			}
		});
	}
	
	function _lostCardOnClick(){
		//
		swal({
			title: _msg.customerCardMgmt,
			text: _msg.confirmLost,
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: _msg.confirm,
			cancelButtonText: _msg.cancel,
			closeOnConfirm: true
		}, function () {
			let cutCardNo = String($("#cutCardNo1").val()) + String($("#cutCardNo2").val()) + String($("#cutCardNo3").val()) + String($("#cutCardNo4").val());
			_chagneCustStatCode(cutCardNo, "MEML02");
		});
	}
	
	function _deleteCardOnClick(){
		//
		swal({
			title: _msg.customerCardMgmt,
			text: _msg.confirmDeleteDefect,
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: _msg.confirm,
			cancelButtonText: _msg.cancel,
			closeOnConfirm: true
		}, function () {
			let cutCardNo = String($("#cutCardNo1").val()) + String($("#cutCardNo2").val()) + String($("#cutCardNo3").val()) + String($("#cutCardNo4").val());
			_chagneCustStatCode(cutCardNo, "MEML03");
		});
	}
	
	function _chagneCustStatCode (cutCardNo, custStatCode) {
    	$.ajax({
			type : 'PUT' ,
			method : 'PUT',
			url : _ctx + "/ws/customer/card/changeCustStatCode/" + cutCardNo + "/status/" + custStatCode,
			contentType:"application/json",
			dataType : 'json' ,
			data : {},
			success : function(jsonData) {
				if(jsonData.status === 'SUCCESS'){
					toastr.success(_commonMsg.successApply, _msg.customerCardMgmt);
					self.location= _ctx + "/customer/detail?customerId=" + customerId;
				}else{
					toastr.error(_commonMsg.failApply, _msg.customerCardMgmt);
				}
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
				toastr.error(_commonMsg.failApply, _msg.customerCardMgmt);
			}
		});
    }
	
	function _search(customerId){
		//
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/customer/detail/" + customerId,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayCustomerInfo(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
				$("#btnConfirmRcvComplate").remove();
				$("#btnLostCardRequest").remove();
				$("#btnDeleteCardRequest").remove();
				$("#btnReCustomerCardRequest").remove();
			}
		});
	}
	
	function _displayCustomerInfo(jsonData){
		
		$('#btnCardNoChecker').css('display', 'none');
		
		if(!jsonData){
			return ;
		}
		data = jsonData;
		$("#custName").val(jsonData.custName);
		$("#customerId").val(jsonData.customerId);
		let mblPhoneNo = formmatUtilsJs.phoneFormat(jsonData.mblPhoneNo);
		$('#mblPhoneNo1').val(mblPhoneNo.substring(0,3));
		$('#mblPhoneNo2').val(mblPhoneNo.substring(4,mblPhoneNo.length-5));
		$('#mblPhoneNo3').val(mblPhoneNo.substring(mblPhoneNo.length-4,mblPhoneNo.length));
		$("#email").val(jsonData.email);
		$("#carName").val(jsonData.carName);
		$("#carNumber").val(jsonData.carNumber);
		// V2X 정책 — OCPP 2.1 AuthorizeResponse.allowedEnergyTransfer
		_loadV2xFields(jsonData);
		if(jsonData.customerMgt.cutCardNo) {
			let cutCardNo = jsonData.customerMgt.cutCardNo;
			$("#cutCardNo1").val(cutCardNo.substring(0,4));
			$("#cutCardNo2").val(cutCardNo.substring(4,8));
			$("#cutCardNo3").val(cutCardNo.substring(8,12));
			$("#cutCardNo4").val(cutCardNo.substring(12,16));
		}
		$("#tagType").val(jsonData.customerMgt.tagType || "");
		if (jsonData.carModel) {
			$("#carModelId").val(jsonData.carModel.carModelId);
		}
		$("#custStatName").val(parent.commonCodeJs.getCodeNameBySubCode(jsonData.customerCard.custStatCode));
		$("#lossId").val(jsonData.customerCard.lossId);
		jsonData.customerCard.lossId && $("#lossName").val(jsonData.customerCard.lossName +'(' + jsonData.customerCard.lossId + ')');
		if(jsonData.customerCard.lossDate){
			$("#lossDate").val(dateUtilsJs.formatDate(new Date(jsonData.customerCard.lossDate), 'YYYY-MM-DD HH:MM:SS'));
		} else {
			$("#lossDate").val("");
		}
		$("#deleteId").val(jsonData.customerCard.deleteId);
		if(jsonData.customerCard.deleteName){
			jsonData.customerCard.deleteId && $("#deleteName").val(jsonData.customerCard.deleteName +'(' + jsonData.customerCard.deleteId + ')');
		} else {
			jsonData.customerCard.deleteId && $("#deleteName").val('(' + jsonData.customerCard.deleteId + ')');
		}
		if(jsonData.customerCard.delDate) {
			$("#delDate").val(dateUtilsJs.formatDate(new Date(jsonData.customerCard.delDate), 'YYYY-MM-DD HH:MM:SS'));
		} else {
			$("#delDate").val("");
		}
		
		if(jsonData.customerCard.custStatCode == "MEML01"){
			$("#custStatCodeTd").html('<option value="MEML01" >' + _msg.statusNormal + '</option><option value="MEML02">' + _msg.statusLost + '</option><option value="MEML03">' + _msg.statusDeleteDefect + '</option>');
		}
		if(jsonData.customerCard.custStatCode == "MEML02"){			
			$("#custStatCodeTd").html('<option value="00">' + _msg.statusNew + '</option><option value="MEML02">' + _msg.statusLost + '</option>');
		}
		if(jsonData.customerCard.custStatCode == "MEML03"){
			$("#custStatCodeTd").html('<option value="00">' + _msg.statusNew + '</option><option value="MEML03">' + _msg.statusDeleteDefect + '</option>');
		}
		$("#custStatCodeTd").val(jsonData.customerCard.custStatCode);
		if(jsonData.writer.registrationDate) {
			$("#regDate").html(dateUtilsJs.formatDate(new Date(jsonData.writer.registrationDate), 'YYYY-MM-DD HH:MM:SS'))
		} else {
			$("#regDate").html("");
		}
		if(jsonData.writer.updateDate) {
			$("#updDate").html(dateUtilsJs.formatDate(new Date(jsonData.writer.updateDate), 'YYYY-MM-DD HH:MM:SS'))
		} else {
			$("#updDate").html("");
		}
	}
	
	function _validate(){
		data.customerId = customerId;
		if ($("#custName").val() === '') {
			swal(_commonMsg.validationCheck, _msg.validCustName, "warning");
			return false;
		}
		data.custName = $("#custName").val();
		
		let mblPhoneNo = $("#mblPhoneNo1").val() + $("#mblPhoneNo2").val() + $("#mblPhoneNo3").val();
		if(!$('#mblPhoneNo1').val() || !$('#mblPhoneNo2').val() || !$('#mblPhoneNo3').val()){
			swal(_commonMsg.validationCheck, _msg.validPhone, 'warning');
			return false;
		}
		if($('#mblPhoneNo2').val().length < 4 || $('#mblPhoneNo3').val().length < 4){
			swal(_commonMsg.validationCheck, _msg.validPhoneFormat, 'warning');
			return false;
		}
		data.mblPhoneNo = mblPhoneNo;
		
		data.companyId = "CO0000000";
		data.carNumber = $("#carNumber").val();
		data.carModel = {};
		data.carModel.carModelId = $("#carModelId").val();
		// V2X 정책 수집
		data.v2xContractYn          = $("#v2xContractYn").is(":checked") ? "Y" : "N";
		data.allowedEnergyTransfer  = _collectAllowedEnergyTransfer();
		if(!data.customerMgt) {
			data.customerMgt = {};
		}
		if(data.carModel.carModelId == "CM9999"){
			data.carName = $("#carName").val();
		}else{
			if(data.carModel.carModelId == "" ) {
				data.carName = "-";
			} else {
				data.carName = $("#carModelId option:checked").text();
			}			
		}
		let cutCardNo = String($("#cutCardNo1").val()) + String($("#cutCardNo2").val()) + String($("#cutCardNo3").val()) + String($("#cutCardNo4").val());
		if(!$("#cutCardNo1").val() || !$("#cutCardNo2").val() || !$("#cutCardNo3").val() || !$("#cutCardNo4").val()){
			swal(_commonMsg.validationCheck, _msg.validCardNo, "warning");
			return false;
		}
		data.customerMgt.cutCardNo = cutCardNo;
		
		if ($('#email').val() && $('#email').val().length > 200) {
			swal(_commonMsg.validationCheck, _msg.validEmailLength, 'warning');
			return false;
		}
		if($('#email').val() && $('#email').val().length <= 200) {
			let email = $('#email').val().trim();
			let regEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
			if(!regEmail.test(email)) {
				swal(_commonMsg.validationCheck, _msg.validEmailFormat, 'warning');
				return false;
			}
			data.email = email;
		}
		if($('#email').val() == '') {
			data.email = "";
		}
		
		data.customerMgt.customerId = customerId;
		data.customerMgt.cutManageCode = $("#cutManageCode").val();
		data.customerMgt.tagType = $("#tagType").val();
		return true;
	}

	function _validateCard(){
		data = {};
		data.customerId = customerId;
		if ($("#cutCardNo1").val() === '' || $("#cutCardNo2").val() === '' || $("#cutCardNo3").val() === '' || $("#cutCardNo4").val() === '') {
			swal(_commonMsg.validationCheck, _msg.validCardNoInput, "warning");
			return false;
		}
		data.cutCardNo = $("#cutCardNo1").val() + $("#cutCardNo2").val() + $("#cutCardNo3").val() + $("#cutCardNo4").val();
		data.custStatCode = $("#custStatCode").val();
		return true;
	}

	function _updateCustomerOnClick(){
		//
		if(($('#btnCardNoChecker').is(':visible'))){
			if(!cardNoChecker) {
				toastr.error(_msg.cardDuplicateCheck, _msg.customerMgmt);
				return ;
			}	
		}
		
		if(!_validate()){
			return ;
		}
		
		swal({
			title: _msg.customerMgmt,
			text: _msg.confirmChangeCustomer,
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: _msg.edit,
			cancelButtonText: _msg.cancel,
			closeOnConfirm: false
		}, function () {
			$.ajax({
				type : 'PUT' ,
				method : 'PUT',
				url : _ctx + "/ws/customer/" + customerId,
				contentType:"application/json",
				dataType : 'json' ,
				data : JSON.stringify(data),
				success : function(jsonData) {
					if (jsonData.status === 'SUCCESS') {
						toastr.success(_commonMsg.successModify, _msg.customerMgmt);
						_moveDetail(customerId);
					} else {
						toastr.error(jsonData.result + " " + _commonMsg.failModify, _msg.customerMgmt);
					}
				},
				error : function(xhRequest, ErrorText, thrownError) {
					//
					parent.layerJs.fn_exception(xhRequest);
					toastr.error(_commonMsg.failModify, _msg.customerMgmt);
				}
	    	});
		})
	}
	
	function _saveCustomerCardOnClick(){
		//
		 swal({
	            title: _msg.customerMgmt,
	            text: _msg.confirmChangeCard,
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: _msg.edit,
	            cancelButtonText: _msg.cancel,
	            closeOnConfirm: false
	        }, function () {
	        	if(!_validateCard()){
	    			return;
	    		} 
	    		
	    		$.ajax({
	    			type : 'PUT' ,
	    			method : 'PUT',
	    			url : _ctx + "/ws/customer/card/save/" + customerId,
	    			contentType:"application/json",
	    			dataType : 'json' ,
	    			data : JSON.stringify(data),
	    			success : function(jsonData) {
	    				if(jsonData.status === 'SUCCESS'){
	    					toastr.success(_commonMsg.successSave, _msg.customerCardMgmt);
	    					_moveDetail(customerId);
	    				}else{
	    					toastr.error(jsonData.result + " " + _commonMsg.failModify, _msg.customerMgmt);
	    				}
	    			},
	    			error : function(xhRequest, ErrorText, thrownError) {
	    				//
	    				parent.layerJs.fn_exception(xhRequest);
	    				toastr.error(_commonMsg.failModify, _msg.customerCardMgmt);
	    			}
	    		});
	        });
	}
	
	function _displayCarModel(){
		let carModelList = parent.carModelJs.getCarModel();
		let carModelSel = $("#carModelId");
		carModelSel.empty();
		carModelSel.append('<option value="">' + _msg.selectPlease + '</option>');
		for(let i =0, length = carModelList.length; i < length ; ++i){
			carModelSel.append('<option value="' + carModelList[i].carModelId + '">' + carModelList[i].carName + '</option>');
		}
	}
	
	function _moveDetail(id){
		//
		let param = '&pageNumber=' + queryString.pageNumber;
		param += "&pageItemSize=" +queryString.pageItemSize;
		param += "&custName=" + queryString.custName;
		param += "&condCustomerId=" + queryString.condCustomerId;
		param += "&mblPhoneNo=" + queryString.mblPhoneNo;
		param += "&cutCardNo=" + queryString.cutCardNo;
		param += "&cutGrdCode=" + queryString.cutGrdCode;
		param += "&order=" + queryString.order;
		param += "&unPaidYn=" + queryString.unPaidYn;
		param += "&searchType=" + queryString.searchType;

		if(queryString.pageItemSize){
			self.location= _ctx + "/customer/detail?customerId=" + id + param;
		}else{
			self.location= _ctx + "/customer/detail?customerId=" + id;
		}
	}
	
	function _listOnClick(){
		//
		let param = '?pageNumber=' + queryString.pageNumber;
		param += "&pageItemSize=" +queryString.pageItemSize;
		param += "&custName=" + queryString.custName;
		param += "&condCustomerId=" + queryString.condCustomerId;
		param += "&mblPhoneNo=" + queryString.mblPhoneNo;
		param += "&cutCardNo=" + queryString.cutCardNo;
		param += "&cutGrdCode=" + queryString.cutGrdCode;
		param += "&order=" + queryString.order;
		param += "&unPaidYn=" + queryString.unPaidYn;
		param += "&searchType=" + queryString.searchType;
   		if(queryString.pageItemSize){
   			self.location= _ctx + "/customer/list" + param;
   		}else{
   			self.location= _ctx + "/customer/list";
   		}
	}
	
	return {
		init : _init
	};
}();
