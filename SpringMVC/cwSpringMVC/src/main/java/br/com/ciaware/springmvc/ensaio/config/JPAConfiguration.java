/**
 * 
 */
package br.com.ciaware.springmvc.ensaio.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * CIAware :: Centro de Informatizações e Análises
 * -----------------------------------------------
 *
 * @author Flávio Barbosa (fbarb_000, 20/01/2016) Responsabilidade da classe:
 *         Configuração da JPA.
 *
 */
@EnableTransactionManagement
public class JPAConfiguration {

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

		JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUsername("root");
		dataSource.setPassword("");
		dataSource.setUrl("jdbc:mysql://localhost:3306/ensaiospring");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");

		Properties props = new Properties();
		props.setProperty("hibernate.dialect",
				"org.hibernate.dialect.MySQL5Dialect");
		props.setProperty("hibernate.show_sql", "true");
		props.setProperty("hibernate.hbm2ddl.auto", "update");

		factory.setJpaVendorAdapter(jpaVendorAdapter);
		factory.setDataSource(dataSource);
		factory.setJpaProperties(props);
		factory.setPackagesToScan("br.com.ciaware.springmvc.ensaio.models");

		return factory;
	}

	@Bean
	public JpaTransactionManager transactionManager(
			EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
