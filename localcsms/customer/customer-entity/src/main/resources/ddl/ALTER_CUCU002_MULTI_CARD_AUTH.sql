-- TB_CUCU002: CUT_ID 단일 PK 제거, CUT_CRD_NO를 PK로 전환
-- 목적: 고객(세대) 1명이 등록한 여러 회원카드(TB_CUCA001, 최대 5장)가
--       각각 독립적으로 실시간 충전인증(TB_CUCU002)을 통과하도록 한다.
-- 영향 없음 확인: ocpp16-daemon AuthorizeBean/StartTransactionBean은 CUT_CRD_NO로만
--       조회하므로 이 변경으로 코드 수정 불필요.
-- 사전확인: 아래 쿼리 결과가 0이어야 NOT NULL 변경이 안전함.
--   SELECT COUNT(*) FROM TB_CUCU002 WHERE CUT_CRD_NO IS NULL;
ALTER TABLE TB_CUCU002 MODIFY COLUMN CUT_CRD_NO varchar(16) NOT NULL COMMENT '고객카드번호';
ALTER TABLE TB_CUCU002 DROP PRIMARY KEY;
ALTER TABLE TB_CUCU002 DROP INDEX CUT_CRD_NO;
ALTER TABLE TB_CUCU002 ADD PRIMARY KEY (CUT_CRD_NO);
ALTER TABLE TB_CUCU002 ADD KEY IX_TB_CUCU002_CUT_ID (CUT_ID);
