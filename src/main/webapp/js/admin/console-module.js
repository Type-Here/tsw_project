import {doPrintTable} from "./prod-catalog.js";

/**
 * AJAX FUNCTION
 * @param message body data to POST
 * @returns {Promise<Response>} Fetch
 */
async function ajax(message){
    return fetch('console', {
        headers: {
            "Content-type": "application/x-www-form-urlencoded",
            'X-Requested-With': 'XMLHttpRequest',
        },
        method: 'POST',
        body: message
    });
}

/* ======================================== LOAD DATA LISTENERS ========================================== */

/**
 * Add Button Filter Listeners on Orders Status
 */
Array.from(document.getElementsByClassName('order-filters')[0].children)
    .forEach((button, index, array) =>{

        button.addEventListener('click', async () =>{
            array.forEach( btn => { btn.classList.remove('active')});
            button.classList.add('active');
            let orderStatus = button.value;
            let message = "action=ordersManager&ask=retrieve&type=" + orderStatus;
            await populateOrderTable(message);
        });
});


/**
 * Listener For Prev Order Page Button
 */
document.getElementById('prev-order-button').addEventListener('click', async ()=>{
    const ordersTable = document.getElementById('admin-orders-table')
    let page = parseInt(ordersTable.getAttribute('data-page'));
    if(!page || page <= 1) {this.disabled = 'true'; return;}

    let orderStatus = document.getElementsByClassName('order-filters')[0].getElementsByClassName('active')[0].value;
    let message = "action=ordersManager&ask=retrieve&type=" + orderStatus + "&page=" + --page;
    await populateOrderTable(message);
    ordersTable.setAttribute('data-page', page.toString());
});


/**
 * Listener for Next Order Page Button
 */
document.getElementById('next-order-button').addEventListener('click', async ()=>{
    const ordersTable = document.getElementById('admin-orders-table')
    let page = parseInt(ordersTable.getAttribute('data-page'));

    let orderStatus = document.getElementsByClassName('order-filters')[0].getElementsByClassName('active')[0].value;
    let message = "action=ordersManager&ask=retrieve&type=" + orderStatus + "&page=" + ++page;
    await populateOrderTable(message); //This Call will disable next button if page has < 10 elements
    ordersTable.setAttribute('data-page', page.toString());
});


/**
 * Search Bar Order Table AJAX Listener
 */
document.forms.namedItem('order-search').addEventListener('submit', async function (e){
    e.preventDefault(); //Abort Submit and use ajax instead (to reuse code from search bar

    let input = this.elements[0].value;
    let message = "action=ordersManager&ask=retrieveId&id=" + input;
    await populateOrderTable(message);

    document.getElementById("prev-order-button").classList.add('general-display-none'); //Hide Prev Button
    document.getElementById("next-order-button").classList.add('general-display-none'); //Hide Next Button
});

/* ====================== VALIDATION DATA LISTENERS ========================= */

/**
 * Input Order Search Validation - Only Numbers
 */
document.getElementById('search-input-order').addEventListener('input', function(e){
    if(!e.data) return;
    const pattern = /[0-9]+/
    if(!pattern.test(e.data)) this.value = this.value.slice(0, this.value.length - e.data.length);
    //if(this.value.length > 1000) this.value = this.value.slice(0, 1000);
});


/* ============================== FUNCTIONS ======================================== */

/* POPULATE TABLE */

/**
 * Retrieve data for Admin Orders Table based on message
 * @param message ajax message (in body POST)
 */
async function populateOrderTable(message){
    const response = await ajax(message);

    //Error Span to print message
    const span = document.getElementById('order_error_message');
    if(response.ok){
        const data = await response.json();

        const ordersTable = document.getElementById('admin-orders-table')
        ordersTable.setAttribute('data-page', '1');
        document.getElementById('prev-order-button').classList.remove('general-display-none');
        document.getElementById('next-order-button').classList.remove('general-display-none');

        // no more content, disable next button
        if(data.status === "last"){
            document.getElementById('next-order-button').disabled = 'true';
        }

        //Reset Error Message if any
        span.classList.add('general-display-none');
        span.innerHTML = '';

        //Print Data
        doPrintTable(data.data, ordersTable, 2);
        doAddSelectOrderStatus(ordersTable);

    } else { //Display Error Message
        span.classList.remove('general-display-none');
        span.classList.add('invalid-credentials');
        span.innerHTML = await response.text();
        console.error(response.status + ":" + response.statusText);
    }
}

/* ADD SELECT */

/**
 * Add A Select Input at each Order Data Row to Change Order Status
 * @param ordersTable HtmlTableElement of Orders
 */
function doAddSelectOrderStatus(ordersTable){
    const tbody = ordersTable.tBodies[0];
    //Add Select Status to Table
    const statuses = ['in process', 'shipped', 'delivering', 'delivered', 'refunded', 'canceled'];
    let rows = tbody.children.length;

    //For Each Data Row
    for(let i = 1; i < rows; i++){ //(0 = header)
        const row = tbody.children[i];
        const cell = row.insertCell(); //Add Cell at the end of a row

        //Add A Select Input for Each Row Data to Change Order Status
        const select = document.createElement('select');
        select.setAttribute('data-id', row.children[0].innerHTML); //children[0] is ID Order Column
        addListenerToSelect(select); //Add Listener to Select
        statuses.forEach( s => {
            const option = document.createElement('option');
            option.value= s;
            option.innerHTML = s;
            if(s.replace(/\s/g, '') === row.children[5].innerHTML.toLowerCase()) option.selected = true; //children[5] is Status Order Column
            select.add(option);
        })
        cell.appendChild(select);
    }
}


/**
 * Select Input in Each Order Table Row: On Change: send AJAX to modify Order Status
 * @param select select input to add listener
 */
function addListenerToSelect(select){
    select.addEventListener('change', async ()=>{

        //'data-id' custom attribute in tag where id_order is saved, select.value = option selected to update status
        let message = "action=ordersManager&ask=updateStatus&id=" + select.getAttribute('data-id') + "&status=" + select.value;

        const response = await ajax(message);

        //Span to print message status
        const span = document.getElementById('order_error_message');
        span.innerHTML = await response.text();

        if(response.ok){
            span.classList.remove('invalid-credentials');
            span.classList.add('successful-info');
        } else {
            span.classList.remove('successful-info');
            span.classList.add('invalid-credentials');
        }

        span.classList.remove('general-display-none');
        setTimeout(() =>{
            span.classList.add('general-display-none');
            span.classList.remove('successful-info');
            span.innerHTML = '';
        }, 6000);
    });
}

/* ----------------------------------------------------------------------------------------------------------------------------------------------------------------- */

/* ========================================================================= USERS TABLE =========================================================================== */




/* ------- LISTENERS ------- */

/**
 * Listener for Load Data Button: Load First Page
 */
document.getElementById('load-users-button').addEventListener('click', async ()=>{
    let message = "action=usersManager&ask=retrieve";
    await populateUserTable(message)
});


/**
 * Listener For Prev Users Page Button
 */
document.getElementById('prev-users-button').addEventListener('click', async ()=>{
    const usersTable = document.getElementById('admin-users-table')
    let page = parseInt(usersTable.getAttribute('data-page'));
    if(!page || page <= 1) {this.disabled = 'true'; return;}

    let message = "action=ordersManager&ask=retrieve&page=" + --page;
    await populateUserTable(message);
    usersTable.setAttribute('data-page', page.toString());
});


/**
 * Listener for Next Users Page Button
 */
document.getElementById('next-users-button').addEventListener('click', async ()=>{
    const usersTable = document.getElementById('admin-users-table')
    let page = parseInt(usersTable.getAttribute('data-page'));
    let message = "action=ordersManager&ask=retrieve&page=" + ++page;
    await populateUserTable(message); //This Call will disable next button if page has < 10 elements
    usersTable.setAttribute('data-page', page.toString());
});


/**
 * Search Bar Users Table AJAX Listener
 */
document.forms.namedItem('users-search').addEventListener('submit', async function (e){
    e.preventDefault(); //Abort Submit and use ajax instead (to reuse code from search bar)

    let input = this.elements[0].value;
    let message = "action=usersManager&ask=retrieveId&id=" + input;
    await populateUserTable(message);

    document.getElementById("prev-order-button").classList.add('general-display-none'); //Hide Prev Button
    document.getElementById("next-order-button").classList.add('general-display-none'); //Hide Next Button
});




/* ------- FUNCTIONS ------ */


/**
 * Function To Populate User Table.
 * Execute AJAX based on message as parameter.
 * Call printTable
 * Set span (error) messages
 * @param message POST request message
 */
async function populateUserTable(message){
    const response = await ajax(message);
    //Error Span to print message
    const span = document.getElementById('users_error_message');
    if(response.ok){
        const data = await response.json();

        const usersTable = document.getElementById('admin-users-table')
        usersTable.setAttribute('data-page', '1');
        document.getElementById('prev-users-button').classList.remove('general-display-none');
        document.getElementById('next-users-button').classList.remove('general-display-none');

        // no more content, disable next button
        if(data.status === "last"){
            document.getElementById('next-users-button').disabled = 'true';
        }

        //Reset Error Message if any
        span.classList.add('general-display-none');
        span.innerHTML = '';

        //Print Data
        doPrintTable(data.data, usersTable, 3);

    } else { //Display Error Message
        span.classList.remove('general-display-none');
        span.classList.add('invalid-credentials');
        span.innerHTML = await response.text();
        console.error(response.status + ":" + response.statusText);
    }
}

/* ------ VALIDATION DATA LISTENERS ----- */

/**
 * Input User Search Validation - Only Numbers
 */
document.getElementById('search-input-users').addEventListener('input', function(e){
    if(!e.data) return;
    const pattern = /[0-9]+/
    if(!pattern.test(e.data)) this.value = this.value.slice(0, this.value.length - e.data.length);
    //if(this.value.length > 1000) this.value = this.value.slice(0, 1000);
});





/* ----------------------------------------------------------------------------------------------------------------------------------------------------------------- */

/* ========================================================================= SETTINGS TABLE =========================================================================== */


/* ------------- CHANGE PASSWORD ------------- */

/**
 * Input New and Confirm listeners to validate data (visual suggestion to user red = wrong, green = ok)
 */
Array.from(document.forms.namedItem('pass-change').elements).forEach((input, index, array) =>{
    if((input.type === 'text' || input.type === 'password') && input.name !== 'old'){
        input.addEventListener('input', function (){

            let confirm = array.find(e => e.name === 'confirm'); //confirm input
            let isConfirmCheck = (confirm.value === array.find(e => e.name === 'new').value); //verify if new and confirm input are equals

            const pattern = /^(?=.*[a-zà-ÿ])(?=.*[A-ZÀ-Ÿ])(?=.*[#@€!£$%&/()=?'^])(?=.*[0-9]).{8,}$/ //Validation Pattern
            if(!pattern.test(input.value)) this.style.background = '#77000090';
            else this.style.background = '#00770090';

            if(!isConfirmCheck) confirm.style.background = '#77000090'; //set confirm to red if password doesn't match with 'new' input value
        });
    }
})


/**
 * Listener on Submit Change Password Form: validate and send data to Update: AJAX
 */
document.forms.namedItem('pass-change').addEventListener('submit', async function(e){
    e.preventDefault();
    const pattern = /^(?=.*[a-zà-ÿ])(?=.*[A-ZÀ-Ÿ])(?=.*[#@€!£$%&/()=?'^])(?=.*[0-9]).{8,}$/ //Validation Pattern
    const data = new FormData(this);

    const newInput = data.get('new').toString(); //Gets Directly the Value
    const oldInput = data.get('old').toString();
    const confirmInput = data.get('confirm').toString();

    const span = document.getElementById('setting_error_message');

    if(!pattern.test(newInput)){
        span.classList.remove('general-display-none');
        span.innerHTML = 'Criteri Nuova Password: <ul class="text-left"><li>Almeno 8 Caratteri</li><li>Almeno una lettera minuscola a-z</li>' +
            '<li>Almeno una lettera maiuscola A-Z</li><li>Almeno un numero 0-9</li><li> Almeno un Carattere Speciale £$%&€...</li></ul>'
        return;
    }

    if(newInput !== confirmInput){
        span.classList.remove('general-display-none');
        span.innerHTML = 'La Nuova Password non combacia con la Conferma!'
        return;
    }

    if(!oldInput){
        span.classList.remove('general-display-none');
        span.innerHTML = 'Inserisci la Vecchia Password'
        return;
    }

    let message = 'action=settings&ask=modifyAdmin&old=' + oldInput + '&new=' + newInput;
    let response = await ajax(message);
    let responseText = await response.text();

    printSpanMessage(span, responseText, response.ok); //Print Success or Error Message
})



/* ---------------------------- DISCOUNT CODE -------------------------------------- */


/* REMOVE CODE */

/**
 * Remove Discount Code Button(s) Listener AJAX
 * Should be only 1 Discount Code, but used class for future improvements
 */
Array.from(document.getElementsByClassName('remove-discount-button')).forEach(button => {
    button.addEventListener('click', async ()=>{
        let message = 'action=settings&ask=removeDiscountCode&key=' + button.getAttribute('data-value');
        const response = await ajax(message);
        const responseText = await response.text();

        const span = document.getElementById('discount_error_message');
        printSpanMessage(span, responseText, response.ok); //Print Response

        //Remove Old Code from Console View
        if(response.ok){
            document.getElementById('discount-display').innerHTML = ' <span class="text-center">Non ci sono Codici Attivi</span>';
        }
    });
})


/* SET NEW CODE (ADD/CHANGE) */

/**
 * Discount Code Name Validation Listener
 */
document.getElementById('discountName').addEventListener('input', function(e){

    if(!e.data) return;
    const pattern = /^[A-Z0-9!€$]+$/;
    if(!pattern.test(e.data)) this.value = this.value.slice(0, this.value.length - e.data.length);
    if(this.value.length > 30) this.value = this.value.slice(0, 30); //max length 30
});


/**
 * Discount Code Value Validation Listener
 */
document.getElementById('discountValue').addEventListener('input', function(e){
    if(!e.data) return;
    const pattern = /^[0-9,.]+$/;
    if(!pattern.test(e.data)) this.value = this.value.slice(0, this.value.length - e.data.length);
    if(parseFloat(this.value) > 100.0) this.value = '100';
    if(this.value.length > 5) this.value = this.value.slice(0, 5); //max length 30
});


/**
 * Submit Discount Code Button Listener: Validation and Send Data: AJAX
 * Change / Add new Discount Code
 */
document.getElementById('add-discountCode-button').addEventListener('click', async () => {
    let key = document.getElementById('discountName').value;
    let value = document.getElementById('discountValue').value;
    const patternKey = /^[A-Z0-9!€$]{5,30}$/;
    const patternValue = /^[0-9,.]{1,5}$/;
    let val = parseFloat(value);

    const span = document.getElementById('discount_error_message');
    if (!patternKey.test(key) || !patternValue.test(value) || val <= 0 || val > 100) {
        printSpanMessage(span, "Codice o Valore non Valido", false);
        return;
    }

    let message = 'action=settings&ask=changeDiscountCode&key=' + key + '&value=' + value;
    let response = await ajax(message);
    printSpanMessage(span, await response.text(), response.ok);

    // Change Old Discount Code View and Add The New One if Response is OK
    if(response.ok) {
        const ul = document.createElement('ul');
        ul.classList.add('text-center');
        ul.classList.add('hide-bullets');

        const liName = document.createElement('li');
        liName.classList.add('margin-v-10');
        liName.innerHTML = 'Codice Sconto: <span class="green-info">' + key + '</span>';

        const liVal = document.createElement('li');
        liVal.innerHTML = 'Percentuale Sconto: <span class="discount">' + value +' </span>';

        ul.appendChild(liName);
        ul.appendChild(liVal);

        const display = document.getElementById('discount-display');
        display.innerHTML = '';
        display.appendChild(ul);

    }

});


/* ===================================================== USEFUL FUNCTIONS ==================================================== */


/**
 * Display Message inside Message Span <br />
 * Show Success - Error Message, Hide after 6 second
 * @param span pointer to span to show
 * @param message message to print inside the span
 * @param isOk true = success message, false = error message, default = false
 */
function printSpanMessage(span, message, isOk = false){
    isOk ? span.classList.add('successful-info') : span.classList.remove('successful-info');
    span.classList.remove('general-display-none');
    span.innerHTML = message;

    setTimeout(() =>{
        span.classList.remove('successful-info');
        span.classList.add('general-display-none');
        span.innerHTML = '';
    }, 6000);
}