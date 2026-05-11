/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.process;

import kr.co.kevit.localcsms.common.domain.CarModel;
import kr.co.kevit.localcsms.common.shared.CarModelSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

import java.util.List;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 1. 23.
 */
public interface CarModelService {
	
    void registerCarModel(CarModel model);
    
    void modifyCarModel(CarModel model);
    
    CarModel retrieveCarModel(String modelId);
    
    List<CarModel> retrieveCarModelByCarModelSearchCond(CarModelSearchCond searchCond);

    Page<CarModel> retrieveCarModelAllByCondition(CarModelSearchCond searchCond);
}
