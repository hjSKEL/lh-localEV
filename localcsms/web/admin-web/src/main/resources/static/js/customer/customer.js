/**
 * 고객 등록/상세
 */
let customerJs = function(){
	"use strict";

	let data = {};

	function _init() {
		_initEvent();
		if (customerId) {
			_search(customerId);
		}
	}

	function _initEvent(){
		//
		$("#btnCustomerRegister").click(function(){
			_registerCustomerOnClick();
		});
		$("#btnList").click(function(){
			_listOnClick();
		});
		$("#btnCustomerUpdate").click(function(){
			_updateCustomerOnClick();
		});
	}

	function _registerCustomerOnClick(){
		//
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
					_expandAfterRegister(jsonData.result);
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

	function _expandAfterRegister(id){
		// 카드/차량 없이 저장 완료 → 같은 화면에서 회원카드/차량 등록으로 확장 (별도 이동 없음)
		customerId = id;
		history.replaceState(null, '', _ctx + "/customer/detail?customerId=" + id);
		$("#pageTitle").text("고객 상세");
		$("#custName").prop('readonly', true);
		$("#cardListSection").show();
		$("#vehicleListSection").show();
		$("#btnCustomerRegister").hide();
		$("#btnCustomerUpdate").show();
		customerVehicleJs.init(customerId);
		customerCardListJs.init(customerId);
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
			}
		});
	}

	function _displayCustomerInfo(jsonData){

		if(!jsonData){
			return ;
		}
		data = jsonData;
		$("#custName").val(jsonData.custName);
		let mblPhoneNo = formmatUtilsJs.phoneFormat(jsonData.mblPhoneNo);
		$('#mblPhoneNo1').val(mblPhoneNo.substring(0,3));
		$('#mblPhoneNo2').val(mblPhoneNo.substring(4,mblPhoneNo.length-5));
		$('#mblPhoneNo3').val(mblPhoneNo.substring(mblPhoneNo.length-4,mblPhoneNo.length));
		$("#email").val(jsonData.email);
		$("#cxNum").val(jsonData.complexName || "");
		$("#dong").val(jsonData.dong || "");
		$("#ho").val(jsonData.ho || "");
		// cxNum은 조회 전용(위 한 번의 _search 응답에 TB_ORCX001 조인 결과로 포함됨). 저장(cxNum→complexId 매핑)은 단지 조회 API 연결 후 처리 예정

		if(jsonData.customerMgt && jsonData.customerMgt.registrationDate) {
			$("#regDate").val(dateUtilsJs.formatDate(new Date(jsonData.customerMgt.registrationDate), 'YYYY-MM-DD HH:MM:SS'));
		} else {
			$("#regDate").val("");
		}

		// 고객상태 - 공통코드 MEMK00, TB_CUCU002.CUT_MNG_CD = TB_SYCO001.CD 일 때 CD_NM
		let cutManageCode = jsonData.customerMgt && jsonData.customerMgt.cutManageCode;
		$("#custStatCodeName").val(cutManageCode ? parent.commonCodeJs.getCodeNameByPCodeNSubCode('MEMK00', cutManageCode) : "");
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

		if(!$("#dong").val() || !$("#ho").val()){
			swal(_commonMsg.validationCheck, '세대정보(동/호)를 입력해 주세요.', 'warning');
			return false;
		}
		data.dong = $("#dong").val();
		data.ho = $("#ho").val();
		// cxNum(단지코드) → complexId 매핑은 단지 조회 API 연결 후 추가 예정 (현재는 미전송)

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

		return true;
	}

	function _updateCustomerOnClick(){
		//
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
