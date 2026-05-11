var ocppTestJs = function() {
	"use strict";
	
	let data = {}
	
	function _connect(addr){
		//
		data.ws = new WebSocket(addr, ["ocpp1.6"]);
		data.ws.onerror = _onerror;
		data.ws.onclose = _onclose;
		console.log("OPEN : " + new Date());
	}
	
	function _onerror(event) {
		console.log(event.data);
	};
	
	function _onclose(event) {
		console.log(event.data);
		console.log("CLOSE : " + new Date())
	};
	
	function _sendMessage(msg){
		//console.log(msg);
		data.ws.send(msg);
	};
	
	function _close(){
		data.ws.close();
	}
	
	return {
		connect : _connect,
		sendMessage : _sendMessage,
		close : _close
	};
}();
/*

ocppTestJs.connect("ws://10.132.142.228:38081/ocpp/endpoint/IA0001-01");

ocppTestJs.sendMessage(JSON.stringify([]));


JSON.stringify([2,"90c776cb-a2df-4148-876d-41a7f5b76da8","BootNotification",{"chargeBoxSerialNumber":"IA0001-01","chargePointModel":"FC50K","chargePointSerialNumber":"IA0001-01","chargePointVendor":"KEVIT","firmwareVersion":"3.19AZ"}])

################################
##### CS  ->   CSMS
################################
@StatusNotification
-Available
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Available","vendorId":"kr.co.kevit","timestamp":"2024-08-08T04:06:40Z"}]
-Charging
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Charging","vendorId":"kr.co.kevit","timestamp":"2024-08-08T02:16:00Z"}]
-Finishing
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Finishing","vendorId":"kr.co.kevit","timestamp":"2024-08-08T05:47:00Z"}]
-Preparing
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Preparing","vendorId":"kr.co.kevit","timestamp":"2024-08-08T10:11:20Z"}]
-Unavailable
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Unavailable","vendorId":"kr.co.kevit","timestamp":"2024-08-08T08:01:00Z"}]
-Reserved
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Reserved","vendorId":"kr.co.kevit","timestamp":"2024-08-08T08:01:00Z"}]
-Faulted
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Faulted","vendorId":"kr.co.kevit","vendorErrorCode":"120","timestamp":"2024-08-08T08:01:00Z"}]





############ 부팅시

@BootNotification
[2,"90c776cb-a2df-4148-876d-41a7f5b76da8","BootNotification",{"chargeBoxSerialNumber":"ME18B296-11","chargePointModel":"FC50K","chargePointSerialNumber":"M000000101","chargePointVendor":"KEVIT","firmwareVersion":"3.19AZ"}]

@StatusNotification
-Available
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Available","vendorId":"kr.co.kevit","timestamp":"2024-08-08T01:38:40Z"}]

@DataTransfer-Status
#[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"response_date\":\"20240808114100\",\"ui_ver\":\"VER12S_1.17\",\"charger_status\":\"2\",\"rf_status\":\"1\",\"ic_status\":\"2\",\"app_start_date\":\"20240808114100\",\"stop_button_status\":\"2\",\"charging_mode\":\"2\",\"electricity_meter_mode\":\"2\",\"ui_mode\":\"1\",\"update_file_count\":\"0\",\"power_module\":\"0000000000000000\",\"free_space\":\"231472\",\"ava_mem\":\"192659456\",\"timelimit_yn\":\"Y\",\"timelimit_value\":\"40\",\"test_yn\":\"Y\",\"pay_yn\":\"Y\",\"volume_day\":\"3\",\"volume_night\":\"3\",\"volumemovie_day\":\"3\",\"volumemovie_night\":\"3\",\"charger_firmware\":\"\",\"notice_cnt\":\"1\",\"system_date\":\"20240808114100\",\"lcd_ip\":\"192.168.0.2\", \"current_unit_cost\":\"405.5\"}"}]
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808114100\",\"create_date\":\"20240808114100\",\"charger_status\":\"1\",\"mode\":\"1\",\"charger_door\":\"0\",\"integrated_power\":\"30000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"1\"}"}]


########### 회원
## TEST : 1010010122401111
#  1010000000000000
#  1010010189921562
#  bckim : 1010010250130689
@Authorize
[2,"104e04f59b0a422c9750d1cd61454cb6","Authorize",{"idTag":"1010010189921562"}]

@DataTransfer-Tariff
[2,"20240617182522","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Tariff","data":"{\"timestamp\":\"2024-08-08T04:09:00Z\",\"idTag\":\"1010010189921562\",\"pointCheck\":\"Y\",\"connector_id\":\"1\"}"}]

-Preparing
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Preparing","vendorId":"kr.co.kevit","timestamp":"2024-08-08T04:10:20Z"}]

@StartTransaction
[2,"104e04f59b0a422c9750d1cd61454cb6","StartTransaction",{"connectorId":1,"idTag":"1010010189921562","meterStart":28900,"timestamp":"2024-08-08T04:15:10Z"}]

-Charging
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Charging","vendorId":"kr.co.kevit","timestamp":"2024-08-08T01:55:00Z"}]

[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808114100\",\"create_date\":\"20240808114100\",\"charger_status\":\"1\",\"mode\":\"1\",\"charger_door\":\"0\",\"integrated_power\":\"30000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"1\"}"}]

@MeterValues
[2,"104e04f59b0a422c9750d1cd61454cb6","MeterValues",{"connectorId":1,"transactionId":808112010,"meterValue":[{"timestamp":"2024-08-08T02:25:30Z","sampledValue":[{"value":"29100"},{"value":"32","unit":"percent","measurand":"SoC"},{"value":"30","unit":"A","measurand":"Current.Import"},{"value":"300","unit":"kW","measurand":"Voltage"}]}]}]

-- DTStatus
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808112600\",\"create_date\":\"20240808112600\",\"charger_status\":\"2\",\"mode\":\"1\",\"charger_door\":\"0\",\"integrated_power\":\"30000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"2\"}"}]

@StopTransaction
[2,"20240617182522","StopTransaction",{"transactionId":808112010,"idTag":"1010010189921562","meterStop":35000,"timestamp":"2024-08-08T02:32:00Z","reason":"Local","transactionData":[{"timestamp":"2024-08-08T01:07:00Z","sampledValue":[{"value":40,"unit":"percent","measurand":"SoC"},{"value":30,"unit":"A","measurand":"Current.Import"},{"value":300,"unit":"kW","measurand":"Voltage"}]}]}]

-Finishing
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Finishing","vendorId":"kr.co.kevit","timestamp":"2024-08-08T02:32:10Z"}]

-- DTStatus
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808112600\",\"create_date\":\"20240808112600\",\"charger_status\":\"2\",\"mode\":\"1\",\"charger_door\":\"0\",\"integrated_power\":\"30000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"2\"}"}]


-Available
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Available","vendorId":"kr.co.kevit","timestamp":"2024-08-08T02:35:40Z"}]

-- DTStatus
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808112600\",\"create_date\":\"20240808112600\",\"charger_status\":\"2\",\"mode\":\"1\",\"charger_door\":\"0\",\"integrated_power\":\"30000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"2\"}"}]


############# 신용카드


@DataTransfer-Tariff
[2,"20240808121212123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Tariff","data":"{\"timestamp\":\"2024-08-08T02:45:00Z\",\"idTag\":\"-9999\",\"pointCheck\":\"N\",\"connector_id\":\"1\"}"}]

-Preparing
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Preparing","vendorId":"kr.co.kevit","timestamp":"2024-08-08T02:45:10Z"}]

@DataTransfer-GuestStartTransaction
[2,"20240808121212123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"GuestStartTransaction","data":"{\"order_no\":\"ME18B29611_20240808140510\",\"connector_id\":\"1\",\"send_date\":\"20240808140510\",\"create_date\":\"20240808140510\",\"unit_cost\":\"324.4\",\"previous_trno\":\"202408\",\"previous_date\":\"20240808140510\",\"before_cost\":\"30000\",\"card_num\":\"1010********1030\",\"van_type\":\"KICC\",\"mall_id\":\"205010\",\"start_mv\":\"300000\",\"pay_yn\":\"Y\"}"}]

-Charging
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Charging","vendorId":"kr.co.kevit","timestamp":"2024-08-08T05:46:30Z"}]


-- DTStatus
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808112600\",\"create_date\":\"20240808112600\",\"charger_status\":\"2\",\"mode\":\"1\",\"charger_door\":\"0\",\"integrated_power\":\"301000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"2\"}"}]


@MeterValues
[2,"104e04f59b0a422c9750d1cd61454cb6","MeterValues",{"connectorId":1,"transactionId":0808140510,"meterValue":[{"timestamp":"2024-08-08T02:17:30Z","sampledValue":[{"value":"301000"},{"value":"32","unit":"percent","measurand":"SoC"},{"value":"30","unit":"A","measurand":"Current.Import"},{"value":"300","unit":"kW","measurand":"Voltage"}]}]}]

-- DTStatus
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808112600\",\"create_date\":\"20240808112600\",\"charger_status\":\"2\",\"mode\":\"1\",\"charger_door\":\"0\",\"integrated_power\":\"30000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"2\"}"}]

@DataTransfer-GuestStopTransaction
[2,"20240808121212123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"GuestStopTransaction","data":"{\"order_no\":\"ME18B29611_20240808140510\",\"transaction_id\":\"0808140510\",\"connector_id\":\"1\",\"send_date\":\"20240808115900\",\"create_date\":\"20240808115900\",\"previous_trno\":\"202408\",\"previous_date\":\"20240808140510\",\"before_cost\":\"30000\",\"cancel_cost\":\"10000\",\"after_cost\":\"20000\",\"cancel_date\":\"20240808115810\",\"cancel_result\":\"Y\",\"card_num\":\"1010********1030\",\"van_type\":\"KICC\",\"mall_id\":\"205010\",\"end_dt\":\"20240808115710\",\"end_mv\":\"305000\",\"end_soc\":\"90\",\"current_v\":\"300\",\"current_a\":\"32\",\"charge_end_type\":\"1\"}"}]

-Finishing
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Finishing","vendorId":"kr.co.kevit","timestamp":"2024-08-08T02:59:40Z"}]

-- DTStatus
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808120000\",\"create_date\":\"20240808120000\",\"charger_status\":\"1\",\"mode\":\"1\",\"charger_door\":\"0\",\"integrated_power\":\"305000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"2\"}"}]

-Available
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Available","vendorId":"kr.co.kevit","timestamp":"2024-08-08T03:01:40Z"}]

-- DTStatus
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808120300\",\"create_date\":\"20240808120300\",\"charger_status\":\"1\",\"mode\":\"1\",\"charger_door\":\"0\",\"integrated_power\":\"305000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"1\"}"}]




############# QR사용자


@DataTransfer-RmtStartTransaction
[3,"ME18B296-11-20240808121209839",{"status":"Accepted"}]

-Preparing
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Preparing","vendorId":"kr.co.kevit","timestamp":"2024-08-08T02:45:10Z"}]

@DataTransfer-GuestStartTransaction
[2,"20240808121212123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"GuestStartTransaction","data":"{\"order_no\":\"ME18B29611_20240808140510\",\"connector_id\":\"1\",\"send_date\":\"20240808140510\",\"create_date\":\"20240808140510\",\"unit_cost\":\"324.4\",\"previous_trno\":\"20240808000001\",\"previous_date\":\"-9999\",\"before_cost\":\"0\",\"card_num\":\"-9999\",\"van_type\":\"\",\"mall_id\":\"\",\"start_mv\":\"300000\",\"pay_yn\":\"Q\"}"}]

-Charging
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Charging","vendorId":"kr.co.kevit","timestamp":"2024-08-08T05:46:30Z"}]


-- DTStatus
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808112600\",\"create_date\":\"20240808112600\",\"charger_status\":\"2\",\"mode\":\"1\",\"charger_door\":\"0\",\"integrated_power\":\"301000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"2\"}"}]


@MeterValues
[2,"104e04f59b0a422c9750d1cd61454cb6","MeterValues",{"connectorId":1,"transactionId":0808140510,"meterValue":[{"timestamp":"2024-08-08T02:17:30Z","sampledValue":[{"value":"301000"},{"value":"32","unit":"percent","measurand":"SoC"},{"value":"30","unit":"A","measurand":"Current.Import"},{"value":"300","unit":"kW","measurand":"Voltage"}]}]}]

-- DTStatus
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808112600\",\"create_date\":\"20240808112600\",\"charger_status\":\"2\",\"mode\":\"1\",\"charger_door\":\"0\",\"integrated_power\":\"30000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"2\"}"}]


@DataTransfer-RmtStopTransaction
[3,"ME18B296-11-20240808121209839",{"status":"Accepted"}]


@DataTransfer-GuestStopTransaction
[2,"20240808121212123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"GuestStopTransaction","data":"{\"order_no\":\"ME18B29611_20240808140510\",\"transaction_id\":\"0808140510\",\"connector_id\":\"1\",\"send_date\":\"20240808115900\",\"create_date\":\"20240808115900\",\"previous_trno\":\"202408\",\"previous_date\":\"20240808140510\",\"before_cost\":\"30000\",\"cancel_cost\":\"10000\",\"after_cost\":\"20000\",\"cancel_date\":\"20240808115810\",\"cancel_result\":\"Y\",\"card_num\":\"1010********1030\",\"van_type\":\"KICC\",\"mall_id\":\"205010\",\"end_dt\":\"20240808115710\",\"end_mv\":\"305000\",\"end_soc\":\"90\",\"current_v\":\"300\",\"current_a\":\"32\",\"charge_end_type\":\"1\"}"}]

-Finishing
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Finishing","vendorId":"kr.co.kevit","timestamp":"2024-08-08T02:59:40Z"}]

-- DTStatus
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808120000\",\"create_date\":\"20240808120000\",\"charger_status\":\"1\",\"mode\":\"1\",\"charger_door\":\"0\",\"integrated_power\":\"305000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"2\"}"}]

-Available
[2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Available","vendorId":"kr.co.kevit","timestamp":"2024-08-08T03:01:40Z"}]

-- DTStatus
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808120300\",\"create_date\":\"20240808120300\",\"charger_status\":\"1\",\"mode\":\"1\",\"charger_door\":\"0\",\"integrated_power\":\"305000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"1\"}"}]


############################

@Heartbeat
[2,"90c776cb-a2df-4148-876d-41a7f5b76da8","Heartbeat",{}]



@DataTransfer-Status
##[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"response_date\":\"20240808114100\",\"ui_ver\":\"VER12S_1.17\",\"charger_status\":\"2\",\"rf_status\":\"1\",\"ic_status\":\"2\",\"app_start_date\":\"20240808114100\",\"stop_button_status\":\"2\",\"charging_mode\":\"2\",\"electricity_meter_mode\":\"2\",\"ui_mode\":\"1\",\"update_file_count\":\"0\",\"power_module\":\"0000000000000000\",\"free_space\":\"231472\",\"ava_mem\":\"192659456\",\"timelimit_yn\":\"Y\",\"timelimit_value\":\"40\",\"test_yn\":\"Y\",\"pay_yn\":\"Y\",\"volume_day\":\"3\",\"volume_night\":\"3\",\"volumemovie_day\":\"3\",\"volumemovie_night\":\"3\",\"charger_firmware\":\"\",\"notice_cnt\":\"1\",\"system_date\":\"20240808114100\",\"lcd_ip\":\"192.168.0.2\", \"current_unit_cost\":\"405.5\"}"}]
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"Status","data":"{\"send_date\":\"20240808114100\",\"create_date\":\"20240808114100\",\"charger_status\":\"2\",\"mode\":\"1\",\"charger_door\":\"1\",\"integrated_power\":\"30000\",\"powerbox\":\"0000000000000000\",\"charger_plug\":\"1\"}"}]


@DataTransfer-SMS
[2,"20240808120303123","DataTransfer",{"vendorId":"kr.or.keco","messageId":"SMS","data":"{\"station_id\":\"ME18B296\",\"charger_id\":\"11\",\"send_date\":\"20240808114100\",\"create_date\":\"20240808114100\",\"card_num\":\"01012345678\",\"msg\": \"SMS\",\"msg_type\":\"1\",\"Data1\":\"12345678\",\"Data2\":\"01\",\"Data3\":\"1234\",\"Data4\":\"\",\"Data5\":\"\"}"}]



################################
##### CSMS  ->   CS
##### 응답 부분 
################################
@Response-DTRmtStatus
[3,"ME18B296-11-20240808121209839",{"status":"Accepted", "data":"{\"response_date\":\"20240808121209\",\"ui_ver\":\"VER12S_1.17\",\"charger_status\":\"2\",\"rf_status\":\"1\",\"ic_status\":\"2\",\"app_start_date\":\"20240808121633\",\"stop_button_status\":\"2\",\"charging_mode\":\"2\",\"electricity_meter_mode\":\"2\",\"ui_mode\":\"1\",\"update_file_count\":\"0\",\"power_module\":\"0000000000000000\",\"free_space\":\"231472\",\"ava_mem\":\"192659456\",\"timelimit_yn\":\"Y\",\"timelimit_value\":\"40\",\"test_yn\":\"Y\",\"pay_yn\":\"Y\",\"volume_day\":\"3\",\"volume_night\":\"3\",\"volumemovie_day\":\"3\",\"volumemovie_night\":\"3\",\"charger_firmware\":\"\",\"notice_cnt\":\"1\",\"system_date\":\"20240808120555\",\"lcd_ip\":\"192.168.0.2\", \"current_unit_cost\":\"123.45\"}"}]

* DataTransfer
[3,"ME18B296-11-20240808121209839",{"status":"Accepted"}]

* Update
[3,"ME18B296-11-20240808121209839",{}]

*/