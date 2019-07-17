angular.module("vraptor").service("TodoService", [ '$resource', function($resource) {
	return $resource("todo/:id", {
		id : '@_id'
	}, {
		update : {
			method : 'PUT'
		}
	});
} ]);