package com.mxinteligente.infra.model.nego;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import org.hibernate.criterion.Criterion;

import com.mxinteligente.model.entidades.Usuarios;

public interface GenServicioI {
	public void setPersistentClass(Class clase);
	public String getEstado();	
	
	public boolean insertar(Object obj);
	public boolean eliminar(Object obj);
	public boolean eliminarPorId(Serializable id);
	public boolean actualizar(Object obj);
	public boolean insertarActualizar(Object obj);
	
	public List<Object> buscarAll();	
	public Object buscarPorId(Serializable id);
	public List<Object> buscarPorEjemplo(Object ejemplo, String... excluirPropiedad);	
	public List<Object> buscarPorEjemploLike(Object exampleInstance,String... excludeProperty);	
	public List<Object> buscarPorEjemploLikeForeign(Object exampleInstance, Vector<String> foreign, String... excludeProperty);	
	public List<Object> buscarPorCriterio(Criterion... criterion);
	
	public int contarTodos(Object ejemplo, String... excludeProperties);	
	public int contarTodosLike(Object ejemplo, String... excludeProperties);	
	public int contarTodosLikeForeign(Object ejemplo, Vector<String> foreign,String... excludeProperties);	
	public int contarTodos();	
		
	public List consultaHQL(String query);
	
	public List buscarObjeto(Object obj);
	

}