-- TB_SYCF001 (시스템설정) - 싱글턴 1행(ID=1)
CREATE TABLE TB_SYCF001 (
    ID                                          INT          NOT NULL COMMENT '고정값 1(싱글턴)',
    CHARGING_TIME_BASED_ON_ABNORMAL_CHARGING    INT                   COMMENT '이상충전 판단 충전시간(시간)',
    ABNORMAL_CHARGE_AMOUNT                      INT                   COMMENT '이상충전 판단 충전량(Wh)',
    EMPLACEMENT                                 VARCHAR(250)          COMMENT '설치장소',
    SERVER_ADDRESS                              VARCHAR(250)          COMMENT '서버주소',
    USE_HOME_NET                                VARCHAR(8)            COMMENT '홈넷적용(TB_SYCO001.HNET00 하위 코드)',
    FILE_STORE_PATH                             VARCHAR(250)          COMMENT '파일저장경로',
    CUSTOMER_SUPPORT                            VARCHAR(250)          COMMENT '고객지원',
    DB_BACKUP_PATH                              VARCHAR(250)          COMMENT 'DB백업 경로',
    DB_BACKUP_CYCLE                             INT                   COMMENT 'DB백업 주기(일)',
    DB_BACKUP_RETENTION_PERIOD                  INT                   COMMENT 'DB백업 보관기간(일)',
    REG_DT                                      DATETIME              COMMENT '등록일',
    REG_ID                                      VARCHAR(20)           COMMENT '등록자',
    UPD_DT                                      DATETIME              COMMENT '수정일',
    UPD_ID                                      VARCHAR(20)           COMMENT '수정자',
    PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='시스템설정';

INSERT INTO TB_SYCF001 (ID) VALUES (1);
