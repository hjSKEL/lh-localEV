/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.recharging;

import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.share.JsonResultSet;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.Reservation;
import kr.co.kevit.localcsms.recharger.entity.shared.ReservationSearchCond;
import kr.co.kevit.localcsms.recharger.process.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Reservation Resource (API)
 */
@RestController
@RequestMapping("ws/reservation")
public class ReservationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationResource.class);

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/list")
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public Page<Reservation> searchReservationList(ReservationSearchCond searchCond) {
        return reservationService.retrieveReservationList(searchCond);
    }

    @GetMapping("/detail/{id}")
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public Reservation searchReservationDetail(@PathVariable("id") Long id) {
        return reservationService.retrieveReservationDetail(id);
    }

    @PostMapping
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet registerReservation(@RequestBody Reservation reservation) {
        User loginUser = SessionManager.getLoginUser();
        Writer writer = new Writer(loginUser.getUserId());
        reservation.setWriter(writer);
        try {
            reservationService.registerReservation(reservation);
        } catch (Exception e) {
            LOGGER.error("registerReservation error", e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }

    @PostMapping("/cancel")
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet modifyReservationCancel(@RequestBody Map<String, Object> params) {
        Long rsvId = Long.valueOf(params.get("rsvId").toString());
        User loginUser = SessionManager.getLoginUser();
        try {
            reservationService.modifyReservationCancel(rsvId, loginUser.getUserId());
        } catch (Exception e) {
            LOGGER.error("modifyReservationCancel error", e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }
}
