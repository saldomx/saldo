package com.mxinteligente.model.dao;

import java.io.Serializable;

import com.mxinteligente.infra.model.dao.GenericoDaoI;
import com.mxinteligente.model.entidades.Codigos;
import com.mxinteligente.model.entidades.Ingresos;

public interface CodigosDaoI extends GenericoDaoI<Codigos,Serializable>{
	
	public Codigos buscarCodigo(String codigo);
	
	public boolean cargarSaldo(Codigos codigo, Ingresos ing);
	
	

}
