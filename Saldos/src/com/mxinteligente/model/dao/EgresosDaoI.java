package com.mxinteligente.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.mxinteligente.infra.model.dao.GenericoDaoI;
import com.mxinteligente.model.entidades.Categresos;
import com.mxinteligente.model.entidades.Egresos;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.entidades.Producto;

public interface EgresosDaoI extends GenericoDaoI<Egresos,Serializable>{

	public int insertarEgreso(Egresos gasto);
	
	public List<Map> obtenerTodosEgresos(Usuarios user, int min, int max);
	
	public int obtenerTotalTodosEgresos(Usuarios user);
	
	public List<Map> obtenerEgresosPorCategoria(Categresos cat, int min, int max);
	
	public int obtenerTotalEgresosPorCategoria(Categresos cat);
	
	public BigDecimal sumaEgresos(Usuarios user);
	
	public int transferir(Egresos e, Usuarios user, Usuarios userTrans);
	
	public int retiro(BigDecimal retiro, BigDecimal comision, Usuarios user, Usuarios agencia, Usuarios admin);

	public int comprar(Egresos e, Usuarios user, Usuarios userTrans, Producto p);

	
	public BigInteger obtenerFolio(Session ses);

}
