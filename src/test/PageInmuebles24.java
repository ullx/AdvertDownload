package test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
					log.info("Opción Seleccionada: " + a.getTransaccion());
					opciones.click();
				}

				if (transaccionTipo.equalsIgnoreCase("renta") && opciones.getText().equalsIgnoreCase("Rentar")) {
					a.setTransaccion(opciones.getText());
					log.info("Opción Seleccionada: " + a.getTransaccion());
					opciones.click();
				}

			}
		}

		catch (Exception e) {
			log.error(e.getMessage(), e);
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
					log.info("Opción Seleccionada: " + a.getTransaccion());
					opciones.click();
				}

				else if (propiedadTipo.toLowerCase().contains("casa") && opciones.getText().equalsIgnoreCase("Casa")) {
					a.setTransaccion(opciones.getText());
					log.info("Opción Seleccionada: " + a.getTransaccion());
					opciones.click();

				}

				else if (propiedadTipo.toLowerCase().contains("terreno")
						&& opciones.getText().equalsIgnoreCase("Terreno / Lote")) {
					a.setTransaccion(opciones.getText());
					log.info("Opción Seleccionada: " + a.getTransaccion());
					opciones.click();
				}

				else if (propiedadTipo.toLowerCase().contains("oficina")
						&& opciones.getText().equalsIgnoreCase("Oficina")) {
					a.setTransaccion(opciones.getText());
					log.info("Opción Seleccionada: " + a.getTransaccion());
					opciones.click();

				}
			}
		}

		catch (Exception e) {
			log.error("Error en captura inmueble", e);
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
			log.error(e.getMessage(), e);
		}

	}
	///////////////////////////////////////////////////////////////////////////////////////

	@Override
	List<Anuncio> extraerGuardarDatos() {

		List<Anuncio> anuncios = null;
		anuncios = extraerDatos();
		log.info("Numero de anuncios en total descargados " + anuncios.size());
		return anuncios;
	}

	///////////////////////////////////////////////////////////////////////////////////////

	private List<Anuncio> extraerDatos() {

		WebElement siguiente = null;
		List<Anuncio> anuncios = new ArrayList<Anuncio>();
		try {
			WebElement precioMinimo = driver.findElement(By.id("preciomin"));
			precioMinimo.sendKeys(config.getProperty("precioMin"));
			WebElement precioMaximo = driver.findElement(By.id("preciomax"));
			precioMaximo.sendKeys(config.getProperty("precioMax"));
			WebElement precioBoton = driver.findElement(By.id("botonPrecio"));
			precioBoton.click();
		} catch (NoSuchElementException e) {
			log.info("No se ingresó rango de precios");
		}

		for (;;) {

			WebElement seleccionarAnuncios = driver.findElement(By.id("avisos-content"));
			List<WebElement> listaResultados = seleccionarAnuncios.findElements(By.className("aviso-desktop"));
			int results = listaResultados.size();
			log.info("NÃºmero de resultados: " + results);

			for (int idx = 0; idx < results; idx++) {

				listaResultados.get(idx).click();
				
				ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
			    driver.switchTo().window(tabs2.get(1));

				Anuncio data = getDataFromLink(driver);

				log.info("TÃ­tulo: " + idx + " " + data.getTitulo());

				log.info("Descripción: " + idx + " " + data.getDescripcion());

				log.info("Datos Principales: " + idx + " " + data.getDatos());

				log.info("TÃ©lefono: " + idx + " " + data.getTelefono());

				log.info("Precio: " + idx + " " + data.getPrecio());

				anuncios.add(data);

				try {

					WebElement regresar = driver.findElement(By.className("ticon-arrow-left"));
					regresar.click();
				} catch (NoSuchElementException e) {
					log.info("No se encontró link para regresar");
					break;
				}
				
				driver.close();
			    driver.switchTo().window(tabs2.get(0));

				seleccionarAnuncios = driver.findElement(By.id("avisos-content"));
				listaResultados = seleccionarAnuncios.findElements(By.className("aviso-desktop"));
				
			}

			try {
				siguiente = driver.findElement(By.className("ticon-next"));
				if (siguiente.isEnabled()) {
					siguiente.click();
				}

			} catch (WebDriverException e) {
				log.info("Se llegó al final de las paginas");
				break;
			}

			log.info("Terminando proceso");
		}
		log.info("Finished ");
		return anuncios;
	}

	private Anuncio getDataFromLink(WebDriver driver) {

		Anuncio a = new Anuncio();
		

		WebElement tituloPropiedad = driver.findElement(By.className("card-title"));
		WebElement titulo = tituloPropiedad.findElement(By.tagName("h1"));
		a.setTitulo(titulo.getText());

		WebElement descripccionPropiedad = driver.findElement(By.className("description-body"));
		a.setDescripcion(descripccionPropiedad.getText());

		try {

			List<WebElement> datosPrincipales = driver.findElements(By.cssSelector(".card.aviso-datos"));
			WebElement datoPrincipal = datosPrincipales.get(0);
			List<WebElement> listaDatos = datoPrincipal.findElements(By.tagName("li"));

			List<String> results = new ArrayList<>();
			StringBuilder datosPrincipales2 = new StringBuilder();
			for (int x = 0; x < listaDatos.size(); x++) {
				WebElement dato = listaDatos.get(x);
				WebElement datosAnuncioNombre = dato.findElement(By.className("nombre"));
				String datos = datosAnuncioNombre.getText();
				datosPrincipales2.append(datos.replaceAll(","," "));
			}
			
			//a.setDatos(sb.toString());
			a.setDescripcion(a.getDescripcion() + " " + datosPrincipales2.toString());
			WebElement precioPropiedad = driver.findElement(By.className("precios"));
			WebElement precioInmueble = precioPropiedad.findElement(By.tagName("strong"));
			a.setPrecio(precioInmueble.getText());

			WebElement botonTel = driver.findElement(By.className("btn-phone-text"));
			botonTel.click();

			try {
				incheCaptcha();

				Utils.retryingFind(driver, By.className("lead-phone"));
				WebElement telefono = driver.findElement(By.className("lead-phone"));
				a.setTelefono(telefono.getText());
			} catch (NoSuchElementException e) {

				Utils.retryingFind(driver, By.className("lead-phone"));
				WebElement telefono = driver.findElement(By.className("lead-phone"));
				a.setTelefono(telefono.getText());
			}

			WebElement cerrarVentana = driver.findElement(By.className("recomendado-lead-close"));
			cerrarVentana.click();

		} catch (NoSuchElementException e) {
			log.info("No se encontro tÃ©lefono o descripción", e);
		}

		return a;
	}

	private void incheCaptcha() {

		driver.switchTo().defaultContent();
		WebElement iframe = driver.findElement(By.cssSelector("div#recaptcha_div iframe"));
		if (iframe != null) {

			driver.switchTo().frame(iframe);
			playSound();
			JOptionPane.showConfirmDialog(null, " Ingresa Captcha para continuar:", "Inmuebles24",
					JOptionPane.WARNING_MESSAGE);

			Utils.retryingFind(driver, By.className("lead-phone"));

			WebDriverWait wait = new WebDriverWait(driver, 1000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("lead-phone")));
		}
	}

	private void playSound() {
		try {
			URL url = ClassLoader.getSystemResource("censor-beep-2.wav");
			Clip clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(url);
			clip.open(ais);
			clip.loop(10);
		} catch (Exception e) {
			log.info("Error al lanzar el mensaje para resolver captcha");
		}

	}

}
