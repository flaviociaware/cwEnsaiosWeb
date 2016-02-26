/**
 * 
 */
package br.com.ciaware.springmvc.ensaio.dal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.ciaware.springmvc.ensaio.models.Produto;

/**
 * CIAware :: Centro de Informatizações e Análises
 * ----------------------------------------------- 
 *
 * @author Flávio Barbosa (fbarb_000, 20/01/2016)
 * Responsabilidade da classe:
 * Acesso a dados dos objetos de produto.
 *
 */
@Repository
@Transactional
public class ProdutoDao {

	@PersistenceContext
	private EntityManager entityManager; 
	
	public void gravar(Produto produto) {
		entityManager.persist(produto);
	}

	/**
	 * @author Flávio Barbosa (fbarb_000, 20/01/2016)
	 * Objetivo: listar produtos 
	 *
	 * @return
	 *
	 */
	public List<Produto> listar() {
		return entityManager.createQuery("Select p from Produto p", Produto.class).getResultList();
	}
}
