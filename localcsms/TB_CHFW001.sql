-- ============================================================
-- TB_CHFW001 : 충전기 펌웨어 업데이트 정보
-- ============================================================
CREATE TABLE TB_CHFW001 (
    CP_ID   CHAR(6)         NOT NULL    COMMENT '충전소ID',
    CS_ID   CHAR(2)         NOT NULL    COMMENT '충전기ID',
    URL     VARCHAR(255)    NULL        COMMENT '펌웨어 다운로드 URL',
    STATUS  CHAR(6)         NOT NULL    COMMENT '상태 (CSFW01:요청, CSFW02:다운로드중, CSFW03:다운로드완료, CSFW04:다운로드실패, CSFW05:설치중, CSFW06:설치완료, CSFW07:설치실패)',
    REQ_ID  CHAR(9)         NULL        COMMENT '요청자ID',
    REQ_DT  DATETIME        NOT NULL    COMMENT '요청일시',
    UPT_DT  DATETIME        NOT NULL    COMMENT '수정일시',
    PRIMARY KEY (CP_ID, CS_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='충전기 펌웨어 업데이트 정보';
