package com.mxinteligente.model.negocio;

import java.util.List;

import com.mxinteligente.infra.model.nego.GenServicioI;
import com.mxinteligente.model.entidades.Comprador;
import com.mxinteligente.model.entidades.Egresos;
import com.mxinteligente.model.entidades.Producto;
import com.mxinteligente.model.entidades.Usuarios;

public interface VentasServI extends GenServicioI{

	public int obtenerTotalProductos(int usuario);
	public List obtenerProductos(int usuario, int min, int max);
	public List obtenerProductosWS(int usuario, int min, int max);
	
	public int realizarCompra(Egresos gasto, Usuarios user, String email, Integer pin, Integer idProducto);
	
	public boolean modificarArchivo(Producto p);
	
	public long generarOrdenCompra(Comprador comprador, Producto producto, int cantidad);
	
	public List obtenerOrdenes(int usuario, int min, int max);

	public boolean abonaSaldoTelefonica(Producto p, String cellphone);

}
