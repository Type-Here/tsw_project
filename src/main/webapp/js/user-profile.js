function showContent(sectionId) {
    // Nascondi tutte le sezioni
    var sections = document.getElementsByClassName('utente-content-section');
    for (var i = 0; i < sections.length; i++) {
        sections[i].style.display = 'none';
    }
    // Mostra la sezione selezionata
    document.getElementById(sectionId).style.display = 'block';
}

function showEditProfile() {
    // Nascondi il contenuto e mostra il form di modifica
    document.getElementById('utente-content').style.display = 'none';
    document.getElementById('edit-profile-form').style.display = 'block';
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