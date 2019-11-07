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
    // console.log(evt.target.value);
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


$('#reserve_Room').on('click', function (evt) {
    evt.preventDefault();
    console.log("show all rooms clicked");
    const roomId = -1;
    getRoomInfo(roomId);
    $("#calendar").show();
    $("#searchResult").show();

});

function Room(roomID, availableTimes = []) {
    this.roomID = roomID;
    this.availableTimes = availableTimes;
}

function StartEndPair(pairStartTime, pairEndTime) {
    this.pairStartTime = pairStartTime;
    this.pairEndTime = pairEndTime;
}

let roomList = [];

function getRoomInfo(roomId) {
    roomList = [];
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
        let formattedHTML = $('#searchResult').empty().html();

        formattedHTML += `<div class="room-result-container">`;
        for (let id in rooms) {
            let newRoom = new Room();
            console.log('id = ', id);
            newRoom.roomID = id;
            console.log("newRoom id=", newRoom.roomID);
            //$("#searchResult > div:last-child").append($(`<div style="color: black; margin-top: 10px;">Room = ${id}</div>`));
            formattedHTML += `<div class="room-result">`;
            formattedHTML += `<div style="color: black; margin-top: 10px;">Room = ${id}</div>`;
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
                let newPair = new StartEndPair(startTime, endTime);
                //const el = $(`<div>${startTime} - ${endTime}</div>`);
                //$("#searchResult > div:last-child").append(el);
                newRoom.availableTimes.push(newPair);
                const el = `<div>${startTime} - ${endTime}</div>`;
                formattedHTML += el;
                formattedHTML += `<div class="quick-reserve"><a class="btn btn-success btn-lg" role="button"
                                    onclick="scrollToReserve(${id})">Reserve</a></div>`;
            });
            console.log("times= ", newRoom.availableTimes);
            // Closing div for every room-result in the loop
            roomList.push(newRoom);
            formattedHTML += `</div>`;
        }
        // Closing div for room-result-container
        console.log(roomList);
        formattedHTML += `</div>`;
        $('#searchResult').append(formattedHTML);
    })
}

function scrollToReserve(roomIDToScrollTo) {
    console.log("id to scroll to= ", roomIDToScrollTo);
    let reserve = document.getElementById('collapseReserveRoom');
    $(reserve).collapse('show');
    $("#Reserve_Room_ID").val(roomIDToScrollTo);
    //$("#Reserve_Timestamp_start_time").val(setStartTime);
    //$("#Reserve_Timestamp_end_time").val(setStartTime).stepUp(120);
    reserve.scrollIntoView({behavior: "smooth"});
}