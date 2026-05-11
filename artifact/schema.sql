-- Schema Export for Database: LOCAL_CSMS

-- Table structure for TB_CHBD001
DROP TABLE IF EXISTS `TB_CHBD001`;
CREATE TABLE `TB_CHBD001` (
  `ID` char(13) NOT NULL,
  `CP_ID` varchar(9) DEFAULT NULL,
  `CS_ID` varchar(2) DEFAULT NULL,
  `RCPT_DT` char(8) DEFAULT NULL,
  `RCPT_TM` char(6) DEFAULT NULL,
  `REP_DT` char(8) DEFAULT NULL,
  `REP_TM` char(6) DEFAULT NULL,
  `BD_STAT` char(6) DEFAULT NULL COMMENT 'BDST00',
  `REG_DT` datetime DEFAULT NULL,
  `REG_ID` char(9) DEFAULT NULL,
  `UPD_DT` datetime DEFAULT NULL,
  `UPD_ID` char(9) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='고장관리';

-- Table structure for TB_CHBD002
DROP TABLE IF EXISTS `TB_CHBD002`;
CREATE TABLE `TB_CHBD002` (
  `ID` char(13) NOT NULL,
  `CS_ER_CD` varchar(6) DEFAULT NULL,
  `BD_CONT` varchar(400) DEFAULT NULL,
  `CAR_MODEL_NM` varchar(10) DEFAULT NULL,
  `CS_CAT_CD` varchar(6) DEFAULT NULL,
  `RPT_NM` varchar(20) DEFAULT NULL,
  `RPT_ADDR` varchar(200) DEFAULT NULL,
  `RPT_PHN_NO` varchar(60) DEFAULT NULL,
  `REG_DT` datetime DEFAULT NULL,
  `REG_ID` char(9) DEFAULT NULL,
  `UPD_DT` datetime DEFAULT NULL,
  `UPD_ID` char(9) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- Table structure for TB_CHBD003
DROP TABLE IF EXISTS `TB_CHBD003`;
CREATE TABLE `TB_CHBD003` (
  `ID` char(13) NOT NULL,
  `REASON` varchar(400) DEFAULT NULL,
  `REP_CONT` varchar(400) DEFAULT NULL,
  `REP_NOTE` varchar(200) DEFAULT NULL,
  `REP_CO_NM` varchar(60) DEFAULT NULL COMMENT '수리 회사명',
  `REP_MBL_PHN_NO` varchar(60) DEFAULT NULL COMMENT '수리기사 연락처',
  `REP_NM` varchar(20) DEFAULT NULL COMMENT '수리기사명',
  `REP_POS` varchar(60) DEFAULT NULL COMMENT '수리기사 직급',
  `REG_DT` datetime DEFAULT NULL,
  `REG_ID` char(9) DEFAULT NULL,
  `UPD_DT` datetime DEFAULT NULL,
  `UPD_ID` char(9) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- Table structure for TB_CHCP001
DROP TABLE IF EXISTS `TB_CHCP001`;
CREATE TABLE `TB_CHCP001` (
  `CP_ID` char(6) NOT NULL COMMENT '충전소아이디',
  `CP_NM` varchar(60) DEFAULT NULL COMMENT '충전소명',
  `HI_CS_CNT` int(11) unsigned NOT NULL DEFAULT 0 COMMENT '급속충전기대수',
  `LO_CS_CNT` int(11) unsigned NOT NULL DEFAULT 0 COMMENT '완속충전기대수',
  `ELEC_SPLY_CPTY` int(5) unsigned NOT NULL DEFAULT 0 COMMENT '전력량',
  `CP_LOC` varchar(200) DEFAULT NULL COMMENT '충전소위치',
  `CP_US_YN` char(1) DEFAULT 'Y' COMMENT '충전소사용가능여부',
  `DEL_YN` char(1) DEFAULT 'N' COMMENT '삭제여부',
  `DEL_DT` datetime DEFAULT NULL COMMENT '삭제일',
  `MEMO` varchar(500) DEFAULT '' COMMENT '메모',
  `REG_DT` datetime DEFAULT NULL COMMENT '등록일',
  `REG_ID` char(9) DEFAULT NULL COMMENT '등록자',
  `UPD_DT` datetime DEFAULT NULL COMMENT '수정일',
  `UPD_ID` char(9) DEFAULT NULL COMMENT '수정자',
  PRIMARY KEY (`CP_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='충전소';

-- Table structure for TB_CHCS001
DROP TABLE IF EXISTS `TB_CHCS001`;
CREATE TABLE `TB_CHCS001` (
  `CP_ID` char(6) NOT NULL COMMENT '충전소아이디',
  `CS_ID` char(2) NOT NULL COMMENT '충전기아이디',
  `CS_UNIQ_ID` varchar(8) DEFAULT NULL,
  `CS_CHN_CNT` int(3) DEFAULT 1 COMMENT '채널(evse) 수',
  `ELEC_SPLY_CPTY` int(3) NOT NULL DEFAULT 7 COMMENT '전력량',
  `CS_CAT_CD` varchar(6) DEFAULT 'CHRA08' COMMENT '충전기유형코드',
  `US_YN` varchar(1) DEFAULT 'Y' COMMENT '사용여부',
  `BD_YN` varchar(1) DEFAULT 'N' COMMENT '고장여부',
  `MAKER_TP` varchar(8) DEFAULT 'KE' COMMENT '제조사유형',
  `INS_YR_MO` varchar(6) DEFAULT NULL COMMENT '설치년월',
  `CS_INS_CO` varchar(50) DEFAULT NULL COMMENT '충전기설치업체',
  `PRD_TP` varchar(6) DEFAULT 'POHO01' COMMENT '상품코드',
  `CS_PWD` varchar(16) DEFAULT NULL,
  `CS_KN_TP` char(6) NOT NULL DEFAULT 'CHKT01' COMMENT '급속/중속/완속(CHKT00)',
  `OCPP_VER` varchar(10) NOT NULL DEFAULT 'ocpp1.6' COMMENT 'OCPP Version',
  `REG_DT` datetime DEFAULT NULL COMMENT '등록일',
  `REG_ID` char(9) DEFAULT NULL COMMENT '등록자',
  `UPD_DT` datetime DEFAULT NULL COMMENT '수정일',
  `UPD_ID` char(9) DEFAULT NULL COMMENT '수정자',
  PRIMARY KEY (`CP_ID`,`CS_ID`) USING BTREE,
  UNIQUE KEY `CS_UNIQ_ID` (`CS_UNIQ_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='충전기';

-- Table structure for TB_CHCS002
DROP TABLE IF EXISTS `TB_CHCS002`;
CREATE TABLE `TB_CHCS002` (
  `CP_ID` char(6) NOT NULL COMMENT '충전소아이디',
  `CS_ID` char(2) NOT NULL COMMENT '충전기아이디',
  `FW_VER` varchar(20) DEFAULT NULL COMMENT '충전기 버전',
  `MODEL_NM` varchar(20) DEFAULT NULL COMMENT '모델명',
  `SER_NO` varchar(20) DEFAULT NULL COMMENT '시리얼번호',
  `LAST_BOOT_DT` datetime DEFAULT NULL COMMENT '최근 부팅 시간',
  PRIMARY KEY (`CP_ID`,`CS_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='충전기 수집정보';

-- Table structure for TB_CHCS005
DROP TABLE IF EXISTS `TB_CHCS005`;
CREATE TABLE `TB_CHCS005` (
  `CP_ID` char(6) NOT NULL COMMENT '충전소 ID',
  `CS_ID` char(2) NOT NULL COMMENT '충전기 ID',
  `EVSE_ID` tinyint(2) NOT NULL DEFAULT 1 COMMENT '충전기EVSE ID',
  `INFO_COLL_DT` datetime DEFAULT NULL COMMENT '정보수집일시',
  `CS_CAT_CD` varchar(6) DEFAULT NULL COMMENT '충전기유형코드',
  `CS_STAT_CD` varchar(6) DEFAULT NULL COMMENT '충전기상태코드',
  `CS_CBL_STAT` varchar(1) DEFAULT NULL COMMENT '충전기케이블상태',
  `RC_ID` varchar(36) DEFAULT NULL COMMENT '충전아이디',
  `CS_ERR_STAT` varchar(50) DEFAULT NULL COMMENT '충전기오류상태',
  `CU_ELE_NRG` decimal(10,3) DEFAULT NULL COMMENT '충전사용전력량',
  `CA_ELE_NRG` decimal(10,3) DEFAULT NULL COMMENT '충전누적전력량',
  `INST_CH_AMT` decimal(10,3) DEFAULT NULL COMMENT '순간충전량',
  `INST_CH_CST` decimal(10,3) DEFAULT NULL COMMENT '순간충전단가',
  `INST_CH_SUM` decimal(10,2) DEFAULT 0.00 COMMENT '순간충전금액',
  `CH_SUM` decimal(10,2) DEFAULT 0.00 COMMENT '충전금액',
  `CH_ST_DT` datetime DEFAULT NULL COMMENT '충전시작시간',
  `CH_ED_DT` datetime DEFAULT NULL COMMENT '충전종료시간',
  `LST_CH_ST_DT` datetime DEFAULT NULL COMMENT '마지막충전시작시간',
  `LST_CH_ED_DT` datetime DEFAULT NULL COMMENT '마지막충전종료시간',
  `CUT_CRD_NO` varchar(16) DEFAULT NULL COMMENT '고객카드번호',
  `EVENT_CD` varchar(6) DEFAULT NULL COMMENT '이벤트코드',
  `CS_CBL_SPD` decimal(10,2) DEFAULT 0.00 COMMENT '충전기케이블스피드',
  `UPD_DT` datetime DEFAULT NULL COMMENT '수정일',
  PRIMARY KEY (`CS_ID`,`CP_ID`,`EVSE_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='충전기 상태';

-- Table structure for TB_CHCS006
DROP TABLE IF EXISTS `TB_CHCS006`;
CREATE TABLE `TB_CHCS006` (
  `CS_STAT_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '충전기상태아이디',
  `CP_ID` char(9) NOT NULL COMMENT '충전소 ID',
  `CS_ID` char(2) NOT NULL COMMENT '충전기 ID',
  `EVSE_ID` tinyint(4) NOT NULL DEFAULT 1 COMMENT '충전기EVSE ID',
  `INFO_COLL_DT` datetime DEFAULT NULL COMMENT '정보수집일시',
  `CS_CAT_CD` varchar(6) DEFAULT NULL COMMENT '충전기유형코드',
  `CS_STAT_CD` varchar(6) DEFAULT NULL COMMENT '충전기상태코드',
  `CS_CBL_STAT` varchar(1) DEFAULT NULL COMMENT '충전기케이블상태',
  `RC_ID` varchar(36) DEFAULT NULL COMMENT '충전아이디',
  `CS_ERR_STAT` varchar(50) DEFAULT NULL COMMENT '충전기오류상태',
  `CU_ELE_NRG` decimal(10,3) DEFAULT NULL COMMENT '충전사용전력량',
  `CA_ELE_NRG` decimal(10,3) DEFAULT NULL COMMENT '충전누적전력량',
  `INST_CH_AMT` decimal(10,3) DEFAULT NULL COMMENT '순간충전량',
  `INST_CH_CST` decimal(10,3) DEFAULT NULL COMMENT '순간충전단가',
  `INST_CH_SUM` decimal(10,2) DEFAULT 0.00 COMMENT '순간충전금액',
  `CH_SUM` decimal(10,2) DEFAULT 0.00 COMMENT '충전금액',
  `CH_ST_DT` datetime DEFAULT NULL COMMENT '충전시작시간',
  `CH_ED_DT` datetime DEFAULT NULL COMMENT '충전종료시간',
  `LST_CH_ST_DT` datetime DEFAULT NULL COMMENT '마지막충전시작시간',
  `LST_CH_ED_DT` datetime DEFAULT NULL COMMENT '마지막충전종료시간',
  `CUT_CRD_NO` varchar(16) DEFAULT NULL COMMENT '고객카드번호',
  `EVENT_CD` varchar(6) DEFAULT NULL COMMENT '이벤트코드',
  `CS_CBL_SPD` decimal(10,2) DEFAULT 0.00 COMMENT '충전기케이블스피드',
  `UPD_DT` datetime DEFAULT NULL COMMENT '수정일',
  PRIMARY KEY (`CS_STAT_ID`) USING BTREE,
  FULLTEXT KEY `CP_ID_CS_ID_EVSE_ID` (`CP_ID`,`CS_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1854 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='충전기 상태 이력';

-- Table structure for TB_CUCA001
DROP TABLE IF EXISTS `TB_CUCA001`;
CREATE TABLE `TB_CUCA001` (
  `CUT_CRD_NO` varchar(16) NOT NULL COMMENT '고객카드번호',
  `CUT_ID` char(9) DEFAULT NULL COMMENT '고객아이디',
  `CUT_STAT_CD` varchar(6) NOT NULL DEFAULT 'MEML01' COMMENT '고객상태코드',
  `LOS_ID` char(9) DEFAULT NULL COMMENT '분실자',
  `LOS_DT` datetime DEFAULT NULL COMMENT '분실일',
  `DEL_ID` char(9) DEFAULT NULL COMMENT '삭제자',
  `DEL_DT` datetime DEFAULT NULL COMMENT '삭제일',
  `REG_DT` datetime DEFAULT NULL COMMENT '등록일',
  `REG_ID` char(9) DEFAULT NULL COMMENT '등록자',
  `UPD_DT` datetime DEFAULT NULL COMMENT '수정일',
  `UPD_ID` char(9) DEFAULT NULL COMMENT '수정자',
  PRIMARY KEY (`CUT_CRD_NO`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='회원카드';

-- Table structure for TB_CUCU001
DROP TABLE IF EXISTS `TB_CUCU001`;
CREATE TABLE `TB_CUCU001` (
  `CUT_ID` char(9) NOT NULL COMMENT '고객아이디',
  `CUT_NM` varchar(20) NOT NULL COMMENT '고객명',
  `MBL_PHN_NO` varchar(60) DEFAULT NULL COMMENT '휴대폰번호',
  `EMAIL` varchar(100) DEFAULT NULL COMMENT '이메일',
  `CO_ID` varchar(9) DEFAULT NULL COMMENT '회사아이디',
  `CAR_NO` varchar(50) DEFAULT NULL COMMENT '차량번호',
  `CAR_MODEL_ID` char(6) DEFAULT NULL COMMENT '차량모델아이디',
  `CAR_NM` varchar(60) DEFAULT NULL COMMENT '차량명',
  `REG_DT` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일',
  `REG_ID` char(9) NOT NULL COMMENT '등록자',
  `UPD_DT` datetime NOT NULL DEFAULT current_timestamp() COMMENT '수정일',
  `UPD_ID` char(9) NOT NULL COMMENT '수정자',
  PRIMARY KEY (`CUT_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='고객-차량정보';

-- Table structure for TB_CUCU002
DROP TABLE IF EXISTS `TB_CUCU002`;
CREATE TABLE `TB_CUCU002` (
  `CUT_ID` char(9) NOT NULL COMMENT '고객아이디',
  `CUT_CRD_NO` varchar(16) DEFAULT NULL COMMENT '고객카드번호',
  `CUT_MNG_CD` varchar(6) NOT NULL DEFAULT 'MEMK01' COMMENT '고객관리코드',
  `CUT_GRD_CD` char(6) NOT NULL DEFAULT 'MEMB02' COMMENT '고객등급코드',
  `DEL_YN` char(1) NOT NULL DEFAULT 'N' COMMENT '삭제여부',
  `DEL_DT` datetime DEFAULT NULL COMMENT '삭제일',
  `STOP_YN` char(1) NOT NULL DEFAULT 'N' COMMENT '정지여부',
  `STOP_DT` datetime DEFAULT NULL COMMENT '정지일',
  `REG_CERT_DT` datetime DEFAULT NULL COMMENT '정회원인증일',
  `MGR_DEM_YN` char(1) DEFAULT 'N' COMMENT '관리자강등여부',
  `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록일',
  `UPD_DT` datetime DEFAULT current_timestamp() COMMENT '수정일',
  PRIMARY KEY (`CUT_ID`),
  UNIQUE KEY `CUT_CRD_NO` (`CUT_CRD_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='고객 정보(회원카드번호 등)';

-- Table structure for TB_ORCP001
DROP TABLE IF EXISTS `TB_ORCP001`;
CREATE TABLE `TB_ORCP001` (
  `CO_ID` char(9) NOT NULL COMMENT '회사아이디',
  `CO_NM` varchar(60) NOT NULL COMMENT '회사명',
  `BIZ_REG_NO` char(10) NOT NULL COMMENT '사업자번호',
  `CEO_NM` varchar(20) NOT NULL COMMENT '대표자명',
  `ZIP_CD` varchar(6) DEFAULT NULL COMMENT '우편번호',
  `RD_NM` varchar(100) DEFAULT NULL COMMENT '도로명',
  `RD_DET_NM` varchar(50) DEFAULT NULL COMMENT '도로명상세주소',
  `CO_PHN_NO` varchar(11) DEFAULT NULL COMMENT '회사전화번호',
  `FAX_NO` varchar(11) DEFAULT NULL COMMENT '팩스번호',
  `MAKER_YN` char(1) DEFAULT NULL COMMENT '제조사여부',
  `POSS_YN` char(1) DEFAULT NULL COMMENT '소유사여부',
  `CUT_CNT` int(5) DEFAULT NULL COMMENT '고객수',
  `REG_ID` char(9) DEFAULT NULL COMMENT '등록자',
  `REG_DT` datetime DEFAULT NULL COMMENT '등록일',
  `UPD_ID` char(9) DEFAULT NULL COMMENT '수정자',
  `UPD_DT` datetime DEFAULT NULL COMMENT '수정일',
  PRIMARY KEY (`CO_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='회사정보';

-- Table structure for TB_ORCP002
DROP TABLE IF EXISTS `TB_ORCP002`;
CREATE TABLE `TB_ORCP002` (
  `CO_ID` char(9) NOT NULL COMMENT '회사아이디',
  `ITEM_ID` char(6) NOT NULL COMMENT '정보항목아이디',
  `VALUE` varchar(20) NOT NULL COMMENT '정보항목값',
  PRIMARY KEY (`CO_ID`,`ITEM_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='회사 정보항목';

-- Table structure for TB_OREM001
DROP TABLE IF EXISTS `TB_OREM001`;
CREATE TABLE `TB_OREM001` (
  `EMP_ID` char(9) NOT NULL COMMENT '직원아이디',
  `CO_ID` char(9) DEFAULT NULL COMMENT '회사아이디',
  `MN_ROLE_TP` varchar(20) DEFAULT NULL COMMENT '메인역할구분',
  `EMP_NM` varchar(20) NOT NULL COMMENT '직원명',
  `EMP_STATUS` char(1) NOT NULL DEFAULT '1' COMMENT '직원상태 1:재직중 2:퇴사',
  `DEPT_NM` varchar(100) DEFAULT NULL COMMENT '부서명',
  `MBL_PHN_NO` varchar(60) DEFAULT NULL COMMENT '휴대폰번호',
  `EMAIL` varchar(200) DEFAULT NULL COMMENT '이메일',
  `SMS_RCT_YN` char(1) DEFAULT NULL COMMENT 'SMS수신여부',
  `EMAIL_RCT_YN` char(1) DEFAULT NULL COMMENT 'email수신여부',
  `OFC_PHN_NO` varchar(60) DEFAULT NULL COMMENT '사무실전화번호',
  `REG_DT` datetime NOT NULL COMMENT '등록일',
  `REG_ID` char(9) NOT NULL COMMENT '등록자',
  `UPD_DT` datetime NOT NULL COMMENT '수정일',
  `UPD_ID` char(9) NOT NULL COMMENT '수정자',
  PRIMARY KEY (`EMP_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='직원';

-- Table structure for TB_PDPD001
DROP TABLE IF EXISTS `TB_PDPD001`;
CREATE TABLE `TB_PDPD001` (
  `ID` char(6) NOT NULL COMMENT '상품아이디',
  `NAME` varchar(30) DEFAULT NULL COMMENT '상품명',
  `REG_DT` datetime DEFAULT NULL COMMENT '등록일',
  `REG_ID` char(9) DEFAULT NULL COMMENT '등록자',
  `UPD_DT` datetime DEFAULT NULL COMMENT '수정일',
  `UPD_ID` char(9) DEFAULT NULL COMMENT '수정자',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='상품 - 요금제';

-- Table structure for TB_PDPD002
DROP TABLE IF EXISTS `TB_PDPD002`;
CREATE TABLE `TB_PDPD002` (
  `ID` char(11) NOT NULL COMMENT '상품아이디',
  `PROD_TP` char(6) NOT NULL COMMENT '요금유형',
  `SEQ` tinyint(4) NOT NULL DEFAULT 1 COMMENT '순번',
  `STRT_DT` char(8) NOT NULL COMMENT '시작일',
  `END_DT` char(8) NOT NULL COMMENT '종료일',
  `FEE` double NOT NULL COMMENT '단가',
  `REG_DT` datetime NOT NULL COMMENT '등록일',
  `REG_ID` char(9) NOT NULL COMMENT '등록자',
  `UPD_DT` datetime NOT NULL COMMENT '수정일',
  `UPD_ID` char(9) NOT NULL COMMENT '수정자',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='상품 - 요금제';

-- Table structure for TB_RCDR001
DROP TABLE IF EXISTS `TB_RCDR001`;
CREATE TABLE `TB_RCDR001` (
  `SEQ` bigint(20) NOT NULL AUTO_INCREMENT,
  `CS_UNIQ_ID` char(12) NOT NULL COMMENT '충전기고유 ID',
  `ST_TM` varchar(14) NOT NULL COMMENT '시작 시간',
  `ED_TM` varchar(14) NOT NULL COMMENT '종료 시간',
  `DUR` char(3) NOT NULL COMMENT '제어 기간(초)',
  `LIM_KW` int(11) NOT NULL DEFAULT 0 COMMENT '제한 용량(kW)',
  PRIMARY KEY (`SEQ`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='충전 스케줄';

-- Table structure for TB_RCLG001
DROP TABLE IF EXISTS `TB_RCLG001`;
CREATE TABLE `TB_RCLG001` (
  `SEQ` bigint(20) NOT NULL AUTO_INCREMENT,
  `CS_ID` char(2) NOT NULL,
  `CP_ID` char(9) NOT NULL,
  `ID` varchar(36) NOT NULL,
  `MSG_DIR_TY` char(7) NOT NULL,
  `MSG_TY_ID` char(1) NOT NULL,
  `OCPP_VER` varchar(10) NOT NULL,
  `ACT_NM` varchar(40) NOT NULL,
  `PAYLOAD` varchar(3000) DEFAULT NULL,
  `REG_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`SEQ`) USING BTREE,
  KEY `CS_ID_CP_ID` (`CS_ID`,`CP_ID`,`MSG_DIR_TY`,`ACT_NM`,`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7700 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='충전-OCPP 로그';

-- Table structure for TB_RCRC001
DROP TABLE IF EXISTS `TB_RCRC001`;
CREATE TABLE `TB_RCRC001` (
  `RC_ID` varchar(36) NOT NULL DEFAULT '' COMMENT '충전아이디',
  `CP_ID` char(6) NOT NULL COMMENT '충전소아이디',
  `CS_ID` char(2) NOT NULL COMMENT '충전기아이디',
  `EVSE_ID` tinyint(2) NOT NULL DEFAULT 1 COMMENT 'EVSE ID',
  `CUT_ID` char(9) DEFAULT NULL COMMENT '고객아이디',
  `CO_ID` char(9) DEFAULT NULL COMMENT '회사ID',
  `CUT_CRD_NO` varchar(16) NOT NULL COMMENT '고객카드번호',
  `PRD_ID` char(11) NOT NULL COMMENT '상품코드',
  `CH_ST_DT` datetime NOT NULL COMMENT '충전시작시간',
  `CH_ED_DT` datetime DEFAULT NULL COMMENT '충전종료시간',
  `CH_STAT_CD` char(6) NOT NULL COMMENT '충전상태코드',
  `CH_US_AMT` decimal(11,3) DEFAULT NULL COMMENT '충전기사용전력량',
  `CH_US_CST` decimal(11,3) DEFAULT NULL COMMENT '충전기사용단가',
  `CH_US_SUM` decimal(11,3) DEFAULT NULL COMMENT '충전기사용전력요금',
  `PAY_SUM` int(11) DEFAULT NULL COMMENT '결제금액',
  `ST_CA_ELE_NRG` decimal(11,3) DEFAULT NULL COMMENT '시작시점충전기누적전력량',
  `ED_CA_ELE_NRG` decimal(11,3) DEFAULT NULL COMMENT '종료시점충전기누적전력량',
  `METER_FLAG` varchar(1) DEFAULT NULL COMMENT '계량기 누적값 FLAG',
  PRIMARY KEY (`RC_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='충전- 충전정보';

-- Table structure for TB_RCRC002
DROP TABLE IF EXISTS `TB_RCRC002`;
CREATE TABLE `TB_RCRC002` (
  `RC_ID` varchar(36) NOT NULL DEFAULT '' COMMENT '충전아이디',
  `ERR_CONT` varchar(255) DEFAULT NULL COMMENT '장애발생 원인',
  PRIMARY KEY (`RC_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='충전- 장애 발생 내용';

-- Table structure for TB_SYCO001
DROP TABLE IF EXISTS `TB_SYCO001`;
CREATE TABLE `TB_SYCO001` (
  `CD` varchar(8) NOT NULL COMMENT '코드',
  `HI_CD` varchar(8) NOT NULL COMMENT '상위코드',
  `CD_NM` varchar(50) DEFAULT NULL COMMENT '코드명',
  `ORD_PRI` int(11) DEFAULT NULL COMMENT '우선순위',
  `CD_DES` varchar(500) DEFAULT NULL COMMENT '코드설명',
  PRIMARY KEY (`CD`,`HI_CD`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- Table structure for TB_SYCO002
DROP TABLE IF EXISTS `TB_SYCO002`;
CREATE TABLE `TB_SYCO002` (
  `CD` char(50) NOT NULL COMMENT '코드',
  `HI_CD` char(50) DEFAULT NULL COMMENT '상위코드',
  `CD_NM` varchar(100) DEFAULT NULL COMMENT '코드명',
  `CD_VAL` varchar(500) DEFAULT NULL COMMENT '코드값',
  `ORD_PRI` int(11) DEFAULT NULL COMMENT '우선순위',
  `CD_DES` varchar(500) DEFAULT NULL COMMENT '코드설명',
  PRIMARY KEY (`CD`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- Table structure for TB_SYCO003
DROP TABLE IF EXISTS `TB_SYCO003`;
CREATE TABLE `TB_SYCO003` (
  `KECO_CD` varchar(50) NOT NULL COMMENT '환경공단코드',
  `HI_CD` varchar(20) NOT NULL COMMENT '상위코드',
  `CD_NM` varchar(100) DEFAULT NULL COMMENT '코드명',
  `ORD_PRI` int(38) DEFAULT NULL COMMENT '우선순위',
  `CD_DES` varchar(500) DEFAULT NULL COMMENT '코드설명',
  PRIMARY KEY (`KECO_CD`,`HI_CD`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- Table structure for TB_SYCO005
DROP TABLE IF EXISTS `TB_SYCO005`;
CREATE TABLE `TB_SYCO005` (
  `CAR_MODEL_ID` char(6) NOT NULL COMMENT '차량모델아이디',
  `CAR_NM` varchar(100) NOT NULL COMMENT '차량이름',
  `LINK_CD` char(6) NOT NULL COMMENT '커넥터',
  `BATT_CPCT` double DEFAULT NULL COMMENT '배터리용량',
  `DRV_DIS` int(11) DEFAULT 0 COMMENT '주행가능거리',
  `KM_KWH` double DEFAULT 0 COMMENT '1kWh 당 주행가능거리',
  `ORD_PRI` int(11) NOT NULL DEFAULT 99 COMMENT '우선 순위',
  PRIMARY KEY (`CAR_MODEL_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- Table structure for TB_SYCO006
DROP TABLE IF EXISTS `TB_SYCO006`;
CREATE TABLE `TB_SYCO006` (
  `ID` int(4) NOT NULL AUTO_INCREMENT,
  `NM_KR` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `NM_EN` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `NM_ABBR` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `TB_NM` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `COL_NM` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `CD_DES` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Table structure for TB_SYLG001
DROP TABLE IF EXISTS `TB_SYLG001`;
CREATE TABLE `TB_SYLG001` (
  `LOG_SEQ` bigint(20) NOT NULL AUTO_INCREMENT,
  `LOG_DT` char(8) DEFAULT NULL,
  `LOG_TM` char(6) DEFAULT NULL,
  `LOG_ID` char(9) DEFAULT NULL,
  `LOG_IP` varchar(50) DEFAULT NULL,
  `LOG_URL` varchar(150) DEFAULT NULL,
  `LOG_TP` char(3) DEFAULT NULL,
  PRIMARY KEY (`LOG_SEQ`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2237 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- Table structure for TB_SYME001
DROP TABLE IF EXISTS `TB_SYME001`;
CREATE TABLE `TB_SYME001` (
  `MENU_ID` char(8) NOT NULL COMMENT '메뉴아이디',
  `HI_MNU_ID` char(8) DEFAULT 'NULL' COMMENT '상위메뉴아이디',
  `MNU_NM` varchar(500) NOT NULL COMMENT '메뉴명',
  `MNU_URL` varchar(128) NOT NULL COMMENT '메뉴URL',
  `ORD_PRI` int(11) DEFAULT 99 COMMENT '우선순위',
  `MNU_DES` varchar(500) NOT NULL COMMENT '메뉴설명',
  `LINK_TP` varchar(10) NOT NULL COMMENT '연결유형',
  `REG_DT` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일',
  `REG_ID` char(9) NOT NULL COMMENT '등록자',
  `UPD_DT` datetime NOT NULL DEFAULT current_timestamp() COMMENT '수정일',
  `UPD_ID` char(9) NOT NULL COMMENT '수정자',
  PRIMARY KEY (`MENU_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- Table structure for TB_SYME002
DROP TABLE IF EXISTS `TB_SYME002`;
CREATE TABLE `TB_SYME002` (
  `MENU_ID` char(8) NOT NULL COMMENT '메뉴아이디',
  `ROLE_TP` varchar(20) NOT NULL COMMENT '역할구분',
  PRIMARY KEY (`MENU_ID`,`ROLE_TP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- Table structure for TB_SYME002_ORG
DROP TABLE IF EXISTS `TB_SYME002_ORG`;
CREATE TABLE `TB_SYME002_ORG` (
  `MENU_ID` char(8) NOT NULL COMMENT '메뉴아이디',
  `ROLE_TP` varchar(20) NOT NULL COMMENT '역할구분',
  PRIMARY KEY (`MENU_ID`,`ROLE_TP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- Table structure for TB_SYSE001
DROP TABLE IF EXISTS `TB_SYSE001`;
CREATE TABLE `TB_SYSE001` (
  `ID` varchar(50) NOT NULL COMMENT '아이디',
  `VALUE` varchar(255) DEFAULT NULL COMMENT '정보항목값',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- Table structure for TB_USAU001
DROP TABLE IF EXISTS `TB_USAU001`;
CREATE TABLE `TB_USAU001` (
  `LOGIN_ID` varchar(100) NOT NULL COMMENT '로그인아이디',
  `USER_ID` char(9) NOT NULL COMMENT '사용자아이디',
  `USER_TP` varchar(20) NOT NULL COMMENT '사용자유형',
  `USER_PWD` varchar(200) NOT NULL COMMENT '사용자비밀번호',
  `USER_SALT` varchar(30) DEFAULT NULL COMMENT 'SALT',
  `USER_STAT` varchar(20) NOT NULL COMMENT '사용자상태',
  `PW_UPD_DT` datetime NOT NULL COMMENT '비밀번호변경일',
  `PW_FAIL_CNT` int(11) NOT NULL COMMENT '비밀번호실패회수',
  `PW_EXP_DT` datetime NOT NULL COMMENT '비밀번호만료일',
  `LST_LOGIN_DT` datetime DEFAULT NULL COMMENT '마지막 로그인일',
  `REG_DT` datetime NOT NULL COMMENT '등록일',
  `REG_ID` varchar(100) NOT NULL COMMENT '등록자',
  `UPD_DT` datetime NOT NULL COMMENT '수정일',
  `UPD_ID` varchar(100) NOT NULL COMMENT '수정자',
  `PW_INIT_YN` varchar(2) DEFAULT 'Y' COMMENT '초기 비밀번호 여부 (Y : 초기 / N: 초기X)',
  PRIMARY KEY (`LOGIN_ID`) USING BTREE,
  KEY `USER_ID` (`USER_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- Table structure for TB_USAU002
DROP TABLE IF EXISTS `TB_USAU002`;
CREATE TABLE `TB_USAU002` (
  `LOGIN_ID` varchar(100) NOT NULL COMMENT '로그인아이디',
  `ROLE_TP` varchar(20) NOT NULL COMMENT '역할구분',
  `ORD_PRI` int(11) DEFAULT 1 COMMENT '우선순위',
  PRIMARY KEY (`LOGIN_ID`,`ROLE_TP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

