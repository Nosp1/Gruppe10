/**
 * Changes the clicked box's border-color
 */
$(document).ready(function() {
    $("body").on("click", function (event) {
        if(event.target != $('.room-card')[0]) {
            alert('You clicked the body!');
        }
    });
    $(".room-card").on("click", function() {
        $(".room-card").not(this).removeClass("card-clicked");
        console.log("Changed color");
        $(this).toggleClass("card-clicked");
    });
});


