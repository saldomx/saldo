package com.mxinteligente.controller.general;

import java.math.BigDecimal;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import com.mxinteligente.model.entidades.Catingresos;
import com.mxinteligente.model.entidades.CatingresosId;
import com.mxinteligente.model.entidades.Ingresos;
import com.mxinteligente.model.entidades.IngresosId;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;

public class Ingreso extends GenericForwardComposer  {

	protected DataBinder binder;
	private List categorias;
	private Usuarios usr;
	private ListModelList ModelCategorias;
	private Combobox CmbCategorias;
	private Textbox textConcepto;
	private Textbox decCantidad;
	private Button btnGuardar;
	private Window winIng;

	@Autowired(required=true)
	private GeneralServI generalServ;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		usr = (Usuarios) arg.get("user");
		this.cargarCategoriaIngresos();
		
		}
	
	
	public void onClick$btnGuardar()throws Exception{
		if(!CmbCategorias.getValue().equals("") && textConcepto.isValid()  ){
			Map Cat = (Map)CmbCategorias.getSelectedItem().getValue();
			if(Cat!=null){
				int idCat =(Integer) Cat.get("id");
				Catingresos categoria =new Catingresos();
				categoria.setId(new CatingresosId(idCat, usr.getId()));
				Ingresos nuevoIngreso  = new Ingresos();
				nuevoIngreso.setId(new IngresosId());
				nuevoIngreso.setCategoria(categoria);
				nuevoIngreso.getId().setCatIngresosId(idCat);
				nuevoIngreso.getId().setCatIngresosUsuariosId(usr.getId());
				nuevoIngreso.setConcepto(textConcepto.getValue());
				nuevoIngreso.setCantidad(new BigDecimal(decCantidad.getValue()));
				nuevoIngreso.setFecha(new Date());
				
				if(!generalServ.insertarIngreso(nuevoIngreso)){
					Messagebox.show("Error al guardar el ingreso, favor de verificar");
					
				}else{
					new ConfirmaGuardadoIngreso().show();
					
				}
			
			}
		}else{
			Messagebox.show("Datos Invalidos, favor de verificar");
			CmbCategorias.setFocus(true);
			textConcepto.setFocus(true);
			decCantidad.setFocus(true);
		}
	}
	
	public void cargarCategoriaIngresos(){
		List<Map> lstCat = generalServ.obtenCategoriasIngresos(usr);
		if(!lstCat.isEmpty()){
			ModelCategorias = new ListModelList(lstCat);
			CmbCategorias.setItemRenderer(new ComboRender());
			CmbCategorias.setModel(ModelCategorias);
			CmbCategorias.addEventListener("onInitRenderLater", this);
			CmbCategorias.setValue("");

		}
	}
	
	
	class ComboRender implements ComboitemRenderer {
		public void render(Comboitem item, Object value) throws Exception {
			String valor;
			String texto;
			Map m = new HashMap((Map) value);
			texto = m.get("nombre").toString();
			valor = m.get("id").toString();
			item.setLabel(texto);
			item.setValue(m);
		}
	}
	
	public void onAccept$btnGuardar(){
		
		winIng.detach();
	}
	
	public void onClick$btnCancelar(){
		winIng.detach();
	}
	
	protected class ConfirmaGuardadoIngreso {

		public void show() {
			try {
				Messagebox.show("El ingreso ha sido guardado satisfactoriamente",
						"Ingreso", Messagebox.OK, Messagebox.INFORMATION,
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
