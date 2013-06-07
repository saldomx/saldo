package com.mxinteligente.controller.admin;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.mxinteligente.model.negocio.AdminServI;

public class GeneraCodigosCtl extends GenericForwardComposer{
	
	private Button btnGenera;
	private Textbox monto;
	private Intbox cantidad;
	private Window win;
	
	@Autowired(required=true)
	private AdminServI adminServ;
	
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	
	public void onClick$btnGenera() throws Exception{
		if(monto.isValid() && cantidad.isValid()){
			if(adminServ.generarCodigos(new BigDecimal(monto.getValue()), cantidad.getValue())){
				Messagebox.show("Codigos generados exitosamente");
				win.detach();
			}
			else{
				Messagebox.show("Existieron problemas al generar los c—digos, favor de verificar");
				win.detach();
			}
		}		
	}

}
