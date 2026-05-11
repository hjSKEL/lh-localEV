/**
 * << < 1 2 3 > >>
 */
let PageInfoJs = function () {
    //
    let pagingUL = 'pagingUL';
    let instName = 'pageInfoJs';
    let searchFunc = undefined;
    let paging = {
        pageNumber: 1,
        pageCount: 10,
        pageItemSize: 20,
        totalCount: 0
    };

    //
    function _init(instanceName, pageUl, pageCount, pageItemSize, func) {
        //
        instName = instanceName;
        pagingUL = pageUl;
        paging.pageNumber = 1;
        paging.pageCount = pageCount;
        paging.pageItemSize = pageItemSize;
        searchFunc = func;
        let html = '';
        $("#" + pagingUL).empty();
        html += '<li class="footable-page-arrow"><a href="javascript:void(0);" data-page="prev">«</a></li>';
        html += '<li class="footable-page active"><a data-page="0" href="#">1</a></li>';
        html += '<li class="footable-page-arrow"><a href="javascript:void(0);" data-page="next">»</a></li>';
        $("#" + pagingUL).html(html);
    }

    function _setTotalCount(totalCount) {
        paging.totalCount = totalCount;
        _display();
    }

    function _setPageNumber(pageNumber) {
        paging.pageNumber = pageNumber;
    }

    //
    function _display() {
        //
        if (paging.totalCount == 0) {
            let html = '';
            $("#" + pagingUL).empty();
            html += '<li class="footable-page-arrow"><a data-page="prev" href="javascript:void(0);">«</a></li>';
            html += '<li class="footable-page"><a data-page="0" href="#">1</a></li>';
            html += '<li class="footable-page-arrow"><a data-page="next" href="javascript:void(0);">»</a></li>';
            $("#" + pagingUL).html(html);
            return;
        }


        let maxPageIndex = Math.floor(paging.totalCount / paging.pageItemSize);
        maxPageIndex = (paging.totalCount % paging.pageItemSize > 0) ? maxPageIndex + 1 : maxPageIndex;
        let lPageIndex = Math.floor((paging.pageNumber - 1) / paging.pageCount) * paging.pageCount + 1;
        let rPageIndex = Math.floor((paging.pageNumber - 1) / paging.pageCount) * paging.pageCount + paging.pageCount;
        if (maxPageIndex < rPageIndex) {
            rPageIndex = maxPageIndex;
        }

        let html = '';
		let firstPageNumber = 1;
        let prePageNumber = paging.pageNumber - 1;
        if(paging.pageNumber != 1) {
            html += '<li class="footable-page-arrow"><a href="javascript:' + instName + '.goToPage(' + firstPageNumber +')" data-page="prev">«</a></li>';
        }
        if(prePageNumber > firstPageNumber - 1) {
            html += '<li class="footable-page-arrow"><a href="javascript:' + instName + '.goToPage(' + prePageNumber + ')" data-page="prev">‹</a></li>';
        }
        for (lPageIndex; lPageIndex <= rPageIndex; ++lPageIndex) {
            if (lPageIndex == paging.pageNumber) {
                html += '<li class="footable-page active"><a href="javascript:' + instName + '.goToPage(' + lPageIndex + ')">' + lPageIndex + '</a></li>';
            } else {
                html += '<li class="footable-page"><a href="javascript:' + instName + '.goToPage(' + lPageIndex + ')" class="goPage">' + lPageIndex + '</a></li>';
            }
        }
        let lastPageNumber = Math.ceil(paging.totalCount / paging.pageItemSize);
        let nextPageNumber = paging.pageNumber + 1;
        if(nextPageNumber < lastPageNumber + 1) {
            html += '<li class="footable-page-arrow"><a href="javascript:' + instName + '.goToPage(' + nextPageNumber + ')" data-page="next">›</a></li>';
        }
        if(lastPageNumber != paging.pageNumber) {
            html += '<li class="footable-page-arrow"><a href="javascript:' + instName + '.goToPage(' + lastPageNumber + ')" data-page="next">»</a></li>';
        }

        $("#" + pagingUL).html(html);
    }

    function _goToPage(pageIndex) {
        paging.pageNumber = pageIndex;
        searchFunc();
    }

    function _getPaging() {
        return paging;
    }

    return {
        init: _init,
        setTotalCount: _setTotalCount,
        setPageNumber: _setPageNumber,
        getPaging: _getPaging,
        goToPage: _goToPage
    };
};
