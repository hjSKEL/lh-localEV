-- certificate-domain 테이블 생성

-- TB_CACS001: 충전소 인증서 (CsCert)
CREATE TABLE IF NOT EXISTS TB_CACS001 (
    CT_ID           CHAR(17)     NOT NULL COMMENT '인증서 ID (ex: 20231109170310123)',
    CS_ID           VARCHAR(14)  DEFAULT NULL COMMENT '충전기 ID',
    CM_NM           VARCHAR(250) DEFAULT NULL COMMENT 'Common Name',
    FILE_LOC        VARCHAR(50)  DEFAULT NULL COMMENT '파일 위치',
    CT_ST           CHAR(6)      DEFAULT NULL COMMENT '인증서 상태 (공통코드 CAST00)',
    EXP_DT          DATETIME     DEFAULT NULL COMMENT '만료 일시',
    CA_TP           CHAR(1)      DEFAULT NULL COMMENT 'CA 타입 (R:ROOT, S:SubCA)',
    REG_DT          DATETIME     NOT NULL COMMENT '등록 일시',
    REG_ID          CHAR(9)      NOT NULL COMMENT '등록자 ID',
    UPD_DT          DATETIME     NOT NULL COMMENT '수정 일시',
    UPD_ID          CHAR(9)      NOT NULL COMMENT '수정자 ID',
    PRIMARY KEY (CT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='충전소 인증서';

-- TB_CACR001: 충전소 인증서 CRL (CsCertCrl)
CREATE TABLE IF NOT EXISTS TB_CACR001 (
    CT_ID           CHAR(17)     NOT NULL COMMENT '인증서 ID',
    CS_ID           VARCHAR(14)  DEFAULT NULL COMMENT '충전기 ID',
    CM_NM           VARCHAR(250) DEFAULT NULL COMMENT 'Common Name',
    FILE_LOC        VARCHAR(50)  DEFAULT NULL COMMENT '파일 위치',
    CT_ST           CHAR(6)      DEFAULT NULL COMMENT '인증서 상태',
    EXP_DT          DATETIME     DEFAULT NULL COMMENT '만료 일시',
    REG_DT          DATETIME     NOT NULL COMMENT '등록 일시',
    REG_ID          CHAR(9)      NOT NULL COMMENT '등록자 ID',
    UPD_DT          DATETIME     NOT NULL COMMENT '수정 일시',
    UPD_ID          CHAR(9)      NOT NULL COMMENT '수정자 ID',
    PRIMARY KEY (CT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='충전소 인증서 CRL';

-- TB_CACU001: 사용자 인증서 (CustomerCert)
CREATE TABLE IF NOT EXISTS TB_CACU001 (
    EMAID           VARCHAR(14)  NOT NULL COMMENT 'eMaid',
    PCID            VARCHAR(20)  DEFAULT NULL COMMENT 'EV PCID',
    CUT_ID          CHAR(9)      NOT NULL COMMENT '사용자 ID',
    SN              VARCHAR(100) NOT NULL COMMENT '인증서 시리얼 번호',
    SUB_DN          VARCHAR(100) NOT NULL COMMENT 'Subject DN',
    XSD_MSG_DEF_NMSP VARCHAR(20) DEFAULT NULL COMMENT 'XSD Message Definition Namespace',
    CERT_VAL_FROM   CHAR(8)      DEFAULT NULL COMMENT '유효 시작일 (YYYYMMDD)',
    CERT_VAL_TO     CHAR(8)      DEFAULT NULL COMMENT '유효 종료일 (YYYYMMDD)',
    STAT            CHAR(6)      DEFAULT NULL COMMENT '상태 (CERT01~CERT05)',
    OCSP_RESP_URL   VARCHAR(50)  DEFAULT NULL COMMENT 'OCSP Responder URL',
    REG_DT          DATETIME     NOT NULL COMMENT '등록 일시',
    REG_ID          CHAR(9)      NOT NULL COMMENT '등록자 ID',
    UPD_DT          DATETIME     NOT NULL COMMENT '수정 일시',
    UPD_ID          CHAR(9)      NOT NULL COMMENT '수정자 ID',
    PRIMARY KEY (EMAID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='사용자 인증서';

-- TB_CACR002: 사용자 인증서 CRL (CustomerCertCrl)
-- CustomerCertCrl extends CustomerCert -> 동일 구조
CREATE TABLE IF NOT EXISTS TB_CACR002 (
    EMAID           VARCHAR(14)  NOT NULL COMMENT 'eMaid',
    PCID            VARCHAR(20)  DEFAULT NULL COMMENT 'EV PCID',
    CUT_ID          CHAR(9)      NOT NULL COMMENT '사용자 ID',
    SN              VARCHAR(100) NOT NULL COMMENT '인증서 시리얼 번호',
    SUB_DN          VARCHAR(100) NOT NULL COMMENT 'Subject DN',
    XSD_MSG_DEF_NMSP VARCHAR(20) DEFAULT NULL COMMENT 'XSD Message Definition Namespace',
    CERT_VAL_FROM   CHAR(8)      DEFAULT NULL COMMENT '유효 시작일 (YYYYMMDD)',
    CERT_VAL_TO     CHAR(8)      DEFAULT NULL COMMENT '유효 종료일 (YYYYMMDD)',
    STAT            CHAR(6)      DEFAULT NULL COMMENT '상태 (CERT01~CERT05)',
    OCSP_RESP_URL   VARCHAR(50)  DEFAULT NULL COMMENT 'OCSP Responder URL',
    REG_DT          DATETIME     NOT NULL COMMENT '등록 일시',
    REG_ID          CHAR(9)      NOT NULL COMMENT '등록자 ID',
    UPD_DT          DATETIME     NOT NULL COMMENT '수정 일시',
    UPD_ID          CHAR(9)      NOT NULL COMMENT '수정자 ID',
    PRIMARY KEY (EMAID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='사용자 인증서 CRL';
