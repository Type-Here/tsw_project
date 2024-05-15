// Seleziona tutti gli elementi con la classe 'grid-faq0'
var elements = document.querySelectorAll('.grid-faq0');

// Seleziona gli elementi con le classi 'grid-faq-base' 'grid-faq1', 'grid-faq2', 'grid-faq3' e 'grid-faq4'
var gridFaqBase = document.querySelector('.grid-faq-base');
var gridFaqSpedizione = document.getElementById('faq-spedizioni')
var gridFaqProdotti = document.getElementById('faq-prodotti')
var gridFaqPagamento = document.getElementById('faq-pagamento')
var gridFaqLavoraConNoi = document.getElementById('faq-lavora-con-noi')

// Imposta inizialmente la div base come visibile
var currentVisibleDiv = gridFaqBase;

// Imposta inizialmente i div come nascosti
hideDiv(gridFaqSpedizione);
hideDiv(gridFaqProdotti);
hideDiv(gridFaqPagamento);
hideDiv(gridFaqLavoraConNoi);

// Aggiungi un evento di click a ciascun elemento
elements.forEach(function(element, index) {
    element.addEventListener('click', function() {

        var newVisibleDiv;

        // Mostra il div corrispondente a quale 'grid-faq0' è stato cliccato
        switch(index) {
            case 0:
                newVisibleDiv = gridFaqSpedizione;
                break;
            case 1:
                newVisibleDiv = gridFaqProdotti;
                break;
            case 2:
                newVisibleDiv = gridFaqPagamento;
                break;
            case 3:
                newVisibleDiv = gridFaqLavoraConNoi;
                break;
        }

        // Se la div corrente non è la stessa di quella che si vuole mostrare, nascondi la div corrente
        if (currentVisibleDiv !== newVisibleDiv) {
            hideDiv(currentVisibleDiv);
            currentVisibleDiv = newVisibleDiv;
            // Mostra la nuova div corrente
            showDiv(currentVisibleDiv);
        }
    });
});

// Funzioni per nascondere un div con animazione
function hideDiv(div) {
    div.style.opacity = '0';
    div.style.visibility = 'hidden';
    setTimeout(function() {
        div.style.display = 'none';
    }, 0); // delay necessario affinche display:none abbia effetto
}

// Funzioni per nascondere e mostrare un div con animazione
function showDiv(div) {
    div.style.display = 'block';
    setTimeout(function() {
        div.style.opacity = '1';
        div.style.visibility = 'visible';
    }, 300); // delay necessario affinche display:block abbia effetto
}
