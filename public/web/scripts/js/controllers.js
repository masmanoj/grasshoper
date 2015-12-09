'use strict';

angular.module('myApp.controllers', []).

  controller('HomeController', ['$scope', '$upload', 'Restangular', function($scope, $upload, Restangular) {
	  Restangular.all("key/maps").getList()
	  .then(function(apiKey) {
		  $scope.apiKey = apiKey[0].name;
		  console.log($scope.apiKey);
		});
	  
	  function initialize() {
		  var style = [
		               {
		               featureType: "transit",
		               stylers: [
		                   { visibility: "off" }
		               ]
		               },{
		               featureType: "road",
		               stylers: [
		                   { lightness: "50" },
		                   { visibility: "on" }
		               ]
		               }
		           ];
		  var options = {
			        zoom: 9,
			        center:  new google.maps.LatLng(28.6454415,77.0907573),
			        mapTypeId: google.maps.MapTypeId.ROADMAP,
			        disableDefaultUI: true
		    };
		  	var map = new google.maps.Map($('#map')[0], options);
		    map.setOptions({
		        styles: style
		    });
		    
		    var polys = [];
		    $scope.infoWindow;
		    Restangular.all("polygon").getList()
			  .then(function(polygons) {
				  for(var i=0; i < polygons.length; i++) {
					  var name = polygons[i].name;
					  var coords = polygons[i].coords;
					  polys.push(new google.maps.Polygon({
	                        paths: coords,
	                        strokeColor: '#FF0000',
	                        strokeOpacity: 0.3,
	                        strokeWeight: 0.8,
	                        fillColor: '#FF0000',
	                        fillOpacity: 0.25
	                      }));
					  polys[polys.length-1].setMap(map);
					  google.maps.event.addListener(polys[i], 'click', function(i, name) {
					      return function(event) {
					    	  if ($scope.infoWindow) {
					    		  $scope.infoWindow.close();
					    	    }
					    	  Restangular.one("politicians/latlong","").get({lat: event.latLng.G, long: event.latLng.K})
					    	  .then(function(pol) {
					    		  var politician = "<b>Constituency Name : " + name + "</b><br/>" +
					    		  	"Name :" + pol.name + "<br/>Party :" + pol.partyName + "<br/>Contact No. : " + pol.contact +
					    		  	"<br/>Address :" + pol.address + "<br/>Email :" + pol.email + "<br/>Education :" +pol.education + "<br/>Total Assets :" +
					    		  	pol.liabilities + "<br/>Liabilities :" + pol.totalAssets + "";
					    		  $scope.infoWindow = new google.maps.InfoWindow({
						              content: politician
						          });
						    	  $scope.infoWindow.setPosition(event.latLng);
						    	  $scope.infoWindow.open(map, polys[i]);
					    	  });
					    	  
					      }
					    }(i, name));
				  }
				});
		    
		}
	  
	  
		google.maps.event.addDomListener(window, 'load', initialize);
	   
  }]).
  
   controller('DownloadController', ['$scope', 'Restangular', function($scope, Restangular) {
	   $scope.sendReq = function() {
		   window.open('/pest-incidence/api/import', '_blank', '');  
	   };
  }]).
  
   controller('FilterController', ['$scope', 'Restangular', function($scope, Restangular) {
	   $scope.attributeTypes = Restangular.all("filter").getList().$object;
	   $scope.filterObjects = [];
	   $scope.filters = [];
	   $scope.filterObject = {};
	   $scope.status;
	   $scope.message;
	   $scope.sendReq = function() {
		   $scope.filterObjects.length = 0;
		   for(var i = 0; i < $scope.filters.length; i++) {
			   if($scope.filters[i] != null) {
				   $scope.filterObject = new filter($scope.attributeTypes[i].attributeName,$scope.filters[i]);
				   $scope.filterObjects.push($scope.filterObject);
			   }
		   }
		   Restangular.all('filter').post($scope.filterObjects).then(function() {
			   $scope.status = true;
			   $scope.message = "Successfully filtered.";
			   
			}, function(response){
				   $scope.status = false;
				   $scope.message = response;
			});
	   };
	   
	   function filter(name, category)
	   {
		   this.name = name;
		   this.category = category;
	   }
	   
  }]).
  
  controller('FakeController', ['$scope', 'Restangular', function($scope, Restangular) {
	$scope.projects = Restangular.one("import").get();
 }]).

  controller('SignUpController',['$scope', 'Restangular', function($scope, Restangular) {
  	$scope.usr = {};

  	$scope.submit = function (){
  		console.log($scope.usr);
  	}
	$scope.projects = Restangular.one("import").get();
  }])
  ;


