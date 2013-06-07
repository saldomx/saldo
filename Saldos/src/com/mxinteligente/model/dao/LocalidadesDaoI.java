package com.mxinteligente.model.dao;

import java.io.Serializable;
import java.util.List;

import com.mxinteligente.infra.model.dao.GenericoDaoI;
import com.mxinteligente.model.entidades.Tokens;

public interface LocalidadesDaoI extends GenericoDaoI<Object,Serializable>{

	
	public List obtenerPaises();
	public List obtenerEstados(String pais);
}
