/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process.logic;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.dao.ReservationMapper;
import kr.co.kevit.localcsms.recharger.entity.domain.Reservation;
import kr.co.kevit.localcsms.recharger.entity.shared.ReservationSearchCond;
import kr.co.kevit.localcsms.recharger.process.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reservation Service implementation
 */
@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Transactional(readOnly = true)

    @Override
    public Reservation retrieveReservationDetail(Long rsvId) {
        return reservationMapper.selectReservationById(rsvId);
    }

    @Transactional(readOnly = true)

    @Override
    public Page<Reservation> retrieveReservationList(ReservationSearchCond searchCond) {
        int totalItemCount = reservationMapper.countReservationBySearchCond(searchCond);
        Page<Reservation> resultSet = new Page<>();
        searchCond.setTotalItemCount(totalItemCount);
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0) {
            resultSet.setResult(new ArrayList<>(0));
            return resultSet;
        }

        List<Reservation> result = reservationMapper.selectReservationList(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

    @Override
    public void registerReservation(Reservation reservation) {
        if (reservation.getWriter() == null) {
            reservation.setWriter(new Writer("SYSTEM"));
        }
        reservation.setStatus("RSVT01");
        int affected = reservationMapper.insertReservation(reservation);
        if (affected == 0) {
            throw new KEVITException("RSVT001", "예약 등록에 실패하였습니다.");
        }
    }

    @Override
    public void modifyReservationCancel(Long rsvId, String userId) {
        Reservation reservation = reservationMapper.selectReservationById(rsvId);
        if (reservation == null) {
            throw new KEVITException("RSVT002", "예약 정보가 존재하지 않습니다.");
        }
        if ("RSVT02".equals(reservation.getStatus())) {
            throw new KEVITException("RSVT003", "이미 취소된 예약입니다.");
        }
        reservation.setStatus("RSVT02");
        Writer writer = reservation.getWriter();
        if (writer == null) {
            writer = new Writer(userId);
        } else {
            writer.setUpdUserId(userId);
            writer.setUpdateDate(new Date());
        }
        reservation.setWriter(writer);
        int affected = reservationMapper.updateReservationStatus(reservation);
        if (affected == 0) {
            throw new KEVITException("RSVT004", "예약 취소 처리에 실패하였습니다.");
        }
    }
}
