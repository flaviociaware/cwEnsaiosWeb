package br.com.caelum.vraptor.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.vraptor.model.Todo;

@ApplicationScoped
public class TodoRepository implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<Long, Todo> todoList;

	@PostConstruct
	public void init() {
		todoList = new HashMap<>();
	}

	public List<Todo> findAll() {
		return new ArrayList<>(todoList.values());
	}

	public Todo find(Long id) {
		return todoList.get(id);
	}

	public Todo create(Todo todo) {
		todo.setId(new Random().nextLong());
		todoList.put(todo.getId(), todo);
		return todo;
	}

	public Todo update(Todo todo) {
		todoList.put(todo.getId(), todo);
		return todo;
	}

	public Todo delete(Long id) {
		return todoList.remove(id);
	}

}
