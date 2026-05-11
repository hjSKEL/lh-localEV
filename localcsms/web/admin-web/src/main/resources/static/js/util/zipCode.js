 /**
  * 
  */
var zipCodeJs = function(){
	//
	function _init(){
		//
	};
	
	function _searchZipCode(param, callBackFunc){
		//
		if(!param){
			param = "";
		}
		$.ajax({ 
			type: 'GET' ,
			url : _ctx + "/ws/zip/codes?zipCodeId=" + param,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				callBackFunc(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	};
	return {
		init : _init,
		searchZipCode : _searchZipCode
	}
}();
