
/* ========================== VALIDATION ON SUBMIT ====================================== */

/**
 * Validate Data Before Register Submit Listener
 */
document.forms.namedItem('register').addEventListener('submit', function(){
    let isValid = true;

    // Validate Each Field
    Array.from(this.elements).forEach(element =>{
        //Validate Pattern
        if(element.pattern){
            let pattern = new RegExp(element.pattern);

            if(!pattern.test(element.value)) {
                isValid = false;
                element.style.backgroundColor = '#77000090';
                const span = document.createElement('span');
                span.classList.add('invalid-credentials');
                element.parentElement.appendChild(span);
                span.innerHTML = "Campo non valido";
                setTimeout(()=>{
                    element.style.backgroundColor = '';
                    element.parentElement.removeChild(span)
                }, 4000);
            }
        }

        //Trim for cleaner data
        if(isValid) element.value = element.value.trim();
    });

    if(!isValid) return;

    //If it's all valid: Submit
    this.submit();
})

/* ======================= ON INPUT CHECKS (not all fields) ================================== */


/**
 * Keeps Data Valid for each specified field (Listeners)
 */
Array.from(document.forms.namedItem('register').elements).forEach(element =>{
    element.addEventListener('input', function (e){
        if(!e.data || element.type === 'submit') return;

        let pattern;
        switch (element.id){
            case 'road-type':
            case 'road-name':
            case 'city':
                pattern = /^[A-Za-zÀ-ÿ' -]+$/;
                break;
            case 'road-number':
            case 'cap':
                pattern = /^[0-9]+$/;
                break;
            case 'prov':
                pattern = /^[a-zA-Z]$/;
                break;
            default:
                return;
        }
        const maxlength = parseInt(element.getAttribute('maxlength'));
        if(pattern && !pattern.test(e.data)) element.value = element.value.slice(0, this.value.length - e.data.length);
        if(element.value.length > maxlength) element.value = element.value.slice(0, maxlength);
        if(element.id === 'prov') element.value = element.value.toUpperCase();
    });
});


/* ============================ PASSWORD FIELD ===================================== */

/**
 * Listener Validation for Password Field
 */
let passField = document.getElementById('pass-2');
passField.addEventListener('input', function (e){
    let pattern = /^(?=.*[a-zà-ÿ])(?=.*[A-ZÀ-Ÿ])(?=.*[!#@£$%&/()=?'^])(?=.*[0-9]).{8,}$/
    if(!pattern.test(passField.value)) this.style.backgroundColor = '#77000090';
    else passField.style.backgroundColor = '#00770090';
});


passField.addEventListener('focus', ()=> {
    let tooltip = document.getElementById('tooltip');
    tooltip.innerHTML = 'Più di 8 Caratteri con:' +
        '<ul><li>Almeno un carattere minuscolo</li>' +
        '<li>Almeno un Carattere Maiuscolo</li>' +
        '<li>Almeno 1 Numero</li>' +
        '<li>Almeno 1 Carattere Speciale: !£%&@</li></ul>'
    tooltip.style.display = "block";
    tooltip.style.position = 'absolute';
    tooltip.style.top = passField.offsetTop + passField.offsetHeight + 5 + "px";
    tooltip.style.left = passField.offsetLeft + "px";

})

passField.addEventListener('blur', () =>{
    let tooltip = document.getElementById('tooltip');
    tooltip.style.display = '';
})