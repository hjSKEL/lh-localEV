-- ============================================================
-- TB_CHCF002 : 충전기 설정 변수 (OCPP 2.x GetVariables/GetBaseReport 결과)
-- TB_CHCF001 은 OCPP 1.6 용으로 예약
-- ============================================================
CREATE TABLE IF NOT EXISTS TB_CHCF002 (
    CP_ID       VARCHAR(6)    NOT NULL         COMMENT '충전소 ID',
    CS_ID       VARCHAR(2)    NOT NULL         COMMENT '충전기 ID',
    COMP_NM     VARCHAR(50)   NOT NULL         COMMENT 'Component.name',
    COMP_INST   VARCHAR(50)   NOT NULL DEFAULT '' COMMENT 'Component.instance (없으면 빈값)',
    EVSE_ID     INT           NOT NULL DEFAULT 0  COMMENT 'Component.evse.id (없으면 0)',
    CONN_ID     INT           NOT NULL DEFAULT 0  COMMENT 'Component.evse.connectorId (없으면 0)',
    VAR_NM      VARCHAR(50)   NOT NULL         COMMENT 'Variable.name',
    VAR_INST    VARCHAR(50)   NOT NULL DEFAULT '' COMMENT 'Variable.instance (없으면 빈값)',
    ATTR_TP     VARCHAR(20)   NOT NULL DEFAULT 'Actual'
                                               COMMENT 'Attribute 타입: Actual|Target|MinSet|MaxSet',
    ATTR_VAL    VARCHAR(2000)     NULL         COMMENT 'Attribute 값',
    ATTR_STAT   VARCHAR(30)   NOT NULL         COMMENT 'GetVariableStatus (Accepted/Rejected/UnknownComponent/...)',
    UPD_DT      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
                              ON UPDATE CURRENT_TIMESTAMP COMMENT '최종 갱신일시',

    PRIMARY KEY (CP_ID, CS_ID, COMP_NM, COMP_INST, EVSE_ID, CONN_ID, VAR_NM, VAR_INST, ATTR_TP),
    INDEX IX_CHCF002_CS   (CP_ID, CS_ID),
    INDEX IX_CHCF002_COMP (CP_ID, CS_ID, COMP_NM, EVSE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
  COMMENT='충전기 설정 변수 - OCPP2.x GetVariables 결과 (TB_CHCF001: OCPP1.6 예약)';
