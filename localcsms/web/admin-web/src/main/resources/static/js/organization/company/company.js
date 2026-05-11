/**
 * 조직회사 등록/상세
 */
var companyJs = function(){
    "use strict";

    var data = {};
    var infoItems = [];
	function _init() {
		_initEvent();
		if (companyId) {
			_search(companyId);
		}
	};

	function _initEvent(){
		//
    	var url = '';

  		if (parent.UserRole !== 'COMPANY_USER') {
   			$("#companyPymtDiv").remove();
   		}

    	$("#btnCompanyRegister").click(function(){
			//
			_registerCompanyOnClick();
		});

		$("#btnList").click(function(){
			//
			_listOnClick();
		});
		$("#btnCompanyUpdate").click(function(){
			_updateCompanyOnClick();
		});

		$("#btnPaymentUpdate").click(function(){
			_updatePaymentOnClick();
		});

		$("#makerYn").click(function(){
			_makerYnCheckBoxOnClick();
		});

		$("#btnSelectPymtCard").click(function(){
			_selectPymtCardOnClick();
		});

		if (parent.UserRole === 'COMPANY_USER') {
			$("#bizRegNo").attr('readonly', true);
		}

		//우편번호검색
		$("#btnSearchZipCod").click(function(){
		    new daum.Postcode({
		        oncomplete: function(data) {
		        	$("#zipCode").val(data.zonecode);
		        	var address = data.address;
		        	if (data.buildingName) {
		        		address += '(' + data.buildingName + ')';
		        	}
		        	$("#roadName").val(address);
		        }
		    }).open();
		});

		//청구방법코드
		var billMethodCodes = parent.commonCodeJs.getCodesByParentCode('APPA00');
		for(var i =0, length = billMethodCodes.length; i < length ; ++i){
			$("#billMethodCode").append('<option value="' + billMethodCodes[i].code + '">' + billMethodCodes[i].codeName + '</option>');
		}
		$("#billMethodCode").val('APPA01');

		//결제요청코드
		var pymtReqCodes = parent.commonCodeJs.getCodesByParentCode('PAYF00');
		for(var i =0, length = pymtReqCodes.length; i < length ; ++i){
			if (pymtReqCodes[i].code !== 'PAYF04') {
				$("#pymtReqCode").append('<option value="' + pymtReqCodes[i].code + '">' + pymtReqCodes[i].codeName + '</option>');
			}
		}
	};

	function _registerCompanyOnClick(){
		//
		if(!_validateCompany()){
			return ;
		}

		$.ajax({
			type : 'POST' ,
			method : 'POST',
			url : _ctx + "/ws/organization/company/register",
			contentType:"application/json",
			dataType : 'json' ,
			data : JSON.stringify(data),
			success : function(jsonData) {
				if(jsonData.status == 'SUCCESS'){
					toastr.success(_commonMsg.successRegister, _msg.companyMgmt);
					_moveDetail(jsonData.result.companyId);
				}else{
					toastr.error(_commonMsg.failRegister, _msg.companyMgmt);
				}
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
				toastr.error(_commonMsg.failRegister, _msg.companyMgmt);
			}
		});
	};

	function _search(companyId){
		//
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/organization/company/retrieve/" + companyId,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayCompanyInfo(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
			}
		});
	};

	function _selectPymtCardOnClick () {
		var form = document.tranMgr;
		form.VerifyValue.value =$("#VerifyValue").val();
		form.ReturnURL.value = document.location.protocol + "//" + document.location.host + _ctx + "/smilePay/returnBillPay";
		form.MallUserID.value = companyId;
//		if ("<%=RequestType%>" == "PC") {
			var popupX = (window.screen.width / 2) - (545 / 2);
			var popupY = (window.screen.height /2) - (573 / 2);

			var winopts= "width=390,height=836,toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=no,resizable=no,left="+ popupX + ", top="+ popupY + ", screenX="+ popupX + ", screenY= "+ popupY;
			var win =  window.open("", "payWindow", winopts);

			try{
				if(win == null || win.closed || typeof win.closed == 'undefined' || win.screenLeft == 0) {
					alert(_commonMsg.popupBlocked);
					return false;
				}
			}catch(e){}

			form.target = "payWindow";//payWindow  고정
//		}
		$("#tranMgr").submit();
	}

	function _displayCompanyInfo(jsonData){
		if(!jsonData){
			return ;
		}
		data = jsonData;
		$("#companyName").val(jsonData.companyName);
		$("#bizRegNo").val(jsonData.bizRegNo);
		$("#ceoName").val(jsonData.ceoName);
		//우편번호
		$("#zipCode").val(jsonData.zipCode);
		//도로명
		$("#roadName").val(jsonData.roadName);
		//도로명상세
		$("#roadDetName").val(jsonData.roadDetName);
		//결제카드아이디
		$("#pymtCardId").val(jsonData.id);
		//결제요청
		$("#pymtReqCode").val(jsonData.pymtReqCode);
		//
		$("#billKey").val(jsonData.billKey);
		$("#cardTrxnNo").val(jsonData.cardTrxnNo);
		$("#cardCoType").val(jsonData.cardCoType);
		$("#displayCardNo").val(jsonData.billKey);

		//고객수
		$("#custCount").val(jsonData.custCount);
		var makerYn =  false;
		var possessionYn = false;

		//제조사여부
		if (jsonData.makerYn && jsonData.makerYn === 'Y') {
			makerYn = true;
		}

		$("#makerYn").attr('checked', makerYn);
		_makerYnCheckBoxOnClick();
		if (jsonData.infoItems) {
			infoItems = jsonData.infoItems;
			jsonData.infoItems.forEach(function(infoItem, index, infoItems) {
				if (infoItem.infoItemId === 'CPIT01') {
					$("#makerType").val(infoItem.infoItemValue);
				}
			});
		}

		//소유사여부
		if (jsonData.possessionYn && jsonData.possessionYn === 'Y') {
			possessionYn = true;
		}
		$("#possessionYn").attr('checked', possessionYn);
	};

	function _validateCompany(){
		//회사정보
		data = {};

		data.companyId = companyId;
		if ($("#companyName").val() === '') {
			swal(_commonMsg.validationCheck, _msg.enterCompanyName, "warning");
			return false;
		}
		data.companyName = $("#companyName").val();

		if ($("#bizRegNo").val() === '') {
			swal(_commonMsg.validationCheck, _msg.enterBizRegNo, "warning");
			return false;
		}
		data.bizRegNo = $("#bizRegNo").val();

		if ($("#ceoName").val() === '') {
			swal(_commonMsg.validationCheck, _msg.enterCeoName, "warning");
			return false;
		}
		data.ceoName = $("#ceoName").val();

		data.zipCode = $("#zipCode").val();
		data.roadName = $("#roadName").val();
		data.roadDetName = $("#roadDetName").val();
		data.makerYn = $("#makerYn").is(":checked") ? 'Y' : 'N';
		data.possessionYn = $("#possessionYn").is(":checked") ? 'Y' : 'N';

		infoItems = infoItems ?  infoItems : [];
		infoItems && infoItems.forEach(function(infoItem, index, infoItems) {
			if (infoItem.infoItemId === 'CPIT01') {
				infoItems.splice(index,1);
			}
		});

		if ($("#makerYn").is(":checked")) {
			var infoItem =
				{
					companyId : companyId,
					infoItemId : 'CPIT01',
					infoItemValue : $("#makerType").val(),
				};
			infoItems.push(infoItem);
			data.infoItems = infoItems;
		} else {

		}
		return true;
	};


	function _validatePayment(){

		//결제회원정보
		data = {};
		data.pymtTargetId = companyId;
		data.targetType = 'Company';
		data.billMethodCode = $("#billMethodCode").val();
		data.paymentCardId = $("#pymtCardId").val() ? $("#pymtCardId").val() : 0;
		data.pymtReqCode = $("#pymtReqCode").val();

		//결제카드정보
		data.paymentCard = {};
		data.paymentCard.id = $("#pymtCardId").val() ? $("#pymtCardId").val() : 0;
		data.paymentCard.pymtTargetId = companyId;
		data.paymentCard.targetType = 'Company';
		data.paymentCard.billKey = $("#billKey").val();
		data.paymentCard.cardTrxnNo = $("#cardTrxnNo").val();
		data.paymentCard.cardCoType = $("#cardCoType").val();

		return true;
	};

	function _updatePaymentOnClick(){
		//
		 swal({
	            title: _msg.companyMgmt,
	            text: _msg.confirmChangePayment,
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: _msg.btnModify,
	            cancelButtonText: _msg.btnCancel,
	            closeOnConfirm: false
	        }, function () {
	        	if(!_validatePayment()){
	    			return ;
	    		}

	    		$.ajax({
	    			type : 'PUT' ,
	    			method : 'PUT',
	    			url : _ctx + "/ws/payment/targetPaymentInfo/" + companyId,
	    			contentType:"application/json",
	    			dataType : 'json' ,
	    			data : JSON.stringify(data),
	    			success : function(jsonData) {
	    				if(jsonData.status == 'SUCCESS'){
	    					toastr.success(_commonMsg.successModify, _msg.companyMgmt);
	    					_moveDetail(companyId);
	    				}else{
	    					toastr.error(_commonMsg.failModify, _msg.companyMgmt);
	    				}
	    			},
	    			error : function(xhRequest, ErrorText, thrownError) {
	    				//
	    				parent.layerJs.fn_exception(xhRequest);
	    				toastr.error(_commonMsg.failModify, _msg.companyMgmt);
	    			}
	    		});
	        });
	};

	function _updateCompanyOnClick(){
		//
		 swal({
	            title: _msg.companyMgmt,
	            text: _msg.confirmChangeCompany,
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: _msg.btnModify,
	            cancelButtonText: _msg.btnCancel,
	            closeOnConfirm: false
	        }, function () {
	        	if(!_validateCompany()){
	    			return ;
	    		}

	    		$.ajax({
	    			type : 'PUT' ,
	    			method : 'PUT',
	    			url : _ctx + "/ws/organization/company/modify/" + companyId,
	    			contentType:"application/json",
	    			dataType : 'json' ,
	    			data : JSON.stringify(data),
	    			success : function(jsonData) {
	    				if(jsonData.status == 'SUCCESS'){
	    					toastr.success(_commonMsg.successModify, _msg.companyMgmt);
	    					_moveDetail(companyId);
	    				}else{
	    					toastr.error(_commonMsg.failModify, _msg.companyMgmt);
	    				}
	    			},
	    			error : function(xhRequest, ErrorText, thrownError) {
	    				//
	    				parent.layerJs.fn_exception(xhRequest);
	    				toastr.error(_commonMsg.failModify, _msg.companyMgmt);
	    			}
	    		});
	        });
	};


	function _selectedEmployee(employeeId, emplName) {
		$("#employeeId").val(employeeId);
		$("#emplName").val(emplName);
	}

	function _selectedPaymentCard() {
	}

	function _listOnClick(){

		let param = '?pageNumber=' + queryString.pageNumber;
		param += "&pageItemSize=" + queryString.pageItemSize;
		param += "&bizRegNo=" + queryString.bizRegNo;
		param += "&companyName=" + queryString.companyName;
		param += "&searchType=" + queryString.searchType;
   		param += "&sWord=" + queryString.sWord;

   		if(queryString.pageItemSize){
   			self.location= _ctx + "/organization/company/list" + param;
   		}else{
   			self.location= _ctx + "/organization/company/list";
   		}
	};

	function _moveDetail(id){
		self.location= _ctx + "/organization/company/detail?companyId=" + id;
	};

	function _makerYnCheckBoxOnClick() {
		if ($("#makerYn").is(":checked")) {
			$("#checkBoxTr").after('<tr id="makerSelectTr"><th>' + _msg.manufacturer + '</th><td><select name="makerType" id="makerType" class="form-control input-sm"></select></td></tr>');
			//제조사
	        var makerTypes = parent.kecoCodeJs.getCodesByParentCode("MAKER");
	        $("#makerType").append('<option value="">' + _msg.optionSelect + '</option>');
			var html = '';
			for(var i =0, size = makerTypes.length; i < size ; ++i){
				html = '<option value="' + makerTypes[i].kecoCode + '">';
				html += makerTypes[i].codeName;
				html += '</option>';
				$("#makerType").append(html);
			}
		} else {
			$("#makerSelectTr").remove();
		}
	}

	return {
		init : _init,
		selectedEmployee : _selectedEmployee,
		selectedPaymentCard : _selectedPaymentCard
	};
}();
