var ocpp20CommandJs = function(){
	//	
	function _Reset(params){
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = {type:params[0]};//Immediate, OnIdle
		if(params[1] && params[1] != ""){
			obj.evseId = params[1]			
		}
		return JSON.stringify(obj);
	}
	
	function _RequestStartTransaction(params){
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = {remoteStartId : params[1], idToken : {idToken : params[2],type:"ISO15693"}};
		if(params[0] && params[0] != ''){
			obj.evseId = params[0];
		}
		
		if(params[3] && params[3] != ''){
			obj.groupIdToken = {idToken : params[3] ,type:"ISO15693"};
		}
		
		return JSON.stringify(obj);
	}
	
	function _RequestStopTransaction(params){
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = {transactionId:params[0]};
		return JSON.stringify(obj);
	}

	function _makeParam(type, params){
		switch(type){
		case 'Reset':
			return _Reset(params);
		case 'RequestStartTransaction':
			return _RequestStartTransaction(params);
		case 'RequestStopTransaction' :
			return _RequestStopTransaction(params);
		}
	}
	
	function _type(){
		var types = [
    		{name: '충전기재시작', value : 'Reset'},
    		{name: '원격충전시작', value : 'RequestStartTransaction'},
    		{name: '원격충전종료', value : 'RequestStopTransaction'}
			];
		return types;
	}
	
	return {
		makeParam : _makeParam,
		type : _type
	};
}();
