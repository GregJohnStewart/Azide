const apiBaseUrl = "/api/app/messages";
const tableBody = document.querySelector('#message-table tbody');
const contentDisplay = document.getElementById('content-display');

// Fetch all messages and populate the table
async function fetchMessages() {
    const response = await fetch(apiBaseUrl);
    const messages = await response.json();
    tableBody.innerHTML = "";
    messages.forEach(message => {
        const row = createTableRow(message);
        tableBody.appendChild(row);
    });
}

// Create a table row
function createTableRow(message) {
    const row = document.createElement('tr');
    row.dataset.id = message.id;
    row.dataset.message = JSON.stringify(message);
    const rowString = '<td>'+message.title+'</td><td>'+message.priority+'</td><td>'+message.date+'</td>';
    row.innerHTML = rowString;
    return row;
}

// Display message content on row click
tableBody.addEventListener('click', (event) => {
    const row = event.target.closest('tr');
    if (row) {
        const message = JSON.parse(row.dataset.message);
        contentDisplay.textContent = message.content;
    }
});

// Initialize
fetchMessages();
