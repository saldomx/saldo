package com.mxinteligente.infra;

import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.core.nvp.NVPEncoder;
import com.paypal.sdk.profiles.APIProfile;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.services.NVPCallerServices;

public class DoDirectPayment 
{
 
	private NVPCallerServices caller = null;
 
	public String DoDirectPaymentCode(String paymentAction,String amount,String cardType,
								String acct,String expdate,String cvv2, String firstName,
								String lastName, String street, String city, String state, 
								String zip, String countryCode)
	{
		NVPEncoder encoder = new NVPEncoder();
		NVPDecoder decoder = new NVPDecoder();
 
		try
		{
 
        	caller = new NVPCallerServices();
		APIProfile profile = ProfileFactory.createSignatureAPIProfile();
			/*
			 WARNING: Do not embed plaintext credentials in your application code.
			 Doing so is insecure and against best practices.
			 Your API credentials must be handled securely. Please consider
			 encrypting them for use in any production environment, and ensure
			 that only authorized individuals may view or modify them.
			 */
		// Set up your API credentials, PayPal end point, API operation and version.
//			profile.setAPIUsername("fermtx_1320865589_biz_api1.yahoo.com.mx");
//	        profile.setAPIPassword("1320865621");
//	        profile.setSignature("AQU0e5vuZCvSg-XJploSa.sGUDlpAzI7ke55L2ZINUbN2Aa83MXLJG6l");
//	        profile.setEnvironment("sandbox");
		
		profile.setAPIUsername("ventas_api1.mexicointeligente.com");
        profile.setAPIPassword("QRBSFFPB2EC9YRMP");
        profile.setSignature("AFcWxV21C7fd0v3bYYYRCpSSRl31AyAb7kEPq1CTq7hE8bzw44szkGmv");
        profile.setEnvironment("live");
	        profile.setSubject("");
	        caller.setAPIProfile(profile);
 
			encoder.add("VERSION", "51.0");
			encoder.add("METHOD","DoDirectPayment");
 
		// Add request-specific fields to the request string.
			encoder.add("PAYMENTACTION",paymentAction);
			encoder.add("AMT",amount);
			encoder.add("CREDITCARDTYPE",cardType);		
			encoder.add("ACCT",acct);						
			encoder.add("EXPDATE",expdate);
			encoder.add("CVV2",cvv2);
			encoder.add("FIRSTNAME",firstName);
			encoder.add("LASTNAME",lastName);										
			encoder.add("STREET",street);
			encoder.add("CITY",city);	
			encoder.add("STATE",state);			
			encoder.add("ZIP",zip);	
			encoder.add("COUNTRYCODE",countryCode);
 
		// Execute the API operation and obtain the response.
			String NVPRequest = encoder.encode();
			String NVPResponse =(String) caller.call(NVPRequest);
			decoder.decode(NVPResponse);
 
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return decoder.get("ACK");
	}
	
	  
    public static void main(String[] args)
    {
    try
    {
    	DoDirectPayment pp = new DoDirectPayment();
    	
    		String r=	pp.DoDirectPaymentCode("Sale", "2","Visa", "4745820735140041", "102016", "0041", "Test", "User", "1 Main St","San Jose","CA","95131","US");

    		System.out.println(r);
//    CreditCardInformation cci = new CreditCardInformation("4721930402892796", "Visa", "000", "11", "2007");
//    CardOwnerInformation coi = new CardOwnerInformation("David", "Bulmore", "123 dfhufidh", null, "Glendale", "AZ", "43844", "US");
//
//    pp.directPayment("12.36.5.78",123.99,cci,coi);
    }
    catch (Exception e)
    {
    e.printStackTrace();
    }
    }
    
 
}