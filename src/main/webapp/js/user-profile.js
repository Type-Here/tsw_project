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