package test;

import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class PageInmuebles24 extends AbstractAnunciosFlow {

	
	
	String baseURL = "http://www.inmuebles24.com/";
	String estado = null;
	Properties pro  = null;
	
	String getURL() {
		return baseURL;
	}

	public PageInmuebles24(Properties config) {
		this.pro  = config;
	}
	
	void hacerConsulta(BusquedaTipo busquedaTipo, String estado) {
		
		//Seleccionar renta o venta
		//poner inmueble
		
		//poner ubicacion
		
		//click de busqueda
		
		//poner rango de precios
		//click en submit
		
		seleccionarTransacciones(driver);
		seleccionarPropiedades(driver);
		seleccionarUbicaciones(driver);
		
	}



	void extraerGuardarDatos() {
		
		//aqui se hace el for o el while que recorre
		//todos los resultados y extrae los datos
		
		

		try {
			extraerDatos(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	
	
	public void seleccionarTransacciones(WebDriver driver) {
		WebElement transacciones = driver.findElement(By.id("vertical-operation-menu"));
		try {

			List<WebElement> transaccionesTipo = transacciones.findElements(By.tagName("a"));
			for (WebElement opciones : transaccionesTipo) {
				String transaccionTipo = pro.getProperty("transaccion");
				if (opciones.getText().equalsIgnoreCase(transaccionTipo)) {
					String a = opciones.getText();
					System.out.println("Transacción Seleccionada: " + a);
					opciones.click();
				}
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void seleccionarPropiedades(WebDriver driver) {
		WebElement tipoPropiedad = driver.findElement(By.id("searchbox-home_tipodepropiedad"));
		tipoPropiedad.click();

		try {
			List<WebElement> seleccionarPropiedad = tipoPropiedad.findElements(By.tagName("option"));

			for (WebElement opciones : seleccionarPropiedad) {
				String propiedadTipo = pro.getProperty("inmueble");
				if (opciones.getText().equalsIgnoreCase(propiedadTipo)) {
					String b = opciones.getText();
					System.out.println("Tipo de inmueble Seleccionado: " + b);
					opciones.click();
				}
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void seleccionarUbicaciones(WebDriver driver) {
		WebElement ubicacion = driver.findElement(By.id("searchbox-home_ubicacion"));
		ubicacion.sendKeys(pro.getProperty("ubicacion"));
		WebElement buscar = driver.findElement(By.id("submitBtn"));
		buscar.click();

	}

	public void extraerDatos(WebDriver driver) throws Exception {
		WebElement siguiente = null;

		for (;;) {

			try {
				WebElement precioMinimo = driver.findElement(By.id("preciomin"));
				precioMinimo.sendKeys(pro.getProperty("precioMin"));
				WebElement precioMaximo = driver.findElement(By.id("preciomax"));
				precioMaximo.sendKeys(pro.getProperty("precioMax"));
				WebElement precioBoton = driver.findElement(By.id("botonPrecio"));
				precioBoton.click();
			} catch (NoSuchElementException e) {
				System.out.println("No se ingresó rango de precios");
			}

			WebElement seleccionarAnuncios = driver.findElement(By.className("list-posts"));
			List<WebElement> listaResultados = seleccionarAnuncios.findElements(By.className("post"));
			int results = listaResultados.size();
			System.out.println("Número de resultados: " + results);

			for (int idx = 0; idx < results; idx++) {

				listaResultados.get(idx).click();

				try {

					WebElement descripccionPropiedad = driver.findElement(By.className("description-body"));
					String descripcion = descripccionPropiedad.getText();

					WebElement datoPropiedad = driver.findElement(By.className("btn-phone-text"));
					datoPropiedad.click();

					WebElement datoTelefono = driver.findElement(By.className("lead-phone"));
					String telefono = datoTelefono.getText();

					System.out.println("Descripcción: " + idx + " " + telefono + " " + descripcion);

					WebElement cerrarVentana = driver.findElement(By.className("fa-times"));
					cerrarVentana.click();

				} catch (NoSuchElementException e) {
					System.out.println("No se encontro télefono o descripción");
				}

				try {
					WebElement regresar = driver.findElement(By.className("ticon-arrow-left"));
					regresar.click();
				} catch (NoSuchElementException e) {
					System.out.println("No se encontro link para regresar");
					break;
				}

				seleccionarAnuncios = driver.findElement(By.className("list-posts"));
				listaResultados = seleccionarAnuncios.findElements(By.className("post"));

			}
			try {
				siguiente = driver.findElement(By.className("ticon-next"));
				if (siguiente.isEnabled()) {
					siguiente.click();
				}

			} catch (WebDriverException e) {
				System.out.println("Se llego al final de las paginas");
				break;
			}

			System.out.println("Terminando proceso");
		}
		System.out.println("Finished ");
	}
	
}
