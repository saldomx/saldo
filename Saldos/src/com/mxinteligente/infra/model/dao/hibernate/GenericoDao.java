package com.mxinteligente.infra.model.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ObjectDeletedException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.PropertyValueException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransactionException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.mxinteligente.infra.model.dao.GenericoDaoI;

public abstract class GenericoDao<T, ID extends Serializable> implements GenericoDaoI<T, ID>{

	
	private Class<T> persistentClass;
	protected SessionFactory sessionFactory;

	private String estado;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session getSession() {
		return SessionFactoryUtils.getSession(sessionFactory, true);
	}	

	public GenericoDao() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}
	
	public void setPersistentClass(Class clase){		
		this.persistentClass = clase;
	}
	
	public void flush(){
		getSession().flush();
	} 
	
	public void clear(){
		getSession().clear();
	}	
	
	public String getEstado(){
		return estado;
	}
	
	protected void setEstado(String estado){
		this.estado = estado;
	}

	@Transactional
	public boolean insertar(T obj) {
		try {			
			getSession().save(obj);
			flush();
			clear();
			return true;
		}
		catch(ConstraintViolationException e){
			setEstado("Violación en restricción de integridad");	
		}catch(PropertyNotFoundException e){
			setEstado("Propiedad no encontrada");	
		}catch(PropertyValueException e){
			setEstado("Valor no permitido, el objeto nlo puede ser persistido"); 
		}catch(DataIntegrityViolationException e){
			setEstado("Valor no permitido, error en integridad de datos");	
		}catch(TransactionException e){
			setEstado("Fallo el inicio la transacción");	
		}catch(HibernateException e){
			e.printStackTrace();
			setEstado("Error en la capa de persistencia");		
		}
		return false;
	}

	@Transactional
	public boolean eliminar(T obj) {
		try {			
			getSession().delete(obj);
			flush();
			clear();
			return true;
		}catch(ConstraintViolationException e){
			setEstado("Violación en restricción de integridad");	
		}catch(DataIntegrityViolationException e){
			setEstado("Error en integridad de datos");		
		}catch(ObjectDeletedException e){
			setEstado("Objeto eliminado, no es posible realizar trasacciones"); 
		}catch(HibernateException e){
			e.printStackTrace();
			setEstado("Error en la capa de persistencia");		
		}
		return false;
	}
	
	@Transactional
	public boolean eliminarPorId(ID id) {
		return eliminar(buscarPorId(id));		
	}

	@Transactional
	public boolean actualizar(T obj) {
		try {			
			getSession().update(obj);
			flush();
			clear();
			return true;
		}
		catch(ConstraintViolationException e){
			setEstado("Violación en restricción de integridad");	
		}catch(PropertyValueException e){
			setEstado("Valor no permitido, el objeto no puede ser persistido"); 
		}catch(DataIntegrityViolationException e){
			setEstado("Valor no permitido, error en integridad de datos"); 	
		}catch(HibernateException e){
			setEstado("Error en la capa de persistencia");		
		}
		return false;
	}
	
	@Transactional
	public boolean insertarActualizar(T obj) {
		try {			
			getSession().saveOrUpdate(obj);
			flush();
			clear();
			return true;
		}
		catch(ConstraintViolationException e){
			setEstado("Violación en restricción de integridad");	
		}catch(PropertyNotFoundException e){
			setEstado("Propiedad no encontrada");	
		}catch(PropertyValueException e){
			setEstado("Valor no permitido, el objeto no puede ser persistido"); 
		}catch(DataIntegrityViolationException e){
			setEstado("Valor no permitido, error en integridad de datos");	
		}catch(HibernateException e){
			setEstado("Error en la capa de persistencia");		
		}
		return false;
	}
	
	@Transactional(readOnly = true)
	public T buscarPorId(ID id) {
		try{
			
			return (T)getSession().get(getPersistentClass(), id);
		}catch(HibernateException e){
			setEstado("Error en la capa de persistencia");	
			return null;
		}
	};
	
	@Transactional(readOnly=true)
	public List<T> buscarPorEjemplo(T ejemplo, String... excluirPropiedad) {
		try{
			
			Criteria crit =	getSession().createCriteria(getPersistentClass());
			Example example = Example.create(ejemplo);
			
			for (String exclude : excluirPropiedad) {
				example.excludeProperty(exclude);
			}
			
			crit.add(example);
			return crit.list();
		}catch(HibernateException e){
			setEstado("Error en la capa de persistencia");			
		}
		return new LinkedList();
	}
	
	@Transactional(readOnly=true)
	public List<T> buscarPorEjemploLike(T exampleInstance, String... excludeProperty) {
		try{
			
			Criteria crit =	getSession().createCriteria(getPersistentClass());
			Example example = Example.create(exampleInstance).enableLike(MatchMode.ANYWHERE).ignoreCase();
			
			for (String exclude : excludeProperty) {
				example.excludeProperty(exclude);
			}
			
			crit.add(example);
			return crit.list();
		}catch(HibernateException e){
			setEstado("Error en la capa de persistencia");			
		}
		return new LinkedList();
	}
	
	@Transactional(readOnly=true)
	public List<T> buscarPorEjemploLikeForeign(T exampleInstance, Vector<String> foreign,String... excludeProperty){
		try{				
			Criteria crit =	getSession().createCriteria(getPersistentClass());
			Example example = Example.create(exampleInstance).enableLike(MatchMode.ANYWHERE).ignoreCase();
			
			for (String exclude : excludeProperty) {
				example.excludeProperty(exclude);
			}
			
			crit.add(example);
			
			for(String fk : foreign){
					try{
						crit.createCriteria(fk).add(Example.create(exampleInstance.getClass().getMethod(
						"get"+fk.substring(0, 1).toUpperCase()+fk.substring(1)).invoke(exampleInstance)));
					}catch(Exception ex){System.out.println("Error al consultar por llave foranea");}				
			}
			
			return crit.list();
		}catch(HibernateException e){
			setEstado("Error en la capa de persistencia");			
		}catch(Exception e){
			e.printStackTrace();			
			setEstado("Error");
		}
		return new LinkedList();
	}
	
	@Transactional(readOnly=true)
	public int contarTodos(T ejemplo,String... excludeProperties) {		
		try{
			Criteria criteria = getSession().createCriteria(getPersistentClass());
			Example example = Example.create(ejemplo);
			for(String ep: excludeProperties)
				example.excludeProperty(ep);
			criteria.add(example);
			criteria.setProjection(Projections.rowCount());
			return new Integer(criteria.uniqueResult()+"");
		}catch(HibernateException e){
			e.printStackTrace();
			setEstado("Error en la capa de persistencia");
		}
		return -1;
	}
	
	@Transactional(readOnly=true)
	public int contarTodosLike(T ejemplo,String... excludeProperties) {		
		try{
			Criteria criteria = getSession().createCriteria(getPersistentClass());
			Example example = Example.create(ejemplo).enableLike(MatchMode.ANYWHERE).ignoreCase();
			for(String ep: excludeProperties)
				example.excludeProperty(ep);
			criteria.add(example);			
			criteria.setProjection(Projections.rowCount());
			return new Integer(criteria.uniqueResult()+"");
		}catch(HibernateException e){
			setEstado("Error en la capa de persistencia");
		}
		return -1;
	}
	
	@Transactional(readOnly=true)
	public int contarTodosLikeForeign(T ejemplo, Vector<String> foreign ,String... excludeProperties) {		
		try{
			
			Criteria criteria = getSession().createCriteria(getPersistentClass());
			Example example = Example.create(ejemplo).enableLike(MatchMode.ANYWHERE).ignoreCase();
			for(String ep: excludeProperties)
				example.excludeProperty(ep);
			criteria.add(example);
			for(String fk : foreign){
				try{
					criteria.createCriteria(fk).add(Example.create(ejemplo.getClass().getMethod(
					"get"+fk.substring(0, 1).toUpperCase()+fk.substring(1)).invoke(ejemplo)));
				}catch(Exception ex){System.out.println("Error al consultar por llave foranea");}				
		}

			criteria.setProjection(Projections.rowCount());
			return new Integer(criteria.uniqueResult()+"");
		}catch(HibernateException e){
			e.printStackTrace();
			setEstado("Error en la capa de persistencia");
		}
		return 0;
	}
	
	@Transactional(readOnly=true)
	public int contarTodos() {		
		try{
			Criteria criteria = getSession().createCriteria(getPersistentClass());			
			criteria.setProjection(Projections.rowCount());
			return new Integer(criteria.uniqueResult()+"");
		}catch(HibernateException e){
			setEstado("Error en la capa de persistencia");
		}
		return -1;
	}

	@Transactional(readOnly=true)
	public List<T> buscarAll() {
		try{			
			return buscarPorCriterio();
		}catch(HibernateException e){
			setEstado("Error en la capa de persistencia");
		}
		return new LinkedList();
	}

	@Transactional(readOnly=true)
	public List<T> buscarPorCriterio(Criterion... criterion) {
		try{			
			Criteria crit = getSession().createCriteria(getPersistentClass());
			for (Criterion c : criterion) {
				crit.add(c);
			}
			return crit.list();
		}catch(HibernateException e){
			setEstado("Error en la capa de persistencia");
		}
		return new LinkedList();
	}
	
	@Transactional(readOnly = true)
	public List consultaHQL(String query) {
		return getSession().createQuery(query).list();
	}
	
	@Transactional(readOnly = true)
	public List<T> buscarPorObjeto(T filtro) {
		Vector<String> vacio = new Vector();
		Query query = getSession().createQuery(generaHqlListEqual(filtro, vacio, vacio));
		return query.list();
	}
	
	@Transactional(readOnly=true)
	public int contarTodosLikeForeignEqual(Object ejemplo, Vector<String> foreign,String... excludeProperties) {
		Query query = getSession().createQuery(generaHqlContarEqual(ejemplo, foreign));
		return new Integer(query.uniqueResult() + "");
	}

	protected String generaHqlContarEqual(Object ejemplo, Vector<String> associations ){
		return "Select count(*) "+generaHqlListEqual(ejemplo, new Vector<String>(), associations);
	}
	
	protected String generaHqlListEqual(Object ejemplo, Vector<String> orden, Vector<String> associations ){
		try{
			String hql = "from "+ejemplo.getClass().getName()+" we where ";			
			ClassMetadata md = getSession().getSessionFactory().getClassMetadata(ejemplo.getClass());
			Class id = Class.forName(md.getIdentifierType().getReturnedClass().getName());
			if(id.getName().contains("Id"))
				for( Field f: id.getDeclaredFields()){
					String getf = "get"+f.getName().substring(0,1).toUpperCase()+f.getName().substring(1);
					Object res = null;
					try{
						res =  id.getMethod( getf ).invoke( ejemplo.getClass().getDeclaredMethod( "getId" ).invoke(ejemplo) ) ;
					}catch(Exception e){;}
					if( res != null && !res.toString().equals("0") )
						hql += " lower( we.id."+f.getName()+" ) = '"+res.toString().toLowerCase()+"' and";
				}
			else{
				String getf = "get"+md.getIdentifierPropertyName().substring(0,1).toUpperCase()+md.getIdentifierPropertyName().substring(1);
				Object res = Class.forName(ejemplo.getClass().getName()).getDeclaredMethod(getf).invoke(ejemplo);
				if( res != null && !res.toString().equals("0") )
					hql += " lower ( we."+md.getIdentifierPropertyName()+") = '"+res.toString().toLowerCase()+"' and";
			}
			
			for(String name :  md.getPropertyNames()){
				if( !md.getPropertyType(name).isCollectionType() && !md.getPropertyType(name).isAssociationType() ){
					String getf = "get"+name.substring(0,1).toUpperCase()+name.substring(1);
					Object res = Class.forName(ejemplo.getClass().getName()).getDeclaredMethod(getf).invoke(ejemplo);
					if( res != null && !res.toString().equals("0") )
						hql += " lower( we."+name+" ) = '"+res.toString().toLowerCase()+"' and";
				}
			}
			
			for(String aso :associations){
				StringTokenizer t = new StringTokenizer(aso, ".");
				List<String> metodos = new LinkedList<String>();
				while (t.hasMoreTokens()) {
					String atrib = t.nextToken();
					metodos.add(("get"+atrib.substring(0, 1).toUpperCase()+atrib.substring(1)));
				}
				Object resultado = ejemplo;
				for (int contador = 0; contador<metodos.size() ; contador++) {
					String metodo = metodos.get(contador);
					Class c = resultado.getClass();
					resultado = c.getMethod(metodo).invoke(resultado);
					if(resultado != null && (contador == metodos.size()-1) )
						hql+=  " lower( we."+aso+" ) = '"+resultado.toString().toLowerCase()+"' and";
				}
			}
			
			if( hql.endsWith(" we where ") )
				hql= hql.substring(0, hql.length()-6);
			else
				hql = hql.substring(0, hql.length()-3);
			
			if( orden.size() > 0) hql+=" order by ";
			for(String c:orden)
				hql += " we."+c.substring(2)+  (c.substring(0, 1).equalsIgnoreCase("a")?" asc ":" desc " )+",";
			if( orden.size() > 0)
				hql=hql.substring(0, hql.length()-1);
			
			return hql;
		}catch (Exception e) {
			e.printStackTrace();
			return "from "+ejemplo.getClass().getName();
		}
	}
		
}

