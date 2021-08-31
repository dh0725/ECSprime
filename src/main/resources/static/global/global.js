/* Home */
function goEnHome() {
    window.location.href = "../en/main.html"
}
function goKoHome() {
    window.location.href = "../ko/main.html"
}


/* change language */
function goToKorean() {
    window.location.href = "../ko/main.html"
}
function goToEnglish() {
    window.location.href = "../en/main.html"
}


/* hand shape click tooltip */
function showTooltip(id) {
    var helloImg = document.getElementById(id);
    helloImg.classList.add('active');
    console.log(helloImg);
}
function hideTooltip(id) {
    var byeImg = document.getElementById(id);
    byeImg.classList.remove('active');
    console.log(byeImg);
}


/* responsive menu bars */
function toggleMenu() {
    var helloMenu = document.getElementById('responsive-background')
    if (helloMenu.classList.contains('active-menu')) {
        helloMenu.classList.remove('active-menu');
    } else {
        helloMenu.classList.add('active-menu');
    }
    console.log(helloMenu);
}


/* background */
function hideToggleMenu(id) {
    var byeMenu = document.getElementById(id);
    byeMenu.classList.remove('active-menu');
    // console.log(byeMenu);
}