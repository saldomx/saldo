package com.mxinteligente.infra.model.dao;

import java.io.Serializable;
import java.util.List;

public interface BusquedaDaoI extends GenericoDaoI<Object,Serializable>{

	public List buscarPorCamposCriterio(Object tabla, String join, String[] filtro, Object[] valor,  String[] filtroEq, Object[] valorEq,int ini, int max, String... campos);
	public int obetenerTotal(Object obj);
	public int obetenerTotalFiltro(Object obj, String[] filtro, Object[] valor, String[] filtroEq, Object[] valorEq);
	
}
