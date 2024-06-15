/**
 * If user is logged add Listeners to Menu Icon and Displayable Div
 */

/**
 * Show/Hide menu on User Icon Click
 */
document.getElementsByClassName('userLoggedIcon')[0].addEventListener('click', () =>{
    const userMenu = document.getElementById('user-menu-div');
    if(userMenu.classList.contains('general-display-none')){
        userMenu.classList.remove('general-display-none');
        userMenu.focus({preventScrollable:true, focusVisible:false}); //Options valid, ignore ide warnings
    } else {
        userMenu.classList.add('general-display-none');
    }
});

/**
 * Hide menu div on Focus Out
 */
document.getElementById('user-menu-div').addEventListener('blur', function (event){
    // if the blur was caused by outside:
    // currentTarget is the parent element
    // relatedTarget is the clicked element
    //Ignore if Icon Element is clicked (icon must be focusable -- added tabindex="0" as attribute
    if (!event.target.contains(event.relatedTarget) &&
        event.relatedTarget !== document.getElementsByClassName('userLoggedIcon')[0]) {
        this.classList.add('general-display-none');
    }
});