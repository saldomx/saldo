package com.mxinteligente.model.entidades;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Comprador {

	
	 private Integer id;
     private String email;
     private String nombre;
     private String password;
     private String msg;
     private Long statusTrasnferencia;


     
     
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPassword() {
		return password;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getStatusTrasnferencia() {
		return statusTrasnferencia;
	}
	public void setStatusTrasnferencia(Long statusTrasnferencia) {
		this.statusTrasnferencia = statusTrasnferencia;
	}
     
     
     
}
