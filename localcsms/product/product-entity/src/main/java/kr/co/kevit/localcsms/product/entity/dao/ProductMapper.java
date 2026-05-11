/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.product.entity.domain.Product;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.entity.shared.ProductSearchCond;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 6.
 */
@Repository
public interface ProductMapper {
    
    int insertProduct(@Param("product") Product product);
    
    int updateProduct(@Param("product") Product product);
    
    Product selectProduct(@Param("id") String id);
    
    int countProductByProductSearchCond(@Param("searchCond") ProductSearchCond searchCond);
    
    List<Product> selectProductByProductSearchCond(@Param("searchCond") ProductSearchCond searchCond);

	List<ProductPrice> selectProductByProductType(@Param("productType") String productType);

}
