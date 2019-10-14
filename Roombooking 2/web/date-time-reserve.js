/**
 * updateEndDate sets the date-field with id "out" to the same value as the first date-field.
 */
function updateEndDate() {
    var date = document.getElementById("Reserve_Timestamp_start_date");
    // First we get the whole value from the first date-field.
    var c = date.value;
    // Then we substring the first 10 characters (yyyy-mm-dd),
    var d = c.substring(0, 10);
    // and set the second date-field to this value.
    document.getElementById("Reserve_Timestamp_end_date").value = d;
}

function checkInputEnd(event) {
    checkInputRangeEnd(event);
}

/**
 * updateTimeEnd takes the end-time and increments it by 2 hours.
 */
function updateTimeEnd() {
    document.getElementById("Reserve_Timestamp_end_time").value = document.getElementById("Reserve_Timestamp_start_time").value;
    // stepUp increments the minutes of a time-field by a set amount, in this case 120 minutes.
    document.getElementById("Reserve_Timestamp_end_time").stepUp(120);
}

function checkInputStart(event) {
    checkInputRangeStart(event);
    updateTimeEnd();
}

function checkInputRangeStart(event) {
    let val = $("#Reserve_Timestamp_start_time").val();
    if (val < "08:00"
        && event.keyCode !== 46 // delete
        && event.keyCode !== 8 // backspace
    ) {
        event.preventDefault();
        $("#Reserve_Timestamp_start_time").val("08:00");
        showTimeLimitMessage();
        $('#Reserve_Timestamp_start_time').focus();
    } else if (val > "22:00"
        && event.keyCode !== 46 // delete
        && event.keyCode !== 8 // backspace
    ) {
        event.preventDefault();
        $("#Reserve_Timestamp_start_time").val("22:00");
        showTimeLimitMessage();
        $('#Reserve_Timestamp_start_time').focus();
    } else {
        hideTimeLimitMessage();
    }
}

function checkInputRangeEnd(event) {
    let val = $("#Reserve_Timestamp_end_time").val();
    if (val < "08:00" || val > "22:00"
        && event.keyCode !== 46 // delete
        && event.keyCode !== 8 // backspace
    ) {
        event.preventDefault();
        updateTimeEnd();
        showTimeLimitMessage();
        $('#Reserve_Timestamp_end_time').focus();
    } else {
        hideTimeLimitMessage();
    }
}

function showTimeLimitMessage() {
    console.log("shown")
    $('.timeLimitMessage').show();
}

function hideTimeLimitMessage() {
    console.log("hidden")
    $('.timeLimitMessage').hide();
}