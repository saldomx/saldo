package com.mxinteligente.controller.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

import com.mxinteligente.model.negocio.GeneralServI;

public class Valida extends GenericForwardComposer{

	@Autowired(required = true)
	GeneralServI generalServ;
	
	private Label lblCorrecto, lblIncorrecto;
	private Div divLogin;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		String code = Executions.getCurrent().getParameter("codigo");
		
		System.out.println("code " + code);
		if(generalServ.validaUsuario(code)){
			lblCorrecto.setVisible(true);
			lblCorrecto.setValue("Tu registro a Saldo.mx ha sido confirmado, gracias");
			divLogin.setVisible(true);
		}else{
			lblIncorrecto.setVisible(true);
			lblIncorrecto.setValue("Lo sentimos tu registro no pudo confirmarse favor de verificar");
		}
		

	}
	
}
