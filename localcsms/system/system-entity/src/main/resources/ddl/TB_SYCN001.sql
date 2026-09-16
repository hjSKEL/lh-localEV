-- TB_SYCN001 (연결설정) - 싱글턴 1행(ID=1)
CREATE TABLE TB_SYCN001 (
    ID                     INT          NOT NULL COMMENT '고정값 1(싱글턴)',
    LH_CSMS_ADDRESS        VARCHAR(250)          COMMENT 'LH CSMS 주소',
    CPO_CSMS_ADDRESS       VARCHAR(250)          COMMENT 'CPO CSMS 주소',
    LOCAL_SYSTEM_ID        VARCHAR(250)          COMMENT '로컬시스템 아이디',
    LOCAL_SYSTEM_SN        VARCHAR(250)          COMMENT '로컬시스템 시리얼넘버',
    CPO_SYSTEM_ID          VARCHAR(250)          COMMENT 'CPO 시스템 아이디',
    LOCAL_OPERATION_TYPE   VARCHAR(8)            COMMENT '로컬서버 운영모드(TB_SYCO001.OPMD00 하위 코드)',
    REG_DT                 DATETIME              COMMENT '등록일',
    REG_ID                 VARCHAR(20)           COMMENT '등록자',
    UPD_DT                 DATETIME              COMMENT '수정일',
    UPD_ID                 VARCHAR(20)           COMMENT '수정자',
    PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='연결설정';

INSERT INTO TB_SYCN001 (ID) VALUES (1);

-- 이미 생성된 환경(테이블 존재)에는 위 CREATE TABLE 이 재실행되지 않으므로 아래 ALTER 를 별도 적용할 것.
-- ALTER TABLE TB_SYCN001 ADD COLUMN CPO_SYSTEM_ID VARCHAR(250) COMMENT 'CPO 시스템 아이디' AFTER LOCAL_SYSTEM_SN;
