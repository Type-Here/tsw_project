/**
 * Load to Data for Each Product in Item Tile
 *
 */
const itemSection = document.getElementById('cart');
Array.from(itemSection.getElementsByClassName('cart-item')).forEach( div => {
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

        if(select && quantity && quantity > select.children.length){
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