-- TB_CUCU001: 세대(CX_ID+DONG+HO)당 대표 고객(세대주) 1명만 허용

-- 사전 정리: 동일 CX_ID+DONG+HO에 여러 CUT_ID가 있던 기존 데이터(7건 중 4건)는
-- 가장 먼저 등록된 CUT_ID만 남기고 나머지는 TB_CUCU002.DEL_YN='Y' 처리 + DONG/HO를 NULL로 비움
-- (CUT_ID, 거래이력은 보존; 2026-08-19 운영 DB에 이미 적용됨)

ALTER TABLE TB_CUCU001
    ADD CONSTRAINT UK_CUCU001_CX_DONG_HO UNIQUE (CX_ID, DONG, HO);
