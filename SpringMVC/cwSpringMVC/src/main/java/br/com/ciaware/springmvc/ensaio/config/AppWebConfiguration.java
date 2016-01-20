/**
 * 
 */
package br.com.ciaware.springmvc.ensaio.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import br.com.ciaware.springmvc.ensaio.controller.HomeController;

/**
 * CIAware :: Centro de Informatiza��es e An�lises
 * ----------------------------------------------- 
 *
 * @author Fl�vio Barbosa (fbarb_000, 20/01/2016)
 * Responsabilidade da classe: Configura��es da aplica��o para Spring.
 *
 *
 */
@EnableWebMvc
@ComponentScan(basePackageClasses={HomeController.class})
public class AppWebConfiguration {
	
}
