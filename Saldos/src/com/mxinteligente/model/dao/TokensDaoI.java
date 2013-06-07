package com.mxinteligente.model.dao;

import java.io.Serializable;

import com.mxinteligente.infra.model.dao.GenericoDaoI;
import com.mxinteligente.model.entidades.Tokens;

public interface TokensDaoI extends GenericoDaoI<Tokens,Serializable>{

	public boolean eliminarToken(String SessionID);
	
}
