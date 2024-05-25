/* ------- ADD PRODUCT FORM VALIDATOR AND FORMATTING ----- */
function isEmpty(value) {
    return (value == null || (typeof value === "string" && value.trim().length === 0));
}

/* INPUT Physic 0, Digital 1 */
const typeAddProd= document.getElementsByClassName('type-prod');
/* NB DIV with checkbox and quantity together*/
const conditionsAdd = document.getElementsByClassName('condition-input');
/*Get Categories INPUT */
const categoriesAdd = document.getElementsByClassName('category-input');

/* Physic Type */
typeAddProd[0].addEventListener('click', () =>{
    Array.from(conditionsAdd).forEach(cond =>{
        Array.from(cond.children).forEach( c =>{ c.children[0].checked = false; c.children[0].disabled = false});
    });
    Array.from(conditionsAdd[0].children).forEach( c =>{ c.children[0].disabled = true});
});

/* Digital Type */
typeAddProd[1].addEventListener('click', () =>{
    Array.from(conditionsAdd).forEach(cond =>{
        Array.from(cond.children).forEach( c =>{ c.children[0].disabled = true});
    });
    Array.from(conditionsAdd[0].children).forEach( c =>{ c.children[0].checked = false; c.children[0].disabled = false});
});


/* ----------- VALIDATE FORM ON SUBMIT ---------- */
document.getElementById('add-form').addEventListener("submit", function(e){

    //Stop Submit
    e.preventDefault();

    if(validationForm(categoriesAdd, conditionsAdd)){
        document.getElementById('add-form').submit();
    }
});


/* Check for:
     * Checked at least 1 Category
     * Checked at least 1 Condition + Set its Quantity
     */
function validationForm(categories, conditions){
    return Array.from(categories).find( cat =>{ return cat.checked === true }) !== undefined
    && Array.from(conditions).find(cond => {
        return cond.children[0].children[0].checked === true //Condition
            && (cond.children[1] === undefined || //Bypass if Digital
                !isEmpty(cond.children[1].children[0].value)) //Quantity
    })  !== undefined;
}