/* ------- ADD PRODUCT FORM VALIDATOR AND FORMATTING ----- */
function isEmpty(value) {
    return (value == null || (typeof value === "string" && value.trim().length === 0));
}

/* INPUT Physic 0, Digital 1 */
const typeProd= document.getElementsByClassName('type-prod');
/* NB DIV with checkbox and quantity together*/
const conditions = document.getElementsByClassName('condition-input');
/*Get Categories INPUT */
const categories = document.getElementsByClassName('category-input')

/* Physic Type */
typeProd[0].addEventListener('click', () =>{
    Array.from(conditions).forEach(cond =>{
        Array.from(cond.children).forEach( c =>{ c.children[0].checked = false; c.children[0].disabled = false});
    });
    Array.from(conditions[0].children).forEach( c =>{ c.children[0].disabled = true});
});

/* Digital Type */
typeProd[1].addEventListener('click', () =>{
    Array.from(conditions).forEach(cond =>{
        Array.from(cond.children).forEach( c =>{ c.children[0].disabled = true});
    });
    Array.from(conditions[0].children).forEach( c =>{ c.children[0].checked = false; c.children[0].disabled = false});
});

/* ----------- VALIDATE FORM ON SUBMIT ---------- */
document.getElementById('add-form').addEventListener("submit", function(e){

    //Stop Submit
    e.preventDefault();

    /* Check for:
     * Checked at least 1 Category
     * Checked at least 1 Condition + Set its Quantity
     */
    let isValid = Array.from(categories).find( cat =>{ return cat.checked === true }) !== undefined
                                && Array.from(conditions).find(cond => {
                                    return cond.children[0].children[0].checked === true //Condition
                                        && (cond.children[1] === undefined || //Bypass if Digital
                                            !isEmpty(cond.children[1].children[0].value)) //Quantity
                                    })  !== undefined;

    if(isValid){
        document.getElementById('add-form').submit();
    }
});