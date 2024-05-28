/* ===== DOCUMENT FOR PRODUCT PAGE ===== */


        /* --- USE OF PROMISE --- */
/**
 * Condition button listeners
 */
Array.from(document.getElementsByClassName('prod-condition-button')).forEach( (button, index, array) =>{
    button.addEventListener('click', function (){

        const id_cond = button.value;
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