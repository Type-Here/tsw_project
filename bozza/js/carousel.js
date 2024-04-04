/**
 * When a thumbnail is selected, change main image slide to selected item;
 * Change update active-thumbnail class to selected thumbnail
 * @param {*} caller 
 */
function carousel_changeimg(caller){
    if(!caller instanceof HTMLLabelElement){
        return;
    }

    const slides = document.getElementsByClassName('carousel-slide');
    const slide_number = caller.htmlFor.slice(-1);

    Array.from(slides).forEach(element => {
        /*element.style.display = "none";*/
        element.classList.remove('active-slide');
    });
    slides[parseInt(slide_number) - 1].classList.add('active-slide');

    const old_thumb = document.getElementsByClassName('active-thumbnail')[0];
    if(old_thumb != null){
        old_thumb.classList.remove('active-thumbnail');
    }
    caller.childNodes[0].classList.add("active-thumbnail");
}