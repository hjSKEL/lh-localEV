/**
 *
 */
var formmatUtilsJs = function () {
    //
    function _postAppend(ch, number, value) {
        //
        for (var i = 0; i < number; ++i) {
            value = value + ch;
        }
        return value;
    }

    function _dotThsFormatStr(ths, value) {
        //
        if (!value || value == "0") return "0";
        value = value + "";

        if (value.indexOf(".") == -1) {
            value = value + ".";
            value = _postAppend("0", ths, value);
        } else {
            var preValue = value.substring(0, value.indexOf("."));
            var postValue = value.substring(value.indexOf(".") + 1);
            if (postValue.length == 0) {
                value = preValue + ".";
                value = _postAppend("0", ths, value);
            }
            if (postValue.length < ths) {
                value = preValue + "." + postValue;
                value = _postAppend("0", ths - postValue.length, value);
            }
            if (postValue.length == ths) {
                value = preValue + "." + postValue;
            }
            if (postValue.length > ths) {
                value = preValue + "." + postValue.substring(0, ths);
            }
        }
        return value;
    };

    function _commaFormat(value) {
        //
        if (!value || value == 0) return 0;

        var reg = /(^[+-]?\d+)(\d{3})/;
        value = '' + value;

        while (reg.test(value)) value = value.replace(reg, '$1' + ',' + '$2');
        return value;
    };

    function _dateFormmat(value, formmat) {
        var year = value.substring(0, 4);
        var month = value.substring(4, 6);
        var day = value.substring(6, 8);
        switch (formmat) {
            case 'YYYY-MM-DD HH:MM:SS':
                var hour = value.substring(8, 10);
                var min = value.substring(10, 12);
                var sec = value.substring(12);
                return year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
            case 'YYYY-MM-DD HH:MM':
                var hour = value.substring(8, 10);
                var min = value.substring(10, 12);
                var sec = value.substring(12);
                return year + "-" + month + "-" + day + " " + hour + ":" + min;
            case 'YYYY/MM/DD HH:MM':
                var hour = value.substring(8, 10);
                var min = value.substring(10, 12);
                var sec = value.substring(12);
                return year + "/" + month + "/" + day + " " + hour + ":" + min;
            case 'YYYY-MM-DD':
                return year + "-" + month + "-" + day;
            case 'YYYY/MM/DD':
                return year + "/" + month + "/" + day;
            case 'MM/DD/YYYY':
                return month + "/" + day + "/" + year;
            case 'YYYY-MM':
                return year + "-" + month;
            case 'YYYY/MM':
                return year + "/" + month;
            default :
                return value;
        }
    }

    function _phoneFormat(value) {
        if (!value || value.length == 0) return value;

        if (value.length == 10) {
            return value.substring(0, 2) + '-' + value.substring(2, 6) + '-' + value.substring(6);
        }
        if (value.length == 11) {
            return value.substring(0, 3) + '-' + value.substring(3, 7) + '-' + value.substring(7);
        }
        return value;
    }

    function _bizRegNoFormat(value) {
        if (!value || value.length == 0) return value;

        if (value.length == 10) {
            return value.substring(0, 3) + '-' + value.substring(3, 5) + '-' + value.substring(5);
        }
        return value;
    }

    function _securityMark(value) {
        if (!value || value.length == 0) return "*";
        return value.substring(0, 2) + "**";
    };

    function _removeDash(value) {
        return _removeSpecialChar(value, '-');
    }

    function _removeSpecialChar(value, specialChar) {
        if (!value || value.length == 0) return "";
        var valueList = value.split(specialChar);
        return valueList.join('');
    }

    function _removeSpace(value) {
        if (!value || value.length == 0) return "";
        return value.trim();
    }

    function _cardFormat(value) {
        if (!value || value.length == 0) return value;
        return value.substring(0, 4) + '-' + value.substring(4, 8) + '-' + value.substring(8, 12) + '-' + value.substring(12);
    }

    return {
        dateFormmat: _dateFormmat,
        commaFormat: _commaFormat,
        phoneFormat: _phoneFormat,
        bizRegNoFormat: _bizRegNoFormat,
        securityMark: _securityMark,
        removeDash: _removeDash,
        removeSpecialChar: _removeSpecialChar,
        removeSpace: _removeSpace,
        dotThsFormatStr: _dotThsFormatStr,
		cardFormat: _cardFormat
    }
}();

var dateUtilsJs = function () {
    // YYYYMMDD
    function _currentDate(format) {
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth();
        var day = date.getDate();
        month = _zeroPreFix(2, month + 1);
        day = _zeroPreFix(2, day);

        switch (format) {
            case 'YYYY/MM/DD' :
                return year + "/" + month + "/" + day;
            case 'MM/DD/YYYY' :
                return month + "/" + day + "/" + year;
            case 'YYYY-MM-DD' :
                return year + "-" + month + "-" + day;
            case 'YYYY/MM' :
                return year + "/" + month;
            case 'YYYY-MM' :
                return year + "-" + month;
            default : //YYYYMMDD
                return year + "" + month + "" + day;
        }
    };

    function _currentWeek() {
        var today = new Date();
        let weekArr = new Array('(일)', '(월)', '(화)', '(수)', '(목)', '(금)', '(토)');
        let week = today.getDay();
        let todayLabel = weekArr[week];
        return todayLabel;
    }

    function _currentTime() {
        var today = new Date();
        let hours = today.getHours();
        hours = (hours < 10 ? '0' : '') + hours;
        let minutes = today.getMinutes();
        minutes = (minutes < 10 ? '0' : '') + minutes;
        let time = hours + ":" + minutes;
        return time;
    }

    // YYYYMMDD
    function _formatDate(date, format) {
        var year = date.getFullYear();
        var month = date.getMonth();
        var day = date.getDate();
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var seconds = date.getSeconds();
        month = _zeroPreFix(2, month + 1);
        day = _zeroPreFix(2, day);
        hours = _zeroPreFix(2, hours);
        minutes = _zeroPreFix(2, minutes);
        seconds = _zeroPreFix(2, seconds);

        switch (format) {
            case 'YYYY/MM/DD' :
                return year + "/" + month + "/" + day;
            case 'MM/DD/YYYY' :
                return month + "/" + day + "/" + year;
            case 'YYYY-MM-DD' :
                return year + "-" + month + "-" + day;
            case 'YYYY/MM' :
                return year + "/" + month;
            case 'YYYY-MM' :
                return year + "-" + month;
            case 'YYYY-MM-DD HH:MM' :
                return year + "-" + month + "-" + day + " " + hours + ":" + minutes;
            case 'YYYY-MM-DD HH:MM:SS' :
                return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
            default : //YYYYMMDD
                return year + "" + month + "" + day;
        }
    };

    function _currentMonthFirstDay() {
        var date = new Date();
        date.setDate(1);
        return date;
    };

    function _currentMonthLastDay() {
        var date = new Date();
        return new Date(date.getFullYear(), date.getMonth() + 1, 0);
    };

    function _addDay(date, value) {
        date.setDate(date.getDate() + value);
        return date;
    };

    function _addMonth(date, value) {
        date.setMonth(date.getMonth() + value);
        return date;
    };

    function _string2date(dateStr, format) {
        //
        var dt = new Date();
        dt.setHours(0);
        dt.setMinutes(0);
        dt.setSeconds(0);
        switch (format) {
            case 'YYYYMMDD' :
                break;
            case 'YYYYMMDDHH24MISS' :
                dt.setHours(Number(dateStr.substring(8, 10)));
                dt.setMinutes(Number(dateStr.substring(10, 12)));
                dt.setSeconds(Number(dateStr.substring(12, 14)));
                break;
            case 'YYYY/MM/DD' :
                dateStr = formmatUtilsJs.removeSpecialChar(dateStr, '/');
                break;
            case 'YYYY-MM-DD' :
                dateStr = formmatUtilsJs.removeSpecialChar(dateStr, '-');
                break;
            default :
                return undefined;
        }
        dt.setFullYear(Number(dateStr.substring(0, 4)));
        dt.setMonth(Number(dateStr.substring(4, 6)) - 1);
        dt.setDate(Number(dateStr.substring(6, 8)));
        return dt;
    };

    function _date2String(date) {
        //
        var year = date.getFullYear();
        var month = date.getMonth();
        var day = date.getDate();
        month = _zeroPreFix(2, month + 1);
        day = _zeroPreFix(2, day);
        var hour = date.getHours();
        hour = _zeroPreFix(2, hour);
        var min = date.getMinutes();
        min = _zeroPreFix(2, min);
        var second = date.getSeconds();
        second = _zeroPreFix(2, second);
        return year + "" + month + "" + day + "" + hour + "" + min + "" + second;
    };

    function _zeroPreFix(maxLength, value) {
        if (!value) value = "";

        value = "" + value;
        if (value.length >= maxLength) {
            return value;
        }

        for (var i = 0, size = maxLength - value.length; i < size; ++i) {
            value = "0" + value;
        }
        return value;
    }

    return {
        currentDate: _currentDate,
        currentWeek: _currentWeek,
        currentTime: _currentTime,
        formatDate: _formatDate,
        date2String: _date2String,
        string2date: _string2date,
        addDay: _addDay,
        addMonth: _addMonth,
        zeroPreFix: _zeroPreFix,
        currentMonthFirstDay: _currentMonthFirstDay,
        currentMonthLastDay: _currentMonthLastDay
    };
}();

var stringUtilsJs = function () {
    //
    function _null2string(value) {
        if (!value) {
            return "";
        }
        return value.trim();
    }

    function _byteCheck(el) {
        var codeByte = 0;
        for (var idx = 0; idx < el.length; idx++) {
            var oneChar = escape(el.charAt(idx));
            if (oneChar.length == 1) {
                codeByte++;
            } else if (oneChar.indexOf("%u") != -1) {
                codeByte += 2;
            } else if (oneChar.indexOf("%") != -1) {
                codeByte++;
            }
        }
        return codeByte;
    }

    return {
        byteCheck: _byteCheck,
        null2string: _null2string
    };
}();

var refreshJs = function () {
    //
    function _function(callback) {
        document.onkeydown = (event) => {
            if (event.keyCode === 116) {
                callback();
                return false;
            } else if (event.ctrlKey && (event.keyCode === 78 || event.keyCode === 82)) {
                callback();
                return false;
            }
        }
    }
    function _stop() {
        document.onkeydown = (event) => {
            if (event.keyCode === 116) {
                return false;
            } else if (event.ctrlKey && (event.keyCode === 78 || event.keyCode === 82)) {
                return false;
            }
        }
    }

    return {
        function: _function,
        stop: _stop
    };
}();

