package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageVivanuncios extends AbstractAnunciosFlow {

	//This link with code is bodegas and jalisco
//	String baseURL = "https://www.vivanuncios.com.mx/s-bodegas/jalisco/v1c33l1013p1";
	String baseURL = "https://www.vivanuncios.com.mx";
	String estado = null;
	
	@Override
	String getURL() {
		return baseURL;
	}

	@Override
	void hacerConsulta(BusquedaTipo tipoBusqueda, String estado) {
		this.estado = estado;
		driver.findElement(By.id("js-browse-item-text")).click();
		driver.findElement(By.id("browse-subcat1")).click();
		WebElement subCategorias = driver.findElement(By.id("js-cat-dropdown"));
		
		switch(tipoBusqueda) {
		case BODEGAS: subCategorias.findElement(By.cssSelector("a[href*='bodegas']")).click();
			break;
		case CASAS: subCategorias.findElement(By.cssSelector("a[href*='renta-inmuebles']")).click(); //falta agregar inmuebles venta
			break;
		case OFICINAS:subCategorias.findElement(By.cssSelector("a[href*='comerciales']")).click();
			break;
		case TERRENOS:subCategorias.findElement(By.cssSelector("a[href*='terrenos']")).click();
			break;
		default: tipoBusqueda = BusquedaTipo.BODEGAS;
			break;
		}
		
		//si no se selecciona localidad toma para todo mexico
		if(estado.equals("mexico") == false) {
			driver.findElement(By.className("location-link")).click();
			driver.findElement(By.id("modal-location")).sendKeys(estado);
			
			WebElement estadosContainer = driver.findElement(By.className("pac-container"));
			List<WebElement> estados = estadosContainer.findElements(By.className("pac-item"));
			for(WebElement estadoSelect : estados) {
				if(estadoSelect.getText().toUpperCase().contains(estado)) {
					estadoSelect.click();
					break;
				}
			}
			
			WebDriverWait wait = new WebDriverWait(driver, 2000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("change-loc")));
			driver.findElement(By.className("change-loc")).click();
		}
		
		//cuando se selecciona la categoria ya hizo la busqueda
//		driver.findElement(By.className("search-button-wrap")).findElement(By.tagName("button")).click();
		
		
	}

	@Override
	void extraerGuardarDatos() {
//List<WebElement> anunciosTitles = driver.findElement(By.id("tileRedesign")).findElements(By.className("tile-panel tileWrapper LandscapeMode"));
		
		List<Anuncio> anuncios = null;
		
		if(estado.equals(("jalisco"))) {
			anuncios = getDatosBusquedaJalisco();
		}else {
			anuncios = getDatosBusqueda();
		}
		
		
		
		System.out.println("Numero de anuncios en total descargados " + anuncios.size());
	}
	
	private List<Anuncio> getDatosBusqueda() {
		List<Anuncio> anuncios = new ArrayList<Anuncio>();
		int countPagina = 1;
		
		do {
			List<WebElement> anunciosTitles = driver.findElement(By.id("tileRedesign")).findElements(By.className("tile-panel"));
			int results = anunciosTitles.size();
			
			System.out.println("Pagina: " + countPagina + " Número de resultados" + results);
			
			for (int idx = results; idx < results; idx++) {

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
		
		WebElement siguiente = null;
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
				System.out.println("clicking " + el.getText());
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
		}while (morePageExistsJalisco());
		
		return anuncios;
	}
	
	
	private boolean morePagesExists() {
		try {
			WebElement pagination = driver.findElement(By.className("pagination"));
			
			
			WebElement deskPagination = driver.findElement(By.className("desktop-pagination"));
			Actions actions = new Actions(driver);
			actions.moveToElement(deskPagination.findElement(By.className("icon-right-arrow"))).click().perform();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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
		
		WebElement desc = driver.findElement(By.className("ad-description"));
		driver.findElement(By.className("show-phone")).click();
		
		a.setTel(driver.findElement(By.className("real-phone")).findElement(By.tagName("a")).getText());
		a.setDescripcion(desc.getText());
		
		return a;
	}

}
