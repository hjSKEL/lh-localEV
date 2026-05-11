/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.Reservation;
import kr.co.kevit.localcsms.recharger.entity.shared.ReservationSearchCond;

/**
 * Reservation Service interface
 */
public interface ReservationService {

    Reservation retrieveReservationDetail(Long rsvId);

    Page<Reservation> retrieveReservationList(ReservationSearchCond searchCond);

    void registerReservation(Reservation reservation);

    void modifyReservationCancel(Long rsvId, String userId);
}
