/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.process;

import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.product.entity.domain.Product;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.entity.shared.ProductSearchCond;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 6.
 */
@Service
public interface ProductService {

    void registerProduct(Product product);
    
    void modifyProduct(Product product);
    
    Product retrieveProduct(String productId);
    
    Page<Product> retrieveProductByProductSearchCond(ProductSearchCond searchCond);

	void removeEmployee(ProductPrice productPrice);
    
}
