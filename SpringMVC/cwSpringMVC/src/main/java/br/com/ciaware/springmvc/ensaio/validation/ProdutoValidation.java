/**
 * 
 */
package br.com.ciaware.springmvc.ensaio.validation;


import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import br.com.ciaware.springmvc.ensaio.models.Produto;

/**
 * CIAware :: Centro de Informatizações e Análises
 * ----------------------------------------------- 
 *
 * @author Flávio Barbosa (fbarb_000, 20/01/2016)
 * Responsabilidade da classe:
 * Regras de negócio de produto.
 *
 */
public class ProdutoValidation implements Validator{
	

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class<?> clazz) {
		return Produto.class.isAssignableFrom(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "titulo", "field.required");
		ValidationUtils.rejectIfEmpty(errors, "descricao", "field.required");
		
		if ((target !=null) && (target instanceof Produto) && (((Produto)target).getPaginas()<=0)) {
			errors.rejectValue("paginas", "field.required");
		}
	}
}
