const apiBaseUrl = "/api/app/messages";
const tableBody = document.querySelector('#message-table tbody');
const contentDisplay = document.getElementById('message-content');


// Display message content on row click.
tableBody.addEventListener('click', (event) => {
    const row = event.target.closest('tr');
    if (row) {
        document.getElementById('message-content').value = row.getAttribute('message-content');
        editingRow = row;
    }
});


// Open the message editor in the iframe.
function editMessages(applocation) {
    var outer = window.parent;
    var iframe = outer.document.getElementById("appframe");
    iframe.src = applocation;
}
