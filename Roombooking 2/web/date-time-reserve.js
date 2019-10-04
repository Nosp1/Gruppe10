/*
TODO: Endre reservering til noe a-la html'en under, og link til denne .js filen for funksjonalitet
    <div>
    Date 1: <input type="date" id="date" onfocusout="MyFunc()">
	    	<input type="time" id="start" onfocusout="Add2Hours()"><br>
    Date 2: <input type="date" id="out" disabled>
	    	<input type="time" id="end"
    </div>
 */

function MyFunc() {
    var date = document.getElementById("date");
    var c = date.value;
    var d = c.substring(0, 10);
    document.getElementById("out").value = d;
}

function Add2Hours() {
    document.getElementById("end").value = document.getElementById("start").value;
    document.getElementById("end").stepUp(120);
}