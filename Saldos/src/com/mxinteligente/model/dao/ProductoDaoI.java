package com.mxinteligente.model.dao;

import java.io.Serializable;
import java.util.List;

import com.mxinteligente.infra.model.dao.GenericoDaoI;
import com.mxinteligente.model.entidades.Producto;

public interface ProductoDaoI  extends GenericoDaoI<Producto,Serializable>{

	public List<Producto> obtenerProductos(int usuario, int min, int max);

	public int obtenerTotalProductos(int usuario);

	
}
