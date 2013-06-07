package com.mxinteligente.infra.model.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.zkoss.spring.SpringUtil;

import org.hibernate.mapping.Column;
import org.hibernate.impl.SessionFactoryImpl;

import com.mxinteligente.config.SpringApplicationContext;
import com.mxinteligente.infra.model.dao.BusquedaDaoI;

@Repository("busquedaDao")
public class BusquedaDao extends GenericoDao<Object, Serializable> implements BusquedaDaoI{
	
	private LocalSessionFactoryBean factoryBean;
	private Column columna;
	static Logger logger = LoggerFactory.getLogger(BusquedaDao.class);
	Criterion igual = null; 
	
	public Criterion setCriterionEqual(String filtro[], Object[] valor){
		int i = 0 ;
		
		for (String str : filtro){
			 igual = Restrictions.eq(str, valor[i]);
			i++;
		}
		
		return igual;
	}
	
	public Criterion getCriterionEqual(){
		return igual;
	}
	
	@Transactional(readOnly = true)
	public List buscarPorCamposCriterio(Object tabla, String join,
			String filtro[], Object[] valor, String filtroEq[], Object[] valorEq,  int ini, int max, String... campos) {
		
		factoryBean = (LocalSessionFactoryBean) SpringApplicationContext.getBean("&sessionFactory");

		
		List res = null;
		try{
		Criteria criteria = this.getSession().createCriteria(tabla.getClass());
		for (String prop : campos) {

			if (prop.contains("."))
				if (!prop.startsWith("id")) {
					String propiedad = prop.substring(0, prop.lastIndexOf("."));
					if (join.equals("inner"))
						criteria.createAlias(propiedad, propiedad,
								CriteriaSpecification.INNER_JOIN);
					else if (join.equals("left"))
						criteria.createAlias(propiedad, propiedad,
								CriteriaSpecification.LEFT_JOIN);
					else if (join.equals("full"))
						criteria.createAlias(propiedad, propiedad,
								CriteriaSpecification.FULL_JOIN);
				}
		}
		
		
		if (filtroEq!=null && filtroEq.length>0) {
			for(int i=0; i<filtroEq.length; i++){
				criteria.add(Restrictions.eq(filtroEq[i],  valorEq[i]));
			}
		}
		
		if (filtro!=null && filtro.length>0) {
			for(int i=0; i<filtro.length; i++){
			if(valor[i]!=null){
				if (valor[i] instanceof String) {
					criteria.add(Restrictions.like(filtro[i],  (String) valor[i], MatchMode.START));
				} else{
					criteria.add(Restrictions.sqlRestriction
						("{alias}."+ ((Column)factoryBean.getConfiguration().getClassMapping(tabla.getClass().getName()).getProperty(filtro[i]).getColumnIterator().next()).getName() +" like " + "'"+valor[i]+ "%'"));
				}
				//criteria.add(Restrictions.like(filtro[i], (String) valor[i], MatchMode.START));
				}
			}
		}
		

		criteria.setFirstResult(ini);
		criteria.setMaxResults(max);
		
		res = criteria.list();

		flush();
		clear();
		
		return res;
		}catch(NumberFormatException e){
			setEstado("Error en la clave del campo a filtrar, favor de verificar los datos");
			return null;
		}catch(Exception w){
			setEstado("Error al buscar por filtro, favor de verificar los datos");
			return null;
		}
		
		
		

	}
	
	@Transactional(readOnly = true)
	public int obetenerTotal(Object obj) {
		try{
		Criteria crit = getSession().createCriteria(obj.getClass());
		crit.setProjection(Projections.rowCount());

		flush();
		clear();
		
		return ((Integer) crit.list().get(0)).intValue();
	}catch(NumberFormatException e){
		setEstado("Error en la clave del campo a filtrar, favor de verificar los datos");
		return -1;
	}catch(Exception w){
		setEstado("Error al buscar por filtro, favor de verificar los datos");
		return -1;
	}
	
	
	}

	
	@Transactional(readOnly = true)
	public int obetenerTotalFiltro(Object obj, String[] filtros,  Object[] valores, String[] filtrosEq, Object[] valoresEq) {
		
		Criteria crit = getSession().createCriteria(obj.getClass());
		try{
		crit.setProjection(Projections.rowCount());
		
		if(filtrosEq!=null && filtrosEq.length>0 && valoresEq != null && valoresEq.length > 0){
			
			
			for(int i = 0; i < filtrosEq.length; i++){
			if(filtrosEq[i]!=null){
				if (filtrosEq[i].contains("."))
					if (!filtrosEq[i].startsWith("id")) {
						String propiedad = filtrosEq[i].substring(0, filtrosEq[i].lastIndexOf("."));
					
						crit.createAlias(propiedad, propiedad);
					
				}
					
					
					crit.add(Restrictions.eq(filtrosEq[i], valoresEq[i]));	
			 }
			}
		}
			
		
	if(filtros!=null && filtros.length>0 && valores != null && valores.length > 0){
		
		
		
		for(int i = 0; i < filtros.length; i++){
			
	if(filtros[i]!=null){
		if (filtros[i].contains("."))
			if (!filtros[i].startsWith("id")) {
				String propiedad = filtros[i].substring(0, filtros[i].lastIndexOf("."));
				
				crit.createAlias(propiedad, propiedad);
				
			}
		
			if (valores[i] instanceof String){
				crit.add(Restrictions.ilike(filtros[i], (String) valores[i], MatchMode.ANYWHERE));
			}
			else{
				crit.add(Restrictions.sqlRestriction
						//("{alias}.id.ctctClv like " + "'"+valores[i]+ "%'"));
						("{alias}."+ ((Column)factoryBean.getConfiguration().getClassMapping(obj.getClass().getName()).getProperty(filtros[i]).getColumnIterator().next()).getName() +" like " + "'"+valores[i]+ "%'"));
			}
	}
 }
}
	
	flush();
	clear();
	
	

			Long total = (Long) crit.list().get(0);
			
		
			
		return total.intValue();

		}catch(NumberFormatException e){
			setEstado("Error en la clave del campo a filtrar, favor de verificar los datos");
			logger.error("e number format" + e.toString());
			return -1;
		}catch(Exception w){
			setEstado("Error al buscar por filtro, favor de verificar los datos");
			logger.error("e w" + w.toString());
			return -1;
		}
		
	}


}
