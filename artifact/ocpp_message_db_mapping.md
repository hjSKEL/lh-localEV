# OCPP 메시지별 검증 조건 → DB 테이블 매핑

대상: `ocpp/ocpp-daemon/ocpp16-localcsms-daemon` (OCPP 1.6, 충전기(CP) → CSMS 방향 메시지).
`bean/res/*`(CSMS → 충전기 원격제어 명령, 예: Reset/GetDiagnostics)는 반대 방향이라 DB 쓰기가 거의 없어 제외.

## 공통 (모든 메시지 이전)

| 단계 | 검증/로직 | 테이블 |
|---|---|---|
| WebSocket 연결 (Basic Auth) | `cpId-csId`로 `ChargingStation` 조회 후 `csPassword` 일치 확인 (`KevitBasicAuthBean`) | `TB_CHCS001` (조회) |
| 매 요청/응답 | 모든 req/res 페이로드를 그대로 로그 적재 (`OcppLogBeanImpl`) | `TB_RCLG001` INSERT |

## 메시지별

| 메시지 | 핵심 검증 조건 | 쓰는 테이블 |
|---|---|---|
| **BootNotification** | `ChargingStation` 미존재→Rejected / `useYn=N` or 상태정보 없음→Pending | `TB_CHCS005` UPDATE+`TB_CHCS006` INSERT(전 커넥터 상태 초기화), `TB_CHCS002` UPSERT(시리얼/모델/펌웨어/부팅시각) |
| **Heartbeat** | 270초 내 재요청이면 캐시 응답(DB 접근 안 함); 연결된 충전 세션이 완료(`RECS04/05`) 상태면 상태값 리셋 | `TB_CHCS005` UPDATE만(이력 없음, `modifyChargerStatusWithoutHis`) |
| **StatusNotification** | `timestamp` null이면 무시; 직전과 동일 상태면 캐시로 스킵(메모리 캐시, DB 미접근); connectorId로 대상 커넥터 특정 | `TB_CHCS005` UPDATE+`TB_CHCS006` INSERT (상태코드/에러코드/케이블상태 반영) |
| **Authorize** | `useYn=N`→Invalid; `idTag`(카드번호)로 `CustomerMgt` 미조회→Invalid, 미등록카드 이벤트(EVT0A2) 기록 후 반환 | `TB_CUCU002` 조회, `TB_CHCS005`/`TB_CHCS006`(단일 커넥터면 UPDATE+이력, 다중이면 이력만 INSERT) |
| **StartTransaction** | `idTag`로 `CustomerMgt` 미조회→Invalid(신규 충전 생성 안 함); 존재 시 요금정책(`ProductPrice`) 조회해 신규 충전 생성 | `TB_RCRC001` INSERT(신규 충전), `TB_CHCS005` UPDATE+`TB_CHCS006` INSERT(충전중 상태·카드번호·rechargingId 반영) |
| **MeterValues** | `transactionId=0` or meterValue 없음→무시; 150초 내 캐시면 스킵; 진행중 `Recharging`이 없거나 `transactionId` 불일치→무시; `timestamp` 없으면 무시 | `TB_RCRC001` UPDATE(사용량/요금 누적), `TB_CHCS005` UPDATE+`TB_CHCS006` INSERT(순간전력량/요금 갱신) |
| **StopTransaction** | `transactionId=0`→응답만; rechargingId 매칭되는 `ChargerStatusInfo` 없으면 별도 경로(`processWithoutStatus`, id로 직접 역산해 종료 처리); `Recharging` 없거나 timestamp 없으면 무시 | `TB_RCRC001` UPDATE(종료·최종요금 확정), `TB_CHCS005` UPDATE+`TB_CHCS006` INSERT(종료 상태 기록) 후 커넥터 초기화 UPDATE(이력없이) |
| **DiagnosticsStatusNotification** | 검증/저장 로직 없음 — debug 로그만 파싱, 응답만 반환 | 없음 |
| **FirmwareStatusNotification** | 위와 동일, 저장 없음 | 없음 |
| **LogStatusNotification** | 위와 동일, 저장 없음 | 없음 |

## 참고

- `TB_CHCS005`는 충전기 실시간 상태(1행/커넥터), `TB_CHCS006`은 그 변경 이력(append).
- `modifyChargerStatus`는 UPDATE+이력 INSERT를 같이 하고, `modifyChargerStatusWithoutHis`는 이력 없이 UPDATE만 함(Heartbeat처럼 잦은 갱신에서 이력 테이블 폭증 방지 목적).
- 소스 위치: `ocpp/ocpp-daemon/ocpp16-localcsms-daemon/src/main/java/kr/co/kevit/ocpp16/daemon/bean/req/`
