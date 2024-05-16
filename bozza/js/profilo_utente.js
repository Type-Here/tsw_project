function showContent(sectionId) {
    // Nascondi tutte le sezioni
    var sections = document.getElementsByClassName('content-section');
    for (var i = 0; i < sections.length; i++) {
        sections[i].style.display = 'none';
    }
    // Mostra la sezione selezionata
    document.getElementById(sectionId).style.display = 'block';
}


document.querySelector('#edit-button').addEventListener('click', function() {
    var inputs = document.querySelectorAll('#section1 input');
    inputs.forEach(function(input) {
        input.disabled = false;
    });
});