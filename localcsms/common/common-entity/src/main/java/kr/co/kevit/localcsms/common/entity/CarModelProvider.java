/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity;

import kr.co.kevit.localcsms.common.domain.CarModel;
import kr.co.kevit.localcsms.common.shared.CarModelSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

import java.util.List;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 9. 10.
 */
public interface CarModelProvider {

    void registerCarModel(CarModel model);

    void modifyCarModel(CarModel model);

    CarModel retrieveCarModel(String modelId);

    List<CarModel> retrieveCarModelByCarModelSearchCond(CarModelSearchCond searchCond);

    Page<CarModel> retrieveCarModelAllByCondition(CarModelSearchCond searchCond);
}
