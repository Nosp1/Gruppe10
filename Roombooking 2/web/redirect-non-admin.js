$(document.head).ready(function() {
    var cookies = document.cookie;
    console.log(cookies);
    if(!cookies.includes("ADMIN")) {
        console.log("Attempted log in from non-admin");

        if(!cookies.includes("user_type")) {
            // User is not logged in
            try {
                console.log("not logged in");
                window.location.replace("http://localhost:8080/index.html");
            } catch(e) {
                window.location = "http://localhost:8080/index.html";
            }
        } else {
            try {
                console.log("not an admin");
                window.location.replace("http://localhost:8080/loggedIn.html");
            } catch(e) {
                window.location = "http://localhost:8080/loggedIn.html";
            }
        }
    }
});