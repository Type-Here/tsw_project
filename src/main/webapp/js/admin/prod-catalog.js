import {addPopup} from "./admin-functions.js";
import {removePopup} from "./admin-functions.js";
import {doPrintProdForm} from "./admin-functions.js";

/* ================== ADMIN RETRIEVE PROD CATALOG ============================ */
/**
 * Send message in a POST Request to Server
 * @param message Message Request in Body
 * @param option <pre>
 *               1: PrintTable (default),
 *               2: Print Prod Form,
 *               3: Do Operation (print response in divToSet if available) //Used in ModifyProd
 *               4: Do Operation (print response in divToSet if available) and reload table od Products //Used in DeleteProd
 *               </pre>
 * @param divToSet Set innerHTML
 */
export function xmlConsoleReq(message, option = 1, divToSet = null){
    let xhr = new XMLHttpRequest();
    let base = document.URL.match("(http[s]?://.*?/.*?/)")[0];
    let url = base + "console";

    if(isFirstRequest) xmlRequestPageNumber();

    xhr.onreadystatechange = function(){
        if(xhr.readyState === 4 && xhr.status === 200){
            switch (option){
                case 1:
                    const table = document.getElementById('admin-prod-table');
                    const json = JSON.parse(xhr.response);
                    doPrintTable(json, table, 1);
                    break;
                case 2:
                    doPrintProdForm(xhr.response);
                    break;
                case 3:
                    if(divToSet) divToSet.innerHTML = "Operazione Conclusa: " + xhr.responseText;
                    break;
                case 4:
                    if(divToSet) divToSet.innerHTML = "Operazione Conclusa: " + xhr.responseText;
                    document.getElementById('load-cat-button').click(); //Reload Table
                    break;
            }
        } else if(xhr.readyState === 4){
            alert("Unable to perform operation - Error code:" + xhr.status);
        }
    }

    xhr.open("POST", url , true);
    xhr.onerror = function (e){console.log(e);  alert("Network error occurred. Please try again.");};
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
                maxPage = xhr.response;
            } else if(xhr.readyState === 4){
                alert("Unable to retrieve data - Error code:" + xhr.status);
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
 * @param type Integer specifying which Table has to Be Set: 1=Products, 2=Orders, 3=Users
 */
function printRow(tableRow, prod, type){
    switch (type){
        //Products Table
        case 1:
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

            //Modifica
            let button1cell = tableRow.insertCell();
            button1cell.setAttribute('class', 'table-row-button');

            let button1 = document.createElement('button');
            button1.title = "Modifica Prod " + prod["id_prod"];
            button1.classList.add('secondary');
            button1.value = prod["id_prod"];
            button1.innerHTML ="Modifica";
            button1cell.appendChild(button1);
            addPopup(button1);

            //Elimina
            let button2cell = tableRow.insertCell();
            button2cell.setAttribute('class', 'table-row-button');

            let button2 = document.createElement('button');
            button2.title = "Elimina Prod " + prod["id_prod"];
            button2.setAttribute('data-name', prod["name"]);
            button2.setAttribute('class','secondary attention');
            button2.value = prod["id_prod"];
            button2.innerHTML ="&Cross;";
            button2.onclick = () => removePopup(button2);
            button2cell.appendChild(button2);
            //addPopupRemove(button2);
            break;

        //Order Table
        case 2:
            Object.keys(prod).forEach( key =>{
                let cellText = prod[key];
                // Insert a cell at the end of the row
                let newCell = tableRow.insertCell();
                // Append a text node to the cell
                let newText = document.createTextNode(cellText);
                newCell.appendChild(newText);
            });
            break;

        //User Table
        case 3:
            const addressComponents = ["road", "address", "CAP", "prov", "city"];
            let address = '';
            let counter = 0;
            Object.keys(prod).forEach( key =>{
                let cellText = prod[key];
                //if(key === 'birth') return; //return is equivalent of continue in this forEach method
                if(addressComponents.includes(key)) {
                    address += prod[key] + " ";
                    if(++counter === 4) cellText = address;
                    else return; //return is equivalent of continue in this forEach method
                }

                // Insert a cell at the end of the row
                let newCell = tableRow.insertCell();
                // Append a text node to the cell
                let newText = document.createTextNode(cellText);
                newCell.appendChild(newText);
            });
            break;

        default:
            console.error("No type defined in printTable Call");
            break
    }

}

/**
 * Remove Old Elements first.
 * Print a List of Product in a table 1 for each row
 * @param responseJson an AJAX response parsed in JSON
 * @param table HTMLTableElement to Fill
 * @param type Integer specifying which Table has to Be Set: 1=Products, 2=Orders, 3=Users
 */
export function doPrintTable(responseJson, table, type){

    let products = responseJson;
    const tbody = table.tBodies[0];

    //Reset Table except first row (headers)
    let rows = tbody.children.length; //Save original value in a variable otherwise for cycle will remove half of the data!
    for(let i = 1; i < rows; i++){
        tbody.children[1].remove(); //Use fixed 1 to remove all data (0 = header) otherwise Error!
    }

    //For Each Product in response List (from JSON) print a row
    for(let i = 0; i < products.length; i++) {
        // Insert a row at the end of table
        let newRow = table.insertRow();
        printRow(newRow, products[i], type);
    }
}


/* ============================ LISTENERS ===================================== */

 /*  -- GLOBAL VARIABLES -- */
let nextPage = 2;
let maxPage = 1;
let isFirstRequest = true;

/* ---- Load Button Click Listener ---- */
/**
 * Load Product List Click Event Listener: Load First Page Product Table on Click
 */
document.getElementById('load-cat-button').addEventListener('click', function(e){
    let message = "action=prodManager&ask=accessProd&page=1";
    xmlConsoleReq(message);
    this.classList.add('general-display-none');
    document.getElementById("prev-cat-button").disabled = true;
    document.getElementById("prev-cat-button").classList.remove('general-display-none');
    document.getElementById("next-cat-button").classList.remove('general-display-none');

});

/* ---- Search Prod Bar Listener ---- */

/* GENERAL INFO: Simplified: 'THIS' variable works only with anon function not lambda */
/**
 * Search Bar Event Listener to Submit: use AJAX to retrieve info
 */
document.forms.namedItem('prod-search').addEventListener('submit', function (e){
    e.preventDefault(); //Abort Submit and use ajax instead (to reuse code from search bar /*TODO*/

    let input = this.elements[0].value;
    let message = "action=prodManager&ask=searchByName&search=" + input;
    xmlConsoleReq(message);
    document.getElementById("prev-cat-button").classList.add('class','general-display-none');
    document.getElementById("next-cat-button").classList.add('general-display-none');
    document.getElementById('load-cat-button').classList.remove('general-display-none');
});

/**
 * Next Product Page Button Listener
 */
document.getElementById("next-cat-button").addEventListener('click', function (){
    let message = "action=prodManager&ask=accessProd&page=" + nextPage++;
    xmlConsoleReq(message);
    document.getElementById("prev-cat-button").disabled = false;
    if(nextPage > maxPage){
        this.disabled = true;
    }
});

/**
 * Previous Product Page Button Listener
 */
document.getElementById("prev-cat-button").addEventListener('click', function (){
    let message = "action=prodManager&ask=accessProd&page=" + (nextPage-- - 2);
    xmlConsoleReq(message);
    if(nextPage === 2) this.disabled = true;
    if(nextPage < maxPage) document.getElementById("next-cat-button").disabled = false;
});



/* ================= VALIDATE AND SEND XML REQUEST FOR MODIFICATION PRODUCT ======== */

/**
 * Action for Modification Popup. Prevent Submit.
 * Validate only cross input fields data (single field data is in html pattern)
 * Send AJAX request
 * @param event the submit event to prevent
 * @param form form to retrieve data from
 * @param divToSetAnswer is the HTMLElement where te XML Response (Answer or Status) will be written into (innerHTML)
 */
function doModifyAction(event, form, divToSetAnswer){
    //Stop Submit
    event.preventDefault();

    /*Get Conditions INPUT */
    const conditions = form.getElementsByClassName('condition-input');
    /*Get Categories INPUT */
    const categories = form.getElementsByClassName('category-input');

    /* Check for:
     * Checked at least 1 Category
     * Checked at least 1 Condition + Set its Quantity
     */
    /* If Conditions and Categories are not correctly set, invalidate submit and alert user */
    /*TODO return validation check in function doesn't work keep it here ftm*/
    if(!(Array.from(categories).find( cat =>{ return cat.checked === true }) !== undefined //Validate Categories
        &&
        Array.from(conditions).find(cond => {
            return cond.children[0].children[0].checked === true //Condition at least one checked
                && (cond.children[1] === undefined || //Bypass Quantity if Digital
                    !isEmpty(cond.children[1].children[0].value)) //Quantity
        })  !== undefined)){
        alert("Input non valido, sistema e riprova");
        return;
    }

    // New Object FormData from form
    let formData = new FormData(event.target);

    // New URLSearchParams to build the string
    let urlParams = new URLSearchParams();

    // Add all fields of FormData to URLSearchParams, avoiding void fields
    formData.forEach(function(value, key) {
        if (value.trim() !== "") {
            urlParams.append(key, value);
        }
    });

    //Get parameters String
    let formString = urlParams.toString();

    // Send XML POST Request
    let message = "ask=saveModProd&" + formString;
    xmlConsoleReq(message, 3, divToSetAnswer);

}
