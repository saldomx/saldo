package com.mxinteligente.infra.model.nego;

import org.springframework.stereotype.Service;

@Service("tamplateHtml")
public class TemplateHtml {

	
	public String templateTransferencia(String beneficiario, String enviadoPor, int idPago, String fecha, String Cantidad){
		
		
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
	"src='http://50.57.140.59:8080/Saldos/images/saldo_login.png' style='width:"+ 
	"298px; height: 120px;' /></td>"+
					"</tr>"+
					"<tr>"+
						"<td style='background-color: rgb(204, 204, 204); width: 180px;'>"+
						"	<span style='color: rgb(0, 51, 0);'><span "+
	"style='font-size: 14px;'><span style='font-family: "+
	"tahoma,geneva,sans-serif;'>Beneficiario:</span></span></span></td>"+
	"					<td>"+
	"						<strong><span style='font-size: 14px;'><span"+ 
	"style='font-family: tahoma,geneva,sans-serif;'>"+ beneficiario +"</span></span></strong></td>"+
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
	"tahoma,geneva,sans-serif;'>"+enviadoPor + "</span></span></span></strong></td>"+
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
	"style='font-family: tahoma,geneva,sans-serif;'>"+idPago+"</span></span></strong></td>"+
	"				</tr>"+
	"				<tr>"+
	"					<td style='background-color: rgb(204, 204, 204);'>"+
	"						<span style='color: rgb(0, 51, 0);'><span "+
	"style='font-size: 14px;'><span style='font-family: "+
	"tahoma,geneva,sans-serif;'>Fecha</span></span></span></td>"+
	"					<td>"+
							"<strong><span style='font-size: 14px;'><span"+ 
	"style='font-family: tahoma,geneva,sans-serif;'>"+fecha+"</span></span></strong></td>"+
	"				</tr>"+
					"<tr>"+
					"	<td style='background-color: rgb(204, 204, 204);'>"+
							"<span style='color: rgb(0, 51, 0);'><span "+
	"style='font-size: 14px;'><span style='font-family: "+
	"tahoma,geneva,sans-serif;'>Cantidad</span></span></span></td>"+
						"<td>"+
						"	<strong><span style='font-size: 14px;'><span"+ 
	"style='font-family: tahoma,geneva,sans-serif;'>$"+ Cantidad + " </span></span></strong></td>"+
	"				</tr>"+
	"			</tbody>"+
	"		</table></body>"+
	"</html>";
		
		return html;
		
	}
	

	public String templateVentaProd(String vendedor, String enviadoPor, int idPago, String fecha, String Cantidad){
		
		
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
	"src='http://50.57.140.59:8080/Saldos/images/saldo_login.png' style='width:"+ 
	"298px; height: 120px;' /></td>"+
					"</tr>"+
					"<tr>"+
						"<td style='background-color: rgb(204, 204, 204); width: 180px;'>"+
						"	<span style='color: rgb(0, 51, 0);'><span "+
	"style='font-size: 14px;'><span style='font-family: "+
	"tahoma,geneva,sans-serif;'>Vendedor:</span></span></span></td>"+
	"					<td>"+
	"						<strong><span style='font-size: 14px;'><span"+ 
	"style='font-family: tahoma,geneva,sans-serif;'>"+ vendedor +"</span></span></strong></td>"+
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
	"tahoma,geneva,sans-serif;'><span style='color: rgb(0, 51, 0);'>Cliente:</span></span></span></td>"+
	"					<td>"+
	"						<strong><span style='color: rgb(0, 0, 0);'><span"+ 
	"style='font-size: 14px;'><span style='font-family:"+ 
	"tahoma,geneva,sans-serif;'>"+enviadoPor + "</span></span></span></strong></td>"+
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
	"style='font-family: tahoma,geneva,sans-serif;'>"+idPago+"</span></span></strong></td>"+
	"				</tr>"+
	"				<tr>"+
	"					<td style='background-color: rgb(204, 204, 204);'>"+
	"						<span style='color: rgb(0, 51, 0);'><span "+
	"style='font-size: 14px;'><span style='font-family: "+
	"tahoma,geneva,sans-serif;'>Fecha</span></span></span></td>"+
	"					<td>"+
							"<strong><span style='font-size: 14px;'><span"+ 
	"style='font-family: tahoma,geneva,sans-serif;'>"+fecha+"</span></span></strong></td>"+
	"				</tr>"+
					"<tr>"+
					"	<td style='background-color: rgb(204, 204, 204);'>"+
							"<span style='color: rgb(0, 51, 0);'><span "+
	"style='font-size: 14px;'><span style='font-family: "+
	"tahoma,geneva,sans-serif;'>Cantidad</span></span></span></td>"+
						"<td>"+
						"	<strong><span style='font-size: 14px;'><span"+ 
	"style='font-family: tahoma,geneva,sans-serif;'>$"+ Cantidad + "</span></span></strong></td>"+
	"				</tr>"+
	"			</tbody>"+
	"		</table></body>"+
	"</html>";
		
		return html;
		
	}
	
	
	public String templateRetiro(String beneficiario, String cantidad, String idRetiro, String fecha){
		String html ="<html>"+
		"	<head>"+
		"		<title></title>"+
		"	</head>"+
		"	<body>"+
		"		<table border='0' cellpadding='3' cellspacing='3' style='height: 343px; width: 594px;'>"+
		"			<tbody>"+
		"				<tr>"+
		"					<td colspan='2'>"+
		"						<img alt='' "+
		"src='http://ec2-174-129-106-44.compute-1.amazonaws.com:8080/Saldos/images/saldo_login.png' style='width: "+
		"298px; height: 120px;' /></td>"+
		"				</tr>"+
		"				<tr>"+
		"					<td style='background-color: rgb(204, 204, 204); width: 180px;'>"+
		"						<span style='color: rgb(0, 51, 0);'><span "+
		"style='font-size: 14px;'><span style='font-family: "+
		"tahoma,geneva,sans-serif;'>Beneficiario:</span></span></span></td>"+
		"					<td>"+
		"						<strong><span style='font-size: 14px;'><span "+
		"style='font-family: tahoma,geneva,sans-serif;'>"+beneficiario+"</span></span></strong></td>"+
		"				</tr>"+
		"				<tr>"+
		"					<td>"+
		"						&nbsp;</td>"+
		"					<td>"+
		"						&nbsp;</td>"+
		"				</tr>"+
		"				<tr>"+
		"					<td style='background-color: rgb(204, 204, 204);'>"+
		"						<span style='font-size: 14px;'><span style='font-family: "+
		"tahoma,geneva,sans-serif;'><span style='color: rgb(0, 51, 0);'>Retiro</span></span></span></td>"+
		"					<td>"+
		"						<strong><span style='color: rgb(0, 0, 0);'><span "+
		"style='font-size: 14px;'><span style='font-family: "+
		"tahoma,geneva,sans-serif;'>"+cantidad+"</span></span></span></strong></td>"+
		"				</tr>"+
		"				<tr>"+
		"					<td>"+
		"						&nbsp;</td>"+
		"					<td>"+
		"						&nbsp;</td>"+
		"				</tr>"+
		"				<tr>"+
		"					<td style='background-color: rgb(204, 204, 204);'>"+
		"						<span style='color: rgb(0, 51, 0);'><span "+
		"style='font-size: 14px;'><span style='font-family: tahoma,geneva,sans-serif;'>ID "+
		"Retiro</span></span></span></td>"+
		"					<td>"+
		"						<strong><span style='font-size: 14px;'><span "+
		"style='font-family: tahoma,geneva,sans-serif;'>"+idRetiro+"</span></span></strong></td>"+
		"				</tr>"+
		"				<tr>"+
		"					<td style='background-color: rgb(204, 204, 204);'>"+
		"						<span style='color: rgb(0, 51, 0);'><span "+
		"style='font-size: 14px;'><span style='font-family: "+
		"tahoma,geneva,sans-serif;'>Fecha</span></span></span></td>"+
		"					<td>"+
		"						<strong><span style='font-size: 14px;'><span "+
		"style='font-family: tahoma,geneva,sans-serif;'>"+fecha+"</span></span></strong></td>"+
		"				</tr>"+
		"			</tbody>"+
		"		</table></body>"+
		"</html>";

		return html;		
		
	}	
	
	
	public String templateCargo(String beneficiario, String codigo, String cantidad, String fecha){
			String html="<html>"+
			"	<head>"+
			"		<title></title>"+
			"	</head>"+
			"	<body>"+
			"		<table border='0' cellpadding='3' cellspacing='3' style='height: 343px; width: 594px;'>"+
			"			<tbody>"+
			"				<tr>"+
			"					<td colspan='2'>"+
			"						<img alt='' "+
			"src='http://50.57.140.59:8080/Saldos/images/saldo_login.png' style='width: "+
			"298px; height: 120px;' /></td>"+
			"				</tr>"+
			"				<tr>"+
			"					<td style='background-color: rgb(204, 204, 204); width: 180px;'>"+
			"						<span style='color: rgb(0, 51, 0);'><span "+
			"style='font-size: 14px;'><span style='font-family: "+
			"tahoma,geneva,sans-serif;'>Beneficiario:</span></span></span></td>"+
			"					<td>"+
			"						<strong><span style='font-size: 14px;'><span "+
			"style='font-family: tahoma,geneva,sans-serif;'>"+beneficiario+"</span></span></strong></td>"+
			"				</tr>"+
			"				<tr>"+
			"					<td>"+
			"						&nbsp;</td>"+
			"					<td>"+
			"						&nbsp;</td>"+
			"				</tr>"+
			"				<tr>"+
			"					<td style='background-color: rgb(204, 204, 204);'>"+
			"						<span style='font-size: 14px;'><span style='font-family: "+
			"tahoma,geneva,sans-serif;'><span style='color: rgb(0, 51, 0);'>C&oacute;digo</span></span></span></td>"+
			"					<td>"+
			"						<strong><span style='color: rgb(0, 0, 0);'><span "+
			"style='font-size: 14px;'><span style='font-family: "+
			"tahoma,geneva,sans-serif;'>"+codigo+"</span></span></span></strong></td>"+
			"				</tr>"+
			"				<tr>"+
			"					<td>"+
			"						&nbsp;</td>"+
			"					<td>"+
			"						&nbsp;</td>"+
			"				</tr>"+
			"				<tr>"+
			"					<td style='background-color: rgb(204, 204, 204);'>"+
			"						<span style='color: rgb(0, 51, 0);'><span "+
			"style='font-size: 14px;'><span style='font-family: "+
			"tahoma,geneva,sans-serif;'>Cantidad</span></span></span></td>"+
			"					<td>"+
			"						<strong><span style='font-size: 14px;'><span "+
			"style='font-family: tahoma,geneva,sans-serif;'>$"+cantidad+"</span></span></strong></td>"+
			"				</tr>"+
			
			"				<tr>"+
			"					<td style='background-color: rgb(204, 204, 204);'>"+
			"						<span style='color: rgb(0, 51, 0);'><span "+
			"style='font-size: 14px;'><span style='font-family: "+
			"tahoma,geneva,sans-serif;'>Fecha</span></span></span></td>"+
			"					<td>"+
			"						<strong><span style='font-size: 14px;'><span "+
			"style='font-family: tahoma,geneva,sans-serif;'>"+fecha+"</span></span></strong></td>"+
			"				</tr>"+
			"			</tbody>"+
			"		</table></body>"+
			"</html>";

return html;
		
	}
	
	public String templateVerificacion(String codigo){
		String html="<html>"+
		"	<head>"+
		"		<title></title>"+
		"	</head>"+
		"	<body>"+
		"		<table border='0' cellpadding='3' cellspacing='3' style='height: 343px; width: 594px;'>"+
		"			<tbody>"+
		"				<tr>"+
		"					<td colspan='2'>"+
		"						<img alt='' "+
		"src='http://50.57.140.59:8080/Saldos/images/saldo_login.png' style='width: "+
		"298px; height: 120px;' /></td>"+
		"				</tr>"+
		"				<tr>"+
		"					<td colspan='2'>Verifica tu registro haciendo Click en el siguiente link"+
		"						&nbsp;</td>"+
		"				</tr>"+	
		
		"				<tr>"+
		"					<td style='background-color: rgb(204, 204, 204);'>"+
		"						<span style='font-size: 14px;'><span style='font-family: "+
		"tahoma,geneva,sans-serif;'><span style='color: rgb(0, 51, 0);'>Link</span></span></span></td>"+
		"					<td>"+
		"						<strong><span style='color: rgb(0, 0, 0);'><span "+
		"style='font-size: 14px;'><span style='font-family: "+
		"tahoma,geneva,sans-serif;'><a href='"+codigo+"'>Verificar mi Registro</a></span></span></span></strong></td>"+
		"				</tr>"+
		"				<tr>"+
		"					<td>"+
		"						&nbsp;</td>"+
		"					<td>"+
		"						&nbsp;</td>"+
		"				</tr>"+
		"			</tbody>"+
		"		</table></body>"+
		"</html>";

return html;
	
}
	
	public String templateReCargo(String beneficiario, String cantidad, String fecha){
		String html="<html>"+
		"	<head>"+
		"		<title></title>"+
		"	</head>"+
		"	<body>"+
		"		<table border='0' cellpadding='3' cellspacing='3' style='height: 343px; width: 594px;'>"+
		"			<tbody>"+
		"				<tr>"+
		"					<td colspan='2'>"+
		"						<img alt='' "+
		"src='http://50.57.140.59:8080/Saldos/images/saldo_login.png' style='width: "+
		"298px; height: 120px;' /></td>"+
		"				</tr>"+
		"				<tr>"+
		"					<td style='background-color: rgb(204, 204, 204); width: 180px;'>"+
		"						<span style='color: rgb(0, 51, 0);'><span "+
		"style='font-size: 14px;'><span style='font-family: "+
		"tahoma,geneva,sans-serif;'>Beneficiario:</span></span></span></td>"+
		"					<td>"+
		"						<strong><span style='font-size: 14px;'><span "+
		"style='font-family: tahoma,geneva,sans-serif;'>"+beneficiario+"</span></span></strong></td>"+
		"				</tr>"+
		"				<tr>"+
		"					<td>"+
		"						&nbsp;</td>"+
		"					<td>"+
		"						&nbsp;</td>"+
		"				</tr>"+
		"				<tr>"+
		"					<td>"+
		"						&nbsp;</td>"+
		"					<td>"+
		"						&nbsp;</td>"+
		"				</tr>"+
		"				<tr>"+
		"					<td style='background-color: rgb(204, 204, 204);'>"+
		"						<span style='color: rgb(0, 51, 0);'><span "+
		"style='font-size: 14px;'><span style='font-family: "+
		"tahoma,geneva,sans-serif;'>Cantidad</span></span></span></td>"+
		"					<td>"+
		"						<strong><span style='font-size: 14px;'><span "+
		"style='font-family: tahoma,geneva,sans-serif;'>"+cantidad+"</span></span></strong></td>"+
		"				</tr>"+
		
		"				<tr>"+
		"					<td style='background-color: rgb(204, 204, 204);'>"+
		"						<span style='color: rgb(0, 51, 0);'><span "+
		"style='font-size: 14px;'><span style='font-family: "+
		"tahoma,geneva,sans-serif;'>Fecha</span></span></span></td>"+
		"					<td>"+
		"						<strong><span style='font-size: 14px;'><span "+
		"style='font-family: tahoma,geneva,sans-serif;'>"+fecha+"</span></span></strong></td>"+
		"				</tr>"+
		"			</tbody>"+
		"		</table></body>"+
		"</html>";

return html;
	
}
	
public String templateErrorTransferencia(String beneficiario, String enviadoPor, int idPago, String fecha, String Cantidad){
		
		
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
	"src='http://50.57.140.59:8080/Saldos/images/saldo_login.png' style='width:"+ 
	"298px; height: 120px;' /></td>"+
					"</tr>"+
					"<tr>"+
						"<td style='background-color: rgb(204, 204, 204); width: 180px;'>"+
						"	<span style='color: rgb(0, 51, 0);'><span "+
	"style='font-size: 14px;'><span style='font-family: "+
	"tahoma,geneva,sans-serif;'>Usuario:</span></span></span></td>"+
	"					<td>"+
	"						<strong><span style='font-size: 14px;'><span"+ 
	"style='font-family: tahoma,geneva,sans-serif;'>"+ beneficiario +"</span></span></strong></td>"+
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
	"tahoma,geneva,sans-serif;'>"+enviadoPor + "</span></span></span></strong></td>"+
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
	"style='font-family: tahoma,geneva,sans-serif;'>"+idPago+"</span></span></strong></td>"+
	"				</tr>"+
	"				<tr>"+
	"					<td style='background-color: rgb(204, 204, 204);'>"+
	"						<span style='color: rgb(0, 51, 0);'><span "+
	"style='font-size: 14px;'><span style='font-family: "+
	"tahoma,geneva,sans-serif;'>Fecha</span></span></span></td>"+
	"					<td>"+
							"<strong><span style='font-size: 14px;'><span"+ 
	"style='font-family: tahoma,geneva,sans-serif;'>"+fecha+"</span></span></strong></td>"+
	"				</tr>"+
					"<tr>"+
					"	<td style='background-color: rgb(204, 204, 204);'>"+
							"<span style='color: rgb(0, 51, 0);'><span "+
	"style='font-size: 14px;'><span style='font-family: "+
	"tahoma,geneva,sans-serif;'>Cantidad</span></span></span></td>"+
						"<td>"+
						"	<strong><span style='font-size: 14px;'><span"+ 
	"style='font-family: tahoma,geneva,sans-serif;'>$"+ Cantidad + " </span></span></strong></td>"+
	"				</tr>"+
	"			</tbody>"+
	"		</table></body>"+
	"</html>";
		
		return html;
		
	}
	

	
}
