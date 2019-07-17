angular.module("vraptor").controller('TodoCtrl', [ '$scope', 'TodoService', function($scope, TodoService) {
	$scope.todo = new TodoService();
	
	$scope.add = function(todo) {
		TodoService.save(todo,function(){
			$scope.todo = new TodoService();		
		});
	};
	
	$scope.todos = TodoService.query(); 
	
} ]);