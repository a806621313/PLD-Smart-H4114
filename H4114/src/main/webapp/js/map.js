function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] === variable) {
            return pair[1];
        }
    }
    return(false);
}
;



var user = getQueryVariable("user");
var latitude;
var longitude;

function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition);
    } else {
        alert("Geolocation is not supported by this browser");
    }
}

function showPosition(position) {
    latitude = position.coords.latitude;
    longitude = position.coords.longitude;
}

function newRally() {
    document.getElementById("createRally").hidden = false;
}


function createRally() {
    $("#createRally").show();
    $('#message').text("");
    var rally = $('#rally').val();
    var description = $('#description').val();
    var place = $('#place').val();
    var date = $('#date').val();
    var time = $('#time').val();
    var radio = $('#radio').val();
    var email = $('#email').val();
    var password = $('#password').val();
    if (false) { //Verify whether there are errors
        $('#message').text("Failed");
    } else {
        $.ajax({
            url: './ActionServlet',
            method: 'POST',
            data: {
                action: 'create rally',
                rally: rally,
                description: description,
                place: place,
                date: date,
                time: time,
                radio: radio,
                email: email,
                password: password
            },
            dataType: 'json',
            error: function () {
                //alert("Error while sending new rally request");
            }
        }).done(function (data) {
            var reponse = data.createRally;
            if (reponse.rallyCreated === "true") {
                document.getElementById("createRally").hidden = true;
            } else {
                $('#message').text("Ups we didn't succed to verify rally creation");
            }
        });
    }
}

var k;
window.setInterval(function () {
    getLocation();
    k=k+1;
    latitude = latitude+k;
    longitude = longitude-k;
    $.ajax({
        url: './ActionServlet',
        method: 'POST',
        data: {
            action: 'assemblies',
            latitude: latitude,
            longitude: longitude,
            user: user
        },
        dataType: 'json',
        error: function () {
            //alert("Error while sending assemblies request");
        }
    }).done(function (data) {
        var reponse = data.assemblies;
        if (reponse.assemblies) {

            //clearInterval() to finish repetating

        } else {
            $('#message').text("Ups we didn't succed to verify rally creation");
        }
    });
}, 10000);


