package com.mxinteligente.infra;

import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

public class ReqListener implements ServletRequestListener {

  private static long reqCount;

  public void requestInitialized(ServletRequestEvent sre) {

    ServletContext context = sre.getServletContext();
    ServletRequest request = sre.getServletRequest();

    synchronized (context) {
      context
          .log("Request for " + (request instanceof HttpServletRequest ? ((HttpServletRequest) request).getRequestURI(): "Unknown") + "; Count=" + ++reqCount);
      
      if (request instanceof HttpServletRequest){
    	  String url = ((HttpServletRequest) request).getRequestURI();
      	if(url.contains("comprar")){
    	  ArduinoFile nr  = new ArduinoFile();
    	  Thread maquinauno = new Thread(nr);
    	  maquinauno.start();
      	}
      }

    }

  }

  public void requestDestroyed(ServletRequestEvent sre) {

  }

  class ArduinoFile implements Runnable {
	   public void run() {
	         try {
	        	 
	          Thread.sleep(8000);
	          FileWriter fstream = new FileWriter("/var/www/vending/xml/buy.xml");
			  BufferedWriter out = new BufferedWriter(fstream);
			  String parametro = "<id>0</id><amount>0</amount>";
			  out.write(parametro);
			  out.close();
	         
	         } catch (InterruptedException ex) { 
	        	 ex.printStackTrace();
	         }
	         catch(Exception e){
	        	 e.printStackTrace();
	         }
	      
	    }
	}


  
}