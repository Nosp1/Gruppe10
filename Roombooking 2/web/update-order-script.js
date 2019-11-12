$.ajaxSetup({cache: false});

$(function() {
    $(document).ready($("#collapseUpdateBooking")
        .append(`<div id="searchResult" hidden>
                    <div style="color: black">Room is available:</div>
                </div>
                <div id="calendar" hidden>
                    <button style="color: black;">Previous day</button>
                    <input type="date">
                    <button style="color: black;">Next day</button>
                </div>`));
});

// Aktiveres når search-rooms knappen blir trykket på
$('#navbar-search-button').on('click', function () {
    console.log("navbar search button clicked")
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

$("#calendar button:nth-of-type(1)").on('click', function () {
    const strDate = $("#calendar input[type='date']").val();
    const arr = strDate.split("-").map(item => +item);
    const date = new Date(arr[0], arr[1] - 1, arr[2]);
    $('#calendar input[type="date"]').val(date.toISOString().substring(0, 10));
    //console.log(date, typeof date);
    showAllRooms();
});

$("#calendar button:nth-of-type(2)").on('click', function () {
    const strDate = $("#calendar input[type='date']").val();
    const arr = strDate.split("-").map(item => +item);
    const date = new Date(arr[0], arr[1] - 1, arr[2] + 2);
    $('#calendar input[type="date"]').val(date.toISOString().substring(0, 10));
    //console.log(date, typeof date);
    showAllRooms();
});

function Room(roomID, roomName, availableTimes = []) {
    this.roomID = roomID;
    this.roomName = roomName;
    this.availableTimes = availableTimes;
}

function StartEndPair(pairStartTime, pairEndTime) {
    this.pairStartTime = pairStartTime;
    this.pairEndTime = pairEndTime;
}

function showAllRooms() {
    console.log("Show all rooms clicked");
    const roomId = -1;
    getRoomInfo(roomId);
    $("#calendar").show();
    $("#searchResult").show();
}

function getRoomInfo(roomId) {
    let roomList = [];
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
        let roomNames = null;
        let first = true;
        data.forEach(room => {
            if (Array.isArray(room)) {
                if(first) {
                    roomIds = room;
                    first = false;
                } else {
                    roomNames = room;
                    console.log("roomNames=",roomNames);
                }
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
        let counter = 0;
        let mappedRooms = {};
        for (let id in rooms) {
            let newRoom = new Room();
            console.log('id = ', id);
            newRoom.roomID = id;
            mappedRooms[id] = roomNames[counter];
            newRoom.roomName = mappedRooms[id];
            counter++;
            console.log("newRoom id=", newRoom.roomID);
            console.log("id to name=", mappedRooms[id]);
            //$("#searchResult > div:last-child").append($(`<div style="color: black; margin-top: 10px;">Room = ${id}</div>`));
            formattedHTML += `<div class="room-result">`;
            formattedHTML += `<div style="color: black; margin-top: 10px;">${mappedRooms[id]}</div>`;
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
                console.log("startTime= ", startTime);
                console.log("endTime= ", endTime);
                formattedHTML += el;
                formattedHTML += `<div class="quick-reserve"><a class="btn btn-success btn-lg" role="button"
                                    onclick="scrollToReserve('${id}', '${mappedRooms[id]}', '${startTime}')">Reserve</a></div>`;
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

function scrollToUpdate(nthButton, newRoomName) {
    showAllRooms();
    console.log("button to scroll to= ", nthButton);
    let update = document.getElementById('collapseUpdateBooking');
    $(update).collapse('show');
    $("#Update_orderID").val(nthButton);
    document.getElementById("Update_roomName").innerText = newRoomName;
    document.getElementById("Update_Timestamp_start_time").value = "08:00";

    document.getElementById("Update_Timestamp_end_time").value = "08:00";
    // stepUp increments the minutes of a time-field by a set amount, in this case 120 minutes.
    document.getElementById("Update_Timestamp_end_time").stepUp(120);

    let date = getCalendarDate();
    document.getElementById("Update_Timestamp_start_date").value = date;
    document.getElementById("Update_Timestamp_end_date").value = date;

    update.scrollIntoView({behavior: "smooth"});
}

function getCalendarDate() {
    return $("#calendar input[type=date]").val();
}
