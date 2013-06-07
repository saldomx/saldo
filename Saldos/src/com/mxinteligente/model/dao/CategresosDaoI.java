package com.mxinteligente.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mxinteligente.infra.model.dao.GenericoDaoI;
import com.mxinteligente.model.entidades.Categresos;
import com.mxinteligente.model.entidades.Usuarios;

public interface CategresosDaoI extends GenericoDaoI<Categresos,Serializable>{

	
	public List<Map> obtenerCategorias(Usuarios user);
	
	public int obtenerSiguienteId(Usuarios user);
}
