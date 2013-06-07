package com.mxinteligente.infra.model.nego.impl;



import java.util.List;
import java.util.Vector;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mxinteligente.infra.model.dao.BusquedaDaoI;
import com.mxinteligente.infra.model.nego.BusquedaServI;

@Service("busquedaServicio")
public class BusquedaServ extends GenServicio implements  BusquedaServI{
	
	
	@Autowired(required=true)
	private BusquedaDaoI busquedaDao;
	
	protected Class clase;
	
	public void ponerClase(Class clase) {
		this.clase = clase;
	}

	public int obtenerTotal(Object obj){
		return busquedaDao.obetenerTotal(obj);
	}
	
	public int obtenerTotalFiltro(Object obj, String filtro[], Object valor[], String filtroeEq[], Object valorEq[]){
		return busquedaDao.obetenerTotalFiltro(obj, filtro, valor, filtroeEq, valorEq );
	}
	
	public List buscarObjetos(Object obj, String join, String[] filtro, Object[] valor, String[] filtroEq, Object[] valorEq, int inicial, int maximo, String ... campos){
		return busquedaDao.buscarPorCamposCriterio(obj, join,filtro, valor, filtroEq, valorEq, inicial, maximo, campos);
		
	}

	
	
	
}
