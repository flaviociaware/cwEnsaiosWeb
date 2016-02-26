/**
 * 
 */
package br.com.ciaware.springmvc.ensaio.models;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * CIAware :: Centro de Informatizações e Análises
 * ----------------------------------------------- 
 *
 * @author Flávio Barbosa (fbarb_000, 20/01/2016)
 * Responsabilidade da classe:
 * Modelo de produto
 *
 */
@Entity
public class Produto {
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	private String descricao;
	private Integer paginas;
	private Double preco;
	
	@ElementCollection
	private List<Preco> precos;
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getPaginas() {
		return paginas;
	}

	public void setPaginas(Integer paginas) {
		this.paginas = paginas;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}
	
	public List<Preco> getPrecos() {
		return precos;
	}



	public void setPrecos(List<Preco> precos) {
		this.precos = precos;
	}



	@Override
    public String toString() {
        return "Produto [titulo=" + titulo + ", descricao=" + descricao + ", paginas=" + paginas + "]";
    }
	
	
}
