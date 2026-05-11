/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.process;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.product.entity.domain.Product;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.entity.shared.ProductSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 14.
 */
public class ProductServiceTest extends AbstractTestCase{
    
    @Autowired
    private ProductService service;
    
    @Autowired
    private ProductPriceService priceService;

    private Product registerProduct() {
        Product product = new Product();
        product.setId("PDLO01");
        product.setName("완속 단일단가");
        product.setWriter(new Writer("E00000001"));
        List<ProductPrice> prices = new ArrayList<>();
        ProductPrice arg0 = new ProductPrice();
        arg0.setProductType(product.getId());
        arg0.setSeq(1);
        arg0.makeProductId();
        arg0.setFee(350.5);
        arg0.setStartDt("20230101");
        arg0.setEndDt("99991231");
        arg0.setWriter(new Writer("E00000001"));
        prices.add(arg0);
        product.setPrices(prices);
        service.registerProduct(product);
        return product;
    }
    
    @Test
    public void testRegisterProduct() {
        Product product = registerProduct();
        assertNotNull(product);
    }
    
    @Test
    public void testModifyProduct() {
        Product product = registerProduct();
        ProductPrice arg0 = new ProductPrice();
        arg0.setProductType(product.getId());
        arg0.setSeq(2);
        arg0.makeProductId();
        arg0.setFee(350.5);
        arg0.setStartDt("20230101");
        arg0.setEndDt("99991231");
        arg0.setWriter(new Writer("E00000001"));
        product.getPrices().add(arg0);
        service.modifyProduct(product);
    }
    
    @Test
    public void testRetrieveProduct() {
        Product product = registerProduct();
        Product newProduct = service.retrieveProduct(product.getId());
        assertNotNull(newProduct);
    }
    
    @Test
    public void testRetrieveProductByProductSearchCond() {
        //
        Product product = registerProduct();
        ProductSearchCond searchCond = new ProductSearchCond();
        searchCond.setName(product.getName());
        Page<Product> resultSet = service.retrieveProductByProductSearchCond(searchCond);
        assertTrue(resultSet.getCriteria().getTotalItemCount() > 0);
    }
    
    @Test
    public void testRetrieveProductPriceInCache() {
        Product product = registerProduct();
        ProductPrice price = priceService.retrieveProductPriceInCache(product.getPrices().get(0).getId());
        assertNotNull(price);
    }
    
    @Test
    public void testRetrieveProductPrice() {
        Product product = registerProduct();
        ProductPrice price = priceService.retrieveProductPrice(product.getPrices().get(0).getId());
        assertNotNull(price);
    }
    
    @Test
    public void testRetrieveLiveProductPriceByType() {
        Product product = registerProduct();
        ProductPrice price = priceService.retrieveLiveProductPriceByType(product.getId(), new Date());
        assertNotNull(price);
    }

}
