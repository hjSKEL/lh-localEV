/**
 * 충전기 사용량 통계
 */
let chargerMonthlyStatisticsJs = function () {
    "use strict";
    let sidoCodes = [];
    let elecArr = [];
    let payArr = [];
    let xArr = [];
    let checkVal = '';

    function _init() {
        $("#date1").val(formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(dateUtilsJs.addMonth(new Date(), -12)), "YYYY-MM"));
        $("#date2").val(formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(dateUtilsJs.addMonth(new Date(), -1)), "YYYY-MM"));
        _initEvent();
    }

    function _initEvent() {
        //
        $('#date1').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm",
            minViewMode: 1,
        });
        $('#date2').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm",
            minViewMode: 1,
        });

        $("#btnSearch").click(function () {
            //
            _searchOnClick();
        });
        $("#cpName").click(function () {
            //
            $("#Popup_ChargePoint").modal();
            chargePointSearchPopupJs.init(_selectedChargePoint);
        });

        _searchOnClick();
//        zipCodeJs.searchZipCode(undefined, _disZipDoSiCode);
    }

    function _searchOnClick() {
        const optionNodeList = document.getElementsByName('option');

        optionNodeList.forEach((node) => {
            if (node.checked) {
                if (node.value == "DATE")
                    document.getElementById('result').innerText = _msg.yearMonth;
                if (node.value == "SIDO")
                    document.getElementById('result').innerText = _msg.region;
                if (node.value == "REGION")
                    document.getElementById('result').innerText = _msg.area;
            }
        })
        _search();
    }

    function _search() {
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="6">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let param = "?pageNumber=0&pageItemSize=10000";
        let date1 = $("#date1").val();
        param += "&fromMonth=" + formmatUtilsJs.removeDash(date1);
        let date2 = $("#date2").val();
        param += "&toMonth=" + formmatUtilsJs.removeDash(date2);
//        let searchKey = $("#cpId").val();
//        param += "&cpId=" + searchKey;
        let checkVal = $('input[name=option]:checked').val();
        param += "&type=" + checkVal;

        let fromDayDate = new Date(date1.substring(0, 4), date1.substring(4, 6) - 1, date1.substring(6, 8), 8);
        let toDayDate = new Date(date2.substring(0, 4), date2.substring(4, 6) - 1, date2.substring(6, 8), 8);
        let betweenDay = (toDayDate.getTime() - fromDayDate.getTime()) / 1000 / 60 / 60 / 24 + 1;

        if (betweenDay > 365) {
            swal(_commonMsg.validationCheck, _msg.dateRange1yr, "warning");
            return false;
        }

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/statistics/monthly/group" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayChart(jsonData);
                _displayStatistics(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    };

    function _displayStatistics(jsonData) {
        $("#tBodyList").empty();
        let html = '';
        if (jsonData.length == 0) {
            html = '<tr style="text-align:center;">';
            html += '<td colspan="6">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
            return;
        }

        let checkVal = $('input[name=option]:checked').val();
        for (let key = 0; key < jsonData.length; key++) {
            html = '<tr style="text-align:center;">';
            switch (checkVal) {
                case "DATE" :
                    html += '<td class="exCon">' + formmatUtilsJs.dateFormmat(String(jsonData[key].yyyymm), 'YYYY-MM') + '</td>'
                    break;
                case "SIDO" :
                    for (let j = 0; j < sidoCodes.length; j++) {
                        if (jsonData[key].sidoCode === sidoCodes[j].zipCodeId) {
                            html += '<td class="exCon">' + sidoCodes[j].zipCodeSi + '</td>'
                        }
                    }
                    break;
                case "REGION" :
                    html += '<td class="exCon">' + jsonData[key].regionId + '</td>'
                    break;
                default:
                    break;
            }

            html += '<td class="exCon">' + formmatUtilsJs.commaFormat(jsonData[key].useCost) + '</td>'
            html += '<td class="exCon">' + formmatUtilsJs.commaFormat(jsonData[key].useElectronicAmount) + '</td>'
            html += '<td class="exCon">' + formmatUtilsJs.commaFormat(jsonData[key].payCost) + '</td>'
            html += '</tr>';
            $("#tBodyList").append(html);
        }
    };

    function _disZipDoSiCode(result) {
        sidoCodes = result;
    }

    function _displayChart(jsonData) {
        elecArr = [];
        payArr = [];
        xArr = [];
        let sumPayCost = 0;
        let sumUseElecAmount = 0;
        let sumUseCount = 0;
        let title_txt = '';

        if (jsonData.length == 0) {
            swal(_commonMsg.validationCheck, _msg.noDataReselect, "warning");
            return false;
        }
        checkVal = $('input[name=option]:checked').val();

        for (let i = 0; i < jsonData.length; i++) {
            switch (checkVal) {
                case "DATE" :
                    title_txt = _msg.dateChAmountCost;
                    xArr.push(formmatUtilsJs.dateFormmat(String(jsonData[i].yyyymm), 'YYYY-MM'));
                    break;
                case "SIDO" :
                    title_txt = _msg.regionChAmountCost;
                    for (let j = 0; j < sidoCodes.length; j++) {
                        if (jsonData[i].sidoCode === sidoCodes[j].zipCodeId) {
                            xArr.push(sidoCodes[j].zipCodeSi);
                        }
                    }
                    break;
                case "REGION" :
                    title_txt = _msg.areaChAmountCost;
                    xArr.push(jsonData[i].regionId);
                    break;
                default:
                    break;
            }
            elecArr.push(jsonData[i].useElectronicAmount);
            payArr.push(jsonData[i].payCost);

            sumPayCost += jsonData[i].payCost;
            sumUseElecAmount += jsonData[i].useElectronicAmount;
            sumUseCount += jsonData[i].useCount;
        }

        $("#sumDayPayCost").html(formmatUtilsJs.commaFormat(sumPayCost) + ' ' + _msg.wonBracket);
        $("#sumDayUseElecAmount").html(formmatUtilsJs.commaFormat(sumUseElecAmount) + ' ' + _msg.kwhBracket);
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
                text: title_txt
            },
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
            xAxis: [{
                categories: xArr,
                crosshair: true
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
                layout: 'vertical',
                align: 'left',
                x: 20,
                verticalAlign: 'top',
                y: 0,
                floating: true
            },
            colors: ['#8ac7ff', '#1065b4'],
            series: [{
                type: 'column',
                name: _msg.totalChAmount,
                data: elecArr,
                tooltip: {
                    valueSuffix: ' kWh'
                }
            }, {
                type: 'spline',
                name: _msg.totalChCost,
                data: payArr,
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
        });
    }

    function _selectedChargePoint(param) {
        $("#cpId").val(param.cpId);
        $("#cpName").val(param.cpName);
    }

    return {
        init: _init,
        search: _searchOnClick
    };
}();
