var queryString = queryString || (function () {
    "use strict";
    
    var result = {},
        query = window.location.search.substring(1),
        vars = query.split('&'),
        i,
        length,
        nameValuePair,
        arr;

    for (i = 0, length = vars.length; i < length; i++) {
        nameValuePair = vars[i].split('=');
        if (typeof result[nameValuePair[0]] === 'undefined') {
            result[nameValuePair[0]] = decodeURIComponent(nameValuePair[1]);
        } else if (typeof result[nameValuePair[0]] === 'string') {
            arr = [result[nameValuePair[0]], decodeURIComponent(nameValuePair[1])];
            result[nameValuePair[0]] = arr;
        } else {
            result[nameValuePair[0]].push(decodeURIComponent(nameValuePair[1]));
        }
    }

    return result;
}());