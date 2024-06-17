import {doPrintTable} from "./prod-catalog.js";

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
    await populateOrderTable(message);
    ordersTable.setAttribute('data-page', page.toString());
});


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

    } else { //Display Error Message
        span.classList.remove('general-display-none');
        span.innerHTML = await response.text();
        console.error(response.status + ":" + response.statusText);
    }
}

/**
 * Input Order Search Validation - Only Numbers
 */
document.getElementById('search-input-order').addEventListener('input', function(e){
    if(!e.data) return;
    const pattern = /[0-9]+/
    if(!pattern.test(e.data)) this.value = this.value.slice(0, this.value.length - e.data.length);
    //if(this.value.length > 1000) this.value = this.value.slice(0, 1000);
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