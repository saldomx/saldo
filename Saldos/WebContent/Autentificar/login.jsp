<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.zkoss.org/jsp/zul" prefix="z"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="i"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript">
function formSubmit()
{
document.getElementById("lg").submit();
}
</script> 
<head>
<link href="../Estilos/v2_styles.css" type="text/css" rel="stylesheet" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body> 
<form id="lg" method="POST" action="<c:url value="/j_spring_security_check" />">
<div
	style="width: 900px; height: 200px; margin-left: auto; margin-right: auto; background-image: url(../images/saldosmxgray.png)">
<div style="height: 40px; width: 40px" ><br/>
<div align="left">
	
</div>
</div>
<div class="center-div">
<div class="contenttext">
<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		<table border="0" cellpadding="0">
			<tr>
				<td align="center" colspan="2"> 
				<h4>CREDENCIALES</h4>
				</td>
			</tr>
			<tr>
				<td align="right"><label class="login-text">Usuario:</label></td>
				<td><input type="text" name="j_username" /></td>
			</tr>
			<tr>
				<td align="right"><label class="login-text">
				Password:</label></td>
				<td><input type="password" name="j_password" /></td>
			</tr>
			
			<tr>
				<td align="right" colspan="2">
				
				<a href="MenuRegistro.zul"><img src="btn283707189.png" 
					  height="30px" width="70px" /></a>
				<input type="image"  src="btn2837071113.png" onclick="formSubmit()"
					  height="30px" width="70px" /></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
</div>
</div>




</form>
</body>
</html>