package com.mxinteligente.model.entidades;

import java.util.Date;

public class OrdenesCompra {

	
	private long id;
	private Comprador comprador;
	private Producto producto;
	private Date fecha;
	private String atendido;
	private int cantidad;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Comprador getComprador() {
		return comprador;
	}
	public void setComprador(Comprador comprador) {
		this.comprador = comprador;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getAtendido() {
		return atendido;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public void setAtendido(String atendido) {
		this.atendido = atendido;
	}
	
	
	
}
