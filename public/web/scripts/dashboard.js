

'use strict';
var runInjector = null;
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

  ]).run(function($injector){
    console.log(" d888b  d8888b.  .d8b.  .d8888. .d8888. db   db  .d88b.  d8888b. d88888b d8888b. "); 
      console.log("88' Y8b 88  `8D d8' `8b 88'  YP 88'  YP 88   88 .8P  Y8. 88  `8D 88'     88  `8D ");
      console.log("88      88oobY' 88ooo88 `8bo.   `8bo.   88ooo88 88    88 88oodD' 88ooooo 88oobY' ");
      console.log("88  ooo 88`8b   88~~~88   `Y8b.   `Y8b. 88~~~88 88    88 88~~~   88~~~~~ 88`8b   ");
      console.log("88. ~8~ 88 `88. 88   88 db   8D db   8D 88   88 `8b  d8' 88      88.     88 `88. ");
      console.log(" Y888P  88   YD YP   YP `8888Y' `8888Y' YP   YP  `Y88P'  88      Y88888P 88   YD "); 
    runInjector = $injector;
  })

  .config(['$translateProvider', function($translateProvider) {
    $translateProvider.useStaticFilesLoader({
            prefix: 'global-translations/locale-',
            suffix: '.json'
        });

        $translateProvider.preferredLanguage('en');
        $translateProvider.fallbackLanguage('en');
  }])
  .config(['RestangularProvider', '$httpProvider', '$rootScopeProvider', 
    function(RestangularProvider, $httpProvider, $rootScopeProvider) {
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
       // console.log("runInjector 0",runInjector);
         //     runInjector .get('$rootScope');
       var pendingRequests = 0;
       RestangularProvider.addRequestInterceptor(function (element, operation, what, url) {
            if (pendingRequests == 0) {
              //console.log(what);
                console.log(pendingRequests,'loading data (show indicator)');
                var rootScope = runInjector.get('$rootScope');
                if(!(rootScope.silentAjaxs &&  rootScope.silentAjaxs.indexOf(what) > -1 )) 
                  rootScope.blockUi = true;
            }
            pendingRequests++;
            return element;
        });
        RestangularProvider.addResponseInterceptor(function (data, operation, what, url, response, deferred) {
            pendingRequests--;
            if (pendingRequests == 0) {
               // console.log('loaded data (hide indicator)');
                var rootScope = runInjector .get('$rootScope');
                rootScope.blockUi = false;
            }
            return data;
        });

        RestangularProvider.setErrorInterceptor(function(response, deferred) {
            pendingRequests--;
            if (pendingRequests == 0) {
                var rootScope = runInjector .get('$rootScope');
                rootScope.blockUi = false;
                //console.log('loaded data (hide indicator) Error',response);
                if(response.statusText == "Unauthorized"){
                    rootScope.authenticationSts = "Unauthorized";
                }
            }
            //console.log("Some error occured ", response);
            //return true; // error not handled
        });
      /*RestangularProvider.setResponseExtractor(function(response) {
        var newResponse = response;
        newResponse.resultElement = angular.copy(response);
        return newResponse;
      });*/
  }]);
