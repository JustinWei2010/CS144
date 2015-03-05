function getMap(coord) {
    var latlng = new google.maps.LatLng(coord.lat, coord.lng);
    var myOptions = {
        zoom: 14,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("map-canvas"), myOptions);
    var marker = new google.maps.Marker({
        map: map,
        position: latlng
    });
    console.log(latlng);
}

//Uses arbitrary coord, with zoomout at 1 to show full globe
function showWorldMap() {
    var myOptions = {
        zoom: 1,
        center: new google.maps.LatLng(0, 78),
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("map-canvas"), myOptions);
}

function getGeocode(address) {
    geocoder = new google.maps.Geocoder();
    geocoder.geocode( { 'address': address}, function(results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
            var location = results[0].geometry.location;
            getMap({lat:location.lat(), lng:location.lng()});
        } else {
            //Display world map if no coord is found for given location
            showWorldMap();
        }
  });
}

function init() {
    if (latitude != null && longitude != null) {
        var coord = {lat: ""+latitude, lng: ""+longitude};
        getMap(coord);
    } else {
        getGeocode(address);
    }
}

google.maps.event.addDomListener(window, "load", init);