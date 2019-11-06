$.ajaxSetup({cache: false});

$(function() {
    $(document.body).append("<div id=\"searchResult\" hidden>\n" +
        "    <div style=\"color: black\">Room is available:</div>\n" +
        "</div>\n" +
        "<div id=\"calendar\" hidden>\n" +
        "    <button style=\"color: black;\">Previous day</button>\n" +
        "    <input type=\"date\">\n" +
        "    <button style=\"color: black;\">Next day</button>\n" +
        "</div>");
});

$('#navbar-search-button').on('click', function() {
    const roomID = $('#navbar-search-input').val();
    if (roomID < 0) {
        return alert("Room number is not correct! RoomID be higher than 0.");
    }
    getRoomInfo(roomID);
    $("#calendar").show();
    $("#searchResult").show();
});

// Aktiveres når search-rooms knappen blir trykket på
$('#navbar-search-button').on('click', function () {
    console.log("navbar search button clicked")
    // preventDefault stopper redirect
    evt.preventDefault();
    // Hent roomID fra text-feltet i navbaren
    const roomId = $('#navbar-search-input').val();
    // Varsle brukeren om roomID er mindre enn 0
    if (roomId < 0) {
        return alert("Room number is not correct! RoomID be higher than 0.");
    }
    getRoomInfo(roomId);
    $("#calendar").show();
    $("#searchResult").show();
});
$('#calendar input[type="date"]').val((new Date()).toISOString().substring(0, 10));
$('#calendar input[type="date"]').on('change', function (evt) {
});

$("#calendar button:nth-of-type(1)").on('click', function () {
    const strDate = $("#calendar input[type='date']").val();
    const arr = strDate.split("-").map(item => +item);
    const date = new Date(arr[0], arr[1] - 1, arr[2]);
    $('#calendar input[type="date"]').val(date.toISOString().substring(0, 10));
    console.log(date, typeof date);
});

$("#calendar button:nth-of-type(2)").on('click', function () {
    const strDate = $("#calendar input[type='date']").val();
    const arr = strDate.split("-").map(item => +item);
    const date = new Date(arr[0], arr[1] - 1, arr[2] + 2);
    $('#calendar input[type="date"]').val(date.toISOString().substring(0, 10));
});

 //Liker ikke at Show all rooms blir påvirket på denne måten
$('#ListOfRooms').on('submit', function (evt) {
    console.log("Show all rooms clicked");
    // hvis denne preventDefault ikke er kommentert fungerer ikke printRooms knappen
    evt.preventDefault();
    const roomId = -1;
    getRoomInfo(roomId);
    $("#calendar").show();
    $("#searchResult").show();
});

$('#collapseReserveRoom > form').on('submit', function(evt) {
    // evt.preventDefault();
    $('.book-start-datetimes').html('');
    $('.book-end-datetimes').html('');
    console.log('SUBMIT!');
    const fd = new FormData(evt.target);
    for (let pair of fd.entries()) {
        console.log(pair);
    }
    const startDate = $('#Reserve_Timestamp_start_date').val();
    const startTime = $('#Reserve_Timestamp_start_time').val();
    const endDate = $('#Reserve_Timestamp_end_date').val();
    const endTime = $('#Reserve_Timestamp_end_time').val();
    const periodMode = +$(evt.target).find('select[name="period"]').val();
    const startDateTimes = getDatePeriodRange(startDate, startTime, periodMode);
    const endDateTimes = getDatePeriodRange(endDate, endTime, periodMode);
    startDateTimes.forEach(item => {
        const el = $(`<input type="checkbox" name="start-datetimes" value="${item}" checked>`);
        $('.book-start-datetimes').append(el);
    });
    endDateTimes.forEach(item => {
        const el = $(`<input type="checkbox" name="end-datetimes" value="${item}" checked>`);
        $('.book-end-datetimes').append(el);
    });
});

function getAmountDaysBeforeSemesterEnd(currentDate) {
    // const currentDate = new Date();
    const currentYear = currentDate.getFullYear();
    const currentMonth = currentDate.getMonth() + 1;
    let endDate;
    if (currentMonth <= 7) {
        endDate = new Date(currentYear, 6, 31);
    } else {
        endDate = new Date(currentYear, 11, 31);
    }
    const utc1 = Date.UTC(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate());
    const utc2 = Date.UTC(endDate.getFullYear(), endDate.getMonth(), endDate.getDate());
    const dayAmount = Math.floor((utc2 - utc1) / (1000 * 60 * 60 * 24));
    return dayAmount;
}

function getDatePeriodRange(date, time, k)  {
    const dateArr = date.split("-").map(item => +item);
    const timeArr = time.split(":").map(item => +item);
    const amountDays = getAmountDaysBeforeSemesterEnd(new Date(dateArr[0], dateArr[1] - 1, dateArr[2]));

    const result = [];
    i = 0;
    do {
        const delta = i * 7 * k;
        const d = new Date(dateArr[0], dateArr[1] - 1, dateArr[2] + delta, timeArr[0], timeArr[1]);
        let year = d.getFullYear();
        let month = d.getMonth() + 1;
        let day = d.getDate();
        month = month < 10 ? ('0' + month) : month;
        day = day < 10 ? ('0' + day) : day;
        let hours = d.getHours();
        let minutes = d.getMinutes();
        hours = hours < 10 ? ('0' + hours) : hours;
        minutes = minutes < 10 ? ('0' + minutes) : minutes;
        const newDate = year + '-' + month + '-' + day;
        const newTime = hours + ':' + minutes;
        result.push(newDate + ' ' + newTime);
        i++;
        if (k === 0) {
            break;
        }
    } while (i * 7 * k <= amountDays);
    return result;
}

    $('#reserve_Room').on('click', function (evt) {
        evt.preventDefault();
        console.log("show all rooms clicked");
        const roomId = -1;
        getRoomInfo(roomId);
        $("#calendar").show();
        $("#searchResult").show();

    });






function getRoomInfo(roomId) {
   // if (roomId < 0) {
       // return alert("Room number is not correct! RoomID be higher than 0.");
   // }
    const date = $('#calendar input[type="date"]').val();
    /* Konstruer en query for bruk av HTTP GET
       Vil f.eks bli 'roomID=1&date=2019-10-26
    */
    const query = `roomId=${roomId}&date=${date}`;
    console.log('/Roombooking_2_Web_exploded/Servlets.ServletSearch?' + query);

    $.get('/Roombooking_2_Web_exploded/Servlets.ServletSearch?' + query, function (response) {
        console.log('response = ', response);
        const data = JSON.parse(response);
        console.log('data', data);
        $("#searchResult > div:last-child").html('');
        if (!data.forEach) {
            $("#searchResult > div:last-child").html(data.error);
            return;
        }
        const rooms = {};
        if (roomId >= 0) {
            rooms[roomId] = [];
        }
        let roomIds = null;
        data.forEach(room => {
            if (Array.isArray(room)) {
            roomIds = room;
            return;
        }
        if (!rooms[room.roomId]) {
            rooms[room.roomId] = [room];
        } else {
            rooms[room.roomId].push(room);
        }
    });
        if (roomIds) {
            roomIds.forEach(roomId => {
                if (!rooms[roomId]) {
                rooms[roomId] = [];
            }
        });
        }
        for (let id in rooms) {
            console.log('id = ', id);
            $("#searchResult > div:last-child").append($(`<div style="color: black; margin-top: 10px;">Room = ${id}</div>`));
            const data = rooms[id];
            let leftTimeBorder = "08:00";
            let rightTimeBorder = "22:00";
            const timePoints = [];
            timePoints.push(leftTimeBorder);
            data.forEach(function (room) {
                let startTime = room["start"];
                let endTime = room["end"];
                startTime = startTime.split(" ")[1].split(".")[0].split(":").slice(0, 2).join(":");
                endTime = endTime.split(" ")[1].split(".")[0].split(":").slice(0, 2).join(":");
                room["start"] = startTime;
                room["end"] = endTime;
                timePoints.push(startTime);
                timePoints.push(endTime);
            });
            timePoints.push(rightTimeBorder);
            const spareIntervals = [];
            for (let i = 0; i < timePoints.length; i += 2) {
                if (timePoints[i] !== timePoints[i + 1]) {
                    spareIntervals.push({
                        start: timePoints[i],
                        end: timePoints[i + 1],
                    });
                }
            }
            spareIntervals.forEach(function (interval) {
                const startTime = interval["start"];
                const endTime = interval["end"];
                const el = $(`<div>${startTime} - ${endTime}</div>`);
                $("#searchResult > div:last-child").append(el);
            })
        }
    })
}
