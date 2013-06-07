package com.mxinteligente.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mxinteligente.infra.model.dao.GenericoDaoI;
import com.mxinteligente.model.entidades.Catingresos;
import com.mxinteligente.model.entidades.Usuarios;

public interface CatingresosDaoI extends GenericoDaoI<Catingresos,Serializable>{

	public List<Map> obtenerCategorias(Usuarios user);

	public int obtenerSiguienteId(Usuarios user);

}
