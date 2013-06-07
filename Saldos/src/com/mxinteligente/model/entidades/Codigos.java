package com.mxinteligente.model.entidades;

import java.math.BigDecimal;

public class Codigos {
	
	private int id;
	private String codigo;
	private BigDecimal monto;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	

}
