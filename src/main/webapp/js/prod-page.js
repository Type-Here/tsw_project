/* ===== DOCUMENT FOR PRODUCT PAGE ===== */

function getIdProdFromURL(){
    const params = new URLSearchParams(window.location.search);
    return params.get('id_prod');
}

        /* --- USE OF PROMISE --- */
/**
 * Condition Button Listeners
 * AJAX call (use Promises) to retrieve price of a specified condition.
 */
Array.from(document.getElementsByClassName('prod-condition-button')).forEach( (button, index, array) =>{
    button.addEventListener('click', function (){

        //button.value has the id_cond
        for(let but of array){
            but.classList.remove('active-button');
        }
        button.classList.add('active-button');

        const id_prod = getIdProdFromURL();

        //Promise
        fetch('desc',{
            headers:{
                "Content-type":"application/x-www-form-urlencoded",
                'X-Requested-With':'XMLHttpRequest',
            },
            method:'POST',
            body:'opt=fetchPrice&condition=' + button.value + '&id_prod=' + id_prod
        })
        .then(promise => promise.json()) // Await and Resolve Promise
        .then(function (response){ // Response resolved = data
            document.getElementById('prod-price').innerHTML = response + '&euro;'
        }).catch(error => console.log(error));

    }.bind(array));
});


/* ---- ADD TO CART ---- */

/**
 * Add to Cart Button Listener
 */

document.getElementById('add-to-cart').addEventListener('click', ()=>{
    const counter = document.getElementById('cart-counter');
    let val = counter.innerHTML;

    const id_prod = getIdProdFromURL();
    const id_condition = document.querySelector('.prod-condition-button.active-button').value;
    addToCart(id_prod, id_condition)
        .then(r => {
        if(r.status === 200){
            counter.innerHTML = (parseInt(val) + 1).toString();
            counter.classList.remove('general-display-none');
        } else {
            alert("Impossibile aggiungere al carrello: Code " + r.status);
        }
    }).catch(r => console.log(r));
});


/**
 * Add to Cart Async Function
 * @param prod id of the product
 * @param condition id cf the condition
 * @returns {Promise<Response>}
 */
async function addToCart(prod, condition){
    if(!prod || !condition){
        console.error("No Enough Data to Add");
        return null;
    }
    return fetch('cart', {
        headers: {
            "Content-type": "application/x-www-form-urlencoded",
            'X-Requested-With': 'XMLHttpRequest',
        },
        method: 'POST',
        body: 'option=addToCart&condition=' + condition + '&id_prod=' + prod
    });
}