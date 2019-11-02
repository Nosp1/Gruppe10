/**
 * Changes the clicked box's border-color
 */
$(document).ready(function() {
    $(".room-card").on("click", function() {
        $(".room-card").not(this).removeClass("card-clicked");
        console.log("Changed color");
        $(this).toggleClass("card-clicked");
    });
});


