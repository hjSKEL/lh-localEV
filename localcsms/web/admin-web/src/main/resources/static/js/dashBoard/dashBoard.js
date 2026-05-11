/**
 * 대시보드
 */
let dashBoardJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    function _init() {
        let targetDate = new Date();
        targetDate.setDate(targetDate.getDate() - 1);
        $("#dayDate2").val(dateUtilsJs.formatDate(targetDate, "YYYY-MM-DD"));
        targetDate.setDate(targetDate.getDate() - 6);
        $("#dayDate1").val(dateUtilsJs.formatDate(targetDate, "YYYY-MM-DD"));

        let targetMonth = new Date();
        targetMonth.setMonth(targetMonth.getMonth() - 1);
        $("#monthDate2").val(dateUtilsJs.formatDate(targetMonth, "YYYY-MM"));
        targetMonth.setMonth(targetMonth.getMonth() - 5);
        $("#monthDate1").val(dateUtilsJs.formatDate(targetMonth, "YYYY-MM"));
        _initEvent();
        _searchOnDayClick();
        _searchOnMonthClick();
    }

    function _initEvent() {
        //
        _searchChargingStationCount();

        $("#btnDaySearch").click(function () {
            //
            _searchOnDayClick();
        });
        $("#btnMonthSearch").click(function () {
            //
            _searchOnMonthClick();
        });

        $('#dayDate1').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm-dd"
        });
        $('#dayDate2').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm-dd"
        });
        $('#monthDate1').monthpicker({
            pattern: 'yyyy-mm',
            selectedYear: 2021,
            startYear: 2020,
            finalYear: 2023,
            monthNames: _msg.months,
            openOnFocus: true,
            disableMonths: [],
        });
        $('#monthDate2').monthpicker({
            pattern: 'yyyy-mm',
            selectedYear: 2021,
            startYear: 2020,
            finalYear: 2023,
            monthNames: _msg.months,
            openOnFocus: true,
            disableMonths: [],
        });

        $("#flot-dashboard-chart").bind("plotclick", function (event, pos, item) {
            if (item) {
                $("#tooltip").remove();
                showTooltip(item.pageX, item.pageY,
                    formmatUtilsJs.commaFormat(item.datapoint[1]) + "</strong> (" + item.series.label + ")");
            }
        });
        $("#flot-dashboard-chart2").bind("plotclick", function (event, pos, item) {
            if (item) {
                $("#tooltip").remove();
                showTooltip(item.pageX, item.pageY,
                    formmatUtilsJs.commaFormat(item.datapoint[1]) + "</strong> (" + item.series.label + ")");
            }
        });
    }

    function showTooltip(x, y, contents) {
        $('<div id="tooltip">' + contents + '</div>').css({
            position: 'absolute',
            display: 'none',
            top: y + 5,
            left: x + 20,
            border: '2px solid #4572A7',
            padding: '2px',
            size: '10',
            'border-radius': '6px 6px 6px 6px',
            'background-color': '#fff',
            opacity: 0.80
        }).appendTo("body").fadeIn(200);
    }

    function _searchOnDayClick() {
        data.searchCond.fromDay = formmatUtilsJs.removeDash($("#dayDate1").val());
        data.searchCond.toDay = formmatUtilsJs.removeDash($("#dayDate2").val());

        _search();
    }

    function _searchOnMonthClick() {
        data.searchCond.fromMonth = formmatUtilsJs.removeDash($("#monthDate1").val());
        data.searchCond.toMonth = formmatUtilsJs.removeDash($("#monthDate2").val());

        _searchMonthChart();
    }

    function _search() {

        let param = "?fromDay=" + data.searchCond.fromDay;
        param += "&toDay=" + data.searchCond.toDay;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/dashBoard/searchDashBoardDayTotalSum" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayChart(jsonData);

            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _searchMonthChart() {

        let param = "?fromMonth=" + data.searchCond.fromMonth;
        param += "&toMonth=" + data.searchCond.toMonth;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/dashBoard/searchDashBoardMonthTotalSum" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayMonthChart(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function gd(year, month, day) {
        if (!day) {
            day = 1
        }
        return new Date(year, month - 1, day, 12).getTime();
    }

    function _searchChargingStationCount() {

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/status/groupCount",
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayChargingStationCnt(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayChargingStationCnt(jsonData) {
        if (!jsonData) {
            return;
        }

        let totalCnt = 0; // 전체 충전기수
        let obstacleCnt = 0; // 1 + 2 + 5 + 6
        let availableCnt = 0; // total - (obstacle + 충전중)

        for (let b = 0; b < Object.keys(jsonData).length; b++) {
            totalCnt = totalCnt + (jsonData["chrs0" + (b + 1) + "Count"] * 1);
        }

        obstacleCnt = jsonData.chrs01Count + jsonData.chrs02Count + jsonData.chrs05Count + jsonData.chrs06Count;
        availableCnt = totalCnt - (obstacleCnt + jsonData.chrs04Count); // 가용 : 현재 사용자가 사용 가능 충전기 개수
        //availableCnt = totalCnt - obstacleCnt;  // 가용 : 고장나지 않은 충전기 개수

        $("#totalCnt").html(totalCnt);
        $("#obstacleCnt").html(obstacleCnt);
        $("#availabilityCnt").html(availableCnt);
        $("#chargingCnt").html(jsonData.chrs04Count);
    }

    function _displayChart(result) {
        let fromDayDate = new Date(data.searchCond.fromDay.substring(0, 4), data.searchCond.fromDay.substring(4, 6) - 1, data.searchCond.fromDay.substring(6, 8), 8);
        let toDayDate = new Date(data.searchCond.toDay.substring(0, 4), data.searchCond.toDay.substring(4, 6) - 1, data.searchCond.toDay.substring(6, 8), 8);
        let betweenDay = (toDayDate.getTime() - fromDayDate.getTime()) / 1000 / 60 / 60 / 24 + 1;

        if (betweenDay > 30) {
            swal(_commonMsg.validationCheck, _msg.dateRange30, "warning");
            return false;
        }
        if (betweenDay <= 0) {
            swal(_commonMsg.validationCheck, _msg.dateRangeReselect, "warning");
            return false;
        }

        let dateArr = [];
        while (fromDayDate.getTime() <= toDayDate.getTime()) {
            let _mon_ = (fromDayDate.getMonth() + 1);
            _mon_ = _mon_ < 10 ? '0' + _mon_ : _mon_;
            let _day_ = fromDayDate.getDate();
            _day_ = _day_ < 10 ? '0' + _day_ : _day_;
            dateArr.push(fromDayDate.getFullYear() + '-' + _mon_ + '-' + _day_);
            fromDayDate.setDate(fromDayDate.getDate() + 1);
        }

        let tmp_day = dateArr[0].replace(/-/g, '');
        tmp_day *= 1;
        if (tmp_day < 20200830) {
            swal(_commonMsg.validationCheck, _msg.dateAfter2020, "warning");
            return false;
        }

        let totalUseElecAmountArr = [];
        let totalPayCostArr = [];

        let sumPayCost = 0;
        let sumUseElecAmount = 0;
        let sumUseCount = 0;

        for (let key = 0; key < betweenDay; key++) {
            if (key < result.length) {
                totalUseElecAmountArr[key] = Number(result[key].totalUseElecAmount);
                totalPayCostArr[key] = Number(result[key].totalPayCost);

                sumPayCost += totalPayCostArr[key];
                sumUseElecAmount += totalUseElecAmountArr[key];
                sumUseCount += Number(result[key].totalUseCount);
            } else {
                totalUseElecAmountArr[key] = 0;
                totalPayCostArr[key] = 0;
            }
        }

        $("#sumDayPayCost").html(formmatUtilsJs.commaFormat(sumPayCost) + ' ' + _msg.wonBracket);
        $("#sumDayUseElecAmount").html(formmatUtilsJs.commaFormat(sumUseElecAmount.toFixed(2)) + ' ' + _msg.kwhBracket);
        $("#sumDayUseCount").html(formmatUtilsJs.commaFormat(sumUseCount) + _msg.casesBracket);

        Highcharts.setOptions({
            lang: {
                thousandsSep: ','
            }
        });

        let chart = Highcharts.chart('chart', {
            chart: {
                zoomType: 'xy'
            },
            title: {
                text: _msg.dailyTotalChart
            },
            xAxis: [{
                categories: dateArr,
                crosshair: true
            }],
            yAxis: [{ // Primary yAxis
                labels: {
                    format: '{value:,.0f} kWh',
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                },
                title: {
                    text: _msg.chAmountKwh,
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                }
            }, { // Secondary yAxis
                title: {
                    text: _msg.chCostWon,
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                },
                labels: {
                    format: '{value:,.0f} ' + _msg.won,
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                },
                opposite: true
            }],
            credits: {
                enabled: false
            },
            navigation: {
                buttonOptions: {
                    y: -1000
                }
            },
            tooltip: {
                shared: true
            },
            legend: {
                layout: 'vertical',
                align: 'left',
                x: 20,
                verticalAlign: 'top',
                y: 0,
                floating: true
            },
            colors: ['#90EE90', '#008080'],
            series: [{
                type: 'column',
                name: _msg.totalChAmount,
                data: totalUseElecAmountArr,
                tooltip: {
                    valueSuffix: ' kWh'
                }
            }, {
                type: 'spline',
                name: _msg.totalChCost,
                data: totalPayCostArr,
                yAxis: 1,
                marker: {
                    lineWidth: 2,
                    lineColor: '#008080',
                    fillColor: 'white'
                },
                tooltip: {
                    valueSuffix: ' ' + _msg.won
                }

            }],
            lang: {
                thousandsSep: ','
            }
        });
    }

    function _displayMonthChart(result) {
        let fromMonthDate = new Date(data.searchCond.fromMonth.substring(0, 4), data.searchCond.fromMonth.substring(4, 6) - 1, data.searchCond.fromDay.substring(6, 8), 8);
        let toMonthDate = new Date(data.searchCond.toMonth.substring(0, 4), data.searchCond.toMonth.substring(4, 6), data.searchCond.toDay.substring(6, 8), 8);

        let MonthArr = [];
        while (fromMonthDate.getTime() <= toMonthDate.getTime()) {
            let _mon_ = (fromMonthDate.getMonth() + 1);
            _mon_ = _mon_ < 10 ? '0' + _mon_ : _mon_;
            let _day_ = fromMonthDate.getDate();
            _day_ = _day_ < 10 ? '0' + _day_ : _day_;
            MonthArr.push(fromMonthDate.getFullYear() + '-' + _mon_);
            fromMonthDate.setMonth(fromMonthDate.getMonth() + 1);
        }

        let totalUseElecAmountArr = [];
        let totalPayCostArr = [];
        let sumPayCost = 0;
        let sumUseElecAmount = 0;
        let sumUseCount = 0;
        let tmp_month = 0;

        let tmp_mon = MonthArr[0].replace(/-/g, '');
        tmp_mon *= 1;
        if (tmp_mon < 202008) {
            swal(_commonMsg.validationCheck, _msg.dateAfter202008, "warning");
            return false;
        }

        for (let key = 0; key < MonthArr.length; key++) {
            tmp_month = MonthArr[key].replace(/-/g, '');
            if (key < result.length) {
                totalUseElecAmountArr[key] = Number(result[key].totalUseElecAmount);
                totalPayCostArr[key] = Number(result[key].totalPayCost);

                sumPayCost += totalPayCostArr[key];
                sumUseElecAmount += totalUseElecAmountArr[key];
                sumUseCount += Number(result[key].totalUseCount);
            } else {
                totalUseElecAmountArr[key] = 0;
                totalPayCostArr[key] = 0;
            }

        }

        $("#sumMonthPayCost").html(formmatUtilsJs.commaFormat(sumPayCost) + ' ' + _msg.wonBracket);
        $("#sumMonthUseElecAmount").html(formmatUtilsJs.commaFormat(sumUseElecAmount.toFixed(2)) + ' ' + _msg.kwhBracket);
        $("#sumMonthUseCount").html(formmatUtilsJs.commaFormat(sumUseCount) + _msg.casesBracket);

        Highcharts.setOptions({
            lang: {
                thousandsSep: ','
            }
        });

        let chart2 = Highcharts.chart('chart2', {
            chart: {
                zoomType: 'xy'
            },
            title: {
                text: _msg.monthlyTotalChart
            },
            xAxis: [{
                categories: MonthArr,
                crosshair: true
            }],
            yAxis: [{ // Primary yAxis
                labels: {
                    format: '{value:,.0f} kWh',
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                },
                title: {
                    text: _msg.chAmountKwh,
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                }
            }, { // Secondary yAxis
                title: {
                    text: _msg.chCostWon,
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                },
                labels: {
                    format: '{value:,.0f} ' + _msg.won,
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                },
                opposite: true
            }],
            credits: {
                enabled: false
            },
            navigation: {
                buttonOptions: {
                    y: -1000
                }
            },
            tooltip: {
                shared: true
            },
            legend: {
                layout: 'vertical',
                align: 'left',
                x: 20,
                verticalAlign: 'top',
                y: 0,
                floating: true
            },
            colors: ['#6495ed', '#483d8b'],
            series: [{
                type: 'column',
                name: _msg.totalChAmount,
                data: totalUseElecAmountArr,
                tooltip: {
                    valueSuffix: ' kWh'
                }
            }, {
                type: 'spline',
                name: _msg.totalChCost,
                data: totalPayCostArr,
                yAxis: 1,
                marker: {
                    lineWidth: 2,
                    lineColor: '#483d8b',
                    fillColor: 'white'
                },
                tooltip: {
                    valueSuffix: ' ' + _msg.won
                }

            }],
        });
    }

    return {
        init: _init
    };
}();
