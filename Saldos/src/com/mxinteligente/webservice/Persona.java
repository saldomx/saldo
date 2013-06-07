package com.mxinteligente.webservice;



import javax.xml.bind.annotation.XmlRootElement;


/** Esta anotaci�n es para serializar usando Jaxb2 */
@XmlRootElement
public class Persona {

    private String nombre;
    private String apellido;

    public Persona() {
    }

    public Persona(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



}
