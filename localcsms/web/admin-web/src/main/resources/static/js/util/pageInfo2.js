/**
 * 1 2 3 +
 */
let PageInfo2Js = function () {
    //
    let pagingUL = 'pagingUL';
    let instName = 'pageInfo2Js';
    let searchFunc = undefined;
    let paging = {
        pageNumber: 1,
        pageCount: 40,
        //pageItemSize: 40,
        pageEndIdex : 1
    };

    //
    function _init(instanceName, pageUl, pageItemSize, func) {
        //
        instName = instanceName;
        pagingUL = pageUl;
        paging.pageNumber = 1;
        paging.pageEndIdex = 1;
        paging.pageItemSize = pageItemSize;
        searchFunc = func;
        $("#" + pagingUL).empty();
    }

    function _setCount(count) {
        _display(count);
    }
    
    function _display(count) {
    	//추가 페이지 없음.
        if (count == 0) {
        	$("#" + pagingUL).children().last().remove();
            return;
        }
        
        let html = '';
        let pageIndex = 1;
        if(pageIndex != paging.pageNumber){
            let prePageIndex = paging.pageNumber - 1;
            html += '<li class="footable-page-arrow"><a href="javascript:' + instName + '.goToPage(' + prePageIndex + ')" data-page="prev">‹</a></li>';
        }
        for (pageIndex; pageIndex <= paging.pageEndIdex; ++pageIndex) {
            if (pageIndex == paging.pageNumber) {
                html += '<li class="footable-page active"><a href="javascript:' + instName + '.goToPage(' + pageIndex + ')">' + pageIndex + '</a></li>';
            } else {
                html += '<li class="footable-page"><a href="javascript:' + instName + '.goToPage(' + pageIndex + ')" class="goPage">' + pageIndex + '</a></li>';
            }
        }
        if(count == paging.pageItemSize){
        	let nextPageIndex = paging.pageNumber + 1;
        	html += '<li class="footable-page-arrow"><a href="javascript:' + instName + '.goToPage(' + nextPageIndex + ')" data-page="next">›</a></li>';
        }
        $("#" + pagingUL).html(html);
    }

    function _goToPage(pageIndex) {
        paging.pageNumber = pageIndex;
        if(paging.pageEndIdex < pageIndex){
        	paging.pageEndIdex = pageIndex;
        }
        searchFunc();
    }

    function _getPaging() {
        return paging;
    }

    return {
        init: _init,
        setCount: _setCount,
        getPaging: _getPaging,
        goToPage: _goToPage
    };
};
