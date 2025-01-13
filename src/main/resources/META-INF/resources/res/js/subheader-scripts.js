var selectedAppId="";
var selectedAppName="";

function toggleAppBar() { 
    var div = document.getElementById("appselector"); 
    if (div.style.display === "none") { 
        div.style.display = "block"; 
    } else { 
        div.style.display = "none"; 
    } 
}

function loadApp(appId) {
    toggleAppBar();
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
    divs.forEach(function(indiv) { 
        if (indiv.id.toLowerCase().includes(input)) { 
            indiv.style.display = 'inline-block';
        } else { 
            indiv.style.display = 'none'; 
        } 
    });
}

function appSelected(appName, appId) {
    if(localStorage.getItem('dontAskAgain') && localStorage.getItem('dontAskAgain') !== null) {
        loadApp(appId);
    } else { 
        showPopup(appName, appId);
    }
}

function showPopup(appName, appId) { 
    selectedAppId = appId;
    document.getElementById('popupMessage').innerText = appName + " will replace your current application. Would you like to continue?";
    document.getElementById('popup').style.display = 'block'; 
} 

function hidePopup() { 
    document.getElementById('popup').style.display = 'none'; 
} 

function handleOk() { 
    var dontAskAgain = document.getElementById('dontAskAgain').checked; 
    if (dontAskAgain) { 
        localStorage.setItem('dontAskAgain', 'true'); 
    } 
    loadApp(selectedAppId);
    hidePopup();  
} 

function handleCancel() { 
    hidePopup(); 
} 
