const apiBaseUrl = "/api/app/messages";
const tableBody = document.querySelector('#message-table tbody');
//const contentDisplay = document.getElementById('content-display');
const contentDisplay = document.getElementById('message-content');


// Display message content on row click
tableBody.addEventListener('click', (event) => {
    const row = event.target.closest('tr');
    if (row) {
        const message = JSON.parse(row.dataset.message);
        contentDisplay.textContent = message.content;
    }
});

function editMessages(application) {
    // load the message editor page
//    window.location.href = appId;
    var outer = window.parent;
    var iframe = outer.document.getElementById("appframe");
//    var html = "";
    iframe.src = application;
//    iframe.contentWindow.document.open();
//    iframe.contentWindow.document.write(html);
//    iframe.contentWindow.document.close();
}

