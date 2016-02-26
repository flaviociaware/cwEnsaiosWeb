/**
 * 
 */
package br.com.ciaware.springmvc.ensaio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CIAware :: Centro de Informatizações e Análises
 * ----------------------------------------------- 
 *
 * @author Flávio Barbosa (fbarb_000, 20/01/2016)
 * Responsabilidade da classe: 
 * Responder as requisições à pagina principal 
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
