
angular.module('router', []).config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when('/main', {templateUrl: 'views/main.html'})
    .when('/admin/tags', {templateUrl: 'views/admin/tags.html'})
    .when('/admin/users', {templateUrl: 'views/admin/userlist.html'})
    .when('/product/createnew', {templateUrl: 'views/manage/createproduct.html'})
    .when('/product/editproduct/:productId', {templateUrl: 'views/manage/createproduct.html'})
    .when('/manage/products', {templateUrl: 'views/manage/productlist.html'})
	.otherwise({redirectTo: '/main'});
  }]);