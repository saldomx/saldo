package com.mxinteligente.model.entidades;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Movimientos {
	
	
    private BigInteger folio;
    private String tipo;
    private String concepto;
    private BigDecimal cantidad;
    private String fecha;
    private String contraparte;
	
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
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getContraparte() {
		return contraparte;
	}
	public void setContraparte(String contraparte) {
		this.contraparte = contraparte;
	}


}
