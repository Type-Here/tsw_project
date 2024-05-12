/* Close menu on overlay click scree-width < 700 px */
function overlay_menu(){
    document.getElementById('nav').checked = false;
}


/* Close Popup Info */
function hidePopup(){
    document.getElementsByClassName("overlay-popup")[0].style.display = 'none';
    document.getElementsByClassName("info-popup")[0].style.display = 'none';
}