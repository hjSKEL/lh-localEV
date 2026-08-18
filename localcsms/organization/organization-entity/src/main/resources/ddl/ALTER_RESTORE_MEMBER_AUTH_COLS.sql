-- TB_CUCU002 (고객관리)에서 유실된 인증 관련 컬럼 복구
-- OCPP 충전기 인증(AuthorizeBean/StartTransactionBean)이 실시간으로 참조하는 컬럼이므로
-- 다른 PC에서의 스키마 변경으로 삭제된 것을 원복한다.
ALTER TABLE TB_CUCU002
    ADD COLUMN CUT_MNG_CD varchar(6) NOT NULL DEFAULT 'MEMK01' COMMENT '고객관리코드' AFTER CUT_CRD_NO;

ALTER TABLE TB_CUCU002
    ADD COLUMN CUT_GRD_CD char(6) NOT NULL DEFAULT 'MEMB02' COMMENT '고객등급코드' AFTER CUT_MNG_CD;

-- TB_CUCU001 (고객)에서 유실된 V2X 계약 컬럼 복구
-- OCPP 2.1 AuthorizeResponse.allowedEnergyTransfer 정책 응답에 실사용되는 컬럼.
-- CO_ID는 이미 CX_ID(단지)로 대체되어 컬럼 자체가 사라졌으므로 CX_ID 뒤에 붙인다.
ALTER TABLE TB_CUCU001
    ADD COLUMN V2X_CONTRACT_YN char(1) NOT NULL DEFAULT 'N' COMMENT 'V2X 서비스 가입 여부' AFTER CX_ID;

ALTER TABLE TB_CUCU001
    ADD COLUMN ALLOWED_ENERGY_TRANSFER varchar(200) NULL COMMENT '계약상 허용 energy transfer modes (CSV)' AFTER V2X_CONTRACT_YN;
