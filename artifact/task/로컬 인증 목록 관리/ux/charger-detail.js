// State
let currentChargerId = null;
let versionsData = {};
let currentVersions = [];
let selectedVersion = null;
let draftVersion = null;
let editingMember = null;
let deletingIdTag = null;
let filterStatus = 'all';
let searchIdTag = '';

// Status mappings
const statusColors = {
    'Accepted': 'badge-accepted',
    'Blocked': 'badge-blocked',
    'Expired': 'badge-expired',
    'Invalid': 'badge-invalid',
    'ConcurrentTx': 'badge-concurrent'
};

const statusLabels = {
    'Accepted': '승인됨',
    'Blocked': '차단됨',
    'Expired': '만료됨',
    'Invalid': '무효',
    'ConcurrentTx': '동시 거래'
};

const syncStatusColors = {
    'Accepted': 'badge-accepted',
    'Failed': 'badge-blocked',
    'NotSupported': 'badge-invalid',
    'VersionMismatch': 'badge-expired'
};

const syncStatusLabels = {
    'Accepted': '승인됨',
    'Failed': '실패',
    'NotSupported': '지원되지 않음',
    'VersionMismatch': '버전 불일치'
};

// Initialize
document.addEventListener('DOMContentLoaded', function() {
    // Get charger ID from URL
    const urlParams = new URLSearchParams(window.location.search);
    currentChargerId = urlParams.get('id');
    
    if (!currentChargerId) {
        window.location.href = 'charger-list.html';
        return;
    }
    
    // Load data
    versionsData = JSON.parse(JSON.stringify(mockVersions)); // Deep clone
    currentVersions = versionsData[currentChargerId] || [];
    
    // Set initial selected version
    if (currentVersions.length > 0) {
        selectedVersion = currentVersions[currentVersions.length - 1].version;
    }
    
    // Render
    document.getElementById('chargerId').textContent = currentChargerId;
    renderVersionList();
    renderMemberList();
});

// Navigation
function goBack() {
    window.location.href = 'charger-list.html';
}

// Version management
function renderVersionList() {
    const versionList = document.getElementById('versionList');
    
    let html = '';
    
    // Draft version
    if (draftVersion) {
        html += `
            <div class="version-item draft">
                <div class="version-header">
                    <div>
                        <div class="version-title">Version ${draftVersion.version}</div>
                        <span class="badge badge-draft" style="margin-top: 4px;">작성 중</span>
                    </div>
                    <div class="version-count">${draftVersion.members.length}명</div>
                </div>
                <div class="version-actions">
                    <button class="btn btn-primary btn-sm" onclick="saveDraft()">
                        <svg class="icon-sm" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                            <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"></path>
                            <polyline points="17 21 17 13 7 13 7 21"></polyline>
                            <polyline points="7 3 7 8 15 8"></polyline>
                        </svg>
                        저장
                    </button>
                    <button class="btn btn-outline btn-sm" onclick="cancelDraft()">취소</button>
                </div>
            </div>
        `;
    }
    
    // Existing versions
    if (currentVersions.length === 0 && !draftVersion) {
        html = '<div class="empty-state"><p>버전이 없습니다</p></div>';
    } else {
        currentVersions.forEach(version => {
            const isActive = !draftVersion && selectedVersion === version.version;
            html += `
                <div class="version-item ${isActive ? 'active' : ''}" onclick="selectVersion(${version.version})">
                    <div class="version-header">
                        <div>
                            <div class="version-title">Version ${version.version}</div>
                            <div class="version-date">${formatDate(version.createdAt)}</div>
                        </div>
                        <div class="version-count">${version.members.length}명</div>
                    </div>
                    ${version.syncStatus ? `
                        <span class="badge ${syncStatusColors[version.syncStatus]}" style="margin-top: 8px;">
                            ${syncStatusLabels[version.syncStatus]}
                        </span>
                    ` : ''}
                </div>
            `;
        });
    }
    
    versionList.innerHTML = html;
}

function createNewVersion() {
    const latestVersion = currentVersions.length > 0 
        ? Math.max(...currentVersions.map(v => v.version))
        : 0;
    
    draftVersion = {
        version: latestVersion + 1,
        updateType: "Differential",
        members: [],
        createdAt: new Date().toISOString(),
        description: "새 버전"
    };
    
    selectedVersion = null;
    renderVersionList();
    renderMemberList();
}

function saveDraft() {
    if (!draftVersion) return;
    
    currentVersions.push(draftVersion);
    versionsData[currentChargerId] = currentVersions;
    selectedVersion = draftVersion.version;
    draftVersion = null;
    
    renderVersionList();
    renderMemberList();
    showToast('버전이 저장되었습니다');
}

function cancelDraft() {
    draftVersion = null;
    if (currentVersions.length > 0) {
        selectedVersion = currentVersions[currentVersions.length - 1].version;
    }
    renderVersionList();
    renderMemberList();
}

function selectVersion(version) {
    selectedVersion = version;
    draftVersion = null;
    renderVersionList();
    renderMemberList();
}

// Member list
function getCurrentVersionData() {
    if (draftVersion) return draftVersion;
    return currentVersions.find(v => v.version === selectedVersion) || null;
}

function renderMemberList() {
    const versionData = getCurrentVersionData();
    const emptyVersionState = document.getElementById('emptyVersionState');
    const versionContent = document.getElementById('versionContent');
    const versionInfo = document.getElementById('versionInfo');
    const exportBtn = document.getElementById('exportBtn');
    const syncBtn = document.getElementById('syncBtn');
    const updateTypeSelect = document.getElementById('updateType');
    
    if (!versionData) {
        emptyVersionState.style.display = 'flex';
        versionContent.style.display = 'none';
        exportBtn.disabled = true;
        return;
    }
    
    emptyVersionState.style.display = 'none';
    versionContent.style.display = 'block';
    exportBtn.disabled = false;
    
    // Update header info
    versionInfo.textContent = `Version ${versionData.version} • ${versionData.updateType} • ${versionData.members.length}명`;
    
    // Show/hide sync button and update type selector
    if (draftVersion) {
        syncBtn.style.display = 'none';
        updateTypeSelect.style.display = 'block';
        updateTypeSelect.value = versionData.updateType;
    } else {
        syncBtn.style.display = 'inline-flex';
        updateTypeSelect.style.display = 'none';
        syncBtn.disabled = versionData.members.length === 0;
    }
    
    // Filter members
    const filteredMembers = versionData.members.filter(member => {
        const matchesStatus = filterStatus === 'all' || member.idTagInfo?.status === filterStatus;
        const matchesSearch = !searchIdTag || member.idTag.toLowerCase().includes(searchIdTag.toLowerCase());
        return matchesStatus && matchesSearch;
    });
    
    // Update count
    document.getElementById('memberCount').textContent = filteredMembers.length;
    
    // Render table
    const tbody = document.getElementById('memberTableBody');
    const emptyState = document.getElementById('emptyMemberState');
    const table = document.getElementById('memberTable');
    
    if (filteredMembers.length === 0) {
        table.style.display = 'none';
        emptyState.style.display = 'flex';
        return;
    }
    
    table.style.display = 'table';
    emptyState.style.display = 'none';
    
    const rows = filteredMembers.map((member, index) => {
        const status = member.idTagInfo?.status || 'Accepted';
        return `
            <tr>
                <td class="text-center font-medium">${index + 1}</td>
                <td class="font-mono font-medium">${member.idTag}</td>
                <td>
                    <span class="badge ${statusColors[status]}">
                        ${statusLabels[status]}
                    </span>
                </td>
                <td>
                    ${member.idTagInfo?.expiryDate 
                        ? formatDate(member.idTagInfo.expiryDate)
                        : '<span class="text-muted">-</span>'}
                </td>
                <td>
                    ${member.idTagInfo?.parentIdTag 
                        ? `<span class="font-mono">${member.idTagInfo.parentIdTag}</span>`
                        : '<span class="text-muted">-</span>'}
                </td>
                <td class="text-center">
                    <button class="btn btn-ghost" onclick='editMember(${JSON.stringify(member).replace(/'/g, "&apos;")})'>
                        <svg class="icon-sm" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                            <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                        </svg>
                    </button>
                    <button class="btn btn-ghost btn-ghost-destructive" onclick="deleteMember('${member.idTag}')">
                        <svg class="icon-sm" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                            <polyline points="3 6 5 6 21 6"></polyline>
                            <path d="m19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                        </svg>
                    </button>
                </td>
            </tr>
        `;
    }).join('');
    
    tbody.innerHTML = rows;
}

// Filters
function applyFilters() {
    filterStatus = document.getElementById('filterStatus').value;
    searchIdTag = document.getElementById('searchIdTag').value;
    renderMemberList();
}

function resetFilters() {
    filterStatus = 'all';
    searchIdTag = '';
    document.getElementById('filterStatus').value = 'all';
    document.getElementById('searchIdTag').value = '';
    renderMemberList();
}

function updateVersionType() {
    if (!draftVersion) return;
    draftVersion.updateType = document.getElementById('updateType').value;
    renderVersionList();
    renderMemberList();
}

// Member dialog
function openAddMemberDialog() {
    const versionData = getCurrentVersionData();
    if (!versionData) {
        showToast('버전을 선택하거나 생성해주세요', 'error');
        return;
    }
    
    editingMember = null;
    document.getElementById('dialogTitle').textContent = '회원 추가';
    document.getElementById('idTag').value = '';
    document.getElementById('idTag').disabled = false;
    document.getElementById('status').value = 'Accepted';
    document.getElementById('expiryDate').value = '';
    document.getElementById('parentIdTag').value = '';
    document.getElementById('memberDialog').style.display = 'flex';
}

function editMember(member) {
    editingMember = member;
    document.getElementById('dialogTitle').textContent = '회원 수정';
    document.getElementById('idTag').value = member.idTag;
    document.getElementById('idTag').disabled = true;
    document.getElementById('status').value = member.idTagInfo?.status || 'Accepted';
    
    // Format datetime for input
    if (member.idTagInfo?.expiryDate) {
        const date = new Date(member.idTagInfo.expiryDate);
        const formatted = date.toISOString().slice(0, 16);
        document.getElementById('expiryDate').value = formatted;
    } else {
        document.getElementById('expiryDate').value = '';
    }
    
    document.getElementById('parentIdTag').value = member.idTagInfo?.parentIdTag || '';
    document.getElementById('memberDialog').style.display = 'flex';
}

function closeMemberDialog() {
    document.getElementById('memberDialog').style.display = 'none';
    editingMember = null;
}

function saveMember() {
    const versionData = getCurrentVersionData();
    if (!versionData) return;
    
    const idTag = document.getElementById('idTag').value.trim();
    const status = document.getElementById('status').value;
    const expiryDate = document.getElementById('expiryDate').value;
    const parentIdTag = document.getElementById('parentIdTag').value.trim();
    
    if (!idTag) {
        showToast('ID Tag를 입력해주세요', 'error');
        return;
    }
    
    // Check duplicate
    if (!editingMember && versionData.members.some(m => m.idTag === idTag)) {
        showToast('이미 존재하는 ID Tag입니다', 'error');
        return;
    }
    
    const member = {
        idTag: idTag,
        idTagInfo: {
            status: status,
            ...(expiryDate && { expiryDate: new Date(expiryDate).toISOString() }),
            ...(parentIdTag && { parentIdTag: parentIdTag })
        }
    };
    
    if (editingMember) {
        // Update
        const index = versionData.members.findIndex(m => m.idTag === editingMember.idTag);
        versionData.members[index] = member;
        showToast('회원 정보가 수정되었습니다');
    } else {
        // Add
        versionData.members.push(member);
        showToast('회원이 추가되었습니다');
    }
    
    closeMemberDialog();
    renderMemberList();
}

// Delete member
function deleteMember(idTag) {
    deletingIdTag = idTag;
    document.getElementById('deleteIdTag').textContent = idTag;
    document.getElementById('deleteDialog').style.display = 'flex';
}

function closeDeleteDialog() {
    document.getElementById('deleteDialog').style.display = 'none';
    deletingIdTag = null;
}

function confirmDelete() {
    const versionData = getCurrentVersionData();
    if (!versionData || !deletingIdTag) return;
    
    versionData.members = versionData.members.filter(m => m.idTag !== deletingIdTag);
    
    closeDeleteDialog();
    renderMemberList();
    showToast('회원이 삭제되었습니다');
}

// Sync
function syncToCharger() {
    const versionData = getCurrentVersionData();
    if (!versionData || draftVersion) return;
    
    const syncBtn = document.getElementById('syncBtn');
    const syncIcon = syncBtn.querySelector('.sync-icon');
    
    syncBtn.disabled = true;
    syncIcon.classList.add('spinning');
    syncBtn.textContent = '';
    syncBtn.appendChild(syncIcon);
    syncBtn.appendChild(document.createTextNode(' 동기화 중...'));
    
    // Simulate sync
    setTimeout(() => {
        const success = Math.random() > 0.2;
        
        // Update version with sync result
        const versionIndex = currentVersions.findIndex(v => v.version === versionData.version);
        if (versionIndex !== -1) {
            currentVersions[versionIndex].syncedAt = new Date().toISOString();
            currentVersions[versionIndex].syncStatus = success ? 'Accepted' : 'Failed';
        }
        
        syncBtn.disabled = false;
        syncIcon.classList.remove('spinning');
        syncBtn.textContent = '';
        syncBtn.appendChild(syncIcon);
        syncBtn.appendChild(document.createTextNode(' 충전기와 동기화'));
        
        if (success) {
            showToast('로컬 리스트가 동기화되었습니다');
        } else {
            showToast('동기화 실패', 'error');
        }
        
        renderVersionList();
    }, 1500);
}

// Export/Import
function exportData() {
    const versionData = getCurrentVersionData();
    if (!versionData) return;
    
    const data = {
        chargerId: currentChargerId,
        version: versionData
    };
    
    const json = JSON.stringify(data, null, 2);
    const blob = new Blob([json], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${currentChargerId}_v${versionData.version}.json`;
    a.click();
    URL.revokeObjectURL(url);
    
    showToast('리스트가 내보내기되었습니다');
}

function importData(event) {
    const file = event.target.files[0];
    if (!file) return;
    
    const reader = new FileReader();
    reader.onload = function(e) {
        try {
            const json = JSON.parse(e.target.result);
            if (json.version) {
                currentVersions.push(json.version);
                versionsData[currentChargerId] = currentVersions;
                renderVersionList();
                renderMemberList();
                showToast('리스트가 가져오기되었습니다');
            }
        } catch (error) {
            showToast('JSON 파일을 읽는 중 오류가 발생했습니다', 'error');
        }
    };
    reader.readAsText(file);
    event.target.value = '';
}
