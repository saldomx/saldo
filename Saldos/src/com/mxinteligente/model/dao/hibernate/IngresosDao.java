package com.mxinteligente.model.dao.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.mxinteligente.controller.general.General;
import com.mxinteligente.infra.model.dao.hibernate.GenericoDao;
import com.mxinteligente.model.dao.IngresosDaoI;
import com.mxinteligente.model.dao.UsuariosDaoI;
import com.mxinteligente.model.entidades.Catingresos;
import com.mxinteligente.model.entidades.Egresos;
import com.mxinteligente.model.entidades.Folios;
import com.mxinteligente.model.entidades.Ingresos;
import com.mxinteligente.model.entidades.Usuarios;

@Repository("ingresosDao")
public class IngresosDao extends GenericoDao<Ingresos, Serializable> implements
		IngresosDaoI {

	static Logger logger = LoggerFactory.getLogger(IngresosDao.class);

	
	@Autowired(required=true)
	private UsuariosDaoI usuariosDao;
	
	public BigInteger obtenerFolio(Session ses){
		BigInteger folio;

		Criteria crit = ses.createCriteria(Folios.class);
		crit.setProjection(Projections.projectionList().add(
				Projections.max("folio")));

		folio= (BigInteger) crit.uniqueResult();

		if (folio != null)
			folio= folio.add(new BigInteger("1"));
		else
			folio= new BigInteger("1");
		
		return folio;
		
	}
	
	@Transactional(readOnly = true)
	public List<Map> obtenerTodosIngresos(Usuarios user, int min, int max) {
		Criteria crit = getSession().createCriteria(Ingresos.class);
		crit.add(Restrictions.eq("id.catIngresosUsuariosId", user.getId()));
		crit.createAlias("categoria","categoria");
		crit.setProjection(Projections.projectionList()
				.add(Property.forName("id.id"), "folio")
				.add(Property.forName("concepto"), "concepto")
				.add(Property.forName("id.catIngresosId"), "idCategoria")
				.add(Property.forName("cantidad"), "cantidad")
				.add(Property.forName("contraparte"), "contraparte")
				.add(Property.forName("categoria.nombre"), "categoria")
				.add(Property.forName("fecha"), "fecha"));
		crit.addOrder(Order.desc("fecha"));
		crit.setFirstResult(min);
		crit.setMaxResults(max);
		crit.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		return crit.list();
	}

	@Transactional(rollbackFor=RuntimeException.class, isolation = Isolation.SERIALIZABLE)
	public boolean insertarIngreso(Ingresos ingreso) {
		try {
			Session ses = super.getSession();
			
			BigInteger folio;
			int idUser = ingreso.getId().getCatIngresosUsuariosId();
			Usuarios user = new Usuarios ();
			user.setId(idUser);
			
			user = usuariosDao.buscarPorObjeto(user).get(0);
			folio = this.obtenerFolio(ses);
			
			Folios regFolio = new Folios();
			regFolio.setUsuario(user.getEmail());
			regFolio.setFolio(folio);			
			regFolio.setTipo("ingreso");
			
			Integer i = obtenerUltimoID(ingreso.getId()
					.getCatIngresosUsuariosId(), ingreso.getId()
					.getCatIngresosId());
			ingreso.getId().setId(i);
			ingreso.setFolio(folio);
			ses.save(regFolio);
			ses.save(ingreso);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Transactional(readOnly = true)
	public Integer obtenerUltimoID(int user, int Cating) {
		Criteria crit = getSession().createCriteria(Ingresos.class);
		crit.add(Restrictions.eq("id.catIngresosUsuariosId", user));
		crit.add(Restrictions.eq("id.catIngresosId", Cating));
		crit.setProjection(Projections.projectionList().add(
				Projections.max("id.id")));

		Integer ultimo = (Integer) crit.uniqueResult();

		if (ultimo != null)
			return ultimo + 1;
		else
			return 1;

	}

	@Transactional(readOnly = true)
	public List<Map> obtenerIngresosPorCategoria(Catingresos cat, int min, int max) {
		Criteria crit = getSession().createCriteria(Ingresos.class);
		crit.add(Restrictions.eq("id.catIngresosUsuariosId", cat.getId().getUsuariosId()));
		crit.createAlias("categoria", "categoria");

		if(cat.getId().getId()!=0)
			crit.add(Restrictions.eq("id.catIngresosId", cat.getId().getId()));
		
		crit.setProjection(Projections.projectionList()
				.add(Property.forName("concepto"), "concepto")
				.add(Property.forName("id.id"), "id")
				.add(Property.forName("id.catIngresosId"), "idCategoria")
				.add(Property.forName("contraparte"), "contraparte")
				.add(Property.forName("cantidad"), "cantidad")
				.add(Property.forName("categoria.nombre"), "categoria")
				.add(Property.forName("fecha"), "fecha"));
		crit.addOrder(Order.desc("fecha"));
		crit.setFirstResult(min);
		crit.setMaxResults(max);
		crit.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		return crit.list();
	}

	@Transactional(readOnly = true)
	public int obtenerTotalTodosIngresos(Usuarios user) {
		ProjectionList list = Projections.projectionList();
		list.add(Projections.rowCount(),"total");
		
		Object res = getSession().createCriteria(Ingresos.class)
				.setProjection(list)
				.add(Restrictions.eq("id.catIngresosUsuariosId", user.getId()))
				
				.uniqueResult();
			

		return (res!=null)?new Integer(""+res):0;
	}

	@Transactional(readOnly = true)
	public int obtenerTotalIngresosPorCategoria(Catingresos cat) {
		ProjectionList list = Projections.projectionList();
		list.add(Projections.rowCount(),"total");
		
		Object res = getSession().createCriteria(Ingresos.class)
				.setProjection(list)
				.add(Restrictions.eq("id.catIngresosUsuariosId", cat.getId().getUsuariosId()))
				.add(Restrictions.eq("id.catIngresosId", cat.getId().getId()))
				.uniqueResult();
			

		return (res!=null)?new Integer(""+res):0;
	}

	@Transactional(readOnly = true)
	public List<Map> obtenerTodo(Usuarios user, int min, int max) {
		
		
		String cons ="(SELECT 1 tipo, folio, i.fecha, i.cantidad, i.concepto, i.contraparte, ci.Nombre FROM ingresos i inner join catingresos ci on i.CatIngresos_id = ci.id " +
				"WHERE i.CatIngresos_Usuarios_id = " +user.getId()+ " AND ci.Usuarios_id = "+user.getId() +") UNION  " +
				"(SELECT 2 tipo,  folio, e.fecha, e.cantidad,  e.concepto, e.contraparte, ce.Nombre from egresos e inner join categresos ce on e.CatEgresos_id=ce.id " +
				"WHERE e.CatEgresos_Usuarios_id = " +user.getId()+ " AND ce.Usuarios_id=" +user.getId() + ") " +
				"ORDER BY fecha DESC LIMIT "+ min +","+ max +" ";
		
		Query q = getSession().createSQLQuery(cons);
		q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		
		return q.list();
	}

	@Transactional(readOnly = true)
	public BigDecimal sumaIngresos(Usuarios user) {
		ProjectionList list = Projections.projectionList();
		list.add(Projections.sum("cantidad"),"total");
		
		Object res = getSession().createCriteria(Ingresos.class)
				.setProjection(list)
				.add(Restrictions.eq("id.catIngresosUsuariosId", user.getId()))
				.uniqueResult();
				
			BigDecimal b = (BigDecimal)res; 

		return (b!=null)?b:BigDecimal.ZERO;
	}

}
