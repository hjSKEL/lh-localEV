/**
 * 모니터링 팝업
 */

var monitoringPopupJs = function(){
    "use strict";

	let centerPoint = new naver.maps.LatLng(36.7542235, 127.9913002);
	let map = new naver.maps.Map('map', {
		center: centerPoint,
		zoom: 8,
		maxZoom: 18,
		minZoom: 7,
		disableDoubleTapZoom: true,
		disableTwoFingerTapZoom: true,
		disableDoubleClickZoom: true
	});

	let cpList = [];
	let cpData = {};
	let cpMarker = [];
	let clusterMarkerImg = {
		content: '<div style="cursor:pointer;width:48px;height:48px; padding-top: 6px; line-height:30px;font-size:18px;color:white;text-align:center;font-weight:bold;background:url('+ _ctx + '/resources/img/marker/cluster.svg);background-size:contain;"></div>',
		size: new naver.maps.Size(32, 38),
		anchor: new naver.maps.Point(16, 38)
	};

	let now = new Date();
	const year = now.getFullYear(); // 년
	const month = now.getMonth();   // 월
	const day = now.getDate();      // 일

	let markerClustering;
	let quickCnt;
	let slowCnt;
	let pins = [];
	let pin = [];

	function _init() {
		_displayTimeChart();
		_displayWeekRecharging();
		_getTime();
		_displayDayRecharging();
		_displayMonthRecharging();
		_getCsStatusQuick();
		_getCsStatusSlow();
		_getCsbreak();
		_getCp();
		setInterval(_getTime, 1000);
		setInterval(_displayDayRecharging, 600000);
		setInterval(_displayMonthRecharging, 600000);
		setInterval(_getCsStatusQuick, 600000);
		setInterval(_getCsStatusSlow, 600000);
		setInterval(_getCsbreak, 600000);
		setInterval(_getCp, 600000);
	}

	// 충전소 정보 가져오기
	function _getCp() {
		var param = "?pageItemSize=" + 999999;

		cpList = [];
		cpData = {};
		$.ajax({
			type: 'GET',
			url: _ctx + '/ws/charger/chargePoint/allChargePointList4Monitoring' + param,
			dataType : 'json',
			success : function (jsonData, textStatus, jqXHR) {
				let result = jsonData.result;
				let seq = 0;
				for(let i = 0; i< result.length;i++){
					// 사용가능 충전소만 필터링
					if(result[i].cpUseYn === "N") {continue;} // 사용가능
					if(result[i].deleteYn === "Y") {continue;} // 삭제

					// 항목 새로 추가
					result[i].csTotal = 0;  // 충전소별 충전기 개수
					result[i].csStandby = 0; // 대기
					result[i].cscharging = 0;
					result[i].csErr = 0;
					cpData[result[i].cpId] = result[i];  // cpId를 키로 저장
					cpList.push(result[i]);
				}
				cpMarker = Array.from({ length: cpList.length }, () => null);
				_getCs();
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}

	// 충전기 정보 가져오기
	function _getCs(){
		var param = "?pageItemSize=" + 999999;

		$.ajax({
			type: 'GET',
			url: _ctx + '/ws/charger/status/chargerList' + param,
			dataType : 'json',
			success : function (jsonData, textStatus, jqXHR) {
				let result = jsonData.result;
				for(let i = 0; i<result.length;i++){
					if(!cpData[result[i].cpId]) {continue;}
					if(result[i].csCableChn === 'CH1')
					{cpData[result[i].cpId].csTotal++; } // 충전소별 충전기 개수 세기

					if(result[i].csStatCode === 'CHRS03' || result[i].csStatCode === 'CHRS08'){ // 충전대기
						cpData[result[i].cpId].csStandby++;
					} else if(result[i].csStatCode === 'CHRS04' || result[i].csStatCode === 'CHRS09'){ // 충전중
						cpData[result[i].cpId].cscharging++;
					}
					else if(result[i].csStatCode === 'CHRS01' || result[i].csStatCode === 'CHRS02' || result[i].csStatCode === 'CHRS05' || result[i].csStatCode === 'CHRS06'){
						cpData[result[i].cpId].csErr++;
					}
					else
						cpData[result[i].cpId].csErr++;
				}
				_displayCp(cpList);
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}

	//좌표 설정
	function _displayCp(result){

		if(markerClustering) {

			markerClustering.setMap(null);
		}
		if(pins.length !== 0)
		{
			for(let j=0;j<pins.length;j++)
			{
				pins[j].setMap(null);
			}
		}
		pins = [];
		for(let i=0;i<result.length;i++)
		{
			let curPoint = new naver.maps.LatLng(result[i].lat, result[i].lon);
			map.setCenter(curPoint);

			let mapUrl = '';
			let csStandby = (result[i].csStandby < 10) ? ('0' + result[i].csStandby) : result[i].csStandby;
			if(result[i].csErr !== 0)
				mapUrl = _ctx + '/resources/img/marker/not_connected.svg';

			// 충전기 개수 최대 10개
			if(result[i].csTotal > 10 &&  csStandby > 10) {
				result[i].csTotal = 10;
				csStandby = 10;
			}

			// 충전기 개수별 마커 설정
			if(result[i].csTotal !== 0) {
				mapUrl = _ctx + '/resources/img/marker/' + result[i].csTotal + '_' + csStandby + '(A).svg';
			}

			pin = new naver.maps.Marker({
				map: map,
				position: curPoint,
				center: centerPoint,
				icon: {
					url: mapUrl,
					size: new naver.maps.Size(40, 40),
					scaledSize: new naver.maps.Size(40, 40),
					origin: new naver.maps.Point(0, 0),
					anchor: new naver.maps.Point(20, 20)
				}
			});
			_clickMarker(result[i], pin);
			pins.push(pin);
		}
		map.setCenter(centerPoint);

		markerClustering = new MarkerClustering({
			minClusterSize: 1,
			maxZoom: 10,
			map: map,
			markers: pins,
			disableClickZoom: false,
			averageCenter: true,
			gridSize: 100,
			icons: [clusterMarkerImg],
			stylingFunction: function(clusterMarker, count) {
				if(count > 999) {
					$(clusterMarker.getElement()).find('div:first-child').css('font-size','11px').text('999+');
				} else if(count > 99 && count < 1000) {
					$(clusterMarker.getElement()).find('div:first-child').css('font-size','14px').text(count);
				} else {
					$(clusterMarker.getElement()).find('div:first-child').text(count);
				}
			}
		});
	}

	function _clickMarker(cpInfo, pinInfo){
		pinInfo.addListener('click', function (){
			console.log("클릭한 충전소 정보",cpInfo);
		});
	}

	// 당일 충전 현황
	function _displayDayRecharging(){

		$('.updateDate').empty();

		let date = formmatUtilsJs.dateFormmat(dateUtilsJs.currentDate("YYYY/MM/DD"));
		let clock = '';
		let time = new Date();
		let hour = time.getHours();
		let minutes = time.getMinutes();
		clock += ((hour<10) ? '0' + hour : hour) + ":" + ((minutes<10) ? '0' + minutes : minutes);

		let html = '';
		html += date + " " + clock + " " + _msg.base;

		$('.updateDate').append(html);

		let toDay = formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date()),'YYYY-MM-DD');
		toDay = toDay + ' 23:59:59';
		let fromDay = formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(year, month, day - 1)),'YYYY-MM-DD');
		fromDay= fromDay + ' 00:00:00';

		let param = "?fromDay=" + fromDay + "&toDay=" + toDay;
		// 급속
		let quick = param + "&csKindType=CHKT02";
		let quickRecharg = _rechargingStatis(quick);
		// 완속
		let slow = param + "&csKindType=CHKT01";
		let slowRecharg = _rechargingStatis(slow);
		// 합계
		let sumRecharg = _rechargingStatis(param);

		$(".dayCh").empty();
		var html1='';
		html1 += '<span style="flex:1;">' + _msg.chCount + '</span>';
		html1 += '<span class="statusTxt blue" >' + quickRecharg.count.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		html1 += '<span class="statusTxt green" >'+ slowRecharg.count.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		html1 += '<span class="statusTxt gray" >'+ sumRecharg.count.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","); +'</span>';
		$("#dStatus1").append(html1);
		var html2='';
		html2 += '<span style="flex:1;">' + _msg.chAmount + '</span>';
		html2 += '<span class="statusTxt blue" >' + quickRecharg.sumAmount.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		html2 += '<span class="statusTxt green" >' + slowRecharg.sumAmount.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		html2 += '<span class="statusTxt gray" >' + sumRecharg.sumAmount.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		$("#dStatus2").append(html2);

		var html3='';
		html3 += '<span style="flex:1;">' + _msg.chCost + '</span>';
		html3 += '<span class="statusTxt blue" >' + quickRecharg.sumPay.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		html3 += '<span class="statusTxt green" >' + slowRecharg.sumPay.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		html3 += '<span class="statusTxt gray" >' + sumRecharg.sumPay.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		$("#dStatus3").append(html3);

	}

	// 당월 충전현황
	function _displayMonthRecharging(){

		let toDay = formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(now),'YYYY-MM-DD');
		toDay = toDay + ' 23:59:59';
		let fromDay = formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(year, month, 1)),'YYYY-MM-DD');
		fromDay = fromDay + ' 00:00:00';
		let param = "?fromDay=" + fromDay + "&toDay=" + toDay;
		// let param = "?fromDay=" + fromDay + "&toDay=" + toDay;
		// 급속
		let quick = param + "&csKindType=CHKT02";
		let quickRecharg = _rechargingStatis(quick);
		// 완속
		let slow = param + "&csKindType=CHKT01";
		let slowRecharg = _rechargingStatis(slow);
		// 합계
		let sumRecharg = _rechargingStatis(param);

		$(".monthCh").empty();
		var html1='';
		html1 += '<span style="flex:1;">' + _msg.chCount + '</span>';
		html1 += '<span class="statusTxt blue" >' + quickRecharg.count.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		html1 += '<span class="statusTxt green" >'+ slowRecharg.count.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		html1 += '<span class="statusTxt gray" >'+ sumRecharg.count.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","); +'</span>';
		$("#mStatus1").append(html1);

		var html2='';
		html2 += '<span style="flex:1;">' + _msg.chAmount + '</span>';
		html2 += '<span class="statusTxt blue" >' + quickRecharg.sumAmount.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		html2 += '<span class="statusTxt green" >' + slowRecharg.sumAmount.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		html2 += '<span class="statusTxt gray" >' + sumRecharg.sumAmount.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		$("#mStatus2").append(html2);

		var html3='';
		html3 += '<span style="flex:1;">' + _msg.chCost + '</span>';
		html3 += '<span class="statusTxt blue" >' + quickRecharg.sumPay.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		html3 += '<span class="statusTxt green" >' + slowRecharg.sumPay.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		html3 += '<span class="statusTxt gray" >' + sumRecharg.sumPay.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",") +'</span>';
		$("#mStatus3").append(html3);
	}

	// 7일 충전현황
	function _displayWeekRecharging() {
		let chCount = [];
		let chAmount = [];
		let fromDayDate;
		let toDayDate;
		let toDay;
		let fromDay;
		let param;
		let sumRecharg;

		let weekstart = new Date(year, month, day - 7);
		let weekend = new Date(year, month, day - 1);
		let dateArr = [];
		while(weekstart.getTime() <= weekend.getTime()){
			let _mon_ = (weekstart.getMonth()+1);
			_mon_ = _mon_ < 10 ? '0'+_mon_ : _mon_;
			let _day_ = weekstart.getDate();
			_day_ = _day_ < 10 ? '0'+_day_ : _day_;
			dateArr.push(weekstart.getFullYear() + '/' + _mon_ + '/' +  _day_);
			weekstart.setDate(weekstart.getDate() + 1);
		}

		for (let i = 7; i > 0; i--)
		{
			fromDayDate = new Date(year, month, day - i);
			toDayDate = new Date(year, month, day - i);
			fromDay = formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(fromDayDate),'YYYY-MM-DD');
			fromDay = fromDay + ' 00:00:00';
			toDay = formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(toDayDate),'YYYY-MM-DD');
			toDay = toDay + ' 23:59:59';

			param = "?fromDay=" + fromDay + "&toDay=" + toDay;
			// 합계
			sumRecharg = _rechargingStatis(param);
			chCount.push(sumRecharg.count);
			chAmount.push(sumRecharg.sumAmount);
		}

		let chart = Highcharts.chart('weekChart', {
			chart: {
				zoomType: 'xy',
				marginTop:30
			},
			title: {
				text: ''
			},
			yAxis: [{ // Primary yAxis
				// tickInterval: 20,
				labels: {
					format: '{value:,.0f}',
					style: {
						color: '#3A7AF6'
					}
				},
				title: {
					text: '',
					style: {
						color: Highcharts.getOptions().colors[1]
					}
				}
			}, { // Secondary yAxis
				// tickInterval: 20,
				title: {
					text: '',
					style: {
						color: Highcharts.getOptions().colors[1]
					}
				},
				labels: {
					format: '{value:,.0f}',
					style: {
						color: Highcharts.getOptions().colors[1]
					}
				},
				opposite: true
			}],
			xAxis: [{
				categories: dateArr,
				// crosshair: true
			}],
			credits: {
				enabled: false
			},
			navigation: {
				buttonOptions: {
					y: -1000
				}
			},
			legend: {
				align: 'right',
				x: 18,
				verticalAlign: 'top',
				y: -17,
				floating: true
			},
			colors: ['#8ac7ff', '#1065b4'],
			series: [{
				type: 'column',
				name: _msg.chAmountKwh,
				data: chAmount,
				color:'#3A7AF6'
			}, {
				type: 'spline',
				name: _msg.chCountLabel,
				data: chCount,
				color:'#333333',
				yAxis: 1,
			}],
			lang: {
				thousandsSep: ','
			}
		});
	}

	// 충전 현황
	function _rechargingStatis(param) {
		param = encodeURI(param);
		let data;

		$.ajax({
			type: 'GET',
			url: _ctx + '/ws/dashBoard/rechargingStatistics' + param,
			dataType : 'json',
			async: false,
			success : function (jsonData, textStatus, jqXHR) {
				data = jsonData;
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
		return data;
	}

	// 충전기 현황 가져오기 - 급속
	function _getCsStatusQuick() {
		let param = "?pageItemSize=" + 999999;
		param += "&csKindType=CHKT02";

		$.ajax({
			type: 'GET',
			url: _ctx + '/ws/dashBoard/statusStatistics' + param,
			dataType : 'json',
			success : function (jsonData, textStatus, jqXHR) {
				_quickCsStatus(jsonData);
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}

	// 충전기 현황 가져오기 - 완속
	function _getCsStatusSlow() {
		let param = "?pageItemSize=" + 999999;
		param += "&csKindType=CHKT01";

			$.ajax({
				type: 'GET',
				url: _ctx + '/ws/dashBoard/statusStatistics' + param,
				dataType : 'json',
				success : function (jsonData, textStatus, jqXHR) {
					_slowCsStatus(jsonData);
				},
				error : function(xhRequest, ErrorText, thrownError) {
					//
				}
			});
	}

	// 이상충전기 목록 가져오기
	function _getCsbreak() {
		var param = "?pageItemSize=" + 999999;

		$.ajax({
			type: 'GET',
			url: _ctx + '/ws/dashBoard/chargerstatus',
			dataType : 'json',
			success : function (jsonData, textStatus, jqXHR) {
				_displayUnusable(jsonData);
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}

	function _displayUnusable(jsonData){
		let html = '';
		let classname = '';
		let backcolor = '';
		let status = "";

		for(let i = 0; i<jsonData.length; i++)
		{
			if(jsonData[i].cpId === '412200001' || jsonData[i].cpId === '483300001')
				continue;
			if(jsonData[i].csStatCode === 'CHRS02'){
				classname = 'unlink';
				backcolor = '#EE781F';
				status = _ctx + "/resources/img/monitoring/unlink.svg";
			}
			else if(jsonData[i].csStatCode === 'CHRS01' || jsonData[i].csStatCode === 'CHRS05' || jsonData[i].csStatCode === 'CHRS06')
			{
				classname = 'warning';
				backcolor = '#666666';
				status = _ctx + "/resources/img/monitoring/warning.svg";
			}
			html += '<li class="list">';
			html += '<span class="' + classname + ' item1" style="background-color: ' + backcolor + ';"></span>';
			html += '<span class="' + classname + ' item2"><img src="' + status +'" style="margin: 14px;"></span>';
			html += '<span class="' + classname + ' item3"><p class="tList">' + jsonData[i].cpName + '</p></span>';
			html += '<span class="' + classname + ' item4"><p class="tList">' + jsonData[i].cpId + '-' + jsonData[i].csId + '(' + jsonData[i].csCableChn + ')</p></span>';
			html += '<span class="' + classname + ' item5"><p class="tList">' + (jsonData[i].infoCollDate ? (jsonData[i].infoCollDate).replace(/\-/gi, "/") : '-').replace(/.0$/,"") + '</p></span></li>';
		}
		$('#unCpList').empty();
		$('#unCpList').append(html);
	}

	// 현재 날짜, 시간 불러오기
	function _getTime(){
		let date = formmatUtilsJs.dateFormmat(dateUtilsJs.currentDate("YYYY/MM/DD"));
		let week = formmatUtilsJs.dateFormmat(dateUtilsJs.currentWeek());

		$("#date").empty();
		$("#today").empty();
		$("#date").append(date + " " + week);
		$("#today").append("(" + date + ")");

		let fname = '';
		let hours = new Date().getHours();
		if(hours>=7 && hours<=17){
			fname = _ctx + "/resources/img/monitoring/sun.svg";
		}
		else {
			fname =  _ctx + "/resources/img/monitoring/night.svg";
		}
		$('#night').empty();
		$('#night').attr("src",fname);

		let html = '';
		let time = new Date();
		let hour = time.getHours();
		let minutes = time.getMinutes();
		let seconds = time.getSeconds();
		html += ((hour<10) ? '0' + hour : hour) + ":" + ((minutes<10) ? '0' + minutes : minutes);
		$('#clock').empty();
		$('#clock').append(html);

		if(hour === 8 && minutes === 30 && seconds === 0){
			_displayTimeChart();
			_displayWeekRecharging();
		}
	}

	// 시간대별 충전량
	function _displayTimeChart() {
		let hours = [];
		let slowParam;
		let quickParam;

		for(let i=0;i<24;i++){
			hours[i] = i;
		}

		let fromDay = formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(year, month, day - 1)),'YYYY-MM-DD');
		let toDay = formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(year, month, day)),'YYYY-MM-DD');
		fromDay += " 00:00:00";
		toDay += " 00:00:00";

		slowParam = "?csKindType=CHKT01&fromDay=" + fromDay + "&toDay=" + toDay;
		quickParam = "?csKindType=CHKT02&fromDay=" + fromDay + "&toDay=" + toDay;
		slowParam = encodeURI(slowParam);
		quickParam = encodeURI(quickParam);

		let slow = _rechargingTime(slowParam);
		let quick = _rechargingTime(quickParam);
		let slowList = [];
		let quickList = [];

		for(let i=0;i<24;i++){
			slowList.push(0);
			quickList.push(0);
		}

		for(let i=0;i<slow.length;i++)
		{
			slowList[slow[i].time] = slow[i].amount;
		}
		for(let i=0;i<quick.length;i++){
			quickList[quick[i].time] = quick[i].amount;
		}

		let chart = Highcharts.chart('chart', {
			chart: {
				type: 'line',
				marginTop:30
			},
			title: {
				text: ''
			},
			xAxis: {
				categories: hours
			},
			yAxis: {
				gridLineWidth: 1,
				tickInterval: 5,
				title: {
					text: _msg.chAmountKwh
				}
			},
			plotOptions: {
				line: {
					dataLabels: {
						enabled: true
					},
					enableMouseTracking: true
				}
			},
			credits: {
				enabled: false
			},
			navigation: {
				buttonOptions: {
					y: -1000
				}
			},
			series: [{
				name: _msg.quick,
				data: quickList,
				color: '#3A7AF6',
				dataLabels:{
					color:'#3A7AF6'
				}
			}, {
				name: _msg.slow,
				data: slowList,
				color: '#333333'
			}],
			lang: {
				thousandsSep: ','
			},
			legend: {
				align: 'right',
				x: 18,
				verticalAlign: 'top',
				y: -18,
				floating: true
			}
		});

	}

	// 시간별 충전 현황
	function _rechargingTime(param) {
		let data;

		$.ajax({
			type: 'GET',
			url: _ctx + '/ws/dashBoard/rechargingTimeStatistics' + param,
			dataType : 'json',
			async: false,
			success : function (jsonData, textStatus, jqXHR) {
				data = jsonData;
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
		return data;
	}

	function _quickCsStatus(jsonData){
		let available = jsonData.available;
		let charging = jsonData.charging;
		let networkDisorder = jsonData.networkDisorder;
		let breakdown = jsonData.breakdown;

		$('.quickTxt').empty();
		$('#quickT').empty();
		$('#quickT').append(_msg.quickStatus + " (" + jsonData.chargerTotal + _msg.unit + ")");
		$('#quick1').append(available + _msg.unit + "<br>" + _msg.available);
		$('#quick2').append(charging + _msg.unit + "<br>" + _msg.charging);
		$('#quick3').append(networkDisorder + _msg.unit + "<br>" + _msg.commError2);
		$('#quick4').append(breakdown + _msg.unit + "<br>" + _msg.breakdown);

		// 충전기현황 도넛차트
		$(window).ready(function(){

			draw((available/jsonData.chargerTotal)*100, '.quick-chart1', '#3A7AF6');
			draw((charging/jsonData.chargerTotal)*100, '.quick-chart2', '#19B100');
			draw((networkDisorder/jsonData.chargerTotal)*100, '.quick-chart3','#EE781F');
			draw((breakdown/jsonData.chargerTotal)*100, '.quick-chart4','#666666');

		});
		quickCnt = jsonData.chargerTotal;
	}

	function _slowCsStatus(jsonData){
		let available = jsonData.available;
		let charging = jsonData.charging;
		let networkDisorder = jsonData.networkDisorder;
		let breakdown = jsonData.breakdown;

		$('.slowTxt').empty();
		$('#slowT').empty();
		$('#slowT').append(_msg.slowStatus + " (" + jsonData.chargerTotal + _msg.unit + ")");
		$('#slow1').append(available + _msg.unit + "<br>" + _msg.available);
		$('#slow2').append(charging + _msg.unit + "<br>" + _msg.charging);
		$('#slow3').append(networkDisorder + _msg.unit + "<br>" + _msg.commError2);
		$('#slow4').append(breakdown + _msg.unit + "<br>" + _msg.breakdown);

		// 충전기현황 도넛차트
		$(window).ready(function(){
			draw((available/jsonData.chargerTotal)*100, '.slow-chart1', '#3A7AF6');
			draw((charging/jsonData.chargerTotal)*100, '.slow-chart2', '#19B100');
			draw((networkDisorder/jsonData.chargerTotal)*100, '.slow-chart3','#EE781F');
			draw((breakdown/jsonData.chargerTotal)*100, '.slow-chart4','#666666');
		});
		slowCnt = jsonData.chargerTotal;
		let sum = quickCnt + slowCnt;
		$("#csText").empty();
		$("#csText").append(_msg.totalCs.replace('{0}', sum).replace('{1}', quickCnt).replace('{2}', slowCnt));
	}

	function draw(max, classname, colorname){
		let i=0;
		if(0<max && max<1)
		{
			max = 1;
		}
		let func1 = setInterval(function(){
			if(i<=max){
				color1(i,classname,colorname);
				i++;
			}
			else{
				clearInterval(func1);
			}
		},10);
	}

	function color1(i, classname,colorname){
		$(classname).css({
			"background":"conic-gradient("+colorname+" 0% "+i+"%, #E6E8F3 "+i+"% 100%)"
		});
	}

	return {
		init : _init
	};
}();
