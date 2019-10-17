$.ajaxSetup({cache: false});
$('#search-form').on('submit', function (evt) {
    evt.preventDefault();
    const roomId = +$('.navbar-form .form-group input[type="text"]').val();
    if (roomId < 0) {
        return alert("Room number is not correct!");
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

$('#ListOfRooms').on('submit', function (evt) {
    evt.preventDefault();
    getRoomInfo(-1);
    $("#calendar").show();
    $("#searchResult").show();
});

function getRoomInfo(roomId) {
    const date = $('#calendar input[type="date"]').val();
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