/**
 * 조직회사 등록/상세
 */
let employeeJs = function(){
    'use strict';

    let data = {};
    let currentLoginId = '';
    let isLoginIdDuple = true;

	function _init() {
		$("#companyId").val("CO0000001");
		$("#companyName").val(_msg.aptManageOffice);

		_initEvent();
		if (employeeId) {
			_search(employeeId);
		}
	}

	function _initEvent(){
		//
		if(queryString.status == "2") {
			$('#loginPartDiv').hide();
			$('#btnEmployeeUpdate').hide();
			$('#btnEmployeeRemove').hide();
			$('#btnSearchCompany').hide();
			$('#companyId').attr("readonly",true);
			$('#emplName').attr("readonly",true);
			$('#companyName').attr("readonly",true);
			$('#roleType').attr("readonly",true);
			$('#deptName').attr("readonly",true);
			$('#mblPhoneNo1').attr("readonly",true);
			$('#mblPhoneNo2').attr("readonly",true);
			$('#mblPhoneNo3').attr("readonly",true);
			$('#ofcPhoneNo').attr("readonly",true);
			$('#email').attr("readonly",true);
		}
		//소속회사 검색 팝업
		$('#btnSearchCompany').click(function(){
			$('#Popup_Company').modal();
			companyPopupJs.init(_selectedCompany);

		});

		/*$('#companyName').click(function(){
			$('#Popup_Company').modal();
			companyPopupJs.init(_selectedCompany);

		});*/

		$('#btnEmployeeRegister').click(function(){
			//
			_registerEmployeeOnClick();
		});

		$('#btnList').click(function(){
			//
			_listOnClick();
		});
		$('#btnEmployeeUpdate').click(function(){
			_updateEmployeeOnClick();
		});
		$('#btnUserUpdate').click(function(){
			_updateUserOnClick();
		});
		$('#btnEmployeeRemove').click(function(){
			_removeEmployeeOnClick();
		});
		$('#btnDupleLoginId').click(function(){
			_checkDupleLoginIdOnClick();
		});
		/*$('#luId').change(function(){
			_changeLoginIdInput();
		});*/
		$('#btnAccessIpUpdate').click(function(){
			_updateAccessIpOnClick();
		});

		$('.i-checks').iCheck({
            checkboxClass: 'icheckbox_square-green'
		});

        $('.dual_select').bootstrapDualListbox({
            selectorMinimalHeight: 160
        });
        $('#pwFailCntLabel').html(_msg.pwFailWarning);
		$('#pwFailCntLabel').css('color', 'red');
	}


	function _changeLoginIdInput () {
		isLoginIdDuple = true;
		$('#loginIdCheckLabel').html(_msg.duplicateCheckRequired);
		$('#loginIdCheckLabel').css('color', 'red');
	}

	function _checkDupleLoginIdOnClick(){

		if ($('#luId').val() === '') {
			swal(_commonMsg.validationCheck, _msg.enterLoginId, 'warning');
			return false;
		}
		let loginId = $('#luId').val();

		let regLogin = /^[0-9a-zA-Z]{5,21}$/;
		if(!regLogin.test(loginId)){
			$('#loginIdCheckLabel').html(_msg.duplicateCheckRequired);
			$('#loginIdCheckLabel').css('color', 'red');
			swal(_commonMsg.validationCheck, _msg.loginIdFormat, 'warning');
			return false;
		}

		$.ajax({
			type: 'GET' ,
			url : _ctx + '/ws/system/user/checkDupleLoginId/' + loginId,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_checkDupleLoginId(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
			}
		});
	}

	function _checkDupleLoginId (jsonData) {
		isLoginIdDuple = jsonData;
		let contents = _msg.loginIdAlreadyExists;
		let color = 'red';
		if (!isLoginIdDuple || $('#luId').val() === data.loginId) {
			contents = _msg.loginIdAvailable;
			color = 'green';
		}
			$('#loginIdCheckLabel').html(contents);
			$('#loginIdCheckLabel').css('color', color);
	}


	function _registerEmployeeOnClick(){
		//
		if(!_validateEmployee()){
			return ;
		}
		$.ajax({
			type : 'POST' ,
			method : 'POST',
			url : _ctx + '/ws/organization/employee',
			contentType:'application/json',
			dataType : 'json' ,
			data : JSON.stringify(data),
			success : function(jsonData) {
				if(jsonData.status === 'SUCCESS'){
					toastr.success(_commonMsg.successRegister, _msg.employeeMgmt);
					_moveDetail(jsonData.result.employeeId);
				}else{
					toastr.error(_commonMsg.failRegister, _msg.employeeMgmt);
				}
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
				toastr.error(_commonMsg.failRegister, _msg.employeeMgmt);
			}
		});
	}

	function _search(employeeId){
		//
		$.ajax({
			type: 'GET' ,
			url : _ctx + '/ws/organization/employee/' + employeeId,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayEmployeeInfo(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
			}
		});
	}

	function _displayEmployeeInfo(jsonData){
		if(!jsonData){
			return ;
		}
		data = jsonData;
		$('#companyName').val(jsonData.companyName);
		$('#companyId').val(jsonData.companyId);

		//메인역할
		$('#roleType').val(jsonData.roleType);

		//직원명
		$('#emplName').val(jsonData.emplName);
		//부서명
		$('#deptName').val(jsonData.deptName);
		//휴대폰번호
		let mblPhoneNo = formmatUtilsJs.phoneFormat(jsonData.mblPhoneNo);
		$('#mblPhoneNo1').val(mblPhoneNo.substring(0,3));
		$('#mblPhoneNo2').val(mblPhoneNo.substring(4,mblPhoneNo.length-5));
		$('#mblPhoneNo3').val(mblPhoneNo.substring(mblPhoneNo.length-4,mblPhoneNo.length));
		//$('#mblPhoneNo').val(formmatUtilsJs.phoneFormat(jsonData.mblPhoneNo));
		//사무실전화번호
		$('#ofcPhoneNo').val(formmatUtilsJs.phoneFormat(jsonData.ofcPhoneNo));

		//이메일
		$('#email').val(jsonData.email);

		//비밀번호 실패 카운트
		$('#pwFailCnt').val(jsonData.pwFailCount);
		//마지막 로그인 날짜
		if(jsonData.lastLoginDate == null){
			$('#lastLoginDate').val('-');
		} else {
			$('#lastLoginDate').val(dateUtilsJs.formatDate(new Date(jsonData.lastLoginDate), 'YYYY-MM-DD HH:MM:SS'))
		}

		//로그인아이디
		if (!jsonData.loginId) {
			$('#luId').removeAttr("readonly");
			$('#loginIdCheckLabel').html(_msg.duplicateCheckRequired);
			$('#loginIdCheckLabel').css('color', 'red');
		} else {
			$('#btnDupleLoginId').hide();
			$('#loginIdCheckLabel').html('');
		}
		$('#luId').val(jsonData.loginId);
		currentLoginId = jsonData.loginId;
	}


	function _validateEmployee(){
		//직원정보
		data.employeeId = employeeId;
		/*if (!$('#companyId').val()) {
			swal(_commonMsg.validationCheck, '소속법인을 선택해 주세요.', 'warning');
			return false;
		}*/
		data.companyId = $('#companyId').val();
		if (!$('#roleType').val()) {
			swal(_commonMsg.validationCheck, _msg.selectMainRole, 'warning');
			return false;
		}
		data.roleType = $('#roleType').val();

		if (!$('#emplName').val() || $('#emplName').val().length > 16) {
			swal(_commonMsg.validationCheck, _msg.enterEmployeeName, 'warning');
			return false;
		}
		data.emplName = $('#emplName').val();

		if ($('#deptName').val() && $('#deptName').val().length > 100) {
			swal(_commonMsg.validationCheck, _msg.enterDeptName, 'warning');
			return false;
		}
		if($('#deptName').val() && $('#deptName').val().length < 100) {
			data.deptName = $('#deptName').val();
		}
		if($('#deptName').val() == '') {
			data.deptName = "";
		}


		let mblPhoneNo = $("#mblPhoneNo1").val() + $("#mblPhoneNo2").val() + $("#mblPhoneNo3").val();
		if(!$('#mblPhoneNo1').val() || !$('#mblPhoneNo2').val() || !$('#mblPhoneNo3').val()){
			swal(_commonMsg.validationCheck, _msg.enterPhone, 'warning');
			return false;
		}
		if($('#mblPhoneNo2').val().length < 4 || $('#mblPhoneNo3').val().length < 4){
			swal(_commonMsg.validationCheck, _msg.enterValidPhone, 'warning');
			return false;
		}
		data.mblPhoneNo = mblPhoneNo;

		let ofcPhoneNo = $('#ofcPhoneNo').val().replace(/-/gi, '');
		if($('#ofcPhoneNo').val() && ofcPhoneNo.length != 11) {
			swal(_commonMsg.validationCheck, _msg.enterOfficePhone, 'warning');
			return false;
		}
		data.ofcPhoneNo = ofcPhoneNo;

		if(ofcPhoneNo == "") {
			data.ofcPhoneNo = "";
		}

		if ($('#email').val() && $('#email').val().length > 200) {
			swal(_commonMsg.validationCheck, _msg.enterEmail, 'warning');
			return false;
		}
		if($('#email').val() && $('#email').val().length <= 200) {
			let email = $('#email').val().trim();
			let regEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
			if(!regEmail.test(email)) {
				swal(_commonMsg.validationCheck, _msg.enterValidEmail, 'warning');
				return false;
			}
			data.email = email;
		}
		if($('#email').val() == '') {
			data.email = "";
		}

		return true;
	}

	function _validateUser(){
		data = {};

		if (currentLoginId !==  $('#luId').val()) {
			if (isLoginIdDuple) {
				swal(_commonMsg.validationCheck, _msg.checkLoginIdDuplicate, 'warning');
				return false;
			}
		} else {
			if ($('#wdId').val() === '') {
				swal(_commonMsg.validationCheck, _msg.noChanges, 'warning');
				return false;
			}
		}
		data.loginId = $('#luId').val();
		if ($('#wdId').val() !== '') {
			if ($('#wdId').val() !== $('#wdId2').val()) {
				swal(_commonMsg.validationCheck, _msg.passwordMismatch, 'warning');
				return false;
			}
			let userPwd = $('#wdId').val();
			let regLogin = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,24}$/;
			if(!regLogin.test(userPwd)){
				swal(_commonMsg.validationCheck, _msg.passwordFormat, 'warning');
				return false;
			}
			data.userPwd = $('#wdId').val();
		} else {
			swal(_commonMsg.validationCheck, _msg.enterPassword, 'warning');
			return false;
		}
		data.userType = 'EMPLOYEE';

		return true;
	}

	function _updateUserOnClick(){
		//
		if(!_validateUser()){
			return ;
		}
		data.emplStatus = "1";

		if(pwInitYn == 'N') {
			data.pwInitYn = 'Y';
			swal({
			title: _msg.employeeMgmt,
			text: _msg.confirmModifyLogin,
			type: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#DD6B55',
			confirmButtonText: _msg.btnModify,
			cancelButtonText: _msg.btnCancel,
			}, function () {
				$.ajax({
					type : 'PUT' ,
					method : 'PUT',
					url : _ctx + '/ws/system/user/' + employeeId,
					contentType:'application/json',
					dataType : 'json' ,
					data : JSON.stringify(data),
					success : function(jsonData) {
						if(jsonData.status === 'SUCCESS' && pwInitYn == 'N'){
							toastr.success(_commonMsg.successModify, _msg.employeeMgmt);
						}
						else{
							toastr.error(_commonMsg.failModify, _msg.employeeMgmt);
						}
					},
					error : function(xhRequest, ErrorText, thrownError) {
						//
						parent.layerJs.fn_exception(xhRequest);
						toastr.error(_commonMsg.failModify, _msg.employeeMgmt);
					}
				});
			});
		}
		if(pwInitYn == 'Y'){
			data.pwInitYn = 'N';
			swal({
			title: _msg.employeeMgmt,
			text: _msg.confirmModifyLogin,
			type: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#DD6B55',
			confirmButtonText: _msg.btnModify,
			cancelButtonText: _msg.btnCancel,
		}, function () {
			$.ajax({
				type : 'PUT' ,
				method : 'PUT',
				url : _ctx + '/ws/system/user/' + employeeId,
				contentType:'application/json',
				dataType : 'json' ,
				data : JSON.stringify(data),
				success : function(jsonData) {
					if(jsonData.status === 'SUCCESS'){
						toastr.success(_msg.successModifyRedirect, _msg.employeeMgmt);
						setTimeout(function () {
							location.href='/evAdmin/logout';
						}, 3000);
					}
					else{
						toastr.error(_commonMsg.failModify, _msg.employeeMgmt);
					}
				},
				error : function(xhRequest, ErrorText, thrownError) {
					//
					parent.layerJs.fn_exception(xhRequest);
					toastr.error(_commonMsg.failModify, _msg.employeeMgmt);
				}
			});
		});
		}

	}

	function _updateEmployeeOnClick(){
		//
		if(!_validateEmployee()){
			return ;
		}
		swal({
			title: _msg.employeeMgmt,
			text: _msg.confirmModifyEmployee,
			type: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#DD6B55',
			confirmButtonText: _msg.btnModify,
			cancelButtonText: _msg.btnCancel,
		}, function () {
			$.ajax({
				type : 'PUT' ,
				method : 'PUT',
				url : _ctx + '/ws/organization/employee/' + employeeId,
				contentType:'application/json',
				dataType : 'json' ,
				data : JSON.stringify(data),
				success : function(jsonData) {
					if(jsonData.status === 'SUCCESS'){
						toastr.success(_commonMsg.successModify, _msg.employeeMgmt);
						_moveDetail(employeeId);
					}else{
						toastr.error(_commonMsg.failModify, _msg.employeeMgmt);
					}
				},
				error : function(xhRequest, ErrorText, thrownError) {
					//
					parent.layerJs.fn_exception(xhRequest);
					toastr.error(_commonMsg.failModify, _msg.employeeMgmt);
				}
			});
		});
	}

	function _removeEmployeeOnClick(){
		//
		swal({
			title: _msg.employeeMgmt,
			text: _msg.confirmResignation,
			type: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#ED5565',
			confirmButtonText: _msg.btnResignation,
			cancelButtonText: _msg.btnCancel,
		}, function () {
			$.ajax({
				type : 'DELETE' ,
				method : 'DELETE',
				url : _ctx + '/ws/organization/employee/' + employeeId,
				contentType: 'application/json',
				dataType : 'json' ,
				data : JSON.stringify(data),
				success : function(jsonData) {
					if(jsonData.status === 'SUCCESS'){
						toastr.success(_commonMsg.successProcess, _msg.employeeMgmt);
						_listOnClick();
					}else{
						toastr.error(_msg.failResignation, _msg.employeeMgmt);
					}
				},
				error : function(xhRequest, ErrorText, thrownError) {
					parent.layerJs.fn_exception(xhRequest);
					toastr.error(_msg.failResignation, _msg.employeeMgmt);
				}
			});
		});
	}

	function _updateAccessIpOnClick() {
		let accessIp = $('#accessIp').val().trim().replace(/' '/g, '');
		if(accessIp === ''){
			return swal(_commonMsg.validationCheck, _msg.enterAccessIp, 'warning');
		}
		accessIp = {
			accessIp: accessIp,
		}
		toastr.warning(_msg.savingAccessIp, _msg.saving);
		$.ajax({
			type : 'POST' ,
			method : 'POST',
			url : _ctx + '/ws/system/accessIp/' + employeeId,
			contentType: 'application/json',
			dataType : 'json' ,
			data : JSON.stringify(accessIp),
			success : function(jsonData) {
				if(jsonData.status === 'SUCCESS'){
					toastr.success(_commonMsg.successProcess, _msg.accessIp);
					_moveDetail(employeeId);
				}else{
					toastr.error(jsonData.result, _msg.accessIp);
				}
			},
			error : function(xhRequest, ErrorText, thrownError) {
				parent.layerJs.fn_exception(xhRequest);
				toastr.error(jsonData.result, _msg.accessIp);
			}
		});
	}

	function _selectedCompany(param) {
		$('#companyId').val(param.companyId);
		$('#companyName').val(param.companyName);
	}

	function _moveDetail(id){
		self.location= _ctx + '/organization/employee/detail?employeeId=' + id;
	}

	function _listOnClick(){

		let param = '?pageNumber=' + queryString.pageNumber;
		param += "&pageItemSize=" + queryString.pageItemSize;
		param += "&emplName=" + queryString.emplName;
		param += "&companyName=" + queryString.companyName;
		param += "&searchType=" + queryString.searchType;
   		param += "&sWord=" + queryString.sWord;

   		if(queryString.pageItemSize){
   			self.location= _ctx + "/organization/employee/list" + param;
   		}else{
   			self.location= _ctx + "/organization/employee/list";
   		}
	}

	return {
		init : _init,
		selectedCompany : _selectedCompany
	};
}();
