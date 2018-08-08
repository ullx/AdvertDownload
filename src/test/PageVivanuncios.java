package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageVivanuncios extends AbstractAnunciosFlow {

	String transaccion = config.getProperty("transaccion");

	
	/**
	 * Idea: Se tienen que ir escribiendo los anuncios conforme se vayan obteniendo
	 * cuando menos, deberían guardarse los anuncios de cada pagina, tambien 
	 * podría ponerse al final de los registros de anuncios la pagina a la que pertenecen,
	 * así teóricamente el bot podría empezar a partir de esa pagina 
	 */
	
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
		
		WebElement btnCat = null;
		try {
			btnCat = driver.findElement(By.id("js-browse-item-text"));
		} catch (NoSuchElementException e) {
		}
		
		selectTipoInmuebleEnTabsNuevos();
		
		// si busca casas entonces se tiene que seleccionar "tipo" casa la siguiente
		// ventana
		if(inmuebleTipo == BusquedaTipo.CASA) {
			log.debug("seleccionando tipo casa");
			getTabToClick("tipo").click();
			List<WebElement> submitList  = driver.findElements(By.name("attr.PropertyType")); //Si hay 2 botones se pone este nombre de atributo
			
			log.debug("Lista de submits " + submitList.size());
			if(submitList.size() == 0) { 
				submitList = driver.findElements(By.name("attr.DwellingType")); //Si hay 4 botones se pone este nombre de atributo
			}
			clickSubmitButton(submitList, "house");
		}
		
		agregarFiltros(inmuebleTipo);
	}
	
	private void selectTipoInmuebleEnTabsNuevos() {
//		WebElement tabContainer = driver.findElement(By.id("tab-container"));
//		tabContainer.findElement(By.cssSelector("a[href*='#category']")).click();
		log.debug("Clicking on categoria tab");
		getTabToClick("categoría").click();

		String stringToLookFor = null;

		switch (inmuebleTipo) {
		case BODEGA:
			stringToLookFor = "bodegas";
			break;
		case CASA:
			stringToLookFor = transaccion.equalsIgnoreCase("renta") ? "en renta" : "en venta"; // "inmuebles en renta" o "inmuebles en venta"
			break;
		case OFICINA:
			stringToLookFor = "oficinas";
			break;
		case TERRENO:
			stringToLookFor = "terrenos";
			break;
		default:
			stringToLookFor = "bodegas";
			break;
		}

		System.out.println("stringtolookfor " + stringToLookFor);

		List<WebElement> allCats = driver.findElements(By.className("cat-link-item"));
		
		List<WebElement> allCatsl2 = driver.findElements(By.className("cat-l2-elements"));
		
		log.debug("All categories cat-link-item " + allCats.size());
		boolean found = false;

		// Cuando se busca en todo mexico, las categorias de bienes raices ya se
		// muestran solo en caso de que no se muestre deberia entrar a este loop 
		// para buscar "bienes raices" y clickearla
		if (allCatsl2.size() > 0 && checkIfAnyoneIsDisplayed(allCatsl2) == false) {
			for (int i = 0; i < allCats.size() && found == false; i++) {
				WebElement cat = allCats.get(i);
				if (cat.getText().toLowerCase().contains("bienes raíces")) {
					cat.click();
					uglyWait(1000);
					found = true;
				}
			}
		}

		//busca dentro de las categorias nivel 2 que salen al clickear en una categoria padre
		allCatsl2 = driver.findElements(By.className("cat-l2-elements"));
		for (WebElement catl2 : allCatsl2) {
			if (catl2.isDisplayed()) {
				System.out.println("Scanning2 " + catl2.getText());
			}
			if (catl2.isDisplayed() && catl2.getText().toLowerCase().contains(stringToLookFor)) {
				catl2.findElement(By.className("cat-link-item")).click();
				break;
			}
		}
	}
	
	private boolean checkIfAnyoneIsDisplayed(List<WebElement> list) {
		for(WebElement el : list) {
			if(el.isDisplayed()) {
				return true;
			}
		}
		return false;
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
			
			if(inmuebleTipo != BusquedaTipo.CASA) {
				setFiltroTransaccion();
			}
			setFiltroPrecio();
		}
		log.debug("tipoInmueble " + tipoInmueble);
	}
	
	private void setFiltroOficinaOld() {
		By dropDownList = By.id("L3Category");
		Utils.retryingFindClick(driver,dropDownList);
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
	
	private void setFiltroPrecio() {
		String precioMin = config.getProperty("precioMin");
		String precioMax = config.getProperty("precioMax");
		
		String minPrice = String.format("document.getElementById('%s').setAttribute('value','%s')", "attr.Price.amountMin", precioMin);
		String maxPrice = String.format("document.getElementById('%s').setAttribute('value','%s')", "attr.Price.amountMax",precioMax);
		((JavascriptExecutor) driver).executeScript(minPrice);
		((JavascriptExecutor) driver).executeScript(maxPrice);
		((JavascriptExecutor) driver).executeScript(getElementById("filterSendButton").concat(".click()")); //filterSendButton
	}
	
	private void setFiltroTransaccion() {
		
		log.debug("setting filtro transaccion");
		String submitToClick = null;
		if(transaccion.equalsIgnoreCase("venta")) {
			WebElement tab = getTabToClick("vender");
			submitToClick = "forsale";
			log.debug("selecting tab venta");
			tab.click();
		}else {
			WebElement tab = getTabToClick("rentar");
			submitToClick = "forrent";
			log.debug("selecting tab renta");
			tab.click();
		}
		
		List<WebElement> submits = driver.findElements(By.name("attr.PropertySaleRentType"));
		clickSubmitButton(submits, submitToClick);
	}
	
	private void clickSubmitButton(List<WebElement> submitElementsByName, String identificatorStringToClick) {
		
		
		for(WebElement submit : submitElementsByName) {
			String submitValue =  submit.getAttribute("value");
			log.debug("lookingforsubmitButton " +  identificatorStringToClick + " current scanning: " + submitValue);
			
			if(submitValue.equalsIgnoreCase(identificatorStringToClick)) {
				WebElement parent = submit.findElement(By.xpath(".."));
				WebElement elToClick = parent.findElement(By.className("allowedValue"));
				try {
					// Cuando no existe boton disponible para clickear,
					// entonces continuaría sin haber especificado la seleccion pero seguirá
					// trayendo los anuncios del tipo de transacción que sí haya en la búsqueda
					elToClick.click();
				} catch (WebDriverException e) {
					log.warn("No se pudo clickear en " + identificatorStringToClick + " no se encontro o esta desabilitado ", e.getMessage());
				}
				return;
			}
		}
	}
	
	private WebElement getTabToClick(String nameOfTheTabToFind) {
		log.debug("in gettabtoclick " + nameOfTheTabToFind);
		
		WebElement tabs = driver.findElement(By.id("tab-container"));
		for(WebElement tab : tabs.findElements(By.className("tab"))) {
			if(tab.getText().toLowerCase().contains(nameOfTheTabToFind.toLowerCase())) {
				log.debug("returning tab " + tab.getText());
				return tab;
			}
		}
		return null;
	}
	
	/**
	 * Returns the construction of "document.getElementById(elementId)" as 
	 * to be used in a javascript executor call
	 * @param elementId
	 * @return
	 */
	private String getElementById(String elementId) {
		return String.format("document.getElementById('%s')", elementId);
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
				log.debug("slected {}",Utils.retryingFindClick(driver,by));
//				driver.findElement(by).click();
			}else {
				log.debug("Seleccionando renta");
				By by = By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[2]/div/label/div/div");
				log.debug("slected {}",Utils.retryingFindClick(driver,by) );
//				driver.findElement(by).click();
			}
		}else if(tipoInmueble == BusquedaTipo.OFICINA) {
			setFiltroOficinaOld();
		} else if(tipoInmueble == BusquedaTipo.CASA) {
			By casaSelector = By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[1]/div/label/div/div");
			WebDriverWait wait = new WebDriverWait(driver, 2000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(casaSelector));
			Utils.retryingFindClick(driver,casaSelector);
		}
		
		if (tipoInmueble != BusquedaTipo.TERRENO && tipoInmueble != BusquedaTipo.CASA && tipoInmueble != BusquedaTipo.BODEGA) {

			if (transaccion.equalsIgnoreCase("venta")) {
				By by = By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[1]/div/label/div/div");
				WebDriverWait wait = new WebDriverWait(driver, 2000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(by));

				Utils.retryingFindClick(driver,by);
				// driver.findElement(by).click();
			} else {
				By by = By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[2]/div/label/div/div");
				WebDriverWait wait = new WebDriverWait(driver, 2000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(by));
				Utils.retryingFindClick(driver,by);
				// driver.findElement(by).click();
			}
		}
		
		String precioMin = config.getProperty("precioMin");
		String precioMax = config.getProperty("precioMax");
		log.info("precio min " + precioMin);
		log.info("precio max " + precioMax);
		
		if(precioMin != null && precioMax != null) {
			uglyWait(2000);
			driver.findElement(By.id("attr.Price.amountMin")).sendKeys(precioMin);
			driver.findElement(By.id("attr.Price.amountMax")).sendKeys(precioMax);
		}
		
		uglyWait(2000);
		//Click en aplicar filtros
//		driver.findElement(By.xpath("//*[@id=\"searchFilter\"]/div[3]/div/div")).click();
		driver.findElement(By.id("filterSendButton")).click();
		uglyWait(5000);
	}
	
	private void seleccionarEstado(String estado) {
		// si no se selecciona localidad toma para todo mexico
		if(estado.isEmpty() || estado.equalsIgnoreCase("nacional") || estado.equalsIgnoreCase("mexico")) {
			estado = "méxico";
			driver.findElement(By.xpath("//*[@id=\"hero-form\"]/div")).click();
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
			currentEstadoLink = Utils.clearAcentos(currentEstadoLink);
			System.out.println("estadosLink " + currentEstadoLink + " contains " + estado + "?");
			currentEstadoLink = currentEstadoLink.replaceAll(",", " ");
			if (currentEstadoLink.contains(Utils.clearAcentos(estado))) {
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
			List<WebElement> anunciosTitles = driver.findElement(By.id("tileRedesign")).findElements(By.className("tile-item"));
			int results = anunciosTitles.size();
			
			log.info("Pagina: " + countPagina + " Número de resultados" + results);
			
			for (int idx = 0; idx < results; idx++) {

				WebElement elementToClick = anunciosTitles.get(idx);
				
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("arguments[0].scrollIntoView()", elementToClick);
				log.info("Num Pagina " + countPagina + " idx " + idx + " Logged anuncios " + anuncios.size());
				
				
				log.info(" clicking " + Utils.cleanToWriteCSV(elementToClick.getText()));
				
				String linkToFollow = elementToClick.findElement(By.tagName("meta")).getAttribute("content");
				driver.get(linkToFollow);
//				elementToClick.click();
				
				Anuncio data = getDataFromLink(driver);

				log.info("Descripción: " + idx + " " + Utils.cleanToWriteCSV(data.getDescripcion()));
				anuncios.add(data);

				try {
//					WebElement regresar = driver.findElement(By.className("header-back"));
//					regresar.click();
					driver.navigate().back();
				} catch (NoSuchElementException e) {
					log.info("No se encontro link regresar");
					break;
				}

				anunciosTitles = driver.findElement(By.id("tileRedesign")).findElements(By.className("tile-item"));
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
		a.setDescripcion(getDescripcion());
		a.setPrecio(getPrecio());
		a.setTelefono(getTelefono());
		return a;
	}
	
	private String getPrecio() {
		String pre = "No se pudo obtener precio";
		try {
			pre = driver.findElement(By.xpath("//*[@id=\"viewPage\"]/div[4]/div/div[1]/div[2]/h3/span/span")).getText();
			log.info("Precio " + pre);
		} catch (Exception e) {
			try {
				pre = driver.findElement(By.className("revip-summary")).getText();
			} catch (Exception e2) {
				log.info("No se pudo obtener precio");
			}
			
		}
		return pre;
	}
	
	private String getDescripcion() {
		String det = "no se pudo obtener la descripcion";
		
		try {
			WebElement desc = driver.findElement(By.className("ad-details-container"));
			det = desc.getText();
		} catch (Exception e) {
			try {
				det = driver.findElement(By.className("revip-details")).getText();
			} catch (NoSuchElementException e2) {
				log.info("no se pudo obtener descripcion");
			}
		}
		
		return det;
	}
	
	private String getTelefono() {
		String tel = "no se pudo obtener telefono";
		try {
			driver.findElement(By.className("show-phone")).click();
			tel = driver.findElement(By.className("real-phone")).findElement(By.tagName("a")).getText();

		} catch (NoSuchElementException e) {
			try {
				driver.findElement(By.className("display-phone")).click();
				uglyWait(5000);
				tel = driver.findElement(By.className("real-phone")).getText();	
			} catch (NoSuchElementException e2) {
				log.info("no se pudo obtener telefono");
			}
			
		}
		
		return tel;
	}
	
	private void uglyWait(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
}
