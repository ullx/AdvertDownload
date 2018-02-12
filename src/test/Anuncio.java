package test;

import java.util.List;

public class Anuncio {
 
	String transaccion = "";
	String inmueble = "";
	String descripcion = "";
	String telefono = "";
	String precio = "";
	String datos = "";
	String titulo= "";
	
	public Anuncio() {
		
	}
	
	
	public Anuncio(String transaccion, String inmueble, String descripcion, String telefono, String precio, String datos, String titulo) {
		this.transaccion = transaccion;
		this.inmueble = inmueble;
		this.descripcion = descripcion;
		this.telefono = telefono;
		this.precio = precio;
		this.datos = datos;
	}
	
	public String getTransaccion() {
		return transaccion;
	}
	
	public String getInmueble() {
		return inmueble;
	}
	
	public String getDescripcion () {
		return descripcion;
	}
	
	public String getTelefono () {
		return telefono;
	}
	
	public String getPrecio () {
		return precio;
	}
	
	public String getDatos () {
		return datos;
	}
	
	public String getTitulo () {
		return titulo;
	}
	
	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}
	
	public void setInmueble(String inmueble) {
		this.inmueble = inmueble;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	
	public void setDatos(String datos) {
		this.datos = datos;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}
