/* ===================== CART PAGE JS ============================ */

async function ajax(message, responseType = 'text'){
    if(!message) return;
    return fetch('cart', {
        headers:{
            "Content-type": "application/x-www-form-urlencoded",
            'X-Requested-With': 'XMLHttpRequest',
        },
        method:'POST',
        body: message
    }).then(r => {
        if(!r.ok) {return Promise.reject(new Error("Impossibile effettuare operazione: " + r.status));}
        else return responseType === 'text' ? r.text() : r.json();
    });
}


/**
 * Listener for Remove Buttons: Remove Item from Cart
 */
Array.from(document.getElementsByClassName('remove-item')).forEach( btn =>{
    btn.addEventListener('click', async () =>{
        const message = 'option=removeFromCart&key=' + btn.value;
        try{
            let response = await ajax(message);
            if(!response) return;
            const item = btn.parentElement.parentElement;
            item.classList.add('removed-item');
            item.innerHTML = response;
            item.style.position = 'relative';

            const bar = document.createElement('div');
            item.appendChild(bar);
            bar.classList.add('overlay-bar');
            setTimeout(function (){
                item.remove();
            }.bind(item), 5000);


            const counter = document.getElementById('cart-counter');
            const title = document.getElementsByClassName('cart-overview-title')[0].children[0];

            counter.innerHTML = (parseInt(counter.innerHTML) - 1).toString();
            if(counter.innerHTML === '0') {

                counter.classList.add('general-display-none');
                title.innerHTML = 'Nessun Prodotto in Carrello';
                document.getElementById('void-cart-item').classList.remove('general-display-none');
            } else {
                title.innerHTML = 'Prodotti in Carrello: ' + counter.innerHTML;
            }

            await updatePrice();

        } catch (e){
            console.error(e);
        }
    });
});

/**
 *  Update Quantity Ajax Request from Select Change Input
 */
Array.from(document.getElementsByClassName('quantity-select')).forEach(select =>{
   select.addEventListener('change', async function (){
       let id = this.getAttribute('data-item');
       let condition = this.getAttribute('data-cond');
       let quantity = this.value;
       let message = 'option=addToCart&condition=' + condition + '&id_prod=' + id + '&quantity=' + quantity;
       try{
          await ajax(message, 'json');
          await updatePrice();
       } catch (error){
           alert("OPS... Something Went Wrong:");
       }
   });
});

/**
 * Function to UpdatePrice - Ajax Request then set Subtotal and Total in lateral view
 */
async function updatePrice(){
    let priceMsg = 'option=requestNewPrice';
    const price = await ajax(priceMsg, 'json');
    if(!price) return;

    document.getElementsByClassName('subtotal')[0].children[1].innerHTML = price[0].toFixed(2) + '&euro;';
    document.getElementsByClassName('total')[0].children[1].innerHTML = price[1].toFixed(2) + '&euro;'
}


/* =============================================== DISCOUNT CODE AJAX ================================================== */


/**
 * Input Discount Code Validation
 */
document.getElementById('input-discount-code').addEventListener('input', function (e){
    if(!e.data) return;
    const pattern = /^[a-zA-Z0-9!€$]+$/;
    if(!pattern.test(e.data)) this.value = this.value.slice(0, this.value.length - e.data.length);
    if(this.value.length > 12) this.value = this.value.slice(0, 12); //max length 12
    this.value = this.value.toUpperCase();
});


/**
 * Send Discount Code Button Listener. Validation and AJAX
 */
document.getElementById('button-discount-code').addEventListener('click', async function (){
    const pattern = /^[a-zA-Z0-9!€$]{5,12}$/;
    let code = document.getElementById('input-discount-code').value;
    if(!pattern.test(code)){
        showSpanInfoDiscountCode('Codice Sconto Non Valido');
    }

    try{
        let message = "option=addDiscountCode&key=" + code;
        const data = await ajax(message ,'text');
        //Continue here is OK, otherwise catch
        document.getElementsByClassName('discount-code')[0].innerHTML = '';
        showSpanInfoDiscountCode(data, true);
        await updatePrice();

    } catch (e){
        showSpanInfoDiscountCode('Codice Sconto Non Valido');
    }

});


/**
 * Function to Display a Span with an Info Message in Discount Code Section
 * @param message Message to Display in Span
 * @param isOk is a Successful message = true, is an error message = false; default = false
 */
function showSpanInfoDiscountCode(message, isOk = false){
    let warning = document.getElementById('warning-code');
    if(!warning){
        warning = document.createElement('span');
        warning.id = 'warning-code';
        document.getElementsByClassName('discount-code')[0].appendChild(warning);
    }
    warning.classList.add('text-center');
    if(!isOk) {
        warning.classList.add('invalid-credentials');
        warning.classList.remove('message-ok');
    } else {
        warning.classList.remove('invalid-credentials');
        warning.classList.add('message-ok');
    }
    warning.innerHTML = message;
}