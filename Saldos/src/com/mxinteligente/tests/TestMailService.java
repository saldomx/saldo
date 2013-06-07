package com.mxinteligente.tests;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.testng.annotations.Test;

import com.mxinteligente.infra.model.nego.MailService;
import com.mxinteligente.model.entidades.Usuarios;
import com.mxinteligente.model.negocio.AdminServI;

public class TestMailService {

	
	MailService mailService;
	
	@Test(enabled=true)
	public void setConfiguration() {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"/WebContent/WEB-INF/applicationContext.xml");
		
		

		//ingresosDao = (IngresosDaoI) ctx.getBean("ingresosDao");
		//catingresosDao = (CatingresosDaoI) ctx.getBean("catingresosDao");

		//egresosDao = (EgresosDaoI) ctx.getBean("egresosDao");
		
	//	passwordEncoder = (PasswordEncoder) ctx.getBean("passwordEncode");
		
		 mailService = (MailService)  ctx.getBean("mailService");

	}
	
	@Test(enabled = true, dependsOnMethods = { "setConfiguration" })
	public void obtenerCategorias() {
		


		String html = "<html>"+
	"<head>"+
		"<title></title>"+
	"</head>"+
	"<body>"+
	"	<table border='0' cellpadding='3' cellspacing='3' style='height: 343px; width: 594px;'>"+
	"		<tbody>"+
	"			<tr>"+
	"				<td colspan='2'>"+
	"					<img alt='' "+
"src='http://ec2-107-20-92-191.compute-1.amazonaws.com:8080/Saldos/images/saldo_login.png' style='width:"+ 
"298px; height: 120px;' /></td>"+
				"</tr>"+
				"<tr>"+
					"<td style='background-color: rgb(204, 204, 204); width: 180px;'>"+
					"	<span style='color: rgb(0, 51, 0);'><span "+
"style='font-size: 14px;'><span style='font-family: "+
"tahoma,geneva,sans-serif;'>Beneficiario:</span></span></span></td>"+
"					<td>"+
"						<strong><span style='font-size: 14px;'><span"+ 
"style='font-family: tahoma,geneva,sans-serif;'> {beneficiario} </span></span></strong></td>"+
"				</tr>"+
"				<tr>"+
"					<td>"+
"						&nbsp;</td>"+
"					<td>"+
"						&nbsp;</td>"+
"				</tr>"+
"				<tr>"+
"					<td style='background-color: rgb(204, 204, 204);'>"+
"						<span style='font-size: 14px;'><span style='font-family:"+ 
"tahoma,geneva,sans-serif;'><span style='color: rgb(0, 51, 0);'>Enviado por:</span></span></span></td>"+
"					<td>"+
"						<strong><span style='color: rgb(0, 0, 0);'><span"+ 
"style='font-size: 14px;'><span style='font-family:"+ 
"tahoma,geneva,sans-serif;'>{enviadoPor}</span></span></span></strong></td>"+
"				</tr>"+
"				<tr>"+
"					<td>"+
"						&nbsp;</td>"+
"					<td>"+
"						&nbsp;</td>"+
"				</tr>"+
"				<tr>"+
"				<td style='background-color: rgb(204, 204, 204);'>"+
"						<span style='color: rgb(0, 51, 0);'><span"+ 
"style='font-size: 14px;'><span style='font-family: tahoma,geneva,sans-serif;'>ID"+ 
"Pago</span></span></span></td>"+
					"<td>"+
					"	<strong><span style='font-size: 14px;'><span"+ 
"style='font-family: tahoma,geneva,sans-serif;'>{idPago}</span></span></strong></td>"+
"				</tr>"+
"				<tr>"+
"					<td style='background-color: rgb(204, 204, 204);'>"+
"						<span style='color: rgb(0, 51, 0);'><span "+
"style='font-size: 14px;'><span style='font-family: "+
"tahoma,geneva,sans-serif;'>Fecha</span></span></span></td>"+
"					<td>"+
						"<strong><span style='font-size: 14px;'><span"+ 
"style='font-family: tahoma,geneva,sans-serif;'>{fecha}</span></span></strong></td>"+
"				</tr>"+
				"<tr>"+
				"	<td style='background-color: rgb(204, 204, 204);'>"+
						"<span style='color: rgb(0, 51, 0);'><span "+
"style='font-size: 14px;'><span style='font-family: "+
"tahoma,geneva,sans-serif;'>Cantidad</span></span></span></td>"+
					"<td>"+
					"	<strong><span style='font-size: 14px;'><span"+ 
"style='font-family: tahoma,geneva,sans-serif;'>{cantidad}</span></span></strong></td>"+
"				</tr>"+
"			</tbody>"+
"		</table></body>"+
"</html>";
		
		html.replace("{beneficiario}", "Fernando Morales Serrano");
		html.replace("{enviadoPor}", "Marco Montes Neri");
		html.replace("{idPago}", "34534");
		html.replace("{fecha}", "12 Septiembre del 2011");
		html.replace("{cantidad}", "5678.00");


		mailService.sendMail("avisos@saldo.mx", "fermsx@gmail.com", "Testing123", html);
	        
	      mailService.sendAlertMail("Exception occurred");

		
	}
	
	
}
