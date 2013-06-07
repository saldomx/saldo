package com.mxinteligente.model.negocio;

import com.mxinteligente.infra.model.nego.GenServicioI;
import com.mxinteligente.model.entidades.Tokens;

public interface SesionesServI extends GenServicioI{
	
	public boolean eliminarToken(String sessionId);
	
	public void logout(Tokens tok);

}
