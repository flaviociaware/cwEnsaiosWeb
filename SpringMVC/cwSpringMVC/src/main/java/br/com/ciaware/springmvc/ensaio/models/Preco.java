/**
 * 
 */
package br.com.ciaware.springmvc.ensaio.models;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * CIAware :: Centro de Informatizações e Análises
 * ----------------------------------------------- 
 *
 * @author Flávio Barbosa (fbarb_000, 20/01/2016)
 * Responsabilidade da classe:
 * Preco por tipo.
 *
 */
@Embeddable
public class Preco {
	private BigDecimal valor;
	
	// @Enumerated(EnumType.STRING)
	private ETipoEntrega tipoEntrega;

	public Preco() {
		
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public ETipoEntrega getTipoEntrega() {
		return tipoEntrega;
	}
	public void setTipoEntrega(ETipoEntrega tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
	}
	
	
	
}

