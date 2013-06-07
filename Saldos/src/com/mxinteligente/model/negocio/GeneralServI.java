package com.mxinteligente.model.negocio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mxinteligente.infra.model.nego.GenServicioI;
import com.mxinteligente.model.entidades.Categresos;
import com.mxinteligente.model.entidades.Catingresos;
import com.mxinteligente.model.entidades.Egresos;
import com.mxinteligente.model.entidades.Estados;
import com.mxinteligente.model.entidades.Ingresos;
import com.mxinteligente.model.entidades.Movimientos;
import com.mxinteligente.model.entidades.Pais;
import com.mxinteligente.model.entidades.Usuarios;

public interface GeneralServI extends GenServicioI{
	
	public int obtenTotalIngresos(Usuarios user);
	public List obtenIngresos(Usuarios user, int min, int max);
	public List obtenIngresosPorCategoria(Catingresos cat, int min, int max);
	public int obtenTotalIngresosPorCategoria(Catingresos cat);
	
	public int obtenTotalEgresos(Usuarios user);
	public List obtenEgresos(Usuarios user, int min, int max);
	public List obtenEgresosPorCategoria(Categresos cat, int min, int max);
	public int obtenTotalEgresosPorCategoria(Categresos cat);
	
	public List<Map> obtenCategoriasIngresos(Usuarios user);
	public List<Map> obtenCategoriasEgresos(Usuarios user);
	
	
	public List<Map> obtenTodoslosRegistros(Usuarios user, int min, int max);
	//public int obtenTotalTodoslosRegistros(Usuarios user);

	
	public boolean insertarCategoriaIngreso(Catingresos cat);
	
	public boolean insertarCategoriaEgresos(Categresos cat);
	
	public boolean insertarIngreso(Ingresos i);
	
	public int insertarEgreso(Egresos e, Usuarios user);
	
	public BigDecimal calculaSaldo(Usuarios user);
	
	public int realizarTransferencia(Egresos gasto, Usuarios user, String email, Integer pin);

	public String cargarSaldo(String codigo, String user);
	
	public String retirodeFondos(BigDecimal envio, Usuarios user, String emailAgencia);
	
	public Usuarios buscarUsuario(String email);
	
	public boolean validaUsuario(String codigo);
	
	public String cargarSaldo(BigDecimal cantidad, String user);

	
	public List<Movimientos> obtenerMovimientos(Usuarios user, int min, int max);

	public List<Pais> obtenerPaises();
	
	public List<Estados> obtenerEstados(String pais);
	
	public String sumaPuntos(String user, int puntos, String vendor);


	public String realizarPago(String paymentAction,String amount,String cardType,
			String acct,String expdate,String cvv2, String firstName,
			String lastName, String street, String city, String state, 
			String zip, String countryCode);
}
