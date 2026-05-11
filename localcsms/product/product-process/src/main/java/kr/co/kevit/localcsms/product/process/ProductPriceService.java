/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.process;

import java.util.Date;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 6.
 */
@Service
public interface ProductPriceService {

    /**
     * IF에서 조회
     * 
     * @param productId
     * @return
     */
    @Cacheable(value="retrieveProductPriceInCache", key="#id")
    ProductPrice retrieveProductPriceInCache(String id);
    
    ProductPrice retrieveProductPrice(String id);
    
    ProductPrice retrieveLiveProductPriceByType(String type, Date standDate);
    
}
