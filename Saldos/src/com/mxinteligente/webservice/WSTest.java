package com.mxinteligente.webservice;




import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

//use Lombok to generate the getter/setter/toString/equals/... methods
import lombok.Data;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This resource represents the WebServices "test"
 * 
 * @author Gwennael Buchet
 */
 //the path you will use to call the Webservices : http://xx.xx.xx.xx:8080/test
@Path("/test")
@Component //tell Spring it's a component, so Spring will parse it at the start of the application 
//(see the <context:component-scan base-package="com.codeartex.jersey.web" /> declaration in the applicationcontext.xml)
@Scope("singleton") //tell Spring it's a singleton, so instanciate it only one time
@Data  //Lombok annotation
public class WSTest {

	//uncomment these lines to use a service layer
	// you will ha ve to declare the corresponding bean in the applicationContext.xml as well (same declaration as the bean of this webservice)
	//@Autowired
	//private ITestService iTestService;

	/**
	 * Webservice "showDate": show the date passed in parameter
	 * 
	 * @param dateMaj
	 *            Format : "YYYY-MM-DD"
	 * @return
	 */
	@GET
	// the path to cal this webservice:
	//http://xx.xx.xx.xx:8080/test/showDate/2010-08-27
	@Path("showDate/{dateMaj}") 
	@Produces("text/plain")
	public String getUpdatedMagasins(@PathParam("dateMaj") String dateMaj) {
		return dateMaj;
	}

	/**
	 * Webservice "hello" : return a string returning a "Hello {name}" String
	 * @param name the name you want to show with the "Hello ..."
	 *
	 */
	@GET
	@Path("hello/{name}")
	@Produces("text/plain")
	public String getMagasinById(@PathParam("name") String name) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("Hello ").append(name);

		return buffer.toString();
	}
}
