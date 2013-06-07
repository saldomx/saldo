package com.mxinteligente.model.entidades;

import java.util.Date;

public class UsuariosPuntos {

	private UsuariosPuntosId id;
	private String emailVendor;
	private Integer puntos;
	private Date fechaPuntos;
	private Date fechaRedencion;
	private String redimido;
	
	
	public String getEmailVendor() {
		return emailVendor;
	}
	public void setEmailVendor(String emailVendor) {
		this.emailVendor = emailVendor;
	}
	public Integer getPuntos() {
		return puntos;
	}
	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
	}
	public Date getFechaPuntos() {
		return fechaPuntos;
	}
	public void setFechaPuntos(Date fechaPuntos) {
		this.fechaPuntos = fechaPuntos;
	}
	public Date getFechaRedencion() {
		return fechaRedencion;
	}
	public void setFechaRedencion(Date fechaRedencion) {
		this.fechaRedencion = fechaRedencion;
	}
	public String getRedimido() {
		return redimido;
	}
	public void setRedimido(String redimido) {
		this.redimido = redimido;
	}
	public UsuariosPuntosId getId() {
		return id;
	}
	public void setId(UsuariosPuntosId id) {
		this.id = id;
	}
}
