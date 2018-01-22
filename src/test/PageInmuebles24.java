package test;

import java.util.ArrayList;
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
	Properties pro = null;

	@Override
	String getURL() {
		return baseURL;
	}

	///////////////////////////////////////////////////////////////////////////////////////

	public PageInmuebles24(Properties config) {
		super(config);
	}

	///////////////////////////////////////////////////////////////////////////////////////

	private List<Anuncio> consulta() {

		List<Anuncio> anuncios = new ArrayList<Anuncio>();
		Anuncio a = new Anuncio();
		WebElement transacciones = driver.findElement(By.id("vertical-operation-menu"));
		try {

			List<WebElement> transaccionesTipo = transacciones.findElements(By.tagName("a"));
			String transaccionTipo = config.getProperty("transaccion");

			for (WebElement opciones : transaccionesTipo) {
				if (transaccionTipo.equalsIgnoreCase("venta") && opciones.getText().equalsIgnoreCase("Comprar")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opción Seleccionada: " + a.getTransaccion());
					opciones.click();
				}

				if (transaccionTipo.equalsIgnoreCase("renta") && opciones.getText().equalsIgnoreCase("Rentar")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opción Seleccionada: " + a.getTransaccion());
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

			String propiedadTipo = config.getProperty("inmueble");
			for (WebElement opciones : seleccionarPropiedad) {
				if (propiedadTipo.equalsIgnoreCase("bodegas") && opciones.getText().equalsIgnoreCase("Bodegas")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opción Seleccionada: " + a.getTransaccion());
					opciones.click();
				}

				else if (propiedadTipo.equalsIgnoreCase("casas") && opciones.getText().equalsIgnoreCase("Casa")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opción Seleccionada: " + a.getTransaccion());
					opciones.click();

				}

				else if (propiedadTipo.equalsIgnoreCase("terrenos")
						&& opciones.getText().equalsIgnoreCase("Terreno / Lote")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opción Seleccionada: " + a.getTransaccion());
					opciones.click();
				}

				else if (propiedadTipo.equalsIgnoreCase("oficinas") && opciones.getText().equalsIgnoreCase("Oficina")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opción Seleccionada: " + a.getTransaccion());
					opciones.click();

				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		///////////

		try {

			if (config.getProperty("ubicacion").equalsIgnoreCase("Nacional")) {

				WebElement buscar = driver.findElement(By.id("submitBtn"));
				buscar.click();

			}

			else {
				WebElement ubicacion = driver.findElement(By.id("searchbox-home_ubicacion"));
				ubicacion.sendKeys(config.getProperty("ubicacion"));

				WebElement estado = driver.findElement(By.className("tt-dataset-zonas"));
				List<WebElement> resultadosUbicacion = estado.findElements(By.tagName("div"));
				int size = resultadosUbicacion.size();

				if (size > 0) {
					WebElement seleccionarPrimero = resultadosUbicacion.get(0);
					seleccionarPrimero.click();
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return anuncios;
	}

	@Override
	void hacerConsulta() {
		List<Anuncio> anuncios = null;
		anuncios = consulta();
	}

	@Override
	List<Anuncio> extraerGuardarDatos() {

		List<Anuncio> anuncios = null;
		anuncios = extraerDatos();
		return anuncios;
	}

	///////////////////////////////////////////////////////////////////////////////////////

	private List<Anuncio> extraerDatos() {

		List<Anuncio> anuncios = new ArrayList<Anuncio>();
		WebElement siguiente = null;
		Anuncio a = new Anuncio();

		for (;;) {

			try {
				WebElement precioMinimo = driver.findElement(By.id("preciomin"));
				precioMinimo.sendKeys(config.getProperty("precioMin"));
				WebElement precioMaximo = driver.findElement(By.id("preciomax"));
				precioMaximo.sendKeys(config.getProperty("precioMax"));
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


					Anuncio data = getDataFromLink(driver);

					System.out.println("Descripción: " + idx + " " + data.getDescripcion());

					System.out.println("Télefono: " + idx + " " + data.getTelefono());

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

	private Anuncio getDataFromLink(WebDriver driver) {
		Anuncio a = new Anuncio();

		try {

			WebElement descripccionPropiedad = driver.findElement(By.className("description-body"));
			a.setDescripcion(descripccionPropiedad.getText());

			WebElement botonTel = driver.findElement(By.className("btn-phone-text"));
			botonTel.click();
			
			WebElement datoTelefono = driver.findElement(By.className("lead-phone"));
			a.setTelefono(datoTelefono.getText());
			
			WebElement cerrarVentana = driver.findElement(By.className("fa-times"));
			cerrarVentana.click();

		} catch (NoSuchElementException e) {
			System.out.println("No se encontro télefono o descripción");
		}

		return a;
	}

}
