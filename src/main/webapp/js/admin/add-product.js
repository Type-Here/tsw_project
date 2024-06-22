
/* =============================================== ADD PRODUCT FORM ================================================= */

/* ------- ADD PRODUCT FORM VALIDATOR AND FORMATTING ----- */
export function isEmpty(value) {
    return (value == null || (typeof value === "string" && value.trim().length === 0));
}


/**
 * Apply 'Add Product Form' Listeners in function below
 */
applyProductFormListeners();

/**
 * Apply 'Add Product Listeners'; called on default applies to document <br />
 * WHY? Has parameter and can be reused to reapply listeners in modify product!!
 * @param elementFrom default = document; used like: elementFrom.getElement(s)By....
 */
export function applyProductFormListeners(elementFrom = document) {

    /* INPUT Physic 0, Digital 1 */
    const typeAddProd = elementFrom.getElementsByClassName('type-prod');
    /* NB DIV with checkbox and quantity together*/
    const conditionsAdd = elementFrom.getElementsByClassName('condition-input');
    /*Get Categories INPUT */
    const categoriesAdd = elementFrom.getElementsByClassName('category-input');


    /* Physic Type */
    typeAddProd[0].addEventListener('click', () => {
        Array.from(conditionsAdd).forEach(cond => {
            Array.from(cond.children).forEach(c => {
                c.children[0].checked = false;
                c.children[0].disabled = false
            });
        });
        Array.from(conditionsAdd[0].children).forEach(c => {
            c.children[0].disabled = true
        });
    });

    /* Digital Type */
    typeAddProd[1].addEventListener('click', () => {
        Array.from(conditionsAdd).forEach(cond => {
            Array.from(cond.children).forEach(c => {
                c.children[0].disabled = true
            });
        });
        Array.from(conditionsAdd[0].children).forEach(c => {
            c.children[0].checked = false;
            c.children[0].disabled = false
        });
    });

    /**
     * On Input Listener for Product Key
     */
    elementFrom.getElementsByClassName('key-prod')[0].addEventListener('input', function (e) {
        if (!e.data) return;
        const pattern = /[a-zA-Z0-9]+/
        if (!pattern.test(e.data)) this.value = this.value.slice(0, this.value.length - e.data.length);
        if (this.value.length > 15) this.value = this.value.slice(0, 15);
    });
    /**
     * Price and Discount Input Field Listeners
     */
    elementFrom.getElementsByClassName('price-prod')[0].addEventListener('input', function (e) {
        floatInputValidate(e, this)
    });
    elementFrom.getElementsByClassName('discount-prod')[0].addEventListener('input', function (e) {
        floatInputValidate(e, this)
    });

    /**
     * Function for Price and Discount Listeners. Validate input data
     * @param event input event
     * @param element HTMLElement to validate
     */
    function floatInputValidate(event, element) {
        if (!event.data) return;
        const pattern = /[0-9,.]+/
        if (!pattern.test(event.data)) element.value = element.value.slice(0, element.value.length - event.data.length);
        if (element.maxLength && element.value.length > parseInt(element.maxLength)) element.value = element.value.slice(0, parseInt(element.maxLength));
        if (element.max && element.value > parseInt(element.max)) element.value = parseInt(element.max);
    }

    /**
     * Quantities Validation Listeners
     */
    Array.from(elementFrom.getElementsByClassName('quantity-prod')).forEach(element => {
        element.addEventListener('input', function (e) {
            if (!e.data) return;
            const pattern = /[0-9]+/
            if (!pattern.test(e.data)) element.value = element.value.slice(0, element.value.length - e.data.length);
            if (element.value > 100) element.value = 100;
        });
    })


    /* ----------- VALIDATE FORM ON SUBMIT ---------- */

    //NB : Applies only to 'Add Product Form'
    if(elementFrom === document){
        document.getElementById('add-form').addEventListener("submit", function (e) {

            //Stop Submit
            e.preventDefault();

            if (validationForm(categoriesAdd, conditionsAdd)) {
                document.getElementById('add-form').submit();
            }
        });
    }
}

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