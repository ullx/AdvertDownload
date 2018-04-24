package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageVivanuncios extends AbstractAnunciosFlow {

	public PageVivanuncios(Properties config) {
		super(config);
	}

	String baseURL = "https://www.vivanuncios.com.mx";
	String estado = null;
	
	@Override
	String getURL() {
		return baseURL;
	}

	
	@Override
	void hacerConsulta() {
		this.estado = config.getProperty("ubicacion");
		seleccionarEstado(estado);
		
		driver.findElement(By.id("js-browse-item-text")).click();
		String transaccion = config.getProperty("transaccion");
		
		WebDriverWait wait = new WebDriverWait(driver, 2000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("browse-subcat1")));
		
		driver.findElement(By.id("browse-subcat1")).click();
		WebElement subCategorias = driver.findElement(By.id("js-cat-dropdown"));
		
		switch (inmuebleTipo) {
		case BODEGA:
			subCategorias.findElement(By.cssSelector("a[href*='bodegas']")).click();
			break;
		case CASA:
			if (transaccion.equalsIgnoreCase("renta"))
				subCategorias.findElement(By.cssSelector("a[href*='renta-inmuebles']")).click();
			else
				subCategorias.findElement(By.cssSelector("a[href*='venta-inmuebles']")).click();
			break;
		case OFICINA:
			subCategorias.findElement(By.cssSelector("a[href*='comerciales']")).click();
			break;
		case TERRENO:
			subCategorias.findElement(By.cssSelector("a[href*='terrenos']")).click();
			break;
		default:
			subCategorias.findElement(By.cssSelector("a[href*='bodegas']")).click();
			break;
		}
		
		agregarFiltros(inmuebleTipo);
		
		//Agregar filtros
//		driver.findElement(By.xpath("//*[@id=\"searchChips\"]/div[2]/div/span")).click();
		
		
		//cuando se selecciona la categoria ya hizo la busqueda
//		driver.findElement(By.className("search-button-wrap")).findElement(By.tagName("button")).click();
	}
	
	private void agregarFiltros(BusquedaTipo tipoInmueble) {
		
		WebElement agregarFiltrosBtn = null;
		
		try {
			 agregarFiltrosBtn = driver.findElement(By.xpath("//*[@id=\"searchChips\"]/div[2]/div/span"));
		} catch (NoSuchElementException e) {
		}
		 
		
		if(agregarFiltrosBtn != null && agregarFiltrosBtn.isDisplayed()) {
			agregarFiltrosOld(tipoInmueble);
		}else {
			String transaccion = config.getProperty("transaccion");
			WebElement tabsContainer = driver.findElement(By.id("tab-container"));
			
			
			if(tipoInmueble == BusquedaTipo.BODEGA) {
				// por el momento este [#tab1] seria el tab de seleccion de venta o renta, pero podria
				// cambiar en un futuro supongo
				 tabsContainer.findElement(By.cssSelector("a[href*='#tab1']")).click();
				 
				if(transaccion.equals("venta")) {
					driver.findElement(By.xpath("//*[@id=\"tab1\"]/div/div/div[1]/div/label/span[1]")).click();
				}else {
					driver.findElement(By.xpath("//*[@id=\"tab1\"]/div/div/div[2]/div/label/span[1]")).click();
				}
			} else if(tipoInmueble == BusquedaTipo.OFICINA) {
				setFiltroOficinaOld();
			} 
		}
		log.debug("tipoInmueble " + tipoInmueble);
	}
	
	private void setFiltroOficinaOld() {
		By dropDownList = By.id("L3Category");
		retryingFindClick(dropDownList);
		WebElement select = driver.findElement(By.id("L3Category"));
		select.click();
		List<WebElement> options = select.findElements(By.tagName("option"));
		for(WebElement opt : options) {
			String optText = opt.getText().toLowerCase();
			if(optText.contains("oficina") || optText.contains("comerciales") ) { //propiedades comerciales
				opt.click();
				break;
			}
		}
		uglyWait(2000);
		
		WebDriverWait wait4 = new WebDriverWait(driver, 2000);
		wait4.until(ExpectedConditions.visibilityOfElementLocated(By.className("filterContainer")));
	}
	
	private void setFiltroCasaOld() {
		
	}
	
	
	private void agregarFiltrosOld(BusquedaTipo tipoInmueble) {
		System.out.println("AgregarFiltros old");
		String transaccion = config.getProperty("transaccion");
		driver.findElement(By.xpath("//*[@id=\"searchChips\"]/div[2]/div/span")).click();
		WebDriverWait wait2 = new WebDriverWait(driver, 2000);
		wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("filterScreenTakeOver")));
		
		log.debug("tipoInmueble " + tipoInmueble);
		if(tipoInmueble == BusquedaTipo.BODEGA) {
			if(transaccion.equalsIgnoreCase("venta")) {
				log.debug("Seleccionando venta");
				By by = By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[1]/div/label/div/div"); // By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[1]/div/label/input");  //  "//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[1]/div/label/div/div");
				log.debug("slected {}",retryingFindClick(by));
//				driver.findElement(by).click();
			}else {
				log.debug("Seleccionando renta");
				By by = By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[2]/div/label/div/div");
				log.debug("slected {}",retryingFindClick(by) );
//				driver.findElement(by).click();
			}
		}else if(tipoInmueble == BusquedaTipo.OFICINA) {
			setFiltroOficinaOld();
		} else if(tipoInmueble == BusquedaTipo.CASA) {
			By casaSelector = By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[1]/div/label/div/div");
			WebDriverWait wait = new WebDriverWait(driver, 2000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(casaSelector));
			retryingFindClick(casaSelector);
		}
		
		if (tipoInmueble != BusquedaTipo.TERRENO && tipoInmueble != BusquedaTipo.CASA && tipoInmueble != BusquedaTipo.BODEGA) {

			if (transaccion.equalsIgnoreCase("venta")) {
				By by = By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[1]/div/label/div/div");
				WebDriverWait wait = new WebDriverWait(driver, 2000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(by));

				retryingFindClick(by);
				// driver.findElement(by).click();
			} else {
				By by = By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[2]/div/label/div/div");
				WebDriverWait wait = new WebDriverWait(driver, 2000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(by));
				retryingFindClick(by);
				// driver.findElement(by).click();
			}
		}
		
		String precioMin = config.getProperty("precioMin");
		String precioMax = config.getProperty("precioMax");
		log.info("precio min " + precioMin);
		log.info("precio max " + precioMax);
		
		if(precioMin != null && precioMax != null) {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				// TODO: handle exception
			}
			driver.findElement(By.id("attr.Price.amountMin")).sendKeys(precioMin);
			driver.findElement(By.id("attr.Price.amountMax")).sendKeys(precioMax);
		}
		
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		//Click en aplicar filtros
//		driver.findElement(By.xpath("//*[@id=\"searchFilter\"]/div[3]/div/div")).click();
		driver.findElement(By.id("filterSendButton")).click();
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private boolean retryingFindClick(By by) {
        boolean result = false;
        int attempts = 0;
        while(attempts < 2) {
            try {
                driver.findElement(by).click();
                result = true;
                break;
            } catch(StaleElementReferenceException e) {
            	log.debug(e);
            } catch(WebDriverException e) {
            	log.debug(e);
            }
            attempts++;
        }
        return result;
}
	
	private void seleccionarEstado(String estado) {
		// si no se selecciona localidad toma para todo mexico
		if(estado.isEmpty() || estado.equalsIgnoreCase("nacional") || estado.equalsIgnoreCase("mexico")) {
			estado = "méxico";
			driver.findElement(By.id("hero-form")).submit();
			return;
		}
		
		estado = estado.toLowerCase();
		
		//Clickeando "ver mas" link para tener todos los estados desplegados
		driver.findElement(By.className("view-more")).click();
//		String vocalesAcento = "[áéíóúÁÉÍÓÚ]";
//		String cleanEstado = estado.replace(vocalesAcento, "");
		
		List<WebElement> estadosLinks = driver.findElement(By.id("quickLinks")).findElements(By.className("quick-link-item"));

		for (WebElement estadoLink : estadosLinks) {
			String currentEstadoLink = estadoLink.getText().toLowerCase();
			System.out.println("estadosLink " + currentEstadoLink + " contains " + estado + "?");
			currentEstadoLink = currentEstadoLink.replaceAll(",", " ");
			if (currentEstadoLink.contains(estado)) {
				estadoLink.findElement(By.tagName("a")).click();
				break;
			}
		}
	}

	@Override
	List<Anuncio> extraerGuardarDatos() {
		
		List<Anuncio> anuncios = null;
		
//		if(estado.equals(("jalisco"))) {
//			anuncios = getDatosBusquedaJalisco();
//		}else {
			anuncios = getDatosBusqueda();
//		}
		log.info("Numero de anuncios en total descargados " + anuncios.size());
		return anuncios;
	}
	
	private List<Anuncio> getDatosBusqueda() {
		List<Anuncio> anuncios = new ArrayList<Anuncio>();
		int countPagina = 1;
		
		do {
			List<WebElement> anunciosTitles = driver.findElement(By.id("tileRedesign")).findElements(By.className("tile-panel"));
			int results = anunciosTitles.size();
			
			log.info("Pagina: " + countPagina + " Número de resultados" + results);
			
			for (int idx = 0; idx < results; idx++) {

				WebElement elementToClick = anunciosTitles.get(idx);
				
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("arguments[0].scrollIntoView()", elementToClick); 
				log.info("clicking " + elementToClick.getText());
				
				String linkToFollow = elementToClick.findElement(By.tagName("meta")).getAttribute("content");
				driver.get(linkToFollow);
//				elementToClick.click();
				
				Anuncio data = getDataFromLink(driver);

				log.info("Descripción: " + idx + " " + data.getDescripcion());
				anuncios.add(data);

				try {
//					WebElement regresar = driver.findElement(By.className("header-back"));
//					regresar.click();
					driver.navigate().back();
				} catch (NoSuchElementException e) {
					log.info("No se encontro link regresar");
					break;
				}

				anunciosTitles = driver.findElement(By.id("tileRedesign")).findElements(By.className("tile-panel"));
			}
			countPagina++;
		}while (morePagesExists());
		return anuncios;
	}
	
	private List<Anuncio> getDatosBusquedaJalisco() {
		
		List<Anuncio> anuncios = new ArrayList<Anuncio>();

		do {

			List<WebElement> anunciosTitles = driver.findElement(By.className("results")).findElements(By.className("title"));
			
			int results = anunciosTitles.size();
			log.info("Número de resultados: " + results);
			
			for (int idx = 0; idx < results; idx++) {

//				log.info(anunciosTitles.get(idx).getText());
				WebElement el = anunciosTitles.get(idx).findElement(By.tagName("a"));
				JavascriptExecutor jse = (JavascriptExecutor)driver;

				jse.executeScript("arguments[0].scrollIntoView()", el); 
				el.click();
				
				Anuncio data = getDataFromLink(driver);

//				log.info("Contacto: " + idx + " " + data.getNombreAutor());
				log.info("Descripción: " + idx + " " + data.getDescripcion());
//				log.info("Teléfono: " + idx + " " + data.getTel());
				anuncios.add(data);

				try {
					WebElement regresar = driver.findElement(By.className("header-back"));
					regresar.click();
				} catch (NoSuchElementException e) {
					log.info("No se encontro link regresar");
					break;
				}

				anunciosTitles = driver.findElement(By.className("results")).findElements(By.className("title"));
			}
		} while (morePageExistsJalisco());
		
		return anuncios;
	}
	
	private boolean morePagesExists() {
		try {
			WebElement deskPagination = driver.findElement(By.className("desktop-pagination"));
			Actions actions = new Actions(driver);
			actions.moveToElement(deskPagination.findElement(By.className("icon-right-arrow"))).click().perform();
			
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	private boolean morePageExistsJalisco() {
		try {
			driver.findElement(By.className("next")).click();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private Anuncio getDataFromLink(WebDriver driver) {
		Anuncio a = new Anuncio();
		String tel = "";
		try {
			WebElement desc = driver.findElement(By.className("ad-details-container"));
			a.setDescripcion(desc.getText());
		} catch (Exception e) {
			log.debug("Ex al obtener detalles " + e.getMessage());
		}
		
		try {
			a.setPrecio(driver.findElement(By.xpath("//*[@id=\"viewPage\"]/div[4]/div/div[1]/div[2]/h3/span/span")).getText());
			log.info("Precio " + a.getPrecio());
		} catch (Exception e) {
			log.debug("Ex obteniendo precio " + e.getMessage());
		}
		
		try {
			driver.findElement(By.className("show-phone")).click();
			tel = driver.findElement(By.className("real-phone")).findElement(By.tagName("a")).getText();

		} catch (NoSuchElementException e) {
			log.debug("Ex al obtener telefono " + e.getMessage());
		}

		a.setTelefono(tel);
		return a;
	}
	
	private void uglyWait(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
}
