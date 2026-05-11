/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.dao;

import kr.co.kevit.localcsms.common.domain.CarModel;
import kr.co.kevit.localcsms.common.shared.CarModelSearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 9. 10.
 */
@Repository
public interface CarModelMapper {

    public int insertCarModel(@Param("model") CarModel model);

    public int updateCarModel(@Param("model") CarModel model);

    public CarModel selectCarModel(@Param("modelId") String modelId);

    public List<CarModel> selectCarModelByCarModelSearchCond(@Param("searchCond") CarModelSearchCond searchCond);

    public List<CarModel> selectCarModelAllByCondition(@Param("searchCond") CarModelSearchCond searchCond);

    public int countCarModelByCondition(@Param("searchCond") CarModelSearchCond searchCond);

    public String selectMaxCarModelId();
}
