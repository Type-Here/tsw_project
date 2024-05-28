/* ===== DOCUMENT FOR PRODUCT PAGE ===== */


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
        const params = new URLSearchParams(window.location.search);
        const id_prod = params.get('id_prod');
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
 * Add to Cart
 */

document.getElementById('add-to-cart').addEventListener('click', ()=>{
    const counter = document.getElementById('cart-counter');
    let val = counter.innerHTML;
    counter.innerHTML = (parseInt(val) + 1).toString();
    counter.classList.remove('general-display-none');

});