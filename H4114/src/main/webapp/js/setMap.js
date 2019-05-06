/**
var map = L.map('map').fitWorld();

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);



function createMarker(colorNumber){
    var marker = L.AwesomeMarkers.icon({
    prefix: "fa",
    icon: 'star',
    markerColor: NumberToIcon(colorNumber)
    });
    return marker;  
}

var positions = [];
function createPositions(){
    positions.push({x: rnd(d.x, max_x_stdev), y: rnd(d.y, max_y_stdev)});
    
}

function onLocationFound(e) {
    //var radius = e.accuracy / 2;
    

    L.marker(e.latlng,{icon: createMarker(1)}).addTo(map);
	//L.marker(e.latlng-[0.00001,0.00001]).addTo(map);
	//L.marker([45.781764, 4.872399],{color: 'red'}).addTo(map);
        //.bindPopup("You are within " + radius + " meters from this point").openPopup();
	
    //L.circle(e.latlng, radius).addTo(map);
	//dbscan();
	
}

class cluster_position{
    constructor(lat,lng,cluster){
        this.latlng = L.latLng(lat, lng);
        this.cluster = cluster;        
    }
    
    
    
}

function dbscan() {
    
    function generate_cluster_data() {
		var num_clusters = 3;
		var max_x_stdev = 10;
		var max_y_stdev = 15;
		var cluster_size = 30;

		var raw_point_data = [];
		var cluster_centers = [];
		for (var i = 0; i < num_clusters; i++) {
			cluster_centers.push({x: Math.random() * (width - 30), y: Math.random() * (height - 30)});
		}

		cluster_centers.forEach(function (d) {
			for (var i = 0; i < cluster_size; i++) {
				raw_point_data.push({x: rnd(d.x, max_x_stdev), y: rnd(d.y, max_y_stdev)});
			}
		});

		return raw_point_data;
	}
    
    var raw_point_data = generate_cluster_data();
    
    var dbscanner = jDBSCAN().eps(30).minPts(1).distance('EUCLIDEAN').data(positions);
    var point_assignment_result = dbscanner();
    console.log('Resulting DBSCAN output', point_assignment_result);

    
}

function onLocationError(e) {
    alert(e.message);
}

var popup = L.popup();

function onMapClick(e) {
    popup
        .setLatLng(e.latlng)
        .setContent("You clicked the map at " + e.latlng.toString())
        .openOn(map);
}

map.on('click', onMapClick);

map.on('locationfound', onLocationFound);
map.on('locationerror', onLocationError);

map.locate({setView: true, maxZoom: 16});
*/

var positions = [];
function createPositions(){
    positions.push({location: {latitude: 45.782019, longitude: 4.872554}});
    positions.push({location: {latitude: 45.781980, longitude: 4.872514}});
    positions.push({location: {latitude: 45.782039, longitude: 4.872470}});
    positions.push({location: {latitude: 45.782048, longitude: 4.872501}});
    positions.push({location: {latitude: 45.781048, longitude: 4.872501}});
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
        icon: { url: "./icons/man-blue-user.png"}
    });
    
    var cluster = dbscan();
    var marker = [];
    for (var i = 0; i < positions.length; i++) {
        if (cluster[i] == 1){
            marker[i] = new google.maps.Marker({
            position: {lat: positions[i].location.latitude, lng: positions[i].location.longitude},
            map: map,
            icon: { url: "./icons/man-blue-user.png"}
            });
        }
        if (cluster[i] == 0){
            marker[i] = new google.maps.Marker({
            position: {lat: positions[i].location.latitude, lng: positions[i].location.longitude},
            map: map,
            icon: { url: "./icons/man-red-user.png"}
            });
        }
        
    }
    
    
    
    console.log('initial map');
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
}

function dbscan() {
    //createPositions();
    console.log('positions: ', positions);
   
    var dbscanner = jDBSCAN().eps(0.1).minPts(1).distance('HAVERSINE').data(positions);
    console.log('dbscanner', dbscanner);
    var cluster_centers = dbscanner.getClusters(); 
    console.log('cluster_centers: ', cluster_centers);
    var  point_assignment_result = dbscanner();
    return point_assignment_result;
}