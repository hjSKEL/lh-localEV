-- 로컬 인증 목록 관리 테이블

-- TB_CHCU001: 충전기별 로컬 인증 목록 현황
CREATE TABLE IF NOT EXISTS TB_CHCU001 (
    CP_ID           CHAR(6)      NOT NULL COMMENT '충전소 ID',
    CS_ID           CHAR(2)      NOT NULL COMMENT '충전기 ID',
    CUR_VER         INT          DEFAULT 0 COMMENT '현재 버전',
    LAST_VER        INT          DEFAULT 0 COMMENT '마지막 요청 버전',
    LAST_SYNC_DT    DATETIME     NOT NULL COMMENT '마지막 동기화 일시',
    STATUS          CHAR(6)      NOT NULL DEFAULT 'SYNC01' COMMENT '동기화 상태 (SYNC01:등록, SYNC02:요청, SYNC03:성공, SYNC04:실패)',
    LMT_MEM_CNT     INT          DEFAULT 10 COMMENT '최대 멤버 수',
    REG_DT          DATETIME     NOT NULL COMMENT '등록 일시',
    REG_ID          CHAR(9)      NOT NULL COMMENT '등록자 ID',
    UPD_DT          DATETIME     NOT NULL COMMENT '수정 일시',
    UPD_ID          CHAR(9)      NOT NULL COMMENT '수정자 ID',
    PRIMARY KEY (CP_ID, CS_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='충전기 로컬 인증 목록 현황';

-- TB_CHCU002: 로컬 인증 버전 관리
CREATE TABLE IF NOT EXISTS TB_CHCU002 (
    CP_ID           CHAR(6)      NOT NULL COMMENT '충전소 ID',
    CS_ID           CHAR(2)      NOT NULL COMMENT '충전기 ID',
    VER             INT          NOT NULL COMMENT '버전',
    REG_DT          DATETIME     NOT NULL COMMENT '등록 일시',
    PRIMARY KEY (CP_ID, CS_ID, VER)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='충전기 로컬 인증 버전';

-- TB_CHCU003: 로컬 인증 카드 목록
CREATE TABLE IF NOT EXISTS TB_CHCU003 (
    CP_ID           CHAR(6)      NOT NULL COMMENT '충전소 ID',
    CS_ID           CHAR(2)      NOT NULL COMMENT '충전기 ID',
    VER             INT          NOT NULL COMMENT '버전',
    CUT_ID          CHAR(9)      NOT NULL COMMENT '고객 ID',
    CUT_CRD_NO      VARCHAR(16)  NOT NULL COMMENT '카드번호',
    EXP_DT          DATETIME     NOT NULL COMMENT '만료 일시',
    PRNT_CRD_NO     VARCHAR(16)  DEFAULT NULL COMMENT '부모 카드번호',
    PRIMARY KEY (CP_ID, CS_ID, VER, CUT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='충전기 로컬 인증 카드 목록';
