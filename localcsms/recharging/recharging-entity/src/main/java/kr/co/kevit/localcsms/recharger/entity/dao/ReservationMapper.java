/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.dao;

import kr.co.kevit.localcsms.recharger.entity.domain.Reservation;
import kr.co.kevit.localcsms.recharger.entity.shared.ReservationSearchCond;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * TB_RCRS001 Mapper
 */
@Mapper
public interface ReservationMapper {

    Reservation selectReservationById(@Param("rsvId") Long rsvId);

    List<Reservation> selectReservationList(@Param("searchCond") ReservationSearchCond searchCond);

    int countReservationBySearchCond(@Param("searchCond") ReservationSearchCond searchCond);

    int insertReservation(Reservation reservation);

    int updateReservationStatus(Reservation reservation);
}
