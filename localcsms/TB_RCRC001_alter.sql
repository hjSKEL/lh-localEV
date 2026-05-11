-- ============================================================
-- TB_RCRC001 : 주차/케이블 시간 컬럼 추가
-- ============================================================
ALTER TABLE TB_RCRC001
    ADD COLUMN PK_ST_DT   DATETIME NULL COMMENT '주차시작시간'   AFTER ED_CA_ELE_NRG,
    ADD COLUMN PK_ED_DT   DATETIME NULL COMMENT '출차시간'       AFTER PK_ST_DT,
    ADD COLUMN CBL_ST_DT  DATETIME NULL COMMENT '케이블연결시간'   AFTER PK_ED_DT,
    ADD COLUMN CBL_ED_DT  DATETIME NULL COMMENT '케이블연결종료시간' AFTER CBL_ST_DT;
