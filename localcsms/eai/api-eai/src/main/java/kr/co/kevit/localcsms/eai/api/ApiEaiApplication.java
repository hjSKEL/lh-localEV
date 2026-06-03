package kr.co.kevit.localcsms.eai.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * API EAI — SUT API CSMS for OCPP 1.6 구현체
 *
 * OCTT(Open Charge Testing Tool)가 호출하는 26개 REST 엔드포인트를 제공한다.
 * 각 엔드포인트는 수신된 요청을 ocpp16-daemon 으로 위임하여
 * OCPP 1.6 메시지를 충전기로 전달한다.
 *
 * 실행:
 *   java -jar api-eai.jar --server.port=8082 --api-eai.daemon-url=http://host:port
 *
 * 엔드포인트 그룹:
 *   Configuration  : /reset /getConfiguration /changeConfiguration /updatePassword /clearCache
 *   LocalList      : /getLocalListVersion /sendLocalList
 *   Transaction    : /requestStartTransaction /requestStopTransaction
 *   RemoteControl  : /unlockConnector /triggerMessage /extendedTriggerMessage /changeAvailability
 *   SmartCharging  : /setChargingProfile /setChargingProfiles /clearChargingProfile /getCompositeSchedule
 *   Reservation    : /reserveNow /cancelReservation
 *   Firmware       : /updateFirmware /signedUpdateFirmware
 *   Certificate    : /getInstalledCertificateIds /installCertificate /deleteCertificate
 *   Diagnostics    : /getDiagnostics /getLog
 */
@SpringBootApplication(scanBasePackages = "kr.co.kevit.localcsms")
@MapperScan("kr.co.kevit.localcsms.**.dao")
@EnableScheduling
public class ApiEaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiEaiApplication.class, args);
    }
}
