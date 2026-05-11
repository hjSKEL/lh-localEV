/**
 * 메뉴 정보
 */
var systemMenuDetailJs = function(){
    "use strict";

    var data = {};

	function _init() {
		//
		_initEvent();
		systemMenuDetailJs.search(queryString.menuId);
	};

	function _initEvent(){
		//
		$("#btnList").click(function(){
			_listOnClick();
		});
		$("#btnUpdate").click(function(){
			_updateOnClick();
		});
		$("#btnDelete").click(function(){
			_deleteOnClick();
		});
	};

	function _search(menuId){
		//
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/system/menu/" + menuId,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayMenuInfo(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
			}
		});
	};

	function _displayMenuInfo(jsonData){
		//console.log(jsonData);
		if(!jsonData){
			return ;
		}
		data = jsonData;
		$("#menuId").val(jsonData.menuId);
		$("#highMenuId").val(jsonData.highMenuId);
		$("#menuName").val(jsonData.menuName);
		$("#menuNameEn").val(jsonData.menuNameEn);
		$("#menuUrl").val(jsonData.menuUrl);
		$("#ordPriority").val(jsonData.ordPriority);
		$("#menuDesc").html(jsonData.menuDesc);
		$("#linkType").val(jsonData.linkType);
	};

	function _listOnClick(){
		self.location= _ctx + "/system/menu/menuList";
	};

	function _updateOnClick(){
		//
		if(!data){
			alert(_msg.dataError);
			return ;
		}
		data.menuId = $("#menuId").val();
		data.highMenuId = $("#highMenuId").val();
		data.menuName = $("#menuName").val();
		data.menuNameEn = $("#menuNameEn").val();
		data.menuUrl = $("#menuUrl").val();
		data.ordPriority = $("#ordPriority").val();
		data.menuDesc = $("#menuDesc").val();
		data.linkType = $("#linkType").val();

		data.menuDesc = data.menuDesc.trim();

		$.ajax({
			type : 'PUT' ,
			method : 'PUT',
			url : _ctx + "/ws/system/menu/" + menuId,
			contentType:"application/json",
			dataType : 'json' ,
			data : JSON.stringify(data),
			success : function(jsonData) {
				if(jsonData.status == 'SUCCESS'){
					alert(_commonMsg.successModify);
					_searchDetail(menuId);
				}else{
					alert(_commonMsg.failModify);
				}
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
				alert(_commonMsg.failModify);
			}
		});
	};

	function _deleteOnClick(){
		$.ajax({
			type : 'DELETE' ,
			method : 'DELETE',
			url : _ctx + "/ws/system/menu/" + menuId,
			contentType:"application/json",
			dataType : 'json' ,
			success : function(jsonData) {
				//console.log(jsonData);
				if(jsonData.status == 'SUCCESS'){
					alert(_commonMsg.successDelete);
					_listOnClick();
				}else{
					alert(_commonMsg.failDelete);
				}
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
				alert(_commonMsg.failDelete);
			}
		});
	};

    function _searchDetail(menuId){
        //
        self.location= _ctx + "/system/menu/detail?menuId=" + menuId;
    };

	return {
		init : _init,
		search : _search
	};
}();
