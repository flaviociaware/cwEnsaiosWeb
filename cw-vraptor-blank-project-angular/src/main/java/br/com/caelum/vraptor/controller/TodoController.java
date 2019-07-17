package br.com.caelum.vraptor.controller;

import javax.inject.Inject;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.model.Todo;
import br.com.caelum.vraptor.repository.TodoRepository;
import br.com.caelum.vraptor.serialization.gson.WithoutRoot;
import br.com.caelum.vraptor.view.Results;

@Controller
@Path("/todo")
public class TodoController {

	private final Result result;
	private final TodoRepository todoRepository;

	/**
	 * @deprecated CDI eyes only
	 */
	protected TodoController() {
		this(null, null);
	}

	@Inject
	public TodoController(Result result, TodoRepository todoRepository) {
		this.result = result;
		this.todoRepository = todoRepository;
	}

	@Get("")
	public void get() {
		result.use(Results.json()).withoutRoot().from(todoRepository.findAll())
				.serialize();
	}

	@Get("/{todo.id}")
	public void getOne(Todo todo) {
		result.use(Results.json()).withoutRoot()
				.from(todoRepository.find(todo.getId())).serialize();

	}

	@Consumes(value = "application/json", options = WithoutRoot.class)
	@Post("")
	public void create(Todo todo) {
		result.use(Results.json()).withoutRoot()
				.from(todoRepository.create(todo)).serialize();

	}

	@Consumes(value = "application/json", options = WithoutRoot.class)
	@Put("")
	public void update(Todo todo) {
		result.use(Results.json()).withoutRoot()
				.from(todoRepository.update(todo)).serialize();

	}

	@Delete("/{todo.id}")
	public void delete(Todo todo) {
		result.use(Results.json()).withoutRoot()
				.from(todoRepository.delete(todo.getId())).serialize();

	}

}
