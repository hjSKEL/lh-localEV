var webSocketJs = function(){
    "use strict";
    
    var webSocket;
    var data = {
    		employeeId : "",
    		ip : "",
    		port : "",
    		messageListners : []
    			
    };
	
	function _init(emplId, chatIp, chatPort){
		data.employeeId = emplId;
		data.ip = chatIp;
		data.port = chatPort;
		// webSocket = new WebSocket("ws://" + data.ip + ":" + data.port + "/chat-websocket/endpoint/" + data.employeeId , ["KEVIT_CHAT1.0"]);
		// webSocket.onopen = function(message){ _wsOpen(message);};
		// webSocket.onmessage = function(message){ _wsGetMessage(message);};
		// webSocket.onclose = function(message){ _wsClose(message);};
		// webSocket.onerror = function(message){ _wsError(message);};
	};
	
	function _wsOpen(msg){
		//console.log(msg);
	    //console.log("Connected ... ");
	}
	
	function _wsSendMessage(msg){
		//console.log(msg);
	    webSocket.send(msg);
	}
	
	function _wsCloseConnection(){
	    webSocket.close();
	}
	
	function _wsGetMessage(msg){
		//console.log(msg);
		for(let i = 0, length = data.messageListners.length; i < length ; ++i){
			data.messageListners[i](msg.data);
		}
	}
	
	function _wsClose(msg){
		setTimeout(function(){
			webSocketJs.init(userInfoJs.getUserInfo().userId, _chatIp, _chatPort);
		}, 5 * 1000)
	}
	
	function _wsError(msg){
		//console.log(msg);
	}
	
	function _addMessageListener(func){
		//
		data.messageListners.push(func);
	};
	
	return {
		init : _init,
		addMessageListener : _addMessageListener,
		wsSendMessage : _wsSendMessage,
		wsCloseConnection : _wsCloseConnection
	};
}();