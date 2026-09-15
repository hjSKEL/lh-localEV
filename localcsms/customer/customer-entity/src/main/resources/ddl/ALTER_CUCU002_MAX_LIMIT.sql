-- TB_CUCU002: 회원 기본 충전 한도(금액/에너지/시간/SoC) 컬럼 추가.
-- CustomerMgt.java(maxCost/maxEnergy/maxTime/maxSoC)와 customerMgt_sql.xml은 이미 이 컬럼들을 참조하도록
-- 병합돼 있었으나(origin 커밋 7031216 "골든슈트."), 정작 컬럼 추가 DDL이 커밋에 빠져 있어 원격 DB에는
-- 컬럼이 없는 상태였음 — 이대로면 회원카드 등록/수정 시 SQL 에러 발생. 재실행 안전하지 않음(1회만 실행).

ALTER TABLE TB_CUCU002
  ADD COLUMN MAX_COST   DECIMAL(12,2) DEFAULT NULL COMMENT '회원 기본 충전한도(금액, OCPP TransactionLimitType.maxCost)' AFTER PRNT_CRD_NO,
  ADD COLUMN MAX_ENERGY DECIMAL(12,2) DEFAULT NULL COMMENT '회원 기본 충전한도(에너지 kWh, maxEnergy)' AFTER MAX_COST,
  ADD COLUMN MAX_TIME   INT           DEFAULT NULL COMMENT '회원 기본 충전한도(시간 분, maxTime)' AFTER MAX_ENERGY,
  ADD COLUMN MAX_SOC    INT           DEFAULT NULL COMMENT '회원 기본 충전한도(SoC %, maxSoC)' AFTER MAX_TIME;
