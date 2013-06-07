package com.mxinteligente.infra.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import org.hibernate.criterion.Criterion;

public interface GenericoDaoI<T, ID extends Serializable> {

	public Class<T> getPersistentClass();
	public void setPersistentClass(Class clase);
	public void flush();
	public void clear();
	public String getEstado();	
	
	public boolean insertar(T obj);
	public boolean eliminar(T obj);
	public boolean eliminarPorId(ID id);
	public boolean actualizar(T obj);
	public boolean insertarActualizar(T obj);
	
	public List<T> buscarAll();	
	public T buscarPorId(ID id);
	public List<T> buscarPorEjemplo(T ejemplo, String... excluirPropiedad);	
	public List<T> buscarPorEjemploLike(T exampleInstance,String... excludeProperty);	
	public List<T> buscarPorEjemploLikeForeign(T exampleInstance, Vector<String> foreign, String... excludeProperty);	
	public List<T> buscarPorCriterio(Criterion... criterion);
	
	public int contarTodos(T ejemplo, String... excludeProperties);	
	public int contarTodosLike(T ejemplo, String... excludeProperties);	
	public int contarTodosLikeForeign(T ejemplo, Vector<String> foreign,String... excludeProperties);	
	public int contarTodos();	
		
	public List consultaHQL(String query);
	public List<T> buscarPorObjeto(T obj);

}