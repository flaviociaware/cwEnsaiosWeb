/**
 * 
 */
package br.com.ciaware.springmvc.ensaio.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.ciaware.springmvc.ensaio.dal.ProdutoDao;
import br.com.ciaware.springmvc.ensaio.models.ETipoEntrega;
import br.com.ciaware.springmvc.ensaio.models.Produto;
import br.com.ciaware.springmvc.ensaio.validation.ProdutoValidation;

/**
 * CIAware :: Centro de Informatizações e Análises
 * ----------------------------------------------- 
 *
 * @author Flávio Barbosa (fbarb_000, 20/01/2016)
 * Responsabilidade da classe:
 * Controlar lógica de produtos.
 *
 */

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

	
	@Autowired
	private ProdutoDao produtoDao;

	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		
		binder.addValidators(new ProdutoValidation());
		
		
		
	}
	
//	@RequestMapping("/form" )
//	public String form() {
//		return "produto/form";
//	}

	
	@RequestMapping(value="/form", method=RequestMethod.GET )
	public ModelAndView form() {
		
		ModelAndView result = new ModelAndView("produto/form");
		result.addObject("tiposEntrega", ETipoEntrega.values());
		
		return result;
	}
	
	//Não precisa porque o controller todo é /produtos @RequestMapping(value="/produtos", method=RequestMethod.POST)
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView gravar(@Valid Produto produto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		
		if (bindingResult.hasErrors()) {
			return form();
			
		}
		
		
		produtoDao.gravar(produto);
		return new ModelAndView("produto/sucesso");
		
	}

	//Não precisa porque o controller todo é /produtos @RequestMapping(value="/produtos", method=RequestMethod.GET)
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView listar(Produto produto) {
		List<Produto> produtos = produtoDao.listar();
	
		
		ModelAndView result = new ModelAndView("produto/listar");

		result.addObject("produtos", produtos);
		
		return result;
	}
}
