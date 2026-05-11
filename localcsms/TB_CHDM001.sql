-- ============================================================
-- TB_CHDM001 : 충전기 디스플레이 메시지 (OCPP 2.x SetDisplayMessage/GetDisplayMessages)
-- TB_CHDM002 : 충전기 디스플레이 메시지 내용 (다국어 MessageContent)
--
-- 공통 코드 참조
--   STAT    : DMST00 (DMST01:활성 / DMST02:대기 / DMST03:완료 / DMST04:취소
--                   / DMST05:유효기간만료 / DMST06:오류)
--   MSG_FRMT: DMFM00 (DMFM01:ASCII / DMFM02:HTML / DMFM03:URI / DMFM04:UTF8 / DMFM05:QRCODE)
--
-- Sequence   : SEQ_DisplayMessage (MSG_ID 자동 생성)
-- ============================================================

-- ------------------------------------------------------------
-- SEQ_DisplayMessage  (MSG_ID 채번용)
-- MariaDB 10.3+ Sequence
-- ------------------------------------------------------------
CREATE SEQUENCE IF NOT EXISTS SEQ_DisplayMessage
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    NOCACHE
    NOCYCLE;

-- ------------------------------------------------------------
-- TB_CHDM001 : 디스플레이 메시지 헤더
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS TB_CHDM001 (
    MSG_ID          INT           NOT NULL             COMMENT '메시지 ID (SEQ_DisplayMessage)',
    CP_ID           CHAR(6)       NOT NULL             COMMENT '충전소 ID',
    CS_ID           CHAR(2)       NOT NULL             COMMENT '충전기 ID',
    PRIORITY        VARCHAR(20)   NOT NULL             COMMENT '우선순위 (AlwaysFront / InFront / NormalCycle)',
    STAT            CHAR(6)           NULL             COMMENT '메시지 상태 공통코드 DMST00',
    ST_DT           DATETIME          NULL             COMMENT '표시 시작일시',
    EN_DT           DATETIME          NULL             COMMENT '표시 종료일시',
    RC_ID           VARCHAR(36)       NULL             COMMENT '연관 충전 트랜잭션 ID',
    DISP_NM         VARCHAR(50)       NULL             COMMENT '표시 Component.name',
    DISP_INST       VARCHAR(50)       NULL             COMMENT '표시 Component.instance',
    DISP_EVSE_ID    INT               NULL             COMMENT '표시 EVSE ID',
    DISP_CONN_ID    INT               NULL             COMMENT '표시 Connector ID',
    CS_STATUS       VARCHAR(20)       NULL             COMMENT '충전기 응답 상태 (Accepted / Unknown / ...)',
    REG_DT          DATETIME      NOT NULL             COMMENT '등록 일시',
    REG_ID          CHAR(9)       NOT NULL             COMMENT '등록자 ID',
    UPD_DT          DATETIME      NOT NULL             COMMENT '수정 일시',
    UPD_ID          CHAR(9)       NOT NULL             COMMENT '수정자 ID',

    PRIMARY KEY (MSG_ID),
    INDEX IX_CHDM001_CS       (CP_ID, CS_ID),
    INDEX IX_CHDM001_CS_STAT  (CP_ID, CS_ID, STAT),
    INDEX IX_CHDM001_EN_DT    (EN_DT)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
  COMMENT='충전기 디스플레이 메시지 - OCPP2.x SetDisplayMessage/GetDisplayMessages';

-- ------------------------------------------------------------
-- TB_CHDM002 : 디스플레이 메시지 내용 (다국어)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS TB_CHDM002 (
    MSG_ID          INT           NOT NULL             COMMENT '메시지 ID (TB_CHDM001.MSG_ID)',
    MSG_LANG        CHAR(2)       NOT NULL             COMMENT '언어 코드 (ko / en / ...)',
    MSG_FRMT        CHAR(6)       NOT NULL             COMMENT '메시지 포맷 공통코드 DMFM00',
    MSG_CONT        VARCHAR(512)  NOT NULL             COMMENT '메시지 내용',

    PRIMARY KEY (MSG_ID, MSG_LANG),
    CONSTRAINT FK_CHDM002_MSG FOREIGN KEY (MSG_ID)
        REFERENCES TB_CHDM001 (MSG_ID)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
  COMMENT='충전기 디스플레이 메시지 내용 - 다국어 MessageContent';
