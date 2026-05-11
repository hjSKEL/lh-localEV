// Mock Data
const mockChargers = [
    {
        cpId: "CP001",
        cpName: "서울역 충전소 1번",
        currentVersion: 2,
        lastSyncedAt: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(),
    },
    {
        cpId: "CP002",
        cpName: "강남역 충전소 2번",
        currentVersion: 1,
        lastSyncedAt: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        cpId: "CP003",
        cpName: "판교 테크노밸리 충전소",
        currentVersion: 3,
    },
    {
        cpId: "CP004",
        cpName: "잠실역 충전소 5번",
        currentVersion: 1,
        lastSyncedAt: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        cpId: "CP005",
        cpName: "여의도 IFC 충전소",
        currentVersion: 4,
        lastSyncedAt: new Date(Date.now() - 1 * 60 * 60 * 1000).toISOString(),
    },
];

const mockVersions = {
    CP001: [
        {
            version: 1,
            updateType: "Full",
            members: [
                {
                    idTag: "USER001",
                    idTagInfo: {
                        status: "Accepted",
                        expiryDate: new Date(Date.now() + 365 * 24 * 60 * 60 * 1000).toISOString(),
                        parentIdTag: "GROUP001",
                    },
                },
                {
                    idTag: "USER002",
                    idTagInfo: {
                        status: "Blocked",
                        expiryDate: new Date(Date.now() + 180 * 24 * 60 * 60 * 1000).toISOString(),
                    },
                },
            ],
            createdAt: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString(),
            syncedAt: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString(),
            syncStatus: "Accepted",
            description: "초기 버전",
        },
        {
            version: 2,
            updateType: "Differential",
            members: [
                {
                    idTag: "USER001",
                    idTagInfo: {
                        status: "Accepted",
                        expiryDate: new Date(Date.now() + 365 * 24 * 60 * 60 * 1000).toISOString(),
                        parentIdTag: "GROUP001",
                    },
                },
                {
                    idTag: "USER002",
                    idTagInfo: {
                        status: "Blocked",
                        expiryDate: new Date(Date.now() + 180 * 24 * 60 * 60 * 1000).toISOString(),
                    },
                },
                {
                    idTag: "USER003",
                    idTagInfo: {
                        status: "Accepted",
                        parentIdTag: "GROUP002",
                    },
                },
            ],
            createdAt: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString(),
            syncedAt: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(),
            syncStatus: "Accepted",
            description: "USER003 추가",
        },
    ],
    CP002: [
        {
            version: 1,
            updateType: "Full",
            members: [
                {
                    idTag: "ADMIN001",
                    idTagInfo: {
                        status: "Accepted",
                    },
                },
            ],
            createdAt: new Date(Date.now() - 60 * 24 * 60 * 60 * 1000).toISOString(),
            syncedAt: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(),
            syncStatus: "Accepted",
        },
    ],
    CP003: [
        {
            version: 1,
            updateType: "Full",
            members: [],
            createdAt: new Date(Date.now() - 90 * 24 * 60 * 60 * 1000).toISOString(),
            syncedAt: new Date(Date.now() - 90 * 24 * 60 * 60 * 1000).toISOString(),
            syncStatus: "Accepted",
        },
        {
            version: 2,
            updateType: "Differential",
            members: [
                {
                    idTag: "GUEST001",
                    idTagInfo: {
                        status: "Accepted",
                        expiryDate: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString(),
                    },
                },
            ],
            createdAt: new Date(Date.now() - 15 * 24 * 60 * 60 * 1000).toISOString(),
            syncedAt: new Date(Date.now() - 15 * 24 * 60 * 60 * 1000).toISOString(),
            syncStatus: "Accepted",
        },
        {
            version: 3,
            updateType: "Differential",
            members: [
                {
                    idTag: "GUEST001",
                    idTagInfo: {
                        status: "Accepted",
                        expiryDate: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString(),
                    },
                },
                {
                    idTag: "GUEST002",
                    idTagInfo: {
                        status: "Expired",
                    },
                },
            ],
            createdAt: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000).toISOString(),
            description: "GUEST002 추가",
        },
    ],
    CP004: [
        {
            version: 1,
            updateType: "Full",
            members: [
                {
                    idTag: "USER101",
                    idTagInfo: {
                        status: "Accepted",
                    },
                },
                {
                    idTag: "USER102",
                    idTagInfo: {
                        status: "Accepted",
                    },
                },
            ],
            createdAt: new Date(Date.now() - 45 * 24 * 60 * 60 * 1000).toISOString(),
            syncedAt: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000).toISOString(),
            syncStatus: "Accepted",
        },
    ],
    CP005: [
        {
            version: 1,
            updateType: "Full",
            members: [],
            createdAt: new Date(Date.now() - 120 * 24 * 60 * 60 * 1000).toISOString(),
            syncedAt: new Date(Date.now() - 120 * 24 * 60 * 60 * 1000).toISOString(),
            syncStatus: "Accepted",
        },
        {
            version: 2,
            updateType: "Differential",
            members: [
                {
                    idTag: "VIP001",
                    idTagInfo: {
                        status: "Accepted",
                    },
                },
            ],
            createdAt: new Date(Date.now() - 90 * 24 * 60 * 60 * 1000).toISOString(),
            syncedAt: new Date(Date.now() - 90 * 24 * 60 * 60 * 1000).toISOString(),
            syncStatus: "Accepted",
        },
        {
            version: 3,
            updateType: "Differential",
            members: [
                {
                    idTag: "VIP001",
                    idTagInfo: {
                        status: "Accepted",
                    },
                },
                {
                    idTag: "VIP002",
                    idTagInfo: {
                        status: "Accepted",
                    },
                },
            ],
            createdAt: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString(),
            syncedAt: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString(),
            syncStatus: "Accepted",
        },
        {
            version: 4,
            updateType: "Differential",
            members: [
                {
                    idTag: "VIP001",
                    idTagInfo: {
                        status: "Accepted",
                    },
                },
                {
                    idTag: "VIP002",
                    idTagInfo: {
                        status: "Accepted",
                    },
                },
                {
                    idTag: "VIP003",
                    idTagInfo: {
                        status: "Accepted",
                    },
                },
            ],
            createdAt: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
            syncedAt: new Date(Date.now() - 1 * 60 * 60 * 1000).toISOString(),
            syncStatus: "Accepted",
        },
    ],
};

// Utility functions
function formatDate(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

function showToast(message, type = 'success') {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.textContent = message;
    container.appendChild(toast);
    
    setTimeout(() => {
        toast.style.animation = 'fadeOut 0.3s';
        setTimeout(() => {
            container.removeChild(toast);
        }, 300);
    }, 3000);
}
