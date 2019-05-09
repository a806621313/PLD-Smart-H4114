
$(document).ready(function(){
    $('#content').load("map.html");
 });

function changeToMap() {
    document.getElementsByClassName("active")[0].classList.remove("active");
    document.getElementById("map").classList.add("active");
    $('#content').load("map.html");
}

function changeToView() {
    document.getElementsByClassName("active")[0].classList.remove("active");
    document.getElementById("view").classList.add("active");
    $('#content').load("view.html");
}

function changeToStream() {
    document.getElementsByClassName("active")[0].classList.remove("active");
    document.getElementById("stream").classList.add("active");
    $('#content').load("stream.html");
}

function changeToVote() {
    document.getElementsByClassName("active")[0].classList.remove("active");
    document.getElementById("vote").classList.add("active");
    $('#content').load("vote.html");
    
    getPseudos();
    //getAssemblies();
    //getParticipants();

}

function changeToAlert() {
    document.getElementsByClassName("active")[0].classList.remove("active");
    document.getElementById("alert").classList.add("active");
    $('#content').load("alert.html");
}


function getAssemblies()
{

    $.ajax({
            url: './UserServlet',
            method: 'POST',
            data: {
                action: 'getAssemblies',
            },
            dataType: 'json',
            error: function () {
                console.log("GetAssemblies : Error while getting assemblies");
            }
        }).done(function (data) {
           
        });
}

function getPseudos()
{
   
    $.ajax({
            url: './UserServlet',
            method: 'POST',
            data: {
                action: 'getPseudos',
            },
            dataType: 'json',
            error: function () {
                console.log("getPseudos : Error while getting pseudo");
            }
        }).done(function (data) {
            console.log(data);
        });
}

function getParticipants()
{
   
    $.ajax({
            url: './UserServlet',
            method: 'POST',
            data: {
                action: 'getParticipants',
            },
            dataType: 'json',
            error: function () {
                console.log("GetParticipants : Error while getting participants");
            }
        }).done(function (data) {
            console.log(data);
        });
}

