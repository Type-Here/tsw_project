/* ================== ADMIN RETRIEVE PROD CATALOG ============================ */

function xmlConsoleReq(message){
    let xhr = new XMLHttpRequest();
    let base = document.URL.match("(http[s]?://.*?/.*?/)")[0];
    let url = base + "console";

    if(isFirstRequest) xmlRequestPageNumber();

    xhr.onreadystatechange = function(){
        if(xhr.readyState === 4 && xhr.status === 200){
            doPrintTable(xhr.response);
        }
    }

    xhr.open("POST", url , true);
    xhr.onerror = function (e){console.log(e)};
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
    xhr.send(message);
}


function xmlRequestPageNumber(){
    if(isFirstRequest){
        let xhr = new XMLHttpRequest();
        let base = document.URL.match("(http[s]?://.*?/.*?/)")[0];
        let url = base + "console";
        let message = "action=prodManager&ask=accessProd&requestPages=true";
        xhr.onreadystatechange = function(){
            if(xhr.readyState === 4 && xhr.status === 200){
                isFirstRequest = false;
                console.log(xhr.response)
                maxPage = xhr.response;
                console.log("MaxPage:" + maxPage);
            }
        }

        xhr.open("POST", url , true);
        xhr.onerror = function (e){console.log(e)};
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        xhr.send(message);
    }
}



function printRow(tableRow, prod){
    const printable = ["id_prod","name","price","platform","developer","metadataPath","type","discount"];
    Object.keys(prod).forEach( key =>{
        let cellText = prod[key];
        if(printable.includes(key)){
            if(key === 'type'){
                if(prod[key]){
                    cellText ="Digitale";
                } else {
                    cellText="Fisico";
                }
            }
            // Insert a cell at the end of the row
            let newCell = tableRow.insertCell();
            // Append a text node to the cell
            let newText = document.createTextNode(cellText);
            newCell.appendChild(newText);
        }
    });

    let button1cell = tableRow.insertCell();
    button1cell.setAttribute('class', 'table-row-button');
    button1cell.innerHTML = "<button title=\"modifica "+ prod["id_prod"] +" \" class=\"secondary\" value=\""+ prod["id_prod"] +"\">Modifica</button>";
    addPopup(button1cell);

    let button2cell = tableRow.insertCell();
    button2cell.setAttribute('class', 'table-row-button');
    button2cell.innerHTML = "<button title=\"remove "+ prod["id_prod"] +"\" class=\"secondary attention\" value=\""+ prod["id_prod"] +"\">&Cross;</button>";
}

/**
 * Print a List of Product in a table#admin-prod-table 1 for each row
 * @param response an AJAX response to parse JSON from
 */
function doPrintTable(response){

    let products = JSON.parse(response);
    const table = document.getElementById('admin-prod-table');
    const tbody = table.tBodies[0];
    console.log("Table:" + tbody.children.length);
    //Reset Table except first row (headers)
    let rows = tbody.children.length; //Save original value in a variable otherwise for cycle will remove half of the data!
    for(let i = 1; i < rows; i++){
        tbody.children[1].remove(); //Use fixed 1 to remove all data (0 = header) otherwise Error!
    }

    //For Each Product in product List (from JSON) print a row
    for(let i = 0; i < products.length; i++) {
        // Insert a row at the end of table
        let newRow = table.insertRow();
        printRow(newRow, products[i]);
    }
}


/* ============================ LISTENERS ===================================== */

    /*VARIABLES*/
let nextPage = 2;
let maxPage = 1;
let isFirstRequest = true;

/* ---- Load Button Click Listener ---- */
document.getElementById('load-cat-button').addEventListener('click', function(e){
    let message = "action=prodManager&ask=accessProd&page=1";
    xmlConsoleReq(message);
    this.classList.add('general-display-none');
    document.getElementById("prev-cat-button").disabled = true;
    document.getElementById("prev-cat-button").classList.remove('general-display-none');
    document.getElementById("next-cat-button").classList.remove('general-display-none');

});

/* ---- Search Prod Bar Listener ---- */
/* Simplified: 'THIS' variable works only with anon function not lambda */
document.forms.namedItem('prod-search').addEventListener('submit', function (e){
    e.preventDefault(); //Abort Submit and use ajax instead (to reuse code from search bar /*TODO*/
    console.log("SEARCH:" + this.tagName);
    let input = this.elements[0].value;
    let message = "action=prodManager&ask=searchByName&search=" + input;
    xmlConsoleReq(message);
    document.getElementById("prev-cat-button").classList.add('class','general-display-none');
    document.getElementById("next-cat-button").classList.add('general-display-none');
    document.getElementById('load-cat-button').classList.remove('general-display-none');
});


document.getElementById("next-cat-button").addEventListener('click', function (){
    let message = "action=prodManager&ask=accessProd&page=" + nextPage++;
    xmlConsoleReq(message);
    document.getElementById("prev-cat-button").disabled = false;
    if(nextPage > maxPage){
        this.disabled = true;
    }
});

document.getElementById("prev-cat-button").addEventListener('click', function (){
    let message = "action=prodManager&ask=accessProd&page=" + (nextPage-- - 2);
    xmlConsoleReq(message);
    if(nextPage === 2) this.disabled = true;
    if(nextPage < maxPage) document.getElementById("next-cat-button").disabled = false;
});



/* ============= POPUP MOD AND DELETE BUTTONS ===================== */

/* Listener for Modify Button */
function addPopup(button){
    button.addEventListener('click', function (){
        let popupDiv = document.getElementById('modify-prod-popup');
        let form = document.getElementById('add_prod').cloneNode(true);
        form.id = "modify-form";


        let prodId = button.value;

        popupDiv.classList.remove('general-display-none');
        console.log("Win:" + document.documentElement.clientWidth + "-WID:" + getComputedStyle(popupDiv).top);
        popupDiv.style.left = (window.innerWidth - getComputedStyle(popupDiv).width)/2 + "px";

        popupDiv.appendChild(form);

        /* Overlay */
        document.getElementsByClassName('overlay')[0].classList.add('general-display-block');
        /* Overlay Click removes popup and overlay */
        document.getElementsByClassName('overlay')[0].addEventListener('click', () =>{
            document.getElementsByClassName('overlay')[0].classList.remove( 'general-display-block');
            popupDiv.classList.add('general-display-none');
            popupDiv.removeChild(form);
        });


        let okButton = document.createElement('button');
        okButton.classList.add('default');
        okButton.innerHTML = 'Modifica';


        let cancelButton = document.createElement('button');
        cancelButton.setAttribute('class','default alternative');
        cancelButton.innerHTML = 'Annulla';

    });
}