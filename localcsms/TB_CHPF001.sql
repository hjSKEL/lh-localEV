-- ============================================================
-- TB_CHPF001 : 충전 프로파일 (OCPP 2.x SetChargingProfile)
-- TB_CHPF002 : 충전 스케줄 JSON (ChargingSchedule 목록 JSON)
--
-- 공통 코드 참조
--   PRPS     : CHPP00 (CHPP01:ChargingStationMaxProfile / CHPP02:TxDefaultProfile
--                    / CHPP03:TxProfile / CHPP04:PriorityCharging
--                    / CHPP05:LocalGeneration / CHPP06:ChargingStationExternalConstraints)
--   KIND     : CHKD00 (CHKD01:Absolute / CHKD02:Recurring / CHKD03:Relative)
--   RCRNC_KIND : (D)Daily / (W)Weekly  (Recurring 일 때만 사용)
--
-- Sequence   : SEQ_ChargingProfile (PRFL_ID 자동 생성)
-- ============================================================

-- ------------------------------------------------------------
-- SEQ_ChargingProfile  (PRFL_ID 채번용)
-- MariaDB 10.3+ Sequence
-- ------------------------------------------------------------
CREATE SEQUENCE IF NOT EXISTS SEQ_ChargingProfile
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    NOCACHE
    NOCYCLE;

-- ------------------------------------------------------------
-- TB_CHPF001 : 충전 프로파일 헤더
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS TB_CHPF001 (
    PRFL_ID         INT           NOT NULL             COMMENT '프로파일 ID (SEQ_ChargingProfile)',
    CP_ID           CHAR(6)       NOT NULL             COMMENT '충전소 ID',
    CS_ID           CHAR(2)       NOT NULL             COMMENT '충전기 ID',
    EVSE_ID         INT           NOT NULL DEFAULT 0   COMMENT 'EVSE ID (0=충전기 전체)',
    STACK_LVL       INT           NOT NULL             COMMENT '스택 레벨 (우선순위)',
    PRPS            CHAR(6)       NOT NULL             COMMENT '프로파일 목적 공통코드 CHPP00',
    KIND            CHAR(6)           NULL             COMMENT '프로파일 종류 공통코드 CHKD00',
    RCRNC_KIND      CHAR(1)           NULL             COMMENT '반복 종류 (D:Daily / W:Weekly)',
    VALID_FROM      DATETIME          NULL             COMMENT '프로파일 유효 시작',
    VALID_TO        DATETIME          NULL             COMMENT '프로파일 유효 종료',
    RC_ID           VARCHAR(36)       NULL             COMMENT '연관 트랜잭션 ID (TxProfile용)',
    CS_STATUS       CHAR(6)           NULL             COMMENT '충전기 응답 상태 공통코드',
    REG_DT          DATETIME      NOT NULL             COMMENT '등록 일시',
    REG_ID          CHAR(9)       NOT NULL             COMMENT '등록자 ID',
    UPD_DT          DATETIME      NOT NULL             COMMENT '수정 일시',
    UPD_ID          CHAR(9)       NOT NULL             COMMENT '수정자 ID',

    PRIMARY KEY (PRFL_ID),
    INDEX IX_CHPF001_CS         (CP_ID, CS_ID),
    INDEX IX_CHPF001_CS_EVSE    (CP_ID, CS_ID, EVSE_ID),
    INDEX IX_CHPF001_VALID_TO   (VALID_TO)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
  COMMENT='충전 프로파일 - OCPP2.x SetChargingProfile';

-- ------------------------------------------------------------
-- TB_CHPF002 : 충전 스케줄 JSON
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS TB_CHPF002 (
    PRFL_ID         INT           NOT NULL             COMMENT '프로파일 ID (TB_CHPF001.PRFL_ID)',
    SCHD_LIST_JSON  VARCHAR(5000) NOT NULL             COMMENT '충전 스케줄 목록 JSON',

    PRIMARY KEY (PRFL_ID),
    CONSTRAINT FK_CHPF002_PROFILE FOREIGN KEY (PRFL_ID)
        REFERENCES TB_CHPF001 (PRFL_ID)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
  COMMENT='충전 스케줄 JSON - ChargingScheduleType 목록';
