/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.entity.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.product.entity.ProductProvider;
import kr.co.kevit.localcsms.product.entity.dao.ProductMapper;
import kr.co.kevit.localcsms.product.entity.dao.ProductPriceMapper;
import kr.co.kevit.localcsms.product.entity.domain.Product;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.entity.shared.ProductSearchCond;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 6.
 */
@Component
public class ProductProviderImpl implements ProductProvider {
    
    @Autowired
    private ProductMapper mapper;
    
    @Autowired
    private ProductPriceMapper priceMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerProduct(Product product) {
        // 
        mapper.insertProduct(product);
        for(ProductPrice price : product.getPrices()) {
            price.makeProductId();
            price.setWriter(product.getWriter());
            priceMapper.insertProductPrice(price);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyProduct(Product product) {
        // 
        mapper.updateProduct(product);
        priceMapper.deleteProductPriceByType(product.getId());
        List<ProductPrice> prodPrices = product.getPrices();
        ProductPrice newPrice = prodPrices.get(prodPrices.size() - 1);
        ProductPrice oldPrice = prodPrices.get(prodPrices.size() - 2);
        newPrice.setWriter(new Writer(product.getWriter().getUpdUserId()));
        Date newStartDt = DateUtils.stringToDate(newPrice.getStartDt(), DateUtils.DATE_FORMAT_WITHOUT_DASH);
        Date endDt = DateUtils.changeDateWithDayLevel(newStartDt, -1);
        oldPrice.setEndDt(DateUtils.dateToString(endDt, DateUtils.DATE_FORMAT_WITHOUT_DASH));
        for(ProductPrice price : product.getPrices()) {
            price.makeProductId();
            priceMapper.insertProductPrice(price);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product retrieveProduct(String id) {
        // 
        Product product = mapper.selectProduct(id);
        if(product != null) {            
            product.setPrices(priceMapper.selectProductPriceByType(id));
        }
        return product;
    }
    
    @Override
	public ProductPrice retrieveProductPriceById(String id) {
		// 
		return priceMapper.selectProductPriceById(id);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Product> retrieveProductByProductSearchCond(ProductSearchCond searchCond) {
        // 
        Page<Product> resultSet = new Page<>();
        int totalItemCount = mapper.countProductByProductSearchCond(searchCond);
        searchCond.setTotalItemCount(totalItemCount);
        resultSet.setCriteria(searchCond);
        if(totalItemCount > 0) {
            resultSet.setResult(mapper.selectProductByProductSearchCond(searchCond));
        }else {
            resultSet.setResult(new ArrayList<>(0));
        }
        return resultSet;
    }

	@Override
	public void removeProduct(String id) {
		//
		priceMapper.deleteProductPrice(id);
	}

	@Override
	public void modifyProductPrice(ProductPrice productPrice) {
		//
		priceMapper.updateProductPriceByProductPrice(productPrice);
	}

	@Override
	public ProductPrice retrieveProductByProductPrice(ProductPrice productPrice) {
		//
		return priceMapper.selectProductPriceByProductPrice(productPrice);
	}


}