package com.mxinteligente.controller.ventas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.mxinteligente.infra.PathContext;
import com.mxinteligente.model.entidades.Producto;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;
import com.mxinteligente.model.negocio.VentasServI;

public class Productos extends GenericForwardComposer implements
ListitemRenderer {
	
	protected DataBinder binder;
	private Window winProds;
	private int PAGE_SIZE;
	private ListModelList ModelProductos;
	private List datos;
	private Paging pg;
	private int totalDatos;
	private Listbox productos;
	private Window winUsuario, winBusqueda, winAlta, winEditar;
	private Usuarios buser, usuario;
	private Button btnGuardar, btnEliminar;

	@Autowired(required=true)
	private GeneralServI generalServ;
	
	@Autowired(required=true)
	VentasServI ventasServ;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication autenticacion = context.getAuthentication();
		usuario = new Usuarios();
		usuario.setEmail(autenticacion.getName());
		usuario = (Usuarios) generalServ.buscarObjeto(usuario).get(0);
		
		PAGE_SIZE = pg.getPageSize();
		totalDatos = ventasServ.obtenerTotalProductos(usuario.getId());
		pg.setTotalSize(totalDatos);
		datos = ventasServ.obtenerProductos(usuario.getId(), 0, PAGE_SIZE);
		
		ModelProductos = new ListModelList();
		ModelProductos.addAll(datos);
		productos.setModel(ModelProductos);
		productos.setItemRenderer(this);
		
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
	
	
	public void onClick$btnSaldos()throws Exception{
		winEditar = (Window) Executions.createComponents("/miSaldo/general.zul", null, null);
		winEditar.doModal();
		winEditar.setClosable(true);
		winEditar.setTitle("Historial de Ventas");
		
	}
	
	public void onDoubleClick$productos() throws Exception{
		Producto prodSel = (Producto) productos.getSelectedItem().getValue();
		Map parametros = new HashMap();
		
		parametros.put("usrId", usuario.getId());
		parametros.put("producto", prodSel.getId());
		
		winEditar = (Window) Executions.createComponents("/Ventas/Modificar.zul", null, parametros);
		winEditar.doModal();
		btnGuardar = (Button) winEditar.getFellow("btnGuardar", true);
		btnGuardar.addEventListener("onClick", new EventListener() {
			
			public void onEvent(Event event) throws Exception {
				winEditar.detach();
				totalDatos = ventasServ.obtenerTotalProductos(usuario.getId());
				pg.setTotalSize(totalDatos);
				redraw(0, PAGE_SIZE);
				binder.loadAll();
			}
		});
		
		btnEliminar = (Button) winEditar.getFellow("btnEliminar", true);

		
		btnEliminar.addEventListener("onAccept", new EventListener() {
			
			public void onEvent(Event event) throws Exception {
				winEditar.detach();
				totalDatos = ventasServ.obtenerTotalProductos(usuario.getId());
				pg.setTotalSize(totalDatos);
				redraw(0, PAGE_SIZE);
				binder.loadAll();
			}
		});
	}
	
	private void redraw(int primerResultado, int maxResultados)
	throws Exception {
		ModelProductos.clear();
		productos.setModel(ModelProductos);
		datos = ventasServ.obtenerProductos(usuario.getId(), primerResultado, maxResultados);
		ModelProductos.addAll(datos);
		productos.setModel(ModelProductos);
		productos.setItemRenderer(this);
		binder.loadAll();
		
			
		}
	
	
	public void render(Listitem item, Object dato) throws Exception {
		Producto prod = (Producto)dato;
		item.setValue(prod);
		if(prod.getImage1()!=null && prod.getImage1().length()>0){
			item.appendChild(new Listcell("", PathContext.getImgUrlThumbs() + prod.getImage1()));
		}
		
		item.appendChild(new Listcell( prod.getCode()));
		item.appendChild(new Listcell(prod.getName()));
		item.appendChild(new Listcell(prod.getPrice().toPlainString()));
		
	}
	
	
	public void onClick$btnAgregar() throws SuspendNotAllowedException, InterruptedException{
		Map parametros = new HashMap();
		parametros.put("usrId", usuario.getId());
		winAlta = (Window) Executions.createComponents("/Ventas/AltaProducto.zul", null, parametros);
		winAlta.doModal();
		btnGuardar = (Button) winAlta.getFellow("btnGuardar", true);
		btnGuardar.addEventListener("onClick", new EventListener() {
			
			public void onEvent(Event event) throws Exception {
				winAlta.detach();
				totalDatos = ventasServ.obtenerTotalProductos(usuario.getId());
				pg.setTotalSize(totalDatos);
				redraw(0, PAGE_SIZE);
				binder.loadAll();
			}
		});
		
		
	}

}
