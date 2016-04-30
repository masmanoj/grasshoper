var runInjector = null;
var app = angular.module('bluefin', 

  [
    'ionic', 
    'LocalStorageModule', 
    'ngCordova', 
    'restangular',
    'bluefin.controllers',
    'bluefin.services'
  ]
)
.run(function($ionicPlatform, $rootScope,  $cordovaDevice, $injector) {
  $ionicPlatform.ready(function() {
    if(window.cordova && window.cordova.plugins.Keyboard) {
      // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
      // for form inputs)
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);

      // Don't remove this line unless you know what you are doing. It stops the viewport
      // from snapping when text inputs are focused. Ionic handles this internally for
      // a much nicer keyboard experience.
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if(window.StatusBar) {
      StatusBar.styleDefault();
    }

    try {
      $rootScope.uuid = $cordovaDevice.getUUID();
    }
    catch (err) {
      console.log("Error Reading UUID" );
    }

    runInjector = $injector;
    $rootScope.loadingComplete = true;
  });
});

app.config(function (localStorageServiceProvider) {
  localStorageServiceProvider
    .setPrefix('bluefin'+'');
})
.config(['RestangularProvider', '$httpProvider', '$rootScopeProvider', 
    function(RestangularProvider, $httpProvider, $rootScopeProvider) {
      RestangularProvider.setBaseUrl('https://grasshoper-ghbluefin.rhcloud.com/grasshoper-core/'); 
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
                //console.log(pendingRequests,'loading data (show indicator)');
                var rootScope = runInjector ? runInjector.get('$rootScope') : {};
                rootScope.silentAjaxs =[];
                if(!(rootScope.silentAjaxs.indexOf(what) > -1 ) )
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
  }
]).config(function($stateProvider, $urlRouterProvider) {
  $stateProvider
    .state('home', {
      url: '/home',

          templateUrl: 'templates/home.html'
    })
    .state('cart', {
      url: '/cart',
      views: {
        'cart': {
          templateUrl: 'templates/home.html',
          controller: 'HomeController'
        }
      }
    });

  $urlRouterProvider.otherwise("/home");

});




