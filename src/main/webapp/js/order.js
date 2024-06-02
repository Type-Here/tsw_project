/**
 * AJAX
 * @param message post body
 * @param responseType text/json to return
 */
async function ajax(message, responseType = 'text'){
    if(!message) return;
    return fetch('order', {
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
 * Address Button Tiles Listeners
 */
Array.from(document.getElementsByClassName('address-button'))
                .forEach( function(btn, index, array){
    if(btn.id === 'add-address-btn') return;
    btn.addEventListener('click', function (){
        array.forEach( b => b.classList.remove('address-button-selected'));
        btn.classList.add('address-button-selected');
    }.bind(array));
});


/**
 * Input Payment Validation
 */
const payForm = document.getElementById('pay');

Array.from(payForm.getElementsByTagName('input')).forEach(input =>{
    let inputName = input.name;
    let pattern;
    let max;
    switch (inputName){
        case 'name':
            pattern = /[a-zA-ZÀ-ɏ' ]+/
            max = 60;
            break;
        case 'pan':
            max = 16;
            pattern = /[0-9]+/
            break;
        case 'cvv':
            pattern = /[0-9]+/
            max = 3;
            break;
        case 'expire':
            pattern = /[0-9/]+/
            max = 11;
            break;
        default:
            return;
    }
    input.addEventListener('input', function(e){
        if(!e.data) return;
        if(!pattern.test(e.data)) input.value = input.value.slice(0, input.value.length - e.data.length);
        if(input.value.length > max) input.value = input.value.slice(0, max);

    }.bind(pattern));
});



/**
 * Order Button Listener
 */
document.getElementById('order-button').addEventListener('click', async () =>{
    let form = new FormData(payForm);
    let message ='';
    form.forEach((value, key, parent )=>{
        message += key + '=' + value + '&';
    });

    let addDiv = document.getElementsByClassName('address-button-selected')[0];
    let idAdd =addDiv.getElementsByTagName('input')[0].value;

    message += 'address=' + idAdd + '&';
    message += 'order=true';

    let data = await ajax(message, 'text');
    alert(data);
});