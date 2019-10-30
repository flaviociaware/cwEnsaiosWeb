package br.com.caelum.vraptor.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;

@Controller
public class IndexController {

	private final Result result;

	/**
	 * @deprecated CDI eyes only
	 */
	protected IndexController() {
		this(null);
	}

	@Inject
	public IndexController(Result result) {
		this.result = result;
	}

	@Path("/")
	public void index() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		result.include("variable", "VRaptor!");
		result.include("title", "Meu title");
		result.include("description", "Descrição dinâmica " + sdf.format(Calendar.getInstance().getTime()));
	}
}