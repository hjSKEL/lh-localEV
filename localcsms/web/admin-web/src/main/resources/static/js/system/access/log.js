/**
 * 로그관리 - 접근기록
 */
let logJs = function(){
    'use strict';

	let searchCond = {}

   	function _init() {
		let fromDate = dateUtilsJs.formatDate(dateUtilsJs.addDay(new Date(), -7), 'YYYY-MM-DD');
		let toDate = dateUtilsJs.currentDate('YYYY-MM-DD');
		$('#fromDate').val(fromDate);
		$('#toDate').val(toDate);
   		_initEvent();
   	}

   	function _initEvent(){
   		//
		_searchOnClick();

		$('#fromDate').datepicker({
			todayBtn: "linked",
			autoClose: true,
			format: "yyyy-mm-dd"
		});
		$('#toDate').datepicker({
			todayBtn: "linked",
			autoClose: true,
			format: "yyyy-mm-dd"
		});

		$('#btnSearch').click(function () {
			_searchOnClick();
		});
   	}

   	function _searchOnClick(){
   		pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, logJs.search);

		searchCond.logId = '';
		searchCond.logUrl = '';
		searchCond.fromDate = $('#fromDate').val().replace(/-/g, '');
		searchCond.toDate = $('#toDate').val().replace(/-/g, '');

		let sType = $('#sType').val();
		switch(sType) {
			case 'A':
				searchCond.logId = $('#searchKey').val().trim();
				break;
			case 'B':
				searchCond.logUrl = encodeURI($('#searchKey').val().trim());
				break;
		}

   		_search();
   	}

   	function _search(){
   		//
   		$('#tBodyList').empty();
   		let html = '<tr style="text-align:center;">';
   		html += '<td colspan="6">' + _commonMsg.searching + '</td>';
   		$('#tBodyList').append(html);

   		let paging = pageInfoJs.getPaging();
   		let param = '?pageNumber=' +(paging.pageNumber - 1) + '&pageItemSize=' +paging.pageItemSize;

        param += '&logId=' + searchCond.logId;
		param += '&logUrl=' + searchCond.logUrl;
		param += '&fromDate=' + searchCond.fromDate;
		param += '&toDate=' + searchCond.toDate;

   		$.ajax({
   			type: 'GET' ,
   			url: _ctx + '/ws/system/access/log' + param,
   			dataType: 'json' ,
   			success: function(jsonData, textStatus, jqXHR) {
   				_display(jsonData);
   			},
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   				parent.layerJs.fn_exception(xhRequest);
   			}
   		});
   	}
   	function _display(jsonData){
   		pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

   		$('#totalCount').html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
   		$('#tBodyList').empty();
   		let html = '';
   		if(jsonData.criteria.totalItemCount == 0){
   			html = '<tr style="text-align:center;">';
   			html += '<td colspan="6">' + _commonMsg.noData + '</td>';
   			html += '</tr>';
   			$('#tBodyList').append(html);
   			return ;
   		}
		let result = jsonData.result;
   		let noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
   		for(let i=0, length = result.length ; i < length ; ++i){
   			html = '<tr style="text-align:center;">';
   			html += '<td>' + (noIndex + i) + '</td>';
   			html += '<td class="exCon">' + result[i].logId + '</td>';
			html += '<td class="exCon">' + result[i].logIp + '</td>';
			html += '<td class="exCon">' + result[i].logUrl + '</td>';
			let time = formmatUtilsJs.dateFormmat(result[i].logDate + result[i].logTime, 'YYYY-MM-DD HH:MM:SS')
			html += '<td class="exCon">' + time + '</td>';
			html += '<td class="exCon">' + result[i].logType + '</td>';
   			html += '</tr>';
   			$('#tBodyList').append(html);
   		}
   	}

   	return {
   		init: _init,
		search: _search,
   	};
}();
