package com.mxinteligente.controller.general;




import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.mxinteligente.model.entidades.Categresos;
import com.mxinteligente.model.entidades.CategresosId;
import com.mxinteligente.model.entidades.Catingresos;
import com.mxinteligente.model.entidades.CatingresosId;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;

public class Categoria extends GenericForwardComposer  {

	private Usuarios user;
	private Radiogroup tipoCat;
	private Textbox categoria;
	private Window winCat;
	private Button btnGuardar;
	
	@Autowired(required=true)
	private GeneralServI generalServ;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		user = (Usuarios) arg.get("user");
		
		
	}
	
	public void onClick$btnCancelar()throws Exception{
		winCat.detach();
	}
	
	public void onClick$btnGuardar()throws Exception{
		if(categoria.isValid()){
		String tipo = tipoCat.getSelectedItem().getValue();
		
		if(tipo.equals("1")){
			Catingresos catin= new Catingresos();
			catin.setUsuarios(user);
			catin.setId(new CatingresosId());
			catin.getId().setUsuariosId(user.getId());
			catin.setNombre(this.categoria.getValue());
			
			if(!generalServ.insertarCategoriaIngreso(catin)){
				Messagebox.show("Error al insertar Categoria");
				}
			else{
				new ConfirmaGuardadoIngreso().show();
			}
					
		}
		else{
			Categresos categ= new Categresos();
			categ.setUsuarios(user);
			categ.setId(new CategresosId());
			categ.getId().setUsuariosId(user.getId());
			categ.setNombre(this.categoria.getValue());
			
			if(!generalServ.insertarCategoriaEgresos(categ)){
				Messagebox.show("Error al insertar Categoria");
				}
			else{
				new ConfirmaGuardadoEgreso().show();
			}
					
		}
		}else
			categoria.setFocus(true);
		
	}
	
	public void onAcceptYesIngreso$btnGuardar(){
		winCat.detach();
	}
	
	public void onAcceptYesEgreso$btnGuardar(){
		winCat.detach();
	}
	
	protected class ConfirmaGuardadoIngreso {

		public void show() {
			try {
				Messagebox.show("Categoria Guardada",
						"Categoria", Messagebox.OK, Messagebox.INFORMATION,
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

			Events.sendEvent(new Event("onAcceptYesIngreso", btnGuardar));
		}
	}
	
	protected class ConfirmaGuardadoEgreso {

		public void show() {
			try {
				Messagebox.show("Categoria Guardada",
						"Categoria", Messagebox.OK, Messagebox.INFORMATION,
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

			Events.sendEvent(new Event("onAcceptYesEgreso", btnGuardar));
		}
	}
}
