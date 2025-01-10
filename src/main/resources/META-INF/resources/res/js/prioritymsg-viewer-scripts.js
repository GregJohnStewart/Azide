const apiBaseUrl = "/api/app/messages";
const tableBody = document.querySelector('#message-table tbody');
//const contentDisplay = document.getElementById('content-display');
const contentDisplay = document.getElementById('message-content');

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

function editMessages(applocation) {
    // load the message editor page
//    window.location.href = appId;
    var outer = window.parent;
    var iframe = outer.document.getElementById("appframe");
//    var html = "";
    iframe.src = applocation;
//    iframe.contentWindow.document.open();
//    iframe.contentWindow.document.write(html);
//    iframe.contentWindow.document.close();
}

// Initialize
fetchMessages();