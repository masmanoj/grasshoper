'use strict';

angular.module('myApp', ['ngRoute', 'restangular', 'angularFileUpload', 'myApp.services', 'myApp.controllers']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/main', {templateUrl: 'views/main.html'});
	$routeProvider.otherwise({redirectTo: '/main'});
  }]).
  config(['RestangularProvider', '$httpProvider', function(RestangularProvider, $httpProvider) {
      RestangularProvider.setBaseUrl('http://localhost:8080/grasshoper-core/api/'); 
	  RestangularProvider.setDefaultHeaders({ 'Accept': 'text/html,application/json;q=0.9,*/*;q=0.8'});
	  delete $httpProvider.defaults.headers.common['X-Requested-With'];
      RestangularProvider.setRestangularFields({
        id: '_id.$oid'
      });
      
      RestangularProvider.setRequestInterceptor(function(elem, operation, what) {
        if (operation === 'put') {
          elem._id = undefined;
          return elem;
        }
        return elem;
      });
  }]);