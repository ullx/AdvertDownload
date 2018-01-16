package test;

public class Anuncio {
 
	String nombreAutor = "";
	String descripcion = "";
	String tel = "";
	
	public Anuncio() {
		
	}
	
	public Anuncio(String nombreAutor, String descripcion, String tel) {
		this.nombreAutor = nombreAutor;
		this.descripcion = descripcion;
		this.tel = tel;
	}
	
	public String getNombreAutor() {
		return nombreAutor;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public String getTel () {
		return tel;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public void setNombreAutor(String nombreAutor) {
		this.nombreAutor = nombreAutor;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
}
