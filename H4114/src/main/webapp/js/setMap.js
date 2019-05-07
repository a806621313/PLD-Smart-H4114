var positions = [];
function createPositions(){
    positions.push({location: {latitude: 45.782019, longitude: 4.872554}});
    positions.push({location: {latitude: 45.781980, longitude: 4.872514}});
    positions.push({location: {latitude: 45.782039, longitude: 4.872470}});
    positions.push({location: {latitude: 45.782048, longitude: 4.872501}});
    
    positions.push({location: {latitude: 45.781048, longitude: 4.872501}});
    positions.push({location: {latitude: 45.781108, longitude: 4.872500}});
    positions.push({location: {latitude: 45.781008, longitude: 4.872504}});
    
    positions.push({location: {latitude: 45.782039, longitude: 4.862470}});
}


function initMap() {
    var location = {lat: 45.782012, lng: 4.872501};
    var map = new google.maps.Map(document.getElementById("map"), {
            zoom: 19,
            center: location
    });
    createPositions();
    //Markers
    var marker0 = new google.maps.Marker({
        position: location,
        map: map,
        icon: { url: "./icons/man-red-user.png"}
    });
    
    var cluster = dbscan();
    var marker = [];
    for (var i = 0; i < positions.length; i++) {
        if (cluster[i] == 0){
            marker[i] = new google.maps.Marker({
            position: {lat: positions[i].location.latitude, lng: positions[i].location.longitude},
            map: map,
            icon: { url: "./icons/man-black-user.png"}
            });
        }
        if (cluster[i] == 1){
            marker[i] = new google.maps.Marker({
            position: {lat: positions[i].location.latitude, lng: positions[i].location.longitude},
            map: map,
            icon: { url: "./icons/man-blue-user.png"}
            });
        }
        if (cluster[i] == 2){
            marker[i] = new google.maps.Marker({
            position: {lat: positions[i].location.latitude, lng: positions[i].location.longitude},
            map: map,
            icon: { url: "./icons/man-green-user.png"}
            });
        }
        if (cluster[i] == 3){
            marker[i] = new google.maps.Marker({
            position: {lat: positions[i].location.latitude, lng: positions[i].location.longitude},
            map: map,
            icon: { url: "./icons/man-pink-user.png"}
            });
        }   
    }
}

var x = document.getElementById("demo");
function getLocation() {
    if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(showPosition);
    } else {
            x.innerHTML = "Geolocation is not supported by this browser.";
    }
}

function showPosition(position) {
    x.innerHTML = "Latitude: " + position.coords.latitude + "<br>Longitude: " + position.coords.longitude;
}
/**
function NumberToIcon(number){
    switch(number){
        default:
        case 1 : return "red";
        case 2 : return "darkred";
        case 3 : return "orange";
        case 4 : return "green";
        case 5 : return "darkgreen";
        case 6 : return "blue";
        case 7 : return "purple";
        case 8 : return "darkpurple";    
        case 9 : return "cadetblue"; 
    }
}



class cluster_position{
    constructor(latlng,cluster){
        this.latlng = latlng;
        this.cluster = cluster;        
    }
}*/

function dbscan() {
    //createPositions();
    console.log('positions: ', positions);
   
    var dbscanner = jDBSCAN().eps(0.05).minPts(1).distance('HAVERSINE').data(positions);
    console.log('dbscanner', dbscanner);
    var cluster_centers = dbscanner.getClusters(); 
    console.log('cluster_centers: ', cluster_centers);
    var  point_assignment_result = dbscanner();
    return point_assignment_result;
}