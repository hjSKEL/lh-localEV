/**
 * 쿠폰발급
 */
let couponPopupJs = function(){
    'use strict';

	let cbFunc = undefined;
    let data = {};
	let searchCond = {};

	function _init() {
		$('#popup_coupon_expireDate').datepicker({
			todayBtn: "linked",
			autoClose: true,
			format: "yyyy-mm-dd"
		});

		$('#popup_coupon_btnRegister').click(function() {
			if(_validate()) {
				_registerCoupon();
			}
		});

		$('#popup_coupon_btnSearch').click(function() {
			_searchOnClick();
		});
	}

	function _show(callbackFunc) {
		cbFunc = callbackFunc;
		data = {};
		let date = dateUtilsJs.addMonth(new Date(), 12);
		date = dateUtilsJs.addDay(date, -1);
		$('#popup_coupon_expireDate').val(dateUtilsJs.formatDate(date, 'YYYY-MM-DD'));
		$('#popup_coupon_issue').val('');
		$('#popup_coupon_issueId').val('');
		$('#popup_coupon_etc').val('').attr('readonly', false);
		$('#popup_coupon_duplicate').val('N').attr('disabled', false);
		$('#popup_coupon_ea').val(1);
		_searchOnClick();
	}

	function _validate() {
		let issue = $('#popup_coupon_issue').val();
		if(!issue) {
			toastr.warning(_msg.enterPoint, _msg.couponIssue);
			return false;
		}
		data.issue = issue;

		let ea = $('#popup_coupon_ea').val();
		if(!ea) {
			toastr.warning(_msg.enterEa, _msg.couponIssue);
			return false;
		}
		data.ea = $('#popup_coupon_ea').val();

		let etc = $('#popup_coupon_etc').val().trim();
		if(!etc) {
			toastr.warning(_msg.enterReason, _msg.couponIssue);
			return false;
		}
		data.etc = etc;

		let curDate = dateUtilsJs.currentDate('YYYY-MM-DD').replace(/-/g, '');
		let expireDate = $('#popup_coupon_expireDate').val().replace(/-/g, '');

		if(expireDate < curDate) {
			toastr.warning(_msg.pastDate, _msg.couponIssue);
			return false;
		}
		data.expireDate = $('#popup_coupon_expireDate').val();

		data.duplicate = $('#popup_coupon_duplicate').val();

		let issueId = $('#popup_coupon_issueId').val();
		if(issueId) {
			data.issueId = issueId;
		}
		return true;
	}

	function _registerCoupon() {
		swal({
			title: _msg.couponIssue,
			text: _msg.confirmIssue,
			type: 'info',
			showCancelButton: true,
			confirmButtonColor: '#1C84C6',
			confirmButtonText: _msg.issueBtn,
			cancelButtonText: _msg.cancel,
			closeOnConfirm: true
		}, function () {
			toastr.info(_msg.issuingWait, _msg.couponIssue);
			$.ajax({
				type: 'POST',
				method: 'POST',
				url: _ctx + '/ws/membership/coupon?ea=' + data.ea,
				contentType: 'application/json',
				dataType: 'json',
				data: JSON.stringify(data),
				success: function (jsonData) {
					toastr.success(_msg.issueResult.replace('{0}', jsonData.result.success).replace('{1}', jsonData.result.fail), _msg.couponIssue);
					cbFunc(data.etc);
					$('#Popup_Coupon').modal('toggle');
				},
				error: function (xhRequest, ErrorText, thrownError) {
					//
					toastr.error(_commonMsg.failGeneric, _msg.couponIssue);
				}
			});
		});
	}

	function _searchOnClick() {
		Popup_Coupon_pageInfoJs.init('Popup_Coupon_pageInfoJs', 'popup_coupon_pagingUl', 10, 5, couponPopupJs.search);

		searchCond.issueId = encodeURI($('#popup_coupon_sWord').val().trim());
		_search();
	}

	function _search() {
		$('#popup_coupon_tBodyList').empty();
		let html = '<tr style="text-align:center;">';
		html += '<td colspan="4">' + _commonMsg.searching + '</td>';
		$('#popup_coupon_tBodyList').append(html);

		let paging = Popup_Coupon_pageInfoJs.getPaging();
		let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
		param += '&issueId=' + searchCond.issueId;

		$.ajax({
			type: 'GET',
			url: _ctx + '/ws/membership/coupon/search' + param,
			dataType: 'json',
			success: function (jsonData, textStatus, jqXHR) {
				_display(jsonData);
			},
			error: function (xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}

	let result;
	function _display(jsonData) {
		Popup_Coupon_pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

		result = jsonData.result;
		$('#popup_coupon_tBodyList').empty();
		let html = '';
		if (jsonData.criteria.totalItemCount === 0) {
			html = '<tr style="text-align:center;">'
			html += '<td colspan="4">' + _commonMsg.noData + '</td>';
			html += '</tr>';
			$('#popup_coupon_tBodyList').append(html);
			return;
		}

		let noIndex = (Popup_Coupon_pageInfoJs.getPaging().pageNumber - 1) * Popup_Coupon_pageInfoJs.getPaging().pageItemSize + 1;
		for (let i = 0, length = result.length; i < length; ++i) {
			html = '<tr  style="text-align:center;" onclick="couponPopupJs.click(' + i + ')">';
			html += '<td>' + (i + noIndex) + '</td>';
			html += '<td>' + result[i].issueId + '</td>';
			html += '<td>' + result[i].etc + '</td>';
			html += '<td>' + (result[i].duplicate === 'Y' ? _commonMsg.possible : _commonMsg.impossible) + '</td>';
			html += '</tr>';
			$('#popup_coupon_tBodyList').append(html);
		}
	}

	function _click(index) {
		$('#popup_coupon_issueId').val(result[index].issueId);
		$('#popup_coupon_etc').val(result[index].etc).attr('readonly', true);
		$('#popup_coupon_duplicate').val(result[index].duplicate).attr('disabled', true);
		$('#popup_coupon_list').toggle();
	}

	function _toggle() {
		$('#popup_coupon_list').toggle();
	}
	
	return {
		init: _init,
		show: _show,
		search: _search,
		click: _click,
		toggle: _toggle,
	};
}();
