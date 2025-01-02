            

const apiBaseUrl = "/api/app/messages";
const form = document.getElementById('message-form');
const tableBody = document.querySelector('#message-table tbody');
const clearFormButton = document.getElementById('clear-form');
const deleteMessageButton = document.getElementById('delete-message');
let editingRow = null;

// Fetch all messages and populate the table
async function fetchMessages() {
    const response = await fetch(apiBaseUrl);
    const messages = await response.json();
    /**tableBody.innerHTML = "";
    messages.forEach(message => {
        const row = createTableRow(message);
        tableBody.appendChild(row);
    });*/
}

// Create a table row
function createTableRow(message) {
    const row = document.createElement('tr');
    row.dataset.id = message.id;
    row.dataset.message = JSON.stringify(message);
    const rowString = '<td>'+message.title+'createTableRow'+'</td><td>'+message.priority+'</td><td>'+message.date+'</td>';
    row.innerHTML = rowString;
    return row;
}

// Save or update a message
form.addEventListener('submit', async (event) => {
    event.preventDefault();

    const message = {
        title: document.getElementById('message-title').value,
        priority: document.getElementById('message-priority').value,
        date: document.getElementById('message-date').value,
        content: document.getElementById('message-content').value,
    };

    if (editingRow) {
        const id = editingRow.dataset.id;
        const fetchUrl = apiBaseUrl+"/"+id;
        await fetch(fetchUrl, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(message),
        });
    } else {
        await fetch(apiBaseUrl, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(message),
        });
    }
    location.reload();    
    //fetchMessages();
    form.reset();
    editingRow = null;
});

// Delete a message
deleteMessageButton.addEventListener('click', async () => {
    if (editingRow) {
        const id = editingRow.id;
        await fetch(apiBaseUrl+"/"+id, { method: "DELETE" });
        location.reload();   
        //fetchMessages();
        form.reset();
        editingRow = null;
    } else {
        alert('No message selected to delete.');
    }
});

// Clear form
clearFormButton.addEventListener('click', () => {
    form.reset();
    editingRow = null;
});

// Populate form on row click
tableBody.addEventListener('click', (event) => {
    const row = event.target.closest('tr');
    if (row) {
        document.getElementById('message-title').value = row.cells[0].innerHTML;
        document.getElementById('message-priority').value = row.cells[1].innerHTML;
        document.getElementById('message-date').value = row.cells[2].innerHTML;
        document.getElementById('message-content').value = row.getAttribute('message-content');;
        editingRow = row;
    }
});

// Initialize
//fetchMessages();