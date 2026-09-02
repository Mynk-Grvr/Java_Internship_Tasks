// Task 5: Contact Management System Application Logic

let currentPage = 0;
const pageSize = 5;
let totalPages = 1;
let deleteTargetId = null;
let searchDebounceTimer = null;

document.addEventListener('DOMContentLoaded', () => {
    loadContacts();
});

function getAuthHeader() {
    const user = document.getElementById('authUsername').value.trim();
    const pass = document.getElementById('authPassword').value.trim();
    if (!user || !pass) return {};
    const creds = btoa(`${user}:${pass}`);
    return { 'Authorization': `Basic ${creds}` };
}

async function loadContacts() {
    const search = document.getElementById('searchInput').value.trim();
    const sort = document.getElementById('sortSelect').value;

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

        if (response.status === 401) {
            alert('Authentication Failed: Please enter valid Admin credentials (admin / admin123).');
            return;
        }

        if (!response.ok) {
            throw new Error(`HTTP Error: ${response.status}`);
        }

        const data = await response.json();
        renderTable(data.content);
        renderPagination(data);
    } catch (err) {
        console.error('Error fetching contacts:', err);
        document.getElementById('contactsTableBody').innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center; color: var(--danger);">
                    Failed to load contacts. Please verify backend server is running and credentials are valid.
                </td>
            </tr>`;
    }
}

function renderTable(contacts) {
    const tbody = document.getElementById('contactsTableBody');
    if (!contacts || contacts.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center; color: var(--text-secondary);">No contact records found.</td>
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
                    <button class="action-btn" onclick="openEditModal(${c.id}, '${escapeQuote(c.name)}', '${escapeQuote(c.email)}', '${escapeQuote(c.message)}', '${c.status}')">Edit</button>
                    <button class="action-btn" style="color: var(--danger);" onclick="promptDelete(${c.id})">Delete</button>
                </div>
            </td>
        </tr>
    `).join('');
}

function renderPagination(data) {
    currentPage = data.number;
    totalPages = data.totalPages;

    document.getElementById('paginationInfo').innerText = 
        `Page ${totalPages === 0 ? 0 : currentPage + 1} of ${totalPages} (${data.totalElements} total entries)`;

    document.getElementById('prevBtn').disabled = data.first;
    document.getElementById('nextBtn').disabled = data.last;
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
    document.getElementById('modalTitle').innerText = 'Create New Contact';
    document.getElementById('contactId').value = '';
    document.getElementById('contactForm').reset();
    document.getElementById('contactModal').classList.add('active');
}

function openEditModal(id, name, email, message, status) {
    clearErrors();
    document.getElementById('modalTitle').innerText = 'Edit Contact #' + id;
    document.getElementById('contactId').value = id;
    document.getElementById('contactName').value = name;
    document.getElementById('contactEmail').value = email;
    document.getElementById('contactMessage').value = message;
    document.getElementById('contactStatus').value = status;
    document.getElementById('contactModal').classList.add('active');
}

function closeModal() {
    document.getElementById('contactModal').classList.remove('active');
}

function clearErrors() {
    document.getElementById('err-name').innerText = '';
    document.getElementById('err-email').innerText = '';
    document.getElementById('err-message').innerText = '';
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
            // Handle validation error map from GlobalExceptionHandler
            const errors = await response.json();
            if (errors.name) document.getElementById('err-name').innerText = errors.name;
            if (errors.email) document.getElementById('err-email').innerText = errors.email;
            if (errors.message) document.getElementById('err-message').innerText = errors.message;
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
    document.getElementById('deleteModal').classList.add('active');
}

function closeDeleteModal() {
    deleteTargetId = null;
    document.getElementById('deleteModal').classList.remove('active');
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

function escapeQuote(str) {
    if (!str) return '';
    return str.replace(/'/g, "\\'").replace(/"/g, '&quot;');
}

function formatDate(isoStr) {
    if (!isoStr) return '';
    const date = new Date(isoStr);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}
