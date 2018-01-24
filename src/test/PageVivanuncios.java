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
		seleccionarEstado(estado);
		
		//Agregar filtros
//		driver.findElement(By.xpath("//*[@id=\"searchChips\"]/div[2]/div/span")).click();
		
		
		//cuando se selecciona la categoria ya hizo la busqueda
//		driver.findElement(By.className("search-button-wrap")).findElement(By.tagName("button")).click();
	}
	
	private void agregarFiltros(BusquedaTipo tipoInmueble) {
		
		String transaccion = config.getProperty("transaccion");
		driver.findElement(By.xpath("//*[@id=\"searchChips\"]/div[2]/div/span")).click();
		WebDriverWait wait2 = new WebDriverWait(driver, 2000);
		wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("filterScreenTakeOver")));
		
		
		if(tipoInmueble == BusquedaTipo.BODEGA) {
			if(transaccion.equalsIgnoreCase("venta")) {
				By by = By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[1]/div/label/div/div");
				retryingFindClick(by);
				driver.findElement(by).click();
			}else {
				By by = By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[2]/div/label/div/div");
				retryingFindClick(by);
				driver.findElement(by).click();
			}
		}else if(tipoInmueble == BusquedaTipo.OFICINA) {
			By dropDownList = By.id("L3Category");
			retryingFindClick(dropDownList);
			WebElement select = driver.findElement(By.id("L3Category"));
			select.click();
			List<WebElement> options = select.findElements(By.tagName("option"));
			for(WebElement opt : options) {
				if(opt.getText().toLowerCase().contains("oficina")) {
					opt.click();
					break;
				}
			}
			
			WebDriverWait wait4 = new WebDriverWait(driver, 2000);
			wait4.until(ExpectedConditions.visibilityOfElementLocated(By.className("filterContainer")));
		}
		
		
		if(transaccion.equalsIgnoreCase("venta")) {
			By by = By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[1]/div/label/div/div");
			WebDriverWait wait = new WebDriverWait(driver, 2000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			
			retryingFindClick(by);
			driver.findElement(by).click();
		}else {
			By by = By.xpath("//*[@id=\"filter-attribute\"]/div[3]/div[2]/div[2]/div/label/div/div");
			WebDriverWait wait = new WebDriverWait(driver, 2000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			retryingFindClick(by);
			driver.findElement(by).click();
		}
		
		String precioMin = config.getProperty("precioMin");
		String precioMax = config.getProperty("precioMax");
		
		if(precioMin != null && precioMax != null) {
			driver.findElement(By.id("attr.Price.amountMin")).sendKeys(precioMin);
			driver.findElement(By.id("attr.Price.amountMax")).sendKeys(precioMax);
		}
		
		
		//Click en aplicar filtros
		driver.findElement(By.xpath("//*[@id=\"searchFilter\"]/div[3]/div/div")).click();
		
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
            } catch(WebDriverException e) {
            }
            attempts++;
        }
        return result;
}
	
	private void seleccionarEstado(String estado) {
		// si no se selecciona localidad toma para todo mexico
		if(estado.isEmpty() || estado.equalsIgnoreCase("nacional") || estado.equalsIgnoreCase("mexico")) {
			estado = "méxico";
		}
		driver.findElement(By.className("location-link")).click();
		driver.findElement(By.id("modal-location")).sendKeys(estado);

		WebElement estadosContainer = driver.findElement(By.className("pac-container"));
		List<WebElement> estados = estadosContainer.findElements(By.className("pac-item"));
		for (WebElement estadoSelect : estados) {
			if (estadoSelect.getText().toLowerCase().contains(estado)) {
				estadoSelect.click();
				break;
			}
		}

		// Click en boton aplicar ubicacion
		WebDriverWait wait = new WebDriverWait(driver, 2000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("change-loc")));
		driver.findElement(By.className("change-loc")).click();
	}

	@Override
	List<Anuncio> extraerGuardarDatos() {
//List<WebElement> anunciosTitles = driver.findElement(By.id("tileRedesign")).findElements(By.className("tile-panel tileWrapper LandscapeMode"));
		
		List<Anuncio> anuncios = null;
		
		if(estado.equals(("jalisco"))) {
			anuncios = getDatosBusquedaJalisco();
		}else {
			anuncios = getDatosBusqueda();
		}
		System.out.println("Numero de anuncios en total descargados " + anuncios.size());
		return anuncios;
	}
	
	private List<Anuncio> getDatosBusqueda() {
		List<Anuncio> anuncios = new ArrayList<Anuncio>();
		int countPagina = 1;
		
		do {
			List<WebElement> anunciosTitles = driver.findElement(By.id("tileRedesign")).findElements(By.className("tile-panel"));
			int results = anunciosTitles.size();
			
			System.out.println("Pagina: " + countPagina + " Número de resultados" + results);
			
			for (int idx = 0; idx < results; idx++) {

				WebElement elementToClick = anunciosTitles.get(idx);
						
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("arguments[0].scrollIntoView()", elementToClick); 
				System.out.println("clicking " + elementToClick.getText());
				elementToClick.click();
				
				Anuncio data = getDataFromLink(driver);

				System.out.println("Descripción: " + idx + " " + data.getDescripcion());
				anuncios.add(data);

				try {
					WebElement regresar = driver.findElement(By.className("header-back"));
					regresar.click();
				} catch (NoSuchElementException e) {
					System.out.println("No se encontro link regresar");
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
			System.out.println("Número de resultados: " + results);
			
			for (int idx = 0; idx < results; idx++) {

//				System.out.println(anunciosTitles.get(idx).getText());
				WebElement el = anunciosTitles.get(idx).findElement(By.tagName("a"));
				JavascriptExecutor jse = (JavascriptExecutor)driver;

				jse.executeScript("arguments[0].scrollIntoView()", el); 
				el.click();
				
				Anuncio data = getDataFromLink(driver);

//				System.out.println("Contacto: " + idx + " " + data.getNombreAutor());
				System.out.println("Descripción: " + idx + " " + data.getDescripcion());
//				System.out.println("Teléfono: " + idx + " " + data.getTel());
				anuncios.add(data);

				try {
					WebElement regresar = driver.findElement(By.className("header-back"));
					regresar.click();
				} catch (NoSuchElementException e) {
					System.out.println("No se encontro link regresar");
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

		WebElement desc = driver.findElement(By.className("ad-description"));
		a.setDescripcion(desc.getText());

		try {
			driver.findElement(By.className("show-phone")).click();
			tel = driver.findElement(By.className("real-phone")).findElement(By.tagName("a")).getText();

		} catch (NoSuchElementException e) {
		}

		a.setTelefono(tel);
		return a;
	}

}
