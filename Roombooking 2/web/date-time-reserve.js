/*
    <div>
    Date 1: <input type="date" id="date" onfocusout="MyFunc()">
	    	<input type="time" id="start" onfocusout="Add2Hours()"><br>
    Date 2: <input type="date" id="out" disabled>
	    	<input type="time" id="end"
    </div>

        <div>
            <label for="Reserve_Timestamp_start_date" for="Reserve_Timestamp_start_time">Booking start</label>
            <input type="date" id="Reserve_Timestamp_start_date" name="Reserve_Timestamp_start_date" onfocusout="updateDate2()"/>
            <input type="time" id="Reserve_Timestamp_start_time" name="Reserve_Timestamp_start_time" onfocusout="updateTimeEnd()"/>
        </div>
        <div>
            <label for="Reserve_Timestamp_end_date" for="Reserve_Timestamp_end_time">Booking end</label>
            <input type="date" id="Reserve_Timestamp_end_date" name="Reserve_Timestamp_end_date" disabled/>
            <input type="time" id="Reserve_Timestamp_end_time" name="Reserve_Timestamp_end_time">
        </div>
 */
/**
 * updateDate2 sets the date-field with id "out" to the same value as the first date-field.
 */
function updateDate2() {
    var date = document.getElementById("Reserve_Timestamp_start_date");
    // First we get the whole value from the first date-field.
    var c = date.value;
    // Then we substring the first 10 characters (yyyy-mm-dd),
    var d = c.substring(0, 10);
    // and set the second date-field to this value.
    document.getElementById("Reserve_Timestamp_end_date").value = d;
}

/**
 * updateTimeEnd takes the end-time and increments it by 2 hours.
 */
function updateTimeEnd() {
    document.getElementById("Reserve_Timestamp_end_time").value = document.getElementById("Reserve_Timestamp_start_time").value;
    // stepUp increments the minutes of a time-field by a set amount, in this case 120 minutes.
    document.getElementById("Reserve_Timestamp_end_time").stepUp(120);
}