package com.mxinteligente.model.dao;

import java.io.Serializable;
import java.util.List;

import com.mxinteligente.infra.model.dao.GenericoDaoI;
import com.mxinteligente.model.entidades.Comprador;
import com.mxinteligente.model.entidades.Producto;
import com.mxinteligente.model.entidades.Usuarios;

public interface OrdenesCompraDaoI extends GenericoDaoI<Object,Serializable>{

	
	public long insertarOden(Comprador comprador, Producto prod, int cantidad);
	
	public List obtenerOrdenes(int usuario, int min, int max);

}
