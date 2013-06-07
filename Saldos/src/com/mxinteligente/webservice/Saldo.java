package com.mxinteligente.webservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.zkoss.json.JSONArray;
import org.zkoss.zul.Messagebox;
import org.apache.commons.httpclient.protocol.Protocol;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.Consumes;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.ConnectTimeoutException;

import java.net.InetAddress;
import java.net.Socket;
import com.mxinteligente.config.SpringApplicationContext;

import com.mxinteligente.infra.PathContext;
import com.mxinteligente.infra.model.nego.MailService;
import com.mxinteligente.infra.model.nego.TemplateHtml;
import com.mxinteligente.model.entidades.Categresos;
import com.mxinteligente.model.entidades.CategresosId;
import com.mxinteligente.model.entidades.Catingresos;
import com.mxinteligente.model.entidades.CatingresosId;
import com.mxinteligente.model.entidades.Comprador;
import com.mxinteligente.model.entidades.Egresos;
import com.mxinteligente.model.entidades.EgresosId;
import com.mxinteligente.model.entidades.Movimientos;
import com.mxinteligente.model.entidades.OrdenesCompra;
import com.mxinteligente.model.entidades.Producto;
import com.mxinteligente.model.entidades.Tokens;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.entidades.UsuariosPuntos;
import com.mxinteligente.model.entidades.UsuariosPuntosId;
import com.mxinteligente.model.negocio.GeneralServI;
import com.mxinteligente.model.negocio.SesionesServI;
import com.mxinteligente.model.negocio.VentasServI;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.spring.Autowire;

import static java.lang.Math.round;
import static java.lang.Math.random;
import static java.lang.Math.pow;
import static java.lang.Math.abs;
import static java.lang.Math.min;

import static org.apache.commons.lang.StringUtils.leftPad;

@Path("/saldos/")
@Produces({ "application/json", "application/xml" })
@Consumes({ "application/json", "application/xml" })
@Scope("singleton")
@Autowire
public class Saldo {

	@Autowired(required = true)
	private GeneralServI generalServ;

	@Autowired(required = true)
	private PasswordEncoder passwordEncoder;

	@Autowired(required = true)
	SesionesServI sesionesServ;

	@Autowired(required = true)
	VentasServI ventasServ;

	@Autowired(required = true)
	MailService mailService;

	private static int PUNTOS=5;
	@Autowired
	private TemplateHtml tamplateHtml;

	@GET
	@Path("registrar/{nombre}/{app}/{apm}/{mail}/{fechaNac}/{domicilio}/{clabe}/{passwd}/{nip}")
	@Produces("text/plain")
	public String registrarUsuario(@PathParam("nombre") String nombre,
			@PathParam("app") String app, @PathParam("apm") String apm,
			@PathParam("mail") String mail,
			@PathParam("fechaNac") String fechaNac,
			@PathParam("domicilio") String domicilio,
			@PathParam("clabe") String clabe,
			@PathParam("passwd") String passwd, @PathParam("nip") String nip) {
		Usuarios user;
		try {
			if (nombre != null && app != null && apm != null && mail != null
					&& fechaNac != null) {
				try {
					if (generalServ.buscarUsuario(mail) == null) {
						SimpleDateFormat formatoDeFecha = new SimpleDateFormat(
								"dd/MM/yyyy");

						String codigo = new RandomAlphaNum().gen(25);
						user = new Usuarios();
						user.setApm(apm);
						user.setApp(app);
						user.setEmail(mail);
						user.setEstatus(0);
						user.setNombre(nombre);
						user.setFechnac(formatoDeFecha.parse(fechaNac));
						user.setDomicilio(domicilio);
						user.setClabe(clabe);
						user.setCodigo(codigo);
						passwordEncoder = (PasswordEncoder) SpringApplicationContext
								.getBean("passwordEncoder");
						String paswd = passwordEncoder.encodePassword(passwd,
								null);
						user.setPassword(paswd);
						user.setNip(new Integer(nip));
						user.setRol("ROLE_USER");

						generalServ.insertar(user);

						user = (Usuarios) generalServ.buscarUsuario(mail);

						Categresos egresos = new Categresos();
						egresos.setId(new CategresosId());

						egresos.getId().setUsuariosId(user.getId());
						egresos.getId().setId(1);
						egresos.setNombre("Transferencia");

						generalServ.insertar(egresos);

						Catingresos ingresos = new Catingresos();
						ingresos.setId(new CatingresosId());
						ingresos.getId().setId(1);
						ingresos.getId().setUsuariosId(user.getId());
						ingresos.setNombre("Transferencia");

						generalServ.insertar(ingresos);

						String body1 = tamplateHtml
								.templateVerificacion("https://www.saldo.mx/Saldos/Autentificar/validacion.zul?codigo="
										+ codigo);
						mailService.sendMail("avisos@saldo.mx",
								user.getEmail(), "EMAIL CONFIRMACI�N ", body1);
						return "1";
					} else {
						return "-1";
					}
				} catch (Exception e) {
					e.printStackTrace();
					return "0";

				}
			} else {
				return "0";
			}

		}

		catch (Exception e) {
			e.printStackTrace();

			return "0";
		}
	}
	
	@GET
	@Path("registrarUsuario/{nombre}/{mail}/{fechaNac}/{passwd}/{sexo}/{nip}/{fb}")
	@Produces("text/plain")
	public String registrarUsuarioPuntos(@PathParam("nombre") String nombre,
			@PathParam("mail") String mail,
			@PathParam("fechaNac") String fechaNac,
			@PathParam("passwd") String passwd,
			@PathParam("sexo") String sexo,
			@PathParam("nip") String nip,
			@PathParam("fb") String fb
			) {
		Usuarios user;
		try {
			if (nombre != null  && mail != null
					&& fechaNac != null) {
				try {
					if (generalServ.buscarUsuario(mail) == null) {
						SimpleDateFormat formatoDeFecha = new SimpleDateFormat(
								"dd-MM-yyyy");

						String codigo = new RandomAlphaNum().gen(25);
						user = new Usuarios();
						user.setEmail(mail);
						user.setEstatus(1);
						user.setNombre(nombre);
						user.setFechnac(formatoDeFecha.parse(fechaNac));
						user.setCodigo(codigo);
						passwordEncoder = (PasswordEncoder) SpringApplicationContext
								.getBean("passwordEncoder");
						String paswd = passwordEncoder.encodePassword(passwd,
								null);
						user.setPassword(paswd);
						user.setRol("ROLE_USER");
						user.setSexo(sexo);
						user.setNip(new Integer(nip));
						user.setIsFB(fb);
						generalServ.insertar(user);

						user = (Usuarios) generalServ.buscarUsuario(mail);

						Categresos egresos = new Categresos();
						egresos.setId(new CategresosId());

						egresos.getId().setUsuariosId(user.getId());
						egresos.getId().setId(1);
						egresos.setNombre("Transferencia");

						generalServ.insertar(egresos);

						Catingresos ingresos = new Catingresos();
						ingresos.setId(new CatingresosId());
						ingresos.getId().setId(1);
						ingresos.getId().setUsuariosId(user.getId());
						ingresos.setNombre("Transferencia");

						generalServ.insertar(ingresos);

//						String body1 = tamplateHtml
//								.templateVerificacion("https://www.saldo.mx/Saldos/Autentificar/validacion.zul?codigo="
//										+ codigo);
//						mailService.sendMail("avisos@saldo.mx",
//								user.getEmail(), "EMAIL CONFIRMACI�N ", body1);
						return "1";
					} else {
						return "-1";
					}
				} catch (Exception e) {
					e.printStackTrace();
					return "0";

				}
			} else {
				return "0";
			}

		}

		catch (Exception e) {
			e.printStackTrace();

			return "0";
		}
	}
	

	@GET
	@Path("registrarComprador/{nombre}/{mail}/{passwd}")
	@Produces("text/plain")
	public String registrarComprador(@PathParam("nombre") String nombre,
			@PathParam("mail") String mail,
			@PathParam("passwd") String passwd) {
		Comprador comprador;
		try {
			if (nombre != null  && mail != null	) {
				try {
					comprador = new Comprador();
					comprador.setEmail(mail);
					List lstComprador = generalServ.buscarObjeto(comprador);
					
					if (lstComprador == null || lstComprador.isEmpty() || lstComprador.size()==0) {
						SimpleDateFormat formatoDeFecha = new SimpleDateFormat(
								"dd/MM/yyyy");

						String codigo = new RandomAlphaNum().gen(25);
						comprador = new Comprador();

						comprador.setEmail(mail);

						comprador.setNombre(nombre);

						passwordEncoder = (PasswordEncoder) SpringApplicationContext
								.getBean("passwordEncoder");
						String paswd = passwordEncoder.encodePassword(passwd,
								null);
						comprador.setPassword(paswd);

						generalServ.insertar(comprador);

						return "1";
					} else {
						return "-1";
					}
				} catch (Exception e) {
					e.printStackTrace();
					return "0";

				}
			} else {
				return "0";
			}

		}

		catch (Exception e) {
			e.printStackTrace();

			return "0";
		}
	}

	@GET
	@Path("obtenerSaldo/{user}/{token}")
	@Produces("application/json")
	public Usuarios getUpdatedMagasins(@PathParam("user") String id,
			@PathParam("token") String token) {
		Usuarios usr = null;
		try {
			Tokens tok = new Tokens();
			tok.setToken(token);
			tok.setUsuarioId(Integer.parseInt(id));
			List lstokens = generalServ.buscarObjeto(tok);

			if (lstokens != null && !lstokens.isEmpty()) {
				tok = (Tokens) lstokens.get(0);
				usr = new Usuarios();
				usr.setId(Integer.parseInt(id));
				usr.setSaldo(generalServ.calculaSaldo(usr));
				usr.setId(0);
			} else {
				usr = new Usuarios();
				usr.setMsg("Session terminada");
				usr.setId(-2);
			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			return usr;
		}
	}
	
	@GET
	@Path("getSaldo/{email}/{passwd}")
	@Produces("application/json")
	public Usuarios obtenerSaldoUsrPaswd(@PathParam("email") String email,
			@PathParam("passwd") String passwd) {
		Usuarios usr = null;
		try {
			Usuarios userRegister = new Usuarios();
			userRegister.setEmail(email);
			
			passwordEncoder = (PasswordEncoder) SpringApplicationContext
					.getBean("passwordEncoder");
			String paswd = passwordEncoder.encodePassword(passwd,null);
			
			userRegister.setPassword(paswd);
			List lstokens = generalServ.buscarObjeto(userRegister);

			if (lstokens != null && !lstokens.isEmpty()) {
				usr = (Usuarios) lstokens.get(0);
				usr.setSaldo(generalServ.calculaSaldo(usr));
				usr.setId(0);
				usr.setPassword("");
			} else {
				usr = new Usuarios();
				usr.setMsg("Usuario no registrado");
				usr.setId(-2);
			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Usuario no registrado");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			return usr;
		}
	}

	@GET
	@Path("transferir/{user}/{token}/{mail}/{concepto}/{cantidad}/{pin}")
	@Produces("application/json")
	public Usuarios Transferencia(@PathParam("user") String id,
			@PathParam("token") String token, @PathParam("mail") String mail,
			@PathParam("concepto") String concepto,
			@PathParam("cantidad") String cantidad, @PathParam("pin") String pin) {
		Usuarios usr = null;

		try {
			Tokens tok = new Tokens();
			tok.setToken(token);
			tok.setUsuarioId(Integer.parseInt(id));
			tok = (Tokens) generalServ.buscarObjeto(tok).get(0);
			if (tok != null) {
				usr = new Usuarios();
				usr.setId(Integer.parseInt(id));

				Egresos eg = new Egresos();
				Categresos catEg = new Categresos();
				catEg.setId(new CategresosId());
				catEg.getId().setId(1); // ID de TRANSFERENCIAS
				catEg.getId().setUsuariosId(usr.getId());
				catEg.setUsuarios(usr);

				eg.setId(new EgresosId());
				eg.getId().setCatEgresosId(1);
				eg.getId().setCatEgresosUsuariosId(usr.getId());
				eg.setCategoria(catEg);
				eg.setConcepto(concepto);
				eg.setCantidad(new BigDecimal(cantidad));
				eg.setFecha(new Date());
				int status = generalServ.realizarTransferencia(eg, usr, mail,
						new Integer(pin));

				if (status == 1) {
					usr.setMsg("Saldo Insuficiente");
					usr.setId(-1);
				} else if (status == 2) {
					usr.setMsg("Credencials no válidas");
					usr.setId(-1);
				} else if (status == 0) {
					usr.setMsg("Transferencia satisfactoria");
					usr.setId(0);
				} else if (status == -1) {
					usr.setMsg("Error al transferir");
					usr.setId(-1);
				} else if (status == 3) {
					usr.setMsg("Pin invalido");
					usr.setId(-1);
				}

				usr.setStatusTrasnferencia(status);

			} else {
				usr = new Usuarios();
				usr.setMsg("Session terminada");
				usr.setId(-2);
			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			usr.setMsg(e.toString());
			return usr;
		}
	}
	
	
	@GET
	@Path("transferirSaldo/{userMail}/{passwd}/{mail}/{concepto}/{cantidad}/{pin}")
	@Produces("application/json")
	public Usuarios TransferenciaUsrPasswd(@PathParam("userMail") String userMail,
			@PathParam("passwd") String passwd, @PathParam("mail") String mail,
			@PathParam("concepto") String concepto,
			@PathParam("cantidad") String cantidad, @PathParam("pin") String pin) {
		Usuarios usr = null;

		try {
			Usuarios userRegister = new Usuarios();
			userRegister.setEmail(userMail);
			
			passwordEncoder = (PasswordEncoder) SpringApplicationContext
					.getBean("passwordEncoder");
			String paswd = passwordEncoder.encodePassword(passwd,null);
			
			userRegister.setPassword(paswd);
			
			userRegister= (Usuarios) generalServ.buscarObjeto(userRegister).get(0);
			
			
			if (userRegister != null) {
				usr = userRegister;
				Egresos eg = new Egresos();
				Categresos catEg = new Categresos();
				catEg.setId(new CategresosId());
				catEg.getId().setId(1); // ID de TRANSFERENCIAS
				catEg.getId().setUsuariosId(usr.getId());
				catEg.setUsuarios(usr);

				eg.setId(new EgresosId());
				eg.getId().setCatEgresosId(1);
				eg.getId().setCatEgresosUsuariosId(usr.getId());
				eg.setCategoria(catEg);
				eg.setConcepto(concepto);
				eg.setCantidad(new BigDecimal(cantidad));
				eg.setFecha(new Date());
				int status = generalServ.realizarTransferencia(eg, usr, mail,
						new Integer(pin));

				if (status == 1) {
					usr.setMsg("Saldo Insuficiente");
					usr.setId(-1);
				} else if (status == 2) {
					usr.setMsg("Credencials no válidas");
					usr.setId(-1);
				} else if (status == 0) {
					usr.setMsg("Transferencia satisfactoria");
					usr.setId(0);
				} else if (status == -1) {
					usr.setMsg("Error al transferir");
					usr.setId(-1);
				} else if (status == 3) {
					usr.setMsg("Pin invalido");
					usr.setId(-1);
				}

				usr.setStatusTrasnferencia(status);

			} else {
				usr = new Usuarios();
				usr.setMsg("Session terminada");
				usr.setId(-2);
			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			usr.setMsg(e.toString());
			return usr;
		}
	}

	@GET
	@Path("retiro/{user}/{token}/{mail}/{cantidad}")
	@Produces("application/json")
	public Usuarios SacarFondos(@PathParam("user") String id,
			@PathParam("token") String token, @PathParam("mail") String mail,
			@PathParam("cantidad") String cantidad) {
		Usuarios usr = null;

		try {
			Tokens tok = new Tokens();
			tok.setToken(token);
			tok.setUsuarioId(Integer.parseInt(id));
			tok = (Tokens) generalServ.buscarObjeto(tok).get(0);
			if (tok != null) {
				BigDecimal envio;
				envio = new BigDecimal(cantidad);

				usr = new Usuarios();
				usr.setId(Integer.parseInt(id));

				String status = generalServ.retirodeFondos(envio, usr, mail);
				usr.setMsg(status);

				if (status.startsWith("("))
					usr.setId(0);
				else
					usr.setId(-1);

			} else {
				usr = new Usuarios();
				usr.setMsg("Session terminada");
				usr.setId(-2);
			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setMsg("Error al recuperar token");
			usr.setId(-1);
			return usr;
		}
	}

	@GET
	@Path("hello/{name}")
	@Produces("text/plain")
	public String getMagasinById(@PathParam("name") String name) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("Hello ").append(name);
		System.out.println("HELLO");
		return buffer.toString();
	}

	@GET
	@Path("login/{user}/{password}")
	@Produces("application/json")
	// @Produces("text/plain")
	public Usuarios Loggin(@PathParam("user") String name,
			@PathParam("password") String password) throws HttpException,
			IOException {
		Usuarios userlogin = new Usuarios();
		try {

			passwordEncoder = (PasswordEncoder) SpringApplicationContext
					.getBean("passwordEncoder");
			String paswd = passwordEncoder.encodePassword(password, null);

			userlogin.setEmail(name);
			userlogin.setPassword(paswd);
			userlogin = (Usuarios) generalServ.buscarObjeto(userlogin).get(0);

			HttpClient client = new HttpClient();
			PostMethod login = new PostMethod("https://www.saldo.mx/Saldos/"
					+ "j_spring_security_check"); /* ssl */
			// PostMethod login = new
			// PostMethod("http://50.57.140.59:8080/Saldos/" +
			// "j_spring_security_check");
			// PostMethod login = new
			// PostMethod("http://10.21.192.5:8080/Saldos/" +
			// "j_spring_security_check");
			login.addParameter("j_username", name);
			login.addParameter("j_password", paswd);

			ProtocolSocketFactory protocolSocketFactory = new MyProtocolSocketFactory();
			Protocol myProtocol = new Protocol("https", protocolSocketFactory,
					443);
			Protocol.registerProtocol("https", myProtocol);
			/* ssl */

			client.executeMethod(login);
			login.releaseConnection();

			HttpState s = client.getState();
			Cookie[] logoncookies = s.getCookies();
			String sessionCookie = null;

			for (Cookie cookie : logoncookies) {
				System.out.println("cookie: " + cookie.getName() + "="
						+ cookie.getValue() + " (" + cookie.getPath() + ")");
				if ("JSESSIONID".equals(cookie.getName())
						&& "/eid-idp".equals(cookie.getPath())) {
					sessionCookie = cookie.getValue();
				}
			}

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

			return userlogin;
		} catch (IndexOutOfBoundsException l) {
			userlogin = new Usuarios();
			userlogin.setMsg("Session terminada");
			userlogin.setId(-2);
			return userlogin;
		} catch (Exception e) {
			e.printStackTrace();
			userlogin = new Usuarios();
			userlogin.setToken("");
			userlogin.setMsg("Usuario y Contraseña no válidas");
			userlogin.setId(-1);
			return userlogin;
		}

	}

	@GET
	@Path("logout/{user}/{token}")
	@Produces("application/json")
	public Usuarios Logout(@PathParam("user") String id,
			@PathParam("token") String token) throws HttpException, IOException {
		Usuarios userlogin = new Usuarios();
		Usuarios usr = null;
		try {
			Tokens tok = new Tokens();
			tok.setToken(token);
			tok.setUsuarioId(Integer.parseInt(id));
			tok = (Tokens) generalServ.buscarObjeto(tok).get(0);
			if (tok != null) {
				sesionesServ.logout(tok);
				usr = new Usuarios();
				usr.setId(0);

			} else {
				usr = new Usuarios();
				usr.setMsg("Session terminada");
				usr.setId(-2);

			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			return usr;
		}

	}

	@GET
	@Path("cargarSaldo/{id}/{token}/{codigo}")
	@Produces("application/json")
	public Usuarios cargarSaldo(@PathParam("id") String id,
			@PathParam("token") String token, @PathParam("codigo") String codigo) {
		Usuarios usr = null;
		String res = "";
		try {
			Tokens tok = new Tokens();
			tok.setToken(token);
			tok.setUsuarioId(Integer.parseInt(id));
			tok = (Tokens) generalServ.buscarObjeto(tok).get(0);

			if (tok != null) {

				res = generalServ.cargarSaldo(codigo, id);
				if (res.startsWith("(")) {
					usr = new Usuarios();
					usr.setId(0);
					usr.setMsg(res);
				} else {
					usr = new Usuarios();
					usr.setMsg(res);
					usr.setId(-3);
				}

			} else {
				usr = new Usuarios();
				usr.setMsg("Session terminada");
				usr.setId(-2);
			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			res = e.toString();
			usr.setMsg(res);
			usr.setId(-1);
			return usr;
		}
	}

	@GET
	@Path("obtenerMovimientos/{user}/{token}/{min}/{max}")
	// @Produces({MediaType.APPLICATION_JSON})
	@Produces("application/json")
	public Usuarios obtenerListaTransacciones(@PathParam("user") String id,
			@PathParam("token") String token, @PathParam("min") String min,
			@PathParam("max") String max) {
		JSONArray list = new JSONArray();
		Usuarios usr = null;
		List<Movimientos> lista = new ArrayList();

		try {
			Tokens tok = new Tokens();
			tok.setToken(token);
			tok.setUsuarioId(Integer.parseInt(id));
			tok = (Tokens) generalServ.buscarObjeto(tok).get(0);
			if (tok != null) {
				usr = new Usuarios();
				usr.setId(Integer.parseInt(id));
				usr = (Usuarios) generalServ.buscarObjeto(usr).get(0);
				lista = generalServ.obtenerMovimientos(usr,
						Integer.parseInt(min), Integer.parseInt(max));
				usr = new Usuarios();
				usr.setMovimientos(lista);
				usr.setId(0);

			} else {
				usr = new Usuarios();
				usr.setMsg("Session terminada");
				usr.setId(-2);

			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			return usr;
		}

	}
	
	@GET
	@Path("getMovimientos/{mail}/{passwd}/{min}/{max}")
	// @Produces({MediaType.APPLICATION_JSON})
	@Produces("application/json")
	public Usuarios obtenerListaTransaccionesUsrPasswd(@PathParam("mail") String mail,
			@PathParam("passwd") String passwd, @PathParam("min") String min,
			@PathParam("max") String max) {
		JSONArray list = new JSONArray();
		Usuarios usr = null;
		List<Movimientos> lista = new ArrayList();

		try {
			Usuarios userRegister = new Usuarios();
			userRegister.setEmail(mail);
			
			passwordEncoder = (PasswordEncoder) SpringApplicationContext
					.getBean("passwordEncoder");
			String paswd = passwordEncoder.encodePassword(passwd,null);
			
			userRegister.setPassword(paswd);
			
			userRegister= (Usuarios) generalServ.buscarObjeto(userRegister).get(0);
			if (userRegister != null) {
				usr = userRegister;
				
				usr = (Usuarios) generalServ.buscarObjeto(usr).get(0);
				lista = generalServ.obtenerMovimientos(usr,
						Integer.parseInt(min), Integer.parseInt(max));
				usr = new Usuarios();
				usr.setMovimientos(lista);
				usr.setId(0);

			} else {
				usr = new Usuarios();
				usr.setMsg("Session terminada");
				usr.setId(-2);

			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			return usr;
		}

	}
	
	

	@GET
	@Path("obtenerProductos/{userClient}/{emailVend}/{token}/{min}/{max}")
	// @Produces({MediaType.APPLICATION_JSON})
	@Produces("application/json")
	public Usuarios obtenerListaProductos(@PathParam("userClient") String id,
			@PathParam("emailVend") String emailVend,
			@PathParam("token") String token, @PathParam("min") String min,
			@PathParam("max") String max) {
		JSONArray list = new JSONArray();
		Usuarios usr = null;
		List<Producto> lista = new ArrayList();

		try {
			Tokens tok = new Tokens();
			tok.setToken(token);
			tok.setUsuarioId(Integer.parseInt(id));
			tok = (Tokens) generalServ.buscarObjeto(tok).get(0);
			if (tok != null) {
				usr = new Usuarios();
				usr.setEmail(emailVend);
				usr = (Usuarios) generalServ.buscarObjeto(usr).get(0);
				lista = ventasServ.obtenerProductosWS(usr.getId(),
						Integer.parseInt(min), Integer.parseInt(max));
				usr = new Usuarios();
				usr.setProductos(lista);
				usr.setId(0);

			} else {
				usr = new Usuarios();
				usr.setMsg("Session terminada");
				usr.setId(-2);

			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			return usr;
		}

	}
	
	@GET
	@Path("getProductos/{userClientMail}/{emailVend}/{passwd}/{min}/{max}")
	// @Produces({MediaType.APPLICATION_JSON})
	@Produces("application/json")
	public Usuarios obtenerListaProductosUsrPasswd(@PathParam("userClientMail") String mail,
			@PathParam("emailVend") String emailVend,
			@PathParam("passwd") String passwd, @PathParam("min") String min,
			@PathParam("max") String max) {
		JSONArray list = new JSONArray();
		Usuarios usr = null;
		List<Producto> lista = new ArrayList();

		try {
			Usuarios userRegister = new Usuarios();
			userRegister.setEmail(mail);
			
			passwordEncoder = (PasswordEncoder) SpringApplicationContext
					.getBean("passwordEncoder");
			String paswd = passwordEncoder.encodePassword(passwd,null);
			
			userRegister.setPassword(paswd);
			
			userRegister= (Usuarios) generalServ.buscarObjeto(userRegister).get(0);
			if (userRegister != null) {
				usr = userRegister;
				usr.setEmail(emailVend);
				usr = (Usuarios) generalServ.buscarObjeto(usr).get(0);
				lista = ventasServ.obtenerProductosWS(usr.getId(),
						Integer.parseInt(min), Integer.parseInt(max));
				usr = new Usuarios();
				usr.setProductos(lista);
				usr.setId(0);

			} else {
				usr = new Usuarios();
				usr.setMsg("No se encontro usuario");
				usr.setId(-2);

			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("No se encontro usuario");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			return usr;
		}

	}


	@GET
	@Path("comprar/{user}/{token}/{mail}/{idProd}/{pin}")
	@Produces("application/json")
	public Usuarios CompraProducto(@PathParam("user") String id,
			@PathParam("token") String token, @PathParam("mail") String mail,
			@PathParam("idProd") String idProd, @PathParam("pin") String pin) {
		Usuarios usr = null;

		try {
			Tokens tok = new Tokens();
			tok.setToken(token);
			tok.setUsuarioId(Integer.parseInt(id));
			tok = (Tokens) generalServ.buscarObjeto(tok).get(0);
			if (tok != null) {
				usr = new Usuarios();
				usr.setId(Integer.parseInt(id));

				Egresos eg = new Egresos();
				Categresos catEg = new Categresos();
				catEg.setId(new CategresosId());
				catEg.getId().setId(1); // ID de TRANSFERENCIAS
				catEg.getId().setUsuariosId(usr.getId());
				catEg.setUsuarios(usr);

				eg.setId(new EgresosId());
				eg.getId().setCatEgresosId(1);
				eg.getId().setCatEgresosUsuariosId(usr.getId());
				eg.setCategoria(catEg);
				eg.setConcepto("COMPRA");
				eg.setFecha(new Date());
				int status = ventasServ.realizarCompra(eg, usr, mail,
						new Integer(pin), new Integer(idProd));

				if (status == 1) {
					usr.setMsg("Saldo Insuficiente");
					usr.setId(-1);
				} else if (status == 2) {
					usr.setMsg("Credencials no válidas");
					usr.setId(-1);
				} else if (status == 0) {
					usr.setMsg("Transferencia satisfactoria");
					usr.setId(0);
				} else if (status == -1) {
					usr.setMsg("Error al transferir");
					usr.setId(-1);
				} else if (status == 3) {
					usr.setMsg("Pin invalido");
					usr.setId(-1);
				}

				usr.setStatusTrasnferencia(status);

			} else {
				usr = new Usuarios();
				usr.setMsg("Session terminada");
				usr.setId(-2);
			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			usr.setMsg(e.toString());
			return usr;
		}
	}
	
	
	@GET
	@Path("buy/{userMail}/{passwd}/{mail}/{idProd}/{pin}")
	@Produces("application/json")
	public Usuarios CompraProductoUsrPAsswd(@PathParam("userMail") String Usermail,
			@PathParam("passwd") String passwd, @PathParam("mail") String mail,
			@PathParam("idProd") String idProd, @PathParam("pin") String pin) {
		Usuarios usr = null;

		try {
			Usuarios userRegister = new Usuarios();
			userRegister.setEmail(Usermail);
			
			passwordEncoder = (PasswordEncoder) SpringApplicationContext
					.getBean("passwordEncoder");
			String paswd = passwordEncoder.encodePassword(passwd,null);
			
			userRegister.setPassword(paswd);
			
			userRegister= (Usuarios) generalServ.buscarObjeto(userRegister).get(0);
			if (userRegister != null) {
				usr = userRegister;

				Egresos eg = new Egresos();
				Categresos catEg = new Categresos();
				catEg.setId(new CategresosId());
				catEg.getId().setId(1); // ID de TRANSFERENCIAS
				catEg.getId().setUsuariosId(usr.getId());
				catEg.setUsuarios(usr);

				eg.setId(new EgresosId());
				eg.getId().setCatEgresosId(1);
				eg.getId().setCatEgresosUsuariosId(usr.getId());
				eg.setCategoria(catEg);
				eg.setConcepto("COMPRA");
				eg.setFecha(new Date());
				int status = ventasServ.realizarCompra(eg, usr, mail,
						new Integer(pin), new Integer(idProd));

				if (status == 1) {
					usr.setMsg("Saldo Insuficiente");
					usr.setId(-1);
				} else if (status == 2) {
					usr.setMsg("Credencials no válidas");
					usr.setId(-1);
				} else if (status == 0) {
					usr.setMsg("Transferencia satisfactoria");
					usr.setId(0);
				} else if (status == -1) {
					usr.setMsg("Error al transferir");
					usr.setId(-1);
				} else if (status == 3) {
					usr.setMsg("Pin invalido");
					usr.setId(-1);
				}

				usr.setStatusTrasnferencia(status);

			} else {
				usr = new Usuarios();
				usr.setMsg("Session terminada");
				usr.setId(-2);
			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			usr.setMsg(e.toString());
			return usr;
		}
	}
	
	@GET
	@Path("buyTelefonica/{userMail}/{passwd}/{mail}/{idProd}/{pin}/{celular}")
	@Produces("application/json")
	public Usuarios CompraProductoUsrPAsswdTelefonica(@PathParam("userMail") String Usermail,
			@PathParam("passwd") String passwd, @PathParam("mail") String mail,
			@PathParam("idProd") String idProd, @PathParam("pin") String pin,  @PathParam("celular") String celular) {
		Usuarios usr = null;

		try {
			Usuarios userRegister = new Usuarios();
			userRegister.setEmail(Usermail);
			
			passwordEncoder = (PasswordEncoder) SpringApplicationContext
					.getBean("passwordEncoder");
			String paswd = passwordEncoder.encodePassword(passwd,null);
			
			userRegister.setPassword(paswd);
			
			userRegister= (Usuarios) generalServ.buscarObjeto(userRegister).get(0);
			if (userRegister != null) {
				usr = userRegister;

				Egresos eg = new Egresos();
				Categresos catEg = new Categresos();
				catEg.setId(new CategresosId());
				catEg.getId().setId(1); // ID de TRANSFERENCIAS
				catEg.getId().setUsuariosId(usr.getId());
				catEg.setUsuarios(usr);

				eg.setId(new EgresosId());
				eg.getId().setCatEgresosId(1);
				eg.getId().setCatEgresosUsuariosId(usr.getId());
				eg.setCategoria(catEg);
				eg.setConcepto("COMPRA");
				eg.setFecha(new Date());
				int status = ventasServ.realizarCompra(eg, usr, mail,
						new Integer(pin), new Integer(idProd));

				if (status == 1) {
					usr.setMsg("Saldo Insuficiente");
					usr.setId(-1);
				} else if (status == 2) {
					usr.setMsg("Credencials no válidas");
					usr.setId(-1);
				} else if (status == 0) {
					usr.setMsg("Transferencia satisfactoria");
					usr.setId(0);
				} else if (status == -1) {
					usr.setMsg("Error al transferir");
					usr.setId(-1);
				} else if (status == 3) {
					usr.setMsg("Pin invalido");
					usr.setId(-1);
				}

				usr.setStatusTrasnferencia(status);

			} else {
				usr = new Usuarios();
				usr.setMsg("Session terminada");
				usr.setId(-2);
			}
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			usr.setMsg(e.toString());
			return usr;
		}
	}


	
	@GET
	@Path("obtenerProductosTrans/{emailVend}/{min}/{max}")
	@Produces("application/json")
	public Usuarios obtenerListaProductosST(@PathParam("emailVend") String emailVend,
			@PathParam("min") String min,
			@PathParam("max") String max) {
		JSONArray list = new JSONArray();
		Usuarios usr = null;
		List<Producto> lista = new ArrayList();

		try {		
				usr = new Usuarios();
				usr.setEmail(emailVend);
				usr = (Usuarios) generalServ.buscarObjeto(usr).get(0);
				lista = ventasServ.obtenerProductosWS(usr.getId(),Integer.parseInt(min), Integer.parseInt(max));
				usr = new Usuarios();
				usr.setProductos(lista);
				usr.setId(0);

			
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			return usr;
		}

	}
	
	
	
	@GET
	@Path("procesarOrden/{idOrden}")
	@Produces("application/json")
	public Comprador procesarOrden(
		
			@PathParam("idOrden") String idOrden
			) {
		Comprador usr = null;

		try {
		
			OrdenesCompra orden = new OrdenesCompra();
			orden.setId(new Long(idOrden));
			orden.setAtendido("N");
			
			List lstOrden = generalServ.buscarObjeto(orden);
			
			orden = (OrdenesCompra) lstOrden.get(0);
			orden.setAtendido("S");
			
			 if(ventasServ.actualizar(orden)){
				 usr  = new Comprador();
				 usr.setId(0);
				 usr.setMsg("Orden atendida " + orden.getId());
				 return usr;
			 }
			 else{
				 usr = new  Comprador();
				 usr.setMsg("Orden no procesada");
				 usr.setId(-1);
			 }
				 return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Comprador();
			usr.setMsg("Orden ya procesada");
			usr.setId(-1);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new  Comprador();
			usr.setId(-1);
			usr.setMsg(e.toString());
			return usr;
		}
	}
	
	@GET
	@Path("generarOrden/{mail}/{idProd}/{cantidad}")
	@Produces("application/json")
	public Comprador CompraProductoTrans(
			@PathParam("mail") String mail,
			@PathParam("idProd") String idProd,
			@PathParam("cantidad") String cantidad) {
		Comprador comprador = null;

		try {
		
			Producto producto = new Producto();
			producto.setId(new Integer(idProd));			
			producto = (Producto) ventasServ.buscarObjeto(producto).get(0);
			
			comprador= new Comprador();
			comprador.setEmail(mail);			
			comprador = (Comprador) ventasServ.buscarObjeto(comprador).get(0);
			long orden = ventasServ.generarOrdenCompra(comprador, producto, new Float(cantidad).intValue());
			
			 if(orden!=-1){
				 comprador.setStatusTrasnferencia(orden);
				 
			 }
			 else{
				 comprador = new  Comprador();
				 comprador.setMsg("Orden no procesada");
				 comprador.setId(-1);
			 }
				 return comprador;
		} catch (IndexOutOfBoundsException l) {
			comprador = new Comprador();
			comprador.setMsg("Correo o producto no v�lido");
			comprador.setId(-2);
			return comprador;
		} catch (Exception e) {
			e.printStackTrace();
			comprador = new  Comprador();
			comprador.setId(-1);
			comprador.setMsg(e.toString());
			return comprador;
		}
	}
	
	@GET
	@Path("obtenerOrdenes/{emailVend}/{min}/{max}")
	// @Produces({MediaType.APPLICATION_JSON})
	@Produces("application/json")
	public Usuarios obtenerOrdenes(	@PathParam("emailVend") String emailVend,
			@PathParam("min") String min,
			@PathParam("max") String max) {
		JSONArray list = new JSONArray();
		Usuarios usr = null;
		List<OrdenesCompra> lista = new ArrayList();

		try {
		
				usr = new Usuarios();
				usr.setEmail(emailVend);
				usr = (Usuarios) generalServ.buscarObjeto(usr).get(0);
				lista = ventasServ.obtenerOrdenes(usr.getId(),
						Integer.parseInt(min), Integer.parseInt(max));
				usr = new Usuarios();
				usr.setOrdenes(lista);
				usr.setId(0);

			
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			return usr;
		}

	}
	
	
	@GET
	@Path("sumaPuntos/{email}/{idEncuesta}/{codigo}/{emailVendor}")
	// @Produces({MediaType.APPLICATION_JSON})
	@Produces("application/json")
	public Usuarios sumarPuntos(	@PathParam("email") String email,
			@PathParam("idEncuesta") String idEncuesta,
			@PathParam("codigo") String codigo,
			@PathParam("emailVendor") String emailVendor
	) {
		JSONArray list = new JSONArray();
		Usuarios usr = null;
		List<OrdenesCompra> lista = new ArrayList();

		try {
		
				usr = new Usuarios();
				usr.setEmail(email);
				
				usr = (Usuarios) generalServ.buscarObjeto(usr).get(0);
				
				UsuariosPuntos puntos = new UsuariosPuntos();
				puntos.setId(new UsuariosPuntosId());
				puntos.getId().setCodigo(codigo);
				puntos.getId().setEmail(email);
				puntos.getId().setIdEncuesta(new Integer(idEncuesta));
				puntos.getId().setIndice(1);
				puntos.setRedimido("N");
				puntos.setFechaPuntos(new Date());
				puntos.setEmailVendor(emailVendor);
				puntos.setPuntos(PUNTOS);
				
				if(generalServ.insertar(puntos))				
					generalServ.sumaPuntos(usr.getId().toString(), PUNTOS, emailVendor);
				
			
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			return usr;
		}

	}
	

	@GET
	@Path("redimirPuntos/{mailEncuestador}/{mailVendor}/{idProd}/{pin}")
	@Produces("application/json")
	public Usuarios redimePuntos(
			@PathParam("mailEncuestador") String mailEncuestador,
			@PathParam("mailVendor") String mailVendor,
			@PathParam("idProd") String idProd, @PathParam("pin") String pin) {
		Usuarios usr = null;

		try {
		
				
				usr = generalServ.buscarUsuario(mailEncuestador);

				Egresos eg = new Egresos();
				Categresos catEg = new Categresos();
				catEg.setId(new CategresosId());
				catEg.getId().setId(1); // ID de TRANSFERENCIAS
				catEg.getId().setUsuariosId(usr.getId());
				catEg.setUsuarios(usr);

				eg.setId(new EgresosId());
				eg.getId().setCatEgresosId(1);
				eg.getId().setCatEgresosUsuariosId(usr.getId());
				eg.setCategoria(catEg);
				eg.setConcepto("COMPRA");
				eg.setFecha(new Date());
				
				
				
				int status = ventasServ.realizarCompra(eg, usr, mailVendor,
						new Integer(pin), new Integer(idProd));

				
				
				if (status == 1) {
					usr.setMsg("Saldo Insuficiente");
					usr.setId(-1);
				} else if (status == 2) {
					usr.setMsg("Credencials no v�lidas");
					usr.setId(-1);
				} else if (status == 0) {
					usr.setMsg("Transferencia satisfactoria");
					usr.setId(0);
				} else if (status == -1) {
					usr.setMsg("Error al transferir");
					usr.setId(-1);
				} else if (status == 3) {
					usr.setMsg("Pin invalido");
					usr.setId(-1);
				}

				usr.setStatusTrasnferencia(status);

			
			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			usr.setMsg(e.toString());
			return usr;
		}
	}

	
	@GET
	@Path("pointofSale/{mailEmisor}/{mailReceptor}/{cantidad}/{pin}")
	@Produces("application/json")
	public Usuarios pointofSale(@PathParam("mailEmisor") String mailEmisor,
			@PathParam("mailReceptor") String mailReceptor,
			@PathParam("cantidad") String cantidad, @PathParam("pin") String pin) {
		Usuarios usr = null;

		try {

			usr = new Usuarios();
			usr.setEmail(mailEmisor);
			usr = (Usuarios) generalServ.buscarObjeto(usr).get(0);

			Egresos eg = new Egresos();
			Categresos catEg = new Categresos();
			catEg.setId(new CategresosId());
			catEg.getId().setId(1); // ID de TRANSFERENCIAS
			catEg.getId().setUsuariosId(usr.getId());
			catEg.setUsuarios(usr);

			eg.setId(new EgresosId());
			eg.getId().setCatEgresosId(1);
			eg.getId().setCatEgresosUsuariosId(usr.getId());
			eg.setCategoria(catEg);
			eg.setConcepto("Pago en punto de venta");
			eg.setCantidad(new BigDecimal(cantidad));
			eg.setFecha(new Date());
			int status = generalServ.realizarTransferencia(eg, usr,
					mailReceptor, new Integer(pin));

			if (status == 1) {
				usr.setMsg("Saldo Insuficiente");
				usr.setId(-1);
			} else if (status == 2) {
				usr.setMsg("Credencials no válidas");
				usr.setId(-1);
			} else if (status == 0) {
				usr.setMsg("Transferencia satisfactoria");
				usr.setId(0);
			} else if (status == -1) {
				usr.setMsg("Error al transferir");
				usr.setId(-1);
			} else if (status == 3) {
				usr.setMsg("Pin invalido");
				usr.setId(-1);
			}

			usr.setStatusTrasnferencia(status);

			return usr;
		} catch (IndexOutOfBoundsException l) {
			usr = new Usuarios();
			usr.setMsg("Session terminada");
			usr.setId(-2);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			usr = new Usuarios();
			usr.setId(-1);
			usr.setMsg(e.toString());
			return usr;
		}
	}

	@POST
	@Path("subirFoto")
	@Produces("application/json")
	public Usuarios subirImagen(Usuarios foto) {
		try {
			String SAVE_PATH = "\\";
			SAVE_PATH = PathContext.getImgPathReport();

			String nombreArchivo = new RandomAlphaNum().gen(5);
			System.out.print("nom " + nombreArchivo);
			byte[] btDataFile = new sun.misc.BASE64Decoder().decodeBuffer(foto
					.getMsg());
			File of = new File(SAVE_PATH + nombreArchivo + ".jpg");
			FileOutputStream osf = new FileOutputStream(of);
			osf.write(btDataFile);
			osf.flush();

			Usuarios usr = new Usuarios();
			usr.setId(0);
			return usr;
		} catch (Exception e) {
			e.printStackTrace();
			Usuarios usr = new Usuarios();
			usr.setId(-1);
			return usr;
		}

	}

	public static class MyProtocolSocketFactory implements
			ProtocolSocketFactory {
		private final SSLContext sslContext;

		public MyProtocolSocketFactory() throws NoSuchAlgorithmException,
				KeyManagementException {
			this.sslContext = SSLContext.getInstance("SSL");
			TrustManager trustManager = new MyTrustManager();
			TrustManager[] trustManagers = { trustManager };
			this.sslContext.init(null, trustManagers, null);
		}

		public Socket createSocket(String host, int port) throws IOException,
				UnknownHostException {
			return this.sslContext.getSocketFactory().createSocket(host, port);
		}

		public Socket createSocket(String host, int port,
				InetAddress localHost, int localPort) throws IOException,
				UnknownHostException {
			return this.sslContext.getSocketFactory().createSocket(host, port,
					localHost, localPort);
		}

		public Socket createSocket(String host, int port,
				InetAddress localHost, int localPort,
				HttpConnectionParams params) throws IOException,
				UnknownHostException, ConnectTimeoutException {
			return this.sslContext.getSocketFactory().createSocket(host, port,
					localHost, localPort);
		}

		public static void main(String[] args) {

			try {

				Client client = Client.create();

				WebResource webResource = client
						.resource("https://www.saldo.mx/Saldos/api/saldos/subirFoto");

				String input = "{\"msg\":\"/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB"
						+ "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB"
						+ "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCABkAGQDASIA"
						+ "AhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA"
						+ "AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3"
						+ "ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm"
						+ "p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA"
						+ "AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx"
						+ "BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK"
						+ "U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3"
						+ "uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD+/iii"
						+ "igBrMF6/kOT9cdcU3zAegY/8BNNk++P90/zFeNfFf4mXnge1stP0DT7TVfFOszPBplvfTSx6XZKI"
						+ "JZRqGtS22JreyUQ3Lou6EXZs5bSO5huJ4MuMXJ2jq/y9X0+YHs/mr6N/3yaPNX0b/vk18Xj4tfF1"
						+ "dCkha9+H8niZA3l6kng3xDHocx85tpGgt8R3v4x5KjKv4nyXdH3L+8hj9E8IfHSO51Gy0TxvpNt4"
						+ "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\"}";

				ProtocolSocketFactory protocolSocketFactory = new MyProtocolSocketFactory();
				Protocol myProtocol = new Protocol("https",
						protocolSocketFactory, 443);
				Protocol.registerProtocol("https", myProtocol);

				ClientResponse response = webResource.type("application/json")
						.post(ClientResponse.class, input);

				if (response.getStatus() != 201) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ response.getStatus());
				}

				System.out.println("Output from Server .... \n");
				String output = response.getEntity(String.class);
				System.out.println(output);

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

	}

	private static class MyTrustManager implements X509TrustManager {

		// private static final Log LOG =
		// LogFactory.getLog(MyTrustManager.class);

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			throw new UnsupportedOperationException();
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			throw new UnsupportedOperationException();
		}
	}

	class RandomAlphaNum {
		public String gen(int length) {
			StringBuffer sb = new StringBuffer();
			for (int i = length; i > 0; i -= 12) {
				int n = min(12, abs(i));
				sb.append(leftPad(
						Long.toString(round(random() * pow(36, n)), 36), n, '0'));
			}
			return sb.toString();
		}
	}

}
