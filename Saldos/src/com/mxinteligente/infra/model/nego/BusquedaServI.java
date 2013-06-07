package com.mxinteligente.infra.model.nego;


import java.util.List;
import java.util.Vector;



public interface BusquedaServI extends GenServicioI {

	
	public int obtenerTotal(Object obj);
	public int obtenerTotalFiltro(Object obj, String filtro[], Object valor[], String filtroEq[], Object valorEq[]);
	public List buscarObjetos(Object obj, String join, String[] filtro, Object[] valor, String[] filtroEq, Object[] valorEq, int inicial, int maximo, String ... campos);
	
}
