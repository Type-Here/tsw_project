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

            let priceMsg = 'option=requestNewPrice';
            const price = await ajax(priceMsg, 'json');
            if(!price) return;

            document.getElementsByClassName('subtotal')[0].children[1].innerHTML = price[0].toFixed(2) + '&euro;';
            document.getElementsByClassName('total')[0].children[1].innerHTML = price[1].toFixed(2) + '&euro;'

        } catch (e){
            console.error(e);
        }
    });
});


Array.from(document.getElementsByClassName('cart-item')).forEach( div => {
   if(div.id === 'void-cart-item') return;
   fetchProductData(div);
});

/**
 * Async Function to populate each cart-item div with product data (name, platform, front image, quantity)
 * @param div 'cart-item' div to retrieve prod id and set data
 */
async function fetchProductData(div){
    let name_id = div.querySelector('.cart-item-upper h3');
    let cond = div.getElementsByClassName('item-data')[0].children[1].innerHTML.split(":")[1].trim();
    if(!name_id.innerHTML) return;
    let message = "option=retrieveProduct&id=" + name_id.innerHTML;

    const resp = await ajax(message, 'json');
    if(!resp) return;

    name_id.innerHTML = resp['name'];
    div.getElementsByClassName('item-data')[0].children[0].innerHTML = 'Piattaforma: ' + resp['platform'];
    const img = div.getElementsByClassName('item-img')[0].children[0];
    const metadata = resp['metadataPath'];

    let meta = await fetch('metadata/'+ resp['platform'] + '/' + metadata)
        .then(resp => resp.json())
        .then(data => {
            return data['front'];
        });

    img.src = 'metadata/' + resp['platform'] + '/img/' + resp['id_prod'] + '/' + meta;

    let quantity;
    //Prod is Physical: 0-Phy, 1-Dig
    if(!resp['type']){
        const conditions = resp['conditions'];
        conditions.forEach( c =>{
           if(c['condition'].trim() === cond){
               quantity = c['quantity'];
           }
        });

        const select = div.querySelector('.cart-item-operations select');
        if(quantity && quantity > select.children.length){
            const add = quantity - select.children.length;
            let i = select.children.length + 1;
            for(; i <= quantity; i++){
                let opt = document.createElement('option');
                opt.innerHTML = i.toString();
                select.appendChild(opt);
            }
        }

    }

}