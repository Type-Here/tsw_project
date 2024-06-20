/* ===== DOCUMENT FOR PRODUCT PAGE ===== */

function getIdProdFromURL(){
    const params = new URLSearchParams(window.location.search);
    return params.get('id_prod');
}

        /* --- USE OF PROMISE --- */
/**
 * Condition Button Listeners
 * AJAX call (use Promises) to retrieve price of a specified condition.
 */
Array.from(document.getElementsByClassName('prod-condition-button')).forEach( (button, index, array) =>{
    button.addEventListener('click', function (){

        //button.value has the id_cond
        for(let but of array){
            but.classList.remove('active-button');
        }
        button.classList.add('active-button');

        //Return if Product is Digital!!
        if(button.value === '0') return;

        const id_prod = getIdProdFromURL();

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


/* ---- ADD TO CART ---- */
/* --- USE OF ASYNC/AWAIT --- */

/**
 * Add to Cart Button Listener
 */

document.getElementById('add-to-cart').addEventListener('click',()=>{
    const counter = document.getElementById('cart-counter');
    let val = counter.innerHTML;

    const id_prod = getIdProdFromURL();
    const id_condition = document.querySelector('.prod-condition-button.active-button').value;
    addToCart(id_prod, id_condition)
        .then(r => {if(r.ok) return r.text(); else throw new Error(r.status + ' ' + r.statusText)})
        .then(async data => {
            let itemNum = await data;
            if (itemNum && itemNum === '1') {
                counter.innerHTML = (parseInt(val) + 1).toString();
                counter.classList.remove('general-display-none');
            }
        }).catch(r => console.log(r));
});


/**
 * Add to Cart Async Function
 * @param prod id of the product
 * @param condition id cf the condition
 * @returns {Promise<Response>}
 */
async function addToCart(prod, condition){
    if(!prod || !condition){
        console.error("No Enough Data to Add");
        return null;
    }
    return fetch('cart', {
        headers: {
            "Content-type": "application/x-www-form-urlencoded",
            'X-Requested-With': 'XMLHttpRequest',
        },
        method: 'POST',
        body: 'option=addToCart&condition=' + condition + '&id_prod=' + prod
    });
}


/**
 * Listener for One-Click Button
 */
document.getElementById('one-click').addEventListener('click', async () =>{
    //Simulate a Click on Add To Cart Button
    document.getElementById('add-to-cart').click();

    //Redirect
    //window.location.href = 'order';
    setTimeout( () =>{
        window.location.replace("order");
    }, 200);
});




/* ===================================== GALLERY JS ================================================= */

//section#gallery
const gallerySection = document.getElementById('gallery');
//Arrows SVG elements
const arrows = gallerySection.getElementsByTagName('svg');
//Number of Images in Slideshow count 'img-container-front-slide' class elements
let imgNumber = gallerySection.getElementsByClassName('img-container-front-slide').length;
//Main Container inside #gallery section used as Window for Slideshow
const galleryWindow = gallerySection.getElementsByClassName('img-container-front')[0];

// Change CSS Variable to the effective number images in slideshow IMPORTANT
gallerySection.getElementsByClassName('img-container-front')[0].style.setProperty('--gallery-count', imgNumber.toString())

let imgContainerWidth = gallerySection.getElementsByClassName('img-container-front-inside')[0].clientWidth;
let slideWidth = Math.round(imgContainerWidth / imgNumber);


/**
 * Recalculate Variables on Window Resizing
 * Reset ScrollPosition to 0 avoiding scroll offset while resizing
 */
window.addEventListener('resize', function(event) {
    imgContainerWidth = gallerySection.getElementsByClassName('img-container-front-inside')[0].clientWidth; //Recalculate
    slideWidth = Math.round(imgContainerWidth / imgNumber); //Recalculate
    galleryWindow.scrollTo({top: '0', left: 0, behavior:"instant"}); //Reset Scroll to 0
}, true);


/**
 * AutoScroll Gallery Object
 */
let scrollTimer = {
    timeout:5000,
    timer:null, //Variable handler
    setTimer: function (){ //Set New Timer
        this.timer = setInterval(()=>{
            const slider = document.getElementById('slider');
            slider.style.animation = 'none';
            slider.style.mozAnimation = 'none';
            setTimeout(()=>{
                slider.style.animation = '';
                slider.style.mozAnimation = '';
            }, 10);
            let event = new Event("AnimationEvent");
            arrows[1].dispatchEvent(event);
        }, this.timeout)
    },
    reset: function (){ //Reset Timer
        clearInterval(this.timer);
        this.setTimer();
    }
};
scrollTimer.setTimer();


// Right Arrow Listener
function moveRight () {
    let scrollPosition = galleryWindow.scrollLeft;
    let newPosition = scrollPosition + slideWidth;
    if (scrollPosition + slideWidth >= imgContainerWidth) newPosition = 0;
    galleryWindow.scrollTo({top: '0', left: newPosition, behavior: 'smooth'});
    scrollTimer.reset();
}
arrows[1].addEventListener('click', moveRight);
arrows[1].addEventListener('AnimationEvent', moveRight);


// Left Arrow Listener
arrows[0].addEventListener('click', function (){
    let scrollPosition = galleryWindow.scrollLeft;
    let newPosition = scrollPosition - slideWidth;
    console.log(imgNumber, imgContainerWidth, slideWidth, newPosition);
    if(newPosition < 0) newPosition = imgContainerWidth - slideWidth;
    galleryWindow.scrollTo({top:'0', left:newPosition, behavior:'smooth'});
    scrollTimer.reset();
});