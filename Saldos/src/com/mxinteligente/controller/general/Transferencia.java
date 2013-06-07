package com.mxinteligente.controller.general;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.mxinteligente.controller.general.Egreso.ComboRender;
import com.mxinteligente.controller.general.Egreso.ConfirmaGuardadoIngreso;
import com.mxinteligente.infra.model.nego.MailService;
import com.mxinteligente.infra.model.nego.TemplateHtml;
import com.mxinteligente.model.entidades.Categresos;
import com.mxinteligente.model.entidades.CategresosId;
import com.mxinteligente.model.entidades.Egresos;
import com.mxinteligente.model.entidades.EgresosId;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;

public class Transferencia extends GenericForwardComposer  {

	protected DataBinder binder;
	private List categorias;
	private Usuarios usr;
	private ListModelList ModelCategorias;
	private Textbox textConcepto, textUsuario;
	private Decimalbox decCantidad;
	private Button btnGuardar;
	private Window winTrans;
	private Intbox intNip;

	@Autowired(required=true)
	private GeneralServI generalServ;
	
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		usr = (Usuarios) arg.get("user");
		
		}
	
	
	public void onClick$btnGuardar()throws Exception{
		if(textUsuario.isValid() && textConcepto.isValid() && decCantidad.isValid() ){
			Egresos eg = new Egresos();
			Categresos catEg = new Categresos();
			catEg.setId(new CategresosId());
			catEg.getId().setId(1); //ID de TRANSFERENCIAS
			catEg.getId().setUsuariosId(usr.getId());
			catEg.setUsuarios(usr);
			
			eg.setId(new EgresosId());
			eg.getId().setCatEgresosId(1);
			eg.getId().setCatEgresosUsuariosId(usr.getId());
			eg.setCategoria(catEg);
			eg.setConcepto(textConcepto.getValue());
			eg.setCantidad(decCantidad.getValue());
			eg.setFecha(new Date());
			
			int status = generalServ.realizarTransferencia(eg, usr, textUsuario.getValue(), intNip.getValue());
			if(status==1){
				Messagebox.show("No tienes saldo suficiente para este gasto, favor de verificar");
			}else if(status ==2 ){
				Messagebox.show("El usuario especificado no existe, favor de verificar");

			}
			else if(status ==-1 ){
				Messagebox.show("Ocurri— un error al transferir, intentar nuevamente");

			}else if(status ==3 ){
				Messagebox.show("El nip es incorrecto, favor de verificar");

			}
			else if(status == 0){
					new ConfirmaGuardadoIngreso().show();
			}
			
			
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


