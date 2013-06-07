package com.mxinteligente.model.entidades;

public class UsuariosPuntosId implements java.io.Serializable {

	private String email;
	private String codigo;
	private int idEncuesta;
	private int indice;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public int getIdEncuesta() {
		return idEncuesta;
	}
	public void setIdEncuesta(int idEncuesta) {
		this.idEncuesta = idEncuesta;
	}
	public int getIndice() {
		return indice;
	}
	public void setIndice(int indice) {
		this.indice = indice;
	}
	
}
