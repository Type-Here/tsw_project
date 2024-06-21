/**
 * AJAX to order page
 * @param message post body
 */
async function ajax(message){
    if(!message) return;
    return fetch('order', {
        headers:{
            "Content-type": "application/x-www-form-urlencoded",
            'X-Requested-With': 'XMLHttpRequest',
        },
        method:'POST',
        body: message
    });
}

/* ===================== ADDRESS SELECTOR BUTTONS =================== */

/**
 * Address Button Tiles Listeners
 */
Array.from(document.getElementsByClassName('address-button'))
                .forEach( function(btn){
    if(btn.id === 'add-address-btn') return;
    btn.addEventListener('click', function (){

        //Don't use array bind since elements can change! (i.e. adding a new address)
        Array.from(document.getElementsByClassName('address-button'))
            .forEach( b => b.classList.remove('address-button-selected'));
        btn.classList.add('address-button-selected');
    });
});

/* =================================== PAYMENT VALIDATION =============================================== */


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
            pattern = /[a-zA-ZÀ-ɏ&' ]+/
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

/* ============================================== DO ORDER ==================================== */

/**
 * Order Button Listener
 * Collect Data from user (address and payment details) <br />
 * Send Purchase order to server. <br />
 * Show Status Popup <br />
 */
document.getElementById('order-button').addEventListener('click', async () =>{
    let isValid = true;
    //Validate Data
    Array.from(payForm.elements).forEach(element =>{
        if(element.pattern){
            let pattern = new RegExp(element.pattern);

            if(!element.value || !pattern.test(element.value)) {
                isValid = false;
                element.style.animation = '0.2s ShakyShaky 6';
                const span = document.createElement('span');
                span.classList.add('invalid-credentials');
                element.parentElement.appendChild(span);
                span.innerHTML = "Campo non valido";
                setTimeout(()=>{ element.parentElement.removeChild(span)}, 4000);
            }
        }
    });

    if(!isValid) return;

    let form = new FormData(payForm);
    let message = '';
    form.forEach((value, key, parent) => {
        message += key + '=' + value + '&';
    });

    let addDiv = document.getElementsByClassName('address-button-selected')[0];
    let idAdd = addDiv.getElementsByTagName('input')[0].value;

    message += 'address=' + idAdd + '&';
    message += 'order=true';

    let response = await ajax(message);
    let data = await response.text();
    if (response.ok) {
        doPresent(data);

    } else {
        let span = document.getElementById('order-error-span');
        if(!span){
            span = document.createElement('span');
            span.id = 'order-error-span';
            span.classList.add('invalid-credentials');
            document.getElementsByClassName('cart-overview')[0].appendChild(span);
        }
        span.innerHTML = data;
    }
});


/**
 * Presents a Popup to imitate a Payment. Then shows data as text.
 * Last Redirects to Index Page
 * @param data message from server to show
 */
function doPresent(data){
    const progressBar = document.getElementsByClassName('progress-bar')[0]
    let interval;
    document.getElementById("popup").classList.remove('general-display-none');
    document.getElementById("popup").classList.add('general-display-flex');

    // Start the progress bar when the popup is shown
    interval = setInterval(() => {
        const computedStyle = getComputedStyle(progressBar)
        const width = parseFloat(computedStyle.getPropertyValue('--width')) || 0
        if (width <= 100.00) {
            progressBar.style.setProperty('--width', width + .1)
            progressBar.setAttribute('background-position', (100 - width) + '% center')
        } else {
            clearInterval(interval)
            const message = document.createElement('span');
            message.innerHTML = data;
            message.classList.add('message-ok');

            document.getElementsByClassName('popup-body')[0].innerHTML = '';
            document.getElementsByClassName('popup-body')[0].appendChild(message);
            setTimeout(() => {
                window.location.href = "index"
            }, 2500); //1000ms = 1s
        }
    }, 7);
}



/* ============================================== ADD ADDRESS ================================================================= */

/**
 * Add Address Button Listener: On Click Show Add Address Section
 */
document.getElementById('add-address-btn').addEventListener('click', () =>{
    const section = document.getElementById('add-address');
    section.classList.remove('general-display-none');
    section.style.animation = 'Unfold 2s ease-out';
    section.scrollIntoView({behavior:'smooth'});
});




/**
 * Keeps Data in Add Address for Valid for each field (Listeners)
 */
Array.from(document.forms.namedItem('address-form').elements).forEach(element =>{
    element.addEventListener('input', function (e){
        if(!e.data ||element.type === 'submit') return;

        let pattern;
        switch (element.id){
            case 'road-type2':
            case 'road-name2':
            case 'city2':
                pattern = /^[A-Za-zÀ-ÿ' -]+$/;
                break;
            case 'road-number2':
            case 'cap2':
                pattern = /^[0-9]+$/;
                break;
            case 'prov2':
                pattern = /^[a-zA-Z]$/;
                break;
            default:
                return;
        }
        const maxlength = parseInt(this.getAttribute('maxlength'));
        if(!pattern.test(e.data)) this.value = this.value.slice(0, this.value.length - e.data.length);
        if(this.value.length > maxlength) this.value = this.value.slice(0, maxlength);
        if(this.id === 'prov2') this.value = this.value.toUpperCase();
    });
});

/**
 * Form Add Address, Validate and
 * Send Data AJAX to Add a New Shipping Address for the User
 */
document.forms.namedItem('address-form').addEventListener('submit', async function (e){
    e.preventDefault();
    // Info Message Span
    let span = document.getElementById('info-address-span');
    if(!span){
        span = document.createElement('span');
        span.classList.add('text-center-full-width');
        span.id = 'info-address-span';
        this.appendChild(span);
    }

    try{

        //Validate Data
        Array.from(this.elements).forEach(element =>{
            if(element.pattern){
                let pattern = new RegExp(element.pattern);
                if(!pattern.test(element.value)) {
                    throw new Error("Campo non valido: " + element.value);
                }
            }
        });

        //Get Input Values
        let data = new FormData(this);
        console.log(data);

        let message = 'section=four';
        for(const key of data.keys()){
            message += '&' + key + '=' + data.get(key).trim();
        }

        //AJAX
        let response = await fetch('modify-user', {
            headers:{
                "Content-type": "application/x-www-form-urlencoded",
                'X-Requested-With': 'XMLHttpRequest',
            },
            method:'POST',
            body: message
        });
        let responseData = await response.json();

        span.classList.add('success-info');
        span.innerHTML = "Indirizzo Aggiunto con Successo";

        //Create a New Div/Button for the New Address
        const newDiv = document.getElementsByClassName('address-button-selected')[0].cloneNode(true);
        newDiv.addEventListener('click', function () {
            Array.from(document.getElementsByClassName('address-button'))
                .forEach(b => b.classList.remove('address-button-selected'));
            this.classList.add('address-button-selected');
        });

        //Deselect old Address
        const addressSection = document.getElementById('address');
        Array.from(addressSection.children).forEach(div => {
            div.classList.remove('address-button-selected');
        })

        //Add new Div to Address Section
        addressSection.appendChild(newDiv);

        //Populate Address with thw new Data
        const newDivElement = document.getElementsByClassName('address-button-selected')[0];
        newDivElement.getElementsByTagName('input')[0].value = responseData['id_add'];
        newDivElement.getElementsByClassName('item-data')[0].innerHTML =
            '<li>' + responseData['address'] + '</li>' +
            '<li>' + responseData['city'] + ' ' + responseData['prov'] + '</li>' +
            '<li>' + responseData['CAP'] + '</li>';

        //Animate re-fold
        const addSection = document.getElementById('add-address');
        addSection.style.animation = '';

        setTimeout(() =>{
            addSection.style.animation = 'Unfold reverse 2s';
        },50)

        setTimeout(()=>{
            addSection.classList.add('general-display-none');
        },4000);

    } catch (e) {
        span.classList.add('invalid-credentials');
        span.innerHTML = e;
        this.appendChild(span);
    }

});
