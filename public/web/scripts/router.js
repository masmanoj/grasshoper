
angular.module('router', []).config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when('/main', {templateUrl: 'views/main.html'})
    .when('/admin/tags', {templateUrl: 'views/admin/tags.html'})
    .when('/admin/users', {templateUrl: 'views/admin/userlist.html'})
    .when('/admin/users/editser/:userId', {templateUrl: 'views/admin/edituser.html'})
    .when('/product/createnew', {templateUrl: 'views/manage/createproduct.html'})
    .when('/product/editproduct/:productId', {templateUrl: 'views/manage/createproduct.html'})
    .when('/manage/products', {templateUrl: 'views/manage/productlist.html'})
    .when('/manage/orders', {templateUrl: 'views/manage/orderlist.html'})
    .when('/manage/reports/:reportname', {templateUrl: 'views/manage/reports.html'})
    .when('/order/editorder/:orderId', {templateUrl: 'views/manage/editorder.html'})
    .when('/user/usersettings', {templateUrl: 'views/usersettings.html'})
	.otherwise({redirectTo: '/main'});
  }]);