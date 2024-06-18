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
    await populateOrderTable(message);
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
    select.addEventListener('change', async (e)=>{

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