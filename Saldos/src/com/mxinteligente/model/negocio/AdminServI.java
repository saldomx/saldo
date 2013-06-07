package com.mxinteligente.model.negocio;

import java.math.BigDecimal;
import java.util.List;

import com.mxinteligente.infra.model.nego.GenServicioI;
import com.mxinteligente.model.entidades.Usuarios;

public interface AdminServI extends GenServicioI{
	
	public List<Usuarios> obtenerTodosUsuarios();
	public int obtenerTotalUsuarios();
	public List<Usuarios> obtenerUsuarios(int min, int max);
	
	public boolean generarCodigos(BigDecimal monto, int cantidad);
	

}
