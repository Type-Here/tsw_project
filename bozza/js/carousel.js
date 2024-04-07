/**
 * When a thumbnail is selected, change main image slide to selected item;
 * Change update active-thumbnail class to selected thumbnail
 * @param {*} caller HTMLInputElement or HTMLLabelElement
 */
function carousel_changeimg(caller){
    if(!(caller instanceof HTMLInputElement)){
        return;
    }

    // Unset old thumbnail
    const old_thumb = document.getElementsByClassName('active-thumbnail')[0];
    if(old_thumb != null){
        old_thumb.classList.remove('active-thumbnail');
    }

    // Get number of clicked thumbnail
    let slide_number = parseInt(caller.id.slice(-1));
    /* Set new active thumbnail */
    caller.labels[0].childNodes[0].classList.add('active-thumbnail'); 
    
    /* Beahiour on < 700 width: scroll container to correct position of slide */
    if(screen.width < 700){
        const slide_width = document.getElementsByClassName('carousel-slide')[0].offsetWidth;
        document.getElementsByClassName('carousel')[0].scrollLeft = slide_width * (slide_number-1);
    }
    
    // Set active slide
    const slides = document.getElementsByClassName('carousel-slide');
    Array.from(slides).forEach(element => {
        element.classList.remove('active-slide');
    });
    slides[slide_number - 1].classList.add('active-slide');

    // Set active radio-button (automatic when clicking on radiobutton label)
    //document.getElementById('carousel-slide-' + parseInt(slide_number)).checked = true;   
}

/*Body On Load Function */
function prepare_function(){
    
    /**
     * Sync Radio Button to .carousel container Scroll View; 
     * added to boby on load 
     */
    document.getElementsByClassName('carousel')[0].addEventListener("scroll", function(){
        const slide_width = document.getElementsByClassName('carousel-slide')[0].offsetWidth;
        const scroll_num = Math.round(this.scrollLeft / slide_width) + 1;
        document.getElementById('carousel-slide-' + scroll_num).checked=true;
    });
}
