package kr.co.kevit.localcsms.common.util.page;

/**
 * Pageing 검색 지원 Class
 * <pre>
 * 1. Page 검색 사용시 검색 조건 Class에 Extend하여 사용.
 * </pre>
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 5. 4.
 */
public class PageCriteria {

    private int totalItemCount = 0;
    
    private int pageItemSize = 20;
    private int pageCount = 0;
    
    private int pageNumber = 0;

    public PageCriteria() {
    }
    
    public int getStart() {
        if(pageNumber > pageCount) {
            pageNumber = pageCount - 1;
        }
        return pageNumber * pageItemSize;
    }
    
    public int getEnd() {
        if(pageNumber > pageCount) {
            pageNumber = pageCount - 1;
        }
        return (pageNumber + 1) * pageItemSize;
    }
    
    /**
     * Get totalItemCount
     * @return totalItemCount
     */
    public int getTotalItemCount() {
        return totalItemCount;
    }

    /**
     * Set totalItemCount
     * @param totalItemCount
     */
    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
        
        if(totalItemCount == 0) {
            this.pageCount = 1; 
            return;
        }
        
        this.pageCount = totalItemCount / this.pageItemSize;
        if(totalItemCount % pageItemSize != 0) {
            this.pageCount ++;
        }
    }

    /**
     * Get pageItemSize
     * @return pageItemSize
     */
    public int getPageItemSize() {
        return pageItemSize;
    }

    /**
     * Set pageItemSize
     * @param pageItemSize
     */
    public void setPageItemSize(int pageItemSize) {
        this.pageItemSize = pageItemSize;
    }

    /**
     * Get pageCount
     * @return pageCount
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * Get pageNumber
     * @return pageNumber
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Set pageNumber
     * @param pageNumber
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
