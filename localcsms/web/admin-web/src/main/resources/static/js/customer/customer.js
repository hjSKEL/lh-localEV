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
			_moveListButtonToBottom();
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

		_resolveComplexId(function(){
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
		});
	}

	/**
	 * cxNum(단지, 4자리 숫자) → complexId 매핑 후 콜백 실행. 기존 단지 검색 API 재사용(신규 엔드포인트 불필요).
	 * cxNum이 비어있으면 complexId=null로 바로 콜백. 일치하는 단지가 없으면 저장을 막고 경고만 표시.
	 */
	function _resolveComplexId(callback){
		let cxNum = $("#cxNum").val();
		if(!cxNum){
			data.complexId = null;
			callback();
			return;
		}
		$.ajax({
			type: 'GET',
			url: _ctx + "/ws/organization/complex/search",
			data: { complexName: cxNum },
			dataType: 'json',
			success: function(page){
				let matched = ((page && page.result) || []).find(function(c){
					return String(c.complexName) === cxNum;
				});
				if(!matched){
					swal(_commonMsg.validationCheck, '단지번호(' + cxNum + ')를 찾을 수 없습니다.', 'warning');
					return;
				}
				data.complexId = matched.complexId;
				callback();
			},
			error : function(xhRequest, ErrorText, thrownError) {
				parent.layerJs.fn_exception(xhRequest);
			}
		});
	}

	function _expandAfterRegister(id){
		// 카드/차량 없이 저장 완료 → 같은 화면에서 회원카드/차량 등록으로 확장 (별도 이동 없음)
		// pageTitle은 '고객 등록'을 그대로 유지 - 화면 이동 없이 이어서 등록하는 흐름이라 상세 조회로 바뀐 게 아님.
		customerId = id;
		history.replaceState(null, '', _ctx + "/customer/detail?customerId=" + id);
		$("#custName").prop('readonly', true);
		$("#cardListSection").show();
		$("#vehicleListSection").show();
		$("#btnCustomerRegister").hide();
		$("#btnCustomerUpdate").show();
		_moveListButtonToBottom();
		toastr.info(_msg.registerCardVehicleGuide, _msg.customerMgmt);
		customerVehicleJs.init(customerId);
		customerCardListJs.init(customerId);
	}

	// 회원카드목록/등록차량 영역이 표출되는 상태(기존 고객 상세, 등록 직후 확장)에서는
	// 상단의 '목록'/'수정' 버튼을 떼어내 등록 차량 영역 아래 위치로 옮긴다. append 순서가 그대로
	// pull-right 배치 순서(먼저 붙인 쪽이 오른쪽) 이므로 btnList를 먼저 붙여야 수정이 목록 왼쪽에 온다.
	function _moveListButtonToBottom(){
		var $target = $("#btnListBottomSection").show().find(".ibox-content");
		$target.append($("#btnList"));
		$target.append($("#btnCustomerUpdate"));
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

		if(jsonData.customerMgt && jsonData.customerMgt.registrationDate) {
			$("#regDate").val(dateUtilsJs.formatDate(new Date(jsonData.customerMgt.registrationDate), 'YYYY-MM-DD HH:MM:SS'));
		} else {
			$("#regDate").val("");
		}

		$("input[name='custDel'][value='" + (jsonData.delYn === 'Y' ? 'Y' : 'N') + "']").prop('checked', true);
		$("#custDelDT").text(jsonData.delDate ? '(삭제일: ' + dateUtilsJs.formatDate(new Date(jsonData.delDate), 'YYYY-MM-DD') + ')' : '');
		$("input[name='custStop'][value='" + (jsonData.stopYn === 'Y' ? 'Y' : 'N') + "']").prop('checked', true);
		$("#custStopDT").text(jsonData.stopDate ? '(정지일: ' + dateUtilsJs.formatDate(new Date(jsonData.stopDate), 'YYYY-MM-DD') + ')' : '');
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

		if(!$("#dong").val() || !$("#ho").val()){
			swal(_commonMsg.validationCheck, '세대정보(동/호)를 입력해 주세요.', 'warning');
			return false;
		}
		data.dong = $("#dong").val();
		data.ho = $("#ho").val();
		// cxNum(단지코드)→complexId 매핑은 _resolveComplexId()에서 처리 (저장 직전 비동기 조회)

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

		data.delYn = $("input[name='custDel']:checked").val() || 'N';
		data.stopYn = $("input[name='custStop']:checked").val() || 'N';

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
			_resolveComplexId(function(){
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
