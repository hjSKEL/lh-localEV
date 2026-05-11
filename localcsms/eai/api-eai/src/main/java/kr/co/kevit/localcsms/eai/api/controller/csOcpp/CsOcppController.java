package kr.co.kevit.localcsms.eai.api.controller.csOcpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/csOcpp")
public class CsOcppController {

    private static final Logger log = LoggerFactory.getLogger(CsOcppController.class);

    @PostMapping("/api/v1/plugin")
    public ResponseEntity<Void> plugin(
            @RequestParam(required = false) String evse_id,
            @RequestParam(required = false) String connector_id,
            @RequestParam(required = false) Boolean halfway) {
        log.info("[csOcpp] /plugin evse_id={}&connector_id={}&halfway={}", evse_id, connector_id, halfway);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/plugout")
    public ResponseEntity<Void> plugout(
            @RequestParam(required = false) String evse_id,
            @RequestParam(required = false) String connector_id) {
        log.info("[csOcpp] /plugout evse_id={}&connector_id={}", evse_id, connector_id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/parkingbay")
    public ResponseEntity<Void> parkingbay(
            @RequestParam(required = false) Boolean occupied) {
        log.info("[csOcpp] /parkingbay occupied={}", occupied);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/authorize")
    public ResponseEntity<Void> authorize(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String evse_id,
            @RequestParam(required = false) String connector_id) {
        log.info("[csOcpp] /authorize id={}&type={}&evse_id={}&connector_id={}", id, type, evse_id, connector_id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/reboot")
    public ResponseEntity<Void> reboot() {
        log.info("[csOcpp] /reboot");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/state")
    public ResponseEntity<Void> state(
            @RequestParam(required = false) Boolean faulted,
            @RequestParam(required = false) Boolean unlock_failed,
            @RequestParam(required = false) Boolean refused_local_auth_list,
            @RequestParam(required = false) String charging_limit,
            @RequestParam(required = false) String evse_id,
            @RequestParam(required = false) String connector_id) {
        log.info(
                "[csOcpp] /state faulted={}&unlock_failed={}&refused_local_auth_list={}&charging_limit={}&evse_id={}&connector_id={}",
                faulted, unlock_failed, refused_local_auth_list, charging_limit, evse_id, connector_id);
        return ResponseEntity.ok().build();
    }
}