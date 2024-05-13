
const progressBar = document.getElementsByClassName('progress-bar')[0]



let interval;



function showPopup() {
    document.getElementById("popup").style.display = "block";

    // Start the progress bar when the popup is shown
    interval = setInterval(() => {
        const computedStyle = getComputedStyle(progressBar)
        const width = parseFloat(computedStyle.getPropertyValue('--width')) || 0
        if (width <= 100.00) {
            progressBar.style.setProperty('--width', width + .1)
            progressBar.setAttribute('background-position', (100 - width) + '% center')
        } else {
            clearInterval(interval)
            setTimeout(() => {
                window.location.href = "index.html"
            }, 777); //1000ms = 1s
        }
    }, 7)
}

function hidePopup() {
    document.getElementById("popup").style.display = "none";


}

// Hide the popup immediately when the page loads
hidePopup();

// Ascolta il click sul bottone
document.getElementById("btn").addEventListener("click", function ()    {
    // Show the popup after 2 seconds
    setTimeout(showPopup, 888);
});


