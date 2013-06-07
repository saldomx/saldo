package com.mxinteligente.model.negocio.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mxinteligente.infra.model.nego.impl.GenServicio;
import com.mxinteligente.model.dao.TokensDaoI;
import com.mxinteligente.model.entidades.Tokens;
import com.mxinteligente.model.negocio.SesionesServI;

@Repository("sesionesServ")
public class SesionesServ extends GenServicio implements SesionesServI{

	@Autowired(required=true)
	TokensDaoI tokensDao;
	
	public boolean eliminarToken(String sessionId) {
		return tokensDao.eliminarToken(sessionId);
	}

	public void logout(Tokens tok) {
		try{
		tokensDao.eliminar(tok);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
