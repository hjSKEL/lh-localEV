-- TB_ORCX001.CX_NM: VARCHAR(60) 단지명 -> INT(4자리) 단지코드로 타입 변경
-- 기존 데이터가 4자리 숫자가 아닌 값(문자 단지명 등)을 담고 있다면 이 ALTER 전에 별도 정비 필요

ALTER TABLE TB_ORCX001
    MODIFY COLUMN CX_NM INT NOT NULL COMMENT '단지명(4자리 숫자)';
