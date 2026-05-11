/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.entity.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.product.entity.ProductPriceProvider;
import kr.co.kevit.localcsms.product.entity.dao.ProductPriceMapper;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 6.
 */
@Component
public class ProductPriceProviderImpl implements ProductPriceProvider {
    
    @Autowired
    private ProductPriceMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductPrice retrieveProductPrice(String id) {
        // 
        return mapper.selectProductPriceById(id);
    }


}