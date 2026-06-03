var ocpp20CommandDevJs = function () {
	//	
	function _Reset(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { type: params[0] };//Immediate, OnIdle
		if (params[1] && params[1] != "") {
			obj.evseId = Number(params[1])
		}
		return JSON.stringify(obj);
	}

	function _PublishFirmware(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = {
			requestId: parseInt(params[0]),
			location: params[1],
			checksum: params[2]
		};
		if (params[3] && params[3] != '') {
			obj.retries = parseInt(params[3]);
		}
		if (params[4] && params[4] != '') {
			obj.retryInterval = parseInt(params[4]);
		}
		return JSON.stringify(obj);
	}

	function _UnpublishFirmware(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { checksum: params[0] };
		return JSON.stringify(obj);
	}

	function _GetInstalledCertificateIds(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = {};
		if (params[0] && params[0].length != 0) {
			obj = { certificateType: [] };
			for (let i = 0, size = params[0].length; i < size; ++i) {
				obj.certificateType.push(params[0][i]);
			}
		}
		return JSON.stringify(obj);
	}

	function _CertificateSigned(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { certificateChain: params[1], certificateType: params[0] };
		if (params[2] && params[2] != '') {
			obj.requestId = parseInt(params[2], 10);
		}
		return JSON.stringify(obj);
	}

	function _UnlockConnector(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { evseId: Number(params[0]), connectorId: Number(params[1]) };// > 0
		return JSON.stringify(obj);
	}

	function _CancelReservation(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { reservationId: params[0] };
		return JSON.stringify(obj);
	}

	function _ChangeAvailability(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { operationalStatus: params[2] };//Operative, Inoperative
		if (params[0] && params[0] != '') {
			obj.evse = { id: Number(params[0]) };
			if (params[1] && params[1] != '') {
				obj.evse.connectorId = Number(params[1]);
			}
		}
		return JSON.stringify(obj);
	}

	function _ClearCache(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = {};
		return JSON.stringify(obj);
	}

	function _ClearChargingProfile(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = {};
		if (params[0] && params[0] != '') {
			obj.chargingProfileId = parseInt(params[0]);
		}
		if (params[1] && params[1] != '') {
			if (!obj.chargingProfileCriteria) {
				obj.chargingProfileCriteria = {};
			}
			obj.chargingProfileCriteria.evseId = parseInt(params[1]);
		}
		if (params[2] && params[2] != '') {
			if (!obj.chargingProfileCriteria) {
				obj.chargingProfileCriteria = {};
			}
			obj.chargingProfileCriteria.stackLevel = parseInt(params[2]);
		}
		if (params[3] && params[3] != '') {
			if (!obj.chargingProfileCriteria) {
				obj.chargingProfileCriteria = {};
			}
			obj.chargingProfileCriteria.chargingProfilePurpose = params[3];
		}
		return JSON.stringify(obj);
	}

	function _DataTransfer(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { vendorId: params[0], messageId: params[1], data: params[2] };
		return JSON.stringify(obj);
	}

	function _GetCompositeSchedule(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { evseId: Number(params[0]), duration: Number(params[1]) };
		if (params[2] && params[2] != '') {
			obj.chargingRateUnit = params[2];
		}
		return JSON.stringify(obj);
	}

	function _GetVariables(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { getVariableData: [] };
		obj.getVariableData = params;
		return JSON.stringify(obj);
	}

	function _SetVariables(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { setVariableData: [] };
		obj.setVariableData = params;
		return JSON.stringify(obj);
	}

	function _GetDiagnostics(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { location: params[0], retries: params[1], retryInterval: params[2], startTime: params[3], stopTime: params[4] };
		return JSON.stringify(obj);
	}

	function _GetLocalListVersion(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = {};
		return JSON.stringify(obj);
	}

	function _RequestStartTransaction(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { remoteStartId: params[1], idToken: { idToken: params[2], type: params[18] } };
		if (params[0] && params[0] != '') {
			obj.evseId = Number(params[0]);
		}

		if (params[3] && params[3] != '') {
			obj.groupIdToken = { idToken: params[3], type: params[18] };
		}

		// 2.1 idToken.additionalInfo (JSON 배열, optional)
		if (params[20] && params[20] != '') {
			try {
				obj.idToken.additionalInfo = JSON.parse(params[20]);
			} catch (e) {
				console.error('[RequestStartTransaction] additionalInfo JSON parse 실패:', e);
			}
		}

		// ChargingProfile
		if (params[4] && params[4] != '') {
			obj.chargingProfile = {
				id: params[4],
				stackLevel: params[5],
				chargingProfilePurpose: params[6],
				chargingProfileKind: params[7],
				chargingSchedule: [{
					id: params[16],//
					chargingRateUnit: params[12],
					chargingSchedulePeriod: params[17],
					duration: params[13],
					minChargingRate: params[15]
					//salesTariff:{}
				}]
			};
			//if(params[8] && params[8] != ''){
			//	obj.chargingProfile.transactionId = params[8];
			//}
			if (params[9] && params[9] != '') {
				obj.chargingProfile.recurrencyKind = params[9];
			}
			if (params[10] && params[10] != '') {
				obj.chargingProfile.validFrom = params[10];
			}
			if (params[11] && params[11] != '') {
				obj.chargingProfile.validTo = params[11];
			}
			if (params[14] && params[14] != '') {
				obj.chargingProfile.chargingSchedule[0].startSchedule = params[14];
			}
			// 2.1 확장 — chargingProfile / chargingSchedule / chargingSchedulePeriod (JSON merge)
			_applyChargingProfile21Ext(obj.chargingProfile, params[19]);
		}

		return JSON.stringify(obj);
	}

	/**
	 * OCPP 2.1 ChargingProfile / ChargingSchedule / ChargingSchedulePeriod 확장 필드 병합.
	 * ext 구조: { "profile": {...}, "schedule": {...}, "periods": [{...}, ...] }
	 *   - profile.*  → chargingProfile 에 병합 (maxOfflineDuration, invalidAfterOfflineDuration,
	 *                  dynUpdateInterval, dynUpdateTime, priceScheduleSignature)
	 *   - schedule.* → chargingProfile.chargingSchedule[0] 에 병합 (limitAtSoC, powerTolerance,
	 *                  signatureId, digestValue, useLocalTime, randomizedDelay)
	 *   - periods[i] → chargingSchedule[0].chargingSchedulePeriod[i] 에 병합 (V2X/V2G 필드:
	 *                  operationMode, limit_L2/L3, dischargeLimit*, setpoint*, setpointReactive*,
	 *                  preconditioningRequest, evseSleep, v2xBaseline, v2xFreqWattCurve[],
	 *                  v2xSignalWattCurve[])
	 */
	function _applyChargingProfile21Ext(chargingProfile, extJson) {
		if (!extJson || extJson == '') return;
		var ext;
		try { ext = JSON.parse(extJson); } catch (e) { return; }
		if (!chargingProfile) return;
		if (ext.profile && typeof ext.profile === 'object') {
			Object.assign(chargingProfile, ext.profile);
		}
		var sched = chargingProfile.chargingSchedule && chargingProfile.chargingSchedule[0];
		if (!sched) return;
		if (ext.schedule && typeof ext.schedule === 'object') {
			Object.assign(sched, ext.schedule);
		}
		if (Array.isArray(ext.periods) && Array.isArray(sched.chargingSchedulePeriod)) {
			for (var i = 0; i < ext.periods.length && i < sched.chargingSchedulePeriod.length; ++i) {
				if (ext.periods[i] && typeof ext.periods[i] === 'object') {
					Object.assign(sched.chargingSchedulePeriod[i], ext.periods[i]);
				}
			}
		}
	}

	function _RequestStopTransaction(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { transactionId: params[0] };
		return JSON.stringify(obj);
	}

	function _ReserveNow(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = {
			expiryDateTime: params[1],
			idToken: { idToken: params[2], type: "ISO15693" },
			id: params[3]
		};
		if (params[0] && params[0] != '') {
			obj.evseId = Number(params[0]);
		}
		if (params[4] && params[4] != '') {
			obj.groupIdToken = { idToken: params[4], type: "ISO15693" };
		}
		if (params[5] && params[5] != '') {
			obj.connectorType = params[5];
		}
		// 2.1 idToken.additionalInfo (JSON 배열, optional)
		if (params[6] && params[6] != '') {
			obj.idToken.additionalInfo = JSON.parse(params[6]);
		}
		return JSON.stringify(obj);
	}

	function _SendLocalList(params) {
		let noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		let obj = {
			versionNumber: params[0],
			updateType: params[1]
		};
		let localAuthorizationList = new Array();
		for (let i = 0, length = params[2].length; i < length; ++i) {
			let localAuthorization = {
				idToken: { idToken: params[2][i], type: "ISO15693" }
			};
			if (params[3] == 'Y') {
				localAuthorization.idToken.idTokenInfo = { status: "Accepted", cacheExpiryDateTime: "9999-12-31T00:00:00Z" };
			}

			if (localAuthorization) {
				localAuthorizationList.push(localAuthorization);
			}
		}
		if (localAuthorizationList.length != 0) {
			obj.localAuthorizationList = localAuthorizationList;
		}
		return JSON.stringify(obj);
	}

	function _SetChargingProfile(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var startScheduleDt = new Date().toISOString();

		// chargingSchedulePeriod 빌드 — 신규 2.1 컬럼 sanitize
		var rawPeriods = Array.isArray(params[13]) ? params[13] : [];
		var periods = [];
		for (var i = 0; i < rawPeriods.length; i++) {
			var p = rawPeriods[i] || {};
			var period = { startPeriod: _intOrZero(p.startPeriod) };
			if (p.numberPhases && p.numberPhases !== '') period.numberPhases = parseInt(p.numberPhases, 10);
			if (p.phaseToUse && p.phaseToUse !== '') period.phaseToUse = parseInt(p.phaseToUse, 10);
			if (p.limit && p.limit !== '') period.limit = parseFloat(p.limit);
			if (p.operationMode && p.operationMode !== '') period.operationMode = p.operationMode;
			if (p.setpoint && p.setpoint !== '') period.setpoint = parseFloat(p.setpoint);
			if (p.dischargeLimit && p.dischargeLimit !== '') period.dischargeLimit = parseFloat(p.dischargeLimit);
			periods.push(period);
		}

		var schedule = {
			id: parseInt(params[14], 10),
			chargingRateUnit: params[9],
			chargingSchedulePeriod: periods
		};
		if (params[10] && params[10] != '') schedule.duration = parseInt(params[10], 10);
		if (params[12] && params[12] != '') schedule.minChargingRate = parseFloat(params[12]);
		if (params[11] && params[11] != '') schedule.startSchedule = params[11];
		// 2.1 schedule 신규
		if (params[19] && params[19] != '' && params[20] && params[20] != '') {
			schedule.limitAtSoC = { soc: parseInt(params[19], 10), limit: parseFloat(params[20]) };
		}
		if (params[21] && params[21] != '') schedule.randomizedDelay = parseInt(params[21], 10);
		if (params[22] && params[22] != '') schedule.useLocalTime = (params[22] === 'true');

		var obj = {
			evseId: Number(params[0]),
			chargingProfile: {
				id: parseInt(params[1], 10),
				stackLevel: parseInt(params[2], 10),
				chargingProfilePurpose: params[3],
				chargingProfileKind: params[4],
				chargingSchedule: [schedule]
			}
		};
		if (params[5] && params[5] != '' && params[5] != '0') {
			obj.chargingProfile.transactionId = params[5];
		}
		if (params[6] && params[6] != '') {
			obj.chargingProfile.recurrencyKind = params[6];
		}
		if (params[7] && params[7] != '') {
			obj.chargingProfile.validFrom = params[7];
		}
		if (params[8] && params[8] != '') {
			obj.chargingProfile.validTo = params[8];
		}
		// 2.1 profile 신규
		if (params[16] && params[16] != '') obj.chargingProfile.maxOfflineDuration = parseInt(params[16], 10);
		if (params[17] && params[17] != '') obj.chargingProfile.invalidAfterOfflineDuration = (params[17] === 'true');
		if (params[18] && params[18] != '') obj.chargingProfile.dynUpdateInterval = parseInt(params[18], 10);
		if (params[23] && params[23] != '') obj.chargingProfile.dynUpdateTime = params[23];

		// 2.1 ext JSON 마지막 merge (powerTolerance / salesTariff / v2xBaseline 등)
		_applyChargingProfile21Ext(obj.chargingProfile, params[15]);
		return JSON.stringify(obj);
	}

	function _intOrZero(v) {
		if (v === undefined || v === null || v === '') return 0;
		var n = parseInt(v, 10);
		return isNaN(n) ? 0 : n;
	}

	function _TriggerMessage(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { requestedMessage: params[2] };
		if (params[0] && params[0] != "") {
			obj.evse = { id: Number(params[0]) }
			if (params[1] && params[1] != "") {
				obj.evse.connectorId = Number(params[1]);
			}
		}
		if (params[3] && params[3] != "") {
			obj.customTrigger = params[3];
		}
		return JSON.stringify(obj);
	}

	function _UpdateFirmware(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = {
			requestId: parseInt(params[0]),
			firmware: {
				location: params[3],
				retrieveDateTime: params[4]
			},
		};
		if (params[1] && params[1] != '') {
			obj.retries = parseInt(params[1]);
		}
		if (params[2] && params[2] != '') {
			obj.retryInterval = parseInt(params[2]);
		}

		if (params[5] && params[5] != '') {
			obj.firmware.installDateTime = params[5];
		}
		if (params[6] && params[6] != '') {
			obj.firmware.signingCertificate = params[6];
		}
		if (params[7] && params[7] != '') {
			obj.firmware.signature = params[7];
		}
		return JSON.stringify(obj);
	}

	function _DeleteCertificate(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = {
			certificateHashData: {
				hashAlgorithm: params[0],
				issuerNameHash: params[1],
				issuerKeyHash: params[2],
				serialNumber: params[3]
			}
		};
		return JSON.stringify(obj);
	}

	function _InstallCertificate(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = { certificateType: params[0], certificate: params[1] };
		return JSON.stringify(obj);
	}

	function _CostUpdated(params) {
		var obj = {
			totalCost: params[0],
			transactionId: params[1]
		};
		return JSON.stringify(obj);
	}

	function _GetLog(params) {
		var noStr = dateUtilsJs.date2String(new Date(), 'YYYYMMDDHH24MISSFF');
		var obj = {
			logType: params[0],
			requestId: parseInt(params[1]),
			log: {
				remoteLocation: params[4]
			},
		};
		if (params[2] && params[2] != "") {
			obj.retries = parseInt(params[2]);
		}
		if (params[3] && params[3] != "") {
			obj.retryInterval = parseInt(params[3]);
		}
		if (params[5] && params[5] != "") {
			obj.log.oldestTimestamp = params[5];
		}
		if (params[6] && params[6] != "") {
			obj.log.latestTimestamp = params[6];
		}
		return JSON.stringify(obj);
	}

	function _GetBaseReport(params) {
		var obj = {
			requestId: parseInt(params[0]),
			reportBase: params[1]
		};
		return JSON.stringify(obj);
	}
	function _GetChargingProfiles(params) {
		var obj = {
			requestId: parseInt(params[0]),
			chargingProfile: {}
		};
		if (params[1] && params[1] != '') {
			obj.evseId = parseInt(params[1]);
		}
		if (params[2] && params[2] != '') {
			obj.chargingProfile.chargingProfilePurpose = params[2];
		}
		if (params[3] && params[3] != '') {
			obj.chargingProfile.stackLevel = params[3];
		}
		if (params[4] && params[4].length > 0) {
			obj.chargingProfile.chargingProfileId = params[4];
		}
		if (params[5] && params[5].length > 0) {
			obj.chargingProfile.chargingLimitSource = params[5];
		}
		return JSON.stringify(obj);
	}
	function _GetReport(params) {
		var obj = {
			requestId: parseInt(params[0])
		}

		if (params[1] && params[1] != "") {
			obj.componentCriteria = [];
			obj.componentCriteria.push(params[1]);
		}

		if (params[2] && params[2] != "") {
			obj.componentVariable = [];
			obj.componentVariable.push({
				component: {
					name: params[2],
					evse: { id: parseInt(params[3]) }
				},
				variable: {
					name: params[4]
				}
			});
		}
		return JSON.stringify(obj);
	}
	function _SetNetworkProfile(params) {
		var obj = {
			configurationSlot: parseInt(params[0]),
			connectionData: {
				ocppVersion: params[1],
				ocppTransport: params[2],
				ocppCsmsUrl: params[3],
				messageTimeout: parseInt(params[4]),
				securityProfile: params[5],
				ocppInterface: params[6]
			}
		};
		// 2.1 보안 관련 신규 필드 (optional)
		if (params[7] && params[7] != '') {
			obj.connectionData.identity = params[7];
		}
		if (params[8] && params[8] != '') {
			obj.connectionData.basicAuthPassword = params[8];
		}
		if (params[9] && params[9] != '') {
			obj.connectionData.apn = JSON.parse(params[9]);
		}
		if (params[10] && params[10] != '') {
			obj.connectionData.vpn = JSON.parse(params[10]);
		}
		return JSON.stringify(obj);
	}
	function _GetTransactionStatus(params) {
		var obj = {};
		if (params[0] != '') {
			obj.transactionId = params[0];
		}
		return JSON.stringify(obj);
	}
	function _GetMonitoringReport(params) {
		var obj = {
			requestId: parseInt(params[0])
		}

		if (params[1] && params[1].length > 0) {
			obj.monitoringCriteria = [];
			for (let i = 0, size = params[1].length; i < size; ++i) {
				obj.monitoringCriteria.push(params[1][i]);
			}
		}

		if (params[2] && params[2].length > 0) {
			obj.componentVariable = [];
			for (let i = 0, size = params[2].length; i < size; ++i) {
				let temp = {
					component: {
						name: params[2][i].cName
					},
					variable: {
						name: params[2][i].aName
					}
				};
				if (params[2][i].evseId && params[2][i].evseId != '') {
					temp.component.evse = { id: Number(params[2][i].evseId) };
				}
				obj.componentVariable.push(temp);
			}
		}
		return JSON.stringify(obj);
	}
	function _SetMonitoringBase(params) {
		var obj = {
			monitoringBase: params[0],
		};
		return JSON.stringify(obj);
	}
	function _SetVariableMonitoring(params) {
		var obj = {
			setMonitoringData: params[0],
		};
		return JSON.stringify(obj);
	}
	function _SetMonitoringLevel(params) {
		var obj = {
			severity: params[0]
		};
		return JSON.stringify(obj);
	}
	function _GetDERControl(params) {
		var obj = {
			requestId: parseInt(params[0])
		};
		if (params[1]) {
			obj.isDefault = Boolean(params[1] == "true");
		}
		if (params[2]) {
			obj.controlType = params[2];
		}
		if (params[3]) {
			obj.controlId = params[3];
		}
		return JSON.stringify(obj);
	}
	function _SetDERControl(params) {
		var obj = {
			isDefault: Boolean(params[0] == "true")
		};
		if (params[1]) {
			obj.controlId = params[1];
		}
		if (params[2]) {
			obj.controlType = params[2];
		}
		// 2.1 sub-object JSON 병합.
		//   {"freqDroop":{...}} 형식 → 그대로 merge
		//   {"priority":6,...}   형식 → controlType 으로 key 추정해 wrap
		if (params[3] && params[3] != '') {
			try {
				var sub = JSON.parse(params[3]);
				if (_derContainsAnySubKey(sub)) {
					for (var k in sub) {
						if (sub.hasOwnProperty(k)) obj[k] = sub[k];
					}
				} else {
					var key = _derDeriveSubKey(params[2]);
					obj[key] = sub;
				}
			} catch (e) {
				throw new Error('SetDERControl sub-object JSON 파싱 실패: ' + e.message);
			}
		}
		return JSON.stringify(obj);
	}

	function _derContainsAnySubKey(o) {
		if (!o || typeof o !== 'object') return false;
		return ('enterService' in o) || ('freqDroop' in o)
			|| ('fixedPFAbsorb' in o) || ('fixedPFInject' in o)
			|| ('fixedVar' in o) || ('gradient' in o)
			|| ('limitMaxDischarge' in o) || ('curve' in o);
	}
	function _derDeriveSubKey(controlType) {
		switch (controlType) {
			case 'EnterService': return 'enterService';
			case 'FreqDroop': return 'freqDroop';
			case 'FixedPFAbsorb': return 'fixedPFAbsorb';
			case 'FixedPFInject': return 'fixedPFInject';
			case 'FixedVar': return 'fixedVar';
			case 'Gradients': return 'gradient';
			case 'LimitMaxDischarge': return 'limitMaxDischarge';
			default: return 'curve';
		}
	}
	function _ClearDERControl(params) {
		var obj = {
			isDefault: Boolean(params[0] == "true")
		};
		if (params[1]) {
			obj.controlType = params[1];
		}
		if (params[2]) {
			obj.controlId = params[2];
		}
		return JSON.stringify(obj);
	}
	function _ClearVariableMonitoring(params) {
		var ids = [];
		if (Array.isArray(params[0])) {
			for (var i = 0; i < params[0].length; ++i) {
				var n = parseInt(params[0][i], 10);
				if (!isNaN(n)) ids.push(n);
			}
		}
		var obj = { id: ids };
		return JSON.stringify(obj);
	}
	function _CustomerInformation(params) {
		var obj = {
			requestId: parseInt(params[0]),
			report: params[1],
			clear: params[2]
		};

		if (params[4] && params[4] != '') {
			obj.customerIdentifier = params[4]
		}
		if (params[5] && params[5] != '') {
			obj.customerCertificate = {
				hashAlgorithm: "SHA256",
				issuerNameHash: "5D9AA3B240912700F8621901B1FD95C73E2BDBAA00CCE071ADE233CD74A21DDC",
				issuerKeyHash: "41D1DE817DDFA5EDD6D2AC64F98C70D25EC7B3842034D73F5E76A06839D0866A",
				serialNumber: "588D1A56DC2DAC7CAEB2089BAAF1EBE5",
			}
		}
		if (params[3] && params[3] != '') {
			obj.idToken = {
				idToken: params[3],
				type: "ISO15693"
			}
		}
		return JSON.stringify(obj);
	}
	function _SetDisplayMessage(params) {
		var obj = {
			message: {
				id: Number(params[0]),
				priority: params[1],
				message: {
					format: params[2],
					content: params[3]
				}
			}
		};
		if (params[13] && params[13] != '') {
			obj.message.message.language = params[13];
		}
		if (params[4] && params[4] != '') {
			obj.message.state = params[4];
		}
		if (params[5] && params[5] != '') {
			obj.message.startDateTime = params[5];
		}
		if (params[6] && params[6] != '') {
			obj.message.endDateTime = params[6];
		}
		if (params[7] && params[7] != '') {
			obj.message.transactionId = params[7];
		}
		if (params[8] && params[8] != '') {
			obj.message.display = {};
			obj.message.display.name = params[8];
			if (params[9] && params[9] != '') {
				obj.message.display.instance = params[9];
			}
			if (params[10] && params[10] != '' && params[11] && params[11] != '') {
				obj.message.display.evse = {};
			}
			if (params[10] && params[10] != '') {
				obj.message.display.evse.id = params[10];
			}
			if (params[11] && params[11] != '') {
				obj.message.display.evse.connectorId = params[11];
			}
		}
		// 2.1 message.messageExtra (JSON 배열, optional, 최대 4개)
		if (params[12] && params[12] != '') {
			obj.message.messageExtra = JSON.parse(params[12]);
		}
		return JSON.stringify(obj);
	}
	function _GetDisplayMessages(params) {
		var obj = {
			requestId: parseInt(params[0], 10)
		};
		if (params[1] && params[1] != '') {
			obj.priority = params[1];
		}
		if (params[2] && params[2] != '') {
			obj.state = params[2];
		}
		if (params[3] && params[3] != '') {
			obj.id = [];
			let ids = params[3].split(",");
			for (let i = 0, size = ids.length; i < size; ++i) {
				let n = parseInt(ids[i].trim(), 10);
				if (!isNaN(n)) obj.id.push(n);
			}
		}
		return JSON.stringify(obj);
	}
	function _ClearDisplayMessage(params) {
		var obj = {
			id: params[0]
		};
		return JSON.stringify(obj);
	}



	// ── OCPP 2.1 신규 CSMS→CS 원격제어 ───────────────────────────────────────

	function _RequestBatterySwap(params) {
		var obj = {
			requestId: parseInt(params[0], 10),
			idToken: { idToken: params[1], type: params[2] }
		};
		return JSON.stringify(obj);
	}

	function _UsePriorityCharging(params) {
		var obj = {
			transactionId: params[0],
			activate: (params[1] === true || params[1] === 'true')
		};
		return JSON.stringify(obj);
	}

	function _NotifyAllowedEnergyTransfer(params) {
		var modes = [];
		if (params[1] && params[1] != '') {
			var raw = params[1].split(',');
			for (var i = 0; i < raw.length; ++i) {
				var v = raw[i].trim();
				if (v) modes.push(v);
			}
		}
		var obj = {
			transactionId: params[0],
			allowedEnergyTransfer: modes
		};
		return JSON.stringify(obj);
	}

	function _AFRRSignal(params) {
		var obj = {
			timestamp: params[0],
			signal: parseInt(params[1], 10)
		};
		return JSON.stringify(obj);
	}

	function _GetTariffs(params) {
		var obj = { evseId: parseInt(params[0], 10) };
		return JSON.stringify(obj);
	}

	function _ClearTariffs(params) {
		var obj = {};
		if (params[0] && params[0] != '') {
			var raw = params[0].split(',');
			var ids = [];
			for (var i = 0; i < raw.length; ++i) {
				var v = raw[i].trim();
				if (v) ids.push(v);
			}
			if (ids.length > 0) obj.tariffIds = ids;
		}
		if (params[1] && params[1] != '') {
			obj.evseId = parseInt(params[1], 10);
		}
		return JSON.stringify(obj);
	}

	function _SetDefaultTariff(params) {
		var obj = {
			evseId: parseInt(params[0], 10),
			tariff: JSON.parse(params[1])
		};
		return JSON.stringify(obj);
	}

	function _ChangeTransactionTariff(params) {
		var obj = {
			transactionId: params[0],
			tariff: JSON.parse(params[1])
		};
		return JSON.stringify(obj);
	}

	function _UpdateDynamicSchedule(params) {
		var obj = {
			chargingProfileId: parseInt(params[0], 10),
			scheduleUpdate: JSON.parse(params[1])
		};
		return JSON.stringify(obj);
	}

	function _GetCertificateChainStatus(params) {
		var obj = {
			certificateStatusRequests: JSON.parse(params[0])
		};
		return JSON.stringify(obj);
	}

	function _AdjustPeriodicEventStream(params) {
		var inner = {};
		if (params[1] && params[1] != '') inner.interval = parseInt(params[1], 10);
		if (params[2] && params[2] != '') inner.values = parseInt(params[2], 10);
		var obj = {
			id: parseInt(params[0], 10),
			params: inner
		};
		return JSON.stringify(obj);
	}

	function _GetPeriodicEventStream(params) {
		return JSON.stringify({});
	}

	function _makeParam(type, params) {
		switch (type) {
			case 'Reset':
				return _Reset(params);
			case 'UnlockConnector':
				return _UnlockConnector(params);
			case 'SetVariables':
				return _SetVariables(params);
			case 'CancelReservation':
				return _CancelReservation(params);
			case 'ChangeAvailability':
				return _ChangeAvailability(params);
			case 'ClearCache':
				return _ClearCache(params);
			case 'ClearChargingProfile':
				return _ClearChargingProfile(params);
			case 'DataTransfer':
				return _DataTransfer(params);
			case 'GetCompositeSchedule':
				return _GetCompositeSchedule(params);
			case 'GetVariables':
				return _GetVariables(params);
			case 'GetDiagnostics':
				return _GetDiagnostics(params);
			case 'GetLocalListVersion':
				return _GetLocalListVersion(params);
			case 'RequestStartTransaction':
				return _RequestStartTransaction(params);
			case 'RequestStopTransaction':
				return _RequestStopTransaction(params);
			case 'ReserveNow':
				return _ReserveNow(params);
			case 'SendLocalList':
				return _SendLocalList(params);
			case 'SetChargingProfile':
				return _SetChargingProfile(params);
			case 'TriggerMessage':
				return _TriggerMessage(params);
			case 'UpdateFirmware':
				return _UpdateFirmware(params);
			case 'CertificateSigned':
				return _CertificateSigned(params);
			case 'GetInstalledCertificateIds':
				return _GetInstalledCertificateIds(params);
			case 'DeleteCertificate':
				return _DeleteCertificate(params);
			case 'InstallCertificate':
				return _InstallCertificate(params);
			case 'CostUpdated':
				return _CostUpdated(params);
			case 'GetLog':
				return _GetLog(params);

			case 'GetBaseReport':
				return _GetBaseReport(params);
			case 'GetChargingProfiles':
				return _GetChargingProfiles(params);
			case 'GetReport':
				return _GetReport(params);
			case 'SetNetworkProfile':
				return _SetNetworkProfile(params);
			case 'GetTransactionStatus':
				return _GetTransactionStatus(params);
			case 'GetMonitoringReport':
				return _GetMonitoringReport(params);
			case 'SetMonitoringBase':
				return _SetMonitoringBase(params);
			case 'SetVariableMonitoring':
				return _SetVariableMonitoring(params);
			case 'SetMonitoringLevel':
				return _SetMonitoringLevel(params);
			case 'ClearVariableMonitoring':
				return _ClearVariableMonitoring(params);
			case 'CustomerInformation':
				return _CustomerInformation(params);
			case 'SetDisplayMessage':
				return _SetDisplayMessage(params);
			case 'GetDisplayMessages':
				return _GetDisplayMessages(params);
			case 'ClearDisplayMessage':
				return _ClearDisplayMessage(params);
			case 'UnpublishFirmware':
				return _UnpublishFirmware(params);
			case 'PublishFirmware':
				return _PublishFirmware(params)
			case 'GetDERControl':
				return _GetDERControl(params)
			case 'SetDERControl':
				return _SetDERControl(params)
			case 'ClearDERControl':
				return _ClearDERControl(params)
			// 2.1 신규
			case 'RequestBatterySwap':
				return _RequestBatterySwap(params);
			case 'UsePriorityCharging':
				return _UsePriorityCharging(params);
			case 'NotifyAllowedEnergyTransfer':
				return _NotifyAllowedEnergyTransfer(params);
			case 'AFRRSignal':
				return _AFRRSignal(params);
			case 'GetTariffs':
				return _GetTariffs(params);
			case 'ClearTariffs':
				return _ClearTariffs(params);
			case 'SetDefaultTariff':
				return _SetDefaultTariff(params);
			case 'ChangeTransactionTariff':
				return _ChangeTransactionTariff(params);
			case 'UpdateDynamicSchedule':
				return _UpdateDynamicSchedule(params);
			case 'GetCertificateChainStatus':
				return _GetCertificateChainStatus(params);
			case 'AdjustPeriodicEventStream':
				return _AdjustPeriodicEventStream(params);
			case 'GetPeriodicEventStream':
				return _GetPeriodicEventStream(params);
		}
	}

	function _type() {
		var types = [
			'CancelReservation',
			'CertificateSigned',
			'ChangeAvailability',
			'ClearCache',
			'ClearChargingProfile',
			'ClearDisplayMessage',
			'ClearVariableMonitoring',
			'CostUpdated',
			'CustomerInformation',
			'DataTransfer',
			'DeleteCertificate',
			'InstallCertificate',
			'GetBaseReport',
			'GetChargingProfiles',
			'GetCompositeSchedule',
			'GetDiagnostics',
			'GetDisplayMessages',
			'GetInstalledCertificateIds',
			'GetLocalListVersion',
			'GetLog',
			'GetMonitoringReport',
			'GetReport',
			'GetTransactionStatus',
			'GetVariables',
			'PublishFirmware',
			'RequestStartTransaction',
			'RequestStopTransaction',
			'ReserveNow',
			'SendLocalList',
			'SetChargingProfile',
			'SetDisplayMessage',
			'SetMonitoringBase',
			'SetMonitoringLevel',
			'SetNetworkProfile',
			'SetVariableMonitoring',
			'SetVariables',
			'TriggerMessage',
			'UnlockConnector',
			'UpdateFirmware',
			'UnpublishFirmware',
			'GetDERControl',
			'ClearDERControl',
			'SetDERControl',
			'Reset',
			// 2.1 신규 CSMS→CS 원격제어
			'RequestBatterySwap',
			'UsePriorityCharging',
			'NotifyAllowedEnergyTransfer',
			'AFRRSignal',
			'GetTariffs',
			'ClearTariffs',
			'SetDefaultTariff',
			'ChangeTransactionTariff',
			'UpdateDynamicSchedule',
			'GetCertificateChainStatus',
			'AdjustPeriodicEventStream',
			'GetPeriodicEventStream'
		];
		// 명령어명 알파벳 오름차순 정렬 (대소문자 무시)
		types.sort(function (a, b) {
			return a.toLowerCase().localeCompare(b.toLowerCase());
		});
		return types;
	}

	return {
		makeParam: _makeParam,
		type: _type
	};
}();
