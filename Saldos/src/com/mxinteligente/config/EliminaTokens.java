package com.mxinteligente.config;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.mxinteligente.model.entidades.Tokens;
import com.mxinteligente.model.negocio.GeneralServI;
import com.mxinteligente.model.negocio.SesionesServI;

public class EliminaTokens implements HttpSessionListener{

	@Autowired(required=true)
	private SesionesServI sesionesServ;
	

	
	public void sessionCreated(HttpSessionEvent arg0) {
	}

	
	public void sessionDestroyed(HttpSessionEvent arg0) {
		sesionesServ = (SesionesServI) SpringApplicationContext.getBean("sesionesServ");
		String session = arg0.getSession().getId();
		sesionesServ.eliminarToken(session);
	}

}
