/**
 * 
 */
package br.com.ciaware.springmvc.ensaio.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import br.com.ciaware.springmvc.ensaio.controller.HomeController;
import br.com.ciaware.springmvc.ensaio.controller.ProdutoController;
import br.com.ciaware.springmvc.ensaio.dal.ProdutoDao;
import br.com.ciaware.springmvc.ensaio.models.Produto;

/**
 * CIAware :: Centro de Informatizações e Análises
 * ----------------------------------------------- 
 *
 * @author Flávio Barbosa (fbarb_000, 20/01/2016)
 * Responsabilidade da classe: Configurações da aplicação para Spring.
 *
 *
 */
@EnableWebMvc
@ComponentScan(basePackageClasses={HomeController.class, ProdutoDao.class, Produto.class})
public class AppWebConfiguration {
	
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/" );
		resolver.setSuffix(".jsp");
		return resolver;
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource result = new ReloadableResourceBundleMessageSource();
		result.setBasename("/WEB-INF/messages");
		result.setDefaultEncoding("UTF-8");
		result.setCacheSeconds(1);
		
		return result;
	}
	
}
