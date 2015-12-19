(function () {
    require.config({
        waitSeconds: 30,
        paths: {
            'router': 'router',
            'controllers':'controllers/controllers',
            'DashBoardMainController':'controllers/DashBoardMainController',
            'HomeController':'controllers/HomeController',
            'TagsController' : 'controllers/TagsController',

            'services': 'services/services',
            'SessionMgr': 'services/SessionMgr',


            'app':'dashboard'
        },
        shim: {
        	'router': { exports: 'router' },
        	'app': { exports: 'app' },

            'app': {
                deps: [
                
                    'router',
                    'controllers',
                    'DashBoardMainController',
                    'HomeController',
                    'TagsController',

                    'services',
                    'SessionMgr'
                    
                ],
                exports: 'dashboard'
            }
        },
    });

   require(['dashboard'], function (componentsInit) {
    });
}());

/*
require(['scripts/router'], function(App) {
  // app module is available here
  // you can start your application now
  // this is immediately called because
  // we used `require` instead of `define`
  // to define this module.
});*/


