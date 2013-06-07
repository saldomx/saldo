package com.mxinteligente.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mxinteligente.infra.model.dao.GenericoDaoI;
import com.mxinteligente.model.entidades.Catingresos;
import com.mxinteligente.model.entidades.Ingresos;
import com.mxinteligente.model.entidades.Usuarios;

public interface IngresosDaoI extends GenericoDaoI<Ingresos,Serializable>{

	public boolean insertarIngreso(Ingresos ingreso);
	
	public List<Map> obtenerTodosIngresos(Usuarios user, int min, int max);
	public int obtenerTotalTodosIngresos(Usuarios user);

	public List<Map> obtenerTodo(Usuarios user, int min, int max);
	
	public List<Map> obtenerIngresosPorCategoria(Catingresos cat, int min , int max);
	public int obtenerTotalIngresosPorCategoria(Catingresos cat);
	
	public BigDecimal sumaIngresos(Usuarios user);
	
	public Integer obtenerUltimoID(int user, int Cating);



}
