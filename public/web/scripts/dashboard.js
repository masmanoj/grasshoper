

'use strict';
angular.module('dashboard', [

/*common dependencies*/
	'ngRoute',
 	'restangular',
 	'angularFileUpload',
  'pascalprecht.translate',
  'router',
 	'dashboard.controllers',
 	'dashboard.services',
  'ui.bootstrap'

  ])
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
      /*RestangularProvider.setResponseExtractor(function(response) {
        var newResponse = response;
        newResponse.resultElement = angular.copy(response);
        return newResponse;
      });*/
  }]).config(['$translateProvider', function($translateProvider) {
    $translateProvider.useStaticFilesLoader({
            prefix: 'global-translations/locale-',
            suffix: '.json'
        });

        $translateProvider.preferredLanguage('en');
        $translateProvider.fallbackLanguage('en');
  }]);
