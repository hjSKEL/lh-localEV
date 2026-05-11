var ocpp20VarJs = function(){
	//	
	var data = {
			ActiveNetworkProfile : 
			{componentName:"OCPPCommCtrlr", variableName:"ActiveNetworkProfile", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			AllowNewSessionsPendingFirmwareUpdate:
			{componentName:"ChargingStation", variableName:"AllowNewSessionsPendingFirmwareUpdate", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			DefaultMessageTimeout:
			{componentName:"OCPPCommCtrlr", variableName:"MessageTimeout", variableInstance:"Default", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer",unit:"seconds"}},
			FileTransferProtocols:
			{componentName:"OCPPCommCtrlr", variableName:"FileTransferProtocols", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"MemberList", valuesList:"FTP,FTPS,HTTP,HTTPS,SFTP"}},
			HeartbeatInterval:
			{componentName:"OCPPCommCtrlr", variableName:"HeartbeatInterval", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer",unit:"seconds"}},
			NetworkConfigurationPriority:
			{componentName:"OCPPCommCtrlr", variableName:"NetworkConfigurationPriority", variableAttributes:{mutability:"ReadWrite",attributeType:"Actual"}, variableCharacteristics:{dataType:"SequenceList",valueList:"List of possible values"}},
			NetworkProfileConnectionAttempts:
			{componentName:"OCPPCommCtrlr", variableName:"NetworkProfileConnectionAttempts", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer"}},
			OfflineThreshold:
			{componentName:"OCPPCommCtrlr", variableName:"OfflineThreshold", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer",unit:"seconds"}},
			QueueAllMessages:
			{componentName:"OCPPCommCtrlr", variableName:"QueueAllMessages", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			MessageAttemptsTransactionEvent:
			{componentName:"OCPPCommCtrlr", variableName:"MessageAttempts", variableInstance:"TransactionEvent", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer"}},
			MessageAttemptIntervalTransactionEvent:
			{componentName:"OCPPCommCtrlr", variableName:"MessageAttemptInterval", variableInstance:"TransactionEvent", variableAttributes:{mutability:"ReadWrite",attributeType:"Actual"}, variableCharacteristics:{dataType:"integer",unit:"seconds"}},
			UnlockOnEVSideDisconnect:
			{componentName:"OCPPCommCtrlr", variableName:"UnlockOnEVSideDisconnect", evse:"*", variableAttributes:{mutability:"ReadWrite/ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			WebSocketPingInterval:
			{componentName:"OCPPCommCtrlr", variableName:"WebSocketPingInterval", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer",unit:"seconds"}},
			ResetRetries:
			{componentName:"OCPPCommCtrlr", variableName:"ResetRetries", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer"}},
			ItemsPerMessageGetReport:
			{componentName:"DeviceDataCtrlr", variableName:"ItemsPerMessage", variableInstance:"GetReport", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			ItemsPerMessageGetVariables:
			{componentName:"DeviceDataCtrlr", variableName:"ItemsPerMessage", variableInstance:"GetVariables", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			BytesPerMessageGetReport:
			{componentName:"DeviceDataCtrlr", variableName:"BytesPerMessage", variableInstance:"GetReport", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			BytesPerMessageGetVariables:
			{componentName:"DeviceDataCtrlr", variableName:"BytesPerMessage", variableInstance:"GetVariables", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			ConfigurationValueSize:
			{componentName:"DeviceDataCtrlr", variableName:"ConfigurationValueSize", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer",maxLimit:1000}},
			ReportingValueSize:
			{componentName:"DeviceDataCtrlr", variableName:"ReportingValueSize", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer",maxLimit:2500}},
			ItemsPerMessageSetVariables:
			{componentName:"DeviceDataCtrlr", variableName:"ItemsPerMessage", variableInstance:"SetVariables", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			BytesPerMessageSetVariables:
			{componentName:"DeviceDataCtrlr", variableName:"BytesPerMessage", variableInstance:"SetVariables", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			DateTime:
			{componentName:"ClockCtrlr", variableName:"DateTime", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"DateTime"}},
			NtpSource:
			{componentName:"ClockCtrlr", variableName:"NtpSource", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"OptionList", valuesList:"DHCP,manual"}},
			NtpServerUri:
			{componentName:"ClockCtrlr", variableName:"NtpServerUri", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"string"}},
			TimeOffset:
			{componentName:"ClockCtrlr", variableName:"TimeOffset", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"string"}},
			NextTimeOffsetTransitionDateTime:
			{componentName:"ClockCtrlr", variableName:"NextTimeOffsetTransitionDateTime", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"DateTime"}},
			TimeOffsetNextTransition:
			{componentName:"ClockCtrlr", variableName:"TimeOffset", variableInstance:"NextTransition", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"string"}},
			TimeSource:
			{componentName:"ClockCtrlr", variableName:"TimeSource", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"SequenceList",valuesList:"Heartbeat,NTP,GPS,RealTimeClock,MobileNetwork,RadioTimeTransmitter"}},
			TimeZone:
			{componentName:"ClockCtrlr", variableName:"TimeZone", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"string"}},
			BasicAuthPassword:
			{componentName:"SecurityCtrlr", variableName:"BasicAuthPassword", variableAttributes:{mutability:"WriteOnly"}, variableCharacteristics:{dataType:"identifierString",maxLimit:40}},
			Identity:
			{componentName:"SecurityCtrlr", variableName:"Identity", variableAttributes:{mutability:"ReadOnly/ReadWrite"}, variableCharacteristics:{dataType:"identifierString",maxLimit:40}},
			OrganizationName:
			{componentName:"SecurityCtrlr", variableName:"OrganizationName", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"string"}},
			CertificateEntries:
			{componentName:"SecurityCtrlr", variableName:"CertificateEntries", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer",maxLimit:10}},
			SecurityProfile:
			{componentName:"SecurityCtrlr", variableName:"SecurityProfile", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			AdditionalRootCertificateCheck:
			{componentName:"SecurityCtrlr", variableName:"AdditionalRootCertificateCheck", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			MaxCertificateChainSize:
			{componentName:"SecurityCtrlr", variableName:"MaxCertificateChainSize", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer",maxLimit:10000}},
			AuthEnabled:
			{componentName:"AuthCtrlr", variableName:"Enabled", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			AdditionalInfoItemsPerMessage:
			{componentName:"AuthCtrlr", variableName:"AdditionalInfoItemsPerMessage", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			OfflineTxForUnknownIdEnabled:
			{componentName:"AuthCtrlr", variableName:"OfflineTxForUnknownIdEnabled", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			AuthorizeRemoteStart:
			{componentName:"AuthCtrlr", variableName:"AuthorizeRemoteStart", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			LocalAuthorizeOffline:
			{componentName:"AuthCtrlr", variableName:"LocalAuthorizeOffline", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			LocalPreAuthorize:
			{componentName:"AuthCtrlr", variableName:"LocalPreAuthorize", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			MasterPassGroupId:
			{componentName:"AuthCtrlr", variableName:"MasterPassGroupId", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"string",maxLimit:36}},
			AuthCacheEnabled:
			{componentName:"AuthCacheCtrlr", variableName:"Enabled", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			AuthCacheAvailable:
			{componentName:"AuthCacheCtrlr", variableName:"Available", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			AuthCacheLifeTime:
			{componentName:"AuthCacheCtrlr", variableName:"LifeTime", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer",unit:"seconds"}},
			AuthCacheStorage:
			{componentName:"AuthCacheCtrlr", variableName:"Storage", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer",maxLimit:50}},
			AuthCachePolicy:
			{componentName:"AuthCacheCtrlr", variableName:"Policy", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"OptionList",valuesList:"LRU,LFU,FIFO,CUSTOM"}},
			LocalAuthListEnabled:
			{componentName:"LocalAuthListCtrlr", variableName:"Enabled", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			LocalAuthListEntries:
			{componentName:"LocalAuthListCtrlr", variableName:"Entries", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer",maxLimit:30}},
			LocalAuthListAvailable:
			{componentName:"LocalAuthListCtrlr", variableName:"Available", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			ItemsPerMessageSendLocalList:
			{componentName:"LocalAuthListCtrlr", variableName:"ItemsPerMessage", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			BytesPerMessageSendLocalList:
			{componentName:"LocalAuthListCtrlr", variableName:"BytesPerMessage", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			LocalAuthListStorage:
			{componentName:"LocalAuthListCtrlr", variableName:"Storage", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer",maxLimit:100}},
			EVConnectionTimeOut:
			{componentName:"TxCtrlr", variableName:"EVConnectionTimeOut", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer",unit:"seconds"}},
			StopTxOnEVSideDisconnect:
			{componentName:"TxCtrlr", variableName:"StopTxOnEVSideDisconnect", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			TxBeforeAcceptedEnabled:
			{componentName:"TxCtrlr", variableName:"TxBeforeAcceptedEnabled", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			TxStartPoint:
			{componentName:"TxCtrlr", variableName:"TxStartPoint", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"MemberList",valueList:"ParkingBayOccupancy,EVConnected,Authorized,DataSigned,PowerPathClosed,EnergyTransfer"}},
			TxStopPoint:
			{componentName:"TxCtrlr", variableName:"TxStopPoint", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"MemberList",valueList:"ParkingBayOccupancy,EVConnected,Authorized,DataSigned,PowerPathClosed,EnergyTransfer"}},
			MaxEnergyOnInvalidId:
			{componentName:"TxCtrlr", variableName:"MaxEnergyOnInvalidId", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer",unit:"Wh"}},
			StopTxOnInvalidId:
			{componentName:"TxCtrlr", variableName:"StopTxOnInvalidId", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			SampledDataEnabled:
			{componentName:"SampledDataCtrlr", variableName:"Enabled", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			SampledDataAvailable:
			{componentName:"SampledDataCtrlr", variableName:"Available", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			SampledDataSignReadings:
			{componentName:"SampledDataCtrlr", variableName:"SignReadings", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			SampledDataTxEndedMeasurands:
			{componentName:"SampledDataCtrlr", variableName:"TxEndedMeasurands", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"MemberList",maxLimit:10}},
			SampledDataTxEndedInterval:
			{componentName:"SampledDataCtrlr", variableName:"TxEndedInterval", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer",unit:"seconds"}},
			SampledDataTxStartedMeasurands:
			{componentName:"SampledDataCtrlr", variableName:"TxStartedMeasurands", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"MemberList",maxLimit:10}},
			SampledDataTxUpdatedMeasurands:
			{componentName:"SampledDataCtrlr", variableName:"TxUpdatedMeasurands", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"MemberList",maxLimit:10}},
			SampledDataTxUpdatedInterval:
			{componentName:"SampledDataCtrlr", variableName:"TxUpdatedInterval", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer",unit:"seconds"}},
			SampledDataRegisterValuesWithoutPhases:
			{componentName:"SampledDataCtrlr", variableName:"RegisterValuesWithoutPhases", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			AlignedDataEnabled:
			{componentName:"AlignedDataCtrlr", variableName:"Enabled", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			AlignedDataAvailable:
			{componentName:"AlignedDataCtrlr", variableName:"Available", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			AlignedDataMeasurands:
			{componentName:"AlignedDataCtrlr", variableName:"Measurands", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"MemberList",maxLimit:10}},
			AlignedDataInterval:
			{componentName:"AlignedDataCtrlr", variableName:"Interval", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer",unit:"seconds"}},
			AlignedDataSendDuringIdle:
			{componentName:"AlignedDataCtrlr", variableName:"SendDuringIdle", evse:"*", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			AlignedDataSignReadings:
			{componentName:"AlignedDataCtrlr", variableName:"SignReadings", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			AlignedDataTxEndedMeasurands:
			{componentName:"AlignedDataCtrlr", variableName:"TxEndedMeasurands", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"MemberList",maxLimit:10}},
			AlignedDataTxEndedInterval:
			{componentName:"AlignedDataCtrlr", variableName:"TxEndedInterval", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer",unit:"seconds"}},
			PublicKeyWithSignedMeterValue:
			{componentName:"OCPPCommCtrlr", variableName:"PublicKeyWithSignedMeterValue", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"OptionList",valueList:"Never,OncePerTransaction,EveryMeterValue"}},
			ReservationEnabled:
			{componentName:"ReservationCtrlr", variableName:"Enabled", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			ReservationAvailable:
			{componentName:"ReservationCtrlr", variableName:"Available", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			ReservationNonEvseSpecific:
			{componentName:"ReservationCtrlr", variableName:"NonEvseSpecific", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			SmartChargingEnabled:
			{componentName:"SmartChargingCtrlr", variableName:"Enabled", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			SmartChargingAvailable:
			{componentName:"SmartChargingCtrlr", variableName:"Available", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			ACPhaseSwitchingSupported:
			{componentName:"SmartChargingCtrlr", variableName:"ACPhaseSwitchingSupported", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			ChargingProfileMaxStackLevel:
			{componentName:"SmartChargingCtrlr", variableName:"ProfileStackLevel", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			ChargingScheduleChargingRateUnit:
			{componentName:"SmartChargingCtrlr", variableName:"RateUnit", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"MemberList",valueList:"A,W"}},
			PeriodsPerSchedule:
			{componentName:"SmartChargingCtrlr", variableName:"PeriodsPerSchedule", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			ExternalControlSignalsEnabled:
			{componentName:"SmartChargingCtrlr", variableName:"ExternalControlSignalsEnabled", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			NotifyChargingLimitWithSchedules:
			{componentName:"SmartChargingCtrlr", variableName:"NotifyChargingLimitWithSchedules", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			Phases3to1:
			{componentName:"SmartChargingCtrlr", variableName:"Phases3to1", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			ChargingProfileEntries:
			{componentName:"SmartChargingCtrlr", variableName:"Entries", variableInstance:"ChargingProfiles", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer",maxLimit:10}},
			LimitChangeSignificance:
			{componentName:"SmartChargingCtrlr", variableName:"LimitChangeSignificance", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"decimal"}},
			TariffEnabled:
			{componentName:"TariffCostCtrlr", variableName:"Enabled", variableInstance:"Tariff", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			TariffAvailable:
			{componentName:"TariffCostCtrlr", variableName:"Available", variableInstance:"Tariff", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			TariffFallbackMessage:
			{componentName:"TariffCostCtrlr", variableName:"TariffFallbackMessage", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"string",maxLimit:255}},
			CostEnabled:
			{componentName:"TariffCostCtrlr", variableName:"Enabled", variableInstance:"Cost", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			CostAvailable:
			{componentName:"TariffCostCtrlr", variableName:"Available", variableInstance:"Cost", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			TotalCostFallbackMessage:
			{componentName:"TariffCostCtrlr", variableName:"TotalCostFallbackMessage", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"string",maxLimit:255}},
			Currency:
			{componentName:"TariffCostCtrlr", variableName:"Currency", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"string",maxLimit:3}},
			MonitoringEnabled:
			{componentName:"MonitoringCtrlr", variableName:"Enabled", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			MonitoringAvailable:
			{componentName:"MonitoringCtrlr", variableName:"Available", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			ItemsPerMessageClearVariableMonitoring:
			{componentName:"MonitoringCtrlr", variableName:"ItemsPerMessage", variableInstance:"ClearVariableMonitoring", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			ItemsPerMessageSetVariableMonitoring:
			{componentName:"MonitoringCtrlr", variableName:"ItemsPerMessage", variableInstance:"SetVariableMonitoring", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			BytesPerMessageClearVariableMonitoring:
			{componentName:"MonitoringCtrlr", variableName:"BytesPerMessage", variableInstance:"ClearVariableMonitoring", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			BytesPerMessageSetVariableMonitoring:
			{componentName:"MonitoringCtrlr", variableName:"BytesPerMessage", variableInstance:"SetVariableMonitoring", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			OfflineMonitoringEventQueuingSeverity:
			{componentName:"MonitoringCtrlr", variableName:"OfflineQueuingSeverity", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"integer"}},
			DisplayMessageEnabled:
			{componentName:"DisplayMessageCtrlr", variableName:"Enabled", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			DisplayMessageAvailable:
			{componentName:"DisplayMessageCtrlr", variableName:"Available", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			NumberOfDisplayMessages:
			{componentName:"DisplayMessageCtrlr", variableName:"DisplayMessages", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer",maxLimit:10}},
			DisplayMessageSupportedFormats:
			{componentName:"DisplayMessageCtrlr", variableName:"SupportedFormats", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"MemberList",valueList:"ASCII,HTML,URI,UTF8"}},
			DisplayMessageSupportedPriorities:
			{componentName:"DisplayMessageCtrlr", variableName:"SupportedPriorities", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"MemberList",valueList:"AlwaysFront,InFront,NormalCycle"}},
			CustomImplementationEnabled:
			{componentName:"CustomizationCtrlr", variableName:"CustomImplementationEnabled", variableInstance:"KEVIT", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			Available:
			{componentName:"ChargingStation", evse:"*", variableName:"Available", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			AvailabilityState:
			{componentName:"ChargingStation", evse:"*", variableName:"AvailabilityState", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"optionList",valuesList:"Available,Occupied,Reserved,Unavailable,Faulted"}},
			AllowReset:
			{componentName:"EVSE", evse:"*", variableName:"AllowReset", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"boolean"}},
			ConnectorType:
			{componentName:"Connector", evse:"*", variableName:"ConnectorType", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"string"}},
			SupplyPhases:
			{componentName:"ChargingStation", evse:"*", variableName:"SupplyPhases", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"integer"}},
			Power:
			{componentName:"EVSE", evse:"*", variableName:"Power", variableAttributes:{mutability:"ReadOnly"}, variableCharacteristics:{dataType:"decimal"}},
			CentralContractValidationAllowed:
			{componentName:"ISO15118Ctrlr", variableName:"CentralContractValidationAllowed", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}},
			ContractValidationOffline:
			{componentName:"ISO15118Ctrlr", variableName:"ContractValidationOffline", variableAttributes:{mutability:"ReadWrite"}, variableCharacteristics:{dataType:"boolean"}}
		}
	
	function _getVariable(key){
		return data[key];
	}
	
	function _getAllVariableKey(){
		let result = [];
		for (var key in data) { 
			result.push(key); 
		}
		return result;
	}
	
	return {
		getVariable:_getVariable,
		getAllVariableKey:_getAllVariableKey
	};
}();
