package com.mxinteligente.model.dao.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mxinteligente.controller.ventas.Productos;
import com.mxinteligente.infra.model.dao.hibernate.GenericoDao;
import com.mxinteligente.model.dao.OrdenesCompraDaoI;
import com.mxinteligente.model.entidades.Comprador;
import com.mxinteligente.model.entidades.OrdenesCompra;
import com.mxinteligente.model.entidades.Producto;
import com.mxinteligente.model.entidades.Usuarios;

@Repository("ordenesCompraDao")
public class OrdenesCompraDao extends GenericoDao<Object, Serializable> implements OrdenesCompraDaoI {

	
	public long insertarOden(Comprador comprador, Producto producto, int cantidad){
		try{
			OrdenesCompra orden  = new OrdenesCompra();
			orden.setComprador(comprador);
			orden.setProducto(producto);
			orden.setCantidad(cantidad);
			orden.setFecha(new Date());
			orden.setAtendido("N");
		
			this.insertar(orden);
			return orden.getId();
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
		
	}

	public List obtenerOrdenes(int usuario, int min, int max) {
		DetachedCriteria productosUsr = DetachedCriteria.forClass(Producto.class);
		productosUsr.setProjection((Projection) Property.forName("id"));
		productosUsr.add(Restrictions.eq("users.id", usuario));
		
		Criteria crit = getSession().createCriteria(OrdenesCompra.class);
		crit.setFirstResult(min);
		crit.setMaxResults(max);		
		crit.add((Property.forName("producto.id").in(productosUsr)));
		crit.addOrder(Order.desc("fecha"));
		return crit.list();
	}

}
