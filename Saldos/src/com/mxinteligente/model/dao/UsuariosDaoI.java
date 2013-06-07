package com.mxinteligente.model.dao;

import java.io.Serializable;
import java.util.List;

import com.mxinteligente.infra.model.dao.GenericoDaoI;
import com.mxinteligente.model.entidades.Usuarios;

public interface UsuariosDaoI extends GenericoDaoI<Usuarios,Serializable>{

	public Usuarios isUsurio(String email);
	
	public Usuarios validaPin(Integer id, Integer pin);

	public List<Usuarios> obtenerUsuarios(int min, int max);
}
