// Task 5: Contact Management System Application Logic with Interactive Login & Session Storage

let currentPage = 0;
const pageSize = 5;
let totalPages = 1;
let deleteTargetId = null;
let searchDebounceTimer = null;
let currentContacts = []; // Store loaded contact objects for safe editing

// Session Auth State
let activeUsername = "";
let activePassword = "";

document.addEventListener('DOMContentLoaded', () => {
    // Check if session credentials exist in sessionStorage
    const savedUser = sessionStorage.getItem('admin_user');
    const savedPass = sessionStorage.getItem('admin_pass');

    if (savedUser && savedPass) {
        activeUsername = savedUser;
        activePassword = savedPass;
        verifyAndShowDashboard();
    } else {
        showLoginOverlay();
    }
});

function showLoginOverlay() {
    const loginOverlay = document.getElementById('loginModalOverlay');
    const dashboardContainer = document.getElementById('dashboardContainer');
    const loginErrorMessage = document.getElementById('loginErrorMessage');

    if (loginOverlay) {
        loginOverlay.style.display = 'flex';
        loginOverlay.classList.add('active');
    }
    if (dashboardContainer) dashboardContainer.style.display = 'none';
    if (loginErrorMessage) loginErrorMessage.style.display = 'none';
}

function hideLoginOverlay() {
    const loginOverlay = document.getElementById('loginModalOverlay');
    if (loginOverlay) {
        loginOverlay.style.display = 'none';
        loginOverlay.classList.remove('active');
    }
}

function getAuthHeader() {
    if (!activeUsername || !activePassword) return {};
    try {
        const creds = btoa(unescape(encodeURIComponent(`${activeUsername}:${activePassword}`)));
        return { 'Authorization': `Basic ${creds}` };
    } catch (e) {
        return {};
    }
}

async function handleLoginSubmit(e) {
    e.preventDefault();
    const user = document.getElementById('loginUsername').value.trim();
    const pass = document.getElementById('loginPassword').value.trim();
    const btn = document.getElementById('loginSubmitBtn');
    const err = document.getElementById('loginErrorMessage');

    if (err) err.style.display = 'none';
    if (btn) {
        btn.disabled = true;
        btn.innerText = "Authenticating...";
    }

    activeUsername = user;
    activePassword = pass;

    const success = await loadContacts(false);
    if (btn) {
        btn.disabled = false;
        btn.innerText = "Sign In to Dashboard";
    }

    if (success) {
        // Save session
        sessionStorage.setItem('admin_user', user);
        sessionStorage.setItem('admin_pass', pass);
        hideLoginOverlay();
        if (document.getElementById('dashboardContainer')) document.getElementById('dashboardContainer').style.display = 'block';
        if (document.getElementById('loggedInUserDisplay')) document.getElementById('loggedInUserDisplay').innerText = user;
    } else {
        if (err) err.style.display = 'block';
        activeUsername = "";
        activePassword = "";
        sessionStorage.removeItem('admin_user');
        sessionStorage.removeItem('admin_pass');
    }
}

async function verifyAndShowDashboard() {
    const success = await loadContacts(false);
    if (success) {
        hideLoginOverlay();
        if (document.getElementById('dashboardContainer')) document.getElementById('dashboardContainer').style.display = 'block';
        if (document.getElementById('loggedInUserDisplay')) document.getElementById('loggedInUserDisplay').innerText = activeUsername;
    } else {
        showLoginOverlay();
    }
}

function handleLogout() {
    activeUsername = "";
    activePassword = "";
    sessionStorage.removeItem('admin_user');
    sessionStorage.removeItem('admin_pass');
    const uInput = document.getElementById('loginUsername');
    const pInput = document.getElementById('loginPassword');
    if (uInput) uInput.value = "";
    if (pInput) pInput.value = "";
    showLoginOverlay();
}

async function loadContacts(showAlertOnSuccess = false) {
    const searchInput = document.getElementById('searchInput');
    const sortSelect = document.getElementById('sortSelect');
    
    const search = searchInput ? searchInput.value.trim() : "";
    const sort = sortSelect ? sortSelect.value : "createdAt,desc";

    const url = new URL('/contacts', window.location.origin);
    url.searchParams.append('page', currentPage);
    url.searchParams.append('size', pageSize);
    url.searchParams.append('sort', sort);
    if (search) {
        url.searchParams.append('search', search);
    }

    try {
        const response = await fetch(url, {
            headers: {
                ...getAuthHeader(),
                'Accept': 'application/json'
            }
        });

        if (response.status === 401 || response.status === 403) {
            return false;
        }

        if (!response.ok) {
            return false;
        }

        const data = await response.json();
        currentContacts = data.content || [];
        renderTable(currentContacts);
        renderPagination(data);

        if (showAlertOnSuccess) {
            alert('✅ Authenticated successfully as ADMIN!');
        }
        return true;
    } catch (err) {
        console.error('Error fetching contacts:', err);
        return false;
    }
}

function renderTable(contacts) {
    const tbody = document.getElementById('contactsTableBody');
    if (!tbody) return;

    if (!contacts || contacts.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center; color: var(--text-secondary);">No contact records found. Click "+ Create New Entry" to add one!</td>
            </tr>`;
        return;
    }

    tbody.innerHTML = contacts.map(c => `
        <tr>
            <td>#${c.id}</td>
            <td><strong>${escapeHtml(c.name)}</strong></td>
            <td>${escapeHtml(c.email)}</td>
            <td style="max-width: 250px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">${escapeHtml(c.message)}</td>
            <td>
                <span class="badge ${c.status === 'RESOLVED' ? 'badge-resolved' : 'badge-pending'}">
                    ${c.status}
                </span>
            </td>
            <td style="font-size: 0.8rem; color: var(--text-secondary);">${formatDate(c.createdAt)}</td>
            <td>
                <div class="actions">
                    <button class="action-btn" onclick="openEditModalById(${c.id})">Edit</button>
                    <button class="action-btn" style="color: var(--danger);" onclick="promptDelete(${c.id})">Delete</button>
                </div>
            </td>
        </tr>
    `).join('');
}

function renderPagination(data) {
    currentPage = data.number;
    totalPages = data.totalPages;

    const info = document.getElementById('paginationInfo');
    if (info) {
        info.innerText = `Page ${totalPages === 0 ? 0 : currentPage + 1} of ${totalPages} (${data.totalElements} total entries)`;
    }

    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    if (prevBtn) prevBtn.disabled = data.first;
    if (nextBtn) nextBtn.disabled = data.last;
}

function changePage(delta) {
    const newPage = currentPage + delta;
    if (newPage >= 0 && newPage < totalPages) {
        currentPage = newPage;
        loadContacts();
    }
}

function debounceSearch() {
    clearTimeout(searchDebounceTimer);
    searchDebounceTimer = setTimeout(() => {
        currentPage = 0;
        loadContacts();
    }, 300);
}

function openCreateModal() {
    clearErrors();
    const modalTitle = document.getElementById('modalTitle');
    const contactId = document.getElementById('contactId');
    const contactForm = document.getElementById('contactForm');
    const modal = document.getElementById('contactModal');

    if (modalTitle) modalTitle.innerText = 'Create New Contact';
    if (contactId) contactId.value = '';
    if (contactForm) contactForm.reset();
    if (modal) modal.classList.add('active');
}

function openEditModalById(id) {
    const c = currentContacts.find(item => item.id === id);
    if (!c) return;

    clearErrors();
    const modalTitle = document.getElementById('modalTitle');
    const contactId = document.getElementById('contactId');
    const contactName = document.getElementById('contactName');
    const contactEmail = document.getElementById('contactEmail');
    const contactMessage = document.getElementById('contactMessage');
    const contactStatus = document.getElementById('contactStatus');
    const modal = document.getElementById('contactModal');

    if (modalTitle) modalTitle.innerText = 'Edit Contact #' + c.id;
    if (contactId) contactId.value = c.id;
    if (contactName) contactName.value = c.name;
    if (contactEmail) contactEmail.value = c.email;
    if (contactMessage) contactMessage.value = c.message;
    if (contactStatus) contactStatus.value = c.status;
    if (modal) modal.classList.add('active');
}

function closeModal() {
    const modal = document.getElementById('contactModal');
    if (modal) modal.classList.remove('active');
}

function clearErrors() {
    const errName = document.getElementById('err-name');
    const errEmail = document.getElementById('err-email');
    const errMsg = document.getElementById('err-message');

    if (errName) errName.innerText = '';
    if (errEmail) errEmail.innerText = '';
    if (errMsg) errMsg.innerText = '';
}

async function handleFormSubmit(e) {
    e.preventDefault();
    clearErrors();

    const id = document.getElementById('contactId').value;
    const requestData = {
        name: document.getElementById('contactName').value.trim(),
        email: document.getElementById('contactEmail').value.trim(),
        message: document.getElementById('contactMessage').value.trim(),
        status: document.getElementById('contactStatus').value
    };

    const isEdit = !!id;
    const url = isEdit ? `/contacts/${id}` : '/contacts';
    const method = isEdit ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                ...getAuthHeader(),
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(requestData)
        });

        if (response.status === 400) {
            const errors = await response.json();
            if (errors.name && document.getElementById('err-name')) document.getElementById('err-name').innerText = errors.name;
            if (errors.email && document.getElementById('err-email')) document.getElementById('err-email').innerText = errors.email;
            if (errors.message && document.getElementById('err-message')) document.getElementById('err-message').innerText = errors.message;
            return;
        }

        if (!response.ok) {
            alert('Failed to save contact entry.');
            return;
        }

        closeModal();
        loadContacts();
    } catch (err) {
        console.error('Error saving contact:', err);
        alert('Network error while saving contact.');
    }
}

function promptDelete(id) {
    deleteTargetId = id;
    const modal = document.getElementById('deleteModal');
    if (modal) modal.classList.add('active');
}

function closeDeleteModal() {
    deleteTargetId = null;
    const modal = document.getElementById('deleteModal');
    if (modal) modal.classList.remove('active');
}

async function confirmDelete() {
    if (!deleteTargetId) return;

    try {
        const response = await fetch(`/contacts/${deleteTargetId}`, {
            method: 'DELETE',
            headers: getAuthHeader()
        });

        if (!response.ok) {
            alert('Failed to delete contact entry.');
            return;
        }

        closeDeleteModal();
        loadContacts();
    } catch (err) {
        console.error('Error deleting contact:', err);
        alert('Network error while deleting contact.');
    }
}

function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
}

function formatDate(isoStr) {
    if (!isoStr) return '';
    const date = new Date(isoStr);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}
