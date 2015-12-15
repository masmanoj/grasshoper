'use strict';
angular.module('dashboard', [

/*common dependencies*/
	'ngRoute',
 	'restangular',
 	'angularFileUpload',
  'pascalprecht.translate',
 	'dashboard.controllers',
 	'dashboard.services'

  ])
  
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/main', {templateUrl: 'views/main.html'});
	$routeProvider.otherwise({redirectTo: '/main'});
  }])

  .config(['RestangularProvider', '$httpProvider', function(RestangularProvider, $httpProvider) {
      RestangularProvider.setBaseUrl('https://localhost:8443/grasshoper-core/'); 
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
  }]).config(['$translateProvider', function($translateProvider) {
    $translateProvider.useStaticFilesLoader({
            prefix: 'global-translations/locale-',
            suffix: '.json'
        });

        $translateProvider.preferredLanguage('en');
        $translateProvider.fallbackLanguage('en');
  }]);