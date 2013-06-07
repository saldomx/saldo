package com.mxinteligente.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.mxinteligente.controller.general.General;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.AdminServI;
import com.mxinteligente.model.negocio.GeneralServI;

public class UsuariosCtrl extends GenericForwardComposer implements
ListitemRenderer {
	
	protected DataBinder binder;
	private Window winAdmin;
	private int PAGE_SIZE;
	private ListModelList ModelUsuarios;
	private List datos;
	private Paging pg;
	private int totalDatos;
	private Listbox usuarios;
	private Window winUsuario, winBusqueda, winGenera;
	private Usuarios buser;

	@Autowired(required=true)
	private GeneralServI generalServ;
	
	@Autowired(required=true)
	private AdminServI adminServ;
	
	static Logger logger = LoggerFactory.getLogger(UsuariosCtrl.class);

	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		PAGE_SIZE = pg.getPageSize();
		totalDatos = adminServ.obtenerTotalUsuarios();
		pg.setTotalSize(totalDatos);
		datos = adminServ.obtenerUsuarios(0, PAGE_SIZE);
		ModelUsuarios = new ListModelList();
		ModelUsuarios.addAll(datos);
		usuarios.setModel(ModelUsuarios);
		usuarios.setItemRenderer(this);
		
		pg.addEventListener("onPaging", new EventListener() {
			public void onEvent(Event event) {
				PagingEvent pe = (PagingEvent) event;
				int pgno = pg.getActivePage();
				int ofs = pgno * (PAGE_SIZE);
				
				try {
					redraw(ofs, PAGE_SIZE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public void onClick$btnBuscar() throws Exception {

		buser = new Usuarios();
		Map params = new HashMap();
		String[] columnas = { "id", "email", "nombre", "app", "apm" };
		String[] where = { "rol" };
		Object[] valWhere = { "ROLE_USER" };
		params.put("columnas", columnas);
		params.put("join", "left");
		params.put("principal", buser.toString());
		params.put("where", where);
		params.put("valWhere", valWhere);

		winBusqueda = (Window) Executions.createComponents("/saldosAdmin/Busqueda.zul", null, params);
		winBusqueda.doModal();
		Listbox box = (Listbox) winBusqueda.getFellow("box", true);
		box.addEventListener("onDoubleClick", new EventListener() {
			public void onEvent(Event event) throws SuspendNotAllowedException, InterruptedException {
				buser = (Usuarios) winBusqueda.getAttribute("seleccionado");
				Usuarios userSelected = new Usuarios();
				userSelected =buser;
				Map params = new HashMap();
				
				params.put("user", userSelected);

				winUsuario = (Window) Executions.createComponents("/saldosAdmin/User.zul", null, params);
				winUsuario.doModal();
			}
		});
	}
	
	public void onDoubleClick$usuarios() throws SuspendNotAllowedException, InterruptedException{
		Usuarios userSelected = new Usuarios();
		userSelected = (Usuarios)usuarios.getSelectedItem().getValue();
		Map params = new HashMap();
		
		params.put("user", userSelected);

		winUsuario = (Window) Executions.createComponents("/saldosAdmin/User.zul", null, params);
		winUsuario.doModal();
		
	}
	
	private void redraw(int primerResultado, int maxResultados)
	throws Exception {
		ModelUsuarios.clear();
		usuarios.setModel(ModelUsuarios);
		datos = adminServ.obtenerUsuarios(primerResultado, maxResultados);
		ModelUsuarios.addAll(datos);
		usuarios.setModel(ModelUsuarios);
		usuarios.setItemRenderer(this);
		binder.loadAll();
		
			
		}
			

	public void render(Listitem item, Object dato) throws Exception {
		Usuarios user = (Usuarios)dato;
		item.setValue(user);
		item.appendChild(new Listcell(user.getId().toString()));
		item.appendChild(new Listcell(user.getEmail()));
		item.appendChild(new Listcell(user.getNombre()+ " " + user.getApp() + " " +user.getApm() ));
		item.appendChild(new Listcell(generalServ.calculaSaldo(user).toString()));
		
	}

	

	public void onClick$btnGenera() throws SuspendNotAllowedException, InterruptedException{
		winGenera = (Window) Executions.createComponents("/saldosAdmin/GeneraCodigos.zul", null, null);
		winGenera.doModal();
		
	}
}
