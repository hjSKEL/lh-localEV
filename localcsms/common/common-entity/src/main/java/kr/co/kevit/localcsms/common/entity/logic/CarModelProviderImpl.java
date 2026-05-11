/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.logic;

import kr.co.kevit.localcsms.common.domain.CarModel;
import kr.co.kevit.localcsms.common.shared.CarModelSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.entity.CarModelProvider;
import kr.co.kevit.localcsms.common.entity.dao.CarModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 9. 10.
 */
@Component
public class CarModelProviderImpl implements CarModelProvider {

    @Autowired
    private CarModelMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCarModel(CarModel carModel) {
        //
        String maxCarModelId = mapper.selectMaxCarModelId();
        carModel.makeCarModelId(maxCarModelId);
        mapper.insertCarModel(carModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyCarModel(CarModel model) {
        //
        mapper.updateCarModel(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CarModel retrieveCarModel(String modelId) {
        //
        return mapper.selectCarModel(modelId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CarModel> retrieveCarModelByCarModelSearchCond(CarModelSearchCond searchCond) {
        //
        return mapper.selectCarModelByCarModelSearchCond(searchCond);
    }

    @Override
    public Page<CarModel> retrieveCarModelAllByCondition(CarModelSearchCond searchCond) {
        //
        int totalItemCount = mapper.countCarModelByCondition(searchCond);
        Page<CarModel> resultSet = new Page<>();
        searchCond.setTotalItemCount(totalItemCount);
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0) {
            return resultSet;
        }
        resultSet.setResult(mapper.selectCarModelAllByCondition(searchCond));
        return resultSet;
    }
}
