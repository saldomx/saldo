package com.mxinteligente.infra.model.nego.impl;


import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mxinteligente.infra.model.dao.GenDaoI;
import com.mxinteligente.infra.model.nego.GenServicioI;

@Service("genServicio")
public class GenServicio implements GenServicioI{

	@Autowired(required=true)
	private GenDaoI genDao;
	
	public boolean insertar(Object obj){
		return genDao.insertar(obj);
	}
	public boolean actualizar(Object obj){
		return genDao.actualizar(obj);
	}
	
	public boolean eliminar(Object obj){
		return genDao.eliminar(obj);
	}
	
	public boolean eliminarPorId(Serializable id){
		return genDao.eliminarPorId(id);
	}
	
	public boolean insertarActualizar(Object obj){
		return genDao.insertarActualizar(obj);
	}
	
	public Object buscarPorId(Serializable id){
		return genDao.buscarPorId(id);
	}
	
	public List buscarAll(){
		return genDao.buscarAll();
	}
	
	public List buscarPorEjemplo(Object exampleInstance,String... excludeProperty){
		return genDao.buscarPorEjemplo(exampleInstance, excludeProperty);
	}
	
	public List buscarPorEjemploLike(Object exampleInstance,String... excludeProperty){
		return genDao.buscarPorEjemploLike(exampleInstance, excludeProperty);
	}	
	public void setPersistentClass(Class clase){
		genDao.setPersistentClass(clase);
	}
		
	public String getEstado() {		
		return genDao.getEstado();
	}
	public List<Object> buscarPorEjemploLikeForeign(Object exampleInstance,Vector<String> foreign, String... excludeProperty) {		
		return genDao.buscarPorEjemploLikeForeign(exampleInstance, foreign, excludeProperty);
	}
	public List<Object> buscarPorCriterio(Criterion... criterion) {
		return genDao.buscarPorCriterio(criterion);
	}
	public int contarTodos(Object ejemplo, String... excludeProperties) {
		return genDao.contarTodos(ejemplo,excludeProperties);
	}
	public int contarTodosLike(Object ejemplo, String... excludeProperties) {
		return genDao.contarTodosLike(ejemplo, excludeProperties);
	}
	public int contarTodosLikeForeign(Object ejemplo,Vector<String> foreign, String... excludeProperties) {
		return genDao.contarTodosLikeForeign(ejemplo, foreign, excludeProperties);
	}
	public int contarTodos() {
		return genDao.contarTodos();
	}	
	public List consultaHQL(String query) {		
		return genDao.consultaHQL(query);
	}
	public List buscarObjeto(Object obj) {
		return genDao.buscarPorObjeto(obj);
		
	}
}
