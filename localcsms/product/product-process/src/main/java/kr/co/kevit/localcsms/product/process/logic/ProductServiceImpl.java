/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.product.process.logic;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.domain.Employee;
import kr.co.kevit.localcsms.organization.external.EmployeeExtProcess;
import kr.co.kevit.localcsms.product.entity.ProductProvider;
import kr.co.kevit.localcsms.product.entity.domain.Product;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.entity.shared.ProductSearchCond;
import kr.co.kevit.localcsms.product.process.ProductService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 9. 6.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductProvider provider;
    
    @Autowired
    private EmployeeExtProcess emplExtProcess;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerProduct(Product product) {
        // 
        provider.registerProduct(product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyProduct(Product product) {
        // 
        provider.modifyProduct(product);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Product retrieveProduct(String productId) {
        // 
        Product product = provider.retrieveProduct(productId);
        if(product != null) {            
            Employee employee = emplExtProcess.retrieveEmployeeById(product.getWriter().getRegUserId());
            product.getWriter().setRegUserName(employee.getEmplName());
            employee = emplExtProcess.retrieveEmployeeById(product.getWriter().getUpdUserId());
            product.getWriter().setUpdUserName(employee.getEmplName());
        }
        return product;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<Product> retrieveProductByProductSearchCond(ProductSearchCond searchCond) {
        // 
        Page<Product> resultSet = provider.retrieveProductByProductSearchCond(searchCond);
        Map<String, String> nameMap = new HashMap<>();
        Employee employee = null;
        for(Product product : resultSet.getResult()) {
            String empId = product.getWriter().getRegUserId();
            String empName = nameMap.get(empId);
            if(empName == null) {                
                employee = emplExtProcess.retrieveEmployeeById(empId);
                empName = employee.getEmplName();
                nameMap.put(empId, empName);
            }
            product.getWriter().setRegUserName(empName);
            empId = product.getWriter().getUpdUserId();
            empName = nameMap.get(empId);
            if(empName == null) {                
                employee = emplExtProcess.retrieveEmployeeById(empId);
                empName = employee.getEmplName();
                nameMap.put(empId, empName);
            }
            product.getWriter().setUpdUserName(empName);
        }
        return resultSet;
    }

	@Override
	public void removeEmployee(ProductPrice productPrice) {
		//
		ProductPrice ckProductPrice = provider.retrieveProductPriceById(productPrice.getId());
		if(ckProductPrice != null) {
			LocalDate temp = LocalDate.now();
			LocalDate nextDay = temp.plusDays(1);
			Date nextDt = java.sql.Date.valueOf(nextDay);
			
			if(ckProductPrice.getStartDt().compareTo(DateUtils.dateToString(nextDt, DateUtils.DATE_FORMAT_WITHOUT_DASH)) >= 0) {
				provider.removeProduct(productPrice.getId());
			}
		}
		productPrice.setSeq(ckProductPrice.getSeq()-1);
		productPrice.setProductType(ckProductPrice.getProductType());
		ProductPrice updateProductPrice = provider.retrieveProductByProductPrice(productPrice);
		updateProductPrice.setEndDt("99991231");
		provider.modifyProductPrice(updateProductPrice);
		
	}

}
