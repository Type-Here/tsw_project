function showContent(sectionId) {
    // Nascondi tutte le sezioni
    var sections = document.getElementsByClassName('utente-content-section');
    for (var i = 0; i < sections.length; i++) {
        sections[i].style.display = 'none';
    }
    // Mostra la sezione selezionata
    document.getElementById(sectionId).style.display = 'block';
}