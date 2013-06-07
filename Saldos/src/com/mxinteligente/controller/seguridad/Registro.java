package com.mxinteligente.controller.seguridad;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static org.apache.commons.lang.StringUtils.leftPad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Captcha;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.mxinteligente.config.SpringApplicationContext;
import com.mxinteligente.infra.model.nego.MailService;
import com.mxinteligente.infra.model.nego.TemplateHtml;
import com.mxinteligente.model.entidades.Categresos;
import com.mxinteligente.model.entidades.CategresosId;
import com.mxinteligente.model.entidades.Catingresos;
import com.mxinteligente.model.entidades.CatingresosId;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.GeneralServI;
import com.mxinteligente.model.negocio.SesionesServI;

public class Registro extends GenericForwardComposer {

	private Textbox textNombre, textApp, textApm, textEmail, txtPassword;
	private Intbox intNip;
	private Usuarios user;
	private Grid formRegistro;
	private Label lblRegistro, lblRegistro2;
	private Button btnGuardar;
	private Captcha cpa;
	private Textbox captchaInput;
	private Label captchaResult;
	private Datebox dateFec;
	private Textbox textDom, textCLABE;
	

	@Autowired(required = true)
	GeneralServI generalServ;

	@Autowired(required = true)
	private PasswordEncoder passwordEncoder;
	
	@Autowired(required=true)
	MailService mailService;
	
	 @Autowired
	 private TemplateHtml tamplateHtml; 
	 

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		lblRegistro.setVisible(false);
		lblRegistro2.setVisible(false);

	}

	public void onClick$btnGuardar() throws Exception {
		if (cpa.getValue().equalsIgnoreCase(captchaInput.getValue())) {
			if (textNombre.isValid() && textApm.isValid() && textApm.isValid()
					&& textEmail.isValid() && dateFec.isValid()) {
				try {
					if(generalServ.buscarUsuario(textEmail.getValue())==null){

					String codigo = new RandomAlphaNum().gen(25);
					user = new Usuarios();
					user.setApm(textApm.getValue());
					user.setApp(textApp.getValue());
					user.setEmail(textEmail.getValue());
					user.setEstatus(0);
					user.setNombre(textNombre.getValue());
					user.setFechnac(dateFec.getValue());
					user.setDomicilio(textDom.getValue());
					user.setClabe(textCLABE.getValue());
					user.setCodigo(codigo);
					passwordEncoder = (PasswordEncoder) SpringApplicationContext
							.getBean("passwordEncoder");
					String paswd = passwordEncoder.encodePassword(txtPassword
							.getValue(), null);
					user.setPassword(paswd);
					user.setNip(intNip.getValue());
					user.setRol("ROLE_USER");

					generalServ.insertar(user);
					lblRegistro.setVisible(true);
					formRegistro.setVisible(false);
					btnGuardar.setVisible(false);

					user = (Usuarios) generalServ.buscarUsuario(textEmail.getValue());

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
					
					String body1 = tamplateHtml.templateVerificacion("https://www.saldo.mx/Saldos/Autentificar/validacion.zul?codigo="+codigo);
					mailService.sendMail("avisos@saldo.mx", user.getEmail(), "EMAIL CONFIRMACIîN ", body1);
					
					}
					else{
						Messagebox
						.show("El correo especificado ya ha sido registrado, favor de verificar");
					}
				} catch (Exception e) {
					e.printStackTrace();
					Messagebox
					
							.show("No fue posible completar el registro, favor de verificar");
					lblRegistro2.setVisible(true);


				}
			}else{
				textNombre.getValue();
				textApm.getValue();
				textApp.getValue();
				textEmail.getValue();
				dateFec.getValue();
			}
			
		}
		else{
			Messagebox
			.show("Datos incorrectos, favor de verificar");

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
