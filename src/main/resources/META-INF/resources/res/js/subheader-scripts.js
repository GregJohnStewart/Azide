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
    var iframe = document.getElementById("appframe");
    iframe.src = "/app/viewer/no-app";
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

const hoverImages = document.getElementsByClassName('AppImage'); 
const hoverText = document.getElementsByClassName('hover-text'); 
let hoverTimeout; 

for (let i = 0; i < hoverImages.length; i++){
    hoverImages[i].addEventListener('mousemove', (event) => { 
        var parentOffset = $(this).offsetParent();
        var relX = event.pageX - parentOffset.left;
        var relY = event.pageY - parentOffset.top;
        hoverText[i].style.left = (relX) + 'px'; 
        hoverText[i].style.top = (relY) + 'px'; 
    }); 


    hoverImages[i].addEventListener('mouseenter', () => { 
        hoverTimeout = setTimeout(() => { 
            hoverText[i].style.display = 'block';
        }, 750); // 750 milliseconds delay 
    }); 

    hoverImages[i].addEventListener('mouseleave', () => { 
        clearTimeout(hoverTimeout); 
        hoverText[i].style.display = 'none'; 
    });
}