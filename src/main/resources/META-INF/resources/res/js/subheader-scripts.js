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
    });; 

}
