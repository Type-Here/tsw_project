/* Close menu on overlay click scree-width < 700 px */
function overlay_menu(){
    document.getElementById('nav').checked = false;
}

/* Adapt Last Row of Tiles to Better Alignment */
function adaptMarginLastTile(){
    let tiles = document.getElementsByClassName('tile');
    let container = document.getElementsByClassName('main_home')[0];
    //If elements are mod3 == 2
    //Adapt last element margin to = 3 * marginLeft of first element + 1 tile width
    if(tiles.length > 0 && tiles.length % 3 === 2){
        console.log("Cont:" + container.offsetWidth);
        /*let margin2 = tiles[0].offsetWidth + parseInt(window.getComputedStyle(tiles[0]).marginLeft) * 3;*/
        let margin = tiles[0].offsetWidth + container.offsetWidth * 2.67/100 * 3;
        tiles[tiles.length - 1].style.marginRight =  margin + 'px';
        console.log("Margin: " + margin);
    } else {
        Array.from(tiles).forEach(t =>{t.style.removeProperty('margin-right');});
    }

}

window.addEventListener('load', () =>{
    adaptMarginLastTile();
});

window.addEventListener('resize', function(event) {
    adaptMarginLastTile();
}, true);

//Change the color of the current page in navbar
function  updateActiveNavElement(){
    let path = window.location.pathname;

    //all element a in the nav class
    let navItems = document.querySelectorAll('.nav li a');


    navItems.forEach(item => {

        let itemHref = item.getAttribute('href');

        //?????
        let contextPath = "${pageContext.request.contextPath}";
        let itemPath = itemHref.replace(contextPath, "");
        let currentPath = path.replace(contextPath, "");


        if(itemHref === path || itemPath === currentPath){
            //console.log("Adding this-page class to: " + item);
            if (!item.parentElement.classList.contains('this-page')){
                item.parentElement.classList.add('this-page');
            }
        } else {
            if (item.parentElement.classList.contains('this-page')) {
                item.parentElement.classList.remove('this-page');
            }
        }
    });
}

//Caricamento pagina
window.addEventListener('load', updateActiveNavElement);
//Cambia URL
window.addEventListener('popstate', adaptMarginLastTile);

/**
 * Function called by Hide Button and on click over overlay div to close Modify Popup
 */
function hidePopup(){
    const popupDiv = document.getElementById('modify-prod-popup');
    const formSection = document.getElementById('modify-form');
    const removeDiv = document.getElementById('remove-prod-popup');
    const infoPopup = document.getElementsByClassName('info-popup')[0];
    document.getElementsByClassName('overlay')[0].classList.remove( 'general-display-block');
    document.getElementsByClassName('overlay-popup')[0].classList.add( 'general-display-none');
    popupDiv.classList.add('general-display-none');
    if(infoPopup) infoPopup.remove();
    if(formSection) popupDiv.removeChild(formSection);
    if(removeDiv) popupDiv.removeChild(removeDiv);
}
