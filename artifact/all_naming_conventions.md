# 통합 명명 규칙 가이드 (Naming Conventions Guide)

본 문서는 `local-csms` 프로젝트의 주요 계층별(Mapper, Service, Web Layer) 메서드 명명 규칙 및 URL 엔드포인트 라우팅 패턴을 통합하여 정리한 문서입니다.


---

# MyBatis Mapper Naming Conventions

Based on `181` methods found in `27` `*Mapper.java` files across `*-entity` components, here are the naming conventions:

## 1. Prefix (Verb) Analysis

| Prefix | Count | Example Formats | Summary |
|---|---|---|---|
| `select` | 88 | select{Entity}By{Entity}Type<br>selectAdmin{Entity}<br>select{Entity}CsmByCpId<br>select{Entity}ById<br>... | 상세 조회, 조건 조회, 리스트 조회 등 데이터를 가져올 때 사용 |
| `insert` | 30 | insert{Entity}<br>insert{Entity}InfoItem<br>insert{Entity}Error<br>insertManagementFile<br>... | 신규 데이터 등록 시 사용 |
| `update` | 24 | update{Entity}Csm<br>update{Entity}All<br>update{Entity}By{Entity}<br>updateMemberCard<br>... | 기존 데이터 수정 시 사용 |
| `count` | 20 | countChargerStatusInfoHisBySearchCond<br>count{Entity}AllByCondition<br>count{Entity}By{Entity}SearchCond<br>count{Entity}DtoBy{Entity}SearchCond<br>... | 조건에 맞는 데이터의 총 개수(Total Count)를 가져올 때 주로 페이징 처리와 함께 사용 |
| `delete` | 16 | delete{Entity}ByRoleAndMenu<br>delete{Entity}ByType<br>delete{Entity}ByRoleType<br>delete{Entity}InfoItem<br>... | 데이터 삭제 시 사용 |
| `get` | 2 | getBreakdownSequence<br>get{Entity}Sequence | 기타 특수 목적 함수 |
| `insere` | 1 | insere{Entity} | 기타 특수 목적 함수 |

## 2. Standard Naming Rules (Derived)

- **등록 (Create)**: `insert{Entity}` 형식 사용 (예: `insertUser`)
- **조회 (Read)**: `select`로 시작. 
  - 단건 조회: PK나 유니크 키 조회 시 `select{Entity}ById` 또는 `select{Entity}By{Condition}`
  - 다건 조회 (Search): 검색 조건이 있을 경우 `select{Entity}BySearchCond`
  - 전체/리스트 조회: `select{Entity}List`
- **수정 (Update)**: `update{Entity}` 형식 사용 (예: `updateUser`)
- **삭제 (Delete)**: `delete{Entity}` 형식 사용 (단건은 PK 기준, 다건은 조건 기준)
- **건수 (Count)**: 검색 조건에 대한 데이터 건수를 구할 때 `count{Entity}BySearchCond` 패턴 주로 사용



---

# Service & ExtProcess Naming Conventions

This document outlines the method naming conventions used in `*Service.java` (Business Logic) and `*ExtProcess.java` (External Interface) files.

## 1. Service 인터페이스 명명 규칙 (`*-process` 컴포넌트)
**총 파일 수:** 25 개 | **총 메서드 수:** 144 개

| Prefix (Verb) | Count | Example Formats | Summary |
|---|---|---|---|
| `retrieve` | 82 | retrieveFileStorage<br>retrieve{Entity}ByParent{Entity}s<br>retrieve{Entity}4DownloadBy{Entity}SearchCond<br>retrieve{Entity}{Entity}ByParentCode<br>... | 기타 비즈니스/연동 로직 |
| `modify` | 24 | modify{Entity}WithoutHis<br>modifyPassWord<br>modify{Entity}AndCard<br>modify{Entity}<br>... | 기존 데이터 수정 로직 |
| `register` | 20 | registerChargerStatusHis<br>registerMemberCard<br>registerUserRole<br>register{Entity} | 신규 데이터 등록/생성 비즈니스 로직 |
| `remove` | 10 | remove{Entity}ByRoleType<br>removeManagementFile<br>removeEmployee<br>removeUserRoleByUserId<br>... | 데이터 삭제 로직 |
| `manage` | 2 | manageFiles<br>manageFile | 기타 비즈니스/연동 로직 |
| `change` | 1 | change{Entity} | 기타 비즈니스/연동 로직 |
| `save` | 1 | save{Entity} | 기타 비즈니스/연동 로직 |
| `retrievet` | 1 | retrievetAll{Entity} | 기타 비즈니스/연동 로직 |
| `regist` | 1 | registManagementFile | 기타 비즈니스/연동 로직 |
| `is` | 1 | isReadyCardMapping{Entity} | 기타 비즈니스/연동 로직 |
| `complete` | 1 | complete{Entity} | 기타 비즈니스/연동 로직 |


## 2. ExtProcess 인터페이스 명명 규칙 (`*-external` 컴포넌트)
**총 파일 수:** 4 개 | **총 메서드 수:** 12 개

| Prefix (Verb) | Count | Example Formats | Summary |
|---|---|---|---|
| `retrieve` | 9 | retrieve{Entity}ByUserId<br>retrieveCoAdminEmployeeDtos<br>retrieveCoAdminEmployeeDto<br>retrieve{Entity}ById<br>... | 기타 비즈니스/연동 로직 |
| `save` | 1 | save{Entity} | 기타 비즈니스/연동 로직 |
| `remove` | 1 | remove{Entity} | 데이터 삭제 로직 |
| `synchronize` | 1 | synchronizeCustomerCount | 기타 비즈니스/연동 로직 |


## 3. Standard Naming Rules (Derived)

### Service (`*-process`) 규칙
- **등록 (Create)**: 주로 `register...`, `insert...`, `add...` 사용
- **조회 (Read)**: 주로 `get...`, `find...`, `search...` (Mapper와 달리 비즈니스 관점의 용어 사용)
- **수정 (Update)**: 주로 `modify...`, `update...` 사용
- **삭제 (Delete)**: 주로 `remove...`, `delete...` 사용
- **기타 비즈니스**: `process...`, `execute...` 등 행위 중심의 명명

### ExtProcess (`*-external`) 규칙
- 외부 시스템 또는 다른 MSA 도메인과의 통신을 담당하므로, 내부 Service와 유사하거나 외부 API 엔드포인트에 매핑되는 명사형/동사형 조합을 가짐.



---

# Web Layer Naming Conventions (`admin-web` Component)

This document outlines the method naming and endpoint routing conventions used in `*Controller.java` (Page/View routing) and `*Resource.java` (REST API data endpoints) files within the `admin-web` component.

## 1. Controller 명명 규칙 (화면 이동 및 View 반환) (`controller`)
**총 파일 수:** 23 개 | **총 메서드 수:** 51 개

### 1. Method Prefix (Verb) Analysis
| Prefix (Verb) | Count | Example Formats | Summary |
|---|---|---|---|
| `customer` | 5 | customerCreate<br>customerCardList<br>customerList<br>customerDetail | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `charge` | 4 | chargePointDetail<br>chargePointCreate<br>chargePointList<br>chargePointRead | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `breakdown` | 3 | breakdownList<br>breakdownDetail<br>breakdownCreate | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `charging` | 3 | chargingStationList<br>chargingStationCreate<br>chargingStationDetail | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `company` | 3 | companyCreate<br>companyList<br>companyDetail | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `employee` | 3 | employeeList<br>employeeCreate<br>employeeDetail | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `recharging` | 3 | rechargingAdjustmentList<br>rechargingList<br>rechargingCustomerList | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `car` | 3 | carModelCreate<br>carModelList<br>carModelDetail | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `menu` | 3 | menuList<br>menuCreate<br>menuDetail | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `main` | 2 | mainAdmin<br>mainOperation | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `kev` | 2 | kev001Control | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `product` | 2 | productList<br>productDetail | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `reservation` | 2 | reservationExceptionList<br>reservationList | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `common` | 2 | commonCodeDetail<br>commonCodeList | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `login` | 1 | loginPage | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `denied` | 1 | denied | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `root` | 1 | root | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `index` | 1 | index | HTML/JSP 뷰 페이지로 이동 (Controller 주로 사용) |
| `charger` | 1 | chargerStatusList | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `fmw` | 1 | fmwVersion | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `status` | 1 | statusLogList | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `control` | 1 | control | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `access` | 1 | accessLog | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `un` | 1 | unPayment | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `consult` | 1 | consultCodeList | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |

### 2. URL Endpoint Mapping Rules
- **주요 URL Prefix**: `/authority, /read, /detail, /log, /customer, /view, /commonCode, /operation`
- **URL 패턴 예시**: 
  - `/authority`
  - `charger/chargePoint`
  - `system/access`
  - `/`
  - `system`
  - `charger/breakdown`
  - `charger/statusInfo`
  - `/read`
  - `recharging/exception`
  - `/admin/main`


## 2. Resource 명명 규칙 (REST API 데이터 제공) (`resource`)
**총 파일 수:** 25 개 | **총 메서드 수:** 92 개

### 1. Method Prefix (Verb) Analysis
| Prefix (Verb) | Count | Example Formats | Summary |
|---|---|---|---|
| `search` | 30 | searchEmployeeBySearchCond<br>searchAllChildMenuList<br>search{Entity}sByParentCode<br>search{Entity}List<br>... | 데이터 조회용 (API 엔드포인트) |
| `register` | 11 | register{Entity}l<br>registerChargingStation<br>register{Entity}<br>register{Entity}Info<br>... | 신규 데이터 생성/등록 API |
| `update` | 9 | updateTargetPaymentInfo<br>updateMenu<br>updateCompany<br>updateChargePoint<br>... | 데이터 수정 API |
| `download` | 6 | download{Entity}<br>downloadChargingStationList<br>download{Entity}List<br>downloadCharePointList<br>... | 파일 입출력 로직 (엑셀 다운로드 등) |
| `find` | 6 | find{Entity}ListWithCustomer<br>findChargingScheduleBySearchCond<br>find{Entity}By{Entity}SearchCond<br>findUserInfoDtoBySearchCond<br>... | 데이터 조회용 (API 엔드포인트) |
| `get` | 5 | get{Entity}Detail<br>get{Entity}ByProdType<br>getMyUserInfo<br>getAccessIp | 데이터 조회용 (API 엔드포인트) |
| `delete` | 3 | delete{Entity}<br>deleteSpot | 데이터 삭제 API |
| `check` | 3 | checkDuple{Entity}Id<br>checkDupleLoginId<br>checkCardNo | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `remove` | 3 | removeChargingStation<br>removeCompany<br>remove{Entity}Price | 데이터 삭제 API |
| `retrieve` | 3 | retrieve{Entity}BySearchCond<br>retrieveCodeByCodeSearchCond<br>retrieveMaxCsId | 데이터 조회용 (API 엔드포인트) |
| `command` | 2 | command | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `file` | 2 | fileUpload4checkList<br>fileUpload | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `modify` | 2 | modifyUserRoleTypeAuthForMenu<br>modify{Entity} | 데이터 수정 API |
| `searc` | 2 | searc{Entity}Detail<br>searcMenuDetail | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `serach` | 1 | serach{Entity} | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `change` | 1 | changeCustStatCode | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `exist` | 1 | exist{Entity}Id | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `save` | 1 | saveChargingSchedule | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |
| `complete` | 1 | complete{Entity}Info | 기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등) |

### 2. URL Endpoint Mapping Rules
- **주요 URL Prefix**: `/check, /remove, /{menuId}, /checkDupleLoginId, /allChargePointList4Monitoring, /codes, /prodType, /status`
- **URL 패턴 예시**: 
  - `/checkDupleChargePointId/{cpId}`
  - `ws/charger/breakdown`
  - `/val/{pCode}`
  - `ws/charger`
  - `/{menuId}`
  - `ws/customer/card`
  - `/allChargePointList4Monitoring`
  - `/detail/{productId}`
  - `/maxCpId/{cpId}`
  - `/codes`


## 3. Standard Naming Rules (Derived)

### 화면 라우팅 (Controller)
- **역할**: JSP/HTML과 같은 뷰(View) 페이지 반환 및 엑셀 다운로드
- **URL 규칙**: 주로 `/system`, `/organization`, `/charger` 등 도메인/메뉴 위계에 맞는 경로를 가짐.
- **메서드 접두어**: `show...`, `index`, `excel...` 등 뷰 렌더링 및 파일 다운로드를 나타내는 동사 활용.

### API 엔드포인트 (Resource)
- **역할**: JSON 데이터를 반환하는 RESTful API (프론트엔드 AJAX 요청 처리)
- **URL 규칙**: 주로 `/api/...` 형태의 prefix를 가지며, RESTful 특징을 살려 명사 위주의 자원(Resource)을 명시
- **메서드 접두어**: `get...`, `remove...`, `modify...`, `register...`, `retrieve...` 등 HTTP 메서드 성격(CRUD)에 매핑되는 동사 활용.


---
# SQL Mapper (XML) Naming Conventions

This document outlines the Query ID naming conventions used in `*sql.xml` files within the `*-entity` components.

**총 파일 수:** 27 개 | **총 쿼리 수:** 180 개

## 1. Query ID Prefix (Verb) Analysis
| Query Type | Count | Example Formats | Summary |
|---|---|---|---|
| `<select>` | 109 | selectAllChild{Entity}<br>selectAdmin{Entity}<br>selectMaxCpId<br>select{Entity}By{Entity}<br>selectUserRoleByUserId<br>... | 데이터 조회용 쿼리 (단건, 다건, 페이징, 카운트 포함) |
| `<insert>` | 31 | insert{Entity}InfoItem<br>insertMemberCard<br>insertManagementFile<br>insert{Entity}<br>insere{Entity}<br>... | 새로운 데이터 삽입 쿼리 |
| `<update>` | 24 | update{Entity}<br>update{Entity}Csm<br>update{Entity}All<br>updateManagementFile<br>updateUserRole<br>... | 기존 데이터 변경 쿼리 |
| `<delete>` | 16 | delete{Entity}InfoItem<br>delete{Entity}ByRoleType<br>deleteManagementFile<br>delete{Entity}ByType<br>delete{Entity}<br>... | 데이터 삭제 쿼리 |

## 2. Standard Naming Rules (Derived)

- **등록 (Insert)**: `<insert id="insert{Entity}">` 형태를 사용. 복수 등록 시 `insert{Entity}List` 등 사용.
- **조회 (Select)**: `<select id="select{Entity}...">` 형태를 사용.
  - 단건 조회: `select{Entity}ById` 등 식별자 명시
  - 다건/조건 조회: `select{Entity}BySearchCond` 등 SearchCond 접미사 사용
  - 개수 조회: `count{Entity}BySearchCond` (페이징 시 select와 쌍을 이룸)
- **수정 (Update)**: `<update id="update{Entity}">` 형태를 사용.
- **삭제 (Delete)**: `<delete id="delete{Entity}">` 형태를 사용.

> 참고: XML Mapper의 `id` 값은 `*Mapper.java` (인터페이스)의 메서드명과 1:1로 정확히 일치해야 합니다.

---

# UI Framework Naming Conventions (`admin-web` Component)

This section outlines the directory structure and file naming conventions for Frontend web assets: Javascript (`*.js`) and Views (`*.jsp`).

## 1. Javascript 파일 명명 규칙
**위치:** `webapp/resources/js` | **총 파일 수:** 76 개

### 디렉토리 구조 및 파일 네이밍 분석
| Directory (Menu/Domain) | File Count | Example Files |
|---|---|---|
| `charger\breakdown` | 2 | breakdown.js<br>breakdownList.js |
| `charger\chargePoint` | 2 | chargePoint.js<br>chargePointList.js |
| `charger\chargingStation` | 2 | chargingStation.js<br>chargingStationList.js |
| `charger\csControl` | 4 | chargingStationControl.js<br>ocpp16Control.js<br>ocpp16SchedControl.js<br>ocpp20Control.js |
| `charger\statusInfo` | 1 | statusInfoList.js |
| `charger\statusLog` | 1 | statusLogList.js |
| `customer` | 3 | customer.js<br>customerCardList.js<br>customerList.js |
| `dashBoard` | 1 | dashBoard.js |
| `login` | 1 | loginPage.js |
| `ocpp` | 1 | ocppLog.js |
| `organization` | 1 | userInfo.js |
| `organization\company` | 2 | company.js<br>companyList.js |
| `organization\employee` | 2 | employee.js<br>employeeList.js |
| `popup` | 24 | breakdownCategorySearchPopup.js<br>breakdownListPopup.js<br>breakdownPopup.js<br>breakdownReceiptPopup.js<br>... |
| `product` | 2 | productDetail.js<br>productList.js |
| `recharging` | 1 | recharging.js |
| `recharging\customer` | 1 | rechargingCustomer.js |
| `recharging\exception` | 2 | excepView.js<br>list.js |
| `root` | 2 | common.js<br>queryString.js |
| `statistics` | 2 | chargerDailyStatistics.js<br>chargerMonthlyStatistics.js |
| `system` | 1 | systemAuthority.js |
| `system\access` | 1 | log.js |
| `system\breakdownCode` | 1 | breakdownCode.js |
| `system\carModel` | 2 | carModel.js<br>carModelList.js |
| `system\commonCode` | 2 | commonCodeDetail.js<br>commonCodeList.js |
| `system\consultCode` | 1 | consultCode.js |
| `system\menu` | 3 | systemMenu.js<br>systemMenuDetail.js<br>systemMenuReg.js |
| `util` | 8 | carModel.js<br>common.js<br>commonCode.js<br>commonVal.js<br>... |

### 💡 Naming Rules (Derived)
- **디렉토리 구조**: 업무 도메인(예: `charger`, `customer`, `organization`) 하위에 세부 화면 단위(예: `breakdown`, `chargePoint`)로 폴더를 구성합니다.
- **파일 명명 규칙 (camelCase)**:
  - 대메뉴/소메뉴 명칭과 동일한 영문 카멜표기법을 파일명으로 사용합니다. (예: `chargePointList.js`, `customerCardCreate.js`)
  - 목록 화면은 주로 `*List.js` 로 끝납니다.
  - 단건 조회/상세 화면은 도메인명 단독(예: `customer.js`) 또는 `*Detail.js` 등을 사용합니다.
  - 팝업이나 모달창 관련 파일은 주로 `popup` 디렉토리 하위에 위치하며, `*Popup.js` 혹은 `*Search.js` 접미사가 붙습니다.
- JS 파일은 1:1로 대응되는 JSP 파일과 동일한 이름을 가져가는 것을 원칙으로 하여, 화면 렌더링과 클라이언트 로직을 쉽게 매칭할 수 있습니다.

## 2. JSP 파일 명명 규칙
**위치:** `webapp/WEB-INF/views` | **총 파일 수:** 81 개

### 디렉토리 구조 및 파일 네이밍 분석
| Directory (Menu/Domain) | File Count | Example Files |
|---|---|---|
| `charger\breakdown` | 2 | breakdown.jsp<br>breakdownList.jsp |
| `charger\chargePoint` | 2 | chargePoint.jsp<br>chargePointList.jsp |
| `charger\chargingStation` | 2 | chargingStation.jsp<br>chargingStationList.jsp |
| `charger\csControl` | 4 | chargingStationControl.jsp<br>OCPP16Control.jsp<br>OCPP16SchedControl.jsp<br>OCPP20Control.jsp |
| `charger\statusInfo` | 1 | statusInfoList.jsp |
| `charger\statusLog` | 1 | statusLogList.jsp |
| `common` | 1 | header.jsp |
| `common\error` | 2 | err.jsp<br>err_400.jsp |
| `common\login` | 5 | denied.jsp<br>findIdPin.jsp<br>idReminder.jsp<br>loginPage.jsp<br>... |
| `common\menu` | 1 | menu.jsp |
| `common\popup` | 26 | breakdownCategorySearch.jsp<br>breakdownInfo.jsp<br>breakdownList.jsp<br>breakdownReceipt.jsp<br>... |
| `customer` | 3 | customer.jsp<br>customerCardList.jsp<br>customerList.jsp |
| `dashBoard` | 1 | dashBoard.jsp |
| `layout` | 5 | adminLayout.jsp<br>blankLayout.jsp<br>consultationLayout.jsp<br>defaultLayout.jsp<br>... |
| `ocpp` | 1 | log.jsp |
| `organization\company` | 2 | company.jsp<br>companyList.jsp |
| `organization\employee` | 2 | employee.jsp<br>employeeList.jsp |
| `product` | 2 | productDetail.jsp<br>productList.jsp |
| `recharging` | 1 | rechargingList.jsp |
| `recharging\customer` | 1 | rechargingCustomerList.jsp |
| `recharging\exception` | 2 | list.jsp<br>view.jsp |
| `root` | 1 | index.jsp |
| `statistics` | 2 | chargerDailyStatistics.jsp<br>chargerMonthlyStatistics.jsp |
| `system` | 1 | authority.jsp |
| `system\access` | 1 | log.jsp |
| `system\breakdownCode` | 1 | breakdownCodeList.jsp |
| `system\carModel` | 2 | carModel.jsp<br>carModelList.jsp |
| `system\commonCode` | 2 | commonCodeDetail.jsp<br>commonCodeList.jsp |
| `system\consultCode` | 1 | consultCodeList.jsp |
| `system\menu` | 3 | menuDetail.jsp<br>menuForm.jsp<br>menuList.jsp |

### 💡 Naming Rules (Derived)
- **디렉토리 구조**: 업무 도메인(예: `charger`, `customer`, `organization`) 하위에 세부 화면 단위(예: `breakdown`, `chargePoint`)로 폴더를 구성합니다.
- **파일 명명 규칙 (camelCase)**:
  - 대메뉴/소메뉴 명칭과 동일한 영문 카멜표기법을 파일명으로 사용합니다. (예: `chargePointList.jsp`, `customerCardCreate.jsp`)
  - 목록 화면은 주로 `*List.jsp` 로 끝납니다.
  - 단건 조회/상세 화면은 도메인명 단독(예: `customer.jsp`) 또는 `*Detail.jsp` 등을 사용합니다.
  - 팝업이나 모달창 관련 파일은 주로 `popup` 디렉토리 하위에 위치하며, `*Popup.jsp` 혹은 `*Search.jsp` 접미사가 붙습니다.
- JSP는 `common` 하위에 공통 레이아웃(`header.jsp`, 팝업 모듈 등)을 별도로 관리하여 재사용성을 높입니다.
