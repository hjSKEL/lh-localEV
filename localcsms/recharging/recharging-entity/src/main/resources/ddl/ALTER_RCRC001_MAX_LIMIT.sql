-- TB_RCRC001: 트랜잭션 최대 한도(금액/시간/SoC) 컬럼 추가.
-- Recharging.java(maxCost/maxTime/maxSoC)와 recharging_sql.xml은 이미 이 컬럼들을 참조하도록
-- 병합돼 있었으나, MAX_ENERGY만 DDL이 반영되고 나머지 세 컬럼 추가 DDL이 누락돼 원격 DB에는
-- 컬럼이 없는 상태였음 — 이대로면 StartTransaction 처리 시 SQL 에러 발생. 재실행 안전하지 않음(1회만 실행).

ALTER TABLE TB_RCRC001
  ADD COLUMN MAX_COST DECIMAL(12,2) DEFAULT NULL COMMENT '트랜잭션 최대 금액 한도(OCPP TransactionLimitType.maxCost)' AFTER MAX_ENERGY,
  ADD COLUMN MAX_TIME INT           DEFAULT NULL COMMENT '트랜잭션 최대 시간 한도(분, maxTime)' AFTER MAX_COST,
  ADD COLUMN MAX_SOC  INT           DEFAULT NULL COMMENT '트랜잭션 최대 SoC 한도(%, maxSoC)' AFTER MAX_TIME;
