/**
 * User Info
 */
var userInfoJs = function(){
    "use strict";
    
    let data = {};
    let cbFunc = undefined;
    
	function _init(paramCbFunc) {
		_searchMyUserInfo();
		cbFunc = paramCbFunc;
	};
	
	function _searchMyUserInfo(){
   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/system/myUserInfo",
   			dataType : 'json' ,
   			success : function(jsonData, textStatus, jqXHR) {
   				data = jsonData;
   				cbFunc(jsonData);
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   				parent.layerJs.fn_exception(xhRequest);
   			}
   		});
	};
	
	function _getUserInfo(){
		return data;
	}
	
	return {
		init : _init,
		getUserInfo : _getUserInfo
	};
}();
