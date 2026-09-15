-- TB_SYCO001 공통코드 추가: 충전현황(RECS00)에 RECS04(취소)/RECS05(결제) 누락
-- kr.co.kevit.localcsms.common.util.enumtype.charger.RechargingStatus 에는 정의돼 있으나
-- 공통코드 테이블에 없어 화면에서 CD_NM 대신 코드값(RECS04/RECS05)이 그대로 표출되던 버그 수정

INSERT INTO TB_SYCO001 (CD, HI_CD, CD_NM, CD_NM_EN, ORD_PRI, CD_DES) VALUES
('RECS04', 'RECS00', '취소', 'Cancel',  4, '충전기 충전취소'),
('RECS05', 'RECS00', '결제', 'Payment', 5, '충전기 결제완료');
