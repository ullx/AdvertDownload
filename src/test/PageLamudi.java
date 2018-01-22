package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageLamudi extends AbstractAnunciosFlow {

	public PageLamudi(Properties config) {
		super(config);
	}

//	String baseUrl = "https://www.lamudi.com.mx/";
	String baseUrl = "https://www.lamudi.com.mx/for-sale/q:bodega/";
	String estado = null;
	
	@Override
	String getURL() {
		return baseUrl;
	}

	@SuppressWarnings("unused")
	@Override
	void hacerConsulta() {
		if(true) {
			return;	
		}
		
		
		try {
		
		estado = config.getProperty("ubicacion");
		String transaccion = config.getProperty("transaccion").toLowerCase();
		
		WebElement searchTabs = driver.findElement(By.id("search-widget-tabs"));
		
		if(transaccion.contains("compra")  || transaccion.contains("venta")) {
			driver.findElement(By.xpath("//*[@id=\"search-widget-tabs\"]/div[1]/label")).click();
		}else if(transaccion.contains("renta")) {
			driver.findElement(By.xpath("//*[@id=\"search-widget-tabs\"]/div[2]/label/span")).click();
		}
		////*[@id="search-widget-tabs"]/div[1]/label/span
		
		
		if(inmuebleTipo == BusquedaTipo.BODEGA) {
			driver.findElement(By.id("searchbar")).sendKeys("bodega");
		}
		
		
		//Se selecciona el inmueble a buscar
		//Si es bodega se salta este if porque se mete texto en el buscador
		if (inmuebleTipo != BusquedaTipo.BODEGA) {
			driver.findElement(By.id("lnkSelectKit-display")).click();
			WebElement dropDownCategories = driver.findElement(By.className("selectkit-choices"));
			List<WebElement> options = dropDownCategories.findElements(By.className("selectkit-choice"));
			
			
			for (WebElement opt : options) {
				System.out.println(opt.getText());
				if (opt.getText().contains(inmuebleTipo.toString().toLowerCase())) {
					opt.click();
				}
			}
		}else {
			
		}
		
		Thread.sleep(2000);

		//CLick on search
//		driver.findElement(By.id("btnSubmitSearch")).click();
		driver.findElement(By.xpath("//*[@id=\"btnSubmitSearch\"]/span")).click();
		 
		} catch (Exception e) {

		}
		 
		 //TESTING
		 //TESTING
		 throw new RuntimeException();
	}
	
	@Override
	List<Anuncio> extraerGuardarDatos() throws Exception {
		
		List<Anuncio> anuncios = new ArrayList<Anuncio>();
		int countPagina = 1;
		
		do {
			List<WebElement> anunciosTitles = driver.findElement(By.id("pnlPropertiesListPanel")).findElements(By.className("highlight-box"));
			int results = anunciosTitles.size();
			
			System.out.println("Pagina: " + countPagina + " Número de resultados" + results);
			
			for (int idx = results; idx < results; idx++) {

				WebElement anuncioContainer = anunciosTitles.get(idx);
						
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("arguments[0].scrollIntoView()", anuncioContainer); 
				System.out.println("clicking " + anuncioContainer.getText());
				
				
				anuncioContainer.findElement(By.className("listing-info")).findElement(By.tagName("a")).click();
				
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

				anunciosTitles = driver.findElement(By.id("pnlPropertiesListPanel")).findElements(By.className("highlight-box"));
			}
			countPagina++;
		}while (morePagesExists());
		
		
		return anuncios;
	}
	
	private boolean morePagesExists() {
		
		try {
			WebElement pagination = driver.findElement(By.className("Pagination-item--arrow"));
			pagination.click();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private Anuncio getDataFromLink(WebDriver driver) {
		Anuncio a = new Anuncio();
		
		//show more link
		driver.findElement(By.className("ViewMore-trigger-text")).click();;
		a.setDescripcion(driver.findElement(By.className("Description")).getText());
		//Reveal phone number
		driver.findElement(By.partialLinkText("Mostrar el tel")).click();
		driver.findElement(By.id("request_phone_phone_input")).sendKeys("33 33 33 33 33");
		driver.findElement(By.id("js-requestPhoneBtn")).click();
		
		
		WebElement telefono = driver.findElement(By.className("AgentBoxSuccess-agent-phone"));
		
		WebDriverWait wait = new WebDriverWait(driver, 5000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("AgentBoxSuccess-agent-phone")));
		a.setTelefono(telefono.getText());
		
		driver.findElement(By.className("js-viewerContainerRequestPhoneCloseBtn\"")).click();
		
		return a;
	}

}
