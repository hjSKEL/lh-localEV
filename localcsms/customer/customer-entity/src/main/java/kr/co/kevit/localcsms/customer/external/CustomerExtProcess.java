/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.external;

import kr.co.kevit.localcsms.customer.entity.domain.Customer;

/**
 *
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 11. 13.
 */
public interface CustomerExtProcess {

    Customer retrieveCustomerByUserId(String userId);
}
