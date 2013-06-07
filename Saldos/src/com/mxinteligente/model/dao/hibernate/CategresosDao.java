package com.mxinteligente.model.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mxinteligente.model.dao.CategresosDaoI;
import com.mxinteligente.infra.model.dao.hibernate.GenericoDao;
import com.mxinteligente.model.entidades.Categresos;
import com.mxinteligente.model.entidades.Catingresos;
import com.mxinteligente.model.entidades.Usuarios;

@Repository("categresosDao")
public class CategresosDao extends GenericoDao<Categresos, Serializable> implements CategresosDaoI{

	@Transactional(readOnly=true)
	public List<Map> obtenerCategorias(Usuarios user) {
		Criteria crit = getSession().createCriteria(Categresos.class);
		crit.createAlias("usuarios", "usuarios");
		crit.add(Restrictions.eq("usuarios.id", user.getId()));
		crit.setProjection(Projections.projectionList()
				.add(Property.forName("nombre"), "nombre")
				.add(Property.forName("id.id"), "id")
				.add(Property.forName("id.usuariosId"), "userid")
				);
		crit.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
	  return crit.list();
	}

	@Transactional(readOnly=true)
	public int obtenerSiguienteId(Usuarios user) {
		Criteria crit = getSession().createCriteria(Categresos.class);
		crit.add(Restrictions.eq("id.usuariosId", user.getId()));
		crit.setProjection(Projections.projectionList().add(
				Projections.max("id.id")));

		Integer ultimo = (Integer) crit.uniqueResult();

		if (ultimo != null)
			return ultimo + 1;
		else
			return 1;
	}
}


