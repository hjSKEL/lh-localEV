/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.entity;

import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 6.
 */
@Component
public interface ProductPriceProvider {
    
    ProductPrice retrieveProductPrice(String id);

}
