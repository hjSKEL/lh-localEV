 /**
  * 
  */
var commonValJs = function(){
	//
	var data = {
			dataMap : [],
			callBackFun : undefined
	};
	
	function _init(pCodes, callBackFun){
		data.dataMap = [];
		data.callBackFun = callBackFun;
		if(pCodes){
			_asyncSearchCodesByParentCodes(pCodes);
		}
	};
	
	function _asyncSearchCodesByParentCodes(pCodes){
		//
		var params = "";
		for(var i=0, size = pCodes.length ; i < size ; ++i){
			params += pCodes[i] + ",";
		}
		if(params.length > 0){
			params = params.substring(0,params.length - 1);
		}
		$.ajax({ 
			type: 'GET' ,
			url : _ctx + "/ws/common/val?highCodes=" + params,
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
	
	function _getCodesByParentCode(pCode, callBackFunc){
		//
		var subCodeVals = undefined;
		
		for(var i=0,length = data.dataMap.length ; i < length; ++i){
			if(pCode == data.dataMap[i].code){
				subCodeVals =  data.dataMap[i].codeVals;
				break;
			}
		}
		if(subCodeVals){
			if(callBackFunc){
				return callBackFunc(subCodeVals);
			}
			return subCodeVals;
		}
		return [];
	};
	function _getCodeNameBySubCode(subCode){
		//
		if(!subCode) return "";
		
		for(var i=0,ilength = data.dataMap.length ; i < ilength; ++i){
			for(var j=0,jlength = data.dataMap[i].codeVals.length ; j < jlength; ++j){
				if(subCode == data.dataMap[i].codeVals[j].code){
					return data.dataMap[i].codeVals[j].codeName;
				}
			}
		}
		return subCode;
	};
	
	function _getCodeValueBySubCode(subCode){
		//
		if(!subCode) return "";
		
		for(var i=0,ilength = data.dataMap.length ; i < ilength; ++i){
			for(var j=0,jlength = data.dataMap[i].codeVals.length ; j < jlength; ++j){
				if(subCode == data.dataMap[i].codeVals[j].code){
					return data.dataMap[i].codeVals[j].codeValue;
				}
			}
		}
		return subCode;
	};
	
	function _log(){
		console.log(data);
	};
	return {
		init : _init,
		getCodesByParentCode : _getCodesByParentCode,
		getCodeNameBySubCode : _getCodeNameBySubCode,
		getCodeValueBySubCode : _getCodeValueBySubCode,
		log : _log
	}
}();
