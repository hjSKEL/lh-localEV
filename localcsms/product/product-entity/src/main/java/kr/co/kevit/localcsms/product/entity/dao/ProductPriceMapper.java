/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 10.
 */
@Repository
public interface ProductPriceMapper {
    
    int insertProductPrice(@Param("price") ProductPrice price);
    
    int deleteProductPrice(@Param("id") String id);
    
    int deleteProductPriceByType(@Param("productType") String productType);
    
    ProductPrice selectProductPriceById(@Param("id") String id);
    
    List<ProductPrice> selectProductPriceByType(@Param("productType") String productType);

	ProductPrice selectProductPriceByProductPrice(@Param("productPrice") ProductPrice productPrice);

	void updateProductPriceByProductPrice(@Param("productPrice") ProductPrice productPrice);
}
