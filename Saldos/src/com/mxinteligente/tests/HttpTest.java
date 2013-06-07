package com.mxinteligente.tests;



import java.io.IOException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpClient;
import org.testng.annotations.Test;


public class HttpTest {

    private static final String HOST = "http://localhost:8080/Saldos/app";

    @Test
    public void personaTodos() throws IOException {
        HttpClient httpclient = new HttpClient();
        HttpMethod method = new GetMethod(HOST + "/persona/todos");
        method.addRequestHeader("accept", "application/json");

        httpclient.executeMethod(method);
        String resultado = method.getResponseBodyAsString();
        System.out.println(resultado);

      
    }

    @Test
    public void personaPorId() throws IOException {
        HttpClient httpclient = new HttpClient();
        HttpMethod method = new GetMethod(HOST + "/persona/34421");
        method.addRequestHeader("accept", "application/json");

        httpclient.executeMethod(method);
        String resultado = method.getResponseBodyAsString();
        System.out.println(resultado);

    
    }

    @Test
    public void personaPorIdXml() throws IOException {
        HttpClient httpclient = new HttpClient();
        HttpMethod method = new GetMethod(HOST + "/persona/34421");
        method.addRequestHeader("accept", "application/xml");

        httpclient.executeMethod(method);
        String resultado = method.getResponseBodyAsString();
        System.out.println(resultado);

        
    }


    @Test
    public void mostrar() throws IOException {
        HttpClient httpclient = new HttpClient();
        HttpMethod method = new GetMethod(HOST + "/persona/34421");
        method.addRequestHeader("accept", "application/xml");

        httpclient.executeMethod(method);
        String resultado = method.getResponseBodyAsString();

        System.out.println("RESULTADO:");
        System.out.println(resultado);
    }


}