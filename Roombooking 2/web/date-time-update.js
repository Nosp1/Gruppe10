/**
 * updateEndDate sets the date-field with id "out" to the same value as the first date-field.
 * TODO: Endre til generell metode for bedre cohesion, men vi bruker det kun i reserve og update, så kanskje ikke nødvendig
 */
function update_updateEndDate() {
    var date = document.getElementById("Update_Timestamp_start_date");
    // First we get the whole value from the first date-field.
    var c = date.value;
    // Then we substring the first 10 characters (yyyy-mm-dd),
    var d = c.substring(0, 10);
    // and set the second date-field to this value.
    document.getElementById("Update_Timestamp_end_date").value = d;
}

function update_checkInputEnd(event) {
    update_checkInputRangeEnd(event);
}

/**
 * updateTimeEnd takes the end-time and increments it by 2 hours.
 */
function update_updateTimeEnd() {
    document.getElementById("Update_Timestamp_end_time").value = document.getElementById("Update_Timestamp_start_time").value;
    // stepUp increments the minutes of a time-field by a set amount, in this case 120 minutes.
    document.getElementById("Update_Timestamp_end_time").stepUp(120);
}

function update_checkInputStart(event) {
    update_checkInputRangeStart(event);
    update_updateTimeEnd();
}

function update_checkInputRangeStart(event) {
    let val = $("#Update_Timestamp_start_time").val();
    if (val < "08:00"
        && event.keyCode !== 46 // delete
        && event.keyCode !== 8 // backspace
    ) {
        event.preventDefault();
        $("#Update_Timestamp_start_time").val("08:00");
        update_showTimeLimitMessage();
        $('#Update_Timestamp_start_time').focus();
    } else if (val > "22:00"
        && event.keyCode !== 46 // delete
        && event.keyCode !== 8 // backspace
    ) {
        event.preventDefault();
        $("#Update_Timestamp_start_time").val("22:00");
        update_showTimeLimitMessage();
        $('#Update_Timestamp_start_time').focus();
    } else {
        update_hideTimeLimitMessage();
    }
}

function update_checkInputRangeEnd(event) {
    let val = $("#Update_Timestamp_end_time").val();
    if (val < "08:00" || val > "22:00"
        && event.keyCode !== 46 // delete
        && event.keyCode !== 8 // backspace
    ) {
        event.preventDefault();
        update_updateTimeEnd();
        update_showTimeLimitMessage();
        $('#Update_Timestamp_end_time').focus();
    } else {
        update_hideTimeLimitMessage();
    }
}

function update_showTimeLimitMessage() {
    console.log("shown")
    $('.timeLimitMessage').show();
}

function update_hideTimeLimitMessage() {
    console.log("hidden")
    $('.timeLimitMessage').hide();
}