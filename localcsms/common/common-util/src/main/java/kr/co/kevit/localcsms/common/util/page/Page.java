package kr.co.kevit.localcsms.common.util.page;

import java.util.List;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 5. 4.
 */
public class Page<T> {
    
    private PageCriteria criteria;
    
    private List<T> result;
    
    public Page() {}
    public Page(PageCriteria criteria) {
        this.criteria = criteria;
    }
    
    public Page(PageCriteria criteria, List<T> result) {
        
        this.criteria = criteria;
        this.result = result;
    }
    
    /**
     * Get criteria
     * @return criteria
     */
    public PageCriteria getCriteria() {
        return criteria;
    }
    
    /**
     * Set criteria
     * @param criteria
     */
    public void setCriteria(PageCriteria criteria) {
        this.criteria = criteria;
    }
    
    /**
     * Get result
     * @return result
     */
    public List<T> getResult() {
        return result;
    }
    
    /**
     * Set result
     * @param result
     */
    public void setResult(List<T> result) {
        this.result = result;
    }
}
