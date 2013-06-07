package com.mxinteligente.controller.general;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.mxinteligente.infra.model.nego.MailService;
import com.mxinteligente.infra.model.nego.TemplateHtml;
import com.mxinteligente.model.entidades.Estados;
import com.mxinteligente.model.entidades.Ingresos;
import com.mxinteligente.model.entidades.IngresosId;
import com.mxinteligente.model.entidades.Pais;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;



public class Recarga extends GenericForwardComposer  {

	static Logger logger = LoggerFactory.getLogger(Recarga.class);

	
	private Textbox textNombre, codigo, textApellidos, textCiudad, textCalle,textCP;
	private Intbox intNip;
	private Textbox numeroCuenta;
	private Combobox cmbCantidad,CreditCard, cmbPais,cmbEstado;
	private DataBinder binder;
	private List<Pais> paises;
	private List<Estados> estados;
	private ListModelList LMod;
	private Window pin, winRec;
	private Button btnConfirmar;
	private Datebox fchExpiracion;
	private Button btnAceptar;
	
	@Autowired(required=true)
	MailService mailService;
	
	 @Autowired
	 private TemplateHtml tamplateHtml; 

	
	@Autowired(required=true)
	private GeneralServI generalServ;
	private Usuarios usr;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		paises = generalServ.obtenerPaises();
		usr = (Usuarios) arg.get("user");
		
		for (Pais p : paises) {
			Comboitem camitem = new Comboitem(p.getNombre());
			camitem.setValue(p);
			cmbPais.appendChild(camitem);
		}
		cmbCantidad.setSelectedIndex(0);
		CreditCard.setSelectedIndex(0);
		
		}
	
	public void onChange$cmbPais(){
		LMod = new ListModelList();
		if(!LMod.isEmpty()){
			LMod.clear();
			cmbEstado.setModel(LMod);
		}
		
		if(cmbPais.getSelectedItem()!=null){
			Pais p = (Pais)cmbPais.getSelectedItem().getValue();
			estados = generalServ.obtenerEstados(p.getIso());
			LMod = new ListModelList(estados);
			cmbEstado.setItemRenderer(new ComboRender());
			cmbEstado.setModel(LMod);
			cmbEstado.addEventListener("onInitRenderLater", this);
		}
		
		if(cmbEstado.getItemCount()>0){
			cmbEstado.setSelectedIndex(0);
			
		}
		binder.loadAll();
	}
	
	
	public void onClick$btnAceptar()throws Exception
	{
		if(textApellidos.isValid() && textCalle.isValid() && textCiudad.isValid() && textCP.isValid()
		&& textNombre.isValid() && cmbCantidad.isValid() && cmbEstado.isValid() && cmbPais.isValid() && numeroCuenta.isValid() && codigo.isValid()
		&& CreditCard.isValid()
		){


		pin = (Window) Executions.createComponents("/miSaldo/nip.zul", null, null);
		pin.doModal();
		btnConfirmar = (Button) pin.getFellow("btnConfirmar", true);
		intNip = (Intbox) pin.getFellow("intNip", true);
		btnConfirmar.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) throws Exception {
				usr = (Usuarios) generalServ.buscarObjeto(usr).get(0);
				
				
				if(usr.getNip().compareTo(intNip.getValue()) != 0){
					Messagebox.show("El nip es incorrecto, favor de verificar");
				}
				else{
					Date fecha = fchExpiracion.getValue();
					Calendar exp = Calendar.getInstance();
					exp.setTime(fecha);
					int mes = exp.get(Calendar.MONTH);
					String smes="";
					if(mes<9)
						smes = "0" + (mes+1);
					else
						smes = "" + (mes + 1);
					
					String sexpdate = smes + exp.get(Calendar.YEAR);
					String estado = (String)cmbEstado.getSelectedItem().getValue();
					Pais pais =(Pais)cmbPais.getSelectedItem().getValue();
					
					String status = generalServ.realizarPago("Sale", (String)cmbCantidad.getSelectedItem().getValue(),
							(String)CreditCard.getSelectedItem().getValue(), numeroCuenta.getValue(), sexpdate, codigo.getValue(), 
							textNombre.getValue(), textApellidos.getValue(), textCalle.getValue(), textCiudad.getValue(), 
							estado,textCP.getValue(), pais.getIso());
					
					logger.info("status " + status);
					
					if(status.contains("Success")){
						Ingresos ing =new Ingresos();
						ing.setId(new IngresosId());
						ing.getId().setCatIngresosUsuariosId(usr.getId());
						ing.getId().setCatIngresosId(1);
						ing.setConcepto("Recarga de Saldo");
						ing.setContraparte("N/A");
						ing.setFecha(new Date());
						ing.setCantidad(new BigDecimal((String)cmbCantidad.getSelectedItem().getValue()));
						
						if(generalServ.insertarIngreso(ing)){
							//Messagebox.show("Se han recargado : $" + ing.getCantidad().toPlainString() + " satisfactoriamente ");
							pin.detach();
							new ConfirmaGuardadoIngreso().show();
						}else{
							Messagebox.show("No se pudo completar, la recarga de saldo, favor de verificar.");
							String body2 = tamplateHtml.templateErrorTransferencia(usr.getNombre() + " " + usr.getApp() + " " +usr.getApm(),
									usr.getEmail() + " " + usr.getApp() + " " +usr.getApm(), ing.getId().getId(), new Date().toString(), ing.getCantidad().toPlainString());
							
							mailService.sendMail("avisos@saldo.mx", "marco@saldo.mx" , "ERROR DE INSERCION ", body2);
							
						}
						
					}else{
						Messagebox.show("No se pudo completar, la recarga de saldo, favor de verificar.");
					}
					
				}
			}
		});
		}else
			textNombre.getValue();
	}
	
	public void onAccept$btnAceptar(){
		winRec.detach();
	}
	
	protected class ConfirmaGuardadoIngreso {

		public void show() {
			try {
				Messagebox.show("La recarga se ha realizado  satisfactoriamente",
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
			Events.sendEvent(new Event("onAccept", btnAceptar));
		}
	}
	
	class ComboRender implements ComboitemRenderer {
		public void render(Comboitem item, Object value) throws Exception {
			String valor;
			String texto;
			Estados m = (Estados) value;
			texto = m.getNombre();
			valor = m.getClave();
			item.setLabel(texto);
			item.setValue(valor);
		}
	}
	
	


}
