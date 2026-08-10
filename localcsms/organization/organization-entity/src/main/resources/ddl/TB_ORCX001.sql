-- TB_ORCX001 (단지)
CREATE TABLE TB_ORCX001 (
    CX_ID       CHAR(9)      NOT NULL COMMENT '단지아이디',
    CX_NM       VARCHAR(60)  NOT NULL COMMENT '단지명',
    CX_ADDR     VARCHAR(200)          COMMENT '단지주소',
    CO_ID       CHAR(9)      NOT NULL COMMENT '관리법인아이디',
    CX_US_YN    CHAR(1)      NOT NULL DEFAULT 'Y' COMMENT '단지사용가능여부',
    DEL_YN      CHAR(1)      NOT NULL DEFAULT 'N' COMMENT '삭제여부',
    DEL_DT      DATETIME              COMMENT '삭제일',
    MEMO        VARCHAR(200)          COMMENT '메모',
    REG_DT      DATETIME              COMMENT '등록일',
    REG_ID      VARCHAR(20)           COMMENT '등록자',
    UPD_DT      DATETIME              COMMENT '수정일',
    UPD_ID      VARCHAR(20)           COMMENT '수정자',
    PRIMARY KEY (CX_ID),
    CONSTRAINT FK_ORCX001_CO_ID FOREIGN KEY (CO_ID) REFERENCES TB_ORCP001 (CO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='단지';
