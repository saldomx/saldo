package com.mxinteligente.model.negocio.impl;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static org.apache.commons.lang.StringUtils.leftPad;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mxinteligente.infra.model.nego.impl.GenServicio;
import com.mxinteligente.model.dao.CodigosDaoI;
import com.mxinteligente.model.dao.UsuariosDaoI;
import com.mxinteligente.model.entidades.Codigos;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.AdminServI;

@Service("adminServ")
public class AdminServ extends GenServicio implements AdminServI{

	
	@Autowired(required=true)
	UsuariosDaoI usuariosDao;
	
	@Autowired(required=true)
	CodigosDaoI codigosDao;

	public List<Usuarios> obtenerTodosUsuarios() {
		return usuariosDao.buscarAll();
	}

	public int obtenerTotalUsuarios() {
		return usuariosDao.contarTodos();
	}

	public List<Usuarios> obtenerUsuarios(int min, int max) {
		return usuariosDao.obtenerUsuarios(min, max);
	
	}

	public boolean generarCodigos(BigDecimal monto, int cantidad) {
		Codigos code;
		String key;
		try{
		for(int i=0; i<cantidad; i++){
			code = new Codigos();
			key = new RandomAlphaNum().gen(15);
			code.setCodigo(key.toLowerCase());
			code.setMonto(monto);
			codigosDao.insertar(code);
		}
		return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
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
