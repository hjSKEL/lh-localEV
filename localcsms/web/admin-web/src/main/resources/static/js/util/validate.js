 /**
  * 
  */
var validateJs = function(){
	//
	function _isEmpty(value){
		return !value || value.trim().length == 0;
	};
	
	return {
		isEmpty : _isEmpty
	};
}();
