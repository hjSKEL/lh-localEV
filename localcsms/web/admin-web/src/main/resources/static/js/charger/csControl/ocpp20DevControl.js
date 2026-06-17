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

		// SetDERControl 2.1 — sub-object JSON 샘플 / UUID 버튼
		$("#btnSetDERControlUuid").click(function () {
			$("#SetDERControlValue2").val(_uuidv4());
		});
		$("#btnSetDERSample1").click(function () { // FreqDroop (TC_R_107 Step 5)
			$("#SetDERControlValue1").val("true");
			$("#SetDERControlValue3").val("FreqDroop");
			$("#SetDERControlValue4").val(JSON.stringify({
				freqDroop: { priority: 6, overFreq: 50.5, underFreq: 49.5,
				             overDroop: 0.05, underDroop: 0.05, responseTime: 10 }
			}, null, 2));
		});
		$("#btnSetDERSample2").click(function () { // FreqWatt Curve (Step 7)
			$("#SetDERControlValue1").val("false");
			$("#SetDERControlValue3").val("FreqWatt");
			$("#SetDERControlValue4").val(JSON.stringify({
				curve: {
					priority: 4, yUnit: "PctMaxW",
					startTime: new Date().toISOString().replace(/\.\d{3}Z$/, "Z"),
					duration: 900,
					curveData: [
						{ x: 49,   y: 75 }, { x: 49.5, y: 90 },
						{ x: 50.5, y: 100 }, { x: 51, y: 100 }
					]
				}
			}, null, 2));
		});
		$("#btnSetDERSample3").click(function () { // EnterService (Step 9)
			$("#SetDERControlValue1").val("false");
			$("#SetDERControlValue3").val("EnterService");
			$("#SetDERControlValue4").val(JSON.stringify({
				enterService: { priority: 1, highVoltage: 250, lowVoltage: 210,
				                highFreq: 50.5, lowFreq: 49.5 }
			}, null, 2));
		});
		$("#btnSetDERSample4").click(function () { // Gradients
			$("#SetDERControlValue1").val("true");
			$("#SetDERControlValue3").val("Gradients");
			$("#SetDERControlValue4").val(JSON.stringify({
				gradient: { priority: 0, gradient: 600, softGradient: 300 }
			}, null, 2));
		});
		$("#btnSetDERSample5").click(function () { // LimitMaxDischarge
			$("#SetDERControlValue1").val("false");
			$("#SetDERControlValue3").val("LimitMaxDischarge");
			$("#SetDERControlValue4").val(JSON.stringify({
				limitMaxDischarge: { priority: 2, pctMaxDischargePower: 80,
				                     startTime: new Date().toISOString().replace(/\.\d{3}Z$/, "Z"),
				                     duration: 3600 }
			}, null, 2));
		});
		$("#btnSetDERSample6").click(function () { // FixedPFInject
			$("#SetDERControlValue1").val("false");
			$("#SetDERControlValue3").val("FixedPFInject");
			$("#SetDERControlValue4").val(JSON.stringify({
				fixedPFInject: { priority: 3, displacement: 0.95, excitation: false,
				                 startTime: new Date().toISOString().replace(/\.\d{3}Z$/, "Z"),
				                 duration: 1800 }
			}, null, 2));
		});
		$("#btnSetDERSample7").click(function () { // FixedVar
			$("#SetDERControlValue1").val("false");
			$("#SetDERControlValue3").val("FixedVar");
			$("#SetDERControlValue4").val(JSON.stringify({
				fixedVar: { priority: 3, setpoint: 30, unit: "PctMaxVar",
				            startTime: new Date().toISOString().replace(/\.\d{3}Z$/, "Z"),
				            duration: 1800 }
			}, null, 2));
		});

		// SetChargingProfile 2.1 샘플 3종
		$("#btnSetCPSample1").click(function () { // Basic TxDefaultProfile (Absolute, A)
			$("#SetChargingProfileValue1").val("1");
			$("#SetChargingProfileValue2").val("1001");
			$("#SetChargingProfileValue3").val("3");
			$("#SetChargingProfileValue4").val("TxDefaultProfile");
			$("#SetChargingProfileValue5").val("Absolute");
			$("#SetChargingProfileValue6").val("");
			$("#SetChargingProfileValue7").val("");
			$("#SetChargingProfileValue8").val("");
			$("#SetChargingProfileValue9").val("");
			$("#SetChargingProfileValue10").val("A");
			$("#SetChargingProfileValue11").val("3600");
			$("#SetChargingProfileValue12").val(new Date().toISOString().replace(/\.\d{3}Z$/, "Z"));
			$("#SetChargingProfileValue13").val("6");
			$("#SetChargingProfileValue15").val("1");
			$("#SetChargingProfileValue14").empty();
			_addChargingSchedulePeriod();
			let row = $("#SetChargingProfileValue14 tr:last-child");
			row.find("td:eq(0) input").val("0");
			row.find("td:eq(1) input").val("3");
			row.find("td:eq(3) input").val("32");
		});
		$("#btnSetCPSample2").click(function () { // V2X Discharge (TxProfile, ExternalLimits, dischargeLimit)
			$("#SetChargingProfileValue1").val("1");
			$("#SetChargingProfileValue2").val("2001");
			$("#SetChargingProfileValue3").val("5");
			$("#SetChargingProfileValue4").val("TxProfile");
			$("#SetChargingProfileValue5").val("Absolute");
			$("#SetChargingProfileValue10").val("W");
			$("#SetChargingProfileValue11").val("1800");
			$("#SetChargingProfileValue12").val(new Date().toISOString().replace(/\.\d{3}Z$/, "Z"));
			$("#SetChargingProfileValue15").val("2");
			$("#SetChargingProfileValue14").empty();
			_addChargingSchedulePeriod();
			let row = $("#SetChargingProfileValue14 tr:last-child");
			row.find("td:eq(0) input").val("0");        // startPeriod
			row.find("td:eq(3) input").val("7000");     // limit (charge upper)
			row.find("td:eq(4) select").val("ExternalLimits"); // operationMode
			row.find("td:eq(5) input").val("3000");     // setpoint
			row.find("td:eq(6) input").val("-5000");    // dischargeLimit
		});
		$("#btnSetCPSample3").click(function () { // Dynamic Profile
			$("#SetChargingProfileValue1").val("1");
			$("#SetChargingProfileValue2").val("3001");
			$("#SetChargingProfileValue3").val("7");
			$("#SetChargingProfileValue4").val("TxDefaultProfile");
			$("#SetChargingProfileValue5").val("Dynamic");
			$("#SetChargingProfileValue10").val("W");
			$("#SetChargingProfileValue11").val("3600");
			$("#SetChargingProfileValue12").val(new Date().toISOString().replace(/\.\d{3}Z$/, "Z"));
			$("#SetChargingProfileValue15").val("3");
			$("#SetChargingProfileValue17").val("3600");  // maxOfflineDuration
			$("#SetChargingProfileValue18").val("false"); // invalidAfterOfflineDuration
			$("#SetChargingProfileValue19").val("300");   // dynUpdateInterval
			$("#SetChargingProfileValue24").val(new Date().toISOString().replace(/\.\d{3}Z$/, "Z")); // dynUpdateTime
			$("#SetChargingProfileValue20").val("80");    // limitAtSoC.soc
			$("#SetChargingProfileValue21").val("3000");  // limitAtSoC.limit
			$("#SetChargingProfileValue14").empty();
			_addChargingSchedulePeriod();
			let row = $("#SetChargingProfileValue14 tr:last-child");
			row.find("td:eq(0) input").val("0");
			row.find("td:eq(3) input").val("11000");
			row.find("td:eq(4) select").val("ChargingOnly");
		});

		// Q_120 AFRR (LocalFrequency + v2xBaseline + v2xFreqWattCurve + v2xSignalWattCurve)
		$("#btnSetCPSampleQ120").click(function () {
			_fillSetCPCommonV2X({
				purpose: "TxDefaultProfile", kind: "Absolute", profileId: "1",
				stackLevel: "3", transactionId: "", operationMode: "LocalFrequency"
			});
			$("#SetChargingProfileValue16").val(JSON.stringify({
				periods: [{
					v2xBaseline: 5000,
					v2xFreqWattCurve: [
						{ frequency: 49.5, power:  5000 },
						{ frequency: 50.0, power:  0 },
						{ frequency: 50.5, power: -5000 }
					],
					v2xSignalWattCurve: [
						{ signal: -1.0, power:  5000 },
						{ signal:  0.0, power:  0 },
						{ signal:  1.0, power: -5000 }
					]
				}]
			}, null, 2));
		});

		// Q_121 LocalFrequency (TxProfile + transactionId + v2xBaseline=7000 + 3-point curve)
		$("#btnSetCPSampleQ121").click(function () {
			_fillSetCPCommonV2X({
				purpose: "TxProfile", kind: "Absolute", profileId: "2",
				stackLevel: "3", transactionId: "__FILL_TRANSACTION_ID__",
				operationMode: "LocalFrequency"
			});
			$("#SetChargingProfileValue16").val(JSON.stringify({
				periods: [{
					v2xBaseline: 7000,
					v2xFreqWattCurve: [
						{ frequency: 49.5, power: -1000 },
						{ frequency: 50.0, power:  0 },
						{ frequency: 50.5, power:  1000 }
					]
				}]
			}, null, 2));
		});

		// Q_124 LocalLoadBalancing (TxDefaultProfile, 확장 JSON 불필요)
		$("#btnSetCPSampleQ124").click(function () {
			_fillSetCPCommonV2X({
				purpose: "TxDefaultProfile", kind: "Absolute", profileId: "3",
				stackLevel: "3", transactionId: "",
				operationMode: "LocalLoadBalancing"
			});
			$("#SetChargingProfileValue16").val("");
		});

		// Q_125a Idle (TxProfile + evseSleep=false)
		$("#btnSetCPSampleQ125a").click(function () {
			_fillSetCPCommonV2X({
				purpose: "TxProfile", kind: "Absolute", profileId: "4",
				stackLevel: "3", transactionId: "__FILL_TRANSACTION_ID__",
				operationMode: "Idle"
			});
			$("#SetChargingProfileValue16").val(JSON.stringify({
				periods: [ { evseSleep: false } ]
			}, null, 2));
		});

		// Q_125b Idle (TxProfile + evseSleep=true)
		$("#btnSetCPSampleQ125b").click(function () {
			_fillSetCPCommonV2X({
				purpose: "TxProfile", kind: "Absolute", profileId: "5",
				stackLevel: "3", transactionId: "__FILL_TRANSACTION_ID__",
				operationMode: "Idle"
			});
			$("#SetChargingProfileValue16").val(JSON.stringify({
				periods: [ { evseSleep: true } ]
			}, null, 2));
		});

		// Q_124 SetVariables 4건 — V2XChargingCtrlr.V2XLocalLoadBalancing (Upper/LowerThreshold/Offset)
		$("#btnSetVarSampleQ124").click(function () {
			$("#SetVariablesTbody").empty();
			let items = [
				{ key: "V2XLocalLoadBalancingUpperThreshold", value: "2000" },
				{ key: "V2XLocalLoadBalancingLowerThreshold", value: "-1000" },
				{ key: "V2XLocalLoadBalancingUpperOffset",    value: "200"  },
				{ key: "V2XLocalLoadBalancingLowerOffset",    value: "0"    }
			];
			items.forEach(function (item) {
				_addSetVariables();
				let lastRow = $("#SetVariablesTbody tr:last-child");
				let select = lastRow.find("td:eq(0) select")[0];
				select.value = item.key;
				// changeSetVariableItem 트리거 — evse 정의 항목이므로 evseId + value input 두 개 생성됨
				_changeSetVariableItem(select);
				let inputs = lastRow.find("td:eq(1) input");
				// inputs[0] = evseId (meter-value="evseId"), inputs[1] = value (meter-value="value")
				inputs[0].value = "1";
				inputs[1].value = item.value;
			});
		});

		// 샘플: NetworkConfiguration instance=2 (10건) + OCPPCommCtrlr.NetworkConfigurationPriority (1건)
		$("#btnSetVarSampleNetwork").click(function () {
			$("#SetVariablesTbody").empty();
			let instance = "2";
			// component.instance 가변 항목 — NetworkConfiguration.* (key, value)
			let ncItems = [
				{ key: "NetworkConfiguration.OcppVersion",       value: "OCPP21" },
				{ key: "NetworkConfiguration.OcppTransport",     value: "JSON" },
				{ key: "NetworkConfiguration.OcppInterface",     value: "Wired0" },
				{ key: "NetworkConfiguration.OcppCsmsUrl",       value: "ws://test.kevit.co.kr:32001/ocpp20/223402-01" },
				{ key: "NetworkConfiguration.MessageTimeout",    value: "30" },
				{ key: "NetworkConfiguration.SecurityProfile",   value: "1" },
				{ key: "NetworkConfiguration.Identity",          value: "223402-01" },
				{ key: "NetworkConfiguration.BasicAuthPassword", value: "A123456789012345" },
				{ key: "NetworkConfiguration.VpnEnabled",        value: "false" },
				{ key: "NetworkConfiguration.ApnEnabled",        value: "false" }
			];
			ncItems.forEach(function (item) {
				_addSetVariables();
				let lastRow = $("#SetVariablesTbody tr:last-child");
				let select = lastRow.find("td:eq(0) select")[0];
				select.value = item.key;
				_changeSetVariableItem(select);
				lastRow.find('td:eq(1) input[meter-value="componentInstance"]').val(instance);
				lastRow.find('td:eq(1) input[meter-value="value"]').val(item.value);
			});
			// NetworkConfigurationPriority — instance 없음, value="1,2"
			_addSetVariables();
			let priRow = $("#SetVariablesTbody tr:last-child");
			let priSelect = priRow.find("td:eq(0) select")[0];
			priSelect.value = "NetworkConfigurationPriority";
			_changeSetVariableItem(priSelect);
			priRow.find('td:eq(1) input[meter-value="value"]').val("1,2");
		});

		// 샘플: NetworkConfiguration + APN (14건) — instance=2, APN 사용(ApnEnabled=true) 케이스
		$("#btnSetVarSampleNetworkApn").click(function () {
			$("#SetVariablesTbody").empty();
			let instance = "2";
			let items = [
				{ key: "NetworkConfiguration.OcppCsmsUrl",        value: "ws://test.kevit.co.kr:32001/ocpp20/223402-01" },
				{ key: "NetworkConfiguration.OcppInterface",      value: "Any" },
				{ key: "NetworkConfiguration.OcppTransport",      value: "JSON" },
				{ key: "NetworkConfiguration.OcppVersion",        value: "OCPP21" },
				{ key: "NetworkConfiguration.MessageTimeout",     value: "30" },
				{ key: "NetworkConfiguration.SecurityProfile",    value: "2" },
				{ key: "NetworkConfiguration.Identity",           value: "223402-01" },
				{ key: "NetworkConfiguration.BasicAuthPassword",  value: "PasswordOfSufficientLength" },
				{ key: "NetworkConfiguration.VpnEnabled",         value: "false" },
				{ key: "NetworkConfiguration.ApnEnabled",         value: "true" },
				{ key: "NetworkConfiguration.Apn",                value: "internet" },
				{ key: "NetworkConfiguration.ApnUserName",        value: "user" },
				{ key: "NetworkConfiguration.ApnPassword",        value: "password" },
				{ key: "NetworkConfiguration.ApnAuthentication",  value: "AUTO" }
			];
			items.forEach(function (item) {
				_addSetVariables();
				let lastRow = $("#SetVariablesTbody tr:last-child");
				let select = lastRow.find("td:eq(0) select")[0];
				select.value = item.key;
				_changeSetVariableItem(select);
				lastRow.find('td:eq(1) input[meter-value="componentInstance"]').val(instance);
				lastRow.find('td:eq(1) input[meter-value="value"]').val(item.value);
			});
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
			case 'SetDERControl'://2.0 + 2.1 sub-object JSON
				params[0] = $("#" + ocppCommandType + "Value1").val();
				params[1] = $("#" + ocppCommandType + "Value2").val();
				params[2] = $("#" + ocppCommandType + "Value3").val();
				params[3] = $("#" + ocppCommandType + "Value4").val();
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
				params[7] = $("#" + ocppCommandType + "Value8").val();// idToken/groupIdToken 공통 type
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
					// 2.1 OCPP — same variableName 가 instance 별로 분리되는 경우 (V2XLocalLoadBalancing 등)
					if (varInstance.variableInstance) {
						temp.variable.instance = varInstance.variableInstance;
					}
					// meter-value 속성 기준으로 입력 추출 (componentInstance / evseId / value)
					let $valueTd = $(valueTd);
					let valInput = $valueTd.find('input[meter-value="value"]');
					if (valInput.length === 0) {
						// 변경 전 기본 행은 meter-value 없는 단일 텍스트 input
						valInput = $valueTd.find("input").filter(function () {
							return !this.getAttribute("meter-value");
						}).first();
					}
					if (valInput.length) {
						temp.attributeValue = valInput[0].value;
					}
					let evseInput = $valueTd.find('input[meter-value="evseId"]');
					if (evseInput.length && evseInput[0].value !== "") {
						temp.component.evse = { id: evseInput[0].value };
					}
					let compInstInput = $valueTd.find('input[meter-value="componentInstance"]');
					if (compInstInput.length && compInstInput[0].value !== "") {
						temp.component.instance = compInstInput[0].value;
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
				params[20] = $("#" + ocppCommandType + "Value21").val();// 2.1 idToken.additionalInfo (JSON 배열)
				break;
			case 'SetChargingProfile'://2.0 + 2.1 확장
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
					// 8 컬럼: startPeriod / numberPhases / phaseToUse / limit / operationMode / setpoint / dischargeLimit / 삭제
					let startPeriod    = $(chList[i].children[0]).find("INPUT").val();
					let numberPhases   = $(chList[i].children[1]).find("INPUT").val();
					let phaseToUse     = $(chList[i].children[2]).find("INPUT").val();
					let limit          = $(chList[i].children[3]).find("INPUT").val();
					let operationMode  = $(chList[i].children[4]).find("SELECT").val();
					let setpoint       = $(chList[i].children[5]).find("INPUT").val();
					let dischargeLimit = $(chList[i].children[6]).find("INPUT").val();
					params[13].push({
						startPeriod: startPeriod,
						numberPhases: numberPhases,
						phaseToUse: phaseToUse,
						limit: limit,
						operationMode: operationMode,
						setpoint: setpoint,
						dischargeLimit: dischargeLimit
					});
				}
				params[14] = $("#" + ocppCommandType + "Value15").val();
				params[15] = $("#" + ocppCommandType + "Value16").val(); // 2.1 ChargingProfile 확장 JSON
				// 2.1 신규 직접 입력 — profile / schedule level
				params[16] = $("#" + ocppCommandType + "Value17").val(); // maxOfflineDuration
				params[17] = $("#" + ocppCommandType + "Value18").val(); // invalidAfterOfflineDuration
				params[18] = $("#" + ocppCommandType + "Value19").val(); // dynUpdateInterval
				params[19] = $("#" + ocppCommandType + "Value20").val(); // limitAtSoC.soc
				params[20] = $("#" + ocppCommandType + "Value21").val(); // limitAtSoC.limit
				params[21] = $("#" + ocppCommandType + "Value22").val(); // randomizedDelay
				params[22] = $("#" + ocppCommandType + "Value23").val(); // useLocalTime
				params[23] = $("#" + ocppCommandType + "Value24").val(); // dynUpdateTime
				break;

			case 'SetVariableMonitoring'://2.0 + 2.1 (id / transaction / periodicEventStream)
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
					let monId = $(chSvmList[i].children[6]).find("INPUT").val();
					let txFlag = $(chSvmList[i].children[7]).find("SELECT").val();
					let pesInterval = $(chSvmList[i].children[8]).find("INPUT").val();
					let pesValues = $(chSvmList[i].children[9]).find("INPUT").val();
					let temp = {
						value: Number(value),
						type: type,
						severity: parseInt(severity),
						component: { name: component },
						variable: { name: variable }
					};
					if (evseId && evseId != "") {
						temp.component.evse = { id: parseInt(evseId) };
					}
					// 2.1 — 기존 monitor 교체 시에만 id 사용
					if (monId && monId != "") {
						temp.id = parseInt(monId);
					}
					// 2.1 — transaction (boolean)
					if (txFlag && txFlag != "") {
						temp.transaction = (txFlag === "true");
					}
					// 2.1 — periodicEventStream (interval + values, Periodic/PeriodicClockAligned 와 함께 사용)
					if ((pesInterval && pesInterval != "") || (pesValues && pesValues != "")) {
						temp.periodicEventStream = {};
						if (pesInterval && pesInterval != "") temp.periodicEventStream.interval = parseInt(pesInterval);
						if (pesValues && pesValues != "")     temp.periodicEventStream.values   = parseInt(pesValues);
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
				params[13] = $("#" + ocppCommandType + "Value14").val();
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

		data.searchCond.cpCsId = queryString.cpId + '-' + queryString.csId;

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
				url: _ctx + '/ws/cmd/ocpp20/bypass/' + data.searchCond.cpCsId,
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

	/**
	 * Q-series SetChargingProfile 샘플 공통 채우기.
	 *
	 * 모든 V2X Local* / Idle 케이스 공통 — limit/setpoint/dischargeLimit/setpointReactive 등
	 * 충전률 필드는 모두 omitted (검증 항목). chargingRateUnit=W, schedulePeriod startPeriod=0.
	 * v2xBaseline / v2xFreqWattCurve / v2xSignalWattCurve / evseSleep 같은 V2X 옵션은
	 * 호출 측에서 SetChargingProfileValue16 (확장 JSON) 으로 추가 주입.
	 */
	function _fillSetCPCommonV2X(opts) {
		$("#SetChargingProfileValue1").val("1");                                  // evseId
		$("#SetChargingProfileValue2").val(opts.profileId || "");                 // chargingProfileId
		$("#SetChargingProfileValue3").val(opts.stackLevel || "3");               // stackLevel
		$("#SetChargingProfileValue4").val(opts.purpose);                         // chargingProfilePurpose
		$("#SetChargingProfileValue5").val(opts.kind || "Absolute");              // chargingProfileKind
		$("#SetChargingProfileValue6").val(opts.transactionId || "");             // transactionId
		$("#SetChargingProfileValue7").val("");                                   // recurrencyKind
		$("#SetChargingProfileValue8").val("");                                   // validFrom
		$("#SetChargingProfileValue9").val("");                                   // validTo
		$("#SetChargingProfileValue10").val("W");                                 // chargingRateUnit
		$("#SetChargingProfileValue11").val("");                                  // duration
		$("#SetChargingProfileValue12").val("");                                  // startSchedule
		$("#SetChargingProfileValue13").val("");                                  // minChargingRate
		$("#SetChargingProfileValue15").val("1");                                 // chargingSchedule.id
		$("#SetChargingProfileValue17").val("");                                  // maxOfflineDuration
		$("#SetChargingProfileValue18").val("");                                  // invalidAfterOfflineDuration
		$("#SetChargingProfileValue19").val("");                                  // dynUpdateInterval
		$("#SetChargingProfileValue20").val("");                                  // limitAtSoC.soc
		$("#SetChargingProfileValue21").val("");                                  // limitAtSoC.limit
		$("#SetChargingProfileValue22").val("");                                  // randomizedDelay
		$("#SetChargingProfileValue23").val("");                                  // useLocalTime
		$("#SetChargingProfileValue24").val("");                                  // dynUpdateTime
		$("#SetChargingProfileValue14").empty();
		_addChargingSchedulePeriod();
		let row = $("#SetChargingProfileValue14 tr:last-child");
		row.find("td:eq(0) input").val("0");                  // startPeriod
		// numberPhases, phaseToUse, limit, setpoint, dischargeLimit 모두 비움 (omitted)
		row.find("td:eq(4) select").val(opts.operationMode);  // operationMode
	}

	function _addChargingSchedulePeriod() {
		//
		// [0] startPeriod, [1] numberPhases, [2] phaseToUse, [3] limit,
		// [4] operationMode (2.1), [5] setpoint (2.1), [6] dischargeLimit (2.1), [7] 삭제
		let html = "<tr>";
		html += '<td><input type="number" placeholder="0"   class="form-control input-sm" /></td>';
		html += '<td><input type="number" placeholder="3"   class="form-control input-sm" /></td>';
		html += '<td><input type="number" placeholder=""    class="form-control input-sm" /></td>';
		html += '<td><input type="number" placeholder="1"   class="form-control input-sm" /></td>';
		html += '<td><select class="input-sm form-control input-s-sm inline">' +
			'<option value="" selected>-</option>' +
			'<option value="Idle">Idle</option>' +
			'<option value="ChargingOnly">ChargingOnly</option>' +
			'<option value="CentralSetpoint">CentralSetpoint</option>' +
			'<option value="ExternalSetpoint">ExternalSetpoint</option>' +
			'<option value="ExternalLimits">ExternalLimits</option>' +
			'<option value="CentralFrequency">CentralFrequency</option>' +
			'<option value="LocalFrequency">LocalFrequency</option>' +
			'<option value="LocalLoadBalancing">LocalLoadBalancing</option>' +
			'</select></td>';
		html += '<td><input type="number" placeholder=""    class="form-control input-sm" /></td>';
		html += '<td><input type="number" placeholder=""    class="form-control input-sm" /></td>';
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
		// [0] type
		html += '<td><select class="input-sm form-control input-s-sm inline" id="SetVariableMonitoringValue">' +
			'<option value="UpperThreshold" selected>UpperThreshold</option>' +
			'<option value="LowerThreshold">LowerThreshold</option>' +
			'<option value="Delta">Delta</option>' +
			'<option value="Periodic">Periodic</option>' +
			'<option value="PeriodicClockAligned">PeriodicClockAligned</option>' +
			'</select></td>';
		// [1] value
		html += '<td><input type="number" placeholder="0"   class="form-control input-sm" /></td>';
		// [2] severity
		html += '<td><input type="number" placeholder="0" class="form-control input-sm" /></td>';
		// [3] component(name)
		html += '<td><input type="text"  class="form-control input-sm" /></td>';
		// [4] evseId
		html += '<td><input type="number"  class="form-control input-sm" /></td>';
		// [5] variable(name)
		html += '<td><input type="text"  class="form-control input-sm" /></td>';
		// [6] id (replace existing monitor)
		html += '<td><input type="number" placeholder="" class="form-control input-sm" /></td>';
		// [7] transaction
		html += '<td><select class="input-sm form-control input-s-sm inline">' +
			'<option value="" selected>-</option>' +
			'<option value="true">true</option>' +
			'<option value="false">false</option>' +
			'</select></td>';
		// [8] periodicEventStream.interval
		html += '<td><input type="number" placeholder="" class="form-control input-sm" /></td>';
		// [9] periodicEventStream.values
		html += '<td><input type="number" placeholder="" class="form-control input-sm" /></td>';
		// [10] 삭제
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
				html += '<option value="' + varList[i] + '" selected>' + varList[i] + '</option>';
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
		if (variable.componentInstance) {
			html += '<input type="text" placeholder="component.instance" meter-value="componentInstance" class="form-control input-sm"/>';
		}
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

	/** RFC 4122 v4 UUID 생성 (crypto API 없으면 Math.random fallback) */
	function _uuidv4() {
		if (window.crypto && typeof window.crypto.randomUUID === 'function') {
			return window.crypto.randomUUID();
		}
		return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
			var r = Math.random() * 16 | 0;
			var v = c === 'x' ? r : (r & 0x3 | 0x8);
			return v.toString(16);
		});
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
