 /**
  * 
  */
var carModelJs = function(){
	//
	var data = {
			dataMap : []
	};
	
	function _init(callBackFun){
		//
		data.callBackFun = callBackFun;
		if(data.dataMap && data.dataMap.length > 0){
			data.callBackFun();
		}else{
			data.dataMap = [];
			_search();
		}
	};
	
	function _search(){
		//
		$.ajax({ 
			type: 'GET' ,
			url : _ctx + "/ws/carModel/search",
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				//console.log(jsonData);
				data.dataMap = jsonData;
				if(data.callBackFun){
					data.callBackFun();
				}
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	};
	
	function _getCarModel(){
		return data.dataMap;
	}
	
	function _log(){
		console.log(data);
	};
	return {
		init : _init,
		getCarModel : _getCarModel,
		log : _log
	}
}();
