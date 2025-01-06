function toggleAppBar() { 
    var div = document.getElementById("appselector"); 
    if (div.style.display === "none") { 
        div.style.display = "block"; 
    } else { 
        div.style.display = "none"; 
    } 
}

function loadApp(appId) {
    window.location.href = appId;
}

function closeApp() {
    //document.getElementById("appframe").src = "";
    // or
    var iframe = document.getElementById("appframe");
    var html = "";
    iframe.src = "";
    iframe.contentWindow.document.open();
    iframe.contentWindow.document.write(html);
    iframe.contentWindow.document.close();
}

function filterApps() { 
    let input = document.getElementById('filterInput').value.toLowerCase(); 
    let divs = document.querySelectorAll('div.clickable-app');
    divs.forEach(function(div) { 
        if (div.id.toLowerCase().includes(input)) { 
            div.style.display = 'inline-block';
        } else { 
            div.style.display = 'none'; 
        } 
    });
}
