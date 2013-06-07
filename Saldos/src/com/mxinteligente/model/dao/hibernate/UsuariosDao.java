package com.mxinteligente.model.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mxinteligente.infra.model.dao.hibernate.GenericoDao;
import com.mxinteligente.model.dao.UsuariosDaoI;
import com.mxinteligente.model.entidades.Ingresos;
import com.mxinteligente.model.entidades.Usuarios;

@Repository("usuariosDao")
public class UsuariosDao extends GenericoDao<Usuarios, Serializable> implements
UsuariosDaoI {
	
	
	@Transactional(readOnly = true)
	public Usuarios isUsurio(String email){
		
		Criteria crit = getSession().createCriteria(Usuarios.class);
		crit.add(Restrictions.eq("email", email));
		if(crit.list()!=null)
				return (Usuarios) crit.uniqueResult();
		else
			return null;
		
	}
	
	@Transactional(readOnly = true)
	public List<Usuarios> obtenerUsuarios(int min, int max) {
		Criteria crit = getSession().createCriteria(Usuarios.class);
		crit.add(Restrictions.eq("rol", "ROLE_USER"));
		crit.addOrder(Order.asc("id"));
		crit.setFirstResult(min);
		crit.setMaxResults(max);		
		return crit.list();
	}

	@Transactional(readOnly = true)
	public Usuarios validaPin(Integer id, Integer pin){
		Criteria crit = getSession().createCriteria(Usuarios.class);
		crit.add(Restrictions.eq("id", id));
		if(crit.list()!=null){
			Usuarios usr = (Usuarios) crit.list().get(0);
			if(usr.getNip().equals(pin))
				return usr;
			else
				return null;
		}
		else
			return null;
	}
	
	
	


}
