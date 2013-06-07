package com.mxinteligente.infra.controller;


import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import org.zkoss.zkplus.databind.DataBinder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.*;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zk.ui.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mxinteligente.infra.model.nego.BusquedaServI;

public class BusquedaFiltro extends GenericForwardComposer implements
		ListitemRenderer {

	protected Object actual;
	protected Paging paginacion;
	private Textbox tfiltro;
	protected DataBinder binder;

	protected Class clase;

	private Listbox box, lstFiltro;
	private Listhead encabezado;

	protected ListModelList listModelList;

	protected Textbox txtBusqueda;
	protected Button btnFiltrar;
	protected Vbox camposFiltros;
	protected Groupbox panelBusqueda;
	private Auxhead headFiltro;

	@Autowired(required = true)
	protected BusquedaServI busquedaServicio;

	private String[] columnas;
	private String principal;
	private String tabla;
	private int inicial, maximo;
	private String join;
	private String[] filtros;
	private Object[] valores;
	private Textbox valor;
	
	private String[] where;
	private Object[] valWhere;
	
	private int PAGE_SIZE;
	private Window winBusqueda;
	private boolean esFiltrar = false;
	
	static Logger logger = LoggerFactory.getLogger(BusquedaFiltro.class);


	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		listModelList = new ListModelList();
		PAGE_SIZE = paginacion.getPageSize();

		this.obtenerArgumentos(); // Obtenemos los argumentos que son pasados a
									// Busqueda.zul
		actual = clase.newInstance();
		this.inicializaListBox(columnas);
		this.agregarOpcionFiltro();

		this.obtenerTodos();

		paginacion.addEventListener("onPaging", new EventListener() {
			public void onEvent(Event event) {
				PagingEvent pe = (PagingEvent) event;
				int pgno = pe.getActivePage();
				int ofs = pgno * PAGE_SIZE;
				try {
					if (esFiltrar)
						redraw(ofs, PAGE_SIZE, true);
					else
						redraw(ofs, PAGE_SIZE, false);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void obtenerTodos() throws Exception {
		int total = busquedaServicio.obtenerTotalFiltro(actual, filtros, valores, where, valWhere);
		if(total>0){
			paginacion.setTotalSize(total);
			redraw(0, PAGE_SIZE, false);
		}		

	}

	public boolean obtenerFiltros() throws Exception {
		int j = 0;
		try{
			
			filtros = new String[headFiltro.getChildren().size()];
			valores = new Object[headFiltro.getChildren().size()];

		for (Object li : this.headFiltro.getChildren()) {
			Auxheader sel = (Auxheader) li;
			Textbox txtvalor = (Textbox) sel.getChildren().get(0);
			
			
			
			if(txtvalor.getValue()!=null && !txtvalor.getValue().equals("")){
				filtros[j] = txtvalor.getId();
				valores[j] = this.obtenValor(filtros[j], txtvalor.getValue());
				if(valores[j] instanceof String){
					String mayusculas = (String)valores[j];
					valores[j] = mayusculas.toUpperCase(); 
						
				}
					
				j++;
			}
		}
		
		if( filtros.length==0 ){
			filtros=null;
			valores=null;
		}
		
		return true;
		
		}catch(Exception e){
		Messagebox.show("Datos incorrectos, favor de verificar");
		logger.error("Exception in BusquedaFiltro " + e.toString());
			return false;
		}
	}

	public void onDoubleClick$box() {
		int sel = box.getSelectedIndex();
		actual = listModelList.get(sel);
		binder.saveAll();
		winBusqueda.setAttribute("seleccionado", actual);
		winBusqueda.detach();

	}

	public void onClick$btnFiltrar() throws Exception {
		//obtenerFiltros();
		if (obtenerFiltros() && filtros.length > 0 && valores.length > 0 ) {

			int total = busquedaServicio.obtenerTotalFiltro(actual, filtros, valores, where, valWhere );
			
			paginacion.setTotalSize(total);
			redraw(0, PAGE_SIZE, true);
			binder.loadAll();
		} else {
			this.esFiltrar = false;
			this.obtenerTodos();
			redraw(0, PAGE_SIZE, false);
			binder.loadAll();
		}

		panelBusqueda.setOpen(false);
	}
	
	public void buscar(){
		try{

			if (obtenerFiltros() && filtros.length > 0 && valores.length > 0 ) {
				int total = busquedaServicio.obtenerTotalFiltro(actual, filtros, valores, where, valWhere );
				
				logger.info("total en Busqueda " + total );
				
				paginacion.setTotalSize(total);
				redraw(0, PAGE_SIZE, true);
				binder.loadAll();
			} else {
				this.esFiltrar = false;
				this.obtenerTodos();
				redraw(0, PAGE_SIZE, false);
				binder.loadAll();
			}
		}catch(Exception e ){
			
		}
		
	}

	public void agregarOpcionFiltroOld() {
		for (String col : columnas) {
			Listitem it = new Listitem();

			Listcell campoFiltro = new Listcell(Labels.getLabel(col.contains(".") ? col.substring(col.indexOf(".") + 1, col.length()):col));

			Textbox valor = new Textbox();
			valor.setId(col);
			Listcell valorFiltro = new Listcell();
			valorFiltro.appendChild(valor);

			it.appendChild(campoFiltro);
			it.appendChild(valorFiltro);

			lstFiltro.appendChild(it);

		}
	}
	
	public void agregarOpcionFiltro() {
		for (String col : columnas) {
			
			Auxheader fil = new Auxheader();
			
			valor = new Textbox();
			valor.setId(col);

			valor.setClass("descTextbox");
		
			valor.addEventListener("onChange", new EventListener() {
				public void onEvent(Event arg0) throws Exception {
					
					buscar();
				}
			});
			
			valor.addEventListener("onDoubleClick", new EventListener() {
				public void onEvent(Event arg0) throws Exception {
				}
			});
			
			
			
			fil.appendChild(valor);
			
			headFiltro.appendChild(fil);
			
			
		}
	}

	



	
	@SuppressWarnings("unchecked")
	private void redraw(int primerResultado, int maxResultados, boolean filtrado)
			throws Exception {

		listModelList.clear();
		box.setModel(listModelList);
		List lst;

		if (!filtrado) {
			lst = this.getDatosFiltro(primerResultado, maxResultados, filtros, valores, where,
					valWhere);
		} else {
			lst = this.getDatosFiltro(primerResultado, maxResultados, filtros,
					valores, where, valWhere);
		}
		if (lst!=null){
		listModelList.addAll(lst);
		box.setModel(listModelList);
		box.setItemRenderer(this);
		}
		else
		{
			Messagebox.show("Error al recargar los registros");

		}

	}

	public void obtenerArgumentos() {

		columnas = (String[]) arg.get("columnas");
		join = (String) arg.get("join");
		principal = (String) arg.get("principal");
		where = (String[]) arg.get("where");
		valWhere = (Object[]) arg.get("valWhere");



		if (principal.contains("@")) {
			tabla = principal.substring(0, principal.indexOf("@"));
		} else
			tabla = principal;

		try {
			clase = Class.forName(tabla);
		} catch (Exception e) {
			System.out.print("Error al iniciar clase");
		}
	}

	public void inicializaListBox(String[] columnas) {

		Listheader enc;
		Listcell celda;
		for (String col : columnas) {
			
			
			enc = new Listheader(Labels.getLabel(col.contains(".") ? col.substring(col.indexOf(".") + 1, col.length()):col));
			encabezado.appendChild(enc);
		}

	}

	public List getDatos(int inicial, int maximo) throws Exception {

		return busquedaServicio.buscarObjetos(actual, join, filtros, valores, where, valWhere,
				inicial, maximo, columnas);
	}

	public List getDatosFiltro(int inicial, int maximo, String filtro[],
			Object valor[], String filtroEq[],
			Object valorEq[]) throws Exception {
		return busquedaServicio.buscarObjetos(actual, join, filtro, valor, filtroEq, valorEq,
				inicial, maximo, columnas);
	}

	public void render(Listitem li, Object data) throws Exception {
		Method getMet = null;
		Object celda;
		for (String fi : columnas) {

			if (fi.contains("id.")) {
				Object objId;
				Method metId;
				metId = this.obtenMetodo(data, "id");
				objId = metId.invoke(data);
				getMet = this.obtenMetodo(objId, fi.substring(3));
				celda = getMet.invoke(objId);

				if (celda != null)
					new Listcell(" " + getMet.invoke(objId, new Object[] {}))
							.setParent(li);
				else
					new Listcell(" ").setParent(li);

			} else if (fi.contains(".")) {
				Method metodo;
				Object objetoRelacionado;

				metodo = this.obtenMetodo(data, fi.substring(0, fi
						.lastIndexOf(".")));
				objetoRelacionado = metodo.invoke(data);

				if (objetoRelacionado != null) {
					String metodoRel = fi.substring(fi.indexOf(".") + 1);
					getMet = this.obtenMetodo(objetoRelacionado, metodoRel);

					celda = getMet.invoke(objetoRelacionado);

					if (celda != null)
						new Listcell(" "
								+ getMet.invoke(objetoRelacionado,
										new Object[] {})).setParent(li);
					else
						new Listcell(" ").setParent(li);

				} else
					new Listcell(" ").setParent(li);
			}

			else {
				getMet = this.obtenMetodo(data, fi);
				celda = getMet.invoke(data);
				if (celda != null)
					new Listcell(" " + getMet.invoke(data, new Object[] {}))
							.setParent(li);
				else
					new Listcell(" ").setParent(li);
			}

		}
	}

	public Method obtenMetodo(Object obj, String metodo) {
		Method[] mets = obj.getClass().getDeclaredMethods();
		for (Method itsMet : mets) {
			if (itsMet.getName().toUpperCase().startsWith(
					("get" + metodo).toUpperCase())) {

				return itsMet;

			}
		}
		return null;

	}

	private Object obtenValor(String filtro, String valor) throws Exception {
		Method metodo = null;
		Class tipo;
		Object entidad = clase.newInstance();
		if (filtro.startsWith("id")) {
			Class claseId;
			claseId = Class.forName(tabla + "Id");
			Object objId;
			Method metId;
			metId = this.obtenMetodo(actual, "id");
			objId = claseId.newInstance();
			metodo = this.obtenMetodo(objId, filtro.substring(3));
			tipo = metodo.getReturnType();

			if (tipo.getSimpleName().equals("long")
					|| tipo.getSimpleName().equals("Long"))
				return new Long(valor);
			else if (tipo.getSimpleName().equals("int")
					|| tipo.getSimpleName().equals("Integer"))
				return new Integer(valor);
			else if (tipo.getSimpleName().equals("String"))
				return valor;
			else if (tipo.getSimpleName().equals("byte")
					|| tipo.getSimpleName().equals("Byte"))
				return new Byte(valor);

		} else if (filtro.contains(".")) {
			return new String(valor);
		}

		else {

			metodo = this.obtenMetodo(actual, filtro);
			tipo = metodo.getReturnType();

			if (tipo.getSimpleName().equals("long")
					|| tipo.getSimpleName().equals("Long"))
				return new Long(valor);
			else if (tipo.getSimpleName().equals("int")
					|| tipo.getSimpleName().equals("Integer"))
				return new Integer(valor);
			else if (tipo.getSimpleName().equals("String"))
				return valor;
			else if (tipo.getSimpleName().equals("byte")
					|| tipo.getSimpleName().equals("Byte"))
				return new Byte(valor);

		}

		return null;

	}
	
    


}
