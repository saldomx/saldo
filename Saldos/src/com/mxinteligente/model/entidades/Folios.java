package com.mxinteligente.model.entidades;

import java.math.BigInteger;

public class Folios {

	
	private BigInteger folio;
	private String tipo;
	private String usuario;
	public BigInteger getFolio() {
		return folio;
	}
	public void setFolio(BigInteger folio) {
		this.folio = folio;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
}
