import {xmlConsoleReq} from "./prod-catalog.js";

/* ============= POPUP MOD AND DELETE BUTTONS ===================== */

/**
 * Function called by Hide Button and on click over overlay div to close Modify Popup
 */
function hidePopup(){
    const popupDiv = document.getElementById('modify-prod-popup');
    const formSection = document.getElementById('modify-form');
    const removeDiv = document.getElementById('remove-prod-popup');
    const infoPopup = document.getElementsByClassName('info-popup')[0];
    document.getElementsByClassName('overlay')[0].classList.remove( 'general-display-block');
    document.getElementsByClassName('overlay-popup')[0].classList.add( 'general-display-none');
    popupDiv.classList.add('general-display-none');
    if(infoPopup) infoPopup.remove();
    if(formSection) popupDiv.removeChild(formSection);
    if(removeDiv) popupDiv.removeChild(removeDiv);
}

/* =============== MODIFY PRODUCT =============== */

/* Listener for Modify Button */
/**
 * Add a Listener to Modify Button: On Click Show Popup to Modify the selected Product
 * @param button button to which add the listener
 */
export function addPopup(button){
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
export function doPrintProdForm(response){
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
export function removePopup(button){
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
