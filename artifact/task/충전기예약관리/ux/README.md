# 충전기 예약 시스템 - HTML5/CSS/JavaScript 추출본

이 폴더는 React 기반 충전기 예약 시스템을 순수 HTML5, CSS, JavaScript로 추출한 버전입니다.

## 파일 구조

```
exported/
├── list.html          # 충전기 목록 페이지
├── detail.html        # 충전기 상세 페이지
├── style.css          # 공통 스타일시트
├── data.js            # 충전기 및 예약 데이터
├── list.js            # 목록 페이지 JavaScript
├── detail.js          # 상세 페이지 JavaScript
└── README.md          # 이 파일
```

## 실행 방법

1. **로컬 웹 서버 실행**
   
   이 파일들은 로컬 파일 시스템에서 직접 열면 작동하지만, 웹 서버를 통해 실행하는 것을 권장합니다.

   ```bash
   # Python 3가 설치된 경우
   python -m http.server 8000
   
   # Node.js의 http-server가 설치된 경우
   npx http-server
   ```

2. **브라우저에서 열기**
   
   ```
   http://localhost:8000/list.html
   ```

## 기능 설명

### 목록 페이지 (list.html)

- **충전기 목록 조회**: 전체 충전기 목록을 테이블 형식으로 표시
- **상태 필터링**: Available, Charging, Faulted, Unavailable 상태별 필터
- **검색 기능**: Station ID 또는 Station Name으로 검색
- **상세 페이지 이동**: 충전기 ID 클릭 시 상세 페이지로 이동

### 상세 페이지 (detail.html)

- **충전기 정보 표시**: 선택한 충전기의 상세 정보
- **예약 기능**: 
  - Expiry Date, ID Tag, Parent ID Tag 입력
  - Available 상태일 때만 예약 가능
  - 예약 성공/실패 메시지 표시
- **예약 이력**: 해당 충전기의 과거 예약 이력 표시
  - 예약 ID, 상태, 시작/종료 시간 등

## 데이터 구조

### 충전기 데이터 (chargers)

```javascript
{
  id: 1,
  name: '충전기 A-01',
  type: 'Fast',
  status: 'Available',
  power: '50kW',
  location: '서울시 강남구...',
  price: '400원/kWh',
  connector: 'DC콤보',
  lastMaintenance: '2026-03-01'
}
```

### 예약 이력 데이터 (reservationHistories)

```javascript
{
  id: 'RES00000000001',
  chargerId: 1,
  chargerName: '충전기 A-01',
  reservationId: 12345,
  idTag: 'USER001',
  parentIdTag: 'PARENT001',
  expiryDate: '2026-03-08 18:00:00',
  status: 'Completed',
  createdDate: '2026-03-08 14:30:00',
  startDate: '2026-03-08 15:00:00',
  endDate: '2026-03-08 16:45:00'
}
```

## 커스터마이징

### 데이터 수정

`data.js` 파일을 수정하여 충전기 및 예약 데이터를 변경할 수 있습니다.

### 스타일 수정

`style.css` 파일을 수정하여 디자인을 변경할 수 있습니다. Bootstrap 스타일을 기반으로 작성되었습니다.

### 기능 추가

- `list.js`: 목록 페이지 로직 수정
- `detail.js`: 상세 페이지 로직 수정

## 브라우저 호환성

- Chrome, Firefox, Safari, Edge 최신 버전 지원
- IE11 이하는 지원하지 않습니다 (ES6+ 문법 사용)

## 주의사항

- 이 버전은 프론트엔드만 구현된 정적 웹페이지입니다
- 실제 API 연동이나 데이터베이스 연결이 필요한 경우 백엔드 개발이 필요합니다
- 예약 기능은 시뮬레이션이며, 실제로 데이터가 저장되지 않습니다

## 기술 스택

- HTML5
- CSS3
- Vanilla JavaScript (ES6+)
- Bootstrap 스타일 기반 커스텀 CSS

## 라이선스

이 프로젝트는 예제용으로 제작되었습니다.
