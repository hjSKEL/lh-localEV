 /**
  * 
  */
var commonCodeJs = function(){
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
			url : _ctx + "/ws/common/codes?highCodes=" + params,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				//console.log(jsonData);
				data.dataMap = jsonData;
				_applyLocale();
				if(data.callBackFun){
					data.callBackFun();
				}
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	};

	function _applyLocale(){
		if(typeof _locale === 'undefined' || _locale === 'ko') return;
		for(var i = 0; i < data.dataMap.length; i++){
			var codes = data.dataMap[i].codes;
			if(!codes) continue;
			for(var j = 0; j < codes.length; j++){
				if(codes[j].codeNameEn){
					codes[j].codeName = codes[j].codeNameEn;
				}
			}
		}
	};

		function _syncSearchCodesByParentCodes(pCodes){
    		let params = "";
    		for(var i=0, size = pCodes.length ; i < size ; ++i){
    			params += pCodes[i] + ",";
    		}
    		if(params.length > 0){
    			params = params.substring(0,params.length - 1);
    		}
    		let result = undefined;
    		$.ajax({
    			type: 'GET' ,
    			url : _ctx + "/ws/common/codes?highCodes=" + params,
    			dataType : 'json' ,
    			async : false,
    			success : function(jsonData, textStatus, jqXHR) {
    				result = jsonData;
    			} ,
    			error : function(xhRequest, ErrorText, thrownError) {
    				//
    			}
    		});
    		for(let j = 0, jLength = result.length ; j < jLength; j ++){
    			data.dataMap.push(result[j]);
    		}
    		_applyLocale();
    	}

	function _getCodesByParentCode(pCode, callBackFunc){
		//
		var subCodes = undefined;
		
		for(var i=0,length = data.dataMap.length ; i < length; ++i){
			if(pCode == data.dataMap[i].code){
				subCodes =  data.dataMap[i].codes;
				break;
			}
		}
		if(subCodes){
			if(callBackFunc){
				return callBackFunc(subCodes);
			}
			return subCodes;
		}else{
			var codes = _syncSearchCodesByParentCodes(pCode);
			if(callBackFunc){
				return callBackFunc(codes);
			}
			return codes;
		}
	};
	function _getCodeNameBySubCode(subCode){
		//
		if(!subCode) return "";
		for(var i=0,ilength = data.dataMap.length ; i < ilength; ++i){
			for(var j=0,jlength = data.dataMap[i].codes.length ; j < jlength; ++j){
				if(subCode == data.dataMap[i].codes[j].code){
					return data.dataMap[i].codes[j].codeName;
				}
			}
		}
		return subCode;
	};
	
	function _getRfIcCardStatusDesc(rfIcCardStatus){
		//
		switch(rfIcCardStatus){
		case '1':
			return _commonMsg.rfAuth;
		case '2':
			return _commonMsg.rfAuthOk;
		case '3':
			return _commonMsg.rfAuthErr;
		default: // 0
			return _commonMsg.rfReadWait;
		}
	};

	function _getBtnStatusDesc(buttonStatus){
		//
		switch(buttonStatus){
		case '1':
			return _commonMsg.btnStart;
		case '2':
			return _commonMsg.btnStop;
		default: // 0
			return _commonMsg.btnNone;
		}
	};

	function _getChargerCableStatusDesc(chargerCableStatus){
		return chargerCableStatus == '0' ? _commonMsg.cableDisconnect : _commonMsg.cableConnect;
	};
	
	function _getCodeNameByPCodeNSubCode(pCode, subCode){
		//
		if(!pCode) return "";
		if(!subCode) return "";
		
		for(var i=0,ilength = data.dataMap.length ; i < ilength; ++i){
			if(data.dataMap[i].code == pCode){
				for(var j=0,jlength = data.dataMap[i].codes.length ; j < jlength; ++j){
					if(subCode == data.dataMap[i].codes[j].code){
						return data.dataMap[i].codes[j].codeName;
					}
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
		getRfIcCardStatusDesc : _getRfIcCardStatusDesc,
		getBtnStatusDesc : _getBtnStatusDesc,
		getChargerCableStatusDesc : _getChargerCableStatusDesc, 
		getCodeNameByPCodeNSubCode : _getCodeNameByPCodeNSubCode, 
		log : _log
	}
}();
