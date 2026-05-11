/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.process.logic;

import kr.co.kevit.localcsms.common.domain.CarModel;
import kr.co.kevit.localcsms.common.entity.CarModelProvider;
import kr.co.kevit.localcsms.common.shared.CarModelSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.process.CarModelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 23.
 *
 */
@Service
@Transactional
public class CarModelServiceImpl implements CarModelService {

    @Autowired
    private CarModelProvider provider;

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void registerCarModel(CarModel model) {
        //
        provider.registerCarModel(model);
    }

    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void modifyCarModel(CarModel model) {
        //
        provider.modifyCarModel(model);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public CarModel retrieveCarModel(String modelId) {
        //
        return provider.retrieveCarModel(modelId);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<CarModel> retrieveCarModelByCarModelSearchCond(CarModelSearchCond searchCond) {
        //
        return provider.retrieveCarModelByCarModelSearchCond(searchCond);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<CarModel> retrieveCarModelAllByCondition(CarModelSearchCond searchCond) {
        //
        return provider.retrieveCarModelAllByCondition(searchCond);
    }
}
