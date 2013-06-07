package com.mxinteligente.controller.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;

public class Retiro extends GenericForwardComposer{
	
	protected DataBinder binder;
	private Usuarios usr;
	private Textbox textEmail;
	private Decimalbox decCantidad;
	private Button btnGuardar;
	private Window winRet;
	private String cantidadRetirada;

	@Autowired(required=true)
	private GeneralServI generalServ;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		usr = (Usuarios) arg.get("user");
		
		}
	
	
	public void onClick$btnGuardar()throws Exception{
		if(textEmail.isValid() && decCantidad.isValid() ){

			String status = generalServ.retirodeFondos(decCantidad.getValue(), usr, textEmail.getValue());
			if(status.startsWith("(")){
				cantidadRetirada = decCantidad.getValue().toPlainString();
				new ConfirmaGuardadoIngreso().show();
			}else {
				Messagebox.show(status);

			}			
		}else{
			textEmail.getValue();
			decCantidad.getValue();
		}
		
	}
	

	
	

	
	public void onAccept$btnGuardar(){
		
		winRet.detach();
	}
	
	public void onClick$btnCancelar(){
		winRet.detach();
	}
	
	protected class ConfirmaGuardadoIngreso {

		public void show() {
			try {
				Messagebox.show("Cantidad retirada : $" + cantidadRetirada,
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
