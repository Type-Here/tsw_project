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
 * Listener to Remove Buttons to remove Item from Cart
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