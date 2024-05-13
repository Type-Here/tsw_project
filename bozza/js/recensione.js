
let stars = document.querySelectorAll('.star');

stars.forEach(star => {
    star.addEventListener('click', function() {
        // Remove star-off class and add star-on class
        this.classList.remove('star-off');
        this.classList.add('star-on');

        // Update the value of the select input
        let value = this.getAttribute('data-value');
        document.getElementById('valutazione').value = value;

        // Update other stars
        stars.forEach(s => {
            if (s.getAttribute('data-value') > value) {
                s.classList.remove('star-on');
                s.classList.add('star-off');
            } else {
                s.classList.remove('star-off');
                s.classList.add('star-on');
            }
        });
    });
});


//NON FUNZIONA
// Call the function initially to set the width
updateTextareaWidth();

// Add a resize event listener to the window
window.addEventListener('resize', updateTextareaWidth);
document.querySelector('.recensione-form-textarea textarea').addEventListener('mouseover', checkTextareaWidth);
document.querySelector('.recensione-form-textarea textarea').addEventListener('mouseover', updateTextareaWidth);

// Function to update the width of the textarea
function checkTextareaWidth() {
    // Select the recensione-container
    var recensioneContainer = document.querySelector('.recensione-container');

    // Get the current width of the recensione-container
    var containerWidth = recensioneContainer.offsetWidth;

    // Select recensione-form-textarea textarea
    var textarea = document.querySelector('.recensione-form-textarea textarea');

    // Set the width of recensione-form-textarea textarea equal to the width of recensione-container
    if (textarea.offsetWidth >= containerWidth - 85) {
        textarea.style.resize = 'vertical';
    } else {
        textarea.style.resize = 'auto';
    }

}

// Function to update the width of the textarea
function updateTextareaWidth() {
    // Select the recensione-container
    var recensioneContainer = document.querySelector('.recensione-container');

    // Get the current width of the recensione-container
    var containerWidth = recensioneContainer.offsetWidth;

    // Select recensione-form-textarea textarea
    var textarea = document.querySelector('.recensione-form-textarea textarea');

    // Set the width of recensione-form-textarea textarea equal to the width of recensione-container
    textarea.style.width = containerWidth - 85 + 'px';

}