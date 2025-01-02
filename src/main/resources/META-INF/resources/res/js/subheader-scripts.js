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
