package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class PageInformador extends AbstractAnunciosFlow {

	String baseURL = "http://aviso.informador.com.mx/";
	String estado = null;
	Properties pro = null;

	@Override
	String getURL() {
		return baseURL;
	}

	///////////////////////////////////////////////////////////////////////////////

	public PageInformador(Properties config) {
		super(config);
	}

	///////////////////////////////////////////////////////////////////////////////

	@Override
	void hacerConsulta() {

		Anuncio a = new Anuncio();

		WebElement bienesRaices = driver.findElement(By.className("type-house"));
		bienesRaices.click();

		WebElement DropdownRenta = driver.findElement(By.id("drop_tran"));
		WebElement div1 = DropdownRenta.findElement(By.className("dropdown"));
		div1.click();

		try {
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			WebElement div2 = div1.findElement(By.tagName("div"));
			List<WebElement> transaccionesTipo = div2.findElements(By.tagName("li"));

			String transaccionTipo = config.getProperty("transaccion");

			for (WebElement opciones : transaccionesTipo) {

				Thread.sleep(1000);
				if (transaccionTipo.equalsIgnoreCase("venta")
						&& opciones.getText().equalsIgnoreCase("Venta / Traspaso")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opción Seleccionada: " + a.getTransaccion());
					Thread.sleep(1000);
					opciones.click();
				}

				else if (transaccionTipo.equalsIgnoreCase("renta") && opciones.getText().equalsIgnoreCase("Renta")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opción Seleccionada: " + a.getTransaccion());
					Thread.sleep(1000);
					opciones.click();
				}

			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		WebElement DropdownInmuebles = driver.findElement(By.id("drop_inmu"));
		WebElement div3 = DropdownInmuebles.findElement(By.className("dropdown"));
		div3.click();

		///////////////////////////

		try {

			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			WebElement div4 = div3.findElement(By.tagName("div"));
			List<WebElement> liListaBodega = div4.findElements(By.tagName("li"));
			String propiedadTipo = config.getProperty("inmueble");

			for (WebElement opciones : liListaBodega) {
				Thread.sleep(1000);
				if (propiedadTipo.toLowerCase().contains("bodega") && opciones.getText().equalsIgnoreCase("Bodegas")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opción Seleccionada: " + a.getTransaccion());
					Thread.sleep(1000);
					opciones.click();
				}

				else if (propiedadTipo.toLowerCase().contains("casa") && opciones.getText().equalsIgnoreCase("Casa")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opción Seleccionada: " + a.getTransaccion());
					Thread.sleep(1000);
					opciones.click();

				}

				else if (propiedadTipo.toLowerCase().contains("terreno")
						&& opciones.getText().equalsIgnoreCase("Terreno")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opción Seleccionada: " + a.getTransaccion());
					Thread.sleep(1000);
					opciones.click();
				}

				else if (propiedadTipo.toLowerCase().contains("oficina")
						&& opciones.getText().equalsIgnoreCase("Oficina")) {
					a.setTransaccion(opciones.getText());
					System.out.println("Opción Seleccionada: " + a.getTransaccion());
					Thread.sleep(1000);
					opciones.click();

				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		///////////////////////////

		try {

			String ubicacionBusqueda = config.getProperty("ubicacion");
			if (ubicacionBusqueda.equalsIgnoreCase("zona metropolitana")) {
				WebElement zonaMetro = driver.findElement(By.id("quick-search"));
				System.out.println("Opción Seleccionada: " + config.getProperty("ubicacion"));
				zonaMetro.click();
			}

			else if (ubicacionBusqueda.equalsIgnoreCase("zapopan")) {
				WebElement zapopan = driver.findElement(By.id("quick-searchZap"));
				System.out.println("Opción Seleccionada: " + config.getProperty("ubicacion"));
				zapopan.click();
			}

			else if (ubicacionBusqueda.equalsIgnoreCase("guadalajara")) {

				WebElement gdl = driver.findElement(By.id("quick-searchGdl"));
				System.out.println("Opción Seleccionada: " + config.getProperty("ubicacion"));
				gdl.click();
			}

			else if (ubicacionBusqueda.equalsIgnoreCase("tlaquepaque")) {

				WebElement tlaque = driver.findElement(By.id("quick-searchTlaq"));
				System.out.println("Opción Seleccionada: " + config.getProperty("ubicacion"));

				tlaque.click();
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	///////////////////////////////////////////////////////////////////////////////

	@Override
	List<Anuncio> extraerGuardarDatos() {

		List<Anuncio> anuncios = null;
		anuncios = extraerDatos();
		System.out.println("Numero de anuncios en total descargados " + anuncios.size());
		return anuncios;

	}

	/////////////////////////////////////////////////////////////////////////////

	private List<Anuncio> extraerDatos() {

		WebElement siguiente = null;
		List<Anuncio> anuncios = new ArrayList<Anuncio>();

		for (;;) {

			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			WebElement municipio = driver.findElement(By.id("items"));
			List<WebElement> listaResultados = municipio.findElements(By.tagName("li"));
			int results = listaResultados.size();
			System.out.println("Número de resultados: " + results);

			for (int idx = 0; idx < results; idx++) {

				listaResultados.get(idx).click();
				Anuncio data = getDataFromLink(driver);

				System.out.println("Descripción: " + idx + " " + data.getDescripcion());
				System.out.println("Teléfono: " + idx + " " + data.getTelefono());
				System.out.println("Precio: " + idx + " " + data.getPrecio());

				anuncios.add(data);

				try {
					WebElement regresar = driver.findElement(By.linkText("< Regresar"));
					regresar.click();
				} catch (NoSuchElementException e) {
					System.out.println("No se encontro link regresar");
					break;
				}

				municipio = driver.findElement(By.id("items"));
				listaResultados = municipio.findElements(By.tagName("li"));

			}
			try {
				siguiente = driver.findElement(By.linkText("Siguiente"));
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

		WebElement desc = driver.findElement(By.className("detail-description"));
		a.setDescripcion(desc.getText());

		try {

			WebElement precioInmueble = driver.findElement(By.className("detail-view"));
			WebElement precio = precioInmueble.findElement(By.tagName("h2"));
			String costo = precio.getText();
			a.setPrecio(costo);

		} catch (Exception e) {
			e.printStackTrace();

			System.out.println("El anuncio no tiene Precio");
		}

		try {

			WebElement telefono = driver.findElement(By.cssSelector("a[href*='tel']"));
			String tel = telefono.getText();
			a.setTelefono(tel);

		} catch (Exception e) {
			e.printStackTrace();

			System.out.println("El anuncio no tiene Télefono");
		}

		return a;
	}
}
