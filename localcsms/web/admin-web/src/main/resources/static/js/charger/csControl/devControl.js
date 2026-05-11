/**
 * 충전관리 - 충전기 제어
 */
var chargingStationControlJs = function () {
	"use strict";

	var data = {
		searchCond: {}
	};

	function _init() {
		// console.log("......");
		if (queryString.cpCsId) {
			$("#sWord").val(queryString.cpCsId);
		}
		_initEvent();
	};

	function _initEvent() {
		//

		$("#btnSearch").click(function () {
			//
			_searchEnvInfoOnClick();
		});

		// 검색조건 Enter키로 검색기능
		$("#sWord").keypress(function (event) {
			if (event.keyCode == 13) {
				_searchEnvInfoOnClick();
			}
		});
		if (queryString.cpCsId) {
			_searchEnvInfoOnClick();
		}
	};

	function _searchEnvInfoOnClick() {
		//
		var sWord = $("#sWord").val().replace(/-/g, '').trim();
		if (!sWord || sWord == '') {
			toastr.warning(_commonMsg.searchInputReq, _msg.title);
			return;
		}
		if (sWord.length != 8) {
			toastr.warning(_msg.csIdValid8, _msg.title);
			return;
		}

		data.searchCond.cpId = sWord.substring(0, 6);
		data.searchCond.csId = sWord.substring(6);

		$("#sWord").val(sWord);

		_searchEnvInfo();
	}

	function _searchEnvInfo() {
		//
		$.ajax({
			type: 'GET',
			url: _ctx + "/ws/charger/" + data.searchCond.cpId + "/" + data.searchCond.csId + "/info",
			dataType: 'json',
			success: function (jsonData, textStatus, jqXHR) {
				if (jsonData) {
					let param = "?cpId=" + data.searchCond.cpId + "&csId=" + data.searchCond.csId;
					let type = jsonData.ocppVersion == "ocpp1.6" ? "OCPP16" : "OCPP20";
					self.location = _ctx + "/charger/chargingStation/" + type + "/devControl" + param;
				} else {
					toastr.warning(_msg.chargerNotFound, _msg.title);
				}
			},
			error: function (xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}

	return {
		init: _init
	};
}();
