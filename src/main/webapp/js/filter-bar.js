/* --- SUBMENUS OPEN AND CLOSE --- */
let filterTitles = document.getElementsByClassName("filter-title");
Array.from(filterTitles).forEach(title => {
    title.addEventListener('click', function() {
        
        //Get filter-list element 
        const element = this.parentNode.children[2]; //Equals to div.filter-list of this submenu

        if(element.style.display.length === 0|| element.style.display === 'none'){
            title.children[1].children[0].style.transform = "rotate(-90deg)"; //Arrow
            element.style.display = 'flex';
            element.style.visibility = ' hidden';
            setTimeout(() => {
                element.style.visibility = 'visible';
                element.style.height = 'fit-content';
            }, 20)

        } else {
            title.children[1].children[0].style.transform = "rotate(90deg)";
            element.style.height = '0';
            setTimeout(() => {
                element.style.display = 'none';
            }, 20)
        }
        
    })
});




/* --- PRICE SLIDER --- */
let inputRanges = document.getElementsByClassName('price-range');
//Set Min Price
inputRanges[0].oninput = function(){
    //If min goes >= of max range set it to max-range - 1
    let max = parseInt(inputRanges[1].value, 10);
    if(this.value >= max){
        this.value = max - 1;
    }

    if(this.value !== 0){
        let reset = document.getElementById('remove-filter');
        reset.style.display = 'block';
    }

    let priceValues = document.getElementsByClassName('price-value');
    priceValues[0].innerHTML = ' ' + this.value + ' &euro;' 
}

//Set Max Price
inputRanges[1].oninput = function(){
    //If max goes <= of min range set it to min-range + 1
    let min = parseInt(inputRanges[0].value, 10);
    if(this.value <= min){
        this.value = min + 1;
    }

    if(this.value !== this.max){
        /*let reset = document.getElementById('remove-filter');
        reset.style.display = 'block';*/
        displayRemoveFilter();
    }

    let priceValues = document.getElementsByClassName('price-value');
    priceValues[1].innerHTML = ' ' + this.value + ' &euro;'
}



/* --- RESET FILTERS BUTTON: REMOVES ONLY FILTERS IN BAR NO FROM RESULTS (SEE RESET FILTER BELOW) --- */

/* ---- Function to Animate Remove Filters Button Display ----- */

//1. Display Remove Filter Button 
//2. Enable Apply Button
function displayRemoveFilter(){
    const removeButton = document.getElementById('remove-filter');
    removeButton.style.display = 'block';

    const apply = document.getElementById('apply-filter');
    apply.disabled = false;

    setTimeout(() => {
        removeButton.style.visibility = 'visible';
        removeButton.setAttribute('class', 'animation');
    }, 50);
}


/* Add Reset Filter Button Click Actions */
let resetButton = document.getElementById('remove-filter');
resetButton.addEventListener('click', function(){
    
    //Reset Platform Filter
    const radioPlat = document.querySelectorAll('.platform-filter input[type=radio]');
    Array.from(radioPlat).forEach(radio => {
        radio.checked = false;
    });

    //Reset Category Filter
    const checkCat = document.querySelectorAll('.category-filter input[type=checkbox]');
    Array.from(checkCat).forEach(check => {
        check.checked = false;
    });

    //Reset Developer Filter
    const radioDev = document.querySelectorAll('.dev-filter input[type=radio]');
    Array.from(radioDev).forEach(radio => {
        radio.checked = false;
    });

    //Reset Price Filters
    const prices = document.getElementsByClassName('price-range');
    prices[0].value = 0;
    prices[1].value = prices[1].max;

    //Disable Apply Button
    const apply = document.getElementById('apply-filter');
    apply.disabled = true;
    //this.removeAttribute('class', 'animation');
    setTimeout(() =>{
        this.style.display = 'none';
    }, 500);
});


/* Add RadioButton Listener: When clicked display 'X' to remove filter */
let radioButtons = document.querySelectorAll('input[type=radio]');
radioButtons.forEach(radio => {
    radio.addEventListener('click', function(){
        displayRemoveFilter();
    }) ;
});

/* Add CheckBox Listener: When clicked display 'X' to remove filter */
let checkBoxes = document.querySelectorAll('input[type=checkbox]');
checkBoxes.forEach(check => {
    check.addEventListener('click', () =>{
        displayRemoveFilter();
    }) ;
});



/* ========= MOBILE FILTER BUTTON =========== */
document.getElementById('filter-mobile').addEventListener('click', () =>{
    document.getElementsByClassName('overlay')[0].setAttribute('class', 'overlay show');
    document.getElementsByClassName('filter-bar')[0].classList.add('filter-bar-showdown');
});

/* Overlay Click removes filter bar and overlay */
document.getElementsByClassName('overlay')[0].addEventListener('click', () =>{
    document.getElementsByClassName('filter-bar')[0].classList.remove('filter-bar-showdown');
    document.getElementsByClassName('overlay')[0].setAttribute('class', 'overlay');
});

/* Click on hide removes filter bar and overlay */
document.getElementById('hide-filter').addEventListener('click', () =>{
    document.getElementsByClassName('filter-bar')[0].classList.remove('filter-bar-showdown');
    document.getElementsByClassName('overlay')[0].setAttribute('class', 'overlay');
});



/* ===================== APPLY BUTTON ================================== */

/* ------------ XML-HTTP-REQUEST WHEN APPLY FILTER BUTTON IS CLICKED --------------- */
document.getElementById('apply-filter').addEventListener("click", () => {

    //Set Query Object with parameter data
    let queryObj = {platform: '', dev: '', minprice: '', maxprice: ''};
    queryObj.platform = document.querySelector('.platform-filter input[type=radio]:checked');
    queryObj.dev = document.querySelector('.dev-filter input[type=radio]:checked');
    queryObj.minprice = document.getElementsByClassName('price-range')[0];
    queryObj.maxprice = document.getElementsByClassName('price-range')[1];
    let categories = document.querySelectorAll('.category-filter input[type=checkbox]:checked');

    let query = '';

    // Set Parameters on GET Request as a Query String
    let filterNum = 0;
    for (const [key, htmlElement] of Object.entries(queryObj)) {
        if (htmlElement == null || htmlElement.value == null) continue; //If Object or its value is null, jump to next
        if (key === 'minprice' && htmlElement.value === '0') continue; //If min price is 0 ignore parameter
        if (key === 'maxprice' && htmlElement.value === queryObj.maxprice.max) continue; //If max price is equal to max ignore parameter
        if (htmlElement.value.length > 0) { //Set parameters if present
            query = query + key + '=' + htmlElement.value + '&';
            filterNum++;
        }
    }

    //For Categories, set all Applied
    Array.from(categories).forEach( input =>{
        query = query + 'category=' + input.value + '&';
        filterNum++;
    });

    //IF query has length > 0 (some filter is applied) send AJAX
    if (query.length > 0) {
        ajaxUpdateCatalog(filterNum, query);
        //Show Reset Button
        document.getElementById('reset-filter').setAttribute('class', 'active');
    }
});



/* ====== RESET BUTTON: CLEAR ALL FILTERS FROM RESULTS ======= */
document.getElementById('reset-filter').addEventListener('click' , function(){
    ajaxUpdateCatalog(0, null);
    this.classList.remove( 'active');
    // Remove Filters Visually
    document.getElementById('remove-filter').click();
});


/* ==================== SET RESET FILTER BUTTON AND FILTER NUMBER AT THE PAGE (RE)LOAD FROM USER ==================== */
window.addEventListener('load' , function(){
   //Get all url from '?'
    let url = window.location.search;
    //Map of Key=Value in URL
    const urlParams = new URLSearchParams(url);
    //Iterating over parameters
    for(const key of urlParams.keys()){
        //If there are any filters (other than page) set Reset CLick Active (to remove them)
        if(key !== 'page') document.getElementById('reset-filter').setAttribute('class','active');
        //Display the remove button (to deselect them only on filter bar)
        displayRemoveFilter();
        //Set the number of filters as the number of paragraph
        document.getElementById("filter-number").innerHTML = 'Filtri(' + urlParams.size + ')';
    }
});

/* ================ AJAX FUNCTION =================== */

//SEND AJAX
function ajaxUpdateCatalog(filterNum, query){
    let xhr = new XMLHttpRequest(); // Creare una richiesta XML HTTP
    let url = 'store'
    if(query){
        url = 'store?' + query;
    }

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) { //if no error from server: set new DATA

            //Update Results
            document.getElementsByClassName('container')[0].innerHTML = xhr.responseText;

            //Update Filter Number
            document.getElementById("filter-number").innerHTML = 'Filtri(' + filterNum.toString() + ')';

            //Update URL and history on browser
            window.history.pushState("data", "Filter", url);


            //Update Prev - This - Next Page Buttons
            const buttons = [];
            buttons[0] = document.getElementById('prev-page');
            buttons[1] = document.getElementById('this-page');
            buttons[2] = document.getElementById('next-page');

            for(let i = 0; i < buttons.length; i++){
                if(buttons[i] && query){
                    buttons[i].href +='&' + query;
                }
            }


        }
    };

    //Send Request
    xhr.open("GET", url, true); // Open new GET Request
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
    xhr.send(); // Send Request
}

/*
* let responseJson = JSON.parse(xhr.responseText); // Analizzare la risposta JSON
                for (let key in responseJson) {
                    if (Object.hasOwn(responseJson, key)) {
                        alert(key + ":" + responseJson[key]);
                    }
                }
* */