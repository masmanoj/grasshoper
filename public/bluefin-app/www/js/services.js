//services
angular.module('bluefin.services', [])

.service('SessionMgr',[ '$rootScope', 'Restangular', function($rootScope, Restangular){
  var EMPTY_SESSION = {};
  this.get = function (data) {
        if (data.shouldRenewPassword) {
            Restangular.setDefaultHeaders({'Authorization': 'Custom '+data.sessionKey});
        } else{
          $rootScope.sessionData = {
                userId: data.userId,
                authenticationKey: data.sessionKey,
                userPermissions: data.permissions,
                name: data.name
            };
            var user = {
              userId: data.userId,
                userPermissions: data.permissions,
                name: data.name
            }
            if(data.sessionKey)
              Restangular.setDefaultHeaders({'Authorization': 'Custom '+data.sessionKey});
            else
              Restangular.setDefaultHeaders({'Authorization': ''});
            return {user: user};
        };
    }
    this.clear = function () {
      $rootScope.sessionData = null;
        Restangular.setDefaultHeaders({'Authorization': ''});
        return EMPTY_SESSION;
    };

}])
.service('CartMgr',[ '$rootScope', function($rootScope){
  var EMPTY_SESSION = {};
  $rootScope.cart = {totalAmount:0.00,cartItems:[]};
    this.addToCart = function(item, qty){
      var amount = item.pricePerUnit * qty;
      $rootScope.cart.totalAmount += amount;
      $rootScope.cart.cartItems.push({itemName : item.name, 
        quantity:  qty, 
        qtyUnit:item.quantityUnit, 
        amount:amount,
        itemId : item.productUid
      });
    } 
    this.removeFromCart = function (index) {
      var amount = $rootScope.cart.cartItems[index].amount;
      $rootScope.cart.totalAmount -= amount;
      $rootScope.cart.cartItems.splice(index, 1);
    };

}])
