-- ============================================================
-- TB_CHCF001 : 충전기 설정정보 (OCPP 1.6 Configuration Key)
-- ============================================================
CREATE TABLE TB_CHCF001 (
    CP_ID       CHAR(6)         NOT NULL    COMMENT '충전소ID',
    CS_ID       CHAR(2)         NOT NULL    COMMENT '충전기ID',
    CONF_KEY    VARCHAR(50)     NOT NULL    COMMENT '설정키 (e.g. SupportedFeatureProfiles)',
    CONF_VAL    VARCHAR(500)    NULL        COMMENT '설정값',
    READONLY_YN CHAR(1)         NOT NULL    DEFAULT 'Y' COMMENT '읽기전용여부 (Y/N)',
    UPD_DT      DATETIME        NOT NULL    COMMENT '최종수정일시',
    PRIMARY KEY (CP_ID, CS_ID, CONF_KEY)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='충전기 설정정보 (OCPP 1.6)';

CREATE INDEX IDX_CHCF001_01 ON TB_CHCF001 (CP_ID, CS_ID);
