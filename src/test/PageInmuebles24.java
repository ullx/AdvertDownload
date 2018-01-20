package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class PageInmuebles24 extends AbstractAnunciosFlow {

	String baseURL = "http://www.inmuebles24.com/";
	String estado = null;
	Properties pro = null;

	@Override
	String getURL() {
		return baseURL;
	}

	///////////////////////////////////////////////////////////////////////////////////////

	public PageInmuebles24(Properties config) {
		this.pro = config;
	}

	///////////////////////////////////////////////////////////////////////////////////////

	private List<Anuncio> consulta() {
		
		List<Anuncio> anuncios = new ArrayList<Anuncio>();
		Anuncio a = new Anuncio();
		WebElement transacciones = driver.findElement(By.id("vertical-operation-menu"));
		try {

			List<WebElement> transaccionesTipo = transacciones.findElements(By.tagName("a"));
			for (WebElement opciones : transaccionesTipo) {
				String transaccionTipo = pro.getProperty("transaccion");
				if (opciones.getText().equalsIgnoreCase(transaccionTipo)) {
					a.setTransaccion(opciones.getText());
					System.out.println("Transacción Seleccionada: " + a.getTransaccion());
					opciones.click();
				}
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		///////////

		WebElement tipoPropiedad = driver.findElement(By.id("searchbox-home_tipodepropiedad"));
		tipoPropiedad.click();

		try {
			List<WebElement> seleccionarPropiedad = tipoPropiedad.findElements(By.tagName("option"));

			for (WebElement opciones : seleccionarPropiedad) {
				String propiedadTipo = pro.getProperty("inmueble");
				if (opciones.getText().equalsIgnoreCase(propiedadTipo)) {
					a.setInmueble(opciones.getText());
					System.out.println("Tipo de inmueble Seleccionado: " + a.getInmueble());
					opciones.click();
				}
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		///////////

		WebElement ubicacion = driver.findElement(By.id("searchbox-home_ubicacion"));
		ubicacion.sendKeys(pro.getProperty("ubicacion"));
		WebElement buscar = driver.findElement(By.id("submitBtn"));
		buscar.click();

		return anuncios;
	}

	///////////////////////////////////////////////////////////////////////////////////////
	@Override
	void hacerConsulta(BusquedaTipo busquedaTipo, String estado) {
		List<Anuncio> anuncios = null;
		anuncios = consulta();
	}

	///////////////////////////////////////////////////////////////////////////////////////
	@Override
	void extraerGuardarDatos() {

		List<Anuncio> anuncios = null;
		anuncios = extraerDatos();
	}

	///////////////////////////////////////////////////////////////////////////////////////

	private List<Anuncio> extraerDatos() {

		List<Anuncio> anuncios = new ArrayList<Anuncio>();
		WebElement siguiente = null;
		Anuncio a = new Anuncio();

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
					a.setDescripcion(descripccionPropiedad.getText());

					WebElement datoPropiedad = driver.findElement(By.className("btn-phone-text"));
					datoPropiedad.click();

					WebElement datoTelefono = driver.findElement(By.className("lead-phone"));
					a.setTelefono(datoTelefono.getText());

					System.out.println("Descripcción: " + idx + " " + a.getTelefono() + " " + a.getDescripcion());

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
		return anuncios;
	}

}
