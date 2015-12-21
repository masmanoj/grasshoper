/*(function () {
    require.config({
        waitSeconds: 30,
        paths: {
        	'jquery': '../bower_components/jquery/dist/jquery.min',
        	'bootstrap':'../bower_components/bootstrap/dist/js/bootstrap.min',
        	'angular':'../bower_components/angular/angular.min',
        	'angularFileUpload':'../bower_components/angularjs-file-upload/angular-file-upload.min',
        	'angularRoute':'../bower_components/angular-route/angular-route.min',
        	'underscore':'../bower_components/underscore/underscore-min',
        	'restangular':'../bower_components/restangular/dist/restangular.min',
        	'angularTranslate':'../bower_components/angular-translate/angular-translate.min',
        	'angularFileLoader':'../bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.min',
            'router': 'router',
            'controllers':'controllers/controllers',
            'DashBoardMainController':'controllers/DashBoardMainController',
            'HomeController':'controllers/HomeController',
            'TagsController' : 'controllers/TagsController',
            'ProductsController':'controllers/ProductsController',

            'services': 'services/services',
            'SessionMgr': 'services/SessionMgr'


           // 'app':'dashboard'
        },
        shim: {
        	'angular': {exports:'angular'},
        	'jquery' : {exports : 'jquery'},

        	'bootstrap' : { deps: [ 'jquery' ], exports: 'bootstrap' },

        	'angularRoute': {  deps: [ 'angular' ], exports:  'angularRoute'},
        	'restangular' : { deps: ['angular'], exports:  'restangular'},
        	'angularFileUpload': { deps: ['angular'], exports:  'angularFileUpload'},
        	'angularTranslate': { deps: ['angular'], exports:  'angularTranslate'},
        	'angularFileLoader': { deps: ['angular', 'angularTranslate'], exports: 'angularFileLoader' },
        	'router': { deps: ['angular'] , exports: 'router'},
        	'controllers': { deps: ['angular'],exports: 'controllers' },
        	'DashBoardMainController': { deps: ['angular'], exports: 'DashBoardMainController'  },
        	'HomeController': { deps: ['angular'], exports: 'HomeController' },
        	'TagsController': { deps: ['angular'], exports:  'TagsController'},
        	'ProductsController': { deps: ['angular'], exports:  'ProductsController'},

        	'services': { deps: ['angular'] , exports:'services'},
        	'SessionMgr': { deps: ['angular'] , exports:'SessionMgr'},
            'dashboard': {
                deps: [
                	'angular',

                	'jquery',
                	'bootstrap',
                	
                	'angularFileUpload',
                	'angularRoute',
                	'underscore',
                	'restangular',
                	'angularTranslate',
                	'angularFileLoader',
                    'router',
                    'controllers',
                    'DashBoardMainController',
                    'HomeController',
                    'TagsController',
                    'ProductsController',

                    'services',
                    'SessionMgr'
                    
                ],
                exports: 'dashboard'
            }
        },
    });

   

   require(['dashboard'], function (dashboard) {
    });

   require(['jquery'], function( $ ) {
	   	$(document).ready(function(){
					$('#loaderDiv').fadeOut();
				});
	});
   
}());

/*
define(['app'], function (app) {
	
});

require(['scripts/router'], function(App) {
  // app module is available here
  // you can start your application now
  // this is immediately called because
  // we used `require` instead of `define`
  // to define this module.
});*/


