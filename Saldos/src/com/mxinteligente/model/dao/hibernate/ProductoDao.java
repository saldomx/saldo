package com.mxinteligente.model.dao.hibernate;

import java.io.Serializable;
import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mxinteligente.infra.model.dao.hibernate.GenericoDao;
import com.mxinteligente.model.dao.ProductoDaoI;
import com.mxinteligente.model.entidades.Producto;

@Repository("productoDao")
public class ProductoDao extends GenericoDao<Producto, Serializable> implements ProductoDaoI {

	
	@Transactional(readOnly=true)
	public int obtenerTotalProductos(int usuario) {
//		System.out.println(campus.getCampClv()+"/"+mes+"/"+anio+"/"+tipo);
		ProjectionList list = Projections.projectionList();
		list.add(Projections.rowCount(),"total");
		Object res = getSession().createCriteria(Producto.class)
				.setProjection(list)
				.add(Restrictions.eq("users.id", usuario))
				.uniqueResult();
		return (res!=null)?new Integer(""+res):0;
	} 
	
	
	@Transactional(readOnly = true)
	public List<Producto> obtenerProductos(int usuario, int min, int max) {
		Criteria crit = getSession().createCriteria(Producto.class);
		crit.createAlias("users", "users");
		crit.add(Restrictions.eq("users.id", usuario));
		crit.addOrder(Order.asc("id"));
		crit.setFirstResult(min);
		crit.setMaxResults(max);		
		return crit.list();
	}
}
