/* --- SUBMENUS OPEN AND CLOSE --- */
let filterTitles = document.getElementsByClassName("filter-title");
Array.from(filterTitles).forEach(title => {
    title.addEventListener('click', function() {
        
        //Get filter-list element 
        const element = this.parentNode.children[2];
        
        if(element.style.display == 'none'){
            title.children[1].children[0].style.transform = "rotate(-90deg)";
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

    if(this.value != 0){
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

    if(this.value != this.max){
        /*let reset = document.getElementById('remove-filter');
        reset.style.display = 'block';*/
        displayRemoveFilter();
    }

    let priceValues = document.getElementsByClassName('price-value');
    priceValues[1].innerHTML = ' ' + this.value + ' &euro;'
}



/* --- RESET FILTERS BUTTON --- */

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
    this.removeAttribute('class', 'animation');
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