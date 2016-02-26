/**
 * 
 */
package br.com.ciaware.springmvc.ensaio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CIAware :: Centro de Informatiza��es e An�lises
 * ----------------------------------------------- 
 *
 * @author Fl�vio Barbosa (fbarb_000, 20/01/2016)
 * Responsabilidade da classe: 
 * Responder as requisi��es � pagina principal 
 *
 *
 */
@Controller
public class HomeController  {

	@RequestMapping("/")
	public String index() {

		return "home";
	}
}
