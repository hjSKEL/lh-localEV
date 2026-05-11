/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.entity;

import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.product.entity.domain.Product;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.entity.shared.ProductSearchCond;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 6.
 */
@Component
public interface ProductProvider {
    
    void registerProduct(Product product);
    
    void modifyProduct(Product product);
    
    Product retrieveProduct(String productId);
    
    ProductPrice retrieveProductPriceById(String id);
    
    Page<Product> retrieveProductByProductSearchCond(ProductSearchCond searchCond);

	void removeProduct(String id);

	void modifyProductPrice(ProductPrice productPrice);

	ProductPrice retrieveProductByProductPrice(ProductPrice productPrice);



}
