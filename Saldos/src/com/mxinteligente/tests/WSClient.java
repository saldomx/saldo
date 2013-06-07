package com.mxinteligente.tests;


import java.io.BufferedReader;
import java.io.InputStreamReader;
 
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
 
/**
* This program demonstrates consuming web service using Apache HTTPClient and SOAP message.
* Reference: http://www.webservicex.net/stockquote.asmx?op=GetQuote
* ClassPath: Keep these two files in class path: commons-httpclient-3.0.1.jar, commons-codec-1.4.jar
* @author Bhavani P Polimetla
* @since April-27-2011
*/
public class WSClient {
 
public static void main(String args[]) {
 
HttpClient httpClient = new HttpClient();
httpClient.getParams()
.setParameter("http.useragent", "Web Service Test Client");
 
BufferedReader br = null;
String data = "<?xml version='1.0' encoding='UTF-8'?>"+
"<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:loc='http://www.csapi.org/schema/parlayx/bonus/v1_0/local'>"+
"<soapenv:Header>"+
"<tns:RequestSOAPHeader xmlns:tns='http://www.huawei.com/schema/osg/common/v2_1'>"+
"<tns:spId>000180</tns:spId>"+
"<tns:spPassword>56B75B439760380E680E46764DD389BE</tns:spPassword>"+
"<tns:timeStamp>20130307162800</tns:timeStamp>"+
"<tns:serviceId>0100130100</tns:serviceId>"+
"<tns:OA>525524394853</tns:OA>"+
"</tns:RequestSOAPHeader>"+
"</soapenv:Header>"+
"<soapenv:Body>"+
"<loc:addBonusRequest>"+
"<loc:MSISDN>525524394853</loc:MSISDN>"+
"<loc:bonusCode></loc:bonusCode>"+
"<loc:bonusDescription></loc:bonusDescription>"+
"<loc:Quantity></loc:Quantity>"+
"<loc:Cost></loc:Cost>"+
"<loc:currencyCode></loc:currencyCode>"+
"<loc:userCode></loc:userCode>"+
"<loc:startDate></loc:startDate>"+
"<loc:endDate></loc:endDate>"+
"<loc:PlanAdditional></loc:PlanAdditional>"+
"<loc:ServiceType>DATOS</loc:ServiceType>"+
"</loc:addBonusRequest>"+
"</soapenv:Body>"+
"</soapenv:Envelope>";
PostMethod methodPost = new PostMethod(
"http://200.39.21.26:8080/osg/services/BonusService");
 
methodPost.setRequestBody(data);
methodPost.setRequestHeader("Content-Type", "text/xml");
 
try {
int returnCode = httpClient.executeMethod(methodPost);
 
if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
System.out
.println("The Post method is not implemented by this URI");
methodPost.getResponseBodyAsString();
} else {
br = new BufferedReader(new InputStreamReader(methodPost
.getResponseBodyAsStream()));
String readLine;
while (((readLine = br.readLine()) != null)) {
System.out.println(readLine);
}
}
} catch (Exception e) {
e.printStackTrace();
} finally {
methodPost.releaseConnection();
if (br != null)
try {
br.close();
} catch (Exception fe) {
fe.printStackTrace();
}
}
 
}
}