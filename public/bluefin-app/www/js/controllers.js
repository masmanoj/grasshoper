//services
angular.module('bluefin.controllers', [])


//controllers
.controller('MainController', [ '$scope', '$ionicModal', 'localStorageService', 'Restangular', '$rootScope', 'SessionMgr', '$state', '$ionicSideMenuDelegate','$ionicPopover', 'CartMgr',
  function (scope, $ionicModal, localStorageService, Restangular, $rootScope, SessionMgr, $state, $ionicSideMenuDelegate, $ionicPopover, CartMgr) { 
    scope.goHome = function(){
      console.log("go Home");
     // $state.go("home");
    }
    scope.toggleLeft = function() {
      $ionicSideMenuDelegate.toggleLeft();
    };
    //to make sure the side menu donot flicker before loading
    document.getElementById("mainPanel").style.display='';

    $ionicPopover.fromTemplateUrl('cart.html', {
      scope: scope
    }).then(function(cartPopOver) {
      scope.cartPopOver = cartPopOver;
    });
    scope.openCart = function($event) {
      scope.cartPopOver.show($event);
    };
    scope.closeCart = function($event) {
      scope.cartPopOver.hide($event);
    };
    scope.removeFromCart = function(index){
      CartMgr.removeFromCart(index);
    }
  }
])
.controller('HomeController', [ '$scope', 'Restangular', '$rootScope', 'CartMgr', '$ionicModal',
  function (scope, Restangular, $rootScope, CartMgr,  $ionicModal){
    scope.moreDataCanBeLoaded = true;
    scope.limit =10;
    scope.offset =0
    scope.products=[];
    
      
      CartMgr.addToCart({name : "Bew Hdsfdfsds", quantityUnit:"Kg", pricePerUnit:75,productUid:"as"},3);
      CartMgr.addToCart({name : "Jdf Dffddf", quantityUnit:"Kg", pricePerUnit:55,productUid:"as"},5);
     // ({itemName : "Bew Hdsfdfsds", quantity: 3 , qtyUnit:"Kg", amount:75});
     // ({itemName : "Bew Hdsfdfsds", quantity: 3 , qtyUnit:"Kg", amount:75});
      //({itemName : "Bew Hdsfdfsds", quantity: 3 , qtyUnit:"Kg", amount:75});

    scope.loadProducts = function(){
      Restangular.all("stage/search-items").getList({l:scope.limit+1,o:scope.offset})
        .then(function(data) {
          var products = data.plain();
          if( products.length <= scope.limit){
            scope.moreDataCanBeLoaded = false;
          }else{
            products.splice(products.length-1, 1);
          }
          Array.prototype.push.apply(scope.products,products);
          scope.offset += scope.limit;
          scope.$broadcast('scroll.infiniteScrollComplete');

      });
    }

    scope.loadStage = function(){
      Restangular.all("stage/").customGET()
        .then(function(data) {
          scope.stageData = data.plain();
          $rootScope.currencyCode = scope.stageData.currencyCode;
      });
    }
    

    scope.$on('$stateChangeSuccess', function() {
      console.log("at Home");
      scope.loadStage();
    });

    scope.toggleShowCategories = function(){
      if(scope.showSort) scope.showSort = false;
      scope.showCategories = scope.showCategories ? false : true;
    };
    scope.toggleShowSort = function(){
      if(scope.showCategories ) scope.showCategories = false;
      scope.showSort = scope.showSort ? false : true;
    };
    $ionicModal.fromTemplateUrl('add-tocart-modal.html', {
        scope: scope,
        animation: 'slide-in-down'
    }).then(function (modal) {
        scope.addToCartModel = modal;
    });

    scope.openAddToCartModel = function(){
      scope.addToCartModel.show();
    }

    scope.closeAddToCartModal = function(){
      scope.addToCartModel.hide();
    }
  }
])
.controller('LoginController', [ '$scope', '$ionicModal', 'localStorageService', 'Restangular', '$rootScope', 'SessionMgr',
  function (scope, $ionicModal, localStorageService, Restangular, $rootScope, SessionMgr) { 
  scope.authCredentials = {}

  if (localStorageService.get("userCred")) {
     var authCredentials = localStorageService.get("userCred");
     console.log("authCredentials",authCredentials);

     localStorageService.set("userCred", null);
     Restangular.all("authentication").post(authCredentials)
          .then(function(data) {
            if(data.authenticated){
              if (SessionMgr.get(data)) {
                scope.currentSession = SessionMgr.get(data);
                $rootScope.isAutherised = true;
                $rootScope.loginNeeded = false;
                $rootScope.loginCheckFinished = true;
              }
            }else{
              localStorageService.set("userCred", null);
              localStorageService.remove("userCred");


              $rootScope.loginCheckFinished = true;
              $rootScope.loginNeeded = true;
            }
        });

  }else{
    $rootScope.loginCheckFinished = true;
    $rootScope.loginNeeded = true;

  }


  scope.authenticate = function(){
    //console.log(scope.authCredentials);
    Restangular.all("authentication").post(scope.authCredentials)
        .then(function(data) {
          if(data.authenticated){
            if (SessionMgr.get(data)) {
              scope.currentSession = SessionMgr.get(data);
              $rootScope.isAutherised = true;
              scope.authCredentials = {};
              }
          }else{
            localStorageService.set("userCred", null);
            localStorageService.remove("userCred");
          }
      });
  };

  scope.logout = function(){
    scope.currentSession = null;
    $rootScope.isAutherised = false;
    Restangular.all("authentication/logout").post()
        .then(function(data) {
          scope.currentSession = null;
        SessionMgr.get(data);
        localStorageService.set("userCred", null);
        localStorageService.remove("userCred");
      });
    
  }

  /*localStorageService.set("userSession", "YES");

  if (localStorageService.get("userSession")) {
     alert(localStorageService.get("userSession"));
  }*/

}])