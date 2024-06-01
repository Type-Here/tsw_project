function showContent(sectionId) {
    if (sectionId === 'section4') {
        loadShippingAddresses();
    }
    if (sectionId === 'section2') {
        loadOrders();
    }

    // Nascondi tutte le sezioni
    var sections = document.getElementsByClassName('utente-content-section');
    for (var i = 0; i < sections.length; i++) {
        sections[i].style.display = 'none';
    }
    // Mostra la sezione selezionata
    if (sectionId === 'section2') {
        document.getElementById(sectionId).style.display = 'flex';
    } else {
        document.getElementById(sectionId).style.display = 'block';
    }
}


window.onload = function() {
    splitAddress();
}

function splitAddress() {
    var address = document.getElementById('address').value;
    var addressParts = address.split(' ');
    document.getElementById('road-type').value = addressParts[0];
    document.getElementById('road-name').value = addressParts[1];
    document.getElementById('road-number').value = addressParts[2];
}

// Seleziona tutti gli elementi 'li' con la classe 'utente-nav'
var listItems = document.querySelectorAll('.utente-sidebar ul.utente-nav li');

// Aggiungi un gestore di eventi 'click' a ciascun elemento 'li'
listItems.forEach(function(listItem) {
    listItem.addEventListener('click', function() {
        // Prima di tutto, rendi tutti gli elementi 'li' trasparenti
        listItems.forEach(function(otherListItem) {
            otherListItem.style.backgroundColor = 'transparent';
        });

        // Poi, cambia il colore di sfondo dell'elemento 'li' cliccato a grigio
        this.style.backgroundColor = '#808080';
    });
});

//Show the content of the selected section
function enableUserModify(formId, button) {
    let form = document.getElementById(formId);
    form.querySelectorAll('input').forEach(input => input.removeAttribute('disabled'));
    button.disabled = true;
}

//Function to control valid order ID
document.getElementById('order-id').oninput = function () {
    let orderId = document.getElementById('order-id').value;
    let regex = /^([0-9]+)?$/;
    if (!regex.test(orderId)) {
        orderId = '';
        document.getElementById('order-id').value = orderId;
        document.getElementById('labelID').textContent = 'Inserisci un ID valido';
        document.getElementById('labelID').style.color = 'red';
    } else {
        document.getElementById('labelID').textContent = 'Cerca Ordine per ID';
        document.getElementById('labelID').style.color = '#f5f5f5';
    }
    displayFilteredOrders(orderId);

}

// Function to display the orders filtered by order ID
function displayFilteredOrders(ordersID) {
    let table = document.getElementById('orders-table');
    let rows = table.rows;
    for (let i = 1; i < rows.length; i++) {
        let orderId = rows[i].cells[0].textContent;
        if (ordersID === '') {
            rows[i].style.display = '';
        } else  if (orderId === ordersID){
            rows[i].style.display = '';
        } else {
            rows[i].style.display = 'none';
        }
        console.log("Order ID: " + ordersID);
    }

}

// Function to load orders orderID = '' -> means all orders if no argument is passed
function loadOrders() {
    let base = document.URL.match("(http[s]?://.*?/.*?/)")[0]
    let url = base + "loadOrders";

    fetch(url)
        .then(response => {
            if (!response.ok) {
                console.error('HTTP error', response.status, response.statusText, response.json());
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            displayOrders(data.orders, data.addresses);

        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}

// Function to display all orders in the table
function displayOrders(orders, addresses) {
    let table = document.getElementById('orders-table');

    // Clear the table
    while (table.rows.length > 1) {
        table.deleteRow(1);
    }

    if (orders !== null) {
        // Add each order to the table
        for (let i = 0; i < orders.length; i++) {
            let row = table.insertRow();

            // Add cells to the row
            let cell1 = row.insertCell(0);
            let cell2 = row.insertCell(1);
            let cell3 = row.insertCell(2);
            let cell4 = row.insertCell(3);
            let cell5 = row.insertCell(4);

            // Set the text of the cells
            cell1.textContent = orders[i].id_cart;
            cell2.textContent = orders[i].status;
            cell3.textContent = orders[i].order_date;

            for (let j = 0; j < addresses.length; j++) {
                if (addresses[j].id_add === orders[i].id_add) {
                    cell4.textContent = addresses[j].address + ', ' + addresses[j].city + ' ' + addresses[j].prov + ',' + addresses[j].CAP;
                    break;
                }
            }

            // Create a button to view the order details
            let button = document.createElement('button');
            button.textContent = 'Visualizza';
            button.onclick = function() {
                viewOrderDetails(orders[i].id_cart);
            };

            // Add the button to the cell
            cell5.appendChild(button);
        }
    }
}

// Function to redirect to the servlet
function viewOrderDetails(orderId) {
    let base = document.URL.match("(http[s]?://.*?/.*?/)")[0]
    let url = base + "loadOrderInformation";

    window.location.href = url + "?orderId=" + orderId;

}

//Show Shipping Address
function loadShippingAddresses() {
    let xhr = new XMLHttpRequest();
    let base = document.URL.match("(http[s]?://.*?/.*?/)")[0];
    let url = base + "loadShippingAddresses";
    xhr.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            doPrintAddressesTable(xhr.response);
        }
    }

    xhr.open('POST', url , true);
    xhr.onerror = function (e) {
        console.log(e)
    };
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded"); //Needed for POST req
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest"); //Header for Inform Server of an XMLHttpRequest
    xhr.send();

}

function doPrintAddressesTable(response) {
    let addresses = JSON.parse(response);
    const table = document.getElementById('addresses-table');
    const tbody = table.tBodies[0];

    //Reset Table except first row (headers)
    let rows = tbody.children.length; //Save original value in a variable otherwise for cycle will remove half of the data!
    for (let i = 1; i < rows; i++) {
        tbody.children[1].remove(); //Use fixed 1 to remove all data (0 = header) otherwise Error!
    }

    //For Each Address in address List (from JSON) print a row
    for (let i = 0; i < addresses.length; i++) {
        // Insert a row at the end of table
        let newRow = table.insertRow();
        doPrintRow(newRow, addresses[i]);

        let button = newRow.querySelector('.table-row-button button');

        //If the current address is the first, disable the button
        if (i === 0 && addresses.length === 1) {
            button.disabled = true;
        } else {
            disableDeleteButton(button);
        }
    }
}

function disableDeleteButton(button){
    let base = document.URL.match("(http[s]?://.*?/.*?/)")[0]
    let url = base + "modify-user";

    const id = button.value;

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: "section=" + "buttonDelete" + "&id_add=" + id
    })
        .then(response => {
            if (!response.ok) {
                console.error('HTTP error', response.status, response.statusText, response.json());
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data === true) {
                button.disabled = true;
            }
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}

function doPrintRow(tableRow, address) {
    const printable = ["firstname","lastname","address","city","prov","CAP"];
    Object.keys(address).forEach( key =>{
        let cellText = address[key];
        if(printable.includes(key)){
            // Insert a cell at the end of the row
            let newCell = tableRow.insertCell();
            // Append a text node to the cell
            let newText = document.createTextNode(cellText);
            newCell.appendChild(newText);
        }
    });

// Insert a cell at the end of the row

    let buttonCell = tableRow.insertCell();
    buttonCell.setAttribute('class', 'table-row-button');

    let button = document.createElement('button');
    button.title = "Elimina Indirizzo";
    button.setAttribute('class','secondary attention');
    button.value = address["id_add"];
    button.onclick = function() {
        deleteAddress(this.value);
    };
    button.innerHTML ="&Cross;";
    buttonCell.appendChild(button);
}

//Delete Shipping Address
function deleteAddress(id) {
    let section = 'delete';
    let xhr = new XMLHttpRequest();
    let base = document.URL.match("(http[s]?://.*?/.*?/)")[0];
    let url = base + "modify-user";
    xhr.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            loadShippingAddresses();
        }
    }

    xhr.open('POST', url , true);
    xhr.onerror = function (e) {
        console.log(e)
    };
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded"); //Needed for POST req
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest"); //Header for Inform Server of an XMLHttpRequest
    xhr.send("section="+section+"&id_add="+id);
}

//Function to modify password
document.getElementById('changePassword').addEventListener('submit', function(event) {

    event.preventDefault();

    let passwordOld = document.getElementById('pass-old').value;
    let passwordNew = document.getElementById('pass-new').value;
    let confirmPassword = document.getElementById('pass-new-confirm').value;

    if (passwordOld === '' || confirmPassword === '' || passwordNew === '') {
        document.getElementById('passwordResults').innerHTML = 'Riempi tutti i campi obbligatori!';
        document.getElementById('passwordResults').style.color = '#ad212e';
    } else if (passwordNew !== confirmPassword) {
        document.getElementById('passwordResults').innerHTML = 'Le password non corrispondono';
        document.getElementById('passwordResults').style.color = '#ad212e';
    } else {
        let base = document.URL.match("(http[s]?://.*?/.*?/)")[0]
        let url = base + "modify-user";

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: "section=updatePassword" + "&passwordOld=" + passwordOld + "&passwordNew=" + passwordNew
        }).then(async response => {
            let text = await response.text();
            if (response.ok) {
                document.getElementById('passSend').disabled = true;
                document.getElementById('passwordResults').innerHTML = text + ' Reindirizzamento in corso...'
                document.getElementById('passwordResults').style.color = '#f5f5f5';
                setTimeout(() => {
                    location.reload();
                }, 3000);
            } else {
                document.getElementById('passwordResults').innerHTML = text;
                document.getElementById('passwordResults').style.color = '#ad212e';
            }
        }).catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
    }
});