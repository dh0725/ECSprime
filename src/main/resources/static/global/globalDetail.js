/* GoToTop GoToBottom*/
$(function() {
    $(window).scroll(function() {
        if ($(this).scrollTop() > 100) {
            $('#btnToTop').fadeIn();
            $('#btnToBottom').fadeIn();
        } else {
            $('#btnToTop').fadeOut();
            $('#btnToBottom').fadeOut();
        }
    });
    $("#btnToTop").click(function() {
        $('html, body').animate({
            scrollTop: 0
        }, 100);
        return false;
    });
    $("#btnToBottom").click(function() {
        $('html, body').animate({
            scrollTop: $('#footer-container').offset().top
        }, 100);
    });
});