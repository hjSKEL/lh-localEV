# Domain Models and SQL Mapper Glossary

This glossary extracts the mapping between domain variables, SQL columns, and their Korean descriptions from Java domain classes and `*sql.xml` mappers.

| Domain Object / Module | Variable Name (Property) | Database Column | Logical Name (Description) |
|---|---|---|---|
| menu | `highMenuId` | `HI_MNU_ID` | 상위메뉴아이디 |
| menu | `menuName` | `MNU_NM` | 메뉴명 |
| menu | `menuUrl` | `MNU_URL` | 메뉴URL |
| codeVal | `ordPriority` | `ORD_PRI` | 우선순위 |
| roleAuthority | `ordPriority` | `ORD_PRI` | 우선순위 |
| menu | `ordPriority` | `ORD_PRI` | 우선순위 |
| code | `ordPriority` | `ORD_PRI` | 우선순위 |
| menu | `menuDesc` | `MNU_DES` | 메뉴설명 |
| menu | `linkType` | `LINK_TP` | 연결유형 |
| menu | `highMenuName` | `HI_MNU_NM` |  |
| employee | `roleType` | `MN_ROLE_TP` | 역할구분 ROLE_TP |
| roleAuthority | `roleType` | `ROLE_TP` | 역할구분 |
| roleAuthority | `menuId` | `MENU_ID` | 메뉴아이디 |
| user | `loginId` | `LOGIN_ID` | 로그인아이디 |
| roleAuthority | `loginId` | `LOGIN_ID` | 로그인아이디 |
| employee | `loginId` | `LOGIN_ID` | 로그인아이디 |
| user | `userId` | `USER_ID` | 사용자아이디 |
| user | `userType` | `USER_TP` | 사용자유형 |
| user | `userPwd` | `USER_PWD` | 사용자비밀번호 |
| user | `salt` | `USER_SALT` |  |
| user | `pwFailCount` | `PW_FAIL_CNT` | 비밀번호실패회수  INT NOT NULL |
| user | `userStatus` | `USER_STAT` | 사용자상태 |
| user | `pwUpdateDate` | `PW_UPD_DT` | 비밀번호변경일 |
| user | `pwExpireDate` | `PW_EXP_DT` | 비밀번호만료일 PW_EXP_DAT |
| user | `lastLoginDate` | `LST_LOGIN_DT` | 마지막 로그인 일  NULL |
| user | `pwInitYn` | `PW_INIT_YN` | 초기 비밀번호 판단 YN |
| breakdownInfo | `stationErrorCode` | `CS_ER_CD` | 충전기 오류 코드  VARCHAR2(6) |
| breakdownInfo | `breakdownContent` | `BD_CONT` | VARCHAR2(200) |
| breakdownInfo | `carModelName` | `CAR_MODEL_NM` | 차량모델명 |
| breakdownInfo | `csCatCode` | `CS_CAT_CD` | 충전기 타입 공통코드 : CHRA00  VARCHAR2(6 BYTE) NOT NULL |
| chargerStatusHis | `csCatCode` | `CS_CAT_CD` | 충전기 타입 공통코드 : CHRA00  VARCHAR2(6 BYTE) NOT NULL |
| chargingStation | `csCatCode` | `CS_CAT_CD` | 충전기 타입 공통코드 : CHRA00  VARCHAR2(6 BYTE) NOT NULL |
| chargerStatus | `csCatCode` | `CS_CAT_CD` | 충전기 타입 공통코드 : CHRA00  VARCHAR2(6 BYTE) NOT NULL |
| breakdownInfo | `reporterName` | `RPT_NM` | 신고자명 |
| breakdownInfo | `reporterAddr` | `RPT_ADDR` | 신고자 거주지 |
| breakdownInfo | `reporterPhoneNum` | `RPT_PHN_NO` | 신고자 휴대폰 번호 |
| customer | `writer.registrationDate` | `REG_DT` |  |
| chargePoint | `writer.registrationDate` | `REG_DT` |  |
| breakdownRepairInfo | `writer.registrationDate` | `REG_DT` |  |
| productPrice | `writer.registrationDate` | `REG_DT` |  |
| breakdownMgtInfo | `writer.registrationDate` | `REG_DT` |  |
| employee | `writer.registrationDate` | `REG_DT` |  |
| customerCard | `writer.registrationDate` | `REG_DT` |  |
| breakdownInfo | `writer.registrationDate` | `REG_DT` |  |
| company | `writer.registrationDate` | `REG_DT` |  |
| product | `writer.registrationDate` | `REG_DT` |  |
| chargingStation | `writer.registrationDate` | `REG_DT` |  |
| company | `writer.regUserId` | `REG_ID` |  |
| chargingStation | `writer.regUserId` | `REG_ID` |  |
| customerCard | `writer.regUserId` | `REG_ID` |  |
| chargePoint | `writer.regUserId` | `REG_ID` |  |
| customer | `writer.regUserId` | `REG_ID` |  |
| breakdownRepairInfo | `writer.regUserId` | `REG_ID` |  |
| productPrice | `writer.regUserId` | `REG_ID` |  |
| breakdownMgtInfo | `writer.regUserId` | `REG_ID` |  |
| employee | `writer.regUserId` | `REG_ID` |  |
| breakdownInfo | `writer.regUserId` | `REG_ID` |  |
| product | `writer.regUserId` | `REG_ID` |  |
| employee | `writer.updateDate` | `UPD_DT` |  |
| breakdownInfo | `writer.updateDate` | `UPD_DT` |  |
| product | `writer.updateDate` | `UPD_DT` |  |
| chargingStation | `writer.updateDate` | `UPD_DT` |  |
| company | `writer.updateDate` | `UPD_DT` |  |
| customer | `writer.updateDate` | `UPD_DT` |  |
| chargePoint | `writer.updateDate` | `UPD_DT` |  |
| productPrice | `writer.updateDate` | `UPD_DT` |  |
| breakdownMgtInfo | `writer.updateDate` | `UPD_DT` |  |
| breakdownRepairInfo | `writer.updateDate` | `UPD_DT` |  |
| customerCard | `writer.updateDate` | `UPD_DT` |  |
| breakdownMgtInfo | `writer.updUserId` | `UPD_ID` |  |
| employee | `writer.updUserId` | `UPD_ID` |  |
| breakdownInfo | `writer.updUserId` | `UPD_ID` |  |
| product | `writer.updUserId` | `UPD_ID` |  |
| chargingStation | `writer.updUserId` | `UPD_ID` |  |
| company | `writer.updUserId` | `UPD_ID` |  |
| chargePoint | `writer.updUserId` | `UPD_ID` |  |
| productPrice | `writer.updUserId` | `UPD_ID` |  |
| customer | `writer.updUserId` | `UPD_ID` |  |
| breakdownRepairInfo | `writer.updUserId` | `UPD_ID` |  |
| customerCard | `writer.updUserId` | `UPD_ID` |  |
| ocppLog | `cpId` | `CP_ID` | PK 충전소 ID  VARCHAR2(6 BYTE) NOT NULL |
| recharging | `cpId` | `CP_ID` | PK 충전소 ID  VARCHAR2(6 BYTE) NOT NULL |
| chargerStatusHis | `cpId` | `CP_ID` | PK 충전소 ID  VARCHAR2(6 BYTE) NOT NULL |
| breakdownMgtInfo | `cpId` | `CP_ID` | PK 충전소 ID  VARCHAR2(6 BYTE) NOT NULL |
| employee | `cpId` | `CP_ID` | PK 충전소 ID  VARCHAR2(6 BYTE) NOT NULL |
| breakdownMgtInfo | `csId` | `CS_ID` | PK 충전기 ID  VARCHAR2(2 BYTE) NOT NULL |
| chargerStatusHis | `csId` | `CS_ID` | PK 충전기 ID  VARCHAR2(2 BYTE) NOT NULL |
| ocppLog | `csId` | `CS_ID` | PK 충전기 ID  VARCHAR2(2 BYTE) NOT NULL |
| recharging | `csId` | `CS_ID` | PK 충전기 ID  VARCHAR2(2 BYTE) NOT NULL |
| breakdownMgtInfo | `receiptDate` | `RCPT_DT` | 접수날짜 |
| breakdownMgtInfo | `receiptTime` | `RCPT_TM` | 접수시간 |
| breakdownMgtInfo | `repairDate` | `REP_DT` | 조치(수리) 날짜 |
| breakdownMgtInfo | `repairTime` | `REP_TM` | 조치(수리) 시간 |
| breakdownMgtInfo | `breakdownStatus` | `BD_STAT` | 상태 공통코드 : BDST00 |
| chargePoint | `cpName` | `CP_NM` | 충전소명  VARCHAR2(60 BYTE) NOT NULL |
| recharging | `cpName` | `CP_NM` | 충전소명  VARCHAR2(60 BYTE) NOT NULL |
| breakdownMgtInfo | `cpName` | `CP_NM` | 충전소명  VARCHAR2(60 BYTE) NOT NULL |
| chargerStatus | `cpName` | `CP_NM` | 충전소명  VARCHAR2(60 BYTE) NOT NULL |
| employee | `cpName` | `CP_NM` | 충전소명  VARCHAR2(60 BYTE) NOT NULL |
| chargingStation | `cpName` | `CP_NM` | 충전소명  VARCHAR2(60 BYTE) NOT NULL |
| breakdownRepairInfo | `reason` | `REASON` | 고장 사유 |
| breakdownRepairInfo | `repairContent` | `REP_CONT` | 조치내용 |
| breakdownRepairInfo | `repairNote` | `REP_NOTE` | 비고 |
| breakdownRepairInfo | `repairCompanyName` | `REP_CO_NM` | 고장수리 회사명 |
| breakdownRepairInfo | `repairMblPhoneNo` | `REP_MBL_PHN_NO` | 고장 수리자 연락처  VARCHAR2(60 BYTE) NOT NULL |
| breakdownRepairInfo | `repairName` | `REP_NM` | 고장수리자 명  VARCHAR2(20 BYTE) NOT NULL |
| breakdownRepairInfo | `repairPosition` | `REP_POS` | 고장 수리자 연락처  VARCHAR2(60 BYTE) NOT NULL |
| chargePoint | `highCsCount` | `HI_CS_CNT` | 급속충전기대수  NUMBER |
| chargePoint | `lowCsCount` | `LO_CS_CNT` | 완속충전기대수  NUMBER |
| chargePoint | `electSupplyCapability` | `ELEC_SPLY_CPTY` | 전력량 |
| chargingStation | `electSupplyCapability` | `ELEC_SPLY_CPTY` | 전력량 |
| chargePoint | `cpLocation` | `CP_LOC` | 충전소 위치  VARCHAR2(200 BYTE) |
| chargePoint | `cpUseYn` | `CP_US_YN` | 충전소사용가능여부  BYTE) DEFAULT 'N' NOT NULL |
| chargePoint | `deleteYn` | `DEL_YN` | 삭제여부  BYTE) DEFAULT 'N' NOT NULL |
| chargePoint | `deleteDate` | `DEL_DT` | 삭제일  DATE |
| chargePoint | `memo` | `MEMO` |  |
| recharging | `evseId` | `EVSE_ID` | PK |
| chargerStatusHis | `evseId` | `EVSE_ID` | PK |
| chargerStatus | `evseId` | `EVSE_ID` | PK |
| chargerStatus | `infoCollDate` | `INFO_COLL_DT` | 정보수집일시  DATE |
| chargerStatusHis | `infoCollDate` | `INFO_COLL_DT` | 정보수집일시  DATE |
| chargerStatus | `csStatCode` | `CS_STAT_CD` | 충전기 상태정보 공통코드 : 공통코드 : CHRS00  VARCHAR2(6 BYTE) |
| chargerStatusHis | `csStatCode` | `CS_STAT_CD` | 충전기 상태정보 공통코드 : 공통코드 : CHRS00  VARCHAR2(6 BYTE) |
| chargerStatus | `csCableStatus` | `CS_CBL_STAT` | 충전기 케이블 상태 10  VARCHAR2(1 BYTE) |
| chargerStatusHis | `csCableStatus` | `CS_CBL_STAT` | 충전기 케이블 상태 10  VARCHAR2(1 BYTE) |
| chargerStatus | `rechargingId` | `RC_ID` | 충전아이디 - 충전소ID(9)+충전기ID(2)+날짜(8)+번호(5) ex)112905004+01+20190417+00001 |
| chargerStatusHis | `rechargingId` | `RC_ID` | 충전아이디 - 충전소ID(9)+충전기ID(2)+날짜(8)+번호(5) ex)112905004+01+20190417+00001 |
| chargerStatusHis | `csErrorStatus` | `CS_ERR_STAT` | 충전기 오류 상태 공통코드 : CHE000  VARCHAR2(6 BYTE) |
| chargerStatus | `csErrorStatus` | `CS_ERR_STAT` | 충전기 오류 상태 공통코드 : CHE000  VARCHAR2(6 BYTE) |
| chargerStatusHis | `cuEleEnerge` | `CU_ELE_NRG` | 충전사용전력량 |
| chargerStatus | `cuEleEnerge` | `CU_ELE_NRG` | 충전사용전력량 |
| chargerStatus | `caEleEnerge` | `CA_ELE_NRG` | 충전 누적 전력량 |
| chargerStatusHis | `caEleEnerge` | `CA_ELE_NRG` | 충전 누적 전력량 |
| chargerStatusHis | `instChAmont` | `INST_CH_AMT` | 순간 충전량 |
| chargerStatus | `instChAmont` | `INST_CH_AMT` | 순간 충전량 |
| chargerStatusHis | `instChCost` | `INST_CH_CST` | 순간충전단가 |
| chargerStatus | `instChCost` | `INST_CH_CST` | 순간충전단가 |
| chargerStatus | `instChSum` | `INST_CH_SUM` | 순간충전금액 |
| chargerStatusHis | `instChSum` | `INST_CH_SUM` | 순간충전금액 |
| chargerStatusHis | `chSum` | `CH_SUM` | 충전금액 |
| chargerStatus | `chSum` | `CH_SUM` | 충전금액 |
| chargerStatusHis | `chStartDate` | `CH_ST_DT` | 충전시작시간  DATE |
| chargerStatus | `chStartDate` | `CH_ST_DT` | 충전시작시간  DATE |
| recharging | `chStartDate` | `CH_ST_DT` | 충전시작시간  DATE |
| recharging | `chEndDate` | `CH_ED_DT` | 충전종료시간  DATE |
| chargerStatusHis | `chEndDate` | `CH_ED_DT` | 충전종료시간  DATE |
| chargerStatus | `chEndDate` | `CH_ED_DT` | 충전종료시간  DATE |
| chargerStatus | `lastChStartDate` | `LST_CH_ST_DT` | 충전시작시간  DATE |
| chargerStatusHis | `lastChStartDate` | `LST_CH_ST_DT` | 충전시작시간  DATE |
| chargerStatus | `lastChEndDate` | `LST_CH_ED_DT` | 충전종료시간  DATE |
| chargerStatusHis | `lastChEndDate` | `LST_CH_ED_DT` | 충전종료시간  DATE |
| chargerStatus | `cutCardNo` | `CUT_CRD_NO` | PK 고객카드번호(티머니)  VARCHAR2(16 BYTE) NOT NULL |
| recharging | `cutCardNo` | `CUT_CRD_NO` | PK 고객카드번호(티머니)  VARCHAR2(16 BYTE) NOT NULL |
| customerMgt | `cutCardNo` | `CUT_CRD_NO` | PK 고객카드번호(티머니)  VARCHAR2(16 BYTE) NOT NULL |
| chargerStatusHis | `cutCardNo` | `CUT_CRD_NO` | PK 고객카드번호(티머니)  VARCHAR2(16 BYTE) NOT NULL |
| chargerStatusHis | `eventCode` | `EVENT_CD` | 이벤트 코드  VARCHAR2(6 BYTE) |
| chargerStatus | `eventCode` | `EVENT_CD` | 이벤트 코드  VARCHAR2(6 BYTE) |
| chargerStatus | `csCableSpeed` | `CS_CBL_SPD` | 2019.01.16 추가 충전케이블 충전 속도 |
| chargerStatusHis | `csCableSpeed` | `CS_CBL_SPD` | 2019.01.16 추가 충전케이블 충전 속도 |
| customerMgt | `updateDate` | `UPD_DT` | 수정일 |
| chargerStatusHis | `updateDate` | `UPD_DT` | 수정일 |
| chargerStatus | `updateDate` | `UPD_DT` | 수정일 |
| chargingSchedule | `csUniqId` | `CS_UNIQ_ID` | 충전기 고유 ID |
| chargingStation | `csUniqId` | `CS_UNIQ_ID` | 충전기 고유 ID |
| chargingStation | `csChanelCount` | `CS_CHN_CNT` | 충전기 채널 수 |
| chargingStation | `useYn` | `US_YN` | 사용여부 YN  VARCHAR2(1 BYTE) DEFAULT 'Y' |
| chargingStation | `brkdownYn` | `BD_YN` | 고장여부 YN  VARCHAR2(1 BYTE) DEFAULT 'N' |
| chargingStation | `makerType` | `MAKER_TP` | 제조사 코드 CHMK00  VARCHAR2(6) |
| chargingStation | `insYearMon` | `INS_YR_MO` | 설치년월  BYTE) |
| chargingStation | `csInstallCo` | `CS_INS_CO` | 충전기 설치업체(환경부보조금)  VARCHAR2(50 BYTE) |
| chargingStation | `prodType` | `PRD_TP` | 상품 타입 PO 상품 HILO 고압저압 01 순번 6자리 |
| chargingStation | `csPassword` | `CS_PWD` |  |
| chargingStation | `csKindType` | `CS_KN_TP` | 충전기 타입  CHKT00(급속중속완속) |
| chargingStation | `ocppVersion` | `OCPP_VER` | OCPP Version  OCPP1.6 OCPP2.0.1 |
| chargingStation | `fwVer` | `FW_VER` | 충전기 버전 ` |
| chargingStation | `modelName` | `MODEL_NM` | 모델 명 |
| chargingStation | `serialNumber` | `SER_NO` | 시리얼번호 |
| chargingStation | `lastBootDate` | `LAST_BOOT_DT` | 최근 부팅 시간 ` DATETIME |
| productPrice | `seq` | `SEQ` | 시퀀스 |
| accessLog | `seq` | `LOG_SEQ` | 시퀀스 SEQ |
| accessLog | `logDate` | `LOG_DT` |  |
| accessLog | `logTime` | `LOG_TM` |  |
| accessLog | `logId` | `LOG_ID` |  |
| accessLog | `logIp` | `LOG_IP` |  |
| accessLog | `logUrl` | `LOG_URL` |  |
| accessLog | `logType` | `LOG_TP` |  |
| customer | `carName` | `CAR_NM` | 차량이름.  BYTE) NOT NULL |
| carModel | `carName` | `CAR_NM` | 차량이름.  BYTE) NOT NULL |
| carModel | `linkCode` | `LINK_CD` | 연결코드  BYTE) NOT NULL |
| carModel | `drivingDistance` | `DRV_DIS` | 주행 가능 거리  INT |
| carModel | `kmKwh` | `KM_KWH` | 1kW에 주행 가능 거리 |
| carModel | `batteryCapacity` | `BATT_CPCT` | 베터리용량  DOUBLE |
| code | `highCode` | `HI_CD` | 상위 코드 |
| codeVal | `highCode` | `HI_CD` | 상위 코드 |
| code | `codeName` | `CD_NM` | 코드명  BYTE) |
| codeVal | `codeName` | `CD_NM` | 코드명  BYTE) |
| codeVal | `codeValue` | `CD_VAL` | 코드값  BYTE) |
| code | `codeDescription` | `CD_DES` | 코드 설명  VARCHAR2(500 BYTE) |
| codeVal | `codeDescription` | `CD_DES` | 코드 설명  VARCHAR2(500 BYTE) |
| fileManagement | `id` | `ID` | Maximum of 36 characters to allow for GUs |
| ocppLog | `id` | `ID` | Maximum of 36 characters to allow for GUs |
| fileStorage | `id` | `ID` | Maximum of 36 characters to allow for GUs |
| fileManagement | `modifySeq` | `SEQ` | 파일 수정 이력번호 |
| fileStorage | `modifySeq` | `SEQ` | 파일 수정 이력번호 |
| fileManagement | `owner` | `OWNER_ID` |  |
| fileManagement | `name` | `FILE_NM` |  |
| product | `name` | `NAME` | FILE_NM |
| fileManagement | `kind` | `FILE_KIND` |  |
| fileManagement | `size` | `FILE_SIZE` |  |
| fileStorage | `location` | `LOCATION` |  |
| fileStorage | `fileName` | `NAME` |  |
| customerCard | `custStatCode` | `CUT_STAT_CD` | 고객카드상태코드 공통코드 : MEML00 미사용발송수령분실삭제_불량재발급요청  VARCHAR2(6 BYTE) DEFAULT 'MEML01' NOT NULL |
| recharging | `customerId` | `CUT_ID` | PK 사용자아이디  BYTE) NOT NULL |
| customerCard | `customerId` | `CUT_ID` | PK 사용자아이디  BYTE) NOT NULL |
| customerCard | `lossId` | `LOS_ID` | 분실자  BYTE) |
| customerCard | `lossDate` | `LOS_DT` | 분실일  DATE |
| customerCard | `deleteId` | `DEL_ID` | 삭제자  BYTE) |
| customerCard | `delDate` | `DEL_DT` | 삭제일  DATE |
| customerCard | `customerName` | `CUT_NM` | 고객명 |
| customerMgt | `stopYn` | `STOP_YN` | 정지여부  BYTE) DEFAULT 'N' |
| customerMgt | `stopDate` | `STOP_DT` | 정지일  DATE |
| customerMgt | `registrationDate` | `REG_DT` | 등록일 |
| customer | `custName` | `CUT_NM` | 고객명  VARCHAR2(20 BYTE) NOT NULL |
| customer | `mblPhoneNo` | `MBL_PHN_NO` | 휴대폰 번호  VARCHAR2(60 BYTE) NOT NULL |
| employee | `mblPhoneNo` | `MBL_PHN_NO` | 휴대폰 번호  VARCHAR2(60 BYTE) NOT NULL |
| customer | `email` | `EMAIL` | E-Mail  VARCHAR2(200 BYTE) |
| employee | `email` | `EMAIL` | E-Mail  VARCHAR2(200 BYTE) |
| employee | `companyId` | `CO_ID` | 회사아이디 |
| recharging | `companyId` | `CO_ID` | 회사아이디 |
| customer | `companyId` | `CO_ID` | 회사아이디 |
| customer | `carModel.carModelId` | `CAR_MODEL_ID` |  |
| customer | `carNumber` | `CAR_NO` | 차량번호 |
| customer | `customerMgt.cutCardNo` | `CUT_CRD_NO` |  |
| customer | `customerMgt.customerId` | `CUT_ID` |  |
| customer | `customerMgt.stopYn` | `STOP_YN` |  |
| customer | `customerMgt.stopDate` | `STOP_DT` |  |
| employee | `companyName` | `CO_NM` | 회사명 |
| company | `companyName` | `CO_NM` | 회사명 |
| company | `bizRegNo` | `BIZ_REG_NO` | 사업자번호 |
| company | `ceoName` | `CEO_NM` | 대표자명 |
| company | `zipCode` | `ZIP_CD` | 우편번호 |
| company | `roadName` | `RD_NM` | 도로명 |
| company | `roadDetName` | `RD_DET_NM` | 도로명상세주소 |
| company | `coPhoneNo` | `CO_PHN_NO` | 회사전화번호 RD_DET_NM |
| company | `faxNo` | `FAX_NO` | 팩스번호 |
| company | `makerYn` | `MAKER_YN` | 제조사 여부 |
| company | `possessionYn` | `POSS_YN` | 소유사 여부 |
| company | `custCount` | `CUT_CNT` | 고객수 |
| company | `infoItemValue` | `VALUE` | 정보항목값 |
| company | `company.companyId` | `CO_ID` |  |
| company | `company.companyName` | `CO_NM` |  |
| company | `company.bizRegNo` | `BIZ_REG_NO` |  |
| company | `company.ceoName` | `CEO_NM` |  |
| company | `company.zipCode` | `ZIP_CD` |  |
| company | `company.roadName` | `RD_NM` |  |
| company | `company.roadDetName` | `RD_DET_NM` |  |
| company | `company.coPhoneNo` | `CO_PHN_NO` |  |
| company | `company.faxNo` | `FAX_NO` |  |
| company | `company.makerYn` | `MAKER_YN` |  |
| company | `company.possessionYn` | `POSS_YN` |  |
| company | `company.custCount` | `CUT_CNT` |  |
| company | `employee.employeeId` | `EMP_ID` |  |
| company | `employee.companyId` | `CO_ID` |  |
| company | `employee.emplName` | `EMP_NM` |  |
| company | `employee.ofcPhoneNo` | `OFC_PHN_NO` |  |
| company | `employee.deptName` | `DEPT_NM` |  |
| company | `employee.mblPhoneNo` | `MBL_PHN_NO` |  |
| employee | `emplName` | `EMP_NM` | 직원명 |
| employee | `emplStatus` | `EMP_STATUS` | 직원 상태 |
| employee | `ofcPhoneNo` | `OFC_PHN_NO` | 사무실전화번호 |
| employee | `deptName` | `DEPT_NM` | 부서명 |
| employee | `smsRctYn` | `SMS_RCT_YN` | SMS수신여부 |
| employee | `emailRctYn` | `EMAIL_RCT_YN` | email수신여부 |
| productPrice | `productType` | `PROD_TP` | 상품 타입 PO 상품 HILO 고압저압 01 순번 6자리 |
| productPrice | `startDt` | `STRT_DT` | 시작 년월일 |
| productPrice | `endDt` | `END_DT` | 종료 년월일 |
| productPrice | `fee` | `FEE` | 요금 |
| chargingSchedule | `startTime` | `ST_TM` |  |
| chargingSchedule | `endTime` | `ED_TM` |  |
| chargingSchedule | `duration` | `DUR` | 단위 초 |
| chargingSchedule | `limitKW` | `LIM_KW` | INTERGER NOT NULL |
| ocppLog | `directType` | `MSG_DIR_TY` |  |
| ocppLog | `messageTypeId` | `MSG_TY_ID` | MessageTypeId 2 : 요청 3 : 응답 4 : 응답 에러 |
| ocppLog | `ocppVer` | `OCPP_VER` | 1.6 2.0 OCPP Version. |
| ocppLog | `action` | `ACT_NM` | Action name |
| ocppLog | `payloadJson` | `PAYLOAD` | 내용 |
| ocppLog | `regDate` | `REG_DT` | DATETIME 서버 수집 시간 |
| recharging | `productId` | `PRD_ID` | 상품코드 PRD_TP VARCHAR2(6 BYTE) |
| recharging | `chStatCode` | `CH_STAT_CD` | 충전상태코드 - 공통코드 : RECS00  BYTE) |
| recharging | `chUseAmount` | `CH_US_AMT` | 충전기사용전력량 |
| recharging | `chUseUnitCost` | `CH_US_CST` | 충전기사용단가 |
| recharging | `chUseCost` | `CH_US_SUM` | 충전기사용전력요금  DOUBLE |
| recharging | `paySum` | `PAY_SUM` | 결제금액 |
| recharging | `startCaEleEnerge` | `ST_CA_ELE_NRG` | 충전 시작 누적 전력량 |
| recharging | `endCaEleEnerge` | `ED_CA_ELE_NRG` | 충전 종료 누적 전력량 |
| Menu | `child` |  | 하위 메뉴 |
| Menu | `writer` |  | 등록정보 |
| ChargePoint | `final` |  | UID |
| UserDeviceSearchCond | `deviceType` |  | DEV_TP Android, IOS |
| BreakdownInfo | `breakdownMgtInfo` |  | Relation .... |
| BreakdownMgtInfo | `receptInfo` |  | Relation |
| BreakdownMgtInfo | `repairInfo` |  | Relation |
| ChargingStation | `chargerStatusInfo` |  | Object Relation 충전기 실 상태 정보 |
| ChargingStation | `chargePoint` |  | Object Relation |
| OcppLogSearchCond | `fromDate` |  | 서버 수집 시간 |
| ManagementFile | `status` |  | 파일 상태 |
| CarModel | `carModelId` |  | 차량모델아이디 CAR_MODEL_ID , |
| Code | `code` |  | 코드 CD  |
| CustomerSearchCond | `order` |  | 정렬 00 : 가입일 ASC 01 : 가입일 DESC 10 : 고객명 ASC 11 : 고객명 DESC |
| Writer | `regUserId` |  | 등록자 REG_ID |
| Writer | `regUserName` |  | 등록자 명 (VO) |
| Writer | `updUserId` |  | 수정자 UPD_ID |
| Writer | `updUserName` |  | 수정자 명(VO) |
| CustomerMgt | `regCertDate` |  | 정회원인증일 REG_CERT_DT DATE, |
| CustomerCardDto | `rctName` |  | 수신자 |
| CustomerCardDto | `lossName` |  | 분실자 |
| CustomerCardDto | `deleteName` |  | 삭제자 |
| CustomerSearchCond | `cutGrdCode` |  | 멤버등급 공통코드 : MEMB00 MEMGRD VARCHAR2(6 BYTE) DEFAULT 'MEMB02' NOT NULL, |
| CompanyInfoItem | `infoItemId` |  | 정보항목아이디 ITEM_ID , |
| Employee | `employeeId` |  | 직원아이디 EMP_ID , |
| MajorZone | `taskType` |  | TASK_TP  |
| MajorZone | `sidoCode` |  | SIDO  |
| MinorZone | `siguCode` |  | SIGU  |
| CompanyDto | `billMethodCode` |  | 청구방법코드 공통코드 APPA00 APPA01:일괄결제, APPA02 : 건별결제 BILL_METH_CD BYTE), |
| CompanyDto | `pymtReqCode` |  | 결제요청코드 공통코드 PAYF00 PAYF01 카드 PAYF02 세금계산서 PAYF03 별도 PAYF04 취소 PYMT_REQ_CD BYTE), |
| CompanyDto | `billKey` |  | 카드키값 BILL_KEY VARCHAR2(40 BYTE) NOT NULL, |
| CompanyDto | `cardTrxnNo` |  | 카드인증승인번호 거래번호 CRD_TRXN_NO VARCHAR2(20 BYTE) NOT NULL, |
| CompanyDto | `cardCoType` |  | 카드사유형 CRD_CO_TP VARCHAR2(2 BYTE) NOT NULL, |
| CompanyDto | `coUserLoginId` |  | 법인관리자 로그인아이디 |
| Product | `prices` |  | Relation |
| Recharging | `closedDate` |  | 마감 날짜 CL_DT  |
| Recharging | `finalPaySum` |  | 할인된최종금액 DIS_SUM NUMBER DEFAULT 0, |
| Recharging | `errorContent` |  | ERR_CONT DEFAULT NULL |
| Recharging | `chargingStation` |  | Relation ... |
| RechargingSearchCond | `dateType` |  | S : Start E : End |