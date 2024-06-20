//Modal for order information
const modal = document.querySelector('.prodModal');
const modalOpen = document.querySelectorAll('.openModal');
const modalClose = document.querySelector('.closeModal');

modalOpen.forEach(button => {
    button.addEventListener('click', () => {

        if (modal.querySelector('input[name="id_prod"]').value !== button.value) {
            modal.querySelector('input[name="evaluation"]').value = 0;
            stars.forEach(star => {
                star.classList.remove('star-on');
                star.classList.add('star-off');
            })
            modal.querySelector('textarea[name="comment"]').value = '';
        }
        modal.querySelector('input[name="id_prod"]').value = button.value;
        modal.showModal();
    })
})

modalClose.addEventListener('click', () => {
    modal.close();
})

modal.addEventListener('click', e => {
    const dialogDimensions = modal.getBoundingClientRect();
    if (
        e.clientX < dialogDimensions.left ||
        e.clientX > dialogDimensions.right ||
        e.clientY < dialogDimensions.top ||
        e.clientY > dialogDimensions.bottom
    ) {
        modal.close();
    }
})

//Stars
let stars = document.querySelectorAll('.star');

stars.forEach(star => {
    star.addEventListener('click', function() {
        // Remove star-off class and add star-on class
        this.classList.remove('star-off');
        this.classList.add('star-on');

        // Update the value of the select input
        let value = this.getAttribute('data-value');
        document.getElementById('evaluation').value = value;

        // Update other stars
        stars.forEach(s => {
            if (s.getAttribute('data-value') > value) {
                s.classList.remove('star-on');
                s.classList.add('star-off');
            } else {
                s.classList.remove('star-off');
                s.classList.add('star-on');
            }
        });
    });
});

//control the form for required fields
document.querySelector('.modalForm').addEventListener('submit', function(event) {
    let evaluation = document.querySelector('input[name="evaluation"]').value;
    let comment = document.querySelector('textarea[name="comment"]').value;

    if (evaluation === '0' || comment === '') {
        event.preventDefault();
        alert('Riempi tutti i campi obbligatori!');
    } else { //if all fields are filled, send the form
        
        event.preventDefault();
        const formData = new FormData(this); //refer to all form data
        const data = new URLSearchParams(formData);

        let button = document.querySelector('.openModal[value="' + formData.get('id_prod') + '"]');

        let base = document.URL.match("(http[s]?://.*?/.*?/)")[0]
        let url = base + "registerReviewServlet";

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: data
        }).then(response => {
            if (response.ok) {
                modal.close();
                button.disabled = true;
            } else {
                alert('Errore nella valutazione del prodotto');
            }
        })
    }
})

//Refund button
const refundButtons = document.querySelectorAll('.refundButton');

function refundCartItem(cartItemID, button){
    let base = document.URL.match("(http[s]?://.*?/.*?/)")[0]
    let url = base + "refund";

    const cartID = document.getElementById('cartID').value

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'cartItemID=' + cartItemID + '&cartID=' + cartID
    }).then(response => {
        if (response.ok) {
            const text = document.createTextNode('In attesa di conferma');
            button.parentNode.replaceChild(text, button);
            alert('Richiesta di rimborso inoltrata.');
        } else {
            alert('Errore nel rimborso del prodotto');
        }
    })
}

//Img loader
//${pageContext.request.contextPath}/metadata/${product.platform}/img/${product.id_prod}/0.jpeg
