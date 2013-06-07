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

import com.mxinteligente.model.entidades.Categresos;
import com.mxinteligente.model.entidades.CategresosId;
import com.mxinteligente.model.entidades.Egresos;
import com.mxinteligente.model.entidades.EgresosId;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;

public class Egreso extends GenericForwardComposer  {

	protected DataBinder binder;
	private List categorias;
	private Usuarios usr;
	private ListModelList ModelCategorias;
	private Combobox CmbCategorias;
	private Textbox textConcepto;
	private Textbox decCantidad;
	private Button btnGuardar;
	private Window winEg;

	@Autowired(required=true)
	private GeneralServI generalServ;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		usr = (Usuarios) arg.get("user");
		this.cargarCategoriaEgresos();
		
		}
	
	
	public void onClick$btnGuardar()throws Exception{
		if(!CmbCategorias.getValue().equals("") && textConcepto.isValid() && decCantidad.isValid() ){
			Map Cat = (Map)CmbCategorias.getSelectedItem().getValue();
			if(Cat!=null){
				int idCat =(Integer) Cat.get("id");
				Categresos categoria =new Categresos();
				categoria.setId(new CategresosId(idCat, usr.getId()));
				Egresos nuevoGasto  = new Egresos();
				nuevoGasto.setId(new EgresosId());
				nuevoGasto.setCategoria(categoria);
				nuevoGasto.getId().setCatEgresosId(idCat);
				nuevoGasto.getId().setCatEgresosUsuariosId(usr.getId());
				nuevoGasto.setConcepto(textConcepto.getValue());
				nuevoGasto.setCantidad(new BigDecimal(decCantidad.getValue()));
				nuevoGasto.setFecha(new Date());
				int status = generalServ.insertarEgreso(nuevoGasto, usr);
				
				if(status ==-1){
					Messagebox.show("Error al guardar el ingreso, favor de verificar");
				}
				else if(status == 1){
						Messagebox.show("No tienes saldo suficiente para este gasto, favor de verificar");
				}	
				else if(status == 0){
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
	
	public void cargarCategoriaEgresos(){

		List<Map> lstCat = generalServ.obtenCategoriasEgresos(usr);
		Map todos = new HashMap();
		todos.put("nombre", "Todos");
		todos.put("id", 0);
		todos.put("usrid", usr.getId());
		//lstCat.add(0, todos);
		//Collection
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
		
		winEg.detach();
	}
	
	public void onClick$btnCancelar(){
		winEg.detach();
	}
	
	protected class ConfirmaGuardadoIngreso {

		public void show() {
			try {
				Messagebox.show("El gasto ha sido guardado satisfactoriamente",
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
