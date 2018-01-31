package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

	@Override
	void hacerConsulta() {

		Anuncio a = new Anuncio();
		WebElement transacciones = driver.findElement(By.id("vertical-operation-menu"));
		try {

			List<WebElement> transaccionesTipo = transacciones.findElements(By.tagName("a"));
			String transaccionTipo = config.getProperty("transaccion");

			for (WebElement opciones : transaccionesTipo) {
				if (transaccionTipo.equalsIgnoreCase("venta") && opciones.getText().equalsIgnoreCase("Comprar")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opci�n Seleccionada: " + a.getTransaccion());
					opciones.click();
				}

				if (transaccionTipo.equalsIgnoreCase("renta") && opciones.getText().equalsIgnoreCase("Rentar")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opci�n Seleccionada: " + a.getTransaccion());
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
				if (propiedadTipo.toLowerCase().contains("bodega") && opciones.getText().equalsIgnoreCase("Bodegas")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opci�n Seleccionada: " + a.getTransaccion());
					opciones.click();
				}

				else if (propiedadTipo.toLowerCase().contains("casa") && opciones.getText().equalsIgnoreCase("Casa")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opci�n Seleccionada: " + a.getTransaccion());
					opciones.click();

				}

				else if (propiedadTipo.toLowerCase().contains("terreno")
						&& opciones.getText().equalsIgnoreCase("Terreno / Lote")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opci�n Seleccionada: " + a.getTransaccion());
					opciones.click();
				}

				else if (propiedadTipo.toLowerCase().contains("oficina")
						&& opciones.getText().equalsIgnoreCase("Oficina")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opci�n Seleccionada: " + a.getTransaccion());
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

	}
	///////////////////////////////////////////////////////////////////////////////////////

	@Override
	List<Anuncio> extraerGuardarDatos() {

		List<Anuncio> anuncios = null;
		anuncios = extraerDatos();
		System.out.println("Numero de anuncios en total descargados " + anuncios.size());
		return anuncios;
	}

	///////////////////////////////////////////////////////////////////////////////////////

	private List<Anuncio> extraerDatos() {

		WebElement siguiente = null;
		List<Anuncio> anuncios = new ArrayList<Anuncio>();

		for (;;) {

			try {
				WebElement precioMinimo = driver.findElement(By.id("preciomin"));
				precioMinimo.sendKeys(config.getProperty("precioMin"));
				WebElement precioMaximo = driver.findElement(By.id("preciomax"));
				precioMaximo.sendKeys(config.getProperty("precioMax"));
				WebElement precioBoton = driver.findElement(By.id("botonPrecio"));
				precioBoton.click();
			} catch (NoSuchElementException e) {
				System.out.println("No se ingres� rango de precios");
			}

			WebElement seleccionarAnuncios = driver.findElement(By.id("avisos-content"));
			List<WebElement> listaResultados = seleccionarAnuncios.findElements(By.className("aviso-desktop"));
			int results = listaResultados.size();
			System.out.println("N�mero de resultados: " + results);

			for (int idx = 0; idx < results; idx++) {

				listaResultados.get(idx).click();

				Anuncio data = getDataFromLink(driver);

				System.out.println("Descripci�n: " + idx + " " + data.getDescripcion());

				System.out.println("T�lefono: " + idx + " " + data.getTelefono());

				System.out.println("Precio: " + idx + " " + data.getPrecio());

				anuncios.add(data);

				try {

					WebElement regresar = driver.findElement(By.className("ticon-arrow-left"));
					regresar.click();
				} catch (NoSuchElementException e) {
					System.out.println("No se encontr� link para regresar");
					break;
				}

				seleccionarAnuncios = driver.findElement(By.id("avisos-content"));
				listaResultados = seleccionarAnuncios.findElements(By.className("aviso-desktop"));

			}

			try {
				siguiente = driver.findElement(By.className("ticon-next"));
				if (siguiente.isEnabled()) {
					siguiente.click();
				}

			} catch (WebDriverException e) {
				System.out.println("Se lleg� al final de las paginas");
				break;
			}

			System.out.println("Terminando proceso");
		}
		System.out.println("Finished ");
		return anuncios;
	}

	private Anuncio getDataFromLink(WebDriver driver) {

		Anuncio a = new Anuncio();
		String datoTelefono = "";

		try {

			WebElement descripccionPropiedad = driver.findElement(By.className("description-body"));
			a.setDescripcion(descripccionPropiedad.getText());

			WebElement precioPropiedad = driver.findElement(By.className("precios"));
			WebElement precioInmueble = precioPropiedad.findElement(By.tagName("strong"));
			a.setPrecio(precioInmueble.getText());

			WebElement botonTel = driver.findElement(By.className("btn-phone-text"));
			botonTel.click();

			try {
				
				driver.switchTo().defaultContent();
				WebElement iframe = driver.findElement(By.cssSelector("div#recaptcha_div iframe"));
				driver.switchTo().frame(iframe);
				JOptionPane.showConfirmDialog(null, " Ingresa Captcha para continuar:");
				WebDriverWait wait = new WebDriverWait(driver, 2000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("lead-phone")));
				
				WebElement Telefono = driver.findElement(By.className("lead-phone"));
				datoTelefono = Telefono.getText();

			}

			catch (NoSuchElementException e) {
			}

			WebElement cerrarVentana = driver.findElement(By.className("fa-times"));
			cerrarVentana.click();

		} catch (NoSuchElementException e) {
			System.out.println("No se encontro t�lefono o descripci�n");
		}

		a.setTelefono(datoTelefono);
		return a;
	}

	// private void incheCaptcha(WebDriver driver) {
	// driver.switchTo().defaultContent();
	// WebElement iframe = driver.findElement(By.cssSelector("div#recaptcha_div
	// iframe"));
	// driver.switchTo().frame(iframe);
	// JOptionPane.showConfirmDialog(null, " Ingresa Captcha para continuar:");
	// WebDriverWait wait = new WebDriverWait(driver, 2000);
	// wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("lead-phone")));
	// }

}
