/* ================== ADMIN RETRIEVE PROD CATALOG ============================ */
/**
 * Send message in a POST Request to Server
 * @param message Message Request in Body
 * @param option 1: PrintTable (default), 2: Print Prod Form
 */
function xmlConsoleReq(message, option = 1){
    let xhr = new XMLHttpRequest();
    let base = document.URL.match("(http[s]?://.*?/.*?/)")[0];
    let url = base + "console";

    if(isFirstRequest) xmlRequestPageNumber();

    xhr.onreadystatechange = function(){
        if(xhr.readyState === 4 && xhr.status === 200){
            if(option === 1) {
                doPrintTable(xhr.response);
            } else if(option === 2){
                doPrintProdForm(xhr.response);
            }
        }
    }

    xhr.open("POST", url , true);
    xhr.onerror = function (e){console.log(e)};
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded"); //Needed for POST req
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest"); //Header for Inform Server of an XMLHttpRequest
    xhr.send(message);
}

/**
 * AJAX Request to retrieve the number of product pages available in Product Management Section
 */
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


/**
 * Print a Row inside a Table
 * @param tableRow Pointer to the HTMLTableRowElement to fill
 * @param prod Product object to retrieve data from
 */
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
    //button1cell.innerHTML = "<button title=\"modifica "+ prod["id_prod"] +" \" class=\"secondary\" value=\""+ prod["id_prod"] +"\">Modifica</button>";

    let button1 = document.createElement('button');
    button1.title = "Modifica Prod " + prod["id_prod"];
    button1.classList.add('secondary');
    button1.value = prod["id_prod"];
    button1.innerHTML ="Modifica";
    button1cell.appendChild(button1);
    addPopup(button1);

    let button2cell = tableRow.insertCell();
    button2cell.setAttribute('class', 'table-row-button');

    let button2 = document.createElement('button');
    button2.title = "Elimina Prod " + prod["id_prod"];
    button2.setAttribute('class','secondary attention');
    button2.value = prod["id_prod"];
    button2.innerHTML ="&Cross;";
    button2cell.appendChild(button2);
    //addPopupRemove(button2);
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
/**
 * Add a Listener to Modify Button: On Click Show Popup to Modify the selected Product
 * @param button button to which add the listener
 */
function addPopup(button){
    button.addEventListener('click', function (){
        let popupDiv = document.getElementById('modify-prod-popup');
        let formSection = document.getElementById('add_prod').cloneNode(true);
        formSection.id = "modify-form";

        let prodId = this.value; //Button Value

        let message = "action=prodManager&ask=modifyProd&id=" + prodId;
        xmlConsoleReq(message, 2);

        popupDiv.classList.remove('general-display-none');
        popupDiv.style.left = (window.innerWidth - getComputedStyle(popupDiv).width)/2 + "px";

        //REMOVE INPUT FILE FOR IMAGES IN POPUP
        formSection.querySelector('label:has(input[type=file])').remove();
        formSection.querySelector('label:has(input[type=file])').remove();

        //formSection.getElementsByTagName('form')[0].removeChild(document.querySelector('label:has(input[type=file])'));

        popupDiv.appendChild(formSection);

        /* Overlay */
        document.getElementsByClassName('overlay')[0].classList.add('general-display-block');
        /* Overlay Click removes popup and overlay */
        document.getElementsByClassName('overlay')[0].addEventListener('click', () =>{
            document.getElementsByClassName('overlay')[0].classList.remove( 'general-display-block');
            popupDiv.classList.add('general-display-none');
            popupDiv.removeChild(formSection);
        });


        let okButton = document.createElement('button');
        okButton.classList.add('default');
        okButton.innerHTML = 'Modifica';


        let cancelButton = document.createElement('button');
        cancelButton.setAttribute('class','default alternative');
        cancelButton.innerHTML = 'Annulla';

    });
}

/**
 * Add all elements of a product modifiable inside the popup form
 * @param response an AJAX Response to retrieve data from (JSON format)
 */
function doPrintProdForm(response){
    const formSection = document.getElementById('modify-form');
    if(!formSection){
        let popupDiv = document.getElementById('modify-prod-popup');
        popupDiv.innerHTML = 'Unable to Set Form';
        return;
    }

    let prod = JSON.parse(response);
    let form = formSection.getElementsByTagName('form')[0];

    console.log(prod);
    const conditionsValues=['X','A','B','C','D','E'];
    Object.keys(prod).forEach((key, index, array)=>{
        let formElement = form.querySelectorAll('[name="' + key + '"');

        switch(key) {
            case "type":
                form.getElementsByClassName('type-prod')[0].checked = (prod['type'] === false);
                form.getElementsByClassName('type-prod')[1].checked = (prod['type'] === true);
                break;
            case "categoryBeanList":
                const cat = prod[key];
                //each category is parsed as an object and its value are retrievable with its key (name of variable)
                cat.forEach(catElem => {
                    const checkbox = form.querySelector('[name="category"][value="' + catElem['id_cat'] + '"]');
                    checkbox.checked = true;
                });
                break;
            case "conditions":
                // Array of obj: 0: Object { id_prod: 0, id_cond: 1, quantity: 1 }
                prod[key].forEach((condition) => {
                    Array.from(form.getElementsByClassName('condition-input')).forEach(row => {
                        let input = row.querySelector('[name="condition"][value="' + conditionsValues[condition["id_cond"]] + '"]');
                        if (input) {
                            input.checked = true;
                            if (condition["quantity"]) row.querySelector('[name="quantity"]').value = condition["quantity"];
                        }
                    })

                });
                break;
            default:
                if (formElement[0]) form.querySelector('[name="' + key + '"]').value = prod[key];
        }

    });
}