package com.mxinteligente.model.negocio.impl;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mxinteligente.infra.PathContext;
import com.mxinteligente.infra.model.nego.impl.GenServicio;
import com.mxinteligente.model.dao.EgresosDaoI;
import com.mxinteligente.model.dao.OrdenesCompraDaoI;
import com.mxinteligente.model.dao.ProductoDaoI;
import com.mxinteligente.model.dao.UsuariosDaoI;
import com.mxinteligente.model.entidades.Comprador;
import com.mxinteligente.model.entidades.Egresos;
import com.mxinteligente.model.entidades.Producto;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;
import com.mxinteligente.model.negocio.VentasServI;

@Repository("ventasServ")
public class VentasServ extends GenServicio implements VentasServI{

	@Autowired(required=true)
	ProductoDaoI productoDao;
	
	@Autowired(required=true)
	private UsuariosDaoI usuariosDao;
	
	@Autowired(required=true)
	private GeneralServI generalServ;
	
	@Autowired(required=true)
	private EgresosDaoI egresosDao;

	@Autowired(required=true)
	private OrdenesCompraDaoI ordenesCompraDao;
	
	public int obtenerTotalProductos(int usuario) {
		return productoDao.obtenerTotalProductos(usuario);
	}

	public List obtenerProductos(int usuario, int min, int max) {
		return productoDao.obtenerProductos(usuario, min, max);
	
	}

	public List obtenerProductosWS(int usuario, int min, int max) {
		List <Producto> res = new ArrayList();
		List<Producto> lst = productoDao.obtenerProductos(usuario, min, max);
		Producto prod;
		for(Producto p:lst){
			prod=new Producto();
			prod.setCode(p.getCode());
			prod.setDescription(p.getDescription());
			prod.setId(p.getId());
			prod.setName(p.getName());
			
			if(p.getImage1()!=null)
			prod.setImage1(PathContext.getImgUrlThumbs() + p.getImage1());
			
			if(p.getImage2()!=null)
			prod.setImage2(PathContext.getImgUrlThumbs() + p.getImage2());
			
			if(p.getImage3()!=null)
			prod.setImage3(PathContext.getImgUrlThumbs() + p.getImage3());
			
			prod.setPrice(p.getPrice());
			res.add(prod);
		}
		
		return res;
	}
	
	public int realizarCompra(Egresos gasto, Usuarios user, String email, Integer pin, Integer idProducto){
		
		int status = -1;
		BigDecimal saldo = generalServ.calculaSaldo(user);
		
		
		Producto pro = new Producto();
		pro.setId(idProducto);
		List lstpro = productoDao.buscarPorObjeto(pro);
		
		if (lstpro!=null && !lstpro.isEmpty()){
			pro =(Producto) lstpro.get(0);
			gasto.setCantidad(pro.getPrice());
			gasto.setConcepto("COMPRA DE " + pro.getName());
			
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
				    status =  egresosDao.comprar(gasto, user, bUser,pro);
				    
				    
				    
//				    if(status==0)
//				    	this.modificarArchivo(pro);
				    
					return status;
				} else {
					return 2; // Regresa 2 si no existe el usuario
				}

			}
		} else
			return 3; //pin invalido
		}
		else
			return 5; //producto invalido
	}
	

	public boolean modificarArchivo(Producto p) {
		try{
			FileWriter fstream = new FileWriter("/var/www/vending/xml/buy.xml");
			  BufferedWriter out = new BufferedWriter(fstream);
		String parametro = "<id>"+p.getId()+ "</id><amount>"+p.getPrice()+"</amount>";
		  out.write(parametro);
		  out.close();
		return true;
		}catch(Exception t){
			t.printStackTrace();
			return false;
		}
	}

	public long generarOrdenCompra(Comprador comprador, 	Producto producto, int cantidad) {
		
		return ordenesCompraDao.insertarOden(comprador, producto, cantidad);
		
	}

	public List obtenerOrdenes(int usuario, int min, int max) {
		
		return ordenesCompraDao.obtenerOrdenes(usuario, min, max);
	}

	@Override
	public boolean abonaSaldoTelefonica(Producto p, String cellphone) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
