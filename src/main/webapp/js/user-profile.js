function showContent(sectionId) {
    if (sectionId === 'section4') {
        loadShippingAddresses();
    }

    // Nascondi tutte le sezioni
    var sections = document.getElementsByClassName('utente-content-section');
    for (var i = 0; i < sections.length; i++) {
        sections[i].style.display = 'none';
    }
    // Mostra la sezione selezionata
    document.getElementById(sectionId).style.display = 'block';
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


//Show Shipping Address


function loadShippingAddresses() {
    let xhr = new XMLHttpRequest();
    let base = document.URL.match("(http[s]?://.*?/.*?/)")[0];
    let url = base + "loadShippingAddresses";
    xhr.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            doPrintTable(xhr.response);
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

function doPrintTable(response) {
    let addresses = JSON.parse(response);
    const table = document.getElementById('addresses-table');
    const tbody = table.tBodies[0];
    console.log("Table:" + tbody.children.length);
    //Reset Table except first row (headers)
    let rows = tbody.children.length; //Save original value in a variable otherwise for cycle will remove half of the data!
    for (let i = 1; i < rows; i++) {
        tbody.children[1].remove(); //Use fixed 1 to remove all data (0 = header) otherwise Error!
    }

    //For Each Product in product List (from JSON) print a row
    for (let i = 0; i < addresses.length; i++) {
        // Insert a row at the end of table
        let newRow = table.insertRow();
        doPrintRow(newRow, addresses[i]);

        //If the current address is the first, disable the button
        if (i === 0 && addresses.length === 1) {
            newRow.querySelector('.table-row-button button').disabled = true;
        }
    }
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
