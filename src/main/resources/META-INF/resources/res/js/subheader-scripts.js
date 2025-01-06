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

    iframe.contentWindow.document.open();
    iframe.contentWindow.document.write(html);
    iframe.contentWindow.document.close();
}

function filterApps() { 
    let input = document.getElementById('filterInput').value.toLowerCase(); 
    let divs = document.querySelectorAll('div.clickable-app'); 
    console.log("filterApps");
    console.log(divs.length);
    divs.forEach(function(div) { 
        console.log(div.id);
        if (div.id.toLowerCase().includes(input)) { 
            div.style.display = 'inline-block';
        } else { 
            div.style.display = 'none'; 
        } 
    });
}

function refreshCSS() { 
    // Select all stylesheets 
    let stylesheets = document.querySelectorAll('link[rel="stylesheet"]'); 
    // Iterate over the stylesheets and update the href attribute to force a reload 
    stylesheets.forEach(function(stylesheet) { 
        let href = stylesheet.getAttribute('href'); 
        stylesheet.setAttribute('href', href + '?v=' + new Date().getTime()); 
    }); 
}