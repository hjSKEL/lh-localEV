var layerJs = function () {
    function _fn_menu(menuId) {
        //
        let menu = $("#side-menu");
        let length = menu.children().length;
        for (let i = 0; i < length; ++i) {
            let el = menu.children()[i];
            let className = el.className;
            if (el.id == menuId) {
                if (className.indexOf("active") > -1) {
                    el.className = className.replace("active", "").trim();
                    el.children[1].className = "nav nav-second-level collapse";
                } else {
                    el.className = className + "active";
                    el.children[1].className = "nav nav-second-level collapse in";
                }
            } else {
                el.className = className.replace("active", "").trim();
                el.children[1].className = "nav nav-second-level collapse";
            }
        }
    }

    function _fn_subMenu(menuId) {
        //
        let menu = $("#" + menuId);
        if ($("#" + menuId)[0].className.indexOf("active") > -1) {
            return;
        }
        let length = menu.length;
        for (let i = 0; i < length; ++i) {
            let el = menu[i];
            let className = el.className;
            if (el.id == menuId) {
                if (className.indexOf("active") > -1) {
                    el.className = className.replace("active", "").trim();
                    el.children[1].className = "nav nav-third-level collapse";
                } else {
                    el.className = className + "active";
                    el.children[1].className = "nav nav-third-level collapse in";
                }
            } else {
                el.className = className.replace("active", "").trim();
                el.children[1].className = "nav nav-third-level collapse";
            }

        }
    }

    function _fn_clearSubMenu() {
        $(".second-level").removeClass("active");
        $(".nav-third-level").removeClass("in");
    }

    function _fn_moveSubMenu(menuId, menuName, menuUrl, linkType) {
        if (linkType == 'N/A') {
            return;
        }
        if (linkType == 'NEW_BLANK') {
            let wt = window.document.body.clientWidth;
            let ht = window.document.body.clientHeight - 50;
            let openwin = window.open(menuUrl, "monitoring", "width=" + wt + "px, height=" + ht + "px,  location=no, menubar=no, resizable=yes, status=no, scrollbars=no");
            openwin.focus();
            return;
        }
        $(".content_iframe").attr("src", menuUrl);
        let parentName = $("#" + menuId).parent().parent().children()[0].children[1].innerHTML;

        let html = '<ol class="breadcrumb" style="margin-top: 10px;">';
        html += '<li>&nbsp;&nbsp;&nbsp;Home</li>';
        html += '<li>' + parentName + '</li>';
        html += '<li class="active">';
        html += '<strong>' + menuName + '</strong>';
        html += '</li>';
        html += '</ol>';
        $("#divMenuName").html(html);
    }

    function _fn_moveMenu(menuId, menuName, menuUrl, linkType, param) {
        if (linkType == 'NEW_BLANK') {
            let wt = window.document.body.clientWidth;
            let ht = window.document.body.clientHeight - 50;
            let openwin = window.open(menuUrl, "monitoring", "width=" + wt + "px, height=" + ht + "px,  location=no, menubar=no, resizable=yes, status=no, scrollbars=no");
            openwin.focus();
            return;
        }
        let parentName = $("#" + menuId).parent().parent().children()[0].children[1].innerHTML;
        
        let html = '<ol class="breadcrumb" style="margin-top: 10px;">';
        html += '<li>&nbsp;&nbsp;&nbsp;' + parentName + '</li>';
        html += '<li class="active">';
        html += '<strong>' + menuName + '</strong>';
        html += '</li>';
        html += '</ol>';
        $("#divMenuName").html(html);

        _addTabMenu(menuId, menuName, menuUrl, linkType, param);
    }

    function _fn_download(url) {
        //
        $("#downloadIFrame").attr("src", url);
    }

    function _fn_mininavbar() {
        if (!$("BODY")[0].className.includes("mini-navbar")) {
            $("#top-hamburger").trigger("click");
        }
    }

    function _fn_triggerClickEvent(id) {
        $("#" + id).trigger("click");
    }

    function _addTabMenu(menuId, menuName, menuUrl, linkType, param) {
        let isMenu = false;
        let topTab = $("#topTab");
        let length = topTab.children().length;
        $('.content_iframe').hide();
        $('.tm').removeClass('active');
        for (let i = 0; i < length; ++i) {
            let el = topTab.children()[i];
            if (el.id == 'tm' + menuId) {
                isMenu = true;
                $('#tm' + menuId).addClass('active');
                $("#ci" + menuId).show();
                if(param) {
                    $("#ci" + menuId).attr("src", menuUrl);
                }
            }
        }
        if (!isMenu) {
            let html = '<li id="tm' + menuId + '" class="tm active"><a href="javascript:void(0)" onclick="layerJs.fn_moveMenu(\'' + menuId + '\',\'' + menuName + '\',\'' + menuUrl + '\',\'' + linkType + '\');">' + menuName;
            if (menuId != '30000000') {
                html += '<i class="fa fa-times" onclick="layerJs.fn_menuExit(\'' + menuId + '\')"></i>';
            } else {
                html += '<i class="fa"></i>';
            }
            html += '</a></li>';
            $("#topTab").append(html);
            let minHeight = ($('#page-wrapper').height() - 160) + 'px';
            $("#content_div").append('<iframe id=ci' + menuId + ' class="content_iframe" style="min-height: ' + minHeight + '" crossorigin></iframe>');
            $("#ci" + menuId).attr("src", menuUrl);
        }
    }

    function _fn_menuExit(id) {
        event.stopPropagation();
        let topTab = $("#topTab");
        let length = topTab.children().length;
        if (length <= 1) {
            return;
        }
        let moveTabMenu = 0;
        for (let i = 0; i < length; i++) {
            if (topTab.children()[i].id == 'tm' + id) {
                moveTabMenu = i - 1;
                break;
            }
        }
        if ($('#tm' + id)[0].className.includes("active")) {
            $("#tm" + id).remove();
            $("#ci" + id).remove();
            let el = topTab.children()[moveTabMenu];
            let newId = el.id.substring(2);
            $('#tm' + newId).addClass('active');
            $("#ci" + newId).show();
        } else {
            $("#tm" + id).remove();
            $("#ci" + id).remove();
        }
    }
    
    function _fn_exception(xhRequest){
        let index = xhRequest.responseText.indexOf("login-password");
        if(index > 0){
        	location.reload();
        }
    }

    return {
        fn_menu: _fn_menu,
        fn_subMenu: _fn_subMenu,
        fn_clearSubMenu: _fn_clearSubMenu,
        fn_moveSubMenu: _fn_moveSubMenu,
        fn_moveMenu: _fn_moveMenu,
        fn_download: _fn_download,
        fn_triggerClickEvent: _fn_triggerClickEvent,
        fn_mininavbar: _fn_mininavbar,
        fn_menuExit: _fn_menuExit,
        fn_exception: _fn_exception
    };
}();