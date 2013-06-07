package com.mxinteligente.model.negocio.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.mxinteligente.infra.DoDirectPayment;
import com.mxinteligente.infra.model.nego.MailService;
import com.mxinteligente.infra.model.nego.TemplateHtml;
import com.mxinteligente.infra.model.nego.impl.GenServicio;
import com.mxinteligente.model.dao.CategresosDaoI;
import com.mxinteligente.model.dao.CatingresosDaoI;
import com.mxinteligente.model.dao.CodigosDaoI;
import com.mxinteligente.model.dao.EgresosDaoI;
import com.mxinteligente.model.dao.IngresosDaoI;
import com.mxinteligente.model.dao.LocalidadesDaoI;
import com.mxinteligente.model.dao.UsuariosDaoI;
import com.mxinteligente.model.entidades.Categresos;
import com.mxinteligente.model.entidades.CategresosId;
import com.mxinteligente.model.entidades.Catingresos;
import com.mxinteligente.model.entidades.Codigos;
import com.mxinteligente.model.entidades.Egresos;
import com.mxinteligente.model.entidades.EgresosId;
import com.mxinteligente.model.entidades.Estados;
import com.mxinteligente.model.entidades.Ingresos;
import com.mxinteligente.model.entidades.IngresosId;
import com.mxinteligente.model.entidades.Movimientos;
import com.mxinteligente.model.entidades.Pais;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;

@Service("generalServ")
public class GeneralServ extends GenServicio implements GeneralServI{

	@Autowired(required=true)
	private CategresosDaoI categresosDao;
	
	@Autowired(required=true)
	private CatingresosDaoI catingresosDao;
	
	@Autowired(required=true)
	private EgresosDaoI egresosDao;
	
	@Autowired(required=true)
	private IngresosDaoI ingresosDao;
	
	@Autowired(required=true)
	private UsuariosDaoI usuariosDao;
	
	@Autowired(required=true)
	CodigosDaoI codigosDao;
	
	@Autowired(required=true)
	MailService mailService;
	
	 @Autowired
	 private TemplateHtml tamplateHtml; 

	 @Autowired
	 LocalidadesDaoI localidadesDao;
	 
	 
	 
	 
	static Logger logger = LoggerFactory.getLogger(GeneralServ.class);

	
	public List<Map> obtenCategoriasEgresos(Usuarios user) {
		return categresosDao.obtenerCategorias(user);
	}

	public List<Map> obtenCategoriasIngresos(Usuarios user) {
		return catingresosDao.obtenerCategorias(user);
	}

	public List obtenEgresos(Usuarios user, int min, int max) {
		return this.egresosDao.obtenerTodosEgresos(user, min, max);
	}

	public List obtenIngresos(Usuarios user, int min, int max) {
		return ingresosDao.obtenerTodosIngresos(user,  min,  max);
	}

	public int obtenTotalEgresos(Usuarios user) {
		return egresosDao.obtenerTotalTodosEgresos(user);
	}

	public int obtenTotalIngresos(Usuarios user) {
		return ingresosDao.obtenerTotalTodosIngresos(user);
	}

	
	public List obtenIngresosPorCategoria(Catingresos cat, int min, int max) {
		return ingresosDao.obtenerIngresosPorCategoria(cat, min, max);
	}

	public List obtenEgresosPorCategoria(Categresos cat, int min, int max) {
		return egresosDao.obtenerEgresosPorCategoria(cat, min, max);
	}

	public int obtenTotalIngresosPorCategoria(Catingresos cat) {
		return ingresosDao.obtenerTotalIngresosPorCategoria(cat);
	}

	public int obtenTotalEgresosPorCategoria(Categresos cat) {
		
		return egresosDao.obtenerTotalEgresosPorCategoria(cat);
	}

	public List<Map> obtenTodoslosRegistros(Usuarios user, int min, int max) {
		
		return ingresosDao.obtenerTodo(user, min, max);
	}

	
	public boolean insertarCategoriaIngreso(Catingresos cat) {
		try{
		int id = catingresosDao.obtenerSiguienteId(cat.getUsuarios());
		cat.getId().setId(id);
		super.insertar(cat);
		return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}

	
	public boolean insertarCategoriaEgresos(Categresos cat) {
		try{
			int id = categresosDao.obtenerSiguienteId(cat.getUsuarios());
			cat.getId().setId(id);
			super.insertar(cat);
			return true;
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
	}

	
	public boolean insertarIngreso(Ingresos i){
		return ingresosDao.insertarIngreso(i);
	}

	
	public BigDecimal calculaSaldo(Usuarios user) {
		BigDecimal sumIngreso = new BigDecimal("0");
		BigDecimal sumEgreso = new BigDecimal("0");
		
		sumIngreso = ingresosDao.sumaIngresos(user);
		sumEgreso = egresosDao.sumaEgresos(user);
		
	
		return sumIngreso.subtract(sumEgreso);
	}

	
	public int insertarEgreso(Egresos e, Usuarios user) {
		BigDecimal saldo = this.calculaSaldo(user);
		
		if(e.getCantidad().compareTo(saldo) == 1){
			return 1;
		}
		else{
			return egresosDao.insertarEgreso(e);
		}
	}

	public int realizarTransferencia(Egresos gasto, Usuarios user,
			String email, Integer pin) {
		int status = -1;
		BigDecimal saldo = this.calculaSaldo(user);
		if (usuariosDao.validaPin(user.getId(), pin) != null) {
			if (gasto.getCantidad().compareTo(saldo) == 1) { // No tiene
																// suficiente
																// saldo para
																// dar de alta
																// un Gasto
				return 1; // Regresa uno si no tine saldo suficiente
			} else {
				Usuarios bUser = usuariosDao.isUsurio(email); // Revisar
				user = usuariosDao.buscarPorObjeto(user).get(0);											// primero si
																// existe el
																// usuario
				if (bUser != null) {
					
					status =  egresosDao.transferir(gasto, user, bUser);
					return status;
				} else {
					return 2; // Regresa 2 si no existe el usuario
				}

			}
		} else
			return 3; //pin invalido
	}
	
	public String cargarSaldo(String codigo, String user){
		try{
		Codigos code = new Codigos();
		code =  codigosDao.buscarCodigo(codigo);
		if(code!=null){
			Usuarios usr= new Usuarios();
			usr.setId(Integer.parseInt(user));
			usr  = usuariosDao.buscarPorObjeto(usr).get(0);
			
			if(usr!=null){
				Ingresos ing= new Ingresos();
				ing.setId(new IngresosId());
				ing.getId().setCatIngresosId(1);
				ing.getId().setCatIngresosUsuariosId(usr.getId());
				ing.getId().setId(ingresosDao.obtenerUltimoID(usr.getId(), 1));
				
				ing.setConcepto("Recarga de Saldo");
				ing.setFecha(new Date());
				ing.setCantidad(code.getMonto());
				
				
				if(codigosDao.cargarSaldo(code, ing))
					return "("+code.getMonto()+")";
				else
					return "error en la recarga";
			}else
				logger.info("no se encontro usuario");
			return "no se encontro usuario";
		}else
			logger.info("no se encontro codigo");
		return "no se encontro codigo";
		
		}catch(Exception e){
			e.printStackTrace();
			return e.toString();
		}
		
		
	}
	
	public String sumaPuntos(String user, int puntos, String vendor){
		try{
	
			Usuarios usr= new Usuarios();
			usr.setId(Integer.parseInt(user));
			usr  = usuariosDao.buscarPorObjeto(usr).get(0);
			
			if(usr!=null){
				Ingresos ing= new Ingresos();
				ing.setId(new IngresosId());
				ing.getId().setCatIngresosId(1);
				ing.getId().setCatIngresosUsuariosId(usr.getId());
				ing.getId().setId(ingresosDao.obtenerUltimoID(usr.getId(), 1));
				ing.setContraparte(vendor);
				ing.setConcepto("Recompensa Qritiqr");
				ing.setFecha(new Date());
				ing.setCantidad(new BigDecimal(puntos));
				
				if(ingresosDao.insertarIngreso(ing))
					return "ok";
				else
					return "fail";
				
			}else{
				logger.info("no se encontro usuario");
				return "no se encontro usuario";
			}
		
		
		}catch(Exception e){
			e.printStackTrace();
			return e.toString();
		}
		
		
	}

	public String retirodeFondos(BigDecimal envio, Usuarios usr, String emailAgencia) {
		BigDecimal saldo = this.calculaSaldo(usr);
		try{

		if(envio.compareTo(saldo) == 1){ //No
			return "Saldo Insuficiente";
		}
		else{
			Usuarios agencia = usuariosDao.isUsurio(emailAgencia); 
			Usuarios admin = usuariosDao.isUsurio("ingresos@saldo.mx");
			if(agencia!=null && admin!=null){

				BigDecimal retiro, comision;
				comision = new BigDecimal("5");
				retiro = envio.subtract(comision);
				usr = usuariosDao.buscarPorObjeto(usr).get(0);
			
			if(egresosDao.retiro(retiro, comision, usr, agencia, admin)==0){
				return "("+ retiro.toPlainString() +")";
			}
			else{
				return "Error al retirar fondos";
			}
			
			
			
			}
			else{
				return "La agencia o admin no existen";
			}
		}
		}catch(Exception e){
			logger.error(e.toString());
			return "Error al retirar fondos";
		}
		
		
		
	}

	public Usuarios buscarUsuario(String email) {
		
		Usuarios user = new Usuarios();
		user = usuariosDao.isUsurio(email);
		
		return user;
	}

	public boolean validaUsuario(String codigo) {
		Usuarios user = new Usuarios();
		user.setCodigo(codigo);
		
		List res = super.buscarObjeto(user);
		
		if(res!=null && !res.isEmpty()){
			user = (Usuarios)res.get(0);
			user.setEstatus(1);
			usuariosDao.actualizar(user);
			return true;
		}
		else
			return false;
	}

	public String cargarSaldo(BigDecimal cantidad, String user) {
		try{
			if(cantidad.compareTo(BigDecimal.ZERO)==1){
				Usuarios usr= new Usuarios();
				usr.setEmail(user);
				usr  = usuariosDao.buscarPorObjeto(usr).get(0);
				java.text.SimpleDateFormat formatDate=new java.text.SimpleDateFormat("dd/MM/yyyy");

				if(usr!=null){
					Ingresos ing= new Ingresos();
					ing.setId(new IngresosId());
					ing.getId().setCatIngresosId(1);
					ing.getId().setCatIngresosUsuariosId(usr.getId());
					ing.getId().setId(ingresosDao.obtenerUltimoID(usr.getId(), 1));
					ing.setConcepto("Recarga de Saldo");
					ing.setFecha(new Date());
					ing.setCantidad(cantidad);
					
					ingresosDao.insertar(ing);
					
					String body2 = tamplateHtml.templateReCargo(usr.getNombre() + " " + usr.getApp() + " " +usr.getApm(),
							cantidad.toString(), formatDate.format(new Date()));
						
					
					
					mailService.sendMail("avisos@saldo.mx", usr.getEmail(), "RECARGA DE SALDO ", body2);
					
					
					return "(recarga exitosa)";
					
				}else
					logger.info("no se encontro usuario");
				return "no se encontro usuario";
			}else
				logger.info("no se encontro codigo");
			return "no se encontro codigo";
		}catch(IndexOutOfBoundsException er){
			return "Usuario incorrecto, favor de verificar.";
		}
			
			catch(Exception e){
				e.printStackTrace();
				return "Error al recargar saldo";
			}
	}

	
	public List<Movimientos> obtenerMovimientos(Usuarios user, int min, int max){
		List<Map> lst = ingresosDao.obtenerTodo(user, min, max);
		java.text.SimpleDateFormat formatDate=new java.text.SimpleDateFormat("dd/MM/yy hh:mm:ss");
		List<Movimientos> res = new ArrayList();
		Movimientos mov;
		for(Map m : lst){
			BigInteger tipo= (BigInteger)m.get("tipo");
			Date fecha = (Date) m.get("fecha");

			mov =  new Movimientos();
			mov.setFolio((BigInteger)m.get("folio"));
			mov.setConcepto((String)m.get("concepto"));
			mov.setCantidad((BigDecimal)m.get("cantidad"));
			mov.setFecha(formatDate.format(fecha));
			mov.setContraparte(m.get("contraparte")!=null?m.get("contraparte")+"":"");
			
			if(tipo.compareTo(BigInteger.ONE)==0)
				mov.setTipo("Ingreso");
			else
				mov.setTipo("Egreso");
			
			res.add(mov);
			
		}
		
		return res;
		
	}

	public List<Pais> obtenerPaises() {
		List<Pais> lst = localidadesDao.obtenerPaises();
		
		
		return lst;
		
	}
	
	public List<Estados> obtenerEstados(String pais) {
		logger.info("pasi " + pais);
		List<Estados> lst = localidadesDao.obtenerEstados(pais);
		return lst;
		
	}

	
	public String realizarPago(String paymentAction, String amount,
			String cardType, String acct, String expdate, String cvv2,
			String firstName, String lastName, String street, String city,
			String state, String zip, String countryCode) {
		DoDirectPayment pago = new DoDirectPayment();
        return pago.DoDirectPaymentCode(paymentAction, amount, cardType, acct, expdate, cvv2, firstName, lastName, street, city, state, zip, countryCode);
	}
	
}
