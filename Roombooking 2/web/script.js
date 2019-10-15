console.log('href', location.href);

$('#search-form').on('submit', function(evt) {
    evt.preventDefault();
    const value = $('.navbar-form .form-group input[type="text"]').val();
    console.log('value = ', value);
    $.get('/Roombooking_2_Web_exploded/Servlets.ServletSearch?roomId=' + value, function(response) {
        console.log('response = ', response);
        const data = JSON.parse(response);
        console.log('data', data);
        $("#searchResult").html('');
        if (!data.forEach) {
            $("#searchResult").html(data.error);
            return;
        }
        let leftTimeBorder = "08:00";
        let rightTimeBorder = "22:00";
        const timePoints = [];
        timePoints.push(leftTimeBorder);
        data.forEach(function(room) {
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
            if (timePoints[i] !== timePoints[i+1]) {
                spareIntervals.push({
                    start: timePoints[i],
                    end: timePoints[i+1],
                });
            }
        }
        spareIntervals.forEach(function(interval) {
            const startTime = interval["start"];
            const endTime = interval["end"];
            const el = $(`<div>${startTime} - ${endTime}</div>`);
            $("#searchResult").append(el);
        })
    })
});
