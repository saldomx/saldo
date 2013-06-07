package com.mxinteligente.infra;

import org.zkoss.zk.ui.Executions;

public class PathContext {
	private static String path;
	private static String url;
	
	static{
		//path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		//if(!(path.endsWith("/") || path.endsWith("\\")))
		//	 path += "/";
		//path="/Applications/XAMPP/htdocs/";
		path="/var/www/";
		url="http://www.saldo.mx/";
		//url="http://localhost/";
	}
	
	public static String getPath(){
		return path;
	}
	
	public static String getImgPath(){
		return path + "vending/images/";
	}
	public static String getImgThumbs(){
		return path + "vending/thumbnails/";
	}
	
	public static String getImgUrl(){
		return url + "vending/images/";
	}
	public static String getImgUrlThumbs(){
		return url + "vending/thumbnails/";
	}
	
	public static String getRptPath(){
		return path + "WEB-INF/templaterpt/";
	}	
	
	public static String getImgPathReport(){
		return path + "reporting/images/";
	}
	
}