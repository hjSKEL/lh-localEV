/**
 * 충전관리 - 충전기 제어
 */
var ocpp20DevControlJs = function () {
	"use strict";

	var data = {
		searchCond: {},
		searchCondRecharging: {}
	};

	function _init() {
		// console.log("......");
		_initData();
		_initEvent();
	};

	function _initData() {
		//
		if (queryString.cpId && queryString.csId) {
			$("#sWord").val(queryString.cpId + '-' + queryString.csId);
		}
		if (queryString.csUniqId) {
			$("#sWord").val(queryString.csUniqId);
		}
		if (queryString.searchType) {
			$("#searchType").val(queryString.searchType);
		}

		$("#GetDiagnosticsValue4").val(moment().format('YYYY-MM-DDTHH:mm:ss') + 'Z');
		$("#GetDiagnosticsValue5").val(moment().format('YYYY-MM-DDTHH:mm:ss') + 'Z');
		$("#ReserveNowValue2").val(moment().format('YYYY-MM-DDTHH:mm:ss') + 'Z');

		let allVariableKeyList = ocpp20VarJs.getAllVariableKey();
		let html = "";
		for (var i = 0, size = allVariableKeyList.length; i < size; ++i) {
			html += '<tr>';
			html += '<td><input name="GetVariablesCheckBox" type="checkbox"';
			html += ' meta-value="' + allVariableKeyList[i] + '"></td>';
			html += '<td>' + allVariableKeyList[i] + '</td>';
			html += '<td></td>';
			html += '</tr>';
		}
		$("#GetVariablesTBody").append(html);
		$("#GetVariables").css({ display: 'none' });
	};

	function _initEvent() {
		//
		$("#date1").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
		$("#date2").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
		$("#date3").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
		$("#date4").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
		$('#date1').datepicker({
			todayBtn: "linked",
			autoClose: true,
			format: "yyyy-mm-dd"
		});
		$('#date2').datepicker({
			todayBtn: "linked",
			autoClose: true,
			format: "yyyy-mm-dd"
		});
		$('#date3').datepicker({
			todayBtn: "linked",
			autoClose: true,
			format: "yyyy-mm-dd"
		});
		$('#date4').datepicker({
			todayBtn: "linked",
			autoClose: true,
			format: "yyyy-mm-dd"
		});

		$("#btnSearch").click(function () {
			//
			_searchEnvInfoOnClick();
		});

		$("#btnCustRechargingSearch").click(function () {
			_searchCustRechargingOnClick();
		});

		$("#btnOCPPLogSearch").click(function () {
			//
			_searchOcppLogClick();
		});


		// 검색조건 Enter키로 검색기능
		$("#sWord").keypress(function () {
			if (event.keyCode == 13) {
				_searchEnvInfoOnClick();
			}
		});
		if ((queryString.cpId && queryString.csId) || queryString.csUniqId) {
			_searchEnvInfoOnClick();
		}

		//제조사
		var makerTypes = parent.commonCodeJs.getCodesByParentCode("CHMK00");
		$("#makerType").append('<option value="">' + _msg.selectPlaceholder + '</option>');
		for (var i = 0; i < makerTypes.length; i++) {
			$("#makerType").append('<option value="' + makerTypes[i].code + '">' + makerTypes[i].codeName + '</option>');
		}

		var html = '';
		var ocpp20Commands = ocpp20CommandDevJs.type();
		for (var i = 0, size = ocpp20Commands.length; i < size; ++i) {
			html = '<option value="' + ocpp20Commands[i] + '">' + ocpp20Commands[i] + '</option>';
			$("#ocppCommandType").append(html);
		}

		$("#btnCommand").click(function () {
			//
			_sendCommandOnClick();
		});

		$("#ocppCommandType").change(function () {
			//
			_changeOcppCommand(this);
		});

		$("#btnAddChargingSchedulePeriod").click(function () {
			//
			_addChargingSchedulePeriod();
		});

		$("#btnRTAddChargingSchedulePeriod").click(function () {
			//
			_addRTChargingSchedulePeriod();
		});

		$("#btnAddSetVariableMonitoring").click(function () {
			//
			_addSetVariableMonitoring();
		});

		$("#hiddenAdd").click(function () {
			$("#hidden").show();
			$("#hidden2").show();
			$("#hidden3").show();
			$("#hidden4").show();
			$("#hiddenSub").show();
			$("#hiddenAdd").hide();
		});

		$("#hiddenSub").click(function () {
			$("#hidden").hide();
			$("#hidden2").hide();
			$("#hidden3").hide();
			$("#hidden4").hide();
			$("#hiddenSub").hide();
			$("#hiddenAdd").show();
		});

		$("#btnAddGetMonitoringReport").click(function () {
			_addGetMonitoringReport();
		});
	};

	function _addGetMonitoringReport() {
		//
		let html = "<tr>";
		html += '<td><input type="text"   name="cname"   placeholder="ex)EVSE"   class="form-control input-sm" /></td>';
		html += '<td><input type="number" name="evseId"  placeholder="ex)1"   class="form-control input-sm" /></td>';
		html += '<td><input type="text"   name="vname"   placeholder="ex)Avaiable" class="form-control input-sm" /></td>';
		html += '<td><button onclick="ocpp20DevControlJs.removeGetMonitoringReport(this);">삭제</button></td>';
		html += "</tr>";
		$("#GetMonitoringReportValue3").append(html);
	};

	function _removeGetMonitoringReport(target) {
		//
		target.parentElement.parentElement.remove();
	};

	function _searchEnvInfoOnClick() {
		var sWord = $("#sWord").val().replace(/-/g, '').trim();
		if (!sWord || sWord === '') {
			toastr.warning(_commonMsg.searchInputReq, _msg.title);
			return;
		}
		data.searchCond.csId = sWord;
		if (data.searchCond.csId.length !== 8) {
			$("#csUniqIdtr").html("");
			toastr.warning(_msg.csIdLength8, _msg.csIdLabel);
			return;
		}
		data.searchCond.cpId = data.searchCond.csId.substring(0, 6);
		data.searchCond.csId = data.searchCond.csId.substring(6);
		$("#sWord").val(sWord);
		_searchEnvInfo();
	};

	function _findCpCsId(csUniqId) {
		$.ajax({
			type: 'GET',
			url: _ctx + "/ws/charger/chargingStation/csUniqId/" + csUniqId,
			dataType: 'json',
			success: function (jsonData, textStatus, jqXHR) {
				data.searchCond.cpId = jsonData.cpId;
				data.searchCond.csId = jsonData.csId;
				data.searchCond.cpCsId = jsonData.cpId + "-" + jsonData.csId;
				_searchEnvInfo();
				_searchCustRecharging();
			},
			error: function (xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}

	function _findCsUniqId(cpId, csId) {
		$.ajax({
			type: 'GET',
			url: _ctx + "/ws/charger/chargingStation/" + cpId + "/" + csId,
			dataType: 'json',
			success: function (jsonData, textStatus, jqXHR) {
				data.searchCondRecharging.csUniqId = jsonData.csUniqId;
				_searchEnvInfo();
				_searchCustRecharging();
			},
			error: function (xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}

	function _searchEnvInfo() {
		//
		$.ajax({
			type: 'GET',
			url: _ctx + "/ws/charger/chargingStation/" + data.searchCond.cpId + "/" + data.searchCond.csId,
			dataType: 'json',
			success: function (jsonData, textStatus, jqXHR) {
				_displayEnvInfo(jsonData);
			},
			error: function (xhRequest, ErrorText, thrownError) {
				//
			}
		});
	};

	function _displayEnvInfo(jsonData) {
		if (!jsonData) return;
		$("#csUniqIdtr").html(jsonData.csUniqId + "(" + jsonData.cpId + "-" + jsonData.csId + ")");
		$("#makerType").val(jsonData.makerType);
		$("#fmwVer").val(jsonData.fwVer);
		$("#cpNametr").html(jsonData.cpName);
		$("#protocolType").val(jsonData.ocppVersion);
	}

	function _searchOcppLogClick() {
		//console.log("_searchChargerStatusClick start....");
		ocppPageInfoJs.init('ocppPageInfoJs', 'OCPPPagingUl', 10, 20, ocpp20DevControlJs.searchOcppLog);

		data.searchCond.cpId = "";
		data.searchCond.csId = "";
		data.searchCond.telecomNo = "";
		data.searchCond.fromDate = "";
		data.searchCond.toDate = "";

		if (queryString.cpCsId) {
			$("#sWord").val(queryString.cpCsId);
		}
		var searchType = $("#searchType").val();
		var sWord = $("#sWord").val();
		if (!sWord || sWord == '') {
			toastr.warning("검색조건을 입력하세요.", "충전이력");
			return;
		}
		switch (searchType) {
			case 'CHAID':
				if (!sWord.includes('-')) {
					toastr.warning("충전기 ID는 xxxxxxxxxxxxx-xx 형식입니다.", "충전이력");
					return;
				}
				var ids = sWord.split("-");
				data.searchCond.cpId = ids[0];
				data.searchCond.csId = ids[1];
				break;
			default:
		}
		var date1 = $("#date1").val();
		var date2 = $("#date2").val();
		data.searchCond.fromDate = formmatUtilsJs.removeDash(date1) + "000000";
		data.searchCond.toDate = formmatUtilsJs.removeDash(date2) + "235959";

		_searchOcppLog();
	};

	function _searchOcppLog() {
		//
		$("#OCPPtBodyList").empty();
		var html = '<tr style="text-align:center;">';
		html += '<td colspan="10">검색 중입니다.</td>';
		$("#OCPPtBodyList").append(html);

		var paging = ocppPageInfoJs.getPaging();
		var param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;

		param += "&cpId=" + data.searchCond.cpId;
		param += "&csId=" + data.searchCond.csId;
		param += "&telecomNo=" + data.searchCond.telecomNo;
		param += "&fromDate=" + data.searchCond.fromDate;
		param += "&toDate=" + data.searchCond.toDate;

		$.ajax({
			type: 'GET',
			url: _ctx + "/ws/ocpp/log/search" + param,
			dataType: 'json',
			success: function (jsonData, textStatus, jqXHR) {
				_displayOcppLog(jsonData);
			},
			error: function (xhRequest, ErrorText, thrownError) {
				//
			}
		});
	};

	function _displayOcppLog(jsonData) {
		//console.log(jsonData);
		ocppPageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

		$("#OCPPtBodyList").empty();
		var html = '';
		if (jsonData.criteria.totalItemCount == 0) {
			html = '<tr style="text-align:center;">';
			html += '<td colspan="10">검색된 데이터가 없습니다.</td>';
			html += '</tr>';
			$("#OCPPtBodyList").append(html);
			return;
		}
		var result = jsonData.result;
		var noIndex = (ocppPageInfoJs.getPaging().pageNumber - 1) * ocppPageInfoJs.getPaging().pageItemSize + 1;
		for (var i = 0, length = result.length; i < length; ++i) {
			html = '<tr style="text-align:center;">';
			html += '<td>' + (i + noIndex) + '</td>';
			html += '<td class="exCon">' + result[i].id + '</td>';
			html += '<td class="exCon">' + result[i].cpId + '</td>';
			html += '<td class="exCon">' + result[i].csId + '</td>';
			html += '<td class="exCon">' + result[i].directType + '</td>';
			html += '<td class="exCon">' + result[i].messageTypeId + '</td>';
			html += '<td class="exCon">' + result[i].ocppVer + '</td>';
			html += '<td class="exCon">' + result[i].action + '</td>';
			html += '<td class="exCon">' + result[i].payloadJson + '</td>';
			let regDt = new Date(result[i].regDate);
			html += '<td class="exCon">' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(regDt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
			html += '</tr>';
			$("#OCPPtBodyList").append(html);
		}
	}

	function _changeOcppCommand(target) {
		//
		$('div[name=OCPPCommand]').hide();
		var selectedValue = target.value;
		$("#" + selectedValue).show();

		if (selectedValue == 'SendLocalList') {
			_getLocalList();
		}
	}

	function _getLocalList() {
		//
		let cpCsId = data.searchCond.cpCsId;
		if (!cpCsId || cpCsId == "") {
			return;
		}

		let cpCs = cpCsId.split("-");

		$.ajax({
			type: 'GET',
			url: _ctx + "/ws/charger/cpCustomer/" + cpCs[0],
			dataType: 'json',
			success: function (jsonData, textStatus, jqXHR) {
				let html = '';
				if (jsonData && jsonData.length > 0) {
					for (let i = 0, length = jsonData.length; i < length; ++i) {
						html += '<option value="' + jsonData[i].cutCardNo + '">' + jsonData[i].cutCardNo + '</option>';
					}
				}
				$("#SendLocalListValue3").html(html);
			},
			error: function (xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}

	function _sendCommandOnClick() {
		//
		var ocppCommandType = $("#ocppCommandType").val();
		var params = [];
		switch (ocppCommandType) {
			case 'RequestStopTransaction'://2.0
			case 'CancelReservation'://2.0
			case 'GetInstalledCertificateIds'://2.0
			case 'UnpublishFirmware'://2.0
				params[0] = $("#" + ocppCommandType + "Value").val();
				break;
			case 'UnlockConnector'://2.0
			case 'Reset'://2.0
			case 'InstallCertificate'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				break;
			case 'CertificateSigned'://2.0 + 2.1 requestId
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				break;
			case 'ChangeAvailability'://2.0
			case 'DataTransfer'://2.0
			case 'GetCompositeSchedule'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				break;
			case 'TriggerMessage'://2.0 + 2.1 customTrigger
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				break;
			case 'GetDERControl'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				break;
			case 'SetDERControl'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				break;
			case 'ClearDERControl'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				break;
			case 'ClearChargingProfile'://2.0
			case 'DeleteCertificate'://2.0
			case 'GetDisplayMessages'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				break;
			case 'GetDiagnostics'://2.0
			case 'PublishFirmware'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				params[4] = $("#" + ocppCommandType + "Value5").val();
				break;
			case 'ReserveNow'://2.0 + 2.1 idToken.additionalInfo
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				params[4] = $("#" + ocppCommandType + "Value5").val();
				params[5] = $("#" + ocppCommandType + "Value6").val();
				params[6] = $("#" + ocppCommandType + "Value7").val();
				break;
			case 'GetVariables'://2.0
				$("input:checkbox[name='GetVariablesCheckBox']").each(function () {
					if (this.checked) {
						let varInstance = ocpp20VarJs.getVariable(this.getAttribute('meta-value'));
						let param = {
							variable: { name: varInstance.variableName },
							component: { name: varInstance.componentName }
						}
						if (varInstance.variableInstance) {
							param.variable.instance = varInstance.variableInstance;
						}
						params.push(param);
					}
				});
				break;
			case 'SetVariables'://2.0
				let trList = $("#SetVariablesTbody tr");
				for (let i = 0, size = trList.length; i < size; ++i) {
					let keyTd = trList[i].children[0];
					let valueTd = trList[i].children[1];
					let key = $(keyTd).find("select")[0].value;
					let varInstance = ocpp20VarJs.getVariable(key);
					let temp = {
						variable: { name: varInstance.variableName },
						component: { name: varInstance.componentName }
					};
					let inputEle = $(valueTd).find("input");
					if (inputEle.length == 1) {
						temp.attributeValue = inputEle[0].value;
					}
					if (inputEle.length == 2) {
						temp.component.evse = {};
						if (inputEle[0].getAttribute("meter-value") == "value") {
							temp.attributeValue = inputEle[0].value;
							temp.component.evse.id = inputEle[1].value;
						} else {
							temp.component.evse.id = inputEle[0].value;
							temp.attributeValue = inputEle[1].value;
						}
					}
					params.push(temp);
				}

				break;
			case 'SendLocalList'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = [];
				let temp = $("#" + ocppCommandType + 'Value3').val();
				if (temp && temp.length > 0) {
					params[2] = temp;
				}
				params[3] = $("#" + ocppCommandType + "Value4").val();
				break;
			case 'RequestStartTransaction'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				//ChargingProfile
				params[4] = $("#" + ocppCommandType + "Value5").val();//id
				params[5] = $("#" + ocppCommandType + "Value6").val();//stackLevel
				params[6] = $("#" + ocppCommandType + "Value7").val();//chargingProfilePurpose
				params[7] = $("#" + ocppCommandType + "Value8").val();//chargingProfileKind
				params[8] = $("#" + ocppCommandType + "Value9").val();//transactionId X
				params[9] = $("#" + ocppCommandType + "Value10").val();//recurrencyKind
				params[10] = $("#" + ocppCommandType + "Value11").val();//validFrom
				params[11] = $("#" + ocppCommandType + "Value12").val();//validTo
				params[12] = $("#" + ocppCommandType + "Value13").val();//chargingRateUnit
				params[13] = $("#" + ocppCommandType + "Value14").val();//duration
				params[14] = $("#" + ocppCommandType + "Value15").val();//startSchedule
				params[15] = $("#" + ocppCommandType + "Value16").val();//minChargingRate
				params[16] = $("#" + ocppCommandType + "Value17").val();//chargingSchedule.id
				params[17] = [];
				params[18] = $("#" + ocppCommandType + "Value19").val();
				let value18 = $("#" + ocppCommandType + "Value18");//chargingSchedule.chargingSchedulePeriod
				let value18chList = value18.children();
				for (let i = 0, length = value18chList.length; i < length; ++i) {
					let startPeriod = $(value18chList[i].children[0]).find("INPUT").val();
					let numberPhases = $(value18chList[i].children[1]).find("INPUT").val();
					let limit = $(value18chList[i].children[2]).find("INPUT").val();
					params[17].push({ startPeriod: startPeriod, numberPhases: numberPhases, limit: limit });
				}
				params[19] = $("#" + ocppCommandType + "Value20").val();// 2.1 ChargingProfile 확장 JSON
				break;
			case 'SetChargingProfile'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				params[4] = $("#" + ocppCommandType + "Value5").val();
				params[5] = $("#" + ocppCommandType + "Value6").val();
				params[6] = $("#" + ocppCommandType + "Value7").val();
				params[7] = $("#" + ocppCommandType + "Value8").val();
				params[8] = $("#" + ocppCommandType + "Value9").val();
				params[9] = $("#" + ocppCommandType + "Value10").val();
				params[10] = $("#" + ocppCommandType + "Value11").val();
				params[11] = $("#" + ocppCommandType + "Value12").val();
				params[12] = $("#" + ocppCommandType + "Value13").val();
				params[13] = [];
				let value14 = $("#" + ocppCommandType + "Value14");
				let chList = value14.children();
				for (let i = 0, length = chList.length; i < length; ++i) {
					let startPeriod = $(chList[i].children[0]).find("INPUT").val();
					let numberPhases = $(chList[i].children[1]).find("INPUT").val();
					let limit = $(chList[i].children[2]).find("INPUT").val();
					params[13].push({ startPeriod: startPeriod, numberPhases: numberPhases, limit: limit });
				}
				params[14] = $("#" + ocppCommandType + "Value15").val();
				params[15] = $("#" + ocppCommandType + "Value16").val();// 2.1 ChargingProfile 확장 JSON
				break;

			case 'SetVariableMonitoring'://2.0
				let value1 = $("#" + ocppCommandType + "Value1");
				let chSvmList = value1.children();
				params[0] = [];
				for (let i = 0, length = chSvmList.length; i < length; ++i) {
					let type = $(chSvmList[i].children[0]).find("SELECT").val();
					let value = $(chSvmList[i].children[1]).find("INPUT").val();
					let severity = $(chSvmList[i].children[2]).find("INPUT").val();
					let component = $(chSvmList[i].children[3]).find("INPUT").val();
					let evseId = $(chSvmList[i].children[4]).find("INPUT").val();
					let variable = $(chSvmList[i].children[5]).find("INPUT").val();
					let temp = { value: value, type: type, severity: severity, component: { name: component }, variable: { name: variable } };
					if (evseId && evseId != "") {
						temp.component.evse = { id: evseId };
					}
					params[0].push(temp);
				}
				break;
			case 'GetLog'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				params[4] = $("#" + ocppCommandType + "Value5").val();
				params[5] = $("#" + ocppCommandType + "Value6").val();
				params[6] = $("#" + ocppCommandType + "Value7").val();
				break;
			case 'CostUpdated':
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				break;
			case 'GetBaseReport'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				break;
			case 'SetMonitoringLevel':// 2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				break;
			case 'ClearVariableMonitoring'://2.0
				params[0] = [];
				let idList = $("#" + ocppCommandType + "Value1").val().split(',');
				for (var i = 0; i < idList.length; ++i) {
					params[0].push(idList[i]);
				}
				break;
			case 'ClearDisplayMessage'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				break;
			case 'SetDisplayMessage'://2.0 + 2.1 messageExtra
				console.log(params);
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				params[4] = $("#" + ocppCommandType + "Value5").val();
				params[5] = $("#" + ocppCommandType + "Value6").val();
				params[6] = $("#" + ocppCommandType + "Value7").val();
				params[7] = $("#" + ocppCommandType + "Value8").val();
				params[8] = $("#" + ocppCommandType + "Value9").val();
				params[9] = $("#" + ocppCommandType + "Value10").val();
				params[10] = $("#" + ocppCommandType + "Value11").val();
				params[11] = $("#" + ocppCommandType + "Value12").val();
				params[12] = $("#" + ocppCommandType + "Value13").val();
				break;
			case 'CustomerInformation'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				params[4] = $("#" + ocppCommandType + "Value5").val();
				params[5] = $("#" + ocppCommandType + "Value6").val();
				break;
			case 'SetNetworkProfile'://2.0 + 2.1 보안필드
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				params[4] = $("#" + ocppCommandType + "Value5").val();
				params[5] = $("#" + ocppCommandType + "Value6").val();
				params[6] = $("#" + ocppCommandType + "Value7").val();
				params[7] = $("#" + ocppCommandType + "Value8").val();
				params[8] = $("#" + ocppCommandType + "Value9").val();
				params[9] = $("#" + ocppCommandType + "Value10").val();
				params[10] = $("#" + ocppCommandType + "Value11").val();
				break;
			case 'GetReport'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				params[4] = $("#" + ocppCommandType + "Value5").val();
				break;
			case 'GetMonitoringReport'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = [];
				let GetMonitoringReportValue3 = $("#" + ocppCommandType + "Value3").find("tr");
				if (GetMonitoringReportValue3.length > 0) {
					for (let i = 0, length = GetMonitoringReportValue3.length; i < length; ++i) {
						params[2].push({
							cName: GetMonitoringReportValue3[i].children[0].children[0].value,
							evseId: GetMonitoringReportValue3[i].children[1].children[0].value,
							aName: GetMonitoringReportValue3[i].children[2].children[0].value,
						});
					}
				}
				break;
			case 'GetTransactionStatus'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				break;
			case 'SetMonitoringBase'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				break;
			case 'GetChargingProfiles'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				params[4] = [];
				let tempGetChargingProfiles5 = $("#" + ocppCommandType + "Value5").val();
				if (tempGetChargingProfiles5 && tempGetChargingProfiles5 != '') {
					params[4] = tempGetChargingProfiles5.split(",");
				}

				var chargingLimitSource = new Array();
				$("input:checkbox[name='GetChargingProfileCheckBox']").each(function () {
					if (this.checked) {
						chargingLimitSource.push(this.getAttribute('meta-value'));
					}
				});
				params[5] = chargingLimitSource;
				break;
			case 'UpdateFirmware'://2.0
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
				params[4] = $("#" + ocppCommandType + "Value5").val();
				params[5] = $("#" + ocppCommandType + "Value6").val();
				params[6] = $("#" + ocppCommandType + "Value7").val();
				params[7] = $("#" + ocppCommandType + "Value8").val();
				break;
			case 'GetLocalListVersion'://2.0
			case 'ClearCache'://2.0
				break;
			// ── OCPP 2.1 신규 CSMS→CS 원격제어 ──
			case 'RequestBatterySwap'://2.1
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				break;
			case 'UsePriorityCharging'://2.1
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				break;
			case 'NotifyAllowedEnergyTransfer'://2.1
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				break;
			case 'AFRRSignal'://2.1
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				break;
			case 'GetTariffs'://2.1
				params[0] = $("#" + ocppCommandType + "Value1").val();
				break;
			case 'ClearTariffs'://2.1
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				break;
			case 'SetDefaultTariff'://2.1
			case 'ChangeTransactionTariff'://2.1
			case 'UpdateDynamicSchedule'://2.1
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				break;
			case 'GetCertificateChainStatus'://2.1
				params[0] = $("#" + ocppCommandType + "Value1").val();
				break;
			case 'AdjustPeriodicEventStream'://2.1
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				break;
			case 'GetPeriodicEventStream'://2.1
				break;

		}
		var valueStr = ocpp20CommandDevJs.makeParam(ocppCommandType, params);
		_sendCommand(ocppCommandType, valueStr);
	}

	function _sendCommand(ocppCommandType, valueStr) {
		//
		var param = {
			param1: ocppCommandType,
			param2: valueStr
		};


		swal({
			title: _msg.title,
			text: _msg.confirmSendCommand,
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: _msg.btnConfirm,
			cancelButtonText: _msg.btnCancel,
			closeOnConfirm: true
		}, function () {
			$.ajax({
				type: 'PUT',
				method: 'PUT',
				url: _ctx + '/ws/cmd/ocpp20/' + data.searchCond.cpCsId,
				contentType: "application/json",
				dataType: 'json',
				data: JSON.stringify(param),
				success: function (jsonData) {
					if (jsonData.status == 'SUCCESS') {
						toastr.success(_msg.sendSuccess, _msg.title);
					} else {
						toastr.error(jsonData.result, _msg.title);
					}
				},
				error: function (xhRequest, ErrorText, thrownError) {
					//
					toastr.error(_msg.sendFailure, _msg.title);
				}
			});
		});
	}

	function _addChargingSchedulePeriod() {
		//
		let html = "<tr>";
		html += '<td><input type="number" placeholder="0"   class="form-control input-sm" /></td>';
		html += '<td><input type="number" placeholder="3"   class="form-control input-sm" /></td>';
		html += '<td><input type="number" placeholder="1" class="form-control input-sm" /></td>';
		html += '<td><button onclick="ocpp20DevControlJs.removeChargingSchedulePeriod(this);">삭제</button></td>';
		html += "</tr>";
		$("#SetChargingProfileValue14").append(html);
	}

	function _addRTChargingSchedulePeriod() {
		let html = "<tr>";
		html += '<td><input type="number" placeholder="0"   class="form-control input-sm" /></td>';
		html += '<td><input type="number" placeholder="3"   class="form-control input-sm" /></td>';
		html += '<td><input type="number" placeholder="1" class="form-control input-sm" /></td>';
		html += '<td><button onclick="ocpp20DevControlJs.removeChargingSchedulePeriod(this);">삭제</button></td>';
		html += "</tr>";
		$("#RequestStartTransactionValue18").append(html);
	}

	function _addSetVariableMonitoring() {
		//
		let html = "<tr>";
		html += '<td><select class="input-sm form-control input-s-sm inline" id="SetVariableMonitoringValue">' +
			'<option value="UpperThreshold" seledted>UpperThreshold</option>' +
			'<option value="LowerThreshold">LowerThreshold</option>' +
			'<option value="Delta">Delta</option>' +
			'<option value="Periodic">Periodic</option>' +
			'<option value="PeriodicClockAligned">PeriodicClockAligned</option>' +
			'</select></td>';
		html += '<td><input type="number" placeholder="0"   class="form-control input-sm" /></td>';
		html += '<td><input type="number" placeholder="0" class="form-control input-sm" /></td>';
		html += '<td><input type="text"  class="form-control input-sm" /></td>';
		html += '<td><input type="number"  class="form-control input-sm" /></td>';
		html += '<td><input type="text"  class="form-control input-sm" /></td>';
		html += '<td><button onclick="ocpp20DevControlJs.removeSetVariableMonitoring(this);">삭제</button></td>';
		html += "</tr>";
		$("#SetVariableMonitoringValue1").append(html);
	}

	function _removeChargingSchedulePeriod(that) {
		//
		that.parentElement.parentElement.remove();
	}
	function _removeSetVariableMonitoring(that) {
		//
		that.parentElement.parentElement.remove();
	}

	function _log() {
		//
		console.log(data);
	}

	function _addSetVariables() {
		//
		let varList = ocpp20VarJs.getAllVariableKey();

		let html = "<tr><td>";
		html += '<select class="input-sm form-control input-s-sm inline" onchange="ocpp20DevControlJs.changeSetVariableItem(this)">';
		for (let i = 0, size = varList.length; i < size; ++i) {
			if (i == 0) {
				html += '<option value="' + varList[i] + '" seledted>' + varList[i] + '</option>';
			} else {
				html += '<option value="' + varList[i] + '">' + varList[i] + '</option>';
			}
		}
		let variable = ocpp20VarJs.getVariable(varList[0]);
		html += "</select>";
		html += "</td>";
		html += '<td><input type="text" class="form-control input-sm"/>';
		html += '<div>';
		html += variable.componentName;
		if (variable.evse) {
			html += ',evse:' + variable.evse;
		}
		html += ',' + variable.variableName;
		if (variable.variableInstance) {
			html += ',variableInstance:' + variable.variableInstance;
		}
		html += ',' + variable.variableCharacteristics.dataType;
		if (variable.variableCharacteristics.valuesList) {
			html += ',valuesList:' + variable.variableCharacteristics.valuesList;
		}
		html += ',' + variable.variableAttributes.mutability;
		html += '</div></td>';
		html += '<td><button onClick="ocpp20DevControlJs.deleteSetVarialbeItem(this);">삭제</button></td>';
		html += "</tr>";
		$("#SetVariablesTbody").append(html);
	}

	function _changeSetVariableItem(target) {
		//td
		let variable = ocpp20VarJs.getVariable(target.value);
		let children = target.parentElement.parentElement.children[1];
		let html = '';
		if (variable.evse) {
			html += '<input type="number" placeholder="evseId" meter-value="evseId" class="form-control input-sm"/>';
		}
		html += '<input type="text" placeholder="value" meter-value="value" class="form-control input-sm"/>';
		html += '<div>';
		html += variable.componentName;
		if (variable.evse) {
			html += ',evse:' + variable.evse;
		}
		html += ',' + variable.variableName;
		if (variable.variableInstance) {
			html += ',variableInstance:' + variable.variableInstance;
		}
		html += ',' + variable.variableCharacteristics.dataType;
		if (variable.variableCharacteristics.valuesList) {
			html += ',valuesList:' + variable.variableCharacteristics.valuesList;
		}
		html += ',' + variable.variableAttributes.mutability;
		html += '</div>';
		$(children).html(html);

	}

	function _deleteSetVarialbeItem(target) {
		target.parentElement.parentElement.remove();
	}

	return {
		init: _init,
		changeOcppCommand: _changeOcppCommand,
		searchOcppLog: _searchOcppLog,
		removeChargingSchedulePeriod: _removeChargingSchedulePeriod,
		removeSetVariableMonitoring: _removeSetVariableMonitoring,
		log: _log,
		addSetVariables: _addSetVariables,
		changeSetVariableItem: _changeSetVariableItem,
		deleteSetVarialbeItem: _deleteSetVarialbeItem,
		removeGetMonitoringReport: _removeGetMonitoringReport
	};
}();
