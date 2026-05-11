/**
 * 메뉴 등록
 */
var systemMenuRegJs = function(){
    "use strict";

    var data = {};

	function _init() {
		_initEvent();
	};

	function _initEvent(){
		//
		$("#btnRegister").click(function(){
			//
			_registerOnClick();
		});

		$("#btnList").click(function(){
			//
			_listOnClick();
		});
	};

	function _registerOnClick(){
		//
		if(!_validate()){
			return ;
		}

		$.ajax({
			type : 'POST' ,
			method : 'POST',
			url : _ctx + "/ws/system/menu",
			contentType:"application/json",
			dataType : 'json' ,
			data : JSON.stringify(data),
			success : function(jsonData) {
				if(jsonData.status == 'SUCCESS'){
					alert(_commonMsg.successRegister);
					_searchDetail(jsonData.result.menuId);
				}else{
					alert(_commonMsg.failRegister);
				}
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
				alert(_commonMsg.failRegister);
			}
		});
	};

	function _validate(){
		//
		data.menuId = $("#menuId").val();
		data.highMenuId = $("#highMenuId").val();
		data.menuName = $("#menuName").val();
		data.menuNameEn = $("#menuNameEn").val();
		data.menuUrl = $("#menuUrl").val();
		data.ordPriority = $("#ordPriority").val();
		data.menuDesc = $("#menuDesc").val();
		data.linkType = $("#linkType").val();

		data.menuDesc = data.menuDesc.trim();

		//메뉴코드
		if(validateJs.isEmpty(data.parentMenuId)){
			alert(_msg.enterMenuCode);
			return false;
		}
		//메뉴명
		if(validateJs.isEmpty(data.menuName)){
			alert(_msg.enterMenuName);
			return false;
		}
		//URL
		if(validateJs.isEmpty(data.menuUrl)){
			alert(_msg.enterUrl);
			return false;
		}


		return true;
	};

	function _listOnClick(){
		self.location= _ctx + "/system/menu/menuList";
	};

    function _searchDetail(menuId){
        //
        self.location= _ctx + "/system/menu/detail?menuId=" + menuId;
    };

	return {
		init : _init
	};
}();
