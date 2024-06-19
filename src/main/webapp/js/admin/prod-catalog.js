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
function xmlConsoleReq(message, option = 1, divToSet = null){
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



/* ============= POPUP MOD AND DELETE BUTTONS ===================== */

/**
 * Function called by Hide Button and on click over overlay div to close Modify Popup
 */
function hidePopup(){
    let popupDiv = document.getElementById('modify-prod-popup');
    let formSection = document.getElementById('modify-form');
    let removeDiv = document.getElementById('remove-prod-popup');
    document.getElementsByClassName('overlay')[0].classList.remove( 'general-display-block');
    popupDiv.classList.add('general-display-none');
    if(formSection) popupDiv.removeChild(formSection);
    if(removeDiv) popupDiv.removeChild(removeDiv);
}

/* =============== MODIFY PRODUCT =============== */

/* Listener for Modify Button */
/**
 * Add a Listener to Modify Button: On Click Show Popup to Modify the selected Product
 * @param button button to which add the listener
 */
function addPopup(button){
    button.addEventListener('click', function (){
        let popupDiv = document.getElementById('modify-prod-popup');
        popupDiv.style.width = '80%';

        let formSection = document.getElementById('add_prod').cloneNode(true);
        formSection.id = "modify-form";

        const typeProd = formSection.getElementsByClassName('type-prod');
        const conditions = formSection.getElementsByClassName('condition-input');

        //Physical Type (Disable digital condition but not uncheck if previously set
        typeProd[0].addEventListener('click', () =>{
            Array.from(conditions).forEach(cond =>{
                Array.from(cond.children).forEach( c =>{ c.children[0].disabled = false});
            });
            Array.from(conditions[0].children).forEach( c =>{ c.children[0].disabled = true});
        });

        //Digital Type (Disable physical condition but not uncheck if previously set
        typeProd[1].addEventListener('click', () =>{
            Array.from(conditions).forEach(cond =>{
                Array.from(cond.children).forEach( c =>{ c.children[0].disabled = true});
            });
            Array.from(conditions[0].children).forEach( c =>{ c.children[0].disabled = false});
        });

        let form = formSection.getElementsByTagName('form')[0];
        let divForm = formSection.getElementsByClassName('log_form add-prod-info')[0];
        form.enctype = 'application/x-www-form-urlencoded';
        form.id = 'mod-form';
        form.method = 'POST';
        form.action = 'console';
        form.addEventListener('submit', (e) =>{doModifyAction(e, form, divForm)});

        form.querySelector('input[type="hidden"]').value = 'prodManager';

        let hiddenID = document.createElement('input');
        hiddenID.type = 'hidden';
        hiddenID.name = 'id_prod';
        hiddenID.value = button.value;
        form.appendChild(hiddenID);

        //Change Title of Section in Popup
        const divHeading = formSection.getElementsByClassName('section-header')[0];
        divHeading.classList.add('space-between-flex-container');
        divHeading.children[0].classList.add('margin-h-10'); //H2 Title
        divHeading.children[0].innerHTML = "Modifica Prodotto";

        const exitHeading = document.createElement('button');
        exitHeading.innerHTML = '&Cross;';
        exitHeading.classList.add('no-background');
        exitHeading.classList.add('margin-h-10');
        exitHeading.onclick = () =>{hidePopup();};
        divHeading.appendChild(exitHeading);

        let prodId = this.value; //Button Value

        let message = "action=prodManager&ask=modifyProd&id=" + prodId;
        xmlConsoleReq(message, 2);

        popupDiv.classList.remove('general-display-none');
        popupDiv.style.left = (window.innerWidth - parseInt(getComputedStyle(popupDiv).width))/2 + "px"; //Center the popup

        //REMOVE INPUT FILE FOR IMAGES IN POPUP
        formSection.querySelector('label:has(input[type=file])').remove();
        formSection.querySelector('label:has(input[type=file])').remove();

        //Remove Reset Button and Change Submit Name
        formSection.querySelector('input[type=reset]').remove();
        formSection.querySelector('input[type=submit]').value="Modifica";

        //Add Section with Form to Modify Popup
        popupDiv.appendChild(formSection);

        /* Overlay Show */
        document.getElementsByClassName('overlay')[0].classList.add('general-display-block');
        /* Overlay Click removes popup and overlay */
        document.getElementsByClassName('overlay')[0].addEventListener('click', () =>{
            hidePopup();
        });
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
                form.getElementsByClassName('type-prod')[0].checked = (prod['type'] === false); //Physical
                form.getElementsByClassName('type-prod')[1].checked = (prod['type'] === true); //Digital

                if(prod[key] === true){ //If Product is Digital set Condition to digital too
                    form.querySelector('.condition-input [name="condition"][value="X"]').checked = true;
                }
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
                let isDigital = (prod["type"] === true);

                Array.from(form.getElementsByClassName('condition-input')).forEach((row, index) => {

                    console.log("isDig:" + isDigital + ",ind:" + index);
                    //Equivalent to XOR Op
                    //Disable all but first if digital, viceversa if physical,
                    row.getElementsByTagName('input')[0].disabled = (isDigital !== (index === 0));
                    prod[key].forEach((condition) => {
                        let input = row.querySelector('[name="condition"][value="' + conditionsValues[condition["id_cond"]] + '"]');
                        if (input) {
                            input.checked = true;
                            if (condition["quantity"]) {
                                const quantityInput = row.querySelector('[name="quantity"]');
                                quantityInput.value = condition["quantity"];
                                quantityInput.disabled = isDigital;
                            }
                        }
                    });
                });

                break;

            default:
                if (formElement[0]) form.querySelector('[name="' + key + '"]').value = prod[key];
        }

    });
}

/* ========== REMOVE PRODUCT ======== */

/**
 * Show Delete Popup Request to Confirm or Cancel Operation. (AJAX)
 * @param button that has the listener on click. Retrieve id of product to be deleted on its value.
 */
function removePopup(button){
    const popupDiv = document.getElementById('modify-prod-popup');

    // Main div inside popup
    let divPopup = document.createElement('div');
    divPopup.id = 'remove-prod-popup';
    divPopup.classList.add('margin-v-10');
    divPopup.innerHTML = '<h2 class="text-center">Rimuovere Prodotto?</h2>'

    // Span for request
    let prodInfo = document.createElement('span');
    prodInfo.classList.add('margin-v-10');
    prodInfo.innerHTML = "Sei sicuro di voler rimuovere: <br />-ID " + button.value + "?";

    // Div for Buttons
    let btnDiv = document.createElement('div');
    btnDiv.classList.add('space-around-row-flex-container');
    btnDiv.classList.add('margin-v-10');

    // Confirm Delete Button
    let btnConfirm = document.createElement('button');
    btnConfirm.setAttribute('class', 'default attention');
    btnConfirm.innerHTML = "Conferma";
    btnConfirm.onclick = function (){
        let message = 'action=prodManager&ask=deleteProd&id=' + button.value;
        xmlConsoleReq(message, 4, divPopup);
    }

    // Cancel Operation Button
    let btnCancel = document.createElement('button');
    btnCancel.innerHTML = "Annulla";
    btnCancel.setAttribute('class', 'default');
    btnCancel.onclick = () => hidePopup();

    //Append Children Nodes
    divPopup.appendChild(prodInfo);
    btnDiv.appendChild(btnConfirm);
    btnDiv.appendChild(btnCancel);
    divPopup.appendChild(btnDiv);
    popupDiv.appendChild(divPopup);

    //Show Popup and add listener to overlay
    popupDiv.classList.remove('general-display-none');
    popupDiv.style.left = (window.innerWidth - parseInt(getComputedStyle(popupDiv).width))/2 + "px"; //Center the Popup
    /* Overlay Show */
    document.getElementsByClassName('overlay')[0].classList.add('general-display-block');
    /* Overlay Click removes popup and overlay */
    document.getElementsByClassName('overlay')[0].addEventListener('click', () =>{
        hidePopup();
    });
}


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
