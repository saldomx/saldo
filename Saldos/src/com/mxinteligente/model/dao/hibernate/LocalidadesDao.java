package com.mxinteligente.model.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mxinteligente.infra.model.dao.hibernate.GenericoDao;
import com.mxinteligente.model.dao.LocalidadesDaoI;
import com.mxinteligente.model.entidades.Estados;
import com.mxinteligente.model.entidades.Pais;
import com.mxinteligente.model.entidades.Producto;

@Repository("localidadesDao")
public class LocalidadesDao extends GenericoDao<Object, Serializable> implements LocalidadesDaoI {

	@Transactional(readOnly=true)
	public List obtenerPaises() {
		List res=null;
		Session ses = super.getSession();
		Query cons = ses.createSQLQuery("Select iso, name, printable_name from country");
		List<Object[]> lst = cons.list();
		
		Pais pais;
		if(lst!=null && !lst.isEmpty()){
			res= new ArrayList();
			for(Object[] p: lst){
				pais = new Pais();
				String iso = (String)p[0];
				String name = (String)p[1];
				String descr = (String)p[2];
				pais.setDescripcion(descr);
				pais.setIso(iso);
				pais.setNombre(name);
				res.add(pais);
			}
		}
		return res;	
	}

	@Transactional(readOnly=true)
	public List obtenerEstados(String pais) {
		List res=null;
		Session ses = super.getSession();
		Query cons = ses.createSQLQuery("Select clave, nombre from estados where pais = '"+pais+"'");
		List<Object[]> lst = cons.list();
		
		Estados estado;
		if(lst!=null && !lst.isEmpty()){
			res= new ArrayList();
			for(Object[] p: lst){
				estado = new Estados();
				String clave = (String)p[0];
				String nombre = (String)p[1];
				estado.setNombre(nombre);
				estado.setClave(clave);
				res.add(estado);
			}
		}
		return res;	
	}

}
