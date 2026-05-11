/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.process.logic;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.product.entity.ProductPriceProvider;
import kr.co.kevit.localcsms.product.entity.ProductProvider;
import kr.co.kevit.localcsms.product.entity.domain.Product;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.process.ProductPriceService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 9. 6.
 */
@Service
@Transactional
public class ProductPriceServiceImpl implements ProductPriceService {

    @Autowired
    private ProductPriceProvider provider;
    
    @Autowired
    private ProductProvider prodProvider;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ProductPrice retrieveProductPriceInCache(String id) {
        // 
        return provider.retrieveProductPrice(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ProductPrice retrieveProductPrice(String id) {
        // 
        return provider.retrieveProductPrice(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ProductPrice retrieveLiveProductPriceByType(String type, Date standDate) {
        // 
        Product product = prodProvider.retrieveProduct(type);
        String standDt = DateUtils.dateToString(standDate, DateUtils.DATE_FORMAT_WITHOUT_DASH);
        int stand = Integer.parseInt(standDt);
        for(ProductPrice price : product.getPrices()) {
            int startDt = Integer.parseInt(price.getStartDt());
            int endDt = Integer.parseInt(price.getEndDt());
            if(startDt <= stand && stand < endDt) {
                return price;
            }
        }
        return product.getPrices().get(0);
    }


}
