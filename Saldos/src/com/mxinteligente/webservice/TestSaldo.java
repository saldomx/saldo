package com.mxinteligente.webservice;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.zkforge.json.simple.JSONObject;
import org.zkoss.json.JSONArray;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import com.mxinteligente.config.SpringApplicationContext;
import com.mxinteligente.model.entidades.Categresos;
import com.mxinteligente.model.entidades.CategresosId;
import com.mxinteligente.model.entidades.Egresos;
import com.mxinteligente.model.entidades.EgresosId;
import com.mxinteligente.model.entidades.Movimientos;
import com.mxinteligente.model.entidades.Tokens;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;
import com.mxinteligente.model.negocio.SesionesServI;
import com.sun.jersey.api.spring.Autowire;
import static java.lang.Math.round;
import static java.lang.Math.random;
import static java.lang.Math.pow;
import static java.lang.Math.abs;
import static java.lang.Math.min;

import static org.apache.commons.lang.StringUtils.leftPad;

@Path("/tsaldos/")
@Produces( { "text/plain", "application/xml" })
@Consumes( { "application/json", "application/xml" })
@Scope("singleton")
@Autowire
public class TestSaldo {
	
	
	@Autowired(required=true)
	private GeneralServI generalServ;
	
	@Autowired(required = true)
	private PasswordEncoder passwordEncoder;
	
	@Autowired(required = true)
	SesionesServI sesionesServ;

	@GET
	@Path("obtenerSaldo/{user}/{token}") 
	@Produces("text/plain")
	public String getUpdatedMagasins(@PathParam("user") String id, @PathParam("token") String token) {
		Usuarios usr = null;
		try{
		Tokens tok = new Tokens();
		tok.setToken(token);
		tok.setUsuarioId(Integer.parseInt(id));
		tok  = (Tokens)generalServ.buscarObjeto(tok).get(0);
		if(tok!=null){
			usr = new Usuarios();
			usr.setId(Integer.parseInt(id));
			usr.setSaldo(generalServ.calculaSaldo(usr));
		}else{
			usr= new Usuarios();
			usr.setId(-1);
			
		}
		return usr.getSaldo().toPlainString();
		}catch(Exception e){
			e.printStackTrace();
			usr= new Usuarios();
			usr.setId(-1);
			return usr.getId().toString();
		}
	}
	
	@GET
	@Path("transferir/{user}/{token}/{mail}/{concepto}/{cantidad}/{pin}") 
	@Produces("text/plain")
	public String Transferencia(@PathParam("user") String id,
			@PathParam("token") String token,
			@PathParam("mail") String mail,
			@PathParam("concepto") String concepto,
			@PathParam("cantidad") String cantidad,
			@PathParam("pin") String pin
			) {
		Usuarios usr=null;

		try{
		Tokens tok = new Tokens();
		tok.setToken(token);
		tok.setUsuarioId(Integer.parseInt(id));
		tok  = (Tokens)generalServ.buscarObjeto(tok).get(0);
		if(tok!=null){
			usr = new Usuarios();
			usr.setId(Integer.parseInt(id));
		
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
			eg.setConcepto(concepto);
			eg.setCantidad(new BigDecimal(cantidad));
			eg.setFecha(new Date());
			int status = generalServ.realizarTransferencia(eg, usr, mail,new Integer(pin));
			usr.setStatusTrasnferencia(status);
		}else{
			usr= new Usuarios();
			usr.setId(-1);
		}
		return usr.getId().toString();
		}catch(Exception e){
			e.printStackTrace();
			usr= new Usuarios();
			usr.setId(-1);
			return usr.getId().toString();
		}
	}
	
	@GET
	@Path("hello/{name}")
	@Produces("text/plain")
	public String getMagasinById(@PathParam("name") String name) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("Hello ").append(name);

		return buffer.toString();
	}
	
	@GET
	@Path("login/{user}/{password}") 
	@Produces("text/plain")
	//@Produces("text/plain")
	public String Loggin(@PathParam("user") String name, @PathParam("password") String password ) throws HttpException, IOException {
		Usuarios userlogin = new Usuarios();
		try{
		
		passwordEncoder = (PasswordEncoder) SpringApplicationContext
			.getBean("passwordEncoder");
		 String paswd = passwordEncoder.encodePassword(password, null);
			
		userlogin.setEmail(name);
		userlogin.setPassword(paswd);
		userlogin = (Usuarios)generalServ.buscarObjeto(userlogin).get(0);
		
		 HttpClient client = new HttpClient();
		 PostMethod login = new PostMethod("http://localhost:8080/Saldos/" + "j_spring_security_check");
		 login.addParameter("j_username", name);
		 
		 
		 
		 login.addParameter("j_password", paswd);
		 client.executeMethod(login);
		 login.releaseConnection();
		 
		HttpState s = client.getState();
		Cookie[] logoncookies = s.getCookies();
		
		
		String sessionID = logoncookies[0].toString().substring(11);
		String token = new RandomAlphaNum().gen(35);
		
		
		Tokens tokenId = new Tokens();
		tokenId.setSessionID(sessionID);
		tokenId.setToken(token);
		tokenId.setUsuarioId(userlogin.getId());
		generalServ.insertar(tokenId);
		
		userlogin = new Usuarios();
		userlogin.setId(tokenId.getUsuarioId());
		userlogin.setToken(token);
		
        return "tok=" + userlogin.getToken()+ " id = " + userlogin.getId() ;
		}catch(Exception e){
			e.printStackTrace();
			userlogin.setId(-1);
			return userlogin.getId().toString();
		}
        
	}
	
	@GET
	@Path("logout/{user}/{token}") 
	@Produces("text/plain")
	//@Produces("text/plain")
	public String Logout(@PathParam("user") String id, @PathParam("token") String token ) throws HttpException, IOException {
		Usuarios userlogin = new Usuarios();
		Usuarios usr = null;
		try{
		Tokens tok = new Tokens();
		tok.setToken(token);
		tok.setUsuarioId(Integer.parseInt(id));
		tok  = (Tokens)generalServ.buscarObjeto(tok).get(0);
		if(tok!=null){
			sesionesServ.logout(tok);
			usr= new Usuarios();
			usr.setId(1);
			
		}else{
			usr= new Usuarios();
			usr.setId(-1);
			
		}
		return usr.getId().toString();
		}catch(Exception e){
			e.printStackTrace();
			usr= new Usuarios();
			usr.setId(-1);
			return usr.getId().toString();
		}
        
	}
	
	@GET
	@Path("cargarSaldo/{id}/{token}/{codigo}")
	@Produces("text/plain")
	public String cargarSaldo(@PathParam("id") String id,
							@PathParam("token") String token, 
							@PathParam("codigo") String codigo){
		Usuarios usr = null;
		String res="";
		try{
		Tokens tok = new Tokens();
		tok.setToken(token);
		tok.setUsuarioId(Integer.parseInt(id));
		tok  = (Tokens)generalServ.buscarObjeto(tok).get(0);
		
		
		if(tok!=null){
			
		 res = generalServ.cargarSaldo(codigo, id);
			if(res.equals("recarga exitosa")){
				usr = new Usuarios();
				usr.setId(1);	
			}
			else{
				usr= new Usuarios();
				usr.setEmail(res);
				usr.setId(-2);
			}
			
			
		}else{
			usr= new Usuarios();
			usr.setEmail(res);
			usr.setId(-3);
		}
		return usr.getId().toString();
		}catch(Exception e){
			e.printStackTrace();
			usr= new Usuarios();
			usr.setEmail(res);
			usr.setId(-1);
			return usr.getId().toString();
		}
		
		
	}
	

	@GET
	@Path("retiro/{user}/{token}/{mail}/{cantidad}") 
	@Produces("text/plain")
	public String SacarFondos(@PathParam("user") String id,
			@PathParam("token") String token,
			@PathParam("mail") String mail,
			@PathParam("cantidad") String cantidad
			) {
		Usuarios usr=null;

		try{
		Tokens tok = new Tokens();
		tok.setToken(token);
		tok.setUsuarioId(Integer.parseInt(id));
		tok  = (Tokens)generalServ.buscarObjeto(tok).get(0);
		if(tok!=null){
			System.out.println("cantidad" + cantidad);
			BigDecimal envio;
			envio = new BigDecimal(cantidad);
		
			usr = new Usuarios();
			usr.setId(Integer.parseInt(id));
		
			String status = generalServ.retirodeFondos(envio, usr, mail);
			usr.setMsg(status);
			
		}else{
			usr= new Usuarios();
			usr.setMsg("Token Inv‡lido");
			usr.setId(-1);
		}
		return usr.getMsg();
		}catch(Exception e){
			e.printStackTrace();
			usr= new Usuarios();
			usr.setMsg("Error al recuperar token");
			usr.setId(-1);
			return usr.getMsg();
		}
	}
	
	
	 @GET
	 @Path("obtenerMovimientos/{user}/{token}/{min}/{max}") 
	 //@Produces({MediaType.APPLICATION_JSON})
	 @Produces("application/json")
	 public  Usuarios obtenerListaTransacciones(@PathParam("user") String id, 
			 @PathParam("token") String token,
			 @PathParam("min") String min,
			 @PathParam("max") String max){
			JSONArray list = new JSONArray();
			Usuarios usr = null;
		 List<Movimientos>  lista = new ArrayList();
		 
			try{
			Tokens tok = new Tokens();
			tok.setToken(token);
			tok.setUsuarioId(Integer.parseInt(id));
			tok  = (Tokens)generalServ.buscarObjeto(tok).get(0);
			if(tok!=null){
				usr = new Usuarios();
				usr.setId(Integer.parseInt(id));
				usr =(Usuarios) generalServ.buscarObjeto(usr).get(0);
				lista = generalServ.obtenerMovimientos(usr, Integer.parseInt(min), Integer.parseInt(max));
				usr.setMovimientos(lista);
				
			}else{
				usr= new Usuarios();
				usr.setId(-1);
				
			}
			return usr;
			}catch(Exception e){
				e.printStackTrace();
				usr= new Usuarios();
				usr.setId(-1);
				return usr;
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
