/**
 * 이미지 뷰어
 */
var imageViewerPopupJs = function(){
    "use strict";
    
	function _init() {
		//
		$("#Popup_ImageViewer_ID").attr("src","");
	}
	
	function _show(url){
		//
		$("#Popup_ImageViewer_ID").attr("src", url);
	}
	
	return {
		init : _init,
		show : _show
	};
}();
