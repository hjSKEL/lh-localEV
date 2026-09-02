-- TB_SYCO001 공통코드 추가: 운영모드(OPMD00), 홈넷연결(HNET00)
-- 연결설정(TB_SYCN001.LOCAL_OPERATION_TYPE), 시스템설정(TB_SYCF001.USE_HOME_NET)에서 참조

-- 운영모드
INSERT INTO TB_SYCO001 (CD, HI_CD, CD_NM, CD_NM_EN, ORD_PRI, CD_DES) VALUES
('OPMD00', '',       '운영모드',     'Operation Mode', 0, '운영모드 그룹'),
('OPMD01', 'OPMD00', '로컬모드',     'Local Mode',      1, '운영모드-로컬모드'),
('OPMD02', 'OPMD00', 'CPO모드',      'CPO Mode',        2, '운영모드-CPO모드');

-- 홈넷연결
INSERT INTO TB_SYCO001 (CD, HI_CD, CD_NM, CD_NM_EN, ORD_PRI, CD_DES) VALUES
('HNET00', '',       '홈넷연결',      'Home Net',  0, '홈넷연결 그룹'),
('CM',     'HNET00', '코맥스',        'COMAX',     1, '홈넷연결-코맥스'),
('CC',     'HNET00', '코콤',          'KOCOM',     2, '홈넷연결-코콤'),
('HHT',    'HNET00', '현대에이치티',   'HYUNDAIHT', 3, '홈넷연결-현대에이치티'),
('HDC',    'HNET00', 'HDC랩스',       'HDC',       4, '홈넷연결-HDC랩스'),
('KD',     'HNET00', '경동나비엔',    'NAVIEN',    5, '홈넷연결-경동나비엔'),
('CVE',    'HNET00', '씨브이네트',    'CVNET',     6, '홈넷연결-씨브이네트');
