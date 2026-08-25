-- 목적: 카드별 독립 인증 전환(2026-08-24, ALTER_CUCU002_MULTI_CARD_AUTH.sql) 이전에 등록된
--       TB_CUCA001 카드 중 짝이 되는 TB_CUCU002 실시간 인증행이 없는 건을 백필한다.
-- registerMemberCard()의 정상 등록 흐름(CustomerCardServiceImpl)과 동일한 기본값으로 채움.
-- 카드의 현재 STOP_YN/STOP_DT를 그대로 반영(정지된 카드를 새로 활성 인증행으로 만들지 않음).
-- LEFT JOIN NULL 조건으로 이미 짝이 있는 카드는 자동 제외되므로 재실행해도 안전.
INSERT INTO TB_CUCU002 (
     CUT_ID, CUT_CRD_NO, CUT_MNG_CD, CUT_GRD_CD, STOP_YN, STOP_DT,
     REG_CERT_DT, DEL_YN, REG_DT, UPD_DT
)
SELECT A.CUT_ID, A.CUT_CRD_NO, 'MEMK01', 'MEMB02', A.STOP_YN, A.STOP_DT,
       A.REG_DT, 'N', A.REG_DT, A.REG_DT
  FROM TB_CUCA001 A
  LEFT JOIN TB_CUCU002 B ON A.CUT_CRD_NO = B.CUT_CRD_NO
 WHERE B.CUT_CRD_NO IS NULL;
