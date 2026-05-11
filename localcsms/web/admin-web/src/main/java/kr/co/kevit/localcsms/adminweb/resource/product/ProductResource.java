/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.product;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.share.JsonResultSet;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.product.entity.domain.Product;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.entity.shared.ProductSearchCond;
import kr.co.kevit.localcsms.product.process.ProductService;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 2.
 */
@RestController
@RequestMapping("ws/product")
public class ProductResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductResource.class);

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public Page<Product> findProductByProductSearchCond(ProductSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/product/search, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<Product> resultSet = null;
        try {
            resultSet = productService.retrieveProductByProductSearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product/search, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product/search, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    @RequestMapping(value = "/detail/{productId}", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public Product getProductDetail(@PathVariable("productId") String productId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/product/detail/{}, GET, DATA : {}", loginUser.getUserId(), accessIp, productId);
        Product result = null;
        try {
            result = productService.retrieveProduct(productId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product/detail/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, productId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product/detail/{}, GET, FAIL", loginUser.getUserId(), accessIp, productId);
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
    
    @RequestMapping(value = "/exist/{productId}", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet existProductId(@PathVariable("productId") String productId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/product/exist/{}, GET", loginUser.getUserId(), accessIp, productId);
        try {
            Product product = productService.retrieveProduct(productId);
            if(product != null) {
                LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product/exist/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, productId);
                return new JsonResultSet(ResultStatus.SUCCESS, product.getId());
            }else {
                LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product/exist/{}, GET, FAIL", loginUser.getUserId(), accessIp, productId);
                return new JsonResultSet(ResultStatus.FAIL);
            }
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product/exist/{}, GET, FAIL", loginUser.getUserId(), accessIp, productId);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
    }

    @RequestMapping(value = "/prodType/{prodType}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN"})
    public Product getProductByProdType(@PathVariable("prodType") String prodType, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/product/prodType/{}, GET", loginUser.getUserId(), accessIp, prodType);
        Product product = null;
        try {
            product = productService.retrieveProduct(prodType);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product/prodType/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, prodType);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product/prodType/{}, GET, FAIL", loginUser.getUserId(), accessIp, prodType);
            LOGGER.error(e.getMessage(), e);
        }
        return product;
    }

    @RequestMapping(method = RequestMethod.POST)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet registerProduct(@RequestBody Product product, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/product, POST, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(product));
        try {
            product.setWriter(new Writer(loginUser.getUserId()));
            productService.registerProduct(product);            
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product, POST, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product, POST, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS, product.getId());
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet modifyProduct(@RequestBody Product product, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/product, PUT, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(product));
        try {
            product.setWriter(new Writer(loginUser.getUserId()));
            productService.modifyProduct(product);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product, PUT, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product, PUT, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS, product.getId());
    }
    
    /**
     * 요금제 단가 정보 삭제처리
     *
     * @param seq
     * @param product
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet removeProductPrice(@PathVariable("id") String id, @RequestBody ProductPrice productPrice, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/product/{}, DELETE, DATA:{}", loginUser.getUserId(), accessIp, id, new Gson().toJson(productPrice));
        try {
            productPrice.getWriter().setUpdUserId(loginUser.getUserId());
            productPrice.getWriter().setUpdateDate(new Date());
            productPrice.setId(id);
            productService.removeEmployee(productPrice);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product/{}, DELETE, SUCCESS", loginUser.getUserId(), accessIp, id);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/product/{}, DELETE, FAIL", loginUser.getUserId(), accessIp, id);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }
}