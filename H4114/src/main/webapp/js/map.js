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

function newAssembly() {
    document.getElementById("createAssembly").style.display = "block";
}


function createAssembly() {
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
            url: './UserServlet',
            method: 'POST',
            data: {
                action: 'createAssembly',
                rally: rally,
                description: description,
                latitude: 44.12312,
                longitude: 102.12312,
                colour: "white",
                date_time: date,
                radio: radio,
                email: email,
                password: password
            },
            dataType: 'json',
            error: function () {
                console.log("Error while sending new rally request");
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
/*
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
            alert("Error while sending assemblies request");
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
*/

