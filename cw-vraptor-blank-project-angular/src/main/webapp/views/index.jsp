<header id="header">
	<h1>Todos</h1>
	<form id="todo-form" ng-submit="add(todo)">
		<input ng-model="todo.title" id="new-todo"
			placeholder="O que precisa ser feito?" autofocus>
	</form>
	<a ui-sref="list">Lista de tarefas</a>
</header>