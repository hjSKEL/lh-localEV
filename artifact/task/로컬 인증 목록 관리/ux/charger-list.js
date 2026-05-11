// State
let filteredChargers = [...mockChargers];

// Initialize
document.addEventListener('DOMContentLoaded', function() {
    renderTable();
});

// Get charger stats
function getChargerStats(chargerId) {
    const versions = mockVersions[chargerId] || [];
    const currentVersion = versions[versions.length - 1];
    const totalMembers = currentVersion?.members.length || 0;
    const totalVersions = versions.length;
    return { totalMembers, totalVersions, currentVersion };
}

// Render table
function renderTable() {
    const tbody = document.getElementById('chargerTableBody');
    const emptyState = document.getElementById('emptyState');
    const totalCount = document.getElementById('totalCount');
    
    totalCount.textContent = filteredChargers.length;
    
    if (filteredChargers.length === 0) {
        tbody.innerHTML = '';
        emptyState.style.display = 'block';
        return;
    }
    
    emptyState.style.display = 'none';
    
    const rows = filteredChargers.map((charger, index) => {
        const stats = getChargerStats(charger.cpId);
        
        let syncStatusHtml = '<span class="text-muted">-</span>';
        if (stats.currentVersion?.syncStatus) {
            const status = stats.currentVersion.syncStatus;
            const statusClass = status === 'Accepted' ? 'success' : 'error';
            const statusText = status === 'Accepted' ? '동기화됨' : '실패';
            const iconSvg = status === 'Accepted' 
                ? '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>'
                : '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>';
            
            syncStatusHtml = `
                <div class="sync-status ${statusClass}">
                    ${iconSvg}
                    ${statusText}
                </div>
            `;
        }
        
        return `
            <tr>
                <td class="text-center font-medium">${index + 1}</td>
                <td class="font-medium">${charger.cpName}</td>
                <td>
                    <a href="charger-detail.html?id=${charger.cpId}" class="table-link">
                        ${charger.cpId}
                    </a>
                </td>
                <td class="text-center">
                    <span class="badge badge-outline">v${charger.currentVersion || 0}</span>
                </td>
                <td class="text-center">${stats.totalVersions}</td>
                <td class="text-center">${stats.totalMembers}</td>
                <td>
                    ${charger.lastSyncedAt 
                        ? `<span>${formatDate(charger.lastSyncedAt)}</span>` 
                        : '<span class="text-muted">-</span>'}
                </td>
                <td class="text-center">${syncStatusHtml}</td>
            </tr>
        `;
    }).join('');
    
    tbody.innerHTML = rows;
}

// Search and filter
function handleSearch() {
    applyFilters();
}

function handleReset() {
    document.getElementById('searchKey').value = '';
    document.getElementById('searchType').value = 'cpName';
    applyFilters();
}

function applyFilters() {
    const searchType = document.getElementById('searchType').value;
    const searchKey = document.getElementById('searchKey').value.toLowerCase();
    
    filteredChargers = mockChargers.filter(charger => {
        if (!searchKey) return true;
        
        if (searchType === 'cpName') {
            return charger.cpName.toLowerCase().includes(searchKey);
        } else {
            return charger.cpId.toLowerCase().includes(searchKey);
        }
    });
    
    renderTable();
}

// Listen for Enter key on search input
document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchKey');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                handleSearch();
            }
        });
    }
});
