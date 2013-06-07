package com.mxinteligente.model.dao.hibernate;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.mxinteligente.infra.model.dao.hibernate.GenericoDao;
import com.mxinteligente.infra.model.nego.MailService;
import com.mxinteligente.infra.model.nego.TemplateHtml;
import com.mxinteligente.model.dao.CodigosDaoI;
import com.mxinteligente.model.dao.UsuariosDaoI;
import com.mxinteligente.model.entidades.Codigos;
import com.mxinteligente.model.entidades.Egresos;
import com.mxinteligente.model.entidades.Folios;
import com.mxinteligente.model.entidades.Ingresos;
import com.mxinteligente.model.entidades.Usuarios;

@Repository("codigosDao")
public class CodigosDao extends GenericoDao<Codigos, Serializable> implements CodigosDaoI{

	
	@Autowired(required=true)
	MailService mailService;
	
	 @Autowired(required=true)
	 private TemplateHtml tamplateHtml; 
	 
	 @Autowired(required=true)
	private UsuariosDaoI usuariosDao;
	
	
	@Transactional(readOnly=true)
	public Codigos buscarCodigo(String codigo) {
		try{
		Criteria crit = getSession().createCriteria(Codigos.class);
		crit.add(Restrictions.eq("codigo", codigo));
		return (Codigos) crit.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Transactional( rollbackFor= RuntimeException.class, isolation = Isolation.SERIALIZABLE)
	public boolean cargarSaldo(Codigos codigo, Ingresos ing) {
		BigInteger folio;
		java.text.SimpleDateFormat formatDate=new java.text.SimpleDateFormat("dd/MM/yyyy");

		Usuarios usr= new Usuarios();
		usr.setId(ing.getId().getCatIngresosUsuariosId());
		usr  = usuariosDao.buscarPorObjeto(usr).get(0);
		
		Session ses=super.getSession();
		folio = this.obtenerFolio(ses);			
		Folios regFolio = new Folios();
		regFolio.setFolio(folio);
		regFolio.setTipo("ingreso");
		regFolio.setUsuario(usr.getEmail());
		ses.save(regFolio);
		ing.setFolio(folio);
		
		ses.save(ing);
		ses.delete(codigo);
		
		
		
		Usuarios user = new Usuarios();
		user.setId(ing.getId().getCatIngresosUsuariosId());
		
		user = usuariosDao.buscarPorObjeto(user).get(0);

		
		String body2 = tamplateHtml.templateCargo(user.getNombre() + " " + user.getApp() + " " +user.getApm(),
			 codigo.getCodigo(), codigo.getMonto().toPlainString(), formatDate.format(new Date()));
		
	
		
		
		mailService.sendMail("avisos@saldo.mx", user.getEmail(), "RECARGA DE SALDO ", body2);
		
		return true;
	}
	
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

}
