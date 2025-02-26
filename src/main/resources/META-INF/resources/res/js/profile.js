function addFavorite(profileId, name) {
    fetch('/api/profile/favorite/' + profileId, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: '{"name":"' + name + '"}'
    })
    .then(response => {
        console.log(profileId);
        console.log(name);

        if (!response.ok) {
            throw new Error('Error adding favorite app: ' + response.statusText);
        }
        return response.json();
    })
    .then(responseData => {
        showNotification('Favorite app: ' + name + ' added successfully', 'success');
    })
    .catch(error => {
        console.error('Error adding favorite app:', error);
        showNotification('Error adding favorite app: ' + error, 'danger');
    });
}

function deleteFavorite(profileId, name) {
    fetch('/api/profile/favorite/' + profileId, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: '{"name":"' + name + '"}'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error deleting favorite app: ' + response.statusText);
        }
        return response.json();
    })
    .then(responseData => {
        showNotification('Favorite app: ' + name + ' deleted successfully', 'success');
    })
    .catch(error => {
        console.error('Error deleting favorite app:', error);
        showNotification('Error deleting favorite app: ' + error, 'danger');
    });
}

// ====================================================
// Functions etc for profile viewer/editor
// ====================================================
var userProfileId;
var selectedProfile;

function stripQuotes(str) {
    if (str.startsWith('"') || str.startsWith("'")) {
        str = str.slice(1);
    }
    if (str.endsWith('"') || str.endsWith("'")) {
        str = str.slice(0, -1);
    }
    return str;
}

function showDetails(profileId, selectedProfileId) {
    userProfileId = profileId;

    const jsonprofile = document.getElementById('profile-row-' + selectedProfileId).dataset.profileData;
    selectedProfile = JSON.parse(stripQuotes(jsonprofile));

//    console.log(jsonprofile);

    document.getElementById('username').innerText = selectedProfile.username;
    document.getElementById('lastLogin').innerText = selectedProfile.lastLogin;
}

function showNotification(message, type) {
    // 'type' should be one of the following: 
    // 'primary', 'secondary', 'success', 'danger', 'warning', 'info', 'light', 'dark'
    var alertHtml = '<div class="alert alert-' + type + ' alert-dismissible fade show" role="alert">' +
                    message +
                    '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
                    '</div>';
    var $alert = $(alertHtml);
    $('#notification-container').append($alert);

    // Auto hide the notification after 5 seconds
    setTimeout(function() {
        // Use Bootstrap's alert 'close' method
        $alert.alert('close');
    }, 5000);
}

function refreshTable() {
    fetch('/api/profile/profile-table')
    .then(response => {
        if (!response.ok) {
            throw new Error('Error refreshing profiles: ' + response.statusText);
        }
        return response.text();
    })
    .then(htmlFragment => {
        document.querySelector('#profile-table table tbody').innerHTML = htmlFragment;
    })
    .catch(error => {
        showNotification('Error refreshing profiles: ' + error, 'danger');
    });
}

// Instead of a native confirm, show a custom delete confirmation modal.
function deleteProfile() {
    let editModal = bootstrap.Modal.getInstance(document.getElementById('editModal'));
    if (editModal) editModal.hide();

    // Check to make sure the user is not deleting their own profile
    if(userProfileId == selectedProfile.id) {
        showNotification('Deleting your own profile is not allowed.', 'danger');
        return;
    }

    $('#confirmDeleteProfileUsername').text(selectedProfile.username);

    // Show the confirmation modal
    new bootstrap.Modal(document.getElementById('confirmDeleteModal')).show();
}

// Called when user confirms deletion in the custom confirmation modal.
function confirmDelete() {
    fetch('/api/profile/' + selectedProfile.id, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error deleting profile: ' + response.statusText);
        }
        return response.text();
    })
    .then(responseData => {
        refreshTable();
        let editModal = bootstrap.Modal.getInstance(document.getElementById('editModal'));
        if (editModal) editModal.hide();
        let confirmModal = bootstrap.Modal.getInstance(document.getElementById('confirmDeleteModal'));
        if (confirmModal) confirmModal.hide();
        showNotification('Profile deleted successfully', 'success');
    })
    .catch(error => {
        console.error('Error deleting profile:', error);
        showNotification('Error deleting profile: ' + error, 'danger');
    });
}
