package com.mxinteligente.controller.admin;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.Date;
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
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.mxinteligente.controller.general.General;
import com.mxinteligente.model.entidades.Categresos;
import com.mxinteligente.model.entidades.CategresosId;
import com.mxinteligente.model.entidades.Catingresos;
import com.mxinteligente.model.entidades.CatingresosId;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;

public class UserCtrl  extends GenericForwardComposer implements
ListitemRenderer {
	
	protected DataBinder binder;
	private Window winGen;
	private List categorias;
	private List datos;
	private Usuarios usr;
	private ListModelList ModelCategorias, modelDatos;
	private Combobox CmbCategorias;
	private Radiogroup tipo;
	private Listbox gentabla;
	private Paging pg;
	private int PAGE_SIZE;
	private int totalDatos = 0;
	private Catingresos catIng;
	private Categresos catEgr;
	private int tipoFiltro;
	private Window winCategoria, winIngreso, winEgreso;
	private Button btnGuardar;
	private Auxheader lblSaldo;
	private int intusuario;
	private Usuarios usuario;
	private Window winUsr;
	
	@Autowired(required=true)
	private GeneralServI generalServ;
	
	static Logger logger = LoggerFactory.getLogger(General.class);

	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		
		usuario = (Usuarios) arg.get("user");

		winUsr.setTitle("Usuario : " + usuario.getNombre() + " " + usuario.getApp() + " " + usuario.getApm());
		
		intusuario  = usuario.getId();
		usr=new Usuarios();
		usr.setId(intusuario);
		
		Usuarios usrActual = usuario;
		
		PAGE_SIZE = pg.getPageSize();
		
		totalDatos = generalServ.obtenTotalEgresos(usr) + generalServ.obtenTotalIngresos(usr);
		pg.setTotalSize(totalDatos);
		
		datos = generalServ.obtenTodoslosRegistros(usr, 0, PAGE_SIZE);
		lblSaldo.setLabel(usrActual.getNombre() + " " + usrActual.getApp() + " " + usrActual.getApm() +" Saldo: $"+generalServ.calculaSaldo(usr).toString());
		
		modelDatos = new ListModelList();
		
		modelDatos.addAll(datos);
		gentabla.setModel(modelDatos);
		gentabla.setItemRenderer(this);
		gentabla.addEventListener("onInitRenderLater", this);
		tipoFiltro = 1;
		
		pg.addEventListener("onPaging", new EventListener() {
			public void onEvent(Event event) {
				PagingEvent pe = (PagingEvent) event;
				int pgno = pg.getActivePage();
				int ofs = pgno * (PAGE_SIZE);
				
				try {
					redraw(ofs, PAGE_SIZE,
							tipoFiltro );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public List obtenerIngresos(Usuarios user, int min, int max){
		List<Map> lstIng = generalServ.obtenIngresos(user,min,max);
		
		for(Map mapa: lstIng){
			mapa.put("tipo", "Ingreso");
		}
		return lstIng;
	}
	
	
	public List obtenerEgresos(Usuarios user, int min, int max){
		List<Map> lstIng = generalServ.obtenEgresos(user,min,max);
		
		for(Map mapa: lstIng){
			mapa.put("tipo", "Egreso");
		}
		return lstIng;

	}
	
	public List obtenerIngresosCat(Catingresos cat, int min, int max){
		List<Map> lstIng = generalServ.obtenIngresosPorCategoria(cat, min, max);
		BigInteger in;
		in = BigInteger.valueOf(1);
		for(Map mapa: lstIng){
			mapa.put("tipo", in);
		}
		return lstIng;
	}
	
	public List obtenerEgresosCat(Categresos cat, int min, int max){
		List<Map> lstIng = generalServ.obtenEgresosPorCategoria(cat, min, max);
		BigInteger in;
		in = BigInteger.valueOf(2);
		for(Map mapa: lstIng){
			
			mapa.put("tipo", in);
		}
		return lstIng;
		
	}
	
	public void onCheck$tipo()throws Exception{
		String type = tipo.getSelectedItem().getValue();
		int t = Integer.parseInt(type);
		
		if(t==0){
			totalDatos = generalServ.obtenTotalEgresos(usr) + generalServ.obtenTotalIngresos(usr);
			pg.setTotalSize(totalDatos);
			
			this.limpiarCombo();
			CmbCategorias.setDisabled(true);
			tipoFiltro = 1; 
			redraw(0, this.PAGE_SIZE, tipoFiltro);
			binder.loadAll();
		}
		else if(t==1){
			this.limpiarCombo();
			
			CmbCategorias.setDisabled(false);
			this.modelDatos.clear();
			this.gentabla.setModel(modelDatos);
			gentabla.setItemRenderer(this);
			
			this.cargarCategoriaIngresos();
		}
		else if(t==2){
			this.limpiarCombo();
			
			this.modelDatos.clear();
			this.gentabla.setModel(modelDatos);
			gentabla.setItemRenderer(this);
			
			CmbCategorias.setDisabled(false);
			this.cargarCategoriaEgresos();
		}
		
	}
	
	public void limpiarCombo(){
		List lstitem = CmbCategorias.getItems();
		
		for(int i=0; i< lstitem.size(); i++){
			CmbCategorias.removeItemAt(i);
		}
		CmbCategorias.setValue("");
		
	}
	public void cargarCategoriaEgresos(){

		List<Map> lstCat = generalServ.obtenCategoriasEgresos(usr);
		Map todos = new HashMap();
		todos.put("nombre", "Todos");
		todos.put("id", 0);
		todos.put("usrid", usr.getId());
		lstCat.add(0, todos);
		//Collection
		if(!lstCat.isEmpty()){
			ModelCategorias = new ListModelList(lstCat);
			CmbCategorias.setItemRenderer(new ComboRender());
			CmbCategorias.setModel(ModelCategorias);
			CmbCategorias.addEventListener("onInitRenderLater", this);
			CmbCategorias.setValue("");
		}
		
	}
	
	public void cargarCategoriaIngresos(){
		List<Map> lstCat = generalServ.obtenCategoriasIngresos(usr);
		Map todos = new HashMap();
		todos.put("nombre", "Todos");
		todos.put("id", 0);
		todos.put("usrid", usr.getId());
		lstCat.add(0, todos);
		//Collection
		if(!lstCat.isEmpty()){
			ModelCategorias = new ListModelList(lstCat);
			CmbCategorias.setItemRenderer(new ComboRender());
			CmbCategorias.setModel(ModelCategorias);
			CmbCategorias.addEventListener("onInitRenderLater", this);
			CmbCategorias.setValue("");

		}
	}
	
	public void onSelect$CmbCategorias() throws Exception{
		Map valor = (Map) CmbCategorias.getSelectedItem().getValue();
		logger.info("la clave es " + valor.get("id"));
		
		String type = tipo.getSelectedItem().getValue();
		int t = Integer.parseInt(type);
		
		
		if(t==1){
			catIng = new Catingresos();
			catIng.setId(new CatingresosId((Integer)valor.get("id"), usr.getId()));
			pg.setTotalSize(generalServ.obtenTotalIngresosPorCategoria(catIng));
			tipoFiltro = 3; 
			redraw(0, this.PAGE_SIZE, tipoFiltro);
		}
		else if(t==2){
			catEgr = new Categresos();
			catEgr.setId(new CategresosId((Integer)valor.get("id"), usr.getId()));
			pg.setTotalSize(generalServ.obtenTotalEgresosPorCategoria(catEgr));
			tipoFiltro = 2; 
			redraw(0, this.PAGE_SIZE, tipoFiltro);
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
	
	private void redraw(int primerResultado, int maxResultados, int tipo)
	throws Exception {
		modelDatos.clear();
		gentabla.setModel(modelDatos);
		
		if(tipo==1){
		//	datos = this.obtenerIngresos(usr,primerResultado, maxResultados);
		//	datos.addAll(this.obtenerEgresos(usr, primerResultado, maxResultados));
			datos = generalServ.obtenTodoslosRegistros(usr, primerResultado, maxResultados);

			
		}else if(tipo==2){
			datos = this.obtenerEgresosCat(catEgr, primerResultado, maxResultados);
		}
		else if(tipo == 3){
			datos = this.obtenerIngresosCat(catIng, primerResultado, maxResultados);
		}

		if (datos != null) {
			modelDatos.addAll(datos);
			gentabla.setModel(modelDatos);
			gentabla.setItemRenderer(this);
			binder.loadAll();
		}else{
			modelDatos.clear();
			gentabla.setModel(modelDatos);
			gentabla.setItemRenderer(this);
			binder.loadAll();
		}
			
}

	

	
	public void onClick$btnIngreso()throws Exception{
		Map params = new HashMap();
		params.put("user", usr);

		winIngreso = (Window) Executions.createComponents("/miSaldo/Ingreso.zul", null, params);
		winIngreso.doModal();
		btnGuardar = (Button) winIngreso.getFellow("btnGuardar", true);
		btnGuardar.addEventListener("onAccept", new EventListener() {
			public void onEvent(Event event) throws Exception {
				lblSaldo.setLabel("Saldo: $"+generalServ.calculaSaldo(usr).toString());
				totalDatos = generalServ.obtenTotalEgresos(usr) + generalServ.obtenTotalIngresos(usr);
				pg.setTotalSize(totalDatos);
				tipoFiltro = 1; 
				redraw(0, PAGE_SIZE, tipoFiltro);
				binder.loadAll();
			}
		});
	}
	

	
	public class HashMapComparator2 implements Comparator
    {
        public int compare ( Object object1 , Object object2 )
        {
        	    Date fecha1 = (( Date ) ( ( HashMap ) object1 ).get ( "fecha" ));
        	    Date fecha2 = (( Date ) ( ( HashMap ) object2 ).get ( "fecha" ));
        	  
                
                return fecha1.compareTo ( fecha2 ) ;
           
        }
    }

	
	public void render(Listitem item, Object data) throws Exception {
		Map d = (Map) data;
		java.text.SimpleDateFormat formatDate=new java.text.SimpleDateFormat("dd/MM/yy");
		Date fecha = (Date) d.get("fecha");
		item.setValue(d);
		BigInteger tipo= (BigInteger)d.get("tipo");
		
		
		if(tipo.toString().equals("1")){
			item.appendChild(new Listcell("Ingreso", "/images/ing-16.png"));
		}
		else
			item.appendChild(new Listcell("Gasto", "/images/egr-16.png"));
		
		item.appendChild(new Listcell(d.get("Nombre") + ""));
		item.appendChild(new Listcell(d.get("concepto") + ""));
		String scantidad="";
		if(d.get("cantidad") instanceof Double){
			Double dcant = (Double) d.get("cantidad");
			scantidad = dcant.toString();
		}else if(d.get("cantidad") instanceof BigInteger){
			BigDecimal cant = (BigDecimal) d.get("cantidad");
			scantidad = cant.toString();
		}
		item.appendChild(new Listcell(d.get("cantidad") + ""));
		item.appendChild(new Listcell(formatDate.format(fecha)));

		
		
	}
	
	

}

