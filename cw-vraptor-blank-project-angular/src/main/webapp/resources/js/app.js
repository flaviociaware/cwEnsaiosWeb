var app = angular.module("vraptor", [ 'ngResource', 'ui.router' ]);

app.config([ '$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

	$urlRouterProvider.otherwise('/');

	$stateProvider.state('todo', {
		url : '/',
		templateUrl : 'views/index.jsp',
		controller: 'TodoCtrl'
	}).state('list', {
		url : '/list',
		templateUrl : 'views/list.jsp',
		controller: 'TodoCtrl'
	});

} ]);