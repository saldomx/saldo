package com.mxinteligente.tests;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static org.apache.commons.lang.StringUtils.leftPad;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.testng.annotations.Test;

import com.mxinteligente.config.SpringApplicationContext;
import com.mxinteligente.model.dao.CatingresosDaoI;
import com.mxinteligente.model.dao.EgresosDaoI;
import com.mxinteligente.model.dao.IngresosDaoI;
import com.mxinteligente.model.entidades.Catingresos;
import com.mxinteligente.model.entidades.CatingresosId;
import com.mxinteligente.model.entidades.Estados;
import com.mxinteligente.model.entidades.Ingresos;
import com.mxinteligente.model.entidades.IngresosId;
import com.mxinteligente.model.entidades.Movimientos;
import com.mxinteligente.model.entidades.Pais;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.AdminServI;
import com.mxinteligente.model.negocio.GeneralServI;

public class TestIngresosDao {

	protected IngresosDaoI ingresosDao;
	protected CatingresosDaoI catingresosDao;
	protected EgresosDaoI egresosDao;
	private PasswordEncoder passwordEncoder;
	private AdminServI adminServ;
	GeneralServI generalServ;
	@Test(enabled=true)
	public void setConfiguration() {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"/WebContent/WEB-INF/applicationContext.xml");
		
		
		generalServ = (GeneralServI) ctx.getBean("generalServ");
		ingresosDao = (IngresosDaoI) ctx.getBean("ingresosDao");
		//catingresosDao = (CatingresosDaoI) ctx.getBean("catingresosDao");

		//egresosDao = (EgresosDaoI) ctx.getBean("egresosDao");
		
	   passwordEncoder = (PasswordEncoder) ctx.getBean("passwordEncode");
		
		adminServ = (AdminServI) ctx.getBean("adminServ");

	}
	
	@Test(enabled = false, dependsOnMethods = { "setConfiguration" })
	public void obtenerCategorias() {
	
		Usuarios user = new Usuarios();
		user.setId(1);
		
		
		
		List<Map> lst = catingresosDao.obtenerCategorias(user);
		
		for(Map p : lst){
			System.out.println("Nombre " + p.get("nombre"));
			System.out.println("ID " + p.get("id"));
			System.out.println("USER ID " + p.get("userid"));


		}
		
	}
	
	@Test(enabled = false, dependsOnMethods = { "setConfiguration" })
	public void obtenerTodo() {
	
		Usuarios user = new Usuarios();
		user.setId(1);
		
		
		
		List<Movimientos> lst = generalServ.obtenerMovimientos(user, 0, 20);
		
		for(Movimientos p : lst){
			System.out.println("tipo " + p.getTipo());
			System.out.println("id " + p.getConcepto());
			System.out.println("fecha " + p.getCantidad());
//			BigDecimal d = (BigDecimal) p.get("cantidad");
//			System.out.println("cantidad " + d.toString());
//			System.out.println("concepto " + p.get("concepto"));

			

		}
		
	}
	
	@Test(enabled = false, dependsOnMethods = { "setConfiguration" })
	public void obtenerTodosIngresosPorUsuario() {
	
		Usuarios user = new Usuarios();
		user.setId(1);
		
//		Ingresos ing = new Ingresos();
//		ing.setId(new IngresosId());
//		ing.getId().setCatIngresosUsuariosId(user.getId());
//		ing.getId().setCatIngresosId(1);
//		ing.setCantidad(new BigDecimal(12312));
//		ing.setConcepto("AMD ATHLON X2");
//		ing.setFecha(new Date());
//		
//		ingresosDao.insertarIngreso(ing);
//		
//		Catingresos cating = new Catingresos();
//		cating.setId(new CatingresosId());
//		cating.getId().setId(2);
//		cating.getId().setUsuariosId(1);
		
		
		List<Map> lst = ingresosDao.obtenerTodosIngresos(user, 1, 20);
		for(Map p : lst){
			p.put("tipo", "Ingreso");
		}
		
		
		for(Map p : lst){
			System.out.println("p concepto = " + p.get("concepto"));
			System.out.println("p cantidad = " + p.get("cantidad"));
			System.out.println("p fecha = " + p.get("fecha"));
			System.out.println("p cat = " + p.get("categoria"));
			System.out.println("tipo  = " + p.get("tipo"));


		}
		
	}
	
	@Test(enabled = true, dependsOnMethods = { "setConfiguration" })
	public void ObtenerSuma() {
		
		String encodepas = passwordEncoder.encodePassword("transfer", null);
		
		String token = new RandomAlphaNum().gen(15);
		System.out.println("encodepas " + encodepas);
		}
	
	@Test(enabled = false)
	public void EncodeSHA() {
		
		//String encodepas = passwordEncoder.encodePassword("mxinteligente2011", null);
		
		String token = new RandomAlphaNum().gen(15);
		System.out.println("gen " + token.toUpperCase());
		}
	
	
	
	@Test(enabled = false, dependsOnMethods = { "setConfiguration" })
	public void generarCodigos() {
	
		adminServ.generarCodigos(new BigDecimal("30.00"), 100);
		
	}
	
	
	@Test(enabled = false, dependsOnMethods = { "setConfiguration" })
	public void obtenerPaises() {
	
	List<Estados> lst = generalServ.obtenerEstados("MX");
	for(Estados p:lst){
		System.out.println(p.getClave());
		System.out.println(p.getNombre());
	
	}
	
	}
	
	
	
	
	
	
	
	class RandomAlphaNum {
		  public String gen(int length) {
		    StringBuffer sb = new StringBuffer();
		    for (int i = length; i > 0; i -= 12) {
		      int n = min(12, abs(i));
		      sb.append(leftPad(Long.toString(round(random() * pow(36, n)), 36), n, '0'));
		    }
		    return sb.toString();
		  }
		}
	
	
}
