// 상세 페이지 JavaScript

// URL 파라미터에서 ID 가져오기
function getChargerIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return parseInt(urlParams.get('id'));
}

// 현재 충전기 정보
let currentCharger = null;
let chargerReservations = [];

// DOM 요소
const btnListEl = document.getElementById('btnList');
const reservationFormEl = document.getElementById('reservationForm');
const btnReserveEl = document.getElementById('btnReserve');
const warningAlertEl = document.getElementById('warningAlert');
const warningMessageEl = document.getElementById('warningMessage');
const responseAlertEl = document.getElementById('responseAlert');

// 이벤트 리스너
btnListEl.addEventListener('click', () => {
    window.location.href = 'list.html';
});

reservationFormEl.addEventListener('submit', handleReservationSubmit);

// 초기 로드
window.addEventListener('DOMContentLoaded', () => {
    const chargerId = getChargerIdFromUrl();
    
    if (!chargerId || isNaN(chargerId)) {
        alert('Invalid charger ID');
        window.location.href = 'list.html';
        return;
    }
    
    // 충전기 찾기
    currentCharger = chargers.find(c => c.id === chargerId);
    
    if (!currentCharger) {
        alert('Charger not found');
        window.location.href = 'list.html';
        return;
    }
    
    // 예약 이력 필터링
    chargerReservations = reservationHistories.filter(res => res.chargerId === chargerId);
    
    // 페이지 렌더링
    renderChargerDetail();
    renderReservationForm();
    renderReservationHistory();
});

// 충전기 상세 정보 렌더링
function renderChargerDetail() {
    const chargerId = currentCharger.id.toString().padStart(6, '0') + '-01';
    
    document.getElementById('chargerUniqueId').value = chargerId;
    document.getElementById('stationId').value = currentCharger.id;
    document.getElementById('stationName').value = currentCharger.name;
    document.getElementById('chargerType').innerHTML = `<option value="">${currentCharger.type}</option>`;
    document.getElementById('chargerCapacity').innerHTML = `<option value="">${currentCharger.power}</option>`;
    document.getElementById('ratePlan').value = currentCharger.price;
    document.getElementById('connector').value = currentCharger.connector;
    document.getElementById('location').textContent = currentCharger.location;
    document.getElementById('lastMaintenance').textContent = currentCharger.lastMaintenance;
    
    // 상태 배지
    const statusBadgeClass = getStatusBadgeClass(currentCharger.status);
    const chargerStatusEl = document.getElementById('chargerStatus');
    chargerStatusEl.className = `badge ${statusBadgeClass}`;
    chargerStatusEl.textContent = currentCharger.status;
}

// 예약 폼 렌더링
function renderReservationForm() {
    // 예약 ID 생성
    const reservationId = Math.floor(Math.random() * 100000);
    document.getElementById('reservationId').value = reservationId;
    
    // 최소 날짜 설정
    const now = new Date();
    const minDateTime = now.toISOString().slice(0, 16);
    document.getElementById('expiryDate').min = minDateTime;
    
    // 사용 불가능한 경우 경고 표시
    if (currentCharger.status !== 'Available') {
        warningAlertEl.style.display = 'block';
        warningMessageEl.textContent = `This charger is currently ${currentCharger.status.toLowerCase()} and cannot be reserved.`;
        
        // 폼 비활성화
        document.getElementById('expiryDate').disabled = true;
        document.getElementById('idTag').disabled = true;
        document.getElementById('parentIdTag').disabled = true;
        btnReserveEl.disabled = true;
    } else {
        warningAlertEl.style.display = 'none';
    }
}

// 예약 제출
function handleReservationSubmit(e) {
    e.preventDefault();
    
    if (currentCharger.status !== 'Available') {
        showResponse('Rejected', currentCharger.status, false);
        return;
    }
    
    // 로딩 상태
    btnReserveEl.textContent = 'Processing...';
    btnReserveEl.disabled = true;
    
    // 성공 시뮬레이션 (1초 후)
    setTimeout(() => {
        showResponse('Accepted', 'Accepted', true);
        btnReserveEl.textContent = 'Reserve Now';
        btnReserveEl.disabled = false;
    }, 1000);
}

// 응답 표시
function showResponse(status, responseStatus, isSuccess) {
    responseAlertEl.style.display = 'block';
    responseAlertEl.className = `alert ${isSuccess ? 'alert-success' : 'alert-danger'}`;
    
    const responseTitleEl = document.getElementById('responseTitle');
    const responseMessageEl = document.getElementById('responseMessage');
    const responseDetailsEl = document.getElementById('responseDetails');
    
    if (isSuccess) {
        responseTitleEl.textContent = 'Reservation Accepted';
        responseMessageEl.textContent = `Your reservation #${document.getElementById('reservationId').value} has been confirmed.`;
        
        // 상세 정보 표시
        responseDetailsEl.style.display = 'block';
        document.getElementById('resCharger').textContent = currentCharger.name;
        document.getElementById('resLocation').textContent = currentCharger.location;
        
        const expiryDate = new Date(document.getElementById('expiryDate').value);
        const expiryStr = expiryDate.toLocaleString('en-US', {
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
        document.getElementById('resExpiry').textContent = expiryStr;
        document.getElementById('resIdTag').textContent = document.getElementById('idTag').value;
    } else {
        responseTitleEl.textContent = 'Reservation Failed';
        responseDetailsEl.style.display = 'none';
        
        const messages = {
            'Faulted': 'The charger is currently faulted. Please select another charger.',
            'Occupied': 'The charger is currently occupied. Please try again later.',
            'Rejected': 'The reservation was rejected. Please check your information.',
            'Unavailable': 'The charger is unavailable. Please contact support.'
        };
        
        responseMessageEl.textContent = messages[responseStatus] || 'An error occurred.';
    }
}

// 예약 이력 렌더링
function renderReservationHistory() {
    const historyCountEl = document.getElementById('historyCount');
    const historyTableBodyEl = document.getElementById('historyTableBody');
    const historyPaginationEl = document.getElementById('historyPagination');
    
    historyCountEl.textContent = chargerReservations.length;
    
    if (chargerReservations.length === 0) {
        historyTableBodyEl.innerHTML = `
            <tr>
                <td colspan="10" style="padding: 32px; color: #999;">
                    No reservation history
                </td>
            </tr>
        `;
        historyPaginationEl.style.display = 'none';
        return;
    }
    
    historyTableBodyEl.innerHTML = '';
    historyPaginationEl.style.display = 'flex';
    
    chargerReservations.forEach((reservation, index) => {
        const row = document.createElement('tr');
        const chargerId = reservation.chargerId.toString().padStart(6, '0') + '-01';
        const statusBadgeClass = getReservationStatusBadgeClass(reservation.status);
        
        row.innerHTML = `
            <td>${index + 1}</td>
            <td>
                <span style="color: #1ab394; font-family: monospace;">${reservation.id}</span>
            </td>
            <td>${chargerId}</td>
            <td>${reservation.idTag}</td>
            <td>${reservation.parentIdTag || '-'}</td>
            <td>${reservation.expiryDate}</td>
            <td>${reservation.startDate || '-'}</td>
            <td>${reservation.endDate || '-'}</td>
            <td>
                <span class="badge ${statusBadgeClass}">${reservation.status}</span>
            </td>
            <td>${reservation.createdDate}</td>
        `;
        
        historyTableBodyEl.appendChild(row);
    });
}

// 상태 배지 클래스 (충전기)
function getStatusBadgeClass(status) {
    const statusMap = {
        'Available': 'badge-available',
        'Occupied': 'badge-occupied',
        'Faulted': 'badge-faulted',
        'Unavailable': 'badge-unavailable'
    };
    return statusMap[status] || 'badge-unavailable';
}

// 상태 배지 클래스 (예약)
function getReservationStatusBadgeClass(status) {
    const statusMap = {
        'Completed': 'badge-completed',
        'Expired': 'badge-expired',
        'Cancelled': 'badge-cancelled',
        'Rejected': 'badge-rejected',
        'Accepted': 'badge-accepted'
    };
    return statusMap[status] || 'badge-expired';
}
