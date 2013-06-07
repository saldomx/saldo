package com.mxinteligente.controller.admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;

public class Caja extends GenericForwardComposer  {

	protected DataBinder binder;
	private Usuarios usuario;
	private Textbox  textEmail;
	private Decimalbox decCantidad;
	private Button btnGuardar;
	private Window winTrans;
	private Intbox intNip;
	

	@Autowired(required=true)
	private GeneralServI generalServ;
	
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication autenticacion = context.getAuthentication();
		usuario = new Usuarios();
		usuario.setEmail(autenticacion.getName());
		usuario = (Usuarios) generalServ.buscarObjeto(usuario).get(0);
		
		}
	
	
	public void onClick$btnGuardar()throws Exception{
		if(textEmail.isValid() && decCantidad.isValid() && intNip.isValid()){
			
			if(usuario.getNip().equals(intNip.getValue())){
			
						
			String status = generalServ.cargarSaldo(decCantidad.getValue(), textEmail.getValue());
			
			if(status.startsWith("("))
				Messagebox.show("Se ha recargado exitosamente $" + decCantidad.getValue().toPlainString() + " a " + textEmail.getValue() );
			else
				Messagebox.show(status);
			
			
			
		}
			else
				Messagebox.show("NIP incorrecto favor de verificar");
	}
		else{
			textEmail.getValue();
			decCantidad.getValue();
			intNip.getValue();
		}
	

		
	}
	
	

	
	public void onAccept$btnGuardar(){
		
		winTrans.detach();
	}
	
	public void onClick$btnCancelar(){
		winTrans.detach();
	}
	
	protected class ConfirmaGuardadoIngreso {

		public void show() {
			try {
				Messagebox.show("La transferencia se ha realizado  satisfactoriamente",
						"Gastos", Messagebox.OK, Messagebox.INFORMATION,
						new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event event) {
								if ("onOK".equals(event.getName())) {
									realizarAfirmativo();
								}
							}
						});
			} catch (InterruptedException ex) {

			}
		}

		public void realizarAfirmativo() {
			Events.sendEvent(new Event("onAccept", btnGuardar));
		}
	}
}


