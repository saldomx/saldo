package com.mxinteligente.infra;

public class Pago 
{

	public void metodo(){
		String PaymentOption = "";
	if (PaymentOption == "Visa" || PaymentOption == "MasterCard" || PaymentOption == "Amex" || PaymentOption == "Discover")
	{

	
		String paymentAmount = "";//(String)session.getAttribute("Payment_Amount");

		String paymentType = "Sale";
		
		
		
		String creditCardType 		= "<<Visa/MasterCard/Amex/Discover>>"; //' Set this to one of the acceptable values (Visa/MasterCard/Amex/Discover) match it to what was selected on your Billing page
		String creditCardNumber 	= "<<CC number>>"; // ' Set this to the string entered as the credit card number on the Billing page
		String expDate 				= "<<Expiry Date>>"; // ' Set this to the credit card expiry date entered on the Billing page
		String cvv2 				= "<<cvv2>>"; // ' Set this to the CVV2 string entered on the Billing page 
		String firstName 			= "<<firstName>>"; // ' Set this to the customer's first name that was entered on the Billing page 
		String lastName 			= "<<lastName>>"; // ' Set this to the customer's last name that was entered on the Billing page 
		String street 				= "<<street>>"; // ' Set this to the customer's street address that was entered on the Billing page 
		String city 				= "<<city>>"; // ' Set this to the customer's city that was entered on the Billing page 
		String state 				= "<<state>>"; // ' Set this to the customer's state that was entered on the Billing page 
		String zip 					= "<<zip>>"; // ' Set this to the zip code of the customer's address that was entered on the Billing page 
		String countryCode 			= "<<PayPal Country Code>>"; // ' Set this to the PayPal code for the Country of the customer's address that was entered on the Billing page 
		String currencyCode 		= "<<PayPal Currency Code>>"; // ' Set this to the PayPal code for the Currency used by the customer
		String orderDescription 	= "<<OrderDescription>>"; // ' Set this to the textual description of this order 
		
		/*	
		'------------------------------------------------
		' Calls the DoDirectPayment API call
		'
		' The DirectPayment function is defined in PayPalFunctions.java 
		' included at the top of this file.
		'-------------------------------------------------
		*/
	//	nvp = DirectPayment ( paymentType, paymentAmount, creditCardType, creditCardNumber,
//		expDate, cvv2, firstName, lastName, street, city, state, zip, 
//		countryCode, currencyCode, orderDescription ); 
//
//		String strAck = nvp.get("RESULT").toString();
//		if(strAck ==null || !strAck.equalsIgnoreCase("0"))
//		{
//			//Display a user friendly Error on the page using any of the following error information returned by Payflow
//			// See pages 50 through 65 in https://cms.paypal.com/cms_content/US/en_US/files/developer/PP_PayflowPro_Guide.pdf for a list of RESULT values (error codes)
//			String ErrorCode = strAck;
//			String ErrorMsg = nvp.get("RESPMSG").toString();
//		}
	}
	}
}

