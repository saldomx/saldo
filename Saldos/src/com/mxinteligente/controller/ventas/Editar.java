package com.mxinteligente.controller.ventas;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static org.apache.commons.lang.StringUtils.leftPad;

import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.mxinteligente.controller.ventas.AltaProducto.RandomAlphaNum;
import com.mxinteligente.infra.PathContext;
import com.mxinteligente.infra.ThumbNail;
import com.mxinteligente.model.entidades.Producto;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;

public class Editar extends GenericForwardComposer {

	private Textbox textNombre, textCodigo, textDescripcion;
	private Usuarios user;
	private Grid formRegistro;
	private Label lblRegistro, lblRegistro2;
	private Button btnGuarda, btnEliminar;
	private Datebox dateFec;
	private Window winprod;
	private Textbox decPrecio;
	private static final int FILE_SIZE = 100;// 100k
	private static String SAVE_PATH = "\\";
	Fileupload btnCargarImg;
	Image image1, image2, image3;
	private Usuarios usuario;
	Producto p;
	static Logger logger = LoggerFactory.getLogger(AltaProducto.class);

	@Autowired(required=true)
	private GeneralServI generalServ;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try{
		SAVE_PATH = PathContext.getImgPath();
		Integer id = (Integer)arg.get("usrId");
		Integer idProd = (Integer)arg.get("producto");
		usuario = new Usuarios();
		usuario.setId(id);
		p = new Producto();
		p.setId(idProd);
		p = (Producto) generalServ.buscarObjeto(p).get(0);
		
		this.textCodigo.setValue(p.getCode());
		this.textDescripcion.setValue(p.getDescription());
		this.textNombre.setValue(p.getName());
		this.decPrecio.setValue(p.getPrice().toPlainString());
	
		if(p.getImage1()!=null && p.getImage1().length()>0)
			image1.setContent(new org.zkoss.image.AImage(PathContext.getImgPath() + p.getImage1()));
		if(p.getImage2()!=null && p.getImage2().length()>0)
			image2.setContent(new org.zkoss.image.AImage(PathContext.getImgPath() + p.getImage2()));
		if(p.getImage3()!=null && p.getImage3().length()>0)
			image3.setContent(new org.zkoss.image.AImage(PathContext.getImgPath() + p.getImage3()));

		
		
		
		}
		catch(Exception e){
			e.printStackTrace();
			Messagebox.show("Error al recuperar el producto, favor de verificar");
		}
		
	}
	
	
	public void onClick$btnGuardar() throws Exception{
	p.setCode(textCodigo.getValue());
	p.setDescription(textDescripcion.getValue());
	p.setPrice(new BigDecimal(decPrecio.getValue()));
	p.setName(textNombre.getValue());
//	
//	if(image1.getContent()!=null && image1.isVisible()){
//		Media media = image1.getContent();		
//		this.saveFile(image1.getContent(), (p.getRutaImagen1().substring(p.getRutaImagen1().lastIndexOf("/")+1)));
//		
//		new ThumbNail().createThumbnail(SAVE_PATH + (p.getRutaImagen1().substring(p.getRutaImagen1().lastIndexOf("/")+1)),
//				PathContext.getImgThumbs()+(p.getRutaImagen1().substring(p.getRutaImagen1().lastIndexOf("/")+1)) , 100, 100);
//		p.setRutaImagen1(SAVE_PATH + (p.getRutaImagen1().substring(p.getRutaImagen1().lastIndexOf("/")+1)) );
//	}
//	if(image2.getContent()!=null && image2.isVisible()){
//		Media media = image2.getContent();		
//		this.saveFile(image2.getContent(), (p.getRutaImagen2().substring(p.getRutaImagen2().lastIndexOf("/")+1)));
//		
//		new ThumbNail().createThumbnail(SAVE_PATH + (p.getRutaImagen2().substring(p.getRutaImagen2().lastIndexOf("/")+1)),
//				PathContext.getImgThumbs()+(p.getRutaImagen2().substring(p.getRutaImagen2().lastIndexOf("/")+1)) , 100, 100);
//		p.setRutaImagen2(SAVE_PATH + (p.getRutaImagen2().substring(p.getRutaImagen2().lastIndexOf("/")+1)) );
//	}
//	if(image3.getContent()!=null && image3.isVisible()){
//		Media media = image3.getContent();		
//		this.saveFile(image3.getContent(), (p.getImage3().substring(p.getImage3().lastIndexOf("/")+1)));
//		
//		new ThumbNail().createThumbnail(SAVE_PATH + (p.getRutaImagen3().substring(p.getRutaImagen3().lastIndexOf("/")+1)),
//				PathContext.getImgThumbs()+(p.getRutaImagen3().substring(p.getRutaImagen3().lastIndexOf("/")+1)) , 100, 100);
//		p.setRutaImagen1(SAVE_PATH + (p.getRutaImagen3().substring(p.getRutaImagen3().lastIndexOf("/")+1)) );
//	}
//	
	String nombreArchivo;

	if(image1.getContent()!=null && image1.isVisible()){
		if(p.getImage1()!=null && p.getImage1().length()>0){
		this.deleteFile(p.getImage1());
		this.deleteFile(PathContext.getImgThumbs()+(p.getImage1().substring(p.getImage1().lastIndexOf("/")+1)));
		}
		nombreArchivo = new RandomAlphaNum().gen(5)+p.getCode();	
		Media media = image1.getContent();
		String tipo = media.getName().substring(media.getName().indexOf("."));
		
		this.saveFile(image1.getContent(), nombreArchivo+tipo  );
		
		new ThumbNail().createThumbnail(SAVE_PATH + nombreArchivo + tipo,
				PathContext.getImgThumbs()+nombreArchivo + tipo , 100, 100);
		p.setImage1( nombreArchivo  + tipo );
	}else if(p.getImage1()!=null && p.getImage1().length()>0){
		this.deleteFile(PathContext.getImgPath()+ p.getImage1());
		this.deleteFile(PathContext.getImgThumbs()+(p.getImage1()));
		p.setImage1("");
	}
	
	if(image2.getContent()!=null && image2.isVisible()){
		if(p.getImage2()!=null && p.getImage2().length()>0){
		this.deleteFile(p.getImage2());
		this.deleteFile(PathContext.getImgThumbs()+(p.getImage2().substring(p.getImage2().lastIndexOf("/")+1)));
		}
		nombreArchivo = new RandomAlphaNum().gen(5)+p.getCode();	
		Media media = image2.getContent();
		String tipo = media.getName().substring(media.getName().indexOf("."));
		
		this.saveFile(image2.getContent(), nombreArchivo+tipo  );
		
		new ThumbNail().createThumbnail(SAVE_PATH + nombreArchivo + tipo,
				PathContext.getImgThumbs()+nombreArchivo + tipo , 100, 100);
		p.setImage2( nombreArchivo  + tipo );
	
	}else if(p.getImage2()!=null && p.getImage2().length()>0){
		this.deleteFile(PathContext.getImgPath()+p.getImage2());
		this.deleteFile(PathContext.getImgThumbs()+ p.getImage2());
		p.setImage2("");

	}
	if(image3.getContent()!=null && image3.isVisible()){
		if(p.getImage3()!=null && p.getImage3().length()>0){
		this.deleteFile(p.getImage3());
		this.deleteFile(PathContext.getImgThumbs()+(p.getImage3().substring(p.getImage3().lastIndexOf("/")+1)));
		}
		
		nombreArchivo = new RandomAlphaNum().gen(5)+p.getCode();	
		Media media = image3.getContent();
		String tipo = media.getName().substring(media.getName().indexOf("."));
		
		this.saveFile(image3.getContent(), nombreArchivo+tipo  );
		
		new ThumbNail().createThumbnail(SAVE_PATH + nombreArchivo + tipo,
				PathContext.getImgThumbs()+nombreArchivo + tipo , 100, 100);
		p.setImage3( nombreArchivo  + tipo );
		
	}else if(p.getImage3()!=null && p.getImage3().length()>0){
		p.setImage3("");
		this.deleteFile(PathContext.getImgPath() + p.getImage3());
		this.deleteFile(PathContext.getImgThumbs()+p.getImage3());

	}
	generalServ.actualizar(p);	
		
		
	}
	
	public void onClick$btnEliminar()
	{
		new ConfirmaGBorrado().show();
	}	
	
	public void onAccept$btnEliminar(){
		generalServ.eliminar(p);
		
	}
	
	private void  deleteFile(String nameFile){
	
		

		File file = new File( nameFile);
		if (file.exists())	
		file.delete();
		

		
		
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

		

	
	private void saveFile(Media media, String nameFile) {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			
			InputStream fin = media.getStreamData();			
			in = new BufferedInputStream(fin);
			File baseDir = new File(SAVE_PATH);
			
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			File file = new File(SAVE_PATH + nameFile);
			
			OutputStream fout = new FileOutputStream(file);
			out = new BufferedOutputStream(fout);
			byte buffer[] = new byte[1024];
			int ch = in.read(buffer);
			while (ch != -1) {
				out.write(buffer, 0, ch);
				ch = in.read(buffer);
			}			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null) 
					out.close();	
				
				if (in != null)
					in.close();
				
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

	}
	
	protected class ConfirmaGBorrado {

		public void show() {
			try {
				Messagebox.show("Desea Eliminar este registro",
						"Confirmaci�n", Messagebox.OK, Messagebox.INFORMATION,
						new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event event) {
								if ("onOK".equals(event.getName())) {
									realizarAfirmativo();
								}
							}
						});
			} catch (InterruptedException ex) {

			}
		}

		public void realizarAfirmativo() {
			Events.sendEvent(new Event("onAccept", btnEliminar));
		}
	}
	
}
