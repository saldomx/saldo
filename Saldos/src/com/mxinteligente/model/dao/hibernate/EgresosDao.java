package com.mxinteligente.model.dao.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mxinteligente.infra.model.dao.hibernate.GenericoDao;
import com.mxinteligente.infra.model.nego.MailService;
import com.mxinteligente.infra.model.nego.TemplateHtml;
import com.mxinteligente.model.dao.EgresosDaoI;
import com.mxinteligente.model.dao.IngresosDaoI;
import com.mxinteligente.model.dao.UsuariosDaoI;
import com.mxinteligente.model.entidades.Categresos;
import com.mxinteligente.model.entidades.CategresosId;
import com.mxinteligente.model.entidades.Egresos;
import com.mxinteligente.model.entidades.EgresosId;
import com.mxinteligente.model.entidades.Folios;
import com.mxinteligente.model.entidades.Ingresos;
import com.mxinteligente.model.entidades.IngresosId;
import com.mxinteligente.model.entidades.Producto;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.impl.GeneralServ;

@Repository("egresosDao")
public class EgresosDao extends GenericoDao<Egresos, Serializable> implements EgresosDaoI{

	
	@Autowired(required=true)
	private IngresosDaoI ingresosDao;
	
	@Autowired(required=true)
	MailService mailService;
	
	 @Autowired
	 private TemplateHtml tamplateHtml; 
	 
		@Autowired(required=true)
		private UsuariosDaoI usuariosDao;
	
	 
		static Logger logger = LoggerFactory.getLogger(EgresosDao.class);
		
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

	
	@Transactional(rollbackFor=RuntimeException.class, isolation = Isolation.SERIALIZABLE)
	public int insertarEgreso(Egresos egreso) {
		BigInteger folio;
		Session ses = super.getSession();
		folio = this.obtenerFolio(ses);
		
		Folios regFolio = new Folios();
		regFolio.setFolio(folio);
		regFolio.setTipo("egreso");
		ses.save(regFolio);

			
			Integer i = obtenerUltimoID(egreso.getId()
					.getCatEgresosUsuariosId(), egreso.getId()
					.getCatEgresosId(),ses);
			egreso.getId().setId(i);
			egreso.setFolio(folio);
			ses.save(egreso);
			
			return 0;
		
	}

	@Transactional(readOnly = true)
	public List<Map> obtenerTodosEgresos(Usuarios user, int min, int max) {
		Criteria crit = getSession().createCriteria(Egresos.class);
		crit.add(Restrictions.eq("id.catEgresosUsuariosId", user.getId()));
		crit.createAlias("categoria", "categoria");
		crit.setProjection(Projections.projectionList()
				.add(Property.forName("id.id"), "folio")
				.add(Property.forName("concepto"), "concepto")
				.add(Property.forName("id.catEgresosId"), "idCategoria")
				.add(Property.forName("cantidad"), "cantidad")
				.add(Property.forName("contraparte"), "contraparte")
				.add(Property.forName("categoria.nombre"), "categoria")
				.add(Property.forName("fecha"), "fecha"));
		crit.addOrder(Order.asc("fecha"));
		crit.setFirstResult(min);
		crit.setMaxResults(max);
		crit.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		return crit.list();
	}

	@Transactional(readOnly = true)
	public List<Map> obtenerEgresosPorCategoria(Categresos cat, int min, int max) {
		Criteria crit = getSession().createCriteria(Egresos.class);
		crit.createAlias("categoria", "categoria");
		crit.add(Restrictions.eq("id.catEgresosUsuariosId", cat.getId().getUsuariosId()));
		
		if(cat.getId().getId()!=0)
			crit.add(Restrictions.eq("id.catEgresosId", cat.getId().getId()));
		
		crit.setProjection(Projections.projectionList()
				.add(Property.forName("concepto"), "concepto")
				.add(Property.forName("id.id"), "id")
				.add(Property.forName("id.catEgresosId"), "idCategoria")
				.add(Property.forName("categoria.nombre"), "Nombre")
				.add(Property.forName("cantidad"), "cantidad")
				.add(Property.forName("contraparte"), "contraparte")
				.add(Property.forName("fecha"), "fecha"));
		crit.addOrder(Order.asc("fecha"));
		crit.setFirstResult(min);
		crit.setMaxResults(max);
		crit.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		return crit.list();
	}
	
	
	public Integer obtenerUltimoID(int user, int Cating, Session ses) {
		Criteria crit = ses.createCriteria(Egresos.class);
		crit.add(Restrictions.eq("id.catEgresosUsuariosId", user));
		crit.add(Restrictions.eq("id.catEgresosId", Cating));
		crit.setProjection(Projections.projectionList().add(
				Projections.max("id.id")));

		Integer ultimo = (Integer) crit.uniqueResult();

		if (ultimo != null)
			return ultimo + 1;
		else
			return 1;

	}

	@Transactional(readOnly = true)
	public int obtenerTotalTodosEgresos(Usuarios user) {
		ProjectionList list = Projections.projectionList();
		list.add(Projections.rowCount(),"total");
		
		Object res = getSession().createCriteria(Egresos.class)
				.setProjection(list)
				.add(Restrictions.eq("id.catEgresosUsuariosId", user.getId()))
				
				.uniqueResult();
			

		return (res!=null)?new Integer(""+res):0;
		
	}

	@Transactional(readOnly = true)
	public int obtenerTotalEgresosPorCategoria(Categresos cat) {
		ProjectionList list = Projections.projectionList();
		list.add(Projections.rowCount(),"total");
		
		Object res = getSession().createCriteria(Egresos.class)
				.setProjection(list)
				.add(Restrictions.eq("id.catEgresosUsuariosId", cat.getId().getUsuariosId()))
				.add(Restrictions.eq("id.catEgresosId", cat.getId().getId()))
				.uniqueResult();
			

		return (res!=null)?new Integer(""+res):0;
		
	}

	@Transactional(readOnly = true)
	public BigDecimal sumaEgresos(Usuarios user) {
		ProjectionList list = Projections.projectionList();
		list.add(Projections.sum("cantidad"),"total");
		
		Object res = getSession().createCriteria(Egresos.class)
				.setProjection(list)
				.add(Restrictions.eq("id.catEgresosUsuariosId", user.getId()))
				.uniqueResult();
				
			BigDecimal b = (BigDecimal)res; 

		return (b!=null)?b:BigDecimal.ZERO;
	}
	
	
	
	@Transactional(rollbackFor=RuntimeException.class, isolation = Isolation.SERIALIZABLE)
	public int transferir(Egresos e, Usuarios user, Usuarios userTrans) {
		java.text.SimpleDateFormat formatDate=new java.text.SimpleDateFormat("dd/MM/yyyy");
		BigInteger folio;
		Session ses =  null;
	
			ses = super.getSession();
			Ingresos ing = new Ingresos();
			ing.setId(new IngresosId());
			ing.getId().setCatIngresosId(1); //Clave que identifica el id Categoria Transferencia
			ing.getId().setCatIngresosUsuariosId(userTrans.getId());
			ing.getId().setId(ingresosDao.obtenerUltimoID(userTrans.getId(), 1));
			ing.setConcepto(e.getConcepto());
			ing.setFecha(new Date());
			ing.setCantidad(e.getCantidad());
			ing.setContraparte(user.getEmail());
			
			e.setContraparte(userTrans.getEmail());
			e.getId().setId(this.obtenerUltimoID(user.getId(), e.getId().getCatEgresosId(), ses));
			
			folio = this.obtenerFolio(ses);			
			Folios regFolio = new Folios();
			regFolio.setFolio(folio);
			regFolio.setTipo("egreso");
			regFolio.setUsuario(user.getEmail());
			ses.save(regFolio);
			e.setFolio(folio);
			ses.save(e);
			
			
			folio = this.obtenerFolio(ses);			
			regFolio = new Folios();
			regFolio.setFolio(folio);
			regFolio.setTipo("ingreso");
			regFolio.setUsuario(userTrans.getEmail());
			ses.save(regFolio);
			ing.setFolio(folio);
			ses.save(ing);
			
			
			user = usuariosDao.buscarPorObjeto(user).get(0);

			String body1 = tamplateHtml.templateTransferencia(userTrans.getNombre() + " " + userTrans.getApp() + " " +userTrans.getApm(),
					user.getNombre() + " " + user.getApp() + " " +user.getApm(), e.getId().getId(), formatDate.format(new Date()), ing.getCantidad().toPlainString());
			
			String body2 = tamplateHtml.templateTransferencia(userTrans.getNombre() + " " + userTrans.getApp() + " " +userTrans.getApm(),
					user.getNombre() + " " + user.getApp() + " " +user.getApm(), ing.getId().getId(), formatDate.format(new Date()), ing.getCantidad().toPlainString());
			
		
			
			
			mailService.sendMail("avisos@saldo.mx", user.getEmail(), "PAGO ", body2);
			
			mailService.sendMail("avisos@saldo.mx", userTrans.getEmail(), "PAGO ", body1);

			
			
			
			return 0;
			
		
				
	}
	
	
	@Transactional(rollbackFor=RuntimeException.class, isolation = Isolation.SERIALIZABLE)
	public int comprar(Egresos e, Usuarios user, Usuarios userTrans, Producto p) {
		java.text.SimpleDateFormat formatDate=new java.text.SimpleDateFormat("dd/MM/yyyy");
		BigInteger folio;
		Session ses =  null;
	
			ses = super.getSession();
			Ingresos ing = new Ingresos();
			ing.setId(new IngresosId());
			ing.getId().setCatIngresosId(1); //Clave que identifica el id Categoria Transferencia
			ing.getId().setCatIngresosUsuariosId(userTrans.getId());
			ing.getId().setId(ingresosDao.obtenerUltimoID(userTrans.getId(), 1));
			ing.setConcepto("VENTA - CODIGO: " + p.getCode());
			ing.setFecha(new Date());
			ing.setCantidad(e.getCantidad());
			ing.setContraparte(user.getEmail());
			
			e.setContraparte(userTrans.getEmail());
			e.getId().setId(this.obtenerUltimoID(user.getId(), e.getId().getCatEgresosId(), ses));
			
			folio = this.obtenerFolio(ses);			
			Folios regFolio = new Folios();
			regFolio.setFolio(folio);
			regFolio.setTipo("egreso");
			regFolio.setUsuario(user.getEmail());
			ses.save(regFolio);
			e.setFolio(folio);
			ses.save(e);
			
			
			folio = this.obtenerFolio(ses);			
			regFolio = new Folios();
			regFolio.setFolio(folio);
			regFolio.setTipo("ingreso");
			regFolio.setUsuario(userTrans.getEmail());
			ses.save(regFolio);
			ing.setFolio(folio);
			
			
			
			ses.save(ing);
			
			
			user = usuariosDao.buscarPorObjeto(user).get(0);

			String body1 = tamplateHtml.templateVentaProd(userTrans.getNombre() + " " + userTrans.getApp() + " " +userTrans.getApm(),
					user.getNombre() + " " + user.getApp() + " " +user.getApm(), e.getId().getId(), formatDate.format(new Date()), ing.getCantidad().toPlainString());
			
			String body2 = tamplateHtml.templateVentaProd(userTrans.getNombre() + " " + userTrans.getApp() + " " +userTrans.getApm(),
					user.getNombre() + " " + user.getApp() + " " +user.getApm(), ing.getId().getId(), formatDate.format(new Date()), ing.getCantidad().toPlainString());
			
		
			
			
			mailService.sendMail("avisos@saldo.mx", user.getEmail(), "NOTIFICACIÓN DE COMPRA ", body2);
			
			mailService.sendMail("avisos@saldo.mx", userTrans.getEmail(), "NOTIFICACIÓN DE VENTA ", body1);

			
			
			
			return 0;
			
		
				
	}

	
	@Transactional(rollbackFor =RuntimeException.class)
	public int retiro(BigDecimal retiro, BigDecimal comision, Usuarios usr,
			Usuarios agencia, Usuarios admin) {
		BigInteger folio;
		BigDecimal comisionAdmin = comision.subtract(new BigDecimal("3"));
		BigDecimal comisionAgencia = comision.subtract(comisionAdmin);
		java.text.SimpleDateFormat formatDate=new java.text.SimpleDateFormat("dd/MM/yyyy");

		Session ses = super.getSession();
		
		Categresos catEg = new Categresos();
		catEg.setId(new CategresosId());
		catEg.getId().setId(1); // ID de TRANSFERENCIAS
		catEg.getId().setUsuariosId(usr.getId());
		catEg.setUsuarios(usr);
		
		Egresos eg = new Egresos();
		eg.setId(new EgresosId());
		eg.getId().setCatEgresosId(1);
		eg.getId().setCatEgresosUsuariosId(usr.getId());
		eg.setCategoria(catEg);
		eg.setConcepto("RETIRO DE EFECTIVO" );
		eg.setCantidad(retiro);
		eg.setFecha(new Date());
		eg.getId().setId(this.obtenerUltimoID(usr.getId(), eg.getId().getCatEgresosId(), ses));
		eg.setContraparte(agencia.getEmail());
		
		folio = this.obtenerFolio(ses);			
		Folios regFolio = new Folios();
		regFolio.setFolio(folio);
		regFolio.setTipo("egreso");
		regFolio.setTipo(usr.getEmail());
		ses.save(regFolio);
		eg.setFolio(folio);
		ses.save(eg);
		
		Integer idRetiro=eg.getId().getId();

		
		eg = new Egresos();
		eg.setId(new EgresosId());
		eg.getId().setCatEgresosId(1);
		eg.getId().setCatEgresosUsuariosId(usr.getId());
		eg.setCategoria(catEg);
		eg.setConcepto("COMISION DE RETIRO "+ "(" + agencia.getEmail()+" )");
		eg.setCantidad(comision);
		eg.setFecha(new Date());
		eg.getId().setId(this.obtenerUltimoID(usr.getId(), eg.getId().getCatEgresosId(), ses));
		eg.setContraparte(agencia.getEmail());

		folio = this.obtenerFolio(ses);			
		regFolio = new Folios();
		regFolio.setFolio(folio);
		regFolio.setUsuario(usr.getEmail());
		regFolio.setTipo("egreso");
		ses.save(regFolio);
		eg.setFolio(folio);
		ses.save(eg);
		
		
		
		Ingresos ing = new Ingresos();
		ing.setId(new IngresosId());
		ing.getId().setCatIngresosId(1); //Clave que identifica el id Categoria Transferencia
		ing.getId().setCatIngresosUsuariosId(agencia.getId());
		ing.getId().setId(ingresosDao.obtenerUltimoID(agencia.getId(), 1));
		ing.setConcepto("RETIRO DE EFECTIVO " +"$"+retiro  );
		ing.setFecha(new Date());
		ing.setCantidad(comisionAgencia);
		ing.setContraparte(usr.getEmail());
	
		folio = this.obtenerFolio(ses);			
		regFolio = new Folios();
		regFolio.setFolio(folio);
		regFolio.setUsuario(agencia.getEmail());
		regFolio.setTipo("ingreso");
		ses.save(regFolio);
		ing.setFolio(folio);
		ses.save(ing);
		
		ing = new Ingresos();
		ing.setId(new IngresosId());
		ing.getId().setCatIngresosId(1); //Clave que identifica el id Categoria Transferencia
		ing.getId().setCatIngresosUsuariosId(admin.getId());
		ing.getId().setId(ingresosDao.obtenerUltimoID(admin.getId(), 1));
		ing.setConcepto("COMISION");
		ing.setFecha(new Date());
		ing.setCantidad(comisionAdmin);
		ing.setContraparte(agencia.getEmail());
		

		folio = this.obtenerFolio(ses);			
		regFolio = new Folios();
		regFolio.setFolio(folio);
		regFolio.setTipo("ingreso");
		regFolio.setUsuario(admin.getEmail());
		ses.save(regFolio);
		ing.setFolio(folio);
		ses.save(ing);
		
		Usuarios user = usuariosDao.buscarPorObjeto(usr).get(0);

		
		String body2 = tamplateHtml.templateRetiro(user.getNombre() + " " + user.getApp() + " " +user.getApm(),
				retiro.toPlainString(),idRetiro.toString(), formatDate.format(new Date()));
			
		
		
		mailService.sendMail("avisos@saldo.mx", user.getEmail(), "RETIRO DE EFECTIVO ", body2);
		

		
		
		return 0;
	}

}
