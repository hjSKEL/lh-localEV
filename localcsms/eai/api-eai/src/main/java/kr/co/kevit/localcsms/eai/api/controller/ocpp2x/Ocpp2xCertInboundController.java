package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerCert;
import kr.co.kevit.localcsms.customer.process.CustomerCertService;
import kr.co.kevit.ocpp201.enumtype.Iso15118EVCertificateStatusEnumType;

/**
 * OCPP 2.x 인증서(ISO 15118 PnC) Inbound Callback Controller (daemon → api-eai).
 *
 * <p>
 * {@link Ocpp2xCertificateController}(외부 → api-eai → 충전기로 <b>명령 하달</b>,
 * Daemon2xClient.send)
 * 와 방향이 반대다. 본 컨트롤러는 ocpp20-daemon 이 <b>CS 로부터 받은</b> Get15118EVCertificate 요청을
 * 그대로 전달받아 처리하고, 동기 타입 응답({@code Get15118EVCertificate})을 그대로 반환한다.
 * (스마트차징 inbound 인 {@link Ocpp2xInboundController} 와 동일한 daemon → api-eai 방향)
 * </p>
 */
@RestController
@RequestMapping("/ocpp2x/inbound/certificate")
public class Ocpp2xCertInboundController {

    private static final Logger log = LoggerFactory.getLogger(Ocpp2xCertInboundController.class);

    private final CustomerCertService customerCertService;

    /** CustomerCert 상태(STAT) */
    private static final String STATUS_REQUESTED = "CERT01"; // 생성/갱신요청
    private static final String STATUS_COMPLETED = "CERT02"; // 생성/갱신완료
    private static final String STATUS_EV_INSTALLED = "CERT03"; // EV설치
    /** 상태 변경 audit 용 IF 사용자 ID */
    private static final String IF_USER_ID = "E00000001";

    public Ocpp2xCertInboundController(CustomerCertService customerCertService) {
        this.customerCertService = customerCertService;
    }

    /**
     * CS → CSMS: ISO 15118 PnC Get15118EVCertificate.
     *
     * POST /ocpp2x/inbound/certificate/get15118EVCertificate/{cpCsId}
     *
     * @param cpCsId  충전기 식별자("cpId-csId")
     * @param request OCPP 2.1 Get15118EVCertificate 요청
     * @return OCPP 2.1 Get15118EVCertificate 응답
     */
    @PostMapping("/get15118EVCertificate/{cpCsId}")
    public ResponseEntity<kr.co.kevit.ocpp201.response.Get15118EVCertificate> get15118EVCertificate(
            @PathVariable String cpCsId,
            @RequestBody kr.co.kevit.ocpp201.request.Get15118EVCertificate request) {
        log.info(
                "[OCPP2X CERT INBOUND] Get15118EVCertificate cpCsId={} action={} schemaVersion={} maxChains={} prioritizedEMAIDs={} exiRequest={}",
                cpCsId, request.getAction(), request.getIso15118SchemaVersion(),
                request.getMaximumContractCertificateChains(), request.getPrioritizedEMAIDs(), request.getExiRequest());

        String pcid = "OEMProvCert";

        kr.co.kevit.ocpp201.response.Get15118EVCertificate response = new kr.co.kevit.ocpp201.response.Get15118EVCertificate();

        // PCID 로 계약인증서 목록 조회(비페이징). 결과 없으면 Failed.
        List<CustomerCert> customerCerts = customerCertService.retrieveCustomerCertByPcid(pcid);
        if (customerCerts == null || customerCerts.isEmpty()) {
            log.warn("[OCPP2X CERT INBOUND] PCID 일치 계약인증서 없음. cpCsId={} pcid={}", cpCsId, pcid);
            response.setStatus(Iso15118EVCertificateStatusEnumType.Failed);
            return ResponseEntity.ok(response);
        }

        // request eMAID 우선순위에 따라 설치 가능(CERT01/CERT02) 계약인증서 1건 선택
        CustomerCert selected = selectInstallableCert(request.getPrioritizedEMAIDs(), customerCerts);
        if (selected == null) {
            log.warn("[OCPP2X CERT INBOUND] 설치 가능(CERT01/CERT02) 계약인증서 없음. cpCsId={} pcid={}", cpCsId, pcid);
            response.setStatus(Iso15118EVCertificateStatusEnumType.Failed);
            return ResponseEntity.ok(response);
        }

        // 선택된 계약인증서 → CERT03(EV설치)로 상태 변경 + 영속화
        selected.setStatus(STATUS_EV_INSTALLED);
        selected.setWriter(new Writer(IF_USER_ID));
        customerCertService.modifyCustomerCert(selected);

        // 남은 계약 수 = CERT01/CERT02 개수 (방금 CERT03 으로 바꾼 선택 건은 제외됨)
        int remainingContracts = 0;
        for (CustomerCert c : customerCerts) {
            if (STATUS_REQUESTED.equals(c.getStatus()) || STATUS_COMPLETED.equals(c.getStatus())) {
                remainingContracts++;
            }
        }

        switch (request.getAction()) {
            case Install:
                // urn:iso:15118:20:2022:MsgDef
                if (!"urn:iso:15118:2:2013:MsgDef".equals(request.getIso15118SchemaVersion())) {
                    response.setRemainingContracts(remainingContracts);
                    response.setExiResponse(// iso15118-20
                            "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");
                } else {
                    response.setExiResponse(// iso15118-2
                            "+YGTUlJQ2h6Q0NBaTZnQXdJQkFnSVVQTGZWQUthM1RwYUxGT3VyYjlXZEk3L1dnVG93Q2dZSUtvWkl6ajBFQXdJd1p6RVRNQkVHQTFVRUF3d0tUVThnVTNWaVEwRWdNVEVRTUE0R0ExVUVDd3dIVDBOVVZDQk5UekVkTUJzR0ExVUVDZ3dVVDNCbGJpQkRhR0Z5WjJVZ1FXeHNhV0Z1WTJVeEN6QUpCZ05WQkFZVEFrNU1NUkl3RUFZS0NaSW1pWlB5TEdRQkdSWUNUVTh3SUJjTk1qWXdNakl3TURrek1EQTRXaGdQTWpBMk1UQXpNRGd3T1RNd01EaGFNR2N4RXpBUkJnTlZCQU1NQ2sxUElGTjFZa05CSURJeEVEQU9CZ05WQkFzTUIwOURWRlFnVFU4eEhUQWJCZ05WQkFvTUZFOXdaVzRnUTJoaGNtZGxJRUZzYkdsaGJtTmxNUXN3Q1FZRFZRUUdFd0pPVERFU01CQUdDZ21TSm9tVDhpeGtBUmtXQWsxUE1Ga3dFd1lIS29aSXpqMENBUVlJS29aSXpqMERBUWNEUWdBRVpsN2puU2g1Z2hGMk50a3M1WVFoZzR1T01rVmY5bTRwVEQwdnozRklJRExrc2hZUzhwU3Jub0x5V20yekFrQjMrY0JhYlFSL0RRaEU3azJabFJkMW1LT0J0VENCc2pBU0JnTlZIUk1CQWY4RUNEQUdBUUgvQWdFQU1BNEdBMVVkRHdFQi93UUVBd0lCeGpBZEJnTlZIUTRFRmdRVVBNWUZDYWRBMkYya0pTUTBnTHIzRFR2Zjk1QXdUQVlJS3dZQkJRVUhBUUVFUURBK01Ed0dDQ3NHQVFVRkJ6QUJoakJvZEhSd09pOHZiMk56Y0M1dlkzUjBMbTl3Wlc1amFHRnlaMlZoYkd4cFlXNWpaUzV2Y21jdmJXOWZjM1ZpWHpFd0h3WURWUjBqQkJnd0ZvQVVDZ1pJM21KWDhOK2k3RnVybEFhMzJkZHMySWd3Q2dZSUtvWkl6ajBFQXdJRFJ3QXdSQUlnYk4vR09ZWWZ4VVRBRC9oY2xBT0I3YVdOU3dYN2U4dFBKSlpoZHVqMk1INENJR1NBbmJPM2RjTkVlREsxeU1teU9WcnczRjVyVUNEL2xUbUJRaFdid1Rxc0cACKYBQAMfUDJqSkobSiIaGgtJkzoLukoSCzpKqrmJolOK06OKkhPL0YHKi0mCojOTKrNaciLDm7obOspKW3rSS9NRgioLuku609IqomoSKjoJiqoqC7u6WqKpwzqracuzIhoSIoqiKopqCaI6CYqqKhu7ukKhgnKqshoScqPSKyJqE5o6CYqqKhs7uqqhmhNjE0oSIwo6M8rRkqs6irvDmwq6M6rJkqvCG9IKUhM6crISCsqiC1mqamqSS7oqCspaGtJLa0rSg8piOooSOpLKGqKpw7pKExpya1LLumtSS7pqI1vSaiIJmrtDOoJrUgmSaqIL0mojO7p6omu6aiMjCmo7G8Ir0gqSEzpyshIKamobWYqCSjJxistachJKIivCKiIKehM6crISC5pqEYHKIrIyizqiqcPCQqILEhM6crISC3pqMinLutK5ozqJk0NDG2sjYkoqM5sSO2NDE2pzYmqLm7oaisoisoqKOiu6UnqiIiqaahIKOhs7appTe2qhw0vDWgqTWroLWYqCajNbuiu6ykJbetJL01GCGgqKykpbetJL01GCIgqLGiKLOgopixmhshJKO7u6cbrLeoHKyhIaycs6mkI7ciOaU0ozscrTkguLwZNDscMaYps7onmqubMKujtDcmmSE4sSOhsbOxILYXoxsiKLKpOjibqCC3Gbwip70bJ6E6IiGhOaogqaEzpyskKSahILMcIqGiIKOgqKQXoLOioSagmiOgmKqyIjuioRe7qKKgu6ShITUgsiEzpyskKJoiozOoqqGzrSSZtqUsHCcVtJujOrk2ILCZmTIyOZkks7upu6ykpbusoSEoqqQgqKKiqD0gnKaiOaOhobmjoKiqoyE9IKE0NJy3siQpO6e0nDsxGSc9MaGauyyZqRgmNpy7rSuatTCjozytGSs0MSO8OCyrmrUtKZq7MbaxuzErnLMxtpy7MiIgsyEzpyskKaaio6Igq7OhKKOhoKWaKrKmJySXmzIhpyYrIL0itaKtKqIkKiCloTOzuLQ1tSeoKKiiILOnJSCiISOgtKKgt5WrmzcxGbO9O5shKzCxPCU2qCGXmbGyrLanozQ6laosKqyiOyE4ubUzGiGkqKGkLTopKbEntqgmrSylOxk8pTwxPCi7IbazMRist6mrJZu0OSWsshk0uLqonp6rWAFSYBQAMDgCkGmJgYGBgYGBgYGBmxpNREV5TXpRMU5qY3dNVEl6TkRVMk53PT1rAHxMAoAGBwBSDTEwMDAwMDAwMDAxYonJhaYIaCWmBgYIqMbmiMilpkw");
                }
                response.setStatus(Iso15118EVCertificateStatusEnumType.Accepted);
                break;
            case Update:
                response.setExiResponse(
                        "gJgCAABAgMEBQYHQgArACJMAoAGBwBSDTEwMDAwMDAwMDAyLgAQmAUADHtAyakpKGzIiGhoLSns6C7pKEgs6SqsqatMyKlvLY0mSeaMbk1NBmbqRW4szG7uam3u6GzrKSlt60kvTUYIqC7pLutPSKqJqEio6CYqqKgu7ulqiqcM6qZqzSomCKzprUiqKagmiOgmKqiobu7pCoYJyqrIaEnKj0isiahOaOgmKqiobO7qqoZoTYxNKEiMKOjPK0ZKrOoq7w5sKujOqyZKrwhvSClITOnKyEgrKogtZqmpqkku6KgrKWhrSS2tK0oPKYjqKEjqSyhqiqcO6ShMacmtSy7prUku6aiNb0moiCaK7QzqCa1IJkmqiC9JqIzu6eqJrumojQwpqOkvCO9IK0hM6crISCmpqK1mqamKiEiKKmYO6aiISMpNTGYKTWquia1IqimoJojoJiqoqG7u6QqGCcqqyGhJyo9IrImoTmjoJiqoqGzu6qqGaE2MTShIjCjozytGSqzqKu8ObCrozqsmSq8IrUgqKEzt6U1tLClNZekua0goq0jM6UnKj0hLSahJqOhPLijqaaaHKCzoqOhobijqaaaHKC7oqQgmCSgoSEypiKgtCGXtDc0OjC3tLKqrLKlqqWsPBylOrKXtKMyoyEwubennCYhohk6vSgnJ7C6NS0iNjq6Nqw5IpuasaspsxoYpSa3Gq0qm6s8uTMYOyoYPDGjtTOwnDuzsLu7oiCsoispGCogqKQXoSCku6CiIKehM6crJCicISCzHCKhIKahoJWzu6QorKIrKRgnoSEsoqMjJTijKzuxKbIwm6uzKro7NDghIysrFbm8KpumorujoaG5o6CoqqMhO6KhISKgu6g1IJwhM7O5ITOioyEosbugrKy7sKQpGDGiN7smGRy1MZmgurEZJxgyIZq7MaOrOqyZNDQxtrI2LKu8ObCrozqsmSq6sRmlNyYZGLssGacYrLYcPKahHCOgmKqyJLuorKahMKCjIj0joSi2tyinNDI4Iaq1pyShmxy7mBuZl7KopqC3o6GhuKOpppocoSCmoaCYMaCmoqihpKKxrLuhrK0lNiQZupmtOCw4pbs8MqkoqjWkpqWsMZo3sbM2q7wpODmprKIgtKE2mKCjOJm2t7sbtrcVqqWptaahrCOamzOkGCshGCGrHDaispWmlZmlPLW7np6jgEWmAUADC4AEUwCgAY+YGTUlJQ2h6Q0NBaTZnQXdJQkFnSVVQTGZWQUthM1RwYUxGT3VyYjlXZEk3L1dnVG93Q2dZSUtvWkl6ajBFQXdJd1p6RVRNQkVHQTFVRUF3d0tUVThnVTNWaVEwRWdNVEVRTUE0R0ExVUVDd3dIVDBOVVZDQk5UekVkTUJzR0ExVUVDZ3dVVDNCbGJpQkRhR0Z5WjJVZ1FXeHNhV0Z1WTJVeEN6QUpCZ05WQkFZVEFrNU1NUkl3RUFZS0NaSW1pWlB5TEdRQkdSWUNUVTh3SUJjTk1qWXdNakl3TURrek1EQTRXaGdQTWpBMk1UQXpNRGd3T1RNd01EaGFNR2N4RXpBUkJnTlZCQU1NQ2sxUElGTjFZa05CSURJeEVEQU9CZ05WQkFzTUIwOURWRlFnVFU4eEhUQWJCZ05WQkFvTUZFOXdaVzRnUTJoaGNtZGxJRUZzYkdsaGJtTmxNUXN3Q1FZRFZRUUdFd0pPVERFU01CQUdDZ21TSm9tVDhpeGtBUmtXQWsxUE1Ga3dFd1lIS29aSXpqMENBUVlJS29aSXpqMERBUWNEUWdBRVpsN2puU2g1Z2hGMk50a3M1WVFoZzR1T01rVmY5bTRwVEQwdnozRklJRExrc2hZUzhwU3Jub0x5V20yekFrQjMrY0JhYlFSL0RRaEU3azJabFJkMW1LT0J0VENCc2pBU0JnTlZIUk1CQWY4RUNEQUdBUUgvQWdFQU1BNEdBMVVkRHdFQi93UUVBd0lCeGpBZEJnTlZIUTRFRmdRVVBNWUZDYWRBMkYya0pTUTBnTHIzRFR2Zjk1QXdUQVlJS3dZQkJRVUhBUUVFUURBK01Ed0dDQ3NHQVFVRkJ6QUJoakJvZEhSd09pOHZiMk56Y0M1dlkzUjBMbTl3Wlc1amFHRnlaMlZoYkd4cFlXNWpaUzV2Y21jdmJXOWZjM1ZpWHpFd0h3WURWUjBqQkJnd0ZvQVVDZ1pJM21KWDhOK2k3RnVybEFhMzJkZHMySWd3Q2dZSUtvWkl6ajBFQXdJRFJ3QXdSQUlnYk4vR09ZWWZ4VVRBRC9oY2xBT0I3YVdOU3dYN2U4dFBKSlpoZHVqMk1INENJR1NBbmJPM2RjTkVlREsxeU1teU9WcnczRjVyVUNEL2xUbUJRaFdid1Rxc0cACKYBQAMfUDJqSkobSiIaGgtJkzoLukoSCzpKqrmJolOK06OKkhPL0YHKi0mCojOTKrNaciLDm7obOspKW3rSS9NRgioLuku609IqomoSKjoJiqoqC7u6WqKpwzqracuzIhoSIoqiKopqCaI6CYqqKhu7ukKhgnKqshoScqPSKyJqE5o6CYqqKhs7uqqhmhNjE0oSIwo6M8rRkqs6irvDmwq6M6rJkqvCG9IKUhM6crISCsqiC1mqamqSS7oqCspaGtJLa0rSg8piOooSOpLKGqKpw7pKExpya1LLumtSS7pqI1vSaiIJmrtDOoJrUgmSaqIL0mojO7p6omu6aiMjCmo7G8Ir0gqSEzpyshIKamobWYqCSjJxistachJKIivCKiIKehM6crISC5pqEYHKIrIyizqiqcPCQqILEhM6crISC3pqMinLutK5ozqJk0NDG2sjYkoqM5sSO2NDE2pzYmqLm7oaisoisoqKOiu6UnqiIiqaahIKOhs7appTe2qhw0vDWgqTWroLWYqCajNbuiu6ykJbetJL01GCGgqKykpbetJL01GCIgqLGiKLOgopixmhshJKO7u6cbrLeoHKyhIaycs6mkI7ciOaU0ozscrTkguLwZNDscMaYps7onmqubMKujtDcmmSE4sSOhsbOxILYXoxsiKLKpOjibqCC3Gbwip70bJ6E6IiGhOaogqaEzpyskKSahILMcIqGiIKOgqKQXoLOioSagmiOgmKqyIjuioRe7qKKgu6ShITUgsiEzpyskKJoiozOoqqGzrSSZtqUsHCcVtJujOrk2ILCZmTIyOZkks7upu6ykpbusoSEoqqQgqKKiqD0gnKaiOaOhobmjoKiqoyE9IKE0NJy3siQpO6e0nDsxGSc9MaGauyyZqRgmNpy7rSuatTCjozytGSs0MSO8OCyrmrUtKZq7MbaxuzErnLMxtpy7MiIgsyEzpyskKaaio6Igq7OhKKOhoKWaKrKmJySXmzIhpyYrIL0itaKtKqIkKiCloTOzuLQ1tSeoKKiiILOnJSCiISOgtKKgt5WrmzcxGbO9O5shKzCxPCU2qCGXmbGyrLanozQ6laosKqyiOyE4ubUzGiGkqKGkLTopKbEntqgmrSylOxk8pTwxPCi7IbazMRist6mrJZu0OSWsshk0uLqonp6rWAFSYBQAMDgCkGmJgYGBgYGBgYGBmxpNREV5TXpRMU5qY3dNVEl6TkRVMk53PT1rAHxMAoAGBwBSDTEwMDAwMDAwMDAxYonJhaYIaCWmBgYIqMbmiMilpk1gC6mAUADGBmbAA==");
                response.setStatus(Iso15118EVCertificateStatusEnumType.Accepted);
                break;
            default:
                response.setStatus(Iso15118EVCertificateStatusEnumType.Failed);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 설치 가능(CERT01/CERT02) 계약인증서 1건 선택.
     * prioritizedEMAIDs 가 있으면 그 우선순위 순서대로 일치하는 첫 건, 없으면 목록 순서대로 첫 건.
     *
     * @return 선택된 CustomerCert. 없으면 null
     */
    private CustomerCert selectInstallableCert(List<String> prioritizedEMAIDs, List<CustomerCert> customerCerts) {
        if (prioritizedEMAIDs != null && !prioritizedEMAIDs.isEmpty()) {
            for (String emaid : prioritizedEMAIDs) {
                for (CustomerCert c : customerCerts) {
                    if (emaid != null && emaid.equals(c.geteMaid()) && isInstallable(c)) {
                        return c;
                    }
                }
            }
            return null;
        }
        for (CustomerCert c : customerCerts) {
            if (isInstallable(c)) {
                return c;
            }
        }
        return null;
    }

    /** 상태가 설치 가능(CERT01 생성/갱신요청 또는 CERT02 생성/갱신완료)인지 */
    private boolean isInstallable(CustomerCert cert) {
        return STATUS_REQUESTED.equals(cert.getStatus()) || STATUS_COMPLETED.equals(cert.getStatus());
    }
}
