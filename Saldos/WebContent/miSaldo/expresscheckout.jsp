
<html>
<head>
<title>Pay through PayPal: www.TestAccount.com</title>
</head>
<body onload="document.forms['paypalForm'].submit();">

 <form name="paypalForm" action="https://api-3t.sandbox.paypal.com/nvp" method="post">
 <input type="hidden" name="cmd" value="_xclick" />
 <input type="hidden" name="business" value="fermtx_1317599370_biz@yahoo.com.mx" />
 <input type="hidden" name="password" value="1317599405" />
 <input type="hidden" name="custom" value="1123" />
 <input type="hidden" name="item_name" value="Computer-Laptop" />
 <input type="hidden" name="amount" value="30"/>
 <input type="hidden" name="rm" value="1" />
 <input type="hidden" name="return" value="http://localhost:8080/Saldos/Autentificar/login.jsp" />
 <input type="hidden" name="cancel_return" value="http://localhost:8080/Saldos/Autentificar/login.jsp" />
 <input type="hidden" name="cert_id" value="AigPxseG5xXRAWzQvhox8GvzaGlpAm5E8OHQpvTX43xgIE6K.rlgqD0N " />
</form>
</body>
</html>
<!-- 
<form name="paypalForm" action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post">
 -->