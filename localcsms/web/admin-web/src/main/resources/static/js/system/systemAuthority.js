/**
 * 시스템관리 - 권한관리
 */
var systemAuthorityJs = function(){
    "use strict";

    var totalMenus = 0;
    var targetUserRoleType = '';

	function _init() {
		_initEvent();
		//권한조회
		_searchUserRoleTypeList();
		//메뉴조회
		_searchAllChildMenuList();
	};

	function _initEvent(){
		//
	};

	function _searchUserRoleTypeList(){
		//tBodyAutorityList
		$("#tBodyAutorityList").empty();
		var html = '<tr style="text-align:center;">';
		html += '<td colspan="3">' + _commonMsg.searching + '</td>';
		$("#tBodyAutorityList").append(html);

		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/system/userRoleTypeList",
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayUserRoleTypeList(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
			}
		});
	};

	function _displayUserRoleTypeList(jsonData){

		$("#tBodyAutorityList").empty();
		var html = '';
		if(jsonData.length == 0){
			html = '<tr style="text-align:center;">';
			html += '<td colspan="3">' + _commonMsg.noData + '</td>';
			html += '</tr>';
			$("#tBodyAutorityList").append(html);
			return ;
		}
		var result = jsonData;
		for(var i=0, length = result.length ; i < length ; ++i){
			html = '<tr>';
			html += '<td><input name="loadSpot" type="radio" onchange="systemAuthorityJs.searchUserRoleTypeMenuList(\'' + result[i].code +'\')" id="autority_radio_' + i + '" /></td>';
			html += '<td>' + result[i].code+ '</td>';
			html += '<td>' + result[i].value + '</td>';
			html += '</tr>';
			$("#tBodyAutorityList").append(html);
		}
	};

	function _searchAllChildMenuList(){
		//tBodyMenuList
		$("#tBodyMenuList").empty();
		var html = '<tr>';
		html += '<td colspan="5">' + _commonMsg.searching + '</td>';
		$("#tBodyMenuList").append(html);

		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/system/allChildMenuList",
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayAllChildMenuList(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
			}
		});
	};

	function _displayAllChildMenuList(jsonData){

		$("#tBodyMenuList").empty();
		var html = '';
		if(jsonData.length == 0){
			html = '<tr>';
			html += '<td colspan="5">' + _commonMsg.noData + '</td>';
			html += '</tr>';
			$("#tBodyMenuList").append(html);
			return ;
		}
		var result = jsonData;
		totalMenus = result.length;
		for(var i=0, length = result.length ; i < length ; ++i){
			html = '<tr>';
			html += '<td><input name="loadSpot" type="checkbox"  onclick="systemAuthorityJs.changeUserRoleTypeAuthForMenu(this, \'' + result[i].menuId +'\')" id="menu_checkbox_' + i + '" /></td>';
			html += '<td>' + result[i].highMenuName+ '</td>';
			html += '<td>' + result[i].menuName + '</td>';
			html += '<td id = "menuId_' + i + '">' + result[i].menuId + '</td>';
			html += '</tr>';
			$("#tBodyMenuList").append(html);
		}
	};


	function _searchUserRoleTypeMenuList (authName) {
		targetUserRoleType = authName;
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/system/userRoleTypeMenuList?userRoleTypeStr=" + authName,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_checkedMenuForUserRoleType(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
			}
		});
	}

	function _checkedMenuForUserRoleType (jsonData) {
		_checkClearMenus();
		if (jsonData != null && jsonData.length != 0) {
			for (var index in jsonData) {
				if (jsonData[index] != null && jsonData[index] != 0) {
					for (var subIndex in jsonData[index].child) {
						_checkExistInMenus(jsonData[index].child[subIndex].menuId)
					}
				}
			}
		}
	}

	function _checkExistInMenus (menuId) {
		for (var i = 0; i < totalMenus; i++) {
			var targetMenuId =  $("#menuId_" + i).html();
			if (menuId === targetMenuId) {
				$('#menu_checkbox_'+ i).prop('checked', true);
			}
		}
	}

	function _checkClearMenus () {
		for (var i = 0; i < totalMenus; i++) {
				$('#menu_checkbox_'+ i).prop('checked', false);
		}
	}

	function _changeUserRoleTypeAuthForMenu (event, value) {

		if (!targetUserRoleType || targetUserRoleType == null || targetUserRoleType == '') {
			swal(_msg.authorityMgmt, _msg.selectAuthority, "warning");
			return;
		}


		var data = {
				checked : event.checked,
				menuId : value,
				userRoleTypeStr : targetUserRoleType
			};
		console.log(data);

		$.ajax({
			type: 'POST' ,
			url : _ctx + "/ws/system/modifyUserRoleTypeAuthForMenu",
			dataType : 'json' ,
			data : data ,
			success : function(jsonData, textStatus, jqXHR) {
				toastr.success(_commonMsg.successRegister, _msg.authorityMgmt);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				parent.layerJs.fn_exception(xhRequest);
				toastr.error(_commonMsg.failRegister, _msg.authorityMgmt);
			}
		});
	}

	return {
		init : _init,
		searchUserRoleTypeMenuList : _searchUserRoleTypeMenuList,
		changeUserRoleTypeAuthForMenu : _changeUserRoleTypeAuthForMenu,
	};
}();
