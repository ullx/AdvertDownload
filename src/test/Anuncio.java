package test;

public class Anuncio {
 
	String transaccion = "";
	String inmueble = "";
	String descripcion = "";
	String telefono = "";
	String precio = "";
	
	public Anuncio() {
		
	}
	
	
	public Anuncio(String transaccion, String inmueble, String descripcion, String telefono, String precio) {
		this.transaccion = transaccion;
		this.inmueble = inmueble;
		this.descripcion = descripcion;
		this.telefono = telefono;
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
}
